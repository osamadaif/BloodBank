package com.osama.daif.bloodbank.view.fragment.homeCycle.post;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.osama.daif.bloodbank.R;
import com.osama.daif.bloodbank.view.fragment.BaseFragment;

public class PostDetailsFragment extends BaseFragment {

    public PostDetailsFragment() {
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
        return inflater.inflate(R.layout.fragment_blank, container, false);
    }

    @Override
    public void onBack() {
        super.onBack();
    }
}
