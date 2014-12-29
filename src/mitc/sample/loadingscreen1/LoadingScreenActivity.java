package mitc.sample.loadingscreen1;




import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.widget.ImageView;
import com.xhc.util.*;

/**
 * 该类为载入界面，InitialThread线程从服务器获取项目配置信息后，传递给下一个类型列表界面。
 * @author Fan
 *
 */
public class LoadingScreenActivity extends Activity {
	/** Called when the activity is first created. */
	private int count = 5;
	private int[] imgIDs = { R.id.widget0, R.id.widget1, R.id.widget2,
			R.id.widget3, R.id.widget4 };
	private long time = 4 * 1000;
	boolean isStop = false;

	public String json = null;
	
	private static final int TYPE_SELECTED = 1;
	private static final int TYPE_NO_SELECTED = 2;
	private static final int TYPE_STOP = 3;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		for (int id : imgIDs)
			((ImageView) findViewById(id))
					.setBackgroundResource(R.drawable.progress_bg_small);

		final IndexThread showThread = new IndexThread();
		final InitialThread workingThread = new InitialThread();
		showThread.start();
		workingThread.start();
	}

	public Handler myHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case TYPE_STOP:
				if(checkJson(json))
				{
					Intent intent = new Intent(LoadingScreenActivity.this,
							MainActivity.class);
					intent.putExtra("json", json);
					startActivity(intent);
					isStop = true;
				}
				else
				{
				  //add error checked
				}
				finish();
				break;
			case TYPE_SELECTED:
				((ImageView) findViewById(msg.arg1))
						.setBackgroundResource(R.drawable.progress_go_small);
				break;
			case TYPE_NO_SELECTED:
				((ImageView) findViewById(msg.arg1))
						.setBackgroundResource(R.drawable.progress_bg_small);
				break;
			}
		}
	};

	class InitialThread extends Thread {
		@Override
		public void run() {

			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			json = Util.getJsonText(getString(R.string.ip)+"/get_config.php?type=config");
			Message msg;
			msg = new Message();
			msg.what = TYPE_STOP;
			myHandler.sendMessage(msg);

		}
	}
	class IndexThread extends Thread {
		@Override
		public void run() {
			Message msg;
			while (!isStop) {
				for (int i = 0; i < count; i++) {
					msg = new Message();
					msg.what = TYPE_SELECTED;
					msg.arg1 = imgIDs[i];
					myHandler.sendMessage(msg);
					msg = new Message();
					if (i == 0) {
						msg.what = TYPE_NO_SELECTED;
						msg.arg1 = imgIDs[count - 1];
						myHandler.sendMessage(msg);
					} else {
						msg.what = TYPE_NO_SELECTED;
						msg.arg1 = imgIDs[i - 1];
						myHandler.sendMessage(msg);
					}
					SystemClock.sleep(500);
				}
			}
		}
	}
	protected boolean checkJson(String json2) {
		
		
		
		if(json2==null || json2.equals(""))
		{
			return false;
		}
//		
//		if (StringUtils.isBlank(json)) {  
//            return false;  
//        }  
		
		
        try {  
            new JSONArray(json2);
            return true;  
        } catch (Exception e) {  
           
            return false;  
        }  
	}
}