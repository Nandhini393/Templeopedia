package com.aryvart.templeopedia;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.view.inputmethod.InputMethodSubtype;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SlidingDrawer;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.aryvart.templeopedia.Interface.GetTempleLocation;
import com.aryvart.templeopedia.adapter.SpinnerAdapter;
import com.aryvart.templeopedia.bean.TempleListBean;
import com.aryvart.templeopedia.genericclasses.DirectionsJSONParser;
import com.aryvart.templeopedia.genericclasses.GPSTracker_Provider;
import com.aryvart.templeopedia.genericclasses.GeneralData;
import com.aryvart.templeopedia.genericclasses.MultipartUtility;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * Created by android2 on 1/9/16.
 */
public class TempleLocationNew extends AppCompatActivity implements GetTempleLocation, OnMapReadyCallback, Animation.AnimationListener {
    SlidingDrawer slidingDrawer;
    ImageView img_route, slider_arrow;
    LinearLayout ll_route;
    int ncount = 0;
    public static boolean isGPSEnabled = false;
    public static boolean isNetworkEnabled = false;
    static SharedPreferences sharedpreferences;
    LocationManager locationManager;
    GoogleMap googleMap;
    GeneralData gD;
    Double lat = 0.0, longi = 0.0;
    Marker TP;
    String result;
    Context context;
    LatLng TutorialsPoint = new LatLng(lat, longi);
    LatLng TutorialsPoints = new LatLng(lat, longi);
    ArrayList<LatLng> markerPoints;
    String distance = "";
    String duration = "";
    ImageView img_home, img_lang, img_search, img_back, img_menu;
    RelativeLayout rl_choose_lang, rl_content;
    int n_count = 0;
    View itemView1;
    LinearLayout menu_layout;
    String str_temp_id, str_lat, str_long, str_content, str_temp_name, str_temp_address, str_current_address;
    TextView txt_temple_name, txt_loc_content, txt_temp_addr, txt_loc_title;
    ImageView img_english, img_tamil, img_gps;
    EditText txt_current_addr;
    LinearLayout ll_tamil, ll_english, lay_about, lay_specialities, lay_timing, lay_history, lay_administration, lay_gallery, lay_festival, lay_art, lay_location;
    String s_lat, s_long, str_admini;
    String strInitialLat, strInitialLang, str_type;
    ProgressDialog dialog;
    Button btn_search;
    String strAreaId, strStateId, strCityId, strFestivalId, strDietyId, str_lang;
    Spinner spin_area, spin_state, spin_city, spin_deity, spin_festival;
    TextView txt_abt_temple, txt_nearby, txt_dharshan, txt_history, txt_admin, txt_gallery, txt_loc, txt_festival, txt_art, txt_special, txt_search_title;

    int mMode = 0;
    final int MODE_DRIVING = 0;
    final int MODE_TRANSIT = 1;
    final int MODE_WALKING = 2;
    final int MODE_BICYCLING = 3;
    ImageView img_driving_route, img_transit_route, img_walking_route, img_cycle_route, img_search_route;
    String str_slected_route = "driving";
    LinearLayout img_driving, img_transit, img_walking, img_cycling;


    Animation slideUp, slideDown;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.location_new_layout);


        GeneralData.context = this;

        slidingDrawer = (SlidingDrawer) findViewById(R.id.slidingDrawer2);
        img_route = (ImageView) findViewById(R.id.route);
        ll_route = (LinearLayout) findViewById(R.id.ll_route);
        slider_arrow = (ImageView) findViewById(R.id.arrow);
        img_search_route = (ImageView) findViewById(R.id.img_route_search);
        context = this;

        gD = new GeneralData(context);
        gD.googleMapGeneral = null;


        dialog = new ProgressDialog(TempleLocationNew.this);
        txt_temple_name = (TextView) findViewById(R.id.txt_temple_name);
        txt_loc_content = (TextView) findViewById(R.id.txt_loc_content);
        txt_current_addr = (EditText) findViewById(R.id.editWorkLocation);
        txt_temp_addr = (TextView) findViewById(R.id.txt_to_address);
        img_back = (ImageView) findViewById(R.id.back);
        img_home = (ImageView) findViewById(R.id.home);
        img_lang = (ImageView) findViewById(R.id.language);
        img_search = (ImageView) findViewById(R.id.search);
        img_gps = (ImageView) findViewById(R.id.img_gps);
        txt_loc_title = (TextView) findViewById(R.id.txt_loc_title);
        rl_content = (RelativeLayout) findViewById(R.id.content_lay);
        menu_layout = (LinearLayout) findViewById(R.id.menu_layout);
        img_menu = (ImageView) findViewById(R.id.menu);
        img_english = (ImageView) findViewById(R.id.img_english);
        img_tamil = (ImageView) findViewById(R.id.img_tamil);
        ll_english = (LinearLayout) findViewById(R.id.llay_english);
        ll_tamil = (LinearLayout) findViewById(R.id.llay_tamil);
        rl_choose_lang = (RelativeLayout) findViewById(R.id.rl_choose_lang);
        lay_about = (LinearLayout) findViewById(R.id.lay_about);
        lay_specialities = (LinearLayout) findViewById(R.id.lay_specialities);
        lay_timing = (LinearLayout) findViewById(R.id.lay_timing);
        lay_history = (LinearLayout) findViewById(R.id.lay_history);
        lay_administration = (LinearLayout) findViewById(R.id.lay_administration);
        lay_gallery = (LinearLayout) findViewById(R.id.lay_gallery);
        lay_festival = (LinearLayout) findViewById(R.id.lay_festival);
        lay_art = (LinearLayout) findViewById(R.id.lay_art);
        lay_location = (LinearLayout) findViewById(R.id.lay_location);
        txt_abt_temple = (TextView) findViewById(R.id.txt_about_temple);
        txt_nearby = (TextView) findViewById(R.id.txt_nearby);
        txt_dharshan = (TextView) findViewById(R.id.txt_dharshan_timing);
        txt_history = (TextView) findViewById(R.id.txt_history);
        txt_admin = (TextView) findViewById(R.id.txt_admin);
        txt_gallery = (TextView) findViewById(R.id.txt_gallery);
        txt_loc = (TextView) findViewById(R.id.txt_location);
        txt_festival = (TextView) findViewById(R.id.txt_festivals);
        txt_art = (TextView) findViewById(R.id.txt_arts);
        txt_special = (TextView) findViewById(R.id.txt_special);

        img_driving = (LinearLayout) findViewById(R.id.driving_route);
        img_transit = (LinearLayout) findViewById(R.id.transit_route);
        img_walking = (LinearLayout) findViewById(R.id.walk_route);
        img_cycling = (LinearLayout) findViewById(R.id.cycle_route);

        img_driving_route = (ImageView) findViewById(R.id.img_driving_route);
        img_transit_route = (ImageView) findViewById(R.id.img_transit_route);
        img_walking_route = (ImageView) findViewById(R.id.img_walk_route);
        img_cycle_route = (ImageView) findViewById(R.id.img_cycle_route);


        str_temp_id = getIntent().getStringExtra("temple_id");
        str_lat = getIntent().getStringExtra("temp_lat");
        str_long = getIntent().getStringExtra("temp_long");
        str_content = getIntent().getStringExtra("temp_content");
        str_temp_name = getIntent().getStringExtra("temple_name");
        str_temp_address = getIntent().getStringExtra("temp_address");
        str_type = getIntent().getStringExtra("temp_from");


        str_lang = String.valueOf(GeneralData.str_lang);

        if (str_temp_id == null && str_type == null && str_temp_name == null && str_lat == null && str_long == null && str_content == null && str_temp_address == null) {
            str_temp_id = SplashActivity.preferences.getString("temple_id_det", null);
            str_type = SplashActivity.preferences.getString("temple_from_det", null);
            str_temp_name = SplashActivity.preferences.getString("temple_name_det", null);
            str_lat = SplashActivity.preferences.getString("temple_lat_det", null);
            str_long = SplashActivity.preferences.getString("temple_long_det", null);
            str_content = SplashActivity.preferences.getString("temple_content_det", null);
            str_temp_address = SplashActivity.preferences.getString("temple_addr_det", null);


        }

        // load the animation
        slideUp = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.slide_up);
        slideDown = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.slide_down);

        // set animation listener
        slideUp.setAnimationListener(this);
        slideDown.setAnimationListener(this);


        if (str_temp_name == null) {
            str_temp_name = SplashActivity.preferences.getString("temple_name", null);
        } else {
            if (str_temp_name.length() > 10) {
                txt_temple_name.setText(str_temp_name);
                txt_temple_name.setTextSize(13);
            }
        }


        if (str_temp_id == null) {
            str_temp_id = SplashActivity.preferences.getString("sub_temple_id", null);

        }

        // txt_temple_name.setText(str_temp_name);
        s_lat = gD.pro_searchLatitutde;
        s_long = gD.pro_searchLongitude;
        txt_loc_content.setText(str_content);
        if (str_lang.equalsIgnoreCase("ta")) {
            img_english.setBackgroundResource(R.drawable.white_dot);
            img_tamil.setBackgroundResource(R.drawable.orange_dot);
            txt_loc_title.setText("இடவரலாறு");
            txt_current_addr.setHint("இருப்பிடம்");
            txt_current_addr.setTextSize(11);
            txt_temp_addr.setHint("சேருமிடம்");
            txt_temp_addr.setTextSize(11);
        } else if (str_lang.equalsIgnoreCase("en")) {
            img_english.setBackgroundResource(R.drawable.orange_dot);
            img_tamil.setBackgroundResource(R.drawable.white_dot);
            txt_loc_title.setText("about this location");
            txt_current_addr.setHint("Starting Point");
            txt_temp_addr.setHint("Destination Point");
        }

        rl_content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rl_choose_lang.setVisibility(View.GONE);
            }
        });

        img_search_route.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txt_current_addr.performClick();
            }
        });
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentLAtLong();
                SharedPreferences.Editor edit = HomePage.prefs.edit();
                edit.putString("from", str_type);
                edit.commit();
                finish();
            }
        });


        lay_about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                str_type = "about_temple";
                Intent i = new Intent(TempleLocationNew.this, TempleDetails.class);
                i.putExtra("temp_from", str_type);
                i.putExtra("temple_id", str_temp_id);
                i.putExtra("temple_name", str_temp_name);
                startActivity(i);

                //   GetTempleDetails get_Provider_details = new GetTempleDetails(context, str_temp_id, str_type);
                //    get_Provider_details.execute();
                finish();
                n_count = 0;
                menu_layout.setVisibility(View.GONE);

            }
        });
        lay_specialities.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.i("HI", "I came........");
                Log.i("HI", "str_temp_id......" + str_temp_id);

                str_type = "specialities";
                Intent i = new Intent(TempleLocationNew.this, TempleDetails.class);
                i.putExtra("temp_from", str_type);
                i.putExtra("temple_id", str_temp_id);
                i.putExtra("temple_name", str_temp_name);
                startActivity(i);

                //    GetTempleDetails get_Provider_details = new GetTempleDetails(context, str_temp_id, str_type);
                //   get_Provider_details.execute();

                finish();

                n_count = 0;
                menu_layout.setVisibility(View.GONE);

            }
        });
        lay_timing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                str_type = "dharsan_timing";
                Intent i = new Intent(TempleLocationNew.this, TempleDetails.class);
                i.putExtra("temp_from", str_type);
                i.putExtra("temple_id", str_temp_id);
                i.putExtra("temple_name", str_temp_name);
                startActivity(i);

                //   GetTempleDetails get_Provider_details = new GetTempleDetails(context, str_temp_id, str_type);
                //    get_Provider_details.execute();
                finish();
                n_count = 0;
                menu_layout.setVisibility(View.GONE);

            }
        });
        lay_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.i("HI", "I came........");
                Log.i("HI", "str_temp_id......" + str_temp_id);

                str_type = "history";
                Intent i = new Intent(TempleLocationNew.this, TempleDetails.class);
                i.putExtra("temp_from", str_type);
                i.putExtra("temple_id", str_temp_id);
                i.putExtra("temple_name", str_temp_name);
                startActivity(i);

                //    GetTempleDetails get_Provider_details = new GetTempleDetails(context, str_temp_id, str_type);
                //   get_Provider_details.execute();

                finish();

                n_count = 0;
                menu_layout.setVisibility(View.GONE);

            }
        });
        lay_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("HI", "I came........");
                Log.i("HI", "str_temp_id......" + str_temp_id);

                str_type = "administration";
                Intent i = new Intent(TempleLocationNew.this, GalleryVideo.class);
                i.putExtra("temp_from", str_type);
                i.putExtra("temple_id", str_temp_id);
                i.putExtra("temple_name", str_temp_name);
                startActivity(i);

                //    GetTempleDetails get_Provider_details = new GetTempleDetails(context, str_temp_id, str_type);
                //   get_Provider_details.execute();

                finish();

                n_count = 0;
                menu_layout.setVisibility(View.GONE);

            }
        });
        lay_administration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.i("HI", "I came........");
                Log.i("HI", "str_temp_id......" + str_temp_id);

                str_type = "administration";
                Intent i = new Intent(TempleLocationNew.this, TempleDetails.class);
                i.putExtra("temp_from", str_type);
                i.putExtra("temple_id", str_temp_id);
                i.putExtra("temple_name", str_temp_name);
                startActivity(i);

                //    GetTempleDetails get_Provider_details = new GetTempleDetails(context, str_temp_id, str_type);
                //   get_Provider_details.execute();

                finish();

                n_count = 0;
                menu_layout.setVisibility(View.GONE);

            }
        });
        lay_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.i("HI", "I came........");
                Log.i("HI", "str_temp_id......" + str_temp_id);

                str_type = "location";
                Intent i = new Intent(TempleLocationNew.this, TempleDetails.class);
                i.putExtra("temp_from", str_type);
                i.putExtra("temple_id", str_temp_id);
                i.putExtra("temple_name", str_temp_name);
                startActivity(i);

                //    GetTempleDetails get_Provider_details = new GetTempleDetails(context, str_temp_id, str_type);
                //   get_Provider_details.execute();

                finish();

                n_count = 0;
                menu_layout.setVisibility(View.GONE);

            }
        });
        lay_festival.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.i("HI", "I came........");
                Log.i("HI", "str_temp_id......" + str_temp_id);

                str_type = "festivals";
                Intent i = new Intent(TempleLocationNew.this, TempleDetails.class);
                i.putExtra("temp_from", str_type);
                i.putExtra("temple_id", str_temp_id);
                i.putExtra("temple_name", str_temp_name);
                startActivity(i);

                //    GetTempleDetails get_Provider_details = new GetTempleDetails(context, str_temp_id, str_type);
                //   get_Provider_details.execute();

                finish();

                n_count = 0;
                menu_layout.setVisibility(View.GONE);

            }
        });
        lay_art.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.i("HI", "I came........");
                Log.i("HI", "str_temp_id......" + str_temp_id);

                str_type = "art_inspiration";
                Intent i = new Intent(TempleLocationNew.this, TempleDetails.class);
                i.putExtra("temp_from", str_type);
                i.putExtra("temple_id", str_temp_id);
                i.putExtra("temple_name", str_temp_name);
                startActivity(i);

                //    GetTempleDetails get_Provider_details = new GetTempleDetails(context, str_temp_id, str_type);
                //   get_Provider_details.execute();

                finish();

                n_count = 0;
                menu_layout.setVisibility(View.GONE);

            }
        });

        img_lang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                menu_layout.setVisibility(View.GONE);
                if (n_count == 0) {
                    rl_choose_lang.setVisibility(View.VISIBLE);
                    n_count = 1;
                } else {
                    n_count = 0;
                    rl_choose_lang.setVisibility(View.GONE);
                }

            }
        });

        img_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TempleLocationNew.this, HomePage.class));
                finish();
                finishAffinity();
            }
        });

        img_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rl_choose_lang.setVisibility(View.GONE);

                if (n_count == 0) {
                    menu_layout.setVisibility(View.VISIBLE);
                    n_count = 1;
                } else {
                    menu_layout.setVisibility(View.GONE);
                    n_count = 0;
                }
            }
        });


        img_driving.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                str_slected_route = "driving";
                img_driving_route.setBackgroundResource(R.drawable.car_white);
                img_transit_route.setBackgroundResource(R.drawable.bus_grey);
                img_walking_route.setBackgroundResource(R.drawable.walk_grey);
                img_cycle_route.setBackgroundResource(R.drawable.cycle_grey_new);
                ll_route.setVisibility(View.GONE);

                LatLng origin = TutorialsPoint;
                LatLng dest = TutorialsPoints;
                Log.i("JJ", "TutorialsPointsdes.." + TutorialsPoints);
                Log.i("JJ", "TutorialsPointorigii.." + TutorialsPoint);

                String url = getDirectionsUrl(origin, dest);
                DownloadTask downloadTask = new DownloadTask(origin, dest);
                downloadTask.execute(url);
            }
        });

        img_transit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                str_slected_route = "transit";
                img_driving_route.setBackgroundResource(R.drawable.car_grey);
                img_transit_route.setBackgroundResource(R.drawable.bus_white);
                img_walking_route.setBackgroundResource(R.drawable.walk_grey);
                img_cycle_route.setBackgroundResource(R.drawable.cycle_grey_new);
                ll_route.setVisibility(View.GONE);
                LatLng origin = TutorialsPoint;
                LatLng dest = TutorialsPoints;
                Log.i("JJ", "TutorialsPointsdes.." + TutorialsPoints);
                Log.i("JJ", "TutorialsPointorigii.." + TutorialsPoint);

                String url = getDirectionsUrl(origin, dest);
                DownloadTask downloadTask = new DownloadTask(origin, dest);
                downloadTask.execute(url);
            }
        });

        img_walking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                str_slected_route = "walking";
                img_driving_route.setBackgroundResource(R.drawable.car_grey);
                img_transit_route.setBackgroundResource(R.drawable.bus_grey);
                img_walking_route.setBackgroundResource(R.drawable.walk_white);
                img_cycle_route.setBackgroundResource(R.drawable.cycle_grey_new);
                ll_route.setVisibility(View.GONE);
                LatLng origin = TutorialsPoint;
                LatLng dest = TutorialsPoints;
                Log.i("JJ", "TutorialsPointsdes.." + TutorialsPoints);
                Log.i("JJ", "TutorialsPointorigii.." + TutorialsPoint);

                String url = getDirectionsUrl(origin, dest);
                DownloadTask downloadTask = new DownloadTask(origin, dest);
                downloadTask.execute(url);
            }
        });
        img_cycling.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                str_slected_route = "cycling";
                img_driving_route.setBackgroundResource(R.drawable.car_grey);
                img_transit_route.setBackgroundResource(R.drawable.bus_grey);
                img_walking_route.setBackgroundResource(R.drawable.walk_grey);
                img_cycle_route.setBackgroundResource(R.drawable.cycle_white);
                ll_route.setVisibility(View.GONE);

                LatLng origin = TutorialsPoint;
                LatLng dest = TutorialsPoints;
                Log.i("JJ", "TutorialsPointsdes.." + TutorialsPoints);
                Log.i("JJ", "TutorialsPointorigii.." + TutorialsPoint);

                String url = getDirectionsUrl(origin, dest);
                DownloadTask downloadTask = new DownloadTask(origin, dest);
                downloadTask.execute(url);
            }
        });


        img_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                rl_choose_lang.setVisibility(View.GONE);
                final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setCancelable(true);
                itemView1 = LayoutInflater.from(context)
                        .inflate(R.layout.search_popup, null);
                final AlertDialog altDialog = builder.create();
                altDialog.setView(itemView1);
                altDialog.show();
                spin_area = (Spinner) itemView1.findViewById(R.id.spinner_area);
                spin_state = (Spinner) itemView1.findViewById(R.id.spinner_state);
                spin_city = (Spinner) itemView1.findViewById(R.id.spinner_city);
                spin_festival = (Spinner) itemView1.findViewById(R.id.spinner_festival);
                spin_deity = (Spinner) itemView1.findViewById(R.id.spinner_diety);
                btn_search = (Button) itemView1.findViewById(R.id.btn_search);
                txt_search_title = (TextView) itemView1.findViewById(R.id.txt_search_title);


                if (str_lang.equalsIgnoreCase("ta")) {
                    img_english.setBackgroundResource(R.drawable.white_dot);
                    img_tamil.setBackgroundResource(R.drawable.orange_dot);
                    txt_search_title.setText("கோவில் தேடுக");
                    btn_search.setText("தேடுக");
                    loadSpinnerBasedLang(1);
                } else if (str_lang.equalsIgnoreCase("en")) {
                    img_english.setBackgroundResource(R.drawable.orange_dot);
                    img_tamil.setBackgroundResource(R.drawable.white_dot);
                    txt_search_title.setText("Search Your Temples");
                    btn_search.setText("Search");
                    loadSpinnerBasedLang(0);
                }
                Log.i("BG", "kkkkkkkkkkk :2");
                GetSearchResults getSearchResults = new GetSearchResults(context, "state", spin_state, str_lang);
                getSearchResults.execute();
                GetSearchResults getSearchResults2 = new GetSearchResults(context, "festival", spin_festival, str_lang);
                getSearchResults2.execute();
                GetSearchResults getSearchResults1 = new GetSearchResults(context, "diety", spin_deity, str_lang);
                getSearchResults1.execute();
                Log.i("BG", "kkkkkkkkkkk :3 ");
                btn_search.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (spin_state.getSelectedItemPosition() > 0 && spin_city.getSelectedItemPosition() == 0) {

                            Log.i("VB", "1");
                            Log.i("str_lang", str_lang);
                            GetSearchDetails getSearchResults = new GetSearchDetails(context, "search_temple", "nscfd", str_lang);
                            getSearchResults.execute();
                            altDialog.dismiss();
                        } else if (spin_state.getSelectedItemPosition() > 0 && spin_city.getSelectedItemPosition() > 0 && spin_area.getSelectedItemPosition() == 0 && spin_festival.getSelectedItemPosition() == 0 && spin_deity.getSelectedItemPosition() == 0) {
                            Log.i("VB", "2");

                            GetSearchDetails getSearchResults = new GetSearchDetails(context, "search_temple", "ncfd", str_lang);
                            getSearchResults.execute();
                            altDialog.dismiss();
                        } else if (spin_state.getSelectedItemPosition() > 0 && spin_city.getSelectedItemPosition() > 0 && spin_area.getSelectedItemPosition() > 0 && spin_festival.getSelectedItemPosition() == 0 && spin_deity.getSelectedItemPosition() == 0) {
                            Log.i("VB", "3");
                            GetSearchDetails getSearchResults = new GetSearchDetails(context, "search_temple", "nfd", str_lang);
                            getSearchResults.execute();
                            altDialog.dismiss();
                        } else if (spin_state.getSelectedItemPosition() > 0 && spin_city.getSelectedItemPosition() > 0 && spin_area.getSelectedItemPosition() > 0 && spin_festival.getSelectedItemPosition() > 0 && spin_deity.getSelectedItemPosition() == 0) {
                            Log.i("VB", "4");
                            GetSearchDetails getSearchResults = new GetSearchDetails(context, "search_temple", "nd", str_lang);
                            getSearchResults.execute();
                            altDialog.dismiss();
                        } else if (spin_state.getSelectedItemPosition() > 0 && spin_city.getSelectedItemPosition() > 0 && spin_area.getSelectedItemPosition() > 0 && spin_festival.getSelectedItemPosition() > 0 && spin_deity.getSelectedItemPosition() > 0) {
                            Log.i("VB", "5");
                            GetSearchDetails getSearchResults = new GetSearchDetails(context, "search_temple", "n", str_lang);
                            getSearchResults.execute();
                            altDialog.dismiss();
                        }

                        //For festival and diety
                        else if (spin_state.getSelectedItemPosition() == 0 && spin_city.getSelectedItemPosition() == 0 && spin_area.getSelectedItemPosition() == 0 && spin_festival.getSelectedItemPosition() > 0 && spin_deity.getSelectedItemPosition() == 0) {
                            Log.i("VB", "6");
                            GetSearchDetails getSearchResults = new GetSearchDetails(context, "search_temple", "f", str_lang);
                            getSearchResults.execute();
                            altDialog.dismiss();
                        } else if (spin_state.getSelectedItemPosition() == 0 && spin_city.getSelectedItemPosition() == 0 && spin_area.getSelectedItemPosition() == 0 && spin_festival.getSelectedItemPosition() == 0 && spin_deity.getSelectedItemPosition() > 0) {
                            Log.i("VB", "7");
                            GetSearchDetails getSearchResults = new GetSearchDetails(context, "search_temple", "d", str_lang);
                            getSearchResults.execute();
                            altDialog.dismiss();
                        } else {
                           /* GetSearchDetails getSearchResults = new GetSearchDetails(context, "search_temple", "empty_search", str_lang);
                            getSearchResults.execute();*/
                            if (str_lang.equalsIgnoreCase("ta")) {
                                Toast.makeText(TempleLocationNew.this, "குறைந்தது ஒரு துறை தேர்வு செய்க", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(TempleLocationNew.this, "Select atleast one field", Toast.LENGTH_SHORT).show();
                            }
                            Log.i("VB", "8");
                        }


                    }
                });


                spin_city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        if (spin_city.getSelectedItemPosition() != 0) {

                            TempleListBean mSelected = (TempleListBean) parent.getItemAtPosition(position);
                            strCityId = mSelected.getTemple_id();

                            GetSearchResults getSearchResults = new GetSearchResults(context, "area", spin_area, strStateId, strCityId, str_lang);
                            getSearchResults.execute();
                        }

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

                spin_state.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        if (spin_state.getSelectedItemPosition() != 0) {

                            TempleListBean mSelected = (TempleListBean) parent.getItemAtPosition(position);
                            strStateId = mSelected.getTemple_id();


                            GetSearchResults getSearchResults = new GetSearchResults(context, "city", spin_city, strStateId, str_lang);
                            getSearchResults.execute();


                        }


                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

                spin_area.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        if (spin_area.getSelectedItemPosition() != 0) {

                            TempleListBean mSelected = (TempleListBean) parent.getItemAtPosition(position);
                            strAreaId = mSelected.getTemple_id();


                        }


                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });


                spin_festival.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        if (spin_festival.getSelectedItemPosition() != 0) {

                            TempleListBean mSelected = (TempleListBean) parent.getItemAtPosition(position);
                            strFestivalId = mSelected.getTemple_id();

                        }


                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
                spin_deity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        if (spin_deity.getSelectedItemPosition() != 0) {

                            TempleListBean mSelected = (TempleListBean) parent.getItemAtPosition(position);
                            strDietyId = mSelected.getTemple_id();


                        }


                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });


            }
        });

        if (str_lang.equalsIgnoreCase("en")) {
            img_english.setBackgroundResource(R.drawable.orange_dot);
            img_tamil.setBackgroundResource(R.drawable.white_dot);
            txt_abt_temple.setText("ABOUT TEMPLE");
            txt_dharshan.setText("DHARSHAN TIMING");
            txt_history.setText("HISTORY");
            txt_admin.setText("ADMINISTRATION");
            txt_gallery.setText("GALLERY");
            txt_loc.setText("LOCATION");
            txt_festival.setText("FESTIVALS");
            txt_art.setText("Arts and Inscriptions");
            txt_special.setText("SPECIALITIES");


        } else if (str_lang.equalsIgnoreCase("ta")) {
            img_english.setBackgroundResource(R.drawable.white_dot);
            img_tamil.setBackgroundResource(R.drawable.orange_dot);
            txt_abt_temple.setText("கோவில் பற்றி");
            txt_dharshan.setText("தரிசன நேரம்");
            txt_history.setText("வரலாறு");
            txt_admin.setText("நிர்வாகம்");
            txt_gallery.setText("காட்சியகம்");
            txt_loc.setText("இடம்");
            txt_festival.setText("திருவிழாக்கள்");
            txt_art.setText("கலை மற்றும் கல்வெட்டுகள்");
            txt_special.setText("சிறப்பம்சங்கள்");
            txt_special.setTextSize(9);


        }
        ll_english.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                img_english.setBackgroundResource(R.drawable.orange_dot);
                img_tamil.setBackgroundResource(R.drawable.white_dot);
                GetLocation getLocation = new GetLocation(context, str_temp_id, "en");
                getLocation.execute();

                txt_loc_title.setText("about this location");
                txt_current_addr.setHint("Starting Point");
                txt_temp_addr.setHint("Destination Point");

                txt_abt_temple.setText("ABOUT TEMPLE");
                txt_dharshan.setText("DHARSHAN TIMING");
                txt_history.setText("HISTORY");
                txt_admin.setText("ADMINISTRATION");
                txt_gallery.setText("GALLERY");
                txt_loc.setText("LOCATION");
                txt_festival.setText("FESTIVALS");
                txt_art.setText("Arts and Inscriptions");
                txt_special.setText("SPECIALITIES");
                GeneralData.str_lang = "en";
                str_lang = String.valueOf(GeneralData.str_lang);

                rl_choose_lang.setVisibility(View.GONE);

                itemView1 = LayoutInflater.from(context)
                        .inflate(R.layout.search_popup, null);

                spin_area = (Spinner) itemView1.findViewById(R.id.spinner_area);
                spin_state = (Spinner) itemView1.findViewById(R.id.spinner_state);
                spin_city = (Spinner) itemView1.findViewById(R.id.spinner_city);
                spin_festival = (Spinner) itemView1.findViewById(R.id.spinner_festival);
                spin_deity = (Spinner) itemView1.findViewById(R.id.spinner_diety);

                loadSpinnerBasedLang(0);

            }
        });
        ll_tamil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                img_english.setBackgroundResource(R.drawable.white_dot);
                img_tamil.setBackgroundResource(R.drawable.orange_dot);
                GetLocation getLocation = new GetLocation(context, str_temp_id, "ta");
                getLocation.execute();
                txt_loc_title.setText("இந்த இடம் பற்றி");
                txt_current_addr.setHint("புறப்படும் இடம் ");
                txt_current_addr.setTextSize(11);
                txt_temp_addr.setHint("சேரும் இடம்");
                txt_temp_addr.setTextSize(11);

                txt_abt_temple.setText("கோவில் பற்றி");
                txt_dharshan.setText("தரிசன நேரம்");
                txt_history.setText("வரலாறு");
                txt_admin.setText("நிர்வாகம்");
                txt_gallery.setText("காட்சியகம்");
                txt_loc.setText("இடம்");
                txt_festival.setText("திருவிழாக்கள்");
                txt_art.setText("கலை மற்றும் கல்வெட்டுகள்");
                txt_special.setText("சிறப்பம்சங்கள்");
                txt_special.setTextSize(9);
                GeneralData.str_lang = "ta";
                str_lang = String.valueOf(GeneralData.str_lang);

                rl_choose_lang.setVisibility(View.GONE);

                itemView1 = LayoutInflater.from(context)
                        .inflate(R.layout.search_popup, null);

                spin_area = (Spinner) itemView1.findViewById(R.id.spinner_area);
                spin_state = (Spinner) itemView1.findViewById(R.id.spinner_state);
                spin_city = (Spinner) itemView1.findViewById(R.id.spinner_city);
                spin_festival = (Spinner) itemView1.findViewById(R.id.spinner_festival);
                spin_deity = (Spinner) itemView1.findViewById(R.id.spinner_diety);

                loadSpinnerBasedLang(1);
            }
        });
        locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
        isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        sharedpreferences = getSharedPreferences("myprefer", Context.MODE_PRIVATE);

        if (!isGPSEnabled && !isNetworkEnabled) {
            //showGPSDisabledAlertToUser();
            //Toast.makeText(getApplicationContext(), "No provider found!", Toast.LENGTH_SHORT).show();
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setMessage("GPS is disabled in your device. Would you like to enable it?").setCancelable(false).setPositiveButton("Goto Settings Page To Enable GPS", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    Intent callGPSSettingIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(callGPSSettingIntent);
                    //to start the service
                    startService(new Intent(TempleLocationNew.this, GPSTracker_Provider.class));
                    sharedpreferences = getSharedPreferences("myprefer", Context.MODE_PRIVATE);
                }


            });
            alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                }
            });
            AlertDialog alert = alertDialogBuilder.create();
            alert.show();
            Dialog alertDialog = alertDialogBuilder.create();
            alertDialog.setCancelable(false);
            alertDialog.show();
        }


        //to start the service
        startService(new Intent(TempleLocationNew.this, GPSTracker_Provider.class));

        txt_temp_addr.setText( Html.fromHtml("<p>" + str_temp_address + "</p>"));


        ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMapAsync(this);


        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        img_gps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString("Lat", strInitialLat);
                editor.putString("Long", strInitialLang);
                editor.commit();*/
                //currentLAtLong();
                Log.i("ND", "hi i am clicked........");
                Log.i("ND", "strInitialLat : " + strInitialLat);
                Log.i("ND", "strInitialLang : " + strInitialLang);
                getCurrentLocation("myLoc");
                txt_current_addr.setText("");
            }
        });

        getCurrentLocation("onload");


        img_route.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ncount == 0) {
                    ll_route.setVisibility(View.VISIBLE);
                    ll_route.startAnimation(slideDown);
                    ncount = 1;
                } else {
                    ll_route.startAnimation(slideUp);
                    ll_route.setVisibility(View.GONE);
                    ncount = 0;
                }

            }
        });
        slidingDrawer.setOnDrawerOpenListener(new SlidingDrawer.OnDrawerOpenListener() {
            @Override
            public void onDrawerOpened() {
                ll_route.setVisibility(View.GONE);
                slider_arrow.setBackgroundResource(R.drawable.arrow_down);
            }
        });
        slidingDrawer.setOnDrawerCloseListener(new SlidingDrawer.OnDrawerCloseListener() {
            @Override
            public void onDrawerClosed() {
                ll_route.setVisibility(View.GONE);
                slider_arrow.setBackgroundResource(R.drawable.arrow_up);
            }
        });
    }

    public void currentLAtLong() {
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("Lat", strInitialLat);
        editor.putString("Long", strInitialLang);
        editor.commit();
    }

    public void getCurrentLocation(final String strVal) {
        try {
            Handler h = new Handler();
            h.postDelayed(new Runnable() {
                @Override
                public void run() {


                    if ((sharedpreferences.getString("Lat", null) != null) && (sharedpreferences.getString("Long", null) != null)) {

                        if (gD.googleMapGeneral != null) {
                            gD.googleMapGeneral.clear();
                           /* gD.googleMapGeneral = ((MapFragment) getFragmentManager().
                                    findFragmentById(R.id.map)).getMap();*/

                            ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMapAsync(TempleLocationNew.this);
                        }


                        Log.e("Lat->", (sharedpreferences.getString("Lat", null)));
                        Log.e("Long->", (sharedpreferences.getString("Long", null)));
                        if (strVal.equalsIgnoreCase("onload")) {
                            strInitialLat = sharedpreferences.getString("Lat", null);
                            strInitialLang = sharedpreferences.getString("Long", null);
                        }

                        try {
                            Geocoder geocoder;
                            List<Address> addressList = null;

                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            InputMethodSubtype ims = imm.getCurrentInputMethodSubtype();
                            String localeString = ims.getLocale();
                            Locale locale = new Locale(localeString);
                            String currentLanguage = locale.getDisplayLanguage();

                            Log.i("NDlANG", "lANGEAG : " + currentLanguage);


                            geocoder = new Geocoder(TempleLocationNew.this, new Locale(currentLanguage));

                            addressList = geocoder.getFromLocation(Double.parseDouble(sharedpreferences.getString("Lat", null)), Double.parseDouble(sharedpreferences.getString("Long", null)), 1);
                            if (addressList != null && addressList.size() > 0) {
                                Address address = addressList.get(0);
                                StringBuilder sb = new StringBuilder();
                                for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
                                    sb.append(address.getAddressLine(i)).append(",");
                                }
                               /* sb.append(address.getLocality()).append("\n");
                                sb.append(address.getPostalCode()).append("\n");
                                sb.append(address.getCountryName());
                                result = sb.toString();*/
                                sb.append(address.getLocality()).append(",");
                                sb.append(address.getPostalCode()).append(",");
                                sb.append(address.getCountryName());
                                result = sb.toString();
                                txt_current_addr.setText(result);
                                Log.e("Address-->", result);

                                gD.googleMapGeneral.setInfoWindowAdapter(new MyInfoWindowAdapter());

                                Log.i("JJ", "latlong.." + TutorialsPoint);
                                TutorialsPoint = new LatLng(Double.parseDouble(sharedpreferences.getString("Lat", null)), Double.parseDouble(sharedpreferences.getString("Long", null)));


                                TP = gD.googleMapGeneral.addMarker(new MarkerOptions().position(TutorialsPoint).snippet(result).icon(BitmapDescriptorFactory.fromResource(R.drawable.current_marker)));


                                CameraPosition cameraPosition = new CameraPosition.Builder()
                                        .target(TutorialsPoint)      // Sets the center of the map to Mountain View
                                        .zoom(10)                   // Sets the zoom
                                        // Sets the orientation of the camera to east
                                        .tilt(90)     // Sets the tilt of the camera to 30 degrees
                                        .build();                   // Creates a CameraPosition from the builder
                                gD.googleMapGeneral.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));


                                gD.googleMapGeneral.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                                    @Override
                                    public boolean onMarkerClick(Marker marker) {
                                        if (marker.equals(TP)) {


                                            CameraPosition cameraPosition = new CameraPosition.Builder()
                                                    .target(TutorialsPoint)      // Sets the center of the map to Mountain View
                                                    .zoom(10)                   // Sets the zoom
                                                    // Sets the orientation of the camera to east
                                                    .tilt(90)     // Sets the tilt of the camera to 30 degrees
                                                    .build();                   // Creates a CameraPosition from the builder
                                            gD.googleMapGeneral.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));


                                        }
                                        return true;

                                    }
                                });

                                TutorialsPoints = new LatLng(Double.parseDouble(str_lat), Double.parseDouble(str_long));

                                gD.googleMapGeneral.addMarker(new MarkerOptions().position(TutorialsPoints).title("lat-" + Double.parseDouble(str_lat) + " Long-" + Double.parseDouble(str_long)).icon(BitmapDescriptorFactory.defaultMarker((BitmapDescriptorFactory.HUE_RED))));


                                LatLng origin = TutorialsPoint;
                                LatLng dest = TutorialsPoints;

                                Log.i("JJ", "TutorialsPointsdes.." + TutorialsPoints);
                                Log.i("JJ", "TutorialsPointorigii.." + TutorialsPoint);

                                String url = getDirectionsUrl(origin, dest);
                                DownloadTask downloadTask = new DownloadTask(origin, dest);
                                downloadTask.execute(url);


                            } else {
                                Log.i("GF", "ND : i am come to here........");
                            }

                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                }
            }, 1000);
        } catch (Exception e) {
            Log.e("KK", e.toString());

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService(new Intent(TempleLocationNew.this, GPSTracker_Provider.class));
    }


    @Override
    public void onResume() {
        super.onResume();

        if (gD.pro_searchAddress != null && gD.pro_searchLatitutde != null && gD.pro_searchLongitude != null) {
            str_current_address = gD.pro_searchAddress;
            s_lat = gD.pro_searchLatitutde;
            s_long = gD.pro_searchLongitude;
            txt_current_addr.setText(str_current_address);


            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putString("Lat", s_lat);
            editor.putString("Long", s_long);
            editor.commit();

            getCurrentLocation("onload");
            str_lang = GeneralData.str_lang;
           /* if(str_lang.equalsIgnoreCase("en")){
                txt_abt_temple.setText("ABOUT TEMPLE");
                txt_dharshan.setText("DHARSHAN TIMING");
                txt_history.setText("HISTORY");
                txt_admin.setText("ADMINISTRATION");
                txt_gallery.setText("GALLERY");
                txt_loc.setText("LOCATION");
                txt_festival.setText("FESTIVALS");
                txt_art.setText("Arts and Inspirations");
                txt_special.setText("SPECIALITIES");
                txt_nearby.setText("Nearby Visiting Places");

                txt_loc_title.setText("about this location");
                txt_from.setText("From");
                txt_to.setText("To");

            }
            else if(str_lang.equalsIgnoreCase("ta")){
                txt_abt_temple.setText("கோவில் பற்றி");
                txt_dharshan.setText("தரிசனம் நேரம்");
                txt_history.setText("வரலாறு");
                txt_admin.setText("நிர்வாகம்");
                txt_gallery.setText("காட்சியகம்");
                txt_loc.setText("இருப்பிடம்");
                txt_festival.setText("திருவிழாக்கள்");
                txt_art.setText("கலை மற்றும் உத்வேகம்");
                txt_special.setText("சிறப்புத்தன்மைகள்");
                txt_nearby.setText("அருகாமையில் உள்ள இடங்களைப் பற்றி");
                txt_loc_title.setText("இந்த இடம் பற்றி");
                txt_from.setText("புறப்படும் இடம் ");
                txt_from.setTextSize(9);
                txt_to.setText("சேரும் இடம்");
                txt_to.setTextSize(9);


            }*/
        }
    }

    @Override
    public void getSearchLatLon(String str_lat, String str_long) {

    }

    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        } catch (Exception e) {

        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    private String getDirectionsUrl(LatLng origin, LatLng dest) {

        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;

        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;


        // Sensor enabled
        String sensor = "sensor=false";
        String alternative = "alternatives=true";

        // Travelling Mode
        String mode = "mode=driving";

        if (str_slected_route.equalsIgnoreCase("driving")) {
            mode = "mode=driving";
            mMode = 0;
        } else if (str_slected_route.equalsIgnoreCase("transit")) {
            //mode = "mode=transit";
            mode = "mode=driving";
            mMode = 1;
        } else if (str_slected_route.equalsIgnoreCase("walking")) {
            mode = "mode=walking";
            mMode = 2;
        } else if (str_slected_route.equalsIgnoreCase("cycling")) {
            // mode = "mode=bicycling";
            mode = "mode=walking";
            mMode = 3;
        }
       /* if(rbDriving.isChecked()){
            mode = "mode=driving";
            mMode = 0 ;
        }else if(rbBiCycling.isChecked()){
            mode = "mode=bicycling";
            mMode = 1;
        }else if(rbWalking.isChecked()){
            mode = "mode=walking";
            mMode = 2;
        }*/

        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + sensor + "&" + alternative + "&" + mode;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&avoid=tolls|highways|ferries|indoor";
        Log.e("URL", url);

        return url;
    }

    @Override
    public void onMapReady(GoogleMap googMap) {
        googleMap = googMap;


        markerPoints = new ArrayList<LatLng>();
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        googleMap.getUiSettings().setCompassEnabled(true);

        googleMap.getUiSettings().setRotateGesturesEnabled(true);
        googleMap.getUiSettings().setScrollGesturesEnabled(true);
        googleMap.getUiSettings().setTiltGesturesEnabled(true);
        googleMap.getUiSettings().setZoomGesturesEnabled(true);
        googleMap.getUiSettings().setAllGesturesEnabled(true);
        googleMap.getUiSettings().setMapToolbarEnabled(true);
        googleMap.setPadding(20, 20, 20, 150);


        gD.googleMapGeneral = googleMap;
        //displaycurrentlocation(latt, lng);


        Log.i("N-lat", sharedpreferences.getString("Lat", null));
        Log.i("N-long", sharedpreferences.getString("Long", null));
        TutorialsPoint = new LatLng(Double.parseDouble(sharedpreferences.getString("Lat", null)), Double.parseDouble(sharedpreferences.getString("Long", null)));


        //  = new LatLng(11.909494041919183, 79.80592131614685);
        TutorialsPoints = new LatLng(Double.parseDouble(str_lat), Double.parseDouble(str_long));

        // TutorialsPoints = new LatLng(11.909494041919183, 79.80592131614685);
        Marker mark = gD.googleMapGeneral.addMarker(new MarkerOptions().position(TutorialsPoints).title("lat-" + Double.parseDouble(str_lat) + " Long-" + Double.parseDouble(str_long)).icon(BitmapDescriptorFactory.defaultMarker((BitmapDescriptorFactory.HUE_RED))));


        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(TutorialsPoints)      // Sets the center of the map to Mountain View
                .zoom(10)                   // Sets the zoom// Sets the orientation of the camera to east// Sets the tilt of the camera to 30 degrees
                .build();                   // Creates a CameraPosition from the builder
        gD.googleMapGeneral.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

    }

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {

    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }
//message in andorid

    class MyInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {
        private final View myContentsView;

        MyInfoWindowAdapter() {
            myContentsView = getLayoutInflater().inflate(R.layout.user_current_loc_addr_alert, null);
        }

        @Override
        public View getInfoContents(Marker marker) {
            TextView tvSnippet = ((TextView) myContentsView.findViewById(R.id.text_address));
            tvSnippet.setText(marker.getSnippet());
            return myContentsView;
        }

        @Override
        public View getInfoWindow(Marker marker) {
            // TODO Auto-generated method stub
            return null;
        }
    }

    // Fetches data from url passed
    private class DownloadTask extends AsyncTask<String, Void, String> {

        LatLng lOrgin, lDest;

        public DownloadTask(LatLng origin, LatLng dest) {
            this.lOrgin = origin;
            this.lDest = dest;
        }

        // Downloading data in non-ui thread
        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service
            String data = "";

            try {
                // Fetching the data from web service
                data = downloadUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        // Executes in UI thread, after the execution of
        // doInBackground()
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            ParserTask parserTask = new ParserTask(lOrgin, lDest);
            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);

        }
    }

    private void drawMarker(LatLng point, String str_address, String temple_id) {
        // Creating an instance of MarkerOptions
        MarkerOptions markerOptions = new MarkerOptions();
        gD.googleMapGeneral.setInfoWindowAdapter(new MyInfoWindowAdapter());
        // Setting latitude and longitude for the marker
        markerOptions.position(point).snippet(str_address).title(temple_id).icon(BitmapDescriptorFactory.defaultMarker((BitmapDescriptorFactory.HUE_RED)));
        // Adding marker on the Google Map

        googleMap.addMarker(markerOptions);
    }

    /**
     * A class to parse the Google Places in JSON format
     */
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {
        LatLng Lorgin, Ldest;

        public ParserTask(LatLng lOrgin, LatLng lDest) {
            this.Lorgin = lOrgin;
            this.Ldest = lDest;

        }

        // Parsing the data in non-ui thread

        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser(context);

                // Starts parsing data
                routes = parser.parse(jObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {


            try {
                if (gD.googleMapGeneral != null) {
                    gD.googleMapGeneral.clear();
                    ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMapAsync(TempleLocationNew.this);
                    googleMap = gD.googleMapGeneral;
                }


                Log.i("GPS", "Size : " + result.size());

                ArrayList<LatLng> points = null;
                PolylineOptions lineOptions = null;
                MarkerOptions markerOptions = new MarkerOptions();
                lineOptions = new PolylineOptions();

                // Traversing through all the routes
              /*  for (int i = 0; i < result.size(); i++) {*/
                points = new ArrayList<LatLng>();

                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(0);

                Log.i("PP", "Path : " + path.toString());

                // Fetching all the points in i-th route
                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);


                    if (j == 0) { // Get distance from the list
                        distance = (String) point.get("distance");
                        continue;
                    } else if (j == 1) { // Get duration from the list
                        duration = (String) point.get("duration");
                        continue;
                    }


                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);


                }

                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points);
                lineOptions.width(7);
                // lineOptions.color(Color.parseColor("#F96300"));

                if (mMode == MODE_DRIVING)
                    lineOptions.color(Color.RED);
                else if (mMode == MODE_BICYCLING)
                    lineOptions.color(Color.CYAN);
                else if (mMode == MODE_WALKING)
                    lineOptions.color(Color.BLUE);
                else if (mMode == MODE_TRANSIT)
                    lineOptions.color(Color.MAGENTA);


                if (result.size() < 1) {
                    gD.showAlertDialog(context, "No Route Available");
                    //Toast.makeText(getBaseContext(), "No Points", Toast.LENGTH_SHORT).show();
                    return;
                }
                //   }

                // Drawing polyline in the Google Map for the i-th route


                gD.googleMapGeneral.setInfoWindowAdapter(new MyInfoWindowAdapter());
                // Setting latitude and longitude for the marker
                markerOptions.position(Lorgin).icon(BitmapDescriptorFactory.fromResource(R.drawable.current_marker));

                MarkerOptions markerOptions1 = new MarkerOptions();

                markerOptions1.position(Ldest).icon(BitmapDescriptorFactory.defaultMarker((BitmapDescriptorFactory.HUE_RED)));


                // Adding marker on the Google Map

                googleMap.addMarker(markerOptions);
                googleMap.addMarker(markerOptions1);


                googleMap.addPolyline(lineOptions);
                Log.e("distance", distance);
                Log.e("duration", duration);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void loadSpinnerBasedLang(int i) {
        ArrayList<TempleListBean> beanArrayS = new ArrayList<TempleListBean>();
        TempleListBean tB = new TempleListBean();
        if (i == 1) {
            tB.setTemple_name("நகரம்");
        } else {
            tB.setTemple_name("Select City");
        }

        beanArrayS.add(tB);

        ArrayList<TempleListBean> beanArrayC = new ArrayList<TempleListBean>();
        TempleListBean tB1 = new TempleListBean();
        if (i == 1) {
            tB1.setTemple_name("பகுதி");
        } else {
            tB1.setTemple_name("Select Area");
        }

        beanArrayC.add(tB1);

        ArrayList<TempleListBean> beanArrayF = new ArrayList<TempleListBean>();
        TempleListBean tB2 = new TempleListBean();

        if (i == 1) {
            tB2.setTemple_name("விழா");
        } else {
            tB2.setTemple_name("Select Festival");
        }
        beanArrayF.add(tB2);

        ArrayList<TempleListBean> beanArrayD = new ArrayList<TempleListBean>();
        TempleListBean tB3 = new TempleListBean();
        if (i == 1) {
            tB3.setTemple_name("தெய்வம்");
        } else {
            tB3.setTemple_name("Select Deity");
        }

        beanArrayD.add(tB3);

        // spin_country.setPrompt("Select Country");
        spin_city.setAdapter(new SpinnerAdapter(context, R.id.spin_text, beanArrayS));
        spin_area.setAdapter(new SpinnerAdapter(context, R.id.spin_text, beanArrayC));
        spin_festival.setAdapter(new SpinnerAdapter(context, R.id.spin_text, beanArrayF));
        spin_deity.setAdapter(new SpinnerAdapter(context, R.id.spin_text, beanArrayD));
    }

    class GetSearchResults extends AsyncTask {

        String str_lang = null;
        String sResponse = null;
        Context cont;
        String str_typ;
        String nCityId, nStateId;
        Spinner spinCommon;

        public GetSearchResults(Context context, String strType, Spinner spin, String str_lang) {

            this.cont = context;
            this.str_typ = strType;
            this.spinCommon = spin;
            this.str_lang = str_lang;


        }

        public GetSearchResults(Context context, String strType) {

            this.cont = context;
            this.str_typ = strType;


        }

        public GetSearchResults(Context context, String strType, Spinner spin, String strSelectedItem, String str_lang) {

            this.cont = context;
            this.nStateId = strSelectedItem;
            this.spinCommon = spin;
            this.str_typ = strType;
            this.str_lang = str_lang;
        }

        public GetSearchResults(Context context, String strType, Spinner spin, String strSelectedItem, String strSelectedItemone, String str_lang) {
            this.cont = context;
            this.nStateId = strSelectedItem;
            this.nCityId = strSelectedItemone;
            this.spinCommon = spin;
            this.str_typ = strType;
            this.str_lang = str_lang;
        }

        @Override
        protected void onPreExecute() {
            TempleLocationNew.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (dialog != null && !dialog.isShowing()) {
                        dialog.setMessage("Loading.....");
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
                String charset = "UTF-8";
                ///  String requestURL = "http://www.templeopedia.com/templeopedia/web_service/category";
                String requestURL = null;
                requestURL = GeneralData.SERVER_IP + "api_response/" + str_typ;
                if (str_lang.equalsIgnoreCase("ta")) {
                    requestURL = GeneralData.SERVER_IP + "api_response/" + str_typ + "?language=ta";
                } else if (str_lang.equalsIgnoreCase("en")) {
                    requestURL = GeneralData.SERVER_IP + "api_response/" + str_typ;
                }


                if (str_typ.equalsIgnoreCase("city")) {
                    requestURL = GeneralData.SERVER_IP + "api_response/" + str_typ + "?state_id=" + nStateId;


                    Log.i("BG", "city_id :" + nCityId);
                    Log.i("BG", "state_id :" + nStateId);
                    Log.i("BG", "URL :" + requestURL);
                    //  multipart.addFormField("country_id", "" + nCountryId);


                } else if (str_typ.equalsIgnoreCase("area")) {
                    requestURL = GeneralData.SERVER_IP + "api_response/" + str_typ + "?state_id=" + nStateId + "&city_id=" + nCityId;
                    Log.i("BG", "URL :" + requestURL);
                  /*  multipart.addFormField("country_id", "" + nCountryId);
                    multipart.addFormField("state_id", "" + nStateId);*/

                } else if (str_typ.equalsIgnoreCase("search_temple")) {
                    requestURL = GeneralData.SERVER_IP + "api_response/" + str_typ + "?state_id=" + nStateId + "&city_id=" + nCityId;
                    Log.i("BG", "URL :" + requestURL);
                  /*  multipart.addFormField("country_id", "" + nCountryId);
                    multipart.addFormField("state_id", "" + nStateId);*/
                  /*  multipart.addFormField("city_id", "" + nCountryId);
                    multipart.addFormField("diety_id", "" + nStateId);
                    multipart.addFormField("festival_id", "" + nStateId);*/
                }


                if (str_typ.equalsIgnoreCase("city") || str_typ.equalsIgnoreCase("area") || str_typ.equalsIgnoreCase("search_temple")) {
                    if (str_lang.equalsIgnoreCase("ta")) {
                        requestURL += "&language=ta";
                        Log.i("RA", requestURL);
                    }
                }
                MultipartUtility multipart = new MultipartUtility(requestURL, charset);
                // multipart.addFormField("id", "1");

                List<String> response = multipart.finish();
                StringBuilder sb = new StringBuilder();
                for (String line : response) {
                    System.out.println(line);
                    sb.append(line);
                }
                try {

                    JSONObject jsonObj = new JSONObject(sb.toString());
                    Log.i("HH", "StrResp : " + jsonObj.toString());
                    sResponse = jsonObj.toString();

                } catch (Exception e) {
                    e.printStackTrace();
                }

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


                ArrayList<TempleListBean> beanArrayList = new ArrayList<TempleListBean>();

                JSONObject jsobj = new JSONObject(sResponse);

                Log.i("HH", "search : " + sResponse);
                if (jsobj.getString("status").equalsIgnoreCase("true")) {

                    JSONArray templesCategoriesArray = jsobj.getJSONArray(str_typ);

                    if (templesCategoriesArray.length() > 0) {
                        for (int i = 0; i < templesCategoriesArray.length(); i++) {
                            TempleListBean templeListBean = new TempleListBean();
                            JSONObject providersServiceJSONobject = templesCategoriesArray.getJSONObject(i);
                            templeListBean.setTemple_id(providersServiceJSONobject.getString("id"));
                            templeListBean.setTemple_name(providersServiceJSONobject.getString("name"));
                            beanArrayList.add(templeListBean);
                        }
                    }

                    SpinnerAdapter adapter = new SpinnerAdapter(context, R.id.spin_text, beanArrayList);
                    spinCommon.setAdapter(adapter);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                }

              /*  if (str_typ.equalsIgnoreCase("city")) {
                    if (beanArrayList.size() > 0) {
                        GetSearchResults getSearchResults = new GetSearchResults(ctx, "festival", spin_festival);
                        getSearchResults.execute();
                    }

                }
                if (str_typ.equalsIgnoreCase("festival")) {
                    GetSearchResults getSearchResults1 = new GetSearchResults(ctx, "diety", spin_deity);
                    getSearchResults1.execute();
                }*/
                if (str_typ.equalsIgnoreCase("search_temple")) {
                    JSONArray templesCategoriesArray = jsobj.getJSONArray("temples");

                    if (templesCategoriesArray.length() > 0) {
                        for (int i = 0; i < templesCategoriesArray.length(); i++) {
                            TempleListBean templeListBean = new TempleListBean();
                            JSONObject providersServiceJSONobject = templesCategoriesArray.getJSONObject(i);
                            String temple_id = providersServiceJSONobject.getString("temple_id");
                            String temple_name = providersServiceJSONobject.getString("temple_name");
                            String temple_address = providersServiceJSONobject.getString("temple_address");
                            Log.i("NN", temple_id);
                            Log.i("NN", temple_name);
                            Log.i("NN", temple_address);

                        }
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    class GetSearchDetails extends AsyncTask {

        String user_id = null;
        String sResponse = null;
        Context cont;
        String str_typ, strProtocolTyp, str_lan;
        //String nCountry_Id, nState_Id, nCity_Id, nFestival_Id, nDietyId;


        public GetSearchDetails(Context context, String strType, String strProtocol, String str_lang) {

            this.cont = context;
            this.str_typ = strType;
            this.strProtocolTyp = strProtocol;
            this.str_lan = str_lang;


        }

        @Override
        protected void onPreExecute() {
            TempleLocationNew.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (dialog != null && !dialog.isShowing()) {
                        dialog.setMessage("Loading.....");
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
                String charset = "UTF-8";
                ///  String requestURL = "http://www.templeopedia.com/templeopedia/web_service/category";
                String requestURL = GeneralData.SERVER_IP + "api_response/" + str_typ;
                if (str_lan.equalsIgnoreCase("ta")) {
                    requestURL = GeneralData.SERVER_IP + "api_response/" + str_typ + "?language=ta";
                } else if (str_lan.equalsIgnoreCase("en")) {
                    requestURL = GeneralData.SERVER_IP + "api_response/" + str_typ;
                }

                //country_id, state_id, city_id, diety_id, festival_id

                Log.i("BG", "state_id :" + strAreaId);
                Log.i("BG", "strProtocolTyp :" + strProtocolTyp);
                Log.i("BG", "URL :" + requestURL);
               /* if (strProtocolTyp.equalsIgnoreCase("nscfd") || strProtocolTyp.equalsIgnoreCase("ncfd") || strProtocolTyp.equalsIgnoreCase("nfd") || strProtocolTyp.equalsIgnoreCase("nd") || strProtocolTyp.equalsIgnoreCase("n") || strProtocolTyp.equalsIgnoreCase("f") || strProtocolTyp.equalsIgnoreCase("d")) {
                    if (str_lan.equalsIgnoreCase("ta")) {
                        requestURL += "&language=ta";
                        Log.i("RA", requestURL);
                    }
                }*/

                if (strProtocolTyp.equalsIgnoreCase("nscfd")) {
                    if (str_lan.equalsIgnoreCase("ta")) {
                        requestURL = GeneralData.SERVER_IP + "api_response/" + str_typ + "?state_id=" + strStateId;
                        requestURL += "&language=ta";
                        Log.i("RA", requestURL);
                    } else {
                        requestURL = GeneralData.SERVER_IP + "api_response/" + str_typ + "?state_id=" + strStateId;
                    }

                    Log.i("BG", "state_id :" + strAreaId);
                    Log.i("BG", "strProtocolTyp :" + strProtocolTyp);
                    Log.i("BG", "URL :" + requestURL);
                    //  multipart.addFormField("country_id", strCountryId);


                } else if (strProtocolTyp.equalsIgnoreCase("ncfd")) {
                    if (str_lan.equalsIgnoreCase("ta")) {
                        requestURL = GeneralData.SERVER_IP + "api_response/" + str_typ + "?state_id=" + strStateId + "&city_id=" + strCityId;
                        requestURL += "&language=ta";
                        Log.i("RA", requestURL);
                    } else {
                        requestURL = GeneralData.SERVER_IP + "api_response/" + str_typ + "?state_id=" + strStateId + "&city_id=" + strCityId;

                    }
                    Log.i("BG", "state_id :" + strAreaId);
                    Log.i("BG", "strProtocolTyp :" + strProtocolTyp);
                    Log.i("BG", "URL :" + requestURL);
                    /*multipart.addFormField("country_id", strCountryId);
                    multipart.addFormField("state_id", strStateId);*/

                } else if (strProtocolTyp.equalsIgnoreCase("nfd")) {
                    if (str_lan.equalsIgnoreCase("ta")) {
                        requestURL = GeneralData.SERVER_IP + "api_response/" + str_typ + "?state_id=" + strStateId + "&city_id=" + strCityId + "&area_id=" + strAreaId;
                        requestURL += "&language=ta";
                        Log.i("RA", requestURL);
                    } else {
                        requestURL = GeneralData.SERVER_IP + "api_response/" + str_typ + "?state_id=" + strStateId + "&city_id=" + strCityId + "&area_id=" + strAreaId;

                    }
                    Log.i("BG", "state_id :" + strAreaId);
                    Log.i("BG", "strProtocolTyp :" + strProtocolTyp);
                    Log.i("BG", "URL :" + requestURL);
                   /* multipart.addFormField("country_id", strCountryId);
                    multipart.addFormField("state_id", strStateId);

                    multipart.addFormField("city_id", strCityId);*/


                } else if (strProtocolTyp.equalsIgnoreCase("nd")) {
                    if (str_lan.equalsIgnoreCase("ta")) {
                        requestURL = GeneralData.SERVER_IP + "api_response/" + str_typ + "?state_id=" + strStateId + "&city_id=" + strCityId + "&area_id=" + strAreaId + "&festival_id=" + strFestivalId;
                        requestURL += "&language=ta";
                        Log.i("RA", requestURL);
                    } else {
                        requestURL = GeneralData.SERVER_IP + "api_response/" + str_typ + "?state_id=" + strStateId + "&city_id=" + strCityId + "&area_id=" + strAreaId + "&festival_id=" + strFestivalId;

                    }
                    Log.i("BG", "state_id :" + strAreaId);
                    Log.i("BG", "strProtocolTyp :" + strProtocolTyp);
                    Log.i("BG", "URL :" + requestURL);
                   /* multipart.addFormField("country_id", strCountryId);
                    multipart.addFormField("state_id", "" + strStateId);

                    multipart.addFormField("city_id", strCityId);
                    multipart.addFormField("festival_id", strFestivalId);*/


                } else if (strProtocolTyp.equalsIgnoreCase("n")) {
                    if (str_lan.equalsIgnoreCase("ta")) {
                        requestURL = GeneralData.SERVER_IP + "api_response/" + str_typ + "?state_id=" + strStateId + "&city_id=" + strCityId + "&area_id=" + strAreaId + "&festival_id=" + strFestivalId + "&diety_id=" + strDietyId;
                        requestURL += "&language=ta";
                        Log.i("RA", requestURL);
                    } else {
                        requestURL = GeneralData.SERVER_IP + "api_response/" + str_typ + "?state_id=" + strStateId + "&city_id=" + strCityId + "&area_id=" + strAreaId + "&festival_id=" + strFestivalId + "&diety_id=" + strDietyId;

                    }
                    Log.i("BG", "state_id :" + strAreaId);
                    Log.i("BG", "strProtocolTyp :" + strProtocolTyp);
                    Log.i("BG", "URL :" + requestURL);
                   /* multipart.addFormField("country_id", strCountryId);
                    multipart.addFormField("state_id", strStateId);

                    multipart.addFormField("city_id", strCityId);
                    multipart.addFormField("festival_id", strFestivalId);
                    multipart.addFormField("diety_id", strDietyId);
*/
                }

                //Festival
                else if (strProtocolTyp.equalsIgnoreCase("f")) {
                    if (str_lan.equalsIgnoreCase("ta")) {
                        requestURL = GeneralData.SERVER_IP + "api_response/" + str_typ + "?festival_id=" + strFestivalId;
                        requestURL += "&language=ta";
                        Log.i("RA", requestURL);
                    } else {
                        requestURL = GeneralData.SERVER_IP + "api_response/" + str_typ + "?festival_id=" + strFestivalId;

                    }
                    Log.i("BG", "state_id :" + strAreaId);
                    Log.i("BG", "strProtocolTyp :" + strProtocolTyp);
                    Log.i("BG", "strProtocolTyp :" + strProtocolTyp);
                    Log.i("BG", "URL :" + requestURL);
                   /* multipart.addFormField("country_id", strCountryId);
                    multipart.addFormField("state_id", strStateId);

                    multipart.addFormField("city_id", strCityId);
                    multipart.addFormField("festival_id", strFestivalId);
                    multipart.addFormField("diety_id", strDietyId);
*/
                }
                //Diety
                else if (strProtocolTyp.equalsIgnoreCase("d")) {
                    if (str_lan.equalsIgnoreCase("ta")) {
                        requestURL = GeneralData.SERVER_IP + "api_response/" + str_typ + "?diety_id=" + strDietyId;
                        requestURL += "&language=ta";
                        Log.i("RA", requestURL);
                    } else {
                        requestURL = GeneralData.SERVER_IP + "api_response/" + str_typ + "?diety_id=" + strDietyId;

                    }
                    Log.i("BG", "state_id :" + strAreaId);
                    Log.i("BG", "diety_id :" + strDietyId);
                    Log.i("BG", "strProtocolTyp :" + strProtocolTyp);
                    Log.i("BG", "URL :" + requestURL);
                   /* multipart.addFormField("country_id", strCountryId);
                    multipart.addFormField("state_id", strStateId);

                    multipart.addFormField("city_id", strCityId);
                    multipart.addFormField("festival_id", strFestivalId);
                    multipart.addFormField("diety_id", strDietyId);
*/
                }
                //empty_search

                else if (strProtocolTyp.equalsIgnoreCase("empty_search")) {
                    Log.i("VB-n", "8");
                    if (str_lan.equalsIgnoreCase("ta")) {
                        requestURL = GeneralData.SERVER_IP + "api_response/search_temple";
                        requestURL += "&language=ta";
                        Log.i("RA", requestURL);
                    } else {
                        requestURL = GeneralData.SERVER_IP + "api_response/search_temple";

                    }
                    Log.i("BG", "state_id :" + strAreaId);
                    Log.i("BG", "diety_id :" + strDietyId);
                    Log.i("BG", "strProtocolTyp :" + strProtocolTyp);
                    Log.i("BG", "URL :" + requestURL);
                   /* multipart.addFormField("country_id", strCountryId);
                    multipart.addFormField("state_id", strStateId);

                    multipart.addFormField("city_id", strCityId);
                    multipart.addFormField("festival_id", strFestivalId);
                    multipart.addFormField("diety_id", strDietyId);
*/
                }

                MultipartUtility multipart = new MultipartUtility(requestURL, charset);
                // multipart.addFormField("id", "1");

                List<String> response = multipart.finish();
                StringBuilder sb = new StringBuilder();
                for (String line : response) {
                    System.out.println(line);
                    sb.append(line);
                }
                try {

                    JSONObject jsonObj = new JSONObject(sb.toString());
                    Log.i("HH", "search-det : " + jsonObj.toString());
                    sResponse = jsonObj.toString();

                } catch (Exception e) {
                    e.printStackTrace();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return sResponse;
        }

        @Override
        protected void onPostExecute(Object response) {
            dialog.dismiss();
            try {
                // requestURL = GeneralData.SERVER_IP + "api_response/" + str_typ + "?country_id=" + strCountryId + "&state_id=" + strStateId + "&city_id=" + strCityId + "&festival_id=" + strFestivalId + "&diety_id=" + strDietyId;
                Log.i("RA", "RESP : " + sResponse);
                Intent i = new Intent(TempleLocationNew.this, TempleSearchDetails.class);
                i.putExtra("search_detail_json", sResponse);
                if (strProtocolTyp.equalsIgnoreCase("nscfd")) {

                    i.putExtra("str_ProtocolTyp", "nscfd");
                    i.putExtra("strStateId", strStateId);


                } else if (strProtocolTyp.equalsIgnoreCase("ncfd")) {
                    i.putExtra("str_ProtocolTyp", "ncfd");
                    i.putExtra("strStateId", strStateId);
                    i.putExtra("strCityId", strCityId);
                } else if (strProtocolTyp.equalsIgnoreCase("nfd")) {
                    i.putExtra("str_ProtocolTyp", "nfd");
                    i.putExtra("strCityId", strCityId);
                    i.putExtra("strStateId", strStateId);
                    i.putExtra("strCountryId", strAreaId);

                } else if (strProtocolTyp.equalsIgnoreCase("nd")) {
                    i.putExtra("str_ProtocolTyp", "nd");
                    i.putExtra("strFestivalId", strFestivalId);
                    i.putExtra("strCityId", strCityId);
                    i.putExtra("strStateId", strStateId);
                    i.putExtra("strCountryId", strAreaId);


                } else if (strProtocolTyp.equalsIgnoreCase("n")) {
                    i.putExtra("str_ProtocolTyp", "n");
                    i.putExtra("strDietyId", strDietyId);
                    i.putExtra("strFestivalId", strFestivalId);
                    i.putExtra("strCityId", strCityId);
                    i.putExtra("strStateId", strStateId);
                    i.putExtra("strCountryId", strAreaId);
                }

                //Festival
                else if (strProtocolTyp.equalsIgnoreCase("f")) {
                    i.putExtra("str_ProtocolTyp", "f");
                    i.putExtra("strFestivalId", strFestivalId);
                }
                //Diety
                else if (strProtocolTyp.equalsIgnoreCase("d")) {
                    i.putExtra("str_ProtocolTyp", "d");
                    i.putExtra("strDietyId", strDietyId);

                } else if (strProtocolTyp.equalsIgnoreCase("empty_search")) {
                    i.putExtra("str_ProtocolTyp", "empty_search");
                }
                startActivity(i);
                //  finish();

             /*   JSONObject jsobj = new JSONObject(sResponse);
                JSONArray templesCategoriesArray = jsobj.getJSONArray("temples");
                if (templesCategoriesArray.length() > 0) {
                    for (int i = 0; i < templesCategoriesArray.length(); i++) {
                        TempleListBean templeListBean = new TempleListBean();
                        JSONObject providersServiceJSONobject = templesCategoriesArray.getJSONObject(i);
                        String temple_id = providersServiceJSONobject.getString("temple_id");
                        String temple_name = providersServiceJSONobject.getString("temple_name");
                        String temple_address = providersServiceJSONobject.getString("temple_address");
                        Log.i("NN", temple_id);
                        Log.i("NN", temple_name);
                        Log.i("NN", temple_address);
                    }
                }*/

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    class GetLocation extends AsyncTask {

        String temp_id = null;
        String sResponse = null;
        Context cont;
        String strLang;

        public GetLocation(Context context, String tem_id, String strlang) {
            this.cont = context;
            this.strLang = strlang;
            this.temp_id = tem_id;
        }

        @Override
        protected void onPreExecute() {
            TempleLocationNew.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (dialog != null && !dialog.isShowing()) {
                        dialog.setMessage("Loading.....");
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
                String charset = "UTF-8";
                ///  String requestURL = "http://www.templeopedia.com/templeopedia/web_service/category";
                String requestURL = null;
                if (strLang.equalsIgnoreCase("ta")) {
                    requestURL = GeneralData.SERVER_IP + "api_response/location?language=ta&temple_id=" + temp_id;
                } else if (strLang.equalsIgnoreCase("en")) {
                    requestURL = GeneralData.SERVER_IP + "api_response/location?temple_id=" + temp_id;
                }

                MultipartUtility multipart = new MultipartUtility(requestURL, charset);
                // multipart.addFormField("id", "1");

                List<String> response = multipart.finish();
                StringBuilder sb = new StringBuilder();
                for (String line : response) {
                    System.out.println(line);
                    sb.append(line);
                }
                try {
                    JSONObject jsonObj = new JSONObject(sb.toString());
                    Log.i("", "StrResp : " + jsonObj.toString());
                    sResponse = jsonObj.toString();
                } catch (Exception e) {
                    e.printStackTrace();
                }

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


                ArrayList<TempleListBean> beanArrayList = new ArrayList<TempleListBean>();

                JSONObject jsobj = new JSONObject(sResponse);

                Log.i("HH", "strResp : " + sResponse);
                if (jsobj.getString("status").equalsIgnoreCase("true")) {
                    str_content = jsobj.getString("location");
                    str_temp_name = jsobj.getString("temple_name");
                    str_temp_address = jsobj.getString("address");

                    txt_temple_name.setText(str_temp_name);
                    txt_loc_content.setText(str_content);
                    txt_temp_addr.setText( Html.fromHtml("<p>" + str_temp_address + "</p>"));


                }


            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}
