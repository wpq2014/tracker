package com.wpq.tracker;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.TypeReference;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import com.wpq.tracker.expose.TrackUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;

import javax.lang.model.element.Modifier;

/**
 * https://github.com/square/javapoet
 *
 * @author wupuquan
 * @version 1.0
 * @since 2019-12-14 16:29
 */
public class TrackGenerator {

    // 工程路径：/Users/wpq/Documents/android/tracker
    private static String PROJECT_PATH = System.getProperty("user.dir");
    // 当前Class包名：com.wpq.tracker
    private static String CURRENT_PACKAGE_NAME = "";
    private static final String POET_TRACK = "Tracker";

    private TrackGenerator() {
    }

    public static void run() throws IOException {
        new TrackGenerator().start();
    }

    private void start() throws IOException, JSONException {
        System.out.println("当前工程路径：" + PROJECT_PATH);
        Package pkg = TrackGenerator.class.getPackage();
        if (pkg != null) {
            CURRENT_PACKAGE_NAME = pkg.getName();
        }
        System.out.println("当前Class包名：" + CURRENT_PACKAGE_NAME);
        // 1.检查json
        Map<String, TrackBean> data = parseData();
        // 2.生成类
        createTrackClass(data);
    }

    private Map<String, TrackBean> parseData() throws FileNotFoundException {
        Map<String, TrackBean> map = JSON.parseObject(readJsonFile(), new TypeReference<Map<String, TrackBean>>() {
        });
        for (Map.Entry<String, TrackBean> entry : map.entrySet()) {
            String key = entry.getKey();
            TrackBean value = entry.getValue();
            // 校验key
            if (StringUtil.isNullOrEmpty(key)) {
                throw new JSONException(("检测到事件key为空！value为：" + value));
            }
            // 校验value
            if (value == null) {
                throw new JSONException(("检测到 事件" + key + " 的value为空或数据格式不正确！"));
            } else {
                // 校验注释
                if (StringUtil.isNullOrEmpty(value.comment)) {
                    System.err.println("警告：检测到 事件" + key + " 的注释为空或格式不正确，请检查该事件是否需要注释！");
                }
                if (value.params == null) {
                    System.err.println("警告：检测到 事件" + key + " 的参数为空或格式不正确，请检查该事件是否需要参数！");
                } else {
                    for (Map.Entry<String, String> param : value.params.entrySet()) {
                        if (StringUtil.isNullOrEmpty(param.getKey())) {
                            throw new JSONException(("检测到 事件" + key + " 的参数key为空或数据格式不正确！注释为：" + param.getValue()));
                        }
                    }
                }
            }
        }
        return map;
    }

    private void createTrackClass(Map<String, TrackBean> legalData) throws IOException {
        ClassName string = ClassName.get("java.lang", "String");
        ClassName object = ClassName.get("java.lang", "Object");
        ClassName map = ClassName.get("java.util", "Map");
        ClassName hashMap = ClassName.get("java.util", "HashMap");
        TypeName mapType = ParameterizedTypeName.get(map, string, object);

        // 设置类属性
        TypeSpec.Builder classBuilder = TypeSpec.classBuilder(POET_TRACK)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL);

        // 遍历JSONObject
        for (Map.Entry<String, TrackBean> entry : legalData.entrySet()) {
            String key = entry.getKey().trim();
            TrackBean value = entry.getValue();

            String method = "event" + key;
            if (value.params != null && value.params.size() > 0) {
                // 方法1
                MethodSpec.Builder builder1 = MethodSpec.methodBuilder(method)
                        .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                        .returns(void.class);
                // 方法注释
                builder1.addJavadoc(value.comment + "\n\n");
                builder1.addStatement("$T params = new $T<>()", mapType, hashMap);
                for (Map.Entry<String, String> param : value.params.entrySet()) {
                    String paramKey = param.getKey().trim();
                    String paramName = paramKey;
                    // 神策内置关键字段以$开头
                    if (paramKey.startsWith("$")) {
                        paramName = paramKey.substring(1);
                    }
                    builder1.addParameter(Object.class, paramName);
                    builder1.addStatement("params.put($S, " + paramName + ")", paramKey);
                    // 参数注释
                    builder1.addJavadoc("@param " + paramName + " " + param.getValue() + "\n");
                }
                builder1.addStatement("$T.track(\"" + method + "\", params)", TrackUtil.class);
                MethodSpec methodSpec = builder1.build();
                classBuilder.addMethod(methodSpec);
            } else {
                // 方法2，参数为空构造默认方法
                MethodSpec methodSpec2 = MethodSpec.methodBuilder(method)
                        .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                        .returns(void.class)
                        .addJavadoc(value.comment + "\n") // 方法注释
                        .addParameter(mapType, "params") // 默认参数
                        .addStatement("$T.track(\"" + method + "\", params)", TrackUtil.class)
                        .build();
                classBuilder.addMethod(methodSpec2);
            }
        }
        TypeSpec poetTrack = classBuilder.build();
        JavaFile javaFile = JavaFile.builder(CURRENT_PACKAGE_NAME + ".expose", poetTrack)
                .build();
//        javaFile.writeTo(System.out); // 控制台输出
        File sourceFile = new File(PROJECT_PATH + "/track-lib/src/main/java");
        if (!sourceFile.exists()) {
            //noinspection ResultOfMethodCallIgnored
            sourceFile.createNewFile();
        }
        javaFile.writeTo(sourceFile);
    }

    /**
     * 读取json文件
     *
     * @return json数据
     * @throws FileNotFoundException 在当前类中手动调用，路径错误直接抛异常
     */
    private String readJsonFile() throws FileNotFoundException {
        String filePath = PROJECT_PATH + "/track-lib/src/main/assets/track.json";
        return IOUtil.readFile(filePath);
    }
}
