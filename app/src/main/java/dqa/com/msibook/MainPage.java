package dqa.com.msibook;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;//
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class MainPage extends AppCompatActivity

    implements NavigationView.OnNavigationItemSelectedListener {

    private ProgressDialog progressBar;

    private Context mContent;

    private String getPush_Key;//抓推播的Key

    private String getPush_Value;//抓推播的Value

    private String User_WorkID;

    private Integer Set_User_Level;//0:無 1:PG人員 2:人資 3:主管 4:一般員工

    private String updatePath;//更新path

    private static final int REQUEST_EXTERNAL_STORAGE = 1;

    private static final String WRITE_EXTERNAL_STORAGE = "android.permission.WRITE_EXTERNAL_STORAGE";
    private static final String READ_EXTERNAL_STORAGE = "android.permission.READ_EXTERNAL_STORAGE";

    private Integer CheckBadge;

    private TabLayout mTablayout;

    private ListView mListView;

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    private LinearLayout linear_location;

    private TextView main_title_text;

    private int[] SelectIcon = {//選取圖案
            R.mipmap.msibook_btn_tab_home_sel,
            R.mipmap.msibook_btn_tab_service_sel
    };

    private int[] UnSelectIcon = {//沒選取圖案
            R.mipmap.msibook_btn_tab_home_nor,
            R.mipmap.msibook_btn_tab_service_nor
    };

//    private int[] SelectIcon = {
//            R.mipmap.btn_tab_projec_sel,
//            R.mipmap.btn_tab_issue_sel,
//            R.mipmap.tab_btn_notification_sel,
//
//    };
//
//    private int[] UnSelectIcon = {
//            R.mipmap.btn_tab_projec_nor,
//            R.mipmap.btn_tab_issue_nor,
//            R.mipmap.tab_btn_notification_nor,
//    };


    private RelativeLayout relative_overtime;
    private LinearLayout linaer_overtime_inside;
    private RelativeLayout relative_dqaweekly;
    private LinearLayout linear_dqaweekly_inside;
    private RelativeLayout relative_IMS;
    private LinearLayout linear_IMS_inside;
    private RelativeLayout relative_LAB;
    private LinearLayout linear_LAB_inside;
    private RelativeLayout relative_RSS;
    private LinearLayout linear_RSS_inside;
    private RelativeLayout relative_MMC;
    private LinearLayout linear_MMC_inside;
    private RelativeLayout relative_certified;
    private LinearLayout linear_certified_inside;
    private RelativeLayout relative_eHR;
    private LinearLayout linear_eHR_inside;
    private RelativeLayout relative_request;
    private LinearLayout linear_request_inside;
    private RelativeLayout relative_requset_form;
    private LinearLayout linear_requset_form_inside;

    //logo
    private TextView textView_ehr_ic;
    private TextView textView_ims_ic;
    private TextView textView_overtime_ic;
    private TextView textView_lab_ic;
    private TextView textView_rss_ic;
    private TextView textView_mmc_ic;
    private TextView textView_certified_ic;
    private TextView textView_request_ic;
    private TextView textView_dqaweekly_ic;
    private TextView textView_requset_form_ic;
    //文字
    private TextView textView_ehr_ic_title;
    private TextView textView_ic_ims_title;
    private TextView textView_ic_overtime_title;
    private TextView textView_ic_lab_title;
    private TextView textView_ic_rss_title;
    private TextView textView_ic_mmc_title;
    private TextView textView_ic_certified_title;
    private TextView textView_ic_request_title;
    private TextView textView_ic_dqaweekly_title;
    private TextView textView_ic_requset_form_title;

    private TextView textView_overtime_badge;
    private TextView textView_dqaweekly_badge;
    private TextView textView_ims_badge;
    private TextView textView_lab_badge;
    private TextView textView_rss_badge;
    private TextView textView_mmc_badge;
    private TextView textView_certified_badge;
    private TextView textView_ehr_badge;
    private TextView textView_requset_form_badge;

    private Integer Control_eHR;
    private Integer Control_eHR_RoleID; //
    private Integer Control_IMS;
    private Integer Control_IMS_RoleID;
    private Integer Control_overtime;
    private Integer Control_overtime_RoleID;
    private Integer Control_LAB;
    private Integer Control_LAB_RoleID;
    private Integer Control_RSS;
    private Integer Control_RSS_RoleID;
    private Integer Control_MMC;
    private Integer Control_MMC_RoleID;
    private Integer Control_certified;
    private Integer Control_certified_RoleID;
    private Integer Control_request;
    private Integer Control_request_RoleID;
    private Integer Control_dqaweekly;
    private Integer Control_dqaweekly_RoleID;
    private Integer Control_requset_form;
    private Integer Control_requset_form_RoleID;


    //判斷回首頁狀態
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (data != null) {
            Bundle bundle = data.getExtras();
            Integer CheckBadge = Integer.valueOf(bundle.getString("Badge_Check"));
            switch (CheckBadge) {
                case 1:
                    textView_overtime_badge.setVisibility(View.INVISIBLE);
                    break;
                case 2:
                    textView_dqaweekly_badge.setVisibility(View.INVISIBLE);
                    break;
                case 3:
                    textView_ims_badge.setVisibility(View.INVISIBLE);
                    break;
                case 4:
                    textView_lab_badge.setVisibility(View.INVISIBLE);
                    break;
                case 5:
                    textView_rss_badge.setVisibility(View.INVISIBLE);
                    break;
                case 6:
                    textView_mmc_badge.setVisibility(View.INVISIBLE);
                    break;
                case 7:
                    textView_certified_badge.setVisibility(View.INVISIBLE);
                    break;
                case 8:
                    textView_ehr_badge.setVisibility(View.INVISIBLE);
                    break;

            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        isUpdate(this);

        mContent = this;

        progressBar = new ProgressDialog(this);
        progressBar.setCancelable(true);
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.setMessage("資料載入中");

//        Log.w("DeptID",UserData.DeptID);
//        Log.w("F_OrgID",UserData.F_OrgID);
//
//        Log.w("Account",UserData.Account);
//        Log.w("Email",UserData.Email);
//        Log.w("Password",UserData.Password);
//        Log.w("WorkID",UserData.WorkID);
//        Log.w("Name",UserData.Name);
//        Log.w("Dept",UserData.Dept);
//        Log.w("Phone",UserData.Phone);
//        Log.w("EName",UserData.EName);
//
//        Log.w("WebFlowBoss",UserData.WebFlowBoss);
//        Log.w("WebFlowBossName",UserData.WebFlowBossName);
//        Log.w("Region",UserData.Region);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        main_title_text = (TextView) findViewById(R.id.main_title_text);

//        main_title_text.setBackgroundResource(R.mipmap.ic_onemsi);
        main_title_text.setText("msiBook");

        //宣告 Tabs  滑動Title
        final TabLayout tabLayout_icon = (TabLayout) findViewById(R.id.tabs_title_icon);
        tabLayout_icon.setupWithViewPager(mViewPager);
        tabLayout_icon.setTabMode(TabLayout.MODE_FIXED);
        tabLayout_icon.setTabGravity(TabLayout.GRAVITY_FILL);

//        View view1 = getLayoutInflater().inflate(R.layout.customtab, null);
//        view1.findViewById(R.id.icon).setBackgroundResource(R.mipmap.msibook_btn_tab_home_sel);
//        tabLayout_icon.addTab(tabLayout_icon.newTab().setCustomView(view1));
//
//        View view2 = getLayoutInflater().inflate(R.layout.customtab, null);
//        view1.findViewById(R.id.icon).setBackgroundResource(R.mipmap.msibook_btn_tab_sustainability_sel);
//        tabLayout_icon.addTab(tabLayout_icon.newTab().setCustomView(view2));
//
//        View view3 = getLayoutInflater().inflate(R.layout.customtab, null);
//        view1.findViewById(R.id.icon).setBackgroundResource(R.mipmap.msibook_btn_tab_setting_sel);
//        tabLayout_icon.addTab(tabLayout_icon.newTab().setCustomView(view3));

        //依序把圖給Tab
        for (int i = 0; i < tabLayout_icon.getTabCount(); i++) {
            tabLayout_icon.getTabAt(i).setIcon(UnSelectIcon[i]);
        }

        //預設一開始為選定第一張Tab
        tabLayout_icon.getTabAt(0).setIcon(SelectIcon[0]);

        //Tablay 監聽事件
        tabLayout_icon.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                switch (tab.getPosition()) {//切換 換ICON 小圖
                    case 0:
                        tabLayout_icon.getTabAt(0).setIcon(SelectIcon[0]);
                        tabLayout_icon.getTabAt(1).setIcon(UnSelectIcon[1]);

                        break;

                    case 1:
                        tabLayout_icon.getTabAt(0).setIcon(UnSelectIcon[0]);
                        tabLayout_icon.getTabAt(1).setIcon(SelectIcon[1]);

                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        main_title_text.setText("msiBook");
//                        main_title_text.setBackgroundResource(R.mipmap.ic_onemsi);
//                        msibook_facility_machine Tab1 = (msibook_facility_machine) mSectionsPagerAdapter.getFragment(position);
//                        Tab1.Find_Fac_Type_List(String.valueOf(Set_Location));
//                        Tab1.Find_Fac_List(String.valueOf(Set_Location),"0");
//                        switch (Set_Location) {
//                            case 0:
//                                main_title_text.setText("實驗室");
//                                break;
//                            case 1:
//                                main_title_text.setText("實驗室");
//                                break;
//                            case 2:
//                                main_title_text.setText("實驗室");
//                                break;
//
//                        }
                        break;

                    case 1:
//                        msibook_facility_my_booking Tab2 = (msibook_facility_my_booking) mSectionsPagerAdapter.getFragment(position);
//                        Tab2.Find_Fac_My_Schedule_List(UserData.WorkID);
//                        Tab2.Set_Keyin(UserData.WorkID);
                        main_title_text.setText("服務中心");
                        main_title_text.setBackgroundResource(0);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        Set_User_Level = 0 ;//0:無 1:PG人員 2:人資 3:主管 4:一般員工

        //整個icon區塊
        relative_overtime = (RelativeLayout) findViewById(R.id.relative_overtime);
        relative_dqaweekly = (RelativeLayout) findViewById(R.id.relative_dqaweekly);
        relative_IMS = (RelativeLayout) findViewById(R.id.relative_IMS);
        relative_LAB = (RelativeLayout) findViewById(R.id.relative_LAB);
        relative_RSS = (RelativeLayout) findViewById(R.id.relative_RSS);
        relative_MMC = (RelativeLayout) findViewById(R.id.relative_MMC);
        relative_certified = (RelativeLayout) findViewById(R.id.relative_certified);
        relative_eHR = (RelativeLayout) findViewById(R.id.relative_eHR);
        relative_request = (RelativeLayout) findViewById(R.id.relative_request);
        relative_requset_form = (RelativeLayout) findViewById(R.id.relative_requset_form);//需求單

        linaer_overtime_inside = (LinearLayout) findViewById(R.id.linaer_overtime_inside);
        linear_dqaweekly_inside = (LinearLayout) findViewById(R.id.linear_dqaweekly_inside);
        linear_IMS_inside = (LinearLayout) findViewById(R.id.linear_IMS_inside);
        linear_LAB_inside = (LinearLayout) findViewById(R.id.linear_LAB_inside);
        linear_RSS_inside = (LinearLayout) findViewById(R.id.linear_RSS_inside);
        linear_MMC_inside = (LinearLayout) findViewById(R.id.linear_MMC_inside);
        linear_certified_inside = (LinearLayout) findViewById(R.id.linear_certified_inside);
        linear_eHR_inside = (LinearLayout) findViewById(R.id.linear_eHR_inside);
        linear_request_inside = (LinearLayout) findViewById(R.id.linear_request_inside);
        linear_requset_form_inside = (LinearLayout) findViewById(R.id.linear_requset_form_inside);

        //icon logo
        textView_ehr_ic = (TextView)findViewById(R.id.textView_ehr_ic);
        textView_ims_ic = (TextView)findViewById(R.id.textView_ims_ic);
        textView_overtime_ic = (TextView)findViewById(R.id.textView_overtime_ic);
        textView_lab_ic = (TextView)findViewById(R.id.textView_lab_ic);
        textView_rss_ic = (TextView)findViewById(R.id.textView_rss_ic);
        textView_mmc_ic = (TextView)findViewById(R.id.textView_mmc_ic);
        textView_certified_ic = (TextView)findViewById(R.id.textView_certified_ic);
        textView_request_ic = (TextView)findViewById(R.id.textView_request_ic);
        textView_dqaweekly_ic = (TextView)findViewById(R.id.textView_dqaweekly_ic);
        textView_requset_form_ic = (TextView)findViewById(R.id.textView_requset_form_ic);

        //logo文字
        textView_ehr_ic_title = (TextView)findViewById(R.id.textView_ehr_ic_title);
        textView_ic_ims_title = (TextView)findViewById(R.id.textView_ic_ims_title);
        textView_ic_overtime_title = (TextView)findViewById(R.id.textView_ic_overtime_title);
        textView_ic_lab_title = (TextView)findViewById(R.id.textView_ic_lab_title);
        textView_ic_rss_title = (TextView)findViewById(R.id.textView_ic_rss_title);
        textView_ic_mmc_title = (TextView)findViewById(R.id.textView_ic_mmc_title);
        textView_ic_certified_title = (TextView)findViewById(R.id.textView_ic_certified_title);
        textView_ic_request_title = (TextView)findViewById(R.id.textView_ic_request_title);
        textView_ic_dqaweekly_title = (TextView)findViewById(R.id.textView_ic_dqaweekly_title);
        textView_ic_requset_form_title = (TextView)findViewById(R.id.textView_ic_requset_form_title);

        //紅點提示
        textView_overtime_badge = (TextView)findViewById(R.id.textView_overtime_badge);
        textView_dqaweekly_badge = (TextView)findViewById(R.id.textView_dqaweekly_badge);
        textView_ims_badge = (TextView)findViewById(R.id.textView_ims_badge);
        textView_lab_badge = (TextView)findViewById(R.id.textView_lab_badge);
        textView_rss_badge = (TextView)findViewById(R.id.textView_rss_badge);
        textView_mmc_badge = (TextView)findViewById(R.id.textView_mmc_badge);
        textView_certified_badge = (TextView)findViewById(R.id.textView_certified_badge);
        textView_ehr_badge = (TextView)findViewById(R.id.textView_ehr_badge);
        textView_requset_form_badge = (TextView)findViewById(R.id.textView_requset_form_badge);

        //預設無權限
        Control_eHR = 0;
        Control_eHR_RoleID = 8;
        Control_IMS = 0;
        Control_IMS_RoleID = 8;
        Control_overtime = 0;
        Control_overtime_RoleID = 8;
        Control_LAB = 0;
        Control_LAB_RoleID = 8;
        Control_RSS = 0;
        Control_RSS_RoleID = 8;
        Control_MMC = 0;
        Control_MMC_RoleID = 8;
        Control_certified = 0;
        Control_certified_RoleID = 8;
        Control_request = 0;
        Control_request_RoleID = 8;
        Control_dqaweekly = 0;
        Control_dqaweekly_RoleID = 8;
        Control_requset_form = 0;
        Control_requset_form_RoleID = 8;


//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        User_WorkID = UserData.WorkID;

        String[] Keyman_PG = {"10015812","10016109","10015657","10010670","10016049","10003275","10010059","10015667","10016295","10015635","10018041","10018042"};//部門權限開放 --- PG使用功能  0:無 1:PG人員 2:人資 3:主管 4:一般員工
        String[] Keyman_HR = {"10012565"};//人資開放使用功能  0:無 1:PG人員 2:人資 3:主管 4:一般員工
        //String[] Keyman_HR = {"10015812", "10012565"};//人資開放使用功能  0:無 1:PG人員 2:人資 3:主管 4:一般員工
        String[] Keyman_Super = {"10015812","10016109","10015657","10010670","10016049","10003275","10010059","10015667","10016295","10015635"};//主管使用功能  0:無 1:PG人員 2:人資 3:主管 4:一般員工
        //String[] Keyman = {"10012565"};//人資開放使用功能

        if (Arrays.asList(Keyman_PG).contains(User_WorkID)){
            Set_User_Level = 1;
        }else if(Arrays.asList(Keyman_HR).contains(User_WorkID)){
            Set_User_Level = 2;
        }else if(Arrays.asList(Keyman_Super).contains(User_WorkID)){
            Set_User_Level = 3;
        }else{
            Set_User_Level = 4;
        }

        Find_System_Role_Type(User_WorkID);
        //Find_System_Role("10015667");

        //權限控制
//        switch (Set_User_Level) {
//            case 1://PG人員
//                relative_overtime.setVisibility(View.VISIBLE);
//                relative_dqaweekly.setVisibility(View.VISIBLE);
//                relative_IMS.setVisibility(View.VISIBLE);
//                relative_LAB.setVisibility(View.VISIBLE);
//                relative_RSS.setVisibility(View.VISIBLE);
//                relative_MMC.setVisibility(View.VISIBLE);
//                relative_certified.setVisibility(View.VISIBLE);
//                relative_eHR.setVisibility(View.VISIBLE);
//                relative_request.setVisibility(View.VISIBLE);
//                relative_non.setVisibility(View.VISIBLE);
//                Log.w("Set_User_Level",String.valueOf(Set_User_Level));
//                break;
//            case 2://人資
//                relative_overtime.setVisibility(View.INVISIBLE);
//                relative_dqaweekly.setVisibility(View.GONE);
//                relative_IMS.setVisibility(View.INVISIBLE);
//                relative_LAB.setVisibility(View.INVISIBLE);
//                relative_RSS.setVisibility(View.GONE);
//                relative_MMC.setVisibility(View.GONE);
//                relative_certified.setVisibility(View.GONE);
//                relative_eHR.setVisibility(View.VISIBLE);
//                relative_request.setVisibility(View.GONE);
//                relative_non.setVisibility(View.GONE);
//                Log.w("Set_User_Level",String.valueOf(Set_User_Level));
//                break;
//            case 3://主管
//                relative_overtime.setVisibility(View.VISIBLE);
//                relative_dqaweekly.setVisibility(View.VISIBLE);
//                relative_IMS.setVisibility(View.VISIBLE);
//                relative_LAB.setVisibility(View.VISIBLE);
//                relative_RSS.setVisibility(View.VISIBLE);
//                relative_MMC.setVisibility(View.VISIBLE);
//                relative_certified.setVisibility(View.VISIBLE);
//                relative_eHR.setVisibility(View.VISIBLE);
//                relative_request.setVisibility(View.VISIBLE);
//                relative_non.setVisibility(View.VISIBLE);
//                Log.w("Set_User_Level",String.valueOf(Set_User_Level));
//                break;
//            case 4://一般User
//                relative_overtime.setVisibility(View.INVISIBLE); //未開放
//                relative_dqaweekly.setVisibility(View.INVISIBLE);//無權限
//                relative_IMS.setVisibility(View.INVISIBLE);//未開放
//                relative_LAB.setVisibility(View.VISIBLE);
//                relative_RSS.setVisibility(View.INVISIBLE);//未開放
//                relative_MMC.setVisibility(View.INVISIBLE);//未開放
//                relative_certified.setVisibility(View.INVISIBLE);//未開放
//                relative_eHR.setVisibility(View.VISIBLE);
//                relative_request.setVisibility(View.INVISIBLE);//未開放
//                relative_non.setVisibility(View.INVISIBLE);//未開放
//                Log.w("Set_User_Level",String.valueOf(Set_User_Level));
//                break;
//        }

        String Settoken = FirebaseInstanceId.getInstance().getToken();
        Log.d("FCM", "Token:"+Settoken);

        InsertUserToken(User_WorkID,Settoken,"BOOK");

//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
//                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        drawer.addDrawerListener(toggle);
//        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


//        Button Btn_Resume = (Button)findViewById(R.id.Btn_Resume);//個人履歷
//        Btn_Resume.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent msibook_resume = new Intent(mContent,msibook_resume.class);
//
//                mContent.startActivity(msibook_resume);
//            }
//        });

//        Button btn_msipay = (Button)findViewById(R.id.btn_msipay);//MSI PAY
//        btn_msipay.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                Intent dqaweekly = new Intent(mContent,msibook_msipay_welcome.class);
//
//                mContent.startActivity(dqaweekly);
//            }
//        });

        getPush_Key = getIntent().getStringExtra("Push_Key");//

        getPush_Value = getIntent().getStringExtra("Push_Value");//

        if(getPush_Key!=null) {
            Log.w("getPush_Key",getPush_Key);
            Log.w("getPush_Value",getPush_Value);
            switch (String.valueOf(getPush_Key)) {
                case "eHR_Application":
                    Intent intent = new Intent();

                    intent.putExtra("Push_Key", "eHR_Application");

                    intent.putExtra("Push_Value", getPush_Value);

                    intent.setClass(MainPage.this, msibook_ehr_main.class);

                    startActivity(intent);
                    break;
                case "YT":

                    Uri uri_YT=Uri.parse("https://www.youtube.com/channel/UCmORzj3VQ_nqs-xX_xCUbwA");
                    Intent i =new Intent(Intent.ACTION_VIEW,uri_YT);
                    startActivity(i);
                    break;
                case "FB":

                    Uri uri_FB=Uri.parse("https://m.facebook.com/DqaPG/?modal=admin_todo_tour");
                    Intent j =new Intent(Intent.ACTION_VIEW,uri_FB);
                    startActivity(j);
                    break;
                case "MENU":
                    Intent intent_MENU = new Intent();

                    intent_MENU.putExtra("Push_Key", "MENU");

                    intent_MENU.putExtra("Push_Value", getPush_Value);

                    intent_MENU.setClass(MainPage.this, msibook_main_setting_foodmenu.class);

                    startActivity(intent_MENU);
                    break;
                case "MMC":
                    Intent intent_MMC = new Intent();

                    intent_MMC.putExtra("Push_Key", "MMC");

                    intent_MMC.putExtra("Push_Value", getPush_Value);

                    intent_MMC.setClass(MainPage.this, msibook_mmc.class);

                    startActivity(intent_MMC);
                    break;
            }
        }



    }

    private void Find_System_Role_Type(String WorkID) {

        //顯示  讀取等待時間Bar
        progressBar.show();

        RequestQueue mQueue = Volley.newRequestQueue(this);

        Map<String,String> map = new HashMap<String, String>();
        map.put("WorkID", WorkID);

        if (mQueue == null) {
            mQueue = Volley.newRequestQueue(this);
        }

        HTTPSTrustManager.allowAllSSL();//信任所有证书，信任憑證
        String Path = GetServiceData.ServicePath + "/Find_System_Role_Type";
        //String Path = "http://wtsc.msi.com.tw/IMS/MsiBook_App_Service.asmx/Insert_E_HR_Master";

        GetServiceData.SendPostRequest(Path, mQueue, new GetServiceData.VolleyStringCallback() {
            @Override
            public void onSendRequestSuccess(String result) {

                try {

                    JSONObject obj = new JSONObject(result);

                    JSONArray UserArray = new JSONArray(obj.getString("Key"));

                    for (int i = 0; i < UserArray.length(); i++) {

                        JSONObject ReuqestData = UserArray.getJSONObject(i);

                        String SysENName = String.valueOf(ReuqestData.getString("SysENName"));//eHR
                        String SysCNName = String.valueOf(ReuqestData.getString("SysCNName"));//Msi內部應徵
                        String RoleCode = "";
                        if  (ReuqestData.isNull("RoleCode")) //SuperUser
                        {
                            RoleCode = "無";
                        }
                        else{
                            RoleCode = String.valueOf(ReuqestData.getString("RoleCode"));
                        }

                        String RoleName = "";
                        if  (ReuqestData.isNull("RoleName")) //管理員
                        {
                            RoleName = "無";
                        }
                        else{
                            RoleName = String.valueOf(ReuqestData.getString("RoleName"));
                        }

                        String RoleID = "";
                        if  (ReuqestData.isNull("RoleID")) //管理員
                        {
                            RoleID = "8";
                        }
                        else{
                            RoleID = String.valueOf(ReuqestData.getInt("RoleID"));
                        }

//                        //權限控制
//                        if(SysENName.contains("eHR")){
//
//                            relative_eHR.setVisibility(View.VISIBLE);
//                            Log.w("eHR","開通eHR");
//
//                        }else if(SysENName.contains("Request_Form")){
//
//                            relative_eHR.setVisibility(View.VISIBLE);
//                            Log.w("Request_Form","開通Request_Form");
//
//                        }else if(SysENName.contains("Action_Item")){
//
//                            relative_request.setVisibility(View.VISIBLE);
//                            Log.w("Action_Item","開通Action_Item");
//
//                        }else if(SysENName.contains("Laboratory")){
//
//                            relative_LAB.setVisibility(View.VISIBLE);
//                            Log.w("Laboratory","開通Laboratory");
//
//                        }else if(SysENName.contains("Certification")){
//
//                            relative_certified.setVisibility(View.VISIBLE);//未開放
//                            Log.w("Certification","開通Laboratory");
//
//                        }else if(SysENName.contains("MMC")){
//
//                            relative_MMC.setVisibility(View.VISIBLE);//未開放
//                            Log.w("Laboratory","開通Laboratory");
//
//                        }
                        //權限控制
                        switch (SysENName) {
                            case "eHR"://Msi內部應徵
                                relative_eHR.setVisibility(View.VISIBLE);
                                Control_eHR = 1;
                                Control_eHR_RoleID = Integer.valueOf(RoleID);
                                textView_ehr_ic.setBackgroundResource(R.mipmap.msibook_ic_msibook_ehr);
                                textView_ehr_ic_title.setTextColor(Color.parseColor("#FFFFFF"));
                                Log.w("eHR","開通eHR");
                                break;
                            case "ims"://IMS
//                                relative_IMS.setVisibility(View.VISIBLE);
//                                Control_IMS = 1;
//                                textView_ims_ic.setBackgroundResource(R.mipmap.msibook_ic_msibook_ims);
//                                textView_ic_ims_title.setTextColor(Color.parseColor("#FFFFFF"));
                                Log.w("ims","開通ims");
                                break;
                            case "OverTime"://加班
//                                relative_overtime.setVisibility(View.VISIBLE);
//                                Control_overtime = 1;
//                                textView_overtime_ic.setBackgroundResource(R.mipmap.msibook_ic_msibook_overtime);
//                                textView_ic_overtime_title.setTextColor(Color.parseColor("#FFFFFF"));
                                Log.w("OverTime","開通OverTime");
                                break;
                            case "Laboratory"://實驗室
                                relative_LAB.setVisibility(View.VISIBLE);
                                Control_LAB = 1;
                                Control_LAB_RoleID = Integer.valueOf(RoleID);
                                textView_lab_ic.setBackgroundResource(R.mipmap.msibook_ic_msibook_laboratory);
                                textView_ic_lab_title.setTextColor(Color.parseColor("#FFFFFF"));
                                Log.w("Laboratory","開通Laboratory");
                                break;
                            case "WorkReport"://工作報告
//                                relative_RSS.setVisibility(View.VISIBLE);
//                                Control_RSS = 1;
//                                textView_rss_ic.setBackgroundResource(R.mipmap.msibook_ic_msibook_workreport);
//                                textView_ic_rss_title.setTextColor(Color.parseColor("#FFFFFF"));
                                Log.w("WorkReport","開通WorkReport");
                                break;
                            case "MMC"://資產管理系統
                                relative_MMC.setVisibility(View.VISIBLE);//未開放
                                Control_MMC = 1;
                                Control_MMC_RoleID = Integer.valueOf(RoleID);
                                textView_mmc_ic.setBackgroundResource(R.mipmap.msibook_ic_msibook_assets);
                                textView_ic_mmc_title.setTextColor(Color.parseColor("#FFFFFF"));
                                Log.w("MMC","開通MMC");
                                break;
                            case "Certification"://認證系統
//                                relative_certified.setVisibility(View.VISIBLE);//未開放
//                                Control_certified = 1;
//                                textView_certified_ic.setBackgroundResource(R.mipmap.msibook_ic_msibook_certified);
//                                textView_ic_certified_title.setTextColor(Color.parseColor("#FFFFFF"));
                                Log.w("Certification","開通Certification");
                                break;
                            case "Action_Item"://工單系統
                                relative_request.setVisibility(View.VISIBLE);
                                Control_request = 1;
                                Control_request_RoleID = Integer.valueOf(RoleID);
                                textView_request_ic.setBackgroundResource(R.mipmap.msibook_ic_msibook_workorder);
                                textView_ic_request_title.setTextColor(Color.parseColor("#FFFFFF"));
                                Log.w("Action_Item","開通Action_Item");
                                break;
                            case "WeeklyReport"://週報系統
                                relative_dqaweekly.setVisibility(View.VISIBLE);
                                Control_dqaweekly = 1;
                                Control_dqaweekly_RoleID = Integer.valueOf(RoleID);
                                textView_dqaweekly_ic.setBackgroundResource(R.mipmap.msibook_ic_msibook_weekly);
                                textView_ic_dqaweekly_title.setTextColor(Color.parseColor("#FFFFFF"));
                                Log.w("WeeklyReport","開通WeeklyReport週報");
                                break;
                            case "Request_Form"://需求單系統
                                relative_requset_form.setVisibility(View.VISIBLE);
                                Control_requset_form = 1;
                                Control_requset_form_RoleID = Integer.valueOf(RoleID);
                                textView_requset_form_ic.setBackgroundResource(R.mipmap.msibook_ic_msibook_demandlist);
                                textView_ic_requset_form_title.setTextColor(Color.parseColor("#FFFFFF"));
                                Log.w("Request_Form","開通Request_Form");
                                break;
                            case "RDWork_Daily"://RD工作日誌

                                Log.w("RDWork_Daily","開通RDWork_Daily");
                                break;


                        }



                    }




                }
                catch (JSONException ex) {
                    Log.w("Json",ex.toString());
                }
                progressBar.dismiss();
            }

            @Override
            public void onSendRequestError(String result) {
                Log.w("RequestError",result);
                progressBar.dismiss();

            }

        }, map);
    }


    //版本說明
    private Button btn_close_popinfo;
    private PopupWindow pwindoinfo;
    private void infoPopupWindow(){
        try{
            // We need to get the instance of the LayoutInflater
            LayoutInflater inflater = (LayoutInflater) MainPage.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View layout = inflater.inflate(R.layout.msibook_info_popup_window,(ViewGroup) findViewById(R.id.popup_element));

            DisplayMetrics metrics = getResources().getDisplayMetrics();
            int screenWidth = (int) (metrics.widthPixels * 0.8);
            int screenHeight = (int) (metrics.heightPixels * 0.78);

            pwindoinfo = new PopupWindow(layout,screenWidth,screenHeight,true);
            pwindoinfo.showAtLocation(layout, Gravity.CENTER,0,0);

            btn_close_popinfo = (Button) layout.findViewById(R.id.close_info);
            btn_close_popinfo.setOnClickListener(cancelinfo_button_click_listener);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private View.OnClickListener cancelinfo_button_click_listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            pwindoinfo.dismiss();
        }
    };

    private void InsertUserToken(String WorkID,
                                 String Token,String App) {

        RequestQueue mQueue = Volley.newRequestQueue(this);

        Map<String, String> map = new HashMap<String, String>();
        map.put("WorkID", WorkID);
        map.put("Token", Token);
        map.put("App", App);

        if (mQueue == null) {
            mQueue = Volley.newRequestQueue(this);
        }
        HTTPSTrustManager.allowAllSSL();//信任所有证书，信任憑證
        String Path = GetServiceData.ServicePath + "/InsertUserToken";

        GetServiceData.SendPostRequest(Path, mQueue, new GetServiceData.VolleyStringCallback() {
            @Override
            public void onSendRequestSuccess(String result) {
                Log.w("VolleySuccess","VolleySuccess");
            }

            @Override
            public void onSendRequestError(String result) {
                Log.w("VolleyError",result);
            }
        }, map);

    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //原先右上角的設定
//        getMenuInflater().inflate(R.menu.main_page, menu);
        return true;
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





    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();


//        if (id == R.id.nav_ehr) {
//            Intent nav_Intent = new Intent(mContent,msibook_ehr_splashscreen.class);
//            mContent.startActivity(nav_Intent);
//
//        } else if (id == R.id.nav_facility) {
//
//            //String[] Keyman_HR = {"10015812", "10012565"};//人資開放使用功能  0:無 1:PG人員 2:人資 3:主管 4:一般員工
//            String[] Keyman_HR = {"10012565"};//人資開放使用功能  0:無 1:PG人員 2:人資 3:主管 4:一般員工
//
//            if(Arrays.asList(Keyman_HR).contains(User_WorkID)){
//                Toast.makeText(MainPage.this, "尚無使用權限。", Toast.LENGTH_SHORT).show();
//            }else{
//                Intent nav_Intent = new Intent(mContent, msibook_facility.class);
//                mContent.startActivity(nav_Intent);
//            }
//
//        }
//        } else if (id == R.id.nav_overtime) {
//
//
//        } else if (id == R.id.nav_ims) {
//            Intent nav_ims_Intent = new Intent(mContent,msibook_ims_issue_myissue.class);
//            mContent.startActivity(nav_ims_Intent);
//
//        } else if (id == R.id.nav_rss) {
//            Intent nav_rss_Intent = new Intent(mContent,msibook_rss.class);
//            mContent.startActivity(nav_rss_Intent);
//
//        } else if (id == R.id.nav_mmc) {
//            Intent nav_mmc_Intent = new Intent(mContent,msibook_mmc.class);
//            mContent.startActivity(nav_mmc_Intent);
//
//        }else if (id == R.id.nav_request) {
//            Intent msibook_request_Intent = new Intent(mContent,msibook_request_main.class);
//            startActivityForResult(msibook_request_Intent,1);
//
//        }else if (id == R.id.nav_certified) {
//
//
//        } else if (id == R.id.nav_weekly_report) {
//            Intent weekly_report_Intent = new Intent(mContent,msibook_dqaweekly_main.class);
//            mContent.startActivity(weekly_report_Intent);
//
//        }
        if(id==R.id.nav_info_history){//版本說明
            infoPopupWindow();
        }
        else if(id==R.id.nav_checkUpdate){//檢查更新
            isUpdate(MainPage.this);
            //Toast.makeText(MainPage.this, "尚未開放", Toast.LENGTH_SHORT).show();
        }else if(id==R.id.nav_QA){//問題回報
            QA_Sent();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //---------比對版本-------

    private static int getVersionCode(Context context)
    {
        int versionCode = 0;
        try
        {
            Log.w("context.getPackageName",context.getPackageName());
            // 獲取軟件版本號，對應AndroidManifest.xml下android:versionCode
            versionCode = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e)
        {
            e.printStackTrace();
        }
        return versionCode;
    }

    public void isUpdate(final Context context){

        RequestQueue mQueue = Volley.newRequestQueue(context);

        //String Path = GetServiceData.ServicePath + "/Get_Application_Version?Type=Android";
        String Path = "http://wtsc.msi.com.tw/IMS/MsiBook_App_Service.asmx/Get_Application_Version?Type=Android";

        GetServiceData.getString(Path, mQueue, new GetServiceData.VolleyCallback() {
            @Override
            public void onSuccess(JSONObject result) {

                try {

                    JSONArray UserArray = new JSONArray(result.getString("Key"));

                    if (UserArray.length() > 0) {

                        UpdateManager mUpdateManager;

                        int versionCode = getVersionCode(context);

                        JSONObject IssueData = UserArray.getJSONObject(0);

                        String Version = String.valueOf(IssueData.getInt("Version"));

                        String Path = String.valueOf(IssueData.getString("Path"));
                        updatePath = Path;

                        Log.w("裝置版本",String.valueOf(versionCode));
                        Log.w("Server上版本",String.valueOf(Version));

                        if (Version != String.valueOf(versionCode))
                        {
                            //showDialog(); Google play的更新
                            int permission = ActivityCompat.checkSelfPermission(MainPage.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

                            if (permission != PackageManager.PERMISSION_GRANTED) {
                                // 無權限，向使用者請求
                                ActivityCompat.requestPermissions(
                                        MainPage.this,
                                        new String[]{WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE}, REQUEST_EXTERNAL_STORAGE
                                );

                            } else {
                                mUpdateManager = new UpdateManager(context,Path);
                                mUpdateManager.checkUpdateInfo();
                            }


                        }else{
                            Toast.makeText(MainPage.this, "目前為最新版本", Toast.LENGTH_SHORT).show();
                        }

                    }
                } catch (JSONException ex) {

                }

            }
        });

        //return VersionCheck;
    }

    public void QA_Sent()
        {
        //指定與電子郵件相關的變量值
        String[] emails = new String[]{
                "larryliao@msi.com"
        };
        // emails for cc
        String[] emailsCC = new String[]{
                "matthewchi@msi.com"
        };

        String mailSubject = "Android msiBook 問題回報";
        String mailBody = "問題描述 :";
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.putExtra(Intent.EXTRA_EMAIL,emails);
        intent.putExtra(Intent.EXTRA_CC,emailsCC);
        intent.putExtra(Intent.EXTRA_SUBJECT,mailSubject);
        intent.putExtra(Intent.EXTRA_TEXT,mailBody);
        intent.setData(Uri.parse("mailto:"));
        // Try to start the activity
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }else{
            // If there are no email client installed in this device
            Toast.makeText(MainPage.this,"No email client installed in this device.",Toast.LENGTH_SHORT).show();
        }
    }


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
            View rootView = inflater.inflate(R.layout.fragment_msibook_app_main, container, false);

            switch (Position) {
                case 0:
                    rootView = inflater.inflate(R.layout.fragment_msibook_app_main, container, false);

                    return rootView;
                case 2:
                    rootView = inflater.inflate(R.layout.fragment_msibook_main_setting, container, false);

                    return rootView;



            }

//            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
//            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        private FragmentManager mFragmentManager;
        private Map<Integer, String> mFragmentTags;

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
            mFragmentTags = new HashMap<Integer, String>();
            mFragmentManager = fm;
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
            //return PlaceholderFragment.newInstance(position);


            Fragment TabFragment = new Fragment();
            switch (position) {
                case 0:
                    return msibook_app_main.newInstance("0", "0");
                case 1:
                    return msibook_main_setting.newInstance("0", "0");
            }

            return TabFragment;

        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 2;
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
//                case 0:
//                    return "";
//                case 1:
//                    return "我的預約";
//                case 2:
//                    return "系統公告";

            }
            return null;
        }
    }

}
