package com.osama.daif.bloodbank.view.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.osama.daif.bloodbank.R;
import com.osama.daif.bloodbank.view.fragment.HomeContainerFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.osama.daif.bloodbank.helper.HelperMethods.replaceFragment;

public class HomeCycleActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    View ylo;
    @BindView(R.id.toolbar_back_btn)
    ImageView toolbarBackBtn;
    @BindView(R.id.toolbar_txt_sup)
    TextView toolbarTxtSup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_cycle);

        ButterKnife.bind(this);
        toolbarBackBtn.setVisibility(View.GONE);
        setTitle("");
        toolbarTxtSup.setText(getString(R.string.app_name));
        replaceFragment(getSupportFragmentManager(), R.id.home_container_fr_frame, new HomeContainerFragment());
        setSupportActionBar(toolbar);

    }

}
