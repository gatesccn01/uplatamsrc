package com.elmandarin.latamsrcupdate;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.elmandarin.latamsrcupdate.testupdatelatamsrc.AESCrypt;
import com.elmandarin.latamsrcupdate.testupdatelatamsrc.ConfigUpdate;
import com.elmandarin.latamsrcupdate.testupdatelatamsrc.ConfigUtil;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Activity Principal
 *
 * @author El Mandarin Sniff
 */

public class ElMandarinDEv extends AppCompatActivity {
    public FirebaseUser currentUser;
    private String PREFS_KEY = "mispreferencias";
    public static final String rutalatamsrc = "Config.json";
    public TextView userlatamsrc;
    private TabLayout tabs;
    private ViewPager vp;
    private static final String[] tabTitle = {"INICIO", "REGISTRO"};
    public TextView userlatamsrccopy;
    public FirebaseAuth mAuth;
    public ConfigUtil config;
    private SweetAlertDialog pDialog;
    //json app latamsrc
    private static final int PICK_FILE_REQUEST = 1;
    int clickCount = 0;
    private StorageReference mStorageRef;
    private EditText editTextFileName, editTextFileName2, editTextFileName3;
    public static EditText LinkLatamSRCUpdate;
    private Button buttonPickFile;
    private SharedPreferences sp, sp1, sp2;
    private Button buttonUpload, buttonUpload2, buttonUpload3;
    private InterstitialAd interstitialAd;
    private InterstitialAd mInterstitialAd;
    boolean isLoading;
    private AdView adsBannerView;
    private RewardedAd rewardedAd;

    //json app latamsrc
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawerlatam);
        doTabs();

        userlatamsrc = (TextView) findViewById(R.id.userlatamsrc);
        userlatamsrccopy = (TextView) findViewById(R.id.userlatamsrccopy);
        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null) {
            mAuth.getCurrentUser().reload().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {

                    currentUser = mAuth.getCurrentUser();


                    if (currentUser != null && currentUser.isEmailVerified()) {
                        userlatamsrc.setText("Correo electrónico del usuario: " + currentUser.getEmail());
                        userlatamsrccopy.setText(currentUser.getEmail());

                    }

                }
            });
        }
        buttonUpload = findViewById(R.id.buttonUpload);
        buttonUpload2 = findViewById(R.id.buttonUpload2);
        buttonUpload3 = findViewById(R.id.buttonUpload3);
        FirebaseApp.initializeApp(this);
        sp = PreferenceManager.getDefaultSharedPreferences(this);
        sp1 = PreferenceManager.getDefaultSharedPreferences(this);
        sp2 = PreferenceManager.getDefaultSharedPreferences(this);
        mStorageRef = FirebaseStorage.getInstance().getReference();
        editTextFileName = findViewById(R.id.editTextFileName);
        editTextFileName2 = findViewById(R.id.editTextFileName2);
        editTextFileName3 = findViewById(R.id.editTextFileName3);
        buttonPickFile = (Button) findViewById(R.id.buttonPickFile);

        buttonPickFile = (Button) findViewById(R.id.buttonPickFile);
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        buttonPickFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.setType("application/json");
                startActivityForResult(Intent.createChooser(intent, "Seleccione un archivo"), PICK_FILE_REQUEST);

            }
        });
        //LOGICA UPDATE JSON BY LATAMSRC
        config = new ConfigUtil(this);
        updateConfig(true);
        TextView version = (TextView)findViewById ( R.id.config_version_info); // version update
        version.setText("" + config.getVersion()); //version update
        LinkLatamSRCUpdate = findViewById(R.id.linklatamsrcgatesccnmandarin);
        //LOGICA UPDATE JSON BY LATAMSRC

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_FILE_REQUEST && resultCode == RESULT_OK) {
            Uri fileUri = data.getData();
            savedatalatamsrc(fileUri);
        }
    }

    //////JAVA ACTUALIZAR JSON BY LATAMSRC
//////JAVA ACTUALIZAR JSON BY LATAMSRC
    private void updateConfig(final boolean isOnCreate) {
        new ConfigUpdate(this, new ConfigUpdate.OnUpdateListener() {
            @Override
            public void onUpdateListener(String result) {
                try {
                    if (!result.contains("Error on getting data")) {
                        String json_data = AESCrypt.decrypt(config.PASSWORD, result);
                        if (isNewVersion(json_data)) {
                            newUpdateDialog(result);
                        } else {
                            if (!isOnCreate) {
                                noUpdateDialog();
                            }
                        }
                    } else if (result.contains("Error on getting data") && !isOnCreate) {
                        errorUpdateDialog(result);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start(isOnCreate);
    }


    private boolean isNewVersion(String result) {
        try {
            String current = config.getVersion();
            String update = new JSONObject(result).getString("Version");
            return config.versionCompare(update, current);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void newUpdateDialog(final String result) throws JSONException, GeneralSecurityException {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View inflate = inflater.inflate(R.layout.updatelatamsrc, (ViewGroup) null);
        AlertDialog.Builder builer = new AlertDialog.Builder(this);
        builer.setView(inflate);
        ImageView iv = inflate.findViewById(R.id.hadsImageView);
        TextView ms = inflate.findViewById(R.id.hadsTextView2);
        TextView vlatamsrc = inflate.findViewById(R.id.hadsTextView3);
        Button ok = inflate.findViewById(R.id.hadsButton);
        iv.setImageResource(R.drawable.mandarinas);
        TextView title = inflate.findViewById(R.id.hadsTextView1);
        title.setText("Tienes Un Nuevo Json");
        String json_data = AESCrypt.decrypt(config.PASSWORD, result);
        String notes = new JSONObject(json_data).getString("ReleaseNotes");
        String update = new JSONObject(json_data).getString("Version");
        vlatamsrc.setText(update);
        ms.setText(notes);
        ok.setText("Cerrar");
        final AlertDialog alert = builer.create();
        alert.setCanceledOnTouchOutside(false);
        alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alert.getWindow().setGravity(Gravity.CENTER);
        alert.show();
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    File file = new File(getFilesDir(), rutalatamsrc);
                    OutputStream out = new FileOutputStream(file);
                    out.write(result.getBytes());
                    out.flush();
                    out.close();
                    restart_app();
                    alert.dismiss();

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        alert.show();
    }

    private void noUpdateDialog() {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View inflate = inflater.inflate(R.layout.no_update, (ViewGroup) null);
        AlertDialog.Builder builer = new AlertDialog.Builder(this);
        builer.setView(inflate);
        ImageView iv = inflate.findViewById(R.id.hadsImageView);
        TextView title = inflate.findViewById(R.id.hadsTextView1);
        TextView ms = inflate.findViewById(R.id.hadsTextView2);
        Button ok = inflate.findViewById(R.id.hadsButton);
        iv.setImageResource(R.drawable.down);
        title.setText("Servidores Actualizados");
        ms.setText("Tienes la ultima versión");
        ok.setText("Aceptar");
        final AlertDialog alert = builer.create();
        alert.setCanceledOnTouchOutside(false);
        alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alert.getWindow().setGravity(Gravity.CENTER);
        alert.show();
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    alert.dismiss();

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        alert.show();
    }

    private void errorUpdateDialog(String error) {

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View inflate = inflater.inflate(R.layout.update_error, (ViewGroup) null);
        AlertDialog.Builder builer = new AlertDialog.Builder(this);
        builer.setView(inflate);
        ImageView iv = inflate.findViewById(R.id.images_update_error1);
        TextView title = inflate.findViewById(R.id.text_view_update_error1);
        TextView ms = inflate.findViewById(R.id.text_view_update_error2);
        Button ok = inflate.findViewById(R.id.update_error);
        iv.setImageResource(R.drawable.iconlatam);
        title.setText("Error on Update!");
        ms.setText("There is an error occurred while checking for update. Please contact Developer.");
        ok.setText("okay");
        final AlertDialog alert = builer.create();
        alert.setCanceledOnTouchOutside(false);
        alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alert.getWindow().setGravity(Gravity.CENTER);
        alert.show();
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    alert.dismiss();

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        alert.show();
    }

    private void restart_app() {
        this.startActivity(this.getIntent());
        this.finish();
        this.overridePendingTransition(0, 0);
        int tab2Position = 2; // La posición de tab2 en tu ViewPager
        vp.setCurrentItem(tab2Position);
    }
    public void deleteDataFromPathlatamsrcrtf5s(View view) {
        try {
            File file = new File(getFilesDir(), rutalatamsrc);

            if (file.exists()) {
                if (file.delete()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setMessage("Archivo eliminado correctamente.")
                            .setTitle("Éxito")
                            .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    restart_app();
                                    dialog.dismiss();
                                }
                            });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                } else {
                    Toast.makeText(this, "Error al eliminar el archivo.", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "El Link o Version no existe en la ruta especificada.", Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }



//////JAVA ACTUALIZAR JSON BY LATAMSRC
//////JAVA ACTUALIZAR JSON BY LATAMSRC


    @Override
    protected void onResume() {
        super.onResume();
        editTextFileName.setText(sp.getString("fileName", ""));
        editTextFileName2.setText(sp.getString("fileName2", ""));
        editTextFileName3.setText(sp.getString("fileName3", ""));
        LinkLatamSRCUpdate.setText(sp.getString("fileName4", ""));

    }

    @Override
    protected void onPause() {
        super.onPause();
        sp.edit().putString("fileName", editTextFileName.getText().toString()).apply();
        sp.edit().putString("fileName2", editTextFileName2.getText().toString()).apply();
        sp.edit().putString("fileName3", editTextFileName3.getText().toString()).apply();
        sp.edit().putString("fileName4", LinkLatamSRCUpdate.getText().toString()).apply();

    }


    private void uploadFile(Uri fileUri, String fileName) {
        String grupo = userlatamsrccopy.getText().toString();
        StorageReference fileRef = mStorageRef.child(grupo + "/" + " " + fileName);
        fileRef.putFile(fileUri)
                .addOnSuccessListener(taskSnapshot -> {
                    Toast.makeText(this, "Documento cargado exitosamente", Toast.LENGTH_SHORT).show();
                    fileRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        String downloadUrl = uri.toString();
                        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);

                        Toast.makeText(this, "Enlace de descarga: " + downloadUrl, Toast.LENGTH_LONG).show();

                        ClipData clip = ClipData.newPlainText("Enlace de descarga", downloadUrl);
                        clipboard.setPrimaryClip(clip);
                        Toast.makeText(this, "Enlace de descarga Copiado al portapapeles", Toast.LENGTH_SHORT).show();
                    });
                })
                .addOnFailureListener(e -> {
                    Log.e("Error", e.getMessage());
                    Toast.makeText(this, "Error al cargar el archivo", Toast.LENGTH_SHORT).show();
                });

    }

    private void savedatalatamsrc(final Uri fileUri) {
        buttonUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String textFileName = editTextFileName.getText().toString();
                uploadFile(fileUri, textFileName);
                sp.edit().putString("editTextFileName", textFileName).apply();
                showInterstitial();
                loadAd();
            }
        });

        buttonUpload2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String textFileName2 = editTextFileName2.getText().toString();
                uploadFile(fileUri, textFileName2);
                sp1.edit().putString("editTextFileName2", textFileName2).apply();
                showInterstitial();
                loadAd();
            }
        });

        buttonUpload3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String textFileName3 = editTextFileName3.getText().toString();
                uploadFile(fileUri, textFileName3);
                sp2.edit().putString("editTextFileName3", textFileName3).apply();
                showInterstitial();
                loadAd();
            }
        });

    }

    public void updatelatamsrc(View view) {
        updateConfig(false);
    }
    public void admin(View view) {
        if (editTextFileName.getText().toString().isEmpty()) {
            editTextFileName.setText("DEX GEN PRO");
        }
        if (editTextFileName2.getText().toString().isEmpty()) {
            editTextFileName2.setText("VIP GEN");
        }
        if (editTextFileName3.getText().toString().isEmpty()) {
            editTextFileName3.setText("LT GEN");
        }

    }

    private void showInterstitial() {
        if (interstitialAd != null) {
            interstitialAd.show(this);
        } else {
            loadAd();
            //Toast.makeText(this, "Ad did not load", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadAd() {
        AdRequest adRequest = new AdRequest.Builder().build();
        InterstitialAd.load(this, getString(R.string.admobinterst), adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        // The mInterstitialAd reference will be null until
                        // an ad is loaded.
                        ElMandarinDEv.this.interstitialAd = interstitialAd;
                        //Log.i(TAG, "onAdLoaded");
                        //Toast.makeText(SocksHttpMainActivity.this, "onAdLoaded()", Toast.LENGTH_SHORT).show();
                        interstitialAd.setFullScreenContentCallback(
                                new FullScreenContentCallback() {
                                    @Override
                                    public void onAdDismissedFullScreenContent() {
                                        // Called when fullscreen content is dismissed.
                                        // Make sure to set your reference to null so you don't
                                        // show it a second time.

                                        ElMandarinDEv.this.interstitialAd = null;
                                        //Log.d("TAG", "The ad was dismissed.");
                                    }

                                    @Override
                                    public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                                        // Called when fullscreen content failed to show.
                                        // Make sure to set your reference to null so you don't
                                        // show it a second time.
                                        loadAd();
                                        ElMandarinDEv.this.interstitialAd = null;
                                        //Log.d("TAG", "The ad failed to show.");
                                    }

                                    @Override
                                    public void onAdShowedFullScreenContent() {
                                        // Called when fullscreen content is shown.
                                        //Log.d("TAG", "The ad was shown.");
                                    }
                                });
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error
                        //Log.i(TAG, loadAdError.getMessage());
                        interstitialAd = null;

                        @SuppressLint("DefaultLocale") String error =
                                String.format(
                                        "domain: %s, code: %d, message: %s",
                                        loadAdError.getDomain(), loadAdError.getCode(), loadAdError.getMessage());
               /* Toast.makeText(
                        SocksHttpMainActivity.this, "onAdFailedToLoad() with error: " + error, Toast.LENGTH_SHORT)
                        .show();*/
                    }
                });
    }


    public void doTabs() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        vp = (ViewPager) findViewById(R.id.viewpager);
        tabs = (TabLayout) findViewById(R.id.tablayout);
        vp.setAdapter(new MyAdapter(Arrays.asList(tabTitle)));
        vp.setOffscreenPageLimit(2);
        tabs.setTabMode(TabLayout.MODE_FIXED);
        tabs.setTabGravity(TabLayout.GRAVITY_FILL);
        tabs.setupWithViewPager(vp);

    }  public class MyAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            // TODO: Implement this method
            return 2;
        }

        @Override
        public boolean isViewFromObject(View p1, Object p2) {
            // TODO: Implement this method
            return p1 == p2;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            int[] ids = new int[]{R.id.tab1, R.id.tab2};
            int id = 0;
            id = ids[position];
            // TODO: Implement this method
            return findViewById(id);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            // TODO: Implement this method
            return titles.get(position);
        }

        private List<String> titles;

        public MyAdapter(List<String> str) {
            titles = str;
        }
    }
}

