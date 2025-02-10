package com.example.myfragment.bean;

public class Function {
    int functionIcon;
    String functionName;

    public Function(int functionIcon, String functionName) {
        this.functionIcon = functionIcon;
        this.functionName = functionName;
    }

    public int getFunctionIcon() {
        return functionIcon;
    }

    public String getFunctionName() {
        return functionName;
    }
}
