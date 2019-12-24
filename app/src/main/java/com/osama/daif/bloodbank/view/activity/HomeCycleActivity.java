package com.osama.daif.bloodbank.view.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.osama.daif.bloodbank.R;
import com.osama.daif.bloodbank.data.model.notificationCount.NotificationCount;
import com.osama.daif.bloodbank.helper.HelperMethods;
import com.osama.daif.bloodbank.view.fragment.HomeContainerFragment;
import com.osama.daif.bloodbank.view.fragment.homeCycle.EditProfileFragment;
import com.osama.daif.bloodbank.view.fragment.homeCycle.notification.NotificationFragment;
import com.osama.daif.bloodbank.view.fragment.homeCycle.staticScreen.MoreFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.osama.daif.bloodbank.data.api.RetrofitClient.getClient;
import static com.osama.daif.bloodbank.data.local.SharedPreferencesManger.loadUserData;
import static com.osama.daif.bloodbank.helper.HelperMethods.getSnackBarWithBottomNavigation;
import static com.osama.daif.bloodbank.helper.HelperMethods.replaceFragment;
import static com.osama.daif.bloodbank.helper.HelperMethods.showSnackBarMargin;
import static com.osama.daif.bloodbank.helper.HelperMethods.showSnackBarWithBottomNavigation;
import static com.osama.daif.bloodbank.helper.SnackBarHelper.configSnackBar;

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
    @BindView(R.id.home_cycle_bottom_navigation)
    BottomNavigationView homeCycleBottomNavigation;
    CoordinatorLayout coordinatorLayout;


    Toolbar toolbar;
    CoordinatorLayout.LayoutParams params;
    private long backPressedTime;
    private Toast backToast;
    private HomeContainerFragment homeContainerFragment;
    private EditProfileFragment editProfileFragment;
    private NotificationFragment notificationFragment;
    Snackbar snackbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_cycle);

        ButterKnife.bind(this);
        coordinatorLayout = findViewById (R.id.home_activity);
        params = (CoordinatorLayout.LayoutParams) homeContainerFrFrame.getLayoutParams();
        setBehavior(null);
        homeContainerFrFrame.requestLayout();
//        toolbar = findViewById(R.id.toolbar);
//        setBackBtnVisibility (View.GONE);
//        setTitle("");
//        toolbarTxtSup.setText(getString(R.string.app_name));
        replaceFragment(getSupportFragmentManager(), R.id.home_container_fr_frame, new HomeContainerFragment());
//        setSupportActionBar(toolbar);
        homeCycleBottomNavigation.setOnNavigationItemSelectedListener(item -> {
            int id =item.getItemId();
            if (id == R.id.nav_home){
                if (homeContainerFragment == null) {
                    homeContainerFragment = new HomeContainerFragment();
                }
                replaceFragment(getSupportFragmentManager(), R.id.home_container_fr_frame, homeContainerFragment);
            }
            else if (id == R.id.nav_user_account){
                if (editProfileFragment == null) {
                    editProfileFragment = new EditProfileFragment();
                }
                replaceFragment(getSupportFragmentManager(), R.id.home_container_fr_frame, editProfileFragment);

            }
            else if (id == R.id.nav_notification){
                if (notificationFragment == null) {
                    notificationFragment = new NotificationFragment();
                }
                replaceFragment(getSupportFragmentManager(), R.id.home_container_fr_frame, notificationFragment);
            }
            else if (id == R.id.nav_more){
                replaceFragment(getSupportFragmentManager(), R.id.home_container_fr_frame, new MoreFragment());
            }
            return true;
        });




    }

    public void setBadgeData(){
        BadgeDrawable badge = homeCycleBottomNavigation.getOrCreateBadge (R.id.nav_notification);
        badge.setBackgroundColor (getResources ().getColor (R.color.gray));
        badge.setMaxCharacterCount (3);
        badge.setBadgeTextColor (getResources ().getColor (R.color.orange));
        badge.setHorizontalOffset (20);
        badge.setVerticalOffset (20);

        String apiToken = loadUserData (this).getApiToken ( );
        getClient ().getNotificationsCount (apiToken).enqueue (new Callback<NotificationCount> ( ) {
            @Override
            public void onResponse(Call<NotificationCount> call, Response<NotificationCount> response) {
                assert response.body ( ) != null;
                if (response.body ().getStatus () == 1) {
                    int notificationCount = response.body ().getData ().getNotificationsCount ();
                    if (notificationCount == 0){
                        homeCycleBottomNavigation.removeBadge(R.id.nav_notification);
                    } else {
                        badge.setVisible(true);
                        badge.setNumber (notificationCount);
                    }
                } else {
//                    Toast.makeText (HomeCycleActivity.this, response.body ().getMsg (), Toast.LENGTH_SHORT).show ( );
                    homeCycleBottomNavigation.removeBadge(R.id.nav_notification);
                }
            }

            @Override
            public void onFailure(Call<NotificationCount> call, Throwable t) {
//                Toast.makeText (HomeCycleActivity.this, t.getMessage (), Toast.LENGTH_SHORT).show ( );
                homeCycleBottomNavigation.removeBadge(R.id.nav_notification);
            }
        });
    }

    public void setSelection(int id){
        homeCycleBottomNavigation.setSelectedItemId(id);
    }

    public Boolean isSelection(int id){
        int selectedItem = homeCycleBottomNavigation.getSelectedItemId ();
        if (id == selectedItem){
            return true;
        }else {
            return false;
        }
    }
    public void setBehavior(AppBarLayout.ScrollingViewBehavior behavior){
        params.setBehavior(behavior);
    }

    public void bottomNavigationView (BottomNavigationView bottomNavigation){
        homeCycleBottomNavigation = bottomNavigation;
    }

    public void  editToolbarTxtSup(int nameID){
        toolbarTxtSup.setText(getString(nameID));
    }

    public void setBackBtnVisibility(int id){
        toolbarBackBtn.setVisibility(id);
    }

    public void onBackBtnClick(View.OnClickListener listener){
        toolbarBackBtn.setOnClickListener (listener);
    }

    public void editToolbarTxtSup(String s){
        toolbarTxtSup.setText(s);
    }

    public void appbarVisibility(int visibility) {
        appbarId.setVisibility(visibility);
    }

    public void bottomNavigationVisibility(int visibility){
        homeCycleBottomNavigation.setVisibility(visibility);
    }

    public void onBackHome(){
        if (backPressedTime + 2000 > System.currentTimeMillis()) {
            snackbar.dismiss ();
            finish();
        } else {

//            backToast = Toast.makeText(this, getResources().getString(R.string.Press_back_again), Toast.LENGTH_SHORT);
//            backToast.show();
            snackbar = getSnackBarWithBottomNavigation (coordinatorLayout,getResources().getString(R.string.Press_back_again),homeCycleBottomNavigation);
            showSnackBarMargin (snackbar,32,32);
        }
        backPressedTime = System.currentTimeMillis();
    }

    public void useSnackBar(String message){
        showSnackBarWithBottomNavigation (coordinatorLayout,message,homeCycleBottomNavigation);
    }


}
