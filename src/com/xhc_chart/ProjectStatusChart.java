/**
 * Copyright (C) 2009 - 2013 SC 4ViewSoft SRL
 *  
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *  
 *      http://www.apache.org/licenses/LICENSE-2.0
 *  
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.xhc_chart;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.TimeSeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.SimpleSeriesRenderer;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import mitc.sample.loadingscreen1.R;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint.Align;

/**
 * �����ʾ��һ������ͼ��������ͼֻ����Ⱦһ������
 * @author Fan
 *
 */
public class ProjectStatusChart extends AbstractDemoChart {

  List<Date[]> dates = new ArrayList<Date[]>(); // X��Ӧ��ʱ��
  List<double[]> values = new ArrayList<double[]>(); // Y���ڵ�ֵ
  private String name = ""; // ����ͼ������
  private String desc = ""; // ����ͼ������
  private String x_label = "";
  private String y_label = "";
  private double y_max; // ����Y
  private double y_min; // ��С��Y
  private int N; // ���ݼ���С
  private XYMultipleSeriesDataset mdateset; // ���ݼ�
  private XYMultipleSeriesRenderer renderer; // ��Ⱦ��
  private String[] titles = new String[] { "" }; // ���ߵı���
  private Context context;

  /**
   * ����X��ʱ��
   * 
   * @param dts ʱ����Ϣ��X�ᣩ
   */
  public void setDates(ArrayList<String> dts) {
    if (dts == null) {
      dts = new ArrayList<String>();
    }
    N = dts.size();
    dates.clear();
    dates.add(new Date[dts.size()]);
    for (int i = 0; i < dts.size(); i++) {
      dates.get(0)[i] = new Date(dts.get(i));
    }
  }

  /**
   * ����Y����ֵ
   * 
   * @param vs ֵ��Y�ᣩ
   */
  public void setValues(ArrayList<Double> vs) {
    if (vs == null) {
      vs = new ArrayList<Double>();
    }
    values.clear();
    values.add(new double[vs.size()]);
    for (int i = 0; i < vs.size(); i++) {

      if (i == 0) {
        y_max = y_min = vs.get(i);
      } else {
        if (y_max < vs.get(i)) {
          y_max = vs.get(i);
        }
        if (y_min > vs.get(i)) {
          y_min = vs.get(i);
        }
      }
      values.get(0)[i] = vs.get(i);
    }
  }

  /**
   * 
   * @param name
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Returns the chart name.
   * 
   * @return the chart name
   */
  public String getName() {
    return name;
  }

  /**
   * Returns the chart description.
   * 
   * @return the chart description
   */
  public String getDesc() {
    return desc;
  }

  /**
   * �õ�������ͼ���ڵ���ͼ
   * 
   * @param context ʹ�ø���ͼ��Activity
   * @return ����ͼ���ɵ���ͼ
   */

  public GraphicalView getView(Context context) {

    this.context = context;
    int length = titles.length;
    ArrayList<Double> ls = new ArrayList<Double>();
    ls.add(0.0);
    this.setValues(ls);

    ArrayList<String> dts = new ArrayList<String>();
    dts.add("2014/12/12 00:00:00");
    this.setDates(dts);
    // �������ݼ�
    mdateset = buildDateDataset(titles, dates, values);
    if (values.size() == 0) {
      length = 0;
    } else {
      length = values.get(0).length;
    }
    /*
     * ������������Ⱦ��
     */
    int[] colors = new int[] { context.getResources().getColor(R.color.serial_color) };
    PointStyle[] styles = new PointStyle[] { PointStyle.POINT };
    renderer = buildRenderer(colors, styles);
    setChartSettings(renderer, name, x_label, y_label, 0, 0, y_min, y_max, context.getResources()
        .getColor(R.color.axis_color), context.getResources().getColor(R.color.axis_color));
    renderer.setXLabels(10);
    renderer.setYLabels(10);
    renderer.setYAxisAlign(Align.LEFT, 0);
    renderer.setYLabelsAlign(Align.RIGHT, 0);
    renderer.setAxesColor( context.getResources().getColor(R.color.axis_color)); // ����XY����ɫ
    renderer.setXLabelsColor(context.getResources().getColor(R.color.xlabel_color));
    renderer.setYLabelsColor(0,context.getResources().getColor(R.color.ylabel_color));
    renderer.setXLabelsAngle(-45); // ����X���ǩ��б�Ƕ�(clockwise degree)
    renderer.setApplyBackgroundColor(true);
    renderer.setBackgroundColor(context.getResources().getColor(R.color.chart_bg));
    renderer.setMarginsColor(context.getResources().getColor(R.color.chart_bg));
    renderer.setZoomEnabled(false, false);
    renderer.setZoomEnabled(false);
    renderer.setXRoundedLabels(false);
    length = renderer.getSeriesRendererCount();
    for (int i = 0; i < length; i++) {
      SimpleSeriesRenderer seriesRenderer = renderer.getSeriesRendererAt(i);
      seriesRenderer.setDisplayChartValues(true);
    }

    /*
     * ����ͼ��
     */
    GraphicalView mchart = ChartFactory.getTimeChartView(context, mdateset, renderer,
        "yyyy-MM-dd HH:mm:ss");

    return mchart;
  }

  private boolean hasDatas() {
    if (titles.length != 0 && dates.size() > 0 && dates.get(0).length > 0) {
      return true;
    }
    return false;
  }

  protected void setRenderer() {

    if (hasDatas()) {
      setChartSettings(renderer, name, x_label, y_label, dates.get(0)[0].getTime(),
          dates.get(0)[N - 1].getTime(), y_min, y_max,
          context.getResources().getColor(R.color.axis_color),
          context.getResources().getColor(R.color.axis_color));
    } else {
      // ������
      setChartSettings(renderer, name, x_label, y_label, 0, 0, y_min, y_max, context.getResources()
          .getColor(R.color.axis_color), context.getResources().getColor(R.color.axis_color));
    }

  }

  /**
   * ��������ͼ�ı���
   * @param title ����
   */
  public void setTitle(String title) {
    titles = new String[] { title };
  }

  /**
   * ���ݸı�����¹������ݼ���APPÿ�ν��յ��µ����ݺ󶼻���ø÷���
   */
  public void RebuildDateset() {
    setRenderer();
    int count = mdateset.getSeriesCount();
    for (int i = 0; i < count; i++) {
      mdateset.removeSeries(i);
    }

    int length = titles.length;
    for (int i = 0; i < length; i++) {
      TimeSeries series = new TimeSeries(titles[i]);
      Date[] xV = dates.get(i);
      double[] yV = values.get(i);
      int seriesLength = xV.length;
      for (int k = 0; k < seriesLength; k++) {
        series.add(xV[k], yV[k]);
      }
      mdateset.addSeries(series);
    }
  }

  @Override
  public Intent execute(Context context) {
    // TODO Auto-generated method stub
    return null;
  }

}
