package com.backend.vo;

public class AnStatisData {
	public AnStatisData() {

	}

	public AnStatisData(int id, AnValue maxValue, String maxTime,
			AnValue minValue, String minTime, AnValue avgValue, int count,
			float accValue) {
		this.id = id;
		this.maxTime = maxTime;
		this.maxValue = maxValue;
		this.minTime = minTime;
		this.minValue = minValue;
	}

	private int id;
	private AnValue maxValue;
	private String maxTime;
	private AnValue minValue;
	private String minTime;
	private AnValue avgValue;
	private int count;
	private float accValue;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public AnValue getMaxValue() {
		return maxValue;
	}

	public void setMaxValue(AnValue maxValue) {
		this.maxValue = maxValue;
	}

	public String getMaxTime() {
		return maxTime;
	}

	public void setMaxTime(String maxTime) {
		this.maxTime = maxTime;
	}

	public AnValue getMinValue() {
		return minValue;
	}

	public void setMinValue(AnValue minValue) {
		this.minValue = minValue;
	}

	public String getMinTime() {
		return minTime;
	}

	public void setMinTime(String minTime) {
		this.minTime = minTime;
	}

	public AnValue getAvgValue() {
		return avgValue;
	}

	public void setAvgValue(AnValue avgValue) {
		this.avgValue = avgValue;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public float getAccValue() {
		return accValue;
	}

	public void setAccValue(float accValue) {
		this.accValue = accValue;
	}
}
