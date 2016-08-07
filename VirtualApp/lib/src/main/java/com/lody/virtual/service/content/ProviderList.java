package com.lody.virtual.service.content;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import android.app.IActivityManager;

/**
 * @author Lody
 *
 */
public class ProviderList {
	private final Map<String, IActivityManager.ContentProviderHolder> mHolders = new HashMap<>();

	public IActivityManager.ContentProviderHolder getHolder(String auth) {
		return mHolders.get(auth);
	}

	public void putHolder(String auth, IActivityManager.ContentProviderHolder holder) {
		mHolders.put(auth, holder);
	}

	public Set<String> getAuthorities() {
		return mHolders.keySet();
	}

	public void removeAuthority(String name) {
		mHolders.remove(name);
	}
}
