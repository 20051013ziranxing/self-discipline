package com.example.clockinfragment.bean;

public class Icon {
    int IconIn;
    String IconBackground;

    public Icon(int iconIn, String iconBackground) {
        IconIn = iconIn;
        IconBackground = iconBackground;
    }

    public int getIconIn() {
        return IconIn;
    }

    public void setIconIn(int iconIn) {
        IconIn = iconIn;
    }

    public String getIconBackground() {
        return IconBackground;
    }

    public void setIconBackground(String iconBackground) {
        IconBackground = iconBackground;
    }
}
