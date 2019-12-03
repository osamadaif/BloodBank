package com.osama.daif.bloodbank.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.osama.daif.bloodbank.R;
import com.osama.daif.bloodbank.data.model.city.GeneralResponseData;

import java.util.ArrayList;
import java.util.List;

public class SpinnerAdapter2 extends BaseAdapter {

    private Context context;
    private LayoutInflater inflater;
    public List<GeneralResponseData> generalResponseDataList = new ArrayList<>();
    public int selectedId = 0;

    public SpinnerAdapter2(Context applicationContext) {
        this.context = applicationContext;
        inflater = (LayoutInflater.from(applicationContext));
    }

    public void setData(List<GeneralResponseData> generalResponseDataList, String hint) {
        this.generalResponseDataList = new ArrayList<>();
        this.generalResponseDataList.add(new GeneralResponseData(0, hint));
        this.generalResponseDataList.addAll(generalResponseDataList);
    }

    @Override
    public int getCount() {
        return generalResponseDataList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView = inflater.inflate(R.layout.item_custom_spinner_layout, null);

        TextView spinnerTV = convertView.findViewById(R.id.tvSpinnerLayout);
//        ImageView spinnerIv = convertView.findViewById(R.id.ivSpinnerLayout);

        spinnerTV.setText(generalResponseDataList.get(position).getName());
        selectedId = generalResponseDataList.get(position).getId();

        return convertView;
    }

    public int getSelectedId() {
        return selectedId;
    }

    public void setSelectedId(int selectedId) {
        this.selectedId = selectedId;
    }
    //    @Override
//    public View getDropDownView(int position, View convertView, ViewGroup parent) {
//        if (convertView == null) {
//            convertView = inflater.inflate(R.layout.item_custom_dropdown_layout, parent, false);
//        }
//        ImageView dropDownIV = convertView.findViewById(R.id.ivDropDownLayout);
//        TextView dropDownTV = convertView.findViewById(R.id.tvDropDownLayout);
//
//        return convertView;
//    }

}
