package com.example.localdatabase.bean;

import java.util.ArrayList;
import java.util.List;

//用户的清单与标签信息
public class UserTables {
    String userName;
    String userId;
    List<UserListing> userListings;
    List<UserLabel> userLabels;

    public UserTables(String userName, String userId, List<UserListing> userListings, List<UserLabel> userLabels) {
        this.userName = userName;
        this.userId = userId;
        this.userListings = userListings;
        this.userLabels = userLabels;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<UserListing> getUserListings() {
        return userListings;
    }

    public void setUserListings(List<UserListing> userListings) {
        this.userListings = userListings;
    }

    public List<UserLabel> getUserLabels() {
        return userLabels;
    }

    public void setUserLabels(List<UserLabel> userLabels) {
        this.userLabels = userLabels;
    }

    public boolean addUserLabel(UserLabel userLabel) {
        if (userLabels == null) {
            userLabels = new ArrayList<>();
        }
        for (UserLabel existingLabel : userLabels) {
            if (existingLabel.getLabelName().equals(userLabel.getLabelName())) {
                existingLabel.setLabelColor(userLabel.getLabelColor());
                existingLabel.setLabelName(userLabel.getLabelName());
                return false; // 表示更新成功
            }
        }
        userLabels.add(userLabel);
        return true; // 表示添加成功
    }

    // 删除UserLabel对象
    public boolean removeUserLabel(String labelName) {
        if (userLabels == null) {
            return false;
        }
        for (UserLabel label : userLabels) {
            if (label.getLabelName().equals(labelName)) {
                userLabels.remove(label);
                return true;
            }
        }
        return false;
    }

    public boolean addUserListing(UserListing userListing) {
        if (userListings == null) {
            userListings = new ArrayList<>();
        }
        for (UserListing existingListing : userListings) {
            if (existingListing.getListingName().equals(userListing.getListingName())) {
                existingListing.setListingColor(userListing.getListingColor());
                existingListing.setListingName(userListing.getListingName());
                return false; 
            }
        }
        userListings.add(userListing);
        return true;
    }

    public boolean removeUserListing(String listingName) {
        if (userListings == null) {
            return false;
        }
        for (UserListing listing : userListings) {
            if (listing.getListingName().equals(listingName)) {
                userListings.remove(listing);
                return true;
            }
        }
        return false;
    }

    public static class UserListing {
        String listingColor;
        String listingName;

        public UserListing(String listingColor, String listingName) {
            this.listingColor = listingColor;
            this.listingName = listingName;
        }

        public String getListingColor() {
            return listingColor;
        }

        public void setListingColor(String listingColor) {
            this.listingColor = listingColor;
        }

        public String getListingName() {
            return listingName;
        }

        public void setListingName(String listingName) {
            this.listingName = listingName;
        }

    }

    public static class UserLabel {
        String labelColor;
        String labelName;

        public UserLabel(String labelColor, String labelName) {
            this.labelColor = labelColor;
            this.labelName = labelName;
        }

        public String getLabelColor() {
            return labelColor;
        }

        public void setLabelColor(String labelColor) {
            this.labelColor = labelColor;
        }

        public String getLabelName() {
            return labelName;
        }

        public void setLabelName(String labelName) {
            this.labelName = labelName;
        }
    }
}
