package com.ats.barstockexchangeuser.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.ats.barstockexchangeuser.R;
import com.ats.barstockexchangeuser.util.ViewBillInterface;
import com.ats.barstockexchangeuser.util.ViewOrderInterface;
import static com.ats.barstockexchangeuser.activity.HomeActivity.tvTitle;

public class MyOrdersFragment extends Fragment implements View.OnClickListener {

    private ViewPager viewPager;
    private TabLayout tab;
    FragmentPagerAdapter adapterViewPager;

    private Button btnHome, btnOrders;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_orders, container, false);
        tvTitle.setText("My Orders");

        viewPager = view.findViewById(R.id.viewPager);
        tab = view.findViewById(R.id.tab);

        btnHome = view.findViewById(R.id.btnMyOrder_Home);
        btnOrders = view.findViewById(R.id.btnMyOrders_Orders);
        btnHome.setOnClickListener(this);
        btnOrders.setOnClickListener(this);


        adapterViewPager = new ViewPagerAdapter(getChildFragmentManager(), getContext());
        viewPager.setAdapter(adapterViewPager);
        tab.post(new Runnable() {
            @Override
            public void run() {
                try {
                    tab.setupWithViewPager(viewPager);
                } catch (Exception e) {
                }
            }
        });


        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                ////Log.e("POSITION : ", "----------------------" + position);

                if (position == 0) {
                    ViewOrderInterface fragment = (ViewOrderInterface) adapterViewPager.instantiateItem(viewPager, position);
                    if (fragment != null) {
                        fragment.fragmentBecameVisible();
                    }
                } else if (position == 1) {
                    ViewBillInterface fragmentInterested = (ViewBillInterface) adapterViewPager.instantiateItem(viewPager, position);
                    if (fragmentInterested != null) {
                        fragmentInterested.fragmentBecameVisible();
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        return view;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnMyOrder_Home) {
            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, new UserHomeFragment(), "Home");
            ft.commit();
        } else if (v.getId() == R.id.btnMyOrders_Orders) {
            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, new MyOrdersFragment(), "HomeFragment");
            ft.commit();
        }
    }

    public static class ViewPagerAdapter extends FragmentPagerAdapter {


        private Context mContext;
        private String jobId, jobProId;

        public ViewPagerAdapter(FragmentManager fm, Context mContext) {
            super(fm);
            this.mContext = mContext;
            this.jobId = jobId;
            this.jobProId = jobProId;
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                return new ViewOrdersFragment();
            } else {
                return new ViewBillFragment();
            }
        }

        @Override
        public int getCount() {
            return 2;
        }


        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Orders";
                case 1:
                    return "Bill";
                default:
                    return null;
            }
        }
    }

}
