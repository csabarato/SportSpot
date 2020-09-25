package com.sportspot.sportspot.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.view.ContextThemeWrapper;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.sportspot.sportspot.R;

public class DialogUtils {

    public static AlertDialog buildAlertDialog(String title, String message, AppCompatActivity context) {

        final MaterialAlertDialogBuilder matAlertDialogBuilder = new MaterialAlertDialogBuilder(context).setTitle(title).setMessage(message)
                .setNegativeButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        return matAlertDialogBuilder.create();
    }
}
