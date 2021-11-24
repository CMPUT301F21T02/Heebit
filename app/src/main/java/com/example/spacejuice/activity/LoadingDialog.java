package com.example.spacejuice.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;

import com.example.spacejuice.R;

public class LoadingDialog {
    private final Activity activity;
    private AlertDialog dialog;

    public LoadingDialog(Activity currentActivity){
        activity = currentActivity;
    }

    public void startLoadingAlertDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        LayoutInflater inflater = activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.loading_dialog, null));
        builder.setCancelable(true);

        dialog = builder.create();
        dialog.show();
    }

    void dismissDialog(){
        dialog.dismiss();
    }
}
