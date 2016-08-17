package com.lody.virtual.client.core;

import android.app.Activity;
import android.app.ActivityThread;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.ServiceInfo;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Looper;
import android.os.Process;
import android.os.RemoteException;
import android.text.TextUtils;

import com.lody.virtual.client.env.Constants;
import com.lody.virtual.client.env.VirtualRuntime;
import com.lody.virtual.client.fixer.ContextFixer;
import com.lody.virtual.client.local.VActivityManager;
import com.lody.virtual.client.local.VPackageManager;
import com.lody.virtual.client.service.ServiceManagerNative;
import com.lody.virtual.helper.ExtraConstants;
import com.lody.virtual.helper.compat.ActivityThreadCompat;
import com.lody.virtual.helper.compat.BundleCompat;
import com.lody.virtual.helper.proto.AppSetting;
import com.lody.virtual.helper.proto.InstallResult;
import com.lody.virtual.os.VUserHandle;
import com.lody.virtual.service.IAppManager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dalvik.system.DexFile;

/**
 * @author Lody
 * @version 2.2
 */
public final class VirtualCore {

	private static VirtualCore gCore = new VirtualCore();
	/**
	 * 纯净无钩子的PackageManager
	 */
	private PackageManager unHookPackageManager;
	/**
	 * Host包名
	 */
	private String pkgName;
	/**
	 * 在API 16以前, ActivityThread通过ThreadLocal管理, 非主线程调用为空, 故在此保存实例.
	 */
	private ActivityThread mainThread;
	private Context context;

	private Object hostBindData;
	/**
	 * 主进程名
	 */
	private String mainProcessName;
	/**
	 * 当前进程名
	 */
	private String processName;
	private ProcessType processType;
	private IAppManager mService;
	private boolean isStartUp;
	private PackageInfo hostPkgInfo;
	private Map<ComponentName, ActivityInfo> activityInfoCache = new HashMap<ComponentName, ActivityInfo>();
	private final int myUid = Process.myUid();
	private int systemPid;


	private VirtualCore() {

	}

	public int myUid() {
		return myUid;
	}

	public int myUserId() {
		return VUserHandle.getUserId(myUid);
	}

	public static Object getHostBindData() {
		return getCore().hostBindData;
	}

	public static VirtualCore getCore() {
		return gCore;
	}

	public static PackageManager getPM() {
		return getCore().getPackageManager();
	}

	public static ActivityThread mainThread() {
		return getCore().mainThread;
	}

	public static String getPermissionBroadcast() {
		return "com.lody.virtual.permission.VIRTUAL_BROADCAST";
	}

	public static ComponentName getOriginComponentName(String action) {
		String brc = String.format("%s.BRC_", getCore().getHostPkg());
		if (action != null && action.startsWith(brc)) {
			String comStr = action.replaceFirst(brc, "");
			comStr = comStr.replace("_", "/");
			return ComponentName.unflattenFromString(comStr);
		}
		return null;
	}

	public static String getReceiverAction(String packageName, String className) {
		if (className != null && className.startsWith(".")) {
			className = packageName + className;
		}
		String extAction = packageName + "_" + className;
		return String.format("%s.BRC_%s", getCore().getHostPkg(), extAction);
	}

	public int[] getGids() {
		return hostPkgInfo.gids;
	}

	public Context getContext() {
		return context;
	}

	public PackageManager getPackageManager() {
		return context.getPackageManager();
	}

	public String getHostPkg() {
		return pkgName;
	}

	public PackageManager getUnHookPackageManager() {
		return unHookPackageManager;
	}


	public void startup(Context context) throws Throwable {
		if (!isStartUp) {
			if (Looper.myLooper() != Looper.getMainLooper()) {
				throw new IllegalStateException("VirtualCore.startup() must called in main thread.");
			}
			this.context = context;
			mainThread = ActivityThread.currentActivityThread();
			hostBindData = ActivityThreadCompat.getBoundApplication(mainThread);
			unHookPackageManager = context.getPackageManager();
			hostPkgInfo = unHookPackageManager.getPackageInfo(context.getPackageName(), PackageManager.GET_PROVIDERS);
			// Host包名
			pkgName = context.getApplicationInfo().packageName;
			// 主进程名
			mainProcessName = context.getApplicationInfo().processName;
			// 当前进程名
			processName = mainThread.getProcessName();
			if (processName.equals(mainProcessName)) {
				processType = ProcessType.Main;
			} else if (processName.endsWith(Constants.SERVER_PROCESS_NAME)) {
				processType = ProcessType.Server;
			} else if (VActivityManager.getInstance().isAppProcess(processName)) {
				processType = ProcessType.VAppClient;
			} else {
				processType = ProcessType.CHILD;
			}
			if (isVAppProcess()) {
				systemPid = VActivityManager.getInstance().getSystemPid();
			}
			PatchManager patchManager = PatchManager.getInstance();
			patchManager.injectAll();
			patchManager.checkEnv();
			ContextFixer.fixContext(context);
			isStartUp = true;
		}
	}

	public IAppManager getService() {
		if (mService == null) {
			synchronized (this) {
				if (mService == null) {
					mService = IAppManager.Stub
							.asInterface(ServiceManagerNative.getService(ServiceManagerNative.APP_MANAGER));
				}
			}
		}
		return mService;
	}

	/**
	 * @return 当前进程是否为Virtual App进程
	 */
	public boolean isVAppProcess() {
		return ProcessType.VAppClient == processType;
	}

	/**
	 * @return 当前进程是否为主进程
	 */
	public boolean isMainProcess() {
		return ProcessType.Main == processType;
	}

	/**
	 * @return 当前进程是否为子进程
	 */
	public boolean isChildProcess() {
		return ProcessType.CHILD == processType;
	}

	/**
	 * @return 当前进程是否为服务进程
	 */
	public boolean isServiceProcess() {
		return ProcessType.Server == processType;
	}

	/**
	 * @return 当前进程名
	 */
	public String getProcessName() {
		return processName;
	}

	/**
	 * @return 主进程名
	 */
	public String getMainProcessName() {
		return mainProcessName;
	}

	public void preOpt(String pkg) throws Exception {
		AppSetting info = findApp(pkg);
		if (info != null && !info.dependSystem) {
			DexFile.loadDex(info.apkPath, info.getOdexFile().getPath(), 0).close();
		}
	}

	public InstallResult installApp(String apkPath, int flags) {
		try {
			return getService().installApp(apkPath, flags);
		} catch (RemoteException e) {
			return VirtualRuntime.crash(e);
		}
	}

	public boolean isAppInstalled(String pkg) {
		try {
			return getService().isAppInstalled(pkg);
		} catch (RemoteException e) {
			return VirtualRuntime.crash(e);
		}
	}

	public Intent getLaunchIntent(String packageName, int userId) {
		VPackageManager pm = VPackageManager.getInstance();
		Intent intentToResolve = new Intent(Intent.ACTION_MAIN);
		intentToResolve.addCategory(Intent.CATEGORY_INFO);
		intentToResolve.setPackage(packageName);
		List<ResolveInfo> ris = pm.queryIntentActivities(intentToResolve, intentToResolve.resolveType(context), 0, userId);

		// Otherwise, try to find a main launcher activity.
		if (ris == null || ris.size() <= 0) {
			// reuse the intent instance
			intentToResolve.removeCategory(Intent.CATEGORY_INFO);
			intentToResolve.addCategory(Intent.CATEGORY_LAUNCHER);
			intentToResolve.setPackage(packageName);
			ris = pm.queryIntentActivities(intentToResolve, intentToResolve.resolveType(context), 0, userId);
		}
		if (ris == null || ris.size() <= 0) {
			return null;
		}
		Intent intent = new Intent(intentToResolve);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setClassName(ris.get(0).activityInfo.packageName,
				ris.get(0).activityInfo.name);
		intent.putExtra(ExtraConstants.EXTRA_TARGET_USER, userId);
		return intent;
	}


	public void addLoadingPage(Intent intent, Activity activity) {
		if (activity != null) {
			addLoadingPage(intent, activity.getActivityToken());
		}
	}

	public void addLoadingPage(Intent intent, IBinder token) {
		if (token != null) {
			Bundle bundle = new Bundle();
			BundleCompat.putBinder(bundle, ExtraConstants.EXTRA_BINDER, token);
			intent.putExtra(ExtraConstants.EXTRA_SENDER, bundle);
		}
	}

	public AppSetting findApp(String pkg) {
		try {
			return getService().findAppInfo(pkg);
		} catch (RemoteException e) {
			return VirtualRuntime.crash(e);
		}
	}

	public int getAppCount() {
		try {
			return getService().getAppCount();
		} catch (RemoteException e) {
			return VirtualRuntime.crash(e);
		}
	}

	public boolean isStartup() {
		return isStartUp;
	}

	public boolean uninstallApp(String pkgName) {
		try {
			return getService().uninstallApp(pkgName);
		} catch (RemoteException e) {
			// Ignore
		}
		return false;
	}

	public Resources getResources(String pkg) {
		AppSetting appSetting = findApp(pkg);
		if (appSetting != null) {
			AssetManager assets = new AssetManager();
			assets.addAssetPath(appSetting.apkPath);
			Resources hostRes = context.getResources();
			return new Resources(assets, hostRes.getDisplayMetrics(), hostRes.getConfiguration());
		}
		return null;
	}

	public boolean isHostPackageName(String pkgName) {
		return TextUtils.equals(pkgName, context.getPackageName());
	}

	public synchronized ActivityInfo resolveActivityInfo(Intent intent, int userId) {
		ActivityInfo activityInfo = null;
		if (intent.getComponent() == null) {
			ResolveInfo resolveInfo = VPackageManager.getInstance().resolveIntent(intent, intent.getType(), 0, 0);
			if (resolveInfo != null && resolveInfo.activityInfo != null) {
				activityInfo = resolveInfo.activityInfo;
				intent.setClassName(activityInfo.packageName, activityInfo.name);
				activityInfoCache.put(intent.getComponent(), activityInfo);
			}
		} else {
			activityInfo = resolveActivityInfo(intent.getComponent(), userId);
		}
		return activityInfo;
	}

	public synchronized ActivityInfo resolveActivityInfo(ComponentName componentName, int userId) {
		ActivityInfo activityInfo = activityInfoCache.get(componentName);
		if (activityInfo == null) {
			activityInfo = VPackageManager.getInstance().getActivityInfo(componentName, 0, userId);
			if (activityInfo != null) {
				activityInfoCache.put(componentName, activityInfo);
			}
		}
		return activityInfo;
	}

	public ServiceInfo resolveServiceInfo(Intent intent, int userId) {
		ServiceInfo serviceInfo = null;
		ResolveInfo resolveInfo = VPackageManager.getInstance().resolveService(intent, intent.getType(), 0, userId);
		if (resolveInfo != null) {
			serviceInfo = resolveInfo.serviceInfo;
		}
		return serviceInfo;
	}

	public void killApp(String pkg, int userId) {
		VActivityManager.getInstance().killAppByPkg(pkg, userId);
	}

	public void killAllApps() {
		VActivityManager.getInstance().killAllApps();
	}

	public List<AppSetting> getAllApps() {
		try {
			return getService().getAllApps();
		} catch (RemoteException e) {
			return VirtualRuntime.crash(e);
		}
	}

	public void preloadAllApps() {
		try {
			getService().preloadAllApps();
		} catch (RemoteException e) {
			// Ignore
		}
	}

	public boolean isOutsideInstalled(String packageName) {
		try {
			return unHookPackageManager.getApplicationInfo(packageName, 0) != null;
		} catch (PackageManager.NameNotFoundException e) {
			// Ignore
		}
		return false;
	}

	public int getSystemPid() {
		return systemPid;
	}

	/**
	 * 进程类型
	 */
	enum ProcessType {
		/**
		 * Server process
		 */
		Server,
		/**
		 * Virtual app process
		 */
		VAppClient,
		/**
		 * Main process
		 */
		Main,
		/**
		 * Child process
		 */
		CHILD
	}
}
