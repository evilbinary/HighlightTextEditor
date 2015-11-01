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

import java.io.PrintWriter;
import java.io.StringWriter;

import android.util.*;

public class Logger   {
	public static String LOG_TAG="Highlight";
	
	public static void v(String info){
		Log.v(LOG_TAG,info);
	 }
	 public static void i(String info){
		Log.i(LOG_TAG,info);
	 }
	 public static void d(String debug){
			Log.d(LOG_TAG,debug);
	 }
	
	 public static void w(String warn){
		 Log.w(LOG_TAG, warn);
	 }
	 public static void e(String error){
		 Log.e(LOG_TAG, error);
	 }
	 public static void v(String tag,String info){
		 Log.v(tag,info);
	 }
	 public static void i(String tag,String info){
			Log.i(tag,info);
		 }
	 public static void d(String tag,String debug){
			Log.d(tag,debug);
	 }
	 public static void w(String tag,String warn){
		 Log.w(tag, warn);
	 }
	 public static void e(String tag,String error){
		 Log.e(tag, error);
	 }
	 
	 public static void e(Throwable ex){
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		ex.printStackTrace(pw);
		pw.flush();   
        sw.flush();   
		Log.e(LOG_TAG, sw.toString());
	 }
	
}
