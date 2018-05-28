package com.ats.barstockexchangeuser.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ats.barstockexchangeuser.R;

import static com.ats.barstockexchangeuser.activity.HomeActivity.tvTitle;

public class TermsFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_terms, container, false);
        tvTitle.setText("Terms and Conditions");
        return view;
    }

}
