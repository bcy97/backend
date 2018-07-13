package com.backend.vo;

public class AcValue {
    public AcValue() {

    }

    public AcValue(double value, float hValue, byte valid) {
        this.value = value;
        this.hValue = hValue;// 小时值
        this.valid = valid;//是否有效位，1是有效，0是无效
    }

    private double value = -1;
    private float hValue = -1;
    private byte valid = 0;

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public byte getValid() {
        return valid;
    }

    public void setValid(byte valid) {
        this.valid = valid;
    }

    public float gethValue() {
        return hValue;
    }

    public void sethValue(float hValue) {
        this.hValue = hValue;
    }

}