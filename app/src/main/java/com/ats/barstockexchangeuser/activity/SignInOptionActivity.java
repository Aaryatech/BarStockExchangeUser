package com.ats.barstockexchangeuser.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ats.barstockexchangeuser.R;

public class SignInOptionActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tvSignIn, tvCreateAcc;
    private LinearLayout llButtonPanel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in_option);

        tvSignIn = findViewById(R.id.tvSignIn);
        tvCreateAcc = findViewById(R.id.tvCreateAcc);
        llButtonPanel = findViewById(R.id.llButtonPanel);

        Animation animTranslate = AnimationUtils.loadAnimation(SignInOptionActivity.this, R.anim.slide_up);
        llButtonPanel.startAnimation(animTranslate);

        tvSignIn.setOnClickListener(this);
        tvCreateAcc.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.tvSignIn) {
            startActivity(new Intent(SignInOptionActivity.this, LoginActivity.class));
            finish();
        } else if (view.getId() == R.id.tvCreateAcc) {
            startActivity(new Intent(SignInOptionActivity.this, RegistrationActivity.class));
        }
    }
}
