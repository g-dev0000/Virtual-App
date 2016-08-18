package com.lody.virtual.client.hook.patchs.am;

import com.lody.virtual.client.hook.base.Hook;
import com.lody.virtual.client.local.VActivityManager;
import com.lody.virtual.os.VUserHandle;

import java.lang.reflect.Method;

/**
 * @author Lody
 *
 *
 * @see android.app.IActivityManager#forceStopPackage(String, int)
 */
/* package */ class Hook_ForceStopPackage extends Hook {

	@Override
	public String getName() {
		return "forceStopPackage";
	}

	@Override
	public Object onHook(Object who, Method method, Object... args) throws Throwable {
		String pkg = (String) args[0];
		int userId = VUserHandle.myUserId();
		if (args.length > 1 && args[1] instanceof Integer) {
			userId = (int) args[1];
		}
		VActivityManager.get().killAppByPkg(pkg, userId);
		return 0;
	}

	@Override
	public boolean isEnable() {
		return isAppProcess();
	}
}
