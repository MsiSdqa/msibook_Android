package dqa.com.msibook;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Switch;
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

public class msibook_dqaweekly_project_activity_msr_data_msr_detial extends AppCompatActivity {

    private Context mContext;
    private RecyclerView mRecycleView;

    private ProjectActivity_msr_detial_adapter adapter;
    private ProjectActivity_msr_detial_adapter_Other adapter_other;

    private List<ProjectActivity_msr_detial_Item> msr_detial_Item = new ArrayList<ProjectActivity_msr_detial_Item>();
    private List<ProjectActivity_msr_detial_Item_Other> msr_detial_Item_Other = new ArrayList<ProjectActivity_msr_detial_Item_Other>();

    private ProgressDialog progressBar;

    private TextView textView_BU_title;
    private TextView textView_Lob;

    private String getBU;
    private String getPeople;

    private Double BU_For_People;
    //private List<String> ArrayBU_Type = new ArrayList<String>();

    private TextView line;
    private LinearLayout line2;
    private LinearLayout line_other;
    private LinearLayout line_other_ks;

    private TextView np_7850;//非專案
    private Double np_7850_total;
    private TextView np_7860;
    private Double np_7860_total;
    private TextView np_7870;
    private Double np_7870_total;
    private TextView np_7880;
    private Double np_7880_total;

    //百分比
    private Double precent_7850;
    private Double precent_7860;
    private Double precent_7870;
    private Double precent_7880;


    private TextView nw_7850;//未填寫
    private Double nw_7850_total;
    private TextView nw_7860;
    private Double nw_7860_total;
    private TextView nw_7870;
    private Double nw_7870_total;
    private TextView nw_7880;
    private Double nw_7880_total;

    private TextView leave_7850;//請假
    private Double leave_7850_total;
    private TextView leave_7860;
    private Double leave_7860_total;
    private TextView leave_7870;
    private Double leave_7870_total;
    private TextView leave_7880;
    private Double leave_7880_total;
    private Double leave_total;

    private RecyclerView Non_Project_ForKS_Recycleview;
    private RecyclerView Non_Write_ForKS_Recycleview;

    private Non_Project_ForKS_Recycleview_adapter mNon_Project_ForKS_Recycleadapter;
    private List<Non_Project_ForKS_Recycleview_Item> non_project_forks_Recycleview_Item = new ArrayList<Non_Project_ForKS_Recycleview_Item>();

    private Non_Wirte_ForKS_Recycleview_adapter mNon_Wirte_ForKS_Recycleadapter;
    private List<Non_Write_ForKS_Recycleview_Item> non_write_forks_recycleview_Item = new ArrayList<Non_Write_ForKS_Recycleview_Item>();

    private List<String> ArrayT_Dept_Name = new ArrayList<String>();
    private List<String> ArrayK_Dept_Name = new ArrayList<String>();
    private List<String> ArrayS_Dept_Name = new ArrayList<String>();

    private Double Non_pro_total;
    private Double Non_write_total;
    private TextView textView_non_pro_total;
    private TextView textView_non_write_total;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msibook_dqaweekly_project_msr_data_msr_detial);

        //讀取時間Bar
        progressBar = new ProgressDialog(this);
        progressBar.setCancelable(false);
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.setMessage("資料載入中");

        mContext = msibook_dqaweekly_project_activity_msr_data_msr_detial.this;

        mRecycleView = (RecyclerView) findViewById(R.id.msr_detial_Recycleview);

        Non_Project_ForKS_Recycleview = (RecyclerView) findViewById(R.id.recycle_non_project);
        Non_Write_ForKS_Recycleview = (RecyclerView) findViewById(R.id.recycle_non_write);

        textView_Lob = (TextView) findViewById(R.id.textView_Lob);

        textView_non_pro_total = (TextView) findViewById(R.id.textView_non_pro_total);
        textView_non_write_total = (TextView) findViewById(R.id.textView_non_write_total);

        line = (TextView) findViewById(R.id.line);
        line2 = (LinearLayout) findViewById(R.id.line2);
        line_other = (LinearLayout) findViewById(R.id.line_other);
        line_other_ks = (LinearLayout) findViewById(R.id.line_other_ks);

        np_7850 = (TextView) findViewById(R.id.np_7850);
        np_7860 = (TextView) findViewById(R.id.np_7860);
        np_7870= (TextView) findViewById(R.id.np_7870);
        np_7880= (TextView) findViewById(R.id.np_7880);

        nw_7850= (TextView) findViewById(R.id.nw_7850);
        nw_7860= (TextView) findViewById(R.id.nw_7860);
        nw_7870= (TextView) findViewById(R.id.nw_7870);
        nw_7880= (TextView) findViewById(R.id.nw_7880);

        leave_7850= (TextView) findViewById(R.id.leave_7850);
        leave_7860= (TextView) findViewById(R.id.leave_7860);
        leave_7870= (TextView) findViewById(R.id.leave_7870);
        leave_7880= (TextView) findViewById(R.id.leave_7880);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecycleView.setLayoutManager(layoutManager);

        LinearLayoutManager layoutManager2 = new LinearLayoutManager(this);
        layoutManager2.setOrientation(LinearLayoutManager.HORIZONTAL);
        Non_Project_ForKS_Recycleview.setLayoutManager(layoutManager2);

        LinearLayoutManager layoutManager3 = new LinearLayoutManager(this);
        layoutManager3.setOrientation(LinearLayoutManager.HORIZONTAL);
        Non_Write_ForKS_Recycleview.setLayoutManager(layoutManager3);

        textView_BU_title = (TextView) findViewById(R.id.textView_BU_title);

        final LayoutInflater factory = getLayoutInflater();
        final View textEntryView = factory.inflate(R.layout.activity_msibook_dqaweekly_project_msr_data_msr_detial, null);

        String getEtraWeek = getIntent().getStringExtra("Week");//抓第一頁選的部門代號
        String getEtraYear = getIntent().getStringExtra("Year");//抓第一頁選的部門代號
        String getRegionID = getIntent().getStringExtra("RegionID");//抓第一頁選的部門代號
        getPeople = getIntent().getStringExtra("People");//抓第一頁選的部門代號
        getBU = getIntent().getStringExtra("BU");//抓第一頁選的部門代號
        Log.w("抓到人數",getPeople);
        Log.w("以判斷點的是哪一個Type",getBU);

        textView_BU_title.setText(getBU);

        precent_7850 = 0.0;
        precent_7860 = 0.0;
        precent_7870 = 0.0;
        precent_7880 = 0.0;

        if(getBU.contains("Other")){
            textView_Lob.setText("項目");
            textView_Lob.setVisibility(View.INVISIBLE);
            line.setVisibility(View.GONE);
            //暫時先讓台北區另外去跑 獨立的 請假 跟 非專案
            if(getRegionID.contains("1")){
                line2.setVisibility(View.GONE);
                line_other.setVisibility(View.VISIBLE);//台北Other
                line_other_ks.setVisibility(View.GONE);//  KS Other
                //Find_BU_WeeklyReport_Other(getEtraYear,getEtraWeek,getRegionID); //上層看全部
                Find_BU_WeeklyReport_Other_Leave(getEtraYear,getEtraWeek,getRegionID); // 下層看細項
                //Find_BU_WeeklyReport_Other_Non_Project(getEtraYear,getEtraWeek,getRegionID);
            }else{
                //選Other 的 昆、寶安
                //textView_Lob.setText("項目");
                textView_Lob.setVisibility(View.INVISIBLE);
                line.setVisibility(View.GONE);
                line2.setVisibility(View.GONE);
                line_other_ks.setVisibility(View.VISIBLE);  //KS Other
                line_other.setVisibility(View.GONE);//台北Other
                //Find_BU_WeeklyReport_Other(getEtraYear,getEtraWeek,getRegionID);
                Find_BU_WeeklyReport_Other_Non_Project_ForKS(getEtraYear,getEtraWeek,getRegionID);
            }
            Log.w("進入Other","");
        }else{
            line.setVisibility(View.VISIBLE);
            line2.setVisibility(View.VISIBLE);
            line_other.setVisibility(View.GONE); //台北Other
            line_other_ks.setVisibility(View.GONE); //KS Other
            textView_Lob.setText("Lob");
            textView_Lob.setVisibility(View.VISIBLE);
            Log.w("沒梅梅進入Other","");
            Find_BU_PM_Rate(getEtraWeek,getEtraYear,getRegionID,"All",getBU);
        }



    }

    public void Find_BU_WeeklyReport_Other_Leave(final String Year, final String Week,final String RegionID) {

        //顯示  讀取等待時間Bar
        progressBar.show();

        msr_detial_Item_Other.clear();

        leave_7850_total = 0.0;
        leave_7860_total = 0.0;
        leave_7870_total = 0.0;
        leave_7880_total = 0.0;

        RequestQueue mQueue = Volley.newRequestQueue(this);

        getString("http://wtsc.msi.com.tw/IMS/Weekly_Report.asmx/Find_BU_WeeklyReport_Other_Leave?Year=" + Year + "&Week=" + Week + "&RegionID=" + RegionID, mQueue, new GetServiceData.VolleyCallback() {
            @Override
            public void onSuccess(JSONObject result) {

                try {
                    JSONArray UserArray = new JSONArray(result.getString("Key"));
                    if (UserArray.length() > 0)
                    {

                        for (int i = 0; i < UserArray.length(); i++) {

                            JSONObject IssueData = UserArray.getJSONObject(i);

                            String F_Orgid = String.valueOf(IssueData.getString("F_Orgid"));

                            String DeptID = String.valueOf(IssueData.getInt("DeptID"));

                            String Leave = String.valueOf(IssueData.getString("請假"));

                            if(F_Orgid.contains("785")){
                                leave_7850_total += Double.valueOf(Leave);
                            }
                            if(F_Orgid.contains("786")){
                                leave_7860_total += Double.valueOf(Leave);
                            }
                            if(F_Orgid.contains("787")){
                                leave_7870_total += Double.valueOf(Leave);
                            }
                            if(F_Orgid.contains("788")){
                                leave_7880_total += Double.valueOf(Leave);
                            }
                        }
                        leave_7850.setText(String.valueOf(leave_7850_total));
                        leave_7860.setText(String.valueOf(leave_7860_total));
                        leave_7870.setText(String.valueOf(leave_7870_total));
                        leave_7880.setText(String.valueOf(leave_7880_total));


                    }

                    else
                    {
                    }

                    Find_BU_WeeklyReport_Other_Non_Project(Year,Week,RegionID);

                } catch (JSONException ex) {
                    Log.w("稼動綠測試Error","");
                }
            }
        });


    }

    public void Find_BU_WeeklyReport_Other_Non_Project(final String Year, final String Week,final String RegionID) {

        //顯示  讀取等待時間Bar
        //progressBar.show();

        msr_detial_Item_Other.clear();

        Non_pro_total =0.0;

        np_7850_total = 0.0;
        np_7860_total = 0.0;
        np_7870_total = 0.0;
        np_7880_total = 0.0;

//                        np_7850.setText(String.valueOf("30.56"));
//                        np_7860.setText(String.valueOf("1"));
//                        np_7870.setText(String.valueOf("1"));
//                        np_7880.setText(String.valueOf("38"));

        RequestQueue mQueue = Volley.newRequestQueue(this);

        getString("http://wtsc.msi.com.tw/IMS/Weekly_Report.asmx/Find_BU_WeeklyReport_Other_Non_Project?Year=" + Year + "&Week=" + Week + "&RegionID=" + RegionID, mQueue, new GetServiceData.VolleyCallback() {
            @Override
            public void onSuccess(JSONObject result) {

                try {
                    JSONArray UserArray = new JSONArray(result.getString("Key"));
                    if (UserArray.length() > 0)
                    {

                        for (int i = 0; i < UserArray.length(); i++) {

                            JSONObject IssueData = UserArray.getJSONObject(i);

                            String F_Orgid = String.valueOf(IssueData.getString("Dept"));

                            //String DeptID = String.valueOf(IssueData.getInt("DeptID"));

                            String Leave = String.valueOf(IssueData.getInt("非專案"));

                            if(F_Orgid.contains("785")){
                                np_7850_total += Double.valueOf(Leave);
                            }
                            if(F_Orgid.contains("786")){
                                np_7860_total += Double.valueOf(Leave);
                            }
                            if(F_Orgid.contains("787")){
                                np_7870_total += Double.valueOf(Leave);
                            }
                            if(F_Orgid.contains("788")){
                                np_7880_total += Double.valueOf(Leave);
                            }

                            Non_pro_total += Double.valueOf(Leave);

                        }
                        Log.w("加總是",String.valueOf(Non_pro_total)+"當分母");
                        precent_7850 = np_7850_total/Non_pro_total;
                        precent_7860 = np_7860_total/Non_pro_total;
                        precent_7870 = np_7870_total/Non_pro_total;
                        precent_7880 = np_7880_total/Non_pro_total;
                        Log.w("7850的百分比為",String.valueOf(precent_7850));
                        Log.w("7860的百分比為",String.valueOf(precent_7860));
                        Log.w("7870的百分比為",String.valueOf(precent_7870));
                        Log.w("7880的百分比為",String.valueOf(precent_7880));

                    }
                    else
                    {
                    }
                    Find_BU_WeeklyReport_Other_Non_Wirte(Year,Week,RegionID);

                } catch (JSONException ex) {
                    Log.w("稼動綠測試Error","");
                }

                //progressBar.dismiss();
            }
        });


    }

    public void Find_BU_WeeklyReport_Other_Non_Wirte(final String Year, final String Week,final String RegionID) {

        //顯示  讀取等待時間Bar
        //progressBar.show();

        msr_detial_Item_Other.clear();

        Non_write_total = 0.0;

        nw_7850_total = 0.0;
        nw_7860_total = 0.0;
        nw_7870_total = 0.0;
        nw_7880_total = 0.0;

//        nw_7850.setText(String.valueOf("37.84"));
//        nw_7860.setText(String.valueOf("1.19"));
//        nw_7870.setText(String.valueOf("1.19"));
//        nw_7880.setText(String.valueOf("30.34"));
//
//        textView_non_write_total.setText(String.valueOf("70.56"));

        RequestQueue mQueue = Volley.newRequestQueue(this);

        getString("http://wtsc.msi.com.tw/IMS/Weekly_Report.asmx/Find_BU_WeeklyReport_Other_Non_Wirte?Year=" + Year + "&Week=" + Week + "&RegionID=" + RegionID, mQueue, new GetServiceData.VolleyCallback() {
            @Override
            public void onSuccess(JSONObject result) {

                try {
                    JSONArray UserArray = new JSONArray(result.getString("Key"));
                    if (UserArray.length() > 0)
                    {

                        for (int i = 0; i < UserArray.length(); i++) {

                            JSONObject IssueData = UserArray.getJSONObject(i);

                            String F_Orgid = String.valueOf(IssueData.getString("Dept"));

                            String NonWrite_Total = String.valueOf(IssueData.getInt("未填寫總人數"));

                            if(F_Orgid.contains("785")){
                                nw_7850_total += Double.valueOf(NonWrite_Total);
                            }

                            if(F_Orgid.contains("786")){
                                nw_7860_total += Double.valueOf(NonWrite_Total);
                            }

                            if(F_Orgid.contains("787")){
                                nw_7870_total += Double.valueOf(NonWrite_Total);
                            }

                            if(F_Orgid.contains("788")){
                                nw_7880_total += Double.valueOf(NonWrite_Total);
                            }

                            Non_write_total += Double.valueOf(NonWrite_Total);
                        }
                        nw_7850.setText(String.valueOf(nw_7850_total));
                        nw_7860.setText(String.valueOf(nw_7860_total));
                        nw_7870.setText(String.valueOf(nw_7870_total));
                        nw_7880.setText(String.valueOf(nw_7880_total));

                        textView_non_write_total.setText(String.valueOf(Non_write_total));

                        //非專案最後取值
                        Double Precent_7850 = (Double.valueOf(getPeople)-Non_write_total)*precent_7850;
                        Double Precent_7860 = (Double.valueOf(getPeople)-Non_write_total)*precent_7860;
                        Double Precent_7870 = (Double.valueOf(getPeople)-Non_write_total)*precent_7870;
                        Double Precent_7880 = (Double.valueOf(getPeople)-Non_write_total)*precent_7880;

                        np_7850.setText(String.format("%.2f",Precent_7850));
                        np_7860.setText(String.format("%.2f",Precent_7860));
                        np_7870.setText(String.format("%.2f",Precent_7870));
                        np_7880.setText(String.format("%.2f",Precent_7880));
                        //nw_7850.setText();
                        textView_non_pro_total.setText(String.format("%.2f",Precent_7850+Precent_7860+Precent_7870+Precent_7880));
                    }
                    else
                    {
                    }


                } catch (JSONException ex) {
                    Log.w("稼動綠測試Error","");
                }

                progressBar.dismiss();
            }
        });
    }

    public void Find_BU_PM_Rate(final String Week, final String Year, final String RegionID, String Dept, String BU) {

        //ArrayBU_Type.clear();
        BU_For_People = 0.0;

        RequestQueue mQueue = Volley.newRequestQueue(this);

        getString("http://wtsc.msi.com.tw/IMS/Weekly_Report.asmx/Find_BU_PM_Rate?Week=" + Week + "&Year=" + Year + "&RegionID=" + RegionID + "&Dept=" + Dept, mQueue, new GetServiceData.VolleyCallback() {
            @Override
            public void onSuccess(JSONObject result) {

                try {
                    JSONArray UserArray = new JSONArray(result.getString("Key"));
                    if (UserArray.length() > 0)
                    {
                        for (int i = 0; i < UserArray.length(); i++) {

                            JSONObject IssueData = UserArray.getJSONObject(i);

                            String M_Type = String.valueOf(IssueData.getString("M_Type"));

                            String Cnt = String.valueOf(IssueData.getDouble("Cnt"));

                            String People = String.valueOf(IssueData.getDouble("People"));

                            String WorkHour = String.valueOf(IssueData.getDouble("WorkHour"));

                            if(M_Type.contains(getBU)){
                                BU_For_People = Double.valueOf(People);
                                Log.w("以判斷點的是哪一個Type",getBU + String.valueOf(BU_For_People));
                            }
                        }
                    }
                    else
                    {

                    }
                    Find_MSR_Data_Detail(Week,Year,RegionID,getBU,"All");

                } catch (JSONException ex) {
                    Log.w("稼動綠測試Error","");
                }
            }
        });
    }

    public void Find_BU_WeeklyReport_Other(final String Year, final String Week,final String RegionID) { //舊 Other  昆、寶地區

        msr_detial_Item_Other.clear();

        RequestQueue mQueue = Volley.newRequestQueue(this);

        getString("http://wtsc.msi.com.tw/IMS/Weekly_Report.asmx/Find_BU_WeeklyReport_Other?Year=" + Year + "&Week=" + Week + "&RegionID=" + RegionID, mQueue, new GetServiceData.VolleyCallback() {
            @Override
            public void onSuccess(JSONObject result) {

                try {
                    JSONArray UserArray = new JSONArray(result.getString("Key"));
                    if (UserArray.length() > 0)
                    {

                        for (int i = 0; i < UserArray.length(); i++) {

                            JSONObject IssueData = UserArray.getJSONObject(i);

                            String Leave = String.valueOf(IssueData.getDouble("請假"));

                            String NoWrite = String.valueOf(IssueData.getDouble("未填寫"));

                            String NotProject = String.format("%.2f", (Double.valueOf(getPeople)-Double.valueOf(Leave)-Double.valueOf(NoWrite)));

                            msr_detial_Item_Other.add(0,new ProjectActivity_msr_detial_Item_Other(Leave,NoWrite,NotProject));
                            msr_detial_Item_Other.add(1,new ProjectActivity_msr_detial_Item_Other(Leave,NoWrite,NotProject));
                            msr_detial_Item_Other.add(2,new ProjectActivity_msr_detial_Item_Other(Leave,NoWrite,NotProject));
                        }

                        adapter_other = new ProjectActivity_msr_detial_adapter_Other(mContext,msr_detial_Item_Other);
                        mRecycleView.setAdapter(adapter_other);

                    }
                    else
                    {
                    }
                    Find_MSR_Data_Detail(Week,Year,RegionID,getBU,"All");

                } catch (JSONException ex) {
                    Log.w("稼動綠測試Error","");
                }
            }
        });


    }

    public void Find_BU_WeeklyReport_Other_Non_Project_ForKS(final String Year, final String Week,final String RegionID) { // 昆山 寶安  非專案  新版本

        //顯示  讀取等待時間Bar
        progressBar.show();

        non_project_forks_Recycleview_Item.clear();

        RequestQueue mQueue = Volley.newRequestQueue(this);

        getString("http://wtsc.msi.com.tw/IMS/Weekly_Report.asmx/Find_BU_WeeklyReport_Other_Non_Project?Year=" + Year + "&Week=" + Week + "&RegionID=" + RegionID, mQueue, new GetServiceData.VolleyCallback() {
            @Override
            public void onSuccess(JSONObject result) {

                try {
                    JSONArray UserArray = new JSONArray(result.getString("Key"));
                    if (UserArray.length() > 0)
                    {

                            for (int i = 0; i < UserArray.length(); i++) {

                                JSONObject IssueData = UserArray.getJSONObject(i);

                                String F_Orgid = String.valueOf(IssueData.getString("Dept"));

                                //String DeptID = String.valueOf(IssueData.getInt("DeptID"));

                                //String Dept = String.valueOf(IssueData.getString("Dept"));

                                String Non_Project = String.valueOf(IssueData.getInt("非專案"));

                                non_project_forks_Recycleview_Item.add(i,new Non_Project_ForKS_Recycleview_Item(F_Orgid,Non_Project,RegionID));
                            }

                        mNon_Project_ForKS_Recycleadapter = new Non_Project_ForKS_Recycleview_adapter(mContext,non_project_forks_Recycleview_Item);
                        Non_Project_ForKS_Recycleview.setAdapter(mNon_Project_ForKS_Recycleadapter);

                    }
                    else
                    {

                    }
                    Find_BU_WeeklyReport_Other_Non_Wirte_ForKS(Year,Week,RegionID);

                } catch (JSONException ex) {
                    Log.w("稼動綠測試Error","");
                }

                //progressBar.dismiss();
            }
        });


    }

    public void Find_BU_WeeklyReport_Other_Non_Wirte_ForKS(final String Year, final String Week,final String RegionID) { //昆山 寶安 未填寫

        //顯示  讀取等待時間Bar
        //progressBar.show();

        msr_detial_Item_Other.clear();

        RequestQueue mQueue = Volley.newRequestQueue(this);

        getString("http://wtsc.msi.com.tw/IMS/Weekly_Report.asmx/Find_BU_WeeklyReport_Other_Non_Wirte?Year=" + Year + "&Week=" + Week + "&RegionID=" + RegionID, mQueue, new GetServiceData.VolleyCallback() {
            @Override
            public void onSuccess(JSONObject result) {

                try {
                    JSONArray UserArray = new JSONArray(result.getString("Key"));
                    if (UserArray.length() > 0)
                    {

                        for (int i = 0; i < UserArray.length(); i++) {

                            JSONObject IssueData = UserArray.getJSONObject(i);

                            String Dept = String.valueOf(IssueData.getString("Dept"));

                            String NonWrite_Total = String.valueOf(IssueData.getInt("未填寫總人數"));

                            non_write_forks_recycleview_Item.add(i,new Non_Write_ForKS_Recycleview_Item(Dept,NonWrite_Total));
                        }

                        mNon_Wirte_ForKS_Recycleadapter = new Non_Wirte_ForKS_Recycleview_adapter(mContext,non_write_forks_recycleview_Item);
                        Non_Write_ForKS_Recycleview.setAdapter(mNon_Wirte_ForKS_Recycleadapter);
                    }
                    else
                    {
                    }


                } catch (JSONException ex) {
                    Log.w("稼動綠測試Error","");
                }

                progressBar.dismiss();
            }
        });


    }

    public void Find_MSR_Data_Detail(String Week,String Year, String RegionID,String BU,String Dept) {


        //顯示  讀取等待時間Bar
        progressBar.show();

        msr_detial_Item.clear();

        RequestQueue mQueue = Volley.newRequestQueue(this);

        getString("http://wtsc.msi.com.tw/IMS/Weekly_Report.asmx/Find_MSR_Data_Detail?Week=" + Week + "&Year=" + Year + "&RegionID=" + RegionID + "&BU=" + BU  + "&Dept=" + Dept, mQueue, new GetServiceData.VolleyCallback() {
            @Override
            public void onSuccess(JSONObject result) {

                try {
                    Log.w("稼動綠測試","");
                    JSONArray UserArray = new JSONArray(result.getString("Key"));
                    if (UserArray.length() > 0)
                    {

                        for (int i = 0; i < UserArray.length(); i++) {

                            JSONObject IssueData = UserArray.getJSONObject(i);

                            //String TotalPeople = String.valueOf(IssueData.getInt("TotalPeople"));

                            String BU = String.valueOf(IssueData.getString("BU"));

                            //String Project_Rate = String.valueOf(IssueData.getDouble("Project_Rate")*100);  原本
                            String Project_Rate = String.format("%.2f",IssueData.getDouble("Project_Rate")*BU_For_People); //轉人數佔比
                            String IntPR = String.valueOf((int)Math.ceil(Double.valueOf(Project_Rate)));

                            String MSR_Rate = String.valueOf(IssueData.getDouble("MSR_Rate"));

                            String Column1 = String.valueOf(IssueData.getInt("Column1"));

                            msr_detial_Item.add(i,new ProjectActivity_msr_detial_Item(BU,Project_Rate));
                        }

//                        mListView = (ListView) findViewById(R.id.utilization_dpt_detial_listview);
//                        adapter = new ProjectActivity_utilization_listview_detial_adapter(mContext,utilization_list_detial_Item);
//                        //mRecycleView.setAdapter(adapter);
//                        mListView.setAdapter(adapter);

//                        mRecycleView = (RecyclerView) findViewById(R.id.utilization_dpt_detial_Recycleview);
                        adapter = new ProjectActivity_msr_detial_adapter(mContext,msr_detial_Item);
                        mRecycleView.setAdapter(adapter);

                    }
                    else
                    {

                    }


                } catch (JSONException ex) {
                    Log.w("稼動綠測試Error","");
                }
                //關閉-讀取等待時間Bar
                progressBar.dismiss();
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

    //-------------------Item-------------------
    public class ProjectActivity_msr_detial_Item {

        private String BU;
        private String Project_Rate;

        public ProjectActivity_msr_detial_Item(String BU, String Project_Rate) {

            this.BU = BU;
            this.Project_Rate = Project_Rate;

        }

        public String getBU() {
            return BU;
        }

        public String getProject_Rate() {
            return Project_Rate;
        }
    }

    public class ProjectActivity_msr_detial_Item_Other {

        private String Leave;
        private String NoWrite;
        private String NotProject;


        public ProjectActivity_msr_detial_Item_Other(String Leave, String NoWrite, String NotProject) {

            this.Leave = Leave;
            this.NoWrite = NoWrite;
            this.NotProject = NotProject;

        }

        public String getLeave() {
            return Leave;
        }

        public String getNoWrite() {
            return NoWrite;
        }

        public String getNotProject() {
            return NotProject;
        }
    }

    public class Non_Project_ForKS_Recycleview_Item {

        private String Dept;
        private String F_Orgid;
        private String Non_Project;
        private String RegionID;

        public Non_Project_ForKS_Recycleview_Item(String F_Orgid, String Non_Project,String RegionID) {

            this.Dept = Dept;
            this.F_Orgid = F_Orgid;
            this.Non_Project = Non_Project;
            this.RegionID = RegionID;

        }
        public String getDept() {
            return Dept;
        }

        public String getF_Orgid() {
            return F_Orgid;
        }

        public String getNon_Project() {
            return Non_Project;
        }

        public String getRegionID() {
            return RegionID;
        }
    }

    public class Non_Write_ForKS_Recycleview_Item {

        private String Dept;
        private String Non_Write;

        public Non_Write_ForKS_Recycleview_Item(String Dept, String Non_Write) {

            this.Dept = Dept;
            this.Non_Write = Non_Write;

        }

        public String getDept() {
            return Dept;
        }

        public String getNon_Write() {
            return Non_Write;
        }
    }



    //-----------------Adapter-----------------
    public class ProjectActivity_msr_detial_adapter extends RecyclerView.Adapter<ProjectActivity_msr_detial_adapter.ViewHolder> {

        private Context mContext;
        private LayoutInflater mInflater;
        private List<ProjectActivity_msr_detial_Item> msr_detial_list;

        TextView textView_BU;
        TextView textView_project_rate;

        public ProjectActivity_msr_detial_adapter(Context context, List<ProjectActivity_msr_detial_Item> datas) {

            mInflater = LayoutInflater.from(context);
            mContext = context;
            msr_detial_list = datas;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {


            public ViewHolder(View LayoutView) {
                super(LayoutView);

                textView_BU = (TextView)LayoutView.findViewById(R.id.textView_BU);
                textView_project_rate = (TextView)LayoutView.findViewById(R.id.textView_project_rate);

            }


        }

        @Override
        public ProjectActivity_msr_detial_adapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

            View view = mInflater.inflate(R.layout.msibook_dqaweekly_projectactivity_page2_msr_detial_layout,
                    viewGroup, false);

            ProjectActivity_msr_detial_adapter.ViewHolder viewHolder = new ProjectActivity_msr_detial_adapter.ViewHolder(view);

            return viewHolder;
        }

        @Override
        public void onBindViewHolder(ProjectActivity_msr_detial_adapter.ViewHolder holder, int position) {

            ProjectActivity_msr_detial_Item movie = msr_detial_list.get(position);

            textView_BU.setText(movie.getBU());
            textView_project_rate.setText(movie.getProject_Rate());

        }

        @Override
        public int getItemCount() {
            return msr_detial_list.size();
        }
    }


    public class ProjectActivity_msr_detial_adapter_Other extends RecyclerView.Adapter<ProjectActivity_msr_detial_adapter_Other.ViewHolder> {

        private Context mContext;
        private LayoutInflater mInflater;
        private List<ProjectActivity_msr_detial_Item_Other> msr_detial_list;

        TextView textView_BU;
        TextView textView_project_rate;

        public ProjectActivity_msr_detial_adapter_Other(Context context, List<ProjectActivity_msr_detial_Item_Other> datas) {

            mInflater = LayoutInflater.from(context);
            mContext = context;
            msr_detial_list = datas;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            public ViewHolder(View LayoutView) {
                super(LayoutView);

                textView_BU = (TextView)LayoutView.findViewById(R.id.textView_BU);
                textView_project_rate = (TextView)LayoutView.findViewById(R.id.textView_project_rate);

            }


        }

        @Override
        public ProjectActivity_msr_detial_adapter_Other.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

            View view = mInflater.inflate(R.layout.msibook_dqaweekly_projectactivity_page2_msr_detial_layout,
                    viewGroup, false);

            ProjectActivity_msr_detial_adapter_Other.ViewHolder viewHolder = new ProjectActivity_msr_detial_adapter_Other.ViewHolder(view);

            return viewHolder;
        }

        @Override
        public void onBindViewHolder(ProjectActivity_msr_detial_adapter_Other.ViewHolder holder, int position) {

            ProjectActivity_msr_detial_Item_Other movie = msr_detial_list.get(position);

            switch (position) {
                case 0:
                    textView_BU.setText("非專案");
                    textView_project_rate.setText(movie.getNotProject());
                    break;
                case 1:
                    textView_BU.setText("未填寫");
                    textView_project_rate.setText(movie.getNoWrite());
                    break;
                case 2:
                    textView_BU.setText("請假");
                    textView_project_rate.setText(movie.getLeave());
                    break;
            }
        }
        @Override
        public int getItemCount() {
            return msr_detial_list.size();
        }
    }

    // 非專案 昆 寶使用
    public class Non_Project_ForKS_Recycleview_adapter   extends RecyclerView.Adapter<Non_Project_ForKS_Recycleview_adapter.ViewHolder> {

        private Context mContext;
        private LayoutInflater mInflater;
        private List<Non_Project_ForKS_Recycleview_Item> Non_Project_ForKS_list;

        TextView textView_F_Orgid;
        TextView textView_Non_Project;

        public Non_Project_ForKS_Recycleview_adapter(Context context, List<Non_Project_ForKS_Recycleview_Item> datas) {

            mInflater = LayoutInflater.from(context);
            mContext = context;
            Non_Project_ForKS_list = datas;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            public ViewHolder(View LayoutView) {
                super(LayoutView);

                textView_F_Orgid = (TextView)LayoutView.findViewById(R.id.textView_F_Orgid);

                textView_Non_Project = (TextView)LayoutView.findViewById(R.id.textView_Non_Project);
            }
        }

        @Override
        public Non_Project_ForKS_Recycleview_adapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

            View view = mInflater.inflate(R.layout.msibook_dqaweekly_nonproject_forks_layout,
                    viewGroup, false);

            Non_Project_ForKS_Recycleview_adapter.ViewHolder viewHolder = new Non_Project_ForKS_Recycleview_adapter.ViewHolder(view);

            return viewHolder;
        }

        @Override
        public void onBindViewHolder(Non_Project_ForKS_Recycleview_adapter.ViewHolder holder, int position) {
            Non_Project_ForKS_Recycleview_Item movie = Non_Project_ForKS_list.get(position);

            textView_F_Orgid.setText(movie.getF_Orgid());
            textView_Non_Project.setText(movie.getNon_Project());

        }


        @Override
        public int getItemCount() {
            return Non_Project_ForKS_list.size();
        }

    }

    //---------------------------------------------------------------------------------------------------------------------

    // 非專案 昆 寶使用
    public class Non_Wirte_ForKS_Recycleview_adapter   extends RecyclerView.Adapter<Non_Wirte_ForKS_Recycleview_adapter.ViewHolder> {

        private Context mContext;
        private LayoutInflater mInflater;
        private List<Non_Write_ForKS_Recycleview_Item> Non_Write_ForKS_Recycleview_Item_list;

        TextView textView_F_Orgid;
        TextView textView_Non_Project;

        public Non_Wirte_ForKS_Recycleview_adapter(Context context, List<Non_Write_ForKS_Recycleview_Item> datas) {

            mInflater = LayoutInflater.from(context);
            mContext = context;
            Non_Write_ForKS_Recycleview_Item_list = datas;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            public ViewHolder(View LayoutView) {
                super(LayoutView);

                textView_F_Orgid = (TextView)LayoutView.findViewById(R.id.textView_F_Orgid);

                textView_Non_Project = (TextView)LayoutView.findViewById(R.id.textView_Non_Project);
            }
        }

        @Override
        public Non_Wirte_ForKS_Recycleview_adapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

            View view = mInflater.inflate(R.layout.msibook_dqaweekly_nonproject_forks_layout,
                    viewGroup, false);

            Non_Wirte_ForKS_Recycleview_adapter.ViewHolder viewHolder = new Non_Wirte_ForKS_Recycleview_adapter.ViewHolder(view);

            return viewHolder;
        }

        @Override
        public void onBindViewHolder(Non_Wirte_ForKS_Recycleview_adapter.ViewHolder holder, int position) {
            Non_Write_ForKS_Recycleview_Item movie = Non_Write_ForKS_Recycleview_Item_list.get(position);

            textView_F_Orgid.setText(movie.getDept());
            textView_Non_Project.setText(movie.getNon_Write());

        }

        @Override
        public int getItemCount() {
            return Non_Write_ForKS_Recycleview_Item_list.size();
        }

    }


}
