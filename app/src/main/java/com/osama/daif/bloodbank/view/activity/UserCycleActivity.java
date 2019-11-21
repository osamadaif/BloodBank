package com.osama.daif.bloodbank.view.activity;


import com.osama.daif.bloodbank.R;

import static com.osama.daif.bloodbank.helper.HelperMethods.replaceFragment;

import com.osama.daif.bloodbank.view.fragment.userCycle.LoginFragment;

import android.os.Bundle;

public class UserCycleActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_cycle);

        replaceFragment(getSupportFragmentManager(), R.id.fragment_user_container, new LoginFragment());
    }
}