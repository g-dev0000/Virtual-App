package com.lody.virtual.client.hook.patchs.phonesubinfo;

/**
 * @author Lody
 *
 */
/* package */ class Hook_GetLine1Number extends BaseHook_ReplacePkgName {

	@Override
	protected int getIndex() {
		return 0;
	}

	@Override
	public String getName() {
		return "getLine1Number";
	}
}
