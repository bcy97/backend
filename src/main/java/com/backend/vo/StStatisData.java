package com.backend.vo;

public class StStatisData {
	public StStatisData(){
		
	}
	
	public StStatisData(int id,int count){
		this.id = id;
		this.count = count;
	}
	
	private int id;
	private int count;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	
}
