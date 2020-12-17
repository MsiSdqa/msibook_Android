package dqa.com.msibook;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
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
import java.util.Calendar;
import java.util.List;

public class msibook_dqaweekly_project_activity_lottery_report_historylist extends ActionBarActivity {

    private LinearLayout linear_history;
    private ListView mListView;

    private String getWeek;
    private String getYear;
    private Integer system_Year;
    private String getWorkID;

    private String Now_Year;

    private TextView textView_title;
    private TextView textView_back;
    private TextView textView_select_year;
    private TextView textView_next;
    private Integer Type;

    private HistoryReportAdapter mHistoryReportAdapter;
    private List<HistoryReport_Item> HistoryReport_Item_List = new ArrayList<HistoryReport_Item>();

    private Context mContext;
    private ProgressDialog progressBar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msibook_dqaweekly_project_lottery_report_historylist);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_up);
        setSupportActionBar(toolbar);

        setTitle("");
        //讀取時間Bar
        progressBar = new ProgressDialog(this);
        progressBar.setCancelable(true);
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.setMessage("資料載入中");

        mContext = msibook_dqaweekly_project_activity_lottery_report_historylist.this;
        mListView = (ListView) findViewById(R.id.history_listview);

        linear_history = (LinearLayout) findViewById(R.id.linear_history);

        textView_title = (TextView) findViewById(R.id.textView_title);
        textView_back = (TextView) findViewById(R.id.textView_back);
        textView_select_year = (TextView) findViewById(R.id.textView_select_year);
        textView_next = (TextView) findViewById(R.id.textView_next);

        getWeek = getIntent().getStringExtra("Week");//週次
        getYear = getIntent().getStringExtra("Year");//年
        getWorkID = getIntent().getStringExtra("WorkID");//部門ID
        Log.w("WorkID",getWorkID);

        Calendar c = Calendar.getInstance();
        Now_Year = String.valueOf(c.get(Calendar.YEAR));
        textView_select_year.setText(Now_Year);
        Type = 0;

        final Calendar calendar = Calendar.getInstance();
        system_Year = Integer.valueOf(calendar.get(Calendar.YEAR));
        textView_next.setVisibility(View.INVISIBLE);

        Find_RandomRSS_List(getYear,Type);



        //查看以往 歷史報告資訊
        textView_back.setOnTouchListener(new View.OnTouchListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        //textView_back.setBackgroundColor(Color.parseColor("#ffffff"));//按下灰色
                        return true;
                    case MotionEvent.ACTION_UP:
                        //textView_back.setBackgroundColor(Color.parseColor("#60434343"));//放開白色

                        Now_Year = String.valueOf(Integer.valueOf(Now_Year)-1);
                        textView_select_year.setText(Now_Year);
                        Find_RandomRSS_List(Now_Year,Type);

                        Calendar yy = Calendar.getInstance();
                        String nowYear = String.valueOf(yy.get(Calendar.YEAR));

                        if (Integer.valueOf(Now_Year) == 2017){
                            textView_next.setVisibility(View.VISIBLE);
                            textView_back.setVisibility(View.INVISIBLE);
                        }else{
                            textView_next.setVisibility(View.VISIBLE);
                            textView_back.setVisibility(View.VISIBLE);
                        }

//                        Dialog dialog = new AlertDialog.Builder(ProjectActivity_lottery_report_historylist.this)
//                                .setTitle("前往抽驗歷史頁面")
//                                .setMessage("您正在離開週報APP\n前往抽驗歷史頁面")//设置提示内容
//                                //确定按钮
//                                .setPositiveButton("立即前往", new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialog, int which) {
//
//                                        Intent intent = new Intent();
//                                        intent.setAction(Intent.ACTION_VIEW);
//                                        intent.setData(Uri.parse("http://172.16.98.4/Code/Discuss/C_Dis_Article_Detail.aspx?F_Master_ID=18"));
//                                        startActivity(intent);
//                                    }
//                                })
//                                //取消按钮
//                                .setNegativeButton("稍後再說", new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialog, int which) {
//                                        Log.w("稍後再說", "稍後再說");
//                                    }
//                                })
//                                .create();//创建对话框
//                        dialog.show();//显示对话框

                        return true;
                }

                return false;
            }
        });

        //查看以往 歷史報告資訊
        textView_next.setOnTouchListener(new View.OnTouchListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        //textView_back.setBackgroundColor(Color.parseColor("#ffffff"));//按下灰色
                        return true;
                    case MotionEvent.ACTION_UP:
                        //textView_back.setBackgroundColor(Color.parseColor("#60434343"));//放開白色

                        Now_Year = String.valueOf(Integer.valueOf(Now_Year)+1);
                        textView_select_year.setText(Now_Year);
                        Find_RandomRSS_List(Now_Year,Type);

                        Calendar yy = Calendar.getInstance();
                        String nowYear = String.valueOf(yy.get(Calendar.YEAR));
                        Log.w("nowYear",nowYear);
                        Log.w("getYear",Now_Year);

                        if (Integer.valueOf(Now_Year) == 2019){
                            textView_next.setVisibility(View.INVISIBLE);
                            textView_back.setVisibility(View.VISIBLE);
                        }else{
                            textView_next.setVisibility(View.VISIBLE);
                            textView_back.setVisibility(View.VISIBLE);
                        }

                        return true;
                }

                return false;
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

        // Handle your other action bar items...
        switch (item.getItemId()) {
            case R.id.Pass:
//                android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
//                HelpDialog helpDialog = new HelpDialog();
//                helpDialog.show(fm,"Help");
                Type = 1;
                Find_RandomRSS_List(Now_Year,Type);
                Log.w("Pass","Pass");
                return true;

            case R.id.fail:
                Type = 2;
                Find_RandomRSS_List(Now_Year,Type);
                return true;

            case R.id.CheckTime:
//                fm = getSupportFragmentManager();
//                AdminDialog adminDialog = new AdminDialog();
//                adminDialog.show(fm,"Device Admin");
                Type = 0;
                Find_RandomRSS_List(Now_Year,Type);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

        //return super.onOptionsItemSelected(item);
    }

    //DB專案細部清單
    private void Find_RandomRSS_List(String Year, final Integer Type) {

        //顯示  讀取等待時間Bar
        progressBar.show();

        HistoryReport_Item_List.clear();

        RequestQueue mQueue = Volley.newRequestQueue(this);

        String Path = "http://wtsc.msi.com.tw/IMS/Weekly_Report.asmx/Find_RandomRSS_List?Year=" + Year;

        getString(Path, mQueue, new GetServiceData.VolleyCallback() {
            @Override
            public void onSuccess(JSONObject result) {

                try {

                    JSONArray UserArray = new JSONArray(result.getString("Key"));

                    int a = 0;

                    for (int i = 0; i < UserArray.length(); i++) {

                        JSONObject IssueData = UserArray.getJSONObject(i);

                        String Week = String.valueOf(IssueData.getInt("Week"));// 1

                        String Date = String.valueOf(IssueData.getString("Date"));//"2018-01-08T10:02:48.817",

                        String Report_No = String.valueOf(IssueData.getInt("Report_No"));//1592026,

                        String Region = String.valueOf(IssueData.getInt("Region"));// 1

                        String Dept = String.valueOf(IssueData.getString("Dept"));//"設計品質驗證二部一課",

                        String Repoter = String.valueOf(IssueData.getString("Repoter"));//"林玟萱",

                        String Manager = String.valueOf(IssueData.getString("Manager"));//"劉家豪",

                        String Project = String.valueOf(IssueData.getString("Project"));//"MS-B168",

                        String Subject = String.valueOf(IssueData.getString("Subject"));//"[Dell] Centauri R6_Check FV stage test report",

                        Boolean Result = IssueData.getBoolean("Result"); // true

                        String Improve = IssueData.getString("Improve"); // null

                        String Creator = String.valueOf(IssueData.getString("Creator"));//"吳美樺"

                        switch (Type) {
                            case 0:
                                HistoryReport_Item_List.add(a, new HistoryReport_Item(Week, Date, Report_No, Region, Dept, Repoter, Manager, Project, Subject, Result, Improve));
                                a++;
                                break;
                            case 1:
                                if (Result == true) {
                                    HistoryReport_Item_List.add(a, new HistoryReport_Item(Week, Date, Report_No, Region, Dept, Repoter, Manager, Project, Subject, Result, Improve));
                                    a++;
                                }
                                break;
                            case 2:
                                if (Result == false) {
                                    HistoryReport_Item_List.add(a, new HistoryReport_Item(Week, Date, Report_No, Region, Dept, Repoter, Manager, Project, Subject, Result, Improve));
                                    a++;
                                }
                                break;
                        }

                    }
                    if(HistoryReport_Item_List.size()>0){
                        mListView.setVisibility(View.VISIBLE);
                    }else{
                        mListView.setVisibility(View.GONE);
                        mListView.setEmptyView(findViewById(R.id.empty));
                    }
                    mListView = (ListView)findViewById(R.id.history_listview);

                    //mOverhourAdapter = new OverhourAdapter(mContext,Overhour_Item_List,MainTitle);
                    mHistoryReportAdapter = new HistoryReportAdapter(mContext,HistoryReport_Item_List,msibook_dqaweekly_project_activity_lottery_report_historylist.this);

                    mListView.setAdapter(mHistoryReportAdapter);

                    //關閉-讀取等待時間Bar
                    progressBar.dismiss();

//                    //專案細項to 第三頁
//                    mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                        @Override
//                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//                            Intent intent = new Intent();
//
//                            intent.putExtra("RSSNO",HistoryReport_Item_List.get(position).GetReport_No());//給第查詢報告的 序號
//
//                            Log.w("RSSNO",String.valueOf(HistoryReport_Item_List.get(position).GetReport_No()));
//
//                            intent.setClass(ProjectActivity_lottery_report_historylist.this, ProjectActivity_lottery_report_list_tofindReport.class);
//                            //開啟Activity
//                            startActivity(intent);
//
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

    //--------------------Item-------------------
    public class HistoryReport_Item {

        String Week;//週

        String Date;//日期

        String Report_No;//報告編號

        String Region;//報告編號

        String Dept;//部門

        String Repoter;//報告人員

        String Manager;//審核主管

        String Project;//專案

        String Subject;//主旨

        Boolean Result;// true??false??

        String Improve;//null??

        String Creator;// 創立 抽籤者

        public HistoryReport_Item(String Week,String Date,String Report_No,String Region,String Dept,String Repoter,String Manager,String Project,String Subject,Boolean Result,String Improve)
        {
            this.Week = Week;

            this.Date = Date;

            this.Report_No = Report_No;

            this.Region = Region;

            this.Dept = Dept;

            this.Repoter = Repoter;

            this.Manager = Manager;

            this.Project = Project;

            this.Subject = Subject;

            this.Result = Result;

            this.Improve = Improve;


        }


        public String GetWeek()
        {
            return this.Week;
        }

        public String GetDate()
        {
            return this.Date;
        }

        public String GetReport_No()
        {
            return this.Report_No;
        }

        public String GetRegion()
        {
            return this.Region;
        }

        public String GetDept()
        {
            return this.Dept;
        }

        public String GetRepoter()
        {
            return this.Repoter;
        }

        public String GetManager()
        {
            return this.Manager;
        }

        public String GetProject()
        {
            return this.Project;
        }

        public String GetSubject()
        {
            return this.Subject;
        }

        public Boolean GetResult()
        {
            return this.Result;
        }

        public String GetImprove()
        {
            return this.Improve;
        }

    }


    //-------------------Adapter----------------
    public class HistoryReportAdapter extends BaseAdapter {

        private LayoutInflater mLayInf;

        private List<HistoryReport_Item> HistoryReport_Item_List = new ArrayList<HistoryReport_Item>();

        private Context ProjectContext;

        private Activity parentActivity;

        public HistoryReportAdapter(Context context, List<HistoryReport_Item> HistoryReport_Item_List,  Activity parentActivity) {
            mLayInf = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            ProjectContext = context;

            this.HistoryReport_Item_List = HistoryReport_Item_List;

            this.parentActivity = parentActivity;

        }

        @Override
        public int getCount() {
            return HistoryReport_Item_List.size();
        }

        @Override
        public Object getItem(int position) {
            return HistoryReport_Item_List.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            View v = new View(ProjectContext);

            v = mLayInf.inflate(R.layout.msibook_dqaweekly_adapter_history_report_item, parent, false);

            final LinearLayout linear_Result = (LinearLayout) v.findViewById(R.id.linear_Result);
            final LinearLayout linear_listmain = (LinearLayout) v.findViewById(R.id.linear_listmain);
            TextView textView_icon = (TextView) v.findViewById(R.id.textView_icon);
            final TextView textView_Result = (TextView) v.findViewById(R.id.textView_Result);
            TextView textView_Week = (TextView) v.findViewById(R.id.textView_Week);
            TextView textView_Project = (TextView) v.findViewById(R.id.textView_Project);
            TextView textView_Report_No = (TextView) v.findViewById(R.id.textView_Report_No);
            TextView textView_Dept = (TextView) v.findViewById(R.id.textView_Dept);
            TextView textView_Subject = (TextView) v.findViewById(R.id.textView_Subject);

            final TextView textView_next = (TextView) v.findViewById(R.id.textView_next);

            if(HistoryReport_Item_List.get(position).GetResult()==true){
                linear_Result.setEnabled(false);
                textView_Result.setText("及格");
                textView_Result.setTextColor(Color.parseColor("#3cd45b"));
                textView_icon.setVisibility(View.INVISIBLE);
            }else{
                linear_Result.setEnabled(true);
                textView_Result.setText("不及格");
                textView_Result.setTextColor(Color.parseColor("#ed4a47"));
                textView_icon.setVisibility(View.VISIBLE);
            }

            textView_Week.setText("第"+HistoryReport_Item_List.get(position).GetWeek()+"週");
            textView_Project.setText(HistoryReport_Item_List.get(position).GetProject());
            textView_Report_No.setText("No."+String.format("%010d",Integer.valueOf(HistoryReport_Item_List.get(position).GetReport_No())));
            textView_Dept.setText(HistoryReport_Item_List.get(position).GetDept());
            textView_Subject.setText(HistoryReport_Item_List.get(position).GetSubject());

            // 不及格點擊 (查看不及格資訊)
            linear_Result.setOnTouchListener(new View.OnTouchListener() {
                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                @Override
                public boolean onTouch(View v, MotionEvent event) {

                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            linear_Result.setBackgroundColor(Color.parseColor("#60434343"));//按下灰色
                            return true;
                        case MotionEvent.ACTION_UP:
                            linear_Result.setBackgroundColor(Color.parseColor("#ffffff"));//放開白色

                            Dialog dialog = new AlertDialog.Builder(parentActivity)
                                    .setTitle("第"+HistoryReport_Item_List.get(position).GetWeek() + "週")
                                    .setMessage("不及格說明 : \n"+ HistoryReport_Item_List.get(position).GetImprove())//设置提示内容
                                    //确定按钮
                                    .setPositiveButton("確定", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

//                                        Intent intent = new Intent();
//                                        intent.setAction(Intent.ACTION_VIEW);
//                                        intent.setData(Uri.parse(Set_ModelLink));
//                                        startActivity(intent);
                                        }
                                    })
                                    .create();//创建对话框
                            dialog.show();//显示对话框

                            return true;
                    }

                    return false;
                }
            });

            //主體點擊(查看細部內容)
            linear_listmain.setOnTouchListener(new View.OnTouchListener() {
                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                @Override
                public boolean onTouch(View v, MotionEvent event) {

                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            linear_listmain.setBackgroundColor(Color.parseColor("#ffffff"));//按下灰色
                            return true;
                        case MotionEvent.ACTION_UP:
                            linear_listmain.setBackgroundColor(Color.parseColor("#ffffff"));//放開白色

                            Intent intent = new Intent();

                            intent.putExtra("RSSNO",HistoryReport_Item_List.get(position).GetReport_No());//給第查詢報告的 序號

                            Log.w("RSSNO",String.valueOf(HistoryReport_Item_List.get(position).GetReport_No()));

                            intent.setClass(parentActivity, msibook_dqaweekly_project_activity_lottery_report_list_tofindReport.class);
                            //開啟Activity
                            parentActivity.startActivity(intent);

                            return true;
                    }

                    return false;
                }
            });

            //next(查看細部內容)
            textView_next.setOnTouchListener(new View.OnTouchListener() {
                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                @Override
                public boolean onTouch(View v, MotionEvent event) {

                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            //textView_next.setBackgroundColor(Color.parseColor("#ffffff"));//按下灰色
                            return true;
                        case MotionEvent.ACTION_UP:
                            //textView_next.setBackgroundColor(Color.parseColor("#ffffff"));//放開白色

                            Intent intent = new Intent();

                            intent.putExtra("RSSNO",HistoryReport_Item_List.get(position).GetReport_No());//給第查詢報告的 序號

                            Log.w("RSSNO",String.valueOf(HistoryReport_Item_List.get(position).GetReport_No()));

                            intent.setClass(parentActivity, msibook_dqaweekly_project_activity_lottery_report_list_tofindReport.class);
                            //開啟Activity
                            parentActivity.startActivity(intent);

                            return true;
                    }

                    return false;
                }
            });


            return v;
        }
    }





}
