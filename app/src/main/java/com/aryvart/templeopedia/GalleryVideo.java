package com.aryvart.templeopedia;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.aryvart.templeopedia.Interface.GalleryVideoInterface;
import com.aryvart.templeopedia.adapter.GalleryVideoAdpater;
import com.aryvart.templeopedia.adapter.SpinnerAdapter;
import com.aryvart.templeopedia.bean.GalleryVideoBean;
import com.aryvart.templeopedia.bean.TempleListBean;
import com.aryvart.templeopedia.genericclasses.GeneralData;
import com.aryvart.templeopedia.genericclasses.MultipartUtility;
import com.aryvart.templeopedia.test.VideoPlayer;
import com.aryvart.templeopedia.test.VideoPlayerActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Android2 on 8/4/2016.
 */
public class GalleryVideo extends Activity implements GalleryVideoInterface {

    ImageView img_home, img_lang, img_search, img_back;
    RelativeLayout rl_choose_lang;
    int n_count = 0;
    View itemView1;
    Context context;
    ImageView img_english, img_tamil, img_menu;
    LinearLayout ll_tamil, ll_english, lay_about, lay_specialities, lay_timing, lay_history, lay_administration, lay_gallery, lay_festival, lay_art, lay_location;
    ProgressDialog dialog;
    RecyclerView recyclerView;
    String str_temp_id, str_lat, str_long, str_content, str_temp_address;
    TextView txt_all, txt_kumbabishekam, txt_festivals, txt_gallery_title, txt_search_title;
    JSONArray galleryAll, gallerykumbabaeshegam, galleryfestivals;
    Button btn_search;
    LinearLayout menu_layout, rl_content;
    String strAreaId, strStateId, strCityId, strFestivalId, strDietyId, str_temp_name, str_type, str_lang;
    Spinner spin_area, spin_state, spin_city, spin_deity, spin_festival;
    TextView txt_abt_temple, txt_nearby, txt_dharshan, txt_history, txt_admin, txt_gallery, txt_loc, txt_festival, txt_art, txt_special, txt_empty;
    private StaggeredGridLayoutManager gaggeredGridLayoutManager;

    String str_tab_click_value = "all";

    String strGlobalResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gallery_video);
        context = this;
        GeneralData.context = this;
        dialog = new ProgressDialog(GalleryVideo.this);
        img_home = (ImageView) findViewById(R.id.home);


        img_lang = (ImageView) findViewById(R.id.language);
        img_search = (ImageView) findViewById(R.id.search);
        img_english = (ImageView) findViewById(R.id.img_english);
        img_tamil = (ImageView) findViewById(R.id.img_tamil);
        img_back = (ImageView) findViewById(R.id.back);
        rl_content = (LinearLayout) findViewById(R.id.content_lay);
        ll_english = (LinearLayout) findViewById(R.id.llay_english);
        ll_tamil = (LinearLayout) findViewById(R.id.llay_tamil);
        rl_choose_lang = (RelativeLayout) findViewById(R.id.rl_choose_lang);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        txt_all = (TextView) findViewById(R.id.txt_all);
        txt_gallery_title = (TextView) findViewById(R.id.txt_gallery_title);
        menu_layout = (LinearLayout) findViewById(R.id.menu_layout);
        img_menu = (ImageView) findViewById(R.id.menu);
        txt_festival = (TextView) findViewById(R.id.txt_festival);
        txt_kumbabishekam = (TextView) findViewById(R.id.txt_kumbabishekam);
        recyclerView.setHasFixedSize(true);
        txt_empty = (TextView) findViewById(R.id.txt_empty);
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
        txt_festivals = (TextView) findViewById(R.id.txt_festivals);
        txt_art = (TextView) findViewById(R.id.txt_arts);
        txt_special = (TextView) findViewById(R.id.txt_special);
      /*  str_temp_id = getIntent().getStringExtra("temple_id");
        str_lat = getIntent().getStringExtra("temp_lat");
        str_long = getIntent().getStringExtra("temp_long");
        str_content = getIntent().getStringExtra("temp_content");
        str_temp_name = getIntent().getStringExtra("temple_name");
        str_temp_address = getIntent().getStringExtra("temp_address");
        str_type = getIntent().getStringExtra("temp_from");*/
        gaggeredGridLayoutManager = new StaggeredGridLayoutManager(2, 1);
        txt_all.setBackgroundResource(R.drawable.orange_dark_outline);
        txt_all.setTextColor(Color.parseColor("#F96300"));
        str_lang = String.valueOf(GeneralData.str_lang);
        txt_kumbabishekam.setTextColor(Color.parseColor("#000000"));
        txt_festival.setTextColor(Color.parseColor("#000000"));
        txt_kumbabishekam.setBackgroundResource(0);
        txt_festival.setBackgroundResource(0);


        str_temp_id = SplashActivity.preferences.getString("temple_id_det", null);
        str_type = SplashActivity.preferences.getString("temple_from_det", null);
        str_temp_name = SplashActivity.preferences.getString("temple_name_det", null);
        str_lat = SplashActivity.preferences.getString("temple_lat_det", null);
        str_long = SplashActivity.preferences.getString("temple_long_det", null);
        str_content = SplashActivity.preferences.getString("temple_content_det", null);
        str_temp_address = SplashActivity.preferences.getString("temple_addr_det", null);


        if (str_temp_id == null&&str_type == null&& str_temp_name == null&&str_lat==null&&str_long==null&&str_content==null &&str_temp_address==null) {
            str_temp_id = SplashActivity.preferences.getString("temple_id_det", null);
            str_type = SplashActivity.preferences.getString("temple_from_det", null);
            str_temp_name = SplashActivity.preferences.getString("temple_name_det", null);
            str_lat = SplashActivity.preferences.getString("temple_lat_det", null);
            str_long = SplashActivity.preferences.getString("temple_long_det", null);
            str_content = SplashActivity.preferences.getString("temple_content_det", null);
            str_temp_address = SplashActivity.preferences.getString("temple_addr_det", null);
        }
       /* if (str_type == null ) {
            str_type = SplashActivity.preferences.getString("temple_from_det", null);
        }
        if (str_temp_name == null ) {
            str_temp_name = SplashActivity.preferences.getString("temple_name_det", null);
        }*/

        if (str_lang.equalsIgnoreCase("ta")) {
            txt_gallery_title.setText("காட்சியகம்");
            txt_all.setText("அனைத்தும்");
            img_english.setBackgroundResource(R.drawable.white_dot);
            img_tamil.setBackgroundResource(R.drawable.orange_dot);
            txt_kumbabishekam.setText("கும்பாபிஷேகம்");
            txt_festival.setText("திருவிழாக்கள்");
            txt_all.setTextSize(10);

            txt_kumbabishekam.setTextSize(10);
            txt_festival.setTextSize(10);
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
        } else if (str_lang.equalsIgnoreCase("en")) {
            img_english.setBackgroundResource(R.drawable.orange_dot);
            img_tamil.setBackgroundResource(R.drawable.white_dot);
            txt_gallery_title.setText("Gallery");
            txt_all.setText("All");
            txt_kumbabishekam.setText("Kumbabishekam");
            txt_festival.setText("Festivals");
            txt_abt_temple.setText("ABOUT TEMPLE");
            txt_dharshan.setText("DHARSHAN TIMING");
            txt_history.setText("HISTORY");
            txt_admin.setText("ADMINISTRATION");
            txt_gallery.setText("GALLERY");
            txt_loc.setText("LOCATION");
            txt_festivals.setText("FESTIVALS");
            txt_art.setText("Arts and Inscriptions");
            txt_special.setText("SPECIALITIES");

        }

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
        rl_content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rl_choose_lang.setVisibility(View.GONE);
            }
        });
        lay_about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                str_type = "about_temple";
                Intent i = new Intent(GalleryVideo.this, TempleDetails.class);
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
                Intent i = new Intent(GalleryVideo.this, TempleDetails.class);
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
                Intent i = new Intent(GalleryVideo.this, TempleDetails.class);
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
                Intent i = new Intent(GalleryVideo.this, TempleDetails.class);
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
                Intent i = new Intent(GalleryVideo.this, GalleryVideo.class);
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
                Intent i = new Intent(GalleryVideo.this, TempleDetails.class);
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
                Intent i = new Intent(GalleryVideo.this, TempleLocationNew.class);
                i.putExtra("temp_from", str_type);
                i.putExtra("temple_id", str_temp_id);
                i.putExtra("temple_name", str_temp_name);
                i.putExtra("temp_lat", str_lat);
                i.putExtra("temp_long", str_long);
                i.putExtra("temp_content", str_content);
                i.putExtra(("temp_address"), str_temp_address);
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
                Intent i = new Intent(GalleryVideo.this, TempleDetails.class);
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
                Intent i = new Intent(GalleryVideo.this, TempleDetails.class);
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
        txt_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                str_tab_click_value = "all";
                txt_all.setBackgroundResource(R.drawable.orange_dark_outline);
                txt_all.setTextColor(Color.parseColor("#F96300"));
                txt_kumbabishekam.setTextColor(Color.parseColor("#000000"));
                txt_festival.setTextColor(Color.parseColor("#000000"));
                txt_kumbabishekam.setBackgroundResource(0);
                txt_festival.setBackgroundResource(0);
                showView("all");

            }
        });
        txt_festival.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                str_tab_click_value = "Festival";
                txt_all.setBackgroundResource(0);
                txt_kumbabishekam.setBackgroundResource(0);
                txt_festival.setBackgroundResource(R.drawable.orange_dark_outline);
                txt_all.setTextColor(Color.parseColor("#000000"));
                txt_kumbabishekam.setTextColor(Color.parseColor("#000000"));
                txt_festival.setTextColor(Color.parseColor("#F96300"));
                showView("festivals");


            }
        });
        txt_kumbabishekam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                str_tab_click_value = "Kumbabishekam";
                txt_all.setBackgroundResource(0);
                txt_kumbabishekam.setBackgroundResource(R.drawable.orange_dark_outline);
                txt_festival.setBackgroundResource(0);
                txt_all.setTextColor(Color.parseColor("#000000"));
                txt_kumbabishekam.setTextColor(Color.parseColor("#F96300"));
                txt_festival.setTextColor(Color.parseColor("#000000"));
                showView("kumbabaeshegam");

            }
        });
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences.Editor edit = HomePage.prefs.edit();
                edit.putString("from", str_type);
                edit.commit();

                finish();
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
                    txt_search_title.setText("கோவில் தேடுக");
                    btn_search.setText("தேடுக");
                    loadSpinnerBasedLang(1);
                } else if (str_lang.equalsIgnoreCase("en")) {
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
                                Toast.makeText(GalleryVideo.this, "குறைந்தது ஒரு துறை தேர்வு செய்க", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(GalleryVideo.this, "Select atleast one field", Toast.LENGTH_SHORT).show();
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

        ll_english.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                img_english.setBackgroundResource(R.drawable.orange_dot);
                img_tamil.setBackgroundResource(R.drawable.white_dot);

                GeneralData.str_lang = "en";

                txt_gallery_title.setText("Gallery");
                txt_all.setText("All");
                txt_all.setTextSize(13);
                txt_kumbabishekam.setTextSize(13);
                txt_festival.setTextSize(13);
                txt_kumbabishekam.setText("Kumbabishekam");
                txt_festival.setText("Festivals");
                txt_abt_temple.setText("ABOUT TEMPLE");
                txt_dharshan.setText("DHARSHAN TIMING");
                txt_history.setText("HISTORY");
                txt_admin.setText("ADMINISTRATION");
                txt_gallery.setText("GALLERY");
                txt_loc.setText("LOCATION");
                txt_festivals.setText("FESTIVALS");
                txt_art.setText("Arts and Inscriptions");
                txt_special.setText("SPECIALITIES");


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

                GeneralData.str_lang = "ta";

                txt_gallery_title.setText("காட்சியகம்");
                txt_all.setText("அனைத்தும்");

                txt_kumbabishekam.setText("கும்பாபிஷேகம்");
                txt_festival.setText("திருவிழாக்கள்");
                txt_all.setTextSize(10);
                txt_kumbabishekam.setTextSize(10);
                txt_festival.setTextSize(10);
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
        img_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(GalleryVideo.this, HomePage.class));
                finish();
                finishAffinity();
            }
        });


     /*   try {
            final int width, height;
            width = 150;
            height = 150;
            gaggeredGridLayoutManager.setMeasuredDimension(width, height);
        } catch (Exception e) {
            e.printStackTrace();
        }*/


        // recyclerView.setLayoutManager(gaggeredGridLayoutManager);
        GetGallery getGallery = new GetGallery(context, str_temp_id
        );
        getGallery.execute();

       /* List<GalleryVideoBean> gaggeredList = getListItemData();

        GalleryVideoAdpater rcAdapter = new GalleryVideoAdpater(GalleryVideoInterface.this, gaggeredList);
        recyclerView.setAdapter(rcAdapter);*/
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

    private void showView(String data) {
       /* Toast.makeText(getApplicationContext(), data, Toast.LENGTH_SHORT).show();*/
        if (data == "all") {
           /* if(galleryAll!=null){
                if (galleryAll.length() > 0) {
                    txt_empty.setVisibility(View.GONE);*/
            LoadLayout(recyclerView, galleryAll, strGlobalResponse);
              /*  }
                else{
                    txt_empty.setVisibility(View.VISIBLE);
                }
            }*/


        } else if (data == "kumbabaeshegam") {
            if (gallerykumbabaeshegam.length() > 0) {
                txt_empty.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                LoadLayout(recyclerView, gallerykumbabaeshegam, strGlobalResponse);
            } else {
                txt_empty.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
            }


        } else {
            if (galleryfestivals.length() > 0) {
                txt_empty.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                LoadLayout(recyclerView, galleryfestivals, strGlobalResponse);
            } else {
                txt_empty.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
            }

        }
    }

    private void LoadLayout(RecyclerView recyclerView, JSONArray providerServicesMonth, String sResponse) {

        String name = null, image = null, time = null, date = null;
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(gaggeredGridLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
     /*   recyclerView.addItemDecoration(new com.aryvart.utician.adapter.DividerItemDecoration(this, LinearLayoutManager.VERTICAL));*/
        ArrayList<GalleryVideoBean> beanArrayList = new ArrayList<GalleryVideoBean>();
        JSONObject jsoBj = null;
        try {
            if (providerServicesMonth.length() > 0) {
                recyclerView.setVisibility(View.VISIBLE);
                txt_empty.setVisibility(View.GONE);
                for (int i = 0; i < providerServicesMonth.length(); i++) {

                    GalleryVideoBean bean = new GalleryVideoBean();
                    jsoBj = providerServicesMonth.getJSONObject(i);
                    Log.i("GG", "providersServiceJSONobject : " + jsoBj);


                    if (jsoBj.getString("type").equalsIgnoreCase("image")) {
                        String strURL = GeneralData.SERVER_IP + "uploads/gallery/" + jsoBj.getString("url");
                        String strImageName = jsoBj.getString("image");
                        bean.setStr_temp_image(GeneralData.SERVER_IP + "uploads/gallery/" + strImageName);
                        bean.setStr_type(jsoBj.getString("type"));
                        bean.setStrID(jsoBj.getString("id"));
                        bean.setStrName(strImageName);
                        if (sResponse.length() > 0) {
                            bean.setGallery_json(sResponse);
                        }
                        bean.setStr_url(strURL);
                        Log.i("nn-url", strURL);

                    }

                    else if (jsoBj.getString("type").equalsIgnoreCase("video")) {

                        Log.i("nd", "hi");
                        String strURL = GeneralData.SERVER_IP + "uploads/video/" + jsoBj.getString("url");

                        String strVideoName = jsoBj.getString("video");
                        String strThumbnail = jsoBj.getString("thumbnail");
                        String str_checkthumb = GeneralData.SERVER_IP + "uploads/thumbnail/" + strThumbnail;
                        bean.setStr_temp_image(GeneralData.SERVER_IP + "uploads/thumbnail/" + strThumbnail);
                        bean.setStr_type(jsoBj.getString("type"));
                        bean.setStrName(strVideoName);
                        bean.setStr_url(strURL);
                        bean.setStrID(jsoBj.getString("id"));

                        Log.i("nn-url", strURL);
                        Log.i("nn-thumbnail", str_checkthumb);
                    }
                    beanArrayList.add(bean);
                }
            } else {
                if (str_lang.equalsIgnoreCase("en")) {
                    recyclerView.setVisibility(View.GONE);
                    txt_empty.setVisibility(View.VISIBLE);
                    txt_empty.setText("No Data Found");
                } else {
                    recyclerView.setVisibility(View.GONE);
                    txt_empty.setVisibility(View.VISIBLE);
                    txt_empty.setText("எவ்வித தகவலும் இல்லை");
                }


            }
            GalleryVideoAdpater rcAdapter = new GalleryVideoAdpater(context, beanArrayList, (GalleryVideoInterface) context);
            recyclerView.setAdapter(rcAdapter);

            gaggeredGridLayoutManager.scrollToPosition(1);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void getVideoUrl(String video_url, String strFileName, String gallery_type, String gallery_json, String str_position, String str_gallery_type, String strGalId) {
        //we need to call asynchrnous task to load the video url

        if (gallery_type.equalsIgnoreCase("video")) {
           /* GetVideo getVideo = new GetVideo(context, video_url, strFileName);
            getVideo.execute();*/

            String strPath = Environment.getExternalStorageDirectory() + "/" + strFileName;

            Intent i = new Intent(GalleryVideo.this, VideoPlayer.class);
            i.putExtra("fname", strFileName);
            i.putExtra("URL", video_url);
            startActivity(i);
        } else if (gallery_type.equalsIgnoreCase("image")) {
            Intent i = new Intent(GalleryVideo.this, ViewGalleryImage.class);
            i.putExtra("gallery_image", strFileName);
            i.putExtra("gallery_json", gallery_json);
            i.putExtra("gallery_position", str_position);
            i.putExtra("gallery_type", str_gallery_type);
            i.putExtra("gallery_tab_value", str_tab_click_value);
            i.putExtra("gallery_iD", strGalId);
            startActivity(i);
        }

    }

    /*private List<GalleryVideoBean> getListItemData() {
        List<GalleryVideoBean> listViewItems = new ArrayList<GalleryVideoBean>();
        listViewItems.add(new GalleryVideoBean(R.drawable.gallery_one));
        listViewItems.add(new GalleryVideoBean(R.drawable.gallery_two));
        listViewItems.add(new GalleryVideoBean(R.drawable.gallery_three));
        listViewItems.add(new GalleryVideoBean(R.drawable.gallery_four));
        listViewItems.add(new GalleryVideoBean(R.drawable.gallery_one));
        listViewItems.add(new GalleryVideoBean(R.drawable.gallery_two));
        listViewItems.add(new GalleryVideoBean(R.drawable.gallery_three));
        listViewItems.add(new GalleryVideoBean(R.drawable.gallery_four));
        return listViewItems;
    }*/
    class GetGallery extends AsyncTask {

        String temp_id = null;
        String sResponse = null;
        Context cont;
        String str_typ;

        public GetGallery(Context context, String str_temp_id) {

            this.cont = context;
            this.temp_id = str_temp_id;

        }

        @Override
        protected void onPreExecute() {
            GalleryVideo.this.runOnUiThread(new Runnable() {
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
                //String requestURL = "http://www.templeopedia.com/templeopedia/web_service/temple_details";

                String requestURL = GeneralData.SERVER_IP + "api_response/gallery?temple_id=" + temp_id;

                Log.i("HI", "URL : " + requestURL);

                MultipartUtility multipart = new MultipartUtility(requestURL, charset);
                // multipart.addFormField("temple_id", temp_id);

                List<String> response = multipart.finish();
                StringBuilder sb = new StringBuilder();
                for (String line : response) {
                    System.out.println(line);
                    sb.append(line);
                }
                try {
                    JSONObject jsonObj = new JSONObject(sb.toString());
                    Log.i("TEMP DETAIL PAGE", "StrResp : " + jsonObj.toString());
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


                ArrayList<GalleryVideoBean> beanArrayList = new ArrayList<GalleryVideoBean>();

                JSONObject jsobj = new JSONObject(sResponse);

                Log.i("HH", "strResp : " + sResponse);
                JSONObject jsoBj = null;
                strGlobalResponse = sResponse;

                if (jsobj.getString("status").equalsIgnoreCase("true")) {

                    JSONArray jsarGalley = jsobj.getJSONArray("all");
                    galleryAll = jsobj.getJSONArray("all");
                    gallerykumbabaeshegam = jsobj.getJSONArray("Kumbabishekam");
                    galleryfestivals = jsobj.getJSONArray("Festival");
                    if (galleryAll.length() > 0) {
                        for (int i = 0; i < jsarGalley.length(); i++) {

                            txt_empty.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);
                            GalleryVideoBean bean = new GalleryVideoBean();
                            jsoBj = galleryAll.getJSONObject(i);
                            Log.i("GG", "providersServiceJSONobject : " + jsoBj);

                            if (jsoBj.getString("type").equalsIgnoreCase("image")) {
                                String strURL = GeneralData.SERVER_IP + "uploads/gallery/" + jsoBj.getString("url");
                                String strImageName = jsoBj.getString("image");
                                bean.setStr_temp_image(GeneralData.SERVER_IP + "uploads/gallery/" + strImageName);
                                bean.setStr_type(jsoBj.getString("type"));
                                bean.setStrGalleryType(jsoBj.getString("gallery_type"));
                                bean.setStrName(strImageName);
                                bean.setGallery_json(sResponse);
                                bean.setStrID(jsoBj.getString("id"));
                                bean.setStr_url(strURL);
                                Log.i("url", strURL);

                            } else if (jsoBj.getString("type").equalsIgnoreCase("video")) {
                                String strURL = GeneralData.SERVER_IP + "uploads/video/" + jsoBj.getString("url");

                                String strVideoName = jsoBj.getString("video");
                                String strThumbnail = jsoBj.getString("thumbnail");
                                bean.setStrID(jsoBj.getString("id"));
                                bean.setStr_temp_image(GeneralData.SERVER_IP + "uploads/thumbnail/" + strThumbnail);
                                bean.setStr_type(jsoBj.getString("type"));
                                bean.setStrName(strVideoName);
                                bean.setStr_url(strURL);
                                Log.i("url", strURL);
                            }
                            beanArrayList.add(bean);


                        }
                        showView("all");

                        // GalleryVideoAdpater rcAdapter = new GalleryVideoAdpater(context, beanArrayList, (GalleryVideoInterface) context);
                        // recyclerView.setAdapter(rcAdapter);

                        LoadLayout(recyclerView, galleryAll, sResponse);


                    } else {
                        if (str_lang.equalsIgnoreCase("en")) {
                            recyclerView.setVisibility(View.GONE);
                            txt_empty.setVisibility(View.VISIBLE);
                            txt_empty.setText("No Data Found");
                        } else {
                            recyclerView.setVisibility(View.GONE);
                            txt_empty.setVisibility(View.VISIBLE);
                            txt_empty.setText("எவ்வித தகவலும் இல்லை");
                        }

                    }
                }

                // showView("all");

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

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
            GalleryVideo.this.runOnUiThread(new Runnable() {
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
            GalleryVideo.this.runOnUiThread(new Runnable() {
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
                Intent i = new Intent(GalleryVideo.this, TempleSearchDetails.class);
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

}
