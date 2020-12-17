package dqa.com.msibook;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

public class msibook_application_app_use_check extends AppCompatActivity {


    private TextView textView_title;
    private TextView textView_cancel;
    private TextView textView_sent;
    private LinearLayout linear_bottom;

    private String getSysID;
    private String getSysCNName;

    private Context mContext;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msibook_application_app_use_check);

        mContext = this;

        textView_title = (TextView) findViewById(R.id.textView_title);//
        textView_cancel = (TextView) findViewById(R.id.textView_cancel);//
        textView_sent = (TextView) findViewById(R.id.textView_sent);//
        linear_bottom = (LinearLayout) findViewById(R.id.linear_bottom);//

        getSysID =getIntent().getStringExtra("SysID");//取得上一頁 職缺建立者的工號
        getSysCNName =getIntent().getStringExtra("SysCNName");//
        textView_title.setText("確定申請"+getSysCNName+"使用?");

        if(Integer.valueOf(getSysID)==5 || Integer.valueOf(getSysID)==7){
            textView_title.setText("尚未開放");
            linear_bottom.setVisibility(View.GONE);
        }


        textView_sent.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        textView_sent.setBackground(getResources().getDrawable(R.drawable.ehr_btn_background_down));
                        return true;
                    case MotionEvent.ACTION_UP:
                        textView_sent.setBackground(getResources().getDrawable(R.drawable.ehr_btn_background_up));

                        //Find_System_Role_Insert(getSysID,UserData.WorkID);
                        Find_System_Role_Insert(getSysID,"10015989");

                        Bundle bundle = new Bundle();
                        bundle.putString("Application_Check", "1");
                        msibook_application_app_use_check.this.setResult(RESULT_OK,msibook_application_app_use_check.this.getIntent().putExtras(bundle));

                        //推播通知
                        SendPushNotificationDeviceByWorkID mSendPushNotificationDeviceByWorkID;
                        mSendPushNotificationDeviceByWorkID = new SendPushNotificationDeviceByWorkID(mContext);
                        mSendPushNotificationDeviceByWorkID.Insert_SendPushNotificationDeviceByWorkID(UserData.WorkID,"申請使用通知",UserData.Name + "申請"+getSysCNName+"App使用權限已受理。","app_Application","app_Application","app_Application");

                        Toast.makeText(msibook_application_app_use_check.this, "送出成功", Toast.LENGTH_SHORT).show();

                        finish();
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

    private void Find_System_Role_Insert(String SysID,
                                    String WorkID) {

        RequestQueue mQueue = Volley.newRequestQueue(this);

        Map<String,String> map = new HashMap<String, String>();
        map.put("SysID", SysID);
        map.put("WorkID", WorkID);

        if (mQueue == null) {
            mQueue = Volley.newRequestQueue(this);
        }

        HTTPSTrustManager.allowAllSSL();//信任所有证书，信任憑證
        String Path = GetServiceData.ServicePath + "/Find_System_Role_Insert";
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
