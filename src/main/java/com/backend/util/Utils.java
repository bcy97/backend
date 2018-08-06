package com.backend.util;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class Utils {
	
	public static int getId(byte type, short unitNo, short ptNo) {

		int id = type << 24 | (unitNo & 0xFF) << 16 | (ptNo & 0xFFFF);

		return id;
	}

	public static byte getTypeInId(int id) {

		byte type = (byte) (id >> 24);

		return type;
	}

	public static short getUnitNoInId(int id) {

		short unitNo = (byte) (id >> 16);

		return unitNo;
	}

	public static short getPtNoInId(int id) {

		short ptNo = (short) id;

		return ptNo;
	}

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
