package dqa.com.msibook;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class msibook_dqaweekly_project_activity extends AppCompatActivity implements OnChartValueSelectedListener {

    private PieChart mChart1;
    private PieChart mChart2;

    private SectionsPagerAdapter1 mSectionsPagerAdapter1;
    private SectionsPagerAdapter2 mSectionsPagerAdapter2;

    private ViewPager mViewPager1;
    private ViewPager mViewPager2;
    private Integer CheckTab_number;
    private Boolean CheckPagefirst_week;
    private Boolean CheckPagefirst_dp;

    private String SetF_Year;
    private String SetWeek;

    private Typeface tf;

    protected String[] mMonths = new String[]{
            "CE", "DPS", "IPS", "NB", "MB", "EPS"
    };

    protected String[] mParties = new String[]{
            "CE", "DPS", "IPS", "NB", "MB", "EPS"
    };

    private ListView mListView;
    private String test456;
    private TextView tesView;

    private String updatePath;//更新path

    static final String db_name = "check_first";//資料庫名稱
    static final String tb_name = "first_data";//資料表名稱
    SQLiteDatabase db;//資料庫物件

    private msibook_dqaweekly_main.MyAdapter mMyAdapter;
    private DropDownAdapter mDropDownAdapter;
    private DropDownAdapterweek mDropDownAdapterweek;
    private Boolean Tab_Scroll;

    public List<String> ArrayYear = new ArrayList<String>();
    public List<String> ArrayWeek = new ArrayList<String>();
    private List<String> ArrayDeptName = new ArrayList<String>();
    public List<String> ArrayDeptID = new ArrayList<String>();

    public String IM_MSIK;
    public String IM_MSIS;

    public Spinner spinnerdp;
    public Spinner spinner_week;
    private Button btnback;
    private Button btnnext;

    private String Year;

    private LinearLayout lindepart;

    private Double CC;//實際出勤

    private String putEtradp;
    private String putEtrawk;
    //帶到第二頁的變數判斷
    private String putEtraDepID;
    private ProgressDialog progressBar;

    private String getlogingID;

    private String LoginPath;

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;

    private ImageView imageView;
    private PopupWindow popupWindow;
    private LinearLayout main_linearLayout1;
    private LinearLayout main_linearLayout2;
    private LinearLayout main_linearLayout3;
    private LinearLayout main_linearLayout4;
    private LinearLayout main_linearLayout5;
    private LinearLayout main_linearLayout6;
    private LinearLayout main_linearLayout7;

    private TabLayout tabLayout1;
    private TabLayout tabLayout2;

    private int DpSpinnerPosition;

    private static final int REQUEST_EXTERNAL_STORAGE = 1;

    private static final String WRITE_EXTERNAL_STORAGE = "android.permission.WRITE_EXTERNAL_STORAGE";
    private static final String READ_EXTERNAL_STORAGE = "android.permission.READ_EXTERNAL_STORAGE";

    public interface VolleyCallback {
        void onSuccess(JSONObject result);
    }

    //抓部門代號、名稱
    private void Find_Dept_List() {

        ArrayDeptName.clear();
        ArrayDeptID.clear();
        IM_MSIK="";
        IM_MSIS="";

        if(UserData.Region.contains("MSIK")){
            ArrayDeptName.add(0, "設計品質驗證部-昆山");
            ArrayDeptID.add(0, "2");
            spinnerdp = (Spinner) findViewById(R.id.spinner_depart);
            mDropDownAdapter = new DropDownAdapter(msibook_dqaweekly_project_activity.this, ArrayDeptName);
            spinnerdp.setAdapter(mDropDownAdapter);
        }else if(UserData.Region.contains("MSIS")){
            ArrayDeptName.add(0, "設計品質驗證部-寶安");
            ArrayDeptID.add(0, "3");
            spinnerdp = (Spinner) findViewById(R.id.spinner_depart);
            mDropDownAdapter = new DropDownAdapter(msibook_dqaweekly_project_activity.this, ArrayDeptName);
            spinnerdp.setAdapter(mDropDownAdapter);
        }else{
            ArrayDeptName.add(0, "設計品質驗證部-台北");
            ArrayDeptID.add(0, "1");
            ArrayDeptName.add(1, "設計品質驗證部-昆山");
            ArrayDeptID.add(1, "2");
            ArrayDeptName.add(2, "設計品質驗證部-寶安");
            ArrayDeptID.add(2, "3");
            ArrayDeptName.add(3, "台北.昆山.寶安");
            ArrayDeptID.add(3, "4");

            spinnerdp = (Spinner) findViewById(R.id.spinner_depart);
            mDropDownAdapter = new DropDownAdapter(msibook_dqaweekly_project_activity.this, ArrayDeptName);
            spinnerdp.setAdapter(mDropDownAdapter);
        }



        //*****************部門變動監聽************************
        spinnerdp.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {

            public void onItemSelected(AdapterView adapterView, View view, int position, long id) {

                DropDownAdapter DropDownAdapter = (DropDownAdapter) adapterView.getAdapter();

                DropDownAdapter.selectedItemdp = position;

                    TabLayout.Tab tab2 = tabLayout2.getTabAt(0);
                    tab2.select();

                    if(position==3){
                        TabLayout.Tab tab = tabLayout1.getTabAt(0);
                        tab.select();
                    }else {

                    }




//                if(position==1 || position==2){
//                    if(DpSpinnerPosition !=0){
//
//                    }else{
//                        TabLayout.Tab tab11 = tabLayout1.getTabAt(0);
//                        tab11.select();
//                    }
//                }else {
//                    TabLayout.Tab tab22 = tabLayout2.getTabAt(0);
//                    tab22.select();
//
//                    TabLayout.Tab tab11 = tabLayout1.getTabAt(0);
//                    tab11.select();
//                }

                mDropDownAdapter.notifyDataSetChanged();

                putEtradp = (String) spinnerdp.getSelectedItem();
                //抓部門代號到第二頁去
                putEtraDepID = ArrayDeptID.get(position);

                mSectionsPagerAdapter1.RegionIndex = position;

                Log.w("Position",String.valueOf(position));

                mSectionsPagerAdapter1.notifyDataSetChanged();
                //動態隱藏Tab
                if(ArrayDeptID.get(position).indexOf("4")==-1){
                    //選前三個
                    //CheckTab_number=0;
                    tabLayout2.setVisibility(View.GONE);
                    mViewPager2.setVisibility(View.GONE);
                    tabLayout1.setVisibility(View.VISIBLE);
                    mViewPager1.setVisibility(View.VISIBLE);
                }else{
                    Log.w("9487878787",String.valueOf(ArrayDeptID.get(position)));
                    //tabLayout.getTabWidget().getChildAt(ArrayDeptID.get(position)).setVisibility(View.GONE);
                    //選第四個 綜合
                    tabLayout2.setVisibility(View.VISIBLE);
                    mViewPager2.setVisibility(View.VISIBLE);
                    tabLayout1.setVisibility(View.GONE);
                    mViewPager1.setVisibility(View.GONE);
                }


                if ((DpSpinnerPosition == 0 || DpSpinnerPosition == 1 || DpSpinnerPosition == 2 ) && position == 3)
                {
                    CheckTab_number = 5;
//                    TabLayout.Tab tab2 = tabLayout2.getTabAt(0);
//                    tab2.select();
                    //Log.w("PPPPPP22222",String.valueOf(tab2.getPosition()));
                }
                else if(DpSpinnerPosition == 3 &&  (position == 0 || position == 1 || position == 2 ))
                {
                    CheckTab_number = 0;
//                    TabLayout.Tab tab = tabLayout1.getTabAt(0);
//                    tab.select();
                    //Log.w("PPPPPP111111",String.valueOf(tab.getPosition()));
                }

                DpSpinnerPosition = position;


                Log.w("DDDDDNumber", String.valueOf(DpSpinnerPosition));
                Log.w("CCCCCNumber", String.valueOf(CheckTab_number));

                if (CheckPagefirst_week && CheckPagefirst_dp)
                {
                    if (ArrayWeek.size() > 0) {//*******************************************先設變數存tab的數字 在用變數判斷現在是switch多少
                        switch (CheckTab_number) {
                            case 0:
                                //專案
                                msibook_dqaweekly_project_msr_project Tab1_1 = (msibook_dqaweekly_project_msr_project) mSectionsPagerAdapter1.getFragment(0);//Tab0
                                Tab1_1.Find_Mothly_MSR(ArrayWeek.get(spinner_week.getSelectedItemPosition()).replace("週", ""), ArrayYear.get(spinner_week.getSelectedItemPosition()),String.valueOf(DpSpinnerPosition));
                                Tab1_1.SetYear(ArrayYear.get(spinner_week.getSelectedItemPosition()));
                                Tab1_1.SetWeek(ArrayWeek.get(spinner_week.getSelectedItemPosition()).replace("週", ""));
                                //Tab1_1.SetRegionID(String.valueOf(spinnerdp.getSelectedItemPosition()+1));
                                Tab1_1.SetRegionID(ArrayDeptID.get(spinnerdp.getSelectedItemPosition()));
                                break;

                            case 1:
                                //資源
                                msibook_dqaweekly_project_activity_resource Tab3_3 = (msibook_dqaweekly_project_activity_resource) mSectionsPagerAdapter1.getFragment(1);//Tab2
                                Tab3_3.Find_Weekly_utilization_Total(ArrayWeek.get(spinner_week.getSelectedItemPosition()).replace("週", ""),ArrayYear.get(spinner_week.getSelectedItemPosition()), ArrayDeptID.get(spinnerdp.getSelectedItemPosition()));
                                Tab3_3.Find_Resource_LsetWeek(ArrayWeek.get(spinner_week.getSelectedItemPosition()).replace("週", ""),ArrayYear.get(spinner_week.getSelectedItemPosition()), ArrayDeptID.get(spinnerdp.getSelectedItemPosition()),"All");
                                Log.w("oooooo",String.valueOf(ArrayDeptID.get(spinnerdp.getSelectedItemPosition())));
                                Tab3_3.Find_Total_Cost(ArrayWeek.get(spinner_week.getSelectedItemPosition()).replace("週", ""),ArrayYear.get(spinner_week.getSelectedItemPosition()), ArrayDeptID.get(spinnerdp.getSelectedItemPosition()),"All");
                                break;

                            case 2:
                                //稼動率
                                msibook_dqaweekly_project_activity_utilization Tab2_2 = (msibook_dqaweekly_project_activity_utilization) mSectionsPagerAdapter1.getFragment(2);//Tab1
                                Tab2_2.Find_Weekly_utilization_Total(ArrayWeek.get(spinner_week.getSelectedItemPosition()).replace("週", ""),ArrayYear.get(spinner_week.getSelectedItemPosition()), ArrayDeptID.get(spinnerdp.getSelectedItemPosition()));
                                if(ArrayDeptID.get(spinnerdp.getSelectedItemPosition()).indexOf("1")==-1){
                                    //昆山寶安
                                    //CheckTab_number=0;
                                    Tab2_2.Find_Week_Utilization(ArrayWeek.get(spinner_week.getSelectedItemPosition()).replace("週", ""),ArrayYear.get(spinner_week.getSelectedItemPosition()), ArrayDeptID.get(spinnerdp.getSelectedItemPosition()));
                                }else{
                                    //選台北
                                    Tab2_2.Find_Week_Utilization(ArrayWeek.get(spinner_week.getSelectedItemPosition()).replace("週", ""),ArrayYear.get(spinner_week.getSelectedItemPosition()),"19762");
                                }
                                Tab2_2.SetYear(ArrayYear.get(spinner_week.getSelectedItemPosition()));
                                Tab2_2.SetWeek(ArrayWeek.get(spinner_week.getSelectedItemPosition()).replace("週", ""));
                                break;

                            case 3:
                                //專案
                                msibook_dqaweekly_project_activity_project Tab1_3 = (msibook_dqaweekly_project_activity_project) mSectionsPagerAdapter1.getFragment(3);//Tab0
                                Tab1_3.Find_BU_PM_Rate_LastWeek(ArrayWeek.get(spinner_week.getSelectedItemPosition()).replace("週", ""), ArrayYear.get(spinner_week.getSelectedItemPosition()), ArrayDeptID.get(spinnerdp.getSelectedItemPosition()), "All");
                                Tab1_3.SetYear(ArrayYear.get(spinner_week.getSelectedItemPosition()));
                                Tab1_3.SetWeek(ArrayWeek.get(spinner_week.getSelectedItemPosition()).replace("週", ""));
                                //Tab1_1.SetRegionID(String.valueOf(spinnerdp.getSelectedItemPosition()+1));
                                Tab1_3.SetRegionID(ArrayDeptID.get(spinnerdp.getSelectedItemPosition()));
                                break;

                            case 4:

                                break;

                            case 5:
                                //新增專案
                                    msibook_dqaweekly_project_activity_new_project Tab5_5 = (msibook_dqaweekly_project_activity_new_project) mSectionsPagerAdapter2.getFragment(0);
                                    Tab5_5.Find_PM_NewModel(ArrayWeek.get(spinner_week.getSelectedItemPosition()).replace("週", ""), ArrayYear.get(spinner_week.getSelectedItemPosition()));
                                    //Tab5_5.Find_Fac_List(Year,ArrayWeek.get(spinner_week.getSelectedItemPosition()).replace("週", ""));
                                    //Tab5_5.Find_Fac_Data(ArrayWeek.get(spinner_week.getSelectedItemPosition()).replace("週", ""));
                                break;
                            case 6:
                                //彙報
                                msibook_dqaweekly_project_activity_fac_data Tab6_6 = (msibook_dqaweekly_project_activity_fac_data) mSectionsPagerAdapter2.getFragment(1);
                                Tab6_6.Find_Fac_List_LastWeek(ArrayYear.get(spinner_week.getSelectedItemPosition()),ArrayWeek.get(spinner_week.getSelectedItemPosition()).replace("週", ""));
                                //Tab5_5.Find_Fac_Data(ArrayWeek.get(spinner_week.getSelectedItemPosition()).replace("週", ""));
                                break;
                            case 7:
                                //抽驗報告
                                msibook_dqaweekly_project_activity_lottery_report Tab7_7 = (msibook_dqaweekly_project_activity_lottery_report) mSectionsPagerAdapter2.getFragment(2);
                                Tab7_7.SetWeek(ArrayWeek.get(spinner_week.getSelectedItemPosition()).replace("週", ""));
                                Tab7_7.SetYear(ArrayYear.get(spinner_week.getSelectedItemPosition()));
                                //Tab5_5.Find_Fac_Data(ArrayWeek.get(spinner_week.getSelectedItemPosition()).replace("週", ""));
                                break;
                            case 8:
                                //工作負載 - 專案成本報告
                                msibook_dqaweekly_project_activity_jobs_load Tab8_8 = (msibook_dqaweekly_project_activity_jobs_load) mSectionsPagerAdapter2.getFragment(3);
                                Tab8_8.SetWeek(ArrayWeek.get(spinner_week.getSelectedItemPosition()).replace("週", ""));
                                Tab8_8.SetYear(ArrayYear.get(spinner_week.getSelectedItemPosition()));
                                Tab8_8.Find_Week_Utilization_Job_Load(ArrayWeek.get(spinner_week.getSelectedItemPosition()).replace("週", ""),ArrayYear.get(spinner_week.getSelectedItemPosition()),"19762");
                                //Tab5_5.Find_Fac_Data(ArrayWeek.get(spinner_week.getSelectedItemPosition()).replace("週", ""));
                                break;
                            case 9:
                                //排行榜
                                msibook_dqaweekly_project_activity_rank Tab9_9 = (msibook_dqaweekly_project_activity_rank) mSectionsPagerAdapter2.getFragment(4);
                                Tab9_9.Find_RSS_Rank(ArrayYear.get(spinner_week.getSelectedItemPosition()),ArrayWeek.get(spinner_week.getSelectedItemPosition()).replace("週", ""));

                                break;

                            case 10:
                                //排行榜
                                msibook_dqaweekly_find_important_message Tab10_10 = (msibook_dqaweekly_find_important_message) mSectionsPagerAdapter2.getFragment(5);
                                Tab10_10.Find_Important_Message(ArrayYear.get(spinner_week.getSelectedItemPosition()),ArrayWeek.get(spinner_week.getSelectedItemPosition()).replace("週", ""));

                                break;

                        }
                    }else {

                    }
                }else{
                    if(ArrayWeek!=null && ArrayWeek.size() > 0) {

                        msibook_dqaweekly_project_activity_new_project Tab5_5 = (msibook_dqaweekly_project_activity_new_project) mSectionsPagerAdapter2.getFragment(0);
                        if(Tab5_5 != null) {
                            Tab5_5.Find_PM_NewModel(ArrayWeek.get(spinner_week.getSelectedItemPosition()).replace("週", ""), ArrayYear.get(spinner_week.getSelectedItemPosition()));
                        }
                        //專案
                        msibook_dqaweekly_project_msr_project Tab1_1 = (msibook_dqaweekly_project_msr_project) mSectionsPagerAdapter1.getFragment(0);//Tab0
                        if(Tab1_1 != null) {
                            Tab1_1.Find_Mothly_MSR(ArrayWeek.get(spinner_week.getSelectedItemPosition()).replace("週", ""), ArrayYear.get(spinner_week.getSelectedItemPosition()), "0");
                            Tab1_1.SetYear(ArrayYear.get(spinner_week.getSelectedItemPosition()));
                            Tab1_1.SetWeek(ArrayWeek.get(spinner_week.getSelectedItemPosition()).replace("週", ""));
                            //Tab1_1.SetRegionID(String.valueOf(spinnerdp.getSelectedItemPosition() + 1));
                            Tab1_1.SetRegionID(ArrayDeptID.get(spinnerdp.getSelectedItemPosition()));
                        }

                    }
                }

                CheckPagefirst_dp = true;



            }

            public void onNothingSelected(AdapterView arg0) {

            }
        });

    }

    public static void getString(String Url, RequestQueue mQueue, final GetServiceData.VolleyCallback callback) {

        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, Url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        callback.onSuccess(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("TestJsonObj");
                        System.out.println(error);
                    }
                }
        );

        int socketTimeout = 30000;
        getRequest.setRetryPolicy(new DefaultRetryPolicy(socketTimeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        mQueue.add(getRequest);
    }

    //讀取週數
    private void Find_Get_Week() {
        //顯示  讀取等待時間Bar
        progressBar.show();
        ArrayYear.clear();
        ArrayWeek.clear();

        RequestQueue mQueue = Volley.newRequestQueue(this);

        String Path = "http://wtsc.msi.com.tw/IMS/Weekly_Report.asmx/Get_Week";

        getString(Path, mQueue, new GetServiceData.VolleyCallback() {
            @Override
            public void onSuccess(JSONObject result) {

                try {
                    JSONArray UserArray = new JSONArray(result.getString("Key"));

                    if (UserArray.length() > 0) {
                        for (int i = 0; i < UserArray.length(); i++) {
                            JSONObject IssueData = UserArray.getJSONObject(i);

                            String F_Year = String.valueOf(IssueData.getInt("F_Year"));

                            String weekID = String.valueOf(IssueData.getInt("F_Week"));
                            //SetWeek = weekID;
                            ArrayYear.add(i, F_Year);
                            ArrayWeek.add(i, weekID + "週");
                            //tesView.setText(UserArray.length());

                        }
                        spinner_week = (Spinner) findViewById(R.id.spinner_week);

                        mDropDownAdapterweek = new DropDownAdapterweek(msibook_dqaweekly_project_activity.this, ArrayWeek);

                        spinner_week.setAdapter(mDropDownAdapterweek);

                        if(ArrayWeek.size() >=1) {
                            spinner_week.setSelection(1);
                            //週次載完 載入部門
                            Find_Dept_List();
                        }

                        //***********週次變動監聽*************************
                        spinner_week.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
                            public void onItemSelected(AdapterView adapterView, View view, int position, long id) {

                                DropDownAdapterweek DropDownAdapterweek = (DropDownAdapterweek) adapterView.getAdapter();

                                DropDownAdapterweek.selectedItemweek = position;

                                mDropDownAdapterweek.notifyDataSetChanged();

                                //Find_Get_Week(ArrayWeek.get(position),"1");
                                putEtrawk = (String) spinner_week.getSelectedItem();
                                //String spStr = String.valueOf(spinner_week.getSelectedItemPosition());
                                //tesView.setText(spStr);
                                //tesView.setText(weekID);

//                                if(spinnerdp.getSelectedItem() != null && spinner_week.getSelectedItem() !=null) {
//                                    Find_WeekReport(ArrayDeptID.get(spinnerdp.getSelectedItemPosition()), ArrayWeek.get(position).replace("週", ""));
//                                }
                                if (CheckPagefirst_week && CheckPagefirst_dp)
                                {
                                    if (spinnerdp.getSelectedItem() != null) {//判斷部門下拉是否為空值
                                        switch (CheckTab_number) {
                                            case 0:
                                                //專案
                                                msibook_dqaweekly_project_msr_project Tab1_1 = (msibook_dqaweekly_project_msr_project) mSectionsPagerAdapter1.getFragment(0);//Tab0
                                                Tab1_1.Find_Mothly_MSR(ArrayWeek.get(spinner_week.getSelectedItemPosition()).replace("週", ""), ArrayYear.get(spinner_week.getSelectedItemPosition()),String.valueOf(DpSpinnerPosition));
                                                Tab1_1.SetYear(ArrayYear.get(spinner_week.getSelectedItemPosition()));
                                                Tab1_1.SetWeek(ArrayWeek.get(spinner_week.getSelectedItemPosition()).replace("週", ""));
                                                //Tab1_1.SetRegionID(String.valueOf(spinnerdp.getSelectedItemPosition()+1));
                                                Tab1_1.SetRegionID(ArrayDeptID.get(spinnerdp.getSelectedItemPosition()));
                                                break;

                                            case 1:
                                                //資源
                                                msibook_dqaweekly_project_activity_resource Tab3_3 = (msibook_dqaweekly_project_activity_resource) mSectionsPagerAdapter1.getFragment(1);//Tab2
                                                Tab3_3.Find_Weekly_utilization_Total(ArrayWeek.get(spinner_week.getSelectedItemPosition()).replace("週", ""),ArrayYear.get(spinner_week.getSelectedItemPosition()), ArrayDeptID.get(spinnerdp.getSelectedItemPosition()));
                                                Tab3_3.Find_Resource_LsetWeek(ArrayWeek.get(spinner_week.getSelectedItemPosition()).replace("週", ""),ArrayYear.get(spinner_week.getSelectedItemPosition()), ArrayDeptID.get(spinnerdp.getSelectedItemPosition()),"All");
                                                Tab3_3.Find_Total_Cost(ArrayWeek.get(spinner_week.getSelectedItemPosition()).replace("週", ""),ArrayYear.get(spinner_week.getSelectedItemPosition()), ArrayDeptID.get(spinnerdp.getSelectedItemPosition()),"All");
                                                break;

                                            case 2:
                                                //稼動率
                                                msibook_dqaweekly_project_activity_utilization Tab2_2 = (msibook_dqaweekly_project_activity_utilization) mSectionsPagerAdapter1.getFragment(2);//Tab1
                                                Tab2_2.Find_Weekly_utilization_Total(ArrayWeek.get(spinner_week.getSelectedItemPosition()).replace("週", ""),ArrayYear.get(spinner_week.getSelectedItemPosition()), ArrayDeptID.get(spinnerdp.getSelectedItemPosition()));
                                                if(ArrayDeptID.get(spinnerdp.getSelectedItemPosition()).indexOf("1")==-1){
                                                    //昆山寶安
                                                    //CheckTab_number=0;
                                                    Tab2_2.Find_Week_Utilization(ArrayWeek.get(spinner_week.getSelectedItemPosition()).replace("週", ""),ArrayYear.get(spinner_week.getSelectedItemPosition()), ArrayDeptID.get(spinnerdp.getSelectedItemPosition()));
                                                }else{
                                                    //選台北
                                                    Tab2_2.Find_Week_Utilization(ArrayWeek.get(spinner_week.getSelectedItemPosition()).replace("週", ""),ArrayYear.get(spinner_week.getSelectedItemPosition()),"19762");
                                                }
                                                Tab2_2.SetYear(ArrayYear.get(spinner_week.getSelectedItemPosition()));
                                                Tab2_2.SetWeek(ArrayWeek.get(spinner_week.getSelectedItemPosition()).replace("週", ""));

                                            case 3:
                                                //工作負載
                                                msibook_dqaweekly_project_activity_project Tab1_3 = (msibook_dqaweekly_project_activity_project) mSectionsPagerAdapter1.getFragment(3);//Tab0
                                                Tab1_3.Find_BU_PM_Rate_LastWeek(ArrayWeek.get(spinner_week.getSelectedItemPosition()).replace("週", ""), ArrayYear.get(spinner_week.getSelectedItemPosition()), ArrayDeptID.get(spinnerdp.getSelectedItemPosition()),"All");
                                                Tab1_3.SetYear(ArrayYear.get(spinner_week.getSelectedItemPosition()));
                                                Tab1_3.SetWeek(ArrayWeek.get(spinner_week.getSelectedItemPosition()).replace("週", ""));
                                                //Tab1_1.SetRegionID(String.valueOf(spinnerdp.getSelectedItemPosition()+1));
                                                Tab1_3.SetRegionID(ArrayDeptID.get(spinnerdp.getSelectedItemPosition()));

                                            case 4:

                                                break;

                                            case 5:
                                                //新增專案
                                                msibook_dqaweekly_project_activity_new_project Tab5_5 = (msibook_dqaweekly_project_activity_new_project) mSectionsPagerAdapter2.getFragment(0);
                                                Tab5_5.Find_PM_NewModel(ArrayWeek.get(spinner_week.getSelectedItemPosition()).replace("週", ""),ArrayYear.get(spinner_week.getSelectedItemPosition()));
                                                //Tab5_5.Find_Fac_List(Year,ArrayWeek.get(spinner_week.getSelectedItemPosition()).replace("週", ""));
                                                //Tab5_5.Find_Fac_Data(ArrayWeek.get(spinner_week.getSelectedItemPosition()).replace("週", ""));
                                                break;
                                            case 6:
                                                //彙報
                                                msibook_dqaweekly_project_activity_fac_data Tab6_6 = (msibook_dqaweekly_project_activity_fac_data) mSectionsPagerAdapter2.getFragment(1);
                                                Tab6_6.Find_Fac_List_LastWeek(ArrayYear.get(spinner_week.getSelectedItemPosition()),ArrayWeek.get(spinner_week.getSelectedItemPosition()).replace("週", ""));
                                                //Tab5_5.Find_Fac_Data(ArrayWeek.get(spinner_week.getSelectedItemPosition()).replace("週", ""));
                                                break;
                                            case 7:
                                                //抽驗報告
                                                msibook_dqaweekly_project_activity_lottery_report Tab7_7 = (msibook_dqaweekly_project_activity_lottery_report) mSectionsPagerAdapter2.getFragment(2);
                                                Tab7_7.SetWeek(ArrayWeek.get(spinner_week.getSelectedItemPosition()).replace("週", ""));
                                                Tab7_7.SetYear(ArrayYear.get(spinner_week.getSelectedItemPosition()));
                                                //Tab5_5.Find_Fac_Data(ArrayWeek.get(spinner_week.getSelectedItemPosition()).replace("週", ""));
                                                break;
                                            case 8:
                                                //工作負載 - 專案成本報告
                                                msibook_dqaweekly_project_activity_jobs_load Tab8_8 = (msibook_dqaweekly_project_activity_jobs_load) mSectionsPagerAdapter2.getFragment(3);
                                                Tab8_8.SetWeek(ArrayWeek.get(spinner_week.getSelectedItemPosition()).replace("週", ""));
                                                Tab8_8.SetYear(ArrayYear.get(spinner_week.getSelectedItemPosition()));
                                                Tab8_8.Find_Week_Utilization_Job_Load(ArrayWeek.get(spinner_week.getSelectedItemPosition()).replace("週", ""),ArrayYear.get(spinner_week.getSelectedItemPosition()),"19762");
                                                //Tab5_5.Find_Fac_Data(ArrayWeek.get(spinner_week.getSelectedItemPosition()).replace("週", ""));
                                                break;
                                            case 9:
                                                //排行榜
                                                msibook_dqaweekly_project_activity_rank Tab9_9 = (msibook_dqaweekly_project_activity_rank) mSectionsPagerAdapter2.getFragment(4);
                                                Tab9_9.Find_RSS_Rank(ArrayYear.get(spinner_week.getSelectedItemPosition()),ArrayWeek.get(spinner_week.getSelectedItemPosition()).replace("週", ""));

                                                break;

                                            case 10:
                                                //排行榜
                                                msibook_dqaweekly_find_important_message Tab10_10 = (msibook_dqaweekly_find_important_message) mSectionsPagerAdapter2.getFragment(5);
                                                Tab10_10.Find_Important_Message(ArrayYear.get(spinner_week.getSelectedItemPosition()),ArrayWeek.get(spinner_week.getSelectedItemPosition()).replace("週", ""));

                                                break;

                                        }

                                    } else {
                                    }
                                }
                                Log.w("Week", ArrayWeek.get(spinner_week.getSelectedItemPosition()).replace("週", ""));

                                CheckPagefirst_week = true;
                            }

                            public void onNothingSelected(AdapterView arg0) {

                            }
                        });
                    }
                    //關閉-讀取等待時間Bar
                    progressBar.dismiss();



                } catch (JSONException ex) {

                }

            }
        });


    }

    //---------比對版本-------

    private static int getVersionCode(Context context) {
        int versionCode = 0;
        try {
            // 獲取軟件版本號，對應AndroidManifest.xml下android:versionCode
            versionCode = context.getPackageManager().getPackageInfo("com.example.administrator.dqaweekly", 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionCode;
    }

    public void isUpdate(final Context context) {

        RequestQueue mQueue = Volley.newRequestQueue(context);

        String Path = "http://wtsc.msi.com.tw/IMS/Weekly_Report.asmx/Get_Application_Version?Type=Android";

        getString(Path, mQueue, new GetServiceData.VolleyCallback() {
            @Override
            public void onSuccess(JSONObject result) {

                try {

                    JSONArray UserArray = new JSONArray(result.getString("Key"));

                    if (UserArray.length() > 0) {

                        UpdateManager mUpdateManager;

                        int versionCode = getVersionCode(context);

                        JSONObject IssueData = UserArray.getJSONObject(0);

                        String Version = String.valueOf(IssueData.getInt("Version"));

                        String Path = String.valueOf(IssueData.getInt("Path"));
                        updatePath = Path;

                        Log.w("裝置版本", String.valueOf(versionCode));
                        Log.w("Server上版本", String.valueOf(Version));

                        if (Version != String.valueOf(versionCode)) {
                            int permission = ActivityCompat.checkSelfPermission(msibook_dqaweekly_project_activity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

                            if (permission != PackageManager.PERMISSION_GRANTED) {
                                // 無權限，向使用者請求
                                ActivityCompat.requestPermissions(
                                        msibook_dqaweekly_project_activity.this,
                                        new String[]{WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE}, REQUEST_EXTERNAL_STORAGE
                                );

                            } else {
                                mUpdateManager = new UpdateManager(context,updatePath);

                                mUpdateManager.checkUpdateInfo();
                            }

                        } else {
                            Toast.makeText(msibook_dqaweekly_project_activity.this, "目前為最新版本", Toast.LENGTH_SHORT).show();
                        }

                    }
                } catch (JSONException ex) {

                }

            }
        });

        //return VersionCheck;
    }




    class MainItem {
        String Title;
        String now_man;
        String expect_man;
        String now_hour;
        String expect_hour;
        String count;
        String slash;
        boolean Type;

        public MainItem(String Title, String now_man, String expect_man, String now_hour, String expect_hour, String count, String slash, boolean Type) {
            this.Title = Title;
            this.now_man = now_man;
            this.expect_man = expect_man;
            this.now_hour = now_hour;
            this.expect_hour = expect_hour;
            this.count = count;
            this.slash = slash;
            this.Type = Type;
        }

        public String GetTitle() {
            return this.Title;
        }

        public String Getnow_man() {
            return this.now_man;
        }

        public String Getexpect_man() {
            return this.expect_man;
        }

        public String Getnow_hour() {
            return this.now_hour;
        }

        public String Getexpect_hour() {
            return this.expect_hour;
        }

        public String Getcount() {
            return this.count;
        }

        public String Getslash() {
            return slash;
        }

        public boolean GetType() {
            return Type;
        }

    }

    private Button btn_open_pop;
    private Button btn_close_pop;
    private Button btn_close_popinfo;


    private void addAnimation() {//加入了旋转动画
        RotateAnimation rotateAnimation = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setDuration(500);//设置动画时间
        btn_open_pop.setAnimation(rotateAnimation);//设置动画
        btn_open_pop.startAnimation(rotateAnimation);//开始动画
    }

    //建立含資料項目的物件
    private void addData(String checkfirst) {
        ContentValues cv = new ContentValues(1);//建立含2個資料項目的物件
        cv.put("checkfirst", checkfirst);
        Log.w("checkfirst", checkfirst);
        db.insert(tb_name, null, cv);//將資料加入資料表
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    UpdateManager mUpdateManager = new UpdateManager(msibook_dqaweekly_project_activity.this,updatePath);

                    mUpdateManager.checkUpdateInfo();

                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }


    //漢堡列show popWindow
    private void showPopupWindow() {
        View view = LayoutInflater.from(this).inflate(R.layout.activity_msibook_dqaweekly_popupwindow_layout, null);//获取popupWindow子布局对象
        popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, false);//初始化
        popupWindow.setAnimationStyle(R.style.dqaweekly_popupAnim);//设置动画
        popupWindow.showAsDropDown(lindepart, -300, 0);//在ImageView控件下方弹出

        //首頁被點擊
        main_linearLayout1 = (LinearLayout) view.findViewById(R.id.main_lin1);
        main_linearLayout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(msibook_dqaweekly_project_activity.this, "回首頁", Toast.LENGTH_SHORT).show();
                popupWindow.dismiss();
                Intent intent = new Intent();
                //從MainActivity 到Main2Activity
                intent.setClass(msibook_dqaweekly_project_activity.this, msibook_dqaweekly_main.class);
                //開啟Activity
                startActivity(intent);
                finish();
            }
        });
        //部級週報被點擊
        main_linearLayout6 = (LinearLayout) view.findViewById(R.id.main_lin6);
        main_linearLayout6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(msibook_dqaweekly_project_activity.this, "回部級週報", Toast.LENGTH_SHORT).show();
                popupWindow.dismiss();
                Intent intent = new Intent();
                //從MainActivity 到Main2Activity
                intent.setClass(msibook_dqaweekly_project_activity.this, msibook_dqaweekly_project_activity.class);
                //開啟Activity
                startActivity(intent);
            }
        });
        //計算公式被點擊
        main_linearLayout2 = (LinearLayout) view.findViewById(R.id.main_lin2);
        main_linearLayout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(msibook_dqaweekly_project_activity.this, "計算公式", Toast.LENGTH_SHORT).show();
                popupWindow.dismiss();
                initiatePopupWindow();//呼叫pop 計算公式
            }
        });
        //操作說明被點擊
        main_linearLayout3 = (LinearLayout) view.findViewById(R.id.main_lin3);
        main_linearLayout3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(msibook_dqaweekly_project_activity.this, "操作說明", Toast.LENGTH_SHORT).show();
                popupWindow.dismiss();

//                Intent intent = new Intent();
//                //從MainActivity 到Main2Activity
//                intent.setClass(ProjectActivity.this, Main_ViewPager.class);
//                //開啟Activity
//                startActivity(intent);
            }
        });
        //版本紀錄被點擊
        main_linearLayout4 = (LinearLayout) view.findViewById(R.id.main_lin4);
        main_linearLayout4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(msibook_dqaweekly_project_activity.this, "版本紀錄", Toast.LENGTH_SHORT).show();
                popupWindow.dismiss();

                infoPopupWindow();

            }
        });
        //檢查更新被點擊
        main_linearLayout5 = (LinearLayout) view.findViewById(R.id.main_lin5);
        main_linearLayout5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(MainActivity.this, "檢查更新", Toast.LENGTH_SHORT).show();
                popupWindow.dismiss();

                isUpdate(msibook_dqaweekly_project_activity.this);
                //infoPopupWindow();
            }
        });

        //目前版本被點擊
        main_linearLayout7=(LinearLayout)view.findViewById(R.id.main_lin7);
        main_linearLayout7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(msibook_dqaweekly_project_activity.this, "目前版本", Toast.LENGTH_SHORT).show();
                popupWindow.dismiss();
                nowversionPopupWindow();//呼叫版本QR_Code
            }
        });

    }

    private PopupWindow pwindo;

    private void initiatePopupWindow() {
        try {
            // We need to get the instance of the LayoutInflater
            LayoutInflater inflater = (LayoutInflater) msibook_dqaweekly_project_activity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View layout = inflater.inflate(R.layout.msibook_dqaweekly_help_popup_window, (ViewGroup) findViewById(R.id.popup_element));

            DisplayMetrics metrics = getResources().getDisplayMetrics();
            int screenWidth = (int) (metrics.widthPixels * 0.8);
            int screenHeight = (int) (metrics.heightPixels * 0.78);

            pwindo = new PopupWindow(layout, screenWidth, screenHeight, true);
            pwindo.showAtLocation(layout, Gravity.CENTER, 0, 0);

            btn_close_pop = (Button) layout.findViewById(R.id.btn_closepop);
            btn_close_pop.setOnClickListener(cancel_button_click_listener);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void nowversionPopupWindow(){
        try{
            // We need to get the instance of the LayoutInflater
            LayoutInflater inflater = (LayoutInflater) msibook_dqaweekly_project_activity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View layout = inflater.inflate(R.layout.msibook_dqaweekly_version_popup_window,(ViewGroup) findViewById(R.id.popup_version));

            DisplayMetrics metrics = getResources().getDisplayMetrics();
            int screenWidth = (int) (metrics.widthPixels * 0.8);
            int screenHeight = (int) (metrics.heightPixels * 0.78);

            pwindo = new PopupWindow(layout,screenWidth,screenHeight,true);
            pwindo.showAtLocation(layout, Gravity.CENTER,0,0);

            btn_close_pop = (Button) layout.findViewById(R.id.btn_closepop);
            btn_close_pop.setOnClickListener(cancel_button_click_listener);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private PopupWindow pwindoinfo;

    private void infoPopupWindow() {
        try {
            // We need to get the instance of the LayoutInflater
            LayoutInflater inflater = (LayoutInflater) msibook_dqaweekly_project_activity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View layout = inflater.inflate(R.layout.msibook_dqaweekly_info_popup_window, (ViewGroup) findViewById(R.id.popup_element));

            DisplayMetrics metrics = getResources().getDisplayMetrics();
            int screenWidth = (int) (metrics.widthPixels * 0.8);
            int screenHeight = (int) (metrics.heightPixels * 0.78);

            pwindoinfo = new PopupWindow(layout, screenWidth, screenHeight, true);
            pwindoinfo.showAtLocation(layout, Gravity.CENTER, 0, 0);

            btn_close_popinfo = (Button) layout.findViewById(R.id.close_info);
            btn_close_popinfo.setOnClickListener(cancelinfo_button_click_listener);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private View.OnClickListener cancelinfo_button_click_listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            pwindoinfo.dismiss();
        }
    };

    private View.OnClickListener cancel_button_click_listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            pwindo.dismiss();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msibook_dqaweekly_project_activity);

        CheckTab_number = 0;
        DpSpinnerPosition = 0;
        //Spinner 給定Adapter時 監聽都會跑一次 所以初始頁載入時需做判斷 不然List會跑兩次
        CheckPagefirst_week = false;
        CheckPagefirst_dp = false;

        Tab_Scroll = false;

        mSectionsPagerAdapter1 = new SectionsPagerAdapter1(getSupportFragmentManager());
        mSectionsPagerAdapter2 = new SectionsPagerAdapter2(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager1 = (ViewPager) findViewById(R.id.container1);
//        mViewPager1.setOffscreenPageLimit(0);        //设置viewpager的缓存页面的个数
        mViewPager1.setAdapter(mSectionsPagerAdapter1);

        mViewPager2 = (ViewPager) findViewById(R.id.container2);
//        mViewPager2.setOffscreenPageLimit(0);        //设置viewpager的缓存页面的个数
        mViewPager2.setAdapter(mSectionsPagerAdapter2);

        Calendar yy = Calendar.getInstance();
        Year = String.valueOf(yy.get(Calendar.YEAR));

        //滑動選單tabs
        tabLayout1 = (TabLayout) findViewById(R.id.tabs1);
        tabLayout1.setupWithViewPager(mViewPager1);
        //tabLayout.setTabMode(TabLayout.MODE_FIXED); //鎖定
        tabLayout1.setTabGravity(TabLayout.GRAVITY_FILL);

        //滑動選單tabs
        tabLayout2 = (TabLayout) findViewById(R.id.tabs2);
        tabLayout2.setupWithViewPager(mViewPager2);
        //tabLayout.setTabMode(TabLayout.MODE_FIXED);
        tabLayout2.setTabGravity(TabLayout.GRAVITY_FILL);

        mChart1 = (PieChart) findViewById(R.id.chart1);
        mChart2 = (PieChart) findViewById(R.id.chart2);
        mChart1.setUsePercentValues(true);
        mChart2.setUsePercentValues(true);
        mChart1.getDescription().setEnabled(false);
        mChart2.getDescription().setEnabled(false);
        mChart1.setExtraOffsets(5, 5, 5, 5);
        mChart2.setExtraOffsets(5, 10, 5, 5);

        mChart1.setDragDecelerationFrictionCoef(0.95f);
        mChart2.setDragDecelerationFrictionCoef(0.95f);

        //tf = Typeface.createFromAsset(getAssets(), "OpenSans-Regular.ttf");

        //mChart.setCenterTextTypeface(Typeface.createFromAsset(getAssets(), "OpenSans-Light.ttf"));
//        mChart1.setCenterText(generateCenterSpannableText());
//        mChart2.setCenterText(generateCenterSpannableText());

        mChart1.setExtraOffsets(0.f, 0.f, 0.f, 0.f);
        mChart2.setExtraOffsets(0.f, 0.f, 0.f, 0.f);

        mChart1.setDrawHoleEnabled(true);
        mChart2.setDrawHoleEnabled(true);


        mChart1.setTransparentCircleColor(Color.WHITE);
        mChart2.setTransparentCircleColor(Color.WHITE);
        mChart1.setTransparentCircleAlpha(110);
        mChart2.setTransparentCircleAlpha(110);

        mChart1.setHoleRadius(75f);
        mChart2.setHoleRadius(90f);
        mChart1.setTransparentCircleRadius(61f);
        mChart2.setTransparentCircleRadius(90f);

        mChart1.setDrawCenterText(true);
        mChart2.setDrawCenterText(true);

        mChart1.setRotationAngle(220f);
        mChart2.setRotationAngle(0);
        // enable rotation of the chart by touch
        mChart1.setRotationEnabled(true);
        mChart2.setRotationEnabled(true);
        mChart1.setHighlightPerTapEnabled(true);
        mChart2.setHighlightPerTapEnabled(true);

        // mChart.setUnit(" €");
        // mChart.setDrawUnitsInChart(true);

        // add a selection listener
        mChart1.setOnChartValueSelectedListener(this);
        mChart2.setOnChartValueSelectedListener(this);

        mChart1.animateY(1400, Easing.EasingOption.EaseInOutQuad);
        mChart2.animateY(1400, Easing.EasingOption.EaseInOutQuad);
        // mChart.spin(2000, 0, 360);

        Legend l = mChart1.getLegend();
        Legend l2 = mChart2.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setEnabled(false);

        l2.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l2.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l2.setOrientation(Legend.LegendOrientation.VERTICAL);
        l2.setDrawInside(false);
        l2.setEnabled(false);

        //isUpdate(this);

        lindepart = (LinearLayout) findViewById(R.id.linear_spinnerdp);

//        //開啟或建立資料庫
//        db = openOrCreateDatabase(db_name, Context.MODE_PRIVATE, null);
//        String createTable = "CREATE TABLE IF NOT EXISTS " +
//                tb_name +
//                "(checkfirst VARCHAR(32))";
//        db.execSQL(createTable);//建立資料表
//
//        //呼叫自訂的addData()寫入資料
//        //tvx.setText("資料庫檔路徑:"+db.getPath()+"\n" + "資料庫分頁大小:"+db.getPageSize() + "Bytes\n" + "資料上限:"+ db.getMaximumSize()+ "Bytes\n");
//        Cursor c = db.rawQuery("SELECT  *  FROM " + tb_name, null);//查詢tb_name資料表中的所有資料
//        Log.w("幾筆資料", String.valueOf(c.getCount()));
//
//        if (c.getCount() == 0) {
//            addData("1");
//            c = db.rawQuery("SELECT * FROM " + tb_name, null);
//            Intent intent = new Intent();
//            intent.setClass(msibook_dqaweekly_project_activity.this, msibook_dqaweekly_Main_ViewPager.class);//跳轉頁面至第二頁
//            //開啟Activity
//            startActivity(intent);
//            db.close();
//        }

        tesView = (TextView) findViewById(R.id.textView123);
        progressBar = new ProgressDialog(this);
        progressBar.setCancelable(true);
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.setMessage("資料載入中");

        btn_open_pop = (Button) findViewById(R.id.btn_creat_pop);//漢堡列按鈕

//        if(UserData.Region.contains("MSIT")){
//            btn_open_pop.setVisibility(View.VISIBLE);
//        }else{
//            btn_open_pop.setVisibility(View.INVISIBLE);
//        }

        //漢堡列點選事件
        btn_open_pop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                addAnimation();//圖片旋轉動畫
                if (popupWindow == null) {
                    showPopupWindow();//顯示popwindow
                } else if (popupWindow.isShowing()) {
                    popupWindow.dismiss();
                } else {
                    popupWindow.showAsDropDown(lindepart, -300, 0);
                }

            }
        });

        spinner_week = (Spinner) findViewById(R.id.spinner_week);//宣告週次下拉選單
        spinnerdp = (Spinner) findViewById(R.id.spinner_depart);//宣告部門下拉選單

        btnback = (Button) findViewById(R.id.btn_spinnerdp_back);
        btnnext = (Button) findViewById(R.id.btn_spinnerdp_next);
        LinearLayout Lnl_Human_Resource = (LinearLayout) findViewById(R.id.Lnl_Human_Resource);

        Find_Get_Week();



        //跳下一個部門
        btnnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (popupWindow == null) {
                    if (ArrayDeptID.size() == spinnerdp.getSelectedItemPosition() + 1) {
                        //don't doing
                    } else {
                        spinnerdp.setSelection(spinnerdp.getSelectedItemPosition() + 1);
                    }
                } else if (popupWindow.isShowing()) {
                    popupWindow.dismiss();
                } else {
                    if (ArrayDeptID.size() == spinnerdp.getSelectedItemPosition() + 1) {
                        //don't doing
                    } else {
                        spinnerdp.setSelection(spinnerdp.getSelectedItemPosition() + 1);
                    }
                }
//                if(spinnerdp.getSelectedItemPosition() < spinnerdp.getAdapter().getCount()){
//
//                }
            }
        });


        //跳上一個部門
        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (popupWindow == null) {
                    spinnerdp.setSelection(spinnerdp.getSelectedItemPosition() - 1);
                } else if (popupWindow.isShowing()) {
                    popupWindow.dismiss();
                } else {
                    spinnerdp.setSelection(spinnerdp.getSelectedItemPosition() - 1);
                }
//                if(spinnerdp.getSelectedItemPosition() > spinnerdp.getAdapter().getCount()){
//
//                }
            }
        });


        Lnl_Human_Resource.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


//                Intent intent = new Intent(msibook_dqaweekly_project_activity.this,ProjectActivity2.class);//跳轉頁面至第二頁
//                Bundle extras = new Bundle();
//
//                extras.putInt("Position",spinnerdp.getSelectedItemPosition());
//
//                intent.putExtras(extras);
//
//
//                //開啟Activity
//                startActivity(intent);
            }
        });
        mViewPager1.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                switch (position) {
                    case 0:
                        CheckTab_number = 0;
                        if(ArrayWeek!=null && ArrayWeek.size() > 0) {
                            //專案
                            msibook_dqaweekly_project_msr_project Tab1 = (msibook_dqaweekly_project_msr_project) mSectionsPagerAdapter1.getFragment(position);
                            Tab1.Find_Mothly_MSR(ArrayWeek.get(spinner_week.getSelectedItemPosition()).replace("週", ""), ArrayYear.get(spinner_week.getSelectedItemPosition()),String.valueOf(DpSpinnerPosition));
                            Tab1.SetYear(ArrayYear.get(spinner_week.getSelectedItemPosition()));
                            Tab1.SetWeek(ArrayWeek.get(spinner_week.getSelectedItemPosition()).replace("週", ""));
                            //Tab1.SetRegionID(String.valueOf(spinnerdp.getSelectedItemPosition() + 1));
                            Tab1.SetRegionID(ArrayDeptID.get(spinnerdp.getSelectedItemPosition()));
                            break;
                        }
                    case 1:

                        CheckTab_number = 1;
                        if(ArrayWeek!=null && ArrayWeek.size() > 0){
                        //資源
                            msibook_dqaweekly_project_activity_resource Tab3 = (msibook_dqaweekly_project_activity_resource) mSectionsPagerAdapter1.getFragment(position);
                            Tab3.Find_Weekly_utilization_Total(ArrayWeek.get(spinner_week.getSelectedItemPosition()).replace("週", ""),ArrayYear.get(spinner_week.getSelectedItemPosition()), ArrayDeptID.get(spinnerdp.getSelectedItemPosition()));
                            Tab3.Find_Resource_LsetWeek(ArrayWeek.get(spinner_week.getSelectedItemPosition()).replace("週", ""),ArrayYear.get(spinner_week.getSelectedItemPosition()), ArrayDeptID.get(spinnerdp.getSelectedItemPosition()),"All");
                            Tab3.Find_Total_Cost(ArrayWeek.get(spinner_week.getSelectedItemPosition()).replace("週", ""),ArrayYear.get(spinner_week.getSelectedItemPosition()), ArrayDeptID.get(spinnerdp.getSelectedItemPosition()),"All");
                            //Log.w("Tab資源","Tab資源");
                            Log.w("ArrayWeek",String.valueOf(ArrayWeek.get(spinner_week.getSelectedItemPosition()).replace("週", "")));
                            Log.w("ArrayDeptID",String.valueOf(ArrayWeek.get(spinner_week.getSelectedItemPosition()).replace("週", "")));
                        }

                        break;
                    case 2:
                        CheckTab_number = 2;
                        //稼動率
                        if(ArrayWeek!=null && ArrayWeek.size() > 0) {
                            msibook_dqaweekly_project_activity_utilization Tab2 = (msibook_dqaweekly_project_activity_utilization) mSectionsPagerAdapter1.getFragment(position);
                            Tab2.Find_Weekly_utilization_Total(ArrayWeek.get(spinner_week.getSelectedItemPosition()).replace("週", ""), ArrayYear.get(spinner_week.getSelectedItemPosition()), ArrayDeptID.get(spinnerdp.getSelectedItemPosition()));
                            if (ArrayDeptID.get(spinnerdp.getSelectedItemPosition()).indexOf("1") == -1) {
                                //昆山寶安
                                //CheckTab_number=0;
                                Tab2.Find_Week_Utilization(ArrayWeek.get(spinner_week.getSelectedItemPosition()).replace("週", ""), ArrayYear.get(spinner_week.getSelectedItemPosition()), ArrayDeptID.get(spinnerdp.getSelectedItemPosition()));
                            } else {
                                //選台北
                                Tab2.Find_Week_Utilization(ArrayWeek.get(spinner_week.getSelectedItemPosition()).replace("週", ""), ArrayYear.get(spinner_week.getSelectedItemPosition()), "19762");
                            }
                            Tab2.SetYear(ArrayYear.get(spinner_week.getSelectedItemPosition()));
                            Tab2.SetWeek(ArrayWeek.get(spinner_week.getSelectedItemPosition()).replace("週", ""));
                            break;
                        }

                    case 3:
                        CheckTab_number = 3;
                        if(ArrayWeek!=null && ArrayWeek.size() > 0) {
                            //專案
                            msibook_dqaweekly_project_activity_project Tab1 = (msibook_dqaweekly_project_activity_project) mSectionsPagerAdapter1.getFragment(position);
                            Tab1.Find_BU_PM_Rate_LastWeek(ArrayWeek.get(spinner_week.getSelectedItemPosition()).replace("週", ""), ArrayYear.get(spinner_week.getSelectedItemPosition()), ArrayDeptID.get(spinnerdp.getSelectedItemPosition()),"All");
                            Tab1.SetYear(ArrayYear.get(spinner_week.getSelectedItemPosition()));
                            Tab1.SetWeek(ArrayWeek.get(spinner_week.getSelectedItemPosition()).replace("週", ""));
                            //Tab1.SetRegionID(String.valueOf(spinnerdp.getSelectedItemPosition() + 1));
                            Tab1.SetRegionID(ArrayDeptID.get(spinnerdp.getSelectedItemPosition()));
                            break;
                        }

                        break;
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mViewPager2.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                switch (position) {
                    case 0:
                        CheckTab_number = 5;
                        if(ArrayWeek!=null && ArrayWeek.size() > 0) {
                            //新增專案
                                msibook_dqaweekly_project_activity_new_project Tab5_5 = (msibook_dqaweekly_project_activity_new_project) mSectionsPagerAdapter2.getFragment(position);
                                Tab5_5.Find_PM_NewModel(ArrayWeek.get(spinner_week.getSelectedItemPosition()).replace("週", ""), ArrayYear.get(spinner_week.getSelectedItemPosition()));
                                //Tab5_5.Find_Fac_List(Year,ArrayWeek.get(spinner_week.getSelectedItemPosition()).replace("週", ""));
                                //Tab5_5.Find_Fac_Data(ArrayWeek.get(spinner_week.getSelectedItemPosition()).replace("週", ""));
                                //Log.w("Tab工廠生產會報","Tab工廠生產會報");
                            break;
                        }
                    case 1:
                        CheckTab_number = 6;
                        if(ArrayWeek!=null && ArrayWeek.size() > 0) {
                            //彙報
                            msibook_dqaweekly_project_activity_fac_data Tab6_6 = (msibook_dqaweekly_project_activity_fac_data) mSectionsPagerAdapter2.getFragment(position);
                            Tab6_6.Find_Fac_List_LastWeek(ArrayYear.get(spinner_week.getSelectedItemPosition()), ArrayWeek.get(spinner_week.getSelectedItemPosition()).replace("週", ""));
                            //Tab5_5.Find_Fac_Data(ArrayWeek.get(spinner_week.getSelectedItemPosition()).replace("週", ""));
                            //Log.w("Tab工廠生產會報","Tab工廠生產會報");
                            break;
                        }
                    case 2:
                        CheckTab_number = 7;
                        if(ArrayWeek!=null && ArrayWeek.size() > 0) {
//                            if(ArrayWeek.size() > 0) {
                            //抽驗報告
                            msibook_dqaweekly_project_activity_lottery_report Tab7_7 = (msibook_dqaweekly_project_activity_lottery_report) mSectionsPagerAdapter2.getFragment(position);
                            Tab7_7.SetWeek(ArrayWeek.get(spinner_week.getSelectedItemPosition()).replace("週", ""));
                            Tab7_7.SetYear(ArrayYear.get(spinner_week.getSelectedItemPosition()));
                            //Tab7_7.Find_Fac_List(Year,ArrayWeek.get(spinner_week.getSelectedItemPosition()).replace("週", ""));
                            //Tab5_5.Find_Fac_Data(ArrayWeek.get(spinner_week.getSelectedItemPosition()).replace("週", ""));
                            //Log.w("Tab工廠生產會報","Tab工廠生產會報");
                            break;
                        }
                    case 3:
                        CheckTab_number = 8;
                        if(ArrayWeek!=null && ArrayWeek.size() > 0) {
                            //工作負載 - 專案成本報告
                            msibook_dqaweekly_project_activity_jobs_load Tab8_8 = (msibook_dqaweekly_project_activity_jobs_load) mSectionsPagerAdapter2.getFragment(position);
                            Tab8_8.SetWeek(ArrayWeek.get(spinner_week.getSelectedItemPosition()).replace("週", ""));
                            Tab8_8.SetYear(ArrayYear.get(spinner_week.getSelectedItemPosition()));
                            Tab8_8.Find_Week_Utilization_Job_Load(ArrayWeek.get(spinner_week.getSelectedItemPosition()).replace("週", ""), ArrayYear.get(spinner_week.getSelectedItemPosition()), "19762");
                            //Tab7_7.Find_Fac_List(Year,ArrayWeek.get(spinner_week.getSelectedItemPosition()).replace("週", ""));
                            //Tab5_5.Find_Fac_Data(ArrayWeek.get(spinner_week.getSelectedItemPosition()).replace("週", ""));
                            //Log.w("Tab工廠生產會報","Tab工廠生產會報");
                            break;
                        }
                    case 4:
                        CheckTab_number = 9;
                        if(ArrayWeek!=null && ArrayWeek.size() > 0) {
                            //排行榜
                            msibook_dqaweekly_project_activity_rank Tab9_9 = (msibook_dqaweekly_project_activity_rank) mSectionsPagerAdapter2.getFragment(4);
                            Tab9_9.Find_RSS_Rank(ArrayYear.get(spinner_week.getSelectedItemPosition()), ArrayWeek.get(spinner_week.getSelectedItemPosition()).replace("週", ""));
                            break;
                        }

                    case 5:
                        CheckTab_number = 10;
                        if(ArrayWeek!=null && ArrayWeek.size() > 0) {
                            //排行榜
                            msibook_dqaweekly_find_important_message Tab10_10 = (msibook_dqaweekly_find_important_message) mSectionsPagerAdapter2.getFragment(5);
                            Tab10_10.Find_Important_Message(ArrayYear.get(spinner_week.getSelectedItemPosition()), ArrayWeek.get(spinner_week.getSelectedItemPosition()).replace("週", ""));

                            break;
                        }

                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        //Tablay 監聽事件  到當下頁面才會執行
        tabLayout1.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

//                if(tab.getPosition() == 0 ) {
//                    ProjectActivity_project Tab1 = (ProjectActivity_project) mSectionsPagerAdapter.getFragment(tab.getPosition());
//                    Tab1.Find_BU_PM_Rate(ArrayWeek.get(spinner_week.getSelectedItemPosition()).replace("週", ""), ArrayDeptID.get(spinnerdp.getSelectedItemPosition()));
//                    Tab1.Find_PM_NewModel(ArrayWeek.get(spinner_week.getSelectedItemPosition()).replace("週", ""));
//                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        //Tablay 監聽事件  到當下頁面才會執行
        tabLayout2.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

//                if(tab.getPosition() == 0 ) {
//                    ProjectActivity_project Tab1 = (ProjectActivity_project) mSectionsPagerAdapter.getFragment(tab.getPosition());
//                    Tab1.Find_BU_PM_Rate(ArrayWeek.get(spinner_week.getSelectedItemPosition()).replace("週", ""), ArrayDeptID.get(spinnerdp.getSelectedItemPosition()));
//                    Tab1.Find_PM_NewModel(ArrayWeek.get(spinner_week.getSelectedItemPosition()).replace("週", ""));
//                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });



    }


    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        private  View rootView; //緩存Fragment view
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            int Position = getArguments().getInt(ARG_SECTION_NUMBER);

            //緩存的rootView需要判斷是否已經被加過parent， 如果有parent需要從parent刪除，要不然會發生這個rootview已經有parent的錯誤。
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if  (parent !=  null ) {
                parent.removeView(rootView);
            }

            //宣告選定的fragment 從case 0 開始
            View rootView = inflater.inflate(R.layout.fragment_msibook_dqaweekly_project_msr_project, container, false);

            switch (Position) {
                case 0:
                    rootView = inflater.inflate(R.layout.fragment_msibook_dqaweekly_project_msr_project, container, false);
                    return rootView;
                case 1:
                    rootView = inflater.inflate(R.layout.fragment_msibook_dqaweekly_project_activity_resource, container, false);
                    return rootView;
                case 2:
//                    rootView = inflater.inflate(R.layout.fragment_msibook_dqaweekly_project_activity_overtime, container, false);
//                    return rootView;
                case 3:
                    rootView = inflater.inflate(R.layout.fragment_msibook_dqaweekly_project_activity_utilization, container, false);
                    return rootView;
                case 4:
                    rootView = inflater.inflate(R.layout.fragment_msibook_dqaweekly_project_activity_project, container, false);
                    return rootView;
                case 5:
                    rootView = inflater.inflate(R.layout.fragment_msibook_dqaweekly_project_activity_new_project, container, false);
                    return rootView;
                case 6:
                    rootView = inflater.inflate(R.layout.fragment_msibook_dqaweekly_project_activity_fac_data, container, false);
                    return rootView;
                case 7:
                    rootView = inflater.inflate(R.layout.fragment_msibook_dqaweekly_project_activity_lottery_report, container, false);
                    return rootView;
                case 8:
                    rootView = inflater.inflate(R.layout.fragment_msibook_dqaweekly_project_activity_rank, container, false);
                    return rootView;
            }

//            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
//            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }
    }




    //SectionsPagerAdapter 選擇業面控制 FragmentPager *********  SectionsPagerAdapter1
    public class SectionsPagerAdapter1 extends FragmentPagerAdapter {

        private FragmentManager mFragmentManager;
        private Map<Integer, String> mFragmentTags;

        public int RegionIndex;

        public SectionsPagerAdapter1(FragmentManager fm) {
            super(fm);
            mFragmentTags = new HashMap<Integer, String>();
            mFragmentManager = fm;
        }

        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        public Fragment getFragment(int position) {
            String tag = mFragmentTags.get(position);

            if (tag == null)
                return null;
            return mFragmentManager.findFragmentByTag(tag);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            Fragment TabFragment = new Fragment();

            switch (position) {
                case 0:
                    Log.w("Tab專案","Tab專案");
                    return msibook_dqaweekly_project_msr_project.newInstance("0", "0");
                case 1:
                    Log.w("Tab資源","Tab資源");
                    return msibook_dqaweekly_project_activity_resource.newInstance("0", "0");
                case 2:
                    Log.w("Tab稼動率","Tab稼動率");
                    return msibook_dqaweekly_project_activity_utilization.newInstance("0", "0");
                case 3:
                    Log.w("Tab工作負載","工作負載");
                    return msibook_dqaweekly_project_activity_project.newInstance("0", "0");
            }



            return TabFragment;

        }

        @Override
        public int getCount() {
            return 3;

        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Object obj = super.instantiateItem(container, position);

            if (obj instanceof Fragment) {
                // record the fragment tag here.
                Fragment f = (Fragment) obj;
                String tag = f.getTag();
                mFragmentTags.put(position, tag);
            }
            return obj;
        }

        @Override
        public CharSequence getPageTitle(int position) {

            switch (position) {
                case 0:
                    return "管銷研";
                case 1:
                    return "資源";
                case 2:
                    return "稼動率";
                case 3:
                    return "工作負載";
            }

            return null;
        }
    }

    //SectionsPagerAdapter 選擇業面控制 FragmentPager *********  SectionsPagerAdapter2
    public class SectionsPagerAdapter2 extends FragmentPagerAdapter {


        private FragmentManager mFragmentManager;
        private Map<Integer, String> mFragmentTags;

        public int RegionIndex;

        public SectionsPagerAdapter2(FragmentManager fm) {
            super(fm);
            mFragmentTags = new HashMap<Integer, String>();
            mFragmentManager = fm;
        }

        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        public Fragment getFragment(int position) {
            String tag = mFragmentTags.get(position);



            if (tag == null)
                return null;
            return mFragmentManager.findFragmentByTag(tag);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            Fragment TabFragment = new Fragment();

            switch (position) {
                case 0:
                    Log.w("Tab新增專案","Tab新增專案");
                    return msibook_dqaweekly_project_activity_new_project.newInstance("0", "0");
                case 1:
                    Log.w("Tab彙報","Tab彙報");
                    return msibook_dqaweekly_project_activity_fac_data.newInstance("0", "0");
                case 2:
                    Log.w("Tab抽驗報告","Tab抽驗報告");
                    return msibook_dqaweekly_project_activity_lottery_report.newInstance("0", "0");
                case 3:
                    Log.w("Tab工作負載 - 專案成本報告","Tab工作負載 - 專案成本報告");
                    return msibook_dqaweekly_project_activity_jobs_load.newInstance("0", "0");
                case 4:
                    Log.w("Tab排行榜","Tab排行榜");
                    return msibook_dqaweekly_project_activity_rank.newInstance("0", "0");
                case 5:
                    Log.w("Tab重大訊息","Tab重大訊息");
                    return msibook_dqaweekly_find_important_message.newInstance("0", "0");
            }

            return TabFragment;

        }

        @Override
        public int getCount() {
            return 6;

        }
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Object obj = super.instantiateItem(container, position);

            if (obj instanceof Fragment) {
                // record the fragment tag here.
                Fragment f = (Fragment) obj;
                String tag = f.getTag();
                mFragmentTags.put(position, tag);
            }
            return obj;
        }

        @Override
        public CharSequence getPageTitle(int position) {

            switch (position) {
                case 0:
                    //setTitle("加班紀錄");
                    return " 新增專案 ";
                case 1:
                    //setTitle("加班紀錄");
                    return " 工廠生產彙報 ";
                case 2:
                    //setTitle("加班紀錄");
                    return "  抽驗報告  ";
                case 3:
                    //setTitle("加班紀錄");
                    return "專案成本報告";
                case 4:
                    //setTitle("加班紀錄");
                    return "排行榜";
                case 5:
                    //setTitle("加班紀錄");
                    return "重大訊息";
            }
            return null;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.pie, menu);
        return true;
    }

    private SpannableString generateCenterSpannableText(String Title, int a, int b, int c) {

        SpannableString s = new SpannableString(Title);
        s.setSpan(new ForegroundColorSpan(Color.rgb(a,b,c)), 0, s.length()-5, 0);
        s.setSpan(new ForegroundColorSpan(Color.rgb(101, 101, 101)), s.length()-5, s.length(), 0);
        s.setSpan(new RelativeSizeSpan(0.5f), s.length()-5, s.length(), s.SPAN_EXCLUSIVE_EXCLUSIVE);

        return s;
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {

        if (e == null)
            return;
        Log.i("VAL SELECTED",
                "Value: " + e.getY() + ", xIndex: " + e.getX()
                        + ", DataSet index: " + h.getDataSetIndex());
    }

    @Override
    public void onNothingSelected() {
        Log.i("PieChart", "nothing selected");
    }

    //阿德之前做的
    private void Find_BU_PM_Content(String Week, String Region) {

        RequestQueue mQueue = Volley.newRequestQueue(this);


        getString("http://wtsc.msi.com.tw/IMS/Weekly_Report.asmx/Find_BU_PM_Content?Week=" + Week + "&RegionID=" + Region, mQueue, new GetServiceData.VolleyCallback() {
            @Override
            public void onSuccess(JSONObject result) {

                try {

                    JSONArray UserArray = new JSONArray(result.getString("Key"));

                    ArrayList<PieEntry> entries = new ArrayList<PieEntry>();

                    // NOTE: The order of the entries when being added to the entries array determines their position around the center of
                    // the chart.

                    for (int i = 0; i < UserArray.length(); i++) {
                        JSONObject IssueData = UserArray.getJSONObject(i);
                        if(IssueData.getString("M_Type")=="null")

                        {


                        }
                        else{
                            String M_Type = IssueData.getString("M_Type");


                            Double Cnt = IssueData.getDouble("Cnt");

                            float a = Cnt.floatValue();

                            entries.add(new PieEntry(a, M_Type));}


                    }


                    PieDataSet dataSet = new PieDataSet(entries, "Election Results");

                    dataSet.setSliceSpace(3f);
                    dataSet.setSelectionShift(5f);


                    // add a lot of colors

                    ArrayList<Integer> colors = new ArrayList<Integer>();

                    colors.add(Color.parseColor("#527ca2"));
                    colors.add(Color.parseColor("#7497b5"));
                    colors.add(Color.parseColor("#97b1c7"));
                    colors.add(Color.parseColor("#b9cbda"));
                    colors.add(Color.parseColor("#cbd8e3"));

                    colors.add(ColorTemplate.getHoloBlue());

                    dataSet.setColors(colors);
                    //dataSet.setSelectionShift(0f);


                    dataSet.setValueLinePart1OffsetPercentage(80.f);
                    dataSet.setValueLinePart1Length(0.3f);
                    dataSet.setValueLinePart2Length(0.4f);

                    //dataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
                    dataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);

                    PieData data = new PieData(dataSet);
                    data.setValueFormatter(new PercentFormatter());
                    data.setValueTextSize(18f);
                    data.setValueTypeface(tf);



                    mChart1.setData(data);


                    // undo all highlights
                    mChart1.highlightValues(null);


                    mChart1.invalidate();


                } catch (JSONException ex) {

                }

            }
        });

    }

    //阿德之前做的
    private void Find_Weekly_utilization_Total(final String Week, final String Region) {


        RequestQueue mQueue = Volley.newRequestQueue(this);


        getString("http://wtsc.msi.com.tw/IMS/Weekly_Report.asmx/Find_Weekly_utilization_Total?Week=" + Week + "&RegionID=" + Region, mQueue, new GetServiceData.VolleyCallback() {
            @Override
            public void onSuccess(JSONObject result) {

                try {

                    JSONArray UserArray = new JSONArray(result.getString("Key"));

                    ArrayList<PieEntry> entries = new ArrayList<PieEntry>();

                    // NOTE: The order of the entries when being added to the entries array determines their position around the center of
                    // the chart.

                    if (UserArray.length() > 0) {


                        JSONObject IssueData = UserArray.getJSONObject(0);

                        if (!IssueData.isNull("utilization")) {
                            Double utilization = IssueData.getDouble("utilization");

                            float a = utilization.floatValue() / 100;


                            if (a > 1) {
                                entries.add(new PieEntry(a, ""));
                            } else {
                                entries.add(new PieEntry(a, ""));

                                entries.add(new PieEntry(1 - a, ""));
                            }

                            if((IssueData.getDouble("utilization") /100)>1){

                                mChart2.setCenterText(generateCenterSpannableText(String.format("%.2f", utilization)+"%"+"\n稼動率",240,98,93));

                                mChart2.setCenterTextSize(35);

                            }
                            else if ((IssueData.getDouble("utilization") /100)<1){

                                mChart2.setCenterText(generateCenterSpannableText(String.format("%.2f", utilization)+"%"+"\n稼動率",70,170,54));
                                mChart2.setCenterTextSize(35);

                            }
                            else if ((IssueData.getDouble("utilization") /100)<=1 && (IssueData.getDouble("utilization") /100>=0.9)){

                                mChart2.setCenterText(generateCenterSpannableText(String.format("%.2f", utilization)+"%"+"\n稼動率",101, 101, 101));

                                mChart2.setCenterTextSize(35);

                            }



                        }
                        else
                        {
                            entries.add( new PieEntry(0,"稼動率"));
                            mChart2.setCenterText("資料庫未連結");
                            mChart2.setCenterTextColor(Color.rgb(149, 148, 148));
                            mChart2.setCenterTextSize(25);
                        }

                        int TotalMan = IssueData.getInt("TotalMan");

                        int ExpectMan = IssueData.getInt("ExpectMan");


                        TextView text_setresourcenum = (TextView) findViewById(R.id.text_setresourcenum);

                        TextView text_realresourcenum = (TextView) findViewById(R.id.text_realresourcenum);

                        text_setresourcenum.setText(String.valueOf(ExpectMan));

                        text_realresourcenum.setText(String.valueOf(TotalMan));

                        if (Integer.valueOf(text_setresourcenum.getText().toString()) > Integer.valueOf(text_realresourcenum.getText().toString())) {
                            text_realresourcenum.setTextColor(Color.parseColor("#2e3134"));

                        } else {
                            text_realresourcenum.setTextColor(Color.parseColor("#f0625d"));

                        }


                    }


                    PieDataSet dataSet = new PieDataSet(entries, "Election Results");
                    dataSet.setSliceSpace(3f);
                    dataSet.setSelectionShift(5f);


                    // add a lot of colors

                    ArrayList<Integer> colors = new ArrayList<Integer>();
                    colors.add(Color.parseColor("#527ca2"));


                    colors.add(Color.parseColor("#848484"));

                    colors.add(ColorTemplate.getHoloBlue());

                    dataSet.setColors(colors);
                    //dataSet.setSelectionShift(0f);

                    dataSet.setValueLineWidth(0);
                    dataSet.setDrawValues(false);
//                    dataSet.setValueLinePart1OffsetPercentage(90.f);
//                    dataSet.setValueLinePart1Length(0.2f);
//                    dataSet.setValueLinePart2Length(0.4f);
                    //dataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
                    //dataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);

                    PieData data = new PieData(dataSet);
                    data.setValueFormatter(new PercentFormatter());
                    data.setValueTextSize(11f);
                    data.setValueTextColor(Color.BLACK);
                    data.setValueTypeface(tf);

                    mChart2.setData(data);

                    // undo all highlights

                    mChart2.highlightValues(null);


                    mChart2.invalidate();


                } catch (JSONException ex) {

                    Toast.makeText(msibook_dqaweekly_project_activity.this, ex.toString(), Toast.LENGTH_SHORT).show();
                }

            }
        });

    }



    //----------------Item-----------------


    //-------------Adapter--------------
    public class DropDownAdapter extends BaseAdapter {

        private LayoutInflater mLayInf;

        private List<String> ArrayDeptName;

        private Context ProjectContext;

        public int selectedItemdp;

        public DropDownAdapter(Context context,  List<String> ArrayDeptName)
        {
            mLayInf = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            ProjectContext = context;

            this.ArrayDeptName = ArrayDeptName;
        }
        @Override
        public int getCount() {
            return ArrayDeptName.size();
        }

        @Override
        public Object getItem(int position) {
            return ArrayDeptName.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View v = new View(ProjectContext);

            v = mLayInf.inflate(R.layout.msibook_dqaweekly_spinnertextlayout, parent, false);

            TextView style = (TextView) v.findViewById(R.id.style);

            style.setText(ArrayDeptName.get(position));

            Log.w("test","test");

            return v;
        }
        @Override
        public View getDropDownView(int position,View convertView,ViewGroup parent)
        {
            View v = null;
            v = mLayInf.inflate(R.layout.msibook_dqaweekly_spinnertextlayout, parent, false);

            TextView style = (TextView) v.findViewById(R.id.style);

            style.setText(ArrayDeptName.get(position));

            // If this is the selected item position
            if (position == selectedItemdp) {
                style.setBackgroundColor(Color.parseColor("#d94045"));//灰色 848484
            }
            else {
                // for other views
                style.setBackgroundColor(Color.parseColor("#d21e25"));//藍色 618db5
            }
            return v;
        }

    }

    public class DropDownAdapterweek extends BaseAdapter {

        private LayoutInflater mLayInf;

        private List<String> ArrayDeptName;

        private Context ProjectContext;

        public int selectedItemweek;

        public DropDownAdapterweek(Context context,  List<String> ArrayDeptName)
        {
            mLayInf = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            ProjectContext = context;

            this.ArrayDeptName = ArrayDeptName;
        }

        @Override
        public int getCount() {
            return ArrayDeptName.size();
        }

        @Override
        public Object getItem(int position) {
            return ArrayDeptName.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View v = new View(ProjectContext);

            v = mLayInf.inflate(R.layout.msibook_dqaweekly_spinnertextlayout2, parent, false);

            TextView style = (TextView) v.findViewById(R.id.style2);

            style.setText(ArrayDeptName.get(position));

            Log.w("test","test");

            return v;
        }
        @Override
        public View getDropDownView(int position,View convertView,ViewGroup parent)
        {
            View v = null;
            v = mLayInf.inflate(R.layout.msibook_dqaweekly_spinnertextlayout, parent, false);

            TextView style = (TextView) v.findViewById(R.id.style);

            style.setText(ArrayDeptName.get(position));

            // If this is the selected item position
            if (position == selectedItemweek) {
                style.setBackgroundColor(Color.parseColor("#d94045"));//灰色 #848484
            }
            else {
                // for other views
                style.setBackgroundColor(Color.parseColor("#d21e25"));//藍色 #618db5
            }
            return v;
        }

    }

}