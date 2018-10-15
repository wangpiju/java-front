package com.hs3.home.controller.article;

import com.hs3.db.Page;
import com.hs3.entity.article.Message;
import com.hs3.entity.article.MessageContent;
import com.hs3.entity.users.User;
import com.hs3.exceptions.BaseCheckException;
import com.hs3.home.controller.HomeAction;
import com.hs3.models.Jsoner;
import com.hs3.service.article.MessageService;
import com.hs3.service.user.UserService;
import com.hs3.utils.DateUtils;
import com.hs3.utils.StrUtils;
import com.hs3.web.auth.Auth;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Controller
@Scope("prototype")
@RequestMapping({"/message"})
public class MessageAction
        extends HomeAction {
    @Autowired
    private MessageService messageService;
    @Autowired
    private UserService userService;
    @Auth
    @ResponseBody
    @RequestMapping({"/messageCountUnRead"})
    public Object messageCountUnRead() {
        User user = (User) getLogin();
        if(null != user)
            return Jsoner.success(Integer.valueOf(this.messageService.countUnRead(user.getAccount())));
        else
            return Jsoner.success(0);
    }

    @RequestMapping({"/messageTable"})
    public Object messageTable() {
        ModelAndView modelAndView = isMobile() ? getViewWithHeadModel("/message/messageTable") : getView("/message/messageTable");

        User user = this.userService.findByAccount(((User) getLogin()).getAccount());

        Page p = getPageWithParams();

        modelAndView.addObject("messageList", this.messageService.listToShowByUser(user.getAccount(), p));
        modelAndView.addObject("p", p);
        modelAndView.addObject("user", user);
        modelAndView.addObject("specicalStr", StrUtils.getSpecialChars());

        return modelAndView;
    }

    @ResponseBody
    @RequestMapping({"/messageRead"})
    public Object messageRead(Integer id) {
        Message m = new Message();
        m.setId(id);
        m.setRever(((User) getLogin()).getAccount());
        return Jsoner.success(this.messageService.updateByUserRead(m));
    }

    @RequestMapping({"/messageContentRead"})
    public Object messageContentRead(Integer id) {
        ModelAndView modelAndView = getViewWithHeadModel("/message/messageDetail");

        User user = (User) getLogin();

        Message m = new Message();
        m.setId(id);
        m.setRever(user.getAccount());

        MessageContent mc = new MessageContent();
        mc.setMessageId(id);
        List<MessageContent> messageContentList = this.messageService.listContentByCond(mc, null, null, null);
        for (MessageContent messageContent : messageContentList) {
            messageContent.setSender((!user.getAccount().equals(messageContent.getSender())) && (messageContent.getSender().equals(user.getParentAccount())) ? "上级" : messageContent.getSender());
        }

        Message message = this.messageService.updateByUserRead(m);

        modelAndView.addObject("messageContentList", messageContentList);
        modelAndView.addObject("message", message);
        modelAndView.addObject("startTime", DateUtils.format(new Date()));

        return modelAndView;
    }

    @ResponseBody
    @RequestMapping({"/findMessageContent"})
    public Object findMessageContent(MessageContent mc, String startTime) {
        User user = (User) getLogin();

        MessageContent cond = new MessageContent();
        cond.setMessageId(mc.getMessageId());
        cond.setRever(user.getAccount());

        Map<String, Object> map = new HashMap();
        List<MessageContent> messageContentList = this.messageService.listContentByCond(cond, DateUtils.AddSecond(DateUtils.toDateNull(startTime), 1), null, null);
        for (MessageContent messageContent : messageContentList) {
            messageContent.setSender((!user.getAccount().equals(messageContent.getSender())) && (messageContent.getSender().equals(user.getParentAccount())) ? "上级" : messageContent.getSender());
        }
        map.put("messageContentList", messageContentList);
        if (!messageContentList.isEmpty()) {
            startTime = DateUtils.format(new Date());

            Message m = new Message();
            m.setId(cond.getMessageId());
            m.setRever(cond.getRever());
            this.messageService.updateByUserRead(m);
        }
        map.put("startTime", startTime);

        return Jsoner.success(map);
    }

    @ResponseBody
    @RequestMapping({"/messageContentReply"})
    public Object messageContentReply(Integer id, String revContent) {
        User user = (User) getLogin();

        Message message = this.messageService.find(id);
        if (message == null) {
            return Jsoner.error("消息不存在！");
        }
        if ((!message.getRever().equals(user.getAccount())) && (!message.getSender().equals(user.getAccount()))) {
            return Jsoner.error("没有权限回复！");
        }
        if (message.getSendType().intValue() == 0) {
            return Jsoner.error("系统消息不能回复！");
        }
        if (StrUtils.hasEmpty(new Object[]{revContent})) {
            return Jsoner.error("回复内容不能为空！");
        }
        if (revContent.length() > MessageService.MESSAGE_LENGTH_CONTENT) {
            return Jsoner.error("回复内容长度不能大于" + MessageService.MESSAGE_LENGTH_CONTENT + "字符！");
        }
        if (StrUtils.checkHadSpecialChar(revContent)) {
            return Jsoner.error("回复内容包含特殊字符！");
        }

        MessageContent mc = new MessageContent();
        mc.setContent(revContent);
        mc.setCreateTime(new Date());
        mc.setMessageId(id);
        mc.setRever(message.getRever().equals(user.getAccount()) ? message.getSender() : message.getRever());
        mc.setSender(user.getAccount());

        this.messageService.saveContent(message, mc);

        return Jsoner.success(Integer.valueOf(1));
    }

    @ResponseBody
    @RequestMapping({"/messageReply"})
    public Object messageReply(Integer id, String revContent) {
        Message m = new Message();
        m.setId(id);
        m.setRevContent(revContent);
        m.setRever(((User) getLogin()).getAccount());

        return null;
    }

    @ResponseBody
    @RequestMapping({"/messageDelete"})
    public Object messageDelete(Integer id) {
        Message m = new Message();
        m.setId(id);
        m.setRever(((User) getLogin()).getAccount());
        return Jsoner.success(Integer.valueOf(this.messageService.deleteByUser(m)));
    }

    @ResponseBody
    @RequestMapping({"/messageSend"})
    public Object messageSend(Message message, Integer contactType) {
        Message m = new Message();
        m.setSender(((User) getLogin()).getAccount());
        if (contactType.intValue() == 0) {
            m.setRever(((User) getLogin()).getParentAccount());
        } else {
            m.setRever(message.getRever());
        }
        m.setSendContent(message.getSendContent());
        m.setTitle(message.getTitle());
        try {
            this.messageService.saveByUser(m);
        } catch (BaseCheckException e) {
            return Jsoner.error(e.getMessage());
        }
        return Jsoner.success();
    }

    @ResponseBody
    @RequestMapping({"/messageReverCheck"})
    public Object messageReverCheck(String account) {
        User u = this.userService.findByAccount(account);
        return Jsoner.success(Boolean.valueOf((u != null) && (u.getParentAccount().equals(((User) getLogin()).getAccount()))));
    }
}
