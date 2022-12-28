package com.msc.common.util;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * 2 * @Author: chenYf
 * 3 * @Date: 2021/11/3 17:05
 * 4
 */
@Slf4j
public class CollectionUtil {

    public static List<?> splitListByNum(List<?> list, int num) {
        if (list == null || list.size() == 0) {
            return list;
        }

        if (num <= 0) {
            new IllegalArgumentException("Wrong quantity.");
        }

        List wrapList = new ArrayList();
        int count = 0;
        while (count < list.size()) {
            wrapList.add(new ArrayList(list.subList(count, (count + num) > list.size() ? list.size() : count + num)));
            count += num;
        }

        return wrapList;
    }
}
