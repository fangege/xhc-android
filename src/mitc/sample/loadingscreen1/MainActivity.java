package mitc.sample.loadingscreen1;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.xhc.util.Util;

/**
 * 该类为类型列表界面，该类从配置信息中获取所有的类型以列表显示。用户点击列表中的某个类型将跳转到该类型下的设备列表界面。
 * @author Fan
 *
 */
public class MainActivity extends Activity {

  String jsonText = null;
  ArrayList<String> types = new ArrayList<String>();
  ArrayAdapter<String> aa = null;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.typelist);
    ListView mylListView = (ListView) findViewById(R.id.type_list);
    Intent intent = getIntent();
    jsonText = intent.getStringExtra("json");
    types = Util.getTypes(jsonText);
    
    aa = new ArrayAdapter<String>(this, R.layout.typeitem, R.id.type_name, types);
    
    mylListView.setAdapter(aa);
    mylListView.setOnItemClickListener(new OnItemClickListener() {

      @Override
      public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        // TODO Auto-generated method stub

        String typename = types.get(arg2);
        Intent intent = new Intent(MainActivity.this, DeviceList.class);
        intent.putExtra("Type", typename);
        intent.putExtra("Json", jsonText);
        startActivity(intent);

      }

    });
  }
}
