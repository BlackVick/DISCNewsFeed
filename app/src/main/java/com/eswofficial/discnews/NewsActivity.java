package com.eswofficial.discnews;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.RelativeLayout;

import com.eswofficial.discnews.Adapters.NewsAdapter;
import com.eswofficial.discnews.Models.News;
import com.eswofficial.discnews.Services.NewsFetchService;
import com.eswofficial.discnews.Util.Common;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;

public class NewsActivity extends AppCompatActivity {

    //widgets
    private RelativeLayout titleBar;
    private RecyclerView newsRecycler;
    private AVLoadingIndicatorView loadingIndicator;
    private CardView newContentIndic;

    //data
    public NewsAdapter adapter;
    private List<News> newsList = new ArrayList<>();

    //thread
    private Handler handler = new Handler();
    private Runnable runnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //dark mode
        int nightModeFlags = this.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        switch (nightModeFlags) {
            case Configuration.UI_MODE_NIGHT_YES:
                setTheme(R.style.NightTheme);
                break;

            case Configuration.UI_MODE_NIGHT_NO:

            case Configuration.UI_MODE_NIGHT_UNDEFINED:
                setTheme(R.style.AppTheme);
                break;
        }


        setContentView(R.layout.activity_news);

        //widgets
        titleBar = findViewById(R.id.titleBar);
        newsRecycler = findViewById(R.id.newsRecycler);
        loadingIndicator = findViewById(R.id.loadingIndicator);
        newContentIndic = findViewById(R.id.newContentIndic);

        //initialize ui
        initUI();
    }

    private void initUI() {

        //hide new content card
        newContentIndic.setVisibility(View.GONE);

        //start fetch service
        try {
            startService(new Intent(this, NewsFetchService.class));
        } catch (Exception e){}

        //set scroll listener
        newsRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) {
                    titleBar.setVisibility(View.GONE);
                } else {
                    titleBar.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

            }
        });

        //start timed refresh
        startTimedRefresh();

    }

    private void startTimedRefresh() {

        //news
        handler.postDelayed(runnable = () -> {

            if (Paper.book().read(Common.FETCH_STATE).equals(Common.DONE_STATE)){

                checkNews();

            } else {

                startTimedRefresh();

            }

        }, 7000);

    }

    public void checkNews(){

        //new list
        List<News> newNewsList = Paper.book().read(Common.NEWS_TABLE);

        if (newsList.size() == 0){

            //set list
            newsList = newNewsList;

            //load news
            loadNews(newsList);

        } else

        if (newNewsList.size() > newsList.size()){

            //show new stuff
            newContentIndic.setVisibility(View.VISIBLE);

            //click new stuff
            newContentIndic.setOnClickListener(view -> {

                //clear former list
                newsList.clear();

                //hide new cotent button
                newContentIndic.setVisibility(View.GONE);

                //set list
                newsList = newNewsList;

                //load news
                loadNews(newsList);

                //scroll up
                newsRecycler.getLayoutManager().smoothScrollToPosition(newsRecycler, new RecyclerView.State(), 1);

            });

        }

    }

    private void loadNews(List<News> newsList) {

        //init recycler
        newsRecycler.setHasFixedSize(true);
        newsRecycler.setLayoutManager(new LinearLayoutManager(this));

        //adapter
        adapter = new NewsAdapter(this, NewsActivity.this, newsList);
        newsRecycler.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        //hide loading
        loadingIndicator.hide();

        //write fetch state
        Paper.book().write(Common.FETCH_STATE, Common.UNDONE_STATE);

        //refresh
        startTimedRefresh();

    }

    @Override
    protected void onResume() {
        super.onResume();
        startTimedRefresh();
    }

    @Override
    protected void onPause() {
        super.onPause();
        handler.removeCallbacks(runnable);
    }

    @Override
    protected void onStop() {
        super.onStop();
        handler.removeCallbacks(runnable);
    }
}