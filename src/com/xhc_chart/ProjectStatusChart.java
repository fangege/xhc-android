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
 * 该类表示了一个曲线图，该曲线图只能渲染一条曲线
 * @author Fan
 *
 */
public class ProjectStatusChart extends AbstractDemoChart {

  List<Date[]> dates = new ArrayList<Date[]>(); // X对应的时间
  List<double[]> values = new ArrayList<double[]>(); // Y对于的值
  private String name = ""; // 曲线图的名称
  private String desc = ""; // 曲线图的描述
  private String x_label = "";
  private String y_label = "";
  private double y_max; // 最大的Y
  private double y_min; // 最小的Y
  private int N; // 数据集大小
  private XYMultipleSeriesDataset mdateset; // 数据集
  private XYMultipleSeriesRenderer renderer; // 渲染器
  private String[] titles = new String[] { "" }; // 曲线的标题
  private Context context;

  /**
   * 设置X轴时间
   * 
   * @param dts 时间信息（X轴）
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
   * 设置Y轴数值
   * 
   * @param vs 值（Y轴）
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
   * 得到该曲线图对于的视图
   * 
   * @param context 使用该视图的Activity
   * @return 曲线图构成的视图
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
    // 构造数据集
    mdateset = buildDateDataset(titles, dates, values);
    if (values.size() == 0) {
      length = 0;
    } else {
      length = values.get(0).length;
    }
    /*
     * 定义与设置渲染器
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
    renderer.setAxesColor( context.getResources().getColor(R.color.axis_color)); // 设置XY轴颜色
    renderer.setXLabelsColor(context.getResources().getColor(R.color.xlabel_color));
    renderer.setYLabelsColor(0,context.getResources().getColor(R.color.ylabel_color));
    renderer.setXLabelsAngle(-45); // 设置X轴标签倾斜角度(clockwise degree)
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
     * 构造图表
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
      // 无数据
      setChartSettings(renderer, name, x_label, y_label, 0, 0, y_min, y_max, context.getResources()
          .getColor(R.color.axis_color), context.getResources().getColor(R.color.axis_color));
    }

  }

  /**
   * 设置曲线图的标题
   * @param title 标题
   */
  public void setTitle(String title) {
    titles = new String[] { title };
  }

  /**
   * 数据改变后，重新构建数据集，APP每次接收到新的数据后都会调用该方法
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
