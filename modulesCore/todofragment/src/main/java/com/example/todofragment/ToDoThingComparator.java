package com.example.todofragment;

import com.example.todofragment.bean.GetToDoThings;
import com.example.todofragment.bean.ToDoThing;

import java.util.Comparator;

public class ToDoThingComparator implements Comparator<GetToDoThings.GetToDothingMessage> {
    @Override
    public int compare(GetToDoThings.GetToDothingMessage o1, GetToDoThings.GetToDothingMessage o2) {
        String status1 = o1.getStatus();
        String status2 = o2.getStatus();
        if ("pending".equals(status1) && "completed".equals(status2)) {
            return -1;
        }
        else if ("completed".equals(status1) && "pending".equals(status2)) {
            return 1;
        }
        return 0;
    }
}
