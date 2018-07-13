package com.backend.vo;

public class CalValue {
	public CalValue() {
	}

	public CalValue(byte valid, String value) {
		this.valid = valid;
		this.value = value;
	}

	private byte valid;
	private String value;

	public byte getValid() {
		return valid;
	}

	public void setValid(byte valid) {
		this.valid = valid;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
