package com.osama.daif.bloodbank.view.activity;

import android.os.Bundle;
import android.widget.Toast;

import com.osama.daif.bloodbank.R;
import com.osama.daif.bloodbank.data.local.SharedPreferencesManger;
import com.osama.daif.bloodbank.helper.HelperMethods;
import com.osama.daif.bloodbank.view.fragment.splashCycle.SplashFragment;

import static com.osama.daif.bloodbank.helper.HelperMethods.replaceFragment;


public class SplashCycleActivity extends BaseActivity {
    private long backPressedTime;
    private Toast backToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        HelperMethods.changeLang(this, SharedPreferencesManger.onLoadLang(this));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_cycle);

        replaceFragment(getSupportFragmentManager(), R.id.fragment_splash_container, new SplashFragment());

    }
}
