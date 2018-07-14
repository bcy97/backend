package com.backend.service.impl;

import com.backend.service.StaticDataService;
import com.backend.util.Constants;
import com.backend.util.SocketConnect;
import com.backend.vo.*;
import org.apache.log4j.Logger;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class StaticDataServiceImpl implements StaticDataService {

    static Logger logger = Logger.getLogger("CommService");
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    public AnStatisData[] getAnStatisData(int[] ids, String begTime,
                                          String endTime) throws ParseException {
        ByteBuffer bb = getStatisData(ids, begTime, endTime,
                Constants.CC_SDT_AN);
        if (null == bb)
            return null;
        try {
            return parseAnStatisData(bb);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public StStatisData[] getStStatisData(int[] ids, String begTime,
                                          String endTime) throws ParseException {
        ByteBuffer bb = getStatisData(ids, begTime, endTime,
                Constants.CC_SDT_ST);
        if (null == bb)
            return null;
        try {
            return parseStStatisData(bb);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public AcStatisData[] getAcStatisData(int[] ids, String begTime,
                                          String endTime) throws ParseException {
        ByteBuffer bb = getStatisData(ids, begTime, endTime,
                Constants.CC_SDT_AC);
        if (null == bb)
            return null;
        try {
            return parseAcStatisData(bb);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    private ByteBuffer getStatisData(int[] ids, String begTime, String endTime,
                                     byte type) throws ParseException {
        ByteBuffer bb = ByteBuffer.allocate(ids.length * 4 + 17);
        bb.order(ByteOrder.LITTLE_ENDIAN);

        bb.put(type);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(sdf.parse(begTime));
        bb.putLong((long) calendar.getTimeInMillis() / 1000);

        calendar = Calendar.getInstance();
        calendar.setTime(sdf.parse(endTime));
        bb.putLong((long) calendar.getTimeInMillis() / 1000);

        for (int id : ids)
            bb.putInt(id);

        byte[] datas = new byte[bb.position()];
        DataPacket dp = new DataPacket(Constants.CC_EVENTDATA, datas);

        SocketConnect.getData(bb, dp, datas, logger);

        return bb;
    }

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

    private StStatisData[] parseStStatisData(ByteBuffer bb) {
        List<StStatisData> list = new ArrayList<StStatisData>();
        int id = Constants.CC_NOTHINGNESS;
        int size = bb.position();
        bb.flip();

        while (size > 0) {
            id = bb.getInt();
            size -= 4;

            if (Constants.CC_NOTHINGNESS == id) {
                list.add(new StStatisData());
                continue;
            } else {
                list.add(new StStatisData(id, bb.getInt()));
                size -= 4;
            }
        }

        return list.toArray(new StStatisData[list.size()]);
    }

    private AcStatisData[] parseAcStatisData(ByteBuffer bb) {
        List<AcStatisData> list = new ArrayList<AcStatisData>();
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
            if (Constants.CC_IS_NULL == valid)
                begValue = new AcValue(0, 0, (byte) 0);
            begValue = new AcValue(bb.getDouble(), 0, valid);
            size -= 4;

            valid = bb.get();
            size--;
            if (Constants.CC_IS_NULL == valid)
                endValue = new AcValue(0, 0, (byte) 0);
            endValue = new AcValue(bb.getDouble(), 0, valid);
            size -= 4;

            valid = bb.get();
            size--;
            if (Constants.CC_IS_NULL == valid)
                accValue = new AcValue(0, 0, (byte) 0);
            accValue = new AcValue(bb.getDouble(), 0, valid);
            size -= 4;

            list.add(new AcStatisData(id, begValue, endValue, accValue));

        }

        return list.toArray(new AcStatisData[list.size()]);
    }

}
