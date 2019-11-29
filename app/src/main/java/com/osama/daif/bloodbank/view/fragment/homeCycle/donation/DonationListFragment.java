package com.osama.daif.bloodbank.view.fragment.homeCycle.donation;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.osama.daif.bloodbank.R;
import com.osama.daif.bloodbank.adapter.DonationListRecyclerAdapter;
import com.osama.daif.bloodbank.adapter.SpinnerAdapter2;
import com.osama.daif.bloodbank.data.model.donation.Donation;
import com.osama.daif.bloodbank.data.model.donation.DonationData;
import com.osama.daif.bloodbank.helper.OnEndLess;
import com.osama.daif.bloodbank.view.fragment.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.osama.daif.bloodbank.data.api.RetrofitClient.getClient;
import static com.osama.daif.bloodbank.data.local.SharedPreferencesManger.loadUserData;
import static com.osama.daif.bloodbank.helper.GeneralRequest.getData;

public class DonationListFragment extends BaseFragment {

    @BindView(R.id.fragment_home_donation_rv)
    RecyclerView fragmentHomeDonationRv;
    @BindView(R.id.fragment_home_donation_sp_spinner_blood_type)
    Spinner fragmentHomeDonationSpSpinnerBloodType;
    @BindView(R.id.fragment_home_donation_sp_spinner_government)
    Spinner fragmentHomeDonationSpSpinnerGovernment;

    private FloatingActionButton fab;
    private Unbinder unbinder = null;

    private LinearLayoutManager linearLayoutManager;
    private List<DonationData> donationList = new ArrayList<> ( );
    private DonationListRecyclerAdapter donationListAdapter;
    private int maxPage = 0;
    private OnEndLess onEndLess;

    private SpinnerAdapter2 bloodTypeAdapter, governoratesAdapter;

    private long backPressedTime;
    private Toast backToast;
    String apiToken;
    int bloodTypeId;


    public DonationListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate (R.layout.fragment_home_donation, container, false);
        unbinder = ButterKnife.bind (this, view);
        // Inflate the layout for this fragment
        initFragment ( );
        fab = getActivity ( ).findViewById (R.id.home_container_fragment_f_a_btn_add);
        initRecyclerView ( );
        apiToken = loadUserData (getActivity ( )).getApiToken ( );
        bloodTypeAdapter = new SpinnerAdapter2 (getActivity ( ));
        AdapterView.OnItemSelectedListener bloodTypeListener = new AdapterView.OnItemSelectedListener ( ) {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    bloodTypeId = bloodTypeAdapter.selectedId;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };
        getData (getClient ( ).getBloodTypes ( ), bloodTypeAdapter, getResources ( ).getString (R.string.blood_type), fragmentHomeDonationSpSpinnerBloodType);

        governoratesAdapter = new SpinnerAdapter2 (getActivity ( ));
        AdapterView.OnItemSelectedListener governoratesListener = new AdapterView.OnItemSelectedListener ( ) {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    getClient ( ).getAllDonationsRequestsFilter (apiToken, bloodTypeId, governoratesAdapter.selectedId, 1).enqueue (new Callback<Donation> ( ) {
                        @Override
                        public void onResponse(Call<Donation> call, Response<Donation> response) {
                            try {
                                if (response.body ( ).getStatus ( ) == 1) {
                                    donationList.clear ( );
                                    maxPage = response.body ( ).getData ( ).getLastPage ( );
                                    donationList.addAll (response.body ( ).getData ( ).getData ( ));
                                    donationListAdapter.notifyDataSetChanged ( );
                                }
                            } catch (Exception e) {

                            }
                        }

                        @Override
                        public void onFailure(Call<Donation> call, Throwable t) {

                        }
                    });
                } else {
                    getDonations (1);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };
        getData (getClient ( ).getGovernorates ( ), governoratesAdapter, getResources ( ).getString (R.string.governorate), fragmentHomeDonationSpSpinnerGovernment, governoratesListener);
        return view;
    }

    private void initRecyclerView() {
        linearLayoutManager = new LinearLayoutManager (getActivity ( ));
        fragmentHomeDonationRv.setLayoutManager (linearLayoutManager);

        onEndLess = new OnEndLess (linearLayoutManager, 1) {
            @Override
            public void onLoadMore(int current_page) {
                if (current_page <= maxPage) {
                    if (maxPage != 0 && current_page != 1) {
                        onEndLess.previous_page = current_page;

                        getDonations (current_page);
                    } else {
                        onEndLess.current_page = onEndLess.previous_page;
                    }
                }
            }
        };
        fragmentHomeDonationRv.setHasFixedSize (true);
        donationListAdapter = new DonationListRecyclerAdapter (getContext ( ), getActivity ( ), donationList);
        fragmentHomeDonationRv.setAdapter (donationListAdapter);

        fragmentHomeDonationRv.addOnScrollListener (new RecyclerView.OnScrollListener ( ) {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged (recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                if (dy < 0 && !fab.isShown ( ))
                    fab.show ( );
                else if (dy > 0 && fab.isShown ( ))
                    fab.hide ( );
            }

        });
        fragmentHomeDonationRv.addOnScrollListener (onEndLess);

        getDonations (1);
    }

    private void getDonations(int page) {

        getClient ( ).getAllDonations (apiToken, 1).enqueue (new Callback<Donation> ( ) {
            @Override
            public void onResponse(Call<Donation> call, Response<Donation> response) {
                try {
                    if (response.body ( ).getStatus ( ) == 1) {
                        donationList.clear ( );
                        maxPage = response.body ( ).getData ( ).getLastPage ( );
                        donationList.addAll (response.body ( ).getData ( ).getData ( ));
                        donationListAdapter.notifyDataSetChanged ( );
                    }
                } catch (Exception e) {

                }
            }

            @Override
            public void onFailure(Call<Donation> call, Throwable t) {
                Toast.makeText (baseActivity, t.getMessage ( ), Toast.LENGTH_SHORT).show ( );
            }
        });

    }

    @Override
    public void onBack() {
        super.onBack ( );
    }
}
