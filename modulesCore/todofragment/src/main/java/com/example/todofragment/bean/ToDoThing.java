package com.example.todofragment.bean;

public class ToDoThing {
    String thingName;
    String thingGradle;
    String thingTime;
    Boolean thingFinish;

    public ToDoThing(String thingName, String thingGradle, String thingTime, Boolean thingFinish) {
        this.thingName = thingName;
        this.thingGradle = thingGradle;
        this.thingTime = thingTime;
        this.thingFinish = thingFinish;
    }

    public String getThingName() {
        return thingName;
    }

    public String getThingGradle() {
        return thingGradle;
    }

    public String getThingTime() {
        return thingTime;
    }

    public Boolean getThingFinish() {
        return thingFinish;
    }
}
