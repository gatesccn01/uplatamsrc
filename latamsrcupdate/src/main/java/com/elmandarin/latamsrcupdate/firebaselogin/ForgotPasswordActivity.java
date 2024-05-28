package com.elmandarin.latamsrcupdate.firebaselogin;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.elmandarin.latamsrcupdate.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivity extends AppCompatActivity {


    private static final String TAG = "ForgotPasswordActivity";
    public FirebaseAuth mAuth;
    Button resetPasswordButton;
    EditText emailTextInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);


        emailTextInput = findViewById(R.id.fpEmailTextInput);
        resetPasswordButton = findViewById(R.id.resetPasswordButton);
        mAuth = FirebaseAuth.getInstance();


        resetPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                mAuth.sendPasswordResetEmail(emailTextInput.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) Log.d(TAG, "Email enviado.");


                                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                                        ForgotPasswordActivity.this);

                                // set title
                                alertDialogBuilder.setTitle("Restablecer la contraseña");

                                // set dialog message
                                alertDialogBuilder
                                        .setMessage("Se envía un enlace para restablecer la contraseña a su ID de correo electrónico registrado")
                                        .setCancelable(false)
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {

                                                ForgotPasswordActivity.this.finish();
                                            }
                                        });

                                AlertDialog alertDialog = alertDialogBuilder.create();
                                alertDialog.show();


                            }
                        });


            }
        });


    }
}
