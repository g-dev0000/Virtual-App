package com.lody.virtual.client.hook.providers;

import com.lody.virtual.client.core.VirtualCore;

import java.lang.reflect.Method;

/**
 * @author Lody
 */

public class DownloadProviderHook extends ProviderHook {

    public DownloadProviderHook(Object base) {
        super(base);
    }

    @Override
    protected void processArgs(Method method, Object... args) {
        if (args != null && args.length > 0 && args[0] instanceof String) {
            String pkg = (String) args[0];
            if (VirtualCore.getCore().isAppInstalled(pkg)) {
                args[0] = VirtualCore.getCore().getHostPkg();
            }
        }
    }
}
