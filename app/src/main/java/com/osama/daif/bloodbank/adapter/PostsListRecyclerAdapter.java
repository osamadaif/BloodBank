package com.osama.daif.bloodbank.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.osama.daif.bloodbank.R;
import com.osama.daif.bloodbank.data.model.posts.PostsData;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PostsListRecyclerAdapter extends RecyclerView.Adapter<PostsListRecyclerAdapter.PostsListVH> {

    private static final String TAG = PostsListRecyclerAdapter.class.getSimpleName();


    private List<PostsData> postsList = new ArrayList<>();
    private Context mContext;
    private Activity activity;
    private String searchString = "";


    public PostsListRecyclerAdapter(Context context, Activity activity, List<PostsData> postsList) {
        mContext = context;
        this.activity = activity;
        this.postsList = postsList;
    }

    @Override
    public PostsListVH onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_rv_posts, viewGroup, false);
        return new PostsListVH(view);

    }

    @Override
    public void onBindViewHolder(@NonNull final PostsListVH holder, final int position) {
        final PostsData posts = postsList.get(position);
        setData(holder,position);

    }

    private void setData(PostsListVH holder, int position) {
        holder.itemRvPostsTvTitlePost.setText(postsList.get(position).getTitle());
        Glide.with(mContext).load(postsList.get(position).getThumbnailFullPath()).into(holder.itemRvPostsIvImagePost);
    }


    public void setSearchItem(List<PostsData> newList, String searchString) {
        this.searchString = searchString;
        postsList = new ArrayList<>();
        postsList.addAll(newList);
        notifyDataSetChanged();
    }

    public void setSearchItem(List<PostsData> newList) {
        postsList = new ArrayList<>();
        postsList.addAll(newList);
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        if (postsList == null || postsList.isEmpty()) {
            return 0;
        }
        return postsList.size();
    }

    @Override
    public long getItemId(int position) {
        if (position < postsList.size()) {
            return postsList.get(position).getId();
        }
        return RecyclerView.NO_ID;
    }

    @OnClick(R.id.item_rv_posts_iv_add_fav)
    public void onClick() {
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
            super(itemView);
            view = itemView;
            ButterKnife.bind(this,view);

        }


        public List<PostsData> getItems() {
            return postsList;
        }


        public void setItems(List<PostsData> itemList) {
            postsList = itemList;
            notifyDataSetChanged();
        }

        @Override
        public void onClick(View v) {

        }
    }
}
