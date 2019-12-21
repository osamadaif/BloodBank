package com.osama.daif.bloodbank.view.fragment;

import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.osama.daif.bloodbank.R;
import com.osama.daif.bloodbank.view.activity.BaseActivity;
import com.osama.daif.bloodbank.view.activity.HomeCycleActivity;


public class BaseFragment extends Fragment {

    public BaseActivity baseActivity;
    public HomeCycleActivity homeCycleActivity;

    public void initFragment(){
        baseActivity = (BaseActivity) getActivity();
        baseActivity.baseFragment = this;
    }

    public void onBack(){
        baseActivity.superBackPressed();
    }


}
