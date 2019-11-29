package com.osama.daif.bloodbank.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.chauthai.swipereveallayout.ViewBinderHelper;
import com.osama.daif.bloodbank.R;
import com.osama.daif.bloodbank.data.model.donation.DonationData;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DonationListRecyclerAdapter extends RecyclerView.Adapter<DonationListRecyclerAdapter.DonationListVH> {
    private final Context context;


    private List<DonationData> donationList;

    // This object helps you save/restore the open/close state of each view
    private final ViewBinderHelper viewBinderHelper = new ViewBinderHelper ( );

    public DonationListRecyclerAdapter(Context context, Activity activity, List<DonationData> items) {
        this.donationList = items;
        this.context = context;
        viewBinderHelper.setOpenOnlyOne(true);
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
        viewBinderHelper.bind (holder.SwipeRevealLayout, item.getId ( ).toString ( ));

    }

    private void setData(DonationListVH holder, int position) {
        holder.itemRvDonationBloodTypeText.setText(donationList.get(position).getBloodType ().getName ());
        holder.itemRvDonationBloodTypeTxtCityValue.setText (donationList.get (position).getCity ().getName ());
        holder.itemRvDonationBloodTypeTxtHospitalValue.setText (donationList.get (position).getHospitalName ());
        holder.itemRvDonationBloodTypeTxtNameValue.setText (donationList.get (position).getPatientName ());


    }

    @Override
    public int getItemCount() {
        if (donationList == null) {
            return 0;
        }
        return donationList.size ( );
    }

    class DonationListVH extends RecyclerView.ViewHolder implements View.OnClickListener {


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


        public List<DonationData> getItems() {
            return donationList;
        }


        public void setItems(List<DonationData> itemList) {
            donationList = itemList;
            notifyDataSetChanged ( );
        }

        @Override
        public void onClick(View v) {

        }
    }
}