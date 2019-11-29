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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.osama.daif.bloodbank.R;
import com.osama.daif.bloodbank.adapter.PostsListRecyclerAdapter;
import com.osama.daif.bloodbank.adapter.SpinnerAdapter2;
import com.osama.daif.bloodbank.data.model.posts.Posts;
import com.osama.daif.bloodbank.data.model.posts.PostsData;
import com.osama.daif.bloodbank.helper.OnEndLess;
import com.osama.daif.bloodbank.view.fragment.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.osama.daif.bloodbank.data.api.RetrofitClient.getClient;
import static com.osama.daif.bloodbank.data.local.SharedPreferencesManger.loadUserData;
import static com.osama.daif.bloodbank.helper.GeneralRequest.getData;
import static com.osama.daif.bloodbank.helper.HelperMethods.replaceFragment;

public class PostsAndFavoritesListFragment extends BaseFragment implements PostsListRecyclerAdapter.ItemClickListener {
    public static final String IMAGE_URL = "imageUrl";
    public static final String EXTRA_TITLE = "postTitle";
    public static final String EXTRA_IS_FAVOURITE = "postFav";
    public static final String EXTRA_CONTENT = "postContent";
    public static final String EXTRA_POST_ID = "postContent";

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

    private long backPressedTime;
    private Toast backToast;

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
        // Inflate the layout for this fragment
        initFragment();
        fab = getActivity().findViewById(R.id.home_container_fragment_f_a_btn_add);
        initRecyclerView();
        categoriesAdapter = new SpinnerAdapter2(getActivity());
        getData(getClient().getCategories(), categoriesAdapter, getResources().getString(R.string.filter), fragmentHomePostsSpFilterSorting);
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
        postsAdapter = new PostsListRecyclerAdapter(getContext(), getActivity(), postsList,this);
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

        getPosts(1);
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
                Toast.makeText (baseActivity, t.getMessage (), Toast.LENGTH_SHORT).show ( );
            }
        });
    }

    @Override
    public void onBack() {
        if (backPressedTime + 2000 > System.currentTimeMillis()) {
            backToast.cancel();
            getActivity ( ).finish ( );
            return;
        } else {
            backToast = Toast.makeText(getActivity(), getResources().getString(R.string.Press_back_again), Toast.LENGTH_SHORT);
            backToast.show();
        }
        backPressedTime = System.currentTimeMillis();
    }

    @OnClick(R.id.fragment_home_posts_img_search)
    public void onClick() {
        String apiToken = loadUserData(getActivity()).getApiToken();
        String searchTXT = fragmentHomePostsTxtSearch.getText().toString().trim();

       getClient().getPostsFilter(apiToken,1,searchTXT,categoriesAdapter.selectedId).enqueue(new Callback<Posts>() {
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
               Toast.makeText (baseActivity, t.getMessage (), Toast.LENGTH_SHORT).show ( );
           }
       });
    }

    @Override
    public void onItemClickListener(int itemId) {
        Bundle bundle = new Bundle();
//        postsAdapter.getItemId (itemId);
        PostsData clickedItem = postsList.get (itemId);
        bundle.putString (EXTRA_TITLE, clickedItem.getTitle ());
        bundle.putString (IMAGE_URL, clickedItem.getThumbnailFullPath ());
        bundle.putString (EXTRA_CONTENT, clickedItem.getContent ());
        bundle.putBoolean (EXTRA_IS_FAVOURITE, clickedItem.getIsFavourite ());
        bundle.putInt (EXTRA_POST_ID, clickedItem.getId ());


        replaceFragment (getActivity ( ).getSupportFragmentManager ( ), R.id.home_container_fr_frame, new PostDetailsFragment ( ), bundle);


    }
}
