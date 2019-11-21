package com.osama.daif.bloodbank.view.fragment.userCycle;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.appcompat.widget.AppCompatEditText;

import com.osama.daif.bloodbank.R;
import com.osama.daif.bloodbank.view.fragment.BaseFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class ForgetPasswordStep2Fragment extends BaseFragment {

    @BindView(R.id.forget_password2_fragment_txt_code)
    AppCompatEditText txtCode;
    @BindView(R.id.forget_password2_fragment_txt_new_password)
    AppCompatEditText txtNewPassword;
    @BindView(R.id.forget_password2_fragment_txt_confirm_new_password)
    AppCompatEditText txtConfirmNewPassword;
    @BindView(R.id.forget_password2_fragment_btn_change)
    Button btnChange;
    private Unbinder unbinder = null;

    public ForgetPasswordStep2Fragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_forget_password_step2, container, false);
        unbinder = ButterKnife.bind(this, view);
        // Inflate the layout for this fragment
        initFragment();
        return view;
    }

    @Override
    public void onBack() {
        super.onBack();
    }

    @OnClick(R.id.forget_password2_fragment_btn_change)
    public void onClick() {
    }
}
