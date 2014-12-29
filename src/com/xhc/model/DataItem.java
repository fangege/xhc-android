package com.xhc.model;

import java.util.*;

/**
 * 该类为数据类，表示设备每次请求到的一个数据，该数据包含了一个设备所涉及到的一组数据值，包括坐标，添加时间，已经设备自定义的值域
 * @author Fan
 *
 */
public class DataItem {

  public String dev_name;
  public String image_url;
  public ArrayList<Double> X = new ArrayList<Double>();
  public ArrayList<Double> Y = new ArrayList<Double>();
  public ArrayList<String> addtime = new ArrayList<String>();
  public HashMap<String, ArrayList<Double>> data_map = new HashMap<String, ArrayList<Double>>();

  @Override
  public String toString() {

    String str = "";

    str += dev_name;
    str += "\r\n";

    str += "X:";

    for (double b : X) {
      str += b;
      str += " ";
    }
    str += "\r\n";

    str += "Y:";

    for (double b : Y) {
      str += b;
      str += " ";
    }
    str += "\r\n";

    str += "addtime:";

    for (String b : addtime) {
      str += b;
      str += " ";
    }
    str += "\r\n";

    Iterator ict = data_map.entrySet().iterator();

    while (ict.hasNext()) {
      Map.Entry<String, ArrayList<Double>> entry = (Map.Entry<String, ArrayList<Double>>) ict
          .next();

      String key = entry.getKey();
      ArrayList<Double> value = entry.getValue();

      str += key + ":";

      for (double b : value) {
        str += b;
        str += " ";
      }
      str += "\r\n";

    }

    return str;

  }

  public String getImage_url() {
    return image_url;
  }

  public void setImage_url(String image_url) {
    this.image_url = image_url;
  }

  public String getDev_name() {
    return dev_name;
  }

  public void setDev_name(String dev_name) {
    this.dev_name = dev_name;
  }

  public ArrayList<Double> getX() {
    return X;
  }

  public void setX(ArrayList<Double> x) {
    X = x;
  }

  public ArrayList<Double> getY() {
    return Y;
  }

  public void setY(ArrayList<Double> y) {
    Y = y;
  }

  public ArrayList<String> getAddtime() {
    return addtime;
  }

  public void setAddtime(ArrayList<String> addtime) {
    this.addtime = addtime;
  }

  public HashMap<String, ArrayList<Double>> getData_map() {
    return data_map;
  }

  public void setData_map(HashMap<String, ArrayList<Double>> data_map) {
    this.data_map = data_map;
  }

}
