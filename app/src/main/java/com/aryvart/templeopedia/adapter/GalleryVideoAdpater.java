package com.aryvart.templeopedia.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aryvart.templeopedia.Interface.GalleryVideoInterface;
import com.aryvart.templeopedia.R;
import com.aryvart.templeopedia.bean.GalleryVideoBean;
import com.aryvart.templeopedia.imageCache.ImageLoader;

import java.util.List;

/**
 * Created by Android2 on 8/4/2016.
 */
public class GalleryVideoAdpater extends RecyclerView.Adapter<GalleryVideoHolder> {

    ImageLoader imageLoader;
    GalleryVideoInterface galleryVideo;
    private List<GalleryVideoBean> itemList;
    private Context context;

    public GalleryVideoAdpater(Context context, List<GalleryVideoBean> itemList, GalleryVideoInterface gv) {
        this.itemList = itemList;
        this.context = context;
        imageLoader = new ImageLoader(context);
        galleryVideo = gv;
    }

    @Override
    public GalleryVideoHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.gallery_video_content, null);
        GalleryVideoHolder rcv = new GalleryVideoHolder(layoutView);
        return rcv;
    }

    @Override
    public void onBindViewHolder(final GalleryVideoHolder holder, final int position) {

        imageLoader.DisplayImage(itemList.get(position).getStr_temp_image(), holder.templeImage);


        final GalleryVideoBean galleryVideoBean = itemList.get(position);
        if (galleryVideoBean.getStr_type().equalsIgnoreCase("video")) {
            holder.img_play.setVisibility(View.VISIBLE);
        } else if (galleryVideoBean.getStr_type().equalsIgnoreCase("image")) {
            holder.img_play.setVisibility(View.GONE);
        }
        Log.i("MYND", "JSON : " + galleryVideoBean.getGallery_json());

        holder.templeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (galleryVideoBean.getStr_type().equalsIgnoreCase("video")) {

                    galleryVideo.getVideoUrl(galleryVideoBean.getStr_url(), galleryVideoBean.getStrName(), "video", galleryVideoBean.getGallery_json(), String.valueOf(galleryVideoBean), galleryVideoBean.getStrGalleryType(), galleryVideoBean.getStrID());

                } else if (galleryVideoBean.getStr_type().equalsIgnoreCase("image")) {

                    galleryVideo.getVideoUrl(galleryVideoBean.getStr_url(), galleryVideoBean.getStr_temp_image(), "image", galleryVideoBean.getGallery_json(), String.valueOf(galleryVideoBean), galleryVideoBean.getStrGalleryType(), galleryVideoBean.getStrID());
                }

            }
        });
        // holder.templeImage.setImageResource(itemList.get(position).getStr_temp_image());
    }

    @Override
    public int getItemCount() {
        return this.itemList.size();
    }
}
