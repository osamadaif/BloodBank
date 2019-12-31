package com.osama.daif.bloodbank.view.fragment.userCycle;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatEditText;

import com.osama.daif.bloodbank.R;
import com.osama.daif.bloodbank.data.local.SharedPreferencesManger;
import com.osama.daif.bloodbank.data.model.login.Login;
import com.osama.daif.bloodbank.helper.HelperMethods;
import com.osama.daif.bloodbank.view.activity.HomeCycleActivity;
import com.osama.daif.bloodbank.view.fragment.BaseFragment;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.osama.daif.bloodbank.data.api.RetrofitClient.getClient;
import static com.osama.daif.bloodbank.data.local.SharedPreferencesManger.LoadBoolean;
import static com.osama.daif.bloodbank.data.local.SharedPreferencesManger.LoadData;
import static com.osama.daif.bloodbank.data.local.SharedPreferencesManger.SaveData;
import static com.osama.daif.bloodbank.data.local.SharedPreferencesManger.USER_PASSWORD;
import static com.osama.daif.bloodbank.data.local.SharedPreferencesManger.loadUserData;
import static com.osama.daif.bloodbank.helper.HelperMethods.replaceFragment;

public class LoginFragment extends BaseFragment {
    public static final String TAG = "LoginFragment";

    @BindView(R.id.login_fragment_txt_phone_number)
    AppCompatEditText txt_phone_number;
    @BindView(R.id.login_fragment_txt_password)
    AppCompatEditText txt_password;
    @BindView(R.id.login_fragment_ckb_remember_me)
    CheckBox ckb_remember_me;
    @BindView(R.id.login_fragment_txt_forget_password)
    TextView txt_forget_password;
    @BindView(R.id.login_fragment_btn_enter)
    Button btn_enter;
    @BindView(R.id.login_fragment_btn_create_account)
    Button btn_create_account;
    private Unbinder unbinder = null;

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_login, container, false);
        unbinder = ButterKnife.bind(this, view);
        // Inflate the layout for this fragment
        if (LoadBoolean(getActivity(), getResources().getString(R.string.remember_me_instance))) {
            if (LoadData(getActivity(),getResources ( ).getString (R.string.PHONE_USER_DATA_SHARED)) != null) {
                txt_phone_number.setText(LoadData(getActivity(),getResources ( ).getString (R.string.PHONE_USER_DATA_SHARED)));
            }

            ckb_remember_me.setChecked(true);
        }
        initFragment();
        return view;
    }

    @Override
    public void onBack() {
//        super.onBack ( );
        Objects.requireNonNull(getActivity()).finish();
    }

    @OnClick({R.id.login_fragment_ckb_remember_me, R.id.login_fragment_txt_forget_password, R.id.login_fragment_btn_enter, R.id.login_fragment_btn_create_account})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login_fragment_ckb_remember_me:

                break;

            case R.id.login_fragment_txt_forget_password:
                replaceFragment(getActivity().getSupportFragmentManager(), R.id.fragment_user_container, new ForgetPasswordStep1Fragment());
                break;

            case R.id.login_fragment_btn_enter:
                getLogin();
                break;

            case R.id.login_fragment_btn_create_account:
                replaceFragment(getActivity().getSupportFragmentManager(), R.id.fragment_user_container, new RegisterAsUserFragment());
                break;
        }
    }

    private void getLogin() {
        String phoneNumber = txt_phone_number.getText().toString().trim();
        String password = txt_password.getText().toString().trim();

        getClient().getLogin(phoneNumber, password).enqueue(new Callback<Login>() {
            @Override
            public void onResponse(Call<Login> call, Response<Login> response) {
                try {
                    assert response.body() != null;
                    if (txt_phone_number.getText() == null || txt_phone_number.getText().length() != 11) {

                        txt_phone_number.setError(response.body().getMsg());

                    }
                    if (txt_password.getText() == null || txt_password.getText().length() < 3) {
                        txt_password.setError(response.body().getMsg());

                    }
                    if (response.body().getStatus() == 1) {
                        startActivity(new Intent(baseActivity.getApplicationContext(), HomeCycleActivity.class));
                        getActivity().finish();
                        SaveData (getActivity(), USER_PASSWORD, password);
                        SaveData(getActivity(), getResources().getString(R.string.USER_DATA_SHARED), response.body().getData());
                        if (ckb_remember_me.isChecked()) {
                            SharedPreferencesManger.SaveData(getActivity(), getResources().getString(R.string.remember_me_instance), true);
                        }
                    } else {

//                        Toast.makeText(baseActivity, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                        HelperMethods.showSnackBar (getActivity ( ).findViewById (android.R.id.content),response.body().getMsg());
                    }


                } catch (Exception e) {

                }
            }

            @Override
            public void onFailure(Call<Login> call, Throwable t) {
                try {
//                    Toast.makeText(baseActivity, t.getMessage(), Toast.LENGTH_SHORT).show();
                    HelperMethods.showSnackBar (getActivity ( ).findViewById (android.R.id.content),t.getMessage());

                } catch (Exception e) {

                }
            }
        });
    }
}
