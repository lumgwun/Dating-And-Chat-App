package com.lahoriagency.cikolive.Fragments;


import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.lahoriagency.cikolive.Classes.SmoothInterpolator;
import com.lahoriagency.cikolive.R;


public class EmptyLoginFragment extends Fragment {

    public EmptyLoginFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_empty_login, container, false);
        int colorFrom = Color.parseColor("#00FFFFFF");

        int colorTo = Color.parseColor("#FFFFFFFF");
        final ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
        colorAnimation.setDuration(500); // 500 ms
        colorAnimation.setInterpolator(new SmoothInterpolator());
        colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                view.findViewById(R.id.empty_login_layout).setBackgroundColor((int) animator.getAnimatedValue());
            }
        });
        colorAnimation.start();
        return view;
    }
}
