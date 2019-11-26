package com.osama.daif.bloodbank.view.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.google.android.material.appbar.AppBarLayout;
import com.osama.daif.bloodbank.R;
import com.osama.daif.bloodbank.view.fragment.HomeContainerFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.osama.daif.bloodbank.helper.HelperMethods.replaceFragment;

public class HomeCycleActivity extends BaseActivity {


    View ylo;
    @BindView(R.id.toolbar_back_btn)
    ImageView toolbarBackBtn;
    @BindView(R.id.toolbar_txt_sup)
    TextView toolbarTxtSup;
    @BindView(R.id.appbar_id)
    AppBarLayout appbarId;
    @BindView(R.id.home_container_fr_frame)
    FrameLayout homeContainerFrFrame;

    Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_home_cycle);

        ButterKnife.bind (this);

        toolbar = findViewById (R.id.toolbar);

        toolbarBackBtn.setVisibility (View.GONE);
        setTitle ("");
        toolbarTxtSup.setText (getString (R.string.app_name));
        replaceFragment (getSupportFragmentManager ( ), R.id.home_container_fr_frame, new HomeContainerFragment ( ));
        setSupportActionBar (toolbar);

    }

}
