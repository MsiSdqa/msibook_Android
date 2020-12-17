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
import android.view.WindowManager;
import android.widget.LinearLayout;
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
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class msibook_dqaweekly_msr_bu_info extends AppCompatActivity {

    private Context mContext;
    private RecyclerView mRecycleView;

    private ProjectActivity_msr_detial_adapter_Other adapter_other;
    private List<ProjectActivity_msr_detial_Item_Other> msr_detial_Item_Other = new ArrayList<ProjectActivity_msr_detial_Item_Other>();

    private ProgressDialog progressBar;

    private LinearLayout linear_CPS;
    private LinearLayout linear_IPS;

    private TextView textView_CPS_number;
    private TextView textView_IPS_number;

    private String SetDept;

    private TextView textView_msr_bydept_total;
    private Double msr_bydept_total;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msibook_dqaweekly_msr_bu_info);

        mContext = msibook_dqaweekly_msr_bu_info.this;

        //讀取時間Bar
        progressBar = new ProgressDialog(this);
        progressBar.setCancelable(false);
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.setMessage("資料載入中");

//        //設定顯示座標位置
//        String x_Location = getIntent().getStringExtra("x_Location");
//        String y_Location = getIntent().getStringExtra("y_Location");
//
//        WindowManager.LayoutParams lp = this.getWindow().getAttributes();
//        lp.x=Integer.parseInt(x_Location);lp.y=Integer.parseInt(y_Location)-700;
//
//        this.setContentView(textEntryView, lp);

//        linear_CPS = (LinearLayout) findViewById(R.id.linear_CPS);
//        linear_IPS = (LinearLayout) findViewById(R.id.linear_IPS);
//
//        textView_CPS_number = (TextView) findViewById(R.id.textView_CPS_number);
//        textView_IPS_number = (TextView) findViewById(R.id.textView_IPS_number);

        mRecycleView = (RecyclerView) findViewById(R.id.msr_detial_Recycleview);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecycleView.setLayoutManager(layoutManager);

        textView_msr_bydept_total = (TextView) findViewById(R.id.textView_msr_bydept_total);

        String Year = getIntent().getStringExtra("Year");

        String Week = getIntent().getStringExtra("Week");

        String Title = getIntent().getStringExtra("Title");

        SetDept = Title.substring(0,4);

        final String Month;
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, Integer.parseInt(Year));
        cal.set(Calendar.WEEK_OF_YEAR, Integer.parseInt(Week.replace("週","")));
        int start = cal.get(Calendar.MONTH);
        Log.w("換算出是第幾月-預設從0開始",String.valueOf(start));
        if(start==0){
            Month="12";
            //textView_month.setText(Month);
            Year = String.valueOf(Integer.valueOf(Year)-1);
            Find_Mothly_MSR_byDept(Year,Month,SetDept);
        }else{
            Month = String.valueOf(start);
            //textView_month.setText(Month);
            Find_Mothly_MSR_byDept(Year,Month,SetDept);
        }

    }


    public void Find_Mothly_MSR_byDept(final String Year, final String Month,final String Dept) { //舊 Other  昆、寶地區

        msr_bydept_total = 0.00;

        progressBar.show();

        msr_detial_Item_Other.clear();

        RequestQueue mQueue = Volley.newRequestQueue(this);

        getString("http://wtsc.msi.com.tw/IMS/Weekly_Report.asmx/Find_Mothly_MSR_byDept?Year=" + Year + "&Month=" + Month + "&Dept=" + Dept, mQueue, new GetServiceData.VolleyCallback() {
            @Override
            public void onSuccess(JSONObject result) {

                try {
                    JSONArray UserArray = new JSONArray(result.getString("Key"));
                    if (UserArray.length() > 0)
                    {

                        for (int i = 0; i < UserArray.length(); i++) {

                            JSONObject IssueData = UserArray.getJSONObject(i);

                            String F_MSR_Type = String.valueOf(IssueData.getString("F_MSR_Type"));

                            String Column1 = String.valueOf(IssueData.getDouble("Column1"));

                            msr_detial_Item_Other.add(i,new ProjectActivity_msr_detial_Item_Other(F_MSR_Type,Column1));

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

        private String F_MSR_Type;
        private String Column1;


        public ProjectActivity_msr_detial_Item_Other(String F_MSR_Type, String Column1) {

            this.F_MSR_Type = F_MSR_Type;
            this.Column1 = Column1;

        }

        public String GetF_MSR_Type() {
            return F_MSR_Type;
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

            textView_BU.setText(movie.GetF_MSR_Type());
            textView_project_rate.setText(movie.GetColumn1());
        }
        @Override
        public int getItemCount() {
            return msr_detial_list.size();
        }
    }



}
