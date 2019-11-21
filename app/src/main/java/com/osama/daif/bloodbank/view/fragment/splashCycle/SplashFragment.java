package com.osama.daif.bloodbank.view.fragment.splashCycle;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.osama.daif.bloodbank.R;
import com.osama.daif.bloodbank.view.fragment.BaseFragment;

import java.util.Timer;
import java.util.TimerTask;

import static com.osama.daif.bloodbank.helper.HelperMethods.replaceFragment;

public class SplashFragment extends BaseFragment {

    public SplashFragment() {
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
        initFragment();
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {

                replaceFragment(getActivity().getSupportFragmentManager(), R.id.fragment_splash_container, new SliderFragment());

            }
        }, 2000);
        return inflater.inflate(R.layout.fragment_splash_screen, container, false);
    }

    @Override
    public void onBack() {
        baseActivity.finish();
    }
}
