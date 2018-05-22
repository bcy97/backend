package com.backend.vo;

public class AcValue {
    private long value = -1;
    private int hValue = -1;// 小时值
    private byte valid = 0;//是否有效位，1是有效，0是无效

    public long getValue() {
        return value;
    }

    public void setValue(long value) {
        this.value = value;
    }

    public byte getValid() {
        return valid;
    }

    public void setValid(byte valid) {
        this.valid = valid;
    }

    public int gethValue() {
        return hValue;
    }

    public void sethValue(int hValue) {
        this.hValue = hValue;
    }


}
