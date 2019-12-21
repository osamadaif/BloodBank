package com.osama.daif.bloodbank.adapter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.osama.daif.bloodbank.R;
import com.osama.daif.bloodbank.data.model.notification.NotificationData;
import com.osama.daif.bloodbank.view.activity.BaseActivity;
import com.osama.daif.bloodbank.view.fragment.homeCycle.donation.DonationDetailsFragment;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.osama.daif.bloodbank.helper.HelperMethods.replaceFragment;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationVH> {

    private List<NotificationData> notificationDataList = new ArrayList<> ( );
    private Activity activity;

    public NotificationAdapter(Activity activity, List<NotificationData> notificationDataList) {
        this.activity = activity;
        this.notificationDataList = notificationDataList;

    }

    @Override
    public NotificationVH onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View view = LayoutInflater.from (viewGroup.getContext ( ))
                .inflate (R.layout.item_notification, viewGroup, false);
        return new NotificationVH (view);
    }

    @Override
    public void onBindViewHolder(@NonNull final NotificationVH holder, final int position) {
        try {
            setData (holder, position);
        } catch (ParseException e) {
            e.printStackTrace ( );
        }
        setAction (holder, position);
    }

    private void setData(NotificationVH holder, int position) throws ParseException {

        if (notificationDataList.get (position).getPivot ( ).getIsRead ( ).equals ("0")) {
            holder.itemNotificationIvNotificationIcon.setImageResource (R.drawable.ic_notifications_red);
        } else {
            holder.itemNotificationIvNotificationIcon.setImageResource (R.drawable.ic_notifications_none_red);
        }
        notificationDataList.get (position).setPivot (notificationDataList.get (position).getPivot ( ));
        String dateInput = notificationDataList.get (position).getCreatedAt ( );
        SimpleDateFormat inFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = inFormat.parse(dateInput);
        SimpleDateFormat outFormat = new SimpleDateFormat("EE: dd/MM/yyyy");
        assert date != null;
        String goal = outFormat.format(date);

        holder.itemNotificationTvNotificationTitle.setText (notificationDataList.get (position).getTitle ( ));
        holder.itemNotificationTvDate.setText (goal);


    }

    private void setAction(NotificationVH holder, int position) {
        holder.view.setOnClickListener (v -> {
            NotificationData clickedItem = notificationDataList.get(position);
            DonationDetailsFragment donationDetailsFragment = new DonationDetailsFragment();
            donationDetailsFragment.notificationData = clickedItem;
            replaceFragment(((AppCompatActivity)activity).getSupportFragmentManager(), R.id.home_container_fr_frame, donationDetailsFragment);
        });
    }

    @Override
    public int getItemCount() {
        if (notificationDataList == null || notificationDataList.isEmpty ( )) {
            return 0;
        }
        return notificationDataList.size ( );
    }

    @Override
    public long getItemId(int position) {
        if (position < notificationDataList.size ( )) {
            return notificationDataList.get (position).getId ( );
        }
        return RecyclerView.NO_ID;
    }

    class NotificationVH extends RecyclerView.ViewHolder  {
        @BindView(R.id.item_notification_iv_notification_icon)
        ImageView itemNotificationIvNotificationIcon;
        @BindView(R.id.item_notification_tv_notification_title)
        TextView itemNotificationTvNotificationTitle;
        @BindView(R.id.item_notification_tv_date)
        TextView itemNotificationTvDate;
        private View view;

        NotificationVH(View itemView) {
            super (itemView);
            view = itemView;
            ButterKnife.bind (this, view);
        }

//        @Override
//        public void onClick(View v) {
//            if (getAdapterPosition() != RecyclerView.NO_POSITION) {
//                int itemId = getAdapterPosition();
//                clickListener.onClickListener(itemId);
//            }
//        }


    }
//
//    public interface clickListener {
//        void onClickListener(int itemId);
//    }


}
