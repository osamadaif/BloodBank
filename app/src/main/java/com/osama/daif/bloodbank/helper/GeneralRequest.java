package com.osama.daif.bloodbank.helper;

import android.app.Activity;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.osama.daif.bloodbank.adapter.SpinnerAdapter2;
import com.osama.daif.bloodbank.data.model.city.GeneralResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.osama.daif.bloodbank.data.local.SharedPreferencesManger.loadUserData;

public class GeneralRequest {

    public static void getData(Call<GeneralResponse> call, SpinnerAdapter2 adapter, String hint, Spinner spinner) {
        call.enqueue(new Callback<GeneralResponse>() {
            @Override
            public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {
                try {
                    if (response.body().getStatus() == 1) {
                        adapter.setData(response.body().getData(), hint);
                        spinner.setAdapter(adapter);
                    }
                } catch (Exception e) {

                }
            }

            @Override
            public void onFailure(Call<GeneralResponse> call, Throwable t) {

            }
        });
    }

    public static void getData(Call<GeneralResponse> call, SpinnerAdapter2 adapter, String hint, Spinner spinner, AdapterView.OnItemSelectedListener listener) {
        call.enqueue(new Callback<GeneralResponse>() {
            @Override
            public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {
                try {
                    if (response.body().getStatus() == 1) {
                        adapter.setData(response.body().getData(), hint);
                        spinner.setAdapter(adapter);
                        spinner.setOnItemSelectedListener(listener);
                    }
                } catch (Exception e) {

                }
            }

            @Override
            public void onFailure(Call<GeneralResponse> call, Throwable t) {

            }
        });
    }

    public static void getData(Call<GeneralResponse> call, SpinnerAdapter2 adapter, String hint, Spinner spinner, int selection) {
        call.enqueue(new Callback<GeneralResponse>() {
            @Override
            public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {
                try {
                    if (response.body().getStatus() == 1) {
                        adapter.setData(response.body().getData(), hint);
                        spinner.setAdapter(adapter);
                        spinner.setSelection(selection);
                    }
                } catch (Exception e) {

                }
            }

            @Override
            public void onFailure(Call<GeneralResponse> call, Throwable t) {

            }
        });
    }

}
