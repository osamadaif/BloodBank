package com.osama.daif.bloodbank.view.fragment.userCycle;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.appcompat.widget.AppCompatEditText;

import com.osama.daif.bloodbank.R;
import com.osama.daif.bloodbank.data.api.RetrofitClient;
import com.osama.daif.bloodbank.data.model.resetpassword.ResetPassword;
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
import static com.osama.daif.bloodbank.helper.HelperMethods.replaceFragment;
import static com.osama.daif.bloodbank.helper.HelperMethods.showSnackBar;

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
        final String phone = Objects.requireNonNull (txtPhoneNumber.getText ( )).toString().trim ();

        if (phone.isEmpty()) {
            txtPhoneNumber.setError (getString(R.string.enter_phone_number));
            return;
        }

        if (phone.length() < 11) {
            txtPhoneNumber.setError (getString(R.string.invalid_phone));
            return;
        }
        getClient ().forgetPassword (phone).enqueue (new Callback<ResetPassword> ( ) {
            @Override
            public void onResponse(Call<ResetPassword> call, Response<ResetPassword> response) {
                try {
                    assert response.body ( ) != null;
                    if (response.body().getStatus() == 1) {
                        ForgetPasswordStep2Fragment forgetPasswordStep2Fragment = new ForgetPasswordStep2Fragment();
                        forgetPasswordStep2Fragment.phone = phone;
                        replaceFragment(getActivity().getSupportFragmentManager(), R.id.fragment_user_container, forgetPasswordStep2Fragment);
                        showSnackBar (getActivity ().findViewById(android.R.id.content), response.body().getMsg());

                    } else {
                        showSnackBar (getActivity ().findViewById(android.R.id.content), response.body().getMsg());
                    }


                } catch (Exception e) {

                }
            }

            @Override
            public void onFailure(Call<ResetPassword> call, Throwable t) {
                showSnackBar (getActivity ().findViewById(android.R.id.content), t.getMessage ());
            }
        });

    }
}
