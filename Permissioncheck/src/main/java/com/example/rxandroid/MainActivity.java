package com.example.rxandroid;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;

import android.os.Bundle;


import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public int checkPermission(String permission, int pid, int uid) {
        List<String> permissionsNeeded = new ArrayList<>();
        final List<String> permissionsList = new ArrayList<>();
        if (deniedPermission(permissionsList, Manifest.permission.ACCESS_FINE_LOCATION))
            permissionsNeeded.add("GPS");
        if (deniedPermission(permissionsList, Manifest.permission.READ_CONTACTS))
            permissionsNeeded.add("Read Contacts");
        if (permissionsList.size() > 0) {
            if (permissionsNeeded.size() > 0) {
                // Need Rationale
                String message = "You need to grant access to " + permissionsNeeded.get(0);
                for (int i = 1; i < permissionsNeeded.size(); i++)
                    message = message + ", " + permissionsNeeded.get(i);
                //requestPermissions(permissionsList.toArray(new String[permissionsList.size()]), REQUEST_PERMISSIONS);
            }
        }
        return super.checkPermission(permission, pid, uid);
    }

    @TargetApi(Build.VERSION_CODES.M)
    private boolean deniedPermission(List<String> permissionsList, String permission) {
        if (checkSelfPermission(permission) == PackageManager.PERMISSION_DENIED) {
            permissionsList.add(permission);
            // true，表示用户拒绝过
            if (shouldShowRequestPermissionRationale(permission)) {
                return true;
            }
        }
        return false;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);


    }
}
