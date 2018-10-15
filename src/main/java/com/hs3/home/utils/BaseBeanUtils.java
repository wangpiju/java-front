package com.hs3.home.utils;

import com.hs3.home.bean.BaseReturnPojo;
import com.hs3.home.constant.BaseConstant;

/**
 * @author jason.wang
 * 20180410
 */
public class BaseBeanUtils {
    public static BaseReturnPojo getSuccessReturn(Object object) {
        BaseReturnPojo baseReturnPojo = new BaseReturnPojo();
        baseReturnPojo.setCode(BaseConstant.SUCCESS);
        baseReturnPojo.setMessage("success");
        baseReturnPojo.setData(object);
        baseReturnPojo.setPup(false);

        return baseReturnPojo;
    }

    public static BaseReturnPojo getSuccessReturn() {
        return getSuccessReturn(null);
    }

    public static BaseReturnPojo getFailReturn(Object object) {
        BaseReturnPojo baseReturnPojo = new BaseReturnPojo();
        baseReturnPojo.setCode(BaseConstant.FAIL);
        baseReturnPojo.setMessage("fail");
        baseReturnPojo.setData(object);
        baseReturnPojo.setPup(true);

        return baseReturnPojo;
    }

    public static BaseReturnPojo getFailReturn() {
        return getFailReturn(null);
    }

}
