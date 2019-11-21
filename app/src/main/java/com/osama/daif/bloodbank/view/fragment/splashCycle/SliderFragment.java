package com.osama.daif.bloodbank.view.fragment.splashCycle;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.viewpager.widget.ViewPager;

import com.osama.daif.bloodbank.R;
import com.osama.daif.bloodbank.adapter.SliderAdapter;
import com.osama.daif.bloodbank.view.activity.UserCycleActivity;
import com.osama.daif.bloodbank.view.fragment.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class SliderFragment extends BaseFragment {

    @BindView(R.id.slide_view_pager)
    ViewPager slideViewPager;
    @BindView(R.id.dots_layout)
    LinearLayout dotsLayout;
    @BindView(R.id.next_and_done)
    ImageView nextAndDone;
    private Unbinder unbinder = null;

    private int mCurrentPage;

    private List<String> dotsList = new ArrayList<>();

    private TextView[] mDots;

    private SliderAdapter sliderAdapter;

    public SliderFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_slider, container, false);

        unbinder = ButterKnife.bind(this, view);
        // Inflate the layout for this fragment
        initFragment();
        mDots = new TextView[3];
        mDots[0] = new TextView(getActivity());
        mDots[1] = new TextView(getActivity());
        mDots[2] = new TextView(getActivity());

        dotsLayout.addView(mDots[0]);
        dotsLayout.addView(mDots[1]);
        dotsLayout.addView(mDots[2]);


        sliderAdapter = new SliderAdapter(getActivity());
        slideViewPager.setAdapter(sliderAdapter);
        addDotsIndicator(0);
        slideViewPager.addOnPageChangeListener(viewListener);
        nextAndDone.setImageResource(R.drawable.circle_with_arrow);
        return view;
    }

    private void addDotsIndicator(int position) {
    // old for
//        for (int i = 0; i < mDots.length; i++) {
//            mDots[i].setText(Html.fromHtml("&#8226"));
//            mDots[i].setTextSize(64);
//            mDots[i].setTextColor(getResources().getColor(R.color.orange));
//        }

        for (TextView mDot : mDots) {
            mDot.setText(Html.fromHtml("&#8226"));
            mDot.setTextSize(64);
            mDot.setTextColor(getResources().getColor(R.color.orange));
        }

        if (mDots.length > 0) {
            mDots[position].setTextColor(getResources().getColor(R.color.colorPrimary));
        }
    }

    private ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {

            addDotsIndicator(position);
            mCurrentPage = position;

            if (position == 0 || position == 1){
                nextAndDone.setEnabled(true);
                nextAndDone.setImageResource(R.drawable.circle_with_arrow);
                nextAndDone.setVisibility(View.VISIBLE);
            } else {
                nextAndDone.setEnabled(true);
                nextAndDone.setImageResource(R.drawable.circle_with_done);
                nextAndDone.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    @Override
    public void onBack() {
        baseActivity.finish();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.next_and_done)
    public void onClick() {
        if (mCurrentPage == 0 || mCurrentPage == 1) {
            slideViewPager.setCurrentItem(mCurrentPage + 1);
        }else {

            Intent intent = new Intent (getActivity (), UserCycleActivity.class);
            startActivity (intent);
        }
    }
}
