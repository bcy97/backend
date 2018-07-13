package com.backend.vo;

public class AcStatisData {
	public AcStatisData() {

	}

	public AcStatisData(int id, AcValue begValue, AcValue endValue,
			AcValue accValue) {
		this.id = id;
		this.begValue = begValue;
		this.endValue = endValue;
		this.accValue = accValue;
	}

	private int id;
	private AcValue begValue;
	private AcValue endValue;
	private AcValue accValue;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public AcValue getBegValue() {
		return begValue;
	}

	public void setBegValue(AcValue begValue) {
		this.begValue = begValue;
	}

	public AcValue getEndValue() {
		return endValue;
	}

	public void setEndValue(AcValue endValue) {
		this.endValue = endValue;
	}

	public AcValue getAccValue() {
		return accValue;
	}

	public void setAccValue(AcValue accValue) {
		this.accValue = accValue;
	}

}
