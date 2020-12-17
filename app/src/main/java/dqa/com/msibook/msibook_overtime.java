package dqa.com.msibook;

import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.AppBarLayout;
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
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class msibook_overtime extends AppCompatActivity {

    private static final int REQUEST_EXTERNAL_STORAGE = 1;

    private static final String WRITE_EXTERNAL_STORAGE = "android.permission.WRITE_EXTERNAL_STORAGE";
    private static final String READ_EXTERNAL_STORAGE = "android.permission.READ_EXTERNAL_STORAGE";

    SQLiteDatabase db_login;//資料庫物件
    SQLiteDatabase db;//資料庫物件

    static final String db_name_login = "login_db";//資料庫名稱
    static final String tb_name_user_data = "user_data";//資料表名稱

    static final String db_name = "check_first";//資料庫名稱
    static final String tb_name = "first_data";//資料表名稱

    public PopupWindow popupWindow;
    private LinearLayout main_linearLayout1;
    private LinearLayout main_linearLayout2;
    private LinearLayout main_linearLayout3;
    private LinearLayout main_linearLayout4;
    private LinearLayout main_linearLayout5;
    private LinearLayout main_linearLayout6;
    private AppBarLayout appbar;

    private Button btn_open_pop;

    private Button btn_close_pop;
    private Button btn_close_popinfo;

    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    public interface VolleyCallback {

        void onSuccess(JSONObject result);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msibook_overtime);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        final TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        btn_open_pop = (Button) findViewById(R.id.btn_creat_pop);//漢堡列按鈕

        final Button toolbar_icon = (Button)findViewById(R.id.toolbar_icon);//回當天按鈕

        toolbar_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                switch (tabLayout.getSelectedTabPosition()) {
                    case 0:
                        overtime_application Tab1 = (overtime_application) mSectionsPagerAdapter.getFragment(0);
                        Tab1.GoBack();

                        break;
                    case 1:
                        overtime_declare Tab2 = (overtime_declare) mSectionsPagerAdapter.getFragment(1);
                        Tab2.GoBack();

                        break;
                }
            }
        });


        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                switch (tab.getPosition()) {
                    case 0:
                        toolbar_icon.setBackgroundResource(R.mipmap.overtime_btn_apply_return_week);
                        Log.w("TabChagne","1");
                        break;
                    case 1:
                        toolbar_icon.setBackgroundResource(R.mipmap.overtime_btn_declare_return_month);
                        Log.w("TabChagne","2");
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
            View rootView = inflater.inflate(R.layout.fragment_overtime_application, container, false);



            switch (Position) {
                case 0:
                    rootView = inflater.inflate(R.layout.fragment_overtime_application, container, false);
                    return rootView;
                case 1:
                    rootView = inflater.inflate(R.layout.fragment_overtime_declare, container, false);
                    return rootView;
            }

//            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
//            textView.setTextColor(Color.parseColor("#000000"));

            return rootView;
        }

    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
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
            Fragment TabFragment = new Fragment();


            switch (position) {
                case 0:

                    return overtime_application.newInstance("0", "0");
                case 1:

                    return overtime_declare.newInstance("0", "0");
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
                case 0:
                    //setTitle("加班紀錄");
                    return "申請";
                case 1:
                    //setTitle("加班紀錄");
                    return "申報";
            }
            return null;
        }
    }



}
