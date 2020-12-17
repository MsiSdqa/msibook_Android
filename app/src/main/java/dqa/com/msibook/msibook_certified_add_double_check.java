package dqa.com.msibook;

import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class msibook_certified_add_double_check extends AppCompatActivity {

    private Context mContext;

    private TextView textView_Model;
    private TextView textView_Class;
    private TextView textView_Subject;
    private TextView textView_RespUser;
    private TextView textView_cancel;
    private TextView textView_sent;

    private String Get_F_Keyin;
    private String Get_Model;
    private String Get_ModelID;
    private String Get_CerName;
    private String Get_CerID;
    private String Get_F_Subject;
    private String Get_RespUser;
    private String Get_RespUserID;

    private  ProgressDialog progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msibook_certified_add_double_check);

        //讀取時間Bar
        progressBar = new ProgressDialog(this);
        progressBar.setCancelable(true);
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.setMessage("正在預約中，請稍等");

        mContext = msibook_certified_add_double_check.this;

        Get_F_Keyin = getIntent().getStringExtra("F_Keyin");// 工號
        Log.w("F_Keyin",Get_F_Keyin);
        Get_Model = getIntent().getStringExtra("Model");// 專案名稱
        Log.w("Model",Get_Model);
        Get_ModelID = getIntent().getStringExtra("ModelID");// 專案序號
        Log.w("ModelID",Get_ModelID);
        Get_CerName = getIntent().getStringExtra("CerName");// 類別名稱
        Log.w("CerName",Get_CerName);
        Get_CerID = getIntent().getStringExtra("CerID");// 類別代號
        Log.w("CerID",Get_CerID);
        Get_F_Subject = getIntent().getStringExtra("F_Subject");// 說明
        Log.w("F_Subject",Get_F_Subject);
        Get_RespUser = getIntent().getStringExtra("RespUser");// 承辦人
        Log.w("RespUser",Get_RespUser);
        Get_RespUserID = getIntent().getStringExtra("RespUserID");//承辦人工號
        Log.w("RespUserID",Get_RespUserID);

        textView_Model= (TextView)findViewById(R.id.textView_Model);
        textView_Class= (TextView)findViewById(R.id.textView_Class);
        textView_Subject= (TextView)findViewById(R.id.textView_Subject);
        textView_RespUser= (TextView)findViewById(R.id.textView_RespUser);

        textView_cancel= (TextView)findViewById(R.id.textView_cancel);
        textView_sent= (TextView)findViewById(R.id.textView_sent);

        textView_Model.setText(Get_Model);
        textView_Class.setText(Get_CerName);
        textView_Subject.setText(Get_F_Subject);
        textView_RespUser.setText(Get_RespUser);

        textView_cancel.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                finish();
                return false;
            }
        });

        textView_sent.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                Certification_Insert(String.valueOf(Get_F_Keyin),
                        String.valueOf(Get_RespUserID),
                        String.valueOf(Get_ModelID),
                        String.valueOf(Get_CerID),
                        String.valueOf(Get_F_Subject));

                return false;
            }
        });

    }

    private void Certification_Insert(String F_Keyin,
                                     String RespUserID,
                                     String ModelID,
                                     String CerID,
                                     String CertContent) {

        RequestQueue mQueue = Volley.newRequestQueue(this);

        Map<String, String> map = new HashMap<String, String>();
        map.put("WorkID", F_Keyin);
        map.put("RespUserID", RespUserID);
        map.put("ModelID", ModelID);
        map.put("CerID", CerID);
        map.put("CertContent", CertContent);

        if (mQueue == null) {
            mQueue = Volley.newRequestQueue(mContext);
        }

        HTTPSTrustManager.allowAllSSL();//信任所有证书，信任憑證
        String Path = GetServiceData.ServicePath + "/Certification_Insert";

        GetServiceData.SendPostRequest(Path, mQueue, new GetServiceData.VolleyStringCallback() {
            @Override
            public void onSendRequestSuccess(String result) {

                    Toast.makeText(msibook_certified_add_double_check.this, "新增成功", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent();
                    Bundle b = new Bundle();
                    b.putString("Certified_Check","1");
                    intent.putExtras(b);
                    setResult(RESULT_OK, intent);
//
//                    final int notifyID = 1; // 通知的識別號碼
//                    final boolean autoCancel = true; // 點擊通知後是否要自動移除掉通知
//                    final int requestCode = notifyID; // PendingIntent的Request Code
//                    final Intent intent_notification = new Intent(getApplicationContext(), msibook_welcome.class); // 開啟另一個Activity的Intent
//                    final int flags = PendingIntent.FLAG_UPDATE_CURRENT; // ONE_SHOT：PendingIntent只使用一次；CANCEL_CURRENT：PendingIntent執行前會先結束掉之前的；NO_CREATE：沿用先前的PendingIntent，不建立新的PendingIntent；UPDATE_CURRENT：更新先前PendingIntent所帶的額外資料，並繼續沿用
//                    final TaskStackBuilder stackBuilder = TaskStackBuilder.create(getApplicationContext()); // 建立TaskStackBuilder
//                    stackBuilder.addParentStack(msibook_welcome.class); // 加入目前要啟動的Activity，這個方法會將這個Activity的所有上層的Activity(Parents)都加到堆疊中
//                    stackBuilder.addNextIntent(intent_notification); // 加入啟動Activity的Intent
//                    final PendingIntent pendingIntent = stackBuilder.getPendingIntent(requestCode, flags); // 取得PendingIntent
//                    final NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE); // 取得系統的通知服務
//                    final Notification notification = new Notification.Builder(getApplicationContext()).setSmallIcon(R.mipmap.msibook_ic_msibook_applogo).setContentTitle("實驗室預約通知").setContentText("您已完成預約，時間為 : "+String.valueOf(F_StartDate)).setContentIntent(pendingIntent).setAutoCancel(autoCancel).build(); // 建立通知
//                    notificationManager.notify(notifyID, notification); // 發送通知

                    finish();

            }

            @Override
            public void onSendRequestError(String result) {
                Log.w("VolleyError",result);
            }

        }, map);

    }

    private void sendNotification(String messageBody) {
        Intent intent = new Intent(this, msibook_facility_double_check_booking.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.msibook_ic_msibook_applogo)
                .setContentTitle("FCM Message")
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }


}
