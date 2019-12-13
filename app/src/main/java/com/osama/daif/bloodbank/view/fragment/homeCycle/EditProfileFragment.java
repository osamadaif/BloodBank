package com.osama.daif.bloodbank.view.fragment.homeCycle;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatEditText;

import com.osama.daif.bloodbank.R;
import com.osama.daif.bloodbank.adapter.SpinnerAdapter2;
import com.osama.daif.bloodbank.data.local.SharedPreferencesManger;
import com.osama.daif.bloodbank.data.model.DateTxt;
import com.osama.daif.bloodbank.data.model.register.Register;
import com.osama.daif.bloodbank.view.activity.HomeCycleActivity;
import com.osama.daif.bloodbank.view.fragment.BaseFragment;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.osama.daif.bloodbank.data.api.RetrofitClient.getClient;
import static com.osama.daif.bloodbank.data.local.SharedPreferencesManger.loadUserData;
import static com.osama.daif.bloodbank.helper.GeneralRequest.getData;
import static com.osama.daif.bloodbank.helper.HelperMethods.showCalender;

public class EditProfileFragment extends BaseFragment {

    @BindView(R.id.edit_profile_fragment_txt_user_name)
    AppCompatEditText editProfileFragmentTxtUserName;
    @BindView(R.id.edit_profile_fragment_txt_email)
    AppCompatEditText editProfileFragmentTxtEmail;
    @BindView(R.id.edit_profile_fragment_birthday)
    EditText editProfileFragmentBirthday;
    @BindView(R.id.edit_profile_fragment_sp_blood_type)
    Spinner spBloodType;
    @BindView(R.id.edit_profile_fragment_Last_donation_d)
    EditText editProfileFragmentLastDonationD;
    @BindView(R.id.edit_profile_fragment_sp_government)
    Spinner spGovernment;
    @BindView(R.id.edit_profile_fragment_sp_city)
    Spinner spCity;
    @BindView(R.id.edit_profile_fragment_txt_phone_number)
    AppCompatEditText editProfileFragmentTxtPhoneNumber;
    @BindView(R.id.edit_profile_fragment_txt_password)
    AppCompatEditText editProfileFragmentTxtPassword;
    @BindView(R.id.edit_profile_fragment_txt_confirm_password)
    AppCompatEditText editProfileFragmentTxtConfirmPassword;
    @BindView(R.id.edit_profile_fragment_btn_register)
    Button editProfileFragmentBtnRegister;
    @BindView(R.id.edit_profile_fragment_lay_base_layout)
    LinearLayout editProfileFragmentLayBaseLayout;
    @BindView(R.id.edit_profile_fragment_sv_base_scroll_view)
    ScrollView editProfileFragmentSvBaseScrollView;

    private Unbinder unbinder = null;

    private DatePickerDialog datePickerDialog;

    private String apiToken;

    private int year;
    private int month;
    private int day;

    private HomeCycleActivity homeCycleActivity;

    private SpinnerAdapter2 bloodTypeAdapter, governoratesAdapter, cityAdapter;

    public EditProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);
        unbinder = ButterKnife.bind(this, view);
        // Inflate the layout for this fragment
        initFragment();
        homeCycleActivity = (HomeCycleActivity) getActivity();
        assert homeCycleActivity != null;
        homeCycleActivity.bottomNavigationVisibility(View.GONE);
        homeCycleActivity.editToolbarTxtSup(R.string.edit_profile);

        apiToken = loadUserData(getActivity()).getApiToken();

        if (bloodTypeAdapter == null) {
            bloodTypeAdapter = new SpinnerAdapter2 (getActivity ( ));
            getData (getClient ( ).getBloodTypes ( ), bloodTypeAdapter, getResources ( ).getString (R.string.blood_type), spBloodType);
        } else {
            spBloodType.setAdapter(bloodTypeAdapter);
        }

        if (governoratesAdapter == null) {
            governoratesAdapter = new SpinnerAdapter2 (getActivity ( ));
        } else {
            spGovernment.setAdapter(governoratesAdapter);
        }

        governoratesAdapter = new SpinnerAdapter2(getActivity());
        AdapterView.OnItemSelectedListener listener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {

                    cityAdapter = new SpinnerAdapter2(getActivity());
                    getData(getClient().getCities(governoratesAdapter.selectedId), cityAdapter, getResources().getString(R.string.city), spCity);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };
        getData(getClient().getGovernorates(), governoratesAdapter, getResources().getString(R.string.governorate), spGovernment, listener);
        getProfileData();

        return view;
    }


    @Override
    public void onBack() {
//        super.onBack();
        homeCycleActivity.setSelection(R.id.nav_home);
    }


    @OnClick({R.id.edit_profile_fragment_birthday, R.id.edit_profile_fragment_Last_donation_d})
    public void onClick(View view) {
        Calendar calendar = Calendar.getInstance();

        switch (view.getId()) {
            case R.id.edit_profile_fragment_birthday:

                DateTxt dateTxt = new DateTxt("01", "01", "1980", "01-01-1980");
                showCalender(getActivity(), getResources().getString(R.string.birthday), editProfileFragmentBirthday, dateTxt);
                break;

            case R.id.edit_profile_fragment_Last_donation_d:
                year = calendar.get(Calendar.YEAR);
                String sYear = String.valueOf(year);
                month = calendar.get(Calendar.MONTH);
                String sMonth = String.valueOf(month);
                day = calendar.get(Calendar.DAY_OF_MONTH);
                String sDay = String.valueOf(day);

                String txt = day + "/" + (month + 1) + "/" + year;
                DateTxt dateTxt2 = new DateTxt(sDay, sMonth, sYear, txt);
                showCalender(getActivity(), getResources().getString(R.string.last_donation), editProfileFragmentLastDonationD, dateTxt2);

                break;
        }
    }

    private void setEditProfileData() {
        String userName = editProfileFragmentTxtUserName.getText().toString().trim();
        if (userName.trim().equals("") || editProfileFragmentTxtUserName.getText() == null || editProfileFragmentTxtUserName.getText().length() == 0) {
            //   txt_phone_number.setError (getResources ( ).getString (R.string.Please_insert_number));
            return;
        }


        //String userName = txtUserName.getText().toString().trim();
        String email = editProfileFragmentTxtEmail.getText().toString().trim();
        String phoneNumber = editProfileFragmentTxtPhoneNumber.getText().toString().trim();
        String password = editProfileFragmentTxtPassword.getText().toString().trim();
        String confirmPassword = editProfileFragmentTxtConfirmPassword.getText().toString().trim();
        String birthday = editProfileFragmentBirthday.getText().toString().trim();
        String lastDonationDate = editProfileFragmentLastDonationD.getText().toString().trim();
        int bloodType = bloodTypeAdapter.selectedId;
        int governorate = governoratesAdapter.selectedId;
        int city = cityAdapter.selectedId;

        getClient().editProfile(userName, email, birthday, city, phoneNumber, lastDonationDate, password, confirmPassword, bloodType, apiToken).enqueue(new Callback<Register>() {
            @Override
            public void onResponse(Call<Register> call, Response<Register> response) {
                try {
                    Toast.makeText(baseActivity, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Toast.makeText(baseActivity, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                if (response.body().getStatus() == 1) {
                    SharedPreferencesManger.SaveData(getActivity(), getResources().getString(R.string.USER_DATA_SHARED), response.body().getData());
                }
            }

            @Override
            public void onFailure(Call<Register> call, Throwable t) {
                Toast.makeText(baseActivity, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getProfileData() {
//        editProfileFragmentTxtUserName.setText(loadUserData(getActivity()).getClient ().getName ());
//        editProfileFragmentTxtEmail.setText(loadUserData(getActivity()).getClient ().getEmail ());
//        editProfileFragmentBirthday.setText(loadUserData(getActivity()).getClient ().getBirthDate ());
//        editProfileFragmentLastDonationD.setText(loadUserData(getActivity()).getClient ().getDonationLastDate ());
//        editProfileFragmentTxtPhoneNumber.setText(loadUserData(getActivity()).getClient ().getPhone ());
//        spBloodType.setSelection(loadUserData(getActivity()).getClient ().getBloodType ().getId ());
//        spGovernment.setSelection(loadUserData(getActivity()).getClient ().getCity ().getGovernorate ().getId ());
//        AdapterView.OnItemSelectedListener listener = new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                int citySelected = loadUserData(getActivity()).getClient ().getCity ().getId ();
//                cityAdapter = new SpinnerAdapter2(getActivity());
//                getData(getClient().getCities(loadUserData(getActivity()).getClient ().getCity ().getGovernorate ().getId ()), cityAdapter, getResources().getString(R.string.city), spCity, citySelected);
//            }
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//            }
//        };
//        spGovernment.setOnItemSelectedListener(listener);

        getClient().getProfile(apiToken).enqueue(new Callback<Register>() {
            @Override
            public void onResponse(Call<Register> call, Response<Register> response) {
                try {
                    if (response.body().getStatus() == 1) {
                        editProfileFragmentTxtUserName.setText(response.body().getData().getClient().getName());
                        editProfileFragmentTxtEmail.setText(response.body().getData().getClient().getEmail());
                        editProfileFragmentBirthday.setText(response.body().getData().getClient().getBirthDate());
                        editProfileFragmentLastDonationD.setText(response.body().getData().getClient().getDonationLastDate());
                        editProfileFragmentTxtPhoneNumber.setText(response.body().getData().getClient().getPhone());
                        if (spBloodType.getSelectedItemPosition () == 0 || spBloodType != null){
                            spBloodType.setSelection(response.body().getData().getClient().getBloodType().getId());
                        }
                        if (spGovernment.getSelectedItemPosition () == 0){
                            spGovernment.setSelection(response.body().getData().getClient().getCity().getGovernorate().getId());
                        }
                        AdapterView.OnItemSelectedListener listener = new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                int citySelected = response.body().getData().getClient().getCity().getId();

                                if (citySelected != 0 || cityAdapter == null){
                                    cityAdapter = new SpinnerAdapter2(getActivity());
                                    getData(getClient().getCities(response.body().getData().getClient().getCity().getGovernorate().getId()), cityAdapter, getResources().getString(R.string.city), spCity, citySelected);
                                }
                            }
                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {
                            }
                        };
                        spGovernment.setOnItemSelectedListener(listener);
                    }
                } catch (Exception e) {

                }
            }
            @Override
            public void onFailure(Call<Register> call, Throwable t) {
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @OnClick(R.id.edit_profile_fragment_btn_register)
    public void onClick() {
        setEditProfileData();
    }
}
