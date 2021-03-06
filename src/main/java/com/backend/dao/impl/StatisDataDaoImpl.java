package com.backend.dao.impl;

import com.backend.dao.StatisDataDao;
import com.backend.util.Constants;
import com.backend.util.SocketConnect;
import com.backend.util.Utils;
import com.backend.vo.AcStatisData;
import com.backend.vo.AcValue;
import com.backend.vo.AnStatisData;
import com.backend.vo.AnValue;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Repository
public class StatisDataDaoImpl implements StatisDataDao {

    static Logger logger = Logger.getLogger("StatisDataDaoImpl");

    private Utils utils;

    @Autowired
    public StatisDataDaoImpl(Utils utils) {
        this.utils = utils;
    }

    /***
     * 获取电度统计数据
     * */
    @Override
    public AcStatisData[] getAcStatisData(Integer[] ids, String begTime, String endTime, String companyId) {

        return parseAcStatisData(getStatisData(ids, begTime, endTime, Constants.CC_SDT_AC, companyId));
    }

    /***
     * 获取遥测统计数据
     * */
    @Override
    public AnStatisData[] getAnStaticData(Integer[] ids, String begTime, String endTime, String companyId) {
        return parseAnStatisData(getStatisData(ids, begTime, endTime, Constants.CC_SDT_AN, companyId));
    }

    /***
     * 获取统计数据
     * */
    private ByteBuffer getStatisData(Integer[] ids, String begTime, String endTime, byte type, String companyId) {
        ByteBuffer bb = ByteBuffer.allocate(ids.length * 4 + 17);
        bb.order(ByteOrder.LITTLE_ENDIAN);

        bb.put(type);

        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTime(utils._DATE_FORMAT_.parse(begTime));
        } catch (Exception e) {
            logger.error("开始时间有误!" + begTime);
            return ByteBuffer.allocate(0);
        }
        bb.putLong((long) calendar.getTimeInMillis() / 1000);

        calendar = Calendar.getInstance();
        try {
            calendar.setTime(utils._DATE_FORMAT_.parse(endTime));
        } catch (Exception e) {
            logger.error("结束时间有误!" + endTime);
            return ByteBuffer.allocate(0);
        }
        bb.putLong((long) calendar.getTimeInMillis() / 1000);

        for (int id : ids)
            bb.putInt(id);

        byte[] datas = new byte[bb.position()];
        System.arraycopy(bb.array(), 0, datas, 0, datas.length);

        return SocketConnect.getData(datas, Constants.CC_STATISDATA, logger,true, companyId);
    }

    /***
     * 解析电度统计数据
     * */
    private AcStatisData[] parseAcStatisData(ByteBuffer bb) {
        if(null == bb || bb.position() == 0)
            return new AcStatisData[0];
        List<AcStatisData> list = new ArrayList<>();
        int id = Constants.CC_NOTHINGNESS;
        int size = bb.position();
        byte valid = Constants.CC_IS_NULL;
        AcValue begValue = null;
        AcValue endValue = null;
        AcValue accValue = null;
        bb.flip();

        while (size > 0) {
            id = bb.getInt();
            size -= 4;

            if (Constants.CC_NOTHINGNESS == id) {
                list.add(new AcStatisData());
                continue;
            }
            valid = bb.get();
            size--;
            if (Constants.CC_IS_NULL == valid) {
                begValue = new AcValue(0, 0, (byte) 0);
            }else {
                begValue = new AcValue(bb.getDouble(), 0, valid);
                size -= 8;
            }

            valid = bb.get();
            size--;
            if (Constants.CC_IS_NULL == valid) {
                endValue = new AcValue(0, 0, (byte) 0);
            }else {
                endValue = new AcValue(bb.getDouble(), 0, valid);
                size -= 8;
            }

            valid = bb.get();
            size--;
            if (Constants.CC_IS_NULL == valid) {
                accValue = new AcValue(0, 0, (byte) 0);
            }else {
                accValue = new AcValue(bb.getDouble(), 0, valid);
                size -= 8;
            }

            list.add(new AcStatisData(id, begValue, endValue, accValue));

        }

        return list.toArray(new AcStatisData[list.size()]);

    }

    /***
     * 解析遥测统计数据
     * */
    private AnStatisData[] parseAnStatisData(ByteBuffer bb) {
        List<AnStatisData> list = new ArrayList<AnStatisData>();
        int id = Constants.CC_NOTHINGNESS;
        int size = bb.position();
        byte valid = Constants.CC_IS_NULL;
        AnValue maxValue = null;
        AnValue minValue = null;
        String maxTime = null;
        String minTime = null;
        AnValue avgValue = null;
        int count = 0;
        float accValue = 0;
        long time = 0l;
        Calendar cal = Calendar.getInstance();
        bb.flip();

        while (size > 0) {
            id = bb.getInt();
            size -= 4;

            if (Constants.CC_NOTHINGNESS == id) {
                list.add(new AnStatisData());
                continue;
            }

            valid = bb.get();
            size--;
            if (Constants.CC_IS_NULL != valid) {
                maxValue = new AnValue(valid, bb.getFloat());
                size -= 4;
                time = bb.getLong();
                size -= 8;
                cal.setTimeInMillis((long) time * 1000);
            } else {
                maxValue = new AnValue((byte) 0, 0);
            }

            valid = bb.get();
            size--;
            if (Constants.CC_IS_NULL != valid) {
                minValue = new AnValue(valid, bb.getFloat());
                size -= 4;
                time = bb.getLong();
                size -= 8;
                cal.setTimeInMillis((long) time * 1000);
            } else {
                minValue = new AnValue((byte) 0, 0);
            }

            valid = bb.get();
            size--;
            if (Constants.CC_IS_NULL != valid) {
                avgValue = new AnValue(valid, bb.getFloat());
                size -= 4;
            } else {
                avgValue = new AnValue((byte) 0, 0);
            }

            accValue = bb.getFloat();
            size -= 4;

            count = bb.getInt();
            size -= 4;
            list.add(new AnStatisData(id, maxValue, maxTime, minValue, minTime,
                    avgValue, count, accValue));
        }
        return list.toArray(new AnStatisData[list.size()]);
    }
}
