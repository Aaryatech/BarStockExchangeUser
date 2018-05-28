package com.ats.barstockexchangeuser.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ats.barstockexchangeuser.R;

import static com.ats.barstockexchangeuser.activity.HomeActivity.tvTitle;

public class AboutDevFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about_dev, container, false);
        tvTitle.setText("About Developers");

        return view;
    }

}
