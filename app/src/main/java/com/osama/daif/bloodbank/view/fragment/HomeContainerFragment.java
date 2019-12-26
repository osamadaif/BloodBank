package com.osama.daif.bloodbank.view.fragment;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.viewpager.widget.ViewPager;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.osama.daif.bloodbank.R;
import com.osama.daif.bloodbank.adapter.ViewPagerWithFragmentAdapter;
import com.osama.daif.bloodbank.view.activity.HomeCycleActivity;
import com.osama.daif.bloodbank.view.fragment.homeCycle.donation.CreateDonationFragment;
import com.osama.daif.bloodbank.view.fragment.homeCycle.donation.DonationListFragment;
import com.osama.daif.bloodbank.view.fragment.homeCycle.post.PostsAndFavoritesListFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.osama.daif.bloodbank.helper.HelperMethods.replaceFragment;
import static com.osama.daif.bloodbank.helper.HelperMethods.setSystemBarColor;

public class HomeContainerFragment extends BaseFragment {

    @BindView(R.id.home_container_fragment_tl_tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.home_container_fragment_f_a_btn_add)
    FloatingActionButton fab;
    @BindView(R.id.home_container_fragment_vp_view_pager)
    ViewPager tabViewPager;
    private long backPressedTime;
    private Toast backToast;


    private Unbinder unbinder = null;
    private HomeCycleActivity homeCycleActivity;
    private ViewPagerWithFragmentAdapter adapter = null;

    public HomeContainerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_screen, container, false);
        unbinder = ButterKnife.bind(this, view);
        setSystemBarColor (getActivity ());
        // Inflate the layout for this fragment
        initFragment();
        homeCycleActivity = (HomeCycleActivity) getActivity();
        assert homeCycleActivity != null;
        homeCycleActivity.appbarVisibility(View.GONE);
//        homeCycleActivity.editToolbarTxtSup(R.string.app_name);
        homeCycleActivity.bottomNavigationVisibility(View.VISIBLE);
        homeCycleActivity.setBehavior(null);
        setupViewPager(tabViewPager);
        tabLayout.setupWithViewPager(tabViewPager);

        fab.setOnClickListener(view1 ->
                replaceFragment(getActivity().getSupportFragmentManager(), R.id.home_container_fr_frame, new CreateDonationFragment()));
        tabViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}
            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0: {
                        fab.setBackgroundTintList(getResources().getColorStateList(R.color.white));
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            fab.setImageDrawable(getResources().getDrawable(R.drawable.ic_add_red, getContext().getTheme()));
                        } else {
                            fab.setImageDrawable(getResources().getDrawable(R.drawable.ic_add_red));
                        }
                        break;
                    }
                    case 1: {
                        fab.setBackgroundTintList(getResources().getColorStateList(R.color.colorPrimary));
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            fab.setImageDrawable(getResources().getDrawable(R.drawable.ic_add_white, getContext().getTheme()));
                        } else {
                            fab.setImageDrawable(getResources().getDrawable(R.drawable.ic_add_white));
                        }
                        break;
                    }
                }
                if (fab.isShown ()){
                    changeFabIcon (position);
                }else {
                    fab.hide ();
                }
            }
            @Override
            public void onPageScrollStateChanged(int state) {}
        });
        return view;
    }

    private void changeFabIcon(final int index) {
        fab.hide();
        new Handler ().postDelayed(() -> {
            fab.show();
        }, 300);
    }
    @Override
    public void onResume() {
        homeCycleActivity.appbarVisibility(View.GONE);
        super.onResume ( );
    }



    private void setupViewPager(ViewPager viewPager) {
        adapter = new ViewPagerWithFragmentAdapter(getChildFragmentManager());

        adapter.addPager(new PostsAndFavoritesListFragment(), getString(R.string.posts));
        adapter.addPager(new DonationListFragment(), getResources().getString(R.string.donations_requests));

        viewPager.setAdapter(adapter);
    }
}
