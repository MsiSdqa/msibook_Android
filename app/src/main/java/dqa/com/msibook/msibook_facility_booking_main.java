package dqa.com.msibook;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class msibook_facility_booking_main extends AppCompatActivity implements GestureDetector.OnGestureListener {

    private Context mContext;
    private View mView;

    private TextView textView_F_Facility;
    private TextView textView_F_AssetNo;

    private String GetF_Facility;
    private String GetF_AssetNo;
    private String GetF_Master_ID;
    private String GetF_Is_Restrict;
    private String GetLocation;

    private TextView textView_borrow_name;
    private TextView textView_borrow_time;
    private TextView textView_borrow_end_time;

    private Date tdt;//宣告用來加減用的日期變數
    private Date td;//預設是現在時間，會因為加減而變化

    private Button back_month;
    private TextView text_show_month;
    private Button next_month;

    private LinearLayout linear_wanna_booking;
    private ProgressDialog progressBar;

    //*********************************日曆宣告Star
    private GestureDetector gestureDetector = null;
    private CalendarAdapter calV = null;
    private GridView gridView = null;
    private TextView topText = null;
    private static int jumpMonth = 0;      //每次滑动，增加或减去一個月,默認為0（即顯示當前月）
    private static int jumpYear = 0;       // 滑動跨越一年，則增加或者減去一年，默認為0 (即當年)
    private int year_c = 0;
    private int month_c = 0;
    private int day_c = 0;
    private String currentDate = "";
    private Bundle bd=null;//發送參數
    private Bundle bun=null;//接收參數
    private String ruzhuTime;
    private String lidianTime;
    private String state="";
    //*********************************日曆宣告End

    private List<Find_Fac_Schedule_Item> Find_Fac_Schedule_Item_List = new ArrayList<Find_Fac_Schedule_Item>();


    public void Find_Fac_Schedule_List(String F_Master_ID) {

        //顯示  讀取等待時間Bar
        progressBar.show();

        Find_Fac_Schedule_Item_List.clear();

        RequestQueue mQueue = Volley.newRequestQueue(this);

        HTTPSTrustManager.allowAllSSL();//信任所有证书，信任憑證
        String Path = GetServiceData.ServicePath + "/Find_Fac_Schedule_List?F_Master_ID=" + F_Master_ID;

        GetServiceData.getString(Path, mQueue, new GetServiceData.VolleyCallback() {
            @Override
            public void onSuccess(JSONObject result) {

                try {

                    JSONArray UserArray = new JSONArray(result.getString("Key"));

                    for (int i = 0; i < UserArray.length(); i++) {
                        Date F_StartDate = new Date();
                        Date F_EndDate = new Date();
                        JSONObject IssueData = UserArray.getJSONObject(i);

                        String F_CreateDate = String.valueOf(IssueData.getString("F_CreateDate"));

                        String F_Owner = String.valueOf(IssueData.getString("F_Owner"));

                        String F_Keyin = String.valueOf(IssueData.getString("F_Keyin"));

                        String F_Master_ID = String.valueOf(IssueData.getInt("F_Master_ID"));

                        String dtStart = String.valueOf(IssueData.getString("F_StartDate"));
                        Log.w("dtStart",dtStart.toString());

                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

                        try {
                            F_StartDate = format.parse(dtStart);
                            Log.w("TESTF_StartDate",F_StartDate.toString());
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        String dtEnd = String.valueOf(IssueData.getString("F_EndDate"));
                        Log.w("dtEnd",dtEnd.toString());

                        try {
                            F_EndDate = format.parse(dtEnd);
                           Log.w("TestDate",F_EndDate.toString());
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        String ModelName = String.valueOf(IssueData.getString("ModelName"));

                        String F_Desc = String.valueOf(IssueData.getString("F_Desc"));

                        String dtStart_Text = String.valueOf(IssueData.getString("F_StartDate"));
                        Log.w("dtStart_Text",dtStart_Text.toString());
                        String dtEnd_Text = String.valueOf(IssueData.getString("F_EndDate"));
                        Log.w("dtEnd_Text",dtEnd_Text.toString());
                        Find_Fac_Schedule_Item_List.add(i,new Find_Fac_Schedule_Item(F_CreateDate,F_Owner,F_Keyin,F_Master_ID,F_StartDate,F_EndDate,ModelName,F_Desc,dtStart_Text,dtEnd_Text));


                    }

                    calV = new CalendarAdapter(mContext,getResources(),jumpMonth,jumpYear,year_c,month_c,day_c,Find_Fac_Schedule_Item_List);
                    gridView.setAdapter(calV);

                    //關閉-讀取等待時間Bar
                    progressBar.dismiss();

                }
                catch (JSONException ex) {
                    Log.w("Json",ex.toString());
                }
            }
        });


    }


    //點選Local回傳狀態
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (data != null) {
            Bundle bundle = data.getExtras();
            Integer CheckBooking = Integer.valueOf(bundle.getString("Booking_Check"));
            if (CheckBooking == 1) {
                Intent intent = new Intent();
                Bundle b = new Bundle();
                //b.putString("Booking_Check", "1");
                b.putString("Booking_Check", "1");
                b.putString("Type",String.valueOf(GetLocation));
                intent.putExtras(b);
                setResult(RESULT_OK, intent);
                finish();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msibook_facility_booking_main);

        mContext = this;

        progressBar = new ProgressDialog(this.mContext);
        progressBar.setCancelable(true);
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.setMessage("資料載入中");

        GetF_Facility = getIntent().getStringExtra("F_Facility");//抓F_Facility
        GetF_AssetNo = getIntent().getStringExtra("F_AssetNo");//抓F_AssetNo
        GetF_Master_ID = getIntent().getStringExtra("F_Master_ID");//抓機器序號
        GetF_Is_Restrict = getIntent().getStringExtra("F_Is_Restrict");//抓0=3個月  1=3天
        GetLocation = getIntent().getStringExtra("Location");//抓0=3個月  1=3天

        textView_F_Facility = (TextView)findViewById(R.id.textView_F_Facility);
        textView_F_AssetNo = (TextView)findViewById(R.id.textView_F_AssetNo);
        text_show_month = (TextView)findViewById(R.id.text_show_month);
        linear_wanna_booking = (LinearLayout) findViewById(R.id.linear_wanna_booking);

        textView_borrow_name = (TextView)findViewById(R.id.textView_borrow_name);
        textView_borrow_time = (TextView)findViewById(R.id.textView_borrow_time);
        textView_borrow_end_time = (TextView)findViewById(R.id.textView_borrow_end_time);

        textView_F_Facility.setText(GetF_Facility);
        textView_F_AssetNo.setText("財編 : " + GetF_AssetNo);

        back_month = (Button) findViewById(R.id.back_month);
        next_month = (Button) findViewById(R.id.next_month);

        tdt= Calendar.getInstance().getTime();//宣告用來加減用的日期變數
        td = tdt;//預設是現在時間，會因為加減而變化

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy - MM");
        text_show_month.setText(String.valueOf(sdf.format(tdt)));//設定 週 TextView

        //預設值  讓頁面產生時為當月當日
        jumpMonth = 0;      //每次滑动，增加或减去一個月,默認為0（即顯示當前月）
        jumpYear = 0;       // 滑動跨越一年，則增加或者減去一年，默認為0 (即當年)
        year_c = 0;
        month_c = 0;
        day_c = 0;

        //跳上月份
        back_month.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //定義好時間字串的格式
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy - MM"); //讓日期顯示為  年-月 給 Title用 選擇用

                Date test = td;
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(test);
                calendar.add(Calendar.MONTH, -1);//把 now抓出來做減  1 週
                td=calendar.getTime(); //  減完的週數  給 tdt
                text_show_month.setText(String.valueOf(sdf.format(td)));

                //行事曆跳上個月份
                addGridView();   //添加一個gridView
                jumpMonth--;     //上一個月

                calV = new CalendarAdapter(mContext,getResources(),jumpMonth,jumpYear,year_c,month_c,day_c,Find_Fac_Schedule_Item_List);
                //calV = new CalendarAdapter(mContext,getResources(),jumpMonth,jumpYear,year_c,month_c,day_c);
                gridView.setAdapter(calV);
            }

        });

        //跳下月份
        next_month.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //定義好時間字串的格式
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy - MM"); //讓日期顯示為  年-月 給 Title用 選擇用

                Date test = td;
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(test);
                calendar.add(Calendar.MONTH, +1);//把 now抓出來做減  1 週
                td=calendar.getTime(); //  減完的週數  給 tdt
                text_show_month.setText(String.valueOf(sdf.format(td)));

                //行事曆跳下個月
                addGridView();   //添加一個gridView
                jumpMonth++;     //下一個月

                calV = new CalendarAdapter(mContext,getResources(),jumpMonth,jumpYear,year_c,month_c,day_c,Find_Fac_Schedule_Item_List);
                //calV = new CalendarAdapter(mContext,getResources(),jumpMonth,jumpYear,year_c,month_c,day_c);
                gridView.setAdapter(calV);
            }
        });

        linear_wanna_booking.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                Intent intent = new Intent();

                //給F_Facility
                intent.putExtra("F_Facility", GetF_Facility);
                //給F_AssetNo
                intent.putExtra("F_AssetNo", GetF_AssetNo);
                //給F_Facility
                intent.putExtra("F_Master_ID", GetF_Master_ID);
                //給F_AssetNo
                intent.putExtra("F_Is_Restrict", GetF_Is_Restrict);

                // GO TO  booking_main
                intent.setClass(msibook_facility_booking_main.this, msibook_facility_i_wanna_booking.class);
                //開啟Activity
                startActivityForResult(intent,1);

                return false;
            }
        });
        addGridView();
        Find_Fac_Schedule_List(GetF_Master_ID);

        CalendarActivity();
        gestureDetector = new GestureDetector(this);
//      bd=new Bundle();
//        calV = new CalendarAdapter(mContext,getResources(),jumpMonth,jumpYear,year_c,month_c,day_c,Find_Fac_Schedule_Item_List);
//        //calV = new CalendarAdapter(mContext,getResources(),jumpMonth,jumpYear,year_c,month_c,day_c);
//        addGridView();
//        gridView.setAdapter(calV);

    }

    private void addGridView() {

        gridView =(GridView)findViewById(R.id.gridview);

        gridView.setOnTouchListener(new View.OnTouchListener() {
            //将gridview中的触摸事件回传给gestureDetector
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                return false;
            }
        });

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            //gridView中的每一个item的点击事件

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                                    long arg3) {

                textView_borrow_name.setText(null);
                textView_borrow_time.setText(null);
                textView_borrow_end_time.setText(null);

                //点击任何一个item，得到这个item的日期(排除点击的是周日到周六(点击不响应))
                int startPosition = calV.getStartPositon();
                int endPosition = calV.getEndPosition();
                if(startPosition <= position+7  && position <= endPosition-7){
                    String scheduleDay = calV.getDateByClickItem(position).split("\\.")[0];  //这一天的阳历
                    //String scheduleLunarDay = calV.getDateByClickItem(position).split("\\.")[1];  //这一天的阴历
                    String scheduleYear = calV.getShowYear();
                    String scheduleMonth = calV.getShowMonth();
//                    Toast.makeText(CalendarActivity.this, scheduleYear+"-"+scheduleMonth+"-"+scheduleDay, 2000).show();
                    ruzhuTime=scheduleMonth+"月"+scheduleDay+"日";
                    lidianTime=scheduleMonth+"月"+scheduleDay+"日";

                    Log.w("scheduleDay",String.valueOf(scheduleDay));
                    Log.w("scheduleMonth",String.valueOf(scheduleMonth));
                    Log.w("scheduleYear",String.valueOf(scheduleYear));

                    for(int i =0;i<= gridView.getChildCount();i++)
                    {
                        if (gridView.getChildAt(i) != null)
                        {
                            if (gridView.getChildAt(i).findViewById(R.id.date_linearlayout_inside) != null)
                            {
                                final LinearLayout date_linearlayout_inside = (LinearLayout)gridView.getChildAt(i).findViewById(R.id.date_linearlayout_inside);

                                if (    date_linearlayout_inside != null)
                                {
                                    date_linearlayout_inside.setBackgroundResource(0);
                                }

                            }
                        }





                    }

                    final LinearLayout date_linearlayout_inside = (LinearLayout)gridView.getChildAt(position).findViewById(R.id.date_linearlayout_inside);

                    date_linearlayout_inside.setBackgroundResource(R.drawable.facility_booking_click);


                    DateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");

                    SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");

                    Date CalendarDate = new Date();
                    try {
                        CalendarDate = format.parse(scheduleYear+"/" + scheduleMonth + "/" + scheduleDay);
                        Log.w("CalendarDate",CalendarDate.toString());

                    } catch (ParseException e) {
                        Log.w("ParseException",e.toString());
                        e.printStackTrace();
                    }

                    boolean ScheduleRepeat = false;
                    boolean MyScheduleRepeat = false;

                    Log.w("FindScheduleCount",String.valueOf(Find_Fac_Schedule_Item_List.size()));

                    List<Find_Fac_Schedule_Item> Bottom_Find_Fac_Schedule_Item_List = new ArrayList<Find_Fac_Schedule_Item>();

                    for (Find_Fac_Schedule_Item Schedule_Item : Find_Fac_Schedule_Item_List) {

                        if (Schedule_Item.F_StartDate.getTime() <= CalendarDate.getTime() && Schedule_Item.F_EndDate.getTime() >= CalendarDate.getTime())
                        {
                            SimpleDateFormat showbooking = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                            //設定日期格式
                            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss aa");
                            String F_StartDate_short="";
                            String F_EndDate_short="";
                            try {
                                Date F_StartDate = showbooking.parse(Schedule_Item.F_StartDate_Text);
                                F_StartDate_short = sdf.format(F_StartDate);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            try {
                                Date F_EndDate = showbooking.parse(Schedule_Item.F_EndDate_Text);
                                F_EndDate_short = sdf.format(F_EndDate);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }


                            textView_borrow_name.setText(Schedule_Item.GetF_Owner());
                            textView_borrow_time.setText(F_StartDate_short);
                            textView_borrow_end_time.setText(F_EndDate_short);
                            //textView_borrow_time.setText(Schedule_Item.F_StartDate_Text + "-" + Schedule_Item.F_EndDate_Text);
                            //Log.w("F_StartDate",String.valueOf(Schedule_Item.F_StartDate)+String.valueOf(Schedule_Item.F_EndDate));
//                            textView_borrow_end_time.setText();
                        }
                    }
//


//                    Intent intent=new Intent();
//                    if(state.equals("ruzhu"))
//                    {
//
//                        bd.putString("ruzhu", ruzhuTime);
//                        Log.w("ruzhuTime",ruzhuTime);
//                        System.out.println("ruzhuuuuuu"+bd.getString("ruzhu"));
//                    }else if(state.equals("lidian")){
//
//                        bd.putString("lidian", lidianTime);
//                        Log.w("lidianTime",lidianTime);
//                    }
//                    intent.setClass(CalendarActivity.this, HotelActivity.class);
//                    intent.putExtras(bd);
//                    startActivity(intent);
//                    finish();
                }
            }

        });
    }




    public void CalendarActivity() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-d");
        currentDate = sdf.format(date);  //當期日期
        year_c = Integer.parseInt(currentDate.split("-")[0]);
        month_c = Integer.parseInt(currentDate.split("-")[1]);
        day_c = Integer.parseInt(currentDate.split("-")[2]);

    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        int gvFlag = 0;         //每次添加gridview到viewflipper中時給的標記
        if (e1.getX() - e2.getX() > 120) {
            //像左滑动
            addGridView();   //添加一個gridView
            jumpMonth++;     //下一個月

            calV = new CalendarAdapter(mContext,getResources(),jumpMonth,jumpYear,year_c,month_c,day_c,Find_Fac_Schedule_Item_List);
            //calV = new CalendarAdapter(mContext,getResources(),jumpMonth,jumpYear,year_c,month_c,day_c);
            gridView.setAdapter(calV);
            addTextToTopTextView(topText);
            gvFlag++;

            return true;

        } else if (e1.getX() - e2.getX() < -120) {
            //向右滑动
            addGridView();   //添加一個gridView
            jumpMonth--;     //上一個月

            calV = new CalendarAdapter(mContext,getResources(),jumpMonth,jumpYear,year_c,month_c,day_c,Find_Fac_Schedule_Item_List);
            //calV = new CalendarAdapter(mContext,getResources(),jumpMonth,jumpYear,year_c,month_c,day_c);
            gridView.setAdapter(calV);
            gvFlag++;
            addTextToTopTextView(topText);

            return true;
        }
        return false;
    }

    //添加頭部的年份 閏哪月等信息
    public void addTextToTopTextView(TextView view){
        StringBuffer textDate = new StringBuffer();
        textDate.append(calV.getShowYear()).append("年").append(
                calV.getShowMonth()).append("月").append("\t");
        view.setText(textDate);
        view.setTextColor(Color.WHITE);
        view.setTypeface(Typeface.DEFAULT_BOLD);
    }

    //************************Item*****************************
    public class Find_Fac_Schedule_Item {

        String F_CreateDate;//創立時間
        String F_Owner; //預約者
        String F_Keyin;//功號
        String F_Master_ID; //機台序號
        Date F_StartDate;//起始日
        Date F_EndDate; //結束日
        String ModelName; //專案代碼MS-B172
        String F_Desc; //描述
        String F_StartDate_Text;//起始日
        String F_EndDate_Text; //結束日

        public Find_Fac_Schedule_Item(String F_CreateDate,String F_Owner,String F_Keyin,String F_Master_ID,Date F_StartDate,Date F_EndDate,String ModelName,String F_Desc,String F_StartDate_Text,String F_EndDate_Text)
        {
            this.F_CreateDate = F_CreateDate;

            this.F_Owner = F_Owner;

            this.F_Keyin = F_Keyin;

            this.F_Master_ID = F_Master_ID;

            this.F_StartDate = F_StartDate;

            this.F_EndDate = F_EndDate;

            this.ModelName = ModelName;

            this.F_Desc = F_Desc;

            this.F_StartDate_Text = F_StartDate_Text;

            this.F_EndDate_Text = F_EndDate_Text;
        }

        public String GetF_CreateDate()
        {
            return this.F_CreateDate;
        }

        public String GetF_Owner()
        {
            return this.F_Owner;
        }

        public String GetF_Keyin()
        {
            return this.F_Keyin;
        }

        public String GetF_Master_ID()
        {
            return this.F_Master_ID;
        }

        public Date GetF_StartDate()
        {
            return this.F_StartDate;
        }

        public Date GetF_EndDate()
        {
            return this.F_EndDate;
        }

        public String GetModelName()
        {
            return this.ModelName;
        }

        public String GetF_Desc()
        {
            return this.F_Desc;
        }

        public String GetF_StartDate_Text()
        {
            return this.F_StartDate_Text;
        }

        public String GetF_EndDate_Text()
        {
            return this.F_EndDate_Text;
        }
    }

    //************************Adapter*****************************
    public class CalendarAdapter extends BaseAdapter {
        private boolean isLeapyear = false;  //是否為閏月
        private int daysOfMonth = 0;      //某月的天数
        private int dayOfWeek = 0;        //具體某一天是星期幾
        private int lastDaysOfMonth = 0;  //上一个月的總天數
        private Context context;
        private String[] dayNumber = new String[42];  //一個gridview中的日期存入此數據中
        //  private static String week[] = {"周日","周一","周二","周三","周四","周五","周六"};
        private SpecialCalendar sc = null;
        private LunarCalendar lc = null;
        private Resources res = null;
        private Drawable drawable = null;

        private String currentYear = "";
        private String currentMonth = "";
        private String currentDay = "";

        private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-d");
        private int currentFlag = -1;     //用於標記當天
        private int[] schDateTagFlag = null;  //儲存當月所有的日程日期

        private String showYear = "";   //用於在頭部顯示的年份
        private String showMonth = "";  //用於在頭部顯示的月份
        private String animalsYear = "";
        private String leapMonth = "";   //閏哪一個月
        private String cyclical = "";   //天干地支
        //系统當前時間
        private String sysDate = "";
        private String sys_year = "";
        private String sys_month = "";
        private String sys_day = "";
        private List<Find_Fac_Schedule_Item> Find_Fac_Schedule_Item_List = new ArrayList<Find_Fac_Schedule_Item>();

        private LinearLayout date_linearlayout;

        public CalendarAdapter(){
            Date date = new Date();
            sysDate = sdf.format(date);  //當期日期
            sys_year = sysDate.split("-")[0];
            sys_month = sysDate.split("-")[1];
            sys_day = sysDate.split("-")[2];
        }

        public CalendarAdapter(Context context,Resources rs,int jumpMonth,int jumpYear,int year_c,int month_c,int day_c,List<Find_Fac_Schedule_Item> Find_Fac_Schedule_Item_List){
        //public CalendarAdapter(Context context,Resources rs,int jumpMonth,int jumpYear,int year_c,int month_c,int day_c){
            this();
            this.context= context;
            sc = new SpecialCalendar();
            lc = new LunarCalendar();
            this.res = rs;
            this.Find_Fac_Schedule_Item_List = Find_Fac_Schedule_Item_List;
            int stepYear = year_c+jumpYear;
            int stepMonth = month_c+jumpMonth ;
            if(stepMonth > 0){
                //往下一個月滑動
                if(stepMonth%12 == 0){
                    stepYear = year_c + stepMonth/12 -1;
                    stepMonth = 12;
                }else{
                    stepYear = year_c + stepMonth/12;
                    stepMonth = stepMonth%12;
                }
            }else{
                //往上一個月滑動
                stepYear = year_c - 1 + stepMonth/12;
                stepMonth = stepMonth%12 + 12;
                if(stepMonth%12 == 0){
                }
            }

            currentYear = String.valueOf(stepYear);;  //得到當前的年份
            currentMonth = String.valueOf(stepMonth);  //得到本月 （jumpMonth为滑动的次数，每滑动一次就增加一月或减一月）
            currentDay = String.valueOf(day_c);  //得到當前日期是哪天

            getCalendar(Integer.parseInt(currentYear),Integer.parseInt(currentMonth));

        }

        public CalendarAdapter(Context context,Resources rs,int year, int month, int day){
            this();
            this.context= context;
            sc = new SpecialCalendar();
            lc = new LunarCalendar();
            this.res = rs;
            currentYear = String.valueOf(year);;  //得到跳轉到的年份
            currentMonth = String.valueOf(month);  //得到跳轉到月份
            currentDay = String.valueOf(day);  //得到跳轉到的天

            getCalendar(Integer.parseInt(currentYear),Integer.parseInt(currentMonth));

        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return dayNumber.length;
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if(convertView == null){
                convertView = LayoutInflater.from(context).inflate(R.layout.msibook_facility_calendar_item, null);
            }
            TextView textView = (TextView) convertView.findViewById(R.id.tvtext);//textView_yallow
            //TextView textView2 = (TextView) convertView.findViewById(R.id.tvtext2);
            final TextView textView_yallow = (TextView) convertView.findViewById(R.id.textView_yallow);
            final TextView textView_red = (TextView) convertView.findViewById(R.id.textView_red);
            final LinearLayout date_linearlayout = (LinearLayout) convertView.findViewById(R.id.date_linearlayout);
            final LinearLayout date_linearlayout_inside = (LinearLayout) convertView.findViewById(R.id.date_linearlayout_inside);

            String d = dayNumber[position].split("/")[0];

            Log.w("Whatis \\\\\\\\\\\\\\\\.",dayNumber[position]);

            String dv = dayNumber[position].split("/")[1];

            SpannableString sp = new SpannableString(d+"\n"+dv);
            sp.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0, d.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            sp.setSpan(new RelativeSizeSpan(1.2f) , 0, d.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            if(dv != null ||dv != ""){
                sp.setSpan(new RelativeSizeSpan(0.75f), d.length()+1, dayNumber[position].length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }

            DateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");

            SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");

            Date CalendarDate = new Date();
            try {
                CalendarDate = format.parse(currentYear+"/" + currentMonth + "/" + d);
                Log.w("CalendarDate",CalendarDate.toString());
                Log.w("DDDDDDDDDDDDDDDD",d.toString());
            } catch (ParseException e) {
                Log.w("ParseException",e.toString());
                e.printStackTrace();
            }
            textView_red.setVisibility(View.GONE);
            textView_yallow.setVisibility(View.GONE);

            boolean ScheduleRepeat = false;
            boolean MyScheduleRepeat = false;

            Log.w("FindScheduleCount",String.valueOf(Find_Fac_Schedule_Item_List.size()));

            for (Find_Fac_Schedule_Item Schedule_Item : Find_Fac_Schedule_Item_List) {

                if (Schedule_Item.F_StartDate.getTime() <= CalendarDate.getTime() && Schedule_Item.F_EndDate.getTime() >= CalendarDate.getTime())
                {
                    Log.w("VisibleCalendar","Repeat");

                    if (Schedule_Item.F_Keyin.equals(UserData.WorkID) ){
                        MyScheduleRepeat = true;
                    }
                    else
                    {
                        ScheduleRepeat = true;
                    }

                }
            }

            if (ScheduleRepeat)
            {
                textView_yallow.setVisibility(View.VISIBLE);
            }
            else
            {
                textView_yallow.setVisibility(View.GONE);
            }

            if (MyScheduleRepeat)
            {
                textView_red.setVisibility(View.VISIBLE);
            }
            else
            {
                textView_red.setVisibility(View.GONE);
            }



//      sp.setSpan(new ForegroundColorSpan(Color.MAGENTA), 14, 16, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
//        textView.setText(sp);
//        textView.setTextColor(Color.GRAY);
            textView.setText(d);
            textView.setTextColor(Color.GRAY);
//            textView2.setText(dv);
//            textView2.setTextColor(Color.parseColor("#477eba"));

//      if(position<7){
//          //设置周
//          textView.setTextColor(Color.WHITE);
//          textView.setBackgroundColor(color.search_txt_color);
//          textView.setTextSize(14);
//      }

            if (position < daysOfMonth + dayOfWeek && position >= dayOfWeek) {
                // 當前月信息顯示
                textView.setTextColor(Color.parseColor("#767676"));// 當月字體設黑
                //date_linearlayout.setBackgroundColor(Color.parseColor("#ffffff")); //背景
                //drawable = res.getDrawable(R.mipmap.current_day_bgc);
            }

//            date_linearlayout.setOnTouchListener(new View.OnTouchListener() {
//                @Override
//                public boolean onTouch(View v, MotionEvent event) {
//
//
//
//                    date_linearlayout.setBackgroundResource(R.drawable.facility_myself_booking);
//                    return false;
//                }
//            });

//        if(schDateTagFlag != null && schDateTagFlag.length >0){
//            for(int i = 0; i < schDateTagFlag.length; i++){
//                if(schDateTagFlag[i] == position){
//                    //設置日程標記背景
//                    textView.setBackgroundResource(R.mipmap.mark);
//                }
//            }
//        }
//        if(currentFlag == position){
//            //設置當天的背景
//            drawable = res.getDrawable(R.mipmap.current_day_bgc);
//            textView.setBackgroundDrawable(drawable);
//            textView.setTextColor(Color.WHITE);
//        }
            return convertView;
        }

        //得到某年某月的天數且這個月的第一天是星期幾
        public void getCalendar(int year, int month){
            isLeapyear = sc.isLeapYear(year);              //是否為閏月
            daysOfMonth = sc.getDaysOfMonth(isLeapyear, month);  //某月的總天數
            dayOfWeek = sc.getWeekdayOfMonth(year, month);      //某月第一天為星期幾
            lastDaysOfMonth = sc.getDaysOfMonth(isLeapyear, month-1);  //上一个月的總天數
            Log.d("DAY", isLeapyear+" ======  "+daysOfMonth+"  ============  "+dayOfWeek+"  =========   "+lastDaysOfMonth);
            getweek(year,month);
        }

        //將一個月中的每一天的值天加入數組dayNuber中
        private void getweek(int year, int month) {
            int j = 1;
            int flag = 0;
            String lunarDay = "";

            //得到當前月的所有日程日期(這些齊需要標記)

            for (int i = 0; i < dayNumber.length; i++) {
                // 周一
//          if(i<7){
//              dayNumber[i]=week[i]+"."+" ";
//          }
                if(i < dayOfWeek){  //前一個月
//                int temp = lastDaysOfMonth - dayOfWeek+1;
//                lunarDay = lc.getLunarDate(year, month-1, temp+i,false);
//                dayNumber[i] = (temp + i)+"."+lunarDay;
                    dayNumber[i] = " / ";
                }else if(i < daysOfMonth + dayOfWeek){   //本月
                    String day = String.valueOf(i-dayOfWeek+1);   //得到的日期
                    lunarDay = lc.getLunarDate(year, month, i-dayOfWeek+1,false);

                    dayNumber[i] = i-dayOfWeek+1+"/"+"  ";

//                    for (Find_Fac_Schedule_Item MonthItem:Find_Fac_Schedule_Item_List)
//                    {
//                        if (String.valueOf(i-dayOfWeek+1) == MonthItem.Date())
//                        {
//                            dayNumber[i] = i-dayOfWeek+1+"/"+MonthItem.F_TotalHour();
//                        }
//                    }

                    Log.w("DayNumber",dayNumber[i]);

                    //對於當前 月才去標記當前日期
                    if(sys_year.equals(String.valueOf(year)) && sys_month.equals(String.valueOf(month)) && sys_day.equals(day)){
                        //標記當前日期
                        currentFlag = i;
                    }
                    //日曆被點擊
                    setShowYear(String.valueOf(year));
                    setShowMonth(String.valueOf(month));
                    setAnimalsYear(lc.animalsYear(year));
                    setLeapMonth(lc.leapMonth == 0?"":String.valueOf(lc.leapMonth));
                    setCyclical(lc.cyclical(year));
                }else{   //下一個月
                    lunarDay = lc.getLunarDate(year, month+1, j,false);
                    //dayNumber[i] = j+"."+lunarDay;
                    dayNumber[i] = " / ";
                    j++;
                }
            }

            String abc = "";
            for(int i = 0; i < dayNumber.length; i++){
                abc = abc+dayNumber[i]+":";
            }
            Log.d("DAYNUMBER",abc);


        }


        public void matchScheduleDate(int year, int month, int day){

        }

        /**
         * 点击每一个item时返回item中的日期
         * @param position
         * @return
         */
        public String getDateByClickItem(int position){
            return dayNumber[position];
        }

        /**
         * 在点击gridView时，得到这个月中第一天的位置
         * @return
         */
        public int getStartPositon(){
            return dayOfWeek+7;
        }

        /**
         * 在点击gridView时，得到这个月中最后一天的位置
         * @return
         */
        public int getEndPosition(){
            return  (dayOfWeek+daysOfMonth+7)-1;
        }

        public String getShowYear() {
            return showYear;
        }

        public void setShowYear(String showYear) {
            this.showYear = showYear;
        }

        public String getShowMonth() {
            return showMonth;
        }

        public void setShowMonth(String showMonth) {
            this.showMonth = showMonth;
        }

        public String getAnimalsYear() {
            return animalsYear;
        }

        public void setAnimalsYear(String animalsYear) {
            this.animalsYear = animalsYear;
        }

        public String getLeapMonth() {
            return leapMonth;
        }

        public void setLeapMonth(String leapMonth) {
            this.leapMonth = leapMonth;
        }

        public String getCyclical() {
            return cyclical;
        }

        public void setCyclical(String cyclical) {
            this.cyclical = cyclical;
        }
    }

    //閏月
    public class SpecialCalendar {

        private int daysOfMonth = 0;      //某月的天數
        private int dayOfWeek = 0;        //具體某一天是星期幾

        // 判断是否為閏年
        public boolean isLeapYear(int year) {
            if (year % 100 == 0 && year % 400 == 0) {
                return true;
            } else if (year % 100 != 0 && year % 4 == 0) {
                return true;
            }
            return false;
        }

        //得到某月有多少天數
        public int getDaysOfMonth(boolean isLeapyear, int month) {
            switch (month) {
                case 1:
                case 3:
                case 5:
                case 7:
                case 8:
                case 10:
                case 12:
                    daysOfMonth = 31;
                    break;
                case 4:
                case 6:
                case 9:
                case 11:
                    daysOfMonth = 30;
                    break;
                case 2:
                    if (isLeapyear) {
                        daysOfMonth = 29;
                    } else {
                        daysOfMonth = 28;
                    }

            }
            return daysOfMonth;
        }

        //指定某年中的某月的第一天是星期幾
        public int getWeekdayOfMonth(int year, int month) {
            Calendar cal = Calendar.getInstance();
            cal.set(year, month - 1, 1);
            dayOfWeek = cal.get(Calendar.DAY_OF_WEEK) - 1;
            return dayOfWeek;
        }
    }

    //節日
    public static class LunarCalendar {
        private int year;   //農曆的年份
        private int month;
        private int day;
        private String lunarMonth;   //農曆的月份
        private boolean leap;
        public int leapMonth = 0;   //閏的是哪個月

        final static String chineseNumber[] = { "一", "二", "三", "四", "五", "六", "七",
                "八", "九", "十", "十一", "十二" };
        static SimpleDateFormat chineseDateFormat = new SimpleDateFormat(
                "yyyy年MM月dd日");
        final static long[] lunarInfo = new long[] { 0x04bd8, 0x04ae0, 0x0a570,
                0x054d5, 0x0d260, 0x0d950, 0x16554, 0x056a0, 0x09ad0, 0x055d2,
                0x04ae0, 0x0a5b6, 0x0a4d0, 0x0d250, 0x1d255, 0x0b540, 0x0d6a0,
                0x0ada2, 0x095b0, 0x14977, 0x04970, 0x0a4b0, 0x0b4b5, 0x06a50,
                0x06d40, 0x1ab54, 0x02b60, 0x09570, 0x052f2, 0x04970, 0x06566,
                0x0d4a0, 0x0ea50, 0x06e95, 0x05ad0, 0x02b60, 0x186e3, 0x092e0,
                0x1c8d7, 0x0c950, 0x0d4a0, 0x1d8a6, 0x0b550, 0x056a0, 0x1a5b4,
                0x025d0, 0x092d0, 0x0d2b2, 0x0a950, 0x0b557, 0x06ca0, 0x0b550,
                0x15355, 0x04da0, 0x0a5d0, 0x14573, 0x052d0, 0x0a9a8, 0x0e950,
                0x06aa0, 0x0aea6, 0x0ab50, 0x04b60, 0x0aae4, 0x0a570, 0x05260,
                0x0f263, 0x0d950, 0x05b57, 0x056a0, 0x096d0, 0x04dd5, 0x04ad0,
                0x0a4d0, 0x0d4d4, 0x0d250, 0x0d558, 0x0b540, 0x0b5a0, 0x195a6,
                0x095b0, 0x049b0, 0x0a974, 0x0a4b0, 0x0b27a, 0x06a50, 0x06d40,
                0x0af46, 0x0ab60, 0x09570, 0x04af5, 0x04970, 0x064b0, 0x074a3,
                0x0ea50, 0x06b58, 0x055c0, 0x0ab60, 0x096d5, 0x092e0, 0x0c960,
                0x0d954, 0x0d4a0, 0x0da50, 0x07552, 0x056a0, 0x0abb7, 0x025d0,
                0x092d0, 0x0cab5, 0x0a950, 0x0b4a0, 0x0baa4, 0x0ad50, 0x055d9,
                0x04ba0, 0x0a5b0, 0x15176, 0x052b0, 0x0a930, 0x07954, 0x06aa0,
                0x0ad50, 0x05b52, 0x04b60, 0x0a6e6, 0x0a4e0, 0x0d260, 0x0ea65,
                0x0d530, 0x05aa0, 0x076a3, 0x096d0, 0x04bd7, 0x04ad0, 0x0a4d0,
                0x1d0b6, 0x0d250, 0x0d520, 0x0dd45, 0x0b5a0, 0x056d0, 0x055b2,
                0x049b0, 0x0a577, 0x0a4b0, 0x0aa50, 0x1b255, 0x06d20, 0x0ada0 };

        //農曆部分假日
        final static String[] lunarHoliday = new String[]{
                "0101 春节",
                "0115 元宵",
                "0505 端午",
                "0707 七夕情人",
                "0715 中元",
                "0815 中秋",
                "0909 重阳",
                "1208 腊八",
                "1224 小年",
                "0100 除夕"
        };

        //公曆部分例假日
        final static String[] solarHoliday = new String[]{
                "0101 元旦",
                "0214 情人",
                "0308 妇女",
                "0312 植树",
                "0315 消费者权益日",
                "0401 愚人",
                "0501 劳动",
                "0504 青年",
                "0512 护士",
                "0601 儿童",
                "0701 建党",
                "0801 建军",
                "0808 父亲",
                "0909 毛泽东逝世纪念",
                "0910 教师",
                "0928 孔子诞辰",
                "1001 国庆",
                "1006 老人",
                "1024 联合国日",
                "1112 孙中山诞辰纪念",
                "1220 澳门回归纪念",
                "1225 圣诞",
                "1226 毛泽东诞辰纪念"
        };

        // ======傳回農曆  y年的總天數
        final private static int yearDays(int y) {
            int i, sum = 348;
            for (i = 0x8000; i > 0x8; i >>= 1) {
                if ((lunarInfo[y - 1900] & i) != 0)
                    sum += 1;
            }
            return (sum + leapDays(y));
        }

        // ====== 傳回農曆  y年閏月的總天數
        final private static int leapDays(int y) {
            if (leapMonth(y) != 0) {
                if ((lunarInfo[y - 1900] & 0x10000) != 0)
                    return 30;
                else
                    return 29;
            } else
                return 0;
        }

        // ====== 傳回農曆 y年閏哪個月 1-12 , 没閏傳回 0
        final private static int leapMonth(int y) {
            return (int) (lunarInfo[y - 1900] & 0xf);
        }

        // ====== 傳回農曆 y年m月的總天數
        final private static int monthDays(int y, int m) {
            if ((lunarInfo[y - 1900] & (0x10000 >> m)) == 0)
                return 29;
            else
                return 30;
        }

        // ====== 傳回農曆 y年的生肖
        final public String animalsYear(int year) {
            final String[] Animals = new String[] { "鼠", "牛", "虎", "兔", "龙", "蛇",
                    "马", "羊", "猴", "鸡", "狗", "猪" };
            return Animals[(year - 4) % 12];
        }

        // ====== 傳入 月日的offset 傳回干支, 0=甲子
        final private static String cyclicalm(int num) {
            final String[] Gan = new String[] { "甲", "乙", "丙", "丁", "戊", "己", "庚",
                    "辛", "壬", "癸" };
            final String[] Zhi = new String[] { "子", "丑", "寅", "卯", "辰", "巳", "午",
                    "未", "申", "酉", "戌", "亥" };
            return (Gan[num % 10] + Zhi[num % 12]);
        }

        // ====== 傳入 offset 傳回干支, 0=甲子
        final public String cyclical(int year) {
            int num = year - 1900 + 36;
            return (cyclicalm(num));
        }

        public static String getChinaDayString(int day) {
            String chineseTen[] = { "初", "十", "廿", "卅" };
            int n = day % 10 == 0 ? 9 : day % 10 - 1;
            if (day > 30)
                return "";
            if (day == 10)
                return "初十";
            else
                return chineseTen[day / 10] + chineseNumber[n];
        }

        /** */
        /**
         * 传出y年m月d日对应的农历. yearCyl3:农历年与1864的相差数 ? monCyl4:从1900年1月31日以来,闰月数
         * dayCyl5:与1900年1月31日相差的天数,再加40 ?
         *
         * isday: 这个参数为false---日期为节假日时，阴历日期就返回节假日 ，true---不管日期是否为节假日依然返回这天对应的阴历日期
         * @param
         * @return
         */
        public String getLunarDate(int year_log, int month_log, int day_log, boolean isday) {
            // @SuppressWarnings("unused")
            int yearCyl, monCyl, dayCyl;
            //int leapMonth = 0;
            String nowadays;
            Date baseDate = null;
            Date nowaday = null;
            try {
                baseDate = chineseDateFormat.parse("1900年1月31日");
            } catch (ParseException e) {
                e.printStackTrace(); // To change body of catch statement use
                // Options | File Templates.
            }

            nowadays = year_log + "年" + month_log + "月" + day_log + "日";
            try {
                nowaday = chineseDateFormat.parse(nowadays);
            } catch (ParseException e) {
                e.printStackTrace(); // To change body of catch statement use
                // Options | File Templates.
            }

            // 求出和1900年1月31日相差的天数
            int offset = (int) ((nowaday.getTime() - baseDate.getTime()) / 86400000L);
            dayCyl = offset + 40;
            monCyl = 14;

            // 用offset減去每農曆年的天數
            // 計算當天是農曆第幾天
            // i最終結果是農曆的年份
            // offset是當年的第幾天
            int iYear, daysOfYear = 0;
            for (iYear = 1900; iYear < 10000 && offset > 0; iYear++) {
                daysOfYear = yearDays(iYear);
                offset -= daysOfYear;
                monCyl += 12;
            }
            if (offset < 0) {
                offset += daysOfYear;
                iYear--;
                monCyl -= 12;
            }
            // 農曆年份
            year = iYear;
            setYear(year);  //設置公曆對應的農曆年份

            yearCyl = iYear - 1864;
            leapMonth = leapMonth(iYear); // 閏哪个月,1-12
            leap = false;

            //月當年的天數offset,逐個減去每月 (農曆) 的天數，求出當天是本月的第幾天
            int iMonth, daysOfMonth = 0;
            for (iMonth = 1; iMonth < 13 && offset > 0; iMonth++) {
                // 閏月
                if (leapMonth > 0 && iMonth == (leapMonth + 1) && !leap) {
                    --iMonth;
                    leap = true;
                    daysOfMonth = leapDays(year);
                } else
                    daysOfMonth = monthDays(year, iMonth);

                offset -= daysOfMonth;
                // 解除閏月
                if (leap && iMonth == (leapMonth + 1))
                    leap = false;
                if (!leap)
                    monCyl++;
            }
            // offset為0时，並且剛才計算的月份是閏月，要校正
            if (offset == 0 && leapMonth > 0 && iMonth == leapMonth + 1) {
                if (leap) {
                    leap = false;
                } else {
                    leap = true;
                    --iMonth;
                    --monCyl;
                }
            }
            // offset小於0時，也要校正
            if (offset < 0) {
                offset += daysOfMonth;
                --iMonth;
                --monCyl;
            }
            month = iMonth;
            setLunarMonth(chineseNumber[month - 1] + "月");  //設置對應的陰曆月份
            day = offset + 1;

            if(!isday){
                //如果日期為節假日則陰曆日期則返回節假日
                //setLeapMonth(leapMonth);
                for(int i = 0; i < solarHoliday.length; i++){
                    //返回公历节假日名称
                    String sd = solarHoliday[i].split(" ")[0];  //節假日的日期
                    String sdv = solarHoliday[i].split(" ")[1]; //節假日的名稱
                    String smonth_v = month_log+"";
                    String sday_v = day_log+"";
                    String smd = "";
                    if(month_log < 10){
                        smonth_v = "0"+month_log;
                    }
                    if(day_log < 10){
                        sday_v = "0"+day_log;
                    }
                    smd = smonth_v+sday_v;
                    if(sd.trim().equals(smd.trim())){
                        return sdv;
                    }
                }

                for(int i = 0; i < lunarHoliday.length; i++){
                    //返回農曆節假日名稱
                    String ld =lunarHoliday[i].split(" ")[0];   //节假日的日期
                    String ldv = lunarHoliday[i].split(" ")[1];  //节假日的名称
                    String lmonth_v = month+"";
                    String lday_v = day+"";
                    String lmd = "";
                    if(month < 10){
                        lmonth_v = "0"+month;
                    }
                    if(day < 10){
                        lday_v = "0"+day;
                    }
                    lmd = lmonth_v+lday_v;
                    if(ld.trim().equals(lmd.trim())){
                        return ldv;
                    }
                }
            }
            if (day == 1)
                return chineseNumber[month - 1] + "月";
            else
                return getChinaDayString(day);

        }


        public String toString() {
            if (chineseNumber[month - 1] == "一" && getChinaDayString(day) == "初一")
                return "农历" + year + "年";
            else if (getChinaDayString(day) == "初一")
                return chineseNumber[month - 1] + "月";
            else
                return getChinaDayString(day);
            // return year + "年" + (leap ? "闰" : "") + chineseNumber[month - 1] +
            // "月" + getChinaDayString(day);
        }

    /*public static void main(String[] args) {
        System.out.println(new LunarCalendar().getLunarDate(2012, 1, 23));
    }*/

        public int getLeapMonth() {
            return leapMonth;
        }

        public void setLeapMonth(int leapMonth) {
            this.leapMonth = leapMonth;
        }

        /**
         * 得到當前日期對應的陰曆月份
         * @return
         */
        public String getLunarMonth() {
            return lunarMonth;
        }

        public void setLunarMonth(String lunarMonth) {
            this.lunarMonth = lunarMonth;
        }

        /**
         * 得到當前年對應的農曆年份
         * @return
         */
        public int getYear() {
            return year;
        }

        public void setYear(int year) {
            this.year = year;
        }
    }

}
