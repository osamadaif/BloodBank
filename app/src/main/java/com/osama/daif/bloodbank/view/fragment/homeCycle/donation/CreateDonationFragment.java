package com.osama.daif.bloodbank.view.fragment.homeCycle.donation;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.osama.daif.bloodbank.R;
import com.osama.daif.bloodbank.view.activity.HomeCycleActivity;
import com.osama.daif.bloodbank.view.fragment.BaseFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class CreateDonationFragment extends BaseFragment {

    @BindView(R.id.create_donation_txt_name)
    EditText createDonationTxtName;
    @BindView(R.id.create_donation_txt_age)
    EditText createDonationTxtAge;
    @BindView(R.id.create_donation_sp_blood_type)
    Spinner createDonationSpBloodType;
    @BindView(R.id.create_donation_txt_number_of_bags)
    EditText createDonationTxtNumberOfBags;
    @BindView(R.id.create_donation_txt_hospital_address)
    EditText createDonationTxtHospitalAddress;
    @BindView(R.id.create_donation_sp_government)
    Spinner createDonationSpGovernment;
    @BindView(R.id.create_donation_sp_city)
    Spinner createDonationSpCity;
    @BindView(R.id.create_donation_txt_phone)
    EditText createDonationTxtPhone;
    @BindView(R.id.create_donation_txt_notes)
    EditText createDonationTxtNotes;
    @BindView(R.id.create_donation_btn_send)
    Button createDonationBtnSend;

    private Unbinder unbinder = null;

    private HomeCycleActivity homeCycleActivity;

    public CreateDonationFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate (R.layout.fragment_create_donation, container, false);
        unbinder = ButterKnife.bind(this, view);
        // Inflate the layout for this fragment
        initFragment ( );
        homeCycleActivity = (HomeCycleActivity) getActivity();
        assert homeCycleActivity != null;
        homeCycleActivity.editToolbarTxtSup(R.string.donation_request);
        homeCycleActivity.bottomNavigationVisibility (View.GONE);


        return view;
    }

    @Override
    public void onBack() {
        super.onBack ( );
    }

    @OnClick({R.id.create_donation_txt_hospital_address, R.id.create_donation_btn_send})
    public void onClick(View view) {
        switch (view.getId ( )) {
            case R.id.create_donation_txt_hospital_address:
                break;
            case R.id.create_donation_btn_send:
                break;
        }
    }
}
