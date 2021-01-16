package com.eswofficial.discnews;

import android.app.Activity;
import android.app.Instrumentation;
import android.view.View;

import androidx.test.rule.ActivityTestRule;

import com.eswofficial.discnews.Models.News;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.junit.Assert.*;

public class NewsActivityTest {

    @Rule
    public ActivityTestRule<NewsActivity> mActivityTestRule = new ActivityTestRule<NewsActivity>(NewsActivity.class);

    private NewsActivity mActivity = null;

    @Before
    public void setUp() throws Exception {

        mActivity = mActivityTestRule.getActivity();

    }

    @Test
    public void testLaunch(){
        View view = mActivity.findViewById(R.id.newsRecycler);
        assertNotNull(view);

    }

    @After
    public void tearDown() throws Exception {

        mActivity = null;
    }
}