package com.elmandarin.latamsrcupdate;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.github.paolorotolo.appintro.AppIntro;


/**
 * Created by latamsrc on 10/23/2022.
 */
public class MyIntro extends AppIntro {
    // Please DO NOT override onCreate. Use init
    @Override
    public void init(Bundle savedInstanceState) {

//adding the three slides for introduction app you can ad as many you needed
        addSlide(LicenceActivity.newInstance(R.layout.app_intro1));
        addSlide(LicenceActivity.newInstance(R.layout.app_intro2));
        addSlide(LicenceActivity.newInstance(R.layout.app_intro3));
        addSlide(LicenceActivity.newInstance(R.layout.app_intro5));
// Show and Hide Skip and Done buttons
        showStatusBar(true);
        showSkipButton(false);

// Turn vibration on and set intensity
        // You will need to add VIBRATE permission in Manifest file
        setVibrate(true);
        setVibrateIntensity(10);

//Add animation to the intro slider
        //setDepthAnimation();
    }


    @Override
    public void onSkipPressed() {
        // Do something here when users click or tap on Skip button.
        Toast.makeText(getApplicationContext(),
                getString(R.string.app_intro_skip), Toast.LENGTH_SHORT).show();
        Intent i = new Intent(getApplicationContext(), ElMandarinDEv.class);
        startActivity(i);
    }

    @Override
    public void onNextPressed() {
        // Do something here when users click or tap on Next button.
    }

    @Override
    public void onDonePressed() {
        // Do something here when users click or tap tap on Done button.
        finish();
    }

    @Override
    public void onSlideChanged() {
        // Do something here when slide is changed
    }
}
