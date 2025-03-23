package com.example.thematicSection.bean;

public class ColorChoose {
    boolean selected;
    String color;

    public ColorChoose(boolean selected, String color) {
        this.selected = selected;
        this.color = color;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
