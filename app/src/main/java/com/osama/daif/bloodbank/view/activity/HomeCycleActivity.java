package com.osama.daif.bloodbank.view.activity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.osama.daif.bloodbank.R;
import com.osama.daif.bloodbank.view.fragment.HomeContainerFragment;
import com.osama.daif.bloodbank.view.fragment.homeCycle.EditProfileFragment;
import com.osama.daif.bloodbank.view.fragment.homeCycle.staticScreen.MoreFragment;

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
    @BindView(R.id.home_cycle_bottom_navigation)
    BottomNavigationView homeCycleBottomNavigation;
    private long backPressedTime;
    private Toast backToast;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_cycle);

        ButterKnife.bind(this);

        toolbar = findViewById(R.id.toolbar);

        toolbarBackBtn.setVisibility(View.GONE);
        setTitle("");
        toolbarTxtSup.setText(getString(R.string.app_name));
        replaceFragment(getSupportFragmentManager(), R.id.home_container_fr_frame, new HomeContainerFragment());
        setSupportActionBar(toolbar);

        homeCycleBottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id =item.getItemId();
                if (id == R.id.nav_home){
                    replaceFragment(getSupportFragmentManager(), R.id.home_container_fr_frame, new HomeContainerFragment());
                }
                else if (id == R.id.nav_user_account){
                    replaceFragment(getSupportFragmentManager(), R.id.home_container_fr_frame, new EditProfileFragment ());

                }
                else if (id == R.id.nav_notification){

                }
                else if (id == R.id.nav_more){
                    replaceFragment(getSupportFragmentManager(), R.id.home_container_fr_frame, new MoreFragment());
                }
                return true;
            }
        });


    }
    public void  editToolbarTxtSup(int nameID){
        toolbarTxtSup.setText(getString(nameID));
    }

    public void appbarVisibility(int visibility) {
        appbarId.setVisibility(visibility);
    }

    public void bottomNavigationVisibility(int visibility){
        homeCycleBottomNavigation.setVisibility(visibility);
    }


}
