package com.backend.dao.impl;

import com.backend.dao.EventInfoDao;
import com.backend.util.*;
import com.backend.vo.AnO;
import com.backend.vo.EventInfo;
import com.backend.vo.EventLog;
import com.backend.vo.StO;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Repository
public class EventInfoDaoImpl implements EventInfoDao {

    static Logger logger = Logger.getLogger("EventInfoDaoImpl");

    private Utils utils;
    private CfgData cfgData;
    private Evfault evfault;

    @Autowired
    public EventInfoDaoImpl(Utils utils,CfgData cfgData,Evfault evfault) {
        this.utils = utils;
        this.cfgData = cfgData;
        this.evfault = evfault;
    }

    /***
     * 获取事件信息
     * @param ids 要查询的点的id
     * @param begTime 开始时间
     * @param endTime 结束时间
     * @param type 事件类别,0x10->EPD(AnEPD And StEPD) 0x11->AnEPD 0x12->StEPD 0x13->ProtectEPD 0x20->SOE
     * */
    public EventInfo[] getEventInfoByTimeAndId(Integer[] ids, String begTime, String endTime, byte type, String companyId) {
        return parseEventInfo(getEventInfo(ids, begTime, endTime, type, companyId));
    }

    /***
     * 最近3个月内最新的20条事件信息
     * */
    public EventInfo[] getLatestEventInfo(String companyId) {
        Calendar begTime = Calendar.getInstance();
        begTime.add(Calendar.MONTH, -3);

        Calendar endTime = Calendar.getInstance();

        CfgData cfgData = new CfgData();



        EventInfo[] infos = parseEventInfo(getEventInfo(cfgData.getAllStId(), utils._DATE_FORMAT_.format(begTime.getTime()), utils._DATE_FORMAT_.format(endTime.getTime()), Constants.CC_EDT_EPD, companyId));

        if (infos.length <= 20)
            return infos;

        EventInfo[] rtnData = new EventInfo[20];
        for (int i = 0; i < 20; i++)
            infos[i] = infos[infos.length - 1 - i];

        return rtnData;
    }

    /***
     * 从服务器获取事件信息
     * */
    private ByteBuffer getEventInfo(Integer[] ids, String begTime, String endTime, byte type, String companyId) {
        ByteBuffer bb = ByteBuffer.allocate(ids.length * 4 + 25);
        bb.order(ByteOrder.LITTLE_ENDIAN);

        bb.put(type);
        bb.putLong(0l);

        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTime(utils._DATE_FORMAT_.parse(begTime));
        } catch (Exception e) {
            bb = ByteBuffer.allocate(0);
            logger.error("开始时间有误;" + begTime);
            return bb;
        }
        bb.putLong((long) calendar.getTimeInMillis() / 1000);

        calendar = Calendar.getInstance();
        try {
            calendar.setTime(utils._DATE_FORMAT_.parse(endTime));
        } catch (Exception e) {
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

        return SocketConnect.getData(datas, cmd, logger,true, companyId);
    }

    /****
     * 解析事件信息
     * */
    private EventInfo[] parseEventInfo(ByteBuffer bb) {
        if(null == bb || 0 == bb.position() )
            return new EventInfo[0];
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        List<EventInfo> list = new ArrayList<EventInfo>();
        int id = Constants.CC_NOTHINGNESS;
        int size = bb.position();
        int miSec = 0;
        long time = 0l;
        int length = 0;
        byte[] bData = new byte[0];
        String info = "";
        Calendar cal = Calendar.getInstance();
        bb.flip();

        try {
            while (size > 0) {
                id = bb.getInt();
                size -= 4;

                miSec = bb.getInt();
                size -= 4;

                time = bb.getLong();
                size -= 8;
                cal.setTimeInMillis((long) time * 1000);
             //   cal.set(Calendar.SECOND, miSec / 100);
             //   cal.set(Calendar.MILLISECOND, miSec % 100);

                length = bb.get() & 0xff;// byte是有符号的，将length转为无符号整数
                size--;
                if (length > 0) {
                    bData = new byte[length];
                    bb.get(bData);
                    size -= length;
                    try {
                        info = new String(bData, "UTF-8");
                    } catch (Exception e) {
                        info = "";
                    }
                }

                length = bb.get() & 0xff;// byte是有符号的，将length转为无符号整数
                size--;
                if (length > 0) {
                    bData = new byte[length];
                    bb.get(bData);
                    size -= length;
                }

             //   System.out.println(format.format(cal.getTime()));
                EventInfo ei = new EventInfo(id, format.format(cal.getTime()), info);

                if (length > 0)
                    ei.setEventLogs(toEventLogList(length,bData,id));
                list.add(ei);
            }
        }catch(Exception e){
         //   e.printStackTrace();
            System.out.println("解析事件信息出错!" + e.getMessage());
            logger.error("解析事件信息出错!" + e.getMessage());
        }

        return list.toArray(new EventInfo[list.size()]);
    }

    /***
     * 把事件报文转为事件信息
     * */
    public List<EventLog> toEventLogList(int eventLogLength,byte[] eventLog,int id) {
        List<EventLog> elList = new ArrayList<EventLog>();
        try {
            int position = 8;
            if (eventLogLength < position + 1)
                return null;
            byte anNum = eventLog[position];
            Float[] anData = new Float[32];
            ByteBuffer bb = null;
            byte[] bData = new byte[4];
            byte stNum = 0;
            int stData = 0;

            position++;
            for (int i = 0; i < 32; i++) {
                System.arraycopy(eventLog, position, bData, 0, bData.length);
                position += 4;

                bb = ByteBuffer.wrap(bData, 0, bData.length);
                bb.order(ByteOrder.LITTLE_ENDIAN);

                anData[i] = bb.getFloat(0);
            }

            stNum = eventLog[position];
            position++;

            System.arraycopy(eventLog, position, bData, 0, bData.length);
            position += 4;

            bb = ByteBuffer.wrap(bData);
            bb.order(ByteOrder.LITTLE_ENDIAN);
            stData = bb.getInt();

            StO sto = cfgData.getStO(id);

            List<AnO> anList = null;
            if (null != sto)
                anList = evfault.getAnTemp(sto.getSname());
            for (int i = 0; i < anNum; i++) {
                int point = -1;
                String name = "an" + i;
                float value = anData[i];
                if (anList != null && anList.size() > i) {
                    AnO ano = anList.get(i);
                    name = ano.getCname();
                    point = ano.getPoinum();
                    value = anData[i] * ano.getFi();
                }
                EventLog el = new EventLog();
                el.setName(name);
                el.setType("遥测");
                if (-1 == point)
                    el.setData(Float.toString(value));
                else
                    el.setData(String.format("%." + point + "f", value));
                elList.add(el);
            }

            List<StO> stList = null;
            if (null != sto)
                stList = evfault.getStTemp(sto.getSname());
            for (int i = 0; i < stNum; i++) {
                String name = "st" + i;
                if (null != stList && stList.size() > i) {
                    StO st = stList.get(i);
                    name = st.getCname();
                }
                int temp = 1 << i;
                temp &= stData;
                temp = temp >> i;
                temp = temp & 1;

                EventLog el = new EventLog();
                el.setName(name);
                el.setType("遥信");
                el.setData(Integer.toString(temp));

                elList.add(el);
            }
        }catch (Exception e){
            return null;
        }
        return elList;
    }

}
