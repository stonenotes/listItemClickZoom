package com.stonenotes.listitem.utils;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.annotation.SuppressLint;
import android.os.Handler;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

@SuppressLint("NewApi")
public class ObjectAnimatorUtils {

    private static final int DURATION = 250;
    private static Handler mHandler = new Handler();

    public static void animationOpen(View srcView, View desView, int stateBarHeight) {
        if (android.os.Build.VERSION.SDK_INT < 11) {
            desView.setVisibility(View.VISIBLE);
            return;
        }
        int[] xy = new int[2];
        srcView.getLocationOnScreen(xy);
        xy[1] -= stateBarHeight;
        positionAndSizeAsIcon(srcView, desView);
        if ((desView.getWidth() - srcView.getWidth()) + xy[0] == 0
                || (desView.getHeight() - srcView.getHeight()) + xy[1] == 0)
            return;
        float x = srcView.getWidth() * xy[0] / (desView.getWidth() - srcView.getWidth()) + xy[0];
        float y = srcView.getHeight() * xy[1] / (desView.getHeight() - srcView.getHeight()) + xy[1];
        desView.setPivotX(x);
        desView.setPivotY(y);
        PropertyValuesHolder alpha = PropertyValuesHolder.ofFloat("alpha", 1f);
        PropertyValuesHolder scaleX = PropertyValuesHolder.ofFloat("scaleX", 1.0f);
        PropertyValuesHolder scaleY = PropertyValuesHolder.ofFloat("scaleY", 1.0f);
        final ObjectAnimator anim = ofPropertyValuesHolder(desView, alpha, scaleX, scaleY);

        anim.setInterpolator(new DecelerateInterpolator());
        anim.setDuration(DURATION);
        anim.start();
    }

    public static void animationClose(View srcView, final View desView, int stateBarHeight) {
        if (android.os.Build.VERSION.SDK_INT < 11) {
            desView.setVisibility(View.GONE);
            return;
        }
        int[] xy = new int[2];
        srcView.getLocationOnScreen(xy);
        xy[1] -= stateBarHeight;
        if ((desView.getWidth() - srcView.getWidth()) + xy[0] == 0
                || (desView.getHeight() - srcView.getHeight()) + xy[1] == 0)
            return;
        float x = srcView.getWidth() * xy[0] / (desView.getWidth() - srcView.getWidth()) + xy[0];
        float y = srcView.getHeight() * xy[1] / (desView.getHeight() - srcView.getHeight()) + xy[1];
        desView.setPivotX(x);
        desView.setPivotY(y);
        PropertyValuesHolder alpha = PropertyValuesHolder.ofFloat("alpha", 0.2f);
        final float fScaleX = srcView.getWidth() / (float) desView.getWidth();
        PropertyValuesHolder scaleX = PropertyValuesHolder.ofFloat("scaleX", fScaleX);
        PropertyValuesHolder scaleY = PropertyValuesHolder.ofFloat("scaleY",
                srcView.getHeight() / (float) desView.getHeight());
        final ObjectAnimator anim = ofPropertyValuesHolder(desView, alpha, scaleX, scaleY);
        anim.addUpdateListener(new AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float scale = (Float) animation.getAnimatedValue("scaleX");
                if (fScaleX == scale) {
                    desView.setVisibility(View.GONE);
                }
            }
        });

        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                desView.setVisibility(View.GONE);
            }
        });

        anim.setInterpolator(new DecelerateInterpolator());
        anim.setDuration(DURATION);
        anim.start();
    }

    public static void alphaClose(final View desView) {
        if (android.os.Build.VERSION.SDK_INT < 11) {
            desView.setVisibility(View.GONE);
            return;
        }
        desView.animate().setInterpolator(new DecelerateInterpolator()).alpha(0f).setListener(new AnimatorListener() {

            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                desView.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }
        }).setDuration(750).start();
        mHandler.postDelayed(new Runnable() {

            @Override
            public void run() {
                desView.setVisibility(View.GONE);
            }
        }, 700);
    }

    private static void positionAndSizeAsIcon(View srcView, View desView) {
        desView.setScaleX(srcView.getWidth() / (float) desView.getWidth());
        desView.setScaleY(srcView.getHeight() / (float) desView.getHeight());
        desView.setAlpha(0.2f);
        desView.setVisibility(View.VISIBLE);
    }

    public static ObjectAnimator ofPropertyValuesHolder(Object target, PropertyValuesHolder... values) {
        ObjectAnimator anim = new ObjectAnimator();
        anim.setTarget(target);
        anim.setValues(values);
        return anim;
    }
}
