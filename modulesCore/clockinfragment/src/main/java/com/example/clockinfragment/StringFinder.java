package com.example.clockinfragment;

import java.util.HashMap;
import java.util.Map;

public class StringFinder {
    private static final Map<Integer, String> intToStringMap = new HashMap<>();

    // 静态初始化块，用于填充数据
    static {
        intToStringMap.put(R.drawable.walter, "#ADD8E6");
        intToStringMap.put(R.drawable.morning, "#FFFBEA");
        intToStringMap.put(R.drawable.night, "#D8BFD8");
        intToStringMap.put(R.drawable.firut, "#D8BFD8");
        intToStringMap.put(R.drawable.exercise, "#ADD8E6");
        intToStringMap.put(R.drawable.word, "#D2D2E6");
    }

    // 提供一个静态方法来根据 int 查找 String
    public static String getStringFromInt(int key) {
        if (intToStringMap.get(key) == null) {
            return "#ADD8E6";
        } else {
            return intToStringMap.get(key);
        }
    }
}
