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

import androidx.appcompat.widget.AppCompatEditText;

import com.google.android.material.textfield.TextInputLayout;
import com.osama.daif.bloodbank.R;
import com.osama.daif.bloodbank.adapter.CustomSpinnerAdapter;
import com.osama.daif.bloodbank.adapter.SpinnerAdapter2;
import com.osama.daif.bloodbank.data.model.SpinnerItem;
import com.osama.daif.bloodbank.view.fragment.BaseFragment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.osama.daif.bloodbank.data.api.RetrofitClient.getClient;
import static com.osama.daif.bloodbank.helper.GeneralRequest.getData;
import static com.osama.daif.bloodbank.helper.HelperMethods.replaceFragment;

public class RegisterAsUserFragment extends BaseFragment implements AdapterView.OnItemSelectedListener {

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
    private Spinner bloodTypeSpinner;
    private Spinner governmentSpinner;
    private Spinner citySpinner;
    private ArrayList<SpinnerItem> customList;
    private int width = 150;

    private SpinnerAdapter2 bloodTypeAdapter, governoratesAdapter;

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


        bloodTypeSpinner = view.findViewById(R.id.register_and_edit_profile_fragment_til_sp_blood_type);
        governmentSpinner = view.findViewById(R.id.register_and_edit_profile_fragment_til_sp_government);
        citySpinner = view.findViewById(R.id.register_and_edit_profile_fragment_til_sp_city);

        bloodTypeAdapter = new SpinnerAdapter2 (getActivity ());
        getData(getClient ().getBloodTypes (), bloodTypeAdapter, "blood type", bloodTypeSpinner);

        governoratesAdapter = new SpinnerAdapter2 (getActivity ());
        AdapterView.OnItemSelectedListener listener = new AdapterView.OnItemSelectedListener ( ) {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {

                }else {

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };
        getData(getClient ().getGovernorates (), governoratesAdapter, "Governorates", governmentSpinner, listener);



        if (bloodTypeSpinner != null) {
            customList = getCustomList("blood type",R.drawable.blood_drop);
            CustomSpinnerAdapter adapter1 = new CustomSpinnerAdapter(getActivity(), customList);
            bloodTypeSpinner.setAdapter(adapter1);
            bloodTypeSpinner.setOnItemSelectedListener(this);
        }

        if (governmentSpinner != null) {
            customList = getCustomList("government",R.drawable.home_fill);
            CustomSpinnerAdapter adapter2 = new CustomSpinnerAdapter(getActivity(), customList);
            governmentSpinner.setAdapter(adapter2);
            governmentSpinner.setOnItemSelectedListener(this);
        }

        if (citySpinner != null) {
            customList = getCustomList("city",R.drawable.home_fill);
            CustomSpinnerAdapter adapter3 = new CustomSpinnerAdapter(getActivity(), customList);
            citySpinner.setAdapter(adapter3);
            citySpinner.setOnItemSelectedListener(this);
        }


        return view;
    }

    @Override
    public void onBack() {
        //super.onBack();
        replaceFragment(getActivity().getSupportFragmentManager(), R.id.fragment_user_container, new LoginFragment());
    }

    @OnClick(R.id.registers_and_edit_profile_fragment_btn_register)
    public void onClick() {

    }

    @OnClick({R.id.registers_and_edit_profile_fragment_birthday, R.id.register_edt_Last_donation_d})
    public void onClick(View view) {
        Calendar calendar = Calendar.getInstance();
        switch (view.getId()) {
            case R.id.registers_and_edit_profile_fragment_birthday:

                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                day = calendar.get(Calendar.DAY_OF_MONTH);
                datePickerDialog = new DatePickerDialog(Objects.requireNonNull(getActivity()), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        dateBirthday.setText(day + "/" + (month + 1) + "/" + year);
                    }
                }, year, month, day);
                datePickerDialog.show();
                break;

            case R.id.register_edt_Last_donation_d:
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                day = calendar.get(Calendar.DAY_OF_MONTH);
                datePickerDialog = new DatePickerDialog(Objects.requireNonNull(getActivity()), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        dateLastDonationD.setText(day + "/" + (month + 1) + "/" + year);
                    }
                }, year, month, day);
                datePickerDialog.show();
                break;
        }
    }

    private ArrayList<SpinnerItem> getCustomList(String itemText, int id) {
        customList = new ArrayList<>();
        customList.add(new SpinnerItem(itemText, id));
        customList.add(new SpinnerItem(itemText, id));
        customList.add(new SpinnerItem(itemText, id));
        customList.add(new SpinnerItem(itemText, id));
        customList.add(new SpinnerItem(itemText, id));
        return customList;
    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        try {
            LinearLayout linearLayout = getActivity ().findViewById(R.id.customSpinnerItemLayout);
            width = linearLayout.getWidth();
        } catch (Exception e) {
        }
        bloodTypeSpinner.setDropDownWidth(width);
        governmentSpinner.setDropDownWidth(width);
        citySpinner.setDropDownWidth(width);
        SpinnerItem item = (SpinnerItem) adapterView.getSelectedItem();
        // Toast.makeText(getActivity(), item.getSpinnerItemName(), Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

}
