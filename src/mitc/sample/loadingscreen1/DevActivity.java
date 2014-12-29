package mitc.sample.loadingscreen1;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import mitc.sample.loadingscreen1.R;

import org.achartengine.GraphicalView;

import yanbin.imagelazyload.ImageDownloader;
import yanbin.imagelazyload.OnImageDownload;

import com.xhc.model.DataItem;
import com.xhc.model.Device;
import com.xhc.util.Util;
import com.xhc_chart.ProjectStatusChart;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;

import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.format.DateFormat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup.LayoutParams;

import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;

/**
 * 该类为设备数据显示界面，分为三个选项卡，第一个选项卡为设备的图片信息以及设备的基本介绍。第二个选项卡为以列表形式显示设备的所有数据。第三个选项卡为以曲线图形式显示设备所有数据。在该类中，每隔一定时间向服务器请求设备数据信息并更新列表和曲线图，当设备的图片信息更新时，该类会更新设备的图片信息。
 * @author Fan
 *
 */
@TargetApi(Build.VERSION_CODES.ECLAIR)
public class DevActivity extends Activity {

  private TabHost tabHost = null;
  private TextView descriptview;

  private String jsonText;
  private String valuejson = null;
  private Device dev;

  private Timer timer = null;

  private LinearLayout chartContainer;
  private GraphicalView mchart;
  private ProjectStatusChart c;

  private SimpleAdapter adapter = null;
  private List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
  private String valuename = null;

  private ArrayList<String> time_list = new ArrayList<String>();
  private ArrayList<Double> value_list = new ArrayList<Double>();
  private ConnectChangeReciver myReceiver = null;

  
  private ImageDownloader mDownloader;
  private ImageView imageview;
  private int display_width;
  

  /**
   * 从接受到的数据json中提取指定值域的值
   * @param DataJson 数据json
   * @param valuename 值域名称
   */
  private void getData(String DataJson, String valuename) {  
    if (DataJson == null) {
      return;
    }
    DataItem item = Util.getValueJson(dev, DataJson);
    if (item != null) {
      dev.setImage_url(item.image_url);
      list.clear();
      ArrayList<Double> x_list = item.getX();
      ArrayList<Double> y_list = item.getY();
      time_list = item.getAddtime();
      value_list = item.getData_map().get(valuename);
      try {
        ajustValue(time_list, value_list);
      } catch (ParseException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
      list.clear();
      for (int i = 0; i < x_list.size(); i++) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("value", value_list.get(i));
        map.put("index", "(" + x_list.get(i) + "," + y_list.get(i) + ")");
        map.put("datetime", time_list.get(i));
        list.add(map);
      }
    } else {
      // 如果网络有问题，提示用户
    }

    
  }

  /**
   * 在发现设备图片修改后，更新设备图片
   */
  private void updateImage() {
    final String url = getString(R.string.ip)+ dev.getImage_url();
    imageview.setTag(url);
  
    if (mDownloader == null) {
      mDownloader = new ImageDownloader();
      mDownloader.setDisplay_width(display_width);
    }
    mDownloader.imageDownload(url, imageview, "/yanbin",
        DevActivity.this, new OnImageDownload() {
            @TargetApi(19)
			@SuppressLint("NewApi")
			@Override
            public void onDownloadSucc(Bitmap bitmap, String c_url,
                    ImageView mimageView) {
              
                if (imageview != null) { 
                  double ratio = (double) bitmap.getHeight() / (double) bitmap.getWidth();
                  int display_height = (int) ((double) display_width * ratio);                
                  LayoutParams para;  
                  para = imageview.getLayoutParams();  
                  para.width = display_width;
                  para.height = display_height;
                  imageview.setLayoutParams(para);
                  imageview.setImageBitmap(bitmap);
                  imageview.setTag("");
                }
            }
        });
  }

  /**
   * 排序输入图表的值
   * 
   * @param dts 日期
   * @param values 值域的值
   * @throws ParseException 
   */
  private static void ajustValue(ArrayList<String> dts, ArrayList<Double> values)
      throws ParseException {
    for (int i = 0; i < dts.size(); i++) {
      for (int j = 0; j < dts.size(); j++) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date di = dateFormat.parse(dts.get(i));
        Date dj = dateFormat.parse(dts.get(j));
        if (di.compareTo(dj) < 0) {
          String temp = dts.get(i);
          dts.set(i, dts.get(j));
          dts.set(j, temp);
          double t = values.get(i);
          values.set(i, values.get(j));
          values.set(j, t);
        }
      }
    }
  }

  protected void onCreate(Bundle savedInstanceState) {
    // TODO Auto-generated method stub
    super.onCreate(savedInstanceState);
    setContentView(R.layout.info);
    /*
     * 得到界面的配置json
     */
    Intent intent = getIntent();
    String devname = intent.getStringExtra("Dev");
    jsonText = intent.getStringExtra("Json");
    dev = Util.getProjects(devname, jsonText);
    if (dev.getValuesname().size() != 0) {
      valuename = dev.getValues().get(0);
    }
    /*
     * 初始化主要界面
     */
    adapter = new SimpleAdapter(this, list, R.layout.item, new String[] { "value", "index",
        "datetime" }, new int[] { R.id.value, R.id.index, R.id.datetime });
    ListView listview = (ListView) findViewById(R.id.data_list);
    listview.setAdapter(adapter);
    Spinner spinner = (Spinner) findViewById(R.id.select_value);
    ArrayAdapter<String> aa = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,
        dev.getValues());
    aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    spinner.setAdapter(aa);
    spinner.setOnItemSelectedListener(new SpinnerSelectedListener());
    spinner.setVisibility(View.VISIBLE);
    Spinner spinner2 = (Spinner) findViewById(R.id.select_value_3);
    spinner2.setAdapter(aa);
    spinner2.setOnItemSelectedListener(new SpinnerSelectedListener2());
    spinner2.setVisibility(View.VISIBLE);
    ((TextView) findViewById(R.id.info_layout01_devname)).setText(dev.getDevname());
    ((TextView) findViewById(R.id.info_layout02_devname)).setText(dev.getDevname());
    ((TextView) findViewById(R.id.info_layout03_devname)).setText(dev.getDevname());
    descriptview = ((TextView) findViewById(R.id.dev_descript));
    descriptview.setText(dev.getDescription());
    tabHost = (TabHost) findViewById(R.id.tabhost_info);
    tabHost.setup();

    tabHost.addTab(tabHost.newTabSpec(getString(R.string.first_taghost))
        .setContent(R.id.info_include01).setIndicator(getString(R.string.first_taghost)));
    tabHost.addTab(tabHost.newTabSpec(getString(R.string.second_taghost))
        .setContent(R.id.info_include02).setIndicator(getString(R.string.second_taghost)));
    tabHost.addTab(tabHost.newTabSpec(getString(R.string.third_taghost))
        .setContent(R.id.info_include03).setIndicator(getString(R.string.third_taghost)));
    /*
     * 设置图表
     */
    c = new ProjectStatusChart();
    c.setTitle("");
    c.setDates(null);
    c.setValues(null);
    chartContainer = (LinearLayout) findViewById(R.id.chart_container);
    mchart = c.getView(this);
    chartContainer.addView(mchart);

    
    
    DisplayMetrics metrics = new DisplayMetrics();
    getWindowManager().getDefaultDisplay().getMetrics(metrics);
    display_width = metrics.widthPixels;
    imageview = (ImageView) findViewById(R.id.imageview);
    imageview.setImageResource(R.drawable.water);
    updateImage();

    /*
     * 开启数据请求定时器
     */
    timer = new Timer();
    timer.schedule(task, 2000, 1000 * dev.getPeriod());
    this.registerReceiver();
  }

  /*
   * 
   */
  TimerTask task = new TimerTask() {
    @Override
    public void run() {
      // TODO Auto-generated method stub
      String url = null;
      try {
        url = getString(R.string.ip) + "/get_datas.php?type=data&dev=" + URLEncoder.encode(dev.getDevname(), "UTF-8");
      } catch (UnsupportedEncodingException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
      valuejson = Util.getJsonText(url);
      getData(valuejson, valuename);
      Message message = new Message();
      message.what = 1;
      handler.sendMessage(message);
    }
  };
  /*
   * 在主线程中完成界面更新
   */
  Handler handler = new Handler() {
    public void handleMessage(Message msg) {
      super.handleMessage(msg);
      if (msg.what == 1) {
        adapter.notifyDataSetChanged();
        notifyChart();
        updateImage();
      }
    }
  };

  @Override
  protected void onDestroy() {
    this.unregisterReceiver();
    if (timer != null) {
      timer.cancel();
      timer.purge();
      timer = null;
    }
    super.onDestroy();

  }

  /**
   * 更新图表的显示
   */
  private void notifyChart() {
    ArrayList<String> dts = new ArrayList<String>();
    for (String dt : time_list) {
      dts.add(dt.replace("-", "/"));
    }
    // 调整顺序
    try {
      ajustValue(dts, value_list);
    } catch (ParseException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    c.setDates(dts);
    c.setValues(value_list);
    c.RebuildDateset();
    c.setTitle(valuename);
    mchart.invalidate();
  }

  private void registerReceiver() {
    IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
    myReceiver = new ConnectChangeReciver();
    this.registerReceiver(myReceiver, filter);
  }

  private void unregisterReceiver() {
    this.unregisterReceiver(myReceiver);
  }

  class SpinnerSelectedListener implements OnItemSelectedListener {
    public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
      valuename = dev.getValues().get(arg2);
      Log.e("MyTag", valuename);
      getData(valuejson, valuename);
      adapter.notifyDataSetChanged();
    }

    public void onNothingSelected(AdapterView<?> arg0) {
    }
  }

  class SpinnerSelectedListener2 implements OnItemSelectedListener {

    public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
      valuename = dev.getValues().get(arg2);
      getData(valuejson, valuename);
      c.setTitle(valuename);
      notifyChart();
    }

    public void onNothingSelected(AdapterView<?> arg0) {
    }
  }

}
