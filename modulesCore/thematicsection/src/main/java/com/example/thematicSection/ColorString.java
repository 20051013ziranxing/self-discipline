package com.example.thematicSection;

public class ColorString {
    private static ColorString instance;
    private String myColorString;

    public ColorString() {
        this.myColorString = "#94C9E1";
    }
    public static ColorString getInstance() {
        if (instance == null) {
            synchronized (ColorString.class) {
                if (instance == null) {
                    instance = new ColorString();
                }
            }
        }
        return instance;
    }

    public String getMyColorString() {
        return myColorString;
    }

    public void setMyColorString(String myColorString) {
        this.myColorString = myColorString;
    }
}
