package dqa.com.msibook;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class msibook_dqaweekly_check_overtime_history extends AppCompatActivity {


    private ProgressDialog progressBar;
    private TextView textView_dptname;
    private TextView textView_week;

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    private PopupWindow popupWindow;

    private LinearLayout main_linearLayout1;
    private LinearLayout main_linearLayout2;
    private LinearLayout main_linearLayout3;
    private LinearLayout main_linearLayout4;
    private LinearLayout main_linearLayout5;
    private LinearLayout main_linearLayout6;

    private static final int REQUEST_EXTERNAL_STORAGE = 1;

    private static final String WRITE_EXTERNAL_STORAGE = "android.permission.WRITE_EXTERNAL_STORAGE";
    private static final String READ_EXTERNAL_STORAGE = "android.permission.READ_EXTERNAL_STORAGE";

    private String updatePath;//更新path

    private Button btn_open_pop;
    private Button btn_close_pop;
    private Button btn_close_popinfo;

    private String Dpt_name;
    private String Dpt_id;
    private String Year;
    private String Week;



    private void addAnimation() {//加入了旋转动画
        RotateAnimation rotateAnimation = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setDuration(500);//设置动画时间
        btn_open_pop.setAnimation(rotateAnimation);//设置动画
        btn_open_pop.startAnimation(rotateAnimation);//开始动画
    }
    //漢堡列show popWindow
    private void showPopupWindow() {
        View view = LayoutInflater.from(this).inflate(R.layout.activity_msibook_dqaweekly_popupwindow_layout, null);//获取popupWindow子布局对象
        popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, false);//初始化
        popupWindow.setAnimationStyle(R.style.dqaweekly_popupAnim);//设置动画
        popupWindow.showAsDropDown(textView_dptname, -300, 0);//在ImageView控件下方弹出

        //首頁被點擊
        main_linearLayout1 = (LinearLayout) view.findViewById(R.id.main_lin1);
        main_linearLayout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(msibook_dqaweekly_check_overtime_history.this, "回首頁", Toast.LENGTH_SHORT).show();
                popupWindow.dismiss();
                Intent intent = new Intent();
                //從MainActivity 到Main2Activity
                intent.setClass(msibook_dqaweekly_check_overtime_history.this, msibook_dqaweekly_main.class);
                //開啟Activity
                startActivity(intent);
            }
        });
        //計算公式被點擊
        main_linearLayout2 = (LinearLayout) view.findViewById(R.id.main_lin2);
        main_linearLayout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(msibook_dqaweekly_check_overtime_history.this, "計算公式", Toast.LENGTH_SHORT).show();
                popupWindow.dismiss();
                initiatePopupWindow();//呼叫pop 計算公式
            }
        });
        //部級週報被點擊
        main_linearLayout6 = (LinearLayout) view.findViewById(R.id.main_lin6);
        main_linearLayout6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(msibook_dqaweekly_check_overtime_history.this, "回部級週報", Toast.LENGTH_SHORT).show();
                popupWindow.dismiss();
//                Intent intent = new Intent();
//                //從MainActivity 到Main2Activity
//                intent.setClass(msibook_dqaweekly_check_overtime_history.this, ProjectActivity.class);
//                //開啟Activity
//                startActivity(intent);
            }
        });
        //操作說明被點擊
        main_linearLayout3 = (LinearLayout) view.findViewById(R.id.main_lin3);
        main_linearLayout3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(msibook_dqaweekly_check_overtime_history.this, "操作說明", Toast.LENGTH_SHORT).show();
                popupWindow.dismiss();

//                Intent intent = new Intent();
//                //從MainActivity 到Main2Activity
//                intent.setClass(msibook_dqaweekly_check_overtime_history.this, Main_ViewPager.class);
//                //開啟Activity
//                startActivity(intent);
            }
        });
        //版本紀錄被點擊
        main_linearLayout4 = (LinearLayout) view.findViewById(R.id.main_lin4);
        main_linearLayout4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(msibook_dqaweekly_check_overtime_history.this, "版本紀錄", Toast.LENGTH_SHORT).show();
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

//                isUpdate(Check_overtime_history.this);
//                //infoPopupWindow();
            }
        });

    }
    private PopupWindow pwindo;

    private void initiatePopupWindow() {
        try {
            // We need to get the instance of the LayoutInflater
            LayoutInflater inflater = (LayoutInflater) msibook_dqaweekly_check_overtime_history.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
    private PopupWindow pwindoinfo;

    private void infoPopupWindow() {
        try {
            // We need to get the instance of the LayoutInflater
            LayoutInflater inflater = (LayoutInflater) msibook_dqaweekly_check_overtime_history.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
        setContentView(R.layout.activity_msibook_dqaweekly_check_overtime_history);


        progressBar = new ProgressDialog(this);
        progressBar.setCancelable(true);
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.setMessage("資料載入中");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Dpt_name = getIntent().getStringExtra("Dpt_name");//抓第一頁選的部門代號
        Dpt_id = getIntent().getStringExtra("Dpt_id");//抓第一頁選的部門代號
        Year = getIntent().getStringExtra("Year");//抓第一頁選的年
        Log.w("YYYYYYYY",Year);
        Week = getIntent().getStringExtra("Week");//抓第一頁選的週

        // primary sections of the activity.
        mSectionsPagerAdapter = new msibook_dqaweekly_check_overtime_history.SectionsPagerAdapter(getSupportFragmentManager());

        mSectionsPagerAdapter.Year = Year;
        mSectionsPagerAdapter.DeptID = Dpt_id;
        mSectionsPagerAdapter.Week = Week;

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs1);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setupWithViewPager(mViewPager);

        textView_dptname = (TextView)findViewById(R.id.textView_dptname);
        textView_dptname.setText(Dpt_name);
        textView_week = (TextView)findViewById(R.id.textView_week);
        textView_week.setText(Week+" 週");
        btn_open_pop = (Button) findViewById(R.id.btn_open_pop);//漢堡列按鈕


        //漢堡列點選事件
        btn_open_pop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addAnimation();//圖片旋轉動畫
                if (popupWindow == null) {
                    showPopupWindow();//顯示popwindow
                } else if (popupWindow.isShowing()) {
                    popupWindow.dismiss();
                } else {
                    popupWindow.showAsDropDown(textView_dptname, -300, 0);
                }
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
                        //Tab_One_Data(ArrayWeek.get(spinner_week.getSelectedItemPosition()).replace("週", ""), ArrayDeptID.get(spinnerdp.getSelectedItemPosition()));
                        msibook_dqaweekly_check_overtime_history_lastweek Tab1_1 = (msibook_dqaweekly_check_overtime_history_lastweek) mSectionsPagerAdapter.getFragment(position);
                        Tab1_1.SetYear(Year);
                        Tab1_1.Find_LastWeek_Over_Hour_Total(Dpt_id,Week,Year,"0");
                        Tab1_1.Find_LastWeek_Over_Hour_Single(Dpt_id,Week,Year,"1");
                        Tab1_1.Find_LastWeek_Over_Hour_Single(Dpt_id,Week,Year,"2");
                        break;
                    case 1:
                        //SetTabTwoAndThreeData(ArrayWeek.get(spinner_week.getSelectedItemPosition()).replace("週", ""), ArrayDeptID.get(spinnerdp.getSelectedItemPosition()));
                        msibook_dqaweekly_check_overtime_history_lastmonth Tab1_2 = (msibook_dqaweekly_check_overtime_history_lastmonth) mSectionsPagerAdapter.getFragment(position);
                        Tab1_2.SetYear(Year);
                        Tab1_2.Find_LastMonth_Over_Hour_Total(Dpt_id,Week,Year,"0");
                        Tab1_2.Find_LastMonth_Over_Hour_Single(Dpt_id,Week,Year,"1");
                        Tab1_2.Find_LastMonth_Over_Hour_Single(Dpt_id,Week,Year,"2");
                        break;

                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main6, menu);
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
        public static msibook_dqaweekly_check_overtime_history.PlaceholderFragment newInstance(int sectionNumber) {
            msibook_dqaweekly_check_overtime_history.PlaceholderFragment fragment = new msibook_dqaweekly_check_overtime_history.PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            int Position = getArguments().getInt(ARG_SECTION_NUMBER);

            View rootView = inflater.inflate(R.layout.msibook_dqaweekly_main6, container, false);


            switch (Position) {
                case 0:
                    rootView = inflater.inflate(R.layout.fragment_msibook_dqaweekly_check_overtime_history_lastweek, container, false);
                    return rootView;
                case 1:
                    rootView = inflater.inflate(R.layout.fragment_msibook_dqaweekly_check_overtime_history_lastmonth, container, false);
                    return rootView;

            }

            return rootView;
        }
    }


    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        private FragmentManager mFragmentManager;
        private Map<Integer, String> mFragmentTags;

        public String Year;
        public String DeptID;
        public String Week;
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

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
            mFragmentTags = new HashMap<Integer, String>();
            mFragmentManager = fm;
        }

        @Override
        public Fragment getItem(int position) {

            Fragment TabFragment = new Fragment();


            switch (position) {
                case 0:

                    return msibook_dqaweekly_check_overtime_history_lastweek.newInstance("0", "0",Year,DeptID,Week);
                case 1:

                    return msibook_dqaweekly_check_overtime_history_lastmonth.newInstance("0", "0",Year,DeptID,Week);
            }
            return TabFragment;
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "上週申請加班";
                case 1:
                    return "上月加班申報";
            }
            return null;
        }

        public Fragment getFragment(int position) {
            String tag = mFragmentTags.get(position);



            if (tag == null)
                return null;
            return mFragmentManager.findFragmentByTag(tag);
        }
    }

}


