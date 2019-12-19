package com.wpq.tracker.expose;

import java.lang.Object;
import java.lang.String;
import java.util.HashMap;
import java.util.Map;

public final class Tracker {
  /**
   * 事件1
   *
   * @param name 姓名
   * @param age 年龄
   */
  public static void event1(Object name, Object age) {
    Map<String, Object> params = new HashMap<>();
    params.put("name", name);
    params.put("age", age);
    TrackUtil.track("event1", params);
  }

  /**
   * 事件2，列表页埋点需要传所有请求参数
   */
  public static void event2(Map<String, Object> params) {
    TrackUtil.track("event2", params);
  }

  /**
   * 事件3，测试神策内置字段
   *
   * @param pageTitle 神策内置字段：页面标题
   * @param pageName 神策内置字段：页面名称
   */
  public static void event3(Object pageTitle, Object pageName) {
    Map<String, Object> params = new HashMap<>();
    params.put("$pageTitle", pageTitle);
    params.put("$pageName", pageName);
    TrackUtil.track("event3", params);
  }

  /**
   * 事件4，测试替换
   *
   * @param one 老参数
   * @param two 新参数
   * @param three 新参数
   */
  public static void event4(Object one, Object two, Object three) {
    Map<String, Object> params = new HashMap<>();
    params.put("one", one);
    params.put("two", two);
    params.put("three", three);
    TrackUtil.track("event4", params);
  }

  /**
   * 事件5，新增事件
   */
  public static void event5(Map<String, Object> params) {
    TrackUtil.track("event5", params);
  }
}
