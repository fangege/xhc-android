package com.xhc.model;

import java.util.ArrayList;
import java.util.List;

import org.achartengine.GraphicalView;

/**
 * �������������һ���豸���������豸���������ԣ������豸���ƣ��豸ͼ�����ӣ��豸ͼƬ���ӣ��豸�����������ڣ�
 * �豸���������豸�Զ����ֵ������
 * @author Fan
 *
 */
public class Device {

  private String devname;
  private String icon_url;
  private String image_url;
  private int period;

  private String description;

  private ArrayList<Value_Config> valuesname = new ArrayList<Value_Config>();

  public Device(String devname, String icon_url, String image_url, int period, ArrayList<Value_Config> valuesname) {
    super();
    this.devname = devname;
    this.icon_url = icon_url;
    this.image_url = image_url;
    this.period = period;
    this.valuesname = valuesname;

    description = "���豸�ܹ����:\r\n";
    for (int i = 0; i < this.valuesname.size(); i++) {

      Value_Config config = valuesname.get(i);
      description += "  " + config.getValue_name();
      if (!config.getUnit().equals("null")) {
        description += "(" + config.getUnit() + ")";
      }
      description += "\r\n";
    }

    description += "�ͻ���ÿ��";
    description += period;
    description += "��ɼ�һ����������";
  }

  public String getImage_url() {
    return image_url;
  }

  public void setImage_url(String image_url) {
    this.image_url = image_url;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getDevname() {
    return devname;
  }

  public void setDevname(String devname) {
    this.devname = devname;
  }

  public String getIcon_url() {
    return icon_url;
  }

  public void setIcon_url(String icon_url) {
    this.icon_url = icon_url;
  }

  public int getPeriod() {
    return period;
  }

  public void setPeriod(int period) {
    this.period = period;
  }

  public ArrayList<Value_Config> getValuesname() {
    return valuesname;
  }

  public void setValuesname(ArrayList<Value_Config> valuesname) {
    this.valuesname = valuesname;
  }

  public ArrayList<String> getValues() {
    // TODO Auto-generated method stub

    ArrayList<String> list = new ArrayList<String>();
    for (int i = 0; i < this.valuesname.size(); i++) {
      list.add(valuesname.get(i).getValue_name());
    }
    return list;
  }

}