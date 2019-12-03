package com.osama.daif.bloodbank.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.osama.daif.bloodbank.R;
import com.osama.daif.bloodbank.data.model.city.GeneralResponseData;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NotificationSettingAdapter extends RecyclerView.Adapter<NotificationSettingAdapter.NotificationSettingViewHolder> {

    private Context context;
    private Activity activity;
    private List<GeneralResponseData> generalResponseData = new ArrayList<> ( );
    private List<String> oldIds = new ArrayList<> ( );
    public List<Integer> newIds = new ArrayList<> ( );

    public NotificationSettingAdapter(Context context, Activity activity, List<GeneralResponseData> generalResponseData, List<String> oldIds) {
        this.context = context;
        this.activity = activity;
        this.generalResponseData = generalResponseData;
        this.oldIds = oldIds;

    }

    @Override
    public NotificationSettingViewHolder onCreateViewHolder(ViewGroup parent,
                                                            int viewType) {
        View v = LayoutInflater.from (parent.getContext ( ))
                .inflate (R.layout.item_check_box, parent, false);
        return new NotificationSettingViewHolder (v);
    }

    @Override
    public void onBindViewHolder(NotificationSettingViewHolder holder, int position) {
        GeneralResponseData generalResponseData1 = generalResponseData.get (position);

        setData (holder, position);
    }

    private void setData(NotificationSettingViewHolder holder, int position) {
        holder.itemCheckBoxCb.setText (generalResponseData.get (position).getName ());
        if (oldIds.contains (String.valueOf (generalResponseData.get (position).getId ()))) {
            holder.itemCheckBoxCb.setChecked (true);
            newIds.add ((generalResponseData.get (position).getId ()));
        }else {
            holder.itemCheckBoxCb.setChecked (false);
        }
    }



    @Override
    public int getItemCount() {
        if (generalResponseData == null) {
            return 0;
        }
        return generalResponseData.size ( );
    }


    public class NotificationSettingViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.item_check_box_cb)
        CheckBox itemCheckBoxCb;

        private View view;

        public NotificationSettingViewHolder(View itemView) {
            super (itemView);
            view = itemView;
            ButterKnife.bind (this, view);
        }

        @OnClick(R.id.item_check_box_cb)
        public void onClick() {

            if (!itemCheckBoxCb.isChecked ()) {
                for (int i = 0; i < newIds.size ( ); i++) {
                    if (newIds.get (i).equals (generalResponseData.get (getAdapterPosition ( )).getId ( ))) {
                        newIds.remove (i);
                        break;
                    }
                }
            }else {
                newIds.add (generalResponseData.get (getAdapterPosition ( )).getId ( ));
            }

        }
    }
}