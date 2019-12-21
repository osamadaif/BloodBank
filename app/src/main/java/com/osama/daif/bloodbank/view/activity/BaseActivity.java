package com.osama.daif.bloodbank.view.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

import com.osama.daif.bloodbank.R;
import com.osama.daif.bloodbank.view.fragment.BaseFragment;

public class BaseActivity extends AppCompatActivity {

    public BaseFragment baseFragment;
    public long backPressedTime;
    public Toast backToast;

    public void superBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onBackPressed() {
        baseFragment.onBack();
    }

//    public void onBackHome(){
//        if (backPressedTime + 2000 > System.currentTimeMillis()) {
//            backToast.cancel();
//            finish();
//        } else {
//            backToast = Toast.makeText(this, getResources().getString(R.string.Press_back_again), Toast.LENGTH_SHORT);
//            backToast.show();
//        }
//        backPressedTime = System.currentTimeMillis();
//    }
}
