package com.plastic.bevslch.europool2016.views;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.plastic.bevslch.europool2016.R;

/**
 * Created by Admin on 6/5/2016.
 */

public class LoadingOverlayView extends RelativeLayout {
    private ImageView spinnerImage, backgroundImage;
    private AnimatorSet animatorSet;
    private ObjectAnimator fadeOutAnim;
    private int visibility;

    public LoadingOverlayView(Context context) {
        super(context);
        init();
    }

    public LoadingOverlayView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LoadingOverlayView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.loader_overlay, this, true);
        spinnerImage = (ImageView) findViewById(R.id.loader_spinner_image);
        backgroundImage = (ImageView) findViewById(R.id.loader_background_image);

        animatorSet = new AnimatorSet();
        ObjectAnimator rotationAnim = ObjectAnimator.ofFloat(spinnerImage, "rotation", 360.0f);
        rotationAnim.setRepeatCount(ObjectAnimator.INFINITE);
        rotationAnim.setRepeatMode(ObjectAnimator.RESTART);
        ObjectAnimator fadeInAnim = ObjectAnimator.ofFloat(this, "alpha", 0.0f, 1.0f);
        fadeInAnim.setDuration(1000);
        animatorSet.playTogether(fadeInAnim, rotationAnim);
        fadeOutAnim = ObjectAnimator.ofFloat(this, "alpha", 1.0f, 0.0f);
        fadeOutAnim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                LoadingOverlayView.super.setVisibility(visibility);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    private void startAnimations() {
        animatorSet.start();
    }

    @Override
    public void setVisibility(int visibility) {
        this.visibility = visibility;
        if (visibility == VISIBLE) {
            super.setVisibility(visibility);
            startAnimations();
        } else {
            animatorSet.end();
            fadeOutAnim.start();
        }
    }
}
