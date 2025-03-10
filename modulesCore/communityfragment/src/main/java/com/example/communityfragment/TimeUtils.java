package com.example.communityfragment;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TimeUtils {
    public static String getFormatTime(String createdTime) {
        String regex = "(\\d{4})-(\\d{2})-(\\d{2})T(\\d{2}):(\\d{2}):\\d{2}.*";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(createdTime);
        if (matcher.find()) {
            String year = matcher.group(1);
            int month = Integer.parseInt(matcher.group(2));
            int day = Integer.parseInt(matcher.group(3));
            String hour = matcher.group(4);
            String minute = matcher.group(5);

            return "发布于 " + year + "." + month + "." + day + " " + hour + ":" + minute;
        } else {
            return "未知";
        }
    }

    public static String getRelativeTime(String timeStr) {
        String regex = "(\\d{4})-(\\d{2})-(\\d{2})T(\\d{2}):(\\d{2}):\\d{2}.*";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(timeStr);
        if (!matcher.matches()) {
            return "时间格式错误";
        }
        int year = Integer.parseInt(matcher.group(1));
        int month = Integer.parseInt(matcher.group(2));
        int day = Integer.parseInt(matcher.group(3));
        int hour = Integer.parseInt(matcher.group(4));
        int minute = Integer.parseInt(matcher.group(5));

        LocalDateTime parsedTime = LocalDateTime.of(year, month, day, hour, minute);
        LocalDateTime now = LocalDateTime.now();

        Duration duration = Duration.between(parsedTime, now);
        long diffSeconds = duration.getSeconds();

        if (diffSeconds < 0) {
            return "时间错误";
        }

        long diffMinutes = diffSeconds / 60;
        long diffHours = diffSeconds / 3600;
        long diffDays = diffSeconds / (3600 * 24);

        if (diffSeconds < 3600) {
            return diffMinutes + "分钟前";
        } else if (diffSeconds < 3600 * 24) {
            return diffHours + "小时前";
        } else if (diffDays < 3) {
            return diffDays + "天前";
        } else {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm");
            return parsedTime.format(formatter);
        }
    }

}
