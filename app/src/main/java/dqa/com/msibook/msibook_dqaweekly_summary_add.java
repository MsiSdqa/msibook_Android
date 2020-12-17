package dqa.com.msibook;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class msibook_dqaweekly_summary_add extends AppCompatActivity {

    private TextView textView_title;
    private Button btn_closepop;
    private Button btn_save_summary;
    private EditText txt_summary_content;
    private String Dpt_name;
    private String Dpt_id;

    private String GetKey_Manager;
    private String GetBigTitle;
    private String GetType;
    private String GetYear;
    private String GetWeek;
    private String GetDeptID;
    private String GetWorkID;

    private Context mContext;


    public static void SendPostRequest(String Url, RequestQueue mQueue, final GetServiceData.VolleyStringCallback callback, final Map<String, String> map) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                callback.onSendRequestSuccess(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                callback.onSendRequestError(error.getMessage());

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String, String> map = new HashMap<String, String>();
//                map.put("params1", "value1");
//                map.put("params2", "value2");
                return map;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        mQueue.add(stringRequest);
    }

    //新增摘要
    private void Insert_Weekly_Content_Summary(String Year,String Unit,String F_DeptID,String F_Content,String F_Type) {

        RequestQueue mQueue = Volley.newRequestQueue(this);

        Map<String, String> map = new HashMap<String, String>();
        map.put("Year", Year);
        map.put("Unit", Unit);
        map.put("F_DeptID", F_DeptID);
        map.put("F_Content", F_Content);
        map.put("F_Type", F_Type);

        Log.w("Year Year Year",Year);
        Log.w("Unit Unit Unit",Unit);
        Log.w("DeptID DeptID DeptID",F_DeptID);
        Log.w("ContentContent Content",F_Content);
        Log.w("F_Type F_Type",F_Type);

        if (mQueue == null) {
            mQueue = Volley.newRequestQueue(this);
        }

        String Path = "http://wtsc.msi.com.tw/IMS/Weekly_Report.asmx/Insert_Weekly_Content_Summary";

        SendPostRequest(Path, mQueue, new GetServiceData.VolleyStringCallback() {
            @Override
            public void onSendRequestSuccess(String result) {

//                Integer SummaryNumber =  Integer.valueOf(main_summary_number.getText().toString());
//
//                main_summary_number.setText(String.valueOf((SummaryNumber +1)));

            }

            @Override
            public void onSendRequestError(String result) {



            }

        }, map);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msibook_dqaweekly_summary_add);

        mContext = this;

        btn_closepop = (Button) findViewById(R.id.btn_closepop);//關閉視窗
        btn_save_summary = (Button) findViewById(R.id.btn_save_summary);//關閉視窗
        txt_summary_content = (EditText) findViewById(R.id.txt_summary_content);
        textView_title = (TextView) findViewById(R.id.textView_title);

        GetBigTitle = getIntent().getStringExtra("BigTitle");//抓部門
        GetKey_Manager = getIntent().getStringExtra("Key_Manager");//部門主管
        Dpt_name = getIntent().getStringExtra("Title");//抓部門
        GetType = getIntent().getStringExtra("Type");//抓週
        GetYear = getIntent().getStringExtra("Year");//抓週
        GetWeek =  getIntent().getStringExtra("Week").replace("週","");//抓週
        GetDeptID =  getIntent().getStringExtra("DeptID");//抓部門ID
        GetWorkID =  getIntent().getStringExtra("WorkID");//抓工號

        textView_title.setText(GetBigTitle);
        if(GetType.equals("1")){
            txt_summary_content.setHint("請輸入摘要資訊");
        }else if(GetType.equals("2")){
            txt_summary_content.setHint("請輸入Action Item資訊");
        }

//        DisplayMetrics metrics = getResources().getDisplayMetrics();
//
//        int screenWidth = (int) (metrics.widthPixels * 0.8);
//
//        int screenHeight = (int) (metrics.heightPixels * 0.78);
//
//        getWindow().setLayout(screenWidth, screenHeight);

        //關閉 回前頁面
        btn_closepop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //儲存 點擊
        btn_save_summary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //判斷如果是1就加摘要 如果是 2 就加Action Item
                if(GetType.equals("1")){
                    popupchecksent_summary();
                }else if(GetType.equals("2")){
                    popupchecksent_action_item();
                }

            }
        });

    }

    private void popupchecksent_summary(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("確定是否儲存!?"+"\n")
                .setPositiveButton("儲存", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        String Content = txt_summary_content.getText().toString();

                        Toast.makeText(msibook_dqaweekly_summary_add.this, "資料已更新", Toast.LENGTH_SHORT).show();

                        Insert_Weekly_Content_Summary(GetYear,GetWeek.replace("週",""),GetDeptID,Content,"1");

                        Bundle bundle = new Bundle();
                        bundle.putString("Type", "1");
                        msibook_dqaweekly_summary_add.this.setResult(RESULT_OK,msibook_dqaweekly_summary_add.this.getIntent().putExtras(bundle));
                        finish();

                        dialog.cancel();

                    }
                })
                .setNeutralButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .show();
    }

    private void popupchecksent_action_item(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("確定是否儲存!?"+"\n")
                .setPositiveButton("儲存", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        String Content = txt_summary_content.getText().toString();

                        Toast.makeText(msibook_dqaweekly_summary_add.this, "資料已更新", Toast.LENGTH_SHORT).show();

                        Insert_Weekly_Content_Summary(GetYear,GetWeek.replace("週",""),GetDeptID,Content,"2");
                        //推播通知
                        SendPushNotificationDeviceByWorkID mSendPushNotificationDeviceByWorkID;
                        mSendPushNotificationDeviceByWorkID = new SendPushNotificationDeviceByWorkID(mContext);
                        mSendPushNotificationDeviceByWorkID.Insert_SendPushNotificationDeviceByWorkID(GetKey_Manager,"週報提醒通知",UserData.Name + " 新增一筆Action Item給您，請確認。","","","");

                        Bundle bundle = new Bundle();
                        bundle.putString("Type", "2");
                        msibook_dqaweekly_summary_add.this.setResult(RESULT_OK,msibook_dqaweekly_summary_add.this.getIntent().putExtras(bundle));
                        finish();

                        dialog.cancel();

                    }
                })
                .setNeutralButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .show();
    }
}
