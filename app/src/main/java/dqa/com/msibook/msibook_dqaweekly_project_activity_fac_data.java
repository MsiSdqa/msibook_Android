package dqa.com.msibook;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
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
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link msibook_dqaweekly_project_activity_fac_data.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link msibook_dqaweekly_project_activity_fac_data#newInstance} factory method to
 * create an instance of this fragment.
 */
public class msibook_dqaweekly_project_activity_fac_data extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Context mContext;
    private View mView;

    private BarChart mChart;

    private ProgressDialog progressBar;//讀取狀態

    private FacDataAdapter mFacDataAdapter;
    private List<Find_Fac_Data_Item> Find_Fac_Data_Item_List = new ArrayList<Find_Fac_Data_Item>();

    private List<String> ArrayF_Modelcnt = new ArrayList<String>();
    public List<String> ArrayF_COMPLETE_QTY = new ArrayList<String>();
    public List<String> ArrayF_NG_QTY = new ArrayList<String>();

    private ListView mListView;

    private OnFragmentInteractionListener mListener;

    public msibook_dqaweekly_project_activity_fac_data() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProjectActivity_fac_data.
     */
    // TODO: Rename and change types and number of parameters
    public static msibook_dqaweekly_project_activity_fac_data newInstance(String param1, String param2) {
        msibook_dqaweekly_project_activity_fac_data fragment = new msibook_dqaweekly_project_activity_fac_data();
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
        progressBar = new ProgressDialog(getActivity());
        progressBar.setCancelable(true);
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.setMessage("資料載入中");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_msibook_dqaweekly_project_activity_fac_data, container, false);
        mContext = getContext();

        mChart = (BarChart) mView.findViewById(R.id.chart1);

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


//    //當週新增專案 資訊
//    public void Find_Fac_Data(String Week) {
//
//        //顯示  讀取等待時間Bar
//        progressBar.show();
//
//        Find_Fac_Data_Item_List.clear();
//
//        RequestQueue mQueue = Volley.newRequestQueue(getActivity());
//
//        String Path = "http://wtsc.msi.com.tw/IMS/Weekly_Report.asmx/Find_Fac_Data?Week=" + Week;
//
//        getString(Path, mQueue, new MainActivity.VolleyCallback() {
//            @Override
//            public void onSuccess(JSONObject result) {
//                try {
//
//                    JSONArray UserArray = new JSONArray(result.getString("Key"));
//
//                    for (int i = 0; i < UserArray.length(); i++) {
//                        JSONObject IssueData = UserArray.getJSONObject(i);
//
//                        NumberFormat nf = NumberFormat.getInstance();//設定三位數一逗點格式方法
//
//                        String F_BU = String.valueOf(IssueData.getString("F_BU"));// "AIO-OBM",
//
//                        String F_COMPLETE_QTY = String.valueOf(nf.format(IssueData.getLong("F_COMPLETE_QTY"))); //13016,
//
//                        String F_NG_QTY = String.valueOf(nf.format(IssueData.getLong("F_NG_QTY"))); //"3FA4",
//
//                        Find_Fac_Data_Item_List.add(i,new Find_Fac_Data_Item(F_BU,F_COMPLETE_QTY,F_NG_QTY));
//
//                    }
//                    mListView = (ListView)getActivity().findViewById(R.id.listview_fac_data);
//
//                    mFacDataAdapter = new FacDataAdapter(mContext,Find_Fac_Data_Item_List);
//
//                    mListView.setAdapter(mFacDataAdapter);
//
//                    //關閉-讀取等待時間Bar
//                    progressBar.dismiss();
//
//
//                }
//                catch (JSONException ex) {
//
//                    Log.w("Json",ex.toString());
//                }
//
//            }
//        });
//
//
//    }

    public void Find_Fac_List_LastWeek(final String Year, final String Week) {

        //顯示  讀取等待時間Bar
        progressBar.show();

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
        ArrayF_Modelcnt.clear();
        ArrayF_COMPLETE_QTY.clear();
        ArrayF_NG_QTY.clear();

        RequestQueue mQueue = Volley.newRequestQueue(getActivity());
        String Path;

        Path = "http://wtsc.msi.com.tw/IMS/Weekly_Report.asmx/Find_Fac_List?Year=" + laseYear + "&Week=" + laseWeek;

        getString(Path, mQueue, new GetServiceData.VolleyCallback() {
            @Override
            public void onSuccess(JSONObject result) {

                try {
                    String AlertMessage = "";

                    JSONArray UserArray = new JSONArray(result.getString("Key"));

                    for (int i = 0; i < UserArray.length(); i++) {

                        JSONObject IssueData = UserArray.getJSONObject(i);

                        String F_Modelcnt = "";
                        if (IssueData.isNull("F_Modelcnt")) {
                            F_Modelcnt = "0";
                            ArrayF_Modelcnt.add("0");
                        } else {
                            F_Modelcnt = String.valueOf(IssueData.getInt("F_Modelcnt"));
                            ArrayF_Modelcnt.add(F_Modelcnt);
                        }

                        String F_COMPLETE_QTY = "";
                        if (IssueData.isNull("F_COMPLETE_QTY")) {
                            F_COMPLETE_QTY = "0";
                            ArrayF_COMPLETE_QTY.add("0");
                            Log.w("F_COMPLETE_QTY","F_COMPLETE_QTY=0");
                        } else {
                            F_COMPLETE_QTY = String.valueOf(IssueData.getInt("F_COMPLETE_QTY"));
                            ArrayF_COMPLETE_QTY.add(F_COMPLETE_QTY);
                            Log.w("F_COMPLETE_QTY","F_COMPLETE_QTY有");
                        }

                        String F_NG_QTY = "";
                        if (IssueData.isNull("F_NG_QTY")) {
                            F_NG_QTY = "0";
                            ArrayF_NG_QTY.add("0");
                        } else {
                            F_NG_QTY = String.valueOf(IssueData.getInt("F_NG_QTY")); //
                            ArrayF_NG_QTY.add(F_NG_QTY);
                        }

                    }

                    Find_Fac_List(Year,Week);
                    //關閉-讀取等待時間Bar
                    progressBar.dismiss();
                } catch (JSONException ex) {
                    Log.w("Json", ex.toString());
                }
            }

        });

    }

    public void Find_Fac_List(String Year, String Week) {


        //顯示  讀取等待時間Bar
        progressBar.show();

        Find_Fac_Data_Item_List.clear();

        RequestQueue mQueue = Volley.newRequestQueue(getActivity());
        String Path;
//        if(Integer.valueOf(Week)==1){
//            Path = "http://wtsc.msi.com.tw/IMS/Weekly_Report.asmx/Find_Fac_List?Year=" + String.valueOf(Integer.valueOf(Year)-1) + "&Week=" + "53";
//        }else {
//            Path = "http://wtsc.msi.com.tw/IMS/Weekly_Report.asmx/Find_Fac_List?Year=" + Year + "&Week=" + Week;
//        }
        Path = "http://wtsc.msi.com.tw/IMS/Weekly_Report.asmx/Find_Fac_List?Year=" + Year + "&Week=" + Week;

        getString(Path, mQueue, new GetServiceData.VolleyCallback() {
            @Override
            public void onSuccess(JSONObject result) {

                try {
                    String AlertMessage = "";

                    JSONArray UserArray = new JSONArray(result.getString("Key"));

                    ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();
                    ArrayList<String> mylist = new ArrayList<String>();

                    for (int i = 0; i < UserArray.length(); i++) {

                        JSONObject IssueData = UserArray.getJSONObject(i);

                        //NumberFormat nf = NumberFormat.getInstance();//設定三位數一逗點格式方法

                        String F_BU = String.valueOf(IssueData.getString("F_BU")); //

                        String F_Modelcnt = "";

                        if (IssueData.isNull("F_Modelcnt")) {
                            F_Modelcnt = "0";
                        } else {
                            F_Modelcnt = String.valueOf(IssueData.getString("F_Modelcnt"));
                        }

                        String F_COMPLETE_QTY = "";
                        if (IssueData.isNull("F_COMPLETE_QTY")) {
                            F_COMPLETE_QTY = "0";
                            Log.w("F_COMPLETE_QTY","F_COMPLETE_QTY=0");
                        } else {
                            F_COMPLETE_QTY = String.valueOf(IssueData.getInt("F_COMPLETE_QTY"));
                            Log.w("F_COMPLETE_QTY","F_COMPLETE_QTY有");
                        }

                        //String TF_COMPLETE_QTY = nf.format(Integer.valueOf(F_COMPLETE_QTY));
                        String TF_COMPLETE_QTY = String.valueOf(Integer.valueOf(F_COMPLETE_QTY));

                        String F_NG_QTY = "";
                        if (IssueData.isNull("F_NG_QTY")) {
                            F_NG_QTY = "0";
                        } else {
                            F_NG_QTY = String.valueOf(IssueData.getInt("F_NG_QTY")); //
                        }

                        //String TF_NG_QTY = nf.format(Integer.valueOf(F_NG_QTY));
                        String TF_NG_QTY = String.valueOf(Integer.valueOf(F_NG_QTY));

                        //String NG_Rate = String.valueOf(IssueData.getDouble("NG_Rate"));

                        //NG / NG+完美 = 不良率
                        String AvgNG = String.valueOf(Double.valueOf(F_NG_QTY) / (Double.valueOf(F_NG_QTY) + Double.valueOf(F_COMPLETE_QTY)));

                        //mylist.add(i,NG_Rate.replace("2017",""));
                        mylist.add(i, F_BU);

                        yVals1.add(new BarEntry(i, Float.parseFloat(AvgNG) * 100));
                        //yVals1.add(new BarEntry(i * 10f, Float.parseFloat(PeopleCount)));

                        Find_Fac_Data_Item_List.add(i, new Find_Fac_Data_Item(F_BU, F_Modelcnt, TF_COMPLETE_QTY, TF_NG_QTY));


                    }

//                    if (!AlertMessage.isEmpty())
//                    {
//                        AlertDialog alertDialog = new AlertDialog.Builder(mContext).create();
//                        alertDialog.setTitle("Alert");
//                        alertDialog.setMessage(AlertMessage);
//                        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
//                                new DialogInterface.OnClickListener() {
//                                    public void onClick(DialogInterface dialog, int which) {
//                                        dialog.dismiss();
//                                    }
//                                });
//                        alertDialog.show();
//                    }

                    mListView = (ListView) getActivity().findViewById(R.id.listview_fac_data);

                    mFacDataAdapter = new FacDataAdapter(mContext, Find_Fac_Data_Item_List);

                    mListView.setAdapter(mFacDataAdapter);

                    //關閉-讀取等待時間Bar
                    progressBar.dismiss();

                    String[] stockArr = new String[mylist.size()];
                    stockArr = mylist.toArray(stockArr);
                    mChart.getDescription().setEnabled(false);

                    // if more than 60 entries are displayed in the chart, no values will be
                    // drawn
                    //mChart.setMaxVisibleValueCount(100);

                    // scaling can now only be done on x- and y-axis separately
                    mChart.setPinchZoom(false);//y轴的值是否跟随图表变换缩放;如果禁止，y轴的值会跟随图表变换缩放
                    mChart.setTouchEnabled(false); // 设置是否可以触摸
                    mChart.setDrawBarShadow(false);
                    mChart.setDrawGridBackground(false);//setGroupSpace

                    int xlabelColor = getResources().getColor(R.color.chartxlabelColor);
                    XAxis xAxis = mChart.getXAxis();
                    xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                    xAxis.setDrawGridLines(false);//绘制网格线，默认为true
                    xAxis.setDrawAxisLine(true);// X軸 底線
                    xAxis.setDrawLabels(true);
                    xAxis.setTextColor(xlabelColor); //X軸 數值顏色

                    YAxis leftAxis = mChart.getAxisLeft();//获取左轴
                    // YAxis rightAxis = chart.getAxisRight();//获取右轴
                    leftAxis.setSpaceBottom(5);
                    leftAxis.setDrawGridLines(false);//绘制网格线 默认为true
                    leftAxis.setDrawZeroLine(true);//是否繪製0所在的網格線

                    int noneColor = getResources().getColor(R.color.chartnoneColorColor);
                    mChart.getAxisLeft().setTextColor(noneColor);
                    mChart.getAxisRight().setTextColor(noneColor);
                    mChart.getAxisRight().setEnabled(false);//隐藏右轴  默认显示
                    mChart.getAxisLeft().setEnabled(false);//隐藏左轴  默认显示

                    mChart.getAxisLeft().setDrawGridLines(false);
                    // setting data

                    // add a nice and smooth animation
                    mChart.animateY(100);

                    mChart.getLegend().setEnabled(false);

                    BarDataSet set1;

                    if (mChart.getData() != null && mChart.getData().getDataSetCount() > 0) {
                        set1 = (BarDataSet) mChart.getData().getDataSetByIndex(0);
                        set1.setValues(yVals1);

                        mChart.getData().notifyDataChanged();
                        mChart.notifyDataSetChanged();

                    } else {
                        set1 = new BarDataSet(yVals1, "");
                        int BarColor = getResources().getColor(R.color.chartbarColor);
                        int valueColor = getResources().getColor(R.color.chartvalueColor);
                        set1.setColors(BarColor);
                        set1.setDrawValues(true);
                        set1.setValueTextColor(valueColor);

                        ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
                        dataSets.add(set1);

                        BarData data = new BarData(dataSets);

                        data.setBarWidth(0.4f);//線條寬度
                        data.setValueTextSize(14);

                        mChart.setData(data);
                        mChart.setFitBars(true);

                    }
                    mChart.getXAxis().setLabelCount(stockArr.length);
                    mChart.getXAxis().setValueFormatter(new LabelFormatter(stockArr));
                    mChart.getXAxis().setTextSize(Float.parseFloat("12"));

                    //mChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
                    //setbarspacepercent
                    //mChart.setVisibleXRangeMaximum(3);




                } catch (JSONException ex) {
                    Log.w("Json", ex.toString());
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


    //-------------------Item---------------------
    public class Find_Fac_Data_Item  {

        String F_BU;

        String F_Modelcnt;

        String F_COMPLETE_QTY;

        String F_NG_QTY;




        public Find_Fac_Data_Item(String F_BU,String F_Modelcnt,String F_COMPLETE_QTY,String F_NG_QTY)
        {
            this.F_BU = F_BU;

            this.F_Modelcnt = F_Modelcnt;

            this.F_COMPLETE_QTY = F_COMPLETE_QTY;

            this.F_NG_QTY = F_NG_QTY;

        }


        public String GetF_BU()
        {
            return this.F_BU;
        }

        public String GetF_Modelcnt()
        {
            return this.F_Modelcnt;
        }

        public String GetF_COMPLETE_QTY()
        {
            return this.F_COMPLETE_QTY;
        }

        public String GetF_NG_QTY()
        {
            return this.F_NG_QTY;
        }

    }

    //------------------Adapter------------------
    public class FacDataAdapter extends BaseAdapter {

        private LayoutInflater mLayInf;

        private List<Find_Fac_Data_Item> Find_Fac_Data_Item_List;

        private Context FacDataContext;

        private String Title;

        public FacDataAdapter(Context context,  List<Find_Fac_Data_Item> Find_Fac_Data_Item_List)
        {
            mLayInf = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            FacDataContext = context;

            this.Title = Title;

            this.Find_Fac_Data_Item_List = Find_Fac_Data_Item_List;

        }
        @Override
        public int getCount() {
            return Find_Fac_Data_Item_List.size();
        }

        @Override
        public Object getItem(int position) {
            return Find_Fac_Data_Item_List.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View v = new View(FacDataContext);

            v = mLayInf.inflate(R.layout.msibook_dqaweekly_facdata_adapter, parent, false);

            NumberFormat nf = NumberFormat.getInstance();//設定三位數一逗點格式方法      String TF_COMPLETE_QTY = nf.format(Integer.valueOf(F_COMPLETE_QTY));


            TextView textView_bu = (TextView) v.findViewById(R.id.textView_bu);
            TextView textView_modelcnt = (TextView) v.findViewById(R.id.textView_modelcnt);
            TextView textView_complete = (TextView) v.findViewById(R.id.textView_complete);
            TextView textView_ng = (TextView) v.findViewById(R.id.textView_ng);

            TextView textView_modelcnt_status = (TextView) v.findViewById(R.id.textView_modelcnt_status);
            TextView textView_complete_status = (TextView) v.findViewById(R.id.textView_complete_status);
            TextView textView_ng_status = (TextView) v.findViewById(R.id.textView_ng_status);

            textView_bu.setText(Find_Fac_Data_Item_List.get(position).GetF_BU());

            textView_modelcnt.setText(nf.format(Integer.valueOf(Find_Fac_Data_Item_List.get(position).GetF_Modelcnt())));
            if(Integer.valueOf(Find_Fac_Data_Item_List.get(position).GetF_Modelcnt()) > Integer.valueOf(ArrayF_Modelcnt.get(position))){
                textView_modelcnt_status.setBackgroundResource(R.mipmap.dqaweekly_ic_report_upgrade);

            }else if(Integer.valueOf(Find_Fac_Data_Item_List.get(position).GetF_Modelcnt()) < Integer.valueOf(ArrayF_Modelcnt.get(position))){
                textView_modelcnt_status.setBackgroundResource(R.mipmap.dqaweekly_ic_report_lowering);

            }else{
                ViewGroup.LayoutParams params = (ViewGroup.LayoutParams) textView_modelcnt_status.getLayoutParams();
                params.height = 7;
                textView_modelcnt_status.setLayoutParams(params);
                textView_modelcnt_status.setBackgroundResource(R.mipmap.dqaweekly_ic_report_level);
            }

            textView_complete.setText(nf.format(Integer.valueOf(Find_Fac_Data_Item_List.get(position).GetF_COMPLETE_QTY())));
            if(Integer.valueOf(Find_Fac_Data_Item_List.get(position).GetF_COMPLETE_QTY()) > Integer.valueOf(ArrayF_COMPLETE_QTY.get(position))){
                textView_complete_status.setBackgroundResource(R.mipmap.dqaweekly_ic_report_upgrade);

            }else if(Integer.valueOf(Find_Fac_Data_Item_List.get(position).GetF_COMPLETE_QTY()) < Integer.valueOf(ArrayF_COMPLETE_QTY.get(position))){
                textView_complete_status.setBackgroundResource(R.mipmap.dqaweekly_ic_report_lowering);

            }else{
                ViewGroup.LayoutParams params = (ViewGroup.LayoutParams) textView_complete_status.getLayoutParams();
                params.height = 7;
                textView_complete_status.setLayoutParams(params);
                textView_complete_status.setBackgroundResource(R.mipmap.dqaweekly_ic_report_level);
            }

            textView_ng.setText(nf.format(Integer.valueOf(Find_Fac_Data_Item_List.get(position).GetF_NG_QTY())));
            if(Integer.valueOf(Find_Fac_Data_Item_List.get(position).GetF_NG_QTY()) > Integer.valueOf(ArrayF_NG_QTY.get(position))){
                textView_ng_status.setBackgroundResource(R.mipmap.dqaweekly_ic_report_upgrade);

            }else if(Integer.valueOf(Find_Fac_Data_Item_List.get(position).GetF_NG_QTY()) < Integer.valueOf(ArrayF_NG_QTY.get(position))){
                textView_ng_status.setBackgroundResource(R.mipmap.dqaweekly_ic_report_lowering);

            }else{
                ViewGroup.LayoutParams params = (ViewGroup.LayoutParams) textView_ng_status.getLayoutParams();
                params.height = 7;
                textView_ng_status.setLayoutParams(params);
                textView_ng_status.setBackgroundResource(R.mipmap.dqaweekly_ic_report_level);
            }

            return v;
        }

    }

}
