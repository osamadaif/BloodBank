package com.osama.daif.bloodbank.view.fragment.homeCycle.staticScreen;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.google.android.material.appbar.AppBarLayout;
import com.osama.daif.bloodbank.R;
import com.osama.daif.bloodbank.data.local.SharedPreferencesManger;
import com.osama.daif.bloodbank.view.activity.HomeCycleActivity;
import com.osama.daif.bloodbank.view.fragment.BaseFragment;
import com.osama.daif.bloodbank.view.fragment.homeCycle.notification.NotificationSettingFragment;
import com.osama.daif.bloodbank.view.fragment.homeCycle.post.PostsAndFavoritesListFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.osama.daif.bloodbank.data.local.SharedPreferencesManger.LANG_NUM;
import static com.osama.daif.bloodbank.data.local.SharedPreferencesManger.LoadData;
import static com.osama.daif.bloodbank.data.local.SharedPreferencesManger.SaveData;
import static com.osama.daif.bloodbank.data.local.SharedPreferencesManger.USER_LANG;
import static com.osama.daif.bloodbank.data.local.SharedPreferencesManger.loadUserData;
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
    @BindView(R.id.notification_settings_txt)
    TextView notificationSettingsTxt;
    @BindView(R.id.notification_settings_item)
    LinearLayout notificationSettingsItem;
    @BindView(R.id.logout_item)
    LinearLayout logoutItem;
    @BindView(R.id.language_txt)
    TextView languageTxt;
    @BindView(R.id.language_item)
    LinearLayout languageItem;

    private int lanChoose = 0;
    private Unbinder unbinder = null;

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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_more, container, false);
        unbinder = ButterKnife.bind(this, view);
        initFragment();
        homeCycleActivity = (HomeCycleActivity) getActivity();
        homeCycleActivity.editToolbarTxtSup(R.string.more);
        homeCycleActivity.appbarVisibility(View.VISIBLE);
        homeCycleActivity.setBackBtnVisibility(View.GONE);
        homeCycleActivity.setBehavior(new AppBarLayout.ScrollingViewBehavior());
        homeCycleActivity.bottomNavigationVisibility(View.VISIBLE);
        return view;
    }

    @Override
    public void onBack() {
        super.onBack();
        homeCycleActivity.setSelection(R.id.nav_home);
    }

    @OnClick({R.id.favourite_item, R.id.contact_us_item, R.id.about_application_item, R.id.rate_application_item, R.id.notification_settings_item, R.id.logout_item, R.id.language_item})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.favourite_item:
                goToFavourite();
                break;
            case R.id.contact_us_item:
                replaceFragment(getActivity().getSupportFragmentManager(), R.id.home_container_fr_frame, new ContactUsFragment());

                break;
            case R.id.about_application_item:
                replaceFragment(getActivity().getSupportFragmentManager(), R.id.home_container_fr_frame, new AboutAppFragment());

                break;
            case R.id.rate_application_item:
                break;
            case R.id.notification_settings_item:
                replaceFragment(getActivity().getSupportFragmentManager(), R.id.home_container_fr_frame, new NotificationSettingFragment());
                break;
            case R.id.language_item:
                showSelectLanguageDialog();
                break;
            case R.id.logout_item:
                showExitAppConfirmationDialog();
                break;
        }
    }

    public void goToFavourite() {
        Bundle bundle = new Bundle();
        bundle.putInt(PostsAndFavoritesListFragment.EXTRA_FAV, 22);
        replaceFragment(getActivity().getSupportFragmentManager(), R.id.home_container_fr_frame, new PostsAndFavoritesListFragment(), bundle);
    }

    private void showExitAppConfirmationDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.exit_app_dialog_msg);
        builder.setPositiveButton(R.string.exit, (dialog, id) -> {
            String phone = loadUserData(getActivity()).getClient().getPhone();
            SaveData(getActivity(), getResources().getString(R.string.PHONE_USER_DATA_SHARED), phone);
            SaveData(getActivity(), getResources().getString(R.string.USER_DATA_SHARED), null);
            getActivity().finish();
        });
        builder.setNegativeButton(R.string.cancel, (dialog, id) -> {
            if (dialog != null) {
                dialog.dismiss();
            }
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void showSelectLanguageDialog() {

        final String[] lanList = {getResources().getString(R.string.arabic_key), getResources().getString(R.string.english_key)};
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.language);

        if (LoadData(getActivity(),LANG_NUM) != null){
            lanChoose = Integer.parseInt(LoadData(getActivity(),LANG_NUM));
        }

        builder.setSingleChoiceItems(lanList, lanChoose, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    //Arabic
                    if (LoadData(getActivity(),LANG_NUM) != null){
                        if (Integer.parseInt(LoadData(getActivity(),LANG_NUM)) == 0){
                            dialog.dismiss();
                            return;
                        }
                    }
                    SaveData(getActivity(), USER_LANG, "ar");
                } else if (which == 1) {
                    //English
                    if (LoadData(getActivity(),LANG_NUM) != null){
                        if (Integer.parseInt(LoadData(getActivity(),LANG_NUM)) == 1){
                            dialog.dismiss();
                            return;
                        }
                    }
                    SaveData(getActivity(), USER_LANG, "en");
                }
                SaveData(getActivity(),LANG_NUM,Integer.toString(which));
                dialog.dismiss();
                getActivity().startActivity(new Intent(getActivity(), HomeCycleActivity.class));
                getActivity().finish();


            }
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


}
