package com.osama.daif.bloodbank.view.fragment.homeCycle.post;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.osama.daif.bloodbank.R;
import com.osama.daif.bloodbank.data.model.posts.PostsData;
import com.osama.daif.bloodbank.view.activity.HomeCycleActivity;
import com.osama.daif.bloodbank.view.fragment.BaseFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.osama.daif.bloodbank.data.local.SharedPreferencesManger.loadUserData;

public class PostDetailsFragment extends BaseFragment {

    @BindView(R.id.fragment_post_details_img_post_image)
    ImageView fragmentPostDetailsImgPostImage;
    @BindView(R.id.fragment_post_details_img_fav)
    ImageView fragmentPostDetailsImgFav;
    @BindView(R.id.fragment_post_details_txt_title)
    TextView fragmentPostDetailsTxtTitle;
    @BindView(R.id.fragment_post_details_txt_content)
    TextView fragmentPostDetailsTxtContent;
    private Unbinder unbinder = null;

    String apiToken;
    public PostsData postsData;
    private HomeCycleActivity homeCycleActivity;


    public PostDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_post_details, container, false);
        unbinder = ButterKnife.bind(this, view);
        homeCycleActivity = (HomeCycleActivity) getActivity();
        assert homeCycleActivity != null;
        homeCycleActivity.appbarVisibility(View.GONE);
        homeCycleActivity.bottomNavigationVisibility (View.GONE);
        initFragment();
        apiToken = loadUserData(getActivity()).getApiToken();
        getPostDetails();
        return view;
    }

    private void getPostDetails() {
        fragmentPostDetailsTxtTitle.setText(postsData.getTitle());
        fragmentPostDetailsTxtContent.setText(postsData.getContent());
        Glide.with(getActivity()).load(postsData.getThumbnailFullPath()).into(fragmentPostDetailsImgPostImage);
        if (postsData.getIsFavourite()) {
            fragmentPostDetailsImgFav.setImageResource(R.drawable.ic_favorite_fill_red);
        } else {
            fragmentPostDetailsImgFav.setImageResource(R.drawable.ic_favorite_border_red);
        }
    }

    @Override
    public void onBack() {
        super.onBack();
    }

    @OnClick(R.id.fragment_post_details_img_fav)
    public void onClick() {
        if (postsData.getIsFavourite()) {
            postsData.setIsFavourite(false);
            fragmentPostDetailsImgFav.setImageResource(R.drawable.ic_favorite_border_red);
        } else {
            postsData.setIsFavourite(true);
            fragmentPostDetailsImgFav.setImageResource(R.drawable.ic_favorite_fill_red);
        }

    }
}
