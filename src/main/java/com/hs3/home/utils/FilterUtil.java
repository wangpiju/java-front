package com.hs3.home.utils;

import com.hs3.entity.lotts.Lottery;
import com.hs3.home.bean.LotteryPojo;

import java.util.ArrayList;
import java.util.List;

public class FilterUtil {
    public static List<LotteryPojo> filterHotLotter(List<Lottery> list) {
        List<LotteryPojo> hotLotteryPojos = new ArrayList<>();
        list.forEach(x -> {
            if (x.getIsHot() == 1) {
                LotteryPojo lotteryPojo = new LotteryPojo();
                lotteryPojo.setId(x.getId());
                lotteryPojo.setName(x.getTitle());
                lotteryPojo.setImage(x.getId());
                lotteryPojo.setGroupName(x.getGroupName());
                lotteryPojo.setGroupId(x.getGroupId());
                lotteryPojo.setWeight(x.getWeight());
                hotLotteryPojos.add(lotteryPojo);
            }
        });
        hotLotteryPojos.sort(LotteryPojo::compareByWeight);
        return hotLotteryPojos;
    }

}
