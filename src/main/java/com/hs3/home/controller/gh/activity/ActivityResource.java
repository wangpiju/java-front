package com.hs3.home.controller.gh.activity;

import com.hs3.entity.activity.Activity;
import com.hs3.home.constant.RedisConstant;
import com.hs3.home.controller.HomeAction;
import com.hs3.home.controller.lotts.LotteryAction;
import com.hs3.home.utils.BaseBeanUtils;
import com.hs3.home.utils.RedisUtils;
import com.hs3.service.activity.ActivityService;
import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.List;

@Controller
@Scope("prototype")
@RequestMapping({"/api/activity"})
public class ActivityResource extends HomeAction {
    private static final ObjectMapper mapper = new ObjectMapper();
    private static final Logger logger = Logger.getLogger(LotteryAction.class);
    @Autowired
    private ActivityService activityService;

    @ResponseBody
    @RequestMapping(value = {"/getList"}, method = {RequestMethod.GET})
    public JsonNode getList() {
        if (null != RedisUtils.get(RedisConstant.ACTIVITY_LIST_KEY)) {
            try {
                return mapper.readTree(RedisUtils.get(RedisConstant.ACTIVITY_LIST_KEY));
            } catch (IOException e) {
                logger.error(e.getMessage(), e);
            }
        }
        List<Activity> resultList = this.activityService.listByActive(getLogin().getUserType());
        RedisUtils.set(RedisConstant.ACTIVITY_LIST_KEY, mapper.valueToTree(BaseBeanUtils.getSuccessReturn(resultList)).toString(), 500);
        return mapper.valueToTree(BaseBeanUtils.getSuccessReturn(resultList));

    }
}
