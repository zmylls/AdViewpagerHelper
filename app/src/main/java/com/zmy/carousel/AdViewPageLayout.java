package com.zmy.carousel;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.zmy.viewpager.R;


/**
 * Created by zhangmingyao on 16/1/26.
 * Email : 501863760@qq.com
 */
public class AdViewPageLayout extends FrameLayout {

    private ViewPager viewPager;
    private LinearLayout layoutDots;
    private ImageView ivNoAd;//只有一张广告时，显示的

    private Drawable errorDrawable;

    public Drawable getErrorDrawable() {
        return errorDrawable;
    }

    public void setErrorDrawable(Drawable errorDrawable) {
        this.errorDrawable = errorDrawable;
        ivNoAd.setImageDrawable(errorDrawable);
    }

    public AdViewPageLayout(Context context) {
        super(context);
        init(context);
    }

    public AdViewPageLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public AdViewPageLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context) {
        init(context, null);
    }

    private void init(Context context, AttributeSet attrs) {
        LayoutParams parasm = new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT);

        if (null != attrs) {

            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.AdViewPageLayout);
            errorDrawable = typedArray.getDrawable(R.styleable.AdViewPageLayout_errordrawable);
            typedArray.recycle();

        }

        if (errorDrawable == null) {
            errorDrawable = getResources().getDrawable(R.mipmap.ic_launcher);
        }

        ivNoAd = new ImageView(context);
        ivNoAd.setLayoutParams(parasm);
        ivNoAd.setScaleType(ImageView.ScaleType.FIT_XY);
        ivNoAd.setImageDrawable(errorDrawable);

        viewPager = new ViewPager(context);
        viewPager.setLayoutParams(parasm);

        layoutDots = new LinearLayout(context);
        layoutDots.setBackgroundResource(android.R.color.transparent);
        layoutDots.setOrientation(LinearLayout.HORIZONTAL);
        LayoutParams p = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        p.gravity = Gravity.BOTTOM | Gravity.RIGHT;
        int padding = DensityUtil.dip2px(context, 3);
        layoutDots.setPadding(0, padding, 0, padding);
        layoutDots.setLayoutParams(p);
        layoutDots.setHorizontalGravity(Gravity.RIGHT);

        addView(ivNoAd);
        addView(viewPager);
        addView(layoutDots);

    }

    public void withNoAd() {
        ivNoAd.setVisibility(View.VISIBLE);
        viewPager.setVisibility(View.GONE);
        layoutDots.setVisibility(View.GONE);
    }

    public void onlyOneAd() {
        ivNoAd.setVisibility(View.GONE);
        viewPager.setVisibility(View.VISIBLE);
        layoutDots.setVisibility(View.GONE);
    }

    public void moreThanOneAd() {
        ivNoAd.setVisibility(View.GONE);
        viewPager.setVisibility(View.VISIBLE);
        layoutDots.setVisibility(View.VISIBLE);
    }

    public ViewPager getViewPager() {
        return viewPager;
    }

    public void setCurrentItem(int index) {
        viewPager.setCurrentItem(index, false);
    }

    public LinearLayout getLayoutDots() {
        return layoutDots;
    }

//    public ImageView getIvBanner() {
//        return ivBanner;
//    }

    public void removeDots() {
        this.layoutDots.removeAllViews();
    }

    public void setOnPageChangeListener(ViewPager.OnPageChangeListener listener) {
        this.viewPager.setOnPageChangeListener(listener);
    }

    public void setAdapter(PagerAdapter adapter) {
        this.viewPager.setAdapter(adapter);
    }

    public void addDots(View dot, LinearLayout.LayoutParams p) {
        this.layoutDots.addView(dot, p);
    }
}
