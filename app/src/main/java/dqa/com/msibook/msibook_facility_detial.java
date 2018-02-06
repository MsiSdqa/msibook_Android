package dqa.com.msibook;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

public class msibook_facility_detial extends AppCompatActivity {

    private ProgressDialog progressBar;
    private Context mContext;

    private TextView textView_title2;
    private TextView txt_AssetSN;
    private TextView txt_F_Type;
    private TextView txt_F_Location;
    private TextView txt_F_Dept;
    private TextView txt_F_Owner;
    private TextView txt_F_Cost;
    private TextView txt_F_Buy_Date;
    private TextView txt_F_Storage_Date;
    private TextView txt_F_Use_Year;
    private TextView txt_F_Facility_en;
    private TextView txt_F_Facility_cn;
    private TextView txt_F_Facility;
    private TextView txt_F_Model;
    private TextView txt_F_Factory;
    private TextView txt_F_Spec;
    private TextView txt_F_Standard;


    private String F_SeqNo;
    private String F_Facility;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msibook_facility_detial);

        progressBar = new ProgressDialog(this);
        progressBar.setCancelable(true);
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.setMessage("資料載入中");
        mContext = msibook_facility_detial.this;

        F_SeqNo = getIntent().getStringExtra("F_SeqNo");
        F_Facility = getIntent().getStringExtra("F_Facility");

        textView_title2 = (TextView) findViewById(R.id.textView_title2);
        txt_AssetSN = (TextView) findViewById(R.id.txt_AssetSN);
        txt_F_Type = (TextView) findViewById(R.id.txt_F_Type);
        txt_F_Location = (TextView) findViewById(R.id.txt_F_Location);
        txt_F_Dept = (TextView) findViewById(R.id.txt_F_Dept);
        txt_F_Owner = (TextView) findViewById(R.id.txt_F_Owner);
        txt_F_Cost = (TextView) findViewById(R.id.txt_F_Cost);
        txt_F_Buy_Date = (TextView) findViewById(R.id.txt_F_Buy_Date);
        txt_F_Storage_Date = (TextView) findViewById(R.id.txt_F_Storage_Date);
        txt_F_Use_Year = (TextView) findViewById(R.id.txt_F_Use_Year);
        txt_F_Facility_en = (TextView) findViewById(R.id.txt_F_Facility_en);
        txt_F_Facility_cn = (TextView) findViewById(R.id.txt_F_Facility_cn);
        txt_F_Facility = (TextView) findViewById(R.id.txt_F_Facility);
        txt_F_Model = (TextView) findViewById(R.id.txt_F_Model);
        txt_F_Factory = (TextView) findViewById(R.id.txt_F_Factory);
        txt_F_Spec = (TextView) findViewById(R.id.txt_F_Spec);
        txt_F_Standard = (TextView) findViewById(R.id.txt_F_Standard);

        textView_title2.setText(F_Facility);
        Find_Fac_Detail(F_SeqNo);
    }

    public static String getDateTimeFormat(String dateTime, String oldFmt, String newFmt, Locale area) {
        java.text.SimpleDateFormat fmt = new java.text.SimpleDateFormat(oldFmt, area);
        java.util.Date date;
        try {
            date = fmt.parse(dateTime);
            fmt.applyPattern(newFmt);

            return fmt.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateTime;//轉換產生錯誤時，回傳原始的日期時間
    }

    private void Find_Fac_Detail(String F_SeqNo) {

        //顯示  讀取等待時間Bar
        progressBar.show();

        RequestQueue mQueue = Volley.newRequestQueue(this);

        String Path = GetServiceData.ServicePath + "/Find_Fac_Detail?F_SeqNo=" + F_SeqNo;

        GetServiceData.getString(Path, mQueue, new GetServiceData.VolleyCallback() {
            @Override
            public void onSuccess(JSONObject result) {

                try {

                    JSONArray UserArray = new JSONArray(result.getString("Key"));

                    if (UserArray.length() > 0) {
                        JSONObject IssueData = UserArray.getJSONObject(0);

                        NumberFormat nf = NumberFormat.getInstance();//設定三位數一逗點格式方法

                        String F_SeqNo = String.valueOf(IssueData.getInt("F_SeqNo"));
                        String F_AssetNo = String.valueOf(IssueData.getString("F_AssetNo"));
                        String F_Type = String.valueOf(IssueData.getString("F_Type"));
                        String F_Location = String.valueOf(IssueData.getString("F_Location"));
                        String F_Dept = String.valueOf(IssueData.getString("F_Dept"));
                        String F_LocationID = String.valueOf(IssueData.getString("F_LocationID"));
                        String F_Facility = String.valueOf(IssueData.getString("F_Facility"));
                        String F_Facility_cn = String.valueOf(IssueData.getString("F_Facility_cn"));
                        String F_Facility_en = String.valueOf(IssueData.getString("F_Facility_en"));
                        String F_Model = String.valueOf(IssueData.getString("F_Model"));
                        String F_Factory = String.valueOf(IssueData.getString("F_Factory"));
                        String F_Keeper = String.valueOf(IssueData.getString("F_Keeper"));
                        String TEL = String.valueOf(IssueData.getString("TEL"));
                        String F_Keyin = String.valueOf(IssueData.getString("F_Keyin"));
                        String F_Owner = String.valueOf(IssueData.getString("F_Owner"));
                        String F_Purpose = String.valueOf(IssueData.getString("F_Purpose"));
                        String F_Introduction = String.valueOf(IssueData.getString("F_Introduction"));
                        String F_Spec = String.valueOf(IssueData.getString("F_Spec"));
                        String F_Remark = String.valueOf(IssueData.getString("F_Remark"));
                        Integer F_Cost = Integer.valueOf(IssueData.getInt("F_Cost"));
                        String F_Storage_Date = String.valueOf(IssueData.getString("F_Storage_Date"));
                        String F_Buy_Date = String.valueOf(IssueData.getString("F_Buy_Date"));
                        String F_Use_Year = String.valueOf(IssueData.getInt("F_Use_Year"));
                        String F_Standard = String.valueOf(IssueData.getString("F_Standard"));
                        String F_Status = String.valueOf(IssueData.getString("F_Status"));
                        String F_Is_Restrict = String.valueOf(IssueData.getString("F_Is_Restrict"));


                        txt_AssetSN.setText("財編 : " + F_AssetNo);
                        txt_F_Type.setText(F_Type);
                        txt_F_Location.setText(F_Location);
                        txt_F_Dept.setText(F_Dept);
                        txt_F_Owner.setText(F_Owner);
                        txt_F_Cost.setText(nf.format(F_Cost) +" NTD");

                        String Buy_Date = getDateTimeFormat(F_Storage_Date, "yyyy-MM-dd'T'hh:mm:ss", "yyyy-MM-dd", java.util.Locale.US);
                        txt_F_Buy_Date.setText(Buy_Date);

                        String Storage_Date = getDateTimeFormat(F_Storage_Date, "yyyy-MM-dd'T'hh:mm:ss", "yyyy-MM-dd", java.util.Locale.US);
                        txt_F_Storage_Date.setText(Storage_Date);
                        txt_F_Use_Year.setText(F_Use_Year + " 年");
                        txt_F_Facility_en.setText(F_Facility_en);
                        txt_F_Facility_cn.setText(F_Facility_cn);
                        txt_F_Facility.setText(F_Facility);
                        txt_F_Model.setText(F_Model);
                        txt_F_Factory.setText(F_Factory);
                        txt_F_Spec.setText(F_Spec);
                        txt_F_Standard.setText(F_Standard);

                    }

                    progressBar.dismiss();
                } catch (JSONException ex) {

                    Log.w("Json", ex.toString());

                }

            }
        });


    }
}
