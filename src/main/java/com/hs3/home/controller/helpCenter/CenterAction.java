package com.hs3.home.controller.helpCenter;

import com.hs3.entity.article.Article;
import com.hs3.home.controller.HomeAction;
import com.hs3.models.Jsoner;
import com.hs3.models.helpCenter.ArticleModel;
import com.hs3.service.helpCenter.CenterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;


@Controller
@Scope("prototype")
@RequestMapping({"/helpCenter"})
public class CenterAction
        extends HomeAction {

    @Autowired
    private CenterService centerService;

    @RequestMapping(value = {"/index"}, method = {org.springframework.web.bind.annotation.RequestMethod.GET})
    public Object index(Integer id) {
        ModelAndView mv = getViewWithHeadModel("/helpCenter/index");
        List<ArticleModel> articleModels = this.centerService.findList();
        mv.addObject("articleModels", articleModels);
        mv.addObject("id", id);
        return mv;
    }

    @RequestMapping(value = {"/aboutUsIndex"}, method = {org.springframework.web.bind.annotation.RequestMethod.GET})
    public Object aboutUsIndex() {
        ModelAndView mv = getViewWithHeadModel("/helpCenter/aboutUs");

        return mv;
    }

    @RequestMapping(value = {"/getContentById"}, method = {org.springframework.web.bind.annotation.RequestMethod.GET})
    public Object getContentById(Integer id, String groupName)
            throws UnsupportedEncodingException {
        Article article = this.centerService.getContentById(id);
        ModelAndView mv;
        if ((article.getView() != null) && (!"".equals(article.getView()))) {
            mv = getView(article.getView());
        } else {
            mv = getView("/helpCenter/detail");
        }
        String newGroupName = URLDecoder.decode(groupName, "utf-8");
        mv.addObject("article", article);
        mv.addObject("groupName", newGroupName);
        return mv;
    }

    @ResponseBody
    @RequestMapping(value = {"/getContentByAjax"}, method = {org.springframework.web.bind.annotation.RequestMethod.GET})
    public Object getContentByAjax(Integer id) throws UnsupportedEncodingException {
        Article article = this.centerService.getContentById(id);
        return Jsoner.success(article);
    }
}
