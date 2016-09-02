package com.lody.virtual.client.hook.patchs.input;

import java.lang.reflect.Method;

import com.lody.virtual.client.hook.base.Hook;
import com.lody.virtual.helper.utils.ArrayUtils;

import android.view.inputmethod.EditorInfo;

/**
 * @author Lody
 *
 *
 *         原型: public InputBindResult startInput(IInputMethodClient client,
 *         IInputContext inputContext, EditorInfo attribute, int controlFlags)
 */
/* package */ class WindowGainedFocus extends Hook {

	private Boolean noEditorInfo = null;
	private int editorInfoIndex = -1;

	@Override
	public String getName() {
		return "windowGainedFocus";
	}

	@Override
	public Object call(Object who, Method method, Object... args) throws Throwable {
		if (noEditorInfo == null) {
			editorInfoIndex = ArrayUtils.indexOfFirst(args, EditorInfo.class);
			noEditorInfo = editorInfoIndex == -1;
		}
		if (!noEditorInfo) {
			EditorInfo attribute = (EditorInfo) args[editorInfoIndex];
			if (attribute != null) {
				String pkgName = attribute.packageName;
				if (isAppPkg(pkgName)) {
					attribute.packageName = getHostPkg();
				}
			}
		}
		return method.invoke(who, args);
	}

}
