package dqa.com.msibook;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.design.widget.TabLayout;
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

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class msibook_facility extends AppCompatActivity {

    private TabLayout mTablayout;
    private ViewPager mViewPager;
    private ListView mListView;
    private msibook_facility_adapter Fac_List_Adapter;
    private List<msibook_facility_item> msibook_facility_item_List = new ArrayList<msibook_facility_item>();
    private ProgressDialog progressBar;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msibook_facility);
        progressBar = new ProgressDialog(this);
        progressBar.setCancelable(true);
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.setMessage("資料載入中");

        mContext = msibook_facility.this;
        initData();
        initView();
        mListView = (ListView) findViewById(R.id.Lsv_Facility);
        Find_Fac_List("0");
    }

    private void Find_Fac_List(String Type) {

        //顯示  讀取等待時間Bar
        progressBar.show();

        msibook_facility_item_List.clear();

        RequestQueue mQueue = Volley.newRequestQueue(this);

        String Path = GetServiceData.ServicePath + "/Find_Fac_List?Type=" + Type;

        GetServiceData.getString(Path, mQueue, new GetServiceData.VolleyCallback() {
            @Override
            public void onSuccess(JSONObject result) {

                try {

                    JSONArray UserArray = new JSONArray(result.getString("Key"));

                    for (int i = 0; i < UserArray.length(); i++) {

                        JSONObject IssueData = UserArray.getJSONObject(i);

                        String F_SeqNo = String.valueOf(IssueData.getInt("F_SeqNo"));

                        String F_AssetNo = String.valueOf(IssueData.getString("F_AssetNo"));

                        String F_Type = String.valueOf(IssueData.getString("F_Type"));

                        String F_Location = String.valueOf(IssueData.getString("F_Location"));

                        String F_Facility = String.valueOf(IssueData.getString("F_Facility"));

                        String F_Model = String.valueOf(IssueData.getString("F_Model"));

                        String F_Factory = String.valueOf(IssueData.getString("F_Factory"));

                        String Dept = String.valueOf(IssueData.getString("Dept"));

                        String TEL = String.valueOf(IssueData.getString("TEL"));

                        String EMail = String.valueOf(IssueData.getString("EMail"));

                        String F_Owner = String.valueOf(IssueData.getString("F_Owner"));

                        String HourCost = String.valueOf(IssueData.getDouble("HourCost"));

                        String F_Standard = String.valueOf(IssueData.getString("F_Standard"));

                        String F_Status = String.valueOf(IssueData.getString("F_Status"));

                        String F_Is_Restrict = String.valueOf(IssueData.getString("F_Is_Restrict"));

                        String Using = String.valueOf(IssueData.getInt("Using"));

                        String IMG = String.valueOf(IssueData.getString("IMG"));

                        msibook_facility_item_List.add(i,new msibook_facility_item( F_SeqNo, F_AssetNo, F_Type,  F_Location,  F_Facility,  F_Model,  F_Factory,  Dept,  TEL,  EMail,  F_Owner,  HourCost,  F_Standard,  F_Status,  F_Is_Restrict,  Using,  IMG));

                    }

                    mListView = (ListView)findViewById(R.id.Lsv_Facility);

                    Fac_List_Adapter = new msibook_facility_adapter(mContext,msibook_facility_item_List);

                    mListView.setAdapter(Fac_List_Adapter);

                    //關閉-讀取等待時間Bar
                    progressBar.dismiss();

//                    //專案細項to 第三頁
//                    mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                        @Override
//                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//                            Intent intent = new Intent();
//
//
//                            //開啟Activity
//                            startActivity(intent);
//
//                        }
//                    });
                }
                catch (JSONException ex) {

                    Log.w("Json",ex.toString());

                }

            }
        });


    }

    private void initData() {

    }

    private void initView() {
        mTablayout = (TabLayout) findViewById(R.id.tabs);
        mTablayout.addTab(mTablayout.newTab().setText("機構配備"));
        mTablayout.addTab(mTablayout.newTab().setText("環測配備"));
        mTablayout.addTab(mTablayout.newTab().setText("熱流配備"));
        mTablayout.addTab(mTablayout.newTab().setText("無響室設備"));
        mTablayout.addTab(mTablayout.newTab().setText("電子配備"));
        mTablayout.addTab(mTablayout.newTab().setText("音像配備"));
        mTablayout.addTab(mTablayout.newTab().setText("掃地機配備"));


        mTablayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                int Position = tab.getPosition();

                Find_Fac_List(String.valueOf(Position));
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                int Position = tab.getPosition();

                Find_Fac_List(String.valueOf(Position));
            }
        });

    }


    public class msibook_facility_adapter extends BaseAdapter {

        private LayoutInflater mLayInf;

        private List<msibook_facility_item> msibook_facility_item_List = new ArrayList<msibook_facility_item>();

        private Context ProjectContext;


        public msibook_facility_adapter(Context context, List<msibook_facility_item> msibook_facility_item) {
            mLayInf = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            ProjectContext = context;

            this.msibook_facility_item_List = msibook_facility_item;

        }

        @Override
        public int getCount() {
            return msibook_facility_item_List.size();
        }

        @Override
        public Object getItem(int position) {
            return msibook_facility_item_List.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            View v = new View(ProjectContext);

            v = mLayInf.inflate(R.layout.activity_msibook_facility_item, parent, false);

            final ImageView Img_Fac = (ImageView) v.findViewById(R.id.Img_Fac);
            TextView txt_Status = (TextView) v.findViewById(R.id.txt_Status);
            TextView txt_AssetSN = (TextView) v.findViewById(R.id.txt_AssetSN);
            TextView txt_fac_name = (TextView) v.findViewById(R.id.txt_fac_name);
            TextView txt_fac_location = (TextView) v.findViewById(R.id.txt_fac_location);
            TextView txt_save_place = (TextView) v.findViewById(R.id.txt_save_place);
            TextView txt_save_people = (TextView) v.findViewById(R.id.txt_save_people);
            TextView txt_resource = (TextView) v.findViewById(R.id.txt_resource);
            TextView txt_booking_status = (TextView) v.findViewById(R.id.txt_booking_status);
//            TextView txt_save_place = (TextView) v.findViewById(R.id.txt_save_place);

            LinearLayout linear_booking_status = (LinearLayout) v.findViewById(R.id.linear_booking_status);

            linear_booking_status.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    //從MainActivity 到Main2Activity
                    intent.putExtra("F_Facility",msibook_facility_item_List.get(position).GetF_Facility());
                    intent.putExtra("F_SeqNo",msibook_facility_item_List.get(position).GetF_SeqNo());
                    intent.setClass(msibook_facility.this, msibook_facility_booking_status.class);
                    //開啟Activity
                    startActivity(intent);
                }
            });

            LinearLayout linear_fac_detial = (LinearLayout) v.findViewById(R.id.linear_fac_detial);

            linear_fac_detial.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent();
                    intent.putExtra("F_Facility",msibook_facility_item_List.get(position).GetF_Facility());
                    intent.putExtra("F_SeqNo",msibook_facility_item_List.get(position).GetF_SeqNo());
                    intent.setClass(msibook_facility.this, msibook_facility_detial.class);
                    //開啟Activity
                    startActivity(intent);
                }
            });


            if(msibook_facility_item_List.get(position).GetF_Status().indexOf("0")==-1){
                if(msibook_facility_item_List.get(position).GetUsing().indexOf("1")==-1)
                {
                    txt_Status.setText("可預約");
                    txt_Status.setTextColor(Color.parseColor("#ffffff"));
                    txt_Status.setBackgroundColor(Color.parseColor("#3cd45b"));

                }else{
                    txt_Status.setText("使用中");
                    txt_Status.setTextColor(Color.parseColor("#ffffff"));
                    txt_Status.setBackgroundColor(Color.parseColor("#ed4a47"));
                }

            }else{
                txt_Status.setText("不開放");
                txt_Status.setTextColor(Color.parseColor("#6d7073"));
                txt_Status.setBackgroundColor(Color.parseColor("#d8d8db"));
            }

            txt_AssetSN.setText("財編 : "+msibook_facility_item_List.get(position).GetF_AssetNo());
            txt_fac_name.setText(msibook_facility_item_List.get(position).GetF_Facility());
            txt_fac_location.setText("存放位置：" + msibook_facility_item_List.get(position).GetF_Location());
            txt_save_place.setText("保管單位：" + msibook_facility_item_List.get(position).GetDept());
            txt_save_people.setText("保管人員：" + msibook_facility_item_List.get(position).GetF_Owner());
            txt_resource.setText("使用成本：" + "NTD" + msibook_facility_item_List.get(position).GetHourCost() + "/小時");
            //txt_booking_status.setText(msibook_facility_item_List.get(position).Using == "0" ? "使用中" : "未使用");

            String ImagePath = msibook_facility_item_List.get(position).GetIMG().replace("//172.16.111.114/File","http://wtsc.msi.com.tw/IMS/FileServer");

            Log.w("IMagePath",ImagePath);
            Glide
                    .with(ProjectContext)
                    .load(ImagePath)
                    .asBitmap()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.mipmap.progress_image)
                    .into(new SimpleTarget<Bitmap>(100, 100) {
                        @Override
                        public void onResourceReady(Bitmap resource, GlideAnimation glideAnimation) {

                            Img_Fac.setImageBitmap(resource);

                        }

                        @Override
                        public void onLoadFailed(Exception e, Drawable errorDrawable) {
//                            Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

            return v;
        }
    }

    public class msibook_facility_item {

               String F_SeqNo;
               String F_AssetNo;
               String F_Type;
               String F_Location;
               String F_Facility;
               String F_Model;
               String F_Factory;
               String Dept;
               String TEL;
               String EMail;
               String F_Owner;
               String HourCost;
               String F_Standard;
               String F_Status;
               String F_Is_Restrict;
               String Using;
               String IMG;

        public msibook_facility_item(String F_SeqNo,String F_AssetNo,String F_Type, String F_Location, String F_Facility, String F_Model, String F_Factory, String Dept, String TEL, String EMail, String F_Owner, String HourCost, String F_Standard, String F_Status, String F_Is_Restrict, String Using, String IMG)
        {
            this.F_SeqNo = F_SeqNo;
            this.F_AssetNo = F_AssetNo;
            this.F_Type = F_Type;
            this.F_Location = F_Location;
            this.F_Facility = F_Facility;
            this.F_Model = F_Model;
            this.F_Factory = F_Factory;
            this.Dept = Dept;
            this.TEL = TEL;
            this.EMail = EMail;
            this.F_Owner = F_Owner;
            this.HourCost = HourCost;
            this.F_Standard = F_Standard;
            this.F_Status = F_Status;
            this.F_Is_Restrict = F_Is_Restrict;
            this.Using = Using;
            this.IMG = IMG;
        }

        public String GetF_SeqNo()   { return this.F_SeqNo; }

        public String GetF_AssetNo()
        {
            return this.F_AssetNo;
        }

        public String GetF_Type()
        {
            return this.F_Type;
        }


        public String GetF_Facility()
        {
            return this.F_Facility;
        }
        public String GetF_Location()
        {
            return this.F_Location;
        }

        public String GetF_Model()
        {
            return this.F_Model;
        }

        public String GetF_Factory()
        {
            return this.F_Factory;
        }

        public String GetDept()
        {
            return this.Dept;
        }

        public String GetTEL()
        {
            return this.TEL;
        }

        public String GetEMail()
        {
            return this.EMail;
        }

        public String GetF_Owner()
        {
            return this.F_Owner;
        }

        public String GetHourCost()
        {
            return this.HourCost;
        }

        public String GetF_Standard()
        {
            return this.F_Standard;
        }

        public String GetF_Status()
        {
            return this.F_Status;
        }

        public String GetF_Is_Restrict()
        {
            return this.F_Is_Restrict;
        }

        public String GetUsing()
        {
            return this.Using;
        }

        public String GetIMG()
        {
            return this.IMG;
        }

    }

}
