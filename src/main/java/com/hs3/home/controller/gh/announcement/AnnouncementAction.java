package com.hs3.home.controller.gh.announcement;

import com.hs3.home.controller.HomeAction;
import com.hs3.home.utils.BaseBeanUtils;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author Stephen Zhou
 */
@Controller
@Scope("prototype")
@RequestMapping({"/api/announcement"})
public class AnnouncementAction extends HomeAction {

    private static final ObjectMapper mapper = new ObjectMapper();

    //首页-序6

    /***首頁系統公告***/
    @ResponseBody
    @RequestMapping(value = {"/getAnnouncementList"}, method = {RequestMethod.GET})
    public JsonNode getAnnouncementList() {
        //String webUrl = "http://192.168.8.108:8080/res/announcement/";
        String webUrl = "http://115.144.238.217/res/announcement/";

        List<HashMap<String, String>> resultList = new ArrayList<>();
        HashMap<String, String> imageO = new HashMap<>();
        imageO.put("image", webUrl + "announcement-1.jpg");
        HashMap<String, String> imageT = new HashMap<>();
        imageT.put("image", webUrl + "announcement-2.jpg");
        HashMap<String, String> imageTh = new HashMap<>();
        imageTh.put("image", webUrl + "announcement-3.jpg");
        resultList.add(imageO);
        resultList.add(imageT);
        resultList.add(imageTh);

        HashMap<String, List<HashMap<String, String>>> dataO = new HashMap<>();
        dataO.put("list", resultList);

        return mapper.valueToTree(BaseBeanUtils.getSuccessReturn(dataO));

    }


}
