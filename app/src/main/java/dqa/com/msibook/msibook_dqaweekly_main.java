package dqa.com.msibook;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class msibook_dqaweekly_main extends AppCompatActivity implements OnChartValueSelectedListener {
    private ListView mListView;
    private String test456;
    private TextView tesView;

    private MyAdapter mMyAdapter;
    private DropDownAdapter mDropDownAdapter;
    private DropDownAdapterweek mDropDownAdapterweek;


    private List<String> ArrayWeek = new ArrayList<String>();
    private List<String> ArrayDeptName = new ArrayList<String>();
    private List<String> ArrayDeptID = new ArrayList<String>();


    private Spinner spinnerdp;
    private Spinner spinner_week;
    private String SetF_Year;

    private Integer check_now_week;
    private Button btnback;
    private Button btnnext;

    //總fram
    private FrameLayout frame1;
    private Chart chart1;

    //稼動率圖表
    private PieChart mChart1;
    private Typeface tf;

    //請假欄位
    private LinearLayout linear_leave;
    private LinearLayout linear_leave_2;
    private TextView main_leave_number;
    private TextView main_leave_title;

    //稼動率欄位
    private LinearLayout linear_utirate;
    private Button btn_utirateinfo;

    private String putManpower_status;//帶到第二頁小視窗
    private String putWorkhour_status;
    private String putMessage;

    //編制率使用
    private Double HR;

    //加班欄位
    private LinearLayout linear_overhour;
    private LinearLayout linear_overhour_2;
    private TextView main_overhour_number;
    private TextView main_overhour_title;
    private Button btn_overhourinfo;

    //稼動率欄位
    private TextView main_utirate_number;
    private TextView main_utirate_title;

    //編制欄位
    private LinearLayout linear_manpower;
    private LinearLayout linear_manpower_2;
    private TextView main_totalman_number;
    private TextView main_expectman_number;
    private TextView main_manpower_title;
    private TextView main_Manpower_icon;
    //工時欄位
    private LinearLayout linear_workhour;
    private LinearLayout linear_workhour_2;
    private TextView main_realworkhour_number;
    private TextView main_expecthour_number;
    private TextView main_workhour_title;
    private TextView main_Workhour_icon;
    //專案欄位
    private LinearLayout linear_project;
    private LinearLayout linear_project_2;
    private TextView main_project_number;
    private TextView main_project_title;
    //偵測狀態欄位
    private TextView man_power_ic_status;
    private TextView workhour_ic_status;
    //private TextView manpower_workout_title_status; //提示訊息

    //負載狀態
    //private LinearLayout loading_message;
    //private LinearLayout loading_message_1;

    //摘要欄位
    private LinearLayout linear_summary;
    private TextView main_summary_number;
    private TextView main_summary_title;
    private Button summary_add_btn;

    private LinearLayout lindepart;

    private Double CC;//實際出勤

    private String putEtradp;
    private String putEtrawk;
    private String putEtraYear;
    //帶到第二頁的變數判斷
    private String putEtraDepID;
    private ProgressDialog progressBar;

    private String getlogingID;

    private String LoginPath;

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;

    private ImageView imageView;
    private PopupWindow popupWindow;
    private LinearLayout main_linearLayout1;
    private LinearLayout main_linearLayout2;
    private LinearLayout main_linearLayout3;
    private LinearLayout main_linearLayout4;
    private LinearLayout main_linearLayout5;
    private LinearLayout main_linearLayout6;
    private LinearLayout main_linearLayout7;

    //該部門每月目前總計加班時數
    private Double Dpt_overtime_MonthTotal;
    private TextView dpt_overtime_month_total;
    private Double answer;

    private String put_LastWeekTotalHour;
    private String put_LastMonthTotalHour;

    //摘要送出紐
    private EditText txt_summary_content;

    private static final int REQUEST_EXTERNAL_STORAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msibook_dqaweekly_main);

        Log.w("WrokID",String.valueOf(UserData.WorkID));

        //設定登入權限路徑
        getlogingID = UserData.WorkID;//抓第一頁登入工號
        String[] Keyman = {"10015812", "10016109","10015657","10010670","10016049","10003275","10010059","10015667","10016295","10015635"};//部門權限開放
        if (Arrays.asList(Keyman).contains(getlogingID)){
            LoginPath = GetServiceData.DQAWeekly_Login_P1;
        }else{
            LoginPath = GetServiceData.DQAWeekly_Login_P2 + "?WorkID="+getlogingID;
        }

        lindepart = (LinearLayout) findViewById(R.id.linear_spinnerdp);

        dpt_overtime_month_total = (TextView) findViewById(R.id.dpt_overtime_month_total);

        tesView = (TextView) findViewById(R.id.textView123);
        progressBar = new ProgressDialog(this);
        progressBar.setCancelable(true);
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.setMessage("資料載入中");

        btn_open_pop = (Button) findViewById(R.id.btn_creat_pop);//漢堡列按鈕

        Calendar now_week = Calendar.getInstance();
        check_now_week = now_week.get(Calendar.WEEK_OF_YEAR);
        //Log.w("目前是第幾週",String.valueOf(check_now_week));

        //漢堡列點選事件
        btn_open_pop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                addAnimation();//圖片旋轉動畫
                if(popupWindow == null){
                    showPopupWindow();//顯示popwindow
                }else if(popupWindow.isShowing()){
                    popupWindow.dismiss();
                }
                else{
                    popupWindow.showAsDropDown(lindepart,-300,0);
                }
            }
        });

        spinner_week = (Spinner) findViewById(R.id.spinner_week);//宣告週次下拉選單
        spinnerdp = (Spinner) findViewById(R.id.spinner_depart);//宣告部門下拉選單

        btnback = (Button) findViewById(R.id.btn_spinnerdp_back);
        btnnext = (Button) findViewById(R.id.btn_spinnerdp_next);

        //稼動率圖表
        mChart1 = (PieChart) findViewById(R.id.chart1);
        mChart1.setUsePercentValues(true);//如果被啟用，在圖表內的值繪製在百分之，而不是與它們的原始值。規定的值ValueFormatter進行格式化，然後以百分比規定。設定使用百分比值
        mChart1.getDescription().setEnabled(false);//右下角描述
        mChart1.setExtraOffsets(0, 0, 0, 0);//設置額外的偏移   整個圓圈的大小
        mChart1.setDragDecelerationFrictionCoef(0.95f);//設置拖拉減速摩擦係數
        //mChart1.setExtraOffsets(0.f, 0.f, 0.f, 0.f);//設置額外的偏移
        mChart1.setDrawHoleEnabled(true);//設置繪製孔啟用
        mChart1.setTransparentCircleColor(Color.WHITE);//設置透明圓形顏色
        mChart1.setTransparentCircleAlpha(110);//設置透明圓Alpha
        mChart1.setHoleRadius(90f);//設置孔半徑
        mChart1.setTransparentCircleRadius(90f);//設置透明圓半徑
        mChart1.setDrawCenterText(true);//設置繪製中心文本
        mChart1.setRotationAngle(0);//設置旋轉角度
        mChart1.setRotationEnabled(true);//設置旋轉啟用
        mChart1.setHighlightPerTapEnabled(true);//設置突出顯示已啟用PerTap
        // add a selection listener
        mChart1.setOnChartValueSelectedListener(this);//設置在圖表值選擇的監聽器
        mChart1.animateY(1400, Easing.EasingOption.EaseInOutQuad);//動畫Y
        Legend l = mChart1.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setEnabled(false);

        //總表
        frame1 = (FrameLayout) findViewById(R.id.frame1);
        chart1 = (Chart) findViewById(R.id.chart1);

        //請假欄位
        linear_leave = (LinearLayout) findViewById(R.id.Linear_Leave);
        linear_leave_2 = (LinearLayout) findViewById(R.id.Linear_Leave_2);
        main_leave_number = (TextView) findViewById(R.id.main_Leave_number);
        main_leave_number.setText("");
        main_leave_title = (TextView) findViewById(R.id.main_Leave_title);

        //稼動率欄位
        linear_utirate = (LinearLayout) findViewById(R.id.linear_utirate);
        btn_utirateinfo = (Button) findViewById(R.id.btn_utirateinfo);

        //加班欄位
        linear_overhour = (LinearLayout) findViewById(R.id.Linear_Overhour);
        linear_overhour_2 = (LinearLayout) findViewById(R.id.Linear_Overhour_2);
        main_overhour_number = (TextView) findViewById(R.id.main_Overhour_number);
        main_overhour_title = (TextView) findViewById(R.id.main_Overhour_title);
        btn_overhourinfo = (Button) findViewById(R.id.btn_overhourinfo);

        //稼動率欄位
        main_utirate_number = (TextView) findViewById(R.id.main_Utirate_number);
        main_utirate_title = (TextView) findViewById(R.id.main_Utirate_title);

        //人力欄位
        linear_manpower = (LinearLayout) findViewById(R.id.Linear_Manpower);
        linear_manpower_2 = (LinearLayout) findViewById(R.id.Linear_Manpower_2);
        main_totalman_number = (TextView) findViewById(R.id.main_Total_number);
        main_expectman_number = (TextView) findViewById(R.id.main_ExpectMan_number);
        main_manpower_title = (TextView) findViewById(R.id.main_Manpower_title);
        main_Manpower_icon = (TextView) findViewById(R.id.main_Manpower_icon);//圖

        //工時欄位
        linear_workhour = (LinearLayout) findViewById(R.id.Linear_Workhour);
        linear_workhour_2 = (LinearLayout) findViewById(R.id.Linear_Workhour_2);
        main_realworkhour_number = (TextView) findViewById(R.id.main_Realworkhour_number);
        main_expecthour_number = (TextView) findViewById(R.id.main_Expecthour_number);
        main_workhour_title = (TextView) findViewById(R.id.main_Workhour_title);
        main_Workhour_icon = (TextView) findViewById(R.id.main_Workhour_icon);

        //專案欄位
        linear_project = (LinearLayout) findViewById(R.id.Linear_Project);
        linear_project_2 = (LinearLayout) findViewById(R.id.Linear_Project_2);
        main_project_number = (TextView) findViewById(R.id.main_Project_number);
        main_project_title = (TextView) findViewById(R.id.main_Project_title);

        //負載狀態
        //loading_message = (LinearLayout) findViewById(R.id.loading_message);
        //loading_message_1 = (LinearLayout) findViewById(R.id.loading_message_1);

        //偵測狀態欄位
        man_power_ic_status = (TextView) findViewById(R.id.man_power_ic_status);
        workhour_ic_status = (TextView) findViewById(R.id.workhour_ic_status);
        //manpower_workout_title_status = (TextView) findViewById(R.id.manpower_workout_title_status); //提示訊息

        //摘要欄位
        linear_summary = (LinearLayout) findViewById(R.id.Linear_Summary);
        main_summary_number = (TextView) findViewById(R.id.main_Summary_number);
        main_summary_title = (TextView) findViewById(R.id.main_Summary_title);
        summary_add_btn = (Button) findViewById(R.id.summary_add_btn);

        Find_Get_Week();
        Find_Dept_List();


        //跳下一個部門
        btnnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(popupWindow == null){
                    if (ArrayDeptID.size() == spinnerdp.getSelectedItemPosition() + 1) {
                        //don't doing
                    } else {
                        spinnerdp.setSelection(spinnerdp.getSelectedItemPosition() + 1);
                    }
                }
                else if(popupWindow.isShowing()){
                    popupWindow.dismiss();
                }else {
                    if (ArrayDeptID.size() == spinnerdp.getSelectedItemPosition() + 1) {
                        //don't doing
                    } else {
                        spinnerdp.setSelection(spinnerdp.getSelectedItemPosition() + 1);
                    }
                }
            }
        });


        //跳上一個部門
        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(popupWindow == null){
                    spinnerdp.setSelection(spinnerdp.getSelectedItemPosition() - 1);
                }
                else if(popupWindow.isShowing()){
                    popupWindow.dismiss();
                }else {
                    spinnerdp.setSelection(spinnerdp.getSelectedItemPosition() - 1);
                }
            }
        });

        //稼動Info 點選事件
        btn_utirateinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int []location=new int[2];
                v.getLocationOnScreen(location);
                int x=location[0];//获取当前位置的横坐标
                int y=location[1];

                Intent intent = new Intent(msibook_dqaweekly_main.this, msibook_dqaweekly_main_info_utirateinfo.class);


                intent.putExtra("x_Location",String.valueOf(x));
                Log.w("x_Location",String.valueOf(x));

                intent.putExtra("y_Location",String.valueOf(y));
                Log.w("y_Location",String.valueOf(y));

                intent.putExtra("putManpower_status",putManpower_status);

                intent.putExtra("putWorkhour_status",putWorkhour_status);

                intent.putExtra("putMessage",putMessage);


                msibook_dqaweekly_main.this.startActivity(intent);

            }
        });

        // 人力Click事件
        linear_manpower.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(popupWindow == null){
                    if (spinnerdp.getSelectedItem() != null && spinner_week.getSelectedItem() != null) {
                        switch (event.getAction()) {
                            case MotionEvent.ACTION_DOWN:
                                linear_manpower.setBackgroundColor(Color.parseColor("#e2e2e2"));
                                return true;
                            case MotionEvent.ACTION_UP:
                                linear_manpower.setBackgroundColor(Color.parseColor("#FFFFFF"));
                                Intent intent = new Intent();
                                intent.putExtra("CC", String.format("%.1f", CC));//代實際工時給下一頁
                                intent.putExtra("Title", putEtradp);

                                intent.putExtra("RowType", "人力");
                                //給第二頁Week
                                intent.putExtra("Week", putEtrawk);
                                //給第二頁Year
                                intent.putExtra("Year", putEtraYear);//代年到下一頁
                                //給第二頁部門代號
                                intent.putExtra("ChoiceDepID", putEtraDepID);

                                //從MainActivity 到Main2Activity
                                intent.setClass(msibook_dqaweekly_main.this, msibook_dqaweekly_main_page2.class);
                                //開啟Activity
                                startActivity(intent);
                                return true;
                        }
                    }
                }
                else if(popupWindow.isShowing()){
                    popupWindow.dismiss();
                }else {
                    if (spinnerdp.getSelectedItem() != null && spinner_week.getSelectedItem() != null) {
                        switch (event.getAction()) {
                            case MotionEvent.ACTION_DOWN:
                                linear_manpower.setBackgroundColor(Color.parseColor("#e2e2e2"));
                                return true;
                            case MotionEvent.ACTION_UP:
                                linear_manpower.setBackgroundColor(Color.parseColor("#FFFFFF"));
                                Intent intent = new Intent();
                                intent.putExtra("CC", String.format("%.1f", CC));//代實際工時給下一頁
                                intent.putExtra("Title", putEtradp);

                                intent.putExtra("RowType", "人力");
                                //給第二頁Week
                                intent.putExtra("Week", putEtrawk);
                                //給第二頁Year
                                intent.putExtra("Year", putEtraYear);//代年到下一頁
                                //給第二頁部門代號
                                intent.putExtra("ChoiceDepID", putEtraDepID);

                                //從MainActivity 到Main2Activity
                                intent.setClass(msibook_dqaweekly_main.this, msibook_dqaweekly_main_page2.class);
                                //開啟Activity
                                startActivity(intent);
                                return true;
                        }
                    }
                }
                return false;
            }
        });

        //工時Click事件
        linear_workhour.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //判斷漢堡列是否為null
                if(popupWindow == null){
                    if (spinnerdp.getSelectedItem() != null && spinner_week.getSelectedItem() != null) {
                        switch (event.getAction()) {
                            case MotionEvent.ACTION_DOWN:
                                linear_workhour.setBackgroundColor(Color.parseColor("#e2e2e2"));
                                return true;
                            case MotionEvent.ACTION_UP:
                                linear_workhour.setBackgroundColor(Color.parseColor("#FFFFFF"));
                                Intent intent = new Intent();
                                intent.putExtra("CC", String.format("%.1f", CC));//代實際工時給下一頁
                                intent.putExtra("Title", putEtradp);

                                intent.putExtra("RowType", "工時");
                                //給第二頁Week
                                intent.putExtra("Week", putEtrawk);
                                //給第二頁Year
                                intent.putExtra("Year", putEtraYear);//代年到下一頁
                                //給第二頁部門代號
                                intent.putExtra("ChoiceDepID", putEtraDepID);

                                //從MainActivity 到Main2Activity
                                intent.setClass(msibook_dqaweekly_main.this, msibook_dqaweekly_main_page2.class);
                                //開啟Activity
                                startActivity(intent);
                                return true;
                        }
                    }
                    //判斷漢堡列是否展開
                }else if(popupWindow.isShowing()){
                    popupWindow.dismiss();
                }else
                if (spinnerdp.getSelectedItem() != null && spinner_week.getSelectedItem() != null) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            linear_workhour.setBackgroundColor(Color.parseColor("#e2e2e2"));
                            return true;
                        case MotionEvent.ACTION_UP:
                            linear_workhour.setBackgroundColor(Color.parseColor("#FFFFFF"));
                            Intent intent = new Intent();
                            intent.putExtra("CC", String.format("%.1f", CC));//代實際工時給下一頁
                            intent.putExtra("Title", putEtradp);

                            intent.putExtra("RowType", "工時");
                            //給第二頁Week
                            intent.putExtra("Week", putEtrawk);
                            //給第二頁Year
                            intent.putExtra("Year", putEtraYear);//代年到下一頁
                            //給第二頁部門代號
                            intent.putExtra("ChoiceDepID", putEtraDepID);

                            //從MainActivity 到Main2Activity
                            intent.setClass(msibook_dqaweekly_main.this, msibook_dqaweekly_main_page2.class);
                            //開啟Activity
                            startActivity(intent);
                            return true;
                    }
                }
                return false;
            }
        });



        //加班Click事件
        linear_overhour.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(popupWindow == null){
                    if (spinnerdp.getSelectedItem() != null && spinner_week.getSelectedItem() != null) {
                        switch (event.getAction()) {
                            case MotionEvent.ACTION_DOWN:
                                linear_overhour.setBackgroundColor(Color.parseColor("#e2e2e2"));
                                return true;
                            case MotionEvent.ACTION_UP:
                                linear_overhour.setBackgroundColor(Color.parseColor("#FFFFFF"));
                                Intent intent = new Intent();
                                intent.putExtra("CC", String.format("%.1f", CC));//代實際工時給下一頁
                                intent.putExtra("Title", putEtradp);

                                intent.putExtra("RowType", "加班");
                                //給第二頁Week
                                intent.putExtra("Week", putEtrawk);
                                //給第二頁Year
                                intent.putExtra("Year", putEtraYear);//代年到下一頁
                                //給第二頁部門代號
                                intent.putExtra("ChoiceDepID", putEtraDepID);

                                //從MainActivity 到Main2Activity
                                intent.setClass(msibook_dqaweekly_main.this, msibook_dqaweekly_main_page2.class);
                                //開啟Activity
                                startActivity(intent);
                                return true;
                        }
                    }
                }
                else if(popupWindow.isShowing()){
                    popupWindow.dismiss();
                }else {
                    if (spinnerdp.getSelectedItem() != null && spinner_week.getSelectedItem() != null) {
                        switch (event.getAction()) {
                            case MotionEvent.ACTION_DOWN:
                                linear_overhour.setBackgroundColor(Color.parseColor("#e2e2e2"));
                                return true;
                            case MotionEvent.ACTION_UP:
                                linear_overhour.setBackgroundColor(Color.parseColor("#FFFFFF"));
                                Intent intent = new Intent();
                                intent.putExtra("CC", String.format("%.1f", CC));//代實際工時給下一頁
                                intent.putExtra("Title", putEtradp);

                                intent.putExtra("RowType", "加班");
                                //給第二頁Week
                                intent.putExtra("Week", putEtrawk);
                                //給第二頁Year
                                intent.putExtra("Year", putEtraYear);//代年到下一頁
                                //給第二頁部門代號
                                intent.putExtra("ChoiceDepID", putEtraDepID);

                                //從MainActivity 到Main2Activity
                                intent.setClass(msibook_dqaweekly_main.this, msibook_dqaweekly_main_page2.class);
                                //開啟Activity
                                startActivity(intent);
                                return true;
                        }
                    }
                }
                return false;
            }
        });

        //加班Info icon 點選事件
//        btn_overhourinfo.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                int []location=new int[2];
//                v.getLocationOnScreen(location);
//                int x=location[0];//获取当前位置的横坐标
//                int y=location[1];
//
//                Intent intent = new Intent(msibook_dqaweekly_main.this, info_overhourinfo.class);
//
//                intent.putExtra("DptTitle", putEtradp);//部門名稱
//
//                intent.putExtra("ChoiceDepID", putEtraDepID);//給第二頁部門代號
//
//                intent.putExtra("Week", putEtrawk);//給第二頁Week
//
//                intent.putExtra("Year", putEtraYear);//給第二頁Year
//
//                intent.putExtra("LastWeekTotalHour",put_LastWeekTotalHour);//上週申請
//
//                intent.putExtra("LastMonthTotalHour",put_LastMonthTotalHour);//上月申報
//
//                intent.putExtra("x_Location",String.valueOf(x));
//                Log.w("x_Location",String.valueOf(x));
//
//                intent.putExtra("y_Location",String.valueOf(y));
//                Log.w("y_Location",String.valueOf(y));
//
//                msibook_dqaweekly_main.this.startActivity(intent);
//            }
//        });

        //請假Click事件
        linear_leave.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(popupWindow == null){
                    if (spinnerdp.getSelectedItem() != null && spinner_week.getSelectedItem() != null) {
                        switch (event.getAction()) {
                            case MotionEvent.ACTION_DOWN:
                                linear_leave.setBackgroundColor(Color.parseColor("#e2e2e2"));//按下灰色
                                return true;
                            case MotionEvent.ACTION_UP:
                                linear_leave.setBackgroundColor(Color.parseColor("#FFFFFF"));//放開白色
                                Intent intent = new Intent();
                                intent.putExtra("CC", String.format("%.1f", CC));//代實際工時給下一頁
                                intent.putExtra("Title", putEtradp);//代部門名稱到下一頁

                                intent.putExtra("RowType", "請假");//代 "請假"到下一頁
                                //給第二頁Week
                                intent.putExtra("Week", putEtrawk);//代 週次到下一頁
                                //給第二頁Year
                                intent.putExtra("Year", putEtraYear);//代年到下一頁
                                //給第二頁部門代號
                                intent.putExtra("ChoiceDepID", putEtraDepID);//代部門ID到下一頁 EX: 21751

                                //從MainActivity 到Main2Activity
                                intent.setClass(msibook_dqaweekly_main.this, msibook_dqaweekly_main_page2.class);//跳轉頁面至第二頁
                                //開啟Activity
                                startActivity(intent);
                                return true;
                        }
                    }
                }
                else if(popupWindow.isShowing()){
                    popupWindow.dismiss();
                }else {
                    if (spinnerdp.getSelectedItem() != null && spinner_week.getSelectedItem() != null) {
                        switch (event.getAction()) {
                            case MotionEvent.ACTION_DOWN:
                                linear_leave.setBackgroundColor(Color.parseColor("#e2e2e2"));//按下灰色
                                return true;
                            case MotionEvent.ACTION_UP:
                                linear_leave.setBackgroundColor(Color.parseColor("#FFFFFF"));//放開白色
                                Intent intent = new Intent();
                                intent.putExtra("CC", String.format("%.1f", CC));//代實際工時給下一頁
                                intent.putExtra("Title", putEtradp);//代部門名稱到下一頁

                                intent.putExtra("RowType", "請假");//代 "請假"到下一頁
                                //給第二頁Week
                                intent.putExtra("Week", putEtrawk);//代 週次到下一頁
                                //給第二頁Year
                                intent.putExtra("Year", putEtraYear);//代年到下一頁
                                //給第二頁部門代號
                                intent.putExtra("ChoiceDepID", putEtraDepID);//代部門ID到下一頁 EX: 21751

                                //從MainActivity 到Main2Activity
                                intent.setClass(msibook_dqaweekly_main.this, msibook_dqaweekly_main_page2.class);//跳轉頁面至第二頁
                                //開啟Activity
                                startActivity(intent);
                                return true;
                        }
                    }
                }
                return false;
            }
        });

        //專案Click事件
        linear_project.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //判斷漢堡列是否為null
                if(popupWindow == null){
                    if (spinnerdp.getSelectedItem() != null && spinner_week.getSelectedItem() != null) {
                        switch (event.getAction()) {
                            case MotionEvent.ACTION_DOWN:
                                linear_project.setBackgroundColor(Color.parseColor("#e2e2e2"));
                                return true;
                            case MotionEvent.ACTION_UP:
                                linear_project.setBackgroundColor(Color.parseColor("#FFFFFF"));
                                Intent intent = new Intent();
                                intent.putExtra("CC", String.format("%.1f", CC));//代實際工時給下一頁
                                intent.putExtra("Title", putEtradp);

                                intent.putExtra("RowType", "專案");
                                //給第二頁Week
                                intent.putExtra("Week", putEtrawk);
                                //給第二頁Year
                                intent.putExtra("Year", putEtraYear);//代年到下一頁
                                //給第二頁部門代號
                                intent.putExtra("ChoiceDepID", putEtraDepID);

                                //從MainActivity 到Main2Activity
                                intent.setClass(msibook_dqaweekly_main.this, msibook_dqaweekly_main_page2.class);
                                //開啟Activity
                                startActivity(intent);
                                return true;
                        }
                    }
                }
                else if(popupWindow.isShowing()){
                    popupWindow.dismiss();
                }else {
                    if (spinnerdp.getSelectedItem() != null && spinner_week.getSelectedItem() != null) {
                        switch (event.getAction()) {
                            case MotionEvent.ACTION_DOWN:
                                linear_project.setBackgroundColor(Color.parseColor("#e2e2e2"));
                                return true;
                            case MotionEvent.ACTION_UP:
                                linear_project.setBackgroundColor(Color.parseColor("#FFFFFF"));
                                Intent intent = new Intent();
                                intent.putExtra("CC", String.format("%.1f", CC));//代實際工時給下一頁
                                intent.putExtra("Title", putEtradp);

                                intent.putExtra("RowType", "專案");
                                //給第二頁Week
                                intent.putExtra("Week", putEtrawk);
                                //給第二頁Year
                                intent.putExtra("Year", putEtraYear);//代年到下一頁
                                //給第二頁部門代號
                                intent.putExtra("ChoiceDepID", putEtraDepID);

                                //從MainActivity 到Main2Activity
                                intent.setClass(msibook_dqaweekly_main.this, msibook_dqaweekly_main_page2.class);
                                //開啟Activity
                                startActivity(intent);
                                return true;
                        }
                    }
                }
                return false;
            }
        });

        //摘要Click事件
        linear_summary.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(popupWindow == null){
                    if (spinnerdp.getSelectedItem() != null && spinner_week.getSelectedItem() != null) {
                        switch (event.getAction()) {
                            case MotionEvent.ACTION_DOWN:
                                linear_summary.setBackgroundColor(Color.parseColor("#e2e2e2"));
                                return true;
                            case MotionEvent.ACTION_UP:
                                linear_summary.setBackgroundColor(Color.parseColor("#FFFFFF"));//放開白色
                                Intent intent = new Intent();

                                intent.putExtra("Title", putEtradp);//代部門名稱到下一頁

                                //給第二頁Week
                                intent.putExtra("Week", putEtrawk);//代 週次到下一頁
                                //給第二頁Year
                                intent.putExtra("Year", putEtraYear);//代年到下一頁
                                //給第二頁部門代號
                                intent.putExtra("ChoiceDepID", putEtraDepID);//代部門ID到下一頁 EX: 21751

                                //給第二頁部門代號
                                intent.putExtra("WorkID", "10015812");//代部門ID到下一頁 EX: 21751

                                //把位置帶到摘要頁方便回首頁時會回選定的該週次
                                intent.putExtra("SpinnerWeek_position",String.valueOf(spinner_week.getSelectedItemPosition()));

                                //把位置帶到摘要頁方便回首頁時會回選定的該部門
                                intent.putExtra("SpinnerDP_position",String.valueOf(spinnerdp.getSelectedItemPosition()));
                                //從MainActivity 到Main2Activity
                                //intent.setClass(msibook_dqaweekly_main.this, Main_summary.class);//跳轉頁面至第二頁
                                //開啟Activity
                                startActivityForResult(intent,1);
                                return true;
                        }
                    }
                }
                else if(popupWindow.isShowing()){
                    popupWindow.dismiss();
                }else {
                    if (spinnerdp.getSelectedItem() != null && spinner_week.getSelectedItem() != null) {
                        switch (event.getAction()) {
                            case MotionEvent.ACTION_DOWN:
                                linear_summary.setBackgroundColor(Color.parseColor("#e2e2e2"));
                                return true;
                            case MotionEvent.ACTION_UP:
                                linear_summary.setBackgroundColor(Color.parseColor("#FFFFFF"));
                                Intent intent = new Intent();
                                intent.putExtra("CC", String.format("%.1f", CC));//代實際工時給下一頁
                                intent.putExtra("Title", putEtradp);

                                intent.putExtra("RowType", "摘要");
                                //給第二頁Week
                                intent.putExtra("Week", putEtrawk);
                                //給第二頁Year
                                intent.putExtra("Year", putEtraYear);//代年到下一頁
                                //給第二頁部門代號
                                intent.putExtra("ChoiceDepID", putEtraDepID);

                                //從MainActivity 到Main2Activity
                                //intent.setClass(msibook_dqaweekly_main.this, Main_summary.class);
                                //開啟Activity
                                startActivityForResult(intent,1);
                                //startActivity(intent);
                                return true;
                        }
                    }
                }
                return false;
            }
        });

    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {

    }

    @Override
    public void onNothingSelected() {

    }

    private Button btn_open_pop;
    private Button btn_close_pop;
    private Button btn_close_popinfo;

    private void addAnimation() {//加入了旋转动画
        RotateAnimation rotateAnimation=new RotateAnimation(0,360, Animation.RELATIVE_TO_SELF,0.5f, Animation.RELATIVE_TO_SELF,0.5f);
        rotateAnimation.setDuration(500);//设置动画时间
        btn_open_pop.setAnimation(rotateAnimation);//设置动画
        btn_open_pop.startAnimation(rotateAnimation);//开始动画
    }

    //漢堡列show popWindow
    private void showPopupWindow(){
        View view= LayoutInflater.from(this).inflate(R.layout.activity_msibook_dqaweekly_popupwindow_layout,null);//获取popupWindow子布局对象
        popupWindow =new PopupWindow(view, ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT,false);//初始化
        popupWindow.setAnimationStyle(R.style.dqaweekly_popupAnim);//设置动画
        popupWindow.showAsDropDown(lindepart,-300,0);//在ImageView控件下方弹出

        //首頁
        main_linearLayout1=(LinearLayout)view.findViewById(R.id.main_lin1);
        main_linearLayout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(msibook_dqaweekly_main.this, "回首頁", Toast.LENGTH_SHORT).show();
                popupWindow.dismiss();
            }
        });

        //部級週報
        main_linearLayout6 = (LinearLayout) view.findViewById(R.id.main_lin6);
        main_linearLayout6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(msibook_dqaweekly_main.this, "回部級週報", Toast.LENGTH_SHORT).show();
                popupWindow.dismiss();

//                Intent intent = new Intent();
//
//                intent.setClass(msibook_dqaweekly_main.this, ProjectActivity.class);//首頁  > 部級週報
//                //開啟Activity
//                startActivity(intent);

                Log.w("部級週報","部級週報");
            }
        });

        //計算公式
        main_linearLayout2=(LinearLayout)view.findViewById(R.id.main_lin2);
        main_linearLayout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(msibook_dqaweekly_main.this, "計算公式", Toast.LENGTH_SHORT).show();
                popupWindow.dismiss();
                initiatePopupWindow();//呼叫pop 計算公式
            }
        });

        //操作說明
        main_linearLayout3=(LinearLayout)view.findViewById(R.id.main_lin3);
        main_linearLayout3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(msibook_dqaweekly_main.this, "操作說明", Toast.LENGTH_SHORT).show();
                popupWindow.dismiss();

//                Intent intent = new Intent();
//                //從MainActivity 到Main2Activity
//                intent.setClass(msibook_dqaweekly_main.this, Main_ViewPager.class);
//                //開啟Activity
//                startActivity(intent);

                Log.w("操作說明","操作說明");
            }
        });
        //版本紀錄
        main_linearLayout4=(LinearLayout)view.findViewById(R.id.main_lin4);
        main_linearLayout4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(msibook_dqaweekly_main.this, "版本紀錄", Toast.LENGTH_SHORT).show();
                popupWindow.dismiss();

                infoPopupWindow();

            }
        });

        //目前版本
        main_linearLayout7=(LinearLayout)view.findViewById(R.id.main_lin7);
        main_linearLayout7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(msibook_dqaweekly_main.this, "目前版本", Toast.LENGTH_SHORT).show();
                popupWindow.dismiss();
                nowversionPopupWindow();//呼叫版本QR_Code
            }
        });

        //檢查更新
        main_linearLayout5=(LinearLayout)view.findViewById(R.id.main_lin5);
        main_linearLayout5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(MainActivity.this, "檢查更新", Toast.LENGTH_SHORT).show();
                popupWindow.dismiss();

                //isUpdate(msibook_dqaweekly_main.this);

                //infoPopupWindow();
            }
        });
    }

    //支援
    private PopupWindow pwindo;
    private void initiatePopupWindow(){
        try{
            // We need to get the instance of the LayoutInflater
            LayoutInflater inflater = (LayoutInflater) msibook_dqaweekly_main.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View layout = inflater.inflate(R.layout.msibook_dqaweekly_help_popup_window,(ViewGroup) findViewById(R.id.popup_element));

            DisplayMetrics metrics = getResources().getDisplayMetrics();
            int screenWidth = (int) (metrics.widthPixels * 0.8);
            int screenHeight = (int) (metrics.heightPixels * 0.78);

            pwindo = new PopupWindow(layout,screenWidth,screenHeight,true);
            pwindo.showAtLocation(layout, Gravity.CENTER,0,0);

            btn_close_pop = (Button) layout.findViewById(R.id.btn_closepop);
            btn_close_pop.setOnClickListener(cancel_button_click_listener);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //資訊
    private PopupWindow pwindoinfo;
    private void infoPopupWindow(){
        try{
            // We need to get the instance of the LayoutInflater
            LayoutInflater inflater = (LayoutInflater) msibook_dqaweekly_main.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View layout = inflater.inflate(R.layout.msibook_dqaweekly_info_popup_window,(ViewGroup) findViewById(R.id.popup_element));

            DisplayMetrics metrics = getResources().getDisplayMetrics();
            int screenWidth = (int) (metrics.widthPixels * 0.8);
            int screenHeight = (int) (metrics.heightPixels * 0.78);

            pwindoinfo = new PopupWindow(layout,screenWidth,screenHeight,true);
            pwindoinfo.showAtLocation(layout, Gravity.CENTER,0,0);

            btn_close_popinfo = (Button) layout.findViewById(R.id.close_info);
            btn_close_popinfo.setOnClickListener(cancelinfo_button_click_listener);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //現在版本
    private void nowversionPopupWindow(){
        try{
            // We need to get the instance of the LayoutInflater
            LayoutInflater inflater = (LayoutInflater) msibook_dqaweekly_main.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View layout = inflater.inflate(R.layout.msibook_dqaweekly_version_popup_window,(ViewGroup) findViewById(R.id.popup_version));

            DisplayMetrics metrics = getResources().getDisplayMetrics();
            int screenWidth = (int) (metrics.widthPixels * 0.8);
            int screenHeight = (int) (metrics.heightPixels * 0.78);

            pwindo = new PopupWindow(layout,screenWidth,screenHeight,true);
            pwindo.showAtLocation(layout, Gravity.CENTER,0,0);

            btn_close_pop = (Button) layout.findViewById(R.id.btn_closepop);
            btn_close_pop.setOnClickListener(cancel_button_click_listener);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private View.OnClickListener cancelinfo_button_click_listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            pwindoinfo.dismiss();
        }
    };

    private View.OnClickListener cancel_button_click_listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            pwindo.dismiss();
        }
    };

    //--------------------------------------------------------------------------------Void--------------------------------------------------------------------------------
    //抓部門代號、名稱 Spinner
    private void Find_Dept_List() {

        ArrayDeptName.clear();
        ArrayDeptID.clear();

        RequestQueue mQueue = Volley.newRequestQueue(this);

        String Path = LoginPath;
        GetServiceData.getString(Path, mQueue, new GetServiceData.VolleyCallback() {
            @Override
            public void onSuccess(JSONObject result) {

                try {
                    JSONArray UserArray = new JSONArray(result.getString("Key"));

                    for (int i = 0; i < UserArray.length(); i++) {
                        JSONObject IssueData = UserArray.getJSONObject(i);

                        String NickName = IssueData.getString("NickName");

                        String DeptID = String.valueOf(IssueData.getInt("DeptID"));

                        ArrayDeptName.add(i, NickName);

                        ArrayDeptID.add(i, DeptID);
                    }
//                    Log.w("wowowowowow",String.valueOf(ArrayWeek.size()));

                    spinnerdp = (Spinner) findViewById(R.id.spinner_depart);

                    mDropDownAdapter = new DropDownAdapter(msibook_dqaweekly_main.this, ArrayDeptName);

                    spinnerdp.setAdapter(mDropDownAdapter);

                    spinnerdp.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {


                        public void onItemSelected(AdapterView adapterView, View view, int position, long id) {

                            DropDownAdapter DropDownAdapter =  (DropDownAdapter)adapterView.getAdapter();

                            DropDownAdapter.selectedItemdp = position;

                            mDropDownAdapter.notifyDataSetChanged();

                            if (ArrayWeek.size() > 0)
                            {
                                Find_WeekReport(ArrayDeptID.get(position),SetF_Year,ArrayWeek.get(spinner_week.getSelectedItemPosition()).replace("週",""));
                                //Find_Weekly_Content(ArrayDeptID.get(spinnerdp.getSelectedItemPosition()),ArrayWeek.get(position).replace("週",""));
                                //Find_Over_Hour(ArrayDeptID.get(position),ArrayWeek.get(spinner_week.getSelectedItemPosition()).replace("週",""));
                            }
                            else
                            {
                                Find_WeekReport(ArrayDeptID.get(position),SetF_Year,"0");
                                //Find_Weekly_Content(ArrayDeptID.get(position),"0");
                                //Find_Over_Hour(ArrayDeptID.get(position),"0");
                            }

                            putEtradp = (String) spinnerdp.getSelectedItem();
                            //抓部門代號到第二頁去
                            putEtraDepID = ArrayDeptID.get(position);
                        }

                        public void onNothingSelected(AdapterView arg0) {

                        }
                    });

                    //Find_WeekReport(ArrayDeptID.get(0),ArrayWeek.get(0));

                } catch (JSONException ex) {

                }

            }
        });

    }

    //讀取週數 Spinner
    private void Find_Get_Week() {
        //顯示  讀取等待時間Bar
        progressBar.show();
        ArrayWeek.clear();

        RequestQueue mQueue = Volley.newRequestQueue(this);

        String Path = GetServiceData.DQAWeeklyPath + "/Get_Week";

        GetServiceData.getString(Path, mQueue, new GetServiceData.VolleyCallback() {
            @Override
            public void onSuccess(JSONObject result) {

                try {
                    JSONArray UserArray = new JSONArray(result.getString("Key"));

                    if (UserArray.length() > 0) {
                        for (int i = 0; i < UserArray.length(); i++) {
                            JSONObject IssueData = UserArray.getJSONObject(i);

                            String F_Year = String.valueOf(IssueData.getInt("F_Year"));
                            SetF_Year = F_Year;

                            String weekID = String.valueOf(IssueData.getInt("F_Week"));

                            ArrayWeek.add(i,weekID + "週");
                            //tesView.setText(UserArray.length());

                        }
                        putEtraYear = SetF_Year;

                        spinner_week = (Spinner) findViewById(R.id.spinner_week);

                        mDropDownAdapterweek = new DropDownAdapterweek(msibook_dqaweekly_main.this, ArrayWeek);

                        spinner_week.setAdapter(mDropDownAdapterweek);

                        if(ArrayWeek.size() >=1) {

                            spinner_week.setSelection(1);

                        }else{

                            spinner_week.setSelection(0);
                        }

                        spinner_week.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
                            public void onItemSelected(AdapterView adapterView, View view, int position, long id) {

                                DropDownAdapterweek DropDownAdapterweek =  (DropDownAdapterweek)adapterView.getAdapter();


                                DropDownAdapterweek.selectedItemweek = position;

                                mDropDownAdapterweek.notifyDataSetChanged();

                                //Find_Get_Week(ArrayWeek.get(position),"1");
                                putEtrawk = (String) spinner_week.getSelectedItem();
                                Log.w("Spinner NUMBER",String.valueOf(spinner_week.getSelectedItemPosition()));
                                //String spStr = String.valueOf(spinner_week.getSelectedItemPosition());
                                //tesView.setText(spStr);
                                //tesView.setText(weekID);

//                                if(spinnerdp.getSelectedItem() != null && spinner_week.getSelectedItem() !=null) {
//                                    Find_WeekReport(ArrayDeptID.get(spinnerdp.getSelectedItemPosition()), ArrayWeek.get(position).replace("週", ""));
//                                }

                                if(spinnerdp.getSelectedItem() != null)
                                {
                                    Find_WeekReport(ArrayDeptID.get(spinnerdp.getSelectedItemPosition()),SetF_Year,ArrayWeek.get(position).replace("週",""));
                                    //Find_Weekly_Content(ArrayDeptID.get(spinnerdp.getSelectedItemPosition()),ArrayWeek.get(position).replace("週",""));
                                    //Find_Over_Hour(ArrayDeptID.get(spinnerdp.getSelectedItemPosition()),ArrayWeek.get(position).replace("週",""));
                                }
                                else
                                {
                                    //Find_WeekReport(ArrayDeptID.get(0),ArrayWeek.get(position).replace("週",""));
                                }

                                //Errorrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrr
                                //Log.w("Dept",ArrayDeptID.get(spinnerdp.getSelectedItemPosition()));

                                Log.w("Week",ArrayWeek.get(spinner_week.getSelectedItemPosition()).replace("週",""));

                                if (check_now_week == Integer.valueOf(ArrayWeek.get(spinner_week.getSelectedItemPosition()).replace("週",""))){
                                    Log.w("判斷為同一週","隱藏");
                                    frame1.setBackgroundColor(Color.parseColor("#ffffff"));//白色背景

                                    linear_utirate.setBackgroundColor(Color.parseColor("#3c3c3c"));//深灰
                                    linear_utirate.getBackground().setAlpha(30);//透明度

                                    chart1.setVisibility(View.INVISIBLE);//稼動率圖表

                                    btn_utirateinfo.setEnabled(false);
                                    btn_overhourinfo.setEnabled(false);

                                    linear_manpower.setBackgroundColor(Color.parseColor("#3c3c3c"));
                                    linear_manpower.getBackground().setAlpha(30);
                                    linear_manpower.setEnabled(false);
                                    linear_manpower_2.setVisibility(View.INVISIBLE);
                                    main_Manpower_icon.setVisibility(View.INVISIBLE);

                                    linear_workhour.setBackgroundColor(Color.parseColor("#3c3c3c"));
                                    linear_workhour.getBackground().setAlpha(30);
                                    linear_workhour.setEnabled(false);
                                    linear_workhour_2.setVisibility(View.INVISIBLE);
                                    main_Workhour_icon.setVisibility(View.INVISIBLE);

                                    linear_overhour.setBackgroundColor(Color.parseColor("#3c3c3c"));
                                    linear_overhour.getBackground().setAlpha(30);
                                    linear_overhour.setEnabled(false);
                                    linear_overhour_2.setVisibility(View.INVISIBLE);
                                    dpt_overtime_month_total.setVisibility(View.INVISIBLE);

                                    linear_leave.setBackgroundColor(Color.parseColor("#3c3c3c"));
                                    linear_leave.getBackground().setAlpha(30);
                                    linear_leave.setEnabled(false);
                                    linear_leave_2.setVisibility(View.INVISIBLE);

                                    linear_project.setBackgroundColor(Color.parseColor("#3c3c3c"));
                                    linear_project.getBackground().setAlpha(30);
                                    linear_project.setEnabled(false);
                                    linear_project_2.setVisibility(View.INVISIBLE);

//                                    loading_message.setBackgroundColor(Color.parseColor("#3c3c3c"));
//                                    loading_message.getBackground().setAlpha(30);
//                                    loading_message.setEnabled(false);
                                    //loading_message_1.setVisibility(View.INVISIBLE);


                                }else{
                                    Log.w("判斷為不同週","顯示");
                                    frame1.setBackgroundColor(Color.parseColor("#e2e2e2"));//按下灰色;//主體
                                    linear_utirate.setBackgroundColor(Color.parseColor("#ffffff"));

                                    chart1.setVisibility(View.VISIBLE);//稼動率圖表

                                    btn_utirateinfo.setEnabled(true);
                                    btn_overhourinfo.setEnabled(true);

                                    linear_manpower.setBackgroundColor(Color.parseColor("#ffffff"));
                                    linear_manpower.setEnabled(true);
                                    linear_manpower_2.setVisibility(View.VISIBLE);
                                    main_Manpower_icon.setVisibility(View.VISIBLE);

                                    linear_workhour.setBackgroundColor(Color.parseColor("#ffffff"));
                                    linear_workhour.setEnabled(true);
                                    linear_workhour_2.setVisibility(View.VISIBLE);
                                    main_Workhour_icon.setVisibility(View.VISIBLE);

                                    linear_overhour.setBackgroundColor(Color.parseColor("#ffffff"));
                                    linear_overhour.setEnabled(true);
                                    linear_overhour_2.setVisibility(View.VISIBLE);
                                    dpt_overtime_month_total.setVisibility(View.VISIBLE);

                                    linear_leave.setBackgroundColor(Color.parseColor("#ffffff"));
                                    linear_leave.setEnabled(true);
                                    linear_leave_2.setVisibility(View.VISIBLE);


                                    linear_project.setBackgroundColor(Color.parseColor("#ffffff"));
                                    linear_project.setEnabled(true);
                                    linear_project_2.setVisibility(View.VISIBLE);

//                                    loading_message.setBackgroundColor(Color.parseColor("#ffffff"));
//                                    loading_message.setEnabled(true);
                                    //loading_message_1.setVisibility(View.VISIBLE);
                                }
                            }

                            public void onNothingSelected(AdapterView arg0) {

                            }
                        });
                    }
                    //關閉-讀取等待時間Bar
                    progressBar.dismiss();

                } catch (JSONException ex) {

                }

            }
        });

    }

    //抓週報內容
    private void Find_WeekReport(final String DeptID,final String Year,final String Week) {

        //顯示  讀取等待時間Bar
        progressBar.show();
        putMessage="";

        RequestQueue mQueue = Volley.newRequestQueue(this);

        String Path = GetServiceData.DQAWeeklyPath +"/Find_Weekly_Report?DeptID=" + DeptID + "&Year=" + Year+ "&Week=" + Week;

        GetServiceData.getString(Path, mQueue, new GetServiceData.VolleyCallback() {
            @Override
            public void onSuccess(JSONObject result) {

                try {
                    JSONArray UserArray = new JSONArray(result.getString("Key"));

                    if (UserArray.length() > 0) {

                        JSONObject IssueData = UserArray.getJSONObject(0);

                        String TotalMan = String.valueOf(IssueData.getLong("TotalMan"));

                        String ExpectHour = String.valueOf(IssueData.getLong("ExpectHour"));

//                       if(IssueData.getDouble("RealHour")%2==0||IssueData.getDouble("RealHour")%2==1){
//                            String RealHour = String.valueOf(IssueData.getLong("RealHour"));
//                        }else{
//                            String RealHour = String.valueOf(IssueData.getDouble("RealHour"));
//                        }
                        String RealHour = String.valueOf(IssueData.getLong("RealHour"));

                        String LeaveHour = String.valueOf(IssueData.getDouble("LeaveHour"));

                        String OverTimeHour = String.valueOf(IssueData.getDouble("OverTimeHour"));

                        String ExpectCost = String.valueOf(IssueData.getLong("ExpectCost"));

                        String RealCost = String.valueOf(IssueData.getLong("RealCost"));

                        String ReportCount = String.valueOf(IssueData.getLong("ReportCount"));

                        String Proj_Rate = String.valueOf(IssueData.getLong("Proj_Rate"));

                        String Edu_Rate = String.valueOf(IssueData.getLong("Edu_Rate"));

                        String Other_Rate = String.valueOf(IssueData.getLong("Other_Rate"));

                        String FacShare_Cost = String.valueOf(IssueData.getLong("FacShare_Cost"));

                        String OutSource_Cost = String.valueOf(IssueData.getLong("OutSource_Cost"));

                        String Delay = String.valueOf(IssueData.getLong("Delay"));

                        String Night = String.valueOf(IssueData.getLong("Night"));

                        String ExpectMan = String.valueOf(IssueData.getLong("ExpectMan"));

                        String CostSum = String.valueOf(IssueData.getLong("CostSum"));

                        String Project_Count = String.valueOf(IssueData.getLong("Project_Count"));

                        String RealWorkHour = String.valueOf(IssueData.getLong("RealWorkHour"));

                        String Rate = String.valueOf(IssueData.getLong("Rate"));

                        String DayExpectHour = String.valueOf(IssueData.getLong("DayExpectHour"));

                        String MonthTotalHour = String.valueOf(IssueData.getDouble("MonthTotalHour"));//月累計

                        String LastWeekTotalHour = String.valueOf(IssueData.getDouble("LastWeekTotalHour"));//上週申請
                        put_LastWeekTotalHour = LastWeekTotalHour;

                        String LastMonthTotalHour = String.valueOf(IssueData.getDouble("LastMonthTotalHour"));//上月申報
                        put_LastMonthTotalHour = LastMonthTotalHour;

                        String Summery_Count =  String.valueOf(IssueData.getInt("Summery_Count"));//摘要數量

                        List<MainItem> MainItem_List = new ArrayList<MainItem>();

                        double C = (IssueData.getDouble("DayExpectHour") - IssueData.getDouble("LeaveHour") + IssueData.getDouble("OverTimeHour"));//應出勤 - 請假 + 加班 = 實際出勤
                        CC = C;

                        ArrayList<PieEntry> entries = new ArrayList<PieEntry>();//稼動率圖表給資料用

                        double total_A = (C /IssueData.getDouble("DayExpectHour")*100); //(實際出勤) / 應出勤  = 稼動率   百分比

                        Log.w("稼動率", String.valueOf(total_A));

                        //編制率公式(實際人數/編制人數)
                        HR = Double.valueOf(TotalMan) / Double.valueOf(ExpectMan);
                        //Log.w("HRHRHRHR",String.valueOf(HR));

                        main_leave_number.setText(LeaveHour);//請假

                        //摘要數量
                        main_summary_number.setText(Summery_Count);

                        Log.w("Summery_Count",Summery_Count);

                        //加班累積使用率
                        answer = ((Double.valueOf(MonthTotalHour) / (Integer.valueOf(TotalMan) * 46)) * 100);
                        dpt_overtime_month_total.setText(String.format("%.0f",answer) + "%");

                        //編制率 & 稼動率  合併判斷
                        //HR <1
                        if (HR < 1 && total_A >=90 && total_A<=100){//編制率 小於1 、 稼動率介於  90~100
                            //manpower_workout_title_status.setText("專案需求減少");//專案不如預期
                            putMessage="專案需求減少";
                            man_power_ic_status.setVisibility(View.VISIBLE);
                            man_power_ic_status.setBackgroundResource(R.mipmap.dqaweekly_ic_status_shortage);//短缺
                            putManpower_status="ic_status_shortage";
                            workhour_ic_status.setVisibility(View.VISIBLE);
                            workhour_ic_status.setBackgroundResource(R.mipmap.dqaweekly_ic_status_fullload);//滿載
                            putWorkhour_status="ic_status_fullload";
                        }else if (HR < 1 && total_A >100){    //編制率 小於1 、 稼動率大於  100
                            //manpower_workout_title_status.setText("專案轉換階段\n設備未到齊\n專案時程延誤");
                            putMessage="專案轉換階段\n設備未到齊\n專案時程延誤";
                            man_power_ic_status.setVisibility(View.VISIBLE);
                            man_power_ic_status.setBackgroundResource(R.mipmap.dqaweekly_ic_status_shortage);//短缺
                            putManpower_status="ic_status_shortage";
                            workhour_ic_status.setVisibility(View.VISIBLE);
                            workhour_ic_status.setBackgroundResource(R.mipmap.dqaweekly_ic_status_overload);//超載
                            putWorkhour_status="ic_status_overload";
                            RSS_Data_List(DeptID,SetF_Year,Week);//稼動率>100 撈加班
                        }else if (HR < 1 && total_A <90){    //編制率 小於1 、 稼動率小於  90
                            //manpower_workout_title_status.setText("專案轉換階段\n設備未到齊");
                            putMessage="專案轉換階段\n設備未到齊";
                            man_power_ic_status.setVisibility(View.VISIBLE);
                            man_power_ic_status.setBackgroundResource(R.mipmap.dqaweekly_ic_status_shortage);//短缺
                            putManpower_status="ic_status_shortage";
                            workhour_ic_status.setVisibility(View.VISIBLE);
                            workhour_ic_status.setBackgroundResource(R.mipmap.dqaweekly_ic_status_lowload);//低載
                            putWorkhour_status="ic_status_lowload";
                            Leave_Data_List(DeptID,SetF_Year,Week);//稼動率<90 撈請假
                        }

                        //HR = 1
                        if (HR == 1 && total_A >=90 && total_A<=100){//編制率 等於1 、 稼動率介於  90~100
                            //manpower_workout_title_status.setText("無異常");
                            putMessage="無異常";
                            man_power_ic_status.setVisibility(View.VISIBLE);
                            man_power_ic_status.setBackgroundResource(R.mipmap.dqaweekly_ic_status_fulledition);//滿編
                            putManpower_status="ic_status_fulledition";
                            workhour_ic_status.setVisibility(View.VISIBLE);
                            workhour_ic_status.setBackgroundResource(R.mipmap.dqaweekly_ic_status_fullload);//滿載
                            putWorkhour_status="ic_status_fullload";
                        }else if (HR == 1 && total_A >100){    //編制率 等於1 、 稼動率大於  100
                            //manpower_workout_title_status.setText("專案時間重疊\n專案時程延誤\n客戶額外要求");
                            putMessage="專案時間重疊\n專案時程延誤\n客戶額外要求";
                            man_power_ic_status.setVisibility(View.VISIBLE);
                            man_power_ic_status.setBackgroundResource(R.mipmap.dqaweekly_ic_status_fulledition);//滿編
                            putManpower_status="ic_status_fulledition";
                            workhour_ic_status.setVisibility(View.VISIBLE);
                            workhour_ic_status.setBackgroundResource(R.mipmap.dqaweekly_ic_status_overload);//超載
                            putWorkhour_status="ic_status_overload";
                            RSS_Data_List(DeptID,SetF_Year,Week);//稼動率>100 撈加班
                        }else if (HR == 1 && total_A <90){    //編制率 等於1 、 稼動率小於  90
                            //manpower_workout_title_status.setText("專案轉換階段\n設備未到齊");
                            putMessage="專案轉換階段\n設備未到齊";
                            man_power_ic_status.setVisibility(View.VISIBLE);
                            man_power_ic_status.setBackgroundResource(R.mipmap.dqaweekly_ic_status_fulledition);//滿編
                            putManpower_status="ic_status_fulledition";
                            workhour_ic_status.setVisibility(View.VISIBLE);
                            workhour_ic_status.setBackgroundResource(R.mipmap.dqaweekly_ic_status_lowload);//低載
                            putWorkhour_status="ic_status_lowload";
                            Leave_Data_List(DeptID,SetF_Year,Week);//稼動率<90 撈請假
                        }

                        //HR > 1
                        if (HR > 1 && total_A >=90 && total_A<=100){//編制率 大於1 、 稼動率介於  90~100
                            //manpower_workout_title_status.setText("專案需求增加");//專案超出預期
                            putMessage="專案需求增加";
                            man_power_ic_status.setVisibility(View.VISIBLE);
                            man_power_ic_status.setBackgroundResource(R.mipmap.dqaweekly_ic_status_super);//超編
                            putManpower_status="ic_status_super";
                            workhour_ic_status.setVisibility(View.VISIBLE);
                            workhour_ic_status.setBackgroundResource(R.mipmap.dqaweekly_ic_status_fullload);//滿載
                            putWorkhour_status="ic_status_fullload";
                        }else if (HR > 1 && total_A >100){    //編制率 大於1 、 稼動率大於  100
                            //manpower_workout_title_status.setText("專案需求增加\n專案時間重疊\n客戶額外要求");
                            putMessage="專案需求增加\n專案時間重疊\n客戶額外要求";
                            man_power_ic_status.setVisibility(View.VISIBLE);
                            man_power_ic_status.setBackgroundResource(R.mipmap.dqaweekly_ic_status_super);//超編
                            putManpower_status="ic_status_super";
                            workhour_ic_status.setVisibility(View.VISIBLE);
                            workhour_ic_status.setBackgroundResource(R.mipmap.dqaweekly_ic_status_overload);//超載
                            putWorkhour_status="ic_status_overload";
                            RSS_Data_List(DeptID,SetF_Year,Week);//稼動率>100 撈加班
                        }else if (HR > 1 && total_A <90){    //編制率 大於1 、 稼動率小於  90
                            //manpower_workout_title_status.setText("專案需求增加\n設備未到齊");
                            putMessage="專案需求增加\n設備未到齊";
                            man_power_ic_status.setVisibility(View.VISIBLE);
                            man_power_ic_status.setBackgroundResource(R.mipmap.dqaweekly_ic_status_super);//超編
                            putManpower_status="ic_status_super";
                            workhour_ic_status.setVisibility(View.VISIBLE);
                            workhour_ic_status.setBackgroundResource(R.mipmap.dqaweekly_ic_status_lowload);//低載
                            putWorkhour_status="ic_status_lowload";
                            Leave_Data_List(DeptID,SetF_Year,Week);//稼動率<90 撈請假
                        }


//                        //main_utirate_number.setText(String.format("%.2f", A));//稼動率
//                        if(A > 100){//紅色
//                            main_utirate_number.setText(String.format("%.2f", A));//稼動率
//                            main_utirate_number.setTextColor(Color.parseColor("#f0625d"));
//                        }
//                        if(A < 90){//綠色
//                            main_utirate_number.setText(String.format("%.2f", A));//稼動率
//                            main_utirate_number.setTextColor(Color.parseColor("#46aa36"));
//                        }
//                        if(A >= 90 & A <= 100){//黑色
//                            main_utirate_number.setText(String.format("%.2f", A));//稼動率
//                            main_utirate_number.setTextColor(Color.parseColor("#656565"));
//                        }
                        if((total_A /100)>1){
                            mChart1.setCenterText(generateCenterSpannableText(String.format("%.2f", total_A)+"%"+"\n稼動率",240,98,93));
                            mChart1.setCenterTextSize(40);
                        }

                        else if ((total_A /100)<0.9){
                            mChart1.setCenterText(generateCenterSpannableText(String.format("%.2f", total_A)+"%"+"\n稼動率",70,170,54));
                            mChart1.setCenterTextSize(40);
                        }

                        else if ((total_A /100)<=1 && ((total_A /100)>=0.9)){
                            mChart1.setCenterText(generateCenterSpannableText(String.format("%.2f", total_A)+"%"+"\n稼動率",101, 101, 101));
                            mChart1.setCenterTextSize(40);
                        }

                        //稼動率圖表寫入資料
                        float a = (float) (total_A / 100);

                        if (a > 1) {
                            entries.add(new PieEntry(a, ""));
                        } else {
                            entries.add(new PieEntry(a, ""));

                            entries.add(new PieEntry(1 - a, ""));
                        }

                        PieDataSet dataSet = new PieDataSet(entries, "Election Results");

                        dataSet.setSliceSpace(3f);
                        dataSet.setSelectionShift(5f);


                        // add a lot of colors
                        ArrayList<Integer> colors = new ArrayList<Integer>();
                        colors.add(Color.parseColor("#90afcb"));// 百分比 藍色
                        colors.add(Color.parseColor("#e6e7e8"));//百分比 灰色
                        colors.add(ColorTemplate.getHoloBlue());
                        dataSet.setColors(colors);
                        dataSet.setDrawValues(false);//周圍線條資訊拿掉

                        dataSet.setValueLinePart1OffsetPercentage(80.f);
                        dataSet.setValueLinePart1Length(0.3f);
                        dataSet.setValueLinePart2Length(0.4f);

                        //dataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
                        dataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);

                        PieData data = new PieData(dataSet);
                        data.setValueFormatter(new PercentFormatter());
                        data.setValueTextSize(11f);
                        data.setValueTextColor(Color.BLACK);
                        data.setValueTypeface(tf);

                        mChart1.setData(data);

                        // undo all highlights
                        mChart1.highlightValues(null);

                        mChart1.invalidate();


                        //加班判斷  & 工時判斷
                        Double O = Double.valueOf(OverTimeHour);//加班
                        Double Realwroktime = Double.valueOf(String.format("%.1f", C));//實際工時
                        Double Expectwroktime = Double.valueOf(DayExpectHour);//應有工時
                        main_realworkhour_number.setText(String.format("%.1f", C));//實際工時
                        main_expecthour_number.setText(DayExpectHour);//應有工時
                        if (check_now_week != Integer.valueOf(Week)) {
                            if (Realwroktime < Expectwroktime) {//實際+加班 < 應出勤
                                main_Workhour_icon.setVisibility(View.VISIBLE);
                                main_Workhour_icon.setBackgroundResource(R.mipmap.dqaweekly_ic_workhr_low);//工時低
                                main_overhour_number.setText(OverTimeHour);//加班
                                main_overhour_number.setTextColor(Color.parseColor("#656565"));
                                main_overhour_number.setTypeface(null, Typeface.NORMAL);


                                main_realworkhour_number.setText(String.format("%.1f", C));//實際工時
                                main_realworkhour_number.setTextColor(Color.parseColor("#46aa36"));//綠色
                                //main_realworkhour_number.setTypeface(null,Typeface.NORMAL);
                                main_realworkhour_number.setTypeface(null, Typeface.BOLD);
                            }

                            if (Realwroktime > Expectwroktime) {
                                main_Workhour_icon.setVisibility(View.VISIBLE);
                                main_Workhour_icon.setBackgroundResource(R.mipmap.dqaweekly_ic_workhr_heigh);//工時高
                                main_overhour_number.setText(OverTimeHour);//加班
                                main_overhour_number.setTextColor(Color.parseColor("#656565"));
                                //main_overhour_number.setTypeface(null,Typeface.BOLD);
                                main_overhour_number.setTypeface(null, Typeface.NORMAL);

                                main_realworkhour_number.setText(String.format("%.1f", C));//實際工時
                                main_realworkhour_number.setTextColor(Color.parseColor("#f0625d"));//紅色
                                main_realworkhour_number.setTypeface(null, Typeface.BOLD);
                            }

                            if (Realwroktime - Expectwroktime == 0) {
                                main_Workhour_icon.setVisibility(View.INVISIBLE);////工時正常 隱藏
                                main_overhour_number.setText(OverTimeHour);//加班
                                main_overhour_number.setTextColor(Color.parseColor("#656565"));
                                main_overhour_number.setTypeface(null, Typeface.NORMAL);

                                main_realworkhour_number.setText(String.format("%.1f", C));//實際工時
                                main_realworkhour_number.setTextColor(Color.parseColor("#618db5"));//藍色
                                //main_realworkhour_number.setTypeface(null,Typeface.NORMAL);
                                main_realworkhour_number.setTypeface(null, Typeface.BOLD);
                            }
                        }

                        //人力判斷
                        Double Totalman = Double.valueOf(TotalMan);
                        Double Epcman = Double.valueOf(ExpectMan);
                        if (Totalman > Epcman){
                            main_Manpower_icon.setBackgroundResource(R.mipmap.dqaweekly_ic_manpower_exceed);//工時高
                            main_totalman_number.setText(TotalMan);//目前編制
                            main_totalman_number.setTextColor(Color.parseColor("#f0625d"));//紅色
                            main_totalman_number.setTypeface(null,Typeface.BOLD);
                            main_expectman_number.setText(ExpectMan);//應有編制
                        }else if(Totalman < Epcman){
                            main_Manpower_icon.setBackgroundResource(R.mipmap.dqaweekly_ic_manpower_shortage);//工時高
                            main_totalman_number.setText(TotalMan);//目前編制
                            main_totalman_number.setTextColor(Color.parseColor("#656565"));//黑
                            //main_totalman_number.setTypeface(null,Typeface.NORMAL);
                            main_totalman_number.setTypeface(null,Typeface.BOLD);
                            main_expectman_number.setText(ExpectMan);//應有編制
                        }else{
                            main_Manpower_icon.setBackgroundResource(R.mipmap.dqaweekly_ic_manpower_full);//工時高
                            main_totalman_number.setText(TotalMan);//目前編制
                            main_totalman_number.setTextColor(Color.parseColor("#656565"));//黑
                            //main_totalman_number.setTypeface(null,Typeface.NORMAL);
                            main_totalman_number.setTypeface(null,Typeface.BOLD);
                            main_expectman_number.setText(ExpectMan);//應有編制
                        }

                        main_project_number.setText(Project_Count);//專案



                        MainItem MainItem = new MainItem("稼動率", "", "", "", "", String.format("%.2f", total_A), "", true);

                        //MainItem_List.add(0, MainItem);

                        MainItem = new MainItem("請假", "", "", "", "", LeaveHour, "", true);

                        // MainItem_List.add(1, MainItem);

                        MainItem = new MainItem("加班", "", "", RealHour, ExpectHour, OverTimeHour, "", true);

                        //MainItem_List.add(2, MainItem);

                        MainItem = new MainItem("工時", "", "", String.format("%.1f", C), ExpectHour, OverTimeHour, "/", false);

                        //MainItem_List.add(3, MainItem);

                        MainItem = new MainItem("專案", "", "", "", "", Project_Count, "", true);

                        //MainItem_List.add(4, MainItem);

                        MainItem = new MainItem("摘要", "", "", "", "", "1", "", true);

                        // MainItem_List.add(5, MainItem);

                        MainItem = new MainItem("編制", TotalMan, ExpectMan, "", "", "", "/", false);

                        // MainItem_List.add(6, MainItem);

                        //mMyAdapter = new MyAdapter();

                        //mMyAdapter._MainItem_List = MainItem_List;

                        //mListView.setAdapter(mMyAdapter);



//                            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                                @Override
//                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//                                        Log.w("Click", "TestClick");
//
//                                        Intent intent = new Intent();
//                                        //帶給第二頁Title
//                                        MyAdapter MyAdapter = (MyAdapter) parent.getAdapter();
//
//                                        intent.putExtra("CC",String.format("%.1f", CC));
//
//                                    if (MyAdapter._MainItem_List.get(position).GetTitle() == "稼動率"){
//
//                                    }else {
//                                        intent.putExtra("Title", putEtradp);
//
//                                        intent.putExtra("RowType", MyAdapter._MainItem_List.get(position).GetTitle());
//                                        //給第二頁Week
//                                        intent.putExtra("Week",putEtrawk);
//                                        //給第二頁部門代號
//                                        intent.putExtra("ChoiceDepID", putEtraDepID);
//
//
//
//                                        //從MainActivity 到Main2Activity
//                                        intent.setClass(MainActivity.this, Main2Activity.class);
//                                        //開啟Activity
//                                        startActivity(intent);
//
//                                    }
//
//                                }
//                            });
                    }else{
                        //摘要數量   判斷服務沒資料
                        main_summary_number.setText("0");
                    }
                    //關閉-讀取等待時間Bar
                    progressBar.dismiss();
                } catch (JSONException ex) {

                }

            }
        });
    }

    //稼動率 generateCenterSpannableText
    private SpannableString generateCenterSpannableText(String Title, int a, int b, int c) {

        SpannableString s = new SpannableString(Title);
        s.setSpan(new ForegroundColorSpan(Color.rgb(a,b,c)), 0, s.length()-5, 0);
        s.setSpan(new ForegroundColorSpan(Color.rgb(101, 101, 101)), s.length()-5, s.length(), 0);
        s.setSpan(new RelativeSizeSpan(0.5f), s.length()-5, s.length(), s.SPAN_EXCLUSIVE_EXCLUSIVE);

        return s;
    }

    //抓出請假最高
    private void Leave_Data_List(String DeptID,String Year,String Week) {

        //顯示  讀取等待時間Bar
        progressBar.show();

        RequestQueue mQueue = Volley.newRequestQueue(this);

        String Path = GetServiceData.DQAWeeklyPath + "/Leave_Data_List?DeptID=" + DeptID + "&Year=" + Year+ "&Week=" + Week;

        GetServiceData.getString(Path, mQueue, new GetServiceData.VolleyCallback() {
            @Override
            public void onSuccess(JSONObject result) {

                try {

                    JSONArray UserArray = new JSONArray(result.getString("Key"));

                    if (UserArray.length() > 0) {

                        JSONObject IssueData = UserArray.getJSONObject(0);

                        String F_Hour = String.valueOf(IssueData.getDouble("F_Hour"));

                        String F_Name = String.valueOf(IssueData.getString("F_Name"));

                        putMessage = putMessage + "\n員工自我排休("+F_Name+")";
                        Log.w("PUPUPUP MESSAGE",String.valueOf(putMessage));
                    }


                }
                catch (JSONException ex) {

                    Log.w("Json",ex.toString());
                }

            }
        });


    }

    //抓出加班出現的專案
    private void RSS_Data_List(String DeptID,String Year,String Week) {

        //顯示  讀取等待時間Bar
        progressBar.show();

        RequestQueue mQueue = Volley.newRequestQueue(this);

        String Path = GetServiceData.DQAWeeklyPath + "/RSS_Data_List?DeptID=" + DeptID + "&Year=" + Year+ "&Week=" + Week;

        GetServiceData.getString(Path, mQueue, new GetServiceData.VolleyCallback() {
            @Override
            public void onSuccess(JSONObject result) {

                try {

                    JSONArray UserArray = new JSONArray(result.getString("Key"));

                    if (UserArray.length() > 0) {

                        JSONObject IssueData = UserArray.getJSONObject(0);

                        String F_Model = String.valueOf(IssueData.getString("F_Model"));

                        putMessage = putMessage + "\n因專案("+F_Model+")進行,\n故同仁加班";
                    }


                }
                catch (JSONException ex) {

                    Log.w("Json",ex.toString());
                }

            }
        });


    }

    //--------------------------------------------------------------------------------Adapter--------------------------------------------------------------------------------
    ///*****MyAdapter
    public class MyAdapter extends BaseAdapter {

        private class ViewHolder {
            TextView txtDept1;
            TextView txtutilization1;
            TextView txtDept2;
            TextView txtutilization2;
            TextView txtDept3;
            TextView txtutilization3;
            TextView txtDept4;
            TextView txtutilization4;

            public ViewHolder(TextView txtDept1, TextView txtutilization1, TextView txtDept2, TextView txtutilization2, TextView txtDept3, TextView txtutilization3, TextView txtDept4, TextView txtutilization4) {
                this.txtDept1 = txtDept1;
                this.txtutilization1 = txtutilization1;
                this.txtDept2 = txtDept2;
                this.txtutilization2 = txtutilization2;
                this.txtDept3 = txtDept3;
                this.txtutilization3 = txtutilization3;
                this.txtDept4 = txtDept4;
                this.txtutilization4 = txtutilization4;
            }
        }

        private LayoutInflater myInflater;
        private List<ProjectActivity2_utilization_listview> utilization_list;

        public MyAdapter(Context context, List<ProjectActivity2_utilization_listview> movie) {
            myInflater = LayoutInflater.from(context);
            this.utilization_list = movie;
        }

        @Override
        public int getCount() {
            return utilization_list.size();
        }

        @Override
        public Object getItem(int arg0) {
            return utilization_list.get(arg0);
        }

        @Override
        public long getItemId(int position) {
            return utilization_list.indexOf(getItem(position));
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder = null;
            if (convertView == null) {
                convertView = myInflater.inflate(R.layout.msibook_dqaweekly_utilization_listview, null);
                holder = new ViewHolder(
                        (TextView) convertView.findViewById(R.id.Text_Dept_1),
                        (TextView) convertView.findViewById(R.id.Text_utilization1),
                        (TextView) convertView.findViewById(R.id.Text_Dept_2),
                        (TextView) convertView.findViewById(R.id.Text_utilization2),
                        (TextView) convertView.findViewById(R.id.Text_Dept_3),
                        (TextView) convertView.findViewById(R.id.Text_utilization3),
                        (TextView) convertView.findViewById(R.id.Text_Dept_4),
                        (TextView) convertView.findViewById(R.id.Text_utilization4)

                );
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            ProjectActivity2_utilization_listview movie = (ProjectActivity2_utilization_listview) getItem(position);


            int color_title[] = {Color.WHITE, Color.WHITE, Color.YELLOW};
//時間的顏色

            int color_time[] = {Color.WHITE, Color.WHITE, Color.YELLOW};
//背景的顏色

            int color_back[] = {Color.BLACK, Color.BLUE, Color.BLACK};
//時間是否顯示

            int time_vis[] = {View.VISIBLE, View.GONE, View.VISIBLE};


            holder.txtDept1.setText(movie.getDept1());

            holder.txtutilization1.setText(movie.getutilization1());
            holder.txtutilization1.setTextColor(Color.parseColor(setutilizationtextcolor(movie.getutilization1())));


            holder.txtDept2.setText(movie.getDept2());

            holder.txtutilization2.setText(movie.getutilization2());
            holder.txtutilization2.setTextColor(Color.parseColor(setutilizationtextcolor(movie.getutilization2())));
            holder.txtDept3.setText(movie.getDept3());

            holder.txtutilization3.setText(movie.getutilization3());
            holder.txtutilization3.setTextColor(Color.parseColor(setutilizationtextcolor(movie.getutilization3())));
            holder.txtDept4.setText(movie.getDept4());

            holder.txtutilization4.setText(movie.getutilization4());
            holder.txtutilization4.setTextColor(Color.parseColor(setutilizationtextcolor(movie.getutilization4())));

            return convertView;
        }


        public String setutilizationtextcolor(String number) {
            Double afterConvert;
            String color ="";

            try {
                afterConvert = Double.parseDouble(number);
            } catch (Exception e) {
                afterConvert = 0.00;
            }
            if ((afterConvert / 100) > 1) {


                color = "#f0625d";
            } else if ((afterConvert / 100) == 0.0) {

                color ="#c6c6c6";

            } else if ((afterConvert / 100) < 0.9) {

                color ="#46aa36";

            } else if ((afterConvert / 100) <= 1 && (afterConvert / 100 >= 0.9)) {

                color = "#656565";
            }
            return  color;
        }
    }

    //*****DropdownAdapter
    public class DropDownAdapter extends BaseAdapter {

        private LayoutInflater mLayInf;

        private List<String> ArrayDeptName;

        private Context ProjectContext;

        public int selectedItemdp;

        public DropDownAdapter(Context context,  List<String> ArrayDeptName)
        {
            mLayInf = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            ProjectContext = context;

            this.ArrayDeptName = ArrayDeptName;
        }
        @Override
        public int getCount() {
            return ArrayDeptName.size();
        }

        @Override
        public Object getItem(int position) {
            return ArrayDeptName.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View v = new View(ProjectContext);

            v = mLayInf.inflate(R.layout.msibook_dqaweekly_spinnertextlayout, parent, false);

            TextView style = (TextView) v.findViewById(R.id.style);

            style.setText(ArrayDeptName.get(position));

            Log.w("test","test");

            return v;
        }
        @Override
        public View getDropDownView(int position,View convertView,ViewGroup parent)
        {
            View v = null;
            v = mLayInf.inflate(R.layout.msibook_dqaweekly_spinnertextlayout, parent, false);

            TextView style = (TextView) v.findViewById(R.id.style);

            style.setText(ArrayDeptName.get(position));

            // If this is the selected item position
            if (position == selectedItemdp) {
                style.setBackgroundColor(Color.parseColor("#848484"));//灰色
            }
            else {
                // for other views
                style.setBackgroundColor(Color.parseColor("#618db5"));//藍色
            }
            return v;
        }

    }

    //****DropdownAdapterweek
    public class DropDownAdapterweek extends BaseAdapter {

        private LayoutInflater mLayInf;

        private List<String> ArrayDeptName;

        private Context ProjectContext;

        public int selectedItemweek;

        public DropDownAdapterweek(Context context,  List<String> ArrayDeptName)
        {
            mLayInf = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            ProjectContext = context;

            this.ArrayDeptName = ArrayDeptName;
        }
        @Override
        public int getCount() {
            return ArrayDeptName.size();
        }

        @Override
        public Object getItem(int position) {
            return ArrayDeptName.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View v = new View(ProjectContext);

            v = mLayInf.inflate(R.layout.msibook_dqaweekly_spinnertextlayout2, parent, false);

            TextView style = (TextView) v.findViewById(R.id.style2);

            style.setText(ArrayDeptName.get(position));

            Log.w("test","test");

            return v;
        }
        @Override
        public View getDropDownView(int position,View convertView,ViewGroup parent)
        {
            View v = null;
            v = mLayInf.inflate(R.layout.msibook_dqaweekly_spinnertextlayout, parent, false);

            TextView style = (TextView) v.findViewById(R.id.style);

            style.setText(ArrayDeptName.get(position));

            // If this is the selected item position
            if (position == selectedItemweek) {
                style.setBackgroundColor(Color.parseColor("#848484"));//灰色
            }
            else {
                // for other views
                style.setBackgroundColor(Color.parseColor("#618db5"));//藍色
            }
            return v;
        }

    }


    //--------------------------------------------------------------------------------Item--------------------------------------------------------------------------------
    //MainItem
    public class MainItem {
        String Title;
        String now_man;
        String expect_man;
        String now_hour;
        String expect_hour;
        String count;
        String slash;
        boolean Type;

        public MainItem(String Title, String now_man, String expect_man, String now_hour, String expect_hour, String count, String slash, boolean Type) {
            this.Title = Title;
            this.now_man = now_man;
            this.expect_man = expect_man;
            this.now_hour = now_hour;
            this.expect_hour = expect_hour;
            this.count = count;
            this.slash = slash;
            this.Type = Type;
        }

        public String GetTitle() {
            return this.Title;
        }

        public String Getnow_man() {
            return this.now_man;
        }

        public String Getexpect_man() {
            return this.expect_man;
        }

        public String Getnow_hour() {
            return this.now_hour;
        }

        public String Getexpect_hour() {
            return this.expect_hour;
        }

        public String Getcount() {
            return this.count;
        }

        public String Getslash() {
            return slash;
        }

        public boolean GetType() {
            return Type;
        }

    }

    //ProjectActivity2_utilization_listview Item
    public class ProjectActivity2_utilization_listview {

        private String Dept1;
        private String utilization1;
        private String Dept2;
        private String utilization2;
        private String Dept3;
        private String utilization3;
        private String Dept4;
        private String utilization4;
        public ProjectActivity2_utilization_listview(String Dept1,String utilization1,String Dept2,String utilization2,String Dept3,String utilization3,String Dept4,String utilization4) {

            this.Dept1 = Dept1;
            this.utilization1 = utilization1;
            this.Dept2 = Dept2;
            this.utilization2 = utilization2;
            this.Dept3 = Dept3;
            this.utilization3 = utilization3;
            this.Dept4 = Dept4;
            this.utilization4 = utilization4;

        }

        public String getDept1(){
            return Dept1;
        }
        public String getDept2(){
            return Dept2;
        }
        public String getDept3(){
            return Dept3;
        }
        public String getDept4(){
            return Dept4;
        }
        public void setDept(String name){
            this.Dept1 = Dept1;
        }
        public String getutilization1(){
            return utilization1;
        }
        public String getutilization2(){
            return utilization2;
        }
        public String getutilization3(){
            return utilization3;
        }
        public String getutilization4(){
            return utilization4;
        }

        public void setutilization1(String time){
            this.utilization1 = utilization1;
        }
        public void setutilization2(String time){
            this.utilization2 = utilization2;
        }
        public void setutilization3(String time){
            this.utilization3 = utilization3;
        }
        public void setutilization4(String time){
            this.utilization4 = utilization4;
        }
    }

}
