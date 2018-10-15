package com.hs3.home.controller.user;

import com.hs3.entity.lotts.BonusGroup;
import com.hs3.entity.lotts.Lottery;
import com.hs3.entity.users.User;
import com.hs3.home.controller.HomeAction;
import com.hs3.lotts.LotteryBase;
import com.hs3.lotts.LotteryFactory;
import com.hs3.service.lotts.BonusGroupService;
import com.hs3.service.lotts.LotteryService;
import com.hs3.service.lotts.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@Scope("prototype")
@RequestMapping({"/userAjax"})
public class UserAjaxAction extends HomeAction {
    @Autowired
    private LotteryService lotteryService;
    @Autowired
    private PlayerService playerService;
    @Autowired
    private BonusGroupService bonusGroupService;

    @RequestMapping({"/lotts"})
    public Object index(String lotteryId) {
        ModelAndView mv = getView("/user/lotts");
        Integer groupId = ((User) getLogin()).getBonusGroupId();
        Lottery lottery = this.lotteryService.find(lotteryId);

        LotteryBase tree = LotteryFactory.getInstance(lottery.getGroupName());
        List<com.hs3.models.lotts.PlayerBonus> playerBonus = this.playerService.listFullByLotteryIdAndGroupId(lotteryId, groupId);
        tree.loadPlayerBonus(playerBonus);

        BonusGroup bonusGroup = this.bonusGroupService.find(groupId);

        mv.addObject("lotts", lottery);
        mv.addObject("tree", tree);
        mv.addObject("bonusGroup", bonusGroup);
        mv.addObject("u", getLogin());
        return mv;
    }
}
