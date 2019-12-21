package com.osama.daif.bloodbank.view.fragment.homeCycle.staticScreen;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.material.appbar.AppBarLayout;
import com.osama.daif.bloodbank.R;
import com.osama.daif.bloodbank.data.model.settings.Settings;
import com.osama.daif.bloodbank.view.activity.HomeCycleActivity;
import com.osama.daif.bloodbank.view.fragment.BaseFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.osama.daif.bloodbank.data.api.RetrofitClient.getClient;
import static com.osama.daif.bloodbank.data.local.SharedPreferencesManger.loadUserData;

public class AboutAppFragment extends BaseFragment {

    @BindView(R.id.fragment_about_app_tv_content)
    TextView fragmentAboutAppTvContent;
    @BindView(R.id.fragment_about_app_progress_bar)
    ProgressBar fragmentAboutAppProgressBar;
    private Unbinder unbinder = null;

    HomeCycleActivity homeCycleActivity;
    public AboutAppFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate (R.layout.fragment_about_app, container, false);
        unbinder = ButterKnife.bind(this, view);
        homeCycleActivity = (HomeCycleActivity) getActivity();
        assert homeCycleActivity != null;
        homeCycleActivity.editToolbarTxtSup(R.string.about_application);
        homeCycleActivity.appbarVisibility (View.VISIBLE);
        homeCycleActivity.setBackBtnVisibility (View.VISIBLE);
        homeCycleActivity.bottomNavigationVisibility(View.GONE);
        // Inflate the layout for this fragment
        initFragment ( );
        getAboutAppContent ( );
        return view;
    }

    private void getAboutAppContent() {
        fragmentAboutAppProgressBar.setVisibility (View.VISIBLE);
        String apiToken = loadUserData (getActivity ( )).getApiToken ( );
        getClient ( ).getSetting (apiToken).enqueue (new Callback<Settings> ( ) {
            @Override
            public void onResponse(Call<Settings> call, Response<Settings> response) {
                if (response.body ( ).getStatus ( ) == 1) {
                    fragmentAboutAppProgressBar.setVisibility (View.GONE);
                    fragmentAboutAppTvContent.setText (response.body ( ).getData ( ).getAboutApp ());
                }
            }

            @Override
            public void onFailure(Call<Settings> call, Throwable t) {

            }
        });
    }

    @Override
    public void onBack() {
        super.onBack ( );
    }
}
