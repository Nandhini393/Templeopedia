package com.aryvart.templeopedia.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.aryvart.templeopedia.R;

/**
 * Created by Android2 on 8/4/2016.
 */
public class GalleryVideoHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public ImageView templeImage,img_play;

    public GalleryVideoHolder(View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);
        templeImage = (ImageView) itemView.findViewById(R.id.img_temp_image);
        img_play=(ImageView)itemView.findViewById(R.id.play);
    }

    @Override
    public void onClick(View view) {
        Toast.makeText(view.getContext(), "Clicked Position = " + getPosition(), Toast.LENGTH_SHORT).show();
    }
}
