package com.aryvart.templeopedia;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aryvart.templeopedia.Interface.GalleryVideoInterface;
import com.aryvart.templeopedia.PinchZoom.ExtendedViewPager;
import com.aryvart.templeopedia.adapter.CustomPagerAdapter;
import com.aryvart.templeopedia.adapter.GalleryVideoAdpater;
import com.aryvart.templeopedia.bean.GalleryVideoBean;
import com.aryvart.templeopedia.genericclasses.GeneralData;
import com.aryvart.templeopedia.imageCache.ImageLoader;
import com.aryvart.templeopedia.test.VideoPlayerActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

/**
 * Created by Android2 on 8/26/2016.
 */
public class ViewGalleryImage extends AppCompatActivity implements GalleryVideoInterface {
    ImageView img_gallery_image;
    String str_gallery_image, str_gallery_json, str_pos, str_gallery_type, str_tab_value, str_GalId;
    ImageLoader imgLoader;
    Context context;

    ProgressDialog dialog;
    LinearLayout ll_gallery_parent;


    private PagerAdapter mPagerAdapter;
    int nIndex = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_gallery_image);

        GeneralData.context = this;


        ExtendedViewPager mViewPager = (ExtendedViewPager) findViewById(R.id.view_pager);
        dialog = new ProgressDialog(ViewGalleryImage.this);
        str_gallery_image = getIntent().getStringExtra("gallery_image");
        str_gallery_json = getIntent().getStringExtra("gallery_json");


        str_pos = getIntent().getStringExtra("gallery_position");
        str_gallery_type = getIntent().getStringExtra("gallery_type");
        str_tab_value = getIntent().getStringExtra("gallery_tab_value");

        str_GalId = getIntent().getStringExtra("gallery_iD");


        context = this;
        imgLoader = new ImageLoader(context);
        //ll_gallery_parent = (LinearLayout) findViewById(R.id.ll_gallery_parent);

        try {
            //Error Fixed ...............................
            //Please check.....................


            ArrayList<GalleryVideoBean> beanArrayList = new ArrayList<GalleryVideoBean>();


            Log.i("ND", "Value : " + str_gallery_json);

            JSONObject jsobj = new JSONObject(str_gallery_json);

            Log.i("HH", "strResp : " + str_gallery_json);
            JSONObject jsoBj = null;


            /*if (str_tab_value.equalsIgnoreCase("all")) {
                templesCategoriesArray = jsobj.getJSONArray("all");

            } else if (str_tab_value.equalsIgnoreCase("Kumbabishekam")) {
                templesCategoriesArray = jsobj.getJSONArray("Kumbabishekam");

            } else if (str_tab_value.equalsIgnoreCase("Festival")) {
                templesCategoriesArray = jsobj.getJSONArray("Festival");

            }*/

            JSONArray templesCategoriesArray = jsobj.getJSONArray(str_tab_value);

            for (int i = 0; i < templesCategoriesArray.length(); i++) {
                JSONObject providersServiceJSONobject = templesCategoriesArray.getJSONObject(i);
                GalleryVideoBean bean = new GalleryVideoBean();
                if (providersServiceJSONobject.getString("type").equalsIgnoreCase("image")) {
                    String strURL = GeneralData.SERVER_IP + "uploads/gallery/" + providersServiceJSONobject.getString("url");
                    String strImageName = providersServiceJSONobject.getString("image");
                    bean.setStr_temp_image(GeneralData.SERVER_IP + "uploads/gallery/" + strImageName);
                    bean.setStr_type(providersServiceJSONobject.getString("type"));
                    bean.setStrGalleryType(providersServiceJSONobject.getString("gallery_type"));
                    bean.setStrID(providersServiceJSONobject.getString("id"));
                    bean.setStrName(strImageName);
                    bean.setGallery_json("");
                    bean.setStr_url(strURL);
                    Log.i("url", strURL);
                } else if (providersServiceJSONobject.getString("type").equalsIgnoreCase("video")) {
                    String strURL = GeneralData.SERVER_IP + "uploads/video/" + providersServiceJSONobject.getString("url");

                    String strVideoName = providersServiceJSONobject.getString("video");
                    String strThumbnail = providersServiceJSONobject.getString("thumbnail");
                    bean.setStr_temp_image(GeneralData.SERVER_IP + "uploads/thumbnail/" + strThumbnail);
                    bean.setStr_type(providersServiceJSONobject.getString("type"));
                    bean.setStrName(strVideoName);
                    bean.setStrID(providersServiceJSONobject.getString("id"));
                    bean.setStr_url(strURL);
                    Log.i("url", strURL);
                }
                beanArrayList.add(bean);
            }

            Log.i("ND", "beanArrayList Size : " + beanArrayList.size() + "templesCategoriesArray : " + templesCategoriesArray.length());
            if (beanArrayList.size() == templesCategoriesArray.length()) {
                CreateVIew(beanArrayList, mViewPager);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void CreateVIew(ArrayList<GalleryVideoBean> beanArrayList, ViewPager viewPager) {
        ArrayList<GalleryVideoBean> orderedAarraylist = new ArrayList<GalleryVideoBean>();

        Log.i("ND", "str_GalId : " + str_GalId);

        for (int i = 0; i < beanArrayList.size(); i++) {
            Log.i("ND", "Value : " + beanArrayList.get(i).getStrID());
            GalleryVideoBean GVB = beanArrayList.get(i);
            String strID = GVB.getStrID();

            if (strID.equals(String.valueOf(str_GalId))) {
                nIndex = i;
                break;
            }

        }

        for (int j = nIndex; j < beanArrayList.size(); j++) {
            orderedAarraylist.add(beanArrayList.get(j));

        }

        if (nIndex != 0) {
            for (int k = 0; k < nIndex; k++) {
                orderedAarraylist.add(beanArrayList.get(k));
            }

        }


        CustomPagerAdapter rcAdapter = new CustomPagerAdapter(context, orderedAarraylist, (GalleryVideoInterface) context);
        viewPager.setAdapter(rcAdapter);
    }

    private void createChild(String gallery_item, final String gallery_type, final String url, final String video) {


        LayoutInflater inflater = LayoutInflater.from(ViewGalleryImage.this);

        final View view1 = inflater.inflate(R.layout.view_gallery_image_content, null);
        ImageView img_temple_image = (ImageView) view1.findViewById(R.id.gallery_image);
        if (gallery_type.equalsIgnoreCase("image")) {
            imgLoader.DisplayImage(gallery_item, img_temple_image);
        } else if (gallery_type.equalsIgnoreCase("video")) {
            imgLoader.DisplayImage(gallery_item, img_temple_image);

        }

        view1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (gallery_type.equalsIgnoreCase("video")) {
                    GetVideo getVideo = new GetVideo(context, url, video);
                    getVideo.execute();
                }
            }
        });
        ll_gallery_parent.addView(view1);

    }

    @Override
    public void getVideoUrl(String video_url, String strFileName, String str_type, String str_json, String gallery_position, String gallery_type, String strGalID) {

        if (str_type.equalsIgnoreCase("video")) {

            GetVideo getVideo = new GetVideo(context, video_url, strFileName);
            getVideo.execute();
        }
    }

    class GetVideo extends AsyncTask {

        String temp_id = null;
        String sResponse = null;
        Context cont;
        String str_typ;
        String strFname;

        public GetVideo(Context context, String str_temp_id, String strFileName) {

            this.cont = context;
            this.temp_id = str_temp_id;
            this.strFname = strFileName;

        }

        @Override
        protected void onPreExecute() {
            ViewGalleryImage.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (dialog != null && !dialog.isShowing()) {
                        dialog.setMessage("File getting download.....");
                        dialog.setIndeterminate(false);
                        dialog.setCancelable(false);
                        dialog.show();
                    }
                }
            });

        }

        @Override
        protected Object doInBackground(Object[] param) {
            try {

                URL url = new URL(temp_id);
                long startTime = System.currentTimeMillis();


                //Open a connection to that URL.
                URLConnection ucon = url.openConnection();

                //this timeout affects how long it takes for the app to realize there's a connection problem
                //ucon.setReadTimeout(TIMEOUT_CONNECTION);
                //ucon.setConnectTimeout(TIMEOUT_SOCKET);


                //Define InputStreams to read from the URLConnection.                // uses 3KB download buffer
                InputStream is = ucon.getInputStream();
                BufferedInputStream inStream = new BufferedInputStream(is, 1024 * 5);
                FileOutputStream outStream = new FileOutputStream(Environment.getExternalStorageDirectory() + "/" + strFname);

                byte[] buff = new byte[5 * 1024];

                //Read bytes (and store them) until there is nothing more to read(-1)
                int len;
                while ((len = inStream.read(buff)) != -1) {
                    outStream.write(buff, 0, len);
                }

                //clean up
                outStream.flush();
                outStream.close();
                inStream.close();

            } catch (Exception e) {
                e.printStackTrace();
            }
            return sResponse;
        }

        @Override
        protected void onPostExecute(Object response) {
            dialog.dismiss();
            try {
                //Error Fixed ...............................
                //Please check.....................


                // File ff=new File(Environment.getExternalStorageDirectory() + "/" + strFname);
                String strPath = Environment.getExternalStorageDirectory() + "/" + strFname;

                Intent i = new Intent(ViewGalleryImage.this, VideoPlayerActivity.class);
                i.putExtra("vURL", strPath);
                startActivity(i);

                /*
                if(ff.exists())
                {

                }*/

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}
