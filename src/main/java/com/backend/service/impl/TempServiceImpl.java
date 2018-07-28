package com.backend.service.impl;

import com.backend.util.CfgData;
import com.backend.util.Constants;
import com.backend.util.Utils;
import com.backend.vo.*;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class TempServiceImpl {
    static Logger logger = Logger.getLogger("tempServcie");
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    //// real statistic data & real calculate data begin
    public CalValue[] getRealStatisticValue(String[] names) throws UnsupportedEncodingException {
        ByteBuffer bb = getRealCalculateData(Constants.CC_RDT_STA, names);
        if (null == bb)
            return null;
        try {
            return parseRealCalValue(bb);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    public CalValue[] getRealCalculateValue(String[] names) throws UnsupportedEncodingException {
        ByteBuffer bb = getRealCalculateData(Constants.CC_RDT_CAL, names);
        if (null == bb)
            return null;
        try {
            return parseRealCalValue(bb);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    private ByteBuffer getRealCalculateData(byte type, String[] names) throws UnsupportedEncodingException {
        byte[] datas = null;
        ByteBuffer bb = ByteBuffer.allocate(2048);
        bb.order(ByteOrder.LITTLE_ENDIAN);

        bb.put(type);
        for (String item : names) {
            datas = item.getBytes("UTF-8");
            bb.put((byte) datas.length);
            bb.put(datas);
        }

        datas = new byte[bb.position()];
        System.arraycopy(bb.array(), 0, datas, 0, datas.length);
        DataPacket dp = new DataPacket(Constants.CC_REALCALDATA, datas);

        Socket socket = new Socket();
        try {
            socket.connect(getSocketAddress());
            OutputStream os = socket.getOutputStream();
            InputStream is = socket.getInputStream();

            byte[] bDatas = dp.serialize();
            os.write(bDatas, 0, bDatas.length);

            bb = ByteBuffer.allocate(8 * 1024);
            bb.order(ByteOrder.LITTLE_ENDIAN);

            receiveData(bb, is);
            is.close();
            os.close();

            return bb;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            logger.error(e.getMessage());
            return null;
        }
    }

    private CalValue[] parseRealCalValue(ByteBuffer bb) throws UnsupportedEncodingException {
        byte strLength = 0;
        byte valid = 0;
        String value = "";
        byte[] datas;
        List<CalValue> list = new ArrayList<CalValue>();
        int size = bb.position();
        bb.flip();

        while (size > 0) {
            valid = bb.get();
            size--;

            strLength = bb.get();
            size--;
            datas = new byte[strLength];
            bb.get(datas);
            size -= strLength;

            value = new String(datas, "UTF-8");

            list.add(new CalValue(valid, value));

        }

        return list.toArray(new CalValue[list.size()]);
    }

    ////real statistic data & real calculate data end

    // /// user info begin

    public UserInfo[] getAllUserInfo() {
        ByteBuffer bb = getUserInfo(Constants.CC_USERINFO, new byte[0]);
        if (null == bb)
            return null;
        try {
            return parseUserInfo(bb);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;

    }

    private UserInfo[] parseUserInfo(ByteBuffer bb)
            throws UnsupportedEncodingException {
        String name = "";
        String pwd = "";
        String id = "";
        String depaType = "";
        String depaName = "";
        byte roleCount = 0;
        String[] roleIds = null;
        byte permissionType = 0;
        String permissionName = "";
        long controlType = 0;
        int size = bb.position();
        byte strLength = 0;
        byte[] datas;
        List<UserInfo> list = new ArrayList<UserInfo>();
        bb.flip();

        while (size > 0) {
            strLength = bb.get();
            size--;
            datas = new byte[strLength];
            bb.get(datas);
            size -= strLength;
            id = new String(datas, "UTF-8");

            strLength = bb.get();
            size--;
            datas = new byte[strLength];
            bb.get(datas);
            size -= strLength;
            name = new String(datas, "UTF-8");

            strLength = bb.get();
            size--;
            datas = new byte[strLength];
            bb.get(datas);
            size -= strLength;
            pwd = new String(datas, "UTF-8");

            strLength = bb.get();
            size--;
            datas = new byte[strLength];
            bb.get(datas);
            size -= strLength;
            depaType = new String(datas, "UTF-8");

            strLength = bb.get();
            size--;
            datas = new byte[strLength];
            bb.get(datas);
            size -= strLength;
            depaName = new String(datas, "UTF-8");

            roleCount = bb.get();
            size--;
            roleIds = new String[roleCount];
            for (byte i = 0; i < roleCount; i++) {
                strLength = bb.get();
                size--;
                datas = new byte[strLength];
                bb.get(datas);
                size -= strLength;
                roleIds[i] = new String(datas, "UTF-8");
            }

            permissionType = bb.get();
            size--;

            strLength = bb.get();
            size--;
            datas = new byte[strLength];
            bb.get(datas);
            size -= strLength;
            permissionName = new String(datas, "UTF-8");

            controlType = bb.getLong();

            list.add(new UserInfo(name, pwd, depaName, id, depaType));

        }

        return list.toArray(new UserInfo[list.size()]);
    }

    private ByteBuffer getUserInfo(byte type, byte[] datas) {
        ByteBuffer bb = null;
        DataPacket dp = new DataPacket(Constants.CC_USERINFO, datas);

        Socket socket = new Socket();
        try {
            socket.connect(getSocketAddress());
            OutputStream os = socket.getOutputStream();
            InputStream is = socket.getInputStream();

            byte[] bDatas = dp.serialize();
            os.write(bDatas, 0, bDatas.length);

            bb = ByteBuffer.allocate(8 * 1024);
            bb.order(ByteOrder.LITTLE_ENDIAN);

            receiveData(bb, is);
            is.close();
            os.close();

            return bb;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            logger.error(e.getMessage());
            return null;
        }

    }

    // /// user info end

    // /// static data begin

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
        System.arraycopy(bb.array(), 0, datas, 0, datas.length);
        DataPacket dp = new DataPacket(Constants.CC_EVENTDATA, datas);

        Socket socket = new Socket();
        try {
            socket.connect(getSocketAddress());
            OutputStream os = socket.getOutputStream();
            InputStream is = socket.getInputStream();

            byte[] bDatas = dp.serialize();
            os.write(bDatas, 0, bDatas.length);

            bb = ByteBuffer.allocate(8 * 1024);
            bb.order(ByteOrder.LITTLE_ENDIAN);

            receiveData(bb, is);
            is.close();
            os.close();

            return bb;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            logger.error(e.getMessage());
            return null;
        }
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

    // /// static data end

    // /// event info begin

    public EventInfo[] getAnEpdData(int[] ids, String begTime, String endTime)
            throws ParseException {
        ByteBuffer bb = getEventData(ids, begTime, endTime,
                Constants.CC_EDT_ANEPD);
        if (null == bb)
            return null;
        try {
            return parseEventData(bb);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    public EventInfo[] getStEpdData(int[] ids, String begTime, String endTime)
            throws ParseException {
        ByteBuffer bb = getEventData(ids, begTime, endTime,
                Constants.CC_EDT_STEPD);
        if (null == bb)
            return null;
        try {
            return parseEventData(bb);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    public EventInfo[] getSoeData(int[] ids, String begTime, String endTime)
            throws ParseException {
        ByteBuffer bb = getEventData(ids, begTime, endTime,
                Constants.CC_EDT_SOE);
        if (null == bb)
            return null;
        try {
            return parseEventData(bb);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    private ByteBuffer getEventData(int[] ids, String begTime, String endTime,
                                    byte type) throws ParseException {
        ByteBuffer bb = ByteBuffer.allocate(ids.length * 4 + 25);
        bb.order(ByteOrder.LITTLE_ENDIAN);

        bb.put(type);
        bb.putLong(0l);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(sdf.parse(begTime));
        bb.putLong((long) calendar.getTimeInMillis() / 1000);

        calendar = Calendar.getInstance();
        calendar.setTime(sdf.parse(endTime));
        bb.putLong((long) calendar.getTimeInMillis() / 1000);

        for (int id : ids)
            bb.putInt(id);

        byte[] datas = new byte[bb.position()];
        System.arraycopy(bb.array(), 0, datas, 0, datas.length);
        DataPacket dp = new DataPacket(Constants.CC_EVENTDATA, datas);

        Socket socket = new Socket();
        try {
            socket.connect(getSocketAddress());
            OutputStream os = socket.getOutputStream();
            InputStream is = socket.getInputStream();

            byte[] bDatas = dp.serialize();
            os.write(bDatas, 0, bDatas.length);

            bb = ByteBuffer.allocate(8 * 1024);
            bb.order(ByteOrder.LITTLE_ENDIAN);

            receiveData(bb, is);
            is.close();
            os.close();

            return bb;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            logger.error(e.getMessage());
            return null;
        }

    }

    private EventInfo[] parseEventData(ByteBuffer bb)
            throws UnsupportedEncodingException {
        SimpleDateFormat format = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss.sss");
        List<EventInfo> list = new ArrayList<EventInfo>();
        int id = Constants.CC_NOTHINGNESS;
        int size = bb.position();
        int miSec = 0;
        long time = 0l;
        byte length = 0;
        byte[] bData = new byte[0];
        String info = "";
        Calendar cal = Calendar.getInstance();
        bb.flip();

        while (size > 0) {
            id = bb.getInt();
            size -= 4;

            miSec = bb.getInt();
            size -= 4;

            time = bb.getLong();
            size -= 8;
            cal.setTimeInMillis((long) time * 1000);
            cal.set(Calendar.SECOND, miSec / 1000);
            cal.set(Calendar.MILLISECOND, miSec % 1000);

            length = bb.get();
            size--;

            if (length > 0) {
                bData = new byte[length];
                bb.get(bData);
                size -= length;
                info = new String(bData, "UTF-8");
            }

            length = bb.get();
            size--;
            if (length > 0) {
                bData = new byte[length];
                bb.get(bData);
                size -= length;
            }
            EventInfo ei = new EventInfo(id, format.format(cal.getTime()), info);
            ei.setEventLogLength(length);
            if (length > 0)
                ei.setEventLog(bData);
            list.add(ei);

        }

        return list.toArray(new EventInfo[list.size()]);

    }

    // /// event info end

    // /// history data begin

    /***
     * begTime/endTime format yyyy-MM-dd HH:mm:ss
     * */
    public AnValue[] getHistoryAnHourData(int[] ids, String begTime,
                                          String endTime) throws ParseException {
        ByteBuffer bb = getHisData(ids, begTime, endTime,
                Constants.CC_HDT_HOUS_DATA);
        if (null == bb)
            return null;
        return parseHistoryAnData(bb);
    }

    public AnValue[] getHistoryAn5MinData(int[] ids, String begTime,
                                          String endTime) throws ParseException {
        ByteBuffer bb = getHisData(ids, begTime, endTime,
                Constants.CC_HDT_5MIN_DATA);
        if (null == bb)
            return null;
        return parseHistoryAnData(bb);
    }

    public StValue[] getHistoryStHourData(int[] ids, String begTime,
                                          String endTime) throws ParseException {
        ByteBuffer bb = getHisData(ids, begTime, endTime, Constants.CC_HDT_ALL);
        if (null == bb)
            return null;
        return parseHistoryStData(bb);
    }

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

        System.arraycopy(bb.array(), 0, datas, 0, datas.length);
        DataPacket dp = new DataPacket(Constants.CC_HISDATA, datas);

        Socket socket = new Socket();
        try {
            socket.connect(getSocketAddress());
            OutputStream os = socket.getOutputStream();
            InputStream is = socket.getInputStream();

            byte[] bDatas = dp.serialize();
            os.write(bDatas, 0, bDatas.length);

            bb = ByteBuffer.allocate(8 * 1024);
            bb.order(ByteOrder.LITTLE_ENDIAN);

            receiveData(bb, is);
            is.close();
            os.close();

            return bb;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            logger.error(e.getMessage());
            return null;
        }
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

    // /// history data end

    // /// real data begin

    public Object[] getRealData(int[] ids) {
        byte[] datas = Utils.idArrToBytes(ids);
        DataPacket dp = new DataPacket(Constants.CC_REALDATA, datas);
        ByteBuffer bb = null;

        Socket socket = new Socket();
        try {
            socket.connect(getSocketAddress());
            OutputStream os = socket.getOutputStream();
            InputStream is = socket.getInputStream();

            byte[] bDatas = dp.serialize();
            os.write(bDatas, 0, bDatas.length);

            bb = ByteBuffer.allocate(8 * 1024);
            bb.order(ByteOrder.LITTLE_ENDIAN);

            receiveData(bb, is);
            is.close();
            os.close();

            return parseRealData(bb);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            logger.error(e.getMessage());
            return null;
        }
    }

    private Object[] parseRealData(ByteBuffer bb) {
        List<Object> list = new ArrayList<Object>();
        int size = bb.position();
        bb.flip();

        int id = -1;
        byte valid = -1;
        while (true) {
            if (size - bb.position() < 4)
                break;
            id = bb.getInt();
            if (Constants.CC_NOTHINGNESS == id) {
                list.add(-1);
                continue;
            }
            valid = bb.get();
            if (Constants.CC_IS_NULL == valid) {
                list.add(getNullData(id));
                continue;
            }
            list.add(getData(bb, id, valid));
        }
        return list.toArray();
    }

    private Object getData(ByteBuffer bb, int id, byte valid) {
        try {
            switch (Utils.getTypeInId(id)) {
                case Constants.IDACC:
                    AcValue acV = new AcValue();
                    if (null != new CfgData().getAcO(id)) {
                        acV.setValue(bb.getLong()
                                * new CfgData().getAcO(id).getFi());
                        acV.sethValue(bb.getInt()
                                * new CfgData().getAcO(id).getFi());
                    } else {
                        acV.setValue(bb.getLong());
                        acV.sethValue(bb.getInt());
                    }
                    acV.setValid(valid);
                    return acV;
                case Constants.IDAN:
                    AnValue anV = new AnValue();
                    anV.setValue(bb.getFloat());
                    anV.setValid(valid);
                    return anV;
                case Constants.IDST:
                    StValue stV = new StValue();
                    stV.setValid(valid);
                    stV.setValue(bb.get());
                    return stV;
                default:
                    return -1;
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            return -1;
        }
    }

    private SocketAddress getSocketAddress() {
        String ip = "127.0.0.1";
        int port = 8888;

        return new InetSocketAddress(ip, port);
    }

    private Object getNullData(int id) {
        switch (Utils.getTypeInId(id)) {
            case Constants.IDACC:
                return new AcValue();
            case Constants.IDAN:
                return new AnValue();
            case Constants.IDST:
                return new StValue();
            default:
                return 0;
        }
    }

    private void receiveData(ByteBuffer bb, InputStream is) throws IOException {
        DataPacket dp;
        byte[] bDatas;
        while (true) {
            byte[] bHead = new byte[12];
            is.read(bHead, 0, 12);
            dp = new DataPacket();
            dp.toDataPacketHead(bHead);

            bDatas = new byte[dp.getLength()];
            is.read(bDatas, 0, bDatas.length);
            bb.put(bDatas);

            is.read(bDatas, 0, 1);

            if (0 == dp.getTailFlag())
                break;
        }
    }
}

