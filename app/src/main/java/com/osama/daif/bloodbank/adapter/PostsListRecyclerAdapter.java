package com.osama.daif.bloodbank.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.osama.daif.bloodbank.R;
import com.osama.daif.bloodbank.data.model.posts.Posts;
import com.osama.daif.bloodbank.data.model.posts.PostsData;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.osama.daif.bloodbank.data.api.RetrofitClient.getClient;
import static com.osama.daif.bloodbank.data.local.SharedPreferencesManger.loadUserData;

public class PostsListRecyclerAdapter extends RecyclerView.Adapter<PostsListRecyclerAdapter.PostsListVH> {

    private boolean fav = false;
    private List<PostsData> postsList = new ArrayList<> ( );
    private Context mContext;
    private Activity activity;
    private String searchString = "";
    private itemClickListener mItemClickListener;
    public static boolean changeFav = false;


    public PostsListRecyclerAdapter(Context context, Activity activity, List<PostsData> postsList, itemClickListener listener, boolean fav) {
        mContext = context;
        this.activity = activity;
        this.fav = fav;
        this.postsList = postsList;
        this.mItemClickListener = listener;

    }

    @Override
    public PostsListVH onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View view = LayoutInflater.from (viewGroup.getContext ( ))
                .inflate (R.layout.item_rv_posts, viewGroup, false);
        return new PostsListVH (view);
    }

    @Override
    public void onBindViewHolder(@NonNull final PostsListVH holder, final int position) {
        setData (holder, position);
        setAction (holder, position);
    }

    private void setData(PostsListVH holder, int position) {
        holder.itemRvPostsTvTitlePost.setText (postsList.get (position).getTitle ( ));
        Glide.with (mContext).load (postsList.get (position).getThumbnailFullPath ( )).into (holder.itemRvPostsIvImagePost);
        if (postsList.get (position).getIsFavourite ( )) {
            holder.itemRvPostsIvAddFav.setImageResource (R.drawable.circle_with_heart_fill);
        } else {
            holder.itemRvPostsIvAddFav.setImageResource (R.drawable.circle_with_heart_border);
        }
//        postsList.get(position).setIsFavourite(postsList.get(position).getIsFavourite());
    }

    private void setAction(PostsListVH holder, int position) {
        holder.itemRvPostsIvAddFav.setOnClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick(View v) {
                getClient ( ).getPostToggleFavourite (postsList.get (position).getId ( ), loadUserData (activity).getApiToken ( )).enqueue (new Callback<Posts> ( ) {
                    @Override
                    public void onResponse(Call<Posts> call, Response<Posts> response) {


                        if (postsList.get (position).getIsFavourite ( )) {
                            postsList.get (position).setIsFavourite (false);
                            holder.itemRvPostsIvAddFav.setImageResource (R.drawable.circle_with_heart_border);
                        } else {
                            postsList.get (position).setIsFavourite (true);
                            holder.itemRvPostsIvAddFav.setImageResource (R.drawable.circle_with_heart_fill);
                        }
                        if (fav) {
                            postsList.remove (position);
                            changeFav = true;
                        }
                        notifyDataSetChanged ( );
                    }

                    @Override
                    public void onFailure(Call<Posts> call, Throwable t) {
                        Toast.makeText (activity, t.getMessage ( ), Toast.LENGTH_SHORT).show ( );
                    }
                });
            }
        });
    }

    public void setSearchItem(List<PostsData> newList, String searchString) {
        this.searchString = searchString;
        postsList = new ArrayList<> ( );
        postsList.addAll (newList);
        notifyDataSetChanged ( );
    }

    public void setSearchItem(List<PostsData> newList) {
        postsList = new ArrayList<> ( );
        postsList.addAll (newList);
        notifyDataSetChanged ( );
    }

    @Override
    public int getItemCount() {
        if (postsList == null || postsList.isEmpty ( )) {
            return 0;
        }
        return postsList.size ( );
    }

    @Override
    public long getItemId(int position) {
        if (position < postsList.size ( )) {
            return postsList.get (position).getId ( );
        }
        return RecyclerView.NO_ID;
    }

    class PostsListVH extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.item_rv_posts_iv_image_post)
        ImageView itemRvPostsIvImagePost;
        @BindView(R.id.item_rv_posts_iv_add_fav)
        ImageView itemRvPostsIvAddFav;
        @BindView(R.id.item_rv_posts_tv_title_post)
        TextView itemRvPostsTvTitlePost;

        private View view;

        PostsListVH(View itemView) {
            super (itemView);
            view = itemView;
            ButterKnife.bind (this, view);
            itemView.setOnClickListener (this);

        }

        @Override
        public void onClick(View v) {
            if (getAdapterPosition ( ) != RecyclerView.NO_POSITION) {
                int itemId = getAdapterPosition ( );
                mItemClickListener.onItemClickListener (itemId);
            }
        }
    }

    public interface itemClickListener {
        void onItemClickListener(int itemId);
    }

}
