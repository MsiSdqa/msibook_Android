package dqa.com.msibook;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
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

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class msibook_requset_form_chart_detail extends AppCompatActivity {

    private Context mContext;
    private ProgressDialog progressBar;

    private Statistics_manAdapter mStatistics_manAdapter;
    private List<Statistics_manAdapter_Item> Statistics_manAdapter_Item_List = new ArrayList<Statistics_manAdapter_Item>();

    private ListView mListView;

    private TextView textView_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msibook_requset_form_chart_detail);
        mContext = this;

        progressBar = new ProgressDialog(this);
        progressBar.setCancelable(true);
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.setMessage("資料載入中");

        String getTitle = getIntent().getStringExtra("F_Name");//抓第一頁 實際出勤

        String getF_ID = getIntent().getStringExtra("F_ID");//抓第一頁 實際出勤

        textView_title = (TextView) findViewById(R.id.textView_title);

        textView_title.setText(getTitle);

        RequestForm_IPS_Statistics_list(getF_ID);

    }

    // IPS統計數據
    public void RequestForm_IPS_Statistics_list(String F_RequestType) {

        //顯示  讀取等待時間Bar
        progressBar.show();

        RequestQueue mQueue = Volley.newRequestQueue(this);

        Statistics_manAdapter_Item_List.clear();

        getString("http://wtsc.msi.com.tw/IMS/MsiBook_App_Service.asmx/RequestForm_IPS_Statistics_list?F_RequestType=" + F_RequestType, mQueue, new GetServiceData.VolleyCallback() {
            @Override
            public void onSuccess(JSONObject result) {

                try {

                    JSONArray UserArray = new JSONArray(result.getString("Key"));

                    if (UserArray.length() > 0) {
                        for (int i = 0; i < UserArray.length(); i++) {
                            JSONObject IssueData = UserArray.getJSONObject(i);

                            String Column1 = IssueData.getString("Column1");// ID

                            String Column2 = IssueData.getString("Column2");//專案

                            String Status = IssueData.getString("Status");//人力

                            Statistics_manAdapter_Item_List.add(i, new Statistics_manAdapter_Item(Column1, Column2,Status));

                            //下方List使用
                        }

                        mListView = (ListView) findViewById(R.id.listview_statistics);

                        mStatistics_manAdapter = new Statistics_manAdapter(mContext, Statistics_manAdapter_Item_List);

                        mListView.setAdapter(mStatistics_manAdapter);

                        }else{
                        mListView.setVisibility(View.GONE);
                        mListView.setEmptyView(findViewById(R.id.empty2));
                    }
                    //關閉-讀取等待時間Bar
                    progressBar.dismiss();

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

    //-------------------------Item-------------------------
    public class Statistics_manAdapter_Item {

        String Column1;

        String Column2;

        String Status;

        public Statistics_manAdapter_Item(String Column1,String Column2,String Status)
        {
            this.Column1 = Column1;

            this.Column2 = Column2;

            this.Status = Status;
        }

        public String GetColumn1()
        {
            return this.Column1;
        }

        public String GetColumn2()
        {
            return this.Column2;
        }

        public String GetStatus()
        {
            return this.Status;
        }

    }

    //-----------------------Adapter-----------------------
    public class Statistics_manAdapter extends BaseAdapter {

        private LayoutInflater mLayInf;

        private List<Statistics_manAdapter_Item> Statistics_manAdapter_Item_List;

        private Context Project_cnt_man_Context;

        private String Title;

        public Statistics_manAdapter(Context context,  List<Statistics_manAdapter_Item> Statistics_manAdapter_Item_List)
        {
            mLayInf = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            Project_cnt_man_Context = context;

            this.Title = Title;

            this.Statistics_manAdapter_Item_List = Statistics_manAdapter_Item_List;

        }
        @Override
        public int getCount() {
            return Statistics_manAdapter_Item_List.size();
        }

        @Override
        public Object getItem(int position) {
            return Statistics_manAdapter_Item_List.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View v = new View(Project_cnt_man_Context);

            v = mLayInf.inflate(R.layout.msibook_requset_form_chart_detail_adapter, parent, false);

            TextView textView_Column1 = (TextView) v.findViewById(R.id.textView_Column1);
            TextView textView_Column2 = (TextView) v.findViewById(R.id.textView_Column2);
            TextView textView_Status = (TextView) v.findViewById(R.id.textView_Status);

            textView_Column1.setText("MS-"+Statistics_manAdapter_Item_List.get(position).GetColumn1());
            textView_Column2.setText(Statistics_manAdapter_Item_List.get(position).GetColumn2());
            textView_Status.setText(Statistics_manAdapter_Item_List.get(position).GetStatus());

            return v;
        }

    }



}
