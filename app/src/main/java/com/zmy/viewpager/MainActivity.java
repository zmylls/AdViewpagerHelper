package com.zmy.viewpager;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.zmy.carousel.AdViewPageLayout;
import com.zmy.carousel.ViewPageHelper;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    AdViewPageLayout adViewPageLayout;
    ViewPageHelper viewPageHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        List<ViewPageHelper.AdInfo> adInfoList = new ArrayList<ViewPageHelper.AdInfo>();
        ViewPageHelper.AdInfo adInfo = new ViewPageHelper.AdInfo();
        adInfo.setAdUrl("http://pic1a.nipic.com/2008-11-26/200811269238739_2.jpg");
        adInfoList.add(adInfo);

        ViewPageHelper.AdInfo adInfo1 = new ViewPageHelper.AdInfo();
        adInfo1.setAdUrl("http://pic5.nipic.com/20100112/1896872_175216013131_2.jpg");
        adInfoList.add(adInfo1);

        ViewPageHelper.AdInfo adInfo2 = new ViewPageHelper.AdInfo();
        adInfo2.setAdUrl("http://pic9.nipic.com/20100813/2531170_123456384599_2.jpg");
        adInfoList.add(adInfo2);

        adInfoList.clear();

        adViewPageLayout = (AdViewPageLayout) findViewById(R.id.ad_view);
        viewPageHelper = new ViewPageHelper(this, adViewPageLayout, adInfoList);
        viewPageHelper.init();
        viewPageHelper.start();

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (viewPageHelper != null) {
            viewPageHelper.stop();
        }
    }
}
