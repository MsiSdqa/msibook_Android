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
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class msibook_rss extends AppCompatActivity {

    private ListView mListView;

    private String getWeek;
    private String getYear;
    private String getWorkID;

    private msibook_rss_list_adapter mHistoryReportAdapter;
    private List<msibook_rss_list_item> HistoryReport_Item_List = new ArrayList<msibook_rss_list_item>();

    private Context mContext;
    private ProgressDialog progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msibook_rss);

        setTitle("RSS");

        //讀取時間Bar
        progressBar = new ProgressDialog(this);
        progressBar.setCancelable(true);
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.setMessage("資料載入中");

        mContext = msibook_rss.this;
        mListView = (ListView) findViewById(R.id.Lsv_RSS_List);

        //Find_RandomRSS_List(UserData.WorkID);
        Find_RandomRSS_List("10015667");
    }

    private void Find_RandomRSS_List(String WorkID) {

        //顯示  讀取等待時間Bar
        progressBar.show();

        HistoryReport_Item_List.clear();

        RequestQueue mQueue = Volley.newRequestQueue(this);

        String Path = GetServiceData.ServicePath + "/Find_RSS_List?WorkID=" + WorkID;

        GetServiceData.getString(Path, mQueue, new GetServiceData.VolleyCallback() {
            @Override
            public void onSuccess(JSONObject result) {

                try {

                    JSONArray UserArray = new JSONArray(result.getString("Key"));

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

//                        Boolean Result = IssueData.getBoolean("Result"); // true
//
//                        Boolean Improve = IssueData.getBoolean("Improve"); // null

                       // String Creator = String.valueOf(IssueData.getString("Creator"));//"吳美樺"

                        HistoryReport_Item_List.add(i,new msibook_rss_list_item(Week,Date,Report_No,Region,Dept,Repoter,Manager,Project,Subject));

                    }

                    mListView = (ListView)findViewById(R.id.Lsv_RSS_List);

                    //mOverhourAdapter = new OverhourAdapter(mContext,Overhour_Item_List,MainTitle);
                    mHistoryReportAdapter = new msibook_rss_list_adapter(mContext,HistoryReport_Item_List);

                    mListView.setAdapter(mHistoryReportAdapter);

                    //關閉-讀取等待時間Bar
                    progressBar.dismiss();

                    //專案細項to 第三頁
                    mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            Intent intent = new Intent();

                            intent.putExtra("RSSNO",HistoryReport_Item_List.get(position).GetReport_No());//給第查詢報告的 序號

                            Log.w("RSSNO",String.valueOf(HistoryReport_Item_List.get(position).GetReport_No()));

                            intent.setClass(msibook_rss.this, msibook_rss_detail.class);
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

    public class msibook_rss_list_adapter extends BaseAdapter {

        private LayoutInflater mLayInf;

        private List<msibook_rss_list_item> HistoryReport_Item_List = new ArrayList<msibook_rss_list_item>();

        private Context ProjectContext;


        public msibook_rss_list_adapter(Context context, List<msibook_rss_list_item> HistoryReport_Item_List) {
            mLayInf = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            ProjectContext = context;

            this.HistoryReport_Item_List = HistoryReport_Item_List;

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
        public View getView(int position, View convertView, ViewGroup parent) {

            View v = new View(ProjectContext);

            v = mLayInf.inflate(R.layout.activity_msibook_rss_list_item, parent, false);


            TextView textView_Project = (TextView) v.findViewById(R.id.textView_Project);
            TextView textView_Report_No = (TextView) v.findViewById(R.id.textView_Report_No);
            TextView textView_Dept = (TextView) v.findViewById(R.id.textView_Dept);
            TextView textView_Subject = (TextView) v.findViewById(R.id.textView_Subject);

            TextView textView_next = (TextView) v.findViewById(R.id.textView_next);


            textView_Project.setText(HistoryReport_Item_List.get(position).GetProject());
            textView_Report_No.setText("No."+String.format("%010d",Integer.valueOf(HistoryReport_Item_List.get(position).GetReport_No())));
            textView_Dept.setText(HistoryReport_Item_List.get(position).GetDept());
            textView_Subject.setText(HistoryReport_Item_List.get(position).GetSubject());


            return v;
        }
    }

    public class msibook_rss_list_item {

        String Week;//週

        String Date;//日期

        String Report_No;//報告編號

        String Region;//報告編號

        String Dept;//部門

        String Repoter;//報告人員

        String Manager;//審核主管

        String Project;//專案

        String Subject;//主旨

        String Result;// true??false??

        String Improve;//null??

        String Creator;// 創立 抽籤者

        public msibook_rss_list_item(String Week,String Date,String Report_No,String Region,String Dept,String Repoter,String Manager,String Project,String Subject)
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

    }

}
