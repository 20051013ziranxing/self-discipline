package com.example.clockinfragment.bean;

import java.util.List;
import java.util.Map;

public class CheckInBean1 {
    private String message;
    private Data data; // 修改为 Data 类
    private String status;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // 新增 Data 类
    public static class Data {
        private List<CheckinData> checkins;
        private Map<String, Map<String, Integer>> decodedCounts;
        private Map<String, Boolean> seenIDs;

        public List<CheckinData> getCheckins() {
            return checkins;
        }

        public void setCheckins(List<CheckinData> checkins) {
            this.checkins = checkins;
        }

        public Map<String, Map<String, Integer>> getDecodedCounts() {
            return decodedCounts;
        }

        public void setDecodedCounts(Map<String, Map<String, Integer>> decodedCounts) {
            this.decodedCounts = decodedCounts;
        }

        public Map<String, Boolean> getSeenIDs() {
            return seenIDs;
        }

        public void setSeenIDs(Map<String, Boolean> seenIDs) {
            this.seenIDs = seenIDs;
        }
    }

    // CheckinData 类和 Checkin 类保持不变
    public class CheckinData {
        private Checkin checkin;
        private Map<String, Integer> decoded_checkin_count;

        // Getters and Setters
        public Checkin getCheckin() {
            return checkin;
        }

        public void setCheckin(Checkin checkin) {
            this.checkin = checkin;
        }

        public Map<String, Integer> getDecodedCheckinCount() {
            return decoded_checkin_count;
        }

        public void setDecodedCheckinCount(Map<String, Integer> decodedCheckinCount) {
            this.decoded_checkin_count = decodedCheckinCount;
        }
    }

    public class Checkin {
        private int id;
        private String user_id;
        private String start_date;
        private String end_date;
        private String checkin_count; // Base64编码的字符串
        private int target_checkin_count;
        private String title;
        private int icon;
        private String motivation_message;

        // Getters and Setters
        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public String getStart_date() {
            return start_date;
        }

        public void setStart_date(String start_date) {
            this.start_date = start_date;
        }

        public String getEnd_date() {
            return end_date;
        }

        public void setEnd_date(String end_date) {
            this.end_date = end_date;
        }

        public String getCheckin_count() {
            return checkin_count;
        }

        public void setCheckin_count(String checkin_count) {
            this.checkin_count = checkin_count;
        }

        public int getTarget_checkin_count() {
            return target_checkin_count;
        }

        public void setTarget_checkin_count(int target_checkin_count) {
            this.target_checkin_count = target_checkin_count;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getIcon() {
            return icon;
        }

        public void setIcon(int icon) {
            this.icon = icon;
        }
    }
}