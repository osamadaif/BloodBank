package com.osama.daif.bloodbank.view.fragment.homeCycle.staticScreen;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.osama.daif.bloodbank.R;
import com.osama.daif.bloodbank.view.activity.HomeCycleActivity;
import com.osama.daif.bloodbank.view.fragment.BaseFragment;
import com.osama.daif.bloodbank.view.fragment.homeCycle.notification.NotificationSettingFragment;
import com.osama.daif.bloodbank.view.fragment.homeCycle.post.PostsAndFavoritesListFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.osama.daif.bloodbank.helper.HelperMethods.replaceFragment;

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

    private Unbinder unbinder = null;

    HomeCycleActivity homeCycleActivity;
    @BindView(R.id.notification_settings_txt)
    TextView notificationSettingsTxt;

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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_more, container, false);
        unbinder = ButterKnife.bind(this, view);

        initFragment();
        homeCycleActivity = (HomeCycleActivity) getActivity();
        homeCycleActivity.editToolbarTxtSup(R.string.more);
        homeCycleActivity.bottomNavigationVisibility(View.VISIBLE);

        return view;
    }

    @Override
    public void onBack() {
        super.onBack();
        homeCycleActivity.setSelection(R.id.nav_home);
    }

    @OnClick({R.id.favourite_item, R.id.contact_us_item, R.id.about_application_item, R.id.rate_application_item, R.id.notification_settings_item, R.id.logout_item})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.favourite_item:
                goToFavourite();
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

    @OnClick(R.id.notification_settings_txt)
    public void onClick() {
        replaceFragment(getActivity().getSupportFragmentManager(), R.id.home_container_fr_frame, new NotificationSettingFragment());

    }

    public void goToFavourite(){
        Bundle bundle = new Bundle();
        bundle.putInt (PostsAndFavoritesListFragment.EXTRA_FAV, 22);
        replaceFragment(getActivity().getSupportFragmentManager(), R.id.home_container_fr_frame, new PostsAndFavoritesListFragment(),bundle);
    }
}
