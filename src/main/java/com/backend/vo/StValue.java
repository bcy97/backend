package com.backend.vo;

public class StValue {
    private byte value = -1;
    private byte valid = 0;//是否有效位，1是有效，0是无效

    public byte getValue() {
        return value;
    }

    public void setValue(byte value) {
        this.value = value;
    }

    public byte getValid() {
        return valid;
    }

    public void setValid(byte valid) {
        this.valid = valid;
    }

}
