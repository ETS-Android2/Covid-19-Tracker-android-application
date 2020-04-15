package com.example.covidtracer;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class DescriptionActivity extends AppCompatActivity {
    private static final int REQUEST_WRITE_STORAGE_REQUEST_CODE = 101 ;
    private Activity activity;
    private Context context;
    private static final String TAG = "DescriptionActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        activity = this;
        context = this;
        requestAppPermissions();
        Button btnRegister = findViewById(R.id.btn_register);
        btnRegister.setOnClickListener(v -> Utils.checkPermission(activity));


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        activity = null;
        context = null;
    }

    // Function to initiate after permissions are given by user
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == Utils.MULTIPLE_PERMISSIONS) {
            if (grantResults.length > 0) {
                Log.d(TAG, "3");

                boolean internetPermission = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                Log.d(TAG, String.valueOf(internetPermission));
                boolean accesFineLocationPermission = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                Log.d(TAG, String.valueOf(accesFineLocationPermission));
                boolean bluetoothPermission = grantResults[2] == PackageManager.PERMISSION_GRANTED;
                if (internetPermission && accesFineLocationPermission && bluetoothPermission ) {
                    Log.d(TAG, "Starting intent");
                    Intent intent1 = new Intent(DescriptionActivity.this, PhoneRegisterActivity.class);
                    startActivity(intent1);
                }
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(
                            new String[]{Manifest.permission
                                    .INTERNET, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.BLUETOOTH},
                            Utils.MULTIPLE_PERMISSIONS);
                    Intent intent = new Intent(DescriptionActivity.this, PhoneRegisterActivity.class);
                    startActivity(intent);
                }
            }
        }
    }

    private void requestAppPermissions() {
        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            return;
        }

        if (hasReadPermissions() && hasWritePermissions()) {
            return;
        }

        ActivityCompat.requestPermissions(this,
                new String[] {
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                }, REQUEST_WRITE_STORAGE_REQUEST_CODE); // your request code
    }

    private boolean hasReadPermissions() {
        return (ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
    }

    private boolean hasWritePermissions() {
        return (ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
    }

}
