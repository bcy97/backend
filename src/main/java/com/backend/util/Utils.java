package com.backend.util;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class Utils {

    /***
     * 根据类型，单元号，点号生成id
     * */
    public static int getId(byte type, short unitNo, short ptNo) {

        int id = type << 24 | (unitNo & 0xFF) << 16 | (ptNo & 0xFFFF);

        return id;
    }

    /***
     * 在id中获取类型
     * */
    public static byte getTypeInId(int id) {

        byte type = (byte) (id >> 24);

        return type;
    }

    /***
     * 在id中获取单元号
     * */
    public static short getUnitNoInId(int id) {

        short unitNo = (byte) (id >> 16);

        return unitNo;
    }

    /***
     * 在id中获取点号
     * */
    public static short getPtNoInId(int id) {

        short ptNo = (short) id;

        return ptNo;
    }

    /***
     * 把id数组转为字节数组
     * */
    public static byte[] idArrToBytes(int[] idArr) {
        byte[] idByteArr = new byte[idArr.length * 4];

        ByteBuffer bb = ByteBuffer.allocate(idByteArr.length);
        bb.order(ByteOrder.LITTLE_ENDIAN);
        for (int id : idArr)
            bb.putInt(id);

        System.arraycopy(bb.array(), 0, idByteArr, 0, bb.position());

        return idByteArr;
    }

}
