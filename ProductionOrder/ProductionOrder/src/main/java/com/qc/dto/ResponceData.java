package com.qc.dto;

public class ResponceData {
    private String status;
    private int statusCode;
    private String message;
    private Object data;
    private long count;

    public ResponceData(String status, int statusCode, String message, Object data, long count) {
        this.status = status;
        this.statusCode = statusCode;
        this.message = message;
        this.data = data;
        this.count = count;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }
}
