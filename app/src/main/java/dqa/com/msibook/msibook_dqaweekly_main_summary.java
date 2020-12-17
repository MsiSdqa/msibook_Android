package dqa.com.msibook;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TabLayout;
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
import android.view.KeyEvent;
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

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class msibook_dqaweekly_main_summary extends AppCompatActivity {


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

    private Button btn_close_pop;
    private Button btn_close_popinfo;

    private String Dpt_name;
    private String Dpt_id;
    private String Week;


    private String Year;
    private String GetWeek;
    private String GetDeptID;
    private String GetWorkID;

    private String getSpinnerWeek_position;
    private String getSpinnerDP_position;

    //按下返回鍵
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {   //確定按下退出鍵
            Bundle bundle = new Bundle();
            bundle.putString("Type", "1");
            bundle.putString("GetWeek",GetWeek);
            bundle.putString("GetDeptID",GetDeptID);
            bundle.putString("SummaryCheck","1");
//            bundle.putString("getSpinnerWeek_position",getSpinnerWeek_position);
//            bundle.putString("getSpinnerDP_position",getSpinnerDP_position);
            msibook_dqaweekly_main_summary.this.setResult(RESULT_OK,msibook_dqaweekly_main_summary.this.getIntent().putExtras(bundle));
            finish();

            return true;
        }
        return super.onKeyDown(keyCode, event);

    }


    private PopupWindow pwindo;

    private void initiatePopupWindow() {
        try {
            // We need to get the instance of the LayoutInflater
            LayoutInflater inflater = (LayoutInflater) msibook_dqaweekly_main_summary.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
            LayoutInflater inflater = (LayoutInflater) msibook_dqaweekly_main_summary.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
        setContentView(R.layout.activity_msibook_dqaweekly_main_summary);


        progressBar = new ProgressDialog(this);
        progressBar.setCancelable(true);
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.setMessage("資料載入中");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // primary sections of the activity.
        mSectionsPagerAdapter = new msibook_dqaweekly_main_summary.SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs1);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setupWithViewPager(mViewPager);

        Dpt_name = getIntent().getStringExtra("Title");//抓第一頁選的部門代號
        Dpt_id = getIntent().getStringExtra("ChoiceDepID");//抓第一頁選的部門代號
        Week = getIntent().getStringExtra("Week").replace("週","");//抓第一頁選的部門代號
        textView_dptname = (TextView)findViewById(R.id.textView_dptname);
        textView_dptname.setText(Dpt_name);
        textView_week = (TextView)findViewById(R.id.textView_week);
        textView_week.setText(Week+" 週");

        //抓年 、抓首頁帶給 Main_summar 的資料[週、部門ID]
        Year = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
        GetWeek =  getIntent().getStringExtra("Week").replace("週","");//抓週
        GetDeptID =  getIntent().getStringExtra("ChoiceDepID");//抓部門ID
        GetWorkID =  getIntent().getStringExtra("WorkID");//抓工號
        getSpinnerWeek_position = getIntent().getStringExtra("SpinnerWeek_position");//紀錄spinner week 座標
        getSpinnerDP_position = getIntent().getStringExtra("SpinnerDP_position");//紀錄spinner dp 座標




        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        //Tab_One_Data(ArrayWeek.get(spinner_week.getSelectedItemPosition()).replace("週", ""), ArrayDeptID.get(spinnerdp.getSelectedItemPosition()));
                        break;
                    case 1:
                        //SetTabTwoAndThreeData(ArrayWeek.get(spinner_week.getSelectedItemPosition()).replace("週", ""), ArrayDeptID.get(spinnerdp.getSelectedItemPosition()));
                        break;

                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    //搜尋完部門重新載入Main_summary
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


        if(data !=null) {
            Bundle bundle = data.getExtras();
            String Type = bundle.getString("Type");

            final TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs1);

            if(Type.equals("1")){
                tabLayout.getTabAt(0).select();

                msibook_dqaweekly_page_summary_info a = (msibook_dqaweekly_page_summary_info) mSectionsPagerAdapter.getFragment(0);

                a.Get_Weekly_Content_Summary(Year,GetWeek,GetDeptID,"1");

            }else if(Type.equals("2")){
                tabLayout.getTabAt(1).select();

                msibook_dqaweekly_page_action_items_info a = (msibook_dqaweekly_page_action_items_info) mSectionsPagerAdapter.getFragment(1);

                a.Get_Weekly_Content_Summary_ActionItems(Year,GetWeek,GetDeptID,"2");

            }


        }

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
        public static msibook_dqaweekly_main_summary.PlaceholderFragment newInstance(int sectionNumber) {
            msibook_dqaweekly_main_summary.PlaceholderFragment fragment = new msibook_dqaweekly_main_summary.PlaceholderFragment();
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
                    rootView = inflater.inflate(R.layout.fragment_msibook_dqaweekly_page_summary_info, container, false);
                    return rootView;
                case 1:
                    rootView = inflater.inflate(R.layout.fragment_msibook_dqaweekly_page_action_items_info, container, false);
                    return rootView;

            }

            return rootView;
        }
    }



    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        private FragmentManager mFragmentManager;
        private Map<Integer, String> mFragmentTags;


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

                    return msibook_dqaweekly_page_summary_info.newInstance("0", "0");
                case 1:

                    return msibook_dqaweekly_page_action_items_info.newInstance("0", "0");
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
                    return "摘要";
                case 1:
                    return "Action Items";
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
