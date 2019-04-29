package com.traffic.pd.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Environment;
import android.os.Looper;
import android.util.Log;

import com.traffic.pd.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;


/**
 * UncaughtException处理�??当程序发生Uncaught异常的时�??有该类来接管程序,并记录发送错误报�??
 * �??��在Application中注册，为了要在程序启动器就监控整个程序�??
 */
public class CrashHandler implements UncaughtExceptionHandler {
	public static final String TAG = "CrashHandler";
	//系统默认的UncaughtException处理�??
	private UncaughtExceptionHandler mDefaultHandler;
	//CrashHandler实例
	private static CrashHandler instance;
	//程序的Context对象
	private Context mContext;
	//用来存储设备信息和异常信�??
	private Map<String, String> infos = new HashMap<String, String>();
	/** 保证只有�??��CrashHandler实例 */
	private CrashHandler() {
	}
	/** 获取CrashHandler实例 ,单例模式 */
	public static CrashHandler getInstance() {
		if (instance == null)
			instance = new CrashHandler();
		return instance;
	}
	/**
	 * 初始�??
	 */
	public void init(Context context) {
		mContext = context;
		//获取系统默认的UncaughtException处理�??
		mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
		//设置该CrashHandler为程序的默认处理�??   
		Thread.setDefaultUncaughtExceptionHandler(this);
	}
	/**
	 * 当UncaughtException发生时会转入该函数来处理
	 */
	@Override
	public void uncaughtException(Thread thread, Throwable ex) {
		if (!handleException(ex) && mDefaultHandler != null) {
			//如果用户没有处理则让系统默认的异常处理器来处�??
			mDefaultHandler.uncaughtException(thread, ex);
		} else {
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				Log.e(TAG, "error : ", e);
			}
			//�??��程序    
			android.os.Process.killProcess(android.os.Process.myPid());
			System.exit(1);
		}
	}
	/**
	 * 自定义错误处�??收集错误信息 发�?错误报告等操作均在此完成.
	 * @param ex
	 * @return true:如果处理了该异常信息;否则返回false.
	 */
	private boolean handleException(Throwable ex) {
		if (ex == null) {
			return false;
		}
		//收集设备参数信息
		collectDeviceInfo(mContext);
		//使用Toast来显示异常信�??
		new Thread() {
			@Override
			public void run() {
				Looper.prepare();
				//Toast.makeText(mContext, "很抱�??程序出现异常,即将�??��.", Toast.LENGTH_SHORT).show();
				Looper.loop();
			}
		}.start();
		//保存日志文件     
		saveCatchInfo2File(ex);
		return true;
	}
	/**
	 * 收集设备参数信息
	 * @param ctx
	 */
	public void collectDeviceInfo(Context ctx) {
		try {
			PackageManager pm = ctx.getPackageManager();
			PackageInfo pi = pm.getPackageInfo(ctx.getPackageName(), PackageManager.GET_ACTIVITIES);
			if (pi != null) {
				String versionName = pi.versionName == null ? "null" : pi.versionName;
				String versionCode = pi.versionCode + "";
				infos.put("versionName", versionName);
				infos.put("versionCode", versionCode);
			}
		} catch (NameNotFoundException e) {
			Log.e(TAG, "an error occured when collect package info", e);
		}
		Field[] fields = Build.class.getDeclaredFields();
		for (Field field : fields) {
			try {
				field.setAccessible(true);
				infos.put(field.getName(), field.get(null).toString());
				Log.d(TAG, field.getName() + " : " + field.get(null));
			} catch (Exception e) {
				Log.e(TAG, "an error occured when collect crash info", e);
			}
		}
	}
	private String getFilePath() {
		String file_dir = "";
		boolean isSDCardExist = Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
		boolean isRootDirExist = Environment.getExternalStorageDirectory().exists();
		if (isSDCardExist && isRootDirExist) {
			file_dir = mContext.getExternalCacheDir() + "/"+ mContext.getString(R.string.app_dir)+"/crashlog/";
		} else {
			// MyApplication.getInstance().getFilesDir()返回的路劲为/data/data/PACKAGE_NAME/files，其中的包就是我们建立的主Activity�??��的包
			file_dir = mContext.getFilesDir().getAbsolutePath() + "/"+ mContext.getString(R.string.app_dir)+"/crashlog/";
		}
		return file_dir;
	}
	/**
	 * 保存错误信息到文件中
	 * @param ex
	 * @return 返回文件名称,便于将文件传送到服务�??
	 */
	private String saveCatchInfo2File(Throwable ex) {
		StringBuffer sb = new StringBuffer();
		for (Map.Entry<String, String> entry : infos.entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue();
			sb.append(key + "=" + value + "\n");
		}
		Writer writer = new StringWriter();
		PrintWriter printWriter = new PrintWriter(writer);
		ex.printStackTrace(printWriter);
		Throwable cause = ex.getCause();
		while (cause != null) {
			cause.printStackTrace(printWriter);
			cause = cause.getCause();
		}
		printWriter.close();
		String result = writer.toString();
		sb.append(result);
		try {
			String fileName = "crash_error.log";
			String file_dir = getFilePath();
			//			if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			File dir = new File(file_dir);
			if (!dir.exists()) {
				dir.mkdirs();
			}
			File file = new File(file_dir + fileName);
			if (!file.exists()) {
				file.createNewFile();
			}
			FileOutputStream fos = new FileOutputStream(file);
			fos.write(sb.toString().getBytes());
			//发�?给开发人�??
			sendCrashLog2PM(file_dir + fileName);
			Log.i(TAG, sb.toString());
			fos.close();
			//			}
			return fileName;
		} catch (Exception e) {
			Log.e(TAG, "an error occured while writing file...", e);
		}
		return null;
	}
	/**
	 * 将捕获的导致崩溃的错误信息发送给�??��人员
	 * 目前只将log日志保存在sdcard 和输出到LogCat中，并未发�?给后台�?
	 */
	private void sendCrashLog2PM(String fileName) {
		//		if (!new File(fileName).exists()) {
		//			Toast.makeText(mContext, "日志文件不存在！", Toast.LENGTH_SHORT).show();
		//			return;
		//		}
		//		FileInputStream fis = null;
		//		BufferedReader reader = null;
		//		String s = null;
		//		try {
		//			fis = new FileInputStream(fileName);
		//			reader = new BufferedReader(new InputStreamReader(fis, "GBK"));
		//			while (true) {
		//				s = reader.readLine();
		//				if (s == null)
		//					break;
		//				//由于目前尚未确定以何种方式发送，�??��先打出log日志�??
		//				Log.i("info", s.toString());
		//			}
		//		} catch (FileNotFoundException e) {
		//			e.printStackTrace();
		//		} catch (IOException e) {
		//			e.printStackTrace();
		//		} finally { // 关闭�??
		//			try {
		//				reader.close();
		//				fis.close();
		//			} catch (IOException e) {
		//				e.printStackTrace();
		//			}
		//		}
	}
}