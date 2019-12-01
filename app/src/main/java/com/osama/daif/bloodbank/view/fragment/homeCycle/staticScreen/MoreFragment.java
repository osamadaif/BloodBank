package com.osama.daif.bloodbank.view.fragment.homeCycle.staticScreen;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.osama.daif.bloodbank.R;
import com.osama.daif.bloodbank.view.activity.HomeCycleActivity;
import com.osama.daif.bloodbank.view.fragment.BaseFragment;

import butterknife.BindView;
import butterknife.OnClick;

public class MoreFragment extends BaseFragment {

    @BindView(R.id.favourite_item)
    LinearLayout favouriteItem;
    @BindView(R.id.contact_us_item)
    LinearLayout contactUsItem;
    @BindView(R.id.about_application_item)
    LinearLayout aboutApplicationItem;
    @BindView(R.id.rate_application_item)
    LinearLayout rateApplicationItem;
    @BindView(R.id.notification_settings_item)
    LinearLayout notificationSettingsItem;
    @BindView(R.id.logout_item)
    LinearLayout logoutItem;

    HomeCycleActivity homeCycleActivity;

    public MoreFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_more, container, false);
        // Inflate the layout for this fragment
        initFragment();
        homeCycleActivity = (HomeCycleActivity) getActivity();
        homeCycleActivity.editToolbarTxtSup(R.string.more);

        return view;
    }

    @Override
    public void onBack() {
        super.onBack();
    }

    @OnClick({R.id.favourite_item, R.id.contact_us_item, R.id.about_application_item, R.id.rate_application_item, R.id.notification_settings_item, R.id.logout_item})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.favourite_item:
                break;
            case R.id.contact_us_item:
                break;
            case R.id.about_application_item:
                break;
            case R.id.rate_application_item:
                break;
            case R.id.notification_settings_item:
                break;
            case R.id.logout_item:
                break;
        }
    }
}