package com.osama.daif.bloodbank.view.fragment.userCycle;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.appcompat.widget.AppCompatEditText;

import com.osama.daif.bloodbank.R;
import com.osama.daif.bloodbank.data.model.city.GeneralResponse;
import com.osama.daif.bloodbank.view.fragment.BaseFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.osama.daif.bloodbank.data.api.RetrofitClient.getClient;
import static com.osama.daif.bloodbank.helper.HelperMethods.replaceFragment;
import static com.osama.daif.bloodbank.helper.HelperMethods.showSnackBar;

public class ForgetPasswordStep2Fragment extends BaseFragment {

    @BindView(R.id.forget_password2_fragment_txt_code)
    AppCompatEditText txtCode;
    @BindView(R.id.forget_password2_fragment_txt_new_password)
    AppCompatEditText txtNewPassword;
    @BindView(R.id.forget_password2_fragment_txt_confirm_new_password)
    AppCompatEditText txtConfirmNewPassword;
    @BindView(R.id.forget_password2_fragment_btn_change)
    Button btnChange;

    public String phone;

    private Unbinder unbinder = null;

    public ForgetPasswordStep2Fragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate (R.layout.fragment_forget_password_step2, container, false);
        unbinder = ButterKnife.bind (this, view);
        // Inflate the layout for this fragment
        initFragment ( );
        return view;
    }

    @Override
    public void onBack() {
        super.onBack ( );
    }

    @OnClick(R.id.forget_password2_fragment_btn_change)
    public void onClick() {
        String pin_code = txtCode.getText ( ).toString ( );
        String password = txtNewPassword.getText ( ).toString ( );
        String password_confirmation = txtConfirmNewPassword.getText ( ).toString ( );
        if (pin_code.isEmpty ( ) && password.isEmpty ( ) && password_confirmation.isEmpty ( )) {
            return;
        }
        if (pin_code.isEmpty ( )) {
            showSnackBar (getActivity ( ).findViewById (android.R.id.content), getString (R.string.enter_pin_code));
            txtCode.setError (getString (R.string.enter_pin_code));
            return;
        }
        if (password.isEmpty ( )) {
            showSnackBar (getActivity ( ).findViewById (android.R.id.content), getString (R.string.enter_pin_code));
            txtNewPassword.setError (getString (R.string.enter_password));
            return;
        }
        if (password.length ( ) < 3) {
            txtNewPassword.setError (getString (R.string.weak_password));
            return;
        }
        if (password_confirmation.isEmpty ( )) {
            txtNewPassword.setError (getString (R.string.enter_password_confirm));

            return;
        }
        if (!password_confirmation.equals (password)) {
            showSnackBar (getActivity ( ).findViewById (android.R.id.content), getString (R.string.password_and_confirm_not_match));
            return;
        }
        getClient ( ).newPassword (pin_code, password, password_confirmation, phone).enqueue (new Callback<GeneralResponse> ( ) {
            @Override
            public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {
                try {
                    if (response.body ( ).getStatus ( ) == 1) {
                        LoginFragment loginFragment = new LoginFragment ( );
                        replaceFragment (getActivity ( ).getSupportFragmentManager ( ), R.id.fragment_user_container, loginFragment);
                        showSnackBar (getActivity ( ).findViewById (android.R.id.content), response.body ( ).getMsg ( ));

                    } else {
                        showSnackBar (getActivity ( ).findViewById (android.R.id.content), response.body ( ).getMsg ( ));
                    }


                } catch (Exception e) {

                }
            }

            @Override
            public void onFailure(Call<GeneralResponse> call, Throwable t) {
                showSnackBar (getActivity ( ).findViewById (android.R.id.content), t.getMessage ( ));
            }
        });

    }
}
