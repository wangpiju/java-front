package com.hs3.home.controller.finance;

import com.hs3.dao.user.UserPrivilegedDao;
import com.hs3.entity.users.User;
import com.hs3.entity.users.UserPrivileged;
import com.hs3.home.controller.HomeAction;
import com.hs3.home.utils.BaseBeanUtils;
import com.hs3.utils.DateUtils;
import net.sf.json.JSONObject;
import org.apache.commons.io.FileUtils;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Controller
@Scope("prototype")
@RequestMapping({"/upload"})
public class UploadFileAction extends HomeAction {

    private static final ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private UserPrivilegedDao userPrivilegedDao;

    @RequestMapping(value = {"/qrcode"}, method = {org.springframework.web.bind.annotation.RequestMethod.GET})
    public Object rechargeMoneyBank() {

        User user_s = getLogin();
        String account = user_s.getAccount();

        String viewName = "";
        viewName = "/upload/qrcode";

        ModelAndView modelAndView = getViewWithHeadModel(viewName);
        return modelAndView;
    }

    /***上传文件***/
    @ResponseBody
    @RequestMapping(value = {"/setFile"}, method = {RequestMethod.POST})
    public JSONObject setFile(MultipartFile sourceFile) throws IOException {

        //System.out.println("===========================>>>>>>>>>>>进来了！！！");
        //HashMap<String, String> returnC = new HashMap<String, String>();
        JSONObject json = new JSONObject();

        String fileName = "";

        if(sourceFile != null) {

            Date nowDate = new Date();
            String nowDateStr = DateUtils.formatPayTime(nowDate);

            int emailCodeInt = (int) ((Math.random() * 9 + 1) * 1000);
            String code = String.valueOf(emailCodeInt);

            fileName = nowDateStr + code + ".jpg";
            String realPath = getSession().getServletContext().getRealPath("/");
            String targetPath = realPath + "/res/payQRCode/" + fileName;

            FileUtils.copyInputStreamToFile(sourceFile.getInputStream(), new File(targetPath));
        }else{
            //returnC.put("message", "请先选择需要上传的文件！");
            //return BaseBeanUtils.getFailReturn(returnC);
            json.put("code", 1);
            json.put("message", "请先选择需要上传的文件！");
            return json;
        }

        //HashMap<String, Object> returnO = new HashMap<String, Object>();
        //returnO.put("qrcodeurl", fileName); //文件名
        //return BaseBeanUtils.getSuccessReturn(returnO);
        json.put("code", 0);
        json.put("qrcodeurl", fileName);
        return json;
    }

    @ResponseBody
    @RequestMapping(value = {"/getUploadFlag"}, method = {RequestMethod.GET})
    public JsonNode getUploadFlag() {
        HashMap<String, String> returnC = new HashMap<String, String>();
        try {

            User user_s = getLogin();
            String account_s = user_s.getAccount();

            List<UserPrivileged> listUserPrivileged = userPrivilegedDao.listUserPrivileged(1, account_s);
            if(listUserPrivileged.size() > 0 && listUserPrivileged.get(0).getAccount()!=null){
                returnC.put("uflag", "1");
            }else{
                returnC.put("uflag", "0");
            }

            return mapper.valueToTree(BaseBeanUtils.getSuccessReturn(returnC));
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            returnC.put("message", e.getMessage());
            return mapper.valueToTree(BaseBeanUtils.getFailReturn(returnC));
        }
    }



}
