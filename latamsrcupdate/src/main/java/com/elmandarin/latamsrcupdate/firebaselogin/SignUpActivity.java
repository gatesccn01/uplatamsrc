package com.elmandarin.latamsrcupdate.firebaselogin;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.elmandarin.latamsrcupdate.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class SignUpActivity extends AppCompatActivity {


    private static final String TAG = "SignUpActivity";
    public FirebaseAuth mAuth;
    Button signUpButton;
    EditText signUpEmailTextInput;
    EditText signUpPasswordTextInput;
    CheckBox agreementCheckBox;
    TextView errorView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);


        mAuth = FirebaseAuth.getInstance();

        signUpEmailTextInput = findViewById(R.id.signUpEmailTextInput);
        signUpPasswordTextInput = findViewById(R.id.signUpPasswordTextInput);
        signUpButton = findViewById(R.id.signUpButton);
        agreementCheckBox = findViewById(R.id.agreementCheckbox);
        errorView = findViewById(R.id.signUpErrorView);


        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (signUpEmailTextInput.getText().toString().contentEquals("")) {


                    errorView.setText("El correo electrónico no puede estar vacío.");


                } else if (signUpPasswordTextInput.getText().toString().contentEquals("")) {


                    errorView.setText("La contraseña no puede estar vacía");


                } else if (!agreementCheckBox.isChecked()) {

                    errorView.setText("Por favor acepte los términos y condiciones");


                } else {


                    mAuth.createUserWithEmailAndPassword(signUpEmailTextInput.getText().toString(), signUpPasswordTextInput.getText().toString()).addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "crear usuario con correo electrónico: éxito");
                                FirebaseUser user = mAuth.getCurrentUser();

                                try {
                                    if (user != null)
                                        user.sendEmailVerification()
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            Log.d(TAG, "Email enviado.");

                                                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                                                                    SignUpActivity.this);

                                                            // set title
                                                            alertDialogBuilder.setTitle("Por favor verifique su ID de correo electrónico");

                                                            // set dialog message
                                                            alertDialogBuilder
                                                                    .setMessage("Se envío un correo electrónico de verificación a su ID de correo electrónico registrado. Haga clic en el enlace e inicie sesión nuevamente.")
                                                                    .setCancelable(false)
                                                                    .setPositiveButton("Iniciar sesión", new DialogInterface.OnClickListener() {
                                                                        public void onClick(DialogInterface dialog, int id) {

                                                                            Intent signInIntent = new Intent(SignUpActivity.this, SignInActivity.class);
                                                                            startActivity(signInIntent);
                                                                            finish();
                                                                            SignUpActivity.this.finish();
                                                                        }
                                                                    });

                                                            // create alert dialog
                                                            AlertDialog alertDialog = alertDialogBuilder.create();

                                                            // show it
                                                            alertDialog.show();


                                                        }
                                                    }
                                                });

                                } catch (Exception e) {
                                    errorView.setText(e.getMessage());
                                }
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "crear usuario con correo electrónico: error", task.getException());
                                Toast.makeText(SignUpActivity.this, "La autenticación falló.",
                                        Toast.LENGTH_SHORT).show();

                                if (task.getException() != null) {
                                    errorView.setText(task.getException().getMessage());
                                }

                            }

                        }
                    });

                }

            }
        });

    }


    public void linklatamsrc(View view){
        Uri uri = Uri.parse("https://raw.githubusercontent.com/gatesccn01/updateapp/main/terminosgatesccn");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }
}
