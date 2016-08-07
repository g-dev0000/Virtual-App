package com.lody.virtual.client.hook.patchs.account;

import java.lang.reflect.Method;

import com.lody.virtual.client.hook.base.Hook;
import com.lody.virtual.client.local.VAccountManager;

import android.accounts.Account;
import android.os.Bundle;

/**
 * @author Lody
 *
 * @see android.accounts.IAccountManager#addAccountExplicitly(Account, String,
 *      Bundle)
 *
 */

public class Hook_AddAccountExplicitly extends Hook {

	@Override
	public String getName() {
		return "addAccountExplicitly";
	}

	@Override
	public Object onHook(Object who, Method method, Object... args) throws Throwable {
		Account account = (Account) args[0];
		String password = (String) args[1];
		Bundle userdata = (Bundle) args[2];
		return VAccountManager.getInstance().addAccount(account, password, userdata);
	}
}
