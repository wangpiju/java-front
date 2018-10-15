package com.hs3.home.utils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class CollectionUtils {
    // 刪除重複元素
    @SuppressWarnings({"unchecked", "rawtypes"})
    public static List removeDuplicate(List list) {
        List result = new ArrayList<>();
        HashSet h = new HashSet(list);
        result.addAll(h);
        return result;
    }

    // 判斷是否為豹子
    public static boolean checkAllSame(List list) {
        boolean isAllSame = true;
        // 若每個元素都跟第一個相同則是豹子
        if (null != list && 0 < list.size()) {
            Object first = list.get(0);
            for (Object object : list) {
                if (!object.toString().equals(first.toString())) {
                    isAllSame = false;
                }
            }
        } else {
            return false;
        }

        return isAllSame;
    }

    public static void main(String[] args) {
        List list = new ArrayList<>();
        list.add(4);
        list.add(4);
        list.add(4);
        System.out.println(checkAllSame(list));
    }

}
