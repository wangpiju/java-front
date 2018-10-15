package com.hs3.home.controller.article;

import com.hs3.entity.article.Notice;
import com.hs3.entity.noticePage.PageModel;
import com.hs3.home.controller.HomeAction;
import com.hs3.models.Jsoner;
import com.hs3.service.article.NoticeService;
import com.hs3.service.lotts.LotteryService;
import com.hs3.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;


@Controller
@Scope("prototype")
@RequestMapping({"/notice"})
public class NoticeAction
        extends HomeAction {
    @Autowired
    private NoticeService noticeService;
    @Autowired
    private UserService userService;
    @Autowired
    private LotteryService lotteryService;

    @ResponseBody
    @RequestMapping({"/list"})
    public Object list(int pageNo, int pageSize) {
        PageModel<Notice> pageModel = this.noticeService.listByPage(pageNo, pageSize);
        return Jsoner.success(pageModel);
    }

    @RequestMapping(value = {"/index"}, method = {org.springframework.web.bind.annotation.RequestMethod.GET})
    public Object index(Integer id) {
        ModelAndView mv = getViewWithHeadModel("/platNotice/index");
        if (isMobile()) {
            List<Notice> noticeList = this.noticeService.getNoticeList(Integer.valueOf(10));
            mv.addObject("list", noticeList);
        } else {
            Notice notice = null;
            if (id != null) {
                notice = this.noticeService.find(id);
            } else {
                notice = this.noticeService.first();
            }
            mv.addObject("notice", notice);
        }

        return mv;
    }

    @ResponseBody
    @RequestMapping({"/getContentById"})
    public Object getContentById(Integer id) {
        Notice notice = this.noticeService.find(id);
        return Jsoner.success(notice);
    }

    @RequestMapping(value = {"/indexDel"}, method = {org.springframework.web.bind.annotation.RequestMethod.GET})
    public Object indexDel(Integer id) {
        ModelAndView mv = getViewWithHeadModel("/platNotice/detail");
        if (isMobile()) {
            Notice notice = this.noticeService.find(id);
            mv.addObject("notice", notice);
        }
        return mv;
    }
}
