package dqa.com.msibook;

import android.app.ProgressDialog;
import android.content.Context;
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
import android.widget.ListView;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

public class msibook_facility_fac_detail extends AppCompatActivity {

    private ListView mListView;
    private ProgressDialog progressBar;
    private Context mContext;

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private TabLayout mTablayout;

    public String getF_SeqNo;
    public String getF_Is_Restrict;
    private TextView main_title_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msibook_facility_fac_detail);

        progressBar = new ProgressDialog(this);
        progressBar.setCancelable(true);
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.setMessage("資料載入中");

        mContext = msibook_facility_fac_detail.this;

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container_machine);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        mSectionsPagerAdapter.notifyDataSetChanged();

        main_title_text = (TextView) findViewById(R.id.main_title_text);

        //宣告 Tabs
        mTablayout = (TabLayout) findViewById(R.id.tabs_title);
        mTablayout.setupWithViewPager(mViewPager);
        mTablayout.setTabMode(TabLayout.MODE_FIXED);
        mTablayout.setTabGravity(TabLayout.GRAVITY_FILL);

        getF_SeqNo = getIntent().getStringExtra("F_SeqNo");//抓機台序號
        Log.w("F_SeqNo",getF_SeqNo);

        getF_Is_Restrict = getIntent().getStringExtra("F_Is_Restrict");//抓 預約限制
        Log.w("getF_Is_Restrict",getF_Is_Restrict);


        mTablayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

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
                        msibook_facility_machine_detail Tab_1 = (msibook_facility_machine_detail) mSectionsPagerAdapter.getFragment(position);
                        Tab_1.Find_Fac_Detail(getF_SeqNo);
                        break;

                    case 1:

                        break;

                    case 2:

                        break;
                    case 3:

                        break;

                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

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
            View rootView = inflater.inflate(R.layout.fragment_msibook_facility_machine_detail, container, false);

            switch (Position) {
                case 0:
                    rootView = inflater.inflate(R.layout.fragment_msibook_facility_machine_detail, container, false);

                    return rootView;
                case 1:
                    rootView = inflater.inflate(R.layout.fragment_msibook_facility_machine_maintain, container, false);

                    return rootView;
                case 2:
                    rootView = inflater.inflate(R.layout.fragment_msibook_facility_machine_maintain, container, false);

                    return rootView;
                case 3:
                    rootView = inflater.inflate(R.layout.fragment_msibook_facility_machine_maintain, container, false);

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
                    return msibook_facility_machine_detail.newInstance("0", "0");
                case 1:
                    return msibook_facility_machine_maintain.newInstance("0", "0");
                case 2:
                    return msibook_facility_machine_maintain.newInstance("0", "0");
                case 3:
                    return msibook_facility_machine_maintain.newInstance("0", "0");
            }

            return TabFragment;

        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 1;
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
                    return "基本";
                case 1:
                    return "保養";
                case 2:
                    return "維修";
                case 3:
                    return "儀校";

            }
            return null;
        }
    }

}
