package dqa.com.msibook;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
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
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
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
import com.github.mikephil.charting.utils.ViewPortHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link msibook_dqaweekly_project_activity_project.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link msibook_dqaweekly_project_activity_project#newInstance} factory method to
 * create an instance of this fragment.
 */
public class msibook_dqaweekly_project_activity_project extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Context mContext;
    private ProgressDialog progressBar;

    private PieChart mChart1;
    private Typeface tf;

    private BarChart mBarChart;

    private Project_cnt_manAdapter mProject_cnt_manAdapter;
    private List<Project_cnt_manAdapter_Item> Project_cnt_manAdapter_Item_List = new ArrayList<Project_cnt_manAdapter_Item>();

    private ListView mListView;

    private LinearLayout linearlayout_top;
    private LinearLayout linearlayout_down;

    private String SetYear;
    private String SetWeek;
    private String SetRegionID;

    private TextView textView_info;


    //10月18日新增 方框圖形化
    private RecyclerView recyclerView;
    private RecyclerView.Adapter recyclerView_Adapter;
    private RecyclerView.LayoutManager recyclerViewLayoutManager;

    private Button btn_man_resuorce_info;

    private OnFragmentInteractionListener mListener;

    private List<String> ArrayM_Type = new ArrayList<String>();
    public List<String> ArrayPeople = new ArrayList<String>();

    private Double Set_WorkHour_lastweek;
    private Double Set_People_lastweek;
    private Double Set_WorkHour;
    private Double Set_People;

    public msibook_dqaweekly_project_activity_project() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProjectActivity_project.
     */
    // TODO: Rename and change types and number of parameters
    public static msibook_dqaweekly_project_activity_project newInstance(String param1, String param2) {
        msibook_dqaweekly_project_activity_project fragment = new msibook_dqaweekly_project_activity_project();
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

        //讀取時間Bar
        progressBar = new ProgressDialog(getActivity());
        progressBar.setCancelable(false);
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.setMessage("資料載入中");

//        Find_BU_PM_Rate("46","1");
//        Find_PM_NewModel("45");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mContext = getContext();

        View v = inflater.inflate(R.layout.fragment_msibook_dqaweekly_project_activity_project, container, false);

        //*****************長條圖*******************************
        mBarChart = (BarChart) v.findViewById(R.id.chart1);

        //******************************************************

        LinearLayout linearlayout_top = (LinearLayout) v.findViewById(R.id.linearlayout_top);

        LinearLayout linearlayout_down = (LinearLayout) v.findViewById(R.id.linearlayout_down);

        btn_man_resuorce_info = (Button) v.findViewById(R.id.btn_man_resuorce_info);

        //稼動Info 點選事件
        btn_man_resuorce_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();

                intent.putExtra("RegionID", SetRegionID);//代 RegionID到下一頁

                intent.setClass(getActivity(), msibook_project_formula.class);
                //開啟Activity
                startActivity(intent);

            }
        });

        //*********************圓餅型圖***********************
//        //宣告PieChart餅圖屬性
//        mChart1 = (PieChart) v.findViewById(R.id.chart1);
//        mChart1.setTouchEnabled(false);// 设置是否可以触摸
//        mChart1.setDrawSliceText(false);//设置隐藏饼图上文字，只显示百分比
//        mChart1.setUsePercentValues(false);
//        mChart1.getDescription().setEnabled(false);//右下角描述
//        //mChart1.setExtraOffsets(5, 5, 5, 5);
//        mChart1.setDragDecelerationFrictionCoef(0.95f);//与上面那个属性配合，持续滚动时的速度快慢，[0,1) 0代表立即停止。
//        mChart1.setExtraOffsets(0.f, 0.f, 50.f, 0.f);
//        mChart1.setDrawHoleEnabled(true);//設置繪製孔啟用
//        mChart1.setTransparentCircleColor(Color.WHITE);
//        mChart1.setTransparentCircleAlpha(110);//設置透明圓Alpha
//        mChart1.setHoleRadius(80f);//設置孔半徑
//        mChart1.setTransparentCircleRadius(61f);//設置透明圓半徑
//        mChart1.setDrawCenterText(false);//設置繪製中心文本
//        mChart1.setRotationAngle(50f);//設置旋轉角度
//        mChart1.setRotationEnabled(true);//設置旋轉啟用
//        mChart1.setHighlightPerTapEnabled(false);//設置突出顯示已啟用PerTap
////        mChart1.setOnChartValueSelectedListener(this);//設置在圖表值選擇的監聽器
//        mChart1.animateY(1400, Easing.EasingOption.EaseInOutQuad);
//        Legend l = mChart1.getLegend();
//        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
//        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
//        l.setOrientation(Legend.LegendOrientation.VERTICAL);
//        l.setDrawInside(false);
//        l.setEnabled(true);//顯示圖表右側 顏色方框+描述提示
//        l.setTextSize(18);
//        l.setTextColor(Color.parseColor("#656565"));
        //************************************************
        // Inflate the layout for this fragment
//        Find_BU_PM_Rate("46","1");
//        Find_PM_NewModel("45");

        //方框圖形 Recycle宣告
        recyclerView = (RecyclerView) v.findViewById(R.id.recycler_view1);


        return v;
    }

    //抓週
    public void SetWeek(String Week) {
        SetWeek = Week;
    }
    //抓年
    public void SetYear(String Year) {
        SetYear = Year;
    }
    //抓台or昆or寶
    public void SetRegionID(String RegionID) {
        SetRegionID = RegionID;
    }

    //專案資源分配資訊
    public void Find_BU_PM_Rate_LastWeek(final String Week, final String Year, final String Region, final String Dept) {

        String laseWeek="";
        String laseYear="";
        //先撈上週
        if(Integer.valueOf(Week)!=0) {
            if(Integer.valueOf(Week)-1==1){
                laseYear = String.valueOf(Integer.valueOf(Year) - 1);
                laseWeek = "53";
            }else{
                laseYear = Year;
                laseWeek = String.valueOf(Integer.valueOf(Week) - 1);
            }
        }

        textView_info = (TextView) getActivity().findViewById(R.id.textView_info);
        switch(Integer.valueOf(Region)) {
            case 1:
                textView_info.setVisibility(View.VISIBLE);
                break;
            case 2:
                textView_info.setVisibility(View.INVISIBLE);
                break;
            case 3:
                textView_info.setVisibility(View.INVISIBLE);
                break;
        }

        RequestQueue mQueue = Volley.newRequestQueue(getActivity());

        Project_cnt_manAdapter_Item_List.clear();

        ArrayM_Type.clear();
        ArrayPeople.clear();

        getString("http://wtsc.msi.com.tw/IMS/Weekly_Report.asmx/Find_BU_PM_Rate?Week=" + laseWeek + "&Year=" + laseYear + "&RegionID=" + Region+ "&Dept=" + Dept, mQueue, new GetServiceData.VolleyCallback() {
            @Override
            public void onSuccess(JSONObject result) {

                try {

                    JSONArray UserArray = new JSONArray(result.getString("Key"));

                    int j = 0;

                    for (int i = 0; i < UserArray.length(); i++) {
                        JSONObject IssueData = UserArray.getJSONObject(i);
                        if (IssueData.getString("M_Type") == "null") {

                        } else {
                            String M_Type = IssueData.getString("M_Type");

                            Double Cnt = IssueData.getDouble("Cnt");//專案

                            //Double WorkHour = IssueData.getDouble("WorkHour");//人力
                            if (IssueData.isNull("WorkHour")) {
                                String WorkHour = "0.0";
                                Set_WorkHour_lastweek = 0.00;
                            } else {
                                Double WorkHour = IssueData.getDouble("WorkHour");
                                Set_WorkHour_lastweek = WorkHour;
                            }

                            //Double People = IssueData.getDouble("People");//人力
                            if (IssueData.isNull("People")) {
                                String People = "0.0";
                                Set_People_lastweek = 0.00;
                            } else {
                                Double People = IssueData.getDouble("People");
                                Set_People_lastweek = People;
                            }


                            float a = Cnt.floatValue();//圓餅圖用的%數值過濾"其他"
                            if (M_Type.indexOf("其他") == -1) {//過濾掉 "其他"
                                //entries.add(new PieEntry(a, M_Type));}
                                //entries.add(new PieEntry(a, M_Type + "  " + String.valueOf(a) + "  %"));
                                //長條圖使用
                                ArrayM_Type.add(j,M_Type);
                                ArrayPeople.add(String.format("%.2f", Set_People_lastweek));
                                //Project_cnt_manAdapter_Item_List.add(j, new Project_cnt_manAdapter_Item(M_Type, String.valueOf(Cnt), String.valueOf(WorkHour)));
                                //Project_cnt_manAdapter_Item_List.add(j, new Project_cnt_manAdapter_Item(M_Type, String.format("%.2f", WorkHour), String.format("%.2f", Cnt),String.format("%.2f", People)));
                                j++;
                            } else {
                                //Project_cnt_manAdapter_Item_List.add(i,new Project_cnt_manAdapter_Item("","",""));
                            }
                        }
                    }
                    Find_BU_PM_Rate(Week,Year,Region,Dept);

                } catch (JSONException ex) {

                }

            }
        });

    }

    //專案資源分配資訊
    public void Find_BU_PM_Rate(String Week, String Year, final String Region, String Dept) {

        RequestQueue mQueue = Volley.newRequestQueue(getActivity());

        Project_cnt_manAdapter_Item_List.clear();

        final ArrayList<BarEntry> yVals11 = new ArrayList<BarEntry>();
        final ArrayList<BarEntry> yVals12 = new ArrayList<BarEntry>();
        final ArrayList<String> mylist = new ArrayList<String>();

        getString("http://wtsc.msi.com.tw/IMS/Weekly_Report.asmx/Find_BU_PM_Rate?Week=" + Week + "&Year=" + Year + "&RegionID=" + Region+ "&Dept=" + Dept, mQueue, new GetServiceData.VolleyCallback() {
            @Override
            public void onSuccess(JSONObject result) {

                try {

                    JSONArray UserArray = new JSONArray(result.getString("Key"));

                    ArrayList<PieEntry> entries = new ArrayList<PieEntry>();

                    // NOTE: The order of the entries when being added to the entries array determines their position around the center of
                    // the chart.

                    int j = 0;

                    for (int i = 0; i < UserArray.length(); i++) {
                        JSONObject IssueData = UserArray.getJSONObject(i);
                        if (IssueData.getString("M_Type") == "null") {

                        } else {
                            String M_Type = IssueData.getString("M_Type");

                            Double Cnt = IssueData.getDouble("Cnt");//專案

                            //Double WorkHour = IssueData.getDouble("WorkHour");//人力
                            if (IssueData.isNull("WorkHour")) {
                                String WorkHour = "0.0";
                                Set_WorkHour = 0.00;
                            } else {
                                Double WorkHour = IssueData.getDouble("WorkHour");
                                Set_WorkHour = WorkHour;
                            }

                            //Double People = IssueData.getDouble("People");//人力
                            if (IssueData.isNull("People")) {
                                String People = "0.0";
                                Set_People = 0.00;
                            } else {
                                Double People = IssueData.getDouble("People");
                                Set_People = People;
                            }

                            float a = Cnt.floatValue();//圓餅圖用的%數值過濾"其他"
                            if (M_Type.indexOf("其他") == -1) {//過濾掉 "其他"
                                //entries.add(new PieEntry(a, M_Type));}
                                //entries.add(new PieEntry(a, M_Type + "  " + String.valueOf(a) + "  %"));
                                //長條圖使用
                                mylist.add(j, M_Type);//X軸標籤
                                yVals11.add(new BarEntry(j, Float.parseFloat(String.valueOf(Cnt))));//專案比重
                                yVals12.add(new BarEntry(j, Float.parseFloat(String.valueOf(Set_WorkHour))));//人力比重

                                //Project_cnt_manAdapter_Item_List.add(j, new Project_cnt_manAdapter_Item(M_Type, String.valueOf(Cnt), String.valueOf(WorkHour)));
                                Project_cnt_manAdapter_Item_List.add(j, new Project_cnt_manAdapter_Item(M_Type, String.format("%.2f", Set_WorkHour), String.format("%.2f", Cnt),String.format("%.2f", Set_People)));
                                j++;
                            } else {
                                //Project_cnt_manAdapter_Item_List.add(i,new Project_cnt_manAdapter_Item("","",""));
                            }
                            //下方List使用


                        }
                        //10月18新增方塊圖形
                        recyclerViewLayoutManager = new GridLayoutManager(mContext, 2);

                        recyclerView.setLayoutManager(recyclerViewLayoutManager);

                        recyclerView_Adapter = new RecyclerViewAdapter(mContext, Project_cnt_manAdapter_Item_List,Region);

                        recyclerView.setAdapter(recyclerView_Adapter);

                        recyclerView_Adapter.notifyDataSetChanged();



                        mListView = (ListView) getActivity().findViewById(R.id.listview_project_cnt_man);

                        mProject_cnt_manAdapter = new Project_cnt_manAdapter(mContext, Project_cnt_manAdapter_Item_List);

                        mListView.setAdapter(mProject_cnt_manAdapter);

                        //點擊BU to 彈跳視窗
                        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                                Intent intent = new Intent();

                                //給第二頁Week
                                intent.putExtra("Week", SetWeek);//代 週次到下一頁
                                Log.w("Week",String.valueOf(SetWeek));
                                //給第二頁Year
                                intent.putExtra("Year", SetYear);//代年到下一頁
                                Log.w("Year",String.valueOf(SetYear));
                                //給第二頁Week
                                intent.putExtra("RegionID", SetRegionID);//代 RegionID到下一頁
                                Log.w("RegionID",String.valueOf(SetRegionID));
                                //給第二頁Year
                                intent.putExtra("BU", Project_cnt_manAdapter_Item_List.get(position).GetM_Type());//代年到下一頁
                                Log.w("BU",String.valueOf(Project_cnt_manAdapter_Item_List.get(position).GetM_Type()));
                                intent.setClass(getActivity(), msibook_dqaweekly_project_activity_msr_data_msr_detial.class);
                                //開啟Activity
                                startActivity(intent);

                            }
                        });


                    }

//                    if(UserArray.length()>4){
//                        mBarChart.setLayoutParams();
//                    }

                    String[] stockArr = new String[mylist.size()];
                    stockArr = mylist.toArray(stockArr);
                    mBarChart.getDescription().setEnabled(true);

                    //****************長條圖************
                    // if more than 60 entries are displayed in the chart, no values will be
                    // drawn
                    //mChart.setMaxVisibleValueCount(100);

                    // scaling can now only be done on x- and y-axis separately
                    mBarChart.setPinchZoom(false);//y轴的值是否跟随图表变换缩放;如果禁止，y轴的值会跟随图表变换缩放
                    mBarChart.setTouchEnabled(false); // 设置是否可以触摸
                    mBarChart.setDrawBarShadow(false);
                    mBarChart.setDrawGridBackground(false);//setGroupSpace

                    int xlabelColor = getResources().getColor(R.color.chartxlabelColor);
                    XAxis xAxis = mBarChart.getXAxis();
                    xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                    xAxis.setDrawGridLines(false);//绘制网格线，默认为true
                    xAxis.setDrawAxisLine(false);// X軸 底線
                    xAxis.setDrawLabels(true);// X 軸 抵部 Title
                    xAxis.setTextColor(xlabelColor);

                    YAxis leftAxis = mBarChart.getAxisLeft();//获取左轴
                    YAxis rightAxis = mBarChart.getAxisRight();//获取右轴
                    leftAxis.setSpaceBottom(5);

                    leftAxis.setDrawZeroLine(true);//是否繪製0所在的網格線
                    leftAxis.setDrawGridLines(false);//绘制网格线 默认为true
                    rightAxis.setDrawGridLines(false);

                    int noneColor = getResources().getColor(R.color.chartnoneColorColor);
                    //mBarChart.getAxisLeft().setTextColor(noneColor);
                    mBarChart.getAxisRight().setTextColor(noneColor);

                    mBarChart.getAxisRight().setEnabled(true);//隐藏右轴  默认显示
                    mBarChart.getAxisLeft().setEnabled(true);//隐藏左轴  默认显示
                    mBarChart.getAxisLeft().setDrawLabels(true);// X 軸 抵部 Title


                    // setting data

                    // add a nice and smooth animation
                    mBarChart.animateY(100);

                    mBarChart.getLegend().setEnabled(false);

                    BarDataSet set1;
                    BarDataSet set2;

//                    if (mBarChart.getData() != null && mBarChart.getData().getDataSetCount() > 0) {
//                        set1 = (BarDataSet) mBarChart.getData().getDataSetByIndex(0);
//                        set1.setValues(yVals11);
//
//                        set2 = (BarDataSet) mBarChart.getData().getDataSetByIndex(1);
//                        set2.setValues(yVals12);
//
//                        mBarChart.getData().notifyDataChanged();
//
//
//                    } else {
//
//
//
//                    }

                    set1 = new BarDataSet(yVals11, "");
                    set2 = new BarDataSet(yVals12, "");

                    int BarColor1 = getResources().getColor(R.color.chartbarColor_project1);
                    int BarColor2 = getResources().getColor(R.color.chartbarColor_project2);
                    int valueColor1 = getResources().getColor(R.color.chartvalueColor);
                    int valueColor2 = getResources().getColor(R.color.chartvalueColor);
                    set1.setColors(BarColor1);
                    set1.setDrawValues(true);
                    set1.setValueTextColor(valueColor1);

                    //取小數點
                    set1.setValueFormatter(new msibook_dqaweekly_MyValueFormat() {
                        @Override
                        public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {

                            return String.valueOf(value);
                        }
                    });

                    set2.setColors(BarColor2);
                    set2.setDrawValues(true);
                    set2.setValueTextColor(valueColor2);
                    set2.setValueFormatter(new msibook_dqaweekly_MyValueFormat() {
                        @Override
                        public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {

                            return String.valueOf(value);
                        }
                    });

                    BarData data1 = new BarData(set1, set2);

                    mBarChart.setData(data1);

                    float barWidth;
                    float barSpace;
                    float groupSpace;
                    barWidth = 0.3f;
                    barSpace = 0.05f;
                    groupSpace = 0.3f;
                    mBarChart.getBarData().setBarWidth(barWidth);
                    mBarChart.getBarData().setValueTextSize(12);
                    mBarChart.getXAxis().setAxisMinimum(0);
                    mBarChart.getXAxis().setAxisMaximum(0 + mBarChart.getBarData().getGroupWidth(groupSpace, barSpace) * stockArr.length);
                    mBarChart.groupBars(0, groupSpace, barSpace);
                    mBarChart.getData().setHighlightEnabled(false);
                    mBarChart.setDrawValueAboveBar(true);

                    Legend l = mBarChart.getLegend();
                    l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
                    l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
                    l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
                    l.setDrawInside(true);
                    l.setYOffset(20f);
                    l.setXOffset(0f);
                    l.setYEntrySpace(0f);
                    l.setTextSize(8f);

                    mBarChart.getDescription().setEnabled(false);
                    //X-axis
                    XAxis xxAxis = mBarChart.getXAxis();
                    xxAxis.setGranularity(1f);
                    xxAxis.setGranularityEnabled(true);
                    xxAxis.setCenterAxisLabels(true);
                    xxAxis.setDrawGridLines(false);
                    xxAxis.setLabelCount(stockArr.length);
                    xxAxis.setAxisMaximum(stockArr.length);
                    xxAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                    xxAxis.setValueFormatter(new IndexAxisValueFormatter(stockArr));


                    YAxis lleftAxis = mBarChart.getAxisLeft();

                    lleftAxis.setLabelCount(5, false); //Y軸左側 數值個數

                    mBarChart.notifyDataSetChanged();
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

        if (context instanceof Activity) {
            a = (Activity) context;
        }
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
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


    public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{

        List<Project_cnt_manAdapter_Item> values;
        Context context1;
        String Region;

        public RecyclerViewAdapter(Context context2, List<Project_cnt_manAdapter_Item> values2,String region){

            values = values2;

            context1 = context2;

            Region = region;
        }

        public class ViewHolder extends RecyclerView.ViewHolder{

            public CardView cardview1;

            public TextView textView_M_Type;

            public TextView textView_M_People;

            public TextView textView_bu1;
            public TextView textView_bu2;
            public TextView textView_bu_by_year;

            public TextView textview_Cnt;

            public TextView textView_updown;

            public LinearLayout linear_item_main;

            public LinearLayout linear_up;
            public LinearLayout linear_down;
            public LinearLayout linear_down2;

            public ViewHolder(View v){

                super(v);

                cardview1 = (CardView) v.findViewById(R.id.cardview1);

                linear_item_main = (LinearLayout) v.findViewById(R.id.linear_item_main);

                textView_M_Type = (TextView) v.findViewById(R.id.textView_M_Type);

                textView_bu1= (TextView) v.findViewById(R.id.textView_bu1);
                textView_bu2= (TextView) v.findViewById(R.id.textView_bu2);
                textView_bu_by_year = (TextView) v.findViewById(R.id.textView_bu_by_year);

                textView_M_People = (TextView) v.findViewById(R.id.textView_M_People);

                textview_Cnt = (TextView) v.findViewById(R.id.textview_Cnt);

                textView_updown = (TextView) v.findViewById(R.id.textView_updown);

                linear_up = (LinearLayout) v.findViewById(R.id.linear_up);

                linear_down = (LinearLayout) v.findViewById(R.id.linear_down);
                linear_down2 = (LinearLayout) v.findViewById(R.id.linear_down2);
            }
        }

        @Override
        public RecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){

            View view1 = LayoutInflater.from(context1).inflate(R.layout.msibook_dqaweekly_project_recycler_item,parent,false);

            RecyclerViewAdapter.ViewHolder viewHolder1 = new RecyclerViewAdapter.ViewHolder(view1);

            return viewHolder1;
        }

        @Override
        public void onBindViewHolder(final RecyclerViewAdapter.ViewHolder Vholder, final int position){

            for (int i = 0; i < ArrayM_Type.size(); i++){
                if((values.get(position).GetM_Type().contains(ArrayM_Type.get(i)))){
                    if(Double.valueOf(values.get(position).GetPeople()) > Double.valueOf(ArrayPeople.get(i))){
                        Vholder.textView_updown.setBackgroundResource(R.mipmap.dqaweekly_ic_upgrade);
                    }else if(Double.valueOf(values.get(position).GetPeople()) < Double.valueOf(ArrayPeople.get(i))){
                        Vholder.textView_updown.setBackgroundResource(R.mipmap.dqaweekly_ic_lowering);
                    }else{
                        Vholder.textView_updown.setHeight(5);
                        Vholder.textView_updown.setBackgroundResource(R.mipmap.dqaweekly_ic_level);
                    }
                }else{
                }
            }

            if(values.get(position).GetM_Type().contains("CE")){
                Vholder.textView_M_Type.setText("ACS");
            }else {
                Vholder.textView_M_Type.setText(values.get(position).GetM_Type());
            }

            if(values.get(position).GetM_Type().contains("Other")){
                Vholder.cardview1.setBackgroundColor(Color.parseColor("#00c43e"));
                Vholder.linear_up.setBackgroundColor(Color.parseColor("#ffffd3"));
                Vholder.linear_down.setBackgroundColor(Color.parseColor("#ffffd3"));
                Vholder.linear_down2.setBackgroundColor(Color.parseColor("#ffffd3"));
            }

//            if(Region.contains("1")){
//                Log.w("選台北","選台北");
//                Vholder.textView_bu_by_year.setVisibility(View.VISIBLE);
//                Vholder.textView_bu1.setVisibility(View.VISIBLE);
//                Vholder.textView_bu2.setVisibility(View.VISIBLE);
//                switch(values.get(position).GetM_Type()) {
//                    case "ACS":
//                        Vholder.textView_bu_by_year.setText("0.5");
//                        break;
//                    case "CND":
//                        Vholder.textView_bu_by_year.setText("4.5");
//                        break;
//                    case "CPS":
//                        Vholder.textView_bu_by_year.setText("43");
//                        break;
//                    case "EPS":
//                        Vholder.textView_bu_by_year.setText("2.5");
//                        break;
//                    case "GNP":
//                        Vholder.textView_bu_by_year.setText("1");
//                        break;
//                    case "IPS":
//                        Vholder.textView_bu_by_year.setText("14");
//                        break;
//                    case "NB":
//                        Vholder.textView_bu_by_year.setText("10.5");
//                        break;
//                    case "Other":
//                        Vholder.textView_bu_by_year.setVisibility(View.GONE);
//                        Vholder.textView_bu1.setVisibility(View.GONE);
//                        Vholder.textView_bu2.setVisibility(View.GONE);
//                        break;
//                }
//
//            }else{
//                Log.w("選其它區","選其它區");
//                Vholder.textView_bu_by_year.setVisibility(View.GONE);
//                Vholder.textView_bu1.setVisibility(View.GONE);
//                Vholder.textView_bu2.setVisibility(View.GONE);
//            }

            Vholder.textView_M_People.setText(values.get(position).GetPeople());

            Vholder.textview_Cnt.setText(values.get(position).GetCnt());

            Vholder.linear_item_main.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(SetRegionID.contains("2") && values.get(position).GetM_Type().contains("Other")==true){
                        Intent intent = new Intent();

                        intent.putExtra("Week", SetWeek);//代 週次到下一頁

                        intent.putExtra("Year", SetYear);//代年到下一頁

                        intent.putExtra("RegionID", SetRegionID);//代 週次到下一頁

                        intent.putExtra("BU", values.get(position).GetM_Type());//代年到下一頁
                        Log.w("BUBUBUININININININ",values.get(position).GetM_Type());

                        intent.putExtra("People", values.get(position).GetPeople());//

                        intent.setClass(getActivity(), msibook_dqaweekly_project_activity_msr_data_msr_detial_for_K.class);
                        //開啟Activity
                        startActivity(intent);
                    }else if(SetRegionID.contains("3") && values.get(position).GetM_Type().contains("Other")==true){
                        Intent intent = new Intent();

                        intent.putExtra("Week", SetWeek);//代 週次到下一頁

                        intent.putExtra("Year", SetYear);//代年到下一頁

                        intent.putExtra("RegionID", SetRegionID);//代 週次到下一頁

                        intent.putExtra("BU", values.get(position).GetM_Type());//代年到下一頁
                        Log.w("BUBUBUININININININ",values.get(position).GetM_Type());

                        intent.putExtra("People", values.get(position).GetPeople());//

                        intent.setClass(getActivity(), msibook_dqaweekly_project_activity_msr_data_msr_detial_for_S.class);
                        //開啟Activity
                        startActivity(intent);

                    }else{
                        Intent intent = new Intent();

                        intent.putExtra("Week", SetWeek);//代 週次到下一頁

                        intent.putExtra("Year", SetYear);//代年到下一頁

                        intent.putExtra("RegionID", SetRegionID);//代 週次到下一頁

                        intent.putExtra("BU", values.get(position).GetM_Type());//代年到下一頁
                        Log.w("BUBUBUININININININ",values.get(position).GetM_Type());

                        intent.putExtra("People", values.get(position).GetPeople());//

                        intent.setClass(getActivity(), msibook_dqaweekly_project_activity_msr_data_msr_detial.class);
                        //開啟Activity
                        startActivity(intent);
                    }

                }
            });

        }

        @Override
        public int getItemCount(){

            return values.size();
        }
    }

    //-------------------------Item-------------------------
    public class Project_cnt_manAdapter_Item {

        String M_Type;

        String Cnt;

        String WorkHour;

        String People;

        public Project_cnt_manAdapter_Item(String M_Type,String Cnt,String WorkHour,String People)
        {
            this.M_Type = M_Type;

            this.Cnt = Cnt;

            this.WorkHour = WorkHour;

            this.People = People;

        }


        public String GetM_Type()
        {
            return this.M_Type;
        }

        public String GetCnt()
        {
            return this.Cnt;
        }

        public String GetWorkHour()
        {
            return this.WorkHour;
        }

        public String GetPeople()
        {
            return this.People;
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

            v = mLayInf.inflate(R.layout.msibook_dqaweekly_project_cnt_man_adapter, parent, false);

            TextView textView_M_Type = (TextView) v.findViewById(R.id.textView_M_Type);
            TextView textView_M_People = (TextView) v.findViewById(R.id.textView_M_People);
            TextView textView_WorkHour = (TextView) v.findViewById(R.id.textView_WorkHour);
            TextView textView_Cnt = (TextView) v.findViewById(R.id.textView_Cnt);

            textView_M_Type.setText(Project_cnt_manAdapter_Item_List.get(position).GetM_Type());

            //textView_M_People.setText(Project_cnt_manAdapter_Item_List.get(position).GetM_Type());

            textView_WorkHour.setText(Project_cnt_manAdapter_Item_List.get(position).GetWorkHour());

            textView_Cnt.setText(Project_cnt_manAdapter_Item_List.get(position).GetCnt());


            Log.w("DetailAdapter","test");

            return v;
        }

    }



}
