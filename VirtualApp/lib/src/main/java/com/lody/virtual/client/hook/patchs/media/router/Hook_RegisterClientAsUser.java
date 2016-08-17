package com.lody.virtual.client.hook.patchs.media.router;

import com.lody.virtual.client.hook.base.Hook;

import java.lang.reflect.Method;

/**
 * @author Lody
 *
 */
/* package */ class Hook_RegisterClientAsUser extends Hook {

	{
		replaceLastUserId();
	}

	@Override
	public String getName() {
		return "registerClientAsUser";
	}

	@Override
	public Object onHook(Object who, Method method, Object... args) throws Throwable {
		String pkgName = (String) args[1];
		if (isAppPkg(pkgName)) {
			args[1] = getHostPkg();
		}
		return method.invoke(who, args);
	}
}
