package com.sportspot.sportspot.utils;

import android.content.DialogInterface;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class DialogUtils {

    public static AlertDialog createAlertDialog(String title, String message, AppCompatActivity context) {

        final MaterialAlertDialogBuilder matAlertDialogBuilder = new MaterialAlertDialogBuilder(context).setTitle(title).setMessage(message)
                .setNegativeButton("Close", (dialog, which) -> dialog.cancel());

        return matAlertDialogBuilder.create();
    }

    public static MaterialAlertDialogBuilder getDefaultAlertDialogBuilder(String title, String message, AppCompatActivity context) {

        return new MaterialAlertDialogBuilder(context).setTitle(title).setMessage(message)
                .setNegativeButton("Close", (dialog, which) -> dialog.cancel());
    }
}
