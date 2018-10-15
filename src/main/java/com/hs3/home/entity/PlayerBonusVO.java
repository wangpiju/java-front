package com.hs3.home.entity;

import com.hs3.entity.lotts.Player;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.node.ObjectNode;
import java.math.BigDecimal;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class PlayerBonusVO extends Player {
    private BigDecimal bonusRatio;
    private BigDecimal rebateRatio;
    private BigDecimal bonus;
    private ObjectNode bonusArray;
    private String displayBonus;

    public BigDecimal getBonusRatio() {
        return this.bonusRatio;
    }

    public void setBonusRatio(BigDecimal bonusRatio) {
        this.bonusRatio = bonusRatio;
    }

    public BigDecimal getRebateRatio() {
        return this.rebateRatio;
    }

    public void setRebateRatio(BigDecimal rebateRatio) {
        this.rebateRatio = rebateRatio;
    }

    public BigDecimal getBonus() {
        return this.bonus;
    }

    public void setBonus(BigDecimal bonus) {
        this.bonus = bonus;
    }

    public ObjectNode getBonusArray() {
        return bonusArray;
    }

    public void setBonusArray(ObjectNode bonusArray) {
        this.bonusArray = bonusArray;
    }

    public String getDisplayBonus() {
        return displayBonus;
    }

    public void setDisplayBonus(String displayBonus) {
        this.displayBonus = displayBonus;
    }
}