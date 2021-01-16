package com.eswofficial.discnews;

import android.view.View;

import androidx.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.*;

public class NewsDetailTest {

    @Rule
    public ActivityTestRule<NewsDetail> mActivityTestRule = new ActivityTestRule<NewsDetail>(NewsDetail.class);

    private NewsDetail mActivity = null;

    @Before
    public void setUp() throws Exception {

        mActivity = mActivityTestRule.getActivity();

    }

    @Test
    public void testLaunch(){
        View view = mActivity.findViewById(R.id.readMore);
        assertNotNull(view);

    }

    @After
    public void tearDown() throws Exception {

        mActivity = null;
    }
}