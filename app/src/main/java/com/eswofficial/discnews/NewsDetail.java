package com.eswofficial.discnews;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.eswofficial.discnews.Util.Common;
import com.squareup.picasso.Picasso;

public class NewsDetail extends AppCompatActivity {

    //widgets
    private TextView newsTitle, newsTime, newsAuthor, newsDescription, newsContent;
    private ImageView newsImage;
    private CardView readMore, backBtn;

    //intent values
    private String theNewsTitle;
    private String theNewsTime;
    private String theNewsDesc;
    private String theNewsAuthor;
    private String theNewsContent;
    private String theNewsImage;
    private String theNewsUrl;

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

        setContentView(R.layout.activity_news_detail);

        //intent values
        theNewsTitle = getIntent().getStringExtra(Common.NEWS_TITLE);
        theNewsTime = getIntent().getStringExtra(Common.NEWS_TIMESTAMP);
        theNewsDesc = getIntent().getStringExtra(Common.NEWS_DESC);
        theNewsAuthor = getIntent().getStringExtra(Common.NEWS_AUTHOR);
        theNewsContent = getIntent().getStringExtra(Common.NEWS_CONTENT);
        theNewsImage = getIntent().getStringExtra(Common.NEWS_IMAGE);
        theNewsUrl = getIntent().getStringExtra(Common.NEWS_URL);

        //widgets
        newsTitle = findViewById(R.id.newsTitle);
        newsTime = findViewById(R.id.newsTime);
        newsAuthor = findViewById(R.id.newsAuthor);
        newsDescription = findViewById(R.id.newsDescription);
        newsContent = findViewById(R.id.newsContent);
        newsImage = findViewById(R.id.newsImage);
        readMore = findViewById(R.id.readMore);
        backBtn = findViewById(R.id.backBtn);

        //init ui
        initializeUI();
    }

    private void initializeUI() {

        //back
        backBtn.setOnClickListener(view -> {
            finish();
            overridePendingTransition(R.anim.slide_right, R.anim.slide_out_right);
        });

        //set details
        newsTitle.setText(theNewsTitle);
        newsDescription.setText(theNewsDesc);
        newsContent.setText(theNewsContent);
        newsAuthor.setText(theNewsAuthor);
        newsTime.setText(theNewsTime);

        //image
        if (!TextUtils.isEmpty(theNewsImage)){
            Picasso.get()
                    .load(theNewsImage)
                    .into(newsImage);
        }

        //read more
        readMore.setOnClickListener(view -> {

            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(theNewsUrl));
            startActivity(i);

        });

    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.slide_right, R.anim.slide_out_right);
    }
}