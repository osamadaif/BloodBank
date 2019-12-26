package com.osama.daif.bloodbank.view.fragment.homeCycle.notification;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AlertDialog;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.osama.daif.bloodbank.R;
import com.osama.daif.bloodbank.adapter.NotificationSettingAdapter;
import com.osama.daif.bloodbank.data.model.city.GeneralResponse;
import com.osama.daif.bloodbank.data.model.notificationSetting.NotificationSetting;
import com.osama.daif.bloodbank.view.activity.HomeCycleActivity;
import com.osama.daif.bloodbank.view.fragment.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.osama.daif.bloodbank.data.api.RetrofitClient.getClient;
import static com.osama.daif.bloodbank.data.local.SharedPreferencesManger.loadUserData;
import static com.osama.daif.bloodbank.helper.HelperMethods.collapse;
import static com.osama.daif.bloodbank.helper.HelperMethods.expand;
import static com.osama.daif.bloodbank.helper.HelperMethods.showSnackBar;

public class NotificationSettingFragment extends BaseFragment {
    @BindView(R.id.notification_settings_fragment_progress_bar)
    ProgressBar notificationSettingsFragmentProgressBar;
    @BindView(R.id.notification_settings_fragment_rv_blood_types)
    RecyclerView recyclerViewBloodTypes;
    @BindView(R.id.notification_settings_fragment_rel_bloods_gone)
    RelativeLayout notificationSettingsFragmentRelBloodsGone;
    @BindView(R.id.notification_settings_fragment_iv_bloods_bar_img)
    ImageView notificationSettingsFragmentIvBloodsBarImg;
    @BindView(R.id.notification_settings_fragment_rel_bloods_bar)
    RelativeLayout notificationSettingsFragmentRelBloodsBar;
    @BindView(R.id.notification_settings_fragment_rv_government)
    RecyclerView recyclerViewGovernment;
    @BindView(R.id.notification_settings_fragment_rel_government_gone)
    RelativeLayout notificationSettingsFragmentRelGovernmentGone;
    @BindView(R.id.notification_settings_fragment_iv_government_bar_img)
    ImageView notificationSettingsFragmentIvGovernmentBarImg;
    @BindView(R.id.notification_settings_fragment_rel_government_bar)
    RelativeLayout notificationSettingsFragmentRelGovernmentBar;
    @BindView(R.id.nested_scroll_view)
    NestedScrollView nestedScrollView;
    @BindView(R.id.notification_settings_fragment_btn_save)
    Button notificationSettingsFragmentBtnSave;

//    @BindView(R.id.notification_settings_fragment_rv_blood_types)
//    RecyclerView recyclerViewBloodTypes;
//    @BindView(R.id.notification_settings_fragment_rel_bloods_gone)
//    RelativeLayout notificationSettingsFragmentRelBloodsGone;
//    @BindView(R.id.notification_settings_fragment_iv_bloods_bar_img)
//    ImageView notificationSettingsFragmentIvBloodsBarImg;
//    @BindView(R.id.notification_settings_fragment_rel_bloods_bar)
//    RelativeLayout notificationSettingsFragmentRelBloodsBar;
//    @BindView(R.id.notification_settings_fragment_rv_government)
//    RecyclerView recyclerViewGovernment;
//    @BindView(R.id.notification_settings_fragment_rel_government_gone)
//    RelativeLayout notificationSettingsFragmentRelGovernmentGone;
//    @BindView(R.id.notification_settings_fragment_iv_government_bar_img)
//    ImageView notificationSettingsFragmentIvGovernmentBarImg;
//    @BindView(R.id.notification_settings_fragment_rel_government_bar)
//    RelativeLayout notificationSettingsFragmentRelGovernmentBar;
//    @BindView(R.id.notification_settings_fragment_btn_save)
//    Button notificationSettingsFragmentBtnSave;
//    @BindView(R.id.nested_scroll_view)
//    NestedScrollView nestedScrollView;
//    @BindView(R.id.notification_settings_fragment_progress_bar)
//    ProgressBar notificationSettingsFragmentProgressBar;

    private String apiToken;

    private Unbinder unbinder = null;

    private HomeCycleActivity homeCycleActivity;

    private List<String> governorates = new ArrayList<> ( ), bloodTypes = new ArrayList<> ( );
    private NotificationSettingAdapter bloodAdapter, governmentAdapter;

    public NotificationSettingFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate (R.layout.fragment_notification_setting, container, false);
        unbinder = ButterKnife.bind (this, view);
        initRecyclerView ( );
        // Inflate the layout for this fragment
        initFragment ( );
        homeCycleActivity = (HomeCycleActivity) getActivity ( );
        assert homeCycleActivity != null;
        homeCycleActivity.editToolbarTxtSup (R.string.notification_setting);
        homeCycleActivity.setBackBtnVisibility (View.VISIBLE);
        homeCycleActivity.onBackBtnClick (v -> getActivity ( ).onBackPressed ( ));
        homeCycleActivity.appbarVisibility (View.VISIBLE);
        homeCycleActivity.bottomNavigationVisibility (View.GONE);
        apiToken = loadUserData (getActivity ( )).getApiToken ( );

        if (bloodTypes.size ( ) == 0 || governorates.size ( ) == 0) {
            notificationSettingsFragmentProgressBar.setVisibility (View.VISIBLE);
            getNotificationSetting ( );
        }

        notificationSettingsFragmentBtnSave.setOnClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick(View v) {
                getClient ( ).setNotificationSetting (apiToken, governmentAdapter.newIds, bloodAdapter.newIds).enqueue (new Callback<NotificationSetting> ( ) {
                    @Override
                    public void onResponse(Call<NotificationSetting> call, Response<NotificationSetting> response) {
                        try {
                            if (response.body ( ).getStatus ( ) == 1) {
//                                Toast.makeText (homeCycleActivity, response.body ( ).getMsg ( ), Toast.LENGTH_SHORT).show ( );
                                showSnackBar (v, response.body ( ).getMsg ( ));
//                                showSnackBarMargin (snackbar, 32, 32);
//                                getActivity ( ).onBackPressed ( );
                            }

                        } catch (Exception e) {

                        }
                    }

                    @Override
                    public void onFailure(Call<NotificationSetting> call, Throwable t) {

                    }
                });
            }
        });
        return view;
    }

    private void initRecyclerView() {
        recyclerViewBloodTypes.setLayoutManager (new GridLayoutManager (getActivity ( ), 3));
        recyclerViewGovernment.setLayoutManager (new GridLayoutManager (getActivity ( ), 3));
    }

    private void getNotificationSetting() {
        getClient ( ).getNotificationSetting (apiToken).enqueue (new Callback<NotificationSetting> ( ) {
            @Override
            public void onResponse(Call<NotificationSetting> call, Response<NotificationSetting> response) {
                try {
                    if (response.body ( ).getStatus ( ) == 1) {
                        notificationSettingsFragmentProgressBar.setVisibility (View.GONE);
                        bloodTypes = response.body ( ).getData ( ).getBloodTypes ( );
                        governorates = response.body ( ).getData ( ).getGovernorates ( );
                        getBloodTypes ( );
                        getGovernorates ( );
                    }

                } catch (Exception e) {
                    notificationSettingsFragmentProgressBar.setVisibility (View.GONE);
                }
            }

            @Override
            public void onFailure(Call<NotificationSetting> call, Throwable t) {
                notificationSettingsFragmentProgressBar.setVisibility (View.GONE);
                showNoConnectionDialog ( );
            }
        });
    }

    private void getBloodTypes() {
        getClient ( ).getBloodTypes ( ).enqueue (new Callback<GeneralResponse> ( ) {
            @Override
            public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {
                try {

                    bloodAdapter = new NotificationSettingAdapter (getActivity ( ), getActivity ( ),
                            response.body ( ).getData ( ), bloodTypes);
                    recyclerViewBloodTypes.setAdapter (bloodAdapter);

                } catch (Exception e) {

                }
            }

            @Override
            public void onFailure(Call<GeneralResponse> call, Throwable t) {

            }
        });
    }

    private void getGovernorates() {
        getClient ( ).getGovernorates ( ).enqueue (new Callback<GeneralResponse> ( ) {
            @Override
            public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {
                governmentAdapter = new NotificationSettingAdapter (getActivity ( ), getActivity ( ),
                        response.body ( ).getData ( ), governorates);
                recyclerViewGovernment.setAdapter (governmentAdapter);
            }

            @Override
            public void onFailure(Call<GeneralResponse> call, Throwable t) {

            }
        });
    }


    @Override
    public void onBack() {
        super.onBack ( );
    }


    private void visible(View view, ImageView imageView) {
        if (view.getVisibility ( ) == View.GONE) {
            expand (view);
            imageView.animate ( ).setDuration (200).rotation (180);
            imageView.setImageResource (R.drawable.ic_minus_solid);
        } else {
            imageView.animate ( ).setDuration (200).rotation (0);
            imageView.setImageResource (R.drawable.ic_plus_solid);
            collapse (view);
        }
    }

    private void showNoConnectionDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder (getActivity ( ));
        builder.setMessage (R.string.no_connection);
        builder.setPositiveButton (R.string.done, (dialog, id) -> {
            if (dialog != null) {
                dialog.dismiss ( );
                homeCycleActivity.setSelection (R.id.nav_home);
            }
        });
        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create ( );
        alertDialog.show ( );
    }

    @OnClick({R.id.notification_settings_fragment_rel_bloods_bar, R.id.notification_settings_fragment_rel_government_bar, R.id.notification_settings_fragment_btn_save})
    public void onClick(View view) {
        switch (view.getId ( )) {
            case R.id.notification_settings_fragment_rel_bloods_bar:
                visible (notificationSettingsFragmentRelBloodsGone, notificationSettingsFragmentIvBloodsBarImg);
                break;
            case R.id.notification_settings_fragment_rel_government_bar:
                visible (notificationSettingsFragmentRelGovernmentGone, notificationSettingsFragmentIvGovernmentBarImg);
                break;
//            case R.id.notification_settings_fragment_btn_save:
//                getClient ( ).setNotificationSetting (apiToken, governmentAdapter.newIds, bloodAdapter.newIds).enqueue (new Callback<NotificationSetting> ( ) {
//                    @Override
//                    public void onResponse(Call<NotificationSetting> call, Response<NotificationSetting> response) {
//                        try {
//                            if (response.body ( ).getStatus ( ) == 1) {
////                                Toast.makeText (homeCycleActivity, response.body ( ).getMsg ( ), Toast.LENGTH_SHORT).show ( );
//                                Snackbar snackbar = getSnackBar (view,response.body ( ).getMsg ( ));
//                                showSnackBarMargin (snackbar, 32, 32);
//                                getActivity ( ).onBackPressed ( );
//                            }
//
//                        } catch (Exception e) {
//
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(Call<NotificationSetting> call, Throwable t) {
//
//                    }
//                });
//                break;
        }
    }

}
