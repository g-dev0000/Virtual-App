package com.lc.interceptor.service;

import android.util.Log;

import com.lc.interceptor.ICallBody;
import com.lc.interceptor.IInterceptorCallManager;
import com.lc.interceptor.IObjectWrapper;
import com.lc.interceptor.service.providers.ConnectivityProvider;
import com.lc.interceptor.service.providers.LocationManagerProvider;
import com.lc.interceptor.service.providers.TelephonyManagerProvider;
import com.lc.interceptor.service.providers.WifiManagerProvider;
import com.lc.interceptor.service.providers.base.InterceptorDataProvider;
import com.lody.virtual.helper.utils.Reflect;
import com.lody.virtual.helper.utils.ReflectException;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @author legency
 */
public class VInterceptorService extends IInterceptorCallManager.Stub {

    public static final String TAG = VInterceptorService.class.getName();
    public static VInterceptorService sService = new VInterceptorService();
    private Map<String, InterceptorDataProvider> dataProviders = new HashMap<>(12);

    public static VInterceptorService get() {
        return sService;
    }

    public VInterceptorService() {
        init();
    }

    private void init() {
        add(new ConnectivityProvider());
        add(new LocationManagerProvider());
        add(new WifiManagerProvider());
        add(new TelephonyManagerProvider());
    }

    private void add(InterceptorDataProvider provider) {
        if (dataProviders.containsKey(provider.getDelegatePatch().getCanonicalName())) {
            Log.e(TAG, provider.getDelegatePatch().getName() + " is already added");
        } else {
            dataProviders.put(provider.getDelegatePatch().getCanonicalName(), provider);
        }
    }

    @Override
    public IObjectWrapper call(ICallBody iCall) {
        return dispatchCall(iCall);
    }


    private IObjectWrapper dispatchCall(ICallBody call) {
        InterceptorDataProvider interceptorDataProvider = dataProviders.get(call.module);
        if (interceptorDataProvider == null) {
            Log.e(TAG, call.module + " provider not found");
            return null;
        }

        Object object = null;
        try {
            object = Reflect.on(interceptorDataProvider).callBest(call.method, call.args).get();
        } catch (Exception e) {
            Log.e(TAG, call + " failed", e);
        }
        return new IObjectWrapper(object);
    }


    @Deprecated
    private IObjectWrapper<?> callSmart(ICallBody call, InterceptorDataProvider interceptorDataProvider) {
        Object object;//三种方式分发 寻找方法 可以优化为 先找名字的所有方法 按优先级匹配
        //1.直接call
        try {
            object = Reflect.on(interceptorDataProvider).call(call.method, call.args).get();
            return new IObjectWrapper(object);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //2.包裹 为 Object[] call requestLocationUpdates 这种多版本的
        try {
            Object[] arg = {call.args};
            object = Reflect.on(interceptorDataProvider).call(call.method, arg).get();
            return new IObjectWrapper(object);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 寻找同名直接return的
        try {
            object = tryUseNameAsMethod(call, interceptorDataProvider);
            return new IObjectWrapper(object);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Log.e(TAG, call.module + " call " + call.method + " failed");
        return new IObjectWrapper();
    }

    @Deprecated
    private Object tryUseNameAsMethod(ICallBody call, InterceptorDataProvider interceptorDataProvider) {
        try {
            Method[] methods = interceptorDataProvider.getClass().getMethods();
            for (Method method : methods) {
                if (method.getName().equals(call.method)) {
                    method.setAccessible(true);
                    return method.invoke(interceptorDataProvider);
                }
            }

        } catch (Exception e) {
            throw new ReflectException(e);
        }
        throw new ReflectException(call.method + " not found same name method", null);
    }
}
