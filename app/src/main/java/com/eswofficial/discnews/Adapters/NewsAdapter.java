package com.eswofficial.discnews.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.eswofficial.discnews.Interface.ItemClickListener;
import com.eswofficial.discnews.Models.News;
import com.eswofficial.discnews.NewsDetail;
import com.eswofficial.discnews.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {

    //data
    List<News> newsList;
    Context context;
    Activity activity;

    //constructor
    public NewsAdapter(Context context, Activity activity, List<News> theList) {
        this.newsList = theList;
        this.context = context;
        this.activity = activity;
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(context).inflate(R.layout.news_item, parent, false);

        return new NewsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final NewsViewHolder holder, final int position) {

        //get new in focus
        News currentNews = newsList.get(position);

        //bind image data
        if (currentNews.getUrlToImage() != null && !TextUtils.isEmpty(currentNews.getUrlToImage())){
            Picasso.get()
                    .load(currentNews.getUrlToImage())
                    .networkPolicy(NetworkPolicy.OFFLINE)
                    .into(holder.newsImage, new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError(Exception e) {
                            Picasso.get()
                                    .load(currentNews.getUrlToImage())
                                    .into(holder.newsImage);
                        }
                    });
        } else {

            holder.newsImage.setImageResource(R.drawable.placeholder);

        }

        //bind text data
        holder.newsTitle.setText(currentNews.getTitle());
        holder.newsDescription.setText(currentNews.getDescription());
        holder.newsTime.setText(currentNews.getPublishedAt());

        //click
        holder.setItemClickListener((view, position1, isLongClick) -> {

            Intent detailIntent = new Intent(context, NewsDetail.class);


        });

    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }

    public class NewsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        //click interface
        private ItemClickListener itemClickListener;

        //widgets
        ImageView newsImage;
        TextView newsTitle, newsDescription, newsTime;

        public NewsViewHolder(@NonNull View itemView) {
            super(itemView);

            newsImage = itemView.findViewById(R.id.newsImage);
            newsTitle = itemView.findViewById(R.id.newsTitle);
            newsDescription = itemView.findViewById(R.id.newsDescription);
            newsTime = itemView.findViewById(R.id.newsTime);

            itemView.setOnClickListener(this);
        }

        public void setItemClickListener(ItemClickListener itemClickListener) {
            this.itemClickListener = itemClickListener;
        }

        @Override
        public void onClick(View view) {
            itemClickListener.onClick(view, getAdapterPosition(), false);
        }
    }

}
