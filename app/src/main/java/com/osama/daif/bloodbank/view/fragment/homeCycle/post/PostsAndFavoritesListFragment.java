package com.osama.daif.bloodbank.view.fragment.homeCycle.post;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.osama.daif.bloodbank.R;
import com.osama.daif.bloodbank.adapter.PostsListRecyclerAdapter;
import com.osama.daif.bloodbank.adapter.SpinnerAdapter2;
import com.osama.daif.bloodbank.data.model.posts.Posts;
import com.osama.daif.bloodbank.data.model.posts.PostsData;
import com.osama.daif.bloodbank.helper.OnEndLess;
import com.osama.daif.bloodbank.view.activity.HomeCycleActivity;
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

import static com.osama.daif.bloodbank.adapter.PostsListRecyclerAdapter.changeFav;
import static com.osama.daif.bloodbank.data.api.RetrofitClient.getClient;
import static com.osama.daif.bloodbank.data.local.SharedPreferencesManger.loadUserData;
import static com.osama.daif.bloodbank.helper.GeneralRequest.getData;
import static com.osama.daif.bloodbank.helper.HelperMethods.replaceFragment;
import static com.osama.daif.bloodbank.helper.HelperMethods.setSystemBarColor;

public class PostsAndFavoritesListFragment extends BaseFragment implements PostsListRecyclerAdapter.itemClickListener {
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
    @BindView(R.id.fragment_home_posts_fragment_progress_bar)
    ProgressBar fragmentHomePostsFragmentProgressBar;

    //no internet layout
    @BindView(R.id.lyt_no_connection)
    LinearLayout lytNoConnection;

    private Unbinder unbinder = null;
    private FloatingActionButton fab;
    private LinearLayoutManager linearLayoutManager;
    private List<PostsData> postsList = new ArrayList<> ( );
    private PostsListRecyclerAdapter postsAdapter;
    private int loadedPage = 1;
    private int maxPage = 0;
    private OnEndLess onEndLess;

    private Bundle bundle;
    private HomeCycleActivity homeCycleActivity;
    private SpinnerAdapter2 categoriesAdapter = null;

    public PostsAndFavoritesListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate (R.layout.fragment_home_posts, container, false);
        unbinder = ButterKnife.bind (this, view);
        initFragment ( );
        setSystemBarColor (getActivity ( ));
        if (savedInstanceState != null && savedInstanceState.containsKey (INSTANCE_ID)) {
            mId = savedInstanceState.getInt (INSTANCE_ID, DEFAULT_ID);
        }

        lytNoConnection.setVisibility (View.GONE);
        bundle = getArguments ( );
        if (bundle != null && bundle.getInt (EXTRA_FAV) == 22) {
            bundle.getInt (EXTRA_FAV);
            homeCycleActivity = (HomeCycleActivity) getActivity ( );
            assert homeCycleActivity != null;
            homeCycleActivity.appbarVisibility (View.VISIBLE);
            homeCycleActivity.editToolbarTxtSup (R.string.favourite);
            homeCycleActivity.bottomNavigationVisibility (View.GONE);
            homeCycleActivity.setBackBtnVisibility (View.VISIBLE);
            homeCycleActivity.setBehavior (new AppBarLayout.ScrollingViewBehavior ( ));
            homeCycleActivity.onBackBtnClick (v -> getActivity ( ).onBackPressed ( ));
            fragmentHomePostsLayout.setVisibility (View.GONE);
            initFavRecyclerView ( );
        } else {
            homeCycleActivity = (HomeCycleActivity) getActivity ( );
            assert homeCycleActivity != null;
            homeCycleActivity.editToolbarTxtSup (R.string.app_name);
            homeCycleActivity.bottomNavigationVisibility (View.VISIBLE);
            homeCycleActivity.setBehavior (null);
            fragmentHomePostsLayout.setVisibility (View.VISIBLE);
            fab = getActivity ( ).findViewById (R.id.home_container_fragment_f_a_btn_add);
            homeCycleActivity.setBadgeData ();
            initRecyclerView ( );

            if (categoriesAdapter == null) {
                categoriesAdapter = new SpinnerAdapter2 (getActivity ( ));
                getData (getClient ( ).getCategories ( ), categoriesAdapter, getResources ( ).getString (R.string.filter), fragmentHomePostsSpFilterSorting);
            } else {
                fragmentHomePostsSpFilterSorting.setAdapter (categoriesAdapter);
            }
        }
        return view;
    }

    private void initRecyclerView() {
        linearLayoutManager = new LinearLayoutManager (getActivity ( ));
        postsRecyclerView.setLayoutManager (linearLayoutManager);

        onEndLess = new OnEndLess (linearLayoutManager, 1) {
            @Override
            public void onLoadMore(int current_page) {
                if (current_page <= maxPage) {
                    if (maxPage != 0 && current_page != 1) {
                        onEndLess.previous_page = current_page;
                        getPosts (current_page);
                    } else {
                        onEndLess.current_page = onEndLess.previous_page;
                    }
                } else {
                    onEndLess.current_page = onEndLess.previous_page;
                }
            }
        };
        postsRecyclerView.setHasFixedSize (true);
        postsRecyclerView.setItemAnimator (new DefaultItemAnimator ( ));
        postsAdapter = new PostsListRecyclerAdapter (getContext ( ), getActivity ( ), postsList, this, false);
        postsAdapter.setHasStableIds (true);
        postsRecyclerView.setAdapter (postsAdapter);
        postsRecyclerView.addOnScrollListener (onEndLess);
        postsRecyclerView.addOnScrollListener (new RecyclerView.OnScrollListener ( ) {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged (recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                if (dy < 0 && !fab.isShown ( ))
                    fab.show ( );
                else if (dy > 0 && fab.isShown ( ))
                    fab.hide ( );

            }
        });


        if (postsList.size ( ) == 0 || changeFav) {
            checkIfNoConnection (loadedPage);
            changeFav = false;
        }
    }

    private void getPosts(int page) {

        String apiToken = loadUserData (getActivity ( )).getApiToken ( );
        getClient ( ).getAllPosts (apiToken, page).enqueue (new Callback<Posts> ( ) {
            @Override
            public void onResponse(Call<Posts> call, Response<Posts> response) {
                try {
                    if (response.body ( ).getStatus ( ) == 1) {
                        fragmentHomePostsFragmentProgressBar.setVisibility (View.GONE);
                        noInternetVisibility (View.VISIBLE, View.GONE);
                        maxPage = response.body ( ).getData ( ).getLastPage ( );
                        postsList.addAll (response.body ( ).getData ( ).getData ( ));
                        postsAdapter.notifyDataSetChanged ( );
                    }else {
                        setNoConnection ();
                    }

                } catch (Exception e) {
//                    Toast.makeText (getActivity ( ), e.getMessage ( ), Toast.LENGTH_SHORT).show ( );
                    setNoConnection ();
                }
            }

            @Override
            public void onFailure(Call<Posts> call, Throwable t) {
//                Toast.makeText (getActivity ( ), t.getMessage ( ), Toast.LENGTH_SHORT).show ( );
                setNoConnection ();
            }
        });
    }

    private void initFavRecyclerView() {
        linearLayoutManager = new LinearLayoutManager (getActivity ( ));
        postsRecyclerView.setLayoutManager (linearLayoutManager);
        postsRecyclerView.setHasFixedSize (true);
        postsRecyclerView.setItemAnimator (new DefaultItemAnimator ( ));
        postsAdapter = new PostsListRecyclerAdapter (getContext ( ), getActivity ( ), postsList, this, true);
        postsAdapter.setHasStableIds (true);
        postsRecyclerView.setAdapter (postsAdapter);
        if (postsList.size ( ) == 0 || changeFav) {
            checkIfNoConnection (loadedPage);
        }
    }

    private void getFav() {
        String apiToken = loadUserData (getActivity ( )).getApiToken ( );
        getClient ( ).getFavouritesPosts (apiToken).enqueue (new Callback<Posts> ( ) {
            @Override
            public void onResponse(Call<Posts> call, Response<Posts> response) {
                try {
                    if (response.body ( ).getStatus ( ) == 1) {
                        fragmentHomePostsFragmentProgressBar.setVisibility (View.GONE);
                        noInternetVisibility (View.VISIBLE, View.GONE);
                        postsList.clear ( );
                        postsList.addAll (response.body ( ).getData ( ).getData ( ));
                        postsAdapter.notifyDataSetChanged ( );
                    }else {
                        new Handler ( ).postDelayed (() -> {
                            fragmentHomePostsFragmentProgressBar.setVisibility (View.GONE);
                            noInternetVisibility (View.GONE, View.VISIBLE);
                            fab.hide ();
                        }, 1000);
                    }
                } catch (Exception e) {
//                    Toast.makeText (getActivity ( ), e.getMessage ( ), Toast.LENGTH_SHORT).show ( );
                    fragmentHomePostsFragmentProgressBar.setVisibility (View.GONE);
                    noInternetVisibility (View.VISIBLE, View.GONE);
                    homeCycleActivity.useSnackBar (e.getMessage ());
                }
            }

            @Override
            public void onFailure(Call<Posts> call, Throwable t) {
//                Toast.makeText (getActivity ( ), t.getMessage ( ), Toast.LENGTH_SHORT).show ( );
                setNoConnection ();
            }
        });
    }


    @OnClick(R.id.fragment_home_posts_img_search)
    public void onClick() {
        String apiToken = loadUserData (getActivity ( )).getApiToken ( );
        String searchTXT = fragmentHomePostsTxtSearch.getText ( ).toString ( ).trim ( );
        fragmentHomePostsFragmentProgressBar.setVisibility (View.VISIBLE);

        getClient ( ).getPostsFilter (apiToken, 1, searchTXT, categoriesAdapter.selectedId).enqueue (new Callback<Posts> ( ) {
            @Override
            public void onResponse(Call<Posts> call, Response<Posts> response) {
                try {
                    if (response.body ( ).getStatus ( ) == 1) {
                        fragmentHomePostsFragmentProgressBar.setVisibility (View.GONE);
                        if (categoriesAdapter.selectedId != 0 && searchTXT.length ( ) > 0) {
                            maxPage = response.body ( ).getData ( ).getLastPage ( );
                            postsList.clear ( );
                            postsList.addAll (response.body ( ).getData ( ).getData ( ));
                            postsAdapter.notifyDataSetChanged ( );

                        } else {
                            if (postsList.size ( ) == 0) {
//                                getPosts (response.body ( ).getData ( ).getLastPage ( ));
                                checkIfNoConnection (loadedPage);
                            }
                        }

                    }
                } catch (Exception e) {
                    Toast.makeText (getActivity ( ), e.getMessage ( ), Toast.LENGTH_SHORT).show ( );
                }
            }

            @Override
            public void onFailure(Call<Posts> call, Throwable t) {
                Toast.makeText (getActivity ( ), t.getMessage ( ), Toast.LENGTH_SHORT).show ( );
            }
        });
    }

    private void noInternetVisibility(int postsVisibility, int noInternetVisibility) {
        postsRecyclerView.setVisibility (postsVisibility);
        if (bundle != null && bundle.getInt (EXTRA_FAV) == 22) {
            fragmentHomePostsLayout.setVisibility (View.GONE);
        } else {
            fragmentHomePostsLayout.setVisibility (postsVisibility);
        }

        lytNoConnection.setVisibility (noInternetVisibility);
    }

    private void checkIfNoConnection(int page) {
        noInternetVisibility (View.GONE, View.GONE);
        fragmentHomePostsFragmentProgressBar.setVisibility (View.VISIBLE);
        if (bundle != null && bundle.getInt (EXTRA_FAV) == 22) {
            getFav ( );
        } else {
            homeCycleActivity.setBadgeData ();
            getPosts (page );
        }
    }

    private void setNoConnection(){
        new Handler ( ).postDelayed (() -> {
            fragmentHomePostsFragmentProgressBar.setVisibility (View.GONE);
            noInternetVisibility (View.GONE, View.VISIBLE);
            if (fab != null) {
                fab.hide ();
            }

        }, 1000);
    }
    @Override
    public void onItemClickListener(int itemId) {
        PostsData clickedItem = postsList.get (itemId);
        PostDetailsFragment postDetailsFragment = new PostDetailsFragment ( );
        postDetailsFragment.postsData = clickedItem;
        replaceFragment (getActivity ( ).getSupportFragmentManager ( ), R.id.home_container_fr_frame, postDetailsFragment);
    }


    @Override
    public void onBack() {
        if (bundle != null && bundle.getInt (EXTRA_FAV) == 22) {
            super.onBack ( );
        } else {
            homeCycleActivity.onBackHome ( );
        }
    }

    @OnClick(R.id.lyt_no_connection)
    public void onViewClicked() {
        checkIfNoConnection (loadedPage);
    }
}