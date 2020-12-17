package dqa.com.msibook;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

public class msibook_dqaweekly_project_activity_lottery_report_detail extends AppCompatActivity {

    private ProgressDialog progressBar;

    private Context mContext;
    private DownloadFile_Data_Adapter mDownloadFile_Data_Adapter;
    private List<DownloadFile_Data_Item> DownloadFile_Data_Item_List = new ArrayList<DownloadFile_Data_Item>();

    private ListView mListView;

    private TextView textView_F_SeqNo;
    private TextView textView_F_Model;
    private TextView textView_F_Subject;

    private TextView textView_F_Author;
    private TextView textView_F_Progress;
    private TextView textView_F_TestResultID;
    private TextView textView_F_WorkHour;
    private TextView textView_F_CostSum;

    private TextView textView_F_Topic;
    private TextView textView_F_Stage;
    private TextView textView_F_WType;
    private TextView textView_F_PeopleSum;
    private TextView textView_F_Ver;

    private TextView textView_F_Result;
    private TextView textView_F_Env;
    private TextView textView_F_Comments;
    private TextView textView_F_Platform;
    private TextView textView_F_EQP;
    private TextView textView_F_Ref;
    private TextView textView_Lob;

    private TextView textView_Pass;
    private TextView textView_Fail;

    private String Set_F_SeqNo;
    private String Set_F_Model;
    private String Set_F_Subject;

    private String Set_F_Author;
    private String Set_F_Progress;
    private String Set_F_TestResultID;
    private String Set_F_WorkHour;
    private String Set_F_CostSum;

    private String Set_F_Topic;
    private String Set_F_Stage;
    private String Set_F_WType;
    private String Set_F_PeopleSum;
    private String Set_F_Ver;

    private String Set_F_Result;
    private String Set_F_Env;
    private String Set_F_Comments;
    private String Set_F_Platform;
    private String Set_F_EQP;
    private String Set_F_Ref;

    private String getWeek;
    private String getYear;
    private String getDeptID;
    private String getKeyin;

    //*********
    private String Set_F_ModelID;
    private String Set_F_SDate;
    private String Set_F_EDate;
    private String Set_F_Common;
    private String Set_F_RegionID;
    private Boolean Set_F_IsAudit;
    private String Set_ModelLink;//專案頁面網址
    private String Set_IMSLink;//IMS網址
    private String Set_RSSLink;
    private String Set_Lob;
    //*********

    private Drawable drawable;
    private Drawable drawable2;
    private Drawable drawable3;

    private String RSS_NO = "";

    private Boolean CheckReport;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub

        if (keyCode == KeyEvent.KEYCODE_BACK) { // 攔截返回鍵
            if(CheckReport==false) {
                new AlertDialog.Builder(msibook_dqaweekly_project_activity_lottery_report_detail.this)
                        .setTitle("注意")
                        .setMessage("報告尚未評分\n確定是否離開")
                        .setPositiveButton("離開",
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        Update_RSS_Random(Set_F_SeqNo, "1", "","0");
                                        finish();
                                    }
                                })
                        .setNegativeButton("給評分",
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        // TODO Auto-generated method stub

                                    }
                                }).show();
            }else{
                finish();
            }
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msibook_dqaweekly_project_lottery_report_detail);

        mContext = this;

        //讀取時間Bar
        progressBar = new ProgressDialog(this);
        progressBar.setCancelable(true);
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.setMessage("資料載入中");

        CheckReport = false;

        textView_F_SeqNo = (TextView) findViewById(R.id.textView_F_SeqNo);
        textView_F_Model = (TextView) findViewById(R.id.textView_F_Model);
        textView_F_Subject = (TextView) findViewById(R.id.textView_F_Subject);

        textView_F_Author = (TextView) findViewById(R.id.textView_F_Author);
        textView_F_Progress = (TextView) findViewById(R.id.textView_F_Progress);
        textView_F_TestResultID = (TextView) findViewById(R.id.textView_F_TestResultID);
        textView_F_WorkHour = (TextView) findViewById(R.id.textView_F_WorkHour);
        textView_F_CostSum = (TextView) findViewById(R.id.textView_F_CostSum);

        textView_F_Topic = (TextView) findViewById(R.id.textView_F_Topic);
        textView_F_Stage = (TextView) findViewById(R.id.textView_F_Stage);
        textView_F_WType = (TextView) findViewById(R.id.textView_F_WType);
        textView_F_PeopleSum = (TextView) findViewById(R.id.textView_F_PeopleSum);
        textView_F_Ver = (TextView) findViewById(R.id.textView_F_Ver);

        textView_F_Result = (TextView) findViewById(R.id.textView_F_Result);
        textView_F_Env = (TextView) findViewById(R.id.textView_F_Env);
        textView_F_Comments = (TextView) findViewById(R.id.textView_F_Comments);
        textView_F_Platform = (TextView) findViewById(R.id.textView_F_Platform);
        textView_F_EQP = (TextView) findViewById(R.id.textView_F_EQP);
        textView_F_Ref = (TextView) findViewById(R.id.textView_F_Ref);
        textView_Lob = (TextView) findViewById(R.id.textView_Lob);

        textView_Pass = (TextView) findViewById(R.id.textView_Pass);
        textView_Fail = (TextView) findViewById(R.id.textView_Fail);
        Resources res = this.getResources();
        drawable = res.getDrawable(R.drawable.dqaweekly_linear_lottery_up);

        Resources linear_history_up = this.getResources();
        drawable2 = linear_history_up.getDrawable(R.drawable.dqaweekly_lottery_report_history_down);

        Resources linear_history_down = this.getResources();
        drawable3 = linear_history_down.getDrawable(R.drawable.dqaweekly_lottery_report_history_up);

        getWeek = getIntent().getStringExtra("Week");//週次
        getYear = getIntent().getStringExtra("Year");//年
        getDeptID = getIntent().getStringExtra("DepID");//部門ID
        Log.w("DeptID",getDeptID);
        getKeyin = getIntent().getStringExtra("Keyin");//部門ID
        Log.w("getKeyin",getKeyin);

        textView_Pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });

        //PASS 點擊
        textView_Pass.setOnTouchListener(new View.OnTouchListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        textView_Pass.setBackgroundColor(Color.parseColor("#60434343"));//按下灰色
                        return true;
                    case MotionEvent.ACTION_UP:
                        textView_Pass.setBackgroundColor(Color.parseColor("#ffffff"));//放開白色

                        if (!TextUtils.isEmpty(RSS_NO)) {

                            Update_RSS_Random(Set_F_SeqNo, "1", "","1");
                            Toast.makeText(msibook_dqaweekly_project_activity_lottery_report_detail.this, "已評分為及格", Toast.LENGTH_SHORT).show();
                            Log.w("Pass","pass");
                            textView_Pass.setTextColor(Color.parseColor("#ffffff"));
                            textView_Pass.setBackgroundColor(Color.parseColor("#3cd45b"));
                            textView_Fail.setTextColor(Color.parseColor("#ed4a47"));
                            textView_Fail.setBackgroundColor(Color.parseColor("#ffffff"));

                            CheckReport=true;
                        }
                        return true;
                }

                return false;
            }
        });



        //Fail 點擊
        textView_Fail.setOnTouchListener(new View.OnTouchListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        textView_Fail.setBackgroundColor(Color.parseColor("#60434343"));//按下灰色
                        return true;
                    case MotionEvent.ACTION_UP:
                        textView_Fail.setBackgroundColor(Color.parseColor("#ffffff"));//放開白色

                        final View item = LayoutInflater.from(msibook_dqaweekly_project_activity_lottery_report_detail.this).inflate(R.layout.msibook_dqaweekly_alert_dialog, null);

                        new AlertDialog.Builder(msibook_dqaweekly_project_activity_lottery_report_detail.this)
                                .setTitle("不及格原因")
                                .setView(item)
                                .setPositiveButton("送出", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        EditText editText = (EditText) item.findViewById(R.id.edit_text);
                                        String name = editText.getText().toString();
                                        if (!TextUtils.isEmpty(name)) {
                                            if (!TextUtils.isEmpty(RSS_NO)) {

                                                Update_RSS_Random(Set_F_SeqNo, "0", name,"1");
                                                Toast.makeText(msibook_dqaweekly_project_activity_lottery_report_detail.this, "已評分為不及格", Toast.LENGTH_SHORT).show();

                                                textView_Pass.setTextColor(Color.parseColor("#3cd45b"));
                                                textView_Pass.setBackgroundColor(Color.parseColor("#ffffff"));
                                                textView_Fail.setTextColor(Color.parseColor("#ffffff"));
                                                textView_Fail.setBackgroundColor(Color.parseColor("#ed4a47"));

                                                CheckReport=true;

                                                Log.w("Fail","Fail");
                                            }


                                        }
                                    }
                                })
                                .show();

                        return true;
                }

                return false;
            }
        });

        //專案 點擊 (查看網頁版專案資訊)
        textView_F_Model.setOnTouchListener(new View.OnTouchListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        textView_F_Model.setBackgroundColor(Color.parseColor("#60434343"));//按下灰色
                        return true;
                    case MotionEvent.ACTION_UP:
                        textView_F_Model.setBackgroundColor(Color.parseColor("#ffffff"));//放開白色

                        Intent intent = new Intent(msibook_dqaweekly_project_activity_lottery_report_detail.this, msibook_dqaweekly_project_info.class);

                        //給 專案名稱 MS-7788
                        intent.putExtra("F_Model_Name", Set_F_Model);//代 專案名稱下一頁
                        //給 專案代碼 12922
                        intent.putExtra("F_ModelID", Set_F_ModelID);//代專案代碼下一頁

                        startActivity(intent);

                        return true;
                }

                return false;
            }
        });

        //F_TestResultID 點擊 (查看IMS資訊)
        textView_F_TestResultID.setOnTouchListener(new View.OnTouchListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        textView_F_TestResultID.setBackgroundColor(Color.parseColor("#60434343"));//按下灰色
                        return true;
                    case MotionEvent.ACTION_UP:
                        textView_F_TestResultID.setBackgroundColor(Color.parseColor("#ffffff"));//放開白色

                        Dialog dialog = new AlertDialog.Builder(msibook_dqaweekly_project_activity_lottery_report_detail.this)
                                .setTitle("前往IMS頁面")
                                .setMessage("您正在離開週報APP\n前往IMS頁面")//设置提示内容
                                //确定按钮
                                .setPositiveButton("立即前往", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        Intent intent = new Intent();
                                        intent.setAction(Intent.ACTION_VIEW);
                                        intent.setData(Uri.parse(Set_IMSLink));
                                        startActivity(intent);
                                    }
                                })
                                //取消按钮
                                .setNegativeButton("稍後再說", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Log.w("稍後再說", "稍後再說");
                                    }
                                })
                                .create();//创建对话框
                        dialog.show();//显示对话框

                        return true;
                }

                return false;
            }
        });


        Find_Random_RSS(getWeek, getYear, getDeptID, getKeyin);

    }

    public void Update_RSS_Random(String RSS_NO, String Result, String Improve,String Stat) {

        //顯示  讀取等待時間Bar
        progressBar.show();

        RequestQueue mQueue = Volley.newRequestQueue(this);

        getString("http://wtsc.msi.com.tw/IMS/Weekly_Report.asmx/Update_RSS_Random?RSS_NO=" + RSS_NO + "&Result=" + Result + "&Improve=" + Improve + "&Stat=" + Stat, mQueue, new GetServiceData.VolleyCallback() {
            @Override
            public void onSuccess(JSONObject result) {

            }
        });
        //關閉-讀取等待時間Bar
        progressBar.dismiss();

    }

    //抽驗報告
    public void Find_Random_RSS(String Week, String Year, String DeptID, String Keyin) {

        //顯示  讀取等待時間Bar
        progressBar.show();

        RequestQueue mQueue = Volley.newRequestQueue(this);

        getString("http://wtsc.msi.com.tw/IMS/Weekly_Report.asmx/Find_Random_RSS?Week=" + Week + "&Year=" + Year + "&DeptID=" + DeptID + "&Keyin=" + Keyin, mQueue, new GetServiceData.VolleyCallback() {
            @Override
            public void onSuccess(JSONObject result) {

                try {

                    JSONArray UserArray = new JSONArray(result.getString("Key"));

                    if (UserArray.length() > 0) {
                        JSONObject IssueData = UserArray.getJSONObject(0);

                        if (IssueData.isNull("F_SeqNo")) {
                            String F_SeqNo = "N/A";
                            Set_F_SeqNo = "N/A";
                        } else {
                            String F_SeqNo = String.valueOf(String.format("%010d", IssueData.getInt("F_SeqNo")));//報告序號 1
                            Set_F_SeqNo = F_SeqNo;
                            RSS_NO = String.valueOf(IssueData.getInt("F_SeqNo"));
                        }
                        Find_RSS_File(Set_F_SeqNo);//查看附件
                        Log.w("Set_F_SeqNo", String.valueOf(Set_F_SeqNo));

                        if (IssueData.isNull("F_Topic")) {
                            String F_Topic = "N/A";
                            Set_F_Topic = "N/A";
                        } else {
                            String F_Topic = IssueData.getString("F_Topic");//工作性質
                            Set_F_Topic = F_Topic;
                        }
                        Log.w("Set_F_Topic", String.valueOf(Set_F_Topic));

                        if (IssueData.isNull("F_Subject")) {
                            String F_Subject = "N/A";
                            Set_F_Subject = "N/A";
                        } else {
                            String F_Subject = IssueData.getString("F_Subject");// 表頭 主旨 3
                            Set_F_Subject = F_Subject;
                        }
                        Log.w("Set_F_Subject", String.valueOf(Set_F_Subject));

                        if (IssueData.isNull("F_Model")) {
                            String F_Model = "N/A";
                            Set_F_Model = "N/A";
                        } else {
                            String F_Model = IssueData.getString("F_Model");//表頭 專案 2
                            Set_F_Model = F_Model;
                        }
                        Log.w("Set_F_Model", String.valueOf(Set_F_Model));

                        if (IssueData.isNull("F_Author")) {
                            String F_Author = "N/A";
                            Set_F_Author = "N/A";
                        } else {
                            String F_Author = IssueData.getString("F_Author"); //匯報人員  4
                            Set_F_Author = F_Author;
                        }
                        Log.w("Set_F_Author", String.valueOf(Set_F_Author));

                        if (IssueData.isNull("F_Stage")) {
                            String F_Stage = "N/A";
                            Set_F_Stage = "N/A";
                        } else {
                            String F_Stage = IssueData.getString("F_Stage"); //研發階段 10
                            Set_F_Stage = F_Stage;
                        }
                        Log.w("Set_F_Stage", String.valueOf(Set_F_Stage));

                        if (IssueData.isNull("F_WType")) {
                            String F_WType = "N/A";
                            Set_F_WType = "N/A";
                        } else {
                            String F_WType = IssueData.getString("F_WType"); //工作型態 11
                            Set_F_WType = F_WType;
                        }
                        Log.w("Set_F_WType", String.valueOf(Set_F_WType));

                        if (IssueData.isNull("F_PeopleSum")) {
                            String F_PeopleSum = "N/A";
                            Set_F_PeopleSum = "N/A";
                        } else {
                            String F_PeopleSum = String.valueOf(IssueData.getInt("F_PeopleSum")); // 人力 12
                            Set_F_PeopleSum = F_PeopleSum;
                        }
                        Log.w("Set_F_PeopleSum", String.valueOf(Set_F_PeopleSum));

                        if (IssueData.isNull("F_ModelID")) {
                            String F_ModelID = "N/A";
                            Set_F_ModelID = "N/A";
                        } else {
                            String F_ModelID = IssueData.getString("F_ModelID"); //
                            Set_F_ModelID = F_ModelID;
                        }
                        Log.w("Set_F_ModelID", String.valueOf(Set_F_ModelID));

                        if (IssueData.isNull("F_SDate")) {
                            String F_SDate = "N/A";
                            Set_F_SDate = "N/A";
                        } else {
                            String F_SDate = IssueData.getString("F_SDate");
                            Set_F_SDate = F_SDate;
                        }
                        Log.w("Set_F_SDate", String.valueOf(Set_F_SDate));

                        if (IssueData.isNull("F_EDate")) {
                            String F_EDate = "N/A";
                            Set_F_EDate = "N/A";
                        } else {
                            String F_EDate = IssueData.getString("F_EDate");
                            Set_F_EDate = F_EDate;
                        }
                        Log.w("Set_F_EDate", String.valueOf(Set_F_EDate));

                        if (IssueData.isNull("F_WorkHour")) {
                            String F_WorkHour = "N/A";
                            Set_F_WorkHour = "N/A";
                        } else {
                            String F_WorkHour = String.valueOf(IssueData.getDouble("F_WorkHour")); //工時  7
                            Set_F_WorkHour = F_WorkHour;
                        }
                        Log.w("Set_F_WorkHour", String.valueOf(Set_F_WorkHour));

                        if (IssueData.isNull("F_TestResultID")) {
                            String F_TestResultID = "N/A";
                            Set_F_TestResultID = "N/A";
                        } else {
                            String F_TestResultID = IssueData.getString("F_TestResultID"); //結果 6
                            Set_F_TestResultID = F_TestResultID;
                        }
                        Log.w("Set_F_TestResultID", String.valueOf(Set_F_TestResultID));

                        if (IssueData.isNull("F_Ver")) {
                            String F_Ver = "N/A";
                            Set_F_Ver = "N/A";
                        } else {
                            String F_Ver = String.valueOf(IssueData.getString("F_Ver"));//"F_SeqNo": 6159     ????
                            Set_F_Ver = F_Ver;
                        }
                        Log.w("Set_F_Ver", String.valueOf(Set_F_Ver));

                        if (IssueData.isNull("F_Platform")) {
                            String F_Platform = "N/A";
                            Set_F_Platform = "N/A";
                        } else {
                            String F_Platform = IssueData.getString("F_Platform");
                            Set_F_Platform = F_Platform;
                        }
                        Log.w("Set_F_Platform", String.valueOf(Set_F_Platform));

                        if (IssueData.isNull("F_Env")) {
                            String F_Env = "N/A";
                            Set_F_Env = "N/A";
                        } else {
                            String F_Env = IssueData.getString("F_Env");
                            Set_F_Env = F_Env;
                        }
                        Log.w("Set_F_Env", String.valueOf(Set_F_Env));

                        if (IssueData.isNull("F_EQP")) {
                            String F_EQP = "N/A";
                            Set_F_EQP = "N/A";
                        } else {
                            String F_EQP = IssueData.getString("F_EQP");
                            Set_F_EQP = F_EQP;
                        }
                        Log.w("Set_F_EQP", String.valueOf(Set_F_EQP));

                        if (IssueData.isNull("F_Ref")) {
                            String F_Ref = "N/A";
                            Set_F_Ref = "N/A";
                        } else {
                            String F_Ref = IssueData.getString("F_Ref");
                            Set_F_Ref = F_Ref;
                        }
                        Log.w("Set_F_Ref", String.valueOf(Set_F_Ref));

                        if (IssueData.isNull("F_Result")) {
                            String F_Result = "N/A";
                            Set_F_Result = "N/A";
                        } else {
                            String F_Result = IssueData.getString("F_Result"); //測試結果資料 14
                            Set_F_Result = F_Result;
                        }
                        Log.w("Set_F_Result", String.valueOf(Set_F_Result));

                        if (IssueData.isNull("F_Comments")) {
                            String F_Comments = "N/A";
                            Set_F_Comments = "N/A";
                        } else {
                            String F_Comments = IssueData.getString("F_Comments");
                            Set_F_Comments = F_Comments;
                        }
                        Log.w("Set_F_Comments", String.valueOf(Set_F_Comments));

                        if (IssueData.isNull("F_Common")) {
                            String F_Common = "N/A";
                            Set_F_Common = "N/A";
                        } else {
                            String F_Common = IssueData.getString("F_Common");
                            Set_F_Common = F_Common;
                        }
                        Log.w("Set_F_Common", String.valueOf(Set_F_Common));

                        NumberFormat nf = NumberFormat.getInstance();//設定三位數一逗點格式方法
                        if (IssueData.isNull("F_CostSum")) {
                            String F_CostSum = "N/A";
                            Set_F_CostSum = "N/A";
                        } else {
                            String F_CostSum = String.valueOf(nf.format(IssueData.getInt("F_CostSum"))); //費用
                            Set_F_CostSum = F_CostSum;
                        }
                        Log.w("Set_F_CostSum", String.valueOf(Set_F_CostSum));

                        if (IssueData.isNull("F_RegionID")) {
                            String F_RegionID = "N/A";
                            Set_F_RegionID = "N/A";
                        } else {
                            String F_RegionID = IssueData.getString("F_RegionID");//
                            Set_F_RegionID = F_RegionID;
                        }
                        Log.w("Set_F_RegionID", String.valueOf(Set_F_RegionID));

                        if (IssueData.isNull("F_Progress")) {
                            String F_Progress = "N/A";
                            Set_F_Progress = "N/A";
                        } else {
                            String F_Progress = String.valueOf(IssueData.getInt("F_Progress")); // 進度 5
                            Set_F_Progress = F_Progress;
                        }
                        Log.w("Set_F_Progress", String.valueOf(Set_F_Progress));

                        if (IssueData.isNull("F_IsAudit")) {
                            String F_IsAudit = "Null";
                            Set_F_IsAudit = false;
                        } else {
                            Boolean F_IsAudit = IssueData.getBoolean("F_IsAudit");//
                            Set_F_IsAudit = F_IsAudit;///注意是Boolen值
                        }
                        Log.w("Set_F_IsAudit", String.valueOf(Set_F_IsAudit));

                        if (IssueData.isNull("ModelLink")) {
                            String ModelLink = "N/A";
                            Set_ModelLink = "N/A";
                        } else {
                            String ModelLink = IssueData.getString("ModelLink");
                            Set_ModelLink = ModelLink;
                        }
                        Log.w("Set_ModelLink", String.valueOf(Set_ModelLink));

                        if (IssueData.isNull("IMSLink")) {
                            String IMSLink = "N/A";
                            Set_IMSLink = "N/A";
                        } else {
                            String IMSLink = IssueData.getString("IMSLink");
                            Set_IMSLink = IMSLink;
                        }
                        Log.w("Set_IMSLink", String.valueOf(Set_IMSLink));

                        if (IssueData.isNull("RSSLink")) {
                            String RSSLink = "N/A";
                            Set_RSSLink = "N/A";
                        } else {
                            String RSSLink = IssueData.getString("RSSLink");
                            Set_RSSLink = RSSLink;
                        }
                        Log.w("Set_RSSLink", String.valueOf(Set_RSSLink));

                        textView_F_SeqNo.setText("No. " + Set_F_SeqNo);
                        textView_F_Model.setText(Set_F_Model);
                        textView_F_Subject.setText(Set_F_Subject);

                        textView_F_Author.setText(Set_F_Author);
                        textView_F_Progress.setText(Set_F_Progress + "%");

                        if (Set_F_TestResultID.indexOf("FAIL") == -1) {//如果FAIL則變色 + 開放可點擊
                            if (Set_F_TestResultID.indexOf("PASS") == -1) {
                                //其他
                                textView_F_TestResultID.setEnabled(false);
                                textView_F_TestResultID.setText(Set_F_TestResultID);
                                textView_F_TestResultID.setTextColor(Color.parseColor("#333333"));
                            } else {
                                //PASS
                                textView_F_TestResultID.setEnabled(false);
                                textView_F_TestResultID.setText(Set_F_TestResultID);
                                textView_F_TestResultID.setTextColor(Color.parseColor("#46aa36"));
                            }
                        } else {
                            //FAIL
                            textView_F_TestResultID.setEnabled(true);
                            textView_F_TestResultID.setText(Set_F_TestResultID);
                            textView_F_TestResultID.setTextColor(Color.parseColor("#f0625d"));
                        }

                        textView_F_WorkHour.setText(Set_F_WorkHour);

                        if(Set_F_RegionID.indexOf("1") == -1){
                            textView_F_CostSum.setText(Set_F_CostSum + " RMB");
                        }else{
                            textView_F_CostSum.setText(Set_F_CostSum + " NTD");
                        }

                        if (IssueData.isNull("Lob")) {
                            String Lob = "N/A";
                            Set_Lob = "N/A";
                        } else {
                            String Lob = IssueData.getString("Lob");
                            Set_Lob = Lob;
                        }


                        textView_F_Topic.setText(Set_F_Topic);
                        textView_F_Stage.setText(Set_F_Stage);
                        textView_F_WType.setText(Set_F_WType);
                        textView_F_PeopleSum.setText(Set_F_PeopleSum);
                        textView_F_Ver.setText(Set_F_Ver);

                        textView_F_Result.setText(Set_F_Result);
                        textView_F_Env.setText(Set_F_Env);
                        textView_F_Comments.setText(Set_F_Comments);
                        textView_F_Platform.setText(Set_F_Platform);
                        textView_F_EQP.setText(Set_F_EQP);
                        textView_F_Ref.setText(Set_F_Ref);
                        textView_Lob.setText("("+Set_Lob+")");


                        //關閉-讀取等待時間Bar
                        progressBar.dismiss();

                    } else {
                        Dialog dialog = new AlertDialog.Builder(msibook_dqaweekly_project_activity_lottery_report_detail.this)
                                .setMessage("本週無資料")//设置提示内容
                                //确定按钮
                                .setPositiveButton("確定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        finish();
                                    }
                                })
                                .create();//创建对话框
                        dialog.show();//显示对话框
                        //關閉-讀取等待時間Bar
                        progressBar.dismiss();
                    }


                } catch (JSONException ex) {

                }

            }
        });

    }


    //查附件
    public void Find_RSS_File(String RSSNO) {

        DownloadFile_Data_Item_List.clear();

        RequestQueue mQueue = Volley.newRequestQueue(this);

        getString("http://wtsc.msi.com.tw/IMS/Weekly_Report.asmx/Find_RSS_File?RSSNO=" + RSSNO, mQueue, new GetServiceData.VolleyCallback() {
            @Override
            public void onSuccess(JSONObject result) {

                try {

                    JSONArray UserArray = new JSONArray(result.getString("Key"));


                    for (int i = 0; i < UserArray.length(); i++) {

                        JSONObject IssueData = UserArray.getJSONObject(i);

                        String F_FileName = String.valueOf(IssueData.getString("F_FileName")); //

                        String F_DownloadFilePath = String.valueOf(IssueData.getString("F_DownloadFilePath")); //

                        Log.w("WTFURL",F_DownloadFilePath);

                        DownloadFile_Data_Item_List.add(i,new DownloadFile_Data_Item(F_FileName,F_DownloadFilePath));
                    }


                    mListView = (ListView)findViewById(R.id.lsv_F_Result);

                    mDownloadFile_Data_Adapter = new DownloadFile_Data_Adapter(mContext,DownloadFile_Data_Item_List);

                    mListView.setAdapter(mDownloadFile_Data_Adapter);



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

    public class DownloadFile_Data_Item  {

        String F_FileName;

        String F_DownloadFilePath;

        public DownloadFile_Data_Item(String F_FileName,String F_DownloadFilePath)
        {
            this.F_FileName = F_FileName;

            this.F_DownloadFilePath = F_DownloadFilePath;
        }

        public String GetF_FileName()
        {
            return this.F_FileName;
        }

        public String GetF_DownloadFilePath()
        {
            return this.F_DownloadFilePath;
        }

    }

    //------------------------------------Adapter-------------------------------------

    public class DownloadFile_Data_Adapter extends BaseAdapter {

        private LayoutInflater mLayInf;

        private List<DownloadFile_Data_Item> DownloadFile_Data_Item_List;

        private Context Important_DataContext;

        private String Title;

        public DownloadFile_Data_Adapter(Context context, List<DownloadFile_Data_Item> DownloadFile_Data_Item_List) {
            mLayInf = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            Important_DataContext = context;

            this.Title = Title;

            this.DownloadFile_Data_Item_List = DownloadFile_Data_Item_List;

        }

        @Override
        public int getCount() {
            return DownloadFile_Data_Item_List.size();
        }

        @Override
        public Object getItem(int position) {
            return DownloadFile_Data_Item_List.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            View v = new View(Important_DataContext);

            v = mLayInf.inflate(R.layout.msibook_dqaweekly_downloadfilepath_adapter, parent, false);

            TextView textView_F_DownloadFilePath = (TextView) v.findViewById(R.id.textView_F_DownloadFilePath);

            textView_F_DownloadFilePath.setText("附件連結:"+DownloadFile_Data_Item_List.get(position).GetF_FileName());

            textView_F_DownloadFilePath.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Log.w("URL",String.valueOf(GetServiceData.ServicePath + "/Get_File?FileName=" + DownloadFile_Data_Item_List.get(position).GetF_DownloadFilePath()));
                    Uri uri=Uri.parse(GetServiceData.ServicePath + "/Get_File?FileName=" + DownloadFile_Data_Item_List.get(position).GetF_DownloadFilePath());
                    Intent i = new Intent(Intent.ACTION_VIEW,uri);
                    startActivity(i);
                }
            });


            return v;
        }
    }

}
