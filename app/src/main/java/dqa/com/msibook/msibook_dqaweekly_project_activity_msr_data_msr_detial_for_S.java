package dqa.com.msibook;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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

public class msibook_dqaweekly_project_activity_msr_data_msr_detial_for_S extends AppCompatActivity {

    private Context mContext;

    private ProgressDialog progressBar;

    private String getBU;
    private String getPeople;

    private TextView textView_BU_title;


    private TextView textView_dpt_352;
    private TextView textView_non_pro_352;
    private TextView textView_non_write_352;

    private TextView textView_dpt_356;
    private TextView textView_non_pro_356;
    private TextView textView_non_write_356;

    private TextView textView_dpt_357;
    private TextView textView_non_pro_357;
    private TextView textView_non_write_357;

    private TextView textView_dpt_358;
    private TextView textView_non_pro_358;
    private TextView textView_non_write_358;

    private TextView textView_dpt_355;
    private TextView textView_non_pro_355;
    private TextView textView_non_write_355;

    private TextView textView_dpt_359;
    private TextView textView_non_pro_359;
    private TextView textView_non_write_359;

    private TextView textView_non_pro_total;
    private TextView textView_non_write_total;

    private Double Non_project_base;
    private Double Percent_C352;//DQA
    private Double Percent_C356;//DQA1
    private Double Percent_C357;//DQA2
    private Double Percent_C358;//DQA4
    private Double Percent_C355;//DQA5
    private Double Percent_C359;//DQA6

    private Double Non_project_total;
    private Double Non_write_total;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msibook_dqaweekly_project_msr_data_msr_detial_for__s);

        //讀取時間Bar
        progressBar = new ProgressDialog(this);
        progressBar.setCancelable(false);
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.setMessage("資料載入中");

        mContext = msibook_dqaweekly_project_activity_msr_data_msr_detial_for_S.this;

        textView_dpt_352 = (TextView) findViewById(R.id.textView_dpt_352);
        textView_non_pro_352 = (TextView) findViewById(R.id.textView_non_pro_352);
        textView_non_write_352 = (TextView) findViewById(R.id.textView_non_write_352);

        textView_dpt_356 = (TextView) findViewById(R.id.textView_dpt_356);
        textView_non_pro_356 = (TextView) findViewById(R.id.textView_non_pro_356);
        textView_non_write_356 = (TextView) findViewById(R.id.textView_non_write_356);

        textView_dpt_357 = (TextView) findViewById(R.id.textView_dpt_357);
        textView_non_pro_357 = (TextView) findViewById(R.id.textView_non_pro_357);
        textView_non_write_357 = (TextView) findViewById(R.id.textView_non_write_357);

        textView_dpt_358 = (TextView) findViewById(R.id.textView_dpt_358);
        textView_non_pro_358 = (TextView) findViewById(R.id.textView_non_pro_358);
        textView_non_write_358 = (TextView) findViewById(R.id.textView_non_write_358);

        textView_dpt_355 = (TextView) findViewById(R.id.textView_dpt_355);
        textView_non_pro_355 = (TextView) findViewById(R.id.textView_non_pro_355);
        textView_non_write_355 = (TextView) findViewById(R.id.textView_non_write_355);

        textView_dpt_359 = (TextView) findViewById(R.id.textView_dpt_359);
        textView_non_pro_359 = (TextView) findViewById(R.id.textView_non_pro_359);
        textView_non_write_359 = (TextView) findViewById(R.id.textView_non_write_359);

        textView_non_pro_total = (TextView) findViewById(R.id.textView_non_pro_total);
        textView_non_write_total = (TextView) findViewById(R.id.textView_non_write_total);

        textView_BU_title = (TextView) findViewById(R.id.textView_BU_title);

        String getEtraWeek = getIntent().getStringExtra("Week");//抓第一頁選的部門代號
        String getEtraYear = getIntent().getStringExtra("Year");//抓第一頁選的部門代號
        String getRegionID = getIntent().getStringExtra("RegionID");//抓第一頁選的部門代號
        getPeople = getIntent().getStringExtra("People");//抓第一頁選的部門代號
        getBU = getIntent().getStringExtra("BU");//抓第一頁選的部門代號
        Log.w("抓到人數",getPeople);
        Log.w("以判斷點的是哪一個Type",getBU);

        textView_BU_title.setText(getBU);

        Percent_C352 = 0.0;
        Percent_C356 = 0.0;
        Percent_C357= 0.0;
        Percent_C358= 0.0;
        Percent_C355= 0.0;
        Percent_C359= 0.0;

        Non_project_base = 0.0;

        Find_BU_WeeklyReport_Other_Non_Wirte_ForKS(getEtraYear,getEtraWeek,getRegionID);

    }

    public void Find_BU_WeeklyReport_Other_Non_Wirte_ForKS(final String Year, final String Week,final String RegionID) { //昆山 寶安 未填寫

        //顯示  讀取等待時間Bar
        progressBar.show();

        //msr_detial_Item_Other.clear();

        Non_write_total = 0.0;

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

                            Non_write_total += Double.valueOf(NonWrite_Total);

                            switch(Dept) {
                                case "C352":
                                    textView_non_write_352.setText(String.valueOf(Double.valueOf(NonWrite_Total)));
                                    break;
                                case "C356":
                                    textView_non_write_356.setText(String.valueOf(Double.valueOf(NonWrite_Total)));
                                    break;
                                case "C357":
                                    textView_non_write_357.setText(String.valueOf(Double.valueOf(NonWrite_Total)));
                                    break;
                                case "C358":
                                    textView_non_write_358.setText(String.valueOf(Double.valueOf(NonWrite_Total)));
                                    break;
                                case "C355":
                                    textView_non_write_355.setText(String.valueOf(Double.valueOf(NonWrite_Total)));
                                    break;
                                case "C359":
                                    textView_non_write_359.setText(String.valueOf(Double.valueOf(NonWrite_Total)));
                                    break;
                            }
                        }
                        Non_project_base = Double.valueOf(getPeople)-Non_write_total;
                        textView_non_write_total.setText(String.valueOf(Non_write_total));
                    }
                    else
                    {
                    }
                    Find_BU_WeeklyReport_Other_Non_Project_ForKS_Count_Total(Year,Week,RegionID);

                } catch (JSONException ex) {
                    Log.w("稼動綠測試Error","");
                }
            }
        });
    }

    public void Find_BU_WeeklyReport_Other_Non_Project_ForKS_Count_Total(final String Year, final String Week,final String RegionID) { // 昆山 寶安  非專案  新版本

        Non_project_total=0.0;

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

                            Non_project_total += Double.valueOf(Non_Project);
                        }

                        //textView_non_pro_total.setText(String.valueOf(Non_project_total));

                    }
                    else
                    {
                    }
                    Find_BU_WeeklyReport_Other_Non_Project_ForKS(Year,Week,RegionID);

                } catch (JSONException ex) {
                    Log.w("稼動綠測試Error","");
                }

            }
        });

    }

    public void Find_BU_WeeklyReport_Other_Non_Project_ForKS(final String Year, final String Week,final String RegionID) { // 昆山 寶安  非專案  新版本

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

                            switch(F_Orgid) {
                                case "DQA1":
                                    Percent_C352 =((Double.valueOf(Non_Project)/Non_project_total)*Non_project_base);
                                    textView_non_pro_352.setText(String.format("%.2f",Percent_C352));
                                    break;
                                case "DQA2":
                                    Percent_C356 =((Double.valueOf(Non_Project)/Non_project_total)*Non_project_base);
                                    textView_non_pro_356.setText(String.format("%.2f",Percent_C356));
                                    break;
                                case "DQA3":
                                    Percent_C357 =((Double.valueOf(Non_Project)/Non_project_total)*Non_project_base);
                                    textView_non_pro_357.setText(String.format("%.2f",Percent_C357));
                                    break;
                                case "DQA4":
                                    Percent_C358 =((Double.valueOf(Non_Project)/Non_project_total)*Non_project_base);
                                    textView_non_pro_358.setText(String.format("%.2f",Percent_C358));
                                    break;
                                case "IRT1":
                                    Percent_C355 =((Double.valueOf(Non_Project)/Non_project_total)*Non_project_base);
                                    textView_non_pro_355.setText(String.format("%.2f",Percent_C355));
                                    break;
                                case "IRT2":
                                    Percent_C359 =((Double.valueOf(Non_Project)/Non_project_total)*Non_project_base);
                                    textView_non_pro_359.setText(String.format("%.2f",Percent_C359));
                                    break;
                            }

                        }

                        textView_non_pro_total.setText(String.format("%.2f",Percent_C352+Percent_C356+Percent_C357+Percent_C358+Percent_C355+Percent_C359));

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

}
