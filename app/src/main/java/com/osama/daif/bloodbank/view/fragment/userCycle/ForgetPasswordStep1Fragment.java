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

import static com.osama.daif.bloodbank.helper.HelperMethods.replaceFragment;

public class ForgetPasswordStep1Fragment extends BaseFragment {

    @BindView(R.id.forget_password_fragment_txt_phone_number)
    AppCompatEditText txtPhoneNumber;
    @BindView(R.id.forget_password_fragment_btn_send)
    Button forgetPasswordBtnSend;
    private Unbinder unbinder = null;

    public ForgetPasswordStep1Fragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_forget_password_step1, container, false);
        unbinder = ButterKnife.bind(this, view);
        // Inflate the layout for this fragment
        initFragment();
        return view;
    }

    @Override
    public void onBack() {
        super.onBack();
    }

    @OnClick(R.id.forget_password_fragment_btn_send)
    public void onClick() {
        replaceFragment(getActivity().getSupportFragmentManager(), R.id.fragment_user_container, new ForgetPasswordStep2Fragment());
    }
}
