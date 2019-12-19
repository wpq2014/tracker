package com.wpq.tracker;

import java.io.IOException;

/**
 * @author wpq
 * @version 1.0
 */
public class Main {

    public static void main(String[] args) throws IOException {
        // 1.读取excel数据存入json
        ExcelReader.run();
        // 2.读取json构造Tracker
        TrackGenerator.run();
    }
}
