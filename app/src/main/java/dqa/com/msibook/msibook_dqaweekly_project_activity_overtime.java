package dqa.com.msibook;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class msibook_dqaweekly_project_activity_overtime extends Activity {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private LineChart mChart;

    private Context mContext;
    private View mView;

    private TextView textView_TotalMan;
    private TextView textView_TotalHour;
    private TextView textView_AverageMan;

    private TextView textView_main;
    private LinearLayout overtime_main;

    private ProgressDialog progressBar;

    private TextView textView_7850_dp;
    private TextView textView_7850_man;
    private TextView textView_7850_overtime;
    private TextView textView_7850_overtime_avg;

    private TextView textView_7860_dp;
    private TextView textView_7860_man;
    private TextView textView_7860_overtime;
    private TextView textView_7860_overtime_avg;

    private TextView textView_7870_dp;
    private TextView textView_7870_man;
    private TextView textView_7870_overtime;
    private TextView textView_7870_overtime_avg;

    private TextView textView_7880_dp;
    private TextView textView_7880_man;
    private TextView textView_7880_overtime;
    private TextView textView_7880_overtime_avg;

    private String Week;
    private String Year;
    private msibook_dqaweekly_project_activity_overtime.OnFragmentInteractionListener mListener;

    private Integer Save7850_Total_man;
    private Double Save7850_Total_overtime;

    private Integer Save7860_Total_man;
    private Double Save7860_Total_overtime;

    private Integer Save7870_Total_man;
    private Double Save7870_Total_overtime;

    private Integer Save7880_Total_man;
    private Double Save7880_Total_overtime;

    public class LabelFormatter implements IAxisValueFormatter {
        private final String[] mLabels;

        public LabelFormatter(String[] labels) {
            mLabels = labels;
        }

        @Override
        public String getFormattedValue(float value, AxisBase axis) {

            if (mLabels.length -1 >= value)
            {
                return mLabels[(int) value];
            }
            else
            {
                return mLabels[mLabels.length-1];
            }


        }
    }

//    /**
//     * Use this factory method to create a new instance of
//     * this fragment using the provided parameters.
//     *
//     * @param param1 Parameter 1.
//     * @param param2 Parameter 2.
//     * @return A new instance of fragment ProjectActivity_overtime.
//     */
//    // TODO: Rename and change types and number of parameters
//    public static msibook_dqaweekly_project_activity_overtime newInstance(String param1, String param2) {
//        msibook_dqaweekly_project_activity_overtime fragment = new msibook_dqaweekly_project_activity_overtime();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
//        return fragment;
//    }



    //抓年
    public void SetTaipeiON() {
        textView_main.setVisibility(View.GONE);
        overtime_main.setVisibility(View.VISIBLE);
    }

    //抓週
    public void SetTaipeiOFF() {
        textView_main.setVisibility(View.VISIBLE);
        overtime_main.setVisibility(View.GONE);
    }



    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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

    public void Find_Total_OverTime(String Week,String Year) {

        //顯示 讀取等待時間Bar

        RequestQueue mQueue = Volley.newRequestQueue(this);

        String Path = "http://wtsc.msi.com.tw/IMS/Weekly_Report.asmx/Find_Total_OverTime?Week=" + Week + "&Year=" + Year;

        getString(Path, mQueue, new GetServiceData.VolleyCallback() {
            @Override
            public void onSuccess(JSONObject result) {

                try {

                    JSONArray UserArray = new JSONArray(result.getString("Key"));

                    ArrayList<String> yVals = new ArrayList<String>();
                    ArrayList<String> xVals = new ArrayList<String>();

                    if (UserArray.length() > 0) {
                        for (int i = 0; i < UserArray.length(); i++) {

                            JSONObject IssueData = UserArray.getJSONObject(i);

                            String OverTimeHour = String.valueOf(IssueData.getString("OverTimeHour")); //加班

                            String TotalMan = String.valueOf(IssueData.getString("TotalMan")); //總人數

                            String F_DeptCode = String.valueOf(IssueData.getString("F_DeptCode")); //總人數

                            Log.w("F_DeptCode", String.valueOf(F_DeptCode));

                            yVals.add(i, OverTimeHour);

                            xVals.add(i, F_DeptCode);

                            if(F_DeptCode.contains("785")){
                                Save7850_Total_man += Integer.valueOf(TotalMan);
                                Save7850_Total_overtime += Double.valueOf(OverTimeHour);
                            }else if(F_DeptCode.contains("786")) {
                                Save7860_Total_man += Integer.valueOf(TotalMan);
                                Save7860_Total_overtime += Double.valueOf(OverTimeHour);
                            }else if(F_DeptCode.contains("787")){
                                Save7870_Total_man += Integer.valueOf(TotalMan);
                                Save7870_Total_overtime += Double.valueOf(OverTimeHour);
                            }else if(F_DeptCode.contains("788")){
                                Save7880_Total_man += Integer.valueOf(TotalMan);
                                Save7880_Total_overtime += Double.valueOf(OverTimeHour);
                            }
//                            switch (F_DeptCode) {
//                                case "785":
//                                    Save7850_Total_man += Integer.valueOf(TotalMan);
//                                    Save7850_Total_overtime += Double.valueOf(OverTimeHour);
//                                    break;
//                                case "786":
//                                    Save7860_Total_man += Integer.valueOf(TotalMan);
//                                    Save7860_Total_overtime += Double.valueOf(OverTimeHour);
//                                    break;
//                                case "787":
//                                    Save7870_Total_man += Integer.valueOf(TotalMan);
//                                    Save7870_Total_overtime += Double.valueOf(OverTimeHour);
//                                    break;
//                                case "788":
//                                    Save7880_Total_man += Integer.valueOf(TotalMan);
//                                    Save7880_Total_overtime += Double.valueOf(OverTimeHour);
//                                    break;
//                            }


                        }
                        textView_7850_man.setText(String.valueOf(Save7850_Total_man));
                        textView_7850_overtime.setText(String.valueOf("N/A"));
                        textView_7850_overtime_avg.setText("N/A");

                        textView_7860_man.setText(String.valueOf(Save7860_Total_man));
                        textView_7860_overtime.setText(String.valueOf(Save7860_Total_overtime));
                        textView_7860_overtime_avg.setText(String.format("%.2f",Save7860_Total_overtime/Double.valueOf(Save7860_Total_man)));
                        Log.w("7860人數",String.valueOf(Save7860_Total_man));
                        Log.w("7860加班",String.valueOf(Save7860_Total_overtime));

                        textView_7870_man.setText(String.valueOf(Save7870_Total_man));
                        textView_7870_overtime.setText(String.valueOf(Save7870_Total_overtime));
                        textView_7870_overtime_avg.setText(String.format("%.2f",Save7870_Total_overtime/Double.valueOf(Save7870_Total_man)));
                        Log.w("7870人數",String.valueOf(Save7870_Total_man));
                        Log.w("7870加班",String.valueOf(Save7870_Total_overtime));

                        textView_7880_man.setText(String.valueOf(Save7880_Total_man));
                        textView_7880_overtime.setText("N/A");
                        textView_7880_overtime_avg.setText("N/A");
                        Log.w("7880人數",String.valueOf(Save7880_Total_man));
                        Log.w("7880加班",String.valueOf(Save7880_Total_overtime));

                        String[] stockArr = new String[xVals.size()];

                        Log.w(String.valueOf(xVals.size()), "Test2");
                        stockArr = xVals.toArray(stockArr);

                        List<Entry> entries = new ArrayList<>();
                        {
                            for (int i = 0; i < xVals.size(); i++) {
                                Entry e = new Entry(i, Float.parseFloat(yVals.get(i)));
                                entries.add(e);
                                Log.w(String.valueOf(yVals.get(i)), "Test3");
                            }
                        }
                        LineDataSet dataSet = new LineDataSet(entries, "");

                        dataSet.setColor(Color.parseColor("#abacb1"));//折線的顏色
                        //dataSet.setLineWidth(3.0f);

                        dataSet.setLineWidth(1); //線條寬度
                        dataSet.setCircleRadius(0); //圈的 半徑
                        dataSet.setCircleColor(Color.WHITE); //半徑顏色

                        dataSet.setDrawFilled(false);//線下方填滿
                        dataSet.setFillColor(Color.parseColor("#FFFD464E"));

                        dataSet.setDrawCircles(true);//圈圈外圍大小跟顏色
                        dataSet.setCircleColor(Color.parseColor("#d21e25")); //#527da3
                        dataSet.setDrawHorizontalHighlightIndicator(false);
                        dataSet.setDrawHighlightIndicators(false);

                        dataSet.setDrawCircleHole(true);//圈圈裡面洞大小跟顏色
                        dataSet.setCircleColorHole(Color.parseColor("#ffffff"));
                        dataSet.setCircleHoleRadius(0.0f);
                        dataSet.setDrawValues(true);//要不要在點上面顯示數值
                        dataSet.setValueTextColor(Color.parseColor("#495566"));//數值顏色
                        dataSet.setValueTextSize(14);
                        //注意的是這裡的Entry是MPChart lib裡的class， Entry(float x, float y)，所以要記得把X和Y轉型成 Float！ 最後就把數值實作在MPChart裡，就大功告成
                        LineData lineData = new LineData(dataSet);

                        XAxis xAxis = mChart.getXAxis();//获取x轴
                        // xAxis.setDrawAxisLine(true);//绘制X轴，默认为true
                        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);//设置x轴位置，有四个属性。可以说是上下两个位置，只不过轴标签分为轴线内外，感兴趣的可以自己试试
                        // 禁用x轴，设置为false后该轴任何部分都不会绘制,所以即使再设置xAxis.setDrawAxisLine(true);也不会被绘制
                        //xAxis.setEnabled(false);
                        xAxis.setDrawGridLines(false);//绘制网格线，默认为true
                        xAxis.setDrawAxisLine(false);// X軸 底線
                        xAxis.setTextColor(Color.parseColor("#212121"));
                        //xAxis.enableGridDashedLine(10f, 10f, 10f);//網格線長 寬 空格距離

                        YAxis leftAxis = mChart.getAxisLeft();//获取左轴
                        // YAxis rightAxis = chart.getAxisRight();//获取右轴
                        leftAxis.setSpaceBottom(10);
                        mChart.getAxisRight().setEnabled(false);//隐藏右轴  默认显示
                        mChart.getAxisLeft().setEnabled(false);//隐藏左轴  默认显示

                        //设置Y轴最大最小值，不设置chart会自己计算
                        //leftAxis.setAxisMinimum(0f);//设置最小值
                        //leftAxis.setAxisMaximum(70f);//设置最大值
                        leftAxis.setDrawGridLines(false);//绘制网格线 默认为true

                        //xAxis.setGridColor(Color.BLUE);//设置该轴的网格线颜色。
                        //xAxis.setGridLineWidth(5f);// 设置该轴网格线的宽度。
                        mChart.getLegend().setEnabled(false);// 座標底下 的 小方框圖案 影藏
                        mChart.setData(lineData);
                        mChart.getXAxis().setLabelCount(stockArr.length - 1); //設定X軸 長度
                        mChart.getXAxis().setValueFormatter(new LabelFormatter(stockArr));//帶入陣列的值給 X 軸
                        mChart.getXAxis().setTextSize(Float.parseFloat("12")); //字型大小
                        mChart.getXAxis().setTextColor(Color.parseColor("#212121"));
                        Log.w(String.valueOf(stockArr.length), "Test");

                        mChart.notifyDataSetChanged(); // let the chart know it's data changed
                        mChart.invalidate(); // refresh

                    }else{}

                } catch (JSONException ex) {

                }
            }
        });
    }

    //抓費用
    public void Find_Total_OverTime_Total(String Week,String Year) {

        //顯示  讀取等待時間Bar
        progressBar.show();

        RequestQueue mQueue = Volley.newRequestQueue(this);

        String Path = "http://wtsc.msi.com.tw/IMS/Weekly_Report.asmx/Find_Total_OverTime_Total?Week=" + Week + "&Year=" + Year;

        getString(Path, mQueue, new GetServiceData.VolleyCallback() {
            @Override
            public void onSuccess(JSONObject result) {

                try {
                    JSONArray UserArray = new JSONArray(result.getString("Key"));

                    if (UserArray.length() > 0) {
                        for (int i = 0; i < UserArray.length(); i++) {

                            JSONObject IssueData = UserArray.getJSONObject(i);

                            String TotalHour = String.valueOf(IssueData.getDouble("TotalHour"));

                            textView_TotalHour.setText(TotalHour);

                            String TotalMan = String.valueOf(IssueData.getDouble("TotalMan"));

                            textView_TotalMan.setText(TotalMan);

                            Double AverageMan = Double.valueOf(IssueData.getDouble("AverageMan"));

                            textView_AverageMan.setText(String.format("%.2f", AverageMan));


                        }

                        //main_summary_number.setText("1");

                    }else{
                        //main_summary_number.setText("0");
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msibook_dqaweekly_project_overtime);

        progressBar = new ProgressDialog(this);
        progressBar.setCancelable(true);
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.setMessage("資料載入中");

        mContext = this;

        textView_TotalMan = (TextView) findViewById(R.id.textView_TotalMan);
        textView_TotalHour = (TextView) findViewById(R.id.textView_TotalHour);
        textView_AverageMan = (TextView) findViewById(R.id.textView_AverageMan);

        textView_main = (TextView) findViewById(R.id.textView_main);//主體 隱藏開關用
        overtime_main = (LinearLayout) findViewById(R.id.overtime_main);//主體隱藏開關用

        textView_7850_dp = (TextView) findViewById(R.id.textView_7850_dp);
        textView_7850_man = (TextView) findViewById(R.id.textView_7850_man);
        textView_7850_overtime = (TextView) findViewById(R.id.textView_7850_overtime);
        textView_7850_overtime_avg = (TextView) findViewById(R.id.textView_7850_overtime_avg);

        textView_7860_dp = (TextView) findViewById(R.id.textView_7860_dp);
        textView_7860_man = (TextView) findViewById(R.id.textView_7860_man);
        textView_7860_overtime = (TextView) findViewById(R.id.textView_7860_overtime);
        textView_7860_overtime_avg = (TextView) findViewById(R.id.textView_7860_overtime_avg);

        textView_7870_dp = (TextView) findViewById(R.id.textView_7870_dp);
        textView_7870_man = (TextView) findViewById(R.id.textView_7870_man);
        textView_7870_overtime = (TextView) findViewById(R.id.textView_7870_overtime);
        textView_7870_overtime_avg = (TextView) findViewById(R.id.textView_7870_overtime_avg);

        textView_7880_dp = (TextView) findViewById(R.id.textView_7880_dp);
        textView_7880_man = (TextView) findViewById(R.id.textView_7880_man);
        textView_7880_overtime = (TextView) findViewById(R.id.textView_7880_overtime);
        textView_7880_overtime_avg = (TextView) findViewById(R.id.textView_7880_overtime_avg);

        Save7850_Total_man = 0;
        Save7850_Total_overtime = 0.0;

        Save7860_Total_man = 0;
        Save7860_Total_overtime = 0.0;

        Save7870_Total_man = 0;
        Save7870_Total_overtime = 0.0;

        Save7880_Total_man = 0;
        Save7880_Total_overtime = 0.0;

        mChart = (LineChart) findViewById(R.id.chart1);

        //設置圖表style：
        mChart.getDescription().setEnabled(false);
//        mChart.setDescriptionTextSize(30);
//        mChart.setUnit("%"); //設置Y轴上的单位
//mChart.setAlpha(0.8f);//設置透明度
//        mChart.setBorderColor(Color.rgb(213, 216, 214));//設置網格底下的那條線的颜色
//        mChart.setBackgroundColor(Color.rgb(255, 255, 255));//設置背景顏色

        設置圖表功能:
//        mChart.setHighlightEnabled(true);//設置焦點顯示
        mChart.setTouchEnabled(true);//設置是否可以觸摸，如為false，則不能拖動，縮放等
        mChart.setDragEnabled(true);//設置是否可以拖拽，缩放
        mChart.setScaleEnabled(true);//設置是否在圖點上顯示值
        mChart.setPinchZoom(true); //設置是否能變大變小

        mChart.setDragEnabled(false);//啟用/禁用平移圖表，默認為true
        mChart.setDoubleTapToZoomEnabled(false);//雙擊縮放，默認true
        mChart.setTouchEnabled(false);//開啟/禁用所有可能和chart的觸摸交互,默認為true
        mChart.setScaleEnabled(false);//啟用/禁用縮放圖表上的兩個軸,默認為true
        mChart.setDrawGridBackground(false);//開啟chart繪圖區後面的背景矩形將進行繪製
        mChart.setDrawBorders(false);//啟用/禁用繪製圖表邊框

        //設置圖表值： 我想這個是大家最想知道的， 首先我利用ArrayList去存我的XY(橫、縱軸)值
        Bundle bundle0311 =this.getIntent().getExtras();

        Week = bundle0311.getString("Week");
        Year = bundle0311.getString("Year");

        Find_Total_OverTime(Week,Year);

        Find_Total_OverTime_Total(Week,Year);

    }


}
