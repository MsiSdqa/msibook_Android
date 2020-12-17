package dqa.com.msibook;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class msibook_ehr_check_interview_detial_new extends AppCompatActivity {

    private TextView textView_interview_dpt_name;
    private TextView textView_interview_job_name;
    private EditText editText_interview_subject;
    private TextView textView_cancel;
    private TextView textView_sent;

    private TextView textView_ChineseName;
    private TextView textView_Tel;
    private TextView textView_DeptName;
    private TextView textView_WebFlowBossName;
    private TextView textView_interview_job_content;
    private TextView textView_WebFlowBossTel;

    private Context mContext;

    String getF_Keyin;
    String getF_SeqNo;
    String getF_DeptCode;
    String getDeptName;
    String getF_Job_Name;
    String getJob_People;
    String getJob_Content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msibook_ehr_check_interview_detial_new);

        mContext = this;

        textView_interview_dpt_name = (TextView) findViewById(R.id.textView_interview_dpt_name);//
        textView_interview_job_name = (TextView) findViewById(R.id.textView_interview_job_name);//
        textView_cancel = (TextView) findViewById(R.id.textView_cancel);//
        textView_sent = (TextView) findViewById(R.id.textView_sent);//

        textView_ChineseName = (TextView) findViewById(R.id.textView_ChineseName);//
        textView_Tel = (TextView) findViewById(R.id.textView_Tel);//
        textView_DeptName = (TextView) findViewById(R.id.textView_DeptName);//
        textView_WebFlowBossName = (TextView) findViewById(R.id.textView_WebFlowBossName);//
        textView_interview_job_content = (TextView) findViewById(R.id.textView_interview_job_content);//
        textView_WebFlowBossTel = (TextView) findViewById(R.id.textView_WebFlowBossTel);//

        getF_Keyin =getIntent().getStringExtra("F_Keyin");//取得上一頁 職缺建立者的工號
        getF_SeqNo =getIntent().getStringExtra("F_SeqNo");//
        getF_DeptCode =getIntent().getStringExtra("F_DeptCode");//
        getDeptName = getIntent().getStringExtra("DeptName");//
        getF_Job_Name = getIntent().getStringExtra("F_Job_Name");//
        getJob_People = getIntent().getStringExtra("F_Job_People");//
        getJob_Content = getIntent().getStringExtra("F_Job_Content");

        textView_interview_dpt_name.setText(getDeptName);
        textView_interview_job_name.setText(getF_Job_Name);
        textView_interview_job_content.setText(getJob_Content);

        textView_ChineseName.setText(UserData.Name);
        textView_Tel.setText(UserData.Phone);
        textView_DeptName.setText(UserData.Dept);
        textView_WebFlowBossName.setText(UserData.WebFlowBossName);
        textView_WebFlowBossTel.setText(UserData.WebFlowBossTel);

        textView_sent.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        textView_sent.setBackground(getResources().getDrawable(R.drawable.ehr_btn_background_down));
                        return true;
                    case MotionEvent.ACTION_UP:
                        textView_sent.setBackground(getResources().getDrawable(R.drawable.ehr_btn_background_up));

                        Insert_E_HR_Detail();

                        return true;
                }
                return false;
            }
        });

        textView_cancel.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        textView_cancel.setBackground(getResources().getDrawable(R.drawable.ehr_btn_background_down));
                        return true;
                    case MotionEvent.ACTION_UP:
                        //textView_cancel.setBackground(getResources().getDrawable(R.drawable.ehr_btn_background_up));

                        finish();

                        return true;
                }
                return false;
            }
        });

    }


    private void Insert_E_HR_Detail(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("確定應徵!?"+"\n")
                .setPositiveButton("應徵", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Insert_E_HR_Master(UserData.WorkID,
                                getF_SeqNo,
                                "姓名:"+UserData.Name+
                                        "分機:"+UserData.Phone+"\n"+
                                        "部門:"+UserData.Dept+"\n"+
                                        "主管:"+UserData.WebFlowBossName +
                                        "分機:"+UserData.WebFlowBossTel+"\n");

                        dialog.cancel();

                        Bundle bundle = new Bundle();
                        bundle.putString("InterView_Check", "1");
                        msibook_ehr_check_interview_detial_new.this.setResult(RESULT_OK,msibook_ehr_check_interview_detial_new.this.getIntent().putExtras(bundle));


                        //推播通知
                        SendPushNotificationDeviceByWorkID mSendPushNotificationDeviceByWorkID;
                        mSendPushNotificationDeviceByWorkID = new SendPushNotificationDeviceByWorkID(mContext);
                        mSendPushNotificationDeviceByWorkID.Insert_SendPushNotificationDeviceByWorkID(getF_Keyin,"eHR 應徵通知",UserData.Name + "應徵「"+getF_Job_Name+"」一職，請確認。","eHR_Application","eHR_Application",getF_SeqNo+","+getF_DeptCode+","+getDeptName+","+getF_Job_Name);

                        finish();
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


    private void Insert_E_HR_Master(String F_Keyin,
                                    String F_Master_ID,
                                    String Introduce) {

        RequestQueue mQueue = Volley.newRequestQueue(this);

        Map<String,String> map = new HashMap<String, String>();
        map.put("F_Keyin", F_Keyin);
        map.put("F_Master_ID", F_Master_ID);
        map.put("Introduce", Introduce);

        Log.w("F_Keyin",F_Keyin);
        Log.w("F_Master_ID",F_Master_ID);
        Log.w("Introduce",Introduce);


        if (mQueue == null) {
            mQueue = Volley.newRequestQueue(this);
        }
        HTTPSTrustManager.allowAllSSL();//信任所有证书，信任憑證
        String Path = GetServiceData.ServicePath + "/Insert_E_HR_Detail";
        //String Path = "http://wtsc.msi.com.tw/IMS/MsiBook_App_Service.asmx/Insert_E_HR_Master";

        GetServiceData.SendPostRequest(Path, mQueue, new GetServiceData.VolleyStringCallback() {
            @Override
            public void onSendRequestSuccess(String result) {



            }

            @Override
            public void onSendRequestError(String result) {



            }

        }, map);
    }




}
