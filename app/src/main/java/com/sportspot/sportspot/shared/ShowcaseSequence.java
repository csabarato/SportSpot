package com.sportspot.sportspot.shared;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.MotionEvent;
import android.view.View;

import com.github.amlcurran.showcaseview.OnShowcaseEventListener;
import com.github.amlcurran.showcaseview.ShowcaseView;
import com.sportspot.sportspot.constants.SharedPrefConst;
import com.sportspot.sportspot.utils.ShowcaseViewUtils;

import java.util.ArrayList;
import java.util.List;

public class ShowcaseSequence {

    private final List<ShowcaseView.Builder> showcaseViewBuilders = new ArrayList<>();
    private ShowcaseView currentShowcase = null;
    private boolean isShowing = false;
    private Activity activity;
    private SharedPreferences sharedPreferences;

    public ShowcaseSequence(Activity activity) {
        this.activity = activity;
        sharedPreferences = activity.getSharedPreferences(SharedPrefConst.SHARED_PREF_NAME, Context.MODE_PRIVATE);
    }

    public void start() {

        currentShowcase = showcaseViewBuilders.get(0).build();
        isShowing = true;

        for(int i =0; i< showcaseViewBuilders.size(); i++) {
            showcaseViewBuilders.get(i).setShowcaseEventListener(onShowcaseEventListener(i));
        }
    }

    private void finish() {
        currentShowcase = null;
        isShowing = false;
        sharedPreferences.edit().putBoolean(SharedPrefConst.IS_SHOWCASE_COMPLETED, true).apply();
    }

    public void abort() {
        currentShowcase.setOnShowcaseEventListener(null);
        currentShowcase.hide();
    }

    public boolean isShowing() {
        return isShowing;
    }

    public ShowcaseView.Builder createScBuilderAndAddToSequence(View target, String title, String text) {

        ShowcaseView.Builder builder = ShowcaseViewUtils.getDefaultShowcaseBuilder(
                activity, target,title,text);
        showcaseViewBuilders.add(builder);
        return builder;
    }

    private OnShowcaseEventListener onShowcaseEventListener(int indexOfShowcase) {

        return new OnShowcaseEventListener() {
            @Override
            public void onShowcaseViewHide(ShowcaseView showcaseView) {

                if(indexOfShowcase < showcaseViewBuilders.size()-1) {
                    currentShowcase = showcaseViewBuilders.get(indexOfShowcase+1).build();
                } else {
                    finish();
                }
            }

            @Override
            public void onShowcaseViewDidHide(ShowcaseView showcaseView) {

            }

            @Override
            public void onShowcaseViewShow(ShowcaseView showcaseView) {

            }

            @Override
            public void onShowcaseViewTouchBlocked(MotionEvent motionEvent) {

            }
        };
    }
}
