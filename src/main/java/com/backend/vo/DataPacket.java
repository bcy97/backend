package com.backend.vo;

import com.backend.util.Constants;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;


public class DataPacket {
    private byte HEAD = Constants.CC_HEAD;
    private int sessionID;
    private short cmdCode;
    private int length;
    private byte tailFlag;
    private byte[] datas;
    private byte TAIL = Constants.CC_TAIL;

    /***
     * 序例化
     * */
    public byte[] serialize() {
        ByteBuffer bb = ByteBuffer.allocate(length + 13);
        bb.order(ByteOrder.LITTLE_ENDIAN);

        bb.put(HEAD);
        bb.putInt(sessionID);
        bb.putShort(cmdCode);
        bb.putInt(length);
        bb.put(tailFlag);
        bb.put(datas);
        bb.put(TAIL);

        byte[] bDatas = new byte[bb.position()];
        System.arraycopy(bb.array(), 0, bDatas, 0, bb.position());

        return bDatas;
    }

    /***
     * 转数据包头
     * */
    public Boolean toDataPacketHead(byte[] head) {
        if (head.length < 12)
            return false;

        ByteBuffer bb = ByteBuffer.wrap(head);
        bb.order(ByteOrder.LITTLE_ENDIAN);

        HEAD = bb.get();
        sessionID = bb.getInt();
        cmdCode = bb.getShort();
        length = bb.getInt();
        tailFlag = bb.get();

        return true;
    }

    public DataPacket() {
    }

    public DataPacket(short cmdCode, byte[] datas) {
        sessionID = 0;
        this.cmdCode = cmdCode;
        this.length = datas.length;
        this.tailFlag = 0;
        this.datas = new byte[this.length];
        System.arraycopy(datas, 0, this.datas, 0, this.length);
    }

    public DataPacket(short cmdCode, byte[] datas, int sessionID) {
        this.sessionID = sessionID;
        this.cmdCode = cmdCode;
        this.length = datas.length;
        this.tailFlag = 0;
        this.datas = new byte[this.length];
        System.arraycopy(datas, 0, this.datas, 0, this.length);
    }

    public int getSessionID() {
        return sessionID;
    }

    public void setSessionID(int sessionID) {
        this.sessionID = sessionID;
    }

    public short getCmdCode() {
        return cmdCode;
    }

    public void setCmdCode(short cmdCode) {
        this.cmdCode = cmdCode;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public byte getTailFlag() {
        return tailFlag;
    }

    public void setTailFlag(byte tailFlag) {
        this.tailFlag = tailFlag;
    }

    public byte[] getDatas() {
        return datas;
    }

    public void setDatas(byte[] datas) {
        this.datas = datas;
    }

}
