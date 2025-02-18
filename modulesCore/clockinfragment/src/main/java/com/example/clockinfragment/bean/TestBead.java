package com.example.clockinfragment.bean;

public class TestBead {
    //喝水
    String Name;
    //每天所要执行的次数
    String SumCount;
    //完成了的次数
    String FinishCount;
    //
    int IconId;
    int BackgroundId;

    public TestBead(String name, String sumCount, String finishCount, int iconId, int backgroundId) {
        Name = name;
        SumCount = sumCount;
        FinishCount = finishCount;
        IconId = iconId;
        BackgroundId = backgroundId;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getSumCount() {
        return SumCount;
    }

    public void setSumCount(String sumCount) {
        SumCount = sumCount;
    }

    public String getFinishCount() {
        return FinishCount;
    }

    public void setFinishCount(String finishCount) {
        FinishCount = finishCount;
    }

    public int getIconId() {
        return IconId;
    }

    public void setIconId(int iconId) {
        IconId = iconId;
    }

    public int getBackgroundId() {
        return BackgroundId;
    }

    public void setBackgroundId(int backgroundId) {
        BackgroundId = backgroundId;
    }
}
