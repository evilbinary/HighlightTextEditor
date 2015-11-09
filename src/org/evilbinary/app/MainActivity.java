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

package org.evilbinary.app;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipException;

import org.evilbinary.highliter.HighlightEditText;
import org.evilbinary.highliter.R;
import org.evilbinary.highliter.R.id;
import org.evilbinary.highliter.R.menu;
import org.evilbinary.highliter.R.string;
import org.evilbinary.managers.Configure;
import org.evilbinary.managers.ConfigureManager;
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

	
	private ConfigureManager configureManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// setContentView(R.layout.activity_main);

		configureManager=new ConfigureManager(this);	
		configureManager.exractDefaultConfigure();
		Configure conf=configureManager.getDefaultConfigure();
		conf.mTheme="solarized-light";
		conf.mLanguage="python";
		
		final LinearLayout linearLayout = new LinearLayout(this);
		HighlightEditText hi = new HighlightEditText(this,conf);
	
		try {
			String text = FileUtil.readFromAssetsFile(this, "test.html");
			String textcode=FileUtil.readFromAssetsFile(this, "fib.py");
			hi.setSource(textcode);
			//hi.setHtml(text);
			
			linearLayout.setOrientation(LinearLayout.VERTICAL);
			linearLayout.addView(hi);
			
			setContentView(linearLayout);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
