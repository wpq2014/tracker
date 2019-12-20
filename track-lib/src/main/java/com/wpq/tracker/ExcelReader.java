package com.wpq.tracker;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.io.IOException;
import java.util.Map;
import java.util.Scanner;

/**
 * @author wpq
 * @version 1.0
 */
public class ExcelReader {

    // 工程路径：/Users/wpq/Documents/android/usertrack
    private static String PROJECT_PATH = System.getProperty("user.dir");
    private String oldFilePath = PROJECT_PATH + "/track-lib/src/main/assets/track.json";
    private String newFilePath = PROJECT_PATH + "/track-lib/src/main/assets/new.json";

    private ExcelReader() {
    }

    public static void run() throws IOException {
        new ExcelReader().start();
    }

    private void start() throws IOException {
        JSONObject jsonOld = JSON.parseObject(IOUtil.readFile(oldFilePath));
        if (jsonOld == null) {
            jsonOld = new JSONObject();
        }
        JSONObject jsonNew = JSON.parseObject(IOUtil.readFile(newFilePath));
        if (jsonNew == null || jsonNew.isEmpty()) {
            System.err.println("新数据有误！请检查文件路径或内容");
            return;
        }

        Scanner scanner = new Scanner(System.in);
        try {
            for (Map.Entry<String, Object> entry : jsonNew.entrySet()) {
                String key = entry.getKey().trim();
                Object value = entry.getValue();
                if (jsonOld.containsKey(key)) {
                    String oldValue = jsonOld.getString(key);
                    System.out.println("检测到 事件" + key + " 已存在！"
                            + "\n旧数据为：" + oldValue
                            + "\n新数据为：" + value
                            + "\n是否覆盖？（y/n）");
                    if ("y".equalsIgnoreCase(scanner.next())) {
                        jsonOld.put(key, value);
                    }
                } else {
                    jsonOld.put(key, value);
                }
            }
            // 写入旧文件
            IOUtil.write(jsonOld.toJSONString(), oldFilePath);
        } finally {
            IOUtil.closeQuietly(scanner);
        }
    }

}
