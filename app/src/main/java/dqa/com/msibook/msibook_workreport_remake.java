package dqa.com.msibook;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.necer.ncalendar.calendar.NCalendar;
import com.necer.ncalendar.listener.OnCalendarChangedListener;
import com.necer.ncalendar.utils.MyLog;

import org.joda.time.LocalDate;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class msibook_workreport_remake extends AppCompatActivity implements OnCalendarChangedListener {

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static final String WRITE_EXTERNAL_STORAGE = "android.permission.WRITE_EXTERNAL_STORAGE";
    private static final String READ_EXTERNAL_STORAGE = "android.permission.READ_EXTERNAL_STORAGE";

    //修改成登入資訊
    //private String ID=ID="10015423";;//ID="10015423";
    private String ID=UserData.WorkID;//ID="10015423";
    //private String Owner="王凱文";//Owner="王凱文"
    private String Owner=UserData.Name;//Owner="王凱文"
    //修改成登入資訊
    private static final String TAG = "MainActivity";
    Dialog myDialog;
    private String currentDate = "";
    private TextView tv_ShowDate ;
    private TextView tv_totalHR ;
    private TextView tv_Model;
    private TextView tv_hour;
    private TextView tv_Subject;
    private NCalendar ncalendar;
    private RecyclerView recyclerView;
    private ListView listV;
    private MyAdapter adapter;

    public TextView txt_HR ;
    public TextView txt_Model;
    public TextView txt_Type;
    public TextView txt_Subject;
    public TextView txt_totalHR ;
    private JSONObject LOGData;
    final List<Log> log_list = new ArrayList<Log>();

    Date date = new Date();
    final SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
    final SimpleDateFormat cal = new SimpleDateFormat("yyyy-MM-dd");
//    Action action = new Action();

    private TextView text_w7;
    private TextView text_w1;
    private TextView text_w2;
    private TextView text_w3;
    private TextView text_w4;
    private TextView text_w5;
    private TextView text_w6;


    private LinearLayout linear_yellow_inside;
    private LinearLayout linear_warning;

    private TextView textView_wrokhour;
    private TextView textView_reporthour;
    private TextView textView_leavehour;
    private TextView textView_overhour;
    private TextView textView_warning_info;
    private TextView txt_Notifacation_info;

    private String Set_Attend;
    private String Set_RSS;
    private String Set_Leave;
    private String Set_OverTime;

    private String BaseWeekofYear;
    private String BaseWeek;
    private String MoveYear;
    private String MoveWeekofYear;
    private String MoveWeek;
    private TextView testuse;

    private static ArrayList<String> mylist = new ArrayList<String>();
    private ArrayList<String> ArrayAttend = new ArrayList<String>();
    private ArrayList<String> ArrayLeave = new ArrayList<String>();
    private ArrayList<String> ArrayRSS = new ArrayList<String>();
    private ArrayList<String> ArrayOverTime = new ArrayList<String>();

    private String BaseCheckShowWeek;
    private static String D0;
    private String D1;
    private String D2;
    private String D3;
    private String D4;
    private String D5;
    private String D6;

    private ProgressDialog progressBar;
    //判斷回首頁狀態
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

//        if (data != null) {
//            Bundle bundle = data.getExtras();
//            Integer Add_Check = Integer.valueOf(bundle.getString("Add_Check"));
//            if(Add_Check ==1){
//                GetTodaylog(currentDate);
//                GetRssDate();
//            }
//        }
    }

    public interface VolleyCallback {

        void onSuccess(JSONObject result);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msibook_workreport_remake);

        testuse= (TextView)findViewById(R.id.testuse);

        currentDate = sdf.format(date);  //當期日期

        progressBar = new ProgressDialog(this);
        progressBar.setCancelable(true);
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.setMessage("資料載入中");

        Calendar calendar = Calendar.getInstance();
        //calendar.setFirstDayOfWeek(Calendar.SUNDAY);
        calendar.setTime(date);
        MoveYear = String.valueOf(calendar.get(Calendar.YEAR));
        MoveWeekofYear = String.valueOf(calendar.get(Calendar.WEEK_OF_YEAR));
        BaseWeekofYear = String.valueOf(calendar.get(Calendar.WEEK_OF_YEAR));
        MoveWeek = String.valueOf(calendar.get(Calendar.DAY_OF_WEEK));
        BaseWeek = String.valueOf(calendar.get(Calendar.DAY_OF_WEEK));
        //testuse.setText(MoveWeekofYear);
        MyLog.d("當前年" + MoveYear +"當前週次" + MoveWeekofYear + "當前星期" +MoveWeek);
        BaseCheckShowWeek = MoveWeek;

        text_w7 = (TextView)findViewById(R.id.text_w7);
        text_w1 = (TextView)findViewById(R.id.text_w1);
        text_w2 = (TextView)findViewById(R.id.text_w2);
        text_w3 = (TextView)findViewById(R.id.text_w3);
        text_w4 = (TextView)findViewById(R.id.text_w4);
        text_w5 = (TextView)findViewById(R.id.text_w5);
        text_w6 = (TextView)findViewById(R.id.text_w6);

        tv_Model = (TextView)findViewById(R.id.tv_Model);
        tv_hour  = (TextView)findViewById(R.id.tv_hour);
        tv_Subject = (TextView)findViewById(R.id.tv_Subject);
        tv_ShowDate = (TextView) findViewById(R.id.tv_ShowDate);
        tv_totalHR = (TextView) findViewById(R.id.tvTotalHR);
        ncalendar = (NCalendar) findViewById(R.id.ncalendar);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        ncalendar.setOnCalendarChangedListener(this);

        linear_warning = (LinearLayout) findViewById(R.id.linear_warning);
        linear_yellow_inside = (LinearLayout) findViewById(R.id.linear_yellow_inside);

        textView_wrokhour = (TextView)findViewById(R.id.textView_wrokhour);
        textView_reporthour = (TextView)findViewById(R.id.textView_reporthour);
        textView_leavehour = (TextView)findViewById(R.id.textView_leavehour);
        textView_overhour = (TextView)findViewById(R.id.textView_overhour);
        textView_warning_info = (TextView)findViewById(R.id.textView_warning_info);
        txt_Notifacation_info = (TextView)findViewById(R.id.txt_Notifacation_info);
        txt_Notifacation_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();

                intent.setClass(msibook_workreport_remake.this, msibook_workreport_remake_notice.class);
                //開啟Activity
                startActivity(intent);

            }
        });
//        ImageButton btn_never_add = (ImageButton) findViewById(R.id.btn_never_add);
//        LinearLayout LL_never = (LinearLayout) findViewById(R.id.LL_never);
//        ImageButton btn_already_add = (ImageButton) findViewById(R.id.btn_already_add);
//        LL_never.setOnClickListener(action);
//        btn_already_add.setOnClickListener(action);  按+ 按紐
//        btn_never_add.setOnClickListener(action); 沒工作報告按紐
        GetTodaylog(currentDate);
        Find_RSSList_ByWorkID(UserData.WorkID,currentDate);
        Find_RSSList_ByWorkID_2(currentDate,UserData.WorkID,MoveYear,BaseWeekofYear);
        GetRssDate();

        myDialog = new Dialog(this);

    }

    public void ShowPopup(View v, String Model, String Subjec, String Type, String HR){

        myDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.setContentView(R.layout.rd_popup_layout);

        txt_HR = (TextView) myDialog.findViewById(R.id.txt_HR);
        txt_Model = (TextView) myDialog.findViewById(R.id.txt_Model);
        //txt_Type = (TextView) myDialog.findViewById(R.id.txt_Type);
        txt_Subject = (TextView) myDialog.findViewById(R.id.txt_Subject);

        txt_HR.setText(tv_ShowDate.getText()+" "+HR);
        txt_Model.setText(Model);
//        if (Type=="null"){
//            txt_Type.setText("無");
//        }else{
//            txt_Type.setText(Type);
//        }
        txt_Subject.setText(Subjec);

        myDialog.show();
    }

    //取得今天工作日誌
    protected void GetTodaylog(String Date) {

        //顯示  讀取等待時間Bar
        progressBar.show();

        final RequestQueue mQueue = Volley.newRequestQueue(this);
        final LinearLayout LL_never = (LinearLayout) findViewById(R.id.LL_never);
        final LinearLayout total_layout= (LinearLayout) findViewById(R.id.total_layout);
        final LinearLayout LL_already = (LinearLayout) findViewById(R.id.LL_already);

        final ArrayList<String> Model  = new ArrayList<String>() ;
        final ArrayList<String> HR  = new ArrayList<String>() ;
        final ArrayList<String> Subject  = new ArrayList<String>() ;

        final String Path = "http://wtsc.msi.com.tw/IMS/MsiBook_App_Service.asmx/RDLog_RssLog?F_Keyin="+ID+"&F_Date="+Date;

        getString(Path, mQueue, new VolleyCallback() {
            @Override
            public void onSuccess(JSONObject result) {
                int sum = 0;
                List<String> list = new ArrayList<>();
                SwipeMenuListView listV;
                MyAdapter adapter;
                listV = (SwipeMenuListView)findViewById(R.id.lv_total);
                try {

                    JSONArray UserArray = new JSONArray(result.getString("Key"));
                    if (UserArray.length() > 0) {
                        log_list.clear();
                        for (int i = 0; i < UserArray.length(); i++) {

                            LOGData = UserArray.getJSONObject(i);
                            sum += Double.valueOf(LOGData.getString("F_WorkHour"));
                            tv_totalHR.setText("總時數: "+String.valueOf(sum)+"小時");
                            log_list.add(new Log(String.valueOf(LOGData.getString("F_SeqNo")),String.valueOf(LOGData.getString("Model")),String.valueOf(LOGData.getString("F_WorkHour"))+"小時",String.valueOf(LOGData.getString("F_Subject")),String.valueOf(LOGData.getString("RSSType")),String.valueOf(LOGData.getString("F_CostSum"))));

                        }
                        adapter = new MyAdapter(msibook_workreport_remake.this,log_list);
                        SwipeMenuCreator creator = new SwipeMenuCreator() {

                            @Override
                            public void create(SwipeMenu menu) {
                                // create "delete" item
                                SwipeMenuItem deleteItem = new SwipeMenuItem(
                                        getApplicationContext());
                                // set item background
                                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
                                        0x3F, 0x25)));
                                // set item width
                                deleteItem.setWidth(170);
                                // set a icon
                                deleteItem.setIcon(R.drawable.ic_delete);
                                // add to menu
                                menu.addMenuItem(deleteItem);
                            }
                        };

// set creator
                        listV.setMenuCreator(creator);
                        listV.setAdapter(adapter);

                        listV.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                                switch (index) {
                                    case 0:
                                        android.util.Log.d(TAG, "onMenuItemClick: "+log_list.get(position).getID());
                                        //getString( "http://wtsc.msi.com.tw/IMS/MsiBook_App_Service.asmx/RDLog_RssLogDelete?ID="+log_list.get(position).getID(), mQueue,new MainActivity.VolleyCallback() );
                                        final String Path = "http://wtsc.msi.com.tw/IMS/MsiBook_App_Service.asmx/RDLog_RssLogDelete?ID="+log_list.get(position).getID();

                                        getString(Path, mQueue, new VolleyCallback() {
                                            @Override
                                            public void onSuccess(JSONObject result) {
                                            }
                                        });
                                        GetTodaylog(currentDate);
                                        Find_RSSList_ByWorkID(UserData.WorkID,currentDate);
                                        Find_RSSList_ByWorkID_2(currentDate,UserData.WorkID,MoveYear,BaseWeekofYear);
                                        GetRssDate();
                                        break;
                                    case 1:
                                        android.util.Log.d(TAG, "onMenuItemClick: "+log_list.get(position).getID());
                                        break;
                                }
                                // false : close the menu; true : not close the menu
                                return false;
                            }
                        });
                        listV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

//                                Intent intent = new Intent();
//
//                                intent.putExtra("Model", log_list.get(position).getModel());
//
//                                intent.putExtra("Subjec", log_list.get(position).getSubject());
//
//                                intent.putExtra("Type", log_list.get(position).getType());
//
//                                intent.putExtra("HR", tv_ShowDate.getText()+" "+ log_list.get(position).getHR());
//
//                                intent.setClass(msibook_workreport_remake.this, msibook_rd_detial.class);
//                                //開啟Activity
//                                startActivity(intent);


                                Intent intent = new Intent();

                                intent.putExtra("ModelName", log_list.get(position).getModel());

                                intent.putExtra("F_Subject", log_list.get(position).getSubject());

                                intent.putExtra("F_CreateDate", tv_ShowDate.getText());

                                intent.putExtra("F_WorkHour", log_list.get(position).getHR());

                                intent.putExtra("F_CostSum", log_list.get(position).getF_CostSum());

                                intent.setClass(msibook_workreport_remake.this, msibook_workreport_detail.class);
                                //開啟Activity
                                startActivity(intent);

                                //ShowPopup(view, log_list.get(position).getModel(),log_list.get(position).getSubject(),log_list.get(position).getType(),log_list.get(position).getHR());

                            }
                        });
                        listV.setDivider(null);
                        total_layout.setVisibility(View.VISIBLE);
                        LL_never.setVisibility(View.GONE);
                        LL_already.setVisibility(View.VISIBLE);

                    }

                    else{
                        tv_totalHR.setText("");
                        LL_never.setVisibility(View.VISIBLE);
                        total_layout.setVisibility(View.GONE);
                        LL_already.setVisibility(View.GONE);
                    }

                    //顯示  讀取等待時間Bar
                    progressBar.dismiss();
                }
                catch (JSONException ex) {

                }

            }

        });
    }

    //取得此工號有填工作日誌的日期
    protected void GetRssDate() {

        final RequestQueue mQueue = Volley.newRequestQueue(this);
        final String Path = "http://wtsc.msi.com.tw/IMS/MsiBook_App_Service.asmx/RDLog_RssLogDate?F_Keyin="+ID;

        getString(Path, mQueue, new VolleyCallback() {
            @Override
            public void onSuccess(JSONObject result) {

                List<String> list = new ArrayList<>();

                try {

                    JSONArray UserArray = new JSONArray(result.getString("Key"));
                    if (UserArray.length() > 0) {

                        for (int i = 0; i < UserArray.length(); i++) {
                            LOGData = UserArray.getJSONObject(i);
                            list.add(String.valueOf(LOGData.getString("dateForRss")));
                        }
                        ncalendar.setPoint(list);
                    }
                }
                catch (JSONException ex) {
                }
            }
        });
    }

    //取得出勤 + 報告 + 請假 + 加班時數
    protected void Find_RSSList_ByWorkID(String WorkID,String Date) {

        final RequestQueue mQueue = Volley.newRequestQueue(this);
        final String Path = "http://wtsc.msi.com.tw/IMS/Weekly_Report.asmx/Find_RSSList_ByWorkID?WorkID=" + WorkID + "&Date=" + Date;

        Set_Attend="";
        Set_RSS="";
        Set_Leave="";
        Set_OverTime="";

        getString(Path, mQueue, new VolleyCallback() {
            @Override
            public void onSuccess(JSONObject result) {

                try {

                    JSONArray UserArray = new JSONArray(result.getString("Key"));
                    if (UserArray.length() > 0) {

                        for (int i = 0; i < UserArray.length(); i++) {

                            JSONObject IssueData = UserArray.getJSONObject(i);

                            //String Attend = String.valueOf(IssueData.getString("Attend"));//"10015812",
                            if (IssueData.isNull("Attend")) {
                                Set_Attend = "0";
                            } else {
                                Set_Attend = String.valueOf(IssueData.getString("Attend"));
                            }

                            //String RSS = String.valueOf(IssueData.getString("RSS"));//"10015812",
                            if (IssueData.isNull("RSS")) {
                                Set_RSS = "0";
                            } else {
                                Set_RSS = String.valueOf(IssueData.getString("RSS"));
                            }

                            //String Leave = String.valueOf(IssueData.getString("Leave"));//"10015812",
                            if (IssueData.isNull("Leave")) {
                                Set_Leave = "0";
                            } else {
                                Set_Leave = String.valueOf(IssueData.getString("Leave"));
                            }

                            //String OverTime = String.valueOf(IssueData.getString("OverTime"));//"10015812",
                            if (IssueData.isNull("OverTime")) {
                                Set_OverTime = "0";
                            } else {
                                Set_OverTime = String.valueOf(IssueData.getString("OverTime"));
                            }

                            textView_wrokhour.setText(Set_Attend);

                            textView_reporthour.setText(Set_RSS);

                            textView_leavehour.setText(Set_Leave);

                            textView_overhour.setText(Set_OverTime);
                            Double Account = 0.0;
                            MyLog.d(Set_RSS + "比較" + Set_Attend ); // 如果 每日出勤數(Attend)   - 工作報告時數(RSS)  - 請假時數(Leave) + 加班(Overtime)
                            if(Double.valueOf(Set_RSS) + Double.valueOf(Set_Leave) + Double.valueOf(Set_OverTime) > Double.valueOf(Set_Attend)){
                                Account = (Double.valueOf(Set_RSS) + Double.valueOf(Set_Leave) + Double.valueOf(Set_OverTime)) - Double.valueOf(Set_Attend);
                                linear_warning.setVisibility(View.VISIBLE);
                                linear_yellow_inside.setBackgroundColor(Color.parseColor("#ffffae"));
                                textView_warning_info.setText("提醒：您的報告多 "+Account+" 小時。");
                            }else if(Double.valueOf(Set_RSS) + Double.valueOf(Set_Leave) + Double.valueOf(Set_OverTime) < Double.valueOf(Set_Attend)){
                                Account = Double.valueOf(Set_Attend) - (Double.valueOf(Set_RSS) + Double.valueOf(Set_Leave) + Double.valueOf(Set_OverTime));
                                linear_warning.setVisibility(View.VISIBLE);
                                linear_yellow_inside.setBackgroundColor(Color.parseColor("#ffffae"));
                                textView_warning_info.setText("提醒：您的報告少 "+Account+" 小時。");
                            }else{
                                linear_warning.setVisibility(View.GONE);
                                linear_yellow_inside.setBackgroundColor(Color.parseColor("#ffffff"));
                            }


                        }
                    }
                }
                catch (JSONException ex) {
                }
            }
        });
    }


    protected void Find_RSSList_ByWorkID_2(String Date, String WorkID, String Year, final String Week) {

        final RequestQueue mQueue = Volley.newRequestQueue(this);
        final String Path = "http://wtsc.msi.com.tw/IMS/Weekly_Report.asmx/Find_RSSList_ByWorkID_2?WorkID=" + WorkID + "&Year=" + Year + "&Week=" + Week;

        MyLog.d("帶入值" + Date+" -- " +WorkID+" -- "+ Year+" -- " + Week);

        //進行轉換
        Date TurnDate = null;
        try {
            TurnDate = sdf.parse(Date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //getFirstDayOfWeek(TurnDate);
        MyLog.d("目前日期"+ Date);

        mylist.clear();

        for (int i = 0; i < 7; i++) {
            Calendar calendar = Calendar.getInstance();
            calendar.setFirstDayOfWeek(Calendar.SUNDAY);
            calendar.setTime(TurnDate);
            calendar.set(Calendar.DAY_OF_WEEK,
                    calendar.getFirstDayOfWeek() + i); // Sunday

            //設定日期格式
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");

            mylist.add(i, String.valueOf(sdf.format(calendar.getTime()))); //this adds an element to the list.

            MyLog.d("目前日期陣列" + mylist.get(i));

        }

        ArrayAttend.clear();
        ArrayLeave.clear();
        ArrayRSS.clear();
        ArrayOverTime.clear();

        getString(Path, mQueue, new VolleyCallback() {
            @Override
            public void onSuccess(JSONObject result) {

                try {

                    JSONArray UserArray = new JSONArray(result.getString("Key"));
                    if (UserArray.length() > 0) {

                        for (int i = 0; i < UserArray.length(); i++) {

                            MyLog.d("他媽第一個是啥" +String.valueOf(mylist.get(0)));

                            JSONObject IssueData = UserArray.getJSONObject(i);

                            if(i==0){
                                if(IssueData.isNull(String.valueOf(mylist.get(0)))){
                                    ArrayAttend.add(0,"0");
                                }else{
                                    ArrayAttend.add(0,String.valueOf((IssueData.getInt(mylist.get(0))))); //研發階段 10);
                                }

                                if(IssueData.isNull(String.valueOf(mylist.get(1)))){
                                    ArrayAttend.add(1,"0");
                                }else{
                                    ArrayAttend.add(1,String.valueOf((IssueData.getInt(mylist.get(1))))); //研發階段 10);
                                }

                                if(IssueData.isNull(String.valueOf(mylist.get(2)))){
                                    ArrayAttend.add(2,"0");
                                }else{
                                    ArrayAttend.add(2,String.valueOf((IssueData.getInt(mylist.get(2))))); //研發階段 10);
                                }

                                if(IssueData.isNull(String.valueOf(mylist.get(3)))){
                                    ArrayAttend.add(3,"0");
                                }else{
                                    ArrayAttend.add(3,String.valueOf((IssueData.getInt(mylist.get(3))))); //研發階段 10);
                                }

                                if(IssueData.isNull(String.valueOf(mylist.get(4)))){
                                    ArrayAttend.add(4,"0");
                                }else{
                                    ArrayAttend.add(4,String.valueOf((IssueData.getInt(mylist.get(4))))); //研發階段 10);
                                }

                                if(IssueData.isNull(String.valueOf(mylist.get(5)))){
                                    ArrayAttend.add(5,"0");
                                }else{
                                    ArrayAttend.add(5,String.valueOf((IssueData.getInt(mylist.get(5))))); //研發階段 10);
                                }

                                if(IssueData.isNull(String.valueOf(mylist.get(6)))){
                                    ArrayAttend.add(6,"0");
                                }else{
                                    ArrayAttend.add(6,String.valueOf((IssueData.getInt(mylist.get(6))))); //研發階段 10);
                                }
                            }

                            if(i==1){
                                if(IssueData.isNull(String.valueOf(mylist.get(0)))){
                                    ArrayLeave.add(0,"0");
                                }else{
                                    ArrayLeave.add(0,String.valueOf((IssueData.getInt(mylist.get(0))))); //研發階段 10);
                                }

                                if(IssueData.isNull(String.valueOf(mylist.get(1)))){
                                    ArrayLeave.add(1,"0");
                                }else{
                                    ArrayLeave.add(1,String.valueOf((IssueData.getInt(mylist.get(1))))); //研發階段 10);
                                }

                                if(IssueData.isNull(String.valueOf(mylist.get(2)))){
                                    ArrayLeave.add(2,"0");
                                }else{
                                    ArrayLeave.add(2,String.valueOf((IssueData.getInt(mylist.get(2))))); //研發階段 10);
                                }

                                if(IssueData.isNull(String.valueOf(mylist.get(3)))){
                                    ArrayLeave.add(3,"0");
                                }else{
                                    ArrayLeave.add(3,String.valueOf((IssueData.getInt(mylist.get(3))))); //研發階段 10);
                                }

                                if(IssueData.isNull(String.valueOf(mylist.get(4)))){
                                    ArrayLeave.add(4,"0");
                                }else{
                                    ArrayLeave.add(4,String.valueOf((IssueData.getInt(mylist.get(4))))); //研發階段 10);
                                }

                                if(IssueData.isNull(String.valueOf(mylist.get(5)))){
                                    ArrayLeave.add(5,"0");
                                }else{
                                    ArrayLeave.add(5,String.valueOf((IssueData.getInt(mylist.get(5))))); //研發階段 10);
                                }

                                if(IssueData.isNull(String.valueOf(mylist.get(6)))){
                                    ArrayLeave.add(6,"0");
                                }else{
                                    ArrayLeave.add(6,String.valueOf((IssueData.getInt(mylist.get(6))))); //研發階段 10);
                                }
                            }

                            if(i==2){
                                if(IssueData.isNull(String.valueOf(mylist.get(0)))){
                                    ArrayRSS.add(0,"0");
                                }else{
                                    ArrayRSS.add(0,String.valueOf((IssueData.getInt(mylist.get(0))))); //研發階段 10);
                                }

                                if(IssueData.isNull(String.valueOf(mylist.get(1)))){
                                    ArrayRSS.add(1,"0");
                                }else{
                                    ArrayRSS.add(1,String.valueOf((IssueData.getInt(mylist.get(1))))); //研發階段 10);
                                }

                                if(IssueData.isNull(String.valueOf(mylist.get(2)))){
                                    ArrayRSS.add(2,"0");
                                }else{
                                    ArrayRSS.add(2,String.valueOf((IssueData.getInt(mylist.get(2))))); //研發階段 10);
                                }

                                if(IssueData.isNull(String.valueOf(mylist.get(3)))){
                                    ArrayRSS.add(3,"0");
                                }else{
                                    ArrayRSS.add(3,String.valueOf((IssueData.getInt(mylist.get(3))))); //研發階段 10);
                                }

                                if(IssueData.isNull(String.valueOf(mylist.get(4)))){
                                    ArrayRSS.add(4,"0");
                                }else{
                                    ArrayRSS.add(4,String.valueOf((IssueData.getInt(mylist.get(4))))); //研發階段 10);
                                }

                                if(IssueData.isNull(String.valueOf(mylist.get(5)))){
                                    ArrayRSS.add(5,"0");
                                }else{
                                    ArrayRSS.add(5,String.valueOf((IssueData.getInt(mylist.get(5))))); //研發階段 10);
                                }

                                if(IssueData.isNull(String.valueOf(mylist.get(6)))){
                                    ArrayRSS.add(6,"0");
                                }else{
                                    ArrayRSS.add(6,String.valueOf((IssueData.getInt(mylist.get(6))))); //研發階段 10);
                                }
                            }

                            if(i==3){
                                if(IssueData.isNull(String.valueOf(mylist.get(0)))){
                                    ArrayOverTime.add(0,"0");
                                }else{
                                    ArrayOverTime.add(0,String.valueOf((IssueData.getInt(mylist.get(0))))); //研發階段 10);
                                }

                                if(IssueData.isNull(String.valueOf(mylist.get(1)))){
                                    ArrayOverTime.add(1,"0");
                                }else{
                                    ArrayOverTime.add(1,String.valueOf((IssueData.getInt(mylist.get(1))))); //研發階段 10);
                                }

                                if(IssueData.isNull(String.valueOf(mylist.get(2)))){
                                    ArrayOverTime.add(2,"0");
                                }else{
                                    ArrayOverTime.add(2,String.valueOf((IssueData.getInt(mylist.get(2))))); //研發階段 10);
                                }

                                if(IssueData.isNull(String.valueOf(mylist.get(3)))){
                                    ArrayOverTime.add(3,"0");
                                }else{
                                    ArrayOverTime.add(3,String.valueOf((IssueData.getInt(mylist.get(3))))); //研發階段 10);
                                }

                                if(IssueData.isNull(String.valueOf(mylist.get(4)))){
                                    ArrayOverTime.add(4,"0");
                                }else{
                                    ArrayOverTime.add(4,String.valueOf((IssueData.getInt(mylist.get(4))))); //研發階段 10);
                                }

                                if(IssueData.isNull(String.valueOf(mylist.get(5)))){
                                    ArrayOverTime.add(5,"0");
                                }else{
                                    ArrayOverTime.add(5,String.valueOf((IssueData.getInt(mylist.get(5))))); //研發階段 10);
                                }

                                if(IssueData.isNull(String.valueOf(mylist.get(6)))){
                                    ArrayOverTime.add(6,"0");
                                }else{
                                    ArrayOverTime.add(6,String.valueOf((IssueData.getInt(mylist.get(6))))); //研發階段 10);
                                }
                            }

                        }
                        //閃爍
                        final Animation animation = new AlphaAnimation(1, 0); // Change alpha from fully visible to invisible
                        animation.setDuration(500); // duration - half a second
                        animation.setInterpolator(new LinearInterpolator()); // do not alter animation rate
                        animation.setRepeatCount(Animation.INFINITE); // Repeat animation infinitely
                        animation.setRepeatMode(Animation.REVERSE); // Reverse animation at the end so the button will fade back in

                        //如果 工作報告 - 請假 + 加班 =應出勤就 正常 否則就閃
                        //if((Double.valueOf(ArrayRSS.get(0))-(Double.valueOf(ArrayLeave.get(0))+Double.valueOf(ArrayOverTime.get(0))))==Double.valueOf(ArrayAttend.get(0))){
                        if((Double.valueOf(ArrayRSS.get(0))-(Double.valueOf(ArrayLeave.get(0))+Double.valueOf(ArrayOverTime.get(0))))==0.0){
//                            text_w7.clearAnimation();
//                            text_w7.setBackgroundColor(Color.parseColor("#00000000"));
//                            text_w7.setTextColor(Color.parseColor("#636c6f"));
                        }else{
//                            text_w7.startAnimation(animation);
//                            text_w7.setBackgroundColor(Color.parseColor("#d21e25"));
//                            text_w7.setTextColor(Color.parseColor("#ffffff"));
                        }

                        //如果 工作報告 - 請假 + 加班 =應出勤就 正常 否則就閃
                        if((Double.valueOf(ArrayRSS.get(1))-(Double.valueOf(ArrayLeave.get(1))+Double.valueOf(ArrayOverTime.get(1))))==0.0){
//                            text_w1.clearAnimation();
//                            text_w1.setBackgroundColor(Color.parseColor("#00000000"));
//                            text_w1.setTextColor(Color.parseColor("#636c6f"));
                        }else{
                            if(Integer.valueOf(BaseWeekofYear)==Integer.valueOf(Week) && Integer.valueOf(BaseCheckShowWeek) < 3) // 如果在當週  日期<星期幾的話就不閃 因為還沒發生
                            {
//                                text_w1.clearAnimation();
//                                text_w1.setBackgroundColor(Color.parseColor("#00000000"));
//                                text_w1.setTextColor(Color.parseColor("#636c6f"));
                            }else{
//                                text_w1.startAnimation(animation);
//                                text_w1.setBackgroundColor(Color.parseColor("#d21e25"));
//                                text_w1.setTextColor(Color.parseColor("#ffffff"));
                            }

                        }

                        //如果 工作報告 - 請假 + 加班 =應出勤就 正常 否則就閃
                        if((Double.valueOf(ArrayRSS.get(2))-(Double.valueOf(ArrayLeave.get(2))+Double.valueOf(ArrayOverTime.get(2))))==0.0){
//                            text_w2.clearAnimation();
//                            text_w2.setBackgroundColor(Color.parseColor("#00000000"));
//                            text_w2.setTextColor(Color.parseColor("#636c6f"));
                        }else{
                            if(Integer.valueOf(BaseWeekofYear)==Integer.valueOf(Week) && Integer.valueOf(BaseCheckShowWeek) < 4) // 如果在當週  日期<星期幾的話就不閃 因為還沒發生
                            {
//                                text_w2.clearAnimation();
//                                text_w2.setBackgroundColor(Color.parseColor("#00000000"));
//                                text_w2.setTextColor(Color.parseColor("#636c6f"));
                            }else{
//                                text_w2.startAnimation(animation);
//                                text_w2.setBackgroundColor(Color.parseColor("#d21e25"));
//                                text_w2.setTextColor(Color.parseColor("#ffffff"));
                            }

                        }
                        MyLog.d("禮拜三 工作報告" + String.valueOf(Double.valueOf(ArrayRSS.get(3))));
                        MyLog.d("禮拜三 請假" + String.valueOf(Double.valueOf(ArrayLeave.get(3))));
                        MyLog.d("禮拜三 加班" + String.valueOf(Double.valueOf(ArrayOverTime.get(3))));
                        //如果 工作報告 - 請假 + 加班 =應出勤就 正常 否則就閃
                        if((Double.valueOf(ArrayRSS.get(3))-(Double.valueOf(ArrayLeave.get(3))+Double.valueOf(ArrayOverTime.get(3))))==0.0){
//                            text_w3.clearAnimation();
//                            text_w3.setBackgroundColor(Color.parseColor("#00000000"));
//                            text_w3.setTextColor(Color.parseColor("#636c6f"));
                        }else{
                            if(Integer.valueOf(BaseWeekofYear)==Integer.valueOf(Week) && Integer.valueOf(BaseCheckShowWeek) < 5) // 如果在當週  日期<星期幾的話就不閃 因為還沒發生
                            {
//                                text_w3.clearAnimation();
//                                text_w3.setBackgroundColor(Color.parseColor("#00000000"));
//                                text_w3.setTextColor(Color.parseColor("#636c6f"));
                            }else{
//                                text_w3.startAnimation(animation);
//                                text_w3.setBackgroundColor(Color.parseColor("#d21e25"));
//                                text_w3.setTextColor(Color.parseColor("#ffffff"));
                            }

                        }

                        //如果 工作報告 - 請假 + 加班 =應出勤就 正常 否則就閃
                        if((Double.valueOf(ArrayRSS.get(4))-(Double.valueOf(ArrayLeave.get(4))+Double.valueOf(ArrayOverTime.get(4))))==0.0){
//                            text_w4.clearAnimation();
//                            text_w4.setBackgroundColor(Color.parseColor("#00000000"));
//                            text_w4.setTextColor(Color.parseColor("#636c6f"));
                        }else{
                            if(Integer.valueOf(BaseWeekofYear)==Integer.valueOf(Week) && Integer.valueOf(BaseCheckShowWeek) < 6) // 如果在當週  日期<星期幾的話就不閃 因為還沒發生
                            {
//                                text_w4.clearAnimation();
//                                text_w4.setBackgroundColor(Color.parseColor("#00000000"));
//                                text_w4.setTextColor(Color.parseColor("#636c6f"));
                            }else{
//                                text_w4.startAnimation(animation);
//                                text_w4.setBackgroundColor(Color.parseColor("#d21e25"));
//                                text_w4.setTextColor(Color.parseColor("#ffffff"));
                            }

                        }

                        //如果 工作報告 - 請假 + 加班 =應出勤就 正常 否則就閃
                        if((Double.valueOf(ArrayRSS.get(5))-(Double.valueOf(ArrayLeave.get(5))+Double.valueOf(ArrayOverTime.get(5))))==0.0){
//                            text_w5.clearAnimation();
//                            text_w5.setBackgroundColor(Color.parseColor("#00000000"));
//                            text_w5.setTextColor(Color.parseColor("#636c6f"));
                        }else{
                            if(Integer.valueOf(BaseWeekofYear)==Integer.valueOf(Week) && Integer.valueOf(BaseCheckShowWeek) < 7) // 如果在當週  日期<星期幾的話就不閃 因為還沒發生
                            {
//                                text_w5.clearAnimation();
//                                text_w5.setBackgroundColor(Color.parseColor("#00000000"));
//                                text_w5.setTextColor(Color.parseColor("#636c6f"));
                            }else{
//                                text_w5.startAnimation(animation);
//                                text_w5.setBackgroundColor(Color.parseColor("#d21e25"));
//                                text_w5.setTextColor(Color.parseColor("#ffffff"));
                            }

                        }

                        //如果 工作報告 - 請假 + 加班 =應出勤就 正常 否則就閃
                        if((Double.valueOf(ArrayRSS.get(6))-(Double.valueOf(ArrayLeave.get(6))+Double.valueOf(ArrayOverTime.get(6))))==0.0){
//                            text_w6.clearAnimation();
//                            text_w6.setBackgroundColor(Color.parseColor("#00000000"));
//                            text_w6.setTextColor(Color.parseColor("#636c6f"));
                        }else{
//                            text_w6.startAnimation(animation);
//                            text_w6.setBackgroundColor(Color.parseColor("#d21e25"));
//                            text_w6.setTextColor(Color.parseColor("#ffffff"));
                        }

                    }
//                    for (int a = 0; a < 7; a++) {
//                        MyLog.d("測試Attend" + a +" -- " + ArrayAttend.get(a));
//                        MyLog.d("測試Leave" + a +" -- " + ArrayLeave.get(a));
//                        MyLog.d("測試RSS" + a +" -- " + ArrayRSS.get(a));
//                        MyLog.d("測試OverTime" + a +" -- " + ArrayOverTime.get(a));
//                    }
                }
                catch (JSONException ex) {
                }
            }
        });
    }

//    public static Date getFirstDayOfWeek(Date date) {
//        Calendar calendar = Calendar.getInstance();
//        calendar.setFirstDayOfWeek(Calendar.SUNDAY);
//        calendar.setTime(date);
//        calendar.set(Calendar.DAY_OF_WEEK,
//                calendar.getFirstDayOfWeek()); // Sunday
//        return calendar.getTime();
//    }

//    public static ArrayList<String> getFirstDayOfWeekArray(Date date) { // 抓當週星期日~ 星期六的日期
//
//        mylist.clear();
//
//        for (int i = 0; i < 7; i++) {
//            Calendar calendar = Calendar.getInstance();
//            calendar.setFirstDayOfWeek(Calendar.SUNDAY);
//            calendar.setTime(date);
//            calendar.set(Calendar.DAY_OF_WEEK,
//                    calendar.getFirstDayOfWeek() + i); // Sunday
//
//            //設定日期格式
//            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
//
//            mylist.add(i,String.valueOf(sdf.format(calendar.getTime()))); //this adds an element to the list.
//
//            MyLog.d("目前日期陣列"+ mylist.get(i));
//        }
////        D0=mylist.get(0);
//
//        return mylist;
//    }

    public static void getString(String Url, RequestQueue mQueue, final VolleyCallback callback) {

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

//    //跳至新的頁面新增日誌
//    private class Action implements View.OnClickListener {
//
//        public void onClick(View v) {
//            //TextView text_show_month = (TextView) findViewById(R.id.text_show_month);
//            //text_show_month.setText("btn_never_add");
//            Intent intent = new Intent();
//            intent.setClass(msibook_workreport_remake.this, msibook_rd_new.class);
//            Bundle bundle = new Bundle();
//            bundle.putString("UserID",ID);
//            bundle.putString("Date",tv_ShowDate.getText().toString());
//            bundle.putString("Owner",Owner);
//            intent.putExtras(bundle);
//            startActivityForResult(intent,1);
//
//        }
//    }

    public void setDate(View view) {
        tv_totalHR.setText("");
        ncalendar.setVisibility(View.VISIBLE);
        ncalendar.post(new Runnable() {
            @Override
            public void run() {
                currentDate = cal.format(date);
                ncalendar.setDate(currentDate);
            }
        });
    }

    @Override
    public void onCalendarChanged(LocalDate date) {

        tv_ShowDate.setText(date.getYear() + "/" + date.getMonthOfYear() + "/" + date.getDayOfMonth() );

        currentDate = sdf.format(date.toDate());  //當期日期

        Calendar calendar = Calendar.getInstance();
        //calendar.setFirstDayOfWeek(Calendar.SUNDAY);
        calendar.setTime(date.toDate());
        MoveYear = String.valueOf(calendar.get(Calendar.YEAR));
        MoveWeekofYear = String.valueOf(calendar.get(Calendar.WEEK_OF_YEAR));
        MoveWeek = String.valueOf(calendar.get(Calendar.DAY_OF_WEEK));
        //testuse.setText(MoveWeekofYear);
        MyLog.d("當前年" + MoveYear +"當前週次" + MoveWeekofYear + "當前星期" +MoveWeek);

        GetTodaylog(currentDate);
        Find_RSSList_ByWorkID(UserData.WorkID,currentDate);
        Find_RSSList_ByWorkID_2(currentDate,UserData.WorkID,MoveYear,MoveWeekofYear);
        MyLog.d("dateTime::" + currentDate);
    }

    public class MyAdapter extends BaseAdapter {

        private LayoutInflater myInflater;
        private List<Log> logs;

        public MyAdapter(Context context, List<Log> logs){
            myInflater = LayoutInflater.from(context);
            this.logs = logs;
        }

        private class ViewHolder {
            TextView tv_Model;
            TextView tv_hour;
            TextView tv_Subject;
            public ViewHolder(TextView tv_Model, TextView tv_hour, TextView tv_Subject){
                this.tv_Model = tv_Model;
                this.tv_hour = tv_hour;
                this.tv_Subject = tv_Subject;
            }
        }

        //getCount()就是可以取得到底有多少列
        @Override
        public int getCount() {
            return logs.size();
        }

        //取得某一列的內容
        @Override
        public Object getItem(int arg0) {
            return logs.get(arg0);
        }

        //取得某一列的id
        @Override
        public long getItemId(int position) {
            return logs.indexOf(getItem(position));
        }

        //修改某一列View的內容
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            MyAdapter.ViewHolder holder = null;
            if(convertView==null){

                convertView = myInflater.inflate(R.layout.msibook_rd_total_list_item1, null);
                holder = new MyAdapter.ViewHolder(
                        (TextView) convertView.findViewById(R.id.tv_Model),
                        (TextView) convertView.findViewById(R.id.tv_hour),
                        (TextView) convertView.findViewById(R.id.tv_Subject)
                );
                convertView.setTag(holder);
            }else{
                holder = (MyAdapter.ViewHolder) convertView.getTag();
            }
            Log log = (Log)getItem(position);


            holder.tv_Model.setText("MS-"+log.getModel());

            holder.tv_hour.setText(log.getHR());

            holder.tv_Subject.setText(log.getSubject());


            return convertView;
        }
    }

    public class Log {
        private String ID;
        private String Model;
        private String HR;
        private String Subject;
        private String Type;
        private String F_CostSum;

        public Log(String ID,String Model,String HR,String Subject,String Type,String F_CostSum) {
            this.ID = ID;
            this.Model = Model;
            this.HR = HR;
            this.Subject = Subject;
            this.Type = Type;
            this.F_CostSum = F_CostSum;
        }
        public String getID(){
            return ID;
        }
        public String getModel(){
            return Model;
        }
        public void setModel(String Model){
            this.Model = Model;
        }
        public String getHR(){
            return HR;
        }
        public void setHR(String HR){
            this.HR = HR;
        }
        public String getSubject(){
            return Subject;
        }
        public void setSubject(String Subject){
            this.Subject = Subject;
        }
        public String getType(){
            return Type;
        }
        public void setType(String Type){
            this.Type = Type;
        }
        public String getF_CostSum(){
            return F_CostSum;
        }

    }


}
