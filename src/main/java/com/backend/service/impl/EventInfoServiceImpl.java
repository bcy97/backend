package com.backend.service.impl;

import com.backend.service.EventInfoService;
import com.backend.util.Constants;
import com.backend.util.SocketConnect;
import com.backend.vo.DataPacket;
import com.backend.vo.EventInfo;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Service
public class EventInfoServiceImpl implements EventInfoService {

    static Logger logger = Logger.getLogger("EventInfoServiceImpl");
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

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
        DataPacket dp = new DataPacket(Constants.CC_EVENTDATA, datas);

        bb = SocketConnect.getData(dp, logger);

        return bb;

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
}
