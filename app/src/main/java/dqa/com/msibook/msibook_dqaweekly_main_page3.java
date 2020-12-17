package dqa.com.msibook;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static dqa.com.msibook.R.id.m3textView_listtop;

public class msibook_dqaweekly_main_page3  extends AppCompatActivity {

    private List<Overhour_Item> Overhour_Item_List = new ArrayList<Overhour_Item>();//加班

    private List<LeaveData_Item> LeaveData_Item_List = new ArrayList<LeaveData_Item>();//請假

    private List<Week_Project_Item>Week_Project_Item_list = new ArrayList<Week_Project_Item>();

    private OverhourAdapter mOverhourAdapter;
    private LeaveDataAdapter mLeaveDataAdapter;
    private WeekProjectAdapter mWeekProjectAdapter;
    private ListView mListView;
    private Context mContext;
    private ProgressDialog progressBar;
    //private String MainTitle;
    private String[] ModelNameList;

    private String[] ModelIDList;

    //帶到第三頁的變數判斷
    private String m3putEtraYear;//給第三頁年
    private String m3putEtrawk;//給第三頁週
    private String m3putTitle;//給第三頁部門名稱

    private String m3putEtraDepID;//給第三頁部門代號
    private String getm2mannumber;
    private String getDate;

    //DB加班細部清單
    private void Find_Over_Hour_ByDate_Detail(String F_Keyin,String Date) {

        //顯示  讀取等待時間Bar
        progressBar.show();

        Overhour_Item_List.clear();

        RequestQueue mQueue = Volley.newRequestQueue(this);

        String Path = "http://wtsc.msi.com.tw/IMS/Weekly_Report.asmx/Find_Over_Hour_ByDate_Detail?F_Keyin=" + F_Keyin + "&Date=" + Date;

        getString(Path, mQueue, new GetServiceData.VolleyCallback() {
            @Override
            public void onSuccess(JSONObject result) {

                try {

                    JSONArray UserArray = new JSONArray(result.getString("Key"));

                    for (int i = 0; i < UserArray.length(); i++) {
                        JSONObject IssueData = UserArray.getJSONObject(i);

                        String Name = String.valueOf(IssueData.getString("Name"));

                        String F_SRealTime = String.valueOf(IssueData.getString("F_SRealTime")).replace("T", " ");

                        String F_ERealTime = String.valueOf(IssueData.getString("F_ERealTime")).replace("T", " ");

                        String F_TotalHour = String.valueOf(IssueData.getDouble("F_TotalHour"));

                        String F_Model = String.valueOf(IssueData.getString("F_Model"));

                        String F_Subject = String.valueOf(IssueData.getString("F_Subject"));

                        String F_Type = String.valueOf(IssueData.getString("F_Type"));

                        Overhour_Item_List.add(i, new Overhour_Item(Name, F_SRealTime, F_ERealTime, F_TotalHour, F_Model, F_Subject, F_Type));

                    }

                    mListView = (ListView) findViewById(R.id.list_m3);

                    //mOverhourAdapter = new OverhourAdapter(mContext,Overhour_Item_List,MainTitle);
                    mOverhourAdapter = new OverhourAdapter(mContext, Overhour_Item_List);

                    mListView.setAdapter(mOverhourAdapter);

                    //關閉-讀取等待時間Bar
                    progressBar.dismiss();

                    //加班細項to 第三頁
                    mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        }
                    });

                } catch (JSONException ex) {

                    Log.w("Json", ex.toString());
                }

            }
        });

    }

    //DB加班細部清單
    private void Find_Over_Hour(String DeptID,String Year,String Week) {

        //顯示  讀取等待時間Bar
        progressBar.show();

        Overhour_Item_List.clear();

        RequestQueue mQueue = Volley.newRequestQueue(this);

        String Path = "http://wtsc.msi.com.tw/IMS/Weekly_Report.asmx/Find_Over_Data?F_Keyin=" + DeptID + "&Year=" + Year + "&Week=" + Week;

        getString(Path, mQueue, new GetServiceData.VolleyCallback() {
            @Override
            public void onSuccess(JSONObject result) {

                try {

                    JSONArray UserArray = new JSONArray(result.getString("Key"));

                    for (int i = 0; i < UserArray.length(); i++) {
                        JSONObject IssueData = UserArray.getJSONObject(i);

                        String Name = String.valueOf(IssueData.getString("Name"));

                        String F_SRealTime = String.valueOf(IssueData.getString("F_SRealTime")).replace("T"," ");

                        String F_ERealTime = String.valueOf(IssueData.getString("F_ERealTime")).replace("T"," ");

                        String F_TotalHour = String.valueOf(IssueData.getDouble("F_TotalHour"));

                        String F_Model = String.valueOf(IssueData.getString("F_Model"));

                        String F_Subject = String.valueOf(IssueData.getString("F_Subject"));

                        String F_Type = String.valueOf(IssueData.getString("F_Type"));

                        Overhour_Item_List.add(i,new Overhour_Item(Name,F_SRealTime,F_ERealTime,F_TotalHour,F_Model,F_Subject,F_Type));

                    }

                    mListView = (ListView)findViewById(R.id.list_m3);

                    //mOverhourAdapter = new OverhourAdapter(mContext,Overhour_Item_List,MainTitle);
                    mOverhourAdapter = new OverhourAdapter(mContext,Overhour_Item_List);

                    mListView.setAdapter(mOverhourAdapter);

                    //關閉-讀取等待時間Bar
                    progressBar.dismiss();

                    //加班細項to 第三頁
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

    //DB請假細部清單
    private void Find_Leave_Data(String DeptID,String Year,String Week) {

        //顯示  讀取等待時間Bar
        progressBar.show();

        LeaveData_Item_List.clear();

        RequestQueue mQueue = Volley.newRequestQueue(this);

        String Path = "http://wtsc.msi.com.tw/IMS/Weekly_Report.asmx/Find_Leave_Data?F_Keyin=" + DeptID + "&Year=" + Year + "&Week=" + Week;

        getString(Path, mQueue, new GetServiceData.VolleyCallback() {
            @Override
            public void onSuccess(JSONObject result) {

                try {

                    JSONArray UserArray = new JSONArray(result.getString("Key"));

                    for (int i = 0; i < UserArray.length(); i++) {
                        JSONObject IssueData = UserArray.getJSONObject(i);

                        String F_Owner = String.valueOf(IssueData.getString("F_Owner"));

                        String F_S_Date = String.valueOf(IssueData.getString("F_S_Date")).replace("T"," ");

                        String F_E_Date = String.valueOf(IssueData.getString("F_E_Date")).replace("T"," ");

                        String F_TotalHour = String.valueOf(IssueData.getDouble("F_TotalHour"));

                        String F_LeaveType = String.valueOf(IssueData.getString("F_LeaveType"));

                        String F_Desc = String.valueOf(IssueData.getString("F_Desc"));

                        LeaveData_Item_List.add(i,new LeaveData_Item(F_Owner,F_S_Date,F_E_Date,F_TotalHour,F_LeaveType,F_Desc));

                    }

                    mListView = (ListView)findViewById(R.id.list_m3);

                    //mOverhourAdapter = new OverhourAdapter(mContext,Overhour_Item_List,MainTitle);
                    mLeaveDataAdapter = new LeaveDataAdapter(mContext,LeaveData_Item_List);

                    mListView.setAdapter(mLeaveDataAdapter);

                    //關閉-讀取等待時間Bar
                    progressBar.dismiss();


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

    //專案階段清單
    private void Find_Week_Project() {

        for (int i = 0 ;i< ModelIDList.length;i++)
        {
            Week_Project_Item_list.add(i,new Week_Project_Item(ModelNameList[i],"","",ModelIDList[i]));//*********************

        }


        mListView = (ListView)findViewById(R.id.list_m3);

        //mOverhourAdapter = new OverhourAdapter(mContext,Overhour_Item_List,MainTitle);
        mWeekProjectAdapter = new WeekProjectAdapter(mContext,Week_Project_Item_list);

        mListView.setAdapter(mWeekProjectAdapter);

        //
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

//                Intent intent = new Intent();
//
//                intent.putExtra("m3Week", m3putEtrawk);//給第四頁Week
//
//                intent.putExtra("m3putTitle", m3putTitle);//給第四頁部門名稱
//
//                intent.putExtra("m3ChoiceDepID", m3putEtraDepID);//給第四頁部門代號
//
//                intent.putExtra("m3ModelList",Week_Project_Item_list.get(position).GetF_Map());//給第四頁 MS - XXXX
//                intent.putExtra("m3ModelIDList",Week_Project_Item_list.get(position).GetModelIDList());//給第四頁 專案代號
//                //intent.putExtra("m3ModelIDList","12637");//給第四頁 專案代號
//
//                intent.setClass(msibook_dqaweekly_main_page3.this, Main4Activity.class);
//
//                startActivity(intent);



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



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msibook_dqaweekly_main_page3);


        String getm2EtraDepID = getIntent().getStringExtra("m2ChoiceDepID");//部門代號 EX:21751
        m3putEtraDepID = getm2EtraDepID;
        Log.w("getm2EtraDepID", getm2EtraDepID);
        String getm2putTitle = getIntent().getStringExtra("m2putTitle");//讀取前頁部門名稱 EX 7861 驗證二部一課
        m3putTitle = getm2putTitle;
        Log.w("getm2putTitle", getm2putTitle);
        String getm2putEtrawk = getIntent().getStringExtra("m2Week");//讀取前頁週次 EX第三週
        m3putEtrawk = getm2putEtrawk;
        Log.w("getm2putEtrawk", getm2putEtrawk);//

        String getm2putEtraYear = getIntent().getStringExtra("m2ChoiceYear");//讀取前頁週次 EX第三週
        m3putEtraYear = getm2putEtraYear;
        Log.w("getm2putEtraYear", getm2putEtraYear);

        String getm2mantitle = getIntent().getStringExtra("Mantitle");//讀取前頁點選的 名字 or 階段
        Log.w("getm2mantitle", getm2mantitle);
        getm2mannumber = getIntent().getStringExtra("Mannumber");//讀取前頁 工號 EX 10015812
        String getm2rowtype = getIntent().getStringExtra("m2Rowtype");
        Log.w("getm2rowtype", getm2rowtype);
        getDate = getIntent().getStringExtra("getDate");
        //Log.w("getDate", getDate);


        String getm2m2ModelList = getIntent().getStringExtra("m2ModelList");//抓 MS-7A61  MS-7A81

        if (getm2m2ModelList != null)
        {
            ModelNameList = getm2m2ModelList.split(",");
            Log.w("getm2m2ModelList", getm2m2ModelList);
        }

        String getm2ModelIDList = getIntent().getStringExtra("m2ModelIDList");// 抓  12637  12638
        if (getm2ModelIDList != null)
        {
            ModelIDList = getm2ModelIDList.split(",");
            Log.w("getm2ModelIDList", getm2ModelIDList);
        }

        TextView mantitl = (TextView) findViewById(m3textView_listtop);
        TextView textshowwk = (TextView) findViewById(R.id.m3textViewshowweek);
        TextView textshowdp = (TextView) findViewById(R.id.m3textViewshowdepart);

        mContext = msibook_dqaweekly_main_page3.this;
        textshowdp.setText(getm2putTitle);// EX 7861 驗證二部一課
//        mantitl.setText(getm2mannumber + " - " + getm2mantitle); //EX 10015812 - XXX  加班
        textshowwk.setText(getm2putEtrawk + "週"); // EX 第三週

        //Find_Over_Hour(getm2mannumber,getm2putEtrawk);

        //讀取時間Bar
        progressBar = new ProgressDialog(this);
        progressBar.setCancelable(true);
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.setMessage("資料載入中");

        switch (getm2rowtype)
        {
            case "加班":
                //看當天加班
                Find_Over_Hour_ByDate_Detail(getm2mannumber,getDate);
                //看當週
                //Find_Over_Hour(getm2mannumber,getm2putEtraYear,getm2putEtrawk);
                //title1.setText("編制");
                mantitl.setText(getm2mannumber + " - " + getm2mantitle); //EX 10015812 - XXX  加班

                break;
            case "請假":
                //執行Find_Leave_Data副程式
                Find_Leave_Data(getm2mannumber,getm2putEtraYear,getm2putEtrawk);
                //title1.setText("編制");
                mantitl.setText(getm2mannumber + " - " + getm2mantitle); //EX 10015812 - XXX  請假

                break;
            case "專案":
                Find_Week_Project();
                mantitl.setText(getm2mantitle);

                break;
        }
//        switch (getm2mantitle)
//        {
//            case "DVT":
//                Find_Week_Project();
//                mantitl.setText(getm2mantitle);
//
//                break;
//            case "MVT":
//                Find_Week_Project();
//                mantitl.setText(getm2mantitle);
//
//                break;
//            case "MP":
//                Find_Week_Project();
//                mantitl.setText(getm2mantitle);
//
//                break;
//        }

    }

    //------------------------------Item---------------------------------
    public class Overhour_Item {

        String Name;//名字 陳帥兆

        String F_SRealTime;//開始時間

        String F_ERealTime;//結束時間

        String F_TotalHour;//加班總時數

        String F_Model;//加班負責專案

        String F_Subject;//加班事由

        String F_Type;//需換條件 補休or加班費

        public Overhour_Item(String Name,String F_SRealTime,String F_ERealTime,String F_TotalHour,String F_Model,String F_Subject,String F_Type)
        {
            this.Name = Name;

            this.F_SRealTime = F_SRealTime;

            this.F_ERealTime = F_ERealTime;

            this.F_TotalHour = F_TotalHour;

            this.F_Model = F_Model;

            this.F_Subject = F_Subject;

            this.F_Type = F_Type;
        }

        public String GetName()
        {
            return this.Name;
        }

        public String GetF_SRealTime()
        {
            return this.F_SRealTime;
        }

        public String GetF_ERealTime()
        {
            return this.F_ERealTime;
        }
        public String GetF_TotalHour()
        {
            return this.F_TotalHour;
        }
        public String GetF_Model()
        {
            return this.F_Model;
        }
        public String GetF_Subject()
        {
            return this.F_Subject;
        }
        public String GetF_Type()
        {
            return this.F_Type;
        }

    }

    public class LeaveData_Item {

        String F_Owner;//名字 陳帥兆

        String F_S_Date;//開始時間

        String F_E_Date;//結束時間

        String F_TotalHour;//休假總時數

        String F_LeaveType;//假別

        String F_Desc;//放假事由


        public LeaveData_Item(String F_Owner,String F_S_Date,String F_E_Date,String F_TotalHour,String F_LeaveType,String F_Desc)
        {
            this.F_Owner = F_Owner;

            this.F_S_Date = F_S_Date;

            this.F_E_Date = F_E_Date;

            this.F_TotalHour = F_TotalHour;

            this.F_LeaveType = F_LeaveType;

            this.F_Desc = F_Desc;

        }

        public String GetF_Owner()
        {
            return this.F_Owner;
        }

        public String GetF_S_Date()
        {
            return this.F_S_Date;
        }

        public String GetF_E_Date()
        {
            return this.F_E_Date;
        }
        public String GetF_TotalHour()
        {
            return this.F_TotalHour;
        }
        public String GetF_LeaveType()
        {
            return this.F_LeaveType;
        }
        public String GetF_Desc()
        {
            return this.F_Desc;
        }
    }

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

    //------------------------------Adapter---------------------------------

    public class OverhourAdapter extends BaseAdapter {

        private LayoutInflater mLayInf;

        private List<Overhour_Item> Overhour_Item_List;

        private Context ProjectContext;


        public OverhourAdapter(Context context,  List<Overhour_Item> Overhour_Item_List)
        {
            mLayInf = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            ProjectContext = context;


            this.Overhour_Item_List = Overhour_Item_List;

        }
        @Override
        public int getCount() {
            return Overhour_Item_List.size();
        }

        @Override
        public Object getItem(int position) {
            return Overhour_Item_List.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View v = new View(ProjectContext);

            v = mLayInf.inflate(R.layout.msibook_dqaweekly_adapter3_overhour, parent, false);

            TextView tvtimetitle = (TextView) v.findViewById(R.id.tvtimetitle);// 時間 :
            TextView tvtimestart = (TextView) v.findViewById(R.id.tvtimestart);
            TextView tvtimeend = (TextView) v.findViewById(R.id.tvtimeend);
            TextView tvtotaltimetitle = (TextView) v.findViewById(R.id.tvtotaltimetitle);// 時數 :
            TextView tvtotaltime = (TextView) v.findViewById(R.id.tvtotaltime);
            TextView tvprojecttitle = (TextView) v.findViewById(R.id.tvprojecttitle);// 時數 :
            TextView tvprojectmessage = (TextView) v.findViewById(R.id.tvprojecmessage);
            TextView tvmessagetitle = (TextView) v.findViewById(R.id.tvmessagetitle);// 事由 :
            TextView tvmessage = (TextView) v.findViewById(R.id.tvmessage);

            tvtimestart.setText(Overhour_Item_List.get(position).GetF_SRealTime());//開始時間

            tvtimeend.setText(Overhour_Item_List.get(position).GetF_ERealTime());//結束時間

            tvprojectmessage.setText(Overhour_Item_List.get(position).GetF_Model());

            tvtotaltime.setText(Overhour_Item_List.get(position).GetF_TotalHour());//

            tvmessage.setText(Overhour_Item_List.get(position).GetF_Subject());//

            Log.w("OverhourAdapter","test");

            return v;
        }

    }

    public class LeaveDataAdapter extends BaseAdapter {

        private LayoutInflater mLayInf;

        private List<LeaveData_Item> LeaveData_Item_List;

        private Context ProjectContext;


        public LeaveDataAdapter(Context context,  List<LeaveData_Item> LeaveData_Item_List)
        {
            mLayInf = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            ProjectContext = context;


            this.LeaveData_Item_List = LeaveData_Item_List;

        }
        @Override
        public int getCount() {
            return LeaveData_Item_List.size();
        }

        @Override
        public Object getItem(int position) {
            return LeaveData_Item_List.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View v = new View(ProjectContext);

            v = mLayInf.inflate(R.layout.msibook_dqaweekly_adapter3_overhour, parent, false);

            TextView tvtimetitle = (TextView) v.findViewById(R.id.tvtimetitle);// 時間 :
            TextView tvtimestart = (TextView) v.findViewById(R.id.tvtimestart);
            TextView tvtimeend = (TextView) v.findViewById(R.id.tvtimeend);
            TextView tvtotaltimetitle = (TextView) v.findViewById(R.id.tvtotaltimetitle);// 時數 :
            TextView tvtotaltime = (TextView) v.findViewById(R.id.tvtotaltime);
            TextView tvprojecttitle = (TextView) v.findViewById(R.id.tvprojecttitle);// 時數 :
            TextView tvprojectmessage = (TextView) v.findViewById(R.id.tvprojecmessage);
            TextView tvmessagetitle = (TextView) v.findViewById(R.id.tvmessagetitle);// 事由 :
            TextView tvmessage = (TextView) v.findViewById(R.id.tvmessage);

            tvtimestart.setText(LeaveData_Item_List.get(position).GetF_S_Date());//開始時間

            tvtimeend.setText(LeaveData_Item_List.get(position).GetF_E_Date());//結束時間

            tvtotaltime.setText(LeaveData_Item_List.get(position).GetF_TotalHour());//

            tvprojecttitle.setVisibility(View.GONE);
            tvprojectmessage.setVisibility(View.GONE);

            tvmessagetitle.setText("假別 : ");
            tvmessage.setText(LeaveData_Item_List.get(position).GetF_LeaveType());//

            Log.w("LeaveDataAdapter","test");

            return v;
        }

    }

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

            v = mLayInf.inflate(R.layout.msibook_dqaweekly_adapter4_project_item, parent, false);

            TextView textView1 = (TextView) v.findViewById(R.id.textView1);
            TextView textView2 = (TextView) v.findViewById(R.id.textView2);
            Button button1 = (Button) v.findViewById(R.id.button1);

            textView1.setText(Week_Project_Item_list.get(position).GetF_Map());
            textView2.setText(Week_Project_Item_list.get(position).GetCnt());

            Log.w("WeekProjectAdapter","test");

            return v;
        }

    }



}
