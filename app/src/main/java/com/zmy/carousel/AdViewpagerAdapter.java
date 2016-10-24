package com.zmy.carousel;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

/**
 * Created by zhangmingyao on 16/1/26.
 * Email : 501863760@qq.com
 */
public class AdViewpagerAdapter<T extends View> extends PagerAdapter {

    private List<T> imgs;

    public AdViewpagerAdapter(List<T> imgs) {
        this.imgs = imgs;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        T imageView = imgs.get(position % getCount());
        ((ViewPager) container).addView(imageView);

        return imageView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView(imgs.get(position % getCount()));
    }

    @Override
    public int getCount() {
        return imgs.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
}
