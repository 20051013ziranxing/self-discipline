package com.example.activitymanager;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import java.util.Stack;

public class ActivityManager {
    private static ActivityManager instance;
    private final Stack<Activity> activityStack;

    private ActivityManager() {
        activityStack = new Stack<>();
    }

    public static synchronized ActivityManager getInstance() {
        if (instance == null) {
            instance = new ActivityManager();
        }
        return instance;
    }

    public void addActivity(Activity activity) {
        activityStack.add(activity);
    }

    public void removeActivity(Activity activity) {
        activityStack.remove(activity);
    }

    public Activity getCurrentActivity() {
        if (!activityStack.isEmpty()) {
            return activityStack.lastElement();
        }
        return null;
    }

    public void finishAllActivities() {
        while (!activityStack.isEmpty()) {
            Activity activity = activityStack.pop();
            if (activity != null && !activity.isFinishing()) {
                activity.finish();
            }
        }
    }

    public void finishActivity(Class<?> cls) {
        for (Activity activity : activityStack) {
            if (activity.getClass().equals(cls)) {
                activity.finish();
                break;
            }
        }
    }

    public void finishActivity(Activity activity) {
        if (activity != null && !activity.isFinishing()) {
            activity.finish();
        }
    }

    public void clearActivityStack() {
        activityStack.clear();
    }
}
