package dqa.com.msibook;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class msibook_cec_double_check_new extends AppCompatActivity {

    private Context mContext;

    private ProgressDialog progressBar;

    private ListView mListView;

    private CEC_ItemAdapter mCEC_ItemAdapter;
    private List<CEC_Item> CEC_Item_list = new ArrayList<CEC_Item>();

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
    private List<String> Get_Cer_Pic_Item = new ArrayList<String>();

    private List<String> Save_ModelID_Array = new ArrayList<String>(); //項目 SeqNo
    private List<String> Save_Model_Array = new ArrayList<String>(); //項目logo

    private String SaveStr_Item;
    private String SaveStr_Temp;
    private String SaveStr_Temp_Info;

    private TextView textView_totalUSD;
    private TextView textView_TotalCount;

    private Double SaveTotaltime;
    private Double TotalUSD;
    private Integer TotalCount;

    private String ImagePath;

    private TextView textView_next;

    private LinearLayout linear_model;
    private TextView textView_select_model;
    private String Select_Model;
    private String Select_ModelID;

    private TextView textView_check_sent;

    //點選Local回傳狀態
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(data !=null) {
            Bundle bundle = data.getExtras();
            String GetBack_ModelID = bundle.getString("Choice_ModelID");
            String GetBack_Model = bundle.getString("Choice_Model");
            Select_Model=GetBack_Model;
            Select_ModelID=GetBack_ModelID;
            textView_select_model.setText(Select_Model);
//            Get_Bundle_Item = bundle.getStringArrayList("Item");
//            if(CheckBooking ==1){
//                Intent intent = new Intent();
//                Bundle b = new Bundle();
//                b.putString("CEC_Application_Check","1");
//                b.putSerializable("Item", (Serializable) Get_Bundle_Item);
//                intent.putExtras(b);
//                setResult(RESULT_OK, intent);
//                finish();
//            }
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msibook_cec_double_check_new);

        mContext = msibook_cec_double_check_new.this;
        mListView = (ListView) findViewById(R.id.list_check_cec);

        progressBar = new ProgressDialog(this);
        progressBar.setCancelable(true);
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.setMessage("資料送出中");

        textView_totalUSD = (TextView) findViewById(R.id.textView_totalUSD);
        textView_TotalCount = (TextView) findViewById(R.id.textView_TotalCount);
        textView_check_sent = (TextView) findViewById(R.id.textView_check_sent);
        linear_model = (LinearLayout) findViewById(R.id.linear_model);
        textView_select_model = (TextView) findViewById(R.id.textView_select_model);

        Get_Item.clear();
        Get_Temp_Item.clear();
        Get_Cer_Time_Item.clear();
        Get_Expense_Item.clear();
        Get_RWorkID_Item.clear();

        CEC_Item_list.clear();

        Select_Model="";
        Select_ModelID="";

        Get_Item = getIntent().getStringArrayListExtra("Item");
        Get_Temp_Item = getIntent().getStringArrayListExtra("Temp_Item");
        Get_Cer_Time_Item = getIntent().getStringArrayListExtra("Cer_Time_Item");
        Get_Expense_Item = getIntent().getStringArrayListExtra("Cer_Expense_Item");
        Get_RWorkID_Item = getIntent().getStringArrayListExtra("RWorkID_Item");
        Get_Cer_Pic_Item = getIntent().getStringArrayListExtra("Cer_Pic_Item");

        SaveTotaltime = 0.0;
        TotalUSD = 0.0;
        TotalCount = 0;

        String F_Cer_Time = getIntent().getStringExtra("F_Cer_Time");
        Log.w("Get_F_Cer_Time",F_Cer_Time);
        String F_Cer_Expense = getIntent().getStringExtra("F_Cer_Expense");

        DecimalFormat mDecimalFormat = new DecimalFormat("#,###.##");

        textView_totalUSD.setText("USD.$"+mDecimalFormat.format(Double.parseDouble(String.valueOf(F_Cer_Expense))));
        textView_TotalCount.setText("總數量  :"+Get_Expense_Item.size());

        linear_model.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent msibook_cec_select_model = new Intent(msibook_cec_double_check_new.this, msibook_cec_select_model.class);

                msibook_cec_select_model.putStringArrayListExtra("ModelID_Item", (ArrayList<String>) Save_ModelID_Array); //// 陣列加入    ---ModelID
                msibook_cec_select_model.putStringArrayListExtra("Model_Item", (ArrayList<String>) Save_Model_Array); //// 陣列加入    ---ModelID

                startActivityForResult(msibook_cec_select_model,1);
            }
        });


        CEC_Item_list.clear();

        for (int i = 0; i < Get_Temp_Item.size(); i++) {

            String Item = Get_Item.get(i);

            String Temp_Item = Get_Temp_Item.get(i);

            String Cer_Time_Item = Get_Cer_Time_Item.get(i);

            String Expense_Item = Get_Expense_Item.get(i);

            String RWorkID_Item = Get_RWorkID_Item.get(i);

            String Cer_Pic_Item = Get_Cer_Pic_Item.get(i);

            CEC_Item_list.add(i,new CEC_Item(Item,Temp_Item,Cer_Time_Item,Expense_Item,RWorkID_Item,Cer_Pic_Item));

        }
        mCEC_ItemAdapter = new CEC_ItemAdapter(mContext,CEC_Item_list);

        mListView.setAdapter(mCEC_ItemAdapter);



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


        textView_check_sent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //顯示  讀取等待時間Bar
                progressBar.show();

                if(Select_Model.length()==0){
                    progressBar.dismiss();
                    AlertDialog dialog = new AlertDialog.Builder(mContext)
                            .setTitle("ERROR")
                            .setMessage("尚未選擇專案")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                }
                            })
                            .show();
                    dialog.setCanceledOnTouchOutside(false);// 设置点击屏幕Dialog不消失
                }else {
                    AlertDialog dialog = new AlertDialog.Builder(mContext)
                            .setMessage("確認是否送出")
                            .setPositiveButton("送出", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog,int which) {
//                                    for (int i = 0; i < SaveStr_Temp.length(); i++) {
//                                        Log.w("Temp", SaveStr_Temp);
//                                        Log.w("Temp_Info", String.valueOf(SaveStr_Temp_Info.indexOf(i)));
//                                        Log.w("ModelID", String.valueOf(Select_ModelID.indexOf(i)));
//
//                                    }
                                    Find_Certification_Insert(SaveStr_Temp,SaveStr_Temp_Info,Select_ModelID,"10003130","劉慶忠 kevinliu",UserData.WorkID,UserData.EName);
                                }
                            })
                            .setNeutralButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    dialog.cancel();
                                    progressBar.dismiss();
                                }
                            })
                            .show();
                    dialog.setCanceledOnTouchOutside(false);// 设置点击屏幕Dialog不消失

                }

            }
        });

        Find_My_Fac_Model(UserData.WorkID);

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

        String Path = "http://wtsc.msi.com.tw/IMS/MsiBook_App_Service.asmx/Find_Certification_Insert";

        GetServiceData.SendPostRequest(Path, mQueue, new GetServiceData.VolleyStringCallback() {
            @Override
            public void onSendRequestSuccess(String result) {

                    Intent intent = new Intent();
                    Bundle b = new Bundle();
                    b.putString("CEC_Application_Check", "1"); //申請成功
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
                    final Notification notification = new Notification.Builder(getApplicationContext()).setSmallIcon(R.mipmap.ic_msibook_applogo).setContentTitle("認證通知" + " MS-" + Select_Model + "專案申請成功").setContentText("請填妥測試委託單並上傳至需求單系統，待能效實驗室確認。").setContentIntent(pendingIntent).setAutoCancel(autoCancel).build(); // 建立通知
                    notificationManager.notify(notifyID, notification); // 發送通知
                    //關閉-讀取等待時間Bar
                    progressBar.dismiss();

                    finish();

                }

            @Override
            public void onSendRequestError(String result) {
                Log.w("VolleyError",result);
            }

        }, map);

    }


    private void Find_My_Fac_Model(String WorkID) {

        //顯示  讀取等待時間Bar
        progressBar.show();

        Save_ModelID_Array.clear();
        Save_Model_Array.clear();

        RequestQueue mQueue = Volley.newRequestQueue(this);

        String Path = GetServiceData.ServicePath + "/Find_My_Fac_Model?F_Keyin=" + WorkID;

        GetServiceData.getString(Path, mQueue, new GetServiceData.VolleyCallback() {
            @Override
            public void onSuccess(JSONObject result) {

                try {

                    JSONArray UserArray = new JSONArray(result.getString("Key"));

                    for (int i = 0; i < UserArray.length(); i++) {

                        JSONObject IssueData = UserArray.getJSONObject(i);

                        String ModelID = String.valueOf(IssueData.getInt("ModelID"));// ID

                        String Model = String.valueOf(IssueData.getString("Model"));// 中文

                        Save_ModelID_Array.add(ModelID);
                        Save_Model_Array.add(Model);
                    }

                    //關閉-讀取等待時間Bar
                    progressBar.dismiss();

                }
                catch (JSONException ex) {

                    Log.w("Json",ex.toString());

                }

            }
        });

    }

    //Detail_Item
    public class CEC_Item {

        String Item;

        String Temp_Item;

        String Cer_Time_Item;

        String Expense_Item;

        String RWorkID_Item;

        String Cer_Pic_Item;

        public CEC_Item(String Item,String Temp_Item,String Cer_Time_Item,String Expense_Item,String RWorkID_Item,String Cer_Pic_Item)
        {
            this.Item = Item;

            this.Temp_Item = Temp_Item;

            this.Cer_Time_Item = Cer_Time_Item;

            this.Expense_Item = Expense_Item;

            this.RWorkID_Item = RWorkID_Item;

            this.Cer_Pic_Item = Cer_Pic_Item;
        }

        public String GetItem()
        {
            return this.Item;
        }

        public String GetTemp_Item()
        {
            return this.Temp_Item;
        }

        public String GetCer_Time_Item()
        {
            return this.Cer_Time_Item;
        }

        public String GetExpense_Item()
        {
            return this.Expense_Item;
        }

        public String GetRWorkID_Item()
        {
            return this.RWorkID_Item;
        }

        public String GetCer_Pic_Item()
        {
            return this.Cer_Pic_Item;
        }

    }


    //----------------------------------------------Adapter----------------------------------------------
    //WeekProjectAdapter
    public class CEC_ItemAdapter extends BaseAdapter {

        private LayoutInflater mLayInf;

        private List<CEC_Item> CEC_Item_list;

        private Context ProjectContext;

        private String Title;

        public CEC_ItemAdapter(Context context,  List<CEC_Item> CEC_Item_list)
        {
            mLayInf = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            ProjectContext = context;

            this.Title = Title;

            this.CEC_Item_list = CEC_Item_list;

        }
        @Override
        public int getCount() {
            return CEC_Item_list.size();
        }

        @Override
        public Object getItem(int position) {
            return CEC_Item_list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            View v = new View(ProjectContext);

            v = mLayInf.inflate(R.layout.msibook_cec_double_check_new_item, parent, false);

            final ImageView imageView_F_Cer_Pic = (ImageView) v.findViewById(R.id.imageView_F_Cer_Pic);
            TextView textView_F_Cer_Logo = (TextView) v.findViewById(R.id.textView_F_Cer_Logo);
            TextView textView_totalUSD = (TextView) v.findViewById(R.id.textView_totalUSD);

            ImagePath = CEC_Item_list.get(position).GetCer_Pic_Item().replace("//172.16.111.114/File","http://wtsc.msi.com.tw/IMS/FileServer");
            //ImagePath = Apply_Item_List.get(position).GetF_Cer_Pic();
//            final ViewHolder finalHolder = holder;
            Glide
                    .with(mContext)
                    .load(ImagePath)
                    .asBitmap()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.mipmap.pms_img_pms_no_pic)
                    .into(new SimpleTarget<Bitmap>(100, 100) {
                        @Override
                        public void onResourceReady(Bitmap resource, GlideAnimation glideAnimation) {

                            imageView_F_Cer_Pic.setImageBitmap(resource);

                            //顯示  讀取等待時間Bar1080
                            progressBar.dismiss();
                        }

                        @Override
                        public void onLoadFailed(Exception e, Drawable errorDrawable) {
//                            Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_SHORT).show();
                            //顯示  讀取等待時間Bar
                            progressBar.dismiss();
                        }
                    });

            textView_F_Cer_Logo.setText(CEC_Item_list.get(position).GetItem());

            textView_totalUSD.setText("USD " + CEC_Item_list.get(position).GetExpense_Item() +"。"+CEC_Item_list.get(position).GetCer_Time_Item()+" 週");

            return v;
        }

    }

}
