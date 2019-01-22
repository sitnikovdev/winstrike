package ru.prsolution.winstrike.di.module;

import android.content.Context;


/**
 * Date: 8/18/2016
 * Time: 14:50
 *
 * @author Artur Artikov
 */
public class ContextModule {
	private Context mContext;

	public ContextModule(Context context) {
		mContext = context;
	}

	public Context provideContext() {
		return mContext;
	}
}
