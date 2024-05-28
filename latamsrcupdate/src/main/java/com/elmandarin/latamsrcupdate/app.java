package com.elmandarin.latamsrcupdate;

import android.app.Application;

import android.content.Context;

import android.content.res.Configuration;

/**
* App
*/
public class app extends Application
{
	private static app mApp;
	
	@Override
	public void onCreate()
	{
		super.onCreate();
		mApp = this;
		

	}
	
	@Override
	protected void attachBaseContext(Context base) {
		super.attachBaseContext(base);
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		//LocaleHelper.setLocale(this);
	}

	public static app getApp() {
		return mApp;
	}
	

}
