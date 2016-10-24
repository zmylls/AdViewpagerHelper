package com.zmy.viewpager;

import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
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
        adInfo.setAdUrl("http://photo.enterdesk.com/2010-10-24/enterdesk.com-3B11711A460036C51C19F87E7064FE9D.jpg");
        adInfoList.add(adInfo);

        ViewPageHelper.AdInfo adInfo1 = new ViewPageHelper.AdInfo();
        adInfo1.setAdUrl("http://imgstore.cdn.sogou.com/app/a/100540002/503008.png");
        adInfoList.add(adInfo1);

        ViewPageHelper.AdInfo adInfo2 = new ViewPageHelper.AdInfo();
        adInfo2.setAdUrl("http://pic9.nipic.com/20100813/2531170_123456384599_2.jpg");
        adInfoList.add(adInfo2);

//        adInfoList.clear();

        adViewPageLayout = (AdViewPageLayout) findViewById(R.id.ad_view);

        viewPageHelper = new ViewPageHelper.Builder(MainActivity.this)
                .initView(adViewPageLayout)
                .imageLoader(new ViewPageHelper.ImageLoaderListener() {
                    @Override
                    public void loaderBitmap(String url, Drawable errorDrawable, ImageView img) {
                        Glide.with(MainActivity.this).load(url).error(errorDrawable).into(img);
                    }
                })
                .click(new ViewPageHelper.OnBannerClickListenr() {
                    @Override
                    public void onBannerClick(ViewPageHelper.AdInfo bannerInfo) {
                        Toast.makeText(MainActivity.this,bannerInfo.getAdUrl(),Toast.LENGTH_SHORT).show();
                    }
                })
                .initData(adInfoList)
                .build();

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
