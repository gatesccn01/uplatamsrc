package com.elmandarin.latamsrcupdate.firebaselogin;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.elmandarin.latamsrcupdate.ElMandarinDEv;
import com.elmandarin.latamsrcupdate.MyIntro;
import com.elmandarin.latamsrcupdate.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class WelcomeActivity extends AppCompatActivity {

    private String PREFS_KEY = "mispreferencias";
    public Integer REQUEST_EXIT = 9;
    public FirebaseAuth mAuth;
    public FirebaseUser currentUser;
    Button signUpButton;
    Button signInButton;

    public void saveValuePreference(Context context, Boolean mostrar) {
        SharedPreferences settings = context.getSharedPreferences(PREFS_KEY, MODE_PRIVATE);
        SharedPreferences.Editor editor;
        editor = settings.edit();
        editor.putBoolean("license", mostrar);
        editor.commit();
    }

    public boolean getValuePreference(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREFS_KEY, MODE_PRIVATE);
        return preferences.getBoolean("license", true);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        boolean muestra = getValuePreference(getApplicationContext());

        mAuth = FirebaseAuth.getInstance();


        signUpButton = findViewById(R.id.welcomeSignUpButton);
        signInButton = findViewById(R.id.welcomeSignInButton);

        signInButton.setVisibility(INVISIBLE);
        signUpButton.setVisibility(INVISIBLE);
        if (muestra) {
            Intent myIntent = new Intent(WelcomeActivity.this, MyIntro.class);
            startActivity(myIntent);
            saveValuePreference(getApplicationContext(), false);
        }
        if (mAuth.getCurrentUser() != null) {
            mAuth.getCurrentUser().reload().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {

                    currentUser = mAuth.getCurrentUser();


                    if (currentUser != null && currentUser.isEmailVerified()) {
                        System.out.println("Correo Electrónico Verificado : " + currentUser.isEmailVerified());
                        System.out.println("Correo electrónico del usuario: " + currentUser.getEmail());

                        Intent MainActivity = new Intent(WelcomeActivity.this, ElMandarinDEv.class);
                        startActivity(MainActivity);
                        WelcomeActivity.this.finish();

                    }

                }
            });

        } else {

            signInButton.setVisibility(VISIBLE);
            signUpButton.setVisibility(VISIBLE);

            System.out.println("usuario no disponible");

        }

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent signUpIntent = new Intent(WelcomeActivity.this, SignUpActivity.class);

                startActivity(signUpIntent);


            }
        });


        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent signInIntent = new Intent(WelcomeActivity.this, SignInActivity.class);

                startActivityForResult(signInIntent, REQUEST_EXIT);


            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_EXIT) {
            if (resultCode == RESULT_OK) {
                this.finish();

            }
        }
    }



}
