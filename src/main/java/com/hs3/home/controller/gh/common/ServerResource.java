package com.hs3.home.controller.gh.common;

import com.hs3.home.controller.HomeAction;
import com.hs3.home.utils.BaseBeanUtils;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Calendar;

/**
 * @author jason.wang
 * 20180410
 */
@Controller
@Scope("prototype")
@RequestMapping({"/api/server"})
public class ServerResource extends HomeAction {
    private static final ObjectMapper mapper = new ObjectMapper();

    /**
     * 取得服務器時間戳
     */
    @ResponseBody
    @RequestMapping(value = {"/time"}, method = {RequestMethod.GET})
    public JsonNode getServerTime() {
        //取得服務器時間戳
        long date = Calendar.getInstance().getTime().getTime();
        return mapper.valueToTree(BaseBeanUtils.getSuccessReturn(date));
    }


}
