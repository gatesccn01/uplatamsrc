package com.elmandarin.latamsrcupdate.latamsrcutil;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.elmandarin.latamsrcupdate.R;


public class LatamSRC  extends AppCompatActivity {
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mostrarnuevobloqueolatamsrc);
        textView = findViewById(R.id.textView);
        new CountDownTimer(10000, 1000) {

            public void onTick(long millisUntilFinished) {
                textView.setText("Tiempo restante: " + millisUntilFinished / 1000 + " segundos");
            }

            public void onFinish() {
                textView.setText("Finalizado");
                finish();
            }
        }.start();
    }

    public void whatsapphome(View view){
        Uri uri = Uri.parse("https://t.me/gatesccn");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }
}