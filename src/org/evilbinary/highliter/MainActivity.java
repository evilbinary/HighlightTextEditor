/* Copyright (C) 2015 evilbinary.
 * rootdebug@163.com
This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.evilbinary.highliter;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipException;

import org.evilbinary.utils.DirUtil;
import org.evilbinary.utils.FileUtil;
import org.evilbinary.utils.Logger;
import org.evilbinary.utils.ZipUtil;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	private Handler mHandler;
	private final static int SHOW_TIPS = 1;
	private String systemDir;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// setContentView(R.layout.activity_main);

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

		systemDir = DirUtil.getFilesDir(this);

		new Thread() {
			public void run() {
				Looper.prepare();
				extractZipFile("plugins");
				extractZipFile("themes");
				extractZipFile("langDefs");
				Looper.loop();
			}
		}.start();

		final LinearLayout linearLayout = new LinearLayout(this);
		HighlightEditText hi = new HighlightEditText(this);
	
		try {
			String text = FileUtil.readFromAssetsFile(this, "test.html");
			text="<span class=\"hl kwb\">int</span>";
			hi.setHtml(text);
			linearLayout.setOrientation(LinearLayout.VERTICAL);
			linearLayout.addView(hi);
			
			setContentView(linearLayout);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void extractZipFile(String name) {
		String zipFileDir = systemDir + "/" + name;
		File file = new File(zipFileDir);
		if (!file.exists()) {
			try {
				String src = name + ".zip";
				InputStream is = this.getAssets().open(src);
				sendTips(getString(R.string.extract) + " " + name);
				extract(src, systemDir);
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
