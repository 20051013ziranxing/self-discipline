package com.example.eventbus;

public class UserBaseMessage {
    private String userName;
    private String userPassword;
    private String userNumber;
    private String userEmail;

    public UserBaseMessage(String userName, String userPassword, String userNumber, String userEmail) {
        this.userName = userName;
        this.userPassword = userPassword;
        this.userNumber = userNumber;
        this.userEmail = userEmail;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getUserNumber() {
        return userNumber;
    }

    public void setUserNumber(String userNumber) {
        this.userNumber = userNumber;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }
}
