package com.elmandarin.latamsrcupdate;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;


import androidx.appcompat.app.AppCompatActivity;

import com.elmandarin.latamsrcupdate.firebaselogin.WelcomeActivity;
import com.elmandarin.latamsrcupdate.latamsrcutil.LatamSRC;
import com.elmandarin.latamsrcupdate.latamsrcutil.SignatureVerifier;

/**
 * @author El Mandarin Sniff LatamSRC
 */
public class LauncherActivity extends AppCompatActivity
{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash_screen);



		PackageManager pm = getPackageManager();
		String packageName = getPackageName();
		if (SignatureVerifier.verifySignature(pm, packageName)) {

			Intent intent = new Intent(this, WelcomeActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
			startActivity(intent);
			finish();
		} else {
			Intent intent = new Intent(this, WelcomeActivity.class);
		//	Intent intent = new Intent(this, LatamSRC.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
			startActivity(intent);
			finish();
		}

	}


}
