package com.xhc.util;

import java.io.ByteArrayOutputStream;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import mitc.sample.loadingscreen1.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.res.Resources;
import android.util.Log;
import android.webkit.WebView.FindListener;

import com.xhc.model.*;

import java.io.*;


/**
 * ����Ϊ�����࣬������json�������������ӹ��ܺ���
 * @author Fan
 *
 */
public class  Util {
	
	/**
	 * �����豸���������ļ��л�ȡ�豸�Ļ�����Ϣ
	 * @param device_name �豸��
	 * @param jsontext ����json����
	 * @return �豸����
	 */
	public static Device getProjects(String device_name, String jsontext) {

		Device dev = null;
		// ��ȡjson�ļ�����ȡ����Project
		if (jsontext == null) {
			return null;
		}
		JSONArray arr = null;
		try {
			arr = new JSONArray(jsontext);
			// JSONObject objcet = new JSONObject(jsontext);
			// arr = objcet.getJSONArray("Types");
			for (int i = 0; i < arr.length(); i++) {
				JSONObject temp = (JSONObject) arr.get(i);

				JSONArray childs = temp.getJSONArray("device");
				for (int j = 0; j < childs.length(); j++) {
					JSONObject child = (JSONObject) childs.get(j);

					System.out.println(child.getString("device_name"));

					if (child.getString("device_name").equals(device_name)) {

						// ����Device

						String icon_url = child.getString("icon");
						String image_url = child.getString("image");
						int period = child.getInt("period");
						JSONArray value_arr = child.getJSONArray("values");
						JSONArray unit = child.getJSONArray("unit");
						JSONArray decimal = child.getJSONArray("decimal");
						ArrayList<Value_Config> configs = new ArrayList<Value_Config>();
						for (int z = 0; z < value_arr.length(); z++) {

							Value_Config value = new Value_Config(
									value_arr.getString(z), unit.getString(z),
									decimal.getInt(z));
							configs.add(value);
						}
						dev = new Device(device_name, icon_url, image_url,period, configs);
						return dev;
					}
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return dev;
	}

	
	/**
	 * ��ȡĳ�������������豸������
	 * @param type ������
	 * @param jsontext ����json����
	 * @return ���е��豸����
	 */
	public static ArrayList<String> getProjectnames(String type, String jsontext) {
		ArrayList<String> list = new ArrayList<String>();
		// ��ȡjson�ļ�����ȡ����Project
		if (jsontext == null) {
			return null;
		}
		JSONArray arr = null;
		try {
			arr = new JSONArray(jsontext);
			for (int i = 0; i < arr.length(); i++) {
				JSONObject temp = (JSONObject) arr.get(i);
				if (temp.getString("cat_name").equals(type)) {
					JSONArray childs = temp.getJSONArray("device");
					for (int j = 0; j < childs.length(); j++) {
						JSONObject child = (JSONObject) childs.get(j);
						list.add(child.getString("device_name"));
					}
				}

			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}

	
	/**
	 * ������json�����л�ȡ���е�������
	 * @param jsontext ����json����
	 * @return ���е�������
	 */
	public static ArrayList<String> getTypes(String jsontext) {

		ArrayList<String> list = new ArrayList<String>();
		if (jsontext == null) {
			return null;
		}
		JSONArray arr = null;
		try {
			arr = new JSONArray(jsontext);
			for (int i = 0; i < arr.length(); i++) {
				JSONObject temp = (JSONObject) arr.get(i);
				list.add(temp.getString("cat_name"));
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * ��ָ��URL�л�ȡjson����
	 * @param urltext ��������
	 * @return ��ȡ����json����
	 */
	public static String getJsonText(String urltext) {

	  Log.e("MyTag", urltext);
		HttpURLConnection conn = null;
		try {
			URL url = new URL(urltext);
			conn = (HttpURLConnection) url.openConnection(); // ������
			conn.setConnectTimeout(5 * 1000); // �������ӳ�ʱ
			conn.setRequestMethod("GET"); // ��get��ʽ��������
			if (conn.getResponseCode() != 200)
			{
				return null;
			}
			//	throw new RuntimeException("����urlʧ��");
			InputStream is = conn.getInputStream(); // �õ����緵�ص�������
			String result = readData(is, "utf-8"); // ������ȡ�ַ���
			conn.disconnect();
			conn = null;
			return convert(result);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {

			if (conn != null) {
				conn.disconnect();
			}
		}

		return null;

	}

	/**
	 * �����ض��豸��������json
	 * @param dev �豸����
	 * @param jsontext ����json
	 * @return ���ݶ���
	 */
	public static DataItem getValueJson(Device dev, String jsontext) {
	  
		DataItem item = new DataItem();
		try {
			JSONObject objcet = new JSONObject(jsontext);
			String dev_name = objcet.getString("device_name");
			if (!dev_name.equals(dev.getDevname())) {
				System.out.println(dev_name + ":" + dev.getDevname());
				return null;
			}
			item.dev_name = dev_name;
			String image_url = objcet.getString("image");
			item.image_url = image_url;
			JSONArray x_arr = objcet.getJSONArray("����X");
			JSONArray y_arr = objcet.getJSONArray("����Y");
			JSONArray addtime_arr = objcet.getJSONArray("ʱ��");
			for (int i = 0; i < x_arr.length(); i++) {
				item.addtime.add(addtime_arr.getString(i));
				item.X.add(x_arr.getDouble(i));
				item.Y.add(y_arr.getDouble(i));
			}
			ArrayList<String> values_name = dev.getValues();
			for (String valuename : values_name) {
				JSONArray value_arr = objcet.getJSONArray(valuename);
				ArrayList<Double> values = new ArrayList<Double>();
				for (int i = 0; i < value_arr.length(); i++) {
					values.add(value_arr.getDouble(i));
				}
				item.data_map.put(valuename, values);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return item;
	}
    /**
     * 
     * @param inSream
     * @param charsetName
     * @return
     * @throws Exception
     */
	public static String readData(InputStream inSream, String charsetName)
			throws Exception {
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = -1;
		while ((len = inSream.read(buffer)) != -1) {
			outStream.write(buffer, 0, len);
		}
		byte[] data = outStream.toByteArray();
		outStream.close();
		inSream.close();
		return new String(data, charsetName);
	}

	public boolean isOpenNetwork() {

		return true;
	}

	/**
	 * 
	 * @param inputStream
	 * @return
	 */
	public static String getString(InputStream inputStream) {
		InputStreamReader inputStreamReader = null;
		try {
			inputStreamReader = new InputStreamReader(inputStream, "gbk");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		BufferedReader reader = new BufferedReader(inputStreamReader);
		StringBuffer sb = new StringBuffer("");
		String line;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line);
				sb.append("\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return sb.toString();
	}
    /**
     * 
     * @param unicodeStr
     * @return
     */
	public static String unicode2String(String unicodeStr) {
		StringBuffer sb = new StringBuffer();
		String str[] = unicodeStr.toUpperCase().split("U");
		for (int i = 0; i < str.length; i++) {
			if (str[i].equals(""))
				continue;
			char c = (char) Integer.parseInt(str[i].trim(), 16);
			sb.append(c);
		}
		return sb.toString();
	}
	/**
	 * 
	 * @param ascii
	 * @return
	 */
	public static String ascii2native(String ascii) {
		int n = ascii.length() / 6;
		StringBuilder sb = new StringBuilder(n);
		for (int i = 0, j = 2; i < n; i++, j += 6) {
			String code = ascii.substring(j, j + 4);
			char ch = (char) Integer.parseInt(code, 16);
			sb.append(ch);
		}
		return sb.toString();
	}

	/**
	 * �������ı�������
	 * @param str
	 * @return
	 */
	public static String convert(String str) {
	    Log.e("MyTag", "dd");
		String sb = str;
		Pattern pattern_Name = Pattern.compile("(\\\\\\w{5})");
		Matcher matcher = pattern_Name.matcher(str);
		int start = 0;
		while (matcher.find(start)) {
			String z = matcher.group();
			sb = sb.replace(z, ascii2native(z));
			start = matcher.end();
		}
		return sb.replace("\\/", "/");
	}

	public static void main(String[] args) {
	
	}
}
