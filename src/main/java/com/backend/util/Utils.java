package com.backend.util;

import com.backend.vo.AcO;
import com.backend.vo.AnO;
import com.backend.vo.StO;
import com.backend.vo.UnitInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Component
public class Utils {

    public static SimpleDateFormat _DATE_FORMAT_ = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Autowired
    private CfgData cfgData;


    public int getId(byte type, short unitNo, short ptNo) {

        int id = type << 24 | (unitNo & 0xFF) << 16 | (ptNo & 0xFFFF);

        return id;
    }

    public byte getTypeInId(int id) {

        byte type = (byte) (id >> 24);

        return type;
    }

    public short getUnitNoInId(int id) {

        short unitNo = (byte) (id >> 16);

        return unitNo;
    }

    public static short getPtNoInId(int id) {

        short ptNo = (short) id;

        return ptNo;
    }
/*
    public byte[] idArrToBytes(int[] idArr) {
        byte[] idByteArr = new byte[idArr.length * 4];

        ByteBuffer bb = ByteBuffer.allocate(idByteArr.length);
        bb.order(ByteOrder.LITTLE_ENDIAN);
        for (int id : idArr)
            bb.putInt(id);

        System.arraycopy(bb.array(), 0, idByteArr, 0, bb.position());

        return idByteArr;
    }
*/
    public byte[] idArrToBytes(Integer[] idArr) {
        byte[] idByteArr = new byte[idArr.length * 4];

        ByteBuffer bb = ByteBuffer.allocate(idByteArr.length);
        bb.order(ByteOrder.LITTLE_ENDIAN);
        for (int id : idArr)
            bb.putInt(id);

        System.arraycopy(bb.array(), 0, idByteArr, 0, bb.position());

        return idByteArr;
    }

    public Integer[] anPtNamesToIds(String[] ptNames, String companyId) {
        Integer[] ids = new Integer[ptNames.length];
        for (int i = 0; i < ptNames.length; i++)
            ids[i] = cfgData.getAnID(ptNames[i], companyId);
        return ids;
    }

    public Integer[] acPtNamesToIds(String[] ptNames, String companyId) {
        Integer[] ids = new Integer[ptNames.length];
        for (int i = 0; i < ptNames.length; i++)
            ids[i] = cfgData.getAcID(ptNames[i], companyId);
        return ids;
    }

    public Integer[] stPtNamesToIds(String[] ptNames, String companyId) {
        Integer[] ids = new Integer[ptNames.length];
        for (int i = 0; i < ptNames.length; i++)
            ids[i] = cfgData.getStID(ptNames[i], companyId);
        return ids;
    }

    /***
     * 通过单元名获取该单元所有的遥测id
     * */
    public Integer[] getAnIdsByUnitName(String unitName, String companyId) {
        Integer[] ids = new Integer[0];


        List<UnitInfo> unitList = cfgData.getAllUnitInfo(companyId);
        for (UnitInfo ui : unitList) {
            if (ui.getName().equals(unitName)) {
                List<AnO> anoList = cfgData.getAnOByUnitNo(ui.getUnitNo(), companyId);
                ids = new Integer[anoList.size()];
                for (int i = 0; i < ids.length; i++)
                    ids[i] = anoList.get(i).getId();
                break;
            }
        }
        return ids;
    }

    /***
     * 通过单元名获取该单元所有的遥信id
     * */
    public Integer[] getStIdsByUnitName(String unitName, String companyId) {
        Integer[] ids = new Integer[0];


        List<UnitInfo> unitList = cfgData.getAllUnitInfo(companyId);
        for (UnitInfo ui : unitList) {
            if (ui.getName().equals(unitName)) {
                List<StO> stoList = cfgData.getStOByUnitNo(ui.getUnitNo(), companyId);
                ids = new Integer[stoList.size()];
                for (int i = 0; i < ids.length; i++)
                    ids[i] = stoList.get(i).getId();
                break;
            }
        }
        return ids;
    }

    /***
     * 通过单元名获取该单元所有的电度id
     * */
    public Integer[] getAcIdsByUnitName(String unitName, String companyId) {
        Integer[] ids = new Integer[0];


        List<UnitInfo> unitList = cfgData.getAllUnitInfo(companyId);
        for (UnitInfo ui : unitList) {
            if (ui.getName().equals(unitName)) {
                List<AcO> acoList = cfgData.getAcOByUnitNo(ui.getUnitNo(), companyId);
                ids = new Integer[acoList.size()];
                for (int i = 0; i < ids.length; i++)
                    ids[i] = acoList.get(i).getId();
                break;
            }
        }
        return ids;
    }

    /***
     * 获取某个时间段中的所有5分钟点，即00:05、00:10
     * */
    public static List<String> get5MinPoint(Calendar begTime, Calendar endTime) {
        List<String> list = new ArrayList<>();

        Calendar tmpCal = Calendar.getInstance();
        tmpCal.setTimeInMillis(begTime.getTimeInMillis());
        tmpCal.set(Calendar.SECOND, endTime.get(Calendar.SECOND));
        tmpCal.set(Calendar.MILLISECOND, endTime.get(Calendar.MILLISECOND));

        while (tmpCal.getTimeInMillis() <= endTime.getTimeInMillis()) {
            int minute = tmpCal.get(Calendar.MINUTE);
            if (0 == minute % 5) {
                list.add(_DATE_FORMAT_.format(tmpCal.getTime()));
                tmpCal.add(Calendar.MINUTE, 5);
            } else {
                tmpCal.add(Calendar.MINUTE, 1);
            }
        }

        return list;
    }

    /***
     * 获取某个时间段中的所有整点，即00:00、01:00
     * */
    public static List<String> getHourPoint(Calendar begTime, Calendar endTime) {
        List<String> list = new ArrayList<>();

        Calendar tmpCal = Calendar.getInstance();
        tmpCal.setTimeInMillis(begTime.getTimeInMillis());
        tmpCal.set(Calendar.SECOND, endTime.get(Calendar.SECOND));
        tmpCal.set(Calendar.MILLISECOND, endTime.get(Calendar.MILLISECOND));

        while (tmpCal.getTimeInMillis() <= endTime.getTimeInMillis()) {
            int minute = tmpCal.get(Calendar.MINUTE);
            if (0 == minute) {
                list.add(_DATE_FORMAT_.format(tmpCal.getTime()));
                tmpCal.add(Calendar.HOUR, 1);
            } else {
                tmpCal.add(Calendar.MINUTE, 1);
            }
        }

        return list;
    }

    /***
     * 从ByteBuffer取一个字符串
     * */
    public String getString(ByteBuffer bb,byte length) throws UnsupportedEncodingException {
        byte[] temp = new byte[length];
        bb.get(temp);
        return new String(temp,"UTF-8");
    }

    /***
     * 字符串转字节数组,当字符串为Null时，数组为{0x00},否则数组索引0位置为字符串转为字节的长度
     * */
    public byte[] toByte(String text) throws UnsupportedEncodingException{
        byte[]  data = null;
        if(null == text || text.isEmpty()) {
            data = new byte[1];
            data[0] = 0x00;
        }else{
            byte[] bTemp = text.getBytes("UTF-8");
            data = new byte[bTemp.length + 1];
            data[0] = (byte)bTemp.length;
            System.arraycopy(bTemp,0,data,1,bTemp.length);
        }
        return data;
    }
    
    public static boolean isNull(Object obj) {
		if (obj != null) {
			String str = obj.toString().trim();
			return str.isEmpty() || "null".equals(str.toLowerCase());
		} else {
			return true;
		}
	}
}
