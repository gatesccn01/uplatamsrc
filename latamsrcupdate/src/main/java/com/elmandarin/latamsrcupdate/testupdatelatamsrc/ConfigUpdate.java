package com.elmandarin.latamsrcupdate.testupdatelatamsrc;


import static com.elmandarin.latamsrcupdate.ElMandarinDEv.LinkLatamSRCUpdate;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by: ElMandarinSNiff
 * Date Crated: 10/8/2023
 * Project: LatamSRC (Español)
 **/
public class ConfigUpdate extends AsyncTask<String, String, String> {

    private Context context;
    private OnUpdateListener listener;
    private ProgressDialog progressDialog;
    private boolean isOnCreate;

    public ConfigUpdate(Context context, OnUpdateListener listener) {
        this.context = context;
        this.listener = listener;
    }

    public void start(boolean isOnCreate) {
        this.isOnCreate = isOnCreate;
        execute();
    }

    public interface OnUpdateListener {
        void onUpdateListener(String result);
    }

    @Override
    protected String doInBackground(String... strings) {
        try {
            StringBuilder sb = new StringBuilder();
            String textFileName2 = LinkLatamSRCUpdate.getText().toString();
            URL url = new URL(textFileName2);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String response;

            while ((response = br.readLine()) != null) {
                sb.append(response);
            }
            return sb.toString();
        } catch (Exception e) {
            //   SkStatus.logInfo(e.getMessage());
            return "Error Si Persiste Contacta Al Admin: " + e.getMessage();
        }
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (!isOnCreate) {
            progressDialog = new ProgressDialog(context, 5);
            progressDialog.setTitle("Checking Server Update");
            progressDialog.setMessage("Loading please wait...");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
        }
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        if (!isOnCreate && progressDialog != null) {
            progressDialog.dismiss();
        }
        if (listener != null) {
            listener.onUpdateListener(s);
        }
    }
}
