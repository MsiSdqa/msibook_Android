package dqa.com.msibook;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
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
import java.util.Calendar;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link msibook_dqaweekly_project_msr_project.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link msibook_dqaweekly_project_msr_project#newInstance} factory method to
 * create an instance of this fragment.
 */
public class msibook_dqaweekly_project_msr_project extends Fragment {
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

    private List<Project_cnt_manAdapter_Item> Project_cnt_manAdapter_Item_List = new ArrayList<Project_cnt_manAdapter_Item>();

    private ListView mListView;

    private LinearLayout linearlayout_top;
    private LinearLayout linearlayout_down;

    private String SetYear;
    private String CeckSetYear;
    private String SetWeek;
    private String SetMonth;
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

    private TextView textView_month;

    private LinearLayout linear_main;
    private TextView empty2;
    private TextView empty3;

    public msibook_dqaweekly_project_msr_project() {
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
    public static msibook_dqaweekly_project_msr_project newInstance(String param1, String param2) {
        msibook_dqaweekly_project_msr_project fragment = new msibook_dqaweekly_project_msr_project();
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

        View v = inflater.inflate(R.layout.fragment_msibook_dqaweekly_project_msr_project, container, false);

        //*****************長條圖*******************************
        mBarChart = (BarChart) v.findViewById(R.id.chart1);

        //******************************************************

        LinearLayout linearlayout_top = (LinearLayout) v.findViewById(R.id.linearlayout_top);

        LinearLayout linearlayout_down = (LinearLayout) v.findViewById(R.id.linearlayout_down);

        linear_main = (LinearLayout) v.findViewById(R.id.linear_main);

        btn_man_resuorce_info = (Button) v.findViewById(R.id.btn_man_resuorce_info);

        textView_month = (TextView) v.findViewById(R.id.textView_month);

        empty2 = (TextView) v.findViewById(R.id.empty2);
        empty2.setVisibility(View.GONE);
        empty3 = (TextView) v.findViewById(R.id.empty3);
        empty3.setVisibility(View.GONE);

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
    public void Find_Mothly_MSR(String Week, String Year, final String Region) {

        if (Region.contains("0")){ // 0 表示選台北

            progressBar.show();
            empty2.setVisibility(View.GONE);
            linear_main.setVisibility(View.VISIBLE);

        final String Month;
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, Integer.parseInt(Year));
        cal.set(Calendar.WEEK_OF_YEAR, Integer.parseInt(Week));
        int start = cal.get(Calendar.MONTH);
        Log.w("換算出是第幾月-預設從0開始",String.valueOf(start));
        if(start==0){
            Month="12";
            Year = String.valueOf(Integer.valueOf(Year)-1);
            textView_month.setText(Month);
            SetMonth = Month;
            CeckSetYear = Year;
        }else{
            Month = String.valueOf(start);
            textView_month.setText(Month);
            SetMonth = Month;
            CeckSetYear = Year;
        }

        RequestQueue mQueue = Volley.newRequestQueue(getActivity());

        Project_cnt_manAdapter_Item_List.clear();

        getString("http://wtsc.msi.com.tw/IMS/Weekly_Report.asmx/Find_Mothly_MSR?Year=" + Year + "&Month=" + Month, mQueue, new GetServiceData.VolleyCallback() {
            @Override
            public void onSuccess(JSONObject result) {

                try {

                    JSONArray UserArray = new JSONArray(result.getString("Key"));

                    if (UserArray.length() > 0) {

                        recyclerView.setVisibility(View.VISIBLE);

                    for (int i = 0; i < UserArray.length(); i++) {

                        JSONObject IssueData = UserArray.getJSONObject(i);

                        String F_M_Type = IssueData.getString("F_M_Type");

                        String Column1 = String.valueOf(IssueData.getDouble("Column1"));//專案

                        Project_cnt_manAdapter_Item_List.add(i, new Project_cnt_manAdapter_Item(F_M_Type, Column1));

                    }
                    //10月18新增方塊圖形
                    recyclerViewLayoutManager = new GridLayoutManager(mContext, 2);

                    recyclerView.setLayoutManager(recyclerViewLayoutManager);

                    recyclerView_Adapter = new RecyclerViewAdapter(mContext, Project_cnt_manAdapter_Item_List, Region);

                    recyclerView.setAdapter(recyclerView_Adapter);

                    recyclerView_Adapter.notifyDataSetChanged();

                }else{
                        recyclerView.setVisibility(View.GONE);
                        empty3.setVisibility(View.VISIBLE);
                        empty3.setText("第"+Month+"月份尚無資料");
                    }

                } catch (JSONException ex) {

                }
                progressBar.dismiss();
            }
        });

    }else{
            empty2.setVisibility(View.VISIBLE);
            linear_main.setVisibility(View.GONE);
            empty2.setText("尚未提供資料");
        }

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

        public RecyclerViewAdapter(Context context2, List<Project_cnt_manAdapter_Item> values2, String region){

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
            }
        }

        @Override
        public RecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){

            View view1 = LayoutInflater.from(context1).inflate(R.layout.msibook_dqaweekly_project_recycler_msr_item,parent,false);

            RecyclerViewAdapter.ViewHolder viewHolder1 = new RecyclerViewAdapter.ViewHolder(view1);

            return viewHolder1;
        }

        @Override
        public void onBindViewHolder(final RecyclerViewAdapter.ViewHolder Vholder, final int position){

//            for (int i = 0; i < ArrayM_Type.size(); i++){
//                if((values.get(position).GetM_Type().contains(ArrayM_Type.get(i)))){
//                    if(Double.valueOf(values.get(position).GetPeople()) > Double.valueOf(ArrayPeople.get(i))){
//                        Vholder.textView_updown.setBackgroundResource(R.mipmap.dqaweekly_ic_upgrade);
//                    }else if(Double.valueOf(values.get(position).GetPeople()) < Double.valueOf(ArrayPeople.get(i))){
//                        Vholder.textView_updown.setBackgroundResource(R.mipmap.dqaweekly_ic_lowering);
//                    }else{
//                        Vholder.textView_updown.setHeight(5);
//                        Vholder.textView_updown.setBackgroundResource(R.mipmap.dqaweekly_ic_level);
//                    }
//                }else{
//                }
//            }
//
//            if(values.get(position).GetM_Type().contains("CE")){
//                Vholder.textView_M_Type.setText("ACS");
//            }else {
//                Vholder.textView_M_Type.setText(values.get(position).GetM_Type());
//            }
//
//            if(values.get(position).GetM_Type().contains("Other")){
//                Vholder.cardview1.setBackgroundColor(Color.parseColor("#00c43e"));
//                Vholder.linear_up.setBackgroundColor(Color.parseColor("#ffffd3"));
//                Vholder.linear_down.setBackgroundColor(Color.parseColor("#ffffd3"));
//                Vholder.linear_down2.setBackgroundColor(Color.parseColor("#ffffd3"));
//            }

//            if(Region.contains("1")){
//                Log.w("選台北","選台北");
//                Vholder.textView_bu_by_year.setVisibility(View.VISIBLE);
//                Vholder.textView_bu1.setVisibility(View.VISIBLE);
//                Vholder.textView_bu2.setVisibility(View.VISIBLE);
                switch(values.get(position).GetF_M_Type()) {
                    case "ACS":
                        Vholder.textView_bu_by_year.setText("0.5");
                        break;
                    case "CND":
                        Vholder.textView_bu_by_year.setText("4.5");
                        break;
                    case "CPS":
                        Vholder.textView_bu_by_year.setText("43");
                        break;
                    case "EPS":
                        Vholder.textView_bu_by_year.setText("2.5");
                        break;
                    case "GNP":
                        Vholder.textView_bu_by_year.setText("1");
                        break;
                    case "IPS":
                        Vholder.textView_bu_by_year.setText("14");
                        break;
                    case "NB":
                        Vholder.textView_bu_by_year.setText("10.5");
                        break;
                    case "Other":
                        Vholder.textView_bu_by_year.setVisibility(View.GONE);
                        Vholder.textView_bu1.setVisibility(View.GONE);
                        Vholder.textView_bu2.setVisibility(View.GONE);
                        break;
                }
//
//            }else{
//                Log.w("選其它區","選其它區");
//                Vholder.textView_bu_by_year.setVisibility(View.GONE);
//                Vholder.textView_bu1.setVisibility(View.GONE);
//                Vholder.textView_bu2.setVisibility(View.GONE);
//            }
            Vholder.textView_M_Type.setText(values.get(position).GetF_M_Type());

            Vholder.textView_M_People.setText(values.get(position).GetColumn1());

            Vholder.linear_item_main.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                        Intent intent = new Intent();

                        intent.putExtra("Week", SetWeek);//代 週次到下一頁

                        intent.putExtra("Year", CeckSetYear);//代年到下一頁

                        intent.putExtra("Month", SetMonth);//代年到下一頁

                        intent.putExtra("RegionID", SetRegionID);//代 週次到下一頁

                        intent.putExtra("BU", values.get(position).GetF_M_Type());//代年到下一頁
                        Log.w("BUBUBUININININININ",values.get(position).GetF_M_Type());

                        intent.setClass(getActivity(), msibook_dqaweekly_project_activity_msr_bydept.class);
                        //開啟Activity
                        startActivity(intent);

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

        String F_M_Type;

        String Column1;

        public Project_cnt_manAdapter_Item(String F_M_Type,String Column1)
        {
            this.F_M_Type = F_M_Type;

            this.Column1 = Column1;
        }

        public String GetF_M_Type()
        {
            return this.F_M_Type;
        }

        public String GetColumn1()
        {
            return this.Column1;
        }

    }

    //-----------------------Adapter-----------------------



}
