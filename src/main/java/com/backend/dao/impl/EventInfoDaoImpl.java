package com.backend.dao.impl;

import com.backend.dao.EventInfoDao;
import com.backend.util.CfgData;
import com.backend.util.Constants;
import com.backend.util.SocketConnect;
import com.backend.vo.DataPacket;
import com.backend.vo.EventInfo;
import com.backend.util.Utils;
import org.apache.log4j.Logger;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class EventInfoDaoImpl implements EventInfoDao {

    static Logger logger = Logger.getLogger("EventInfoDaoImpl");

    /***
     * 获取事件信息
     * @param ids 要查询的点的id
     * @param begTime 开始时间
     * @param endTime 结束时间
     * @param type 事件类别,0x10->EPD(AnEPD And StEPD) 0x11->AnEPD 0x12->StEPD 0x13->ProtectEPD 0x20->SOE
     * */
    public EventInfo[] getEventInfoByTimeAndId(Integer[] ids,String begTime,String endTime,byte type){
        return parseEventInfo(getEventInfo(ids,begTime,endTime,type));
    }

    /***
     * 最近3个月内最新的20条事件信息
     * */
    public EventInfo[] getLatestEventInfo(){
        Calendar begTime = Calendar.getInstance();
        begTime.add(Calendar.MONTH,-3);

        Calendar endTime = Calendar.getInstance();

        CfgData cfgData = new CfgData();

        EventInfo[] infos = parseEventInfo(getEventInfo(cfgData.getAllStId(),Utils._DATE_FORMAT_.format(begTime.getTime()),Utils._DATE_FORMAT_.format(endTime.getTime()),Constants.CC_EDT_EPD));

        if(infos.length <= 20)
            return infos;

        EventInfo[] rtnData = new EventInfo[20];
        for(int i = 0; i < 20; i++)
            infos[i] = infos[infos.length - 1 - i];

        return rtnData;
    }

    /***
     * 从服务器获取事件信息
     * */
    private ByteBuffer getEventInfo(Integer[] ids,String begTime,String endTime,byte type)  {
        ByteBuffer bb = ByteBuffer.allocate(ids.length * 4 + 25);
        bb.order(ByteOrder.LITTLE_ENDIAN);

        bb.put(type);
        bb.putLong(0l);

        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTime(Utils._DATE_FORMAT_.parse(begTime));
        }catch(Exception e){
            bb = ByteBuffer.allocate(0);
            logger.error("开始时间有误;" + begTime);
            return bb;
        }
        bb.putLong((long) calendar.getTimeInMillis() / 1000);

        calendar = Calendar.getInstance();
        try {
            calendar.setTime(Utils._DATE_FORMAT_.parse(endTime));
        }catch(Exception e){
            bb = ByteBuffer.allocate(0);
            logger.error("结束时间有误;" + begTime);
            return bb;
        }
        bb.putLong((long) calendar.getTimeInMillis() / 1000);

        for (int id : ids)
            bb.putInt(id);

        byte[] datas = new byte[bb.position()];
        System.arraycopy(bb.array(), 0, datas, 0, datas.length);
        byte cmd = Constants.CC_EVENTDATA;

        SocketConnect.getData(datas,cmd,logger );

        return bb;
    }

    private EventInfo[] parseEventInfo(ByteBuffer bb) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.sss");
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
                try {
                    info = new String(bData, "UTF-8");
                }catch(Exception e){
                    info = "";
                }
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
