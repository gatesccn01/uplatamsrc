package com.elmandarin.latamsrcupdate.latamsrcutil;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.util.Log;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SignatureVerifier {

    private static final String TAG = "SignatureVerifier";
    //   private static final String APP_SIGNATURE = "E9:71:D2:7B:21:78:36:56:0A:01:24:B3:7B:94:37:87:EE:32:D0:6A"; // SHA-1 GOOLE LATAMSRC

    private static final String APP_SIGNATURE = "99:8B:E1:AA:B5:5A:DE:3E:9B:87:34:3B:EB:4B:4C:51:87:C4:EB:95"; // Aquí debes colocar tu firma JKS en formato SHA-1

    public static boolean verifySignature(PackageManager pm, String packageName) {
        try {
            PackageInfo packageInfo = pm.getPackageInfo(packageName, PackageManager.GET_SIGNATURES);
            Signature[] signatures = packageInfo.signatures;
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");

            for (Signature signature : signatures) {
                byte[] signatureBytes = signature.toByteArray();
                byte[] digest = messageDigest.digest(signatureBytes);
                StringBuilder stringBuilder = new StringBuilder();

                for (byte b : digest) {
                    stringBuilder.append(String.format("%02X", b));
                }

                String currentSignature = convertToHex(digest);
                if (currentSignature.equals(APP_SIGNATURE.replaceAll(":", "").toUpperCase())) {
                    Log.d(TAG, "Signature is verified");
                    return true;
                }
            }
        } catch (NameNotFoundException | NoSuchAlgorithmException e) {
            Log.e(TAG, "Error verifying signature: " + e.getMessage());
        }

        Log.e(TAG, "Signature is not verified");
        return false;
    }
    public static String convertToHex(byte[] bytes) {
        StringBuilder stringBuilder = new StringBuilder();
        for (byte b : bytes) {
            stringBuilder.append(String.format("%02X", b));
        }
        return stringBuilder.toString();
    }

}
