package dqa.com.msibook;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class msibook_dqaweekly_main_page2 extends AppCompatActivity {

    private List<String> ArrayCName = new ArrayList<String>();
    private List<String> ArrayDeptID = new ArrayList<String>();
    private ListView mListView;

    private DetailAdapter mDetailAdapter;
    private List<Detail_Item> Detail_Item_List = new ArrayList<Detail_Item>();

    private List<Week_Project_Item>Week_Project_Item_list = new ArrayList<Week_Project_Item>();
    private WeekProjectAdapter mWeekProjectAdapter;
    private Context mContext;
    private ProgressDialog progressBar;
    private String MainTitle;

    //帶到第三頁的變數判斷
    private String m2putEtrawk;//給第三頁週
    private String m2putTitle;//給第三頁部門名稱

    private String m2putEtraDepID;//給第三頁部門代號
    private String m2putEtraYear;//給第三頁年

    private String CB;
    private String OverPeopleData = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msibook_dqaweekly_main_page2);

        //讀取時間Bar
        progressBar = new ProgressDialog(this);
        progressBar.setCancelable(true);
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.setMessage("資料載入中");

        //list的Title
        TextView title1 = (TextView) findViewById(R.id.textView_listtop);

        mContext = msibook_dqaweekly_main_page2.this;
        mListView = (ListView) findViewById(R.id.list2);
        TextView textshowwk = (TextView) findViewById(R.id.textViewshowweek);
        TextView textshowdp = (TextView) findViewById(R.id.textViewshowdepart);

        String getEtraCB = getIntent().getStringExtra("CC");//抓第一頁 實際出勤
        CB = getEtraCB;

        String getEtraDepID = getIntent().getStringExtra("ChoiceDepID");//抓第一頁選的部門代號
        m2putEtraDepID = getEtraDepID;
        Log.w("DeptID",getEtraDepID);

        String getEtraYear = getIntent().getStringExtra("Year");//抓第一頁選的部門代號
        m2putEtraYear = getEtraYear;


        String Title = getIntent().getStringExtra("Title");//抓第一頁部門名稱
        m2putTitle = Title;

        String Week = getIntent().getStringExtra("Week");//抓第一頁選的週次
        m2putEtrawk = Week.replace("週","");

        String RowType = getIntent().getStringExtra("RowType");
        //讀前頁部門Title
        textshowdp.setText(Title);
        //讀前頁週
        textshowwk.setText(Week);

        String RealWeek = Week.replace("週","");

        Log.w("RealWeek",RealWeek);

        MainTitle = RowType;

        switch (RowType)
        {
            case "人力":
                //執行Find_Man_Power副程式
                Find_Man_Power(getEtraDepID);
                title1.setText("編制");

                break;
            case "工時":
                Find_WorkHour(getEtraDepID,getEtraYear,RealWeek);
                title1.setText("工時");
                break;
            case "加班":
                Find_Over_Hour(getEtraDepID,getEtraYear,RealWeek);
                title1.setText("加班");
                break;
            case "請假":
                Find_Leave_List(getEtraDepID,getEtraYear,RealWeek);
                title1.setText("請假");
                break;
            case "專案":
                Find_Week_Project(getEtraDepID,getEtraYear,RealWeek);
                title1.setText("專案");
                break;
            case "摘要":
                Find_Message_Content(getEtraDepID,RealWeek);
                title1.setText("摘要");
                break;
        }

    }

    //超過時數彈跳視窗
    private void popupkeyman(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("注意!!").setMessage("以下人員目前已超過40小時!!"+"\n" + OverPeopleData).setPositiveButton("我知道了", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        }).show();
    }

    //--------------------------------------------------------------------------------Void--------------------------------------------------------------------------------
    //DB編制細部清單
    private void Find_Man_Power(String DeptID) {
        //顯示  讀取等待時間Bar
        progressBar.show();

        Detail_Item_List.clear();

        RequestQueue mQueue = Volley.newRequestQueue(this);

        HTTPSTrustManager.allowAllSSL();//信任所有证书，信任憑證
        String Path = GetServiceData.DQAWeeklyPath + "/Find_Man_Power?DeptID=" + DeptID;

        GetServiceData.getString(Path, mQueue, new GetServiceData.VolleyCallback() {
            @Override
            public void onSuccess(JSONObject result) {

                try {
                    JSONArray UserArray = new JSONArray(result.getString("Key"));

                    for (int i = 0; i < UserArray.length(); i++) {
                        JSONObject IssueData = UserArray.getJSONObject(i);

                        String CName = String.valueOf(IssueData.getString("CName"));

                        String title = String.valueOf(IssueData.getString("title"));

                        String WorkID = String.valueOf(IssueData.getString("WorkID"));

                        Detail_Item_List.add(i,new Detail_Item(CName,title,WorkID,"",""));
                    }

                    mListView = (ListView)findViewById(R.id.list2);

                    mDetailAdapter = new DetailAdapter(mContext,Detail_Item_List,MainTitle);

                    mListView.setAdapter(mDetailAdapter);

                    //關閉-讀取等待時間Bar
                    progressBar.dismiss();

                    //編制細項to 第三頁
                    mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        }
                    });
                }
                catch (JSONException ex) {
                    Log.w("Json",ex.toString());
                }
            }
        });
    }
    //DB加班細部清單
    private void Find_Over_Hour(String DeptID,String Year,String Week) {

        //顯示  讀取等待時間Bar
        progressBar.show();
        Detail_Item_List.clear();

        RequestQueue mQueue = Volley.newRequestQueue(this);

        HTTPSTrustManager.allowAllSSL();//信任所有证书，信任憑證
        String Path = GetServiceData.DQAWeeklyPath + "/Find_Over_Hour?DeptID=" + DeptID + "&Year=" + Year + "&Week=" + Week;

        GetServiceData.getString(Path, mQueue, new GetServiceData.VolleyCallback() {
            @Override
            public void onSuccess(JSONObject result) {
                try {

                    JSONArray UserArray = new JSONArray(result.getString("Key"));

                    for (int i = 0; i < UserArray.length(); i++) {
                        JSONObject IssueData = UserArray.getJSONObject(i);

                        String CName = String.valueOf(IssueData.getString("CName"));

                        String Column1 = String.valueOf(IssueData.getDouble("Column1"));

                        String MonthTotal = String.valueOf(IssueData.getDouble("MonthTotal"));

                        String WorkID = String.valueOf(IssueData.getLong("WorkID"));

                        Detail_Item_List.add(i,new Detail_Item(CName,"",WorkID,MonthTotal,Column1));

                        if (Double.valueOf(MonthTotal) > 40 )
                        {
                            OverPeopleData += CName + "\n";

                        }
                    }
                    if (OverPeopleData.length() > 0){
                        popupkeyman();
                    }

                    mListView = (ListView)findViewById(R.id.list2);

                    mDetailAdapter = new DetailAdapter(mContext,Detail_Item_List,MainTitle);

                    mListView.setAdapter(mDetailAdapter);

                    //關閉-讀取等待時間Bar
                    progressBar.dismiss();

                    //加班細項to 第三頁
                    mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            Intent intent = new Intent();

                            intent.putExtra("m2Week", m2putEtrawk);//給第三頁Week

                            intent.putExtra("m2ChoiceDepID", m2putEtraDepID);//給第三頁部門代號

                            intent.putExtra("m2ChoiceYear", m2putEtraYear);//給第三頁部門代號

                            intent.putExtra("m2putTitle", m2putTitle);//給第三頁部門名稱

                            intent.putExtra("m2Rowtype",MainTitle);//給第三頁 選擇的 Type  EX 加班OR請假

                            //intent.putExtra("Mantitle",DetailAdapter.Detail_Item_List.get(position).Gettitle());
                            intent.putExtra("Mantitle",Detail_Item_List.get(position).GetName());
                            intent.putExtra("Mannumber",Detail_Item_List.get(position).GetWorkID());//給第三頁員工 工號
                            intent.setClass(msibook_dqaweekly_main_page2.this, msibook_dqaweekly_main_page3.class);
                            //開啟Activity
                            startActivity(intent);
                        }
                    });

                }
                catch (JSONException ex) {

                    Log.w("Json",ex.toString());
                }

            }
        });


    }

    //DB請假細部清單
    private void Find_Leave_List(String DeptID,String Year,String Week) {

        //顯示  讀取等待時間Bar
        progressBar.show();

        Detail_Item_List.clear();

        RequestQueue mQueue = Volley.newRequestQueue(this);

        HTTPSTrustManager.allowAllSSL();//信任所有证书，信任憑證
        String Path = GetServiceData.DQAWeeklyPath + "/Find_Leave_List?DeptID=" + DeptID + "&Year=" + Year + "&Week=" + Week;

        GetServiceData.getString(Path, mQueue, new GetServiceData.VolleyCallback() {
            @Override
            public void onSuccess(JSONObject result) {

                try {

                    JSONArray UserArray = new JSONArray(result.getString("Key"));

                    for (int i = 0; i < UserArray.length(); i++) {
                        JSONObject IssueData = UserArray.getJSONObject(i);

                        String F_Name = String.valueOf(IssueData.getString("F_Name"));

                        String F_ID = String.valueOf(IssueData.getLong("F_ID"));

                        String LeaveHour = String.valueOf(IssueData.getDouble("LeaveHour"));

                        //String F_ID = String.valueOf(IssueData.getString("F_ID"));

                        Detail_Item_List.add(i,new Detail_Item(F_Name,"","",F_ID,LeaveHour));

                    }

                    mListView = (ListView)findViewById(R.id.list2);

                    mDetailAdapter = new DetailAdapter(mContext,Detail_Item_List,MainTitle);

                    mListView.setAdapter(mDetailAdapter);

                    //關閉-讀取等待時間Bar
                    progressBar.dismiss();

                    //加班細項to 第三頁
                    mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            Intent intent = new Intent();

                            intent.putExtra("m2Week", m2putEtrawk);//給第三頁Week

                            intent.putExtra("m2ChoiceDepID", m2putEtraDepID);//給第三頁部門代號

                            intent.putExtra("m2ChoiceYear", m2putEtraYear);//給第三頁部門代號

                            intent.putExtra("m2putTitle", m2putTitle);//給第三頁部門名稱

                            intent.putExtra("m2Rowtype",MainTitle);//給第三頁 選擇的 Type  EX 加班OR請假

                            //intent.putExtra("Mantitle",DetailAdapter.Detail_Item_List.get(position).Gettitle());
                            intent.putExtra("Mantitle",Detail_Item_List.get(position).GetName());
                            intent.putExtra("Mannumber",Detail_Item_List.get(position).GetF_ID());//給第三頁員工 工號

                            intent.setClass(msibook_dqaweekly_main_page2.this, msibook_dqaweekly_main_page3.class);
                            //開啟Activity
                            startActivity(intent);
                        }
                    });
                }
                catch (JSONException ex) {

                    Log.w("Json",ex.toString());
                }
            }
        });
    }

    //DB專案細部清單
    private void Find_Week_Project(String DeptID,String Year,String Week) {

        //顯示  讀取等待時間Bar
        progressBar.show();
        Week_Project_Item_list.clear();

        RequestQueue mQueue = Volley.newRequestQueue(this);

        HTTPSTrustManager.allowAllSSL();//信任所有证书，信任憑證
        String Path = GetServiceData.DQAWeeklyPath + "/Find_Week_Project?DeptID=" + DeptID + "&Year=" + Year + "&Week=" + Week;

        GetServiceData.getString(Path, mQueue, new GetServiceData.VolleyCallback() {
            @Override
            public void onSuccess(JSONObject result) {

                try {
                    JSONArray UserArray = new JSONArray(result.getString("Key"));

                    for (int i = 0; i < UserArray.length(); i++) {

//                        "F_Map": "EVT",
//                                "Cnt": 2,
//                                "ModelList": "MS-7A81,MS-7A89",
//                                "ModelIDList": "12638,12705"

                        JSONObject IssueData = UserArray.getJSONObject(i);

                        String F_Map = String.valueOf(IssueData.getString("F_Map"));
                        String Cnt = String.valueOf(IssueData.getString("Cnt"));

                        String ModelList = String.valueOf(IssueData.getString("ModelList"));

                        String ModelIDList = String.valueOf(IssueData.getString("ModelIDList"));

                        Week_Project_Item_list.add(i,new Week_Project_Item(F_Map,Cnt,ModelList,ModelIDList));
                    }

                    mListView = (ListView)findViewById(R.id.list2);

                    //mOverhourAdapter = new OverhourAdapter(mContext,Overhour_Item_List,MainTitle);
                    mWeekProjectAdapter = new WeekProjectAdapter(mContext,Week_Project_Item_list);

                    mListView.setAdapter(mWeekProjectAdapter);

                    //關閉-讀取等待時間Bar
                    progressBar.dismiss();

                    //專案細項to 第三頁
                    mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            Intent intent = new Intent();

                            intent.putExtra("m2Week", m2putEtrawk);//給第三頁Week

                            intent.putExtra("m2ChoiceDepID", m2putEtraDepID);//給第三頁部門代號

                            intent.putExtra("m2ChoiceYear", m2putEtraYear);//給第三頁部門代號

                            intent.putExtra("m2putTitle", m2putTitle);//給第三頁部門名稱

                            intent.putExtra("m2Rowtype",MainTitle);//給第三頁 選擇的 Type  EX 加班OR請假OR專案

                            intent.putExtra("Mantitle",Week_Project_Item_list.get(position).GetF_Map());//給第三頁 Title EX:MP、EVT、DVT
                            intent.putExtra("m2ModelList",Week_Project_Item_list.get(position).GetModelList());//給第三頁 MS-7A61  MS-7A81
                            intent.putExtra("m2ModelIDList",Week_Project_Item_list.get(position).GetModelIDList());//給第三頁 12637  12638

                            intent.setClass(msibook_dqaweekly_main_page2.this, msibook_dqaweekly_main_page3.class);
                            //開啟Activity
                            startActivity(intent);
                        }
                    });
                }
                catch (JSONException ex) {
                    Log.w("Json",ex.toString());
                }
            }
        });
    }

    //DB工時細部清單
    private void Find_WorkHour(String DeptID,String Year,String Week) {

        //顯示  讀取等待時間Bar
        progressBar.show();
        Detail_Item_List.clear();

        RequestQueue mQueue = Volley.newRequestQueue(this);

        HTTPSTrustManager.allowAllSSL();//信任所有证书，信任憑證
        String Path = GetServiceData.DQAWeeklyPath + "/Find_WorkHour?DeptID=" + DeptID + "&Year=" + Year + "&Week=" + Week;

        GetServiceData.getString(Path, mQueue, new GetServiceData.VolleyCallback() {
            @Override
            public void onSuccess(JSONObject result) {

                try {
                    JSONArray UserArray = new JSONArray(result.getString("Key"));

                    for (int i = 0; i < UserArray.length(); i++) {
                        JSONObject IssueData = UserArray.getJSONObject(i);

                        String Title = String.valueOf(IssueData.getString("Title"));

                        String Hour = String.valueOf(IssueData.getLong("Hour"));
                        Log.w("HourTest",Hour);

                        //如果不是請假才輸入資料，請假的話就跳過!
                        if (!Title.contains("請假")){
                            if (Title.contains("實際出勤")) {
                                Detail_Item_List.add(i, new Detail_Item(Title, "", "", "", CB));//抓前頁的 實際出勤
                            }
                            if (Title.contains("應出勤")) {
                                Detail_Item_List.add(i, new Detail_Item(Title, "", "", "", Hour));
                            }
                        }
                    }

                    mListView = (ListView)findViewById(R.id.list2);

                    mDetailAdapter = new DetailAdapter(mContext,Detail_Item_List,MainTitle);

                    mListView.setAdapter(mDetailAdapter);

                    //關閉-讀取等待時間Bar
                    progressBar.dismiss();

                    //工時細項to 第三頁
                    mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

//                            Intent intent = new Intent();
//
//                            intent.setClass(msibook_dqaweekly_main_page2.this, msibook_dqaweekly_main_page3.class);
//                            //開啟Activity
//                            startActivity(intent);
                        }
                    });
                }
                catch (JSONException ex) {
                    Log.w("Json",ex.toString());
                }
            }
        });
    }
    //DB摘要
    private void Find_Message_Content(String DeptID,String Week) {

        //顯示  讀取等待時間Bar
        progressBar.show();
        Detail_Item_List.clear();

        RequestQueue mQueue = Volley.newRequestQueue(this);

        HTTPSTrustManager.allowAllSSL();//信任所有证书，信任憑證
        String Path = GetServiceData.DQAWeeklyPath + "/Find_Weekly_Content?DeptID=" + DeptID + "&F_Week=" + Week;

        GetServiceData.getString(Path, mQueue, new GetServiceData.VolleyCallback() {
            @Override
            public void onSuccess(JSONObject result) {

                try {
                    JSONArray UserArray = new JSONArray(result.getString("Key"));

                    for (int i = 0; i < UserArray.length(); i++) {
                        JSONObject IssueData = UserArray.getJSONObject(i);

                        String F_SeqNo = String.valueOf(IssueData.getString("F_SeqNo"));

                        String F_Desc = String.valueOf(IssueData.getString("F_Desc"));

                        Detail_Item_List.add(i,new Detail_Item(F_Desc,"","","",""));
                    }

                    mListView = (ListView)findViewById(R.id.list2);

                    mDetailAdapter = new DetailAdapter(mContext,Detail_Item_List,MainTitle);

                    mListView.setAdapter(mDetailAdapter);

                    //關閉-讀取等待時間Bar
                    progressBar.dismiss();
                }
                catch (JSONException ex) {
                    Log.w("Json",ex.toString());
                }
            }
        });
    }

    //--------------------------------------------------------------------------------Adapter--------------------------------------------------------------------------------
    //DetailAdapter
    public class DetailAdapter extends BaseAdapter {

        private LayoutInflater mLayInf;

        private List<Detail_Item> Detail_Item_List;

        private Context ProjectContext;

        private String Title;

        public DetailAdapter(Context context,  List<Detail_Item> Detail_Item_List,String Title)
        {
            mLayInf = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            ProjectContext = context;

            this.Title = Title;

            this.Detail_Item_List = Detail_Item_List;

        }
        @Override
        public int getCount() {
            return Detail_Item_List.size();
        }

        @Override
        public Object getItem(int position) {
            return Detail_Item_List.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View v = new View(ProjectContext);

            v = mLayInf.inflate(R.layout.msibook_dqaweekly_main_page2_adapter, parent, false);

            RelativeLayout adp2Relativelayout = (RelativeLayout) v.findViewById(R.id.adapter2_Relativelayout);
            TextView textView1 = (TextView) v.findViewById(R.id.textView1);
            TextView textView2 = (TextView) v.findViewById(R.id.textView2);
            TextView textView3 = (TextView) v.findViewById(R.id.textView3);
            TextView textView4 = (TextView) v.findViewById(R.id.textView4);
            LinearLayout m2linear = (LinearLayout) v.findViewById(R.id.m2linear);
            Button button1 = (Button) v.findViewById(R.id.button1);
            Button buttonh = (Button) v.findViewById(R.id.buttonm2H);
            Button buttonup = (Button) v.findViewById(R.id.buttonup);
            Button buttondown = (Button) v.findViewById(R.id.buttondown);


            textView1.setText(Detail_Item_List.get(position).GetName());

            textView2.setText(Detail_Item_List.get(position).GetWorkID());

            textView3.setText(Detail_Item_List.get(position).GetHour());

            textView4.setText(Detail_Item_List.get(position).Gettitle());


            if (Detail_Item_List.get(position).Gettitle() == "null"){
                textView4.setText("");
            }

            if (Title.contains("人力")){
                textView3.setVisibility(View.GONE);
                button1.setVisibility(View.GONE);
                buttonup.setVisibility(View.GONE);
                buttondown.setVisibility(View.GONE);
                buttonh.setVisibility(View.GONE);
            }
            if (Title.contains("專案")){
                textView2.setVisibility(View.GONE);
                textView4.setVisibility(View.GONE);
                buttonup.setVisibility(View.GONE);
                buttondown.setVisibility(View.GONE);
                textView3.setTextSize(24);
                m2linear.setLayoutParams(textView1.getLayoutParams());
                buttonh.setVisibility(View.GONE);
            }

            if (Title.contains("工時")){
                button1.setVisibility(View.GONE);
                textView2.setVisibility(View.GONE);
                buttonup.setVisibility(View.GONE);
                buttondown.setVisibility(View.GONE);
                textView3.setTextSize(24);
                m2linear.setLayoutParams(textView1.getLayoutParams());
//            textView3.setLayoutParams(textView1.getLayoutParams());
                textView4.setVisibility(View.GONE);
                //m2linear.setOrientation(LinearLayout.HORIZONTAL);
            }

            if (Title.contains("加班")){
                textView3.setVisibility(View.GONE);
                buttonh.setVisibility(View.GONE);
                double a = Double.parseDouble(Detail_Item_List.get(position).GetF_ID());
                double b = Double.parseDouble(Detail_Item_List.get(position).GetHour());
                if(a > 40){
                    textView2.setText("當月 "+ Detail_Item_List.get(position).GetF_ID());
                    textView2.setTextColor(Color.parseColor("#f0625d"));
                }else{
                    textView2.setText("當月 "+ Detail_Item_List.get(position).GetF_ID());
                    textView2.setTextColor(Color.parseColor("#ffffff"));
                    //textView2.setTextColor(Color.parseColor("#064472"));
                }
                if (b > 40){
                    textView4.setText("當週 "+ Detail_Item_List.get(position).GetHour());
                    textView4.setTextColor(Color.parseColor("#f0625d"));
                }else{
                    textView4.setText("當週 "+ Detail_Item_List.get(position).GetHour());
                    textView2.setTextColor(Color.parseColor("#ffffff"));
                    //textView2.setTextColor(Color.parseColor("#064472"));
                }
                textView2.setTextSize(20);
                textView4.setTextSize(20);
                m2linear.setLayoutParams(textView1.getLayoutParams());

            }

            if (Title.contains("請假")){
                textView2.setVisibility(View.GONE);
                textView4.setVisibility(View.GONE);
                buttonup.setVisibility(View.GONE);
                buttondown.setVisibility(View.GONE);
                textView3.setTextSize(24);
                m2linear.setLayoutParams(textView1.getLayoutParams());

            }
            if (Title.contains("摘要")){
                //textView1.setVisibility(View.GONE);
                textView2.setVisibility(View.GONE);
                textView4.setVisibility(View.GONE);
                button1.setVisibility(View.GONE);
                buttonup.setVisibility(View.GONE);
                buttondown.setVisibility(View.GONE);
                buttonh.setVisibility(View.GONE);
                //adp2Relativelayout.setBackgroundResource(R.mipmap.pic_summary_construction);//建置中圖
                //textView1.setText(Detail_Item_List.get(position).GetName());
                //textView1.setText(Html.fromHtml(Detail_Item_List.get(position).GetName(), Html.FROM_HTML_MODE_COMPACT));
                textView1.setText(Html.fromHtml(Detail_Item_List.get(position).GetName()));
            }



            Log.w("DetailAdapter","test");

            return v;
        }

    }

    //WeekProjectAdapter
    public class WeekProjectAdapter extends BaseAdapter {

        private LayoutInflater mLayInf;

        private List<Week_Project_Item> Week_Project_Item_list;

        private Context ProjectContext;

        private String Title;

        public WeekProjectAdapter(Context context,  List<Week_Project_Item> Week_Project_Item_list)
        {
            mLayInf = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            ProjectContext = context;

            this.Title = Title;

            this.Week_Project_Item_list = Week_Project_Item_list;

        }
        @Override
        public int getCount() {
            return Week_Project_Item_list.size();
        }

        @Override
        public Object getItem(int position) {
            return Week_Project_Item_list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View v = new View(ProjectContext);

            v = mLayInf.inflate(R.layout.msibook_dqaweekly_main_page2_adapter_project_item, parent, false);

            TextView textView1 = (TextView) v.findViewById(R.id.textView1);
            TextView textView2 = (TextView) v.findViewById(R.id.textView2);
            Button button1 = (Button) v.findViewById(R.id.button1);

            textView1.setText(Week_Project_Item_list.get(position).GetF_Map());
            textView2.setText(Week_Project_Item_list.get(position).GetCnt());

            Log.w("WeekProjectAdapter","test");

            return v;
        }

    }

    //--------------------------------------------------------------------------------Item--------------------------------------------------------------------------------
    //Detail_Item
    public class Detail_Item {

        String Name;

        String title;

        String WorkID;

        String F_ID;

        String Hour;



        public Detail_Item(String Name,String title,String WorkID,String F_ID,String Hour)
        {
            this.Name = Name;

            this.title = title;

            this.WorkID = WorkID;

            this.F_ID = F_ID;

            this.Hour = Hour;
        }


        public String GetName()
        {
            return this.Name;
        }

        public String Gettitle()
        {
            return this.title;
        }

        public String GetWorkID()
        {
            return this.WorkID;
        }
        public String GetF_ID()
        {
            return this.F_ID;
        }
        public String GetHour()
        {
            return this.Hour;
        }
    }

    //Week_Project_Item
    public class Week_Project_Item {

        String F_Map;//DVT MVT MP

        String Cnt;// 1   2 3 4

        String ModelList;//MS-0027     MS-7A61    MS-7A81

        String ModelIDList;// 1048        12637        12638

        public Week_Project_Item(String F_Map,String Cnt,String ModelList,String ModelIDList)
        {
            this.F_Map = F_Map;

            this.Cnt = Cnt;

            this.ModelList = ModelList;

            this.ModelIDList = ModelIDList;
        }

        public String GetF_Map()
        {
            return this.F_Map;
        }

        public String GetCnt()
        {
            return this.Cnt;
        }

        public String GetModelList()
        {
            return this.ModelList;
        }
        public String GetModelIDList()
        {
            return this.ModelIDList;
        }

    }

}
