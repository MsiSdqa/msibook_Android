package dqa.com.msibook;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class msibook_rss_add extends AppCompatActivity {

    private ProgressDialog pDialog;

    private RequestQueue mQueue;

    private String RSS_ID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msibook_rss_add);
        pDialog = new ProgressDialog(this);

        setTitle("RSS");

        Intent startingIntent = getIntent();
        if (startingIntent != null) {

            Integer Message = startingIntent.getIntExtra("value",1);

            Bundle Bdl =  startingIntent.getExtras();

            String ReportID = Bdl.getString("value");

            if (ReportID != null)
            {
                RSS_ID = ReportID;

                GetReportData(ReportID);
            }
        }

        Button btn_Send = (Button)findViewById(R.id.btn_Send);

        btn_Send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                TextView txt_ModelName = (TextView)findViewById(R.id.txt_ModelName);

                EditText txt_Subject = (EditText)findViewById(R.id.txt_Subject);

                EditText txt_Content = (EditText)findViewById(R.id.txt_Content);

                TextView txt_FileName = (TextView)findViewById(R.id.txt_FileName);

                String Subject = txt_Subject.getText().toString();

                String Comment = txt_Content.getText().toString();

                String WorkHour = "8";

                //Insert_RSS_Report(RSS_ID,WorkHour,Subject,Comment);


            }
        });

    }

    @Override
    public void onNewIntent(Intent intent) {
        Intent startingIntent = getIntent();
        if (startingIntent != null) {

            Integer Message = startingIntent.getIntExtra("value",1);

            Bundle Bdl =  startingIntent.getExtras();

            String ReportID = Bdl.getString("value");

            if (ReportID != null)
            {
                GetReportData(ReportID);
            }
        }
    }

    private  void Insert_RSS_Report(String F_Keyin, String ModelID, String WorkHour, String Subject,String Comment)
    {
        pDialog.setMessage("Login...");

        pDialog.show();

        if (mQueue == null) {
            mQueue = Volley.newRequestQueue(this);
        }

        Map<String, String> map = new HashMap<String, String>();

        map.put("F_Keyin", F_Keyin);
        map.put("ModelID", ModelID);
        map.put("WorkHour", WorkHour);
        map.put("Subject", Subject);
        map.put("Comment", Comment);
        String Path = GetServiceData.ServicePath + "/Insert_RSS_Report_MsiBook";

        GetServiceData.SendPostRequest(Path, mQueue, new GetServiceData.VolleyStringCallback() {
            @Override
            public void onSendRequestSuccess(String result) {

                pDialog.hide();

                final AlertDialog alertDialog = getAlertDialog("工作報告","工作報告新增成功");

                alertDialog.show();
            }

            @Override
            public void onSendRequestError(String result) {

                Log.w("Error",result);

                pDialog.hide();
            }

        }, map);


    }

    private AlertDialog getAlertDialog(String title, String message){
        //產生一個Builder物件
        AlertDialog.Builder builder = new AlertDialog.Builder(msibook_rss_add.this);
        //設定Dialog的標題
        builder.setTitle(title);
        //設定Dialog的內容
        builder.setMessage(message);
        //設定Positive按鈕資料
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Intent intent = new Intent(getBaseContext(), msibook_rss_add.class);

                startActivity(intent);

            }
        });
//        //設定Negative按鈕資料
//        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                //按下按鈕時顯示快顯
//                Toast.makeText(Add_Report.this, "您按下Cancel按鈕", Toast.LENGTH_SHORT).show();
//            }
//        });
        //利用Builder物件建立AlertDialog
        return builder.create();
    }


    private void GetReportData(String Report_ID) {

        pDialog.setMessage("Login...");

        pDialog.show();

        if (mQueue == null) {
            mQueue = Volley.newRequestQueue(this);
        }

        Map<String, String> map = new HashMap<String, String>();
        map.put("F_SeqNo", Report_ID);

        String Path = "http://wtsc.msi.com.tw/IMS/RSS_App_Service.asmx/Find_Report_Content";

        GetServiceData.SendPostRequest(Path, mQueue, new GetServiceData.VolleyStringCallback() {

            @Override
            public void onSendRequestSuccess(String result) {

                try
                {
                    JSONObject ReportObject = new JSONObject(result);

                    JSONArray UserArray = new JSONArray(ReportObject.getString("Key"));

                    if (UserArray.length() > 0) {

                        String F_SeqNo = String.valueOf(UserArray.getJSONObject(0).getInt("F_SeqNo"));

                        String ModelName = UserArray.getJSONObject(0).getString("ModelName");

                        String F_WorkHour = String.valueOf(UserArray.getJSONObject(0).getDouble("F_WorkHour"));

                        String FileName = UserArray.getJSONObject(0).getString("FileName");

                        String F_Subject = UserArray.getJSONObject(0).getString("F_Subject");

                        String F_Comments = UserArray.getJSONObject(0).getString("F_Comments");

                        TextView txt_ModelName = (TextView)findViewById(R.id.txt_ModelName);

                        EditText txt_Subject = (EditText)findViewById(R.id.txt_Subject);

                        EditText txt_Content = (EditText)findViewById(R.id.txt_Content);

                        TextView txt_FileName = (TextView)findViewById(R.id.txt_FileName);

                        TextView txt_Hour = (TextView)findViewById(R.id.txt_Hour);

                        txt_ModelName.setText(ModelName);

                        txt_Subject.setText(F_Subject);

                        txt_Content.setText(F_Comments);

                        txt_FileName.setText(FileName);

                        txt_Hour.setText(F_WorkHour);

                        Log.w("F_Subject",F_Subject);

                        Log.w("F_Comments",F_Comments);
                    }

                }
                catch ( JSONException ex)
                {
                    Toast.makeText(msibook_rss_add.this, ex.getMessage(), Toast.LENGTH_SHORT).show();
                }

                pDialog.hide();

            }

            @Override
            public void onSendRequestError(String result) {
                pDialog.hide();
            }

        }, map);


    }
}
