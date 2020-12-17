package dqa.com.msibook;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.HashMap;
import java.util.Map;

public class msibook_certified_main_for_engineer extends AppCompatActivity {

    private static final int REQUEST_EXTERNAL_STORAGE = 1;

    private static final String WRITE_EXTERNAL_STORAGE = "android.permission.WRITE_EXTERNAL_STORAGE";
    private static final String READ_EXTERNAL_STORAGE = "android.permission.READ_EXTERNAL_STORAGE";

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msibook_certified_main_for_engineer);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        final TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);


        //mViewPager 監聽
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        msibook_certified_info_detail_enginner Tab1 = (msibook_certified_info_detail_enginner) mSectionsPagerAdapter.getFragment(position);
                        Tab1.CheckF_Cer_Class("");//
                        Tab1.Find_My_Certification_Info("10003130");

                        break;

                    case 1:
                        msibook_certified_info_detail_enginner Tab2 = (msibook_certified_info_detail_enginner) mSectionsPagerAdapter.getFragment(position);
                        Tab2.CheckF_Cer_Class("環保類");//
                        Tab2.Find_My_Certification_Info("10003130");

                        break;

                    case 2:
                        msibook_certified_info_detail_enginner Tab3 = (msibook_certified_info_detail_enginner) mSectionsPagerAdapter.getFragment(position);
                        Tab3.CheckF_Cer_Class("電磁安規類");//
                        Tab3.Find_My_Certification_Info("10003130");

                        break;

                    case 3:
                        msibook_certified_info_detail_enginner Tab4 = (msibook_certified_info_detail_enginner) mSectionsPagerAdapter.getFragment(position);
                        Tab4.CheckF_Cer_Class("硬體端子類");//
                        Tab4.Find_My_Certification_Info("10003130");

                        break;

                    case 4:
                        msibook_certified_info_detail_enginner Tab5 = (msibook_certified_info_detail_enginner) mSectionsPagerAdapter.getFragment(position);
                        Tab5.CheckF_Cer_Class("設計認證類");//
                        Tab5.Find_My_Certification_Info("10003130");

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
        getMenuInflater().inflate(R.menu.menu_main5, menu);
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
            View rootView = inflater.inflate(R.layout.fragment_msibook_certified_info_detail_enginner, container, false);

            switch (Position) {
                case 0:
                    rootView = inflater.inflate(R.layout.fragment_msibook_certified_info_detail_enginner, container, false);
                    return rootView;
                case 1:
                    rootView = inflater.inflate(R.layout.fragment_msibook_certified_info_detail_enginner, container, false);
                    return rootView;
                case 2:
                    rootView = inflater.inflate(R.layout.fragment_msibook_certified_info_detail_enginner, container, false);
                    return rootView;
                case 3:
                    rootView = inflater.inflate(R.layout.fragment_msibook_certified_info_detail_enginner, container, false);
                    return rootView;
                case 4:
                    rootView = inflater.inflate(R.layout.fragment_msibook_certified_info_detail_enginner, container, false);
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

                    return msibook_certified_info_detail_enginner.newInstance("0", "0");
                case 1:

                    return msibook_certified_info_detail_enginner.newInstance("0", "0");
                case 2:

                    return msibook_certified_info_detail_enginner.newInstance("0", "0");
                case 3:

                    return msibook_certified_info_detail_enginner.newInstance("0", "0");
                case 4:

                    return msibook_certified_info_detail_enginner.newInstance("0", "0");

            }

            return TabFragment;

        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 5;
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
                    return "全部";
                case 1:
                    //setTitle("加班紀錄");
                    return "環保類";
                case 2:
                    //setTitle("加班紀錄");
                    return "電磁安規類";
                case 3:
                    //setTitle("加班紀錄");
                    return "硬體端子類";
                case 4:
                    //setTitle("加班紀錄");
                    return "設計認證類";
            }
            return null;
        }
    }


}
