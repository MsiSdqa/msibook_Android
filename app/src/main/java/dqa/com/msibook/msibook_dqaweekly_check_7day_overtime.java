package dqa.com.msibook;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Resources;
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
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class msibook_dqaweekly_check_7day_overtime extends AppCompatActivity {

    private Context mContext;
    private ProgressDialog progressBar;

    private TabLayout mTablayout;
    private float mTabTextMultiLineSize;

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    private TextView textshowwk;
    private TextView textshowdp;

    private String f_date;
    private String f_week;

    private String Set_Year;
    public String date_one;
    private String date_two;
    private String date_three;
    private String date_four;
    private String date_five;
    private String date_six;
    private String date_seven;

    //帶到第三頁的變數判斷
    private String m2putEtrawk;//給第三頁週
    private String m2putTitle;//給第三頁部門名稱

    public String m2putEtraDepID;//給第三頁部門代號
    private String m2putEtraYear;//給第三頁年

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msibook_dqaweekly_check_7day_overtime);

        //讀取時間Bar
        progressBar = new ProgressDialog(this);
        progressBar.setCancelable(true);
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.setMessage("資料載入中");

        Calendar yy = Calendar.getInstance();
        Set_Year = String.valueOf(yy.get(Calendar.YEAR));

        //ViewPager setting
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        //設定Tab 文字大小
        final Resources res = getResources();
        mTabTextMultiLineSize = res.getDimensionPixelSize(R.dimen.design_tab_text_size_2line);

        mContext = msibook_dqaweekly_check_7day_overtime.this;

        mTablayout = (TabLayout) findViewById(R.id.tabs_days);
        mTablayout.setupWithViewPager(mViewPager);
        mTablayout.setTabMode(TabLayout.MODE_FIXED);
        mTablayout.setTabGravity(TabLayout.GRAVITY_FILL);

        textshowwk = (TextView) findViewById(R.id.textViewshowweek);
        textshowdp = (TextView) findViewById(R.id.textViewshowdepart);

        String Title = getIntent().getStringExtra("Title");//抓第一頁部門名稱
        m2putTitle = Title;

        String Week = getIntent().getStringExtra("Week");//抓第一頁選的週次
        m2putEtrawk = Week.replace("週","");

        String ChoiceDepID = getIntent().getStringExtra("ChoiceDepID");//抓第一頁選的週次
        m2putEtraDepID = ChoiceDepID;

        String getEtraDepID = getIntent().getStringExtra("ChoiceDepID");//抓第一頁選的部門代號
        m2putEtraDepID = getEtraDepID;
        Log.w("DeptID",getEtraDepID);

        String getEtraYear = getIntent().getStringExtra("Year");//
        m2putEtraYear = getEtraYear;


        //讀前頁部門Title
        textshowdp.setText(Title);
        //讀前頁週
        textshowwk.setText(Week);

        Integer RealWeek = Integer.valueOf(Week.replace("週","")); //把週次 去掉 文字

        getZhou1(RealWeek);

        //Tablay 監聽事件
        mTablayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                switch (tab.getPosition()) {//切換 換ICON 小圖
                    case 0:

                        break;
                    case 1:

                        break;
                    case 2:

                        break;
                    case 3:

                        break;
                    case 4:

                        break;
                    case 5:

                        break;
                    case 6:

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
                        msibook_dqaweekly_check_7days_one_day Tab_1 = (msibook_dqaweekly_check_7days_one_day) mSectionsPagerAdapter.getFragment(position);
                        Tab_1.Find_Over_Hour_ByDate(m2putEtraDepID,date_one);
                        //check_7days_one_day Tab_1 = (check_7days_one_day) mSectionsPagerAdapter.getFragment(0);
                        Tab_1.SetFirst_DeptID(m2putEtraDepID);
                        Tab_1.SetFist_Date(date_one);

                        Tab_1.GetTitle(m2putTitle);
                        Tab_1.GetYear(m2putEtraYear);
                        Tab_1.GetWeek(m2putEtrawk);

                        break;

                    case 1:
                        msibook_dqaweekly_check_7days_one_day Tab_2 = (msibook_dqaweekly_check_7days_one_day) mSectionsPagerAdapter.getFragment(position);
                        Tab_2.Find_Over_Hour_ByDate(m2putEtraDepID,date_two);

                        Tab_2.SetFirst_DeptID(m2putEtraDepID);
                        Tab_2.SetFist_Date(date_two);

                        Tab_2.GetTitle(m2putTitle);
                        Tab_2.GetYear(m2putEtraYear);
                        Tab_2.GetWeek(m2putEtrawk);
                        break;

                    case 2:
                        msibook_dqaweekly_check_7days_one_day Tab_3 = (msibook_dqaweekly_check_7days_one_day) mSectionsPagerAdapter.getFragment(position);
                        Tab_3.Find_Over_Hour_ByDate(m2putEtraDepID,date_three);

                        Tab_3.SetFirst_DeptID(m2putEtraDepID);
                        Tab_3.SetFist_Date(date_three);

                        Tab_3.GetTitle(m2putTitle);
                        Tab_3.GetYear(m2putEtraYear);
                        Tab_3.GetWeek(m2putEtrawk);
                        break;
                    case 3:
                        msibook_dqaweekly_check_7days_one_day Tab_4 = (msibook_dqaweekly_check_7days_one_day) mSectionsPagerAdapter.getFragment(position);
                        Tab_4.Find_Over_Hour_ByDate(m2putEtraDepID,date_four);

                        Tab_4.SetFirst_DeptID(m2putEtraDepID);
                        Tab_4.SetFist_Date(date_four);

                        Tab_4.GetTitle(m2putTitle);
                        Tab_4.GetYear(m2putEtraYear);
                        Tab_4.GetWeek(m2putEtrawk);
                        break;

                    case 4:
                        msibook_dqaweekly_check_7days_one_day Tab_5 = (msibook_dqaweekly_check_7days_one_day) mSectionsPagerAdapter.getFragment(position);
                        Tab_5.Find_Over_Hour_ByDate(m2putEtraDepID,date_five);

                        Tab_5.SetFirst_DeptID(m2putEtraDepID);
                        Tab_5.SetFist_Date(date_five);

                        Tab_5.GetTitle(m2putTitle);
                        Tab_5.GetYear(m2putEtraYear);
                        Tab_5.GetWeek(m2putEtrawk);
                        break;

                    case 5:
                        msibook_dqaweekly_check_7days_one_day Tab_6 = (msibook_dqaweekly_check_7days_one_day) mSectionsPagerAdapter.getFragment(position);
                        Tab_6.Find_Over_Hour_ByDate(m2putEtraDepID,date_six);

                        Tab_6.SetFirst_DeptID(m2putEtraDepID);
                        Tab_6.SetFist_Date(date_six);

                        Tab_6.GetTitle(m2putTitle);
                        Tab_6.GetYear(m2putEtraYear);
                        Tab_6.GetWeek(m2putEtrawk);
                        break;
                    case 6:
                        msibook_dqaweekly_check_7days_one_day Tab_7 = (msibook_dqaweekly_check_7days_one_day) mSectionsPagerAdapter.getFragment(position);
                        Tab_7.Find_Over_Hour_ByDate(m2putEtraDepID,date_seven);

                        Tab_7.SetFirst_DeptID(m2putEtraDepID);
                        Tab_7.SetFist_Date(date_seven);

                        Tab_7.GetTitle(m2putTitle);
                        Tab_7.GetYear(m2putEtraYear);
                        Tab_7.GetWeek(m2putEtrawk);
                        break;

                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


    }





    public void getZhou1(Integer RealWeek) {

        SimpleDateFormat simpleDateFormat_complete_date = new SimpleDateFormat("y年MM月dd日", Locale.TAIWAN);
        SimpleDateFormat simpleDateFormat_date = new SimpleDateFormat("d", Locale.TAIWAN);
        SimpleDateFormat simpleDateFormat_week = new SimpleDateFormat("EEEE", Locale.TAIWAN);
        Calendar calendar = Calendar.getInstance(Locale.TAIWAN);

        mTablayout.removeAllTabs();


        int week = RealWeek;
        int year = Integer.valueOf(m2putEtraYear);

        // Get calendar, clear it and set week number and year.
        Calendar calendarA = Calendar.getInstance();
        calendarA.clear();
        calendarA.set(Calendar.WEEK_OF_YEAR, week);
        calendarA.set(Calendar.YEAR, year);


        calendarA.set(Calendar.DAY_OF_WEEK, calendarA.getFirstDayOfWeek());
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE dd.MM.yyyy");

        for (int i = 0; i < 7; i++) {
            Log.i("dateTag", sdf.format(calendarA.getTime()));

//            date_one = myDateFormat2(simpleDateFormat_complete_date.format(calendar.getTime()));
            f_date = simpleDateFormat_date.format(calendarA.getTime());
            f_week = simpleDateFormat_week.format(calendarA.getTime());
            mTablayout.addTab(mTablayout.newTab().setText(f_week.replace("星期","") +"\n"+ f_date));
//            Log.w("日",date_one);
//            Log.w("日","日");

            switch (String.valueOf(i))
            {
                case "0":
                    date_one = myDateFormat2(simpleDateFormat_complete_date.format(calendarA.getTime()));
                    Log.w("日",date_one);
                    break;
                case "1":
                    date_two = myDateFormat2(simpleDateFormat_complete_date.format(calendarA.getTime()));
                    Log.w("日",date_two);
                    break;
                case "2":
                    date_three = myDateFormat2(simpleDateFormat_complete_date.format(calendarA.getTime()));
                    Log.w("日",date_three);
                    break;
                case "3":
                    date_four = myDateFormat2(simpleDateFormat_complete_date.format(calendarA.getTime()));
                    Log.w("日",date_four);
                    break;
                case "4":
                    date_five = myDateFormat2(simpleDateFormat_complete_date.format(calendarA.getTime()));
                    Log.w("日",date_five);
                    break;
                case "5":
                    date_six = myDateFormat2(simpleDateFormat_complete_date.format(calendarA.getTime()));
                    Log.w("日",date_six);
                    break;
                case "6":
                    date_seven = myDateFormat2(simpleDateFormat_complete_date.format(calendarA.getTime()));
                    Log.w("日",date_seven);
                    break;

            }

            calendarA.add(Calendar.DAY_OF_WEEK, 1);

        }

    }
    /**
     * 日期格式化 年-月-日
     */
    private static String myDateFormat2(String format) {
        String[] format_all = format.split(" ");
        format_all[0] = format_all[0].replace("年", "/"); // 替换
        format_all[0] = format_all[0].replace("月", "/"); // 替换
        format_all[0] = format_all[0].replace("日", ""); // 替换
        String formatBack = format_all[0];
        return formatBack;
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
            View rootView = inflater.inflate(R.layout.fragment_msibook_dqaweekly_check_7days_one_day, container, false);

            switch (Position) {
                case 0:
                    rootView = inflater.inflate(R.layout.fragment_msibook_dqaweekly_check_7days_one_day, container, false);

                    return rootView;
                case 1:
                    rootView = inflater.inflate(R.layout.fragment_msibook_dqaweekly_check_7days_one_day, container, false);

                    return rootView;
                case 2:
                    rootView = inflater.inflate(R.layout.fragment_msibook_dqaweekly_check_7days_one_day, container, false);

                    return rootView;
                case 3:
                    rootView = inflater.inflate(R.layout.fragment_msibook_dqaweekly_check_7days_one_day, container, false);

                    return rootView;
                case 4:
                    rootView = inflater.inflate(R.layout.fragment_msibook_dqaweekly_check_7days_one_day, container, false);

                    return rootView;
                case 5:
                    rootView = inflater.inflate(R.layout.fragment_msibook_dqaweekly_check_7days_one_day, container, false);

                    return rootView;
                case 6:
                    rootView = inflater.inflate(R.layout.fragment_msibook_dqaweekly_check_7days_one_day, container, false);

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

            //
            Fragment TabFragment = new Fragment();
            switch (position) {
                case 0:
                    return msibook_dqaweekly_check_7days_one_day.newInstance("0", "0");
                case 1:
                    return msibook_dqaweekly_check_7days_one_day.newInstance("0", "0");
                case 2:
                    return msibook_dqaweekly_check_7days_one_day.newInstance("0", "0");
                case 3:
                    return msibook_dqaweekly_check_7days_one_day.newInstance("0", "0");
                case 4:
                    return msibook_dqaweekly_check_7days_one_day.newInstance("0", "0");
                case 5:
                    return msibook_dqaweekly_check_7days_one_day.newInstance("0", "0");
                case 6:
                    return msibook_dqaweekly_check_7days_one_day.newInstance("0", "0");

            }

            return TabFragment;

        }

        @Override
        public int getCount() {
            // Show 7 total pages.
            return 7;
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
//            switch (position) {
//                case 0:
//                    return "msi HRM";
//                case 1:
//                    return "My HRM";
//                case 2:
//                    return "My Fav";
//
//            }
            return null;
        }
    }


}
