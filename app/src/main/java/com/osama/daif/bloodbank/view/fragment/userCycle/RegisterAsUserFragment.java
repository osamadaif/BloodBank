package com.osama.daif.bloodbank.view.fragment.userCycle;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatEditText;

import com.google.android.material.textfield.TextInputLayout;
import com.osama.daif.bloodbank.R;
import com.osama.daif.bloodbank.adapter.CustomSpinnerAdapter;
import com.osama.daif.bloodbank.adapter.SpinnerAdapter2;
import com.osama.daif.bloodbank.data.model.DateTxt;
import com.osama.daif.bloodbank.data.model.SpinnerItem;
import com.osama.daif.bloodbank.data.model.city.GeneralResponse;
import com.osama.daif.bloodbank.data.model.register.Register;
import com.osama.daif.bloodbank.view.fragment.BaseFragment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.osama.daif.bloodbank.data.api.RetrofitClient.getClient;
import static com.osama.daif.bloodbank.helper.GeneralRequest.getData;
import static com.osama.daif.bloodbank.helper.HelperMethods.replaceFragment;
import static com.osama.daif.bloodbank.helper.HelperMethods.showCalender;

public class RegisterAsUserFragment extends BaseFragment {

    @BindView(R.id.registers_and_edit_profile_fragment_txt_user_name)
    AppCompatEditText txtUserName;
    @BindView(R.id.registers_and_edit_profile_fragment_txt_email)
    AppCompatEditText txtEmail;
    @BindView(R.id.register_and_edit_profile_fragment_til_sp_blood_type)
    Spinner spBloodType;
    @BindView(R.id.register_and_edit_profile_fragment_til_sp_government)
    Spinner spGovernment;
    @BindView(R.id.register_and_edit_profile_fragment_til_sp_city)
    Spinner spCity;
    @BindView(R.id.registers_and_edit_profile_fragment_txt_phone_number)
    AppCompatEditText txtPhoneNumber;
    @BindView(R.id.registers_and_edit_profile_fragment_txt_password)
    AppCompatEditText txtPassword;
    @BindView(R.id.registers_and_edit_profile_fragment_txt_confirm_password)
    AppCompatEditText txtConfirmPassword;
    @BindView(R.id.registers_and_edit_profile_fragment_btn_register)
    Button btnRegister;
    @BindView(R.id.registers_and_edit_profile_fragment_title_user_name)
    TextInputLayout registersAndEditProfileFragmentTitleUserName;
    @BindView(R.id.registers_and_edit_profile_fragment_email)
    TextInputLayout registersAndEditProfileFragmentEmail;
    @BindView(R.id.registers_and_edit_profile_fragment_title_phone_number)
    TextInputLayout registersAndEditProfileFragmentTitlePhoneNumber;
    @BindView(R.id.registers_and_edit_profile_fragment_title_password)
    TextInputLayout registersAndEditProfileFragmentTitlePassword;
    @BindView(R.id.registers_and_edit_profile_fragment_title_confirm_password)
    TextInputLayout registersAndEditProfileFragmentTitleConfirmPassword;
    @BindView(R.id.registers_and_edit_profile_fragment_birthday)
    EditText dateBirthday;
    @BindView(R.id.register_edt_Last_donation_d)
    EditText dateLastDonationD;
    private Unbinder unbinder = null;

    private DatePickerDialog datePickerDialog;

    private int year;
    private int month;
    private int day;

    private SpinnerAdapter2 bloodTypeAdapter, governoratesAdapter, cityAdapter;

    public RegisterAsUserFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register_as_user, container, false);
        unbinder = ButterKnife.bind(this, view);
        // Inflate the layout for this fragment
        initFragment();

        bloodTypeAdapter = new SpinnerAdapter2(getActivity());
        getData(getClient().getBloodTypes(), bloodTypeAdapter, "blood type", spBloodType);

        governoratesAdapter = new SpinnerAdapter2(getActivity());
        AdapterView.OnItemSelectedListener listener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {

                    cityAdapter = new SpinnerAdapter2(getActivity());
                    getData(getClient().getCities(governoratesAdapter.selectedId), cityAdapter, "city", spCity);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };
        getData(getClient().getGovernorates(), governoratesAdapter, "Governorates", spGovernment, listener);
        return view;
    }

    @Override
    public void onBack() {
        //super.onBack();
        replaceFragment(getActivity().getSupportFragmentManager(), R.id.fragment_user_container, new LoginFragment());
    }

    @OnClick(R.id.registers_and_edit_profile_fragment_btn_register)
    public void onClick() {
        setRegisterData();
    }

    @OnClick({R.id.registers_and_edit_profile_fragment_birthday, R.id.register_edt_Last_donation_d})
    public void onClick(View view) {
        Calendar calendar = Calendar.getInstance();

        switch (view.getId()) {
            case R.id.registers_and_edit_profile_fragment_birthday:

                DateTxt dateTxt = new DateTxt("01", "01", "1980", "01-01-1980");
                showCalender(getActivity(), getResources().getString(R.string.birthday), dateBirthday, dateTxt);
                break;

            case R.id.register_edt_Last_donation_d:
                year = calendar.get(Calendar.YEAR);
                String sYear = String.valueOf(year);
                month = calendar.get(Calendar.MONTH);
                String sMonth = String.valueOf(month);
                day = calendar.get(Calendar.DAY_OF_MONTH);
                String sDay = String.valueOf(day);

                String txt = day + "/" + (month + 1) + "/" + year;
                DateTxt dateTxt2 = new DateTxt(sDay, sMonth, sYear, txt);
                showCalender(getActivity(), getResources().getString(R.string.last_donation), dateLastDonationD, dateTxt2);

                break;
        }
    }

    private void setRegisterData() {
        String userName = txtUserName.getText().toString().trim();
        if (userName.trim().equals("")||txtUserName.getText ( ) == null || txtUserName.getText ( ).length ( ) == 0) {
            //   txt_phone_number.setError (getResources ( ).getString (R.string.Please_insert_number));
            return;
        }

        //String userName = txtUserName.getText().toString().trim();
        String email = txtEmail.getText().toString().trim();
        String phoneNumber = txtPhoneNumber.getText().toString().trim();
        String password = txtPassword.getText().toString().trim();
        String confirmPassword = txtConfirmPassword.getText().toString().trim();
        String birthday = dateBirthday.getText().toString().trim();
        String lastDonationDate = dateLastDonationD.getText().toString().trim();
        int bloodType = bloodTypeAdapter.selectedId;
        int governorate = governoratesAdapter.selectedId;
        int city = cityAdapter.selectedId;

        getClient().getRegister(userName, email, birthday, city, phoneNumber, lastDonationDate, password, confirmPassword, bloodType).enqueue(new Callback<Register>() {
            @Override
            public void onResponse(Call<Register> call, Response<Register> response) {
                try {
                    Toast.makeText(baseActivity, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                } catch(Exception e) {
                    Toast.makeText(baseActivity, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Register> call, Throwable t) {
                Toast.makeText(baseActivity, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


}
