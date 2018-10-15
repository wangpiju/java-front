package com.jf;

import com.alibaba.fastjson.JSONObject;
import com.hs3.entity.lotts.Bet;
import com.hs3.home.controller.internal.BonusRiskController;
import com.hs3.service.lotts.BetService;
import com.hs3.service.lotts.BonusRiskService;
import com.hs3.utils.ip.IPSeeker;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * program: java-front
 * des:
 * author: Terra
 * create: 2018-06-09 11:04
 **/
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class IpAddressTest {

    @Autowired
    private BetService betService;

    @Test
    public void testIpAddress() {
//        String address= IPSeeker.getInstance().getAddress("43.247.18.69");
//        System.out.println(address);
        Bet bet =betService.find("DAH0603dc7cd069f2c7428a");
        System.out.println(JSONObject.toJSONString(bet));
    }

}
