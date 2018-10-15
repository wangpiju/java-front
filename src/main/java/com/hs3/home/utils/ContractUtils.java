package com.hs3.home.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ContractUtils {
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");

    public static List<String> getOneDateList(Date curDate) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(curDate);
        int day = cal.get(5);
        String t1 = "";
        String t2 = "";
        String t3 = "";
        String t4 = "";
        List<String> list = new java.util.ArrayList();
        if (day > 15) {
            cal.set(5, 16);
            t1 = sdf.format(cal.getTime());
            cal.set(5, cal.getActualMaximum(5));
            t2 = sdf.format(cal.getTime());
            cal.set(5, 1);
            t3 = sdf.format(cal.getTime());
            cal.set(5, 15);
            t4 = sdf.format(cal.getTime());
        } else {
            cal.set(5, 1);
            t1 = sdf.format(cal.getTime());
            cal.set(5, 15);
            t2 = sdf.format(cal.getTime());
            cal.add(2, -1);
            cal.set(5, 16);
            t3 = sdf.format(cal.getTime());
            cal.set(5, cal.getActualMaximum(5));
            t4 = sdf.format(cal.getTime());
        }
        list.add(t1 + "-" + t2);
        list.add(t3 + "-" + t4);
        return list;
    }

    public static List<String> getSecondDateList(Date curDate) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(curDate);
        String t1 = "";
        String t2 = "";
        String t3 = "";
        String t4 = "";
        List<String> list = new java.util.ArrayList();
        cal.set(5, 1);
        t1 = sdf.format(cal.getTime());
        cal.set(5, cal.getActualMaximum(5));
        t2 = sdf.format(cal.getTime());
        cal.add(2, -1);
        cal.set(5, 1);
        t3 = sdf.format(cal.getTime());
        cal.set(5, cal.getActualMaximum(5));
        t4 = sdf.format(cal.getTime());
        list.add(t1 + "-" + t2);
        list.add(t3 + "-" + t4);
        return list;
    }
}
