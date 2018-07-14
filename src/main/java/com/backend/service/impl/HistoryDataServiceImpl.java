package com.backend.service.impl;

import com.backend.service.HistoryDataService;
import com.backend.util.CfgData;
import com.backend.util.Constants;
import com.backend.util.SocketConnect;
import com.backend.vo.AcValue;
import com.backend.vo.AnValue;
import com.backend.vo.DataPacket;
import com.backend.vo.StValue;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Service
public class HistoryDataServiceImpl implements HistoryDataService {

    static Logger logger = Logger.getLogger("CommService");
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /***
     * begTime/endTime format yyyy-MM-dd HH:mm:ss
     * */
    @Override
    public AnValue[] getHistoryAnHourData(int[] ids, String begTime,
                                          String endTime) throws ParseException {
        ByteBuffer bb = getHisData(ids, begTime, endTime,
                Constants.CC_HDT_HOUS_DATA);
        if (null == bb)
            return null;
        return parseHistoryAnData(bb);
    }

    @Override
    public AnValue[] getHistoryAn5MinData(int[] ids, String begTime,
                                          String endTime) throws ParseException {
        ByteBuffer bb = getHisData(ids, begTime, endTime,
                Constants.CC_HDT_5MIN_DATA);
        if (null == bb)
            return null;
        return parseHistoryAnData(bb);
    }

    @Override
    public StValue[] getHistoryStHourData(int[] ids, String begTime,
                                          String endTime) throws ParseException {
        ByteBuffer bb = getHisData(ids, begTime, endTime, Constants.CC_HDT_ALL);
        if (null == bb)
            return null;
        return parseHistoryStData(bb);
    }

    @Override
    public AcValue[] getHistoryAcHourData(int[] ids, String begTime,
                                          String endTime) throws ParseException {
        ByteBuffer bb = getHisData(ids, begTime, endTime, Constants.CC_HDT_ALL);
        if (null == bb)
            return null;
        return parseHistoryAcData(bb);
    }

    private ByteBuffer getHisData(int[] ids, String begTime, String endTime,
                                  byte type) throws ParseException {
        byte[] datas = new byte[ids.length * 4 + 2 * 8 + 1];
        ByteBuffer bb = ByteBuffer.allocate(datas.length);
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

        DataPacket dp = new DataPacket(Constants.CC_HISDATA, datas);
        SocketConnect.getData(bb, dp, datas, logger);

        return bb;
    }

    private AnValue[] parseHistoryAnData(ByteBuffer bb) {
        List<AnValue> list = new ArrayList<AnValue>();
        int id = Constants.CC_NOTHINGNESS;
        byte valid = Constants.CC_IS_NULL;
        int size = bb.position();
        bb.flip();

        while (size > 0) {
            id = bb.getInt();
            size -= 4;
            if (Constants.CC_NOTHINGNESS == id) {
                list.add(new AnValue((byte) 0, 0));
                continue;
            }

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

        return list.toArray(new AnValue[list.size()]);
    }

    private StValue[] parseHistoryStData(ByteBuffer bb) {
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

        return list.toArray(new StValue[list.size()]);
    }

    private AcValue[] parseHistoryAcData(ByteBuffer bb) {
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

            valid = bb.get();
            size--;
            if (Constants.CC_IS_NULL == valid) {
                list.add(new AcValue(0, 0, (byte) 0));
                continue;
            }
            double data = 0;
            if (null != new CfgData().getAcO(id))
                data = bb.getLong() * new CfgData().getAcO(id).getFi();
            else
                data = bb.getLong();

            size--;
            list.add(new AcValue(data, 0, valid));
        }

        return list.toArray(new AcValue[list.size()]);

    }


}

