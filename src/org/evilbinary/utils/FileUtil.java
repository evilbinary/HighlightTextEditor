package org.evilbinary.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


import android.content.Context;
import android.content.res.AssetManager;
import android.os.Environment;



public class FileUtil {	

	static public boolean externalStorageMounted() {
		String state = Environment.getExternalStorageState();
		return Environment.MEDIA_MOUNTED.equals(state)
				|| Environment.MEDIA_MOUNTED_READ_ONLY.equals(state);
	}

	public static int chmod(File path, int mode) throws Exception {
		Class<?> fileUtils = Class.forName("android.os.FileUtils");
		Method setPermissions = fileUtils.getMethod("setPermissions",
				String.class, int.class, int.class, int.class);
		return (Integer) setPermissions.invoke(null, path.getAbsolutePath(),
				mode, -1, -1);
	}

	public static boolean recursiveChmod(File root, int mode) throws Exception {
		boolean success = chmod(root, mode) == 0;
		for (File path : root.listFiles()) {
			if (path.isDirectory()) {
				success = recursiveChmod(path, mode);
			}
			success &= (chmod(path, mode) == 0);
		}
		return success;
	}

	public static boolean delete(File path) {
		boolean result = true;
		if (path.exists()) {
			if (path.isDirectory()) {
				for (File child : path.listFiles()) {
					result &= delete(child);
				}
				result &= path.delete(); // Delete empty directory.
			}
			if (path.isFile()) {
				result &= path.delete();
			}
			if (!result) {
				Logger.e("Delete failed;");
			}
			return result;
		} else {
			Logger.e("File does not exist.");
			return false;
		}
	}

	public static File copyFromStream(String name, InputStream input) {
		if (name == null || name.length() == 0) {
			Logger.e("No script name specified.");
			return null;
		}
		File file = new File(name);
		if (!makeDirectories(file.getParentFile(), 0755)) {
			return null;
		}
		try {
			OutputStream output = new FileOutputStream(file);
			IoUtils.copy(input, output);
		} catch (Exception e) {
			Logger.e(e);
			return null;
		}
		return file;
	}

	public static boolean makeDirectories(File directory, int mode) {
		File parent = directory;
		while (parent.getParentFile() != null && !parent.exists()) {
			parent = parent.getParentFile();
		}
		if (!directory.exists()) {
			Logger.v("Creating directory: " + directory.getName());
			if (!directory.mkdirs()) {
				Logger.e("Failed to create directory.");
				return false;
			}
		}
		try {
			recursiveChmod(parent, mode);
		} catch (Exception e) {
			Logger.e(e);
			return false;
		}
		return true;
	}

	public static File getExternalDownload() {
		try {
			Class<?> c = Class.forName("android.os.Environment");
			Method m = c.getDeclaredMethod("getExternalStoragePublicDirectory",
					String.class);
			String download = c.getDeclaredField("DIRECTORY_DOWNLOADS")
					.get(null).toString();
			return (File) m.invoke(null, download);
		} catch (Exception e) {
			return new File(Environment.getExternalStorageDirectory(),
					"Download");
		}
	}

	public static boolean rename(File file, String name) {
		return file.renameTo(new File(file.getParent(), name));
	}

	public static String readToString(File file) throws IOException {
		if (file == null || !file.exists()) {
			return null;
		}
		FileReader reader = new FileReader(file);
		StringBuilder out = new StringBuilder();
		char[] buffer = new char[1024 * 4];
		int numRead = 0;
		while ((numRead = reader.read(buffer)) > -1) {
			out.append(String.valueOf(buffer, 0, numRead));
		}
		reader.close();
		return out.toString();
	}

	public static String readFromAssetsFile(Context context, String name)
			throws IOException {
		AssetManager am = context.getAssets();
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				am.open(name)));
		String line;
		StringBuilder builder = new StringBuilder();
		while ((line = reader.readLine()) != null) {
			builder.append(line);
		}
		reader.close();
		return builder.toString();
	}

	public static String ReadFile(InputStream fis, String encoding) {
		BufferedReader br;
		StringBuilder b = new StringBuilder();
		String line;
		String sp = System.getProperty("line.separator");

		try {
			br = new BufferedReader(new InputStreamReader(fis, encoding));
			try {
				while ((line = br.readLine()) != null) {
					b.append(line).append(sp);
				}
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		return b.toString();
	}

	public static String ReadFile(File filename, String encoding) {
		try {
			FileInputStream fis = new FileInputStream(filename);
			return ReadFile(fis, encoding);
		} catch (FileNotFoundException e) {
			return "";
		}
	}
	public static String ReadFile(String filename) {
		return ReadFile(filename,"UTF-8");
	}

	public static String ReadFile(String fileName, String encoding) {
		File file = new File(fileName);
		return ReadFile(file, encoding);
	}

	public static String getExt(String path) {
//		System.out.println("getext:"+path);
		int lastIndex = path.lastIndexOf(".");
		if (lastIndex == -1)
			return null;
//		System.out.println("getext end:"+path.substring(lastIndex + 1).trim().toLowerCase());

		return path.substring(lastIndex + 1).trim().toLowerCase();
		
	}

	public static String getEncoding(String path) {
		String encoding = "";
		// encoding= CharsetDetector.getEncoding(path).trim().toUpperCase();
		if (encoding == null || "".equals(encoding)) {
			encoding = "UTF-8";
		} else if ("GB18030".equals(encoding)) {
			encoding = "GBK";
		}
		return encoding;
	}
	
	
	
	
	

}
