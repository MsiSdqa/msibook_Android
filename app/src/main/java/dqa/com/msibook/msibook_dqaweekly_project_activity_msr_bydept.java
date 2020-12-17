package dqa.com.msibook;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class msibook_dqaweekly_project_activity_msr_bydept extends AppCompatActivity {

    private Context mContext;
    private RecyclerView mRecycleView;

    private ProjectActivity_msr_detial_adapter_Other adapter_other;
    private List<ProjectActivity_msr_detial_Item_Other> msr_detial_Item_Other = new ArrayList<ProjectActivity_msr_detial_Item_Other>();

    private ProgressDialog progressBar;

    private TextView textView_BU_title;
    private TextView textView_Lob;

    private String getBU;
    private String getPeople;

    private Double msr_bydept_total;


    private TextView textView_msr_bydept_total;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msibook_dqaweekly_project_msr_bydept);

        //讀取時間Bar
        progressBar = new ProgressDialog(this);
        progressBar.setCancelable(false);
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.setMessage("資料載入中");

        mContext = msibook_dqaweekly_project_activity_msr_bydept.this;

        mRecycleView = (RecyclerView) findViewById(R.id.msr_detial_Recycleview);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecycleView.setLayoutManager(layoutManager);

        textView_msr_bydept_total = (TextView) findViewById(R.id.textView_msr_bydept_total);
        textView_BU_title = (TextView) findViewById(R.id.textView_BU_title);

        String getEtraWeek = getIntent().getStringExtra("Week");//抓第一頁選的部門代號
        String getEtraYear = getIntent().getStringExtra("Year");//抓第一頁選的部門代號
        String getMonth = getIntent().getStringExtra("Month");//抓第一頁選的部門代號
        String getRegionID = getIntent().getStringExtra("RegionID");//抓第一頁選的部門代號
        getBU = getIntent().getStringExtra("BU");//抓第一頁選的部門代號

        Log.w("以判斷點的是哪一個Type",getBU);

        textView_BU_title.setText(getBU);

        Find_Mothly_MSR_Detail_byDept(getEtraYear,getMonth,getBU);

    }

    public void Find_Mothly_MSR_Detail_byDept(final String Year, final String Month,final String Bu) { //舊 Other  昆、寶地區

        msr_bydept_total = 0.0;

        progressBar.show();

        msr_detial_Item_Other.clear();

        RequestQueue mQueue = Volley.newRequestQueue(this);

        getString("http://wtsc.msi.com.tw/IMS/Weekly_Report.asmx/Find_Mothly_MSR_Detail_byDept?Year=" + Year + "&Month=" + Month + "&Bu=" + Bu, mQueue, new GetServiceData.VolleyCallback() {
            @Override
            public void onSuccess(JSONObject result) {

                try {
                    JSONArray UserArray = new JSONArray(result.getString("Key"));
                    if (UserArray.length() > 0)
                    {

                        for (int i = 0; i < UserArray.length(); i++) {

                            JSONObject IssueData = UserArray.getJSONObject(i);

                            String F_Dept = String.valueOf(IssueData.getString("F_Dept"));

                            String Column1 = String.valueOf(IssueData.getDouble("Column1"));

                            msr_detial_Item_Other.add(i,new ProjectActivity_msr_detial_Item_Other(F_Dept,Column1));

                            msr_bydept_total += Double.valueOf(Column1);

                        }

                        adapter_other = new ProjectActivity_msr_detial_adapter_Other(mContext,msr_detial_Item_Other);
                        mRecycleView.setAdapter(adapter_other);

                    }
                    else
                    {
                    }
                    textView_msr_bydept_total.setText(String.valueOf(msr_bydept_total));


                } catch (JSONException ex) {
                    Log.w("稼動綠測試Error","");
                }

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

    //-----------------Item--------------
    public class ProjectActivity_msr_detial_Item_Other {

        private String F_Dept;
        private String Column1;


        public ProjectActivity_msr_detial_Item_Other(String F_Dept, String Column1) {

            this.F_Dept = F_Dept;
            this.Column1 = Column1;

        }

        public String GetF_Dept() {
            return F_Dept;
        }

        public String GetColumn1() {
            return Column1;
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

            View view = mInflater.inflate(R.layout.msibook_dqaweekly_projectactivity_msr_bydept_item,
                    viewGroup, false);

            ProjectActivity_msr_detial_adapter_Other.ViewHolder viewHolder = new ProjectActivity_msr_detial_adapter_Other.ViewHolder(view);

            return viewHolder;
        }

        @Override
        public void onBindViewHolder(ProjectActivity_msr_detial_adapter_Other.ViewHolder holder, int position) {

            ProjectActivity_msr_detial_Item_Other movie = msr_detial_list.get(position);

            textView_BU.setText(movie.GetF_Dept());
            textView_project_rate.setText(movie.GetColumn1());
        }
        @Override
        public int getItemCount() {
            return msr_detial_list.size();
        }
    }




}
