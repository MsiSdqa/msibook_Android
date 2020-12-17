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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class msibook_cec_main extends AppCompatActivity {

    private ProgressDialog progressBar;

    private Context mContent;

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    private TextView main_title_text;

    private List<String> Get_Bundle_Item = new ArrayList<String>();

    //判斷回首頁狀態
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (data != null) {
            Bundle bundle = data.getExtras();
            Integer CEC_Application_Check = Integer.valueOf(bundle.getString("CEC_Application_Check"));
            Get_Bundle_Item = bundle.getStringArrayList("Item");
            for (int i = 0; i < Get_Bundle_Item.size(); i++) {
                Log.w("Get_Bundle",Get_Bundle_Item.get(i));
            }

            if(CEC_Application_Check == 1){
                msibook_cec_review_page1 Tab1 = (msibook_cec_review_page1) mSectionsPagerAdapter.getFragment(0);
                //Tab1.Find_Certification_Model("10003130","處理中");
                Tab1.SetBundleArray((ArrayList) Get_Bundle_Item);
                Tab1.Find_Certification_Model(UserData.WorkID,"處理中");
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msibook_cec_main);

        mContent = this;

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);//把msiBook 字樣影藏

        main_title_text = (TextView) findViewById(R.id.main_title_text);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        //宣告 Tabs  滑動Title
        final TabLayout tabLayout_icon = (TabLayout) findViewById(R.id.tabs_title_icon);
        tabLayout_icon.setupWithViewPager(mViewPager);
        tabLayout_icon.setTabMode(TabLayout.MODE_FIXED);
        tabLayout_icon.setTabGravity(TabLayout.GRAVITY_FILL);

        tabLayout_icon.getTabAt(0);

        //mSectionsPagerAdapter.notifyDataSetChanged();
//        msibook_cec_review Tab1 = (msibook_cec_review) mSectionsPagerAdapter.getFragment(0);
//        Tab1.Find_Certification_Model("10003130","處理中");

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

//                if (position == 0) {
//                    msibook_cec_review Tab1 = (msibook_cec_review) mSectionsPagerAdapter.getFragment(position);
//                    //Tab1.Find_Certification_Model("10003130","處理中");
//                    Tab1.Find_Certification_Model(UserData.WorkID,"處理中");
//                }

            }
            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:

                        break;
                    case 1:
                        msibook_cec_review Tab2 = (msibook_cec_review) mSectionsPagerAdapter.getFragment(position);
                        //Tab2.Find_Certification_Model("10003130","已完成");
                        Tab2.Find_Certification_Model(UserData.WorkID,"已完成");
                        break;
                    case 2:
                        msibook_cec_review Tab3 = (msibook_cec_review) mSectionsPagerAdapter.getFragment(position);
                        //Tab3.Find_Certification_Model("10003130","已退回");
                        Tab3.Find_Certification_Model(UserData.WorkID,"已退回");
                        break;
                }
            }//換Title字眼

            @Override
            public void onPageScrollStateChanged(int state) {

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
        public static msibook_dqaweekly_project_activity.PlaceholderFragment newInstance(int sectionNumber) {
            msibook_dqaweekly_project_activity.PlaceholderFragment fragment = new msibook_dqaweekly_project_activity.PlaceholderFragment();
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
            View rootView = inflater.inflate(R.layout.fragment_msibook_cec_review_page1, container, false);

            switch (Position) {
                case 0:
                    rootView = inflater.inflate(R.layout.fragment_msibook_cec_review_page1, container, false);
                    return rootView;
                case 1:
                    rootView = inflater.inflate(R.layout.fragment_msibook_cec_review, container, false);
                    return rootView;
                case 2:
                    rootView = inflater.inflate(R.layout.fragment_msibook_cec_review, container, false);
            }

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
                    return msibook_cec_review_page1.newInstance("0", "0");
                case 1:
                    return msibook_cec_review.newInstance("0", "0");
                case 2:
                    return msibook_cec_review.newInstance("0", "0");
            }

            return TabFragment;

        }

        @Override
        public int getCount() {
            // Show 3 total pages.
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
                    return "進行中";
                case 1:
                    return "已完成";
                case 2:
                    return "退件";

            }
            return null;
        }
    }


}
