package com.mylove.module_base.utils;


import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Random;
import java.util.zip.GZIPInputStream;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.text.format.DateFormat;
import android.util.Log;

public class SystemUtils {
	private static final String TAG = "SystemUtility";

	private static int sArmArchitecture = -1;

	
	public static String getProp(String key){
		String value = null;
		//if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                Class c = Class.forName("android.os.SystemProperties");
                Method m = c.getDeclaredMethod("get", String.class);
                m.setAccessible(true);
                value = (String) m.invoke(null, key);
            } catch (Throwable e) {
            	
            }
       // }
		return value;
	}
	
	public static int getArmArchitecture() {
		if (sArmArchitecture != -1)
			return sArmArchitecture;
		try {
			InputStream is = new FileInputStream("/proc/cpuinfo");
			InputStreamReader ir = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(ir);
			try {
				String name = "CPU architecture";
				while (true) {
					String line = br.readLine();
					String[] pair = line.split(":");
					if (pair.length != 2)
						continue;
					String key = pair[0].trim();
					String val = pair[1].trim();
					if (key.compareToIgnoreCase(name) == 0) {
						String n = val.substring(0, 1);
						sArmArchitecture = Integer.parseInt(n);
						break;
					}
				}
			} finally {
				br.close();
				ir.close();
				is.close();
				if (sArmArchitecture == -1)
					sArmArchitecture = 6;
			}
		} catch (Exception e) {
			sArmArchitecture = 6;
		}
		return sArmArchitecture;
	}

	public static int getSDKVersionCode() {
		// TODO: fix this
		return Build.VERSION.SDK_INT;
	}

	public static String getExternalStoragePath() {
		boolean exists = Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED);
		if (exists)
			return Environment.getExternalStorageDirectory().getAbsolutePath();
		else
			return "/";
	}

	@SuppressWarnings("rawtypes")
	public static Object realloc(Object oldArray, int newSize) {
		int oldSize = java.lang.reflect.Array.getLength(oldArray);
		Class elementType = oldArray.getClass().getComponentType();
		Object newArray = java.lang.reflect.Array.newInstance(elementType,
				newSize);
		int preserveLength = Math.min(oldSize, newSize);
		if (preserveLength > 0)
			System.arraycopy(oldArray, 0, newArray, 0, preserveLength);
		return newArray;
	}

	public static String getTimeString(int msec) {
		if (msec < 0) {
			return String.format("--:--:--");
		}
		int total = msec / 1000;
		int hour = total / 3600;
		total = total % 3600;
		int minute = total / 60;
		int second = total % 60;
		return String.format("%02d:%02d:%02d", hour, minute, second);
	}

	protected static String sTempPath = "/data/local/tmp";

	public static String getTempPath() {
		return sTempPath;
	}


	public static int getStringHash(String s) {
		byte[] target = s.getBytes();
		int hash = 1315423911;
		for (int i = 0; i < target.length; i++) {
			byte val = target[i];
			hash ^= ((hash << 5) + val + (hash >> 2));
		}
		hash &= 0x7fffffff;
		return hash;
	}

	public static boolean isNetworkAvailable(Context context) {  
	    ConnectivityManager connectivity = (ConnectivityManager) context  
	            .getSystemService(Context.CONNECTIVITY_SERVICE);  
	    if (connectivity == null) {  
	    	Log.e(TAG, "getSystemService rend null");  
	    } else { 
	        NetworkInfo[] info = connectivity.getAllNetworkInfo();  
	        if (info != null) { 
	            for (int i = 0; i < info.length; i++) {  
	                if (info[i].getState() == NetworkInfo.State.CONNECTED) {  
	                    return true;  
	                }  
	            }  
	        }  
	    }  
	    return false;  
	}
	
    /**
     * get current data connection type name, like:Mobile/WIFI/OFFLINE
     * 
     * @param context
     * @return
     */
    public static String getConnectTypeName(Context context) {
            if (!isNetworkAvailable(context)) {
                    return "OFFLINE";
            }
            ConnectivityManager manager = (ConnectivityManager) context
                            .getSystemService(Activity.CONNECTIVITY_SERVICE);
            NetworkInfo info = manager.getActiveNetworkInfo();
            if (info != null) {
                    return info.getTypeName();
            } else {
                    return "OFFLINE";
            }
    }
    
    public static String getLocalMacAddress(Context ctx) {  
    	/*
        WifiManager wifi = (WifiManager)ctx.getSystemService(Context.WIFI_SERVICE);  
        WifiInfo info = wifi.getConnectionInfo();  
        return info.getMacAddress();  
        */
    	String text = "00:11:22:33:44:55";
		try {
			InputStream is = new FileInputStream("/sys/class/net/eth0/address");
			InputStreamReader ir = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(ir);
			try {
				text = br.readLine();
					
			} finally {
				br.close();
				ir.close();
				is.close();
			}
		} catch (Exception e) {
            Log.e(TAG, "Open File Error!" + e.toString());  
		}
        
        return text.trim();
    }
    
    public static String getCharAndNumr(int length)     
	{     
	    String val = "";
	    Random random = new Random();     
	    for(int i = 0; i < length; i++)     
	    {     
	        String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num"; // 输出字母还是数字        
	        if("char".equalsIgnoreCase(charOrNum)) // 字符串     
	        {     
	            int choice = random.nextInt(2) % 2 == 0 ? 65 : 97; //取得大写字母还是小写字母     
	            val += (char) (choice + random.nextInt(26));     
	        }     
	        else if("num".equalsIgnoreCase(charOrNum)) // 数字     
	        {     
	            val += String.valueOf(random.nextInt(10));     
	        }     
	    }     
	    return val;     
	} 
    
    public static String getTime(Context context) {
		final Calendar c = Calendar.getInstance();
		int hour = c.get(Calendar.HOUR_OF_DAY);
		int minute = c.get(Calendar.MINUTE);
		if (!DateFormat.is24HourFormat(context) && hour > 12) {
			hour = hour - 12;
		}
		String time = "";
		if (hour >= 10) {
			time += Integer.toString(hour);
		} else {
			time += "0" + Integer.toString(hour);
		}
		time += ":";

		if (minute >= 10) {
			time += Integer.toString(minute);
		} else {
			time += "0" + Integer.toString(minute);
		}
		return time;
	}

	public static String getWeek(){
		SimpleDateFormat formatter = new SimpleDateFormat("EEEE");
		Date curDate = new Date(System.currentTimeMillis());
		return formatter.format(curDate);
	}

	public static String getStatu(){
		final Calendar c = Calendar.getInstance();
		String ampmValues;
		if (c.get(Calendar.AM_PM) == 0) {
			ampmValues = "AM";
		} else {
			ampmValues = "PM";
		}
		return ampmValues;
	}

	public static String getIPAddress(Context context) {
		NetworkInfo info = ((ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
		if (info != null && info.isConnected()) {
			if (info.getType() == ConnectivityManager.TYPE_ETHERNET) {//当前使用2G/3G/4G网络
				try {
					//Enumeration<NetworkInterface> en=NetworkInterface.getNetworkInterfaces();
					for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
						NetworkInterface intf = en.nextElement();
						for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
							InetAddress inetAddress = enumIpAddr.nextElement();
							if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
								return inetAddress.getHostAddress();
							}
						}
					}
				} catch (SocketException e) {
					e.printStackTrace();
				}

			} else if (info.getType() == ConnectivityManager.TYPE_WIFI) {//当前使用无线网络
				WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
				WifiInfo wifiInfo = wifiManager.getConnectionInfo();
				String ipAddress = intIP2StringIP(wifiInfo.getIpAddress());//得到IPV4地址
				return ipAddress;
			}
		} else {
			//当前无网络连接,请在设置中打开网络
		}
		return null;
	}

	public static String intIP2StringIP(int ip) {
		return (ip & 0xFF) + "." +
				((ip >> 8) & 0xFF) + "." +
				((ip >> 16) & 0xFF) + "." +
				(ip >> 24 & 0xFF);
	}

	public static PackageInfo getAPKInfo(Context ctx, String apk, boolean type) {// 获取apk的信息
		PackageManager pm = ctx.getPackageManager();
		PackageInfo pakinfo = null;
		if (type) {
			try {
				pakinfo = pm.getPackageInfo(apk, PackageManager.GET_ACTIVITIES);
			} catch (PackageManager.NameNotFoundException e) {
				// TODO Auto-generated catch block
			}
		} else {
			pakinfo = pm.getPackageArchiveInfo(apk,
					PackageManager.GET_UNINSTALLED_PACKAGES);
		}
		return pakinfo;
	}

	public static boolean openApk(Context context, String pkg) {
		try {
			PackageManager pm = context.getPackageManager();
			Intent intent = pm.getLaunchIntentForPackage(pkg);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(intent);
			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			return false;
		}
	}

	public static boolean isLaunch(Context context,String pkg){
		if (pkg == null) return false;

		PackageManager pManager = context.getPackageManager();
		Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
		mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
		List<ResolveInfo> infos = pManager.queryIntentActivities(mainIntent,0);

		for (ResolveInfo info : infos){
			if (pkg.equals(info.activityInfo.packageName)){
				return true;
			}
		}
		return false;
	}

	/**
	 * 查询手机内非系统应用
	 * @param context
	 * @return
	 *  type  0 为系统应用
	 *  	  1 为安装应用
	 *  	  其它为所有应用
	 *
	 *  add
	 */
	public static List<PackageInfo> getAllApps(Context context,int type,boolean add) {

		PackageManager pManager = context.getPackageManager();
		List<PackageInfo> sysApps = new ArrayList<PackageInfo>();
		List<PackageInfo> userApps = new ArrayList<PackageInfo>();
		PackageInfo defaultPak = null;
		//获取手机内所有应用
		List<PackageInfo> paklist = pManager.getInstalledPackages(0);

		for (int i = 0; i < paklist.size(); i++) {
			PackageInfo pak = (PackageInfo) paklist.get(i);

			if(pak.applicationInfo.packageName.equals(context.getPackageName())){
				defaultPak = pak;
				continue;
			}

			if(!isLaunch(context,pak.applicationInfo.packageName)){
				continue;
			}

			if ((pak.applicationInfo.flags & pak.applicationInfo.FLAG_SYSTEM) > 0) {  //判断是否为系统预装的应用程序
				// customs applications
				sysApps.add(pak);
			}else if ((pak.applicationInfo.flags & pak.applicationInfo.FLAG_SYSTEM) <= 0) { //判断是否为非系统预装的应用程序
				// customs applications
				userApps.add(pak);
			}
		}
		switch (type){
			case 0:
				if (defaultPak != null && add)sysApps.add(defaultPak);
				return sysApps;
			case 1:
				if (defaultPak != null && add)userApps.add(defaultPak);
				return userApps;
			default:
				List<PackageInfo> apps = new ArrayList<PackageInfo>();
				apps.addAll(userApps);
				apps.addAll(sysApps);
				if (defaultPak != null && add)apps.add(defaultPak);
				return apps;
		}
	}
}
