package com.aryvart.templeopedia;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.aryvart.templeopedia.Interface.TempleList;
import com.aryvart.templeopedia.adapter.SpinnerAdapter;
import com.aryvart.templeopedia.adapter.TempleListAdapter;
import com.aryvart.templeopedia.adapter.TempleSearchDetailAdapter;
import com.aryvart.templeopedia.bean.TempleListBean;
import com.aryvart.templeopedia.genericclasses.GeneralData;
import com.aryvart.templeopedia.genericclasses.MultipartUtility;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by android1 on 10/8/16.
 */
public class TempleSearchDetails extends Activity implements TempleList {
    String str_getJson;
    ProgressDialog dialog;
    Context ctx;
    TempleSearchDetailAdapter mAdapter;
    ListView temple_list;
    ImageView img_select_language, img_english, img_tamil;
    LinearLayout ll_tamil, ll_english, ll_content;
    RelativeLayout rl_choose_lang;
    TextView txt_temple_name, txt_search_title, txt_search_count, txt_empty;
    ImageView img_back, img_search, img_home;
    View itemView1;
    int n_count = 0;
    Button btn_search;
    String strAreaId, strStateId, strCityId, strFestivalId, strDietyId, str_lang;
    Spinner spin_area, spin_state, spin_city, spin_deity, spin_festival;
    String str_ProtocolTyp, str_DietyId, str_FestivalId, str_CityId, str_StateId, str_AreaId;
    AlertDialog altDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.temple_search_details);

        GeneralData.context = this;

        dialog = new ProgressDialog(TempleSearchDetails.this);
        temple_list = (ListView) findViewById(R.id.temple_list);
        ctx = this;
        txt_search_title = (TextView) findViewById(R.id.txt_search_title);
        ll_english = (LinearLayout) findViewById(R.id.llay_english);
        ll_tamil = (LinearLayout) findViewById(R.id.llay_tamil);
        temple_list = (ListView) findViewById(R.id.temple_list);
        img_english = (ImageView) findViewById(R.id.img_english);
        img_tamil = (ImageView) findViewById(R.id.img_tamil);
        txt_temple_name = (TextView) findViewById(R.id.txt_temple_name);
        img_back = (ImageView) findViewById(R.id.back);
        img_search = (ImageView) findViewById(R.id.search);
        img_home = (ImageView) findViewById(R.id.home);
        txt_empty = (TextView) findViewById(R.id.txt_empty);
        txt_search_count = (TextView) findViewById(R.id.txt_search_count);
        img_select_language = (ImageView) findViewById(R.id.language);
        rl_choose_lang = (RelativeLayout) findViewById(R.id.rl_choose_lang);
        ll_content = (LinearLayout) findViewById(R.id.content_lay);
        str_lang = String.valueOf(GeneralData.str_lang);


        str_ProtocolTyp = getIntent().getStringExtra("str_ProtocolTyp");
        str_getJson = getIntent().getStringExtra("search_detail_json");

        str_AreaId = getIntent().getStringExtra("strCountryId");
        str_StateId = getIntent().getStringExtra("strStateId");
        str_CityId = getIntent().getStringExtra("strCityId");
        str_FestivalId = getIntent().getStringExtra("strFestivalId");
        str_DietyId = getIntent().getStringExtra("strDietyId");


        if (str_lang.equalsIgnoreCase("ta")) {
            txt_search_title.setText("தேடுதலின் விபரம்");
            img_english.setBackgroundResource(R.drawable.white_dot);
            img_tamil.setBackgroundResource(R.drawable.orange_dot);

        } else if (str_lang.equalsIgnoreCase("en")) {
            txt_search_title.setText("Search Results");
            img_english.setBackgroundResource(R.drawable.orange_dot);
            img_tamil.setBackgroundResource(R.drawable.white_dot);
        }
        img_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TempleSearchDetails.this, HomePage.class));
                finish();
                finishAffinity();
            }
        });
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
        img_select_language.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                final AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
                builder.setCancelable(true);
                itemView1 = LayoutInflater.from(ctx)
                        .inflate(R.layout.search_popup, null);
                altDialog = builder.create();
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
                GetSearchResults getSearchResults = new GetSearchResults(ctx, "state", spin_state, str_lang);
                getSearchResults.execute();
                GetSearchResults getSearchResults2 = new GetSearchResults(ctx, "festival", spin_festival, str_lang);
                getSearchResults2.execute();
                GetSearchResults getSearchResults1 = new GetSearchResults(ctx, "diety", spin_deity, str_lang);
                getSearchResults1.execute();
                Log.i("BG", "kkkkkkkkkkk :3 ");
                btn_search.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (spin_state.getSelectedItemPosition() > 0 && spin_city.getSelectedItemPosition() == 0) {

                            Log.i("VB", "1");
                            Log.i("str_lang", str_lang);
                            GetSearchDetails getSearchResults = new GetSearchDetails(ctx, "search_temple", "nscfd", str_lang);
                            getSearchResults.execute();
                            altDialog.dismiss();
                        } else if (spin_state.getSelectedItemPosition() > 0 && spin_city.getSelectedItemPosition() > 0 && spin_area.getSelectedItemPosition() == 0 && spin_festival.getSelectedItemPosition() == 0 && spin_deity.getSelectedItemPosition() == 0) {
                            Log.i("VB", "2");

                            GetSearchDetails getSearchResults = new GetSearchDetails(ctx, "search_temple", "ncfd", str_lang);
                            getSearchResults.execute();
                            altDialog.dismiss();
                        } else if (spin_state.getSelectedItemPosition() > 0 && spin_city.getSelectedItemPosition() > 0 && spin_area.getSelectedItemPosition() > 0 && spin_festival.getSelectedItemPosition() == 0 && spin_deity.getSelectedItemPosition() == 0) {
                            Log.i("VB", "3");
                            GetSearchDetails getSearchResults = new GetSearchDetails(ctx, "search_temple", "nfd", str_lang);
                            getSearchResults.execute();
                            altDialog.dismiss();
                        } else if (spin_state.getSelectedItemPosition() > 0 && spin_city.getSelectedItemPosition() > 0 && spin_area.getSelectedItemPosition() > 0 && spin_festival.getSelectedItemPosition() > 0 && spin_deity.getSelectedItemPosition() == 0) {
                            Log.i("VB", "4");
                            GetSearchDetails getSearchResults = new GetSearchDetails(ctx, "search_temple", "nd", str_lang);
                            getSearchResults.execute();
                            altDialog.dismiss();
                        } else if (spin_state.getSelectedItemPosition() > 0 && spin_city.getSelectedItemPosition() > 0 && spin_area.getSelectedItemPosition() > 0 && spin_festival.getSelectedItemPosition() > 0 && spin_deity.getSelectedItemPosition() > 0) {
                            Log.i("VB", "5");
                            GetSearchDetails getSearchResults = new GetSearchDetails(ctx, "search_temple", "n", str_lang);
                            getSearchResults.execute();
                            altDialog.dismiss();
                        }

                        //For festival and diety
                        else if (spin_state.getSelectedItemPosition() == 0 && spin_city.getSelectedItemPosition() == 0 && spin_area.getSelectedItemPosition() == 0 && spin_festival.getSelectedItemPosition() > 0 && spin_deity.getSelectedItemPosition() == 0) {
                            Log.i("VB", "6");
                            GetSearchDetails getSearchResults = new GetSearchDetails(ctx, "search_temple", "f", str_lang);
                            getSearchResults.execute();
                            altDialog.dismiss();
                        } else if (spin_state.getSelectedItemPosition() == 0 && spin_city.getSelectedItemPosition() == 0 && spin_area.getSelectedItemPosition() == 0 && spin_festival.getSelectedItemPosition() == 0 && spin_deity.getSelectedItemPosition() > 0) {
                            Log.i("VB", "7");
                            GetSearchDetails getSearchResults = new GetSearchDetails(ctx, "search_temple", "d", str_lang);
                            getSearchResults.execute();
                            altDialog.dismiss();
                        } else {
                           /* GetSearchDetails getSearchResults = new GetSearchDetails(ctx, "search_temple", "empty_search", str_lang);
                            getSearchResults.execute();
                            Log.i("VB", "8");*/
                            if (str_lang.equalsIgnoreCase("ta")) {
                                Toast.makeText(TempleSearchDetails.this, "குறைந்தது ஒரு துறை தேர்வு செய்க", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(TempleSearchDetails.this, "Select atleast one field", Toast.LENGTH_SHORT).show();
                            }
                        }


                    }
                });


                spin_city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        if (spin_city.getSelectedItemPosition() != 0) {

                            TempleListBean mSelected = (TempleListBean) parent.getItemAtPosition(position);
                            strCityId = mSelected.getTemple_id();

                            GetSearchResults getSearchResults = new GetSearchResults(ctx, "area", spin_area, strStateId, strCityId, str_lang);
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


                            GetSearchResults getSearchResults = new GetSearchResults(ctx, "city", spin_city, strStateId, str_lang);
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
                txt_search_title.setText("Search Results");
                GetSearchDetail get_Provider_details = new GetSearchDetail(ctx, str_ProtocolTyp, "en");
                get_Provider_details.execute();
                str_lang = String.valueOf(GeneralData.str_lang);

                rl_choose_lang.setVisibility(View.GONE);


                itemView1 = LayoutInflater.from(ctx)
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


                GetSearchDetail get_Provider_details = new GetSearchDetail(ctx, str_ProtocolTyp, "ta");
                get_Provider_details.execute();
                GeneralData.str_lang = "ta";
                txt_search_title.setText("தேடல் முடிவுகள்");
                str_lang = String.valueOf(GeneralData.str_lang);
                rl_choose_lang.setVisibility(View.GONE);

                itemView1 = LayoutInflater.from(ctx)
                        .inflate(R.layout.search_popup, null);

                spin_area = (Spinner) itemView1.findViewById(R.id.spinner_area);
                spin_state = (Spinner) itemView1.findViewById(R.id.spinner_state);
                spin_city = (Spinner) itemView1.findViewById(R.id.spinner_city);
                spin_festival = (Spinner) itemView1.findViewById(R.id.spinner_festival);
                spin_deity = (Spinner) itemView1.findViewById(R.id.spinner_diety);

                loadSpinnerBasedLang(1);
            }
        });
        GetSearchDetail get_Provider_details = new GetSearchDetail(ctx, str_ProtocolTyp, str_lang);
        get_Provider_details.execute();
       /* try {
            //Error Fixed ...............................
            //Please check.....................


            ArrayList<TempleListBean> beanArrayList = new ArrayList<TempleListBean>();

            JSONObject jsobj = new JSONObject(str_getJson);

            Log.i("HH", "strResp : " + str_getJson);
            if (jsobj.getString("status").equalsIgnoreCase("true")) {
                JSONArray templesCategoriesArray = jsobj.getJSONArray("temples");

                if (templesCategoriesArray.length() > 0) {
                    for (int i = 0; i < templesCategoriesArray.length(); i++) {
                        TempleListBean templeListBean = new TempleListBean();
                        JSONObject providersServiceJSONobject = templesCategoriesArray.getJSONObject(i);
                        templeListBean.setTemple_id(providersServiceJSONobject.getString("temple_id"));
                        templeListBean.setTemple_name(providersServiceJSONobject.getString("temple_name"));
                        templeListBean.setTemple_address(providersServiceJSONobject.getString("temple_address"));
                        beanArrayList.add(templeListBean);
                    }
                }

                mAdapter = new TempleSearchDetailAdapter(ctx, beanArrayList, (TempleList) ctx);
                temple_list.setAdapter(mAdapter);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }*/

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
        spin_city.setAdapter(new SpinnerAdapter(ctx, R.id.spin_text, beanArrayS));
        spin_area.setAdapter(new SpinnerAdapter(ctx, R.id.spin_text, beanArrayC));
        spin_festival.setAdapter(new SpinnerAdapter(ctx, R.id.spin_text, beanArrayF));
        spin_deity.setAdapter(new SpinnerAdapter(ctx, R.id.spin_text, beanArrayD));
    }

    @Override
    protected void onResume() {
        super.onResume();

        str_lang = GeneralData.str_lang;

        GetSearchDetail get_Provider_details = new GetSearchDetail(ctx, str_ProtocolTyp, str_lang);
        get_Provider_details.execute();

        if (str_lang.equalsIgnoreCase("ta")) {
            img_english.setBackgroundResource(R.drawable.white_dot);
            img_tamil.setBackgroundResource(R.drawable.orange_dot);
            txt_search_title.setText("தேடல் முடிவுகள்");
        } else {
            img_english.setBackgroundResource(R.drawable.orange_dot);
            img_tamil.setBackgroundResource(R.drawable.white_dot);
            txt_search_title.setText("Search Results");
        }
    }

    @Override
    public void getTempleName(String str_temple_name, String str_temple_id) {

    }

    @Override
    public void getTempleName(String str_temple_name, String str_temple_id, String str_temple_addr) {
        Intent i = new Intent(TempleSearchDetails.this, TempleDetails.class);
        i.putExtra("temple_name", str_temple_name);
        i.putExtra("temple_id", str_temple_id);
        startActivity(i);
    }

    class GetSearchDetail extends AsyncTask {

        String user_id = null;
        String sResponse = null;
        Context cont;
        String str_typ, strProtocolTyp, str_lan;
        //String nCountry_Id, nState_Id, nCity_Id, nFestival_Id, nDietyId;


        public GetSearchDetail(Context context, String strProtocol, String str_lang) {

            this.cont = context;
            this.strProtocolTyp = strProtocol;
            this.str_lan = str_lang;


        }

        @Override
        protected void onPreExecute() {
            TempleSearchDetails.this.runOnUiThread(new Runnable() {
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
                String requestURL = GeneralData.SERVER_IP + "api_response/search_temple";
                if (str_lan.equalsIgnoreCase("ta")) {
                    requestURL = GeneralData.SERVER_IP + "api_response/search_temple?language=ta";
                } else if (str_lan.equalsIgnoreCase("en")) {
                    requestURL = GeneralData.SERVER_IP + "api_response/search_temple";
                }

                //country_id, state_id, city_id, diety_id, festival_id

                Log.i("BG", "state_id :" + str_StateId);
                //   Log.i("BG", "strProtocolTyp :" + strProtocolTyp);
                Log.i("BG", "URL :" + requestURL);
               /* if (strProtocolTyp.equalsIgnoreCase("nscfd") || strProtocolTyp.equalsIgnoreCase("ncfd") || strProtocolTyp.equalsIgnoreCase("nfd") || strProtocolTyp.equalsIgnoreCase("nd") || strProtocolTyp.equalsIgnoreCase("n") || strProtocolTyp.equalsIgnoreCase("f") || strProtocolTyp.equalsIgnoreCase("d")) {
                    if (str_lan.equalsIgnoreCase("ta")) {
                        requestURL += "&language=ta";
                        Log.i("RA", requestURL);
                    }
                }*/
                Log.i("BG", "str_lan :" + str_lan);
                if (strProtocolTyp.equalsIgnoreCase("nscfd")) {
                    if (str_lan.equalsIgnoreCase("ta")) {
                        requestURL = GeneralData.SERVER_IP + "api_response/search_temple?state_id=" + str_StateId;
                        requestURL += "&language=ta";
                        Log.i("RA", requestURL);
                    } else {
                        requestURL = GeneralData.SERVER_IP + "api_response/search_temple?state_id=" + str_StateId;
                    }

                    Log.i("BG", "state_id :" + str_StateId);
                    Log.i("BG", "strProtocolTyp :" + strProtocolTyp);
                    Log.i("BG", "URL :" + requestURL);
                    //  multipart.addFormField("country_id", strCountryId);


                } else if (strProtocolTyp.equalsIgnoreCase("ncfd")) {
                    if (str_lan.equalsIgnoreCase("ta")) {
                        requestURL = GeneralData.SERVER_IP + "api_response/search_temple?state_id=" + str_StateId + "&city_id=" + str_CityId;
                        requestURL += "&language=ta";
                        Log.i("RA", requestURL);
                    } else {
                        requestURL = GeneralData.SERVER_IP + "api_response/search_temple?state_id=" + str_StateId + "&city_id=" + str_CityId;

                    }
                    Log.i("BG", "country_id :" + str_AreaId);
                    Log.i("BG", "strProtocolTyp :" + strProtocolTyp);
                    Log.i("BG", "URL :" + requestURL);
                    /*multipart.addFormField("country_id", strCountryId);
                    multipart.addFormField("state_id", strStateId);*/

                } else if (strProtocolTyp.equalsIgnoreCase("nfd")) {
                    if (str_lan.equalsIgnoreCase("ta")) {
                        requestURL = GeneralData.SERVER_IP + "api_response/search_temple?state_id=" + str_StateId + "&city_id=" + str_CityId + "&area_id=" + str_AreaId;
                        requestURL += "&language=ta";
                        Log.i("RA", requestURL);
                    } else {
                        requestURL = GeneralData.SERVER_IP + "api_response/search_temple?state_id=" + str_StateId + "&city_id=" + str_CityId + "&area_id=" + str_AreaId;

                    }
                    Log.i("BG", "country_id :" + str_AreaId);
                    Log.i("BG", "strProtocolTyp :" + strProtocolTyp);
                    Log.i("BG", "URL :" + requestURL);
                   /* multipart.addFormField("country_id", strCountryId);
                    multipart.addFormField("state_id", strStateId);

                    multipart.addFormField("city_id", strCityId);*/


                } else if (strProtocolTyp.equalsIgnoreCase("nd")) {
                    if (str_lan.equalsIgnoreCase("ta")) {
                        requestURL = GeneralData.SERVER_IP + "api_response/search_temple?state_id=" + str_StateId + "&city_id=" + str_CityId + "&area_id=" + str_AreaId + "&festival_id=" + str_FestivalId;
                        requestURL += "&language=ta";
                        Log.i("RA", requestURL);
                    } else {
                        requestURL = GeneralData.SERVER_IP + "api_response/search_temple?state_id=" + str_StateId + "&city_id=" + str_CityId + "&area_id=" + str_AreaId + "&festival_id=" + str_FestivalId;

                    }
                    Log.i("BG", "country_id :" + str_AreaId);
                    Log.i("BG", "strProtocolTyp :" + strProtocolTyp);
                    Log.i("BG", "URL :" + requestURL);
                   /* multipart.addFormField("country_id", strCountryId);
                    multipart.addFormField("state_id", "" + strStateId);

                    multipart.addFormField("city_id", strCityId);
                    multipart.addFormField("festival_id", strFestivalId);*/


                } else if (strProtocolTyp.equalsIgnoreCase("n")) {
                    if (str_lan.equalsIgnoreCase("ta")) {
                        requestURL = GeneralData.SERVER_IP + "api_response/search_temple?state_id=" + str_StateId + "&city_id=" + str_CityId + "&area_id=" + str_AreaId + "&festival_id=" + str_FestivalId + "&diety_id=" + str_DietyId;
                        requestURL += "&language=ta";
                        Log.i("RA", requestURL);
                    } else {
                        requestURL = GeneralData.SERVER_IP + "api_response/search_temple?state_id=" + str_StateId + "&city_id=" + str_CityId + "&area_id=" + str_AreaId + "&festival_id=" + str_FestivalId + "&diety_id=" + str_DietyId;

                    }
                    Log.i("BG", "country_id :" + str_AreaId);
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
                        requestURL = GeneralData.SERVER_IP + "api_response/search_temple?festival_id=" + str_FestivalId;
                        requestURL += "&language=ta";
                        Log.i("RA", requestURL);
                    } else {
                        requestURL = GeneralData.SERVER_IP + "api_response/search_temple?festival_id=" + str_FestivalId;

                    }
                    Log.i("BG", "country_id :" + str_AreaId);
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
                        requestURL = GeneralData.SERVER_IP + "api_response/search_temple?diety_id=" + str_DietyId;
                        requestURL += "&language=ta";
                        Log.i("RA", requestURL);
                    } else {
                        requestURL = GeneralData.SERVER_IP + "api_response/search_temple?diety_id=" + str_DietyId;

                    }
                    Log.i("BG", "country_id :" + str_AreaId);
                    Log.i("BG", "diety_id :" + str_DietyId);
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
                        requestURL += "?language=ta";
                        Log.i("RA", requestURL);
                    } else {
                        requestURL = GeneralData.SERVER_IP + "api_response/search_temple";

                    }
                    Log.i("BG", "country_id :" + strAreaId);

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
                ArrayList<TempleListBean> beanArrayList = new ArrayList<TempleListBean>();

                JSONObject jsobj = new JSONObject(sResponse);

                Log.i("HH", "strResp : " + sResponse);
                if (jsobj.getString("status").equalsIgnoreCase("true")) {
                    txt_empty.setVisibility(View.GONE);
                    txt_search_title.setVisibility(View.VISIBLE);
                    txt_search_count.setVisibility(View.VISIBLE);
                    temple_list.setVisibility(View.VISIBLE);
                    JSONArray templesCategoriesArray = jsobj.getJSONArray("temples");

                    if (templesCategoriesArray.length() > 0) {
                        for (int i = 0; i < templesCategoriesArray.length(); i++) {
                            TempleListBean templeListBean = new TempleListBean();
                            JSONObject providersServiceJSONobject = templesCategoriesArray.getJSONObject(i);
                            templeListBean.setTemple_id(providersServiceJSONobject.getString("temple_id"));
                            templeListBean.setTemple_name(providersServiceJSONobject.getString("temple_name"));
                            templeListBean.setTemple_address(providersServiceJSONobject.getString("temple_address"));

                            beanArrayList.add(templeListBean);
                        }
                    }

                    String str_search_count = jsobj.getString("count");
                    txt_search_count.setText(str_search_count);
                    if (str_lang.equalsIgnoreCase("ta")) {
                        txt_search_title.setText("தேடுதலின் விபரம்");

                    } else if (str_lang.equalsIgnoreCase("en")) {
                        txt_search_title.setText("Search Results");

                    }
                    mAdapter = new TempleSearchDetailAdapter(ctx, beanArrayList, (TempleList) ctx);
                    temple_list.setAdapter(mAdapter);
                } else if (jsobj.getString("status").equalsIgnoreCase("false")) {
                    txt_search_count.setVisibility(View.GONE);
                    txt_empty.setVisibility(View.VISIBLE);
                    temple_list.setVisibility(View.GONE);
                    txt_search_title.setVisibility(View.GONE);
                    txt_empty.setText(jsobj.getString("error"));

                }


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
            TempleSearchDetails.this.runOnUiThread(new Runnable() {
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

                    SpinnerAdapter adapter = new SpinnerAdapter(ctx, R.id.spin_text, beanArrayList);
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
            TempleSearchDetails.this.runOnUiThread(new Runnable() {
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
                ArrayList<TempleListBean> beanArrayList = new ArrayList<TempleListBean>();

                JSONObject jsobj = new JSONObject(sResponse);

                Log.i("HH", "strResp : " + sResponse);

                if (str_lan.equalsIgnoreCase("ta")) {
                    txt_search_title.setText("தேடுதலின் விபரம்");

                } else if (str_lan.equalsIgnoreCase("en")) {
                    txt_search_title.setText("Search Results");

                }
                if (jsobj.getString("status").equalsIgnoreCase("true")) {
                    txt_empty.setVisibility(View.GONE);
                    temple_list.setVisibility(View.VISIBLE);
                    txt_search_title.setVisibility(View.VISIBLE);
                    txt_search_count.setVisibility(View.VISIBLE);
                    JSONArray templesCategoriesArray = jsobj.getJSONArray("temples");

                    if (templesCategoriesArray.length() > 0) {
                        for (int i = 0; i < templesCategoriesArray.length(); i++) {
                            TempleListBean templeListBean = new TempleListBean();
                            JSONObject providersServiceJSONobject = templesCategoriesArray.getJSONObject(i);
                            templeListBean.setTemple_id(providersServiceJSONobject.getString("temple_id"));
                            templeListBean.setTemple_name(providersServiceJSONobject.getString("temple_name"));
                            templeListBean.setTemple_address(providersServiceJSONobject.getString("temple_address"));
                            String str_search_count = jsobj.getString("count");
                            txt_search_count.setText(str_search_count);
                            beanArrayList.add(templeListBean);
                        }
                    }

                    mAdapter = new TempleSearchDetailAdapter(ctx, beanArrayList, (TempleList) ctx);
                    temple_list.setAdapter(mAdapter);
                } else if (jsobj.getString("status").equalsIgnoreCase("false")) {
                    txt_search_count.setVisibility(View.GONE);
                    txt_empty.setVisibility(View.VISIBLE);
                    temple_list.setVisibility(View.GONE);
                    txt_search_title.setVisibility(View.GONE);
                    txt_empty.setText(jsobj.getString("error"));
                }


            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
