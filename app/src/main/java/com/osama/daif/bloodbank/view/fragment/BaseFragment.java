package com.osama.daif.bloodbank.view.fragment;

import androidx.fragment.app.Fragment;
import com.osama.daif.bloodbank.view.activity.BaseActivity;


public class BaseFragment extends Fragment {

    public BaseActivity baseActivity;

    public void initFragment(){
        baseActivity = (BaseActivity) getActivity();
    }

    public void onBack(){

    }
}
