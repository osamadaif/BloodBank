package com.osama.daif.bloodbank.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.osama.daif.bloodbank.R;

import java.util.ArrayList;
import java.util.List;

public class SliderAdapter extends PagerAdapter {

    Context context;
    LayoutInflater layoutInflater;

    // Array for slider pages
    private List<Integer> slideImage = new ArrayList<>();
    private List<String> slidDescs = new ArrayList<>();

    public SliderAdapter(Context context) {
        this.context = context;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        slideImage.add(R.drawable.slide1);
        slideImage.add(R.drawable.slide2);
        slideImage.add(R.drawable.slide3);

        slidDescs.add(context.getString(R.string.slider1));
        slidDescs.add(context.getString(R.string.slider2));
        slidDescs.add(context.getString(R.string.slider3));
    }

    @Override
    public int getCount() {
        return slidDescs.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = layoutInflater.inflate(R.layout.item_slider, container, false);

        ImageView slideImageView = view.findViewById(R.id.slide_img_1);
        TextView slideTextView = view.findViewById(R.id.slide_txt_1);

        slideImageView.setImageResource(slideImage.get(position));
        slideTextView.setText(slidDescs.get(position));

        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
