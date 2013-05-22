package com.joshlong.spring.walkingtour.android.async;


import android.app.Activity;
import android.util.Log;

public class ActivityThreadLocalHolder {

    private final static ThreadLocal<Activity> activityThreadLocal = new ThreadLocal<Activity>();
    private final static String tag = ActivityThreadLocalHolder.class.getName();

    public static void registerCurrentActivity(Activity activity) {
        if (null == activity) {
            activityThreadLocal.remove();
        } else {
            activityThreadLocal.set(activity);
        }
        Log.d(tag, "registering the current Activity.");
    }

    public static <T extends Activity> T currentActivity() {
        return (T) activityThreadLocal.get();
    }
}
