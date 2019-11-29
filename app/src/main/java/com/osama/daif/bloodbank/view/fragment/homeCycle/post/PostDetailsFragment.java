package com.osama.daif.bloodbank.view.fragment.homeCycle.post;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.osama.daif.bloodbank.R;
import com.osama.daif.bloodbank.data.model.posts.Posts;
import com.osama.daif.bloodbank.data.model.posts.PostsData;
import com.osama.daif.bloodbank.view.fragment.BaseFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.osama.daif.bloodbank.data.api.RetrofitClient.getClient;
import static com.osama.daif.bloodbank.data.local.SharedPreferencesManger.loadUserData;
import static com.osama.daif.bloodbank.helper.HelperMethods.replaceFragment;
import static com.osama.daif.bloodbank.view.fragment.homeCycle.post.PostsAndFavoritesListFragment.EXTRA_CONTENT;
import static com.osama.daif.bloodbank.view.fragment.homeCycle.post.PostsAndFavoritesListFragment.EXTRA_IS_FAVOURITE;
import static com.osama.daif.bloodbank.view.fragment.homeCycle.post.PostsAndFavoritesListFragment.EXTRA_POST_ID;
import static com.osama.daif.bloodbank.view.fragment.homeCycle.post.PostsAndFavoritesListFragment.EXTRA_TITLE;
import static com.osama.daif.bloodbank.view.fragment.homeCycle.post.PostsAndFavoritesListFragment.IMAGE_URL;

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
    private int postId;
    private String postTitle;
    private String postContent;
    private boolean postIsFav;
    private String postImage;
    private PostsData postsData = new PostsData ();

    public PostDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate (R.layout.fragment_post_details, container, false);
        unbinder = ButterKnife.bind (this, view);
        assert getArguments ( ) != null;
        postId = getArguments ().getInt (EXTRA_POST_ID);
        postTitle = getArguments ().getString (EXTRA_TITLE);
        postContent = getArguments ().getString (EXTRA_CONTENT);
        postImage = getArguments ().getString (IMAGE_URL);
        postIsFav = getArguments ().getBoolean (EXTRA_IS_FAVOURITE);

        // Inflate the layout for this fragment
        initFragment ( );
        apiToken = loadUserData (getActivity ( )).getApiToken ( );
        getPostDetails ( );
        return view;
    }

    private void getPostDetails() {
        getClient ().getPostDetails (apiToken, postId,1).enqueue (new Callback<Posts> ( ) {
            @Override
            public void onResponse(Call<Posts> call, Response<Posts> response) {
                if (response.body ().getStatus () == 1) {
                    fragmentPostDetailsTxtTitle.setText (postTitle);
                    fragmentPostDetailsTxtContent.setText (postContent);
                    Glide.with(getActivity ()).load(postImage).into(fragmentPostDetailsImgPostImage);
                    if (postIsFav) {
                        fragmentPostDetailsImgFav.setImageResource(R.drawable.ic_favorite_fill_red);
                    } else {
                        fragmentPostDetailsImgFav.setImageResource(R.drawable.ic_favorite_border_red);
                    }
                }

            }

            @Override
            public void onFailure(Call<Posts> call, Throwable t) {
                Toast.makeText (baseActivity, t.getMessage (), Toast.LENGTH_SHORT).show ( );
            }
        });
    }
    @Override
    public void onBack() {
        super.onBack ();
    }

    @OnClick(R.id.fragment_post_details_img_fav)
    public void onClick() {
        getClient().getPostToggleFavourite(postId,loadUserData(getActivity ()).getApiToken()).enqueue (new Callback<Posts> ( ) {
            @Override
            public void onResponse(Call<Posts> call, Response<Posts> response) {
                if (response.body ().getStatus () == 1) {
                    postsData.setIsFavourite(postsData.getIsFavourite());
                    if (postIsFav) {
                        postsData.setIsFavourite (false);
                        fragmentPostDetailsImgFav.setImageResource(R.drawable.ic_favorite_border_red);
                    } else {
                        postsData.setIsFavourite (true);
                        fragmentPostDetailsImgFav.setImageResource(R.drawable.ic_favorite_fill_red);
                    }
                }
            }

            @Override
            public void onFailure(Call<Posts> call, Throwable t) {
                Toast.makeText (baseActivity, t.getMessage (), Toast.LENGTH_SHORT).show ( );
            }
        });
    }
}
