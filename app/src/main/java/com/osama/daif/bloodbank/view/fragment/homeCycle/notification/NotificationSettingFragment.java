package com.osama.daif.bloodbank.view.fragment.homeCycle.notification;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

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

public class NotificationSettingFragment extends BaseFragment {

    private String apiToken;
    @BindView(R.id.notification_settings_fragment_rv_blood_types)
    RecyclerView recyclerViewBloodTypes;
    @BindView(R.id.notification_settings_fragment_iv_bloods_bar_img)
    ImageView notificationSettingsFragmentIvBloodsBarImg;
    @BindView(R.id.notification_settings_fragment_rel_bloods_bar)
    RelativeLayout notificationSettingsFragmentRelBloodsBar;
    @BindView(R.id.notification_settings_fragment_rv_government)
    RecyclerView recyclerViewGovernment;
    @BindView(R.id.notification_settings_fragment_iv_government_bar_img)
    ImageView notificationSettingsFragmentIvGovernmentBarImg;
    @BindView(R.id.notification_settings_fragment_rel_government_bar)
    RelativeLayout notificationSettingsFragmentRelGovernmentBar;
    @BindView(R.id.notification_settings_fragment_btn_save)
    Button notificationSettingsFragmentBtnSave;
    @BindView(R.id.notification_settings_fragment_rel_bloods_gone)
    RelativeLayout notificationSettingsFragmentRelBloodsGone;
    @BindView(R.id.notification_settings_fragment_rel_government_gone)
    RelativeLayout notificationSettingsFragmentRelGovernmentGone;

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
        homeCycleActivity = (HomeCycleActivity) getActivity();
        assert homeCycleActivity != null;
        homeCycleActivity.editToolbarTxtSup(R.string.notification_setting);
        homeCycleActivity.bottomNavigationVisibility (View.GONE);
        apiToken = loadUserData (getActivity ( )).getApiToken ( );

        getNotificationSetting ( );
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
                        bloodTypes = response.body ( ).getData ( ).getBloodTypes ( );
                        governorates = response.body ( ).getData ( ).getGovernorates ( );
                        getBloodTypes ( );
                        getGovernorates ( );
                    }

                } catch (Exception e) {

                }
            }

            @Override
            public void onFailure(Call<NotificationSetting> call, Throwable t) {

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
            view.setVisibility (View.VISIBLE);
            imageView.setImageResource (R.drawable.ic_minus_solid);
        } else {
            imageView.setImageResource (R.drawable.ic_plus_solid);
            view.setVisibility (View.GONE);
        }
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
            case R.id.notification_settings_fragment_btn_save:
                getClient ( ).setNotificationSetting (apiToken,governmentAdapter.newIds,bloodAdapter.newIds).enqueue (new Callback<NotificationSetting> ( ) {
                    @Override
                    public void onResponse(Call<NotificationSetting> call, Response<NotificationSetting> response) {
                        try {
                            if (response.body ( ).getStatus ( ) == 1) {
                                Toast.makeText (homeCycleActivity, response.body ().getMsg (), Toast.LENGTH_SHORT).show ( );
                            }

                        } catch (Exception e) {

                        }
                    }

                    @Override
                    public void onFailure(Call<NotificationSetting> call, Throwable t) {

                    }
                });
                break;
        }
    }
}
