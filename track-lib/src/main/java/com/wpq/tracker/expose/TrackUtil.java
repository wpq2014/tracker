package com.wpq.tracker.expose;

import android.util.Log;

import com.sensorsdata.analytics.android.sdk.SensorsDataAPI;

import org.json.JSONObject;

import java.util.Map;

/**
 * @author wpq
 * @version 1.0
 */
public final class TrackUtil {

    private static final String TAG = "TrackUtil";

    private TrackUtil() {
        throw new AssertionError();
    }

//    public static void main(String[] args) {
//        Map<String, Object> map = new HashMap<>();
//        map.put("cid", 1);
//        map.put("channel", "huawei");
//        TrackUtil.track("key", map);
//    }

    // 不要public，只在lib中调用
    static void track(String eventName, Map<String, Object> properties) {
        JSONObject jsonObject = null;
        try {
            // 这里new JSONObject(properties) 在java main函数中调用会抛异常，
            // 在android程序中调用没问题，暂时还没查具体原因
            jsonObject = new JSONObject(properties);
            Log.d(TAG, jsonObject.toString());
        } catch (Throwable e) {
            e.printStackTrace();
        }
        SensorsDataAPI.sharedInstance().track(eventName, jsonObject);
    }
}
