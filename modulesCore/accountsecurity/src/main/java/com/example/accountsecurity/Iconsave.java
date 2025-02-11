package com.example.accountsecurity;

public class Iconsave {
    private static final Iconsave INSTANCE = new Iconsave("Default String");

    private String myString;

    Iconsave(String str) {
        this.myString = str;
    }

    public static Iconsave getInstance() {
        return INSTANCE;
    }

    public String getMyString() {
        return myString;
    }

    public void setMyString(String myString) {
        this.myString = myString;
    }
}
