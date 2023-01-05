package com.kaiserdev.bustrips;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

public class ProgressButton {

    private CardView cardView;
    private ConstraintLayout layout;
    private ProgressBar progressBar;
    private TextView textView;

    Animation fade_in;

    ProgressButton(Context ct, View view){

        fade_in = AnimationUtils.loadAnimation(ct, R.anim.fade_in);

        cardView = view.findViewById(R.id.cardview);
        layout = view.findViewById(R.id.constraint_layout);
        progressBar = view.findViewById(R.id.progressBar2);
        textView = view.findViewById(R.id.text_login);

    }
    void buttonActivated(){
        progressBar.setAnimation(fade_in);
        progressBar.setVisibility(View.VISIBLE);
        textView.setVisibility(View.GONE);

    }
    void buttonFinished(){
        layout.setBackgroundColor(cardView.getResources().getColor(R.color.green));
        layout.setAnimation(fade_in);
        progressBar.setVisibility(View.GONE);
        textView.setVisibility(View.VISIBLE);
        textView.setAnimation(fade_in);
        textView.setText("Success");
    }
    void buttonError(){
        layout.setBackgroundColor(cardView.getResources().getColor(R.color.red));
        layout.setAnimation(fade_in);
        progressBar.setVisibility(View.GONE);
        textView.setVisibility(View.VISIBLE);
        textView.setAnimation(fade_in);
        textView.setText("Error");
    }
    void buttonOriginal(){
        layout.setBackgroundColor(cardView.getResources().getColor(R.color.orange));
        layout.setAnimation(fade_in);
        progressBar.setVisibility(View.GONE);
        textView.setVisibility(View.VISIBLE);
        textView.setAnimation(fade_in);
        textView.setText("Login");
    }
}
