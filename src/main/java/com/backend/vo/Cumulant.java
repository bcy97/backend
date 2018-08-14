package com.backend.vo;

public class Cumulant {

    private int id; //序号
    private String name; //名称
    private double today; //今日累加量
    private double lastday; //昨天累加量
    private double thisMonth; //本月累加量
    private double lastMonth; //上月累加量
    private double statis; //统计量

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getToday() {
        return today;
    }

    public void setToday(double today) {
        this.today = today;
    }

    public double getLastday() {
        return lastday;
    }

    public void setLastday(double lastday) {
        this.lastday = lastday;
    }

    public double getThisMonth() {
        return thisMonth;
    }

    public void setThisMonth(double thisMonth) {
        this.thisMonth = thisMonth;
    }

    public double getLastMonth() {
        return lastMonth;
    }

    public void setLastMonth(double lastMonth) {
        this.lastMonth = lastMonth;
    }

    public double getStatis() {
        return statis;
    }

    public void setStatis(double statis) {
        this.statis = statis;
    }
}
