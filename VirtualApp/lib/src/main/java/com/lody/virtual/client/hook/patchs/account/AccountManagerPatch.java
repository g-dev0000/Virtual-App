package com.lody.virtual.client.hook.patchs.account;

import android.accounts.IAccountManager;
import android.content.Context;
import android.os.ServiceManager;

import com.lody.virtual.client.hook.base.Patch;
import com.lody.virtual.client.hook.base.PatchObject;
import com.lody.virtual.client.hook.binders.HookAccountBinder;

/**
 * @author Lody
 */
@Patch({
		Hook_AddAccount.class,
		Hook_AddAccountAsUser.class,
		Hook_AddAccountExplicitly.class,

		Hook_GetPassword.class,

		Hook_GetAccounts.class,
		Hook_GetAccountsAsUser.class,
		Hook_GetAccountsByFeatures.class,
		Hook_GetAccountsByTypeForPackage.class,
		Hook_GetAccountsForPackage.class,

        Hook_GetUserData.class,
		Hook_ClearPassword.class,
		Hook_UpdateAppPermission.class,
		Hook_UpdateCredentials.class,
		Hook_HasFeatures.class,

		Hook_RemoveAccount.class,

		Hook_GetAuthToken.class,
		Hook_GetPreviousName.class,
		Hook_SetPassword.class,
})
public class AccountManagerPatch extends PatchObject<IAccountManager, HookAccountBinder> {

	@Override
	protected HookAccountBinder initHookObject() {
		return new HookAccountBinder();
	}

	@Override
	public void inject() throws Throwable {
		getHookObject().injectService(Context.ACCOUNT_SERVICE);
	}

	@Override
	public boolean isEnvBad() {
		return ServiceManager.getService(Context.ACCOUNT_SERVICE) != getHookObject();
	}
}
