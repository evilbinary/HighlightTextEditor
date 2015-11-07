package org.evilbinary.managers;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.zip.ZipException;

import org.evilbinary.highliter.R;
import org.evilbinary.utils.DirUtil;
import org.evilbinary.utils.Logger;
import org.evilbinary.utils.ZipUtil;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.Toast;

public class ConfigureManager {
	private String mConfigDir;
	private Context mContext;
	private Handler mHandler;
	
	
	private HashMap<String,Configure> mConfigures;
	
	private final static int SHOW_TIPS = 1;
	private final static String CONF_DEFAULT="default";

	public ConfigureManager(Context context) {
		mConfigures=new HashMap<String,Configure>();
		mContext = context;
		mConfigDir = DirUtil.getFilesDir(context);
		mHandler = new Handler() {
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case SHOW_TIPS:
					showTips(msg.obj.toString());
					break;
				}
				super.handleMessage(msg);
			}
		};

	}
	
	public Configure getDefaultConfigure(){
		Configure conf=mConfigures.get(CONF_DEFAULT);
		if(conf==null){
			conf=new Configure(mContext);
			mConfigures.put(CONF_DEFAULT, conf);
		}
		return conf;
	}
	public Configure createConfigure(String name){
		Configure conf=new Configure(mContext);
		mConfigures.put(name, conf);
		return conf;
	}
	public Configure getConfigure(String name){
		Configure conf=mConfigures.get(name);
		return conf;
	}
	
	
	public void exractDefaultConfigure() {
		new Thread() {
			public void run() {
				Looper.prepare();
				extractZipFile("langDefs");
				extractZipFile("themes");
				extractZipFile("plugins");
				Looper.loop();
			}
		}.start();
	}
	public void exractAssertZipFile(String file) {
		extractZipFile(file);
	}
	public String getConfigureDir(){
		return mConfigDir;
	}
	public String setConfigureDir(){
		return mConfigDir;
	}
	
	
	

	private String getString(int id) {
		return mContext.getString(id);
	}

	private AssetManager getAssets() {
		return mContext.getAssets();
	}

	private Context getApplicationContext() {
		return mContext.getApplicationContext();
	}

	private void extractZipFile(String name) {
		String zipFileDir = mConfigDir + "/" + name;
		File file = new File(zipFileDir);
		if (!file.exists()) {
			try {
				String src = name + ".zip";
				InputStream is = getAssets().open(src);
				sendTips(getString(R.string.extract) + " " + name);
				extract(src, mConfigDir);
				sendTips(getString(R.string.extract_finish) + " " + name);
			} catch (Exception e) {
				Logger.e(e);
			}
		} else {
			Logger.i("Dir " + zipFileDir + " exist will not extract.");
		}
	}

	private void extract(String src, String des) {

		try {
			Logger.i("Begin extract " + src + " to " + des + ".");
			InputStream is = this.getAssets().open(src);
			ZipUtil.upZipFile(is, des);
			Logger.i("Extract " + src + " to " + des + " finished.");

		} catch (ZipException e) {
			// TODO Auto-generated catch block
			Logger.e(e);
			showTips(R.string.extract_error, e.getMessage());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Logger.e(e);
			showTips(R.string.extract_error, e.getMessage());
		} catch (Exception e) {
			Logger.e(e);
			showTips(R.string.extract_error, e.getMessage());
		} catch (Error e) {
			Logger.e(e);
			showTips(R.string.extract_error, e.getMessage());
		} catch (Throwable e) {
			Logger.e(e);
			showTips(R.string.extract_error, e.getMessage());
		}
	}

	private void showTips(String msg) {
		Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
	}

	private void showTips(int id, String msg) {
		Toast.makeText(getApplicationContext(), getString(id) + " " + msg, Toast.LENGTH_SHORT).show();
	}

	private void sendTips(String content) {
		Message msg = Message.obtain();
		msg.obj = content;
		msg.what = SHOW_TIPS;
		mHandler.sendMessage(msg);
	}

}
