package com.osama.daif.bloodbank.adapter;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.chauthai.swipereveallayout.ViewBinderHelper;
import com.osama.daif.bloodbank.R;
import com.osama.daif.bloodbank.data.model.donation.DonationData;
import com.osama.daif.bloodbank.view.fragment.homeCycle.donation.DonationDetailsFragment;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.Manifest.permission.CALL_PHONE;
import static com.osama.daif.bloodbank.data.local.SharedPreferencesManger.LANG_NUM;
import static com.osama.daif.bloodbank.data.local.SharedPreferencesManger.LoadData;
import static com.osama.daif.bloodbank.helper.HelperMethods.replaceFragment;

public class DonationListRecyclerAdapter extends RecyclerView.Adapter<DonationListRecyclerAdapter.DonationListVH> {
    private final Context context;

    private Activity activity;
    private List<DonationData> donationList;

    // This object helps you save/restore the open/close state of each view
    private final ViewBinderHelper viewBinderHelper = new ViewBinderHelper ( );

    public DonationListRecyclerAdapter(Context context, Activity activity, List<DonationData> items) {
        this.donationList = items;
        this.context = context;
        this.activity = activity;
        viewBinderHelper.setOpenOnlyOne (true);
    }

    @Override
    public DonationListVH onCreateViewHolder(ViewGroup parent,
                                             int viewType) {
        View v = LayoutInflater.from (parent.getContext ( ))
                .inflate (R.layout.item_rv_donations, parent, false);
        return new DonationListVH (v);
    }

    @Override
    public void onBindViewHolder(DonationListVH holder, final int position) {
        final DonationData item = donationList.get (position);
        setData (holder, position);
        setAction (holder, position);
        viewBinderHelper.bind (holder.SwipeRevealLayout, item.getId ( ).toString ( ));
        if (LoadData(activity,LANG_NUM) != null){
            if (Integer.parseInt(LoadData(activity,LANG_NUM)) == 0){
                holder.SwipeRevealLayout.setDragEdge (0x1);
            }else if (Integer.parseInt(LoadData(activity,LANG_NUM)) == 1){
                holder.SwipeRevealLayout.setDragEdge (0x1 << 1);
            }
        }


    }

    private void setData(DonationListVH holder, int position) {
        holder.itemRvDonationBloodTypeText.setText (donationList.get (position).getBloodType ( ).getName ( ));
        holder.itemRvDonationBloodTypeTxtCityValue.setText (donationList.get (position).getCity ( ).getName ( ));
        holder.itemRvDonationBloodTypeTxtHospitalValue.setText (donationList.get (position).getHospitalName ( ));
        holder.itemRvDonationBloodTypeTxtNameValue.setText (donationList.get (position).getPatientName ( ));
    }

    private void setAction(DonationListVH holder, int position) {
        holder.itemRvDonationImgInfo.setOnClickListener (v -> {
            DonationData clickedItem = donationList.get (position);
            DonationDetailsFragment donationDetailsFragment = new DonationDetailsFragment ( );
            donationDetailsFragment.donationData = clickedItem;
            replaceFragment (((AppCompatActivity) context).getSupportFragmentManager ( ), R.id.home_container_fr_frame, donationDetailsFragment);
        });

        holder.itemRvDonationImgCall.setOnClickListener (v -> {
            String phoneNumber = donationList.get (position).getPhone ( );
            Intent callIntent = new Intent (Intent.ACTION_CALL);
            callIntent.setData (Uri.parse ("tel:" + phoneNumber));
            if (ContextCompat.checkSelfPermission(activity, CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                context.startActivity (callIntent);
            } else {
                ActivityCompat.requestPermissions(activity, new String[]{CALL_PHONE}, 1);
            }

        });
    }

    @Override
    public int getItemCount() {
        if (donationList == null) {
            return 0;
        }
        return donationList.size ( );
    }

    class DonationListVH extends RecyclerView.ViewHolder  {
        @BindView(R.id.SwipeRevealLayout)
        com.chauthai.swipereveallayout.SwipeRevealLayout SwipeRevealLayout;
        @BindView(R.id.item_rv_donation_img_info)
        ImageView itemRvDonationImgInfo;
        @BindView(R.id.item_rv_donation_img_call)
        ImageView itemRvDonationImgCall;
        @BindView(R.id.item_rv_donation_blood_type_text)
        TextView itemRvDonationBloodTypeText;
        @BindView(R.id.item_rv_donation_blood_type_txt_name_value)
        TextView itemRvDonationBloodTypeTxtNameValue;
        @BindView(R.id.item_rv_donation_blood_type_txt_hospital_value)
        TextView itemRvDonationBloodTypeTxtHospitalValue;
        @BindView(R.id.item_rv_donation_blood_type_txt_city_value)
        TextView itemRvDonationBloodTypeTxtCityValue;

        private View view;

        DonationListVH(View itemView) {
            super (itemView);
            view = itemView;
            ButterKnife.bind (this, view);
        }
    }
}