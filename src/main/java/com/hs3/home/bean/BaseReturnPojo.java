package com.hs3.home.bean;

/**
 * @author jason.wang
 * @data 20180410
 * 基礎回傳模板
 */
public class BaseReturnPojo {
    private Integer code;
    private String message;
    private Object data;
    private boolean isPup = false;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public boolean isPup() {
        return isPup;
    }

    public void setPup(boolean isPup) {
        this.isPup = isPup;
    }


}
