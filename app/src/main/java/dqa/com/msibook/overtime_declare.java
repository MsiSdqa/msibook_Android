package dqa.com.msibook;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link overtime_declare.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link overtime_declare#newInstance} factory method to
 * create an instance of this fragment.
 */
public class overtime_declare extends Fragment implements GestureDetector.OnGestureListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private  String Set_F_SeqNo;//暫存F_SeqNo
    private  String Set_F_App_Stat;//暫存 F_App_Stat

    private String getWork_ID;//抓取登入的工號

    private String SetNowYear;//暫存年份
    private String SetBaseYear;//搖動事件Check是否當年用

    private String SetNowMonth;//暫存月份
    private String SetBaseMonth;//搖動事件Check是否當週用

    private Context mContext;

    private ProgressDialog progressBar;//讀取狀態

    private Date tdt;
    private Date td;
    private Date baseuse;
    private Date noww;

    private TextView TotalHour1;
    private TextView TotalHour2;
    private TextView TotalHour3;
    private LinearLayout month_layout;
    private LinearLayout total_layout;
    private LinearLayout week_layout;

    private TextView message_textview1;
    private TextView message_textview2;

    //---------搖動事件宣告-------------
    private SensorManager mSensorManager;   //體感(Sensor)使用管理
    private Sensor mSensor;                 //體感(Sensor)類別
    private double mSpeed;                 //甩動力道數度
    private float mLastX;                    //x軸體感(Sensor)偏移
    private float mLastY;                    //y軸體感(Sensor)偏移
    private float mLastZ;                    //z軸體感(Sensor)偏移
    private long mLastUpdateTime;           //觸發時間
    //甩動力道數度設定值 (數值越大需甩動越大力，數值越小輕輕甩動即會觸發)
    private static final int SPEED_SHRESHOLD = 3000;
    //觸發間隔時間
    private static final int UPTATE_INTERVAL_TIME = 70;
    //---------搖動事件宣告-------------

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

    private List<Declare_Month_Item> Declare_Month_Item_List = new ArrayList<Declare_Month_Item>();

    private View mView;

    private SwipeRefreshLayout mSwipeRefreshLayout;

    private OnFragmentInteractionListener mListener;

    public overtime_declare() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment overtime_declare.
     */
    // TODO: Rename and change types and number of parameters
    public static overtime_declare newInstance(String param1, String param2) {
        overtime_declare fragment = new overtime_declare();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_overtime_declare, container, false);
        mContext = getContext();

        //讀取時間Bar
        progressBar = new ProgressDialog(this.mContext);
        progressBar.setCancelable(false);
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.setMessage("資料載入中");

        //取得體感(Sensor)服務使用權限
        mSensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        //取得手機Sensor狀態設定
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        //註冊體感(Sensor)甩動觸發Listener
        mSensorManager.registerListener(SensorListener, mSensor,SensorManager.SENSOR_DELAY_GAME);

        month_layout = (LinearLayout) mView.findViewById(R.id.month_layout);//宣告週次  textView
        total_layout = (LinearLayout) mView.findViewById(R.id.total_layout);//宣告週次  textView
        week_layout = (LinearLayout) mView.findViewById(R.id.week_layout);


        TotalHour1 = (TextView) mView.findViewById(R.id.text_totalhour1);//宣告週次  textView
        TotalHour2 = (TextView) mView.findViewById(R.id.text_totalhour2);//宣告週次  textView
        TotalHour3 = (TextView) mView.findViewById(R.id.text_totalhour3);//宣告週次  textView

        message_textview1 = (TextView) mView.findViewById(R.id.message_textview1);//宣告週次  textView
        message_textview2 = (TextView) mView.findViewById(R.id.message_textview2);//宣告週次  textView

        tdt= Calendar.getInstance().getTime();//宣告用來加減用的日期變數
        td = tdt;//預設是現在時間，會因為加減而變化

        final TextView text_show_month = (TextView) mView.findViewById(R.id.text_show_month);//宣告 月  textView
        Button back_month = (Button) mView.findViewById(R.id.back_month);//上一月
        final Button next_month = (Button) mView.findViewById(R.id.next_month);//下一月

        final Calendar now = Calendar.getInstance();//宣告行事曆 now 現在
        final Calendar Base = Calendar.getInstance();//宣告行事曆 Base 基底
        final Date BB = Base.getTime();
        Log.w("EEEEEEEEEEEEE",String.valueOf(BB));
        baseuse = Calendar.getInstance().getTime();
        noww = Calendar.getInstance().getTime();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy - MM");
        Log.w("SHOW baseuse",String.valueOf(sdf.format(baseuse)));

        SimpleDateFormat mmm = new SimpleDateFormat("MM"); //一開始給讀DB用 - 月份
        SimpleDateFormat yyy = new SimpleDateFormat("yyyy"); //一開始給讀DB用 - 年份
        SetNowYear = String.valueOf(yyy.format(noww));//抓現在 年
        SetBaseYear = SetNowYear;
        SetNowMonth = String.valueOf(mmm.format(noww));//抓現在 月
        SetBaseMonth = SetNowMonth;

        Log.w("SetNowMonth",SetNowMonth);
        Log.w("baseuse Month",String.valueOf(mmm.format(baseuse)));

        text_show_month.setText(String.valueOf(sdf.format(tdt)));//設定 週 TextView
        final String Baseuse = String.valueOf(sdf.format(baseuse));
        //next_month.setVisibility(View.INVISIBLE);

        //一開始為目前月份所以隱藏箭頭
        if (String.valueOf(SetNowMonth) == String.valueOf(mmm.format(baseuse))){
        }else{
            next_month.setVisibility(View.INVISIBLE);
        }

        //一開始載入 為 目前月份
        //OverTime_Month_Total(getWork_ID,SetNowMonth,SetNowYear);
        OverTime_Month_Total(UserData.WorkID,SetNowMonth,SetNowYear);
        //日曆一開始載入 為 目前月份
        Declare_Info(UserData.WorkID,SetNowYear,SetNowMonth);
        //抓窗口
        Find_Window_Data(UserData.WorkID);

        Log.w("一開始載入 為 目前月份",SetNowMonth+","+SetNowYear);

        //跳上月份
        back_month.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //定義好時間字串的格式
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy - MM"); //讓日期顯示為  年-月 給 Title用 選擇用
                SimpleDateFormat mmm = new SimpleDateFormat("MM"); //給讀DB用 - 月份
                SimpleDateFormat yyy = new SimpleDateFormat("yyyy"); //給讀DB用 - 年份

                //判斷週次是否為當月，如果是的話就-1，不是的話就把上次計算的值在 -1
                if (String.valueOf(SetNowMonth).equals(String.valueOf(mmm.format(BB)))){
                    Calendar now = Calendar.getInstance();
                    now.add(Calendar.MONTH, -1);//把 now抓出來做減  1 週
                    tdt=now.getTime(); //  減完的週數  給 tdt
                    td = tdt; // td = tdt
                    SetNowMonth = String.valueOf(sdf.format(tdt));
                    Log.w("SetNowWeek",String.valueOf(sdf.format(tdt)));
                    text_show_month.setText(String.valueOf(sdf.format(tdt)));
                    Log.w("GGGGGGGGGGG","GGGGGGGGGGG");
                    next_month.setVisibility(View.VISIBLE);

                    //更改過後
                    SetNowMonth = String.valueOf(mmm.format(td));
                    SetNowYear= String.valueOf(yyy.format(td));
                    //OverTime_Month_Total(getWork_ID,SetNowMonth,SetNowYear);
                    OverTime_Month_Total(UserData.WorkID,SetNowMonth,SetNowYear);

                }else{
                    Log.w("BBBBBBBBBBB",String.valueOf(mmm.format(BB)));
                    Log.w("BBBBBBBBBBB",String.valueOf(SetNowMonth));
                    Date test = td;
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(test);
                    calendar.add(Calendar.MONTH, -1);
                    td = calendar.getTime();
                    SetNowMonth = String.valueOf(sdf.format(td));
                    Log.w("SetNowWeek",String.valueOf(sdf.format(td)));
                    text_show_month.setText(String.valueOf(sdf.format(td)));

                    next_month.setVisibility(View.VISIBLE);

                    //更改過後
                    SetNowMonth = String.valueOf(mmm.format(td));
                    SetNowYear= String.valueOf(yyy.format(td));
                    //OverTime_Month_Total(getWork_ID,SetNowMonth,SetNowYear);
                    OverTime_Month_Total(UserData.WorkID,SetNowMonth,SetNowYear);
                }
                //行事曆跳上個月份
                addGridView();   //添加一個gridView
                jumpMonth--;     //上一個月

                Declare_Info(UserData.WorkID,SetNowYear,SetNowMonth);


                mSwipeRefreshLayout = (SwipeRefreshLayout) mView.findViewById(R.id.refresh_layout);

                mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        mSwipeRefreshLayout.setRefreshing(false);

//                if (!UserData.WorkID.matches(""))
//                {
//                    GetPM_Data(UserData.WorkID);
//                }
                        //OverTime_Month_Total(getWork_ID,SetNowMonth,SetNowYear);
                        OverTime_Month_Total(UserData.WorkID,SetNowMonth,SetNowYear);
                        //日曆一開始載入 為 目前月份
                        Declare_Info(UserData.WorkID,SetNowYear,SetNowMonth);
                        //抓窗口
                        Find_Window_Data(UserData.WorkID);
                    }
                });

                bd=new Bundle();//out
                bun= getActivity().getIntent().getExtras();


//        if(bun!=null&&bun.getString("state").equals("ruzhu"))
//        {
//            state=bun.getString("state");
//            System.out.println("%%%%%%"+state);
//        }else if(bun!=null&&bun.getString("state").equals("lidian")){
//
//            state=bun.getString("state");
//            System.out.println("|||||||||||"+state);
//        }

            }
        });

        //跳下月份
        next_month.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //定義好時間字串的格式
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy - MM"); //讓日期顯示為週數
                SimpleDateFormat mmm = new SimpleDateFormat("MM"); //給讀DB用 - 月份
                SimpleDateFormat yyy = new SimpleDateFormat("yyyy"); //給讀DB用 - 年份

                Date AfterDate = td;
                Calendar calendar2 = Calendar.getInstance();
                calendar2.add(Calendar.MONTH, +1);
                calendar2.setTime(AfterDate);


                if(System.currentTimeMillis() > AfterDate.getTime())
                {
                    Date test = td;
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(test);
                    calendar.add(Calendar.MONTH, +1);
                    td = calendar.getTime();
                    SetNowMonth = String.valueOf(sdf.format(td));

                    text_show_month.setText(String.valueOf(sdf.format(td)));

                    SimpleDateFormat zxc = new SimpleDateFormat("yyyy - MM");// 設定 文字轉日期
                    try {
                        Date A1 = zxc.parse(SetNowMonth); // 經過加減過的月份
                        Date A2 = zxc.parse(Baseuse); //目前的月份
                        Long ut1=A1.getTime(); //讀取時間
                        Long ut2=A2.getTime(); //讀取時間
                        Long timeP=ut2-ut1; //算毫秒差
                        Log.w("timePtimeP",String.valueOf(timeP));
                        if(timeP == 0){ //如果 == 0 表示目前已經到達 今天的月份 把按扭給隱藏
                            next_month.setVisibility(View.INVISIBLE);
                        }

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    //更改過後
                    SetNowMonth = String.valueOf(mmm.format(td));
                    SetNowYear= String.valueOf(yyy.format(td));
                    //OverTime_Month_Total(getWork_ID,SetNowMonth,SetNowYear);
                    OverTime_Month_Total(UserData.WorkID,SetNowMonth,SetNowYear);


                    //Application_Info(getWork_ID,SetNowWeek);
                }else{

                }
                //行事曆跳下個月
                addGridView();   //添加一個gridView
                jumpMonth++;     //下一個月
                Declare_Info(UserData.WorkID,SetNowYear,SetNowMonth);

//                calV = new CalendarAdapter(mContext,getResources(),jumpMonth,jumpYear,year_c,month_c,day_c,Declare_Month_Item_List);
//                gridView.setAdapter(calV);

            }
        });

        CalendarActivity();
        gestureDetector = new GestureDetector(this);
//      bd=new Bundle();
        calV = new CalendarAdapter(mContext,getResources(),jumpMonth,jumpYear,year_c,month_c,day_c,Declare_Month_Item_List);
        addGridView();
        gridView.setAdapter(calV);

        return mView;
    }

    //體感(Sensor)觸發Listener
    private SensorEventListener SensorListener = new SensorEventListener(){
        @Override
        public void onSensorChanged(SensorEvent mSensorEvent) {
            //當前觸發時間
            long mCurrentUpdateTime = System.currentTimeMillis();
            //觸發間隔時間 = 當前觸發時間 - 上次觸發時間
            long mTimeInterval = mCurrentUpdateTime - mLastUpdateTime;
            //若觸發間隔時間< 70 則return;
            if (mTimeInterval < UPTATE_INTERVAL_TIME) return;
            mLastUpdateTime = mCurrentUpdateTime;

            //取得xyz體感(Sensor)偏移
            float x = mSensorEvent.values[0];
            float y = mSensorEvent.values[1];
            float z = mSensorEvent.values[2];

            //甩動偏移速度 = xyz體感(Sensor)偏移 - 上次xyz體感(Sensor)偏移
            float mDeltaX = x - mLastX;
            float mDeltaY = y - mLastY;
            float mDeltaZ = z - mLastZ;

            mLastX = x;
            mLastY = y;
            mLastZ = z;

            //體感(Sensor)甩動力道速度公式
            mSpeed = Math.sqrt(mDeltaX * mDeltaX + mDeltaY * mDeltaY + mDeltaZ * mDeltaZ)/ mTimeInterval * 10000;
            //若體感(Sensor)甩動速度大於等於甩動設定值則進入 (達到甩動力道及速度)
            if (mSpeed >= SPEED_SHRESHOLD)
            {
                //達到搖一搖甩動後要做的事情
                //Log.d("TAG","搖一搖中...");
                if (SetNowMonth != SetBaseMonth)
                {
                    GoBack();
                }

            }
        }
        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    };

    public void GoBack(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy - MM");
        TextView text_show_month = (TextView) mView.findViewById(R.id.text_show_month);
        Calendar now = Calendar.getInstance();//宣告行事曆
        text_show_month.setText(String.valueOf(sdf.format(noww)));//設定 週 TextView
        Button next_month = (Button) mView.findViewById(R.id.next_month);//下一月
        next_month.setVisibility(View.INVISIBLE);
        //OverTime_Month_Total(getWork_ID,SetNowMonth,SetNowYear);
        OverTime_Month_Total(UserData.WorkID,SetBaseMonth,SetBaseYear);
        //日曆一開始載入 為 目前月份
        Declare_Info(UserData.WorkID,SetBaseYear,SetBaseMonth);

        Log.w("Declare_Infoddddddddd",SetBaseMonth + SetBaseYear);

        jumpMonth = 0;
        jumpYear = 0;
        calV = new CalendarAdapter(mContext,getResources(),jumpMonth,jumpYear,year_c,month_c,day_c,Declare_Month_Item_List);
        gridView.setAdapter(calV);

        SetNowMonth = SetBaseMonth;
    }

    private void addGridView() {

        gridView =(GridView)mView.findViewById(R.id.gridview);

        gridView.setOnTouchListener(new View.OnTouchListener() {
            // 將gridview中的觸摸事件回傳給gestureDetector
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub

                msibook_overtime activity = (msibook_overtime)getActivity();
                if (activity != null) {
//                    if(activity.popupWindow != null) {
//                        activity.popupWindow.dismiss();
//                    }
                }

//                try{
//                    return Declare.this.gestureDetector.onTouchEvent(event);
//                }catch (Exception e){
//                    Toast.makeText(getContext(), "多點觸控Error", Toast.LENGTH_SHORT).show();
////                    e.printStackTrace();
//                }
                return false;
            }
        });

//        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            //gridView中的每一個item的點擊事件
//
//            @Override
//            public void onItemClick(AdapterView<?> arg0, View arg1, int position,
//                                    long arg3) {
//                //點擊任何一個item，得到這個item的日期(排除點擊的是周日到週六(點擊不響應))
//                int startPosition = calV.getStartPositon();
//                int endPosition = calV.getEndPosition();
//                if(startPosition <= position+7  && position <= endPosition-7){
//                    String scheduleDay = calV.getDateByClickItem(position).split("\\.")[0];  //這一天的陽曆
//                    //String scheduleLunarDay = calV.getDateByClickItem(position).split("\\.")[1]; //這一天的陰曆
//                    String scheduleYear = calV.getShowYear();
//                    String scheduleMonth = calV.getShowMonth();
////                    Toast.makeText(CalendarActivity.this, scheduleYear+"-"+scheduleMonth+"-"+scheduleDay, 2000).show();
//                    ruzhuTime=scheduleMonth+"月"+scheduleDay+"日";
//                    lidianTime=scheduleMonth+"月"+scheduleDay+"日";
//                    Intent intent=new Intent();
//                    if(state.equals("ruzhu"))
//                    {
//
//                        bd.putString("ruzhu", ruzhuTime);
//                        System.out.println("ruzhuuuuuu"+bd.getString("ruzhu"));
//                    }else if(state.equals("lidian")){
//
//                        bd.putString("lidian", lidianTime);
//                    }
////                    intent.setClass(CalendarActivity.this, HotelActivity.class);
////                    intent.putExtras(bd);
////                    startActivity(intent);
//
//                }
//            }
//
//        });
    }
    public void CalendarActivity() {

        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-d");
        currentDate = sdf.format(date);  //當期日期
        year_c = Integer.parseInt(currentDate.split("-")[0]);
        month_c = Integer.parseInt(currentDate.split("-")[1]);
        day_c = Integer.parseInt(currentDate.split("-")[2]);


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
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                           float velocityY) {
        int gvFlag = 0;         //每次添加gridview到viewflipper中時給的標記
        if (e1.getX() - e2.getX() > 120) {
            //像左滑动
            addGridView();   //添加一個gridView
            jumpMonth++;     //下一個月

            calV = new CalendarAdapter(mContext,getResources(),jumpMonth,jumpYear,year_c,month_c,day_c,Declare_Month_Item_List);
            gridView.setAdapter(calV);
            addTextToTopTextView(topText);
            gvFlag++;

            return true;

        } else if (e1.getX() - e2.getX() < -120) {
            //向右滑动
            addGridView();   //添加一個gridView
            jumpMonth--;     //上一個月

            calV = new CalendarAdapter(mContext,getResources(),jumpMonth,jumpYear,year_c,month_c,day_c,Declare_Month_Item_List);
            gridView.setAdapter(calV);
            gvFlag++;
            addTextToTopTextView(topText);

            return true;
        }
        return false;
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Activity a;

        if (context instanceof Activity){
            a=(Activity) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


    //找窗口
    public void Find_Window_Data(String getWork_ID){

        RequestQueue mQueue = Volley.newRequestQueue(getActivity());

        String Path = "http://wtsc.msi.com.tw/IMS/OverTime_App_Service.asmx/Find_Window_Data?WorkID="+ getWork_ID;

        getString(Path, mQueue, new GetServiceData.VolleyCallback() {
            @Override
            public void onSuccess(JSONObject result) {

                try {
                    JSONArray UserArray = new JSONArray(result.getString("Key"));

                    if (UserArray.length() > 0) {

                        JSONObject IssueData = UserArray.getJSONObject(0);

                        String Account = String.valueOf(IssueData.getString("Account"));//"Date": 27        日期的日

                        message_textview1.setText("若您有任何加班申報問題");
                        message_textview2.setText("請聯繫" + Account);

                    }



                } catch (JSONException ex) {

                }

            }
        });


    }

    //DB編制細部清單
    private void Declare_Info(final String getWork_ID, String year,String month) {

        //顯示  讀取等待時間Bar
        progressBar.show();

        Declare_Month_Item_List = new ArrayList<Declare_Month_Item>();


        RequestQueue mQueue = Volley.newRequestQueue(getActivity());

        String Path = "http://wtsc.msi.com.tw/IMS/OverTime_App_Service.asmx/Find_OverTime_List_Month?WorkID="+ getWork_ID +"&Year="+ year +"&Month=" + month;

        getString(Path, mQueue, new GetServiceData.VolleyCallback() {
            @Override
            public void onSuccess(JSONObject result) {

                try {

                    JSONArray UserArray = new JSONArray(result.getString("Key"));

                    for (int i = 0; i < UserArray.length(); i++) {

                        Log.w("測試連線","OK");

                        JSONObject IssueData = UserArray.getJSONObject(i);

                        String Date = String.valueOf(IssueData.getInt("Date"));//"Date": 27        日期的日

                        if (!IssueData.isNull("F_TotalHour")){

                            String F_TotalHour = String.valueOf(IssueData.getDouble("F_TotalHour"));//"Date": 27        日期的日

                            Declare_Month_Item_List.add(0,new Declare_Month_Item(Date,F_TotalHour));
                        }
                    }

                    calV = new CalendarAdapter(mContext,getResources(),jumpMonth,jumpYear,year_c,month_c,day_c,Declare_Month_Item_List);
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

    //月總計 申請時數
    private void OverTime_Month_Total(String Work_ID, String Month,String Year) {

        //顯示  讀取等待時間Bar
        progressBar.show();

        TotalHour1.setText("0.0");
        TotalHour2.setText("0.0");
        TotalHour3.setText("0.0");

        RequestQueue mQueue = Volley.newRequestQueue(getActivity());

        String Path = "http://wtsc.msi.com.tw/IMS/OverTime_App_Service.asmx/Find_OverTime_Month_Total?WorkID=" + Work_ID + "&Month=" + Month + "&Year=" + Year;
        //String Path = "http://wtsc.msi.com.tw/IMS/OverTime_App_Service.asmx/Find_OverTime_Month_Total?WorkID=10012667&Month=3&Year=2017";

        getString(Path, mQueue, new GetServiceData.VolleyCallback() {
            @Override
            public void onSuccess(JSONObject result) {

                try {
                    JSONArray UserArray = new JSONArray(result.getString("Key"));

                    for (int i = 0; i < UserArray.length(); i++) {

                        JSONObject IssueData = UserArray.getJSONObject(i);

                        String TotalHour = String.valueOf(IssueData.getDouble("TotalHour"));//  累計計時數

                        String F_type = String.valueOf(IssueData.getString("F_type")); // 補修 or 加班費 or 總計

                        if(F_type.contains("加班費")){
                            TotalHour3.setText(TotalHour);
                        }
                        if(F_type.contains("補休")){
                            TotalHour2.setText(TotalHour);
                        }
                        if(F_type.contains("總計")){
                            //判斷數字是否大於40
                            if (Double.valueOf(TotalHour) > 40 )
                            {
                                //OverPeopleData += CName + "\n";
                                TotalHour1.setText(TotalHour);
                                TotalHour1.setTextColor(Color.parseColor("#f0625d"));

                            }else{
                                TotalHour1.setText(TotalHour);
                                TotalHour1.setTextColor(Color.parseColor("#477eba"));
                            }
//                            //判斷數字是否大於40 ************************ 遞減方式
//                            if (Double.valueOf(TotalHour) > 40 )
//                            {
//                                //OverPeopleData += CName + "\n";
//                                Double lessTotalhour = 46- Double.valueOf(TotalHour);
//                                TotalHour1.setText(String.valueOf(lessTotalhour));
//                                TotalHour1.setTextColor(Color.parseColor("#f0625d"));
//
//                            }else{
//                                Double lessTotalhour = 46- Double.valueOf(TotalHour);
//                                TotalHour1.setText(String.valueOf(lessTotalhour));
//                                TotalHour1.setTextColor(Color.parseColor("#477eba"));
//                            }

                        }

//                        //main_utirate_number.setText(String.format("%.2f", A));//稼動率
//                        if(A > 100){
//                            main_utirate_number.setText(String.format("%.2f", A));//稼動率
//                            main_utirate_number.setTextColor(Color.parseColor("#f0625d"));
//                        }
//                        if(A < 90){
//                            main_utirate_number.setText(String.format("%.2f", A));//稼動率
//                            main_utirate_number.setTextColor(Color.parseColor("#46aa36"));
//                        }
//                        if(A >= 90 & A <= 100){
//                            main_utirate_number.setText(String.format("%.2f", A));//稼動率
//                            main_utirate_number.setTextColor(Color.parseColor("#656565"));
//                        }

                    }

                    //關閉-讀取等待時間Bar
                    progressBar.dismiss();
                } catch (JSONException ex) {

                }

            }
        });

    }

    public static void getString(String Url, RequestQueue mQueue, final GetServiceData.VolleyCallback callback) {

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




}
