package com.backend.vo;

public class EventInfo {

    public EventInfo() {

    }

    public EventInfo(int id, String strTime, String info) {
        this.id = id;
        this.strTime = strTime;
        this.info = info;
    }

    private int id;
    private String strTime;
    private String info;
    private int eventLogLength;
    private byte[] eventLog;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStrTime() {
        return strTime;
    }

    public void setStrTime(String strTime) {
        this.strTime = strTime;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public int getEventLogLength() {
        return eventLogLength;
    }

    public void setEventLogLength(int eventLogLength) {
        this.eventLogLength = eventLogLength;
    }

    public byte[] getEventLog() {
        return eventLog;
    }

    public void setEventLog(byte[] eventLog) {
        this.eventLog = eventLog;
    }


}
