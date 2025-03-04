package com.example.todofragment;

import com.example.todofragment.bean.ToDoThing;

import java.util.Comparator;

public class ToDoThingComparator implements Comparator<ToDoThing> {
    @Override
    public int compare(ToDoThing o1, ToDoThing o2) {
        if (!o1.getThingFinish() && o2.getThingFinish()) {
            return -1;
        } else if (o1.getThingFinish() && !o2.getThingFinish()) {
            return 1;
        }
        return 0;
    }
}
