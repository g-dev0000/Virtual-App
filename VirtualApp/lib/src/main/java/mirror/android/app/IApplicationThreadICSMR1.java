package mirror.android.app;

import android.app.IApplicationThread;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.CompatibilityInfo;
import android.os.Bundle;

import mirror.ClassDef;
import mirror.MethodDef;
import mirror.MethodInfo;

/**
 * @author Lody
 */

public class IApplicationThreadICSMR1 {
    public static Class<?> Class = ClassDef.init(IApplicationThreadICSMR1.class, IApplicationThread.class);

    @MethodInfo({Intent.class, ActivityInfo.class, CompatibilityInfo.class, int.class, String.class, Bundle.class, boolean.class})
    public static MethodDef scheduleReceiver;
}
