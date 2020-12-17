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
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class msibook_facility_double_check_booking extends AppCompatActivity {

    private Context mContext;

    private TextView textView_model;
    private TextView textView_Sdatetime;
    private TextView textView_Edatetime;
    private TextView textView_subject;
    private TextView textView_use_cost;
    private TextView textView_cancel;
    private TextView textView_sent;

    private String Get_F_Keyin;
    private String Get_Model;
    private String Get_F_Master_ID;
    private String Get_F_Subject;
    private String Get_F_StartDate;
    private String Get_F_EndDate;
    private String Get_F_PM_ID;
    private String Get_F_Is_Restrict;

    private  ProgressDialog progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msibook_facility_double_check_booking);

        //讀取時間Bar
        progressBar = new ProgressDialog(this);
        progressBar.setCancelable(true);
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.setMessage("正在預約中，請稍等");

        mContext = msibook_facility_double_check_booking.this;

        Get_F_Keyin = getIntent().getStringExtra("F_Keyin");// 工號
        Log.w("F_Keyin",Get_F_Keyin);
        Get_Model = getIntent().getStringExtra("Model");// 機台序號
        Get_F_Master_ID = getIntent().getStringExtra("F_Master_ID");// 機台序號
        Log.w("F_Master_ID",Get_F_Master_ID);
        Get_F_Subject = getIntent().getStringExtra("F_Subject");// 內容
        Log.w("F_Subject",Get_F_Subject);
        Log.w("F_Desc","");
        Get_F_StartDate = getIntent().getStringExtra("F_StartDate");// 起日
        Log.w("F_StartDate",Get_F_StartDate.substring(0,Get_F_StartDate.length()-2));
        Get_F_EndDate = getIntent().getStringExtra("F_EndDate");// 結束日
        Log.w("F_EndDate",Get_F_EndDate.substring(0,Get_F_EndDate.length()-2));
        Get_F_PM_ID = getIntent().getStringExtra("F_PM_ID");// 專案ID
        Log.w("F_PM_ID",Get_F_PM_ID);
        Get_F_Is_Restrict = getIntent().getStringExtra("F_Is_Restrict");// 0 or 1
        Log.w("F_Is_Restrict",Get_F_Is_Restrict);

        textView_model= (TextView)findViewById(R.id.textView_model);
        textView_Sdatetime= (TextView)findViewById(R.id.textView_Sdatetime);
        textView_Edatetime= (TextView)findViewById(R.id.textView_Edatetime);
        textView_subject= (TextView)findViewById(R.id.textView_subject);
        textView_use_cost= (TextView)findViewById(R.id.textView_use_cost);
        textView_cancel= (TextView)findViewById(R.id.textView_cancel);
        textView_sent= (TextView)findViewById(R.id.textView_sent);

        textView_model.setText(Get_Model);
        textView_Sdatetime.setText(Get_F_StartDate);
        textView_Edatetime.setText(Get_F_EndDate);
        textView_subject.setText(Get_F_Subject);
        textView_use_cost.setText("");

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

                Insert_Fac_Schedule(String.valueOf(Get_F_Keyin),
                        String.valueOf(Get_F_Master_ID),
                        String.valueOf(Get_F_Subject),
                        String.valueOf(""),
//                        String.valueOf(Get_F_StartDate.substring(0,Get_F_StartDate.length()-3)).replace(" ", ""),
//                        String.valueOf(Get_F_EndDate.substring(0,Get_F_EndDate.length()-3)).replace(" ", ""),
                        String.valueOf(Get_F_StartDate.substring(0,Get_F_StartDate.length()-3)),
                        String.valueOf(Get_F_EndDate.substring(0,Get_F_EndDate.length()-3)),
                        String.valueOf(Get_F_PM_ID),
                        String.valueOf(Get_F_Is_Restrict));

                return false;
            }
        });

    }

//    //新增預約
//    private void Insert_Fac_Schedule(String F_Keyin,
//                                    String F_Master_ID,
//                                    String F_Subject,
//                                    String F_Desc,
//                                    String F_StartDate,
//                                    String F_EndDate,
//                                    String F_PM_ID,
//                                    String F_Is_Restrict) {
//
//        //顯示  讀取等待時間Bar
//        progressBar.show();
//
//        RequestQueue mQueue = Volley.newRequestQueue(this);
//
//        String Path = GetServiceData.ServicePath + "/Insert_Fac_Schedule?F_Keyin=" + F_Keyin +"&F_Master_ID=" + F_Master_ID + "&F_Subject=" + F_Subject +"&F_Desc=" + F_Desc + "&F_StartDate=" + F_StartDate+"&F_EndDate=" + F_EndDate + "&F_PM_ID=" + F_PM_ID+ "&F_Is_Restrict=" + F_Is_Restrict;
//
//        GetServiceData.getString(Path, mQueue, new GetServiceData.VolleyCallback() {
//            @Override
//            public void onSuccess(JSONObject result) {
//
//                if(result == null){
//                    Toast.makeText(msibook_facility_double_check_booking.this, "預約成功", Toast.LENGTH_SHORT).show();
//                    finish();
//                }else{
//
//                    try {
//                        Dialog dialog=new android.support.v7.app.AlertDialog.Builder(mContext)
//                                .setTitle("ERROR")//设置提示内容
//                                .setMessage(result.getString("Key"))//设置提示内容
//                                //确定按钮
//                                .setPositiveButton("確定", new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialog, int which) {
//                                        finish();
//                                    }
//                                })
//                                .create();//创建对话框
//                        dialog.show();//显示对话框
//
//                }
//                catch (JSONException ex) {
//                    Log.w("Json",ex.toString());
//                }
//
//                }
//            }
//        });
//        //關閉-讀取等待時間Bar
//        progressBar.dismiss();
//    }

    private void Insert_Fac_Schedule(String F_Keyin,
                                     String F_Master_ID,
                                     String F_Subject,
                                     String F_Desc,
                                     final String F_StartDate,
                                     String F_EndDate,
                                     String F_PM_ID,
                                     String F_Is_Restrict) {

            RequestQueue mQueue = Volley.newRequestQueue(this);

            Map<String, String> map = new HashMap<String, String>();
            map.put("F_Keyin", F_Keyin);
            map.put("F_Master_ID", F_Master_ID);
            map.put("F_Subject", F_Subject);
            map.put("F_Desc", F_Desc);
            map.put("F_StartDate", F_StartDate);
            map.put("F_EndDate", F_EndDate);
            map.put("F_PM_ID", F_PM_ID);
            map.put("F_Is_Restrict", F_Is_Restrict);

            if (mQueue == null) {
                mQueue = Volley.newRequestQueue(mContext);
            };
        HTTPSTrustManager.allowAllSSL();//信任所有证书，信任憑證
            String Path = GetServiceData.ServicePath + "/Insert_Fac_Schedule";

            GetServiceData.SendPostRequest(Path, mQueue, new GetServiceData.VolleyStringCallback() {
                @Override
                public void onSendRequestSuccess(String result) {
                    String Answer ="";
                    try {
                        Answer = new String(result.getBytes("ISO-8859-1"), "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }

                    Log.w("resultLog",Answer);
                    if(Answer.length()==14 ){
                        Toast.makeText(msibook_facility_double_check_booking.this, "預約成功", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent();
                        Bundle b = new Bundle();
                        b.putString("Booking_Check","1");
                        intent.putExtras(b);
                        setResult(RESULT_OK, intent);

                        final int notifyID = 1; // 通知的識別號碼
                        final boolean autoCancel = true; // 點擊通知後是否要自動移除掉通知
                        final int requestCode = notifyID; // PendingIntent的Request Code
                        final Intent intent_notification = new Intent(getApplicationContext(), msibook_welcome.class); // 開啟另一個Activity的Intent
                        final int flags = PendingIntent.FLAG_UPDATE_CURRENT; // ONE_SHOT：PendingIntent只使用一次；CANCEL_CURRENT：PendingIntent執行前會先結束掉之前的；NO_CREATE：沿用先前的PendingIntent，不建立新的PendingIntent；UPDATE_CURRENT：更新先前PendingIntent所帶的額外資料，並繼續沿用
                        final TaskStackBuilder stackBuilder = TaskStackBuilder.create(getApplicationContext()); // 建立TaskStackBuilder
                        stackBuilder.addParentStack(msibook_welcome.class); // 加入目前要啟動的Activity，這個方法會將這個Activity的所有上層的Activity(Parents)都加到堆疊中
                        stackBuilder.addNextIntent(intent_notification); // 加入啟動Activity的Intent
                        final PendingIntent pendingIntent = stackBuilder.getPendingIntent(requestCode, flags); // 取得PendingIntent
                        final NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE); // 取得系統的通知服務
                        final Notification notification = new Notification.Builder(getApplicationContext()).setSmallIcon(R.mipmap.msibook_ic_msibook_applogo).setContentTitle("實驗室預約通知").setContentText("您已完成預約，時間為 : "+String.valueOf(F_StartDate)).setContentIntent(pendingIntent).setAutoCancel(autoCancel).build(); // 建立通知
                        notificationManager.notify(notifyID, notification); // 發送通知

//                        final int notifyID = 1; // 通知的識別號碼
//                        final boolean autoCancel = true; // 點擊通知後是否要自動移除掉通知
//
//                        final int requestCode = notifyID; // PendingIntent的Request Code
//                        final Intent intent_notification = getIntent(); // 目前Activity的Intent
//                        final int flags = PendingIntent.FLAG_CANCEL_CURRENT; // ONE_SHOT：PendingIntent只使用一次；CANCEL_CURRENT：PendingIntent執行前會先結束掉之前的；NO_CREATE：沿用先前的PendingIntent，不建立新的PendingIntent；UPDATE_CURRENT：更新先前PendingIntent所帶的額外資料，並繼續沿用
//                        final PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), requestCode, intent_notification, flags); // 取得PendingIntent
//
//                        final NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE); // 取得系統的通知服務
//                        final Notification notification = new Notification.Builder(getApplicationContext()).setSmallIcon(R.mipmap.ic_msibook_laboratoryapp).setContentTitle("內容標題").setContentText("內容文字").setContentIntent(pendingIntent).setAutoCancel(autoCancel).build(); // 建立通知
//                        notificationManager.notify(notifyID, notification); // 發送通知



                        finish();
                    }else{
                        Dialog dialog=new android.support.v7.app.AlertDialog.Builder(mContext)
                                .setTitle("ERROR")//设置提示内容
                                .setMessage(Answer)//设置提示内容
                                //确定按钮
                                .setPositiveButton("確定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        finish();
                                    }
                                })
                                .create();//创建对话框
                        dialog.show();//显示对话框
                    }
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
