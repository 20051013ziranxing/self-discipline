package com.example.clockinfragment.singleton;

public class DateSingleton {
    // 私有的静态实例变量
    private static DateSingleton instance;

    // 日期字符串字段
    private String date;

    // 私有的构造函数，防止外部直接创建实例
    private DateSingleton() {
        // 初始化日期字符串，例如设置为当前日期
        this.date = getCurrentDate();
    }

    // 公共的静态方法，用于获取单例实例
    public static synchronized DateSingleton getInstance() {
        if (instance == null) {
            instance = new DateSingleton();
        }
        return instance;
    }

    // 获取当前日期字符串（格式化为YYYY-MM-DD）
    private String getCurrentDate() {
        java.util.Date currentDate = new java.util.Date();
        java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("yyyy-MM-dd");
        return formatter.format(currentDate);
    }

    // 设置日期字符串
    public void setDate(String date) {
        this.date = date;
    }

    // 获取日期字符串
    public String getDate() {
        return this.date;
    }
}
