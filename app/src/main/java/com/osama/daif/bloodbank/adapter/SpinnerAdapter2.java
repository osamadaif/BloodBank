package com.osama.daif.bloodbank.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.osama.daif.bloodbank.R;
import com.osama.daif.bloodbank.data.model.city.CityData;

import java.util.ArrayList;
import java.util.List;

public class SpinnerAdapter2 extends BaseAdapter {

    private Context context;
    private LayoutInflater inflater;
    public List<CityData> generalResponseDataList = new ArrayList<> ( );
    public int selectedId = 0;

    public SpinnerAdapter2(Context applicationContext) {
        this.context = applicationContext;
        inflater = (LayoutInflater.from (applicationContext));
    }

    public void setData(List<CityData> generalResponseDataList, String hint) {
        this.generalResponseDataList = new ArrayList<> ( );
        this.generalResponseDataList.add (new CityData (0, hint));
        this.generalResponseDataList.addAll (generalResponseDataList);
    }

    @Override
    public int getCount() {
        return generalResponseDataList.size ( );
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

        convertView = inflater.inflate (R.layout.item_custom_spinner_layout, null);

        TextView spinnerTV = convertView.findViewById (R.id.tvSpinnerLayout);

            spinnerTV.setText (generalResponseDataList.get (position).getName ());
            selectedId = generalResponseDataList.get (position).getId ();
        return convertView;
    }
}
