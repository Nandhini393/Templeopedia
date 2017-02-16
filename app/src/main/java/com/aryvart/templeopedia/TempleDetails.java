package com.aryvart.templeopedia;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.aryvart.templeopedia.Interface.TempleList;
import com.aryvart.templeopedia.adapter.NearbyTempleListAdapter;
import com.aryvart.templeopedia.adapter.SpinnerAdapter;
import com.aryvart.templeopedia.adapter.SubTempleListAdapter;
import com.aryvart.templeopedia.adapter.TempleListAdapter;
import com.aryvart.templeopedia.bean.TempleListBean;
import com.aryvart.templeopedia.genericclasses.GeneralData;
import com.aryvart.templeopedia.genericclasses.MultipartUtility;
import com.aryvart.templeopedia.imageCache.ImageLoader;
import com.aryvart.templeopedia.justify.JustifiedTextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class TempleDetails extends Activity implements TempleList {
    ImageView menu, temple_image;
    TextView temple_name, text_content;
    WebView contents;
    LinearLayout ll_content, menu_layout, lay_about, lay_timing, lay_history, lay_administration, lay_gallery, lay_festival, lay_art, lay_location, lay_specialities;
    int menuView = 0;
    Context context;
    ProgressDialog pD;
    String str_temple_name, str_temple_id;
    ProgressDialog dialog;
    String str_about_us, str_admini;
    String str_type, str_temp_addr;
    ImageLoader imgLoader;
    ImageView img_home, img_lang, img_search, img_back;
    RelativeLayout rl_choose_lang;
    int n_count = 0;
    View itemView1;
    ImageView img_english, img_tamil;
    LinearLayout ll_tamil, ll_english;
    String str_from;
    Button btn_search;
    ListView near_temple_list;
    ScrollView myscroll;
    NearbyTempleListAdapter mAdapter;
    String strAreaId, strStateId, strCityId, strFestivalId, strDietyId;
    Spinner spin_area, spin_state, spin_city, spin_deity, spin_festival;
    TextView txt_abt_temple, txt_nearby, txt_dharshan, txt_history, txt_admin, txt_gallery, txt_loc, txt_festival, txt_art, txt_special, txt_search_title, txt_error_msg;
    String str_lang = "en", get_temp_name;
    LinearLayout ll_nearby;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.templedetails);
        try {
            context = this;
            GeneralData.context = this;
            dialog = new ProgressDialog(TempleDetails.this);
            menu = (ImageView) findViewById(R.id.menu);
            myscroll = (ScrollView) findViewById(R.id.my_scroll);
            temple_image = (ImageView) findViewById(R.id.temple_image);
            temple_name = (TextView) findViewById(R.id.temple_name);
            contents = (WebView) findViewById(R.id.contents);
            txt_abt_temple = (TextView) findViewById(R.id.txt_about_temple);
            txt_nearby = (TextView) findViewById(R.id.txt_nearby);
            text_content = (TextView) findViewById(R.id.text_content);
            menu_layout = (LinearLayout) findViewById(R.id.menu_layout);
            lay_about = (LinearLayout) findViewById(R.id.lay_about);
            lay_timing = (LinearLayout) findViewById(R.id.lay_timing);
            lay_history = (LinearLayout) findViewById(R.id.lay_history);
            ll_nearby = (LinearLayout) findViewById(R.id.ll_nearby);
            lay_administration = (LinearLayout) findViewById(R.id.lay_administration);
            lay_gallery = (LinearLayout) findViewById(R.id.lay_gallery);
            lay_festival = (LinearLayout) findViewById(R.id.lay_festival);
            lay_specialities = (LinearLayout) findViewById(R.id.lay_specialities);
            lay_art = (LinearLayout) findViewById(R.id.lay_art);
            lay_location = (LinearLayout) findViewById(R.id.lay_location);
            img_home = (ImageView) findViewById(R.id.home);
            //near_temple_list = (ListView) findViewById(R.id.nearby_temple_list);
            img_lang = (ImageView) findViewById(R.id.language);
            img_search = (ImageView) findViewById(R.id.search);
            img_back = (ImageView) findViewById(R.id.back);
            img_english = (ImageView) findViewById(R.id.img_english);
            img_tamil = (ImageView) findViewById(R.id.img_tamil);
            rl_choose_lang = (RelativeLayout) findViewById(R.id.rl_choose_lang);
            ll_english = (LinearLayout) findViewById(R.id.llay_english);
            ll_tamil = (LinearLayout) findViewById(R.id.llay_tamil);
            ll_content = (LinearLayout) findViewById(R.id.content_lay);
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
            txt_error_msg = (TextView) findViewById(R.id.txt_empty);


            str_temple_name = getIntent().getStringExtra("temple_name");
            str_temple_id = getIntent().getStringExtra("temple_id");
            str_type = getIntent().getStringExtra("temp_from");
            str_lang = GeneralData.str_lang;


            myscroll.fullScroll(ScrollView.FOCUS_UP);
            //  temple_name.setText(str_temple_name);
            imgLoader = new ImageLoader(context);
            contents.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {

                    menu_layout.setVisibility(View.GONE);
                    return false;
                }
            });
       //Set textsize for Justified textview
           /* contents.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
            contents.setAlignment(Paint.Align.LEFT);
            contents.setLineSpacing(10);*/
           /* contents.setTextColor(Color.parseColor("#666"));
            contents.setLineSpacing(1);*/

          /*  Click Listener*/
            if (str_temple_name == null) {
                str_temple_name = SplashActivity.preferences.getString("temple_name", null);
            } else {
                if (str_temple_name.length() > 10) {
                    temple_name.setText(str_temple_name);
                    temple_name.setTextSize(13);
                }
            }


            if (str_temple_id == null) {
                str_temple_id = SplashActivity.preferences.getString("sub_temple_id", null);
            }


            if (str_type == null) {
                str_type = "about_temple";
                text_content.setText("About Us");
            } else {
                Log.i("HI", "strType : " + str_type + "******* str_temple_id :" + str_temple_id);

            }

            Log.i("nn-abt", str_type);


            GetTempleDetails get_Provider_details = new GetTempleDetails(context, str_temple_id, str_type, str_lang);
            get_Provider_details.execute();


            GetNearbyTemples get_nearby_temples = new GetNearbyTemples(context, str_temple_id, str_lang);
            get_nearby_temples.execute();

            ll_content.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    rl_choose_lang.setVisibility(View.GONE);
                }
            });
            img_back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
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
                                    Toast.makeText(TempleDetails.this, "குறைந்தது ஒரு துறை தேர்வு செய்க", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(TempleDetails.this, "Select atleast one field", Toast.LENGTH_SHORT).show();
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
                txt_nearby.setText("Nearby Visiting Places");
                temple_name.setText(str_temple_name);

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
                txt_nearby.setText("அருகாமையில் உள்ள ஸ்தலங்கள் பற்றி");
                if (str_temple_name.length() > 10) {
                    temple_name.setText(str_temple_name);
                    temple_name.setTextSize(12);
                } else {
                    temple_name.setTextSize(15);
                }


            }
            ll_english.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    img_english.setBackgroundResource(R.drawable.orange_dot);
                    img_tamil.setBackgroundResource(R.drawable.white_dot);
                    GetTempleDetails get_Provider_details = new GetTempleDetails(context, str_temple_id, str_type, "en");
                    get_Provider_details.execute();
                    txt_abt_temple.setText("ABOUT TEMPLE");
                    txt_dharshan.setText("DHARSHAN TIMING");
                    txt_history.setText("HISTORY");
                    txt_admin.setText("ADMINISTRATION");
                    txt_gallery.setText("GALLERY");
                    txt_loc.setText("LOCATION");
                    txt_festival.setText("FESTIVALS");
                    txt_art.setText("Arts and Inscriptions");
                    txt_special.setText("SPECIALITIES");
                    txt_nearby.setText("Nearby Visiting Places");
                    GeneralData.str_lang = "en";
                    str_lang = String.valueOf(GeneralData.str_lang);
                    GetNearbyTemples get_nearby_temples = new GetNearbyTemples(context, str_temple_id, str_lang);
                    get_nearby_temples.execute();
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
                    GetTempleDetails get_Provider_details = new GetTempleDetails(context, str_temple_id, str_type, "ta");
                    get_Provider_details.execute();
                    txt_abt_temple.setText("கோவில் பற்றி");
                    txt_dharshan.setText("தரிசன நேரம்");
                    txt_history.setText("வரலாறு");
                    txt_admin.setText("நிர்வாகம்");

                    txt_festival.setText("திருவிழாக்கள்");

                    txt_special.setText("சிறப்பம்சங்கள்");
                    txt_special.setTextSize(9);
                    txt_nearby.setText("அருகாமையில் உள்ள ஸ்தலங்கள் பற்றி");

                    txt_gallery.setText("காட்சியகம்");
                    txt_loc.setText("இடம்");
                    txt_art.setText("கலை மற்றும் கல்வெட்டுகள்");
                    GeneralData.str_lang = "ta";
                    str_lang = String.valueOf(GeneralData.str_lang);
                    GetNearbyTemples get_nearby_temples = new GetNearbyTemples(context, str_temple_id, str_lang);
                    get_nearby_temples.execute();
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
                    startActivity(new Intent(TempleDetails.this, HomePage.class));
                    finish();
                    finishAffinity();
                }
            });
            lay_about.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //text_content.setText("About Us");
                    menu_layout.setVisibility(View.GONE);
                    menuView = 0;
                    str_type = "about_temple";
                    GetTempleDetails get_Provider_details = new GetTempleDetails(context, str_temple_id, str_type, str_lang);
                    get_Provider_details.execute();
                }
            });
            lay_timing.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // text_content.setText("Dharshan Timing");
                    menu_layout.setVisibility(View.GONE);
                    menuView = 0;
                    str_type = "dharsan_timing";
                    GetTempleDetails get_Provider_details = new GetTempleDetails(context, str_temple_id, str_type, str_lang);
                    get_Provider_details.execute();

                }
            });
            lay_history.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // text_content.setText("History");
                    menu_layout.setVisibility(View.GONE);
                    menuView = 0;
                    str_type = "history";
                    GetTempleDetails get_Provider_details = new GetTempleDetails(context, str_temple_id, str_type, str_lang);
                    get_Provider_details.execute();
                }
            });
            lay_administration.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // text_content.setText("Administration");
                    menu_layout.setVisibility(View.GONE);
                    menuView = 0;
                    str_type = "administration";
                    GetTempleDetails get_Provider_details = new GetTempleDetails(context, str_temple_id, str_type, str_lang);
                    get_Provider_details.execute();
                }
            });
            lay_gallery.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // text_content.setText("Gallery");
                    menu_layout.setVisibility(View.GONE);
                    menuView = 0;
                    str_type = "gallery";
                    GetTempleDetails get_Provider_details = new GetTempleDetails(context, str_temple_id, str_type, str_lang);
                    get_Provider_details.execute();


                }
            });
            lay_festival.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //text_content.setText("Festivals");
                    menu_layout.setVisibility(View.GONE);
                    menuView = 0;
                    str_type = "festivals";
                    GetTempleDetails get_Provider_details = new GetTempleDetails(context, str_temple_id, str_type, str_lang);
                    get_Provider_details.execute();
                }
            });
            lay_location.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //  text_content.setText("Location");
                    menu_layout.setVisibility(View.GONE);
                    menuView = 0;
                    str_type = "location";
                    GetTempleDetails get_Provider_details = new GetTempleDetails(context, str_temple_id, str_type, str_lang);
                    get_Provider_details.execute();
                }
            });
            lay_art.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //text_content.setText("Arts and Inspirations");
                    menu_layout.setVisibility(View.GONE);
                    menuView = 0;
                    str_type = "art_inspiration";
                    GetTempleDetails get_Provider_details = new GetTempleDetails(context, str_temple_id, str_type, str_lang);
                    get_Provider_details.execute();
                }
            });

            lay_specialities.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // text_content.setText("Specialities");
                    menu_layout.setVisibility(View.GONE);
                    menuView = 0;
                    str_type = "specialities";
                    GetTempleDetails get_Provider_details = new GetTempleDetails(context, str_temple_id, str_type, str_lang);
                    get_Provider_details.execute();

                }
            });


            menu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i("nn", "menu");
                    // finding X and Y co-ordinates
                    rl_choose_lang.setVisibility(View.GONE);
                    int cx = (menu_layout.getLeft() + menu_layout.getRight());
                    int cy = (menu_layout.getTop());

                    // to find  radius when icon is tapped for showing layout
                    int startradius = 0;
                    int endradius = Math.max(menu_layout.getWidth(), menu_layout.getHeight());

                    // performing circular reveal when icon will be tapped
                    Animator animator = ViewAnimationUtils.createCircularReveal(menu_layout, cx, cy, startradius, endradius);
                    animator.setInterpolator(new AccelerateDecelerateInterpolator());
                    animator.setDuration(400);
                    //reverse animation
                    // to find radius when icon is tapped again for hiding layout
                    //  starting radius will be the radius or the extent to which circular reveal animation is to be shown

                    int reverse_startradius = Math.max(menu_layout.getWidth(), menu_layout.getHeight());

                    //endradius will be zero
                    int reverse_endradius = 0;

                    // performing circular reveal for reverse animation
                    Animator animate = ViewAnimationUtils.createCircularReveal(menu_layout, cx, cy, reverse_startradius, reverse_endradius);

                    if (menuView == 0) {
                        menu_layout.setVisibility(View.VISIBLE);
                        animator.start();
                        menuView = 1;
                    } else {
                        menu_layout.setVisibility(View.VISIBLE);

                        // to hide layout on animation end
                        animate.addListener(new AnimatorListenerAdapter() {
                                                @Override
                                                public void onAnimationEnd(Animator animation) {
                                                    super.onAnimationEnd(animation);
                                                    menu_layout.setVisibility(View.GONE);
                                                    menuView = 0;
                                                }
                                            }
                        );
                        animate.start();
                    }
                }
            });
        } catch (Exception e) {

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

    @Override
    public void getTempleName(String str_temple_name, String str_temple_id) {
        Intent i = new Intent(TempleDetails.this, TempleDetails.class);
        i.putExtra("temple_name", str_temple_name);
        i.putExtra("temple_id", str_temple_id);
        startActivity(i);
    }

    @Override
    public void getTempleName(String str_temple_name, String str_temple_id, String str_temple_addr) {

    }

    @Override
    protected void onResume() {
        super.onResume();

        str_lang = GeneralData.str_lang;

        if (HomePage.prefs.getString("from", null) != null) {
            str_type = HomePage.prefs.getString("from", null);
            Log.i("MJ", "StrType : " + str_type);
        }
        GetTempleDetails get_Provider_details = new GetTempleDetails(context, str_temple_id, "about_temple", str_lang);
        get_Provider_details.execute();
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
            txt_nearby.setText("Nearby Visiting Places");

        } else if (str_lang.equalsIgnoreCase("ta")) {
            img_english.setBackgroundResource(R.drawable.white_dot);
            img_tamil.setBackgroundResource(R.drawable.orange_dot);
            txt_abt_temple.setText("கோவில் பற்றி");
            txt_dharshan.setText("தரிசன நேரம்");
            txt_history.setText("வரலாறு");
            txt_admin.setText("நிர்வாகம்");
            txt_festival.setText("திருவிழாக்கள்");
            txt_gallery.setText("காட்சியகம்");
            txt_loc.setText("இடம்");
            txt_art.setText("கலை மற்றும் கல்வெட்டுகள்");
            txt_special.setText("சிறப்பம்சங்கள்");
            txt_special.setTextSize(9);
            txt_nearby.setText("அருகாமையில் உள்ள ஸ்தலங்கள் பற்றி");

        }

    }

    private void createChild(final String temple_name, String temple_image, final String str_temple_id, String str_temple_dist, String str_lang) {


        LayoutInflater inflater = LayoutInflater.from(TempleDetails.this);

        final View view1 = inflater.inflate(R.layout.nearby_temple_content, null);

        TextView txt_temple_name = (TextView) view1.findViewById(R.id.txt_near_temple_name);
        TextView txt_temple_dist = (TextView) view1.findViewById(R.id.txt_near_temple_distance);
        ImageView img_temple_image = (ImageView) view1.findViewById(R.id.img_near_temple_image);
        txt_temple_name.setText(temple_name);
        if (str_lang.equalsIgnoreCase("ta")) {
            txt_temple_dist.setText("தொலைவு(கி.மீ): " + str_temple_dist);
        } else if (str_lang.equalsIgnoreCase("en")) {
            txt_temple_dist.setText("Distance(km): " + str_temple_dist);
        }

        imgLoader.DisplayImage(temple_image, img_temple_image);

        view1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(TempleDetails.this, TempleDetails.class);
                i.putExtra("temple_name", temple_name);
                i.putExtra("temple_id", str_temple_id);
                startActivity(i);
                finish();
            }
        });


        ll_nearby.addView(view1);

    }

    class GetTempleDetails extends AsyncTask {

        String temp_id = null;
        String sResponse = null;
        Context cont;
        String str_typ;
        String str_lan;

        public GetTempleDetails(Context context, String str_temp_id, String type, String str_lang) {

            this.cont = context;
            this.temp_id = str_temp_id;
            this.str_typ = type;
            this.str_lan = str_lang;

        }

        @Override
        protected void onPreExecute() {
            TempleDetails.this.runOnUiThread(new Runnable() {
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

                String requestURL = GeneralData.SERVER_IP + "api_response/" + str_type + "?temple_id=" + temp_id;

                if (str_lan.equalsIgnoreCase("ta")) {
                    requestURL = GeneralData.SERVER_IP + "api_response/" + str_type + "?temple_id=" + temp_id + "&language=ta";
                    //requestURL = GeneralData.SERVER_IP + "api_response/category?language=ta";
                } else if (str_lan.equalsIgnoreCase("en")) {
                    requestURL = GeneralData.SERVER_IP + "api_response/" + str_type + "?temple_id=" + temp_id;
                }
                //   String requestURL = GeneralData.SERVER_IP +"web_service/" + str_type ;
                Log.i("HI", "URL : " + requestURL);

                MultipartUtility multipart = new MultipartUtility(requestURL, charset);
                //  multipart.addFormField("temple_id", URLEncoder.encode(temp_id,"UTF-16"));

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


                ArrayList<TempleListBean> beanArrayList = new ArrayList<TempleListBean>();

                JSONObject jsobj = new JSONObject(sResponse);

                Log.i("HH", "strResp : " + sResponse);
                if (jsobj.getString("status").equalsIgnoreCase("true")) {
                    //JSONArray templesCategoriesArray = jsobj.getJSONArray("temple_details");

                    // if (templesCategoriesArray.length() > 0) {
                    //   for (int i = 0; i < templesCategoriesArray.length(); i++) {
                    TempleListBean templeListBean = new TempleListBean();
                    // JSONObject providersServiceJSONobject = templesCategoriesArray.getJSONObject(0);
                    text_content = (TextView) findViewById(R.id.text_content);


                    if (str_typ.equalsIgnoreCase("about_temple")) {

                        str_about_us = jsobj.getString("description");
                        str_typ = "about_temple";
                        String strimage = GeneralData.SERVER_IP + "uploads/temple/" + jsobj.getString("temple_image");
                        Log.i("SSD", "strimage : " + strimage);
                        text_content.setText(jsobj.getString("title"));
                        get_temp_name = jsobj.getString("temple_name");
                        temple_name.setText(get_temp_name);

                        imgLoader.DisplayImage(strimage, temple_image);

                        //temple_image.setImageBitmap(imgLoader.getBitmap(strimage));
                        //  contents.setText(Html.fromHtml("<body style=text-align:justify><p>" + str_about_us + "</p></body>"));
                        String str_abt = "<body style=text-align:justify><p style=font-size:13px;line-height:1.5;text-indent:30px;font-family:verdana,Lohit Tamil;>" + str_about_us + "</p></body>";
                        contents.getSettings().setJavaScriptEnabled(true);

                        WebSettings settings = contents.getSettings();
                        settings.setDefaultTextEncodingName("utf-8");

                        contents.loadData(str_abt, "text/html; charset=utf-8", "utf-8");


                        // contents.setText(Html.fromHtml("<p>" + str_about_us + "</p>"));

                    } else if (str_typ.equalsIgnoreCase("administration")) {
                        str_type = "administration";
                        String strimage = GeneralData.SERVER_IP + "uploads/temple/" + jsobj.getString("temple_image");
                        imgLoader.DisplayImage(strimage, temple_image);
                        text_content.setText(jsobj.getString("title"));
                        str_admini = jsobj.getString("description");

                        String str_admin = "<body style=text-align:justify><p style=font-size:13px;line-height:1.5;text-indent:30px;font-family:verdana,Lohit Tamil;>" + str_admini + "</p></body>";
                        contents.getSettings().setJavaScriptEnabled(true);

                        WebSettings settings = contents.getSettings();
                        settings.setDefaultTextEncodingName("utf-8");

                        contents.loadData(str_admin, "text/html; charset=utf-8", "utf-8");

                        //contents.setText(Html.fromHtml("<p>" + str_admini + "</p>"));
                        get_temp_name = jsobj.getString("temple_name");
                        temple_name.setText(get_temp_name);


                    } else if (str_typ.equalsIgnoreCase("dharsan_timing")) {
                        str_type = "dharsan_timing";
                        String strimage = GeneralData.SERVER_IP + "uploads/temple/" + jsobj.getString("temple_image");
                        imgLoader.DisplayImage(strimage, temple_image);
                        text_content.setText(jsobj.getString("title"));
                        str_admini = jsobj.getString("description");
                        //  contents.setText(Html.fromHtml("<p>" + str_admini + "</p>"));

                        String str_admin = "<body style=text-align:justify><p style=font-size:13px;line-height:1.5;text-indent:30px;font-family:verdana,Lohit Tamil;>" + str_admini + "</p></body>";
                        contents.getSettings().setJavaScriptEnabled(true);

                        WebSettings settings = contents.getSettings();
                        settings.setDefaultTextEncodingName("utf-8");

                        contents.loadData(str_admin, "text/html; charset=utf-8", "utf-8");


                        get_temp_name = jsobj.getString("temple_name");
                        temple_name.setText(get_temp_name);

                    } else if (str_typ.equalsIgnoreCase("history")) {
                        str_type = "history";
                        String strimage = GeneralData.SERVER_IP + "uploads/temple/" + jsobj.getString("temple_image");
                        imgLoader.DisplayImage(strimage, temple_image);
                        text_content.setText(jsobj.getString("title"));
                        str_admini = jsobj.getString("description");
                        // contents.setText(Html.fromHtml("<p>" + str_admini + "</p>"));
                        String str_admin = "<body style=text-align:justify><p style=font-size:13px;line-height:1.5;text-indent:30px;font-family:verdana,Lohit Tamil;>" + str_admini + "</p></body>";
                        contents.getSettings().setJavaScriptEnabled(true);

                        WebSettings settings = contents.getSettings();
                        settings.setDefaultTextEncodingName("utf-8");

                        contents.loadData(str_admin, "text/html; charset=utf-8", "utf-8");
                        get_temp_name = jsobj.getString("temple_name");
                        temple_name.setText(get_temp_name);

                    } else if (str_typ.equalsIgnoreCase("festivals")) {
                        str_type = "festivals";
                        String strimage = GeneralData.SERVER_IP + "uploads/temple/" + jsobj.getString("temple_image");
                        imgLoader.DisplayImage(strimage, temple_image);
                        text_content.setText(jsobj.getString("title"));
                        str_admini = jsobj.getString("description");
                        // contents.setText(Html.fromHtml("<p>" + str_admini + "</p>"));
                        String str_admin = "<body style=text-align:justify><p style=font-size:13px;line-height:1.5;text-indent:30px;font-family:verdana,Lohit Tamil;>" + str_admini + "</p></body>";
                        contents.getSettings().setJavaScriptEnabled(true);

                        WebSettings settings = contents.getSettings();
                        settings.setDefaultTextEncodingName("utf-8");

                        contents.loadData(str_admin, "text/html; charset=utf-8", "utf-8");
                        get_temp_name = jsobj.getString("temple_name");
                        temple_name.setText(get_temp_name);

                    } else if (str_typ.equalsIgnoreCase("art_inspiration")) {
                        str_type = "art_inspiration";
                        String strimage = GeneralData.SERVER_IP + "uploads/temple/" + jsobj.getString("temple_image");
                        imgLoader.DisplayImage(strimage, temple_image);
                        text_content.setText(jsobj.getString("title"));
                        str_admini = jsobj.getString("description");
                        //contents.setText(Html.fromHtml("<p>" + str_admini + "</p>"));
                        String str_admin = "<body style=text-align:justify><p style=font-size:13px;line-height:1.5;text-indent:30px;font-family:verdana,Lohit Tamil;>" + str_admini + "</p></body>";
                        contents.getSettings().setJavaScriptEnabled(true);

                        WebSettings settings = contents.getSettings();
                        settings.setDefaultTextEncodingName("utf-8");

                        contents.loadData(str_admin, "text/html; charset=utf-8", "utf-8");
                        get_temp_name = jsobj.getString("temple_name");
                        temple_name.setText(get_temp_name);

                    } else if (str_typ.equalsIgnoreCase("specialities")) {
                        str_type = "specialities";
                        String strimage = GeneralData.SERVER_IP + "uploads/temple/" + jsobj.getString("temple_image");
                        imgLoader.DisplayImage(strimage, temple_image);
                        text_content.setText(jsobj.getString("title"));
                        str_admini = jsobj.getString("description");
                        // contents.setText(Html.fromHtml("<p>" + str_admini + "</p>"));
                        String str_admin = "<body style=text-align:justify><p style=font-size:13px;line-height:1.5;text-indent:30px;font-family:verdana,Lohit Tamil;>" + str_admini + "</p></body>";
                        contents.getSettings().setJavaScriptEnabled(true);

                        WebSettings settings = contents.getSettings();
                        settings.setDefaultTextEncodingName("utf-8");

                        contents.loadData(str_admin, "text/html; charset=utf-8", "utf-8");
                        get_temp_name = jsobj.getString("temple_name");
                        temple_name.setText(get_temp_name);

                    } else if (str_typ.equalsIgnoreCase("gallery")) {
                        str_type = "gallery";
                        // text_content.setText(jsobj.getString("title"));
                        String str_admin = "<body style=text-align:justify><p style=font-size:13px;line-height:1.5;text-indent:30px;font-family:verdana,Lohit Tamil;>" + str_admini + "</p></body>";
                        contents.getSettings().setJavaScriptEnabled(true);

                        WebSettings settings = contents.getSettings();
                        settings.setDefaultTextEncodingName("utf-8");

                        contents.loadData(str_admin, "text/html; charset=utf-8", "utf-8");

                        SharedPreferences.Editor editor = SplashActivity.preferences.edit();
                        editor.putString("temple_id_det", temp_id);
                        editor.putString("temple_name_det", str_temple_name);
                        editor.putString("temple_from_det", "about_temple");
                        editor.commit();


                        Intent i = new Intent(TempleDetails.this, GalleryVideo.class);
                        i.putExtra("temple_id", temp_id);
                        i.putExtra("temple_name", str_temple_name);
                        i.putExtra("temp_from", "about_temple");

                       /* i.putExtra("temp_lat", jsobj.getString("lattitude"));
                        i.putExtra("temp_long", jsobj.getString("longitude"));
                        i.putExtra("temp_content", jsobj.getString("location"));
                        i.putExtra(("temp_address"), jsobj.getString("address"));*/
                        startActivity(i);

                    } else if (str_typ.equalsIgnoreCase("location")) {
                        str_type = "location";
                        text_content.setText(jsobj.getString("title"));
                        String str_admin = "<body style=text-align:justify><p style=font-size:13px;line-height:1.5;text-indent:30px;font-family:verdana,Lohit Tamil;>" + str_admini + "</p></body>";
                        contents.getSettings().setJavaScriptEnabled(true);

                        WebSettings settings = contents.getSettings();
                        settings.setDefaultTextEncodingName("utf-8");

                        contents.loadData(str_admin, "text/html; charset=utf-8", "utf-8");
                        SharedPreferences.Editor editor = SplashActivity.preferences.edit();
                        editor.putString("temple_id_det", jsobj.getString("temple_id"));
                        editor.putString("temple_name_det", jsobj.getString("temple_name"));
                        editor.putString("temple_lat_det", jsobj.getString("lattitude"));
                        editor.putString("temple_long_det", jsobj.getString("longitude"));
                        editor.putString("temple_addr_det", jsobj.getString("address"));
                        editor.putString("temple_content_det", jsobj.getString("location"));
                        editor.putString("temple_from_det", "about_temple");
                        editor.commit();
                        Intent i = new Intent(TempleDetails.this, TempleLocationNew.class);
                        i.putExtra("temp_lat", jsobj.getString("lattitude"));
                        i.putExtra("temp_long", jsobj.getString("longitude"));
                        i.putExtra("temp_content", jsobj.getString("location"));
                        i.putExtra("temple_name", jsobj.getString("temple_name"));
                        i.putExtra(("temple_id"), jsobj.getString("temple_id"));
                        i.putExtra(("temp_address"), jsobj.getString("address"));
                        i.putExtra("temp_from", "about_temple");
                        startActivity(i);
                       /* str_admini = jsobj.getString("location");
                        contents.setText(str_admini);*/
                    }

                            /*templeListBean.setTemple_id(providersServiceJSONobject.getString("temple_id"));
                            templeListBean.setTemple_name(providersServiceJSONobject.getString("temple_name"));
                            beanArrayList.add(templeListBean);*/
                    //  }
                } /*else {
                        txt_empty = (TextView) findViewById(R.id.txt_empty);
                        txt_empty.setVisibility(View.VISIBLE);
                    }*/

                //}


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
            TempleDetails.this.runOnUiThread(new Runnable() {
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
            TempleDetails.this.runOnUiThread(new Runnable() {
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
                Intent i = new Intent(TempleDetails.this, TempleSearchDetails.class);
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

    class GetNearbyTemples extends AsyncTask {

        String temp_id = null;
        String sResponse = null;
        String str_lan = null;
        Context cont;

        public GetNearbyTemples(Context context, String str_temple_id, String str_lang) {

            this.cont = context;
            this.temp_id = str_temple_id;
            this.str_lan = str_lang;

        }

        @Override
        protected void onPreExecute() {
            TempleDetails.this.runOnUiThread(new Runnable() {
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
                String requestURL = GeneralData.SERVER_IP + "api_response/nearby_temples?temple_id=" + temp_id;
                if (str_lan.equalsIgnoreCase("ta")) {
                    requestURL = GeneralData.SERVER_IP + "api_response/nearby_temples?temple_id=" + temp_id + "&language=ta";
                } else if (str_lan.equalsIgnoreCase("en")) {
                    requestURL = GeneralData.SERVER_IP + "api_response/nearby_temples?temple_id=" + temp_id;
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
                    JSONArray templesCategoriesArray = jsobj.getJSONArray("temple_details");
                    if (ll_nearby.getChildCount() > 0) {
                        ll_nearby.removeAllViews();
                    }
                    for (int i = 0; i < templesCategoriesArray.length(); i++) {
                        JSONObject providersServiceJSONobject = templesCategoriesArray.getJSONObject(i);
                        String str_tmp_name = providersServiceJSONobject.getString("temple_name");
                        String str_tmp_id = providersServiceJSONobject.getString("temple_id");
                        String str_tmp_img = GeneralData.SERVER_IP + "uploads/temple/" + providersServiceJSONobject.getString("temple_image");
                        String str_tmp_dist = providersServiceJSONobject.getString("distance");
                        createChild(str_tmp_name, str_tmp_img, str_tmp_id, str_tmp_dist, str_lan);
                    }

                 /*   if (templesCategoriesArray.length() > 0) {
                        for (int i = 0; i < templesCategoriesArray.length(); i++) {
                            TempleListBean templeListBean = new TempleListBean();
                            JSONObject providersServiceJSONobject = templesCategoriesArray.getJSONObject(i);
                            templeListBean.setTemple_name(providersServiceJSONobject.getString("temple_name"));
                            templeListBean.setTemple_image(GeneralData.SERVER_IP + "uploads/temple/" + providersServiceJSONobject.getString("temple_image"));
                            beanArrayList.add(templeListBean);
                        }
                    } else {
                        txt_error_msg.setVisibility(View.VISIBLE);
                    }
                    mAdapter = new NearbyTempleListAdapter(cont, beanArrayList, (TempleList) cont);
                    near_temple_list.setAdapter(mAdapter);*/

                }


            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}
