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

public class msibook_dqaweekly_project_activity_msr_data_msr_detial_for_K extends AppCompatActivity {

    private Context mContext;

    private ProgressDialog progressBar;

    private String getBU;
    private String getPeople;

    private TextView textView_BU_title;

    private TextView TextView_K330;  //DQA
    private TextView TextView_K331;  //DQA1
    private TextView TextView_K33F;  //DQA2
    private TextView TextView_K33C;  //DQA3
    private TextView TextView_K33G;  //DQA4
    private TextView TextView_K33H;  //DQA5
    private TextView TextView_K33I;  //DQA6
    private TextView TextView_K33J;  //DQA7

    private TextView textView_dpt_330;//DQA
    private TextView textView_non_pro_330;
    private TextView textView_non_write_330;

    private TextView textView_dpt_331;
    private TextView textView_non_pro_331;
    private TextView textView_non_write_331;

    private TextView textView_dpt_F;
    private TextView textView_non_pro_F;
    private TextView textView_non_write_F;

    private TextView textView_dpt_C;
    private TextView textView_non_pro_C;
    private TextView textView_non_write_C;

    private TextView textView_dpt_G;
    private TextView textView_non_pro_G;
    private TextView textView_non_write_G;

    private TextView textView_dpt_H;
    private TextView textView_non_pro_H;
    private TextView textView_non_write_H;

    private TextView textView_dpt_I;
    private TextView textView_non_pro_I;
    private TextView textView_non_write_I;

    private TextView textView_dpt_J;
    private TextView textView_non_pro_J;
    private TextView textView_non_write_J;

    private TextView textView_non_pro_total;
    private TextView textView_non_write_total;

    private Double Non_project_base;
    private Double Percent_330;//DQA
    private Double Percent_331;//DQA1
    private Double Percent_33F;//DQA2
    private Double Percent_33G;//DQA4
    private Double Percent_33H;//DQA5
    private Double Percent_33I;//DQA6
    private Double Percent_33J;//DQA7

    private Double Non_project_total;
    private Double Non_write_total;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msibook_dqaweekly_project_msr_data_msr_detial_for__k);

        //讀取時間Bar
        progressBar = new ProgressDialog(this);
        progressBar.setCancelable(false);
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.setMessage("資料載入中");

        mContext = msibook_dqaweekly_project_activity_msr_data_msr_detial_for_K.this;

        textView_dpt_330 = (TextView) findViewById(R.id.textView_dpt_330);
        textView_non_pro_330 = (TextView) findViewById(R.id.textView_non_pro_330);
        textView_non_write_330 = (TextView) findViewById(R.id.textView_non_write_330);

        textView_dpt_331 = (TextView) findViewById(R.id.textView_dpt_331);
        textView_non_pro_331 = (TextView) findViewById(R.id.textView_non_pro_331);
        textView_non_write_331 = (TextView) findViewById(R.id.textView_non_write_331);
        textView_dpt_F = (TextView) findViewById(R.id.textView_dpt_F);
        textView_non_pro_F = (TextView) findViewById(R.id.textView_non_pro_F);
        textView_non_write_F = (TextView) findViewById(R.id.textView_non_write_F);

        textView_dpt_C = (TextView) findViewById(R.id.textView_dpt_C);
        textView_non_pro_C = (TextView) findViewById(R.id.textView_non_pro_C);
        textView_non_write_C = (TextView) findViewById(R.id.textView_non_write_C);

        textView_dpt_G = (TextView) findViewById(R.id.textView_dpt_G);
        textView_non_pro_G = (TextView) findViewById(R.id.textView_non_pro_G);
        textView_non_write_G = (TextView) findViewById(R.id.textView_non_write_G);

        textView_dpt_H = (TextView) findViewById(R.id.textView_dpt_H);
        textView_non_pro_H = (TextView) findViewById(R.id.textView_non_pro_H);
        textView_non_write_H = (TextView) findViewById(R.id.textView_non_write_H);

        textView_dpt_I = (TextView) findViewById(R.id.textView_dpt_I);
        textView_non_pro_I = (TextView) findViewById(R.id.textView_non_pro_I);
        textView_non_write_I = (TextView) findViewById(R.id.textView_non_write_I);

        textView_dpt_J = (TextView) findViewById(R.id.textView_dpt_J);
        textView_non_pro_J = (TextView) findViewById(R.id.textView_non_pro_J);
        textView_non_write_J = (TextView) findViewById(R.id.textView_non_write_J);

        textView_non_pro_total = (TextView) findViewById(R.id.textView_non_pro_total);
        textView_non_write_total = (TextView) findViewById(R.id.textView_non_write_total);

        textView_BU_title = (TextView) findViewById(R.id.textView_BU_title);

        Percent_330 = 0.0;
        Percent_331 = 0.0;
        Percent_33F= 0.0;
        Percent_33G= 0.0;
        Percent_33H= 0.0;
        Percent_33I= 0.0;
        Percent_33J= 0.0;

        String getEtraWeek = getIntent().getStringExtra("Week");//抓第一頁選的部門代號
        String getEtraYear = getIntent().getStringExtra("Year");//抓第一頁選的部門代號
        String getRegionID = getIntent().getStringExtra("RegionID");//抓第一頁選的部門代號
        getPeople = getIntent().getStringExtra("People");//抓第一頁選的部門代號
        getBU = getIntent().getStringExtra("BU");//抓第一頁選的部門代號
        Log.w("抓到人數",getPeople);
        Log.w("以判斷點的是哪一個Type",getBU);

        textView_BU_title.setText(getBU);

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
                                case "K330":
                                    textView_non_write_330.setText(String.valueOf(Double.valueOf(NonWrite_Total)));
                                    break;
                                case "K331":
                                    textView_non_write_331.setText(String.valueOf(Double.valueOf(NonWrite_Total)));
                                    break;
                                case "K33F":
                                    textView_non_write_F.setText(String.valueOf(Double.valueOf(NonWrite_Total)));
                                    break;
                                case "K33C":
                                    textView_non_write_C.setText(String.valueOf(Double.valueOf(NonWrite_Total)));
                                    break;
                                case "K33G":
                                    textView_non_write_G.setText(String.valueOf(Double.valueOf(NonWrite_Total)));
                                    break;
                                case "K33H":
                                    textView_non_write_H.setText(String.valueOf(Double.valueOf(NonWrite_Total)));
                                    break;
                                case "K33I":
                                    textView_non_write_I.setText(String.valueOf(Double.valueOf(NonWrite_Total)));
                                    break;
                                case "K33J":
                                    textView_non_write_J.setText(String.valueOf(Double.valueOf(NonWrite_Total)));
                                    break;
                            }
                        }

                        Non_project_base = Double.valueOf(getPeople)-Non_write_total;
                        Log.w("非專案基底人數",String.valueOf(Non_project_base));

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

    public void Find_BU_WeeklyReport_Other_Non_Project_ForKS_Count_Total(final String Year, final String Week,final String RegionID) { // 先跑一次服務的加總  才能算出比例

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
                                case "DQA":
                                    Percent_330 =((Double.valueOf(Non_Project)/Non_project_total)*Non_project_base);
                                    textView_non_pro_330.setText(String.format("%.2f",Percent_330));
                                    break;
                                case "DQA1":
                                    Percent_331 = ((Double.valueOf(Non_Project)/Non_project_total)*Non_project_base);
                                    textView_non_pro_331.setText(String.format("%.2f",Percent_331));
                                    break;
                                case "DQA2":
                                    Percent_33F = ((Double.valueOf(Non_Project)/Non_project_total)*Non_project_base);
                                    textView_non_pro_F.setText(String.format("%.2f",Percent_33F));
                                    break;
//                                case "DQA3":
//                                    textView_non_pro_C.setText(Non_Project);
//                                    break;
                                case "DQA4":
                                    Percent_33G = ((Double.valueOf(Non_Project)/Non_project_total)*Non_project_base);
                                    textView_non_pro_G.setText(String.format("%.2f",Percent_33G));
                                    break;
                                case "DQA5":
                                    Percent_33H =((Double.valueOf(Non_Project)/Non_project_total)*Non_project_base);
                                    textView_non_pro_H.setText(String.format("%.2f",Percent_33H));
                                    break;
                                case "DQA6":
                                    Percent_33I =((Double.valueOf(Non_Project)/Non_project_total)*Non_project_base);
                                    textView_non_pro_I.setText(String.format("%.2f",Percent_33I));
                                    break;
                                case "DQA7":
                                    Percent_33J =((Double.valueOf(Non_Project)/Non_project_total)*Non_project_base);
                                    textView_non_pro_J.setText(String.format("%.2f",Percent_33J));
                                    break;
                            }
                        }
                        textView_non_pro_total.setText(String.format("%.2f",Percent_330+Percent_331+Percent_33F+Percent_33G+Percent_33H+Percent_33I+Percent_33J));

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
