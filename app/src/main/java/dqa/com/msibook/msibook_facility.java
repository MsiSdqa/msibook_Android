package dqa.com.msibook;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class msibook_facility extends AppCompatActivity {

    private TabLayout mTablayout;

    private ListView mListView;
    private ProgressDialog progressBar;
    private Context mContext;

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    private LinearLayout linear_location;

    private int[] SelectIcon = {//選取圖案
            R.mipmap.facility_tabbar_machine_sel,
            R.mipmap.facility_tabbar_reservation_sel,
            R.mipmap.facility_tabbar_notice_sel
    };

    private int[] UnSelectIcon = {//沒選取圖案
            R.mipmap.facility_tabbar_machine_nor,
            R.mipmap.facility_tabbar_reservation_nor,
            R.mipmap.facility_tabbar_notice_nor
    };

    private TextView main_title_text;
    private TextView textView_location;
    private Integer Set_Location;

    //點選Local回傳狀態
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(data !=null) {
            Bundle bundle = data.getExtras();
            String Type = bundle.getString("Type");
            Integer CheckBooking = Integer.valueOf(bundle.getString("Booking_Check"));
            Integer Int_Type = Integer.valueOf(Type);

            if(CheckBooking ==1){// 1 == 有預約成功 跳到我的預約
                final TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs_title_icon);
                tabLayout.getTabAt(1).select();
                msibook_facility_my_booking Tab2 = (msibook_facility_my_booking) mSectionsPagerAdapter.getFragment(1);
                Tab2.Find_Fac_My_Schedule_List(UserData.WorkID);
                Tab2.Set_Keyin(UserData.WorkID);
            }

            if(Type!=null) {
                switch (Int_Type) {
                    case 0:
                        //main_title_text.setText("實驗室");
                        textView_location.setText("台北");
                        Set_Location = 0;
                        msibook_facility_machine Tab1 = (msibook_facility_machine) mSectionsPagerAdapter.getFragment(0);
                        Tab1.Location = String.valueOf(Set_Location);
                        Tab1.Find_Fac_Type_List(String.valueOf(Set_Location));
                        Tab1.Find_Fac_List(String.valueOf(Set_Location), "0");
                        break;

                    case 1:
                        //main_title_text.setText("實驗室");
                        textView_location.setText("昆山");
                        Set_Location = 1;

                        msibook_facility_machine Tab2 = (msibook_facility_machine) mSectionsPagerAdapter.getFragment(0);
                        Tab2.Location = String.valueOf(Set_Location);
                        Tab2.Find_Fac_Type_List(String.valueOf(Set_Location));
                        Tab2.Find_Fac_List(String.valueOf(Set_Location), "0");
                        break;

                    case 2:
                        //main_title_text.setText("實驗室");
                        textView_location.setText("寶安");
                        Set_Location = 2;

                        msibook_facility_machine Tab3 = (msibook_facility_machine) mSectionsPagerAdapter.getFragment(0);
                        Tab3.Location = String.valueOf(Set_Location);
                        Tab3.Find_Fac_Type_List(String.valueOf(Set_Location));
                        Tab3.Find_Fac_List(String.valueOf(Set_Location), "0");
                        break;
                }

            }

        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msibook_facility);
        progressBar = new ProgressDialog(this);
        progressBar.setCancelable(true);
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.setMessage("資料載入中");

        mContext = msibook_facility.this;

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        mListView = (ListView) findViewById(R.id.Lsv_Facility);
//        Find_Fac_List("0");

        linear_location = (LinearLayout) findViewById(R.id.linear_location);

        main_title_text = (TextView) findViewById(R.id.main_title_text);
        textView_location = (TextView) findViewById(R.id.textView_location);

        //宣告 Tabs  滑動Title
        final TabLayout tabLayout_icon = (TabLayout) findViewById(R.id.tabs_title_icon);
        tabLayout_icon.setTabMode(TabLayout.MODE_FIXED);
        tabLayout_icon.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout_icon.setupWithViewPager(mViewPager);

        //依序把圖給Tab
        for (int i = 0; i < tabLayout_icon.getTabCount(); i++) {
            tabLayout_icon.getTabAt(i).setIcon(UnSelectIcon[i]);
        }

        //預設一開始為選定第一張Tab
        tabLayout_icon.getTabAt(0).setIcon(SelectIcon[0]);

        //預設一開始為台北廠區
        Set_Location = 0;
        main_title_text.setText("實驗室");
        textView_location.setText("台北");

        //預設把紅點設為已讀取
        Intent intent = new Intent();
        Bundle b = new Bundle();
        b.putString("Badge_Check","4");
        intent.putExtras(b);
        setResult(RESULT_OK, intent);

        Find_Get_Week(); //暫時讓 Fragment可以塞第一頁的值

        //地點 點選
        linear_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int []location=new int[2];
                v.getLocationOnScreen(location);
                int x=location[0];//获取当前位置的横坐标
                int y=location[1];

                Intent intent = new Intent(msibook_facility.this, msibook_facility_setting_location.class);

                intent.putExtra("Set_Location", String.valueOf(Set_Location));//代部門名稱到下一頁

                startActivityForResult(intent,1);
            }
        });

        //Tablay 監聽事件
        tabLayout_icon.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                switch (tab.getPosition()) {//切換 換ICON 小圖
                    case 0:
                        tabLayout_icon.getTabAt(0).setIcon(SelectIcon[0]);
                        tabLayout_icon.getTabAt(1).setIcon(UnSelectIcon[1]);
//                        tabLayout_icon.getTabAt(2).setIcon(UnSelectIcon[2]);

                        break;

                    case 1:
                        tabLayout_icon.getTabAt(0).setIcon(UnSelectIcon[0]);
                        tabLayout_icon.getTabAt(1).setIcon(SelectIcon[1]);
//                        tabLayout_icon.getTabAt(2).setIcon(UnSelectIcon[2]);

                        break;

//                    case 2:
//                        tabLayout_icon.getTabAt(0).setIcon(UnSelectIcon[0]);
//                        tabLayout_icon.getTabAt(1).setIcon(UnSelectIcon[1]);
//                        tabLayout_icon.getTabAt(2).setIcon(SelectIcon[2]);
//
//                        break;
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
//                        msibook_facility_machine Tab1 = (msibook_facility_machine) mSectionsPagerAdapter.getFragment(position);
//                        Tab1.Find_Fac_Type_List(String.valueOf(Set_Location));
//                        Tab1.Find_Fac_List(String.valueOf(Set_Location),"0");
                        switch (Set_Location) {
                            case 0:
                                main_title_text.setText("實驗室");
                                break;
                            case 1:
                                main_title_text.setText("實驗室");
                                break;
//                            case 2:
//                                main_title_text.setText("實驗室");
//                                break;

                        }
                        break;

                    case 1:
                        msibook_facility_my_booking Tab2 = (msibook_facility_my_booking) mSectionsPagerAdapter.getFragment(position);
                        Tab2.Find_Fac_My_Schedule_List(UserData.WorkID);
                        Tab2.Set_Keyin(UserData.WorkID);
                        main_title_text.setText("我的預約");
                        break;

//                    case 2:
//                        main_title_text.setText("系統公告");
//                        msibook_facility_system_notify Tab3 = (msibook_facility_system_notify) mSectionsPagerAdapter.getFragment(position);
//                        Tab3.Find_System_notify();
//                        break;

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
            View rootView = inflater.inflate(R.layout.fragment_msibook_facility_machine, container, false);

            switch (Position) {
                case 0:
                    rootView = inflater.inflate(R.layout.fragment_msibook_facility_machine, container, false);

                    return rootView;
                case 1:
                    rootView = inflater.inflate(R.layout.fragment_msibook_facility_my_booking, container, false);

                    return rootView;
//                case 2:
//                    rootView = inflater.inflate(R.layout.fragment_msibook_facility_system_notify, container, false);
//
//                    return rootView;


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
                    return msibook_facility_machine.newInstance("0", "0");
                case 1:
                    return msibook_facility_my_booking.newInstance("0", "0");
//                case 2:
//                    return msibook_facility_system_notify.newInstance("0", "0");
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

    private void Find_Get_Week() {
        //顯示  讀取等待時間Bar
        progressBar.show();

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

                        }
                        msibook_facility_machine Tab1 = (msibook_facility_machine) mSectionsPagerAdapter.getFragment(0);
                        Tab1.Find_Fac_Type_List(String.valueOf(Set_Location));
                        Tab1.Find_Fac_List(String.valueOf(Set_Location),"0");

                    }
                    //關閉-讀取等待時間Bar
                    progressBar.dismiss();



                } catch (JSONException ex) {

                }

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

}
