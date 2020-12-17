package dqa.com.msibook;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import java.util.HashMap;
import java.util.Map;

public class msibook_certified_info extends AppCompatActivity {

    private static final int REQUEST_EXTERNAL_STORAGE = 1;

    private static final String WRITE_EXTERNAL_STORAGE = "android.permission.WRITE_EXTERNAL_STORAGE";
    private static final String READ_EXTERNAL_STORAGE = "android.permission.READ_EXTERNAL_STORAGE";

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    private String getModelID;
    private String getModelName;
    private String getModelPic;
    private String getDateDiff;
    private String getProduct_Line;
    private String getFavorite;

    private ImageView imageView_project;
    private TextView textView_ModelName;
    private TextView textView_Product_Line;
    private ImageView imageView_favorite;


    public boolean onKeyDown(int keyCode, KeyEvent event) {

        Log.w("TestKeyDown","TestKeyDown");
        Intent intent = new Intent();
        Bundle b = new Bundle();
        b.putString("Certified_Check","1");
        intent.putExtras(b);
        setResult(RESULT_OK, intent);
        finish();

        if ((keyCode == KeyEvent.KEYCODE_BACK)) {

//            Intent intent = new Intent();
//            intent.putExtra("Certified_Check", "1");
//            setResult(1000, intent);
//            finish();
//            return true;
//            Intent intent = new Intent();
//            Bundle b = new Bundle();
//            b.putString("Certified_Check","1");
//            intent.putExtras(b);
//            setResult(RESULT_OK, intent);
//            finish();
        }
        return super.onKeyDown(keyCode, event);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msibook_certified_info);

        getModelID = getIntent().getStringExtra("ModelID");//
        getModelName = getIntent().getStringExtra("ModelName");//
        getModelPic = getIntent().getStringExtra("ModelPic");//
        getDateDiff = getIntent().getStringExtra("DateDiff");//
        getProduct_Line = getIntent().getStringExtra("Product_Line");//
        getFavorite = getIntent().getStringExtra("Favorite");//

        textView_ModelName = (TextView) findViewById(R.id.textView_ModelName);
        textView_Product_Line = (TextView) findViewById(R.id.textView_Product_Line);
        imageView_favorite= (ImageView) findViewById(R.id.imageView_favorite);
        imageView_favorite.setBackgroundResource(R.mipmap.certified_btn_certified_mark_nor);
        imageView_project = (ImageView) findViewById(R.id.imageView_project);

        if(Integer.valueOf(getFavorite)==0){
            imageView_favorite.setBackgroundResource(R.mipmap.certified_btn_certified_mark_nor);
        }else{
            imageView_favorite.setBackgroundResource(R.mipmap.certified_btn_certified_mark_sel);
        }

        textView_ModelName.setText(getModelName);
        textView_Product_Line.setText("Product Line : " + getProduct_Line);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(),getModelID);

        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setCurrentItem(0);
        final TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

        tabLayout.setupWithViewPager(mViewPager);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        //抓圖片路徑
        if(getModelPic.length()>5){ //有資料就讀取 沒資料就跳過


            Glide.with(msibook_certified_info.this)
                    .load("http://wtsc.msi.com.tw/IMS/IMS_App_Service.asmx/Get_File?FileName=" + getModelPic)
                    .asBitmap()
                    .into(new SimpleTarget<Bitmap>(300, 300) {
                        @Override
                        public void onResourceReady(Bitmap bitmap, GlideAnimation anim) {

                            //BitmapDrawable ob = new BitmapDrawable(getResources(), AppClass.roundCornerImage(bitmap,0));
                            //Img_ProjectInfo.setBackground(ob);

                            BitmapDrawable ob = new BitmapDrawable(getResources(), bitmap);
                            imageView_project.setImageBitmap(ob.getBitmap());
                            //Img_Project_Pic_Large.setBackground(ob);
                            //Rel_Project_Layout.setBackground(ob);
                        }
                    });
        }




        //mViewPager 監聽
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
//                        msibook_certified_info_detail Tab1 = (msibook_certified_info_detail) mSectionsPagerAdapter.getFragment(position);
//                        Tab1.Find_Certification_Info(getModelID,"處理中");
//                        Tab1.SetF_Status("處理中");//設定 當前Tab為 WHEN 1 THEN '處理中' WHEN 2 THEN '已取消' WHEN 3 THEN '已完成' ELSE '尚未申請需求單'
                        break;
                    case 1:
                        msibook_certified_info_detail Tab1 = (msibook_certified_info_detail) mSectionsPagerAdapter.getFragment(position);
                        Tab1.Find_Certification_Info(getModelID,"處理中");
                        Tab1.SetF_Status("處理中");//設定 當前Tab為 WHEN 1 THEN '處理中' WHEN 2 THEN '已取消' WHEN 3 THEN '已完成' ELSE '尚未申請需求單'
                        break;
                    case 2:
                        msibook_certified_info_detail Tab2 = (msibook_certified_info_detail) mSectionsPagerAdapter.getFragment(position);
                        Tab2.Find_Certification_Info(getModelID,"已完成");
                        Tab2.SetF_Status("已完成");//設定 當前Tab為 WHEN 1 THEN '處理中' WHEN 2 THEN '已取消' WHEN 3 THEN '已完成' ELSE '尚未申請需求單'
                        break;
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });



    }

    @Override
    protected void onStart() {
        super.onStart();

//        msibook_certified_info_detail Tab1 = (msibook_certified_info_detail) mSectionsPagerAdapter.getFragment(0);
//        Tab1.Find_Certification_Info(getModelID,"處理中");
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
            View rootView = inflater.inflate(R.layout.fragment_msibook_certified_info_detail, container, false);

            switch (Position) {
                case 0:
                    rootView = inflater.inflate(R.layout.fragment_msibook_certified_info_detail_add, container, false);
                    return rootView;
                case 1:
                    rootView = inflater.inflate(R.layout.fragment_msibook_certified_info_detail, container, false);
                    return rootView;
                case 2:
                    rootView = inflater.inflate(R.layout.fragment_msibook_certified_info_detail, container, false);
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
        private String ModelID;
        public SectionsPagerAdapter(FragmentManager fm,String ModelID) {
            super(fm);
            mFragmentTags = new HashMap<Integer, String>();
            mFragmentManager = fm;
            this.ModelID = ModelID;
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
                    return msibook_certified_info_detail_add.newInstance("0", "0",getModelName,getModelID);
                case 1:
                    return msibook_certified_info_detail.newInstance("0", "0","處理中",ModelID);
                case 2:
                    return msibook_certified_info_detail.newInstance("0", "0","已完成",ModelID);
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
                    //setTitle("加班紀錄");
                    return "認證申請";
                case 1:
                    //setTitle("加班紀錄");
                    return "進行中";
                case 2:
                    //setTitle("加班紀錄");
                    return "已完成";
            }
            return null;
        }
    }


}
