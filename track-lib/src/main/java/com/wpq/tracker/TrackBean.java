package com.wpq.tracker;

import java.io.Serializable;
import java.util.Map;

/**
 * @author wpq
 * @version 1.0
 */
public class TrackBean implements Serializable {

    public String comment; // 类注释
    public Map<String, String> params;

    @Override
    public String toString() {
        return "TrackBean{" +
                "comment='" + comment + '\'' +
                ", params=" + params +
                '}';
    }
}
