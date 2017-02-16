package com.aryvart.templeopedia.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.aryvart.templeopedia.Interface.GalleryVideoInterface;
import com.aryvart.templeopedia.PinchZoom.TouchImageView;
import com.aryvart.templeopedia.R;
import com.aryvart.templeopedia.bean.GalleryVideoBean;
import com.aryvart.templeopedia.imageCache.ImageLoader;

import java.util.List;

/**
 * Created by android2 on 2/9/16.
 */
public class CustomPagerAdapter extends PagerAdapter {

    GalleryVideoInterface galleryVideo;
    private Context mContext;
    private List<GalleryVideoBean> itemList;

    ImageLoader imvLoader;

    public CustomPagerAdapter(Context context, List<GalleryVideoBean> itemList, GalleryVideoInterface gv) {
        mContext = context;
        this.itemList = itemList;
        imvLoader = new ImageLoader(mContext);
        galleryVideo = gv;
    }

    @Override
    public Object instantiateItem(ViewGroup collection, final int position) {

        GalleryVideoBean customPagerEnum = itemList.get(position);
        //LayoutInflater inflater = LayoutInflater.from(mContext);
        //   ViewGroup layout = (ViewGroup) inflater.inflate(R.layout.view_gallery_image_content, collection, false);

        RelativeLayout rllay = null;
        TouchImageView imV = new TouchImageView(mContext);


        imvLoader.DisplayImage(itemList.get(position).getStr_temp_image(), imV);


        if (customPagerEnum.getStr_type().equalsIgnoreCase("video")) {
            //imvLoader.DisplayImage(customPagerEnum.getStr_thumbnail(), imV);
            rllay = new RelativeLayout(mContext);
            RelativeLayout.LayoutParams lParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
            rllay.setLayoutParams(lParams);


            LinearLayout.LayoutParams lMainparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            imV.setLayoutParams(lMainparams);


            ImageView img_play = new ImageView(mContext);
            RelativeLayout.LayoutParams ImgPlayParams = new RelativeLayout.LayoutParams(94, 94);
            img_play.setLayoutParams(ImgPlayParams);
            img_play.setBackgroundResource(R.drawable.play);
            ImgPlayParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);

            rllay.addView(imV);
            rllay.addView(img_play);

        }

        imV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GalleryVideoBean galleryVideoBean = itemList.get(position);
                if (galleryVideoBean.getStr_type().equalsIgnoreCase("video")) {

                    galleryVideo.getVideoUrl(galleryVideoBean.getStr_url(), galleryVideoBean.getStrName(), "video", galleryVideoBean.getGallery_json(), String.valueOf(galleryVideoBean), galleryVideoBean.getStrGalleryType(), "");

                } else if (galleryVideoBean.getStr_type().equalsIgnoreCase("image")) {

                    galleryVideo.getVideoUrl(galleryVideoBean.getStr_url(), galleryVideoBean.getStr_temp_image(), "image", galleryVideoBean.getGallery_json(), String.valueOf(galleryVideoBean), galleryVideoBean.getStrGalleryType(), "");
                }
            }
        });

     /*   if (collection.getChildCount() > 0) {
            collection.removeAllViews();
            Log.i("NN", "hii if...");
        } else {*/
        Log.i("NN", "hii else...");

        if (customPagerEnum.getStr_type().equalsIgnoreCase("video")) {
            collection.addView(rllay, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            return rllay;
        } else {
            collection.addView(imV, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            return imV;
        }
    }

    @Override
    public void destroyItem(ViewGroup collection, int position, Object view) {
        collection.removeView((View) view);
    }

    @Override
    public int getCount() {
        return itemList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getString(position);
    }

}
