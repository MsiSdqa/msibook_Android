package dqa.com.msibook;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class msibook_cec_double_check extends AppCompatActivity {

    private Context mContext;

    private ProgressDialog progressBar;

    private TextView textView_model;
    private TextView textView_cec;
    private TextView textView_item;
    private String Column1;
    private String Get_Model_ID;

    private List<String> Get_Item = new ArrayList<String>();

    private List<String> Get_Temp_Item = new ArrayList<String>();
    private List<String> Get_Cer_Time_Item = new ArrayList<String>();
    private List<String> Get_Expense_Item = new ArrayList<String>();
    private List<String> Get_RWorkID_Item = new ArrayList<String>();

    private String SaveStr_Item;
    private String SaveStr_Temp;
    private String SaveStr_Temp_Info;


    private TextView textView_time;
    private TextView textView_cost;

    private TextView textView_cancel;
    private TextView textView_sent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msibook_cec_double_check);

        mContext = msibook_cec_double_check.this;

        progressBar = new ProgressDialog(this);
        progressBar.setCancelable(true);
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.setMessage("資料送出中");

        textView_model = (TextView) findViewById(R.id.textView_model);
        textView_cec = (TextView) findViewById(R.id.textView_cec);
        textView_item = (TextView) findViewById(R.id.textView_item);
        textView_time = (TextView) findViewById(R.id.textView_time);
        textView_cost = (TextView) findViewById(R.id.textView_cost);

        textView_cancel = (TextView) findViewById(R.id.textView_cancel);
        textView_sent = (TextView) findViewById(R.id.textView_sent);

        //設定顯示座標位置
        Column1 = getIntent().getStringExtra("Column1");
        Get_Model_ID = getIntent().getStringExtra("Model_ID");
        String F_Cer_Application = getIntent().getStringExtra("F_Cer_Application");

        Get_Item.clear();
        Get_Temp_Item.clear();
        Get_Cer_Time_Item.clear();
        Get_Expense_Item.clear();
        Get_RWorkID_Item.clear();

        Get_Item = getIntent().getStringArrayListExtra("Item");
        Get_Temp_Item = getIntent().getStringArrayListExtra("Temp_Item");
        Get_Cer_Time_Item = getIntent().getStringArrayListExtra("Cer_Time_Item");
        Get_Expense_Item = getIntent().getStringArrayListExtra("Cer_Expense_Item");
        Get_RWorkID_Item = getIntent().getStringArrayListExtra("RWorkID_Item");

        String F_Cer_Time = getIntent().getStringExtra("F_Cer_Time");
        Log.w("Get_F_Cer_Time",F_Cer_Time);
        String F_Cer_Expense = getIntent().getStringExtra("F_Cer_Expense");

        textView_model.setText("MS - "+Column1);
        textView_cec.setText(F_Cer_Application);

        SaveStr_Item = "";
        SaveStr_Temp = "";
        SaveStr_Temp_Info = "";

        for (int i = 0; i < Get_Item.size(); i++) {
            SaveStr_Item += Get_Item.get(i)+"\n";
            SaveStr_Temp += Get_Temp_Item.get(i)+";";
            SaveStr_Temp_Info += (Get_Item.get(i) + "。" + Get_RWorkID_Item.get(i) + "。" + Get_Expense_Item.get(i) + "。" + Get_Cer_Time_Item.get(i) + ";");
        }

        Log.w("Temp",String.valueOf(SaveStr_Temp));
        Log.w("Temp_Info",String.valueOf(SaveStr_Temp_Info));

        textView_item.setText(SaveStr_Item);

        textView_time.setText(String.valueOf(Double.valueOf(F_Cer_Time).intValue())+" 週");

        DecimalFormat mDecimalFormat = new DecimalFormat("#,###.##");
        String str__USD = mDecimalFormat.format(Double.parseDouble(String.valueOf(F_Cer_Expense)));
        textView_cost.setText(String.valueOf(str__USD));

        textView_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        textView_sent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //顯示  讀取等待時間Bar
                progressBar.show();
                Find_Certification_Insert(SaveStr_Temp,SaveStr_Temp_Info,Get_Model_ID,"10015700","陳浩偉  williamchen",UserData.WorkID,UserData.EName);
            }
        });
    }

    private void Find_Certification_Insert(String QueryLab_Temp,
                                      String QueryLab_Temp_Info,
                                      String QueryLab_ModelID,
                                      String QueryWorkID,
                                      String QueryWorkIDName,
                                      String QuerystrUserID,
                                      String QuerystrUserName) {

        RequestQueue mQueue = Volley.newRequestQueue(this);

        Map<String, String> map = new HashMap<String, String>();
        map.put("QueryLab_Temp", QueryLab_Temp);
        map.put("QueryLab_Temp_Info", QueryLab_Temp_Info);
        map.put("QueryLab_ModelID", QueryLab_ModelID);
        map.put("QueryWorkID", QueryWorkID);
        map.put("QueryWorkIDName", QueryWorkIDName);
        map.put("QuerystrUserID", QuerystrUserID);
        map.put("QuerystrUserName", QuerystrUserName);

        if (mQueue == null) {
            mQueue = Volley.newRequestQueue(mContext);
        }

        String Path = "http://wtsc.msi.com.tw/Test/MsiBook_App_Service.asmx/Find_Certification_Insert";

        GetServiceData.SendPostRequest(Path, mQueue, new GetServiceData.VolleyStringCallback() {
            @Override
            public void onSendRequestSuccess(String result) {

                Intent intent = new Intent();
                Bundle b = new Bundle();
                b.putString("CEC_Application_Check","1");
                b.putSerializable("Item", (Serializable) Get_Item);
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
                    final Notification notification = new Notification.Builder(getApplicationContext()).setSmallIcon(R.mipmap.msibook_ic_msibook_applogo).setContentTitle("認證通知"+" MS-"+ Column1 +"專案申請成功").setContentText("請填妥測試委託單並上傳至需求單系統，待能效實驗室確認。").setContentIntent(pendingIntent).setAutoCancel(autoCancel).build(); // 建立通知
                    notificationManager.notify(notifyID, notification); // 發送通知
                //關閉-讀取等待時間Bar
                progressBar.dismiss();

                Toast.makeText(msibook_cec_double_check.this, "申請成功，已受理。", Toast.LENGTH_SHORT).show();
                finish();

            }

            @Override
            public void onSendRequestError(String result) {
                Log.w("VolleyError",result);
            }

        }, map);

    }

}
