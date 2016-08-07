package com.lody.virtual.client.hook.patchs.account;

import java.lang.reflect.Method;

import com.lody.virtual.client.hook.base.Hook;
import com.lody.virtual.client.local.VAccountManager;

/**
 * @author Lody
 *
 * @see android.accounts.IAccountManager#getAccountsByTypeForPackage(String,
 *      String, String)
 *
 */

public class Hook_GetAccountTypeForPackage extends Hook {

	@Override
	public String getName() {
		return "getAccountsByTypeForPackage";
	}

	@Override
	public Object onHook(Object who, Method method, Object... args) throws Throwable {
		String type = (String) args[0];
		String packageName = (String) args[1];
		return VAccountManager.getInstance().getAccounts(type);
	}
}
