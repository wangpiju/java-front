package com.hs3.home.bean;

public class InviteCode {
    private String id;
    private String code;
    private String date;
    private String rebateRatio;
    private Long count;
    private String registerCodeUrl;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getRegisterCodeUrl() {
        return registerCodeUrl;
    }

    public void setRegisterCodeUrl(String registerCodeUrl) {
        this.registerCodeUrl = registerCodeUrl;
    }

    public String getRebateRatio() {
        return rebateRatio;
    }

    public void setRebateRatio(String rebateRatio) {
        this.rebateRatio = rebateRatio;
    }

}
