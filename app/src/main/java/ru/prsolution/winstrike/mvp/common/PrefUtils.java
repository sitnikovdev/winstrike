package ru.prsolution.winstrike.mvp.common;

import android.content.Context;
import android.content.SharedPreferences;

import ru.prsolution.winstrike.WinstrikeApp;


/**
 * Date: 18.01.2016
 * Time: 15:01
 *
 * @author Yuri Shmakov
 */
public class PrefUtils {
	private static final String PREF_NAME = "github";

	public static SharedPreferences getPrefs() {
		return WinstrikeApp.getInstance().getAppComponent().getContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
	}

	public static SharedPreferences.Editor getEditor() {
		return getPrefs().edit();
	}
}
