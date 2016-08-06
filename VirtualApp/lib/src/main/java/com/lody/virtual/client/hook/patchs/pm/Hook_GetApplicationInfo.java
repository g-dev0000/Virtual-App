package com.lody.virtual.client.hook.patchs.pm;

import android.content.pm.ApplicationInfo;

import com.lody.virtual.client.fixer.ComponentFixer;
import com.lody.virtual.client.hook.base.Hook;
import com.lody.virtual.client.local.LocalPackageManager;
import com.lody.virtual.helper.utils.ComponentUtils;

import java.lang.reflect.Method;

/**
 * @author Lody
 *
 */
/* package */ class Hook_GetApplicationInfo extends Hook {

	@Override
	public String getName() {
		return "getApplicationInfo";
	}

	@Override
	public Object onHook(Object who, Method method, Object... args) throws Throwable {
		String pkg = (String) args[0];
		int flags = (int) args[1];
		if (getHostPkg().equals(pkg)) {
			return method.invoke(who, args);
		}
		ApplicationInfo applicationInfo = LocalPackageManager.getInstance().getApplicationInfo(pkg, flags);
		if (applicationInfo != null) {
			ComponentFixer.fixUid(applicationInfo);
			return applicationInfo;
		}
		applicationInfo = (ApplicationInfo) method.invoke(who, args);
		if (ComponentUtils.isSystemApp(applicationInfo)) {
			return applicationInfo;
		}
		return null;

	}
}
