package com.sportspot.sportspot.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.view.ContextThemeWrapper;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.sportspot.sportspot.R;

public class DialogUtils {

    public static AlertDialog buildAlertDialog(String title, String message, AppCompatActivity context) {

        final AlertDialog.Builder alertBuilder = new AlertDialog.Builder(new ContextThemeWrapper(context, R.style.AlertDialogTheme));
        alertBuilder.setTitle(title);
        alertBuilder.setNegativeButton("Close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertBuilder.setMessage(message);

        return alertBuilder.create();
    }

}
