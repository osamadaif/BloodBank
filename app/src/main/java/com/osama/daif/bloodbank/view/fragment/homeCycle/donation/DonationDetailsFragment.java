package com.osama.daif.bloodbank.view.fragment.homeCycle.donation;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.osama.daif.bloodbank.R;
import com.osama.daif.bloodbank.view.fragment.BaseFragment;

import butterknife.OnClick;

import static com.osama.daif.bloodbank.data.local.SharedPreferencesManger.loadUserData;

public class DonationDetailsFragment extends BaseFragment {

    public DonationDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate (R.layout.fragment_blank, container, false);
        // Inflate the layout for this fragment
        initFragment ( );

        return view;
    }


    @Override
    public void onBack() {
        super.onBack ( );
    }

    @OnClick(R.id.fragment_post_details_img_fav)
    public void onClick() {
    }
}
