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

package org.evilbinary.utils;

import java.io.File;

import android.content.Context;
import android.os.Environment;

public class DirUtil {
	
	public static String filesDir;
	public static String systemDir;
	
	public static void getEnvironmentDirectories() {
		Logger.i("getRootDirectory(): "
				+ Environment.getRootDirectory().toString());
		Logger.i("getDataDirectory(): "
				+ Environment.getDataDirectory().toString());
		Logger.i("getDownloadCacheDirectory(): "
				+ Environment.getDownloadCacheDirectory().toString());
		Logger.i("getExternalStorageDirectory(): "
				+ Environment.getExternalStorageDirectory().toString());

		Logger.i("getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES): "
				+ Environment.getExternalStoragePublicDirectory(
						Environment.DIRECTORY_PICTURES).toString());
		// Logger.i(
		// "isExternalStorageEmulated(): "
		// + Environment.isExternalStorageEmulated());
		//
		// Logger.i(
		// "isExternalStorageRemovable(): "
		// + Environment.isExternalStorageRemovable());
	}

	public static void getApplicationDirectories(Context context) {
		
		Logger.i("context.getFilesDir(): " + context.getFilesDir().toString());
		Logger.i("context.getCacheDir(): " + context.getCacheDir().toString());

		// methods below will return null if the permissions denied
		Logger.i(

		"context.getExternalFilesDir(Environment.DIRECTORY_MOVIES): "
				+ context.getExternalFilesDir(Environment.DIRECTORY_MOVIES));

		Logger.i("context.getExternalCacheDir(): "
				+ context.getExternalCacheDir());
	}
	public static String getFilesDir(Context context){
		return context.getFilesDir().toString();
	}
	public static String getDir(Context context){
		return context.getFilesDir().getParent().toString();
	}
	public static String getCacheDir(Context context){
		return context.getCacheDir().toString();
	}
	public static String getExternalFilesDir(Context context){
		return context.getExternalFilesDir(Environment.DIRECTORY_MOVIES).toString();
	}
	public static String getExternalCacheDir(Context context){
		return context.getExternalCacheDir().toString();
	}
	public static File getExternalStorageDirectory(){
		return Environment.getExternalStorageDirectory();
	}
	
	 

}
