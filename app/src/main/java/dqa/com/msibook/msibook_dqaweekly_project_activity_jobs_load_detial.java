package dqa.com.msibook;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
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

public class msibook_dqaweekly_project_activity_jobs_load_detial extends AppCompatActivity {

    private Context mContext;
    private RecyclerView mRecycleView;
    private ListView mListView;
    //private ListView utilization_dpt_detial_listview;//各單位稼動率Listview
    private RecyclerView jobs_load_dpt_detial_Recycleview;
    private ListView jobs_load_detial_Listview;
    private ProjectActivity_jobs_load_listview_detial_adapter adapter;

    private ProjectActivity_jobs_load_listview_detial_recycle_adapter mRecycleadapter;
    private List<ProjectActivity_jobs_load_listview_detial_Item> jobs_load_list_detial_Item = new ArrayList<ProjectActivity_jobs_load_listview_detial_Item>();
    private ProgressDialog progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msibook_dqaweekly_project_jobs_load_detial);

        //讀取時間Bar
        progressBar = new ProgressDialog(this);
        progressBar.setCancelable(true);
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.setMessage("資料載入中");

        mContext = msibook_dqaweekly_project_activity_jobs_load_detial.this;

        jobs_load_dpt_detial_Recycleview = (RecyclerView) findViewById(R.id.jobs_load_dpt_detial_Recycleview);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        jobs_load_dpt_detial_Recycleview.setLayoutManager(layoutManager);

        final LayoutInflater factory = getLayoutInflater();
        final View textEntryView = factory.inflate(R.layout.activity_msibook_dqaweekly_project_utilization_dpt_detial, null);
        //設定顯示座標位置

        String getEtraWeek = getIntent().getStringExtra("Week");//抓第一頁選的部門代號
        String getEtraYear = getIntent().getStringExtra("Year");//抓第一頁選的部門代號
        String getEtraDepID = getIntent().getStringExtra("ChoiceDepID");//抓第一頁選的部門代號
        String getDepID = getIntent().getStringExtra("DepID");

        TextView textView_title = (TextView)findViewById(R.id.textView_title);
        textView_title.setText(getDepID +" 單位達成率");

        Find_Week_Utilization(getEtraWeek,getEtraYear,getEtraDepID);

    }

    public void Find_Week_Utilization(String Week,String Year, String DeptID) {


        //顯示  讀取等待時間Bar
        progressBar.show();

        jobs_load_list_detial_Item.clear();

        RequestQueue mQueue = Volley.newRequestQueue(this);

        getString("http://wtsc.msi.com.tw/IMS/Weekly_Report.asmx/Find_Weekly_utilization?Week=" + Week + "&Year=" + Year + "&DeptID=" + DeptID, mQueue, new GetServiceData.VolleyCallback() {
            @Override
            public void onSuccess(JSONObject result) {

                try {
                    Log.w("稼動綠測試","");
                    JSONArray UserArray = new JSONArray(result.getString("Key"));
                    if (UserArray.length() > 0)
                    {

                        for (int i = 0; i < UserArray.length(); i++) {

                            JSONObject IssueData = UserArray.getJSONObject(i);

                            String F_DeptID = String.valueOf(IssueData.getString("F_DeptID"));

                            String DeptCode = String.valueOf(IssueData.getString("DeptCode"));

                            String Rate = String.valueOf(IssueData.getDouble("Rate"));

                            String CostRate = String.valueOf(IssueData.getDouble("CostRate"));

                            jobs_load_list_detial_Item.add(i,new ProjectActivity_jobs_load_listview_detial_Item(DeptCode,CostRate));
                        }

                        mRecycleadapter = new ProjectActivity_jobs_load_listview_detial_recycle_adapter(mContext,jobs_load_list_detial_Item);
                        jobs_load_dpt_detial_Recycleview.setAdapter(mRecycleadapter);

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

    //稼動率 generateCenterSpannableText
    private SpannableString generateCenterSpannableText(String Title, int a, int b, int c) {

        SpannableString s = new SpannableString(Title);
        s.setSpan(new ForegroundColorSpan(Color.rgb(a,b,c)), 0, s.length()-5, 0);
        s.setSpan(new ForegroundColorSpan(Color.rgb(33, 33, 33)), s.length()-5, s.length(), 0);
        s.setSpan(new RelativeSizeSpan(0.5f), s.length()-5, s.length(), s.SPAN_EXCLUSIVE_EXCLUSIVE);

        return s;
    }


    //---------------------------------------------------Item------------------------------------------------------
    public class ProjectActivity_jobs_load_listview_detial_Item {

        private String Dept1;
        private String CostRate;

        public ProjectActivity_jobs_load_listview_detial_Item(String Dept1, String CostRate) {

            this.Dept1 = Dept1;
            this.CostRate = CostRate;

        }

        public String getDept1() {
            return Dept1;
        }

        public String getCostRate() {
            return CostRate;
        }
    }

    //----------------------------------------------------Adapter--------------------------------------------------
    public class ProjectActivity_jobs_load_listview_detial_adapter extends BaseAdapter {

        private class ViewHolder {
            TextView txtDept1;
            TextView txtutilization1;

            public ViewHolder(TextView txtDept1, TextView txtutilization1) {
                this.txtDept1 = txtDept1;
                this.txtutilization1 = txtutilization1;
            }
        }

        private LayoutInflater myInflater;
        private List<ProjectActivity_jobs_load_listview_detial_Item> utilization_list;

        public ProjectActivity_jobs_load_listview_detial_adapter(Context context, List<ProjectActivity_jobs_load_listview_detial_Item> movie) {
            myInflater = LayoutInflater.from(context);
            this.utilization_list = movie;
        }

        @Override
        public int getCount() {
            return utilization_list.size();
        }

        @Override
        public Object getItem(int arg0) {
            return utilization_list.get(arg0);
        }

        @Override
        public long getItemId(int position) {
            return utilization_list.indexOf(getItem(position));
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ProjectActivity_jobs_load_listview_detial_adapter.ViewHolder holder = null;
            if (convertView == null) {
                convertView = myInflater.inflate(R.layout.msibook_dqaweekly_projectactivity_page2_listview_detial_layout, null);
                holder = new ProjectActivity_jobs_load_listview_detial_adapter.ViewHolder(
                        (TextView) convertView.findViewById(R.id.Text_Dept_1),
                        (TextView) convertView.findViewById(R.id.Text_utilization1)
                );
                convertView.setTag(holder);

            } else {
                holder = (ProjectActivity_jobs_load_listview_detial_adapter.ViewHolder) convertView.getTag();
            }

            ProjectActivity_utilization_listview_detial_Item movie = (ProjectActivity_utilization_listview_detial_Item) getItem(position);
            holder.txtDept1.setText(movie.getDept1());
            holder.txtutilization1.setText(movie.getutilization1());
            holder.txtutilization1.setTextColor(Color.parseColor(setutilizationtextcolor(movie.getutilization1())));

            return convertView;
        }

        public String setutilizationtextcolor(String number) {
            Double afterConvert;
            String color ="";

            try {
                afterConvert = Double.parseDouble(number);
            } catch (Exception e) {
                afterConvert = 0.00;
            }
            if ((afterConvert / 100) > 1) {
                color = "#d21e25";//"#f0625d";
            } else if ((afterConvert / 100) == 0.0) {
                color ="#212121";//"#c6c6c6";
            } else if ((afterConvert / 100) < 0.9) {
                color ="#358900";//"#46aa36";
            } else if ((afterConvert / 100) <= 1 && (afterConvert / 100 >= 0.9)) {
                color = "#212121";//"#656565";
            }

            return  color;
        }
    }

    public class ProjectActivity_jobs_load_listview_detial_recycle_adapter   extends RecyclerView.Adapter<ProjectActivity_jobs_load_listview_detial_recycle_adapter.ViewHolder> {

        private Context mContext;
        private LayoutInflater mInflater;
        private List<ProjectActivity_jobs_load_listview_detial_Item> utilization_list;

        TextView txtDept1;
        TextView txtutilization1;

        public ProjectActivity_jobs_load_listview_detial_recycle_adapter(Context context, List<ProjectActivity_jobs_load_listview_detial_Item> datas) {

            mInflater = LayoutInflater.from(context);
            mContext = context;
            utilization_list = datas;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {


            public ViewHolder(View LayoutView) {
                super(LayoutView);


                txtDept1 = (TextView)LayoutView.findViewById(R.id.Text_Dept_1);

                txtutilization1 = (TextView)LayoutView.findViewById(R.id.Text_utilization1);
            }

        }

        @Override
        public ProjectActivity_jobs_load_listview_detial_recycle_adapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

            View view = mInflater.inflate(R.layout.msibook_dqaweekly_projectactivity_page2_listview_detial_layout,
                    viewGroup, false);

            ProjectActivity_jobs_load_listview_detial_recycle_adapter.ViewHolder viewHolder = new ProjectActivity_jobs_load_listview_detial_recycle_adapter.ViewHolder(view);



            return viewHolder;
        }

        @Override
        public void onBindViewHolder(ProjectActivity_jobs_load_listview_detial_recycle_adapter.ViewHolder holder, int position) {
            ProjectActivity_jobs_load_listview_detial_Item movie = utilization_list.get(position);
            txtDept1.setText(movie.getDept1());
            txtutilization1.setText(movie.getCostRate());
            txtutilization1.setTextColor(Color.parseColor(setutilizationtextcolor(movie.getCostRate())));

        }

        @Override
        public int getItemCount() {
            return utilization_list.size();
        }

        public String setutilizationtextcolor(String number) {
            Double afterConvert;
            String color ="";

            try {
                afterConvert = Double.parseDouble(number);
            } catch (Exception e) {
                afterConvert = 0.00;
            }
            if ((afterConvert / 100) > 1) {
                color = "#d21e25";//"#f0625d";
            } else if ((afterConvert / 100) == 0.0) {
                color ="#212121";//"#c6c6c6";
            } else if ((afterConvert / 100) < 0.9) {
                color ="#358900";//"#46aa36";
            } else if ((afterConvert / 100) <= 1 && (afterConvert / 100 >= 0.9)) {
                color = "#212121";//"#656565";
            }
            return  color;
        }

    }

    //-----------------Item----------------
    public class ProjectActivity_utilization_listview_detial_Item {

        private String Dept1;
        private String utilization1;

        public ProjectActivity_utilization_listview_detial_Item(String Dept1, String utilization1) {

            this.Dept1 = Dept1;
            this.utilization1 = utilization1;

        }

        public String getDept1() {
            return Dept1;
        }

        public String getutilization1() {
            return utilization1;
        }
    }

    //----------------Adapter-------------




}
