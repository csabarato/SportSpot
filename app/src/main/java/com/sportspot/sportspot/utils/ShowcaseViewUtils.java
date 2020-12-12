package com.sportspot.sportspot.utils;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.view.View;
import android.widget.Button;

import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.ViewTarget;

public class ShowcaseViewUtils {

    private static TextPaint defaultContentTitlePaint = new TextPaint();
    private static TextPaint defaultContentTextPaint;


    public static ShowcaseView.Builder getDefaultShowcaseBuilder(Activity activity, View target, String title, String text) {

        constructDefTextPaints();

        return new ShowcaseView.Builder(activity)
                .setTarget(new ViewTarget(target))
                .setContentTitle(title)
                .setContentText(text)
                .setContentTitlePaint(defaultContentTitlePaint)
                .setContentTextPaint(defaultContentTextPaint)
                .replaceEndButton(createEndButton(activity));
    }

    private static void constructDefTextPaints() {

        defaultContentTitlePaint = new TextPaint();
        defaultContentTitlePaint.setTextSize(70f);
        defaultContentTitlePaint.setColor(Color.BLUE);


        defaultContentTextPaint = new TextPaint();
        Typeface plain = Typeface.MONOSPACE;
        defaultContentTextPaint.setTypeface(plain);
        defaultContentTextPaint.setTextSize(60f);
        defaultContentTextPaint.setColor(Color.DKGRAY);
    }

    private static Button createEndButton(Activity activity) {
        Button endButton = new Button(activity);
        endButton.setText("OK");
        endButton.setBackgroundColor(Color.rgb(69, 138, 247));
        return endButton;
    }



}
