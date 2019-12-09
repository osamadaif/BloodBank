package com.osama.daif.bloodbank.view.fragment.homeCycle.post;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.osama.daif.bloodbank.R;
import com.osama.daif.bloodbank.adapter.PostsListRecyclerAdapter;
import com.osama.daif.bloodbank.adapter.SpinnerAdapter2;
import com.osama.daif.bloodbank.data.model.posts.Posts;
import com.osama.daif.bloodbank.data.model.posts.PostsData;
import com.osama.daif.bloodbank.helper.OnEndLess;
import com.osama.daif.bloodbank.view.activity.HomeCycleActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.osama.daif.bloodbank.adapter.PostsListRecyclerAdapter.changeFav;
import static com.osama.daif.bloodbank.data.api.RetrofitClient.getClient;
import static com.osama.daif.bloodbank.data.local.SharedPreferencesManger.loadUserData;
import static com.osama.daif.bloodbank.helper.GeneralRequest.getData;
import static com.osama.daif.bloodbank.helper.HelperMethods.replaceFragment;

public class PostsAndFavoritesListFragment extends Fragment implements PostsListRecyclerAdapter.itemClickListener {
    public static final String EXTRA_FAV = "fav page";
    private static final int DEFAULT_ID = -1;
    public static final String INSTANCE_ID = "instanceTaskId";
    private int mId = DEFAULT_ID;

    @BindView(R.id.fragment_home_posts_rv)
    RecyclerView postsRecyclerView;
    @BindView(R.id.fragment_home_posts_txt_search)
    EditText fragmentHomePostsTxtSearch;
    @BindView(R.id.fragment_home_posts_sp_filter_sorting)
    Spinner fragmentHomePostsSpFilterSorting;
    @BindView(R.id.fragment_home_posts_img_search)
    ImageView fragmentHomePostsImgSearch;
    @BindView(R.id.fragment_home_posts_img_sorting)
    ImageView fragmentHomePostsImgSorting;
    @BindView(R.id.fragment_home_posts_layout)
    LinearLayout fragmentHomePostsLayout;


    private FloatingActionButton fab;
    private Unbinder unbinder = null;

    private LinearLayoutManager linearLayoutManager;
    private List<PostsData> postsList = new ArrayList<>();
    private PostsListRecyclerAdapter postsAdapter;
    private int maxPage = 0;
    private OnEndLess onEndLess;
    HomeCycleActivity homeCycleActivity;


    private SpinnerAdapter2 categoriesAdapter;


    public PostsAndFavoritesListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_posts, container, false);
        unbinder = ButterKnife.bind(this, view);


        if (savedInstanceState != null && savedInstanceState.containsKey(INSTANCE_ID)) {
            mId = savedInstanceState.getInt(INSTANCE_ID, DEFAULT_ID);
        }

        Bundle bundle = getArguments();

        if (bundle != null && bundle.getInt(EXTRA_FAV) == 22) {
            bundle.getInt(EXTRA_FAV);
            homeCycleActivity = (HomeCycleActivity) getActivity();
            homeCycleActivity.appbarVisibility(View.VISIBLE);
            homeCycleActivity.editToolbarTxtSup(R.string.favourite);
            homeCycleActivity.bottomNavigationVisibility(View.GONE);
            fragmentHomePostsLayout.setVisibility(View.GONE);
            initFavRecyclerView();
        } else {
            homeCycleActivity = (HomeCycleActivity) getActivity();
            homeCycleActivity.editToolbarTxtSup(R.string.app_name);
            homeCycleActivity.bottomNavigationVisibility(View.VISIBLE);
            fragmentHomePostsLayout.setVisibility(View.VISIBLE);
            fab = getActivity().findViewById(R.id.home_container_fragment_f_a_btn_add);
            initRecyclerView();
            categoriesAdapter = new SpinnerAdapter2(getActivity());
            getData(getClient().getCategories(), categoriesAdapter, getResources().getString(R.string.filter), fragmentHomePostsSpFilterSorting);
        }


        return view;
    }

    private void initRecyclerView() {
        linearLayoutManager = new LinearLayoutManager(getActivity());
        postsRecyclerView.setLayoutManager(linearLayoutManager);

        onEndLess = new OnEndLess(linearLayoutManager, 1) {
            @Override
            public void onLoadMore(int current_page) {
                if (current_page <= maxPage) {
                    if (maxPage != 0 && current_page != 1) {
                        onEndLess.previous_page = current_page;

                        getPosts(current_page);
                    } else {
                        onEndLess.current_page = onEndLess.previous_page;
                    }
                }
            }
        };
        postsRecyclerView.setHasFixedSize(true);
        postsAdapter = new PostsListRecyclerAdapter(getContext(), getActivity(), postsList, this, false);
        postsRecyclerView.setAdapter(postsAdapter);

        postsRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                if (dy < 0 && !fab.isShown())
                    fab.show();
                else if (dy > 0 && fab.isShown())
                    fab.hide();

            }

        });
        postsRecyclerView.addOnScrollListener(onEndLess);

        if (postsList.size() == 0 || changeFav) {
            getPosts(1);
            changeFav = false;
        }
    }


    private void getPosts(int page) {
        String apiToken = loadUserData(getActivity()).getApiToken();
        getClient().getAllPosts(apiToken, page).enqueue(new Callback<Posts>() {
            @Override
            public void onResponse(Call<Posts> call, Response<Posts> response) {
                try {
                    if (response.body().getStatus() == 1) {
                        maxPage = response.body().getData().getLastPage();
                        postsList.addAll(response.body().getData().getData());
                        postsAdapter.notifyDataSetChanged();
                    }
                } catch (Exception e) {

                }
            }

            @Override
            public void onFailure(Call<Posts> call, Throwable t) {
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void initFavRecyclerView() {
        linearLayoutManager = new LinearLayoutManager(getActivity());
        postsRecyclerView.setLayoutManager(linearLayoutManager);

        postsRecyclerView.setHasFixedSize(true);
        postsAdapter = new PostsListRecyclerAdapter(getContext(), getActivity(), postsList, this, true);
        postsRecyclerView.setAdapter(postsAdapter);
        getFav();
    }

    private void getFav() {
        String apiToken = loadUserData(getActivity()).getApiToken();
        getClient().getFavouritesPosts(apiToken).enqueue(new Callback<Posts>() {
            @Override
            public void onResponse(Call<Posts> call, Response<Posts> response) {
                try {
                    if (response.body().getStatus() == 1) {
                        postsList.clear();
                        postsList.addAll(response.body().getData().getData());
                        postsAdapter.notifyDataSetChanged();
                    }
                } catch (Exception e) {

                }
            }

            @Override
            public void onFailure(Call<Posts> call, Throwable t) {
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    @OnClick(R.id.fragment_home_posts_img_search)
    public void onClick() {
        String apiToken = loadUserData(getActivity()).getApiToken();
        String searchTXT = fragmentHomePostsTxtSearch.getText().toString().trim();

        getClient().getPostsFilter(apiToken, 1, searchTXT, categoriesAdapter.selectedId).enqueue(new Callback<Posts>() {
            @Override
            public void onResponse(Call<Posts> call, Response<Posts> response) {
                try {
                    if (response.body().getStatus() == 1) {
                        maxPage = response.body().getData().getLastPage();
                        postsList.clear();
                        postsList.addAll(response.body().getData().getData());
                        postsAdapter.notifyDataSetChanged();
                    }
                } catch (Exception e) {

                }
            }

            @Override
            public void onFailure(Call<Posts> call, Throwable t) {
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onItemClickListener(int itemId) {
        PostsData clickedItem = postsList.get(itemId);

        PostDetailsFragment postDetailsFragment = new PostDetailsFragment();
        postDetailsFragment.postsData = clickedItem;

        replaceFragment(getActivity().getSupportFragmentManager(), R.id.home_container_fr_frame, postDetailsFragment);


    }

}
