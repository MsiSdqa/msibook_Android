package dqa.com.msibook;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
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
 * {@link overtime_application.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link overtime_application#newInstance} factory method to
 * create an instance of this fragment.
 */
public class overtime_application extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    SQLiteDatabase db_login;//資料庫物件
    static final String db_name_login = "login_db";//資料庫名稱
    static final String tb_name_user_data = "user_data";//資料表名稱

    private SwipeRefreshLayout mSwipeRefreshLayout; //頁面刷新
    private LinearLayout application_linall;
    private LinearLayout week_layout;
    private LinearLayout application_linmonth;
    private Integer height_arry;

    private ListView lsv_main;//宣告listView
    private ApplicationAdapter mApplicationAdapter;
    private Context mContext;

    private ProgressDialog progressBar;//讀取狀態

    private List<Application_Detail_Item> Application_Detail_Item_List = new ArrayList<Application_Detail_Item>();

    private  String Set_F_SeqNo;//暫存F_SeqNo
    private  String Set_F_App_Stat;//暫存 F_App_Stat
    private  String Set_F_Web_Stat;//暫存 F_Web_Stat
    private  String Set_F_IsApply;//暫存 F_IsApply

    private String SetNowWeek;//暫存週次
    private String SetBaseWeek;//搖動事件Check是否當週用

    private String SetNowYear;//暫存年份
    private String SetBaseYear;//搖動事件Check是否當年用

    private String getWork_ID;//抓取登入的工號
    private Date tdt;
    private Date td;

    private Long daaa;
    private Integer week_curry;

    private Date baseuse;

    private Integer D1; // 放D1的月
    private Integer D7; //放 D7的月
    private String D1_total;
    private String D7_total;
    private TextView tv_dw1_show1;
    private TextView tv_dw1_show2;
    private TextView tv_dw1_show3;
    private TextView tv_dw1_show4;
    private TextView tv_dw7_show1;
    private TextView tv_dw7_show2;
    private TextView tv_dw7_show3;
    private TextView tv_dw7_show4;

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

    private Integer TestNB = 0;

    private PopupWindow enquiryPopUp;

    private Button main_manue;



    public overtime_application() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment overtime_application.
     */
    // TODO: Rename and change types and number of parameters
    public static overtime_application newInstance(String param1, String param2) {
        overtime_application fragment = new overtime_application();
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
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_overtime_application, container, false);

        mContext = getContext();

        tdt= Calendar.getInstance().getTime();
        td = tdt;

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
        mSensorManager.registerListener(SensorListener, mSensor,SensorManager.SENSOR_DELAY_NORMAL);

        tv_dw1_show1 = (TextView) v.findViewById(R.id.tv_dw1_show1);//宣告月申請總計  Day1 月份
        tv_dw1_show2 = (TextView) v.findViewById(R.id.tv_dw1_show2);//文字
        tv_dw1_show3 = (TextView) v.findViewById(R.id.tv_dw1_show3);//宣告月申請總計  Day1 月 總時數
        tv_dw1_show4 = (TextView) v.findViewById(R.id.tv_dw1_show4);//  /時
        tv_dw7_show1 = (TextView) v.findViewById(R.id.tv_dw7_show1);//宣告月申請總計  Day7 月份
        tv_dw7_show2 = (TextView) v.findViewById(R.id.tv_dw7_show2);//文字
        tv_dw7_show3 = (TextView) v.findViewById(R.id.tv_dw7_show3);//宣告月申請總計  Day7 月 總時數
        tv_dw7_show4 = (TextView) v.findViewById(R.id.tv_dw7_show4);//   /時

        final TextView text_show_week = (TextView) v.findViewById(R.id.text_show_week);//宣告週次  textView
        Button back_week = (Button) v.findViewById(R.id.back_week);//上一週
        final Button next_week = (Button) v.findViewById(R.id.next_week);//下一週

        SimpleDateFormat www = new SimpleDateFormat("ww"); //一開始給讀DB用 - 月份
        baseuse = Calendar.getInstance().getTime();
        final String Baseuse = String.valueOf(www.format(baseuse));
        final Calendar now = Calendar.getInstance();//宣告行事曆
        final Calendar Base = Calendar.getInstance();//宣告行事曆


        SetNowYear = String.valueOf(now.get(Calendar.YEAR));
        SetBaseYear = SetNowYear;

        text_show_week.setText(String.valueOf(now.get(Calendar.WEEK_OF_YEAR)));//設定 週 TextView

        SetNowWeek = String.valueOf(now.get(Calendar.WEEK_OF_YEAR));// 目前週次
        SetBaseWeek = SetNowWeek;

        application_linall = (LinearLayout)v.findViewById(R.id.application_linall);
        week_layout = (LinearLayout)v.findViewById(R.id.week_layout);
        application_linmonth = (LinearLayout)v.findViewById(R.id.application_linmonth);

        //宣告listView
        lsv_main = (ListView)v.findViewById(R.id.listView);

        //一開始為目前月份所以隱藏箭頭
        if (String.valueOf(SetNowWeek) == String.valueOf(www.format(baseuse))){
        }else{
            next_week.setVisibility(View.INVISIBLE);
        }

        back_week.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //定義好時間字串的格式
                SimpleDateFormat sdf = new SimpleDateFormat("ww"); //讓日期顯示為週數

                //判斷週次是否為當前週次，如果是的話就-1，不是的話就把上次計算的值在 -1
                if (String.valueOf(SetNowWeek) == String.valueOf(Base.get(Calendar.WEEK_OF_YEAR))){
                    Calendar now = Calendar.getInstance();
                    now.add(Calendar.WEEK_OF_YEAR, -1);//把 now抓出來做減  1 週
                    tdt=now.getTime(); //  減完的週數  給 tdt
                    td = tdt; // td = tdt
                    Log.w("td時間",String.valueOf(sdf.format(td)));
                    SetNowWeek = String.valueOf(sdf.format(tdt));
                    Log.w("111111111111tdttdttdtt",String.valueOf(sdf.format(tdt)));
                    Log.w("111111111111tdttdttdtt",String.valueOf(sdf.format(td)));
                    Log.w("getWork_ID",UserData.WorkID);
                    text_show_week.setText(String.valueOf(sdf.format(tdt)));
                    Application_Info(UserData.WorkID,SetNowWeek,SetNowYear);
                    next_week.setVisibility(View.VISIBLE);

                }else{
                    Date test = td;
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(test);
                    calendar.add(Calendar.WEEK_OF_YEAR, -1);
                    td = calendar.getTime();
                    SetNowWeek = String.valueOf(sdf.format(td));
                    Log.w("22222222222222tdtdtdtk",String.valueOf(sdf.format(tdt)));
                    Log.w("22222222222222tdtdtdtk",String.valueOf(sdf.format(td)));
                    text_show_week.setText(String.valueOf(sdf.format(td)));
                    Application_Info(UserData.WorkID,SetNowWeek,SetNowYear);
                    next_week.setVisibility(View.VISIBLE);
                }

            }
        });
        next_week.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //定義好時間字串的格式
                SimpleDateFormat sdf = new SimpleDateFormat("ww"); //讓日期顯示為週數

                Date AfterDate = td;
                Calendar calendar2 = Calendar.getInstance();
                calendar2.add(Calendar.WEEK_OF_YEAR, +1);
                calendar2.setTime(AfterDate);

                if(System.currentTimeMillis() > AfterDate.getTime())
                {
                    Date test = td;
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(test);
                    calendar.add(Calendar.WEEK_OF_YEAR, +1);
                    td = calendar.getTime();
                    SetNowWeek = String.valueOf(sdf.format(td));
                    Log.w("SetNowWeek",String.valueOf(sdf.format(td)));
                    Log.w("BaseWeek",String.valueOf(Base.get(Calendar.WEEK_OF_YEAR)));
                    text_show_week.setText(String.valueOf(sdf.format(td)));
                    Application_Info(UserData.WorkID,SetNowWeek,SetNowYear);

                    SimpleDateFormat zxc = new SimpleDateFormat("ww");// 設定 文字轉日期
                    try {
                        Date A1 = zxc.parse(SetNowWeek); // 經過加減過的月份
                        Date A2 = zxc.parse(Baseuse); //目前的月份
                        Long ut1=A1.getTime(); //讀取時間
                        Long ut2=A2.getTime(); //讀取時間
                        Long timeP=ut2-ut1; //算毫秒差
                        Log.w("timePtime",String.valueOf(timeP));
                        if(timeP == 0){ //如果 == 0 表示目前已經到達 今天的月份 把按扭給隱藏
                            next_week.setVisibility(View.INVISIBLE);
                        }

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    //Application_Info(getWork_ID,SetNowWeek);
                }else{

                }
                //把上次計算的週值在 +1
//                if (SetNowWeek.get(Calendar.WEEK_OF_YEAR) == Base.get(Calendar.WEEK_OF_YEAR)){
//
//                }else{
//
//                }

            }
        });


        mSwipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.refresh_layout);

        //lsv_main = (ListView) mSwipeRefreshLayout.findViewById(R.id.listView);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                mSwipeRefreshLayout.setRefreshing(false);

                Application_Info(UserData.WorkID,SetNowWeek,SetNowYear);

            }
        });



        return v;
    }

    @Override
    public void onResume(){
        super.onResume();

        Log.w("!!!!!!SetNowWeek!!!!!",SetNowWeek);

        //Application_Detail_Item_List.clear();

        Application_Info(UserData.WorkID,SetNowWeek,SetNowYear);


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



    //月總計 申請時數
    private void OverTime_Month_Total_D1(String Work_ID, String Month,String Year) {

        //顯示  讀取等待時間Bar
        progressBar.show();

        RequestQueue mQueue = Volley.newRequestQueue(getActivity());

        String Path = "http://wtsc.msi.com.tw/IMS/OverTime_App_Service.asmx/Find_OverTime_Month_Total?WorkID=" + Work_ID + "&Month=" + Month + "&Year=" + Year;


        getString(Path, mQueue, new GetServiceData.VolleyCallback() {
            @Override
            public void onSuccess(JSONObject result) {

                try {
                    JSONArray UserArray = new JSONArray(result.getString("Key"));

                    for (int i = 0; i < UserArray.length(); i++) {

                        JSONObject IssueData = UserArray.getJSONObject(i);

                        String TotalHour = String.valueOf(IssueData.getDouble("TotalHour"));//  累計計時數

                        String F_type = String.valueOf(IssueData.getString("F_type")); // 補修 or 加班費 or 總計

                        if(D1 ==D7){
                            tv_dw7_show1.setVisibility(View.INVISIBLE);
                            tv_dw7_show2.setVisibility(View.INVISIBLE);
                            tv_dw7_show3.setVisibility(View.INVISIBLE);
                            tv_dw7_show4.setVisibility(View.INVISIBLE);
                        }
                        if(F_type.contains("總計")){
                            //判斷數字是否大於40
                            if (Double.valueOf(TotalHour) > 40 )
                            {
                                tv_dw1_show1.setText(String.valueOf(D1));
                                tv_dw1_show3.setText(TotalHour);
                                tv_dw1_show3.setTextColor(Color.parseColor("#f0625d"));
                            }else{
                                tv_dw1_show1.setText(String.valueOf(D1));
                                tv_dw1_show3.setText(TotalHour);
                                tv_dw1_show3.setTextColor(Color.parseColor("#477eba"));
                            }
                        }
                    }
                    //關閉-讀取等待時間Bar
                    progressBar.dismiss();
                } catch (JSONException ex) {

                }
            }
        });
    }
    //月總計 申請時數
    private void OverTime_Month_Total_D2(String Work_ID, String Month,String Year) {

        //顯示  讀取等待時間Bar
        progressBar.show();

        RequestQueue mQueue = Volley.newRequestQueue(getActivity());

        String Path = "http://wtsc.msi.com.tw/IMS/OverTime_App_Service.asmx/Find_OverTime_Month_Total?WorkID=" + Work_ID + "&Month=" + Month + "&Year=" + Year;

        getString(Path, mQueue, new GetServiceData.VolleyCallback() {
            @Override
            public void onSuccess(JSONObject result) {

                try {
                    JSONArray UserArray = new JSONArray(result.getString("Key"));

                    for (int i = 0; i < UserArray.length(); i++) {

                        JSONObject IssueData = UserArray.getJSONObject(i);

                        String TotalHour = String.valueOf(IssueData.getDouble("TotalHour"));//  累計計時數

                        String F_type = String.valueOf(IssueData.getString("F_type")); // 補修 or 加班費 or 總計

                        if(F_type.contains("總計")){
                            //判斷數字是否大於40
                            if (Double.valueOf(TotalHour) > 40 )
                            {
                                //OverPeopleData += CName + "\n";
                                tv_dw7_show1.setText(String.valueOf(D7));
                                tv_dw7_show3.setText(TotalHour);
                                tv_dw7_show3.setTextColor(Color.parseColor("#f0625d"));

                            }else{
                                tv_dw7_show1.setText(String.valueOf(D7));
                                tv_dw7_show3.setText(TotalHour);
                                tv_dw7_show3.setTextColor(Color.parseColor("#477eba"));
                            }
                        }
                    }
                    //關閉-讀取等待時間Bar
                    progressBar.dismiss();
                } catch (JSONException ex) {

                }
            }
        });
    }


    //Find_OverTime_List清單
    private void Application_Info(final String getWork_ID, final String week,String year) {

        mSensorManager.unregisterListener(SensorListener);

        lsv_main.setAdapter(null);
        //顯示  讀取等待時間Bar
        progressBar.show();

        Application_Detail_Item_List = new ArrayList<Application_Detail_Item>();


        RequestQueue mQueue = Volley.newRequestQueue(getActivity());

        String Path = "http://wtsc.msi.com.tw/IMS/OverTime_App_Service.asmx/Find_OverTime_List?WorkID="+ getWork_ID +"&Year="+ year +"&Week=" + week;

        getString(Path, mQueue, new GetServiceData.VolleyCallback() {
            @Override
            public void onSuccess(JSONObject result) {

                try {



                    JSONArray UserArray = new JSONArray(result.getString("Key"));

                    for (int i = 0; i < UserArray.length(); i++) {

                        Log.w("測試連線","OK");

                        JSONObject IssueData = UserArray.getJSONObject(i);

                        String DayofWeek = String.valueOf(IssueData.getInt("DayofWeek"));//"DayofWeek": 2     當週內的第 幾 天 星期日 =  1

                        String Date = String.valueOf(IssueData.getInt("Date"));//"Date": 27        日期的日

                        String F_Date = String.valueOf(IssueData.getString("F_Date"));//"F_Date": "2017-03-27T00:00:00"     幾年幾月

                        //String F_SeqNo = String.valueOf(IssueData.getInt("F_SeqNo"));//"F_SeqNo": 6159     ????
                        if (IssueData.isNull("F_SeqNo")){
                            String F_SeqNo = "null";
                            Set_F_SeqNo = "null";
                            Log.w("F_SeqNo",F_SeqNo);
                        }else{
                            String F_SeqNo = String.valueOf(IssueData.getInt("F_SeqNo"));//"F_SeqNo": 6159     ????
                            Set_F_SeqNo = F_SeqNo;
                            Log.w("F_SeqNo",F_SeqNo);
                        }

                        String F_CreateDate = String.valueOf(IssueData.getString("F_CreateDate"));//"F_CreateDate": "2017-03-28T16:15:00",    開始日期

                        String F_UpdateTime = String.valueOf(IssueData.getString("F_UpdateTime"));//"F_UpdateTime": "2017-03-31T15:30:00",   更新日期

                        String F_Stat = String.valueOf(IssueData.getString("F_Stat"));//"F_Stat": "1",   階段

                        String F_Keyin = String.valueOf(IssueData.getString("F_Keyin"));//"F_Keyin": "10015812",    工號

                        String F_Date1 = String.valueOf(IssueData.getString("F_Date1"));//"F_Date1": "2017-03-27T00:00:00",   ???

                        String F_SRealTime = String.valueOf(IssueData.getString("F_SRealTime"));//"F_SRealTime": "08:27",    開始   時間

                        String F_ERealTime = String.valueOf(IssueData.getString("F_ERealTime"));//"F_ERealTime": "19:02",  結束  時間

                        String F_STime = String.valueOf(IssueData.getString("F_STime"));//"F_STime": "18:00",  加班 開始時間

                        String F_ETime = String.valueOf(IssueData.getString("F_ETime"));//"F_ETime": "19:00",加班 結束時間

                        //String F_BaseTotalHour = String.valueOf(IssueData.getDouble("F_BaseTotalHour"));//"F_BaseTotalHour": 1.0,    極限 最高 可報 時數
                        String Check_F_BaseTotalHour;
                        if (IssueData.isNull("F_BaseTotalHour")){
                            Check_F_BaseTotalHour = "null";
                            Log.w("F_BaseTotalHour",Check_F_BaseTotalHour);
                        }else{
                            Check_F_BaseTotalHour = String.valueOf(IssueData.getDouble("F_BaseTotalHour"));
                            Log.w("F_BaseTotalHour --",Check_F_BaseTotalHour);
                        }

                        //String F_TotalHour = String.valueOf(IssueData.getDouble("F_TotalHour"));//"F_TotalHour": 1.0,   加班 總時數
                        String Check_F_TotalHour;
                        if (IssueData.isNull("F_TotalHour")){
                            Check_F_TotalHour = "null";
                            Log.w("F_TotalHour",Check_F_TotalHour);
                        }else{
                            Check_F_TotalHour = String.valueOf(IssueData.getDouble("F_TotalHour"));
                            Log.w("F_TotalHour --",Check_F_TotalHour);
                        }

                        String F_Model = String.valueOf(IssueData.getString("F_Model"));//"F_Model": "7A61",  加班 專案代碼

                        String F_Subject = String.valueOf(IssueData.getString("F_Subject"));//"F_Subject": "Reliability Validation",     加班 主題

                        String F_Request = String.valueOf(IssueData.getString("F_Request"));//"F_Request": "(Schedule)",    ?????

                        String F_Type = String.valueOf(IssueData.getString("F_Type"));//"F_Type": "補休        ",     給付方式

                        String F_Apply = String.valueOf(IssueData.getString("F_Apply"));//"F_Apply": true,

                        String F_Dept = String.valueOf(IssueData.getString("F_Dept"));//"F_Dept": "7862",   部門代號

                        String Check_F_IsApply;
                        if (IssueData.isNull("F_IsApply")){
                            Set_F_IsApply = "null";
                            Log.w("F_IsApply",String.valueOf(Set_F_IsApply));
                        }else{
                            Set_F_IsApply = String.valueOf(IssueData.getBoolean("F_IsApply"));
                            Log.w("F_IsApply --",String.valueOf(Set_F_IsApply));
                        }
                        //String F_IsApply = String.valueOf(IssueData.getString("F_IsApply")); //"F_IsApply": true,  ????

                        String F_IsFinish = String.valueOf(IssueData.getString("F_IsFinish"));//"F_IsFinish": false,   ????

                        String F_ApplyNo = String.valueOf(IssueData.getString("F_ApplyNo")); //"F_ApplyNo": null, ???

                        String F_OverTime_Reasons = String.valueOf(IssueData.getString("F_OverTime_Reasons"));//"F_OverTime_Reasons": "製作專案報告"

                        String F_Festival = String.valueOf(IssueData.getString("F_Festival"));//放假節日

                        //String F_App_Stat = String.valueOf(IssueData.getBoolean("F_App_Stat"));//"F_App_Stat": false  //判斷有無check過
                        Boolean F_App_Stat;//App是否編輯送出過
                        if (IssueData.isNull("F_App_Stat")){
                            F_App_Stat = null;
                            Set_F_App_Stat ="null";
                            Log.w("F_App_Stat",String.valueOf(F_App_Stat));
                        }else{
                            F_App_Stat = IssueData.getBoolean("F_App_Stat");//"F_App_Stat": false  //判斷有無check過
                            Set_F_App_Stat = String.valueOf(F_App_Stat);
                            Log.w("F_App_Stat",String.valueOf(F_App_Stat));
                        }

                        Boolean F_Web_Stat;
                        if (IssueData.isNull("F_Web_Stat")){
                            F_Web_Stat = null;
                            Set_F_Web_Stat ="null";
                            Log.w("F_App_Stat",String.valueOf(F_Web_Stat));
                        }else{
                            F_Web_Stat = IssueData.getBoolean("F_Web_Stat");//"F_Web_Stat": false  //判斷WEB有無送出過
                            Set_F_Web_Stat = String.valueOf(F_Web_Stat);
                            Log.w("F_Web_Stat",String.valueOf(F_Web_Stat));
                        }


                        if (IssueData.getInt("DayofWeek") == 1){
                            //設定日期格式
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");//資料轉 日期格式
                            SimpleDateFormat bbb = new SimpleDateFormat("MM"); // 資料轉成  月格式
                            Date date = null;
                            try {
                                date = sdf.parse(F_Date);
                                String testst1 = bbb.format(date);//每比資料轉成週數
                                Log.w("DayofWeek1111111月月", String.valueOf(testst1));   // Ex :05
                                D1 = Integer.valueOf(testst1);

                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                        }else if(IssueData.getInt("DayofWeek") == 7){
                            //設定日期格式
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");//資料轉 日期格式
                            SimpleDateFormat bbb = new SimpleDateFormat("MM"); // 資料轉成  月格式
                            Date date = null;
                            try {
                                date = sdf.parse(F_Date);
                                String testst1 = bbb.format(date);//每比資料轉成週數
                                Log.w("DayofWeek777777月月", String.valueOf(testst1));   // Ex :05
                                D7 = Integer.valueOf(testst1);

                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            Log.w("DDDDOOOOOOOOFFFF77777",String.valueOf(F_Date));
                        }
//
                        Application_Detail_Item_List.add(i,new Application_Detail_Item(DayofWeek,Date,Check_F_BaseTotalHour,Check_F_TotalHour,F_Date1,F_STime,F_ETime,F_Model,F_OverTime_Reasons,F_Type,F_Apply,Set_F_SeqNo,Set_F_App_Stat,Set_F_Web_Stat,F_Date,Set_F_IsApply,F_Festival));



                    }



                    lsv_main.setAdapter( new ApplicationAdapter(mContext,Application_Detail_Item_List));

                    int height = lsv_main.getMeasuredHeight();

                    height_arry = height / 7;
                    //取得平均高度

                    Log.w("ListView 高度 / 7  ====",String.valueOf(height_arry));

//                    LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) lsv_main.getLayoutParams();
//                    lp.height = height_arry;
//                    lsv_main.setLayoutParams(lp);

                    if(D1 != D7){  //  4  -  5

                        tv_dw7_show1.setVisibility(View.VISIBLE);
                        tv_dw7_show2.setVisibility(View.VISIBLE);
                        tv_dw7_show3.setVisibility(View.VISIBLE);
                        tv_dw7_show4.setVisibility(View.VISIBLE);
                        OverTime_Month_Total_D1(getWork_ID,String.valueOf(D1),SetNowYear);
                        OverTime_Month_Total_D2(getWork_ID,String.valueOf(D7),SetNowYear);

                    }else{

                        OverTime_Month_Total_D1(getWork_ID,String.valueOf(D1),SetNowYear);
                        tv_dw7_show1.setVisibility(View.INVISIBLE);
                        tv_dw7_show2.setVisibility(View.INVISIBLE);
                        tv_dw7_show3.setVisibility(View.INVISIBLE);
                        tv_dw7_show4.setVisibility(View.INVISIBLE);
                    }

                    //關閉-讀取等待時間Bar
                    progressBar.dismiss();

                    mSensorManager.registerListener(SensorListener, mSensor,SensorManager.SENSOR_DELAY_NORMAL);


                    //listView點選事件
                    lsv_main.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

//                            MainActivity activity = (MainActivity)getActivity();
//                            if (activity != null) {
//                                if(activity.popupWindow != null) {
//                                    activity.popupWindow.dismiss();
//                                }
//                            }

                            String dateString = Application_Detail_Item_List.get(position).F_Date().substring(0,Application_Detail_Item_List.get(position).F_Date().length()-9);
                            //設定日期格式
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                            //判斷 7天之前的資料都反灰
                            Date date = null;
                            try {
                                date = sdf.parse(dateString);
                                Date base=new Date();// 現在時間
                                Long ut1=base.getTime(); // 現在時間
                                Long ut2=date.getTime(); //每筆資料時間
                                Long test = ut1 -ut2; //毫秒差
                                daaa = test/(1000*60*60*24); //毫秒轉 日 差
                                Log.w("日期相減", String.valueOf(daaa));

                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
//                            //設定日期格式
//                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//                            SimpleDateFormat bbb = new SimpleDateFormat("ww");
//                            ////判斷 跨週 的資料無法點選
//                            Date date = null;
//                            try {
//                                date = sdf.parse(dateString);
//                                String testst1 = bbb.format(date);//每比資料轉成週數
//                                Log.w("testst1testst1testst1", String.valueOf(testst1));
//                                Date base=new Date();// 現在時間
//                                String testst2 = bbb.format(base);//今天日期轉成週數
//                                Log.w("testst2testst2testst2", String.valueOf(testst2));
//                                int ut1=Integer.valueOf(testst1); // 現在時間
//                                int ut2=Integer.valueOf(testst2);//每筆資料時間
//                                week_curry = ut2 -ut1; //毫秒差
//                                Log.w("週次相減", String.valueOf(week_curry));
//
//                            } catch (ParseException e) {
//                                e.printStackTrace();
//                            }

                            if(Application_Detail_Item_List.get(position).F_TotalHour() == "null"){//總時數 = 空值
                                lsv_main.setItemChecked(position, false);}
//                            else if(Application_Detail_Item_List.get(position).F_App_Stat().toString().trim() == "true"){ //是否編輯過
//                                lsv_main.setItemChecked(position, false);
//                            }
//                            else if(week_curry != 0){ //已過週次
//                                lsv_main.setItemChecked(position, false);
//                            }
                            else if(daaa > 7){
                                lsv_main.setItemChecked(position, false);
                            }
                            else{
                                //Log.w("TTTTTTTTTTT",Application_Detail_Item_List.get(position).F_TotalHour());
                                Intent intent = new Intent();

                                intent.putExtra("Work_ID",getWork_ID);//給下頁 工號
                                Log.w("測試給下頁工號",getWork_ID);
                                //擷取 年-月-日
                                String aStr_F_Date;
                                aStr_F_Date = Application_Detail_Item_List.get(position).F_Date1().substring(0,Application_Detail_Item_List.get(position).F_Date1().length()-9);
                                intent.putExtra("F_Date1",aStr_F_Date);//給下頁 年-月-日
                                Log.w("F_Date1",aStr_F_Date);

                                //給下頁 開始時間
                                intent.putExtra("F_STime",Application_Detail_Item_List.get(position).F_STime());//給下頁 年-月-日
                                Log.w("F_STime",Application_Detail_Item_List.get(position).F_STime());

                                //給下頁 離開時間
                                intent.putExtra("F_ETime",Application_Detail_Item_List.get(position).F_ETime());//給下頁 年-月-日
                                Log.w("F_ETime",Application_Detail_Item_List.get(position).F_ETime());

                                //給下頁 專案
                                intent.putExtra("F_Model",Application_Detail_Item_List.get(position).F_Model());//給下頁 年-月-日
                                Log.w("F_Model",Application_Detail_Item_List.get(position).F_Model());

                                //給下頁 加班原因
                                intent.putExtra("F_OverTime_Reasons",Application_Detail_Item_List.get(position).F_OverTime_Reasons());//給下頁 年-月-日
                                Log.w("F_OverTime_Reasons",Application_Detail_Item_List.get(position).F_OverTime_Reasons());

                                //給下頁 給付方式
                                intent.putExtra("F_Type",Application_Detail_Item_List.get(position).F_Type().trim());//給下頁 給付方式
                                Log.w("F_Type",Application_Detail_Item_List.get(position).F_Type());

                                //給下頁 基底時數
                                intent.putExtra("F_BaseTotalHour",Application_Detail_Item_List.get(position).F_BaseTotalHour());//給下頁 年-月-日
                                Log.w("F_BaseTotalHour",Application_Detail_Item_List.get(position).F_BaseTotalHour());

                                //給下頁 申請時數
                                intent.putExtra("F_TotalHour",Application_Detail_Item_List.get(position).F_TotalHour());//給下頁 年-月-日
                                Log.w("F_TotalHour",Application_Detail_Item_List.get(position).F_TotalHour());

                                //給下頁 序號
                                intent.putExtra("F_SeqNo",Application_Detail_Item_List.get(position).F_SeqNo());//給下頁 序號
                                Log.w("F_SeqNo",Application_Detail_Item_List.get(position).F_SeqNo());

                                //給下頁 App使用狀態
                                intent.putExtra("F_App_Stat",Application_Detail_Item_List.get(position).F_App_Stat());
                                Log.w("F_App_Stat",Application_Detail_Item_List.get(position).F_App_Stat());

                                //給下頁 Web使用狀態
                                intent.putExtra("F_Web_Stat",Application_Detail_Item_List.get(position).F_Web_Stat());
                                Log.w("F_Web_Stat",Application_Detail_Item_List.get(position).F_Web_Stat());

                                //給下頁 Web使用狀態
                                intent.putExtra("F_Apply",Application_Detail_Item_List.get(position).F_Apply());
                                Log.w("給下頁F_Apply",Application_Detail_Item_List.get(position).F_Apply());

                                intent.setClass(getActivity(), overtime_pop_activity.class);
                                //開啟Activity
                                startActivity(intent);
                                Log.w("項目點選","Test Test");
                            }

//                            Toast.makeText(getActivity().getApplicationContext(), "提示：点击窗口外部关闭窗口！",
//                            Toast.LENGTH_SHORT).show();

//                            //使用PopupWindow
//                            View popUpView = getActivity().getLayoutInflater().inflate(R.layout.activity_main__pop_, null);
//                            enquiryPopUp = new PopupWindow(popUpView, RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT, true);
//                            enquiryPopUp.setBackgroundDrawable(new BitmapDrawable(null, ""));
//                            enquiryPopUp.setAnimationStyle(android.R.style.Animation_Dialog);
//                            enquiryPopUp.showAtLocation(popUpView, Gravity.CENTER, 0, 0);
//                            enquiryPopUp.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

                        }
                    });

                }
                catch (JSONException ex) {

                    Log.w("Json",ex.toString());
                }

            }
        });

    }


    //體感(Sensor)觸發Listener
    private SensorEventListener SensorListener = new SensorEventListener(){
        @Override
        public void onSensorChanged(SensorEvent mSensorEvent) {
            //當前觸發時間
            long mCurrentUpdateTime = System.currentTimeMillis();
            //Log.w("mCurrentUpdateTime",String.valueOf(mCurrentUpdateTime));
            //觸發間隔時間 = 當前觸發時間 - 上次觸發時間
            long mTimeInterval = mCurrentUpdateTime - mLastUpdateTime;
            //Log.w("mTimeInterval",String.valueOf(mTimeInterval));
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
                Log.d("TAG","搖一搖中...");
                //Log.d("TAG",String.valueOf(TestNB));
                if (SetNowWeek != SetBaseWeek)
                {
                    GoBack();
                }
            }

        }
        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    };

    public void GoBack()
    {
        SimpleDateFormat sdf = new SimpleDateFormat("ww"); //讓日期顯示為週數
        Application_Info(UserData.WorkID,SetBaseWeek,SetBaseYear);
        TextView text_show_week = (TextView) getActivity().findViewById(R.id.text_show_week);//宣告週次  textView
        Calendar now = Calendar.getInstance();//宣告行事曆
        text_show_week.setText(String.valueOf(now.get(Calendar.WEEK_OF_YEAR)));//設定 週 TextView
        td = Calendar.getInstance().getTime();
        tdt= Calendar.getInstance().getTime();
        Log.w("111111111111tdttdttdtt",String.valueOf(sdf.format(tdt)));
        Log.w("111111111111tdttdttdtt",String.valueOf(sdf.format(td)));
        //  td = baseuse;
        Button next_week = (Button) getActivity().findViewById(R.id.next_week);//下一週
        next_week.setVisibility(View.INVISIBLE);

        SetNowWeek = SetBaseWeek;
    }



    // Item
    public class Application_Detail_Item {


        String DayofWeek; //當週第幾天(星期顯示)

        String Date; //當日日期

        String F_BaseTotalHour; //極限 最高 可報 時數

        String F_TotalHour;//申請總時數

        String F_Date1;// "2017-03-27T00:00:00" 日期

        String F_STime; //"08:27",    開始   時間

        String F_ETime; //"19:02",  結束  時間

        String F_Model; //"7A61",  加班 專案代碼

        String F_OverTime_Reasons;//"製作專案報告"  加班原因

        String F_Type;//"補休        ",     給付方式

        String F_Apply;

        String F_SeqNo;

        String F_App_Stat;

        String F_Web_Stat;

        String F_Date;

        String F_IsApply;

        String F_Festival;


        public Application_Detail_Item(String DayofWeek,String Date,String F_BaseTotalHour,String F_TotalHour,String F_Date1,String F_STime,String F_ETime,String F_Model,String F_OverTime_Reasons,String F_Type,String F_Apply,String F_SeqNo,String F_App_Stat,String F_Web_Stat,String F_Date,String F_IsApply,String F_Festival)
        {
            this.DayofWeek = DayofWeek;

            this.Date = Date;

            this.F_BaseTotalHour = F_BaseTotalHour;

            this.F_TotalHour = F_TotalHour;

            this.F_Date1 = F_Date1;

            this.F_STime = F_STime;

            this.F_ETime = F_ETime;

            this.F_Model = F_Model;

            this.F_OverTime_Reasons = F_OverTime_Reasons;

            this.F_Type = F_Type;

            this.F_Apply = F_Apply;

            this.F_SeqNo = F_SeqNo;

            this.F_App_Stat = F_App_Stat;

            this.F_Web_Stat = F_Web_Stat;

            this.F_Date = F_Date;

            this.F_IsApply = F_IsApply;

            this.F_Festival = F_Festival;
        }


        public String DayofWeek()
        {
            return this.DayofWeek;
        }

        public String Date()
        {
            return this.Date;
        }

        public String F_BaseTotalHour()
        {
            return this.F_BaseTotalHour;
        }
        public String F_TotalHour()
        {
            return this.F_TotalHour;
        }
        public String F_Date1()
        {
            return this.F_Date1;
        }
        public String F_STime()
        {
            return this.F_STime;
        }
        public String F_ETime()
        {
            return this.F_ETime;
        }
        public String F_Model()
        {
            return this.F_Model;
        }
        public String F_OverTime_Reasons()
        {
            return this.F_OverTime_Reasons;
        }
        public String F_Type()
        {
            return this.F_Type;
        }
        public String F_Apply()
        {
            return this.F_Apply;
        }
        public String F_SeqNo()
        {
            return this.F_SeqNo;
        }
        public String F_App_Stat()
        {
            return this.F_App_Stat;
        }
        public String F_Web_Stat()
        {
            return this.F_Web_Stat;
        }
        public String F_Date()
        {
            return this.F_Date;
        }
        public String F_IsApply()
        {
            return this.F_IsApply;
        }

        public String F_Festival()
        {
            return this.F_Festival;
        }
    }


    //Adapter
    public class ApplicationAdapter extends BaseAdapter {

        private LayoutInflater mLayInf;

        private List<Application_Detail_Item> Application_Detail_Item_List;

        private Context ProjectContext;

        private Integer week_curry;

        private Long checktime;//存放每筆 資料是否大於七天的數字

        private ListView lsv_main;//宣告listView

        public ApplicationAdapter(Context context,  List<Application_Detail_Item> Application_Detail_Item_List)
        {
            mLayInf = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            ProjectContext = context;
            this.Application_Detail_Item_List = Application_Detail_Item_List;
        }
        @Override
        public int getCount() {
            return Application_Detail_Item_List.size();
        }

        @Override
        public Object getItem(int position) {
            return Application_Detail_Item_List.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View v = new View(ProjectContext);

            v = mLayInf.inflate(R.layout.overtime_application_adapter, parent, false);

            //設定每列高度
            int height = parent.getMeasuredHeight() / 7 -20;

            int width = parent.getMeasuredWidth();

            //        RelativeLayout adp2Relativelayout = (RelativeLayout) v.findViewById(R.id.adapter2_Relativelayout);

            //宣告 Linearlayout 判斷項目點選事件
            LinearLayout Application_Linearlayout = (LinearLayout) v.findViewById(R.id.application_relativelayout);

            //平均欄位會ERROR寫法
//        if (Application_Linearlayout != null)
//        {
//            Application_Linearlayout.setLayoutParams(new LinearLayout.LayoutParams(width, height));
//
//        }
            //平均欄位會PASS寫法
            ViewGroup.LayoutParams lp = Application_Linearlayout.getLayoutParams();
            lp.width = width;
            lp.height = height;
            Application_Linearlayout.requestLayout();


            Log.w("height",String.valueOf(parent.getMeasuredHeight()));



            //宣告內部元件
            TextView textView_date = (TextView) v.findViewById(R.id.textView_date);
            TextView textView_weekdate = (TextView) v.findViewById(R.id.textView_weekdate);
            TextView textView_festival = (TextView) v.findViewById(R.id.text_Festival);//放節日
            TextView textView_line = (TextView) v.findViewById(R.id.textView_line);
            TextView textView_hour = (TextView) v.findViewById(R.id.textView_hour);
            TextView textView_slash = (TextView) v.findViewById(R.id.textView_slash);
            TextView textView_si = (TextView) v.findViewById(R.id.textView_si);
            LinearLayout linearlayout_totalhour = (LinearLayout) v.findViewById(R.id.linearlayout_totalhour);//判斷無加班時格式改變用的
            TextView textView_pic1 = (TextView) v.findViewById(R.id.textView_pic1);
            TextView textView_pic2 = (TextView) v.findViewById(R.id.textView_pic2);

            textView_date.setText(Application_Detail_Item_List.get(position).Date());

            // *******圖片1 影藏  暫時用不到
            textView_pic1.setVisibility(View.INVISIBLE);
            //*****小時 / 隱藏*******
            textView_slash.setVisibility(View.INVISIBLE);
            textView_si.setVisibility(View.INVISIBLE);

            //textView_weekdate.setText(Application_Detail_Item_List.get(position).DayofWeek());
            if (Application_Detail_Item_List.get(position).F_Festival() == "null")
            {

                textView_festival.setVisibility(View.GONE);
                linearlayout_totalhour.setGravity(Gravity.CENTER);
            }else{

                textView_festival.setText(Application_Detail_Item_List.get(position).F_Festival());
                //linearlayout_totalhour.setLayoutParams(textView_weekdate.getLayoutParams());


            }


            //星期的判斷
            switch (Application_Detail_Item_List.get(position).DayofWeek())
            {
                case "1":
                    textView_weekdate.setText("日");
                    textView_date.setTextColor(Color.parseColor("#f0625d"));
                    textView_weekdate.setTextColor(Color.parseColor("#f0625d"));
                    break;
                case "2":
                    textView_weekdate.setText("一");
                    break;
                case "3":
                    textView_weekdate.setText("二");
                    break;
                case "4":
                    textView_weekdate.setText("三");
                    break;
                case "5":
                    textView_weekdate.setText("四");
                    break;
                case "6":
                    textView_weekdate.setText("五");
                    break;
                case "7":
                    textView_weekdate.setText("六");
                    textView_date.setTextColor(Color.parseColor("#46aa36"));
                    textView_weekdate.setTextColor(Color.parseColor("#46aa36"));
                    break;
            }

            //*****************************
            //欲轉換的日期字串
            String dateString1 = Application_Detail_Item_List.get(position).F_Date().substring(0,Application_Detail_Item_List.get(position).F_Date().length()-9);
            //設定日期格式
            SimpleDateFormat ccc = new SimpleDateFormat("yyyy-MM-dd");
            //進行轉換
            try {
                Date date = ccc.parse(dateString1); // 每筆資料的日期
                //Log.w("CCCCCCCCCCC日期",String.valueOf(sdf.format(date)));
                //判斷 7天之前的資料都反灰
                Date base=new Date();// 現在時間
                Long ut1=base.getTime(); // 現在時間
                Long ut2=date.getTime(); //每筆資料時間
                Long test = ut1 -ut2; //毫秒差
                Long daaa = test/(1000*60*60*24); //毫秒轉 日 差
                checktime = daaa;
            } catch (ParseException e) {
                e.printStackTrace();
            }

            //*****************************每筆資料判斷*****************************
            switch (Application_Detail_Item_List.get(position).F_IsApply().trim())
            {
                case "true"://有資料有報＝true，這情況出現在上週或是當週遇例外假日提早報

                    switch (Application_Detail_Item_List.get(position).F_Type().trim())//// 判斷F_Type 補休 OR 加班費 OR Null放棄
                    {
                        case "補休":
                            textView_pic2.setBackgroundResource(R.mipmap.overtime_ic_apply_rest);//補休圖
                            textView_hour.setText(Application_Detail_Item_List.get(position).F_TotalHour());
                            if(checktime>7){
                                Application_Linearlayout.setBackgroundColor(Color.parseColor("#edeeef"));//背景反灰
                            }
                            textView_hour.setTextColor(Color.parseColor("#a5b6c3"));//字反灰
                            Log.w("抓到","F_IsApply-TRUE     F_Type--補休 ");
                            break;
                        case "加班費":
                            textView_pic2.setBackgroundResource(R.mipmap.overtime_ic_apply_money);//加班費圖
                            textView_hour.setText(Application_Detail_Item_List.get(position).F_TotalHour());
                            if(checktime>7){
                                Application_Linearlayout.setBackgroundColor(Color.parseColor("#edeeef"));//背景反灰
                            }
                            textView_hour.setTextColor(Color.parseColor("#a5b6c3"));//字反灰
                            Log.w("抓到","F_IsApply-TRUE     F_Type--加班費 ");
                            break;
                        case "null":
                            textView_pic2.setBackgroundResource(R.mipmap.overtime_ic_apply_noapply);//放棄圖
                            textView_hour.setText(Application_Detail_Item_List.get(position).F_TotalHour());
                            if(checktime>7){
                                Application_Linearlayout.setBackgroundColor(Color.parseColor("#edeeef"));//背景反灰
                            }
                            textView_hour.setTextColor(Color.parseColor("#a5b6c3"));//字反灰
                            Log.w("抓到","F_IsApply-TRUE     F_Type--放棄 ");
                            break;
                    }

                case "false"://有資料沒報＝false>>這情況出現在當週

                    if(Application_Detail_Item_List.get(position).F_App_Stat().trim() == "true" || Application_Detail_Item_List.get(position).F_Web_Stat().trim() == "true"){//判斷資料是否"更改or送出"過

                        if(Application_Detail_Item_List.get(position).F_Apply().trim() == "true"){// 要申請

                            switch (Application_Detail_Item_List.get(position).F_Type().trim())//// 判斷F_Type 補休 OR 加班費 OR Null放棄
                            {
                                case "補休":
                                    textView_pic2.setBackgroundResource(R.mipmap.overtime_ic_apply_rest);//補休圖
                                    textView_hour.setText(Application_Detail_Item_List.get(position).F_TotalHour());
                                    if(checktime>7){
                                        Application_Linearlayout.setBackgroundColor(Color.parseColor("#edeeef"));//背景反灰
                                    }
                                    textView_hour.setTextColor(Color.parseColor("#a5b6c3"));//字反灰
                                    Log.w("抓到","F_IsApply-TRUE     F_App_Stat--TRUE   F_Apply--TURE   F_Type--補休 ");
                                    break;
                                case "加班費":
                                    textView_pic2.setBackgroundResource(R.mipmap.overtime_ic_apply_money);//加班費圖
                                    textView_hour.setText(Application_Detail_Item_List.get(position).F_TotalHour());
                                    if(checktime>7){
                                        Application_Linearlayout.setBackgroundColor(Color.parseColor("#edeeef"));//背景反灰
                                    }
                                    textView_hour.setTextColor(Color.parseColor("#a5b6c3"));//字反灰
                                    Log.w("抓到","F_IsApply-TRUE     F_App_Stat--TRUE   F_Apply--TURE   F_Type--加班費 ");
                                    break;
                            }

                        }else{
                            textView_pic2.setBackgroundResource(R.mipmap.overtime_ic_apply_noapply);//放棄圖
                            textView_hour.setText(Application_Detail_Item_List.get(position).F_TotalHour());
                            if(checktime>7){
                                Application_Linearlayout.setBackgroundColor(Color.parseColor("#edeeef"));//背景反灰
                            }
                            textView_hour.setTextColor(Color.parseColor("#a5b6c3"));//字反灰
                            Log.w("抓到","F_IsApply-TRUE     F_App_Stat--TRUE   F_Apply--FALSE   F_Type--Null 放棄 ");
                        }
                    }else{

                        //欲轉換的日期字串
                        String dateString = Application_Detail_Item_List.get(position).F_Date().substring(0,Application_Detail_Item_List.get(position).F_Date().length()-9);
                        //設定日期格式
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        //進行轉換
                        try {
                            Date date = sdf.parse(dateString); // 每筆資料的日期
                            //Log.w("CCCCCCCCCCC日期",String.valueOf(sdf.format(date)));
                            //判斷 7天之前的資料都反灰
                            Date base=new Date();// 現在時間
                            Long ut1=base.getTime(); // 現在時間
                            Long ut2=date.getTime(); //每筆資料時間
                            Long test = ut1 -ut2; //毫秒差
                            Long daaa = test/(1000*60*60*24); //毫秒轉 日 差
                            Log.w("日期相減", String.valueOf(daaa));
                            if(daaa > 7){
                                Application_Linearlayout.setBackgroundColor(Color.parseColor("#edeeef"));//反灰
                                textView_hour.setText(Application_Detail_Item_List.get(position).F_TotalHour());
                                Application_Linearlayout.setClickable(false);//無法點選
                                textView_pic2.setBackgroundResource(R.mipmap.overtime_btn_do_not_edit);// 禁止筆
                                Log.w("抓到","F_IsApply--FALSE         F_App_Stat--FALSE    超過7天 SHOW 禁止-筆");
                            }
                            else{
                                textView_hour.setText(Application_Detail_Item_List.get(position).F_TotalHour());
                                textView_pic2.setBackgroundResource(R.mipmap.overtime_btn_apply_edit);// 可編輯筆
                                Log.w("抓到","F_IsApply--FALSE         F_App_Stat--FALSE    還在7天內 SHOW 編輯-筆");
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }


                    break;
                case "null":
                    //******************************判斷 未來 還沒發生的資料 & 過去已經發生 但沒資料的事件
                    //抓出日期比對
                    //欲轉換的日期字串
                    String dateString = Application_Detail_Item_List.get(position).F_Date().substring(0,Application_Detail_Item_List.get(position).F_Date().length()-9);
                    //設定日期格式
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    //進行轉換
                    try {
                        Date date = sdf.parse(dateString); // 每筆資料的日期
                        //Log.w("CCCCCCCCCCC日期",String.valueOf(sdf.format(date)));
                        if (System.currentTimeMillis() < date.getTime()){
                            Log.w("CCCCCCCCCCC日期",String.valueOf(sdf.format(date))+"尚未發生");
                            textView_hour.setText("尚無紀錄");
                            textView_hour.setTextColor(Color.parseColor("#a5b6c3"));
                            textView_hour.setTextSize(24);
                            textView_slash.setVisibility(View.GONE);
                            textView_si.setVisibility(View.GONE);
                            textView_pic1.setVisibility(View.INVISIBLE);
                            textView_pic2.setVisibility(View.INVISIBLE);
                            linearlayout_totalhour.setLayoutParams(textView_weekdate.getLayoutParams());

                            Log.w("抓到","F_IsApply--NULL   沒資料    又是未來還沒發生");
                        }else{
                            //判斷總時數F_TotalHour 是否有加班紀錄  ，如果沒有的話禁止 被點選  F_App_Stat
                            if(Application_Detail_Item_List.get(position).F_TotalHour() == "null"){
                                Application_Linearlayout.setClickable(false);
                                //textView_hour.setText("無加班紀錄");
                                textView_hour.setText("0");
                                textView_hour.setTextColor(Color.parseColor("#a5b6c3"));
                                textView_hour.setTextSize(24);
                                textView_slash.setVisibility(View.GONE);
                                textView_si.setVisibility(View.GONE);
                                textView_pic1.setVisibility(View.INVISIBLE);
                                textView_pic2.setVisibility(View.INVISIBLE);
                                linearlayout_totalhour.setLayoutParams(textView_weekdate.getLayoutParams());
                                Log.w("抓到","F_IsApply--NULL   已發生但沒資料  ");
                            }else{
                                textView_hour.setText(Application_Detail_Item_List.get(position).F_TotalHour());
                            }
                            //判斷 7天之前的資料都反灰
                            Date base=new Date();// 現在時間
                            Long ut1=base.getTime(); // 現在時間
                            Long ut2=date.getTime(); //每筆資料的時間
                            Long test = ut1 -ut2; //毫秒差
                            Long daaa = test/(1000*60*60*24); //毫秒轉 日 差
                            Log.w("日期相減", String.valueOf(daaa));
                            if(daaa > 7){
                                Application_Linearlayout.setBackgroundColor(Color.parseColor("#edeeef"));//反灰
                                Application_Linearlayout.setClickable(false);//無法點選
                            }

                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    break;
            }



//
//
//        //判斷首頁單獨Show的圖案 是否送出過。沒的話顯示 編輯圖案
//        if(Application_Detail_Item_List.get(position).F_App_Stat().trim() == "true"){
//            //判斷 補休 OR 加班費 OR 放棄
//            switch (Application_Detail_Item_List.get(position).F_Type().trim())
//            {
//                case "補休":
//                    Log.w("抓到","補休");
//                    textView_pic2.setBackgroundResource(R.mipmap.ic_apply_rest);
//                    break;
//                case "加班費":
//                    Log.w("抓到","加班費");
//                    textView_pic2.setBackgroundResource(R.mipmap.ic_apply_money);
//                    break;
////                case "null":
////                    Log.w("抓到","null");
////                    textView_pic2.setBackgroundResource(R.mipmap.ic_apply_noapply);
////                    break;
//            }
//            //判斷 是否放棄
//            switch (Application_Detail_Item_List.get(position).F_Apply().trim())
//            {
//                case "true"://申請
//                    Log.w("抓到F_Apply","true");
//                    //textView_pic2.setBackgroundResource(R.mipmap.ic_apply_noapply);
//                    Application_Linearlayout.setBackgroundColor(Color.parseColor("#edeeef"));//反灰
//                    textView_hour.setTextColor(Color.parseColor("#a5b6c3"));
//                    break;
//                case "false"://不申請
//                    Log.w("抓到F_Apply","false");
//                    textView_pic2.setBackgroundResource(R.mipmap.ic_apply_noapply);
//                    Application_Linearlayout.setBackgroundColor(Color.parseColor("#edeeef"));//反灰
//                    textView_hour.setTextColor(Color.parseColor("#a5b6c3"));
//                    break;
//            }
//        }else{
//            //show 編輯
//            textView_pic2.setBackgroundResource(R.mipmap.btn_apply_edit);
//        }
//
////        //判斷 是否編輯過
////        switch (Application_Detail_Item_List.get(position).F_App_Stat().trim())
////        {
////            case "null":
////                break;
////            case "true":
////                Log.w("抓到Stat","true");
////                Application_Linearlayout.setClickable(false);
////                textView_pic2.setBackgroundResource(R.mipmap.ic_apply_check);
////                break;
////            case "false":
////                Log.w("抓到Stat","false");
////                textView_pic2.setBackgroundResource(R.mipmap.btn_apply_edit);
////                break;
////        }
//
//        //******************************判斷 未來 還沒發生的資料 & 過去已經發生 但沒資料的事件
//        //抓出日期比對
//        //欲轉換的日期字串
//        String dateString = Application_Detail_Item_List.get(position).F_Date().substring(0,Application_Detail_Item_List.get(position).F_Date().length()-9);
//        //設定日期格式
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//        //進行轉換
//        try {
//            Date date = sdf.parse(dateString); // 每筆資料的日期
//            //Log.w("CCCCCCCCCCC日期",String.valueOf(sdf.format(date)));
//            if (System.currentTimeMillis() < date.getTime()){
//                Log.w("CCCCCCCCCCC日期",String.valueOf(sdf.format(date))+"尚未發生");
//                textView_hour.setText("尚無紀錄");
//                textView_hour.setTextColor(Color.parseColor("#a5b6c3"));
//                textView_hour.setTextSize(24);
//                textView_slash.setVisibility(View.GONE);
//                textView_si.setVisibility(View.GONE);
//                textView_pic1.setVisibility(View.INVISIBLE);
//                textView_pic2.setVisibility(View.INVISIBLE);
//                linearlayout_totalhour.setLayoutParams(textView_weekdate.getLayoutParams());
//            }else{
//                //判斷總時數F_TotalHour 是否有加班紀錄  ，如果沒有的話禁止 被點選  F_App_Stat
//                if(Application_Detail_Item_List.get(position).F_TotalHour() == "null"){
//                    Application_Linearlayout.setClickable(false);
//                    textView_hour.setText("無加班紀錄");
//                    textView_hour.setTextColor(Color.parseColor("#a5b6c3"));
//                    textView_hour.setTextSize(24);
//                    textView_slash.setVisibility(View.GONE);
//                    textView_si.setVisibility(View.GONE);
//                    textView_pic1.setVisibility(View.INVISIBLE);
//                    textView_pic2.setVisibility(View.INVISIBLE);
//                    linearlayout_totalhour.setLayoutParams(textView_weekdate.getLayoutParams());
//                }else{
//                    textView_hour.setText(Application_Detail_Item_List.get(position).F_TotalHour());
//                }
////                //判斷 7天之前的資料都反灰
////                Date base=new Date();// 現在時間
////                Long ut1=base.getTime(); // 現在時間
////                Long ut2=date.getTime(); //每筆資料時間
////                Long test = ut1 -ut2; //毫秒差
////                Long daaa = test/(1000*60*60*24); //毫秒轉 日 差
////                Log.w("日期相減", String.valueOf(daaa));
////                if(daaa > 7){
////                    Application_Linearlayout.setBackgroundColor(Color.parseColor("#edeeef"));//反灰
////                    Application_Linearlayout.setClickable(false);
////                }
//                //判斷 跨週 的資料都反灰
//                SimpleDateFormat bbb = new SimpleDateFormat("ww");
//                String testst1 = bbb.format(date);//每比資料轉成週數
//                Date base=new Date();// 現在時間
//                String testst2 = bbb.format(base);//今天日期轉成週數
//                int ut1=Integer.valueOf(testst1); // 現在時間
//                int ut2=Integer.valueOf(testst2);//每筆資料時間
//                week_curry = ut2 -ut1; //毫秒差
//                Log.w("週次相減", String.valueOf(week_curry));
//                if(week_curry != 0){
//                    Application_Linearlayout.setBackgroundColor(Color.parseColor("#edeeef"));//反灰
//                    Application_Linearlayout.setClickable(false);
//                }
//
//
////DayofWeek
//                //************************抓 當週   Dayofweek 1   &&  Dayofweek 7 比較  判斷是否為同個月份  不同的話  個別抓出值 抓 申請總時數
//                if(Application_Detail_Item_List.get(position).DayofWeek() == "1"){
//                    Log.w("WWWWWWWWWWWW",String.valueOf(Application_Detail_Item_List.get(position).F_Date()));
//                }
//
//
//
//
//
//            }
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }


            Log.w("DetailAdapter","test");

            return v;
        }


    }

}
