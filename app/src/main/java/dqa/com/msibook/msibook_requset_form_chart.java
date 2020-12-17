package dqa.com.msibook;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ViewPortHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class msibook_requset_form_chart extends AppCompatActivity {

    private Context mContext;
    private ProgressDialog progressBar;

    private Project_cnt_manAdapter mProject_cnt_manAdapter;
    private List<Project_cnt_manAdapter_Item> Project_cnt_manAdapter_Item_List = new ArrayList<Project_cnt_manAdapter_Item>();

    private ListView mListView;
    private View mView;

    private TextView textView_title;
    private TextView textView_select_year;
    private Button textView_back;
    private Button textView_next;
    private TextView textView_project_count;
    private BarChart mBarChart;
    private ListView listview_project_cnt;

    private String SetNowYear;
    private Date baseuse;
    private Date tdt;
    private Date td;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msibook_requset_form_chart);
        mContext = this;


        progressBar = new ProgressDialog(this);
        progressBar.setCancelable(true);
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.setMessage("資料載入中");

        textView_project_count = (TextView) findViewById(R.id.textView_project_count);
        textView_select_year = (TextView) findViewById(R.id.textView_select_year);
        textView_back = (Button) findViewById(R.id.textView_back);
        textView_next= (Button) findViewById(R.id.textView_next);

        mBarChart = (BarChart) findViewById(R.id.chart1);

        SimpleDateFormat yyyy = new SimpleDateFormat("yyyy"); //一開始給讀DB用 - 月份
        baseuse = Calendar.getInstance().getTime();
        final Calendar Base_Year = Calendar.getInstance();//宣告行事曆
        final Calendar Base = Calendar.getInstance();//宣告行事曆
        final String Baseuse = String.valueOf(yyyy.format(baseuse));
        textView_select_year.setText(String.valueOf(Base_Year.get(Calendar.YEAR)));
        SetNowYear = String.valueOf(Base_Year.get(Calendar.YEAR));

        RequestForm_IPS_Statistics(String.valueOf(Base_Year.get(Calendar.YEAR)));

        if (String.valueOf(SetNowYear) == String.valueOf(yyyy.format(baseuse))){
        }else{
            textView_next.setVisibility(View.INVISIBLE);
        }

        tdt= Calendar.getInstance().getTime();
        td = tdt;

        textView_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //定義好時間字串的格式
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy"); //讓日期顯示為週數

                //判斷週次是否為當前週次，如果是的話就-1，不是的話就把上次計算的值在 -1
                if (String.valueOf(SetNowYear) == String.valueOf(Base.get(Calendar.YEAR))){
                    Calendar now = Calendar.getInstance();
                    now.add(Calendar.YEAR, -1);//把 now抓出來做減  1 週
                    tdt=now.getTime(); //  減完的週數  給 tdt
                    td = tdt; // td = tdt
                    Log.w("td時間",String.valueOf(sdf.format(td)));
                    SetNowYear = String.valueOf(sdf.format(tdt));
                    textView_select_year.setText(String.valueOf(sdf.format(tdt)));
                    RequestForm_IPS_Statistics(SetNowYear);
                    textView_next.setVisibility(View.VISIBLE);

                }else{
                    Date test = td;
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(test);
                    calendar.add(Calendar.YEAR, -1);
                    td = calendar.getTime();
                    SetNowYear = String.valueOf(sdf.format(td));
                    Log.w("22222222222222tdtdtdtk",String.valueOf(sdf.format(tdt)));
                    Log.w("22222222222222tdtdtdtk",String.valueOf(sdf.format(td)));
                    textView_select_year.setText(String.valueOf(sdf.format(td)));
                    RequestForm_IPS_Statistics(SetNowYear);
                    textView_next.setVisibility(View.VISIBLE);
                }

            }
        });

        textView_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //定義好時間字串的格式
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy"); //讓日期顯示為週數

                Date AfterDate = td;
                Calendar calendar2 = Calendar.getInstance();
                calendar2.add(Calendar.YEAR, +1);
                calendar2.setTime(AfterDate);

                if(System.currentTimeMillis() > AfterDate.getTime())
                {
                    Date test = td;
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(test);
                    calendar.add(Calendar.YEAR, +1);
                    td = calendar.getTime();
                    SetNowYear = String.valueOf(sdf.format(td));
                    Log.w("SetNowWeek",String.valueOf(sdf.format(td)));
                    Log.w("BaseWeek",String.valueOf(Base.get(Calendar.YEAR)));
                    textView_select_year.setText(String.valueOf(sdf.format(td)));
                    RequestForm_IPS_Statistics(SetNowYear);

                    SimpleDateFormat zxc = new SimpleDateFormat("ww");// 設定 文字轉日期
                    try {
                        Date A1 = zxc.parse(SetNowYear); // 經過加減過的月份
                        Date A2 = zxc.parse(Baseuse); //目前的月份
                        Long ut1=A1.getTime(); //讀取時間
                        Long ut2=A2.getTime(); //讀取時間
                        Long timeP=ut2-ut1; //算毫秒差
                        Log.w("timePtime",String.valueOf(timeP));
                        if(timeP == 0){ //如果 == 0 表示目前已經到達 今天的月份 把按扭給隱藏
                            textView_next.setVisibility(View.INVISIBLE);
                        }

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    //Application_Info(getWork_ID,SetNowWeek);
                }else{

                }
            }
        });



    }

    // IPS統計數據
    public void RequestForm_IPS_Statistics(String year) {

        //顯示  讀取等待時間Bar
        progressBar.show();

        RequestQueue mQueue = Volley.newRequestQueue(this);

        Project_cnt_manAdapter_Item_List.clear();

        final ArrayList<BarEntry> yVals11 = new ArrayList<BarEntry>();

        final ArrayList<String> mylist = new ArrayList<String>();

        final Integer[] Total_Count = new Integer[1];

        getString("http://wtsc.msi.com.tw/IMS/MsiBook_App_Service.asmx/RequestForm_IPS_Statistics?year=" + year, mQueue, new GetServiceData.VolleyCallback() {
            @Override
            public void onSuccess(JSONObject result) {

                try {

                    JSONArray UserArray = new JSONArray(result.getString("Key"));

                    ArrayList<PieEntry> entries = new ArrayList<PieEntry>();

                    // NOTE: The order of the entries when being added to the entries array determines their position around the center of
                    // the chart.

                    int j = 0;

                    Total_Count[0] = UserArray.length();

                    textView_project_count.setText(String.valueOf(Total_Count[0]));

                    if (UserArray.length() > 0) {
                        mBarChart.setVisibility(View.VISIBLE);

                        for (int i = 0; i < UserArray.length(); i++) {
                            JSONObject IssueData = UserArray.getJSONObject(i);
                            if (IssueData.getString("F_Name") == "null") {

                            } else {
                                String F_Name = IssueData.getString("F_Name");//NPI Sample
                                String str_F_Name = F_Name.replace(" ", "\n");

                                String F_ID = IssueData.getString("F_ID");// ID

                                Integer Count = IssueData.getInt("Count");//專案


                                Double Rate = IssueData.getDouble("Rate");//人力


                                float a = Rate.floatValue();//圓餅圖用的%數值過濾"其他"
                                if (str_F_Name.indexOf("其他") == -1) {//過濾掉 "其他"
                                    //entries.add(new PieEntry(a, M_Type));}
                                    //entries.add(new PieEntry(a, M_Type + "  " + String.valueOf(a) + "  %"));
                                    //長條圖使用
                                    mylist.add(j, str_F_Name);//X軸標籤
                                    yVals11.add(new BarEntry(j, Float.parseFloat(String.valueOf(Rate * 100))));//專案比重

                                    //Project_cnt_manAdapter_Item_List.add(j, new Project_cnt_manAdapter_Item(M_Type, String.valueOf(Cnt), String.valueOf(WorkHour)));
                                    Project_cnt_manAdapter_Item_List.add(j, new Project_cnt_manAdapter_Item(F_ID,F_Name, String.valueOf(Count), String.format("%.2f", Rate)));
                                    j++;
                                } else {
                                    //Project_cnt_manAdapter_Item_List.add(i,new Project_cnt_manAdapter_Item("","",""));
                                }
                                //下方List使用


                            }
                            mListView = (ListView) findViewById(R.id.listview_project_cnt);

                            mProject_cnt_manAdapter = new Project_cnt_manAdapter(mContext, Project_cnt_manAdapter_Item_List);

                            mListView.setAdapter(mProject_cnt_manAdapter);

                            //點擊BU to 彈跳視窗
                            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                                Intent intent = new Intent();

                                intent.putExtra("F_Name", Project_cnt_manAdapter_Item_List.get(position).GetF_Name());//代年到下一頁

                                intent.putExtra("F_ID", Project_cnt_manAdapter_Item_List.get(position).GetF_ID());//代年到下一頁

                                intent.setClass(msibook_requset_form_chart.this, msibook_requset_form_chart_detail.class);
                                //開啟Activity
                                startActivity(intent);


                                }
                            });


                        }

                        String[] stockArr = new String[mylist.size()];
                        stockArr = mylist.toArray(stockArr);
                        mBarChart.getDescription().setEnabled(false);

                        // if more than 60 entries are displayed in the chart, no values will be
                        // drawn
                        //mChart.setMaxVisibleValueCount(100);

                        // scaling can now only be done on x- and y-axis separately
                        mBarChart.setPinchZoom(false);//y轴的值是否跟随图表变换缩放;如果禁止，y轴的值会跟随图表变换缩放
                        mBarChart.setTouchEnabled(false); // 设置是否可以触摸
                        mBarChart.setDrawBarShadow(false);
                        mBarChart.setDrawGridBackground(false);//setGroupSpace

                        int xlabelColor = getResources().getColor(R.color.Request_Form_chartxlabelColor);
                        XAxis xAxis = mBarChart.getXAxis();
                        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                        xAxis.setDrawGridLines(false);//绘制网格线，默认为true
                        xAxis.setDrawAxisLine(true);// X軸 底線
                        xAxis.setDrawLabels(true);
                        xAxis.setTextColor(xlabelColor);

                        YAxis leftAxis = mBarChart.getAxisLeft();//获取左轴
                        // YAxis rightAxis = chart.getAxisRight();//获取右轴
                        leftAxis.setSpaceBottom(5);
                        leftAxis.setDrawGridLines(false);//绘制网格线 默认为true
                        leftAxis.setDrawZeroLine(true);//是否繪製0所在的網格線

                        int noneColor = getResources().getColor(R.color.chartnoneColorColor);
                        mBarChart.getAxisLeft().setTextColor(noneColor);
                        mBarChart.getAxisRight().setTextColor(noneColor);
                        mBarChart.getAxisRight().setEnabled(true);//隐藏右轴  默认显示
                        mBarChart.getAxisLeft().setEnabled(true);//隐藏左轴  默认显示

                        mBarChart.getAxisLeft().setDrawGridLines(false);
                        // setting data

                        // add a nice and smooth animation
                        mBarChart.animateY(100);

                        mBarChart.getLegend().setEnabled(false);

                        BarDataSet set1;

                        if (mBarChart.getData() != null && mBarChart.getData().getDataSetCount() > 0) {
                            set1 = (BarDataSet) mBarChart.getData().getDataSetByIndex(0);
                            set1.setValues(yVals11);

                            mBarChart.getData().notifyDataChanged();
                            mBarChart.notifyDataSetChanged();

                        } else {
                            set1 = new BarDataSet(yVals11, "");
                            int BarColor = getResources().getColor(R.color.Request_Form_chart_ValueColor);
                            int valueColor = getResources().getColor(R.color.Request_Form_chart_ValueColor);
                            set1.setColors(BarColor);
                            set1.setDrawValues(true); //條圖上面的數值
                            set1.setValueTextColor(valueColor);

                            ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
                            dataSets.add(set1);

                            BarData data = new BarData(dataSets);

                            data.setBarWidth(0.4f);//線條寬度
                            data.setValueTextSize(16);

                            mBarChart.setData(data);
                            mBarChart.setFitBars(true);

                        }
                        mBarChart.getXAxis().setLabelCount(stockArr.length);
                        mBarChart.getXAxis().setValueFormatter(new LabelFormatter(stockArr));
                        mBarChart.getXAxis().setTextSize(Float.parseFloat("10"));

                        mBarChart.getDescription().setEnabled(false);
                        //X-axis
//                    XAxis xxAxis = mBarChart.getXAxis();
//                    xxAxis.setGranularity(1f);
//                    xxAxis.setGranularityEnabled(true);
//                    xxAxis.setCenterAxisLabels(true);
//                    xxAxis.setDrawGridLines(false);
//                    xxAxis.setLabelCount(stockArr.length);
//                    xxAxis.setAxisMaximum(stockArr.length);
//                    xxAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
//                    xxAxis.setValueFormatter(new IndexAxisValueFormatter(stockArr));

                        YAxis lleftAxis = mBarChart.getAxisLeft();
                        YAxis rreftAxis = mBarChart.getAxisRight();

                        lleftAxis.setDrawGridLines(false);
                        lleftAxis.setTextSize(14);
                        rreftAxis.setDrawGridLines(false);
                        rreftAxis.setTextSize(14);
                        lleftAxis.setLabelCount(5, false); //Y軸左側 數值個數
                        rreftAxis.setLabelCount(5, false); //Y軸右側 數值個數


                        mBarChart.notifyDataSetChanged();
                    }
                    else{
                        mBarChart.setVisibility(View.GONE);
                        mListView.setVisibility(View.GONE);
                        mListView.setEmptyView(findViewById(R.id.empty2));
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

    public class LabelFormatter implements IAxisValueFormatter {
        private final String[] mLabels;

        public LabelFormatter(String[] labels) {
            mLabels = labels;
        }

        @Override
        public String getFormattedValue(float value, AxisBase axis) {

            if (mLabels.length - 1 >= value) {
                return mLabels[(int) value];
            } else {
                return mLabels[mLabels.length - 1];
            }


        }
    }


    //-------------------------Item-------------------------
    public class Project_cnt_manAdapter_Item {

        String F_ID;

        String F_Name;

        String F_Count;

        String F_Rate;

        public Project_cnt_manAdapter_Item(String F_ID,String F_Name,String F_Count,String F_Rate)
        {
            this.F_ID = F_ID;

            this.F_Name = F_Name;

            this.F_Count = F_Count;

            this.F_Rate = F_Rate;

        }

        public String GetF_ID()
        {
            return this.F_ID;
        }

        public String GetF_Name()
        {
            return this.F_Name;
        }

        public String GetF_Count()
        {
            return this.F_Count;
        }

        public String GetF_Rate()
        {
            return this.F_Rate;
        }

    }

    //-----------------------Adapter-----------------------
    public class Project_cnt_manAdapter extends BaseAdapter {

        private LayoutInflater mLayInf;

        private List<Project_cnt_manAdapter_Item> Project_cnt_manAdapter_Item_List;

        private Context Project_cnt_man_Context;

        private String Title;

        public Project_cnt_manAdapter(Context context,  List<Project_cnt_manAdapter_Item> Project_cnt_manAdapter_Item_List)
        {
            mLayInf = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            Project_cnt_man_Context = context;

            this.Title = Title;

            this.Project_cnt_manAdapter_Item_List = Project_cnt_manAdapter_Item_List;

        }
        @Override
        public int getCount() {
            return Project_cnt_manAdapter_Item_List.size();
        }

        @Override
        public Object getItem(int position) {
            return Project_cnt_manAdapter_Item_List.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View v = new View(Project_cnt_man_Context);

            v = mLayInf.inflate(R.layout.msibook_request_form_chart_item_adapter, parent, false);

            TextView textView_Name = (TextView) v.findViewById(R.id.textView_name);
            TextView textView_count = (TextView) v.findViewById(R.id.textView_count);
            TextView textView_rate = (TextView) v.findViewById(R.id.textView_rate);

            textView_Name.setText(Project_cnt_manAdapter_Item_List.get(position).GetF_Name());

            textView_count.setText(Project_cnt_manAdapter_Item_List.get(position).GetF_Count());

            textView_rate.setText(String.format("%.2f",(Double.valueOf(Project_cnt_manAdapter_Item_List.get(position).GetF_Rate())*100)));


            Log.w("DetailAdapter","test");

            return v;
        }

    }


}
