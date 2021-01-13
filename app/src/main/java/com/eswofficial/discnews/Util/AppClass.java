package com.eswofficial.discnews.Util;

import android.app.Application;
import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;
import io.paperdb.Paper;

public class AppClass extends Application {

    private static final String TAG = "DISC";

    @Override
    public void onCreate() {
        super.onCreate();

        //context paper db init
        Paper.init(getApplicationContext());

        //picasso cache mode
        Picasso.Builder builder = new Picasso.Builder(this);
        builder.downloader(new OkHttp3Downloader(this, Integer.MAX_VALUE));
        Picasso built = builder.build();
        built.setIndicatorsEnabled(false);
        built.setLoggingEnabled(true);
        Picasso.setSingletonInstance(built);

    }

}


