package com.hs3.home.controller.gh.common;

import com.hs3.entity.article.Notice;
import com.hs3.entity.lotts.Lottery;
import com.hs3.entity.webs.Img;
import com.hs3.home.bean.LotteryPojo;
import com.hs3.home.constant.BaseConstant;
import com.hs3.home.constant.RedisConstant;
import com.hs3.home.controller.HomeAction;
import com.hs3.home.controller.lotts.LotteryAction;
import com.hs3.home.utils.BaseBeanUtils;
import com.hs3.home.utils.FilterUtil;
import com.hs3.home.utils.RedisUtils;
import com.hs3.service.article.NoticeService;
import com.hs3.service.lotts.LotteryService;
import com.hs3.service.webs.ImgService;
import com.hs3.web.auth.Auth;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author jason.wang
 */
@Controller
@Scope("prototype")
@RequestMapping({"/api/index"})
public class IndexResource extends HomeAction {
    private static final ObjectMapper mapper = new ObjectMapper();
    private static final Logger logger = Logger.getLogger(LotteryAction.class);
    @Autowired
    LotteryService lotteryService;
    @Autowired
    private ImgService imgService;
    @Autowired
    private NoticeService noticeService;

    /**
     * 取得首頁資訊
     */
    @Auth
    @ResponseBody
    @RequestMapping(value = {"/getIndexInfo"}, method = {RequestMethod.GET})
    public JsonNode getIndexInfo() {
        if (null != RedisUtils.get(RedisConstant.INDEXINFO_REDIS_KEY)) {
            try {
                return mapper.readTree(RedisUtils.get(RedisConstant.INDEXINFO_REDIS_KEY));
            } catch (IOException e) {
                logger.error(e.getMessage(), e);
            }
        }
        //取得banner
        List<Img> images = this.imgService.listByShow(BaseConstant.MOBILE_IMG);

        // 取得正常狀態的彩票組列表
        List<Lottery> lotterys = this.lotteryService.listAndOrder(null);

        if (isMobile()) { // mobile
            lotterys = lotterys.stream().filter(x -> x.getStatus() == 0 && x.getMobileStatus() == 0)
                    .collect(Collectors.toList());
        } else {// pc
            lotterys = lotterys.stream().filter(x -> x.getStatus() == 0).collect(Collectors.toList());
        }
        // hot
        List<LotteryPojo> hotLotteryPojos = FilterUtil.filterHotLotter(lotterys);
        List<Notice> noticeList = this.noticeService.getNoticeList(5);
        List<Notice> noticeListResult = new ArrayList<>();
        for (Notice notice : noticeList) {
            notice.setContent("");
            noticeListResult.add(notice);
        }

        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("hotLotterys", hotLotteryPojos);
        resultMap.put("banners", images);
        resultMap.put("noticeList", noticeListResult);
        System.out.println(resultMap.toString());
        RedisUtils.set(RedisConstant.INDEXINFO_REDIS_KEY, mapper.valueToTree(BaseBeanUtils.getSuccessReturn(resultMap)).toString(), 500);


        return mapper.valueToTree(BaseBeanUtils.getSuccessReturn(resultMap));
    }
}
