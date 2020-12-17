package dqa.com.msibook;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
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
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link msibook_dqaweekly_project_activity_jobs_load.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link msibook_dqaweekly_project_activity_jobs_load#newInstance} factory method to
 * create an instance of this fragment.
 */
public class msibook_dqaweekly_project_activity_jobs_load extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Context mContext;

    private View mView;
    private ProjectActivity_utilization_listview_adapter adapter;
    private ListView listview_dp_utilization;//各單位稼動率Listview
    List<ProjectActivity_utilization_listviewItem> utilization_list_Item = new ArrayList<ProjectActivity_utilization_listviewItem>();

    ArrayList<String> F_DeptIDArray = new ArrayList<String>();  //部門ID
    ArrayList<String> DeptCodeArray = new ArrayList<String>();  //部門代碼
    ArrayList<String> CostRateArray = new ArrayList<String>();       //稼動率

    //稼動率Base欄位 Linearlayout
    private LinearLayout linear_job_load_total;
    private LinearLayout linear_dpt7850;
    private LinearLayout linear_dpt7860;
    private LinearLayout linear_dpt7870;
    private LinearLayout linear_dpt7880;
    private TextView textView_value7850;
    private TextView textView_value7860;
    private TextView textView_value7870;
    private TextView textView_value7880;

    private String SetYear;
    private String SetWeek;

    private TextView textView_expectman;
    private TextView textView_totalman;

    private ProgressDialog progressBar;
    //稼動率圖表
    private PieChart mChart1;
    private Typeface tf;

    private Button btn_man_resuorce_info;

    private OnFragmentInteractionListener mListener;

    public msibook_dqaweekly_project_activity_jobs_load() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProjectActivity_jobs_load.
     */
    // TODO: Rename and change types and number of parameters
    public static msibook_dqaweekly_project_activity_jobs_load newInstance(String param1, String param2) {
        msibook_dqaweekly_project_activity_jobs_load fragment = new msibook_dqaweekly_project_activity_jobs_load();
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

            //讀取時間Bar
            progressBar = new ProgressDialog(getActivity());
            progressBar.setCancelable(true);
            progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressBar.setMessage("資料載入中");

        }
    }

    //抓年
    public void SetYear(String Year) {
        SetYear = Year;
    }

    //抓週
    public void SetWeek(String Week) {
        SetWeek = Week;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_msibook_dqaweekly_project_activity_jobs_load, container, false);
        mContext = getContext();

        listview_dp_utilization = (ListView) mView.findViewById(R.id.listview_dp_utilization);

        linear_job_load_total = (LinearLayout) mView.findViewById(R.id.linear_job_load_total);
        linear_dpt7850 = (LinearLayout) mView.findViewById(R.id.linear_dpt7850);
        linear_dpt7860 = (LinearLayout) mView.findViewById(R.id.linear_dpt7860);
        linear_dpt7870 = (LinearLayout) mView.findViewById(R.id.linear_dpt7870);
        linear_dpt7880 = (LinearLayout) mView.findViewById(R.id.linear_dpt7880);

        textView_value7850 = (TextView) mView.findViewById(R.id.textView_value7850);
        textView_value7860 = (TextView) mView.findViewById(R.id.textView_value7860);
        textView_value7870 = (TextView) mView.findViewById(R.id.textView_value7870);
        textView_value7880 = (TextView) mView.findViewById(R.id.textView_value7880);

        textView_expectman = (TextView) mView.findViewById(R.id.textView_expectman);
        textView_totalman = (TextView) mView.findViewById(R.id.textView_totalman);

        btn_man_resuorce_info = (Button) mView.findViewById(R.id.btn_man_resuorce_info);

        //稼動Info 點選事件
        btn_man_resuorce_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();

                intent.setClass(getActivity(), msibook_dqaweekly_project_activity_jobs_load_popinfo.class);
                //開啟Activity
                startActivity(intent);

            }
        });

        //稼動率圖表
        mChart1 = (PieChart) mView.findViewById(R.id.chart1);
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
        //mChart1.setOnChartValueSelectedListener(this);//設置在圖表值選擇的監聽器
        mChart1.animateY(1400, Easing.EasingOption.EaseInOutQuad);//動畫Y
        Legend l = mChart1.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setEnabled(false);


        //7850Click事件
        linear_dpt7850.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                int []location=new int[2];
                v.getLocationOnScreen(location);
                int x=location[0];//获取当前位置的横坐标
                int y=location[1];


                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        linear_dpt7850.setBackgroundColor(Color.parseColor("#e2e2e2"));//按下灰色
                        return true;
                    case MotionEvent.ACTION_UP:
                        linear_dpt7850.setBackgroundColor(Color.parseColor("#FFFFFF"));//放開白色
                        Intent intent = new Intent();

                        intent.putExtra("x_Location",String.valueOf(x));
                        Log.w("x_Location",String.valueOf(x));

                        intent.putExtra("y_Location",String.valueOf(y));
                        Log.w("y_Location",String.valueOf(y));

                        intent.putExtra("RowType", "請假");//代 "請假"到下一頁
                        //給第二頁Week
                        intent.putExtra("Week", SetWeek);//代 週次到下一頁
                        //給第二頁Year
                        intent.putExtra("Year", SetYear);//代年到下一頁
                        //給第二頁部門代號
                        intent.putExtra("ChoiceDepID", "21746");//代部門ID到下一頁 EX: 21751

                        intent.putExtra("DepID", "7850");//

                        //從MainActivity 到底下部門
                        intent.setClass(getActivity(), msibook_dqaweekly_project_activity_jobs_load_detial.class);//跳轉頁面至第二頁
                        //開啟Activity
                        startActivity(intent);
                        return true;
                }

                return false;
            }
        });

        //7860Click事件
        linear_dpt7860.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                int []location=new int[2];
                v.getLocationOnScreen(location);
                int x=location[0];//获取当前位置的横坐标
                int y=location[1];


                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        linear_dpt7860.setBackgroundColor(Color.parseColor("#e2e2e2"));//按下灰色
                        return true;
                    case MotionEvent.ACTION_UP:
                        linear_dpt7860.setBackgroundColor(Color.parseColor("#FFFFFF"));//放開白色
                        Intent intent = new Intent();

                        intent.putExtra("x_Location",String.valueOf(x));
                        Log.w("x_Location",String.valueOf(x));

                        intent.putExtra("y_Location",String.valueOf(y));
                        Log.w("y_Location",String.valueOf(y));

                        intent.putExtra("RowType", "請假");//代 "請假"到下一頁
                        //給第二頁Week
                        intent.putExtra("Week", SetWeek);//代 週次到下一頁
                        //給第二頁Year
                        intent.putExtra("Year", SetYear);//代年到下一頁
                        //給第二頁部門代號
                        intent.putExtra("ChoiceDepID", "21750");//代部門ID到下一頁 EX: 21751

                        intent.putExtra("DepID", "7860");//

                        //從MainActivity 到底下部門
                        intent.setClass(getActivity(), msibook_dqaweekly_project_activity_jobs_load_detial.class);//跳轉頁面至第二頁
                        //開啟Activity
                        startActivity(intent);
                        return true;
                }

                return false;
            }
        });

        //7870Click事件
        linear_dpt7870.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                int []location=new int[2];
                v.getLocationOnScreen(location);
                int x=location[0];//获取当前位置的横坐标
                int y=location[1];


                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        linear_dpt7870.setBackgroundColor(Color.parseColor("#e2e2e2"));//按下灰色
                        return true;
                    case MotionEvent.ACTION_UP:
                        linear_dpt7870.setBackgroundColor(Color.parseColor("#FFFFFF"));//放開白色
                        Intent intent = new Intent();

                        intent.putExtra("x_Location",String.valueOf(x));
                        Log.w("x_Location",String.valueOf(x));

                        intent.putExtra("y_Location",String.valueOf(y));
                        Log.w("y_Location",String.valueOf(y));

                        intent.putExtra("RowType", "請假");//代 "請假"到下一頁
                        //給第二頁Week
                        intent.putExtra("Week", SetWeek);//代 週次到下一頁
                        //給第二頁Year
                        intent.putExtra("Year", SetYear);//代年到下一頁
                        //給第二頁部門代號
                        intent.putExtra("ChoiceDepID", "21755");//代部門ID到下一頁 EX: 21751

                        intent.putExtra("DepID", "7870");//

                        //從MainActivity 到底下部門
                        intent.setClass(getActivity(), msibook_dqaweekly_project_activity_jobs_load_detial.class);//跳轉頁面至第二頁
                        //開啟Activity
                        startActivity(intent);
                        return true;
                }

                return false;
            }
        });

        //7880Click事件
        linear_dpt7880.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                int []location=new int[2];
                v.getLocationOnScreen(location);
                int x=location[0];//获取当前位置的横坐标
                int y=location[1];


                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        linear_dpt7880.setBackgroundColor(Color.parseColor("#e2e2e2"));//按下灰色
                        return true;
                    case MotionEvent.ACTION_UP:
                        linear_dpt7880.setBackgroundColor(Color.parseColor("#FFFFFF"));//放開白色
                        Intent intent = new Intent();

                        intent.putExtra("x_Location",String.valueOf(x));
                        Log.w("x_Location",String.valueOf(x));

                        intent.putExtra("y_Location",String.valueOf(y));
                        Log.w("y_Location",String.valueOf(y));

                        intent.putExtra("RowType", "請假");//代 "請假"到下一頁
                        //給第二頁Week
                        intent.putExtra("Week", SetWeek);//代 週次到下一頁
                        //給第二頁Year
                        intent.putExtra("Year", SetYear);//代年到下一頁
                        //給第二頁部門代號
                        intent.putExtra("ChoiceDepID", "21795");//代部門ID到下一頁 EX: 21751

                        intent.putExtra("DepID", "7880");//

                        //從MainActivity 到底下部門
                        intent.setClass(getActivity(), msibook_dqaweekly_project_activity_jobs_load_detial.class);//跳轉頁面至第二頁
                        //開啟Activity
                        startActivity(intent);
                        return true;
                }

                return false;
            }
        });

        return mView;
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

    public void Find_Week_Utilization_Job_Load(String Week,String Year, String DeptID) {

        utilization_list_Item.clear();

        final Float[] CheckChart_Job_Load = {null};

        F_DeptIDArray.clear();//部門ID
        DeptCodeArray.clear();//部門代碼
        CostRateArray.clear();//稼動率



        RequestQueue mQueue = Volley.newRequestQueue(getActivity());

        getString("http://wtsc.msi.com.tw/IMS/Weekly_Report.asmx/Find_Weekly_utilization?Week=" + Week + "&Year=" + Year + "&DeptID=" + DeptID, mQueue, new GetServiceData.VolleyCallback() {
            @Override
            public void onSuccess(JSONObject result) {

                try {

                    JSONArray UserArray = new JSONArray(result.getString("Key"));
                    if (UserArray.length() > 0)
                    {
                        linear_job_load_total.setVisibility(View.VISIBLE);

                        Double CostTotalRate = 0.0;

                        for (int i = 0; i < UserArray.length(); i++) {

                            JSONObject IssueData = UserArray.getJSONObject(i);

                            String F_DeptID = String.valueOf(IssueData.getString("F_DeptID"));

                            String DeptCode = String.valueOf(IssueData.getString("DeptCode"));

                            String Rate = String.valueOf(IssueData.getDouble("Rate"));

                            String CostRate = String.valueOf(IssueData.getDouble("CostRate"));

                            CostTotalRate += IssueData.getDouble("CostRate");

                            F_DeptIDArray.add(F_DeptID);
                            DeptCodeArray.add(DeptCode);
                            CostRateArray.add(CostRate);
                        }

                        String AVGJob_Load = String.valueOf(CostTotalRate/UserArray.length());

                        if ((Double.valueOf(CostRateArray.get(0)) / 100) > 1) {
                            textView_value7850.setText(String.format("%.2f",Double.valueOf(CostRateArray.get(0))));
                            textView_value7850.setTextSize(21);
                            textView_value7850.setTextColor(Color.parseColor("#d21e25"));
                        } else if ((Double.valueOf(CostRateArray.get(0)) / 100) < 0.9) {
                            textView_value7850.setText(String.format("%.2f",Double.valueOf(CostRateArray.get(0))));
                            textView_value7850.setTextSize(21);
                            textView_value7850.setTextColor(Color.parseColor("#358900"));
                        } else if ((Double.valueOf(CostRateArray.get(0)) / 100) <= 1 && ((Double.valueOf(CostRateArray.get(0))  / 100) >= 0.9)) {
                            textView_value7850.setText(String.format("%.2f",Double.valueOf(CostRateArray.get(0))));
                            textView_value7850.setTextSize(21);
                            textView_value7850.setTextColor(Color.parseColor("#212121"));
                        }

                        if ((Double.valueOf(CostRateArray.get(1)) / 100) > 1) {
                            textView_value7860.setText(String.format("%.2f",Double.valueOf(CostRateArray.get(1))));
                            textView_value7860.setTextSize(21);
                            textView_value7860.setTextColor(Color.parseColor("#d21e25"));
                        } else if ((Double.valueOf(CostRateArray.get(1)) / 100) < 0.9) {
                            textView_value7860.setText(String.format("%.2f",Double.valueOf(CostRateArray.get(1))));
                            textView_value7860.setTextSize(21);
                            textView_value7860.setTextColor(Color.parseColor("#358900"));
                        } else if ((Double.valueOf(CostRateArray.get(1)) / 100) <= 1 && ((Double.valueOf(CostRateArray.get(1))  / 100) >= 0.9)) {
                            textView_value7860.setText(String.format("%.2f",Double.valueOf(CostRateArray.get(1))));
                            textView_value7860.setTextSize(21);
                            textView_value7860.setTextColor(Color.parseColor("#212121"));
                        }

                        if ((Double.valueOf(CostRateArray.get(2)) / 100) > 1) {
                            textView_value7870.setText(String.format("%.2f",Double.valueOf(CostRateArray.get(2))));
                            textView_value7870.setTextSize(21);
                            textView_value7870.setTextColor(Color.parseColor("#d21e25"));
                        } else if ((Double.valueOf(CostRateArray.get(2)) / 100) < 0.9) {
                            textView_value7870.setText(String.format("%.2f",Double.valueOf(CostRateArray.get(2))));
                            textView_value7870.setTextSize(21);
                            textView_value7870.setTextColor(Color.parseColor("#358900"));
                        } else if ((Double.valueOf(CostRateArray.get(2)) / 100) <= 1 && ((Double.valueOf(CostRateArray.get(2))  / 100) >= 0.9)) {
                            textView_value7870.setText(String.format("%.2f",Double.valueOf(CostRateArray.get(2))));
                            textView_value7870.setTextSize(21);
                            textView_value7870.setTextColor(Color.parseColor("#212121"));
                        }

                        if ((Double.valueOf(CostRateArray.get(3)) / 100) > 1) {
                            textView_value7880.setText(String.format("%.2f",Double.valueOf(CostRateArray.get(3))));
                            textView_value7880.setTextSize(21);
                            textView_value7880.setTextColor(Color.parseColor("#d21e25"));
                        } else if ((Double.valueOf(CostRateArray.get(3)) / 100) < 0.9) {
                            textView_value7880.setText(String.format("%.2f",Double.valueOf(CostRateArray.get(3))));
                            textView_value7880.setTextSize(21);
                            textView_value7880.setTextColor(Color.parseColor("#358900"));
                        } else if ((Double.valueOf(CostRateArray.get(3)) / 100) <= 1 && ((Double.valueOf(CostRateArray.get(3))  / 100) >= 0.9)) {
                            textView_value7880.setText(String.format("%.2f",Double.valueOf(CostRateArray.get(3))));
                            textView_value7880.setTextSize(21);
                            textView_value7880.setTextColor(Color.parseColor("#212121"));
                        }

                        //String AVGJob_Load= String.valueOf((Double.valueOf(CostRateArray.get(0))+Double.valueOf(CostRateArray.get(1))+Double.valueOf(CostRateArray.get(2))+Double.valueOf(CostRateArray.get(3)))/4);

                        CheckChart_Job_Load[0] = Float.valueOf(AVGJob_Load);

                        ArrayList<PieEntry> entries = new ArrayList<PieEntry>();

                        if(CheckChart_Job_Load[0]!=null) {
                            if ((CheckChart_Job_Load[0] / 100) > 1) {
                                mChart1.setCenterText(generateCenterSpannableText(String.format("%.2f", CheckChart_Job_Load[0]) + "%" + "\n達成率", 210, 30, 37));
                                mChart1.setCenterTextSize(35);
                            } else if ((CheckChart_Job_Load[0] / 100) < 0.9) {
                                mChart1.setCenterText(generateCenterSpannableText(String.format("%.2f", CheckChart_Job_Load[0]) + "%" + "\n達成率", 53, 137, 0));
                                mChart1.setCenterTextSize(35);
                            } else if ((CheckChart_Job_Load[0] / 100) <= 1 && ((CheckChart_Job_Load[0] / 100) >= 0.9)) {
                                mChart1.setCenterText(generateCenterSpannableText(String.format("%.2f", CheckChart_Job_Load[0]) + "%" + "\n達成率", 101, 101, 101));
                                mChart1.setCenterTextSize(35);
                            }

                            //稼動率圖表寫入資料
                            float a = (float) (CheckChart_Job_Load[0] / 100);

                            if (a > 1) {
                                entries.add(new PieEntry(a, ""));
                            } else {
                                entries.add(new PieEntry(a, ""));

                                entries.add(new PieEntry(1 - a, ""));
                            }

                        }else{
                            //mChart1.setCenterText(generateCenterSpannableText("資料庫未連結", 101, 101, 101));
                            mChart1.setCenterText("資料庫未連結");
                            mChart1.setCenterTextColor(Color.rgb(33, 33, 33));
                            mChart1.setCenterTextSize(20);
                        }

                        PieDataSet dataSet = new PieDataSet(entries, "Election Results");

                        dataSet.setSliceSpace(3f);
                        dataSet.setSelectionShift(5f);

                        // add a lot of colors
                        ArrayList<Integer> colors = new ArrayList<Integer>();
                        colors.add(Color.parseColor("#d21e25"));// 百分比 藍色 527ca2
                        colors.add(Color.parseColor("#e6e7e8"));//百分比 灰色 e6e7e8
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
                    }
                    else
                    {
                        linear_job_load_total.setVisibility(View.GONE);
                        listview_dp_utilization.setEmptyView(mView.findViewById(R.id.empty));
                    }

                } catch (JSONException ex) {

                }
                adapter = new ProjectActivity_utilization_listview_adapter(getContext(), utilization_list_Item);

                listview_dp_utilization.setAdapter(adapter);
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

    //稼動率 generateCenterSpannableText
    private SpannableString generateCenterSpannableText(String Title, int a, int b, int c) {

        SpannableString s = new SpannableString(Title);
        s.setSpan(new ForegroundColorSpan(Color.rgb(a,b,c)), 0, s.length()-5, 0);
        s.setSpan(new ForegroundColorSpan(Color.rgb(33, 33, 33)), s.length()-5, s.length(), 0);
        s.setSpan(new RelativeSizeSpan(0.5f), s.length()-5, s.length(), s.SPAN_EXCLUSIVE_EXCLUSIVE);

        return s;
    }

    //-------------------Item-----------------------
    public class ProjectActivity_utilization_listviewItem {

        private String Dept1;
        private String utilization1;
        private String Dept2;
        private String utilization2;
        private String Dept3;
        private String utilization3;
        private String Dept4;
        private String utilization4;

        public ProjectActivity_utilization_listviewItem(String Dept1, String utilization1, String Dept2, String utilization2, String Dept3, String utilization3, String Dept4, String utilization4) {

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


    }


    //-----------------Adapter---------------------
    public class ProjectActivity_utilization_listview_adapter extends BaseAdapter {

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
        private List<ProjectActivity_utilization_listviewItem> utilization_list;

        public ProjectActivity_utilization_listview_adapter(Context context, List<ProjectActivity_utilization_listviewItem> movie) {
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

            ProjectActivity_utilization_listview_adapter.ViewHolder holder = null;
            if (convertView == null) {
                convertView = myInflater.inflate(R.layout.msibook_dqaweekly_projectactivity_page2_listview_layout, null);
                holder = new ProjectActivity_utilization_listview_adapter.ViewHolder(
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
                holder = (ProjectActivity_utilization_listview_adapter.ViewHolder) convertView.getTag();
            }
            ProjectActivity_utilization_listviewItem movie = (ProjectActivity_utilization_listviewItem) getItem(position);

//        int color_title[] = {Color.WHITE, Color.WHITE, Color.YELLOW};//時間的顏色
//
//        int color_time[] = {Color.WHITE, Color.WHITE, Color.YELLOW};//背景的顏色
//
//        int color_back[] = {Color.BLACK, Color.BLUE, Color.BLACK};//時間是否顯示
//
//        int time_vis[] = {View.VISIBLE, View.GONE, View.VISIBLE};


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
                color = "#d21e25";//"#f0625d";
            } else if ((afterConvert / 100) == 0.0) {
                color ="#212121";//"#c6c6c6";
            } else if ((afterConvert / 100) < 0.9) {
                color ="#358900";//"#46aa36";
            } else if ((afterConvert / 100) <= 1 && (afterConvert / 100 >= 0.9)) {
                color = "#212121";//"#656565";
            }

            return  color;
        }
    }



}

