package com.aryvart.templeopedia;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
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
import com.aryvart.templeopedia.bean.TempleListBean;
import com.aryvart.templeopedia.genericclasses.ConnectionService;
import com.aryvart.templeopedia.genericclasses.GeneralData;
import com.aryvart.templeopedia.genericclasses.MultipartUtility;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by android1 on 1/8/16.
 */
public class HomePage extends AppCompatActivity implements TempleList {

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.INTERNET, Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.CALL_PHONE,
            Manifest.permission.CAMERA, Manifest.permission.SEND_SMS, Manifest.permission.ACCESS_WIFI_STATE
    };
    ProgressDialog dialog;
    Context ctx;
    GeneralData gD;
    TextView txt_title_list;
    TempleListAdapter mAdapter;
    ListView temple_list;
    ImageView img_select_language, img_english, img_tamil, img_search;
    LinearLayout ll_tamil, ll_english, ll_content;
    RelativeLayout rl_choose_lang;
    TextView txt_empty;
    View itemView1;
    int n_count = 0;
    Button btn_search;
    Spinner spin_area, spin_state, spin_city, spin_deity, spin_festival;
    String strAreaId, strStateId, strCityId, strFestivalId, strDietyId;
    AlertDialog altDialog;
    TextView txt_search_title;
    String str_lang;
    GeneralData generalData;
    static SharedPreferences prefs;

    //persmission method.
    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have read or write permission
        int writePermission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int readPermission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE);

        int internetPermission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.INTERNET);
        int access_network_Permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_NETWORK_STATE);
        int access_fine_loc_Permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION);
        int access_coarse_loc_Permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION);
        int callPermission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.CALL_PHONE);
        int cameraPermission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.CAMERA);
        int smsPermission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.SEND_SMS);
        int wifiPermission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_WIFI_STATE);

        if (writePermission != PackageManager.PERMISSION_GRANTED || readPermission != PackageManager.PERMISSION_GRANTED || internetPermission != PackageManager.PERMISSION_GRANTED ||
                access_network_Permission != PackageManager.PERMISSION_GRANTED ||
                access_fine_loc_Permission != PackageManager.PERMISSION_GRANTED ||
                access_coarse_loc_Permission != PackageManager.PERMISSION_GRANTED ||
                callPermission != PackageManager.PERMISSION_GRANTED ||
                cameraPermission != PackageManager.PERMISSION_GRANTED ||
                smsPermission != PackageManager.PERMISSION_GRANTED ||
                wifiPermission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);


        dialog = new ProgressDialog(HomePage.this);
        ctx = this;
        GeneralData.context = this;


        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);


        prefs = getSharedPreferences("Prefs", Context.MODE_PRIVATE);

        txt_title_list = (TextView) findViewById(R.id.title_list);
        ll_english = (LinearLayout) findViewById(R.id.llay_english);
        ll_tamil = (LinearLayout) findViewById(R.id.llay_tamil);
        ll_content = (LinearLayout) findViewById(R.id.content_lay);
        temple_list = (ListView) findViewById(R.id.temple_list);
        img_english = (ImageView) findViewById(R.id.img_english);
        img_tamil = (ImageView) findViewById(R.id.img_tamil);
        txt_empty = (TextView) findViewById(R.id.txt_empty);
        img_select_language = (ImageView) findViewById(R.id.language);
        rl_choose_lang = (RelativeLayout) findViewById(R.id.rl_choose_lang);
        img_search = (ImageView) findViewById(R.id.searchVal);


        str_lang = GeneralData.str_lang;

        generalData = new GeneralData(ctx);
        Log.i("ZZZV", "Language : " + str_lang);


        ll_content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rl_choose_lang.setVisibility(View.GONE);
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
                            if (str_lang.equalsIgnoreCase("ta")) {
                                Toast.makeText(HomePage.this, "குறைந்தது ஒரு துறை தேர்வு செய்க", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(HomePage.this, "Select atleast one field", Toast.LENGTH_SHORT).show();
                            }
                            Log.i("VB", "8");
                        }
                        //altDialog.dismiss();

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
        ll_english.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                img_english.setBackgroundResource(R.drawable.orange_dot);
                img_tamil.setBackgroundResource(R.drawable.white_dot);
                GetAllTempleList get_Provider_details = new GetAllTempleList(ctx, "en");
                get_Provider_details.execute();
                GeneralData.str_lang = "en";
                str_lang = String.valueOf(GeneralData.str_lang);
                txt_title_list.setText("List Of Temples");

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
                GetAllTempleList get_Provider_details = new GetAllTempleList(ctx, "ta");
                get_Provider_details.execute();
                GeneralData.str_lang = "ta";
                str_lang = String.valueOf(GeneralData.str_lang);
                txt_title_list.setText("கோவில் பட்டியல்");

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

        gD = new GeneralData(this);

        if (GeneralData.ncount == 0) {
            verifyStoragePermissions(this);
        }


        if (gD.isConnectingToInternet()) {
            GetAllTempleList get_Provider_details = new GetAllTempleList(ctx, str_lang);
            get_Provider_details.execute();
        } else {
            final AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
            builder.setCancelable(true);

            View itemView1 = LayoutInflater.from(ctx)
                    .inflate(R.layout.user_current_loc_addr_alert, null);
            final AlertDialog altDialog = builder.create();
            altDialog.setView(itemView1);
            altDialog.show();


            TextView txt_msg = (TextView) itemView1.findViewById(R.id.text_address);
            Button btn_ok = (Button) itemView1.findViewById(R.id.btn_ok);
            btn_ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    altDialog.dismiss();
                    finish();
                }
            });
            txt_msg.setText("Please check your Internet Connection!!");
        }

        GeneralData.ncount = 1;
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
    public void getTempleName(String str_temple_name, String str_temple_id) {

        SharedPreferences.Editor editor = SplashActivity.preferences.edit();
        editor.putString("temple_id",str_temple_id);
        editor.commit();
        Intent i = new Intent(HomePage.this, SubTemples.class);
        i.putExtra("temple_name", str_temple_name);
        i.putExtra("temple_id", str_temple_id);
        startActivity(i);
        finish();
    }


    @Override
    protected void onResume() {
        super.onResume();

        str_lang = GeneralData.str_lang;

        GetAllTempleList get_Provider_details = new GetAllTempleList(ctx, str_lang);
        get_Provider_details.execute();


        if (str_lang.equalsIgnoreCase("ta")) {
            txt_title_list.setText("கோவில் பட்டியல்");
            img_english.setBackgroundResource(R.drawable.white_dot);
            img_tamil.setBackgroundResource(R.drawable.orange_dot);
        } else {
            txt_title_list.setText("List of Temples");
            img_english.setBackgroundResource(R.drawable.orange_dot);
            img_tamil.setBackgroundResource(R.drawable.white_dot);
        }
    }

    @Override
    public void getTempleName(String str_temple_name, String str_temple_id, String str_temple_addr) {

    }


    class GetAllTempleList extends AsyncTask {

        String user_id = null;
        String sResponse = null;
        Context cont;
        String strLang;

        public GetAllTempleList(Context context, String strlang) {
            this.cont = context;
            this.strLang = strlang;

        }

        @Override
        protected void onPreExecute() {
            HomePage.this.runOnUiThread(new Runnable() {
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
                    requestURL = GeneralData.SERVER_IP + "api_response/category?language=ta";
                } else if (strLang.equalsIgnoreCase("en")) {
                    requestURL = GeneralData.SERVER_IP + "api_response/category";
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
               /* } else {
                    dialog.dismiss();
                    generalData.showAlertDialog(cont, "Please check your Internet Connection!", 180);

                }*/
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

                    JSONArray templesCategoriesArray = jsobj.getJSONArray("categories");

                    if (templesCategoriesArray.length() > 0) {
                        for (int i = 0; i < templesCategoriesArray.length(); i++) {
                            TempleListBean templeListBean = new TempleListBean();
                            JSONObject providersServiceJSONobject = templesCategoriesArray.getJSONObject(i);
                            templeListBean.setTemple_id(providersServiceJSONobject.getString("id"));
                            templeListBean.setTemple_name(providersServiceJSONobject.getString("name") + "(" + providersServiceJSONobject.getString("count") + ")");
                            beanArrayList.add(templeListBean);
                        }
                    }

                    mAdapter = new TempleListAdapter(ctx, beanArrayList, (TempleList) ctx);
                    temple_list.setAdapter(mAdapter);
                } else if (jsobj.getString("status").equalsIgnoreCase("false")) {
                    txt_empty = (TextView) findViewById(R.id.txt_empty);
                    txt_empty.setVisibility(View.VISIBLE);
                    txt_empty.setText(jsobj.getString("error"));

                }

                //Error Fixed ...............................
                //Please check.....................


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
        String nCountryId, nStateId;
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
            this.nCountryId = strSelectedItem;
            this.spinCommon = spin;
            this.str_typ = strType;
            this.str_lang = str_lang;
        }

        public GetSearchResults(Context context, String strType, Spinner spin, String strSelectedItem, String strSelectedItemone, String str_lang) {
            this.cont = context;
            this.nCountryId = strSelectedItem;
            this.nStateId = strSelectedItemone;
            this.spinCommon = spin;
            this.str_typ = strType;
            this.str_lang = str_lang;
        }

        @Override
        protected void onPreExecute() {
            HomePage.this.runOnUiThread(new Runnable() {
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
                    requestURL = GeneralData.SERVER_IP + "api_response/" + str_typ + "?state_id=" + nCountryId;

                    Log.i("BG", "country_id :" + nCountryId);
                    Log.i("BG", "state_id :" + nStateId);
                    Log.i("BG", "URL :" + requestURL);
                    //  multipart.addFormField("country_id", "" + nCountryId);


                } else if (str_typ.equalsIgnoreCase("area")) {
                    requestURL = GeneralData.SERVER_IP + "api_response/" + str_typ + "?state_id=" + nCountryId + "&city_id=" + nStateId;
                    Log.i("BG", "URL :" + requestURL);
                  /*  multipart.addFormField("country_id", "" + nCountryId);
                    multipart.addFormField("state_id", "" + nStateId);*/

                } else if (str_typ.equalsIgnoreCase("search_temple")) {
                    requestURL = GeneralData.SERVER_IP + "api_response/" + str_typ + "?state_id=" + nCountryId + "&city_id=" + nStateId;
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
            HomePage.this.runOnUiThread(new Runnable() {
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

                Log.i("BG", "country_id :" + strAreaId);
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
                        requestURL = GeneralData.SERVER_IP + "api_response/" + str_typ + "?country_id=" + strAreaId;
                        requestURL += "&language=ta";
                        Log.i("RA", requestURL);
                    } else {
                        requestURL = GeneralData.SERVER_IP + "api_response/" + str_typ + "?country_id=" + strAreaId;
                    }

                    Log.i("BG", "country_id :" + strAreaId);
                    Log.i("BG", "strProtocolTyp :" + strProtocolTyp);
                    Log.i("BG", "URL :" + requestURL);
                    //  multipart.addFormField("country_id", strCountryId);


                } else if (strProtocolTyp.equalsIgnoreCase("ncfd")) {
                    if (str_lan.equalsIgnoreCase("ta")) {
                        requestURL = GeneralData.SERVER_IP + "api_response/" + str_typ + "?country_id=" + strAreaId + "&state_id=" + strStateId;
                        requestURL += "&language=ta";
                        Log.i("RA", requestURL);
                    } else {
                        requestURL = GeneralData.SERVER_IP + "api_response/" + str_typ + "?country_id=" + strAreaId + "&state_id=" + strStateId;

                    }
                    Log.i("BG", "country_id :" + strAreaId);
                    Log.i("BG", "strProtocolTyp :" + strProtocolTyp);
                    Log.i("BG", "URL :" + requestURL);
                    /*multipart.addFormField("country_id", strCountryId);
                    multipart.addFormField("state_id", strStateId);*/

                } else if (strProtocolTyp.equalsIgnoreCase("nfd")) {
                    if (str_lan.equalsIgnoreCase("ta")) {
                        requestURL = GeneralData.SERVER_IP + "api_response/" + str_typ + "?country_id=" + strAreaId + "&state_id=" + strStateId + "&city_id=" + strCityId;
                        requestURL += "&language=ta";
                        Log.i("RA", requestURL);
                    } else {
                        requestURL = GeneralData.SERVER_IP + "api_response/" + str_typ + "?country_id=" + strAreaId + "&state_id=" + strStateId + "&city_id=" + strCityId;

                    }
                    Log.i("BG", "country_id :" + strAreaId);
                    Log.i("BG", "strProtocolTyp :" + strProtocolTyp);
                    Log.i("BG", "URL :" + requestURL);
                   /* multipart.addFormField("country_id", strCountryId);
                    multipart.addFormField("state_id", strStateId);

                    multipart.addFormField("city_id", strCityId);*/


                } else if (strProtocolTyp.equalsIgnoreCase("nd")) {
                    if (str_lan.equalsIgnoreCase("ta")) {
                        requestURL = GeneralData.SERVER_IP + "api_response/" + str_typ + "?country_id=" + strAreaId + "&state_id=" + strStateId + "&city_id=" + strCityId + "&festival_id=" + strFestivalId;
                        requestURL += "&language=ta";
                        Log.i("RA", requestURL);
                    } else {
                        requestURL = GeneralData.SERVER_IP + "api_response/" + str_typ + "?country_id=" + strAreaId + "&state_id=" + strStateId + "&city_id=" + strCityId + "&festival_id=" + strFestivalId;

                    }
                    Log.i("BG", "country_id :" + strAreaId);
                    Log.i("BG", "strProtocolTyp :" + strProtocolTyp);
                    Log.i("BG", "URL :" + requestURL);
                   /* multipart.addFormField("country_id", strCountryId);
                    multipart.addFormField("state_id", "" + strStateId);

                    multipart.addFormField("city_id", strCityId);
                    multipart.addFormField("festival_id", strFestivalId);*/


                } else if (strProtocolTyp.equalsIgnoreCase("n")) {
                    if (str_lan.equalsIgnoreCase("ta")) {
                        requestURL = GeneralData.SERVER_IP + "api_response/" + str_typ + "?country_id=" + strAreaId + "&state_id=" + strStateId + "&city_id=" + strCityId + "&festival_id=" + strFestivalId + "&diety_id=" + strDietyId;
                        requestURL += "&language=ta";
                        Log.i("RA", requestURL);
                    } else {
                        requestURL = GeneralData.SERVER_IP + "api_response/" + str_typ + "?country_id=" + strAreaId + "&state_id=" + strStateId + "&city_id=" + strCityId + "&festival_id=" + strFestivalId + "&diety_id=" + strDietyId;

                    }
                    Log.i("BG", "country_id :" + strAreaId);
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
                    Log.i("BG", "country_id :" + strAreaId);
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
                // requestURL = GeneralData.SERVER_IP + "api_response/" + str_typ + "?country_id=" + strCountryId + "&state_id=" + strStateId + "&city_id=" + strCityId + "&festival_id=" + strFestivalId + "&diety_id=" + strDietyId;
                Log.i("RA", "RESP : " + sResponse);
                Intent i = new Intent(HomePage.this, TempleSearchDetails.class);
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
