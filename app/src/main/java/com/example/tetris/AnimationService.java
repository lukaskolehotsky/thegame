package com.example.tetris;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.view.View;
import android.view.animation.LinearInterpolator;

public class AnimationService {

    public void clickAnimation(View view) {
        view.animate()
                .rotationX(360).rotationY(360)
                .setDuration(1000)
                .setInterpolator(new LinearInterpolator())
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animator) {
                        view.setRotationX(0);
                        view.setRotationY(0);
                    }
                });
    }

}
