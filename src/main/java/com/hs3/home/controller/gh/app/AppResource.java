package com.hs3.home.controller.gh.app;

import com.hs3.home.constant.BaseConstant;
import com.hs3.home.utils.BaseBeanUtils;
import com.hs3.web.auth.Auth;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author jason.wang
 */
@Controller
@Scope("prototype")
@RequestMapping({"/api/app"})
public class AppResource {
    private static final ObjectMapper mapper = new ObjectMapper();

    /**
     * @param type
     * @return
     */
    @Auth
    @ResponseBody
    @RequestMapping(value = {"/getAppUrl"}, method = {RequestMethod.GET})
    public JsonNode getAppUrl(Integer type) {
        //default is android
        if (null == type)
            type = BaseConstant.ANDROID;
        String url = "";

        if (BaseConstant.ANDROID.equals(type))
            url = BaseConstant.ANDROID_URL;
        else if (BaseConstant.IOS.equals(type))
            url = BaseConstant.IOS_URL;

        return mapper.valueToTree(BaseBeanUtils.getSuccessReturn(url));
    }

}
