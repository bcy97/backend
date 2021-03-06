package com.backend.dao.impl;

import com.backend.dao.HistoryDataDao;
import com.backend.util.CfgData;
import com.backend.util.Constants;
import com.backend.util.SocketConnect;
import com.backend.util.Utils;
import com.backend.vo.AcValue;
import com.backend.vo.AnValue;
import com.backend.vo.StValue;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Repository
public class HistoryDataDaoImpl implements HistoryDataDao {

    static Logger logger = Logger.getLogger("HistoryDataDaoImpl");

    private Utils utils;

    @Autowired
    public HistoryDataDaoImpl(Utils utils) {
        this.utils = utils;
    }

    @Override
    public AnValue[] getAnHistoryData(String begTime, String endTime, Integer[] ids, int count, String companyId) {
        return parseHistoryAnData(getHistoryData(begTime, endTime, ids, Constants.CC_HDT_HOUS_DATA, companyId), count);
    }

    @Override
    public AnValue[] getAn5MinHistoryData(String begTime, String endTime, Integer[] ids, int count, String companyId) {
        return parseHistoryAnData(getHistoryData(begTime, endTime, ids, Constants.CC_HDT_5MIN_DATA, companyId), count);
    }

    @Override
    public StValue[] getStHistoryData(String begTime, String endTime, Integer[] ids, int count, String companyId) {
        return parseHistoryStData(getHistoryData(begTime, endTime, ids, Constants.CC_HDT_HOUS_DATA, companyId), count);
    }

    @Override
    public AcValue[] getAcHistoryData(String begTime, String endTime, Integer[] ids, int count, String companyId) {
        return parseHistoryAcData(getHistoryData(begTime, endTime, ids, Constants.CC_HDT_HOUS_DATA, companyId), count, companyId);
    }

    /***
     * 获取历史数据
     * */
    private ByteBuffer getHistoryData(String begTime, String endTime, Integer[] ids, byte type, String companyId) {
        byte[] datas = new byte[ids.length * 4 + 2 * 8 + 1];
        ByteBuffer bb = ByteBuffer.allocate(datas.length);
        bb.order(ByteOrder.LITTLE_ENDIAN);



        bb.put(type);

        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTime(utils._DATE_FORMAT_.parse(begTime));
        } catch (Exception e) {
            logger.error("开始时间有误;" + begTime);
            return ByteBuffer.allocate(0);
        }
        bb.putLong((long) calendar.getTimeInMillis() / 1000);

        calendar = Calendar.getInstance();
        try {
            calendar.setTime(utils._DATE_FORMAT_.parse(endTime));
        } catch (Exception e) {
            logger.error("结束时间有误;" + endTime);
            return ByteBuffer.allocate(0);
        }
        bb.putLong((long) calendar.getTimeInMillis() / 1000);

        bb.put(utils.idArrToBytes(ids));

        System.arraycopy(bb.array(), 0, datas, 0, datas.length);

        return SocketConnect.getData(datas, Constants.CC_HISDATA, logger,true, companyId);
    }

    private AnValue[] parseHistoryAnData(ByteBuffer bb, int count) {
        List<AnValue> list = new ArrayList<AnValue>();
        int id = Constants.CC_NOTHINGNESS;
        byte valid = Constants.CC_IS_NULL;
        int size = bb.position();
        bb.flip();

        while (size > 0) {
            id = bb.getInt();
            size -= 4;
            if (Constants.CC_NOTHINGNESS == id) {
                for(int i = 0; i < count; i++)
                    list.add(new AnValue((byte) 0, 0));
                continue;
            }

            for (int i = 0; i < count; i++) {
                valid = bb.get();
                size--;
                if (Constants.CC_IS_NULL == valid) {
                    list.add(new AnValue((byte) 0, 0));
                    continue;
                }
                float data = bb.getFloat();
                size -= 4;
                list.add(new AnValue(valid, data));
            }
        }

        return list.toArray(new AnValue[list.size()]);
    }

    private StValue[] parseHistoryStData(ByteBuffer bb, int count) {
        List<StValue> list = new ArrayList<StValue>();
        int id = Constants.CC_NOTHINGNESS;
        byte valid = Constants.CC_IS_NULL;
        int size = bb.position();
        bb.flip();

        while (size > 0) {
            id = bb.getInt();
            size -= 4;
            if (Constants.CC_NOTHINGNESS == id) {
                list.add(new StValue((byte) 0, (byte) 0));
                continue;
            }

            for (int i = 0; i < count; i++) {
                valid = bb.get();
                size--;
                if (Constants.CC_IS_NULL == valid) {
                    list.add(new StValue((byte) 0, (byte) 0));
                    continue;
                }
                byte data = bb.get();
                size--;
                list.add(new StValue(valid, data));
            }
        }

        return list.toArray(new StValue[list.size()]);
    }

    private AcValue[] parseHistoryAcData(ByteBuffer bb, int count, String companyId) {
        List<AcValue> list = new ArrayList<AcValue>();
        int id = Constants.CC_NOTHINGNESS;
        byte valid = Constants.CC_IS_NULL;
        int size = bb.position();
        bb.flip();

        while (size > 0) {
            id = bb.getInt();
            size -= 4;
            if (Constants.CC_NOTHINGNESS == id) {
                list.add(new AcValue(0, 0, (byte) 0));
                continue;
            }

            for (int i = 0; i < count; i++) {
                valid = bb.get();
                size--;
                if (Constants.CC_IS_NULL == valid) {
                    list.add(new AcValue(0, 0, (byte) 0));
                    continue;
                }
                double data = 0;
                if (null != new CfgData().getAcO(id, companyId))
                    data = bb.getLong() * new CfgData().getAcO(id, companyId).getFi();
                else
                    data = bb.getLong();

                size--;
                list.add(new AcValue(data, 0, valid));
            }
        }

        return list.toArray(new AcValue[list.size()]);

    }
}
