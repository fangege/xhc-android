package mitc.sample.loadingscreen1;

import java.util.ArrayList;




import yanbin.imagelazyload.ImageDownloader;
import yanbin.imagelazyload.OnImageDownload;

import com.xhc.util.Util;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

/**
 * ����Ϊ�豸�б���棬�����������Ϣ�л�ȡ�����������豸������ȡ�豸���ڵ�ͼ�깹���б���ʾ���û�����б��е�ĳ���豸����ת�����豸��������ʾ���档
 * @author Fan
 * 
 */
public class DeviceList extends Activity {
	ListView mListView;
	ImageDownloader mDownloader;
	MyListAdapter myListAdapter;

	String jsonText = null;
	String type = null;
	ArrayList<String> devs = new ArrayList<String>();
	

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.devlist);

		Intent intent = getIntent();
		type = intent.getStringExtra("Type");
		jsonText = intent.getStringExtra("Json");
		devs = Util.getProjectnames(type, jsonText);

		mListView = (ListView) findViewById(R.id.dev_list);
		myListAdapter = new MyListAdapter();
		mListView.setAdapter(myListAdapter);

		mListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				String devname = devs.get(arg2);
				Intent intent = new Intent(DeviceList.this, DevActivity.class);
				intent.putExtra("Dev", devname);
				intent.putExtra("Json", jsonText);
				startActivity(intent);
			}

		});

	}

	/**
	 * 
	 * @author Fan
	 *
	 */
	private class MyListAdapter extends BaseAdapter {
		private ViewHolder mHolder;

		@Override
		public int getCount() {
			return devs.size();
		}

		@Override
		public Object getItem(int position) {
			return devs.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			
			if (convertView == null) {
				convertView = getLayoutInflater().inflate(R.layout.single_data,
						null);
				mHolder = new ViewHolder();
				mHolder.mImageView = (ImageView) convertView
						.findViewById(R.id.image_view);
				mHolder.mTextView = (TextView) convertView
						.findViewById(R.id.text_view);
				convertView.setTag(mHolder);
			} else {
				mHolder = (ViewHolder) convertView.getTag();
			}
			
			final String url = getString(R.string.ip)+Util.getProjects(devs.get(position), jsonText).getIcon_url();
			Log.e("MyTag", url);
			mHolder.mTextView.setText(devs.get(position));
			mHolder.mImageView.setTag(url);

			if (mDownloader == null) {
				mDownloader = new ImageDownloader();
			}
			mHolder.mImageView.setImageResource(R.drawable.ic_launcher);
			mDownloader.imageDownload(url, mHolder.mImageView, "/yanbin",
					DeviceList.this, new OnImageDownload() {

						
						@Override
						public void onDownloadSucc(Bitmap bitmap, String c_url,
								ImageView mimageView) {
							ImageView imageView = (ImageView) mListView
									.findViewWithTag(c_url);
							if (imageView != null) {
								imageView.setImageBitmap(bitmap);
								imageView.setTag("");
							}
						}
					});
			return convertView;

		}
		private class ViewHolder {
			ImageView mImageView;
			TextView mTextView;
		}
	}

}
