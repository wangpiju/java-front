package com.hs3.home.test;

import java.math.BigDecimal;
import java.math.RoundingMode;


public class TestBet {

    public static void main(String[] args) {
        BigDecimal bigDecimal = new BigDecimal("4.2545");
        BigDecimal x = new BigDecimal("0.2563");
        System.out.println(bigDecimal.divide(x, 2, RoundingMode.DOWN));
        System.out.println(bigDecimal.multiply(x).setScale(2, RoundingMode.DOWN));
        Integer aInteger = new Integer(0);
        System.out.println(aInteger.equals(Integer.valueOf(0)));
    }

}
