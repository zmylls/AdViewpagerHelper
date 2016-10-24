package com.zmy.carousel;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.zmy.viewpager.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by zhangmingyao on 16/1/26.
 * Email : 501863760@qq.com
 */
public class ViewPageHelper {

    private static final int CHANGE_VIEW = 0x2330;

    private static final int MAX_SIZE = 5;

    private int mPreDotIndex = 0;
    private Timer timer;
    private TimerTask timerTask;

    private Context context;
    private AdViewPageLayout adViewPageLayout;
    private List<AdInfo> adInfos = new ArrayList<AdInfo>();//广告

    private List<View> dots = new ArrayList<View>();
    private List<ImageView> imgs = new ArrayList<ImageView>();
    private AdViewpagerAdapter adViewpagerAdapter;


    private OnBannerClickListenr onBannerClickListenr;

    private ImageLoaderListener mImageLoaderListener;

    private Drawable errorDrawable;

    public ViewPageHelper(Context context, AdViewPageLayout adViewPageLayout, List<AdInfo> objs) {
        this.context = context;
        this.adViewPageLayout = adViewPageLayout;
        this.adInfos = objs;
        this.errorDrawable = adViewPageLayout.getErrorDrawable();
        this.adViewPageLayout.setOnPageChangeListener(pageChangeListener);
    }

    public static class Builder{
        private Context context;
        private AdViewPageLayout adViewPageLayout;
        private List<AdInfo> adInfos = new ArrayList<AdInfo>();//广告

        private ImageLoaderListener mImageLoaderListener;
        private OnBannerClickListenr mOnBannerClickListner;

        public Builder(Context context){
            this.context=context;
        }

        public Builder initView(AdViewPageLayout layout){
            this.adViewPageLayout = layout;
            return this;
        }

        public Builder imageLoader(ImageLoaderListener listener){
            this.mImageLoaderListener = listener;
            return this;
        }

        public Builder click(OnBannerClickListenr clickListenr){
            this.mOnBannerClickListner = clickListenr;
            return this;
        }

        public Builder initData(List<AdInfo> data){
            this.adInfos = data;
            return this;
        }

        public ViewPageHelper build(){
            if(adViewPageLayout == null){
                throw new RuntimeException("AdViewPageLayout can not be null");
            }

            if(mImageLoaderListener == null){
                throw new RuntimeException("must init ImageLoaderListener");
            }

            if(adInfos == null){
                adInfos = new ArrayList<AdInfo>();
            }

            ViewPageHelper viewPageHelper= new ViewPageHelper(context, adViewPageLayout, adInfos);;
            viewPageHelper.setImageLoaderListener(mImageLoaderListener);
            viewPageHelper.setBannerClickListenr(mOnBannerClickListner);
            viewPageHelper.init();
            return viewPageHelper;
        }

    }

    private ImageView createImageView(Context context, int index) {
        ImageView imageView = new ImageView(context);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        ViewGroup.LayoutParams p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        imageView.setLayoutParams(p);
        imageView.setImageResource(R.mipmap.ic_launcher);

        setBannerImageClick(imageView, adInfos.get(index));
        mImageLoaderListener.loaderBitmap(adInfos.get(index).getAdUrl(),errorDrawable,imageView);

        return imageView;
    }

    private void createDotView(Context context) {
        //初始化点
        dots.clear();
        adViewPageLayout.removeDots();

        int realdLength = adInfos.size();

        int dotSize = DensityUtil.dip2px(context, 8);
        int dotMarginRight = DensityUtil.dip2px(context, 4);

        for (int i = 0; i < realdLength; i++) {
            View view = new View(context);
            view.setBackgroundColor(Color.BLUE);
            LinearLayout.LayoutParams pDot = new LinearLayout.LayoutParams(dotSize, dotSize);
            pDot.rightMargin = dotMarginRight;
            pDot.gravity = Gravity.CENTER;
            if (i == 0) {
                view.setBackgroundResource(R.drawable.dot_selected);
            } else {
                view.setBackgroundResource(R.drawable.dot_normal);
            }

            dots.add(view);
            adViewPageLayout.addDots(view, pDot);
        }
    }

    private void init() {

        if(mImageLoaderListener == null){
            throw new RuntimeException("must init ImageLoaderListener");
        }

        int realLength = adInfos.size();
        if (realLength <= 0) {
            adViewPageLayout.withNoAd();//没有广告 显示一张默认图片
            return;
        }

        if (realLength == 1) {
            adViewPageLayout.onlyOneAd();

            imgs.clear();
            imgs.add(createImageView(context, 0));
        } else {
            adViewPageLayout.moreThanOneAd();
            //初始化image
            imgs.clear();

            if (adInfos.size() > MAX_SIZE) {//最多显示5个
                adInfos = adInfos.subList(0, MAX_SIZE);
            }

            int length = adInfos.size() + 2;//头尾都加一个
            for (int i = 0; i < length; i++) {
                //默认加载第一个和最后一个对应的图片
                if (i == 0) {
                    imgs.add(createImageView(context, adInfos.size() - 1));
                } else if (i == length - 1) {
                    imgs.add(createImageView(context, 0));
                } else {
                    imgs.add(createImageView(context, i - 1));
                }
            }

            createDotView(context);

        }
        adViewpagerAdapter = new AdViewpagerAdapter(imgs);
        adViewPageLayout.setAdapter(adViewpagerAdapter);
        adViewPageLayout.getViewPager().setCurrentItem(1);

    }

    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (msg.what == CHANGE_VIEW) {
                if (adViewPageLayout.getViewPager() != null) {

                    int count = adViewpagerAdapter.getCount();//viewpager共有多少个item
                    int index = adViewPageLayout.getViewPager().getCurrentItem();//当前index
                    index = index % (count - 2) + 1; //这里修改过
                    adViewPageLayout.setCurrentItem(index);
                }
            }
        }
    };

    public void start() {
        if (adInfos.size() >= 2) {
            stop();
            resumeTimer();
        }
    }

    private void resumeTimer() {
        timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                mHandler.sendEmptyMessage(CHANGE_VIEW);
            }
        };
        timer.schedule(timerTask, 5 * 1000, 5 * 1000);
    }

    public void stop() {
        if (null != timerTask) {
            timerTask.cancel();
            timerTask = null;
        }
        if (null != timer) {
            timer.cancel();
            timer = null;
        }
    }

    private ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int index) {

            //无限循环
            int pageIndex = index;
            if (index == 0) {
                // 当视图在第一个时，将页面号设置为图片的最后一张。
                pageIndex = adInfos.size();
            } else if (index == adInfos.size() + 1) {
                // 当视图在最后一个是,将页面号设置为图片的第一张。
                pageIndex = 1;
            }
            if (index != pageIndex) {
                adViewPageLayout.setCurrentItem(pageIndex);
            }

            //设置点的显示
            //假如大小为3，则pageIndex的范围是1~3
            dots.get(mPreDotIndex).setBackgroundResource(R.drawable.dot_normal);
            int currentIndex = pageIndex - 1 < 0 ? 0 : pageIndex - 1;
            dots.get(currentIndex).setBackgroundResource(R.drawable.dot_selected);
            mPreDotIndex = currentIndex;

            mImageLoaderListener.loaderBitmap(adInfos.get(currentIndex).getAdUrl(), errorDrawable, imgs.get(pageIndex));
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }
    };

    private void setBannerImageClick(ImageView imageView, final AdInfo adInfo) {
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onBannerClickListenr != null) {
                    onBannerClickListenr.onBannerClick(adInfo);
                }
            }
        });
    }

    private void setBannerClickListenr(OnBannerClickListenr listenr) {
        this.onBannerClickListenr = listenr;
    }

    private void setImageLoaderListener(ImageLoaderListener listener) {
        this.mImageLoaderListener = listener;
    }

    /**
     * item 点击事件
     */
    public interface OnBannerClickListenr {
        public void onBannerClick(AdInfo bannerInfo);
    }

    public interface ImageLoaderListener {
        public void loaderBitmap(String url, Drawable errorDrawable, ImageView img);
    }

    public static class AdInfo {
        private String adUrl;

        public String getAdUrl() {
            return adUrl;
        }

        public void setAdUrl(String adUrl) {
            this.adUrl = adUrl;
        }
    }
}
