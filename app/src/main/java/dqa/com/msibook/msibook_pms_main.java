package dqa.com.msibook;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.os.Build;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.HashMap;
import java.util.Map;

public class msibook_pms_main extends AppCompatActivity {

    private ProgressDialog progressBar;

    private Context mContext;

    private SectionsPagerAdapter mSectionsPagerAdapter;

    private ViewPager mViewPager;

    private SearchView searchView;

    private MenuItem menuSearchItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msibook_pms_main);

        progressBar = new ProgressDialog(this);
        progressBar.setCancelable(true);
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.setMessage("資料載入中");

        mContext = msibook_pms_main.this;

        Toolbar toolbar = (Toolbar) findViewById(dqa.com.msibook.R.id.toolbar);
        toolbar.setTitle("PMS");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);


        //宣告 Tabs  滑動Title
        final TabLayout tabLayout_icon = (TabLayout) findViewById(R.id.tabs_title_icon);
        tabLayout_icon.setupWithViewPager(mViewPager);
        tabLayout_icon.setTabMode(TabLayout.MODE_FIXED);
        tabLayout_icon.setTabGravity(TabLayout.GRAVITY_FILL);


        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                if (searchView != null && !searchView.isIconified()) {
//                    searchView.onActionViewCollapsed();
//                    searchView.setQuery("", false);
//                    searchView.clearFocus();
                    MenuItemCompat.collapseActionView(menuSearchItem);
                }

                switch (position) {
                    case 0:
                        msibook_pms_ips Tab1 = (msibook_pms_ips) mSectionsPagerAdapter.getFragment(position);
                        Tab1.PMS_IPS("SYS");
                        break;

                    case 1:
                        msibook_pms_ips Tab2 = (msibook_pms_ips) mSectionsPagerAdapter.getFragment(position);
                        Tab2.PMS_IPS("MB");
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

        menu.clear();

        TabLayout tabLayout = (TabLayout) findViewById(dqa.com.msibook.R.id.tabs);

        MenuInflater inflater = getMenuInflater();

        getMenuInflater().inflate(R.menu.menu_pms_main_tab, menu);

//        int tab = tabLayout.getSelectedTabPosition();
//
//        if (tab == 0)
//            getMenuInflater().inflate(R.menu.menu_pms_main_tab, menu);
//        else
//            getMenuInflater().inflate(dqa.com.msibook.R.menu.menu_pms_main_tab, menu);

        menuSearchItem = menu.findItem(R.id.search);

        // Get the SearchView and set the searchable configuration
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menuSearchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                if( ! searchView.isIconified()) {
                    searchView.setIconified(true);
                }

                menuSearchItem.collapseActionView();
                return false;
            }
            @Override
            public boolean onQueryTextChange(String s) {
                if (mSectionsPagerAdapter != null)
                {

                    msibook_pms_ips SYS =  (msibook_pms_ips)mSectionsPagerAdapter.getFragment(0);

                    if (SYS != null)
                    {
                        SYS.SetSearchSYS(s);

                    }

                    msibook_pms_ips MB =  (msibook_pms_ips)mSectionsPagerAdapter.getFragment(1);

                    if (MB != null)
                    {
                        MB.SetSearchSYS(s);

                    }
                }
                return false;
            }
        });
        // Assumes current activity is the searchable activity
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        // 這邊讓icon可以還原到搜尋的icon
        searchView.setIconifiedByDefault(true);

        if (Build.VERSION.SDK_INT <16) {
            onPrepareOptionsMenu(menu);
        }

        return true;
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
        public static msibook_pms_main.PlaceholderFragment newInstance(int sectionNumber) {
            msibook_pms_main.PlaceholderFragment fragment = new msibook_pms_main.PlaceholderFragment();
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
            View rootView = inflater.inflate(R.layout.fragment_msibook_facility_machine, container, false);

            switch (Position) {
                case 0:
                    rootView = inflater.inflate(R.layout.fragment_msibook_pms_ips, container, false);

                    return rootView;
                case 1:
                    rootView = inflater.inflate(R.layout.fragment_msibook_pms_ips, container, false);

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
                    return msibook_pms_ips.newInstance("0", "0");
                case 1:
                    return msibook_pms_ips.newInstance("0", "0");
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
                    return "SYS";
                case 1:
                    return "MB";
            }
            return null;
        }
    }

}
