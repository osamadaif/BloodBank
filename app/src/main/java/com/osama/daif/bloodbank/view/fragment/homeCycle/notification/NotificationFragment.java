package com.osama.daif.bloodbank.view.fragment.homeCycle.notification;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.AppBarLayout;
import com.osama.daif.bloodbank.R;
import com.osama.daif.bloodbank.adapter.NotificationAdapter;
import com.osama.daif.bloodbank.data.model.notification.Notification;
import com.osama.daif.bloodbank.data.model.notification.NotificationData;
import com.osama.daif.bloodbank.helper.OnEndLess;
import com.osama.daif.bloodbank.view.activity.HomeCycleActivity;
import com.osama.daif.bloodbank.view.fragment.BaseFragment;
import com.osama.daif.bloodbank.view.fragment.homeCycle.donation.CreateDonationFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.osama.daif.bloodbank.data.api.RetrofitClient.getClient;
import static com.osama.daif.bloodbank.data.local.SharedPreferencesManger.loadUserData;
import static com.osama.daif.bloodbank.helper.HelperMethods.replaceFragment;

public class NotificationFragment extends BaseFragment {

    @BindView(R.id.notification_fragment_rv)
    RecyclerView notificationFragmentRv;
    Unbinder unbinder;
    @BindView(R.id.notification_fragment_progress_bar)
    ProgressBar notificationFragmentProgressBar;

    private LinearLayoutManager linearLayoutManager;
    private List<NotificationData> notificationList = new ArrayList<> ( );
    private NotificationAdapter notificationAdapter;
    private int maxPage = 0;
    private OnEndLess onEndLess;

    HomeCycleActivity homeCycleActivity;

    String apiToken;

    View mEmptyView;

    public NotificationFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate (R.layout.fragment_notification, container, false);
        unbinder = ButterKnife.bind (this, view);
        // Inflate the layout for this fragment
        initFragment ( );
        homeCycleActivity = (HomeCycleActivity) getActivity ( );
        assert homeCycleActivity != null;
        homeCycleActivity.editToolbarTxtSup (R.string.notification);
        homeCycleActivity.appbarVisibility (View.VISIBLE);
        homeCycleActivity.bottomNavigationVisibility (View.VISIBLE);
        homeCycleActivity.setBackBtnVisibility (View.GONE);
        homeCycleActivity.setBehavior (new AppBarLayout.ScrollingViewBehavior ( ));
        apiToken = loadUserData (getActivity ( )).getApiToken ( );
        mEmptyView = view.findViewById (R.id.notification_empty_view);
        initRecyclerView ( );
        return view;
    }

    private void initRecyclerView() {
        linearLayoutManager = new LinearLayoutManager (getActivity ( ));
        notificationFragmentRv.setLayoutManager (linearLayoutManager);

        onEndLess = new OnEndLess (linearLayoutManager, 1) {
            @Override
            public void onLoadMore(int current_page) {
                if (current_page <= maxPage) {
                    if (maxPage != 0 && current_page != 1) {
                        onEndLess.previous_page = current_page;

                        getNotification (current_page);
                    } else {
                        onEndLess.current_page = onEndLess.previous_page;
                    }
                }
            }
        };
        notificationFragmentRv.addOnScrollListener (onEndLess);
        notificationFragmentRv.setHasFixedSize (true);
        notificationAdapter = new NotificationAdapter (getActivity ( ), notificationList);
        notificationFragmentRv.setAdapter (notificationAdapter);
        if (notificationList.size ( ) == 0) {
            notificationFragmentProgressBar.setVisibility (View.VISIBLE);
            getNotification (1);
        }

    }

    private void getNotification(int page) {
        getClient ( ).getNotifications (apiToken, page).enqueue (new Callback<Notification> ( ) {
            @Override
            public void onResponse(Call<Notification> call, Response<Notification> response) {
                try {
                    if (response.body ( ).getStatus ( ) == 1) {
                        notificationFragmentProgressBar.setVisibility (View.GONE);
                        notificationList.addAll (response.body ( ).getData ( ).getData ( ));
                        notificationAdapter.notifyDataSetChanged ( );
                        if (notificationList.size ( ) == 0) {
                            notificationFragmentRv.setVisibility (View.GONE);
                            mEmptyView.setVisibility (View.VISIBLE);

                        } else {
                            notificationFragmentRv.setVisibility (View.VISIBLE);
                            mEmptyView.setVisibility (View.GONE);

                        }
                    }
                } catch (Exception e) {

                }
            }

            @Override
            public void onFailure(Call<Notification> call, Throwable t) {

            }
        });
    }

    @Override
    public void onBack() {
        homeCycleActivity.setSelection (R.id.nav_home);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView ( );
        unbinder.unbind ( );
    }

    @OnClick(R.id.notification_empty_view_fragment_btn_donation)
    public void onViewClicked() {
        replaceFragment (getActivity ( ).getSupportFragmentManager ( ), R.id.home_container_fr_frame, new CreateDonationFragment ( ));
    }
}
