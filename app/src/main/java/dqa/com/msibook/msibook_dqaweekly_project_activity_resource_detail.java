package dqa.com.msibook;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class msibook_dqaweekly_project_activity_resource_detail extends AppCompatActivity {

    private SectionsPagerAdapter1 mSectionsPagerAdapter1;

    private ViewPager mViewPager1;

    private Integer CheckTab_number;
    private Boolean CheckPagefirst_week;
    private Boolean CheckPagefirst_dp;

    private String SetF_Year;
    private String SetWeek;

    private Typeface tf;

    private ListView mListView;
    private String test456;
    private TextView tesView;

    private DropDownAdapter mDropDownAdapter;
    private DropDownAdapterweek mDropDownAdapterweek;
    public List<String> ArrayYear = new ArrayList<String>();
    public List<String> ArrayWeek = new ArrayList<String>();
    private List<String> ArrayDeptName = new ArrayList<String>();
    public List<String> ArrayDeptID = new ArrayList<String>();

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

    private TabLayout tabLayout1;

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

        ArrayDeptName.add(0, "設計品質驗證部-台北");

        ArrayDeptID.add(0, "1");

        ArrayDeptName.add(1, "設計品質驗證部-昆山");

        ArrayDeptID.add(1, "2");

        ArrayDeptName.add(2, "設計品質驗證部-寶安");

        ArrayDeptID.add(2, "3");

//        ArrayDeptName.add(3, "台北.昆山.寶安");
//
//        ArrayDeptID.add(3, "4");

        spinnerdp = (Spinner) findViewById(R.id.spinner_depart);

        mDropDownAdapter = new DropDownAdapter(msibook_dqaweekly_project_activity_resource_detail.this, ArrayDeptName);

        spinnerdp.setAdapter(mDropDownAdapter);


        //*****************部門變動監聽************************
        spinnerdp.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {

            public void onItemSelected(AdapterView adapterView, View view, int position, long id) {

                DropDownAdapter DropDownAdapter = (DropDownAdapter) adapterView.getAdapter();

                DropDownAdapter.selectedItemdp = position;

                mDropDownAdapter.notifyDataSetChanged();

                putEtradp = (String) spinnerdp.getSelectedItem();
                //抓部門代號到第二頁去
                putEtraDepID = ArrayDeptID.get(position);

                mSectionsPagerAdapter1.RegionIndex = position;

                Log.w("Position",String.valueOf(position));

                mSectionsPagerAdapter1.notifyDataSetChanged();
                //動態隱藏Tab

                DpSpinnerPosition = position;

                if (CheckPagefirst_week && CheckPagefirst_dp)
                {
                    if (ArrayWeek.size() > 0) {//*******************************************先設變數存tab的數字 在用變數判斷現在是switch多少
                        switch (CheckTab_number) {
                            case 0:
                                //遲到
                                msibook_dqaweekly_project_activity_resource_detail_delay Tab0 = (msibook_dqaweekly_project_activity_resource_detail_delay) mSectionsPagerAdapter1.getFragment(0);//Tab0
                                Tab0.SetDept(ArrayDeptID.get(spinnerdp.getSelectedItemPosition()));
                                Tab0.SetYear(ArrayYear.get(spinner_week.getSelectedItemPosition()));
                                Tab0.SetWeek(ArrayWeek.get(spinner_week.getSelectedItemPosition()).replace("週", ""));
                                Tab0.Find_Total_OverTime_LastWeek(ArrayWeek.get(spinner_week.getSelectedItemPosition()).replace("週", ""),ArrayYear.get(spinner_week.getSelectedItemPosition()));
                                Tab0.Find_Total_OverTime_Total(ArrayWeek.get(spinner_week.getSelectedItemPosition()).replace("週", ""),ArrayYear.get(spinner_week.getSelectedItemPosition()));
                                break;

                            case 1:
                                //夜歸
                                msibook_dqaweekly_project_activity_resource_detail_night Tab1 = (msibook_dqaweekly_project_activity_resource_detail_night) mSectionsPagerAdapter1.getFragment(1);//Tab0
                                Tab1.SetDept(ArrayDeptID.get(spinnerdp.getSelectedItemPosition()));
                                Tab1.SetYear(ArrayYear.get(spinner_week.getSelectedItemPosition()));
                                Tab1.SetWeek(ArrayWeek.get(spinner_week.getSelectedItemPosition()).replace("週", ""));
                                Tab1.Find_Total_night_LastWeek(ArrayWeek.get(spinner_week.getSelectedItemPosition()).replace("週", ""),ArrayYear.get(spinner_week.getSelectedItemPosition()));
                                Tab1.Find_Total_OverTime_Total(ArrayWeek.get(spinner_week.getSelectedItemPosition()).replace("週", ""),ArrayYear.get(spinner_week.getSelectedItemPosition()));
                                break;

                            case 2:
                                //加班
                                msibook_dqaweekly_project_activity_resource_detail_overtime Tab2 = (msibook_dqaweekly_project_activity_resource_detail_overtime) mSectionsPagerAdapter1.getFragment(2);//Tab0
                                Tab2.SetDept(ArrayDeptID.get(spinnerdp.getSelectedItemPosition()));
                                Tab2.SetYear(ArrayYear.get(spinner_week.getSelectedItemPosition()));
                                Tab2.SetWeek(ArrayWeek.get(spinner_week.getSelectedItemPosition()).replace("週", ""));
                                Tab2.Find_Total_OverTime_LastWeek(ArrayWeek.get(spinner_week.getSelectedItemPosition()).replace("週", ""),ArrayYear.get(spinner_week.getSelectedItemPosition()));
                                Tab2.Find_Total_OverTime_Total(ArrayWeek.get(spinner_week.getSelectedItemPosition()).replace("週", ""),ArrayYear.get(spinner_week.getSelectedItemPosition()));
                                break;
                        }
                    }else {

                    }
                }else{
                    if(ArrayWeek!=null && ArrayWeek.size() > 0) {

                        //遲到
                        msibook_dqaweekly_project_activity_resource_detail_delay Tab0 = (msibook_dqaweekly_project_activity_resource_detail_delay) mSectionsPagerAdapter1.getFragment(0);//Tab0
                        if(Tab0 != null) {
                            Tab0.SetDept(ArrayDeptID.get(spinnerdp.getSelectedItemPosition()));
                            Tab0.SetYear(ArrayYear.get(spinner_week.getSelectedItemPosition()));
                            Tab0.SetWeek(ArrayWeek.get(spinner_week.getSelectedItemPosition()).replace("週", ""));
                            Tab0.Find_Total_OverTime_LastWeek(ArrayWeek.get(spinner_week.getSelectedItemPosition()).replace("週", ""),ArrayYear.get(spinner_week.getSelectedItemPosition()));
                            Tab0.Find_Total_OverTime_Total(ArrayWeek.get(spinner_week.getSelectedItemPosition()).replace("週", ""),ArrayYear.get(spinner_week.getSelectedItemPosition()));
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

                        mDropDownAdapterweek = new DropDownAdapterweek(msibook_dqaweekly_project_activity_resource_detail.this, ArrayWeek);

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
                                                //遲到
                                                msibook_dqaweekly_project_activity_resource_detail_delay Tab0 = (msibook_dqaweekly_project_activity_resource_detail_delay) mSectionsPagerAdapter1.getFragment(0);//Tab0
                                                Tab0.SetDept(ArrayDeptID.get(spinnerdp.getSelectedItemPosition()));
                                                Tab0.SetYear(ArrayYear.get(spinner_week.getSelectedItemPosition()));
                                                Tab0.SetWeek(ArrayWeek.get(spinner_week.getSelectedItemPosition()).replace("週", ""));
                                                Tab0.Find_Total_OverTime_LastWeek(ArrayWeek.get(spinner_week.getSelectedItemPosition()).replace("週", ""),ArrayYear.get(spinner_week.getSelectedItemPosition()));
                                                Tab0.Find_Total_OverTime_Total(ArrayWeek.get(spinner_week.getSelectedItemPosition()).replace("週", ""),ArrayYear.get(spinner_week.getSelectedItemPosition()));
                                                Log.w("spinnerdp_Postion",String.valueOf(ArrayDeptID.get(spinnerdp.getSelectedItemPosition())));
                                                Log.w("SetF_Year",ArrayYear.get(spinner_week.getSelectedItemPosition()));
                                                Log.w("spinner_week_Postion",String.valueOf(ArrayWeek.get(spinner_week.getSelectedItemPosition()).replace("週", "")));
                                                break;

                                            case 1:
                                                //夜歸
                                                msibook_dqaweekly_project_activity_resource_detail_night Tab1 = (msibook_dqaweekly_project_activity_resource_detail_night) mSectionsPagerAdapter1.getFragment(1);//Tab1
                                                Tab1.SetDept(ArrayDeptID.get(spinnerdp.getSelectedItemPosition()));
                                                Tab1.SetYear(ArrayYear.get(spinner_week.getSelectedItemPosition()));
                                                Tab1.SetWeek(ArrayWeek.get(spinner_week.getSelectedItemPosition()).replace("週", ""));
                                                Tab1.Find_Total_night_LastWeek(ArrayWeek.get(spinner_week.getSelectedItemPosition()).replace("週", ""),ArrayYear.get(spinner_week.getSelectedItemPosition()));
                                                Tab1.Find_Total_OverTime_Total(ArrayWeek.get(spinner_week.getSelectedItemPosition()).replace("週", ""),ArrayYear.get(spinner_week.getSelectedItemPosition()));
                                                break;

                                            case 2:
                                                //加班
                                                msibook_dqaweekly_project_activity_resource_detail_overtime Tab2 = (msibook_dqaweekly_project_activity_resource_detail_overtime) mSectionsPagerAdapter1.getFragment(2);//Tab0
                                                Tab2.SetDept(ArrayDeptID.get(spinnerdp.getSelectedItemPosition()));
                                                Tab2.SetYear(ArrayYear.get(spinner_week.getSelectedItemPosition()));
                                                Tab2.SetWeek(ArrayWeek.get(spinner_week.getSelectedItemPosition()).replace("週", ""));
                                                Tab2.Find_Total_OverTime_LastWeek(ArrayWeek.get(spinner_week.getSelectedItemPosition()).replace("週", ""),ArrayYear.get(spinner_week.getSelectedItemPosition()));
                                                Tab2.Find_Total_OverTime_Total(ArrayWeek.get(spinner_week.getSelectedItemPosition()).replace("週", ""),ArrayYear.get(spinner_week.getSelectedItemPosition()));
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msibook_dqaweekly_project_resource_detail);


        CheckTab_number = 0;
        DpSpinnerPosition = 0;
        //Spinner 給定Adapter時 監聽都會跑一次 所以初始頁載入時需做判斷 不然List會跑兩次
        CheckPagefirst_week = false;
        CheckPagefirst_dp = false;

        mSectionsPagerAdapter1 = new SectionsPagerAdapter1(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager1 = (ViewPager) findViewById(R.id.container1);
        mViewPager1.setAdapter(mSectionsPagerAdapter1);

        Calendar yy = Calendar.getInstance();
        Year = String.valueOf(yy.get(Calendar.YEAR));

        //滑動選單tabs
        tabLayout1 = (TabLayout) findViewById(R.id.tabs1);
        tabLayout1.setupWithViewPager(mViewPager1);
        //tabLayout.setTabMode(TabLayout.MODE_FIXED); //鎖定
        tabLayout1.setTabGravity(TabLayout.GRAVITY_FILL);

        lindepart = (LinearLayout) findViewById(R.id.linear_spinnerdp);


        tesView = (TextView) findViewById(R.id.textView123);
        progressBar = new ProgressDialog(this);
        progressBar.setCancelable(true);
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.setMessage("資料載入中");


        spinner_week = (Spinner) findViewById(R.id.spinner_week);//宣告週次下拉選單
        spinnerdp = (Spinner) findViewById(R.id.spinner_depart);//宣告部門下拉選單

        btnback = (Button) findViewById(R.id.btn_spinnerdp_back);
        btnnext = (Button) findViewById(R.id.btn_spinnerdp_next);
        LinearLayout Lnl_Human_Resource = (LinearLayout) findViewById(R.id.Lnl_Human_Resource);

        Find_Get_Week();



        //跳下一個部門.

        btnnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ArrayDeptID.size() == spinnerdp.getSelectedItemPosition() + 1) {
                    //don't doing
                } else {
                    spinnerdp.setSelection(spinnerdp.getSelectedItemPosition() + 1);
                }
            }
        });


        //跳上一個部門
        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinnerdp.setSelection(spinnerdp.getSelectedItemPosition() - 1);
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
                        if (ArrayWeek != null && ArrayWeek.size() > 0) {
                            //遲到
                            msibook_dqaweekly_project_activity_resource_detail_delay Tab0 = (msibook_dqaweekly_project_activity_resource_detail_delay) mSectionsPagerAdapter1.getFragment(0);//Tab0
                            Tab0.SetDept(ArrayDeptID.get(spinnerdp.getSelectedItemPosition()));
                            Tab0.SetYear(ArrayYear.get(spinner_week.getSelectedItemPosition()));
                            Tab0.SetWeek(ArrayWeek.get(spinner_week.getSelectedItemPosition()).replace("週", ""));
                            Tab0.Find_Total_OverTime_LastWeek(ArrayWeek.get(spinner_week.getSelectedItemPosition()).replace("週", ""), ArrayYear.get(spinner_week.getSelectedItemPosition()));
                            Tab0.Find_Total_OverTime_Total(ArrayWeek.get(spinner_week.getSelectedItemPosition()).replace("週", ""), ArrayYear.get(spinner_week.getSelectedItemPosition()));
                        }
                        break;

                    case 1:
                        CheckTab_number = 1;
                        if (ArrayWeek != null && ArrayWeek.size() > 0) {
                            //夜歸
                            msibook_dqaweekly_project_activity_resource_detail_night Tab1 = (msibook_dqaweekly_project_activity_resource_detail_night) mSectionsPagerAdapter1.getFragment(1);//Tab1
                            Tab1.SetDept(ArrayDeptID.get(spinnerdp.getSelectedItemPosition()));
                            Tab1.SetYear(ArrayYear.get(spinner_week.getSelectedItemPosition()));
                            Tab1.SetWeek(ArrayWeek.get(spinner_week.getSelectedItemPosition()).replace("週", ""));
                            Tab1.Find_Total_night_LastWeek(ArrayWeek.get(spinner_week.getSelectedItemPosition()).replace("週", ""), ArrayYear.get(spinner_week.getSelectedItemPosition()));
                            Tab1.Find_Total_OverTime_Total(ArrayWeek.get(spinner_week.getSelectedItemPosition()).replace("週", ""), ArrayYear.get(spinner_week.getSelectedItemPosition()));
                        }

                        break;

                    case 2:
                        CheckTab_number = 2;
                        if (ArrayWeek != null && ArrayWeek.size() > 0) {
                        //加班
                        msibook_dqaweekly_project_activity_resource_detail_overtime Tab2 = (msibook_dqaweekly_project_activity_resource_detail_overtime) mSectionsPagerAdapter1.getFragment(2);//Tab0
                        Tab2.SetDept(ArrayDeptID.get(spinnerdp.getSelectedItemPosition()));
                        Tab2.SetYear(ArrayYear.get(spinner_week.getSelectedItemPosition()));
                        Tab2.SetWeek(ArrayWeek.get(spinner_week.getSelectedItemPosition()).replace("週", ""));
                        Tab2.Find_Total_OverTime_LastWeek(ArrayWeek.get(spinner_week.getSelectedItemPosition()).replace("週", ""), ArrayYear.get(spinner_week.getSelectedItemPosition()));
                        Tab2.Find_Total_OverTime_Total(ArrayWeek.get(spinner_week.getSelectedItemPosition()).replace("週", ""), ArrayYear.get(spinner_week.getSelectedItemPosition()));
                        }

                        break;
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

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            int Position = getArguments().getInt(ARG_SECTION_NUMBER);

            //宣告選定的fragment 從case 0 開始
            View rootView = inflater.inflate(R.layout.fragment_msibook_dqaweekly_project_activity_resource_detail_delay, container, false);

            switch (Position) {
                case 0:
                    rootView = inflater.inflate(R.layout.fragment_msibook_dqaweekly_project_activity_resource_detail_delay, container, false);
                    return rootView;
                case 1:
                    rootView = inflater.inflate(R.layout.fragment_msibook_dqaweekly_project_activity_resource_detail_night, container, false);
                    return rootView;
                case 2:
                    rootView = inflater.inflate(R.layout.fragment_msibook_dqaweekly_project_activity_resource_detail_overtime, container, false);
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
                    Log.w("Tab遲到","Tab遲到");
                    return msibook_dqaweekly_project_activity_resource_detail_delay.newInstance("0", "0");
                case 1:
                    Log.w("Tab夜歸","Tab夜歸");
                    return msibook_dqaweekly_project_activity_resource_detail_night.newInstance("0", "0");
                case 2:
                    Log.w("Tab加班","Tab加班");
                    return msibook_dqaweekly_project_activity_resource_detail_overtime.newInstance("0", "0");
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
                    //setTitle("加班紀錄");
                    return "遲到";
                case 1:
                    //setTitle("加班紀錄");
                    return "夜歸";
                case 2:
                    //setTitle("加班紀錄");
                    return "加班";
            }

            return null;
        }
    }


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
