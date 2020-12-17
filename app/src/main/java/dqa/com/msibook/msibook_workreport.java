package dqa.com.msibook;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class msibook_workreport extends AppCompatActivity {


    private SwipeRefreshLayout mSwipeRefreshLayout; //頁面刷新

    private Context mContext;

    private ProgressDialog progressBar;//讀取狀態

    private ExpandableListAdapter listAdapter;
    private ExpandableListView expListView;

    private List<WorkReport_Item> WorkReport_Item_list = new ArrayList<WorkReport_Item>();
    private List<WorkReport_Item> WorkReport_Item_filter = new ArrayList<WorkReport_Item>();
    private List<List<WorkReport_Item>> Child_WorkReport_Item_Item_list = new ArrayList<List<WorkReport_Item>>();


    private TextView txt_Notifacation_info;

    private TextView text_show_week;

    private Button back_week;

    private Button next_week;

    private TextView tv_dw1_TotalCount;

    private TextView tv_dw1_TotalＨour;

    private TextView tv_dw1_TotalCost;

    private ExpandableListView exp_work_list;


    private String SetNowWeek;//
    private String SetBaseWeek;//
    private String SetNowYear;//
    private String SetBaseYear;//

    private Date tdt;
    private Date td;

    private Date testWeek;
    private Date testWeek2;

    private Long daaa;
    private Date baseuse;

    private Integer D1; // 放D1的月
    private Integer D7; //放 D7的月

    private Integer AddCount;
    private Double AddHour;
    private Double AddCost;
    private String All_Creat_time;

    private Double day7;
    private Double day1;
    private Double day2;
    private Double day3;
    private Double day4;
    private Double day5;
    private Double day6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msibook_workreport);

        mContext = this;

        //讀取時間Bar
        progressBar = new ProgressDialog(this.mContext);
        progressBar.setCancelable(false);
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.setMessage("資料載入中");

        txt_Notifacation_info = (TextView) findViewById(R.id.txt_Notifacation_info);//
        text_show_week = (TextView) findViewById(R.id.text_show_week);//
        back_week = (Button) findViewById(R.id.back_week);//
        next_week = (Button) findViewById(R.id.next_week);//
        tv_dw1_TotalCount = (TextView) findViewById(R.id.tv_dw1_TotalCount);//
        tv_dw1_TotalＨour = (TextView) findViewById(R.id.tv_dw1_TotalＨour);//
        tv_dw1_TotalCost = (TextView) findViewById(R.id.tv_dw1_TotalCost);//

        AddCount = 0;
        AddHour = 0.0;
        AddCost = 0.0;

        //用來測試存每日的報告加總
        day7 = 0.0;
        day1 = 0.0;
        day2 = 0.0;
        day3 = 0.0;
        day4 = 0.0;
        day5 = 0.0;
        day6 = 0.0;


        expListView = (ExpandableListView) findViewById(R.id.exp_work_list);

        tdt= Calendar.getInstance().getTime();
        td = tdt;

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

        Log.w("Now&Base",SetBaseYear+"--"+SetBaseWeek);
        Find_Weekly_Report_List(UserData.WorkID,SetBaseYear,SetBaseWeek);

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
                SimpleDateFormat sdfyy = new SimpleDateFormat("yyyy"); //讓日期顯示為週數

                //判斷週次是否為當前週次，如果是的話就-1，不是的話就把上次計算的值在 -1
                if (String.valueOf(SetNowWeek) == String.valueOf(Base.get(Calendar.WEEK_OF_YEAR))){
                    Calendar now = Calendar.getInstance();
                    now.add(Calendar.WEEK_OF_YEAR, -1);//把 now抓出來做減  1 週
                    tdt=now.getTime(); //  減完的週數  給 tdt
                    td = tdt; // td = tdt
                    Log.w("td時間",String.valueOf(sdf.format(td)));
                    SetNowWeek = String.valueOf(sdf.format(tdt));
                    SetNowYear = String.valueOf(sdfyy.format(tdt));
                    Log.w("111111111111tdttdttdtt",String.valueOf(sdf.format(tdt)));
                    Log.w("111111111111tdttdttdtt",String.valueOf(sdf.format(td)));
                    Log.w("getWork_ID",UserData.WorkID);
                    text_show_week.setText(String.valueOf(sdf.format(tdt)));
                    Find_Weekly_Report_List(UserData.WorkID,SetNowYear,SetNowWeek);
                    next_week.setVisibility(View.VISIBLE);

                }else{
                    Date test = td;
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(test);
                    calendar.add(Calendar.WEEK_OF_YEAR, -1);
                    td = calendar.getTime();
                    SetNowWeek = String.valueOf(sdf.format(td));
                    SetNowYear = String.valueOf(sdfyy.format(td));
                    Log.w("22222222222222tdtdtdtk",String.valueOf(sdf.format(tdt)));
                    Log.w("22222222222222tdtdtdtk",String.valueOf(sdf.format(td)));
                    text_show_week.setText(String.valueOf(sdf.format(td)));
                    Find_Weekly_Report_List(UserData.WorkID,SetNowYear,SetNowWeek);
                    next_week.setVisibility(View.VISIBLE);
                }

            }
        });
        next_week.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //定義好時間字串的格式
                SimpleDateFormat sdf = new SimpleDateFormat("ww"); //讓日期顯示為週數
                SimpleDateFormat sdfyy = new SimpleDateFormat("yyyy"); //讓日期顯示為週數

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
                    SetNowYear = String.valueOf(sdfyy.format(td));
                    Log.w("SetNowWeek",String.valueOf(sdf.format(td)));
                    Log.w("BaseWeek",String.valueOf(Base.get(Calendar.WEEK_OF_YEAR)));
                    text_show_week.setText(String.valueOf(sdf.format(td)));
                    Find_Weekly_Report_List(UserData.WorkID,SetNowYear,SetNowWeek);

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
                    Find_Weekly_Report_List(UserData.WorkID,SetNowYear,SetNowWeek);
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

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh_layout);

        //lsv_main = (ListView) mSwipeRefreshLayout.findViewById(R.id.listView);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                mSwipeRefreshLayout.setRefreshing(false);

                Find_Weekly_Report_List(UserData.WorkID,SetNowYear,SetNowWeek);

            }
        });
    }


    //部門職缺資訊
    public void Find_Weekly_Report_List(String F_Keyin,String Year,String Week) {
        Log.w("TTTTTTT",F_Keyin+"--"+Year+"--"+Week);
        //顯示  讀取等待時間Bar
        progressBar.show();

        WorkReport_Item_list.clear();
        WorkReport_Item_filter.clear();
        Child_WorkReport_Item_Item_list.clear();

        AddCount = 0;
        AddHour = 0.0;
        AddCost = 0.0;

        RequestQueue mQueue = Volley.newRequestQueue(this);

        String Path = "http://wtsc.msi.com.tw/IMS/Weekly_Report.asmx/Find_Weekly_Report_List?F_Keyin=" + F_Keyin + "&Year=" + Year + "&Week=" + Week;
        //String Path = GetServiceData.ServicePath + "/Find_Certification_Model?WorkID=" + WorkID;

        GetServiceData.getString(Path, mQueue, new GetServiceData.VolleyCallback() {
            @Override
            public void onSuccess(JSONObject result) {

                try {

                    JSONArray UserArray = new JSONArray(result.getString("Key"));

                    final Map<String, ArrayList<WorkReport_Item>> mapContent = new HashMap<String,ArrayList<WorkReport_Item>>();

                    List<WorkReport_Item> fatherList = new ArrayList<>();
                    List<List<WorkReport_Item>> childList = new ArrayList<>();
                    Map<String, ArrayList<WorkReport_Item>> linkHashMap = new LinkedHashMap<>();

                    AddCount = UserArray.length();
                    for (int i = 0; i < UserArray.length(); i++) {

                        JSONObject IssueData = UserArray.getJSONObject(i);

                        String F_SeqNo = String.valueOf(IssueData.getInt("F_SeqNo"));// 1906864,

                        String F_CreateDate = String.valueOf(IssueData.getString("F_CreateDate").replace("T"," "));//"2020-01-17T21:39:00",
                                Log.w("F_CreateDate",F_CreateDate);


                        String F_UpdateTime = String.valueOf(IssueData.getString("F_UpdateTime").replace("T"," ")).substring(0,10);//"2020-01-17T21:39:00",

                        String F_Keyin = String.valueOf(IssueData.getString("F_Keyin"));//"10015812",

                        String F_Owner = String.valueOf(IssueData.getString("F_Owner"));//""廖賴國",

                        String F_Date = String.valueOf(IssueData.getString("F_Date")).substring(0,10);// "2020-01-13T00:00:00",

                        String F_ModelID = String.valueOf(IssueData.getString("F_ModelID"));// "13364",

                        String F_DeptID = String.valueOf(IssueData.getString("F_DeptID"));// "21757",

                        String ModelName = String.valueOf(IssueData.getString("ModelName"));// "MS- 0050[Android]",

                        String F_RegionID = String.valueOf(IssueData.getString("F_RegionID"));// "1",

                        String F_WorkHour = String.valueOf(IssueData.getDouble("F_WorkHour"));// 8,
                        AddHour += Double.valueOf(F_WorkHour);

                        String F_Subject = String.valueOf(IssueData.getString("F_Subject"));//"[APP]msiBook能效認證改版",

                        String F_Master_Table = String.valueOf(IssueData.getString("F_Master_Table"));//  "C_ResourceShare_Progress"

                        String F_Master_ID = String.valueOf(IssueData.getString("F_Master_ID"));// 150289,

                        String F_CostSum = String.valueOf(IssueData.getDouble("F_CostSum"));//2800
                        AddCost += Double.valueOf(F_CostSum);

                            if (!linkHashMap.containsKey(F_Date)){  //加入表內
                                ArrayList<WorkReport_Item>  innchildList = new ArrayList<>();
                                innchildList.add(new WorkReport_Item(F_SeqNo,F_CreateDate,F_UpdateTime,F_Keyin,F_Owner,F_Date,F_ModelID,F_DeptID,ModelName,F_RegionID,F_WorkHour,F_Subject,F_Master_Table,F_Master_ID,F_CostSum));
                                linkHashMap.put(F_Date , innchildList);
                            } else {
                                linkHashMap.get(F_Date).add(new WorkReport_Item(F_SeqNo,F_CreateDate,F_UpdateTime,F_Keyin,F_Owner,F_Date,F_ModelID,F_DeptID,ModelName,F_RegionID,F_WorkHour,F_Subject,F_Master_Table,F_Master_ID,F_CostSum));
                            }



                    }

                    DecimalFormat mDecimalFormat = new DecimalFormat("#,###.##");
                    String str__Cost = mDecimalFormat.format(Double.parseDouble(String.valueOf(AddCost)));
                    tv_dw1_TotalCount.setText(String.valueOf(AddCount));
                    tv_dw1_TotalＨour.setText(String.valueOf(AddHour));
                    tv_dw1_TotalCost.setText(String.valueOf(str__Cost));


                    int index = 0;
                    for (Map.Entry<String, ArrayList<WorkReport_Item>>  item  : linkHashMap.entrySet()){
                        fatherList.add(index  , item.getValue().get(0)) ;
                        ArrayList<WorkReport_Item>  tempList = new ArrayList<>();
                        for (WorkReport_Item childItem : item.getValue()){
                            tempList.add(childItem);
                        }
                        childList.add(index, tempList);
                        index++;
                    }


                    listAdapter = new ExpandableListAdapter(mContext, fatherList, childList);
                    // setting list adapter
                    expListView.setAdapter(listAdapter);

                    for (int i = 0 ;i< fatherList.size();i++)//預設展開
                    {
                        expListView.expandGroup(i);
                    }

                    expListView.setGroupIndicator(null);

                    //關閉-讀取等待時間Bar
                    progressBar.dismiss();


                }
                catch (JSONException ex) {

                    Log.w("Json",ex.toString());
                }

            }
        });


    }

    public class WorkReport_Item {

        String F_SeqNo;
        String F_CreateDate;
        String F_UpdateTime;
        String F_Keyin;
        String F_Owner;
        String F_Date;
        String F_ModelID;
        String F_DeptID;
        String ModelName;
        String F_RegionID;
        String F_WorkHour;
        String F_Subject;
        String F_Master_Table;
        String F_Master_ID;
        String F_CostSum;

        public WorkReport_Item(String F_SeqNo,
                               String F_CreateDate,
                               String F_UpdateTime,
                               String F_Keyin,
                               String F_Owner,
                               String F_Date,
                               String F_ModelID,
                               String F_DeptID,
                               String ModelName,
                               String F_RegionID,
                               String F_WorkHour,
                               String F_Subject,
                               String F_Master_Table,
                               String F_Master_ID,
                               String F_CostSum)
        {
            this.F_SeqNo = F_SeqNo;
            this.F_CreateDate = F_CreateDate;
            this.F_UpdateTime = F_UpdateTime;
            this.F_Keyin = F_Keyin;
            this.F_Owner = F_Owner;
            this.F_Date = F_Date;
            this.F_ModelID = F_ModelID;
            this.F_DeptID = F_DeptID;
            this.ModelName = ModelName;
            this.F_RegionID = F_RegionID;
            this.F_WorkHour = F_WorkHour;
            this.F_Subject = F_Subject;
            this.F_Master_Table = F_Master_Table;
            this.F_Master_ID = F_Master_ID;
            this.F_CostSum = F_CostSum;

        }

        public String GetF_SeqNo()
        {
            return this.F_SeqNo;
        }
        public String GetF_CreateDate()
        {
            return this.F_CreateDate;
        }
        public String GetF_UpdateTime()
        {
            return this.F_UpdateTime;
        }
        public String GetF_F_Keyin()
        {
            return this.F_Keyin;
        }
        public String GetF_Owner()
        {
            return this.F_Owner;
        }
        public String GetF_Date()
        {
            return this.F_Date;
        }
        public String GetF_ModelID()
        {
            return this.F_ModelID;
        }
        public String GetF_DeptID()
        {
            return this.F_DeptID;
        }
        public String GetModelName()
        {
            return this.ModelName;
        }
        public String GetF_RegionID()
        {
            return this.F_RegionID;
        }
        public String GetF_WorkHour()
        {
            return this.F_WorkHour;
        }
        public String GetF_Subject()
        {
            return this.F_Subject;
        }
        public String GetF_Master_Table()
        {
            return this.F_Master_Table;
        }
        public String GetF_Master_ID()
        {
            return this.F_Master_ID;
        }
        public String GetF_CostSum()
        {
            return this.F_CostSum;
        }


    }//給Group用

    // ExpandableListAdapter
    public class ExpandableListAdapter extends BaseExpandableListAdapter {
        private Context _context;
        private List<WorkReport_Item> fatherList = new ArrayList<>();
        private List<List<WorkReport_Item>> childList = new ArrayList<>();

        public ExpandableListAdapter(Context context, List<WorkReport_Item> fatherList , List<List<WorkReport_Item>> childList){
            this._context = context;
            this.fatherList = fatherList;
            this.childList = childList;
        }

        @Override
        public int getGroupCount() {

            return this.fatherList.size();
        }

        @Override
        public int getChildrenCount(int groupPosition) {

            return  this.childList.get(groupPosition).size();
        }

        @Override
        public Object getGroup(int groupPosition) {
            return this.fatherList.get(groupPosition);
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return this.childList.get(groupPosition).get(childPosition);
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            WorkReport_Item WorkReport_Item = (WorkReport_Item)  getGroup(groupPosition);

            if (convertView == null){
                LayoutInflater infalInflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = infalInflater.inflate(R.layout.msibook_workreport_group_item,null);
            }

            TextView textView_project = (TextView) convertView.findViewById(R.id.textView_project);

            ImageView parentImageViw = (ImageView) convertView.findViewById(R.id.parentImageViw);

            textView_project.setText(WorkReport_Item.GetF_Date());

            //判斷isExpanded就可以控制是按下還是關閉，同時更換圖片
            if(isExpanded){
                parentImageViw.setBackgroundResource(R.mipmap.ehr_btn_common_collapse);
            }else{
                parentImageViw.setBackgroundResource(R.mipmap.ehr_btn_common_unfolded);
            }

            return convertView;
        }

        @Override
        public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

            final WorkReport_Item WorkReport_Item = (WorkReport_Item)getChild(groupPosition,childPosition);

            //final String childText = (String) getChild(groupPosition, childPosition);
            if (convertView == null) {
                LayoutInflater infalInflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = infalInflater.inflate(R.layout.msibook_workreport_child_item,null);
            }
            LinearLayout linear_main = (LinearLayout) convertView.findViewById(R.id.linear_main);
            TextView textView_ModelName = (TextView) convertView.findViewById(R.id.textView_ModelName);
            TextView textView_F_WorkHour = (TextView) convertView.findViewById(R.id.textView_F_WorkHour);
            TextView textView_F_Subject = (TextView) convertView.findViewById(R.id.textView_F_Subject);

            textView_ModelName.setText(WorkReport_Item.GetModelName());
            textView_F_WorkHour.setText(WorkReport_Item.GetF_WorkHour());
            textView_F_Subject.setText(WorkReport_Item.GetF_Subject());

            SimpleDateFormat dateFormatParse = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat dateFormatParse_week = new SimpleDateFormat("E");

            Date dateString = null;
            String date_formate=null;
            try {
                dateString = dateFormatParse.parse(WorkReport_Item.GetF_Date());//文字轉日期
                date_formate = dateFormatParse_week.format(dateString);//日期 Fromat 自訂格式 成字串

                Date test = dateString;
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(test);

                td = calendar.getTime();
                Log.w("WWWWWWWWWWWWW",String.valueOf(calendar.get(Calendar.DAY_OF_WEEK)));

            } catch (ParseException e) {
                e.printStackTrace();
                Log.w("eeeeee",e.getMessage());
            }

            //專案細項to 第三頁
            linear_main.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();

                    intent.putExtra("ModelName",WorkReport_Item.GetModelName());//

                    intent.putExtra("F_Subject",WorkReport_Item.GetF_Subject());//

                    intent.putExtra("F_CreateDate",WorkReport_Item.GetF_CreateDate());//

                    intent.putExtra("F_WorkHour",WorkReport_Item.GetF_WorkHour());//

                    intent.putExtra("F_CostSum",WorkReport_Item.GetF_CostSum());//

                    intent.setClass(msibook_workreport.this, msibook_workreport_detail.class);
                    //開啟Activity
                    startActivity(intent);
                }
            });

            return convertView;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }
    }





}
