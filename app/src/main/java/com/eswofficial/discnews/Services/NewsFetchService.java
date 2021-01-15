package com.eswofficial.discnews.Services;

import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.eswofficial.discnews.Api.NewsResponse;
import com.eswofficial.discnews.Api.RetrofitClient;
import com.eswofficial.discnews.Models.News;
import com.eswofficial.discnews.Util.Common;

import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewsFetchService extends Service {

    //values
    public boolean isRunning = false;

    //session
    private Handler handler = new Handler();
    private Runnable runnable;

    public NewsFetchService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        //broadcast start
        isRunning = true;

        //write default state
        Paper.book().write(Common.FETCH_STATE, Common.UNDONE_STATE);

        //start check
        startFetch();

        return START_STICKY;
    }

    private void startFetch() {

        //api get call
        RetrofitClient
                .getInstance()
                .getApi()
                .fetchNews("techcrunch", Common.NEWS_API_KEY)
                .enqueue(new Callback<NewsResponse>() {
                    @Override
                    public void onResponse(Call<NewsResponse> call, Response<NewsResponse> response) {

                        if (response.code() == 200){

                            NewsResponse resp = response.body();
                            List<News> newsList = resp.getArticles();

                            //save to database
                            Paper.book().write(Common.NEWS_TABLE, newsList);

                            //write fetch state
                            Paper.book().write(Common.FETCH_STATE, Common.DONE_STATE);

                        }

                    }

                    @Override
                    public void onFailure(Call<NewsResponse> call, Throwable t) {

                    }
                });

        //repeat check at 10 minute interval
        repeatFetch();
    }

    private void repeatFetch() {

        //repeat check at 10 minute interval
        handler.postDelayed(runnable = () -> {

            //api get call
            RetrofitClient
                    .getInstance()
                    .getApi()
                    .fetchNews("techcrunch", Common.NEWS_API_KEY)
                    .enqueue(new Callback<NewsResponse>() {
                        @RequiresApi(api = Build.VERSION_CODES.N)
                        @Override
                        public void onResponse(Call<NewsResponse> call, Response<NewsResponse> response) {

                            if (response.code() == 200){

                                NewsResponse resp = response.body();
                                List<News> newsList = resp.getArticles();
                                List<News> oldNewsList = Paper.book().read(Common.NEWS_TABLE);

                                for (int i = 0; i < newsList.size(); i++){

                                    if (!containsTitle(oldNewsList, newsList.get(i).getTitle())){
                                        oldNewsList.add(newsList.get(i));
                                    }

                                }

                                //save back to db
                                Paper.book().write(Common.NEWS_TABLE, oldNewsList);

                                //write fetch state
                                Paper.book().write(Common.FETCH_STATE, Common.DONE_STATE);

                            }

                        }

                        @Override
                        public void onFailure(Call<NewsResponse> call, Throwable t) {

                        }
                    });

        }, 600000);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public boolean containsTitle(final List<News> list, final String title){
        return list.stream().map(News::getTitle).filter(title::equals).findFirst().isPresent();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        //broadcast the state
        isRunning = false;

        //stop repeat thread
        handler.removeCallbacks(runnable);

        //stop service
        stopSelf();
    }

}