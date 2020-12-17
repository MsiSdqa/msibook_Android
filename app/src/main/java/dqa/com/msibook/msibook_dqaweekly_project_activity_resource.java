package dqa.com.msibook;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

import java.text.NumberFormat;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link msibook_dqaweekly_project_activity_resource.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link msibook_dqaweekly_project_activity_resource#newInstance} factory method to
 * create an instance of this fragment.
 */
public class msibook_dqaweekly_project_activity_resource extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Context mContext;
    private View mView;

    private TextView textView_expectman;
    private TextView textView_totalman;

    private TextView textView_money_type;
    private TextView textView_NewMan;
    private TextView textView_LeaveMan;
    private TextView textView_TransferMan;
    private TextView textView_front_lackman;
    private TextView textView_LackMan;
    private TextView textView_Delay;
    private TextView textView_delay_title;
    private TextView textView_Night;
    private TextView textView_night_title;
    private TextView textView_OverTime;
    private TextView textView_overtime_title;

    private String LastWeek_Delay;
    private String LastWeek_Night;
    private String LastWeek_OverTime;
    private TextView textView_Delay_status;
    private TextView textView_Night_status;
    private TextView textView_OverTime_status;

    private Button btn_cost_info;
    private TextView textView_ExpectCost ;
    private TextView textView_RealCost;
    private TextView textView_FacShare_Cost;
    private TextView textView_OutSource_Cost;
    private TextView textView_CostSum;

    private Integer Set_CostSum;
    private Double Set_Double_CostSum;

    private TextView textView_mancost ;
    private TextView textView_really_cost;
    private TextView textView_report_cost;

    private Button Btn_OverTime;
    private ProgressDialog progressBar;
    private String Week;
    private String Year;
    private OnFragmentInteractionListener mListener;

    private LinearLayout linear_overtime_detail;
    private TextView textView_overtime_corner;

    public msibook_dqaweekly_project_activity_resource() {
        // Required empty public constructor
    }

    //抓人力資源
    public void Find_Resource_LsetWeek(final String Week, final String Year, final String RegionID, final String Dept) {

        String laseWeek="";
        //先撈上週
        if(Integer.valueOf(Week)!=0) {
            laseWeek = String.valueOf(Integer.valueOf(Week) - 1);
        }

        //顯示  讀取等待時間Bar
        progressBar.show();

        RequestQueue mQueue = Volley.newRequestQueue(getActivity());

        String Path = "http://wtsc.msi.com.tw/IMS/Weekly_Report.asmx/Find_Resource?Week=" + laseWeek + "&Year=" + Year  + "&RegionID=" + RegionID + "&Dept=" + Dept;

        getString(Path, mQueue, new GetServiceData.VolleyCallback() {
            @Override
            public void onSuccess(JSONObject result) {

                try {
                    JSONArray UserArray = new JSONArray(result.getString("Key"));

                    if (UserArray.length() > 0) {
                        for (int i = 0; i < UserArray.length(); i++) {

                            JSONObject IssueData = UserArray.getJSONObject(i);

                            String NewMan = String.valueOf(IssueData.getInt("NewMan"));

                            String LeaveMan = String.valueOf(IssueData.getInt("LeaveMan"));

                            String Delay = String.valueOf(IssueData.getInt("Delay"));
                            LastWeek_Delay = Delay;

                            String Night = String.valueOf(IssueData.getInt("Night"));
                            LastWeek_Night = Night;

                            String OverTime = String.valueOf(IssueData.getDouble("OverTime"));
                            LastWeek_OverTime = OverTime;

                            String TransferMan = String.valueOf(IssueData.getInt("TransferMan"));

                            String LackMan = String.valueOf(IssueData.getInt("LackMan"));

                        }

                        Find_Resource(Week,Year,RegionID,Dept);

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

    //抓人力資源
    public void Find_Resource(String Week, String Year, final String RegionID,String Dept) {


        this.Week = Week;
        this.Year = Year;
        //顯示  讀取等待時間Bar
        progressBar.show();

        RequestQueue mQueue = Volley.newRequestQueue(getActivity());

        String Path = "http://wtsc.msi.com.tw/IMS/Weekly_Report.asmx/Find_Resource?Week=" + Week + "&Year=" + Year  + "&RegionID=" + RegionID + "&Dept=" + Dept;

        getString(Path, mQueue, new GetServiceData.VolleyCallback() {
            @Override
            public void onSuccess(JSONObject result) {

                try {
                    JSONArray UserArray = new JSONArray(result.getString("Key"));

                    if (UserArray.length() > 0) {
                        for (int i = 0; i < UserArray.length(); i++) {

                            JSONObject IssueData = UserArray.getJSONObject(i);

                            String NewMan = String.valueOf(IssueData.getInt("NewMan"));

                            textView_NewMan.setText(NewMan);

                            String LeaveMan = String.valueOf(IssueData.getInt("LeaveMan"));

                            textView_LeaveMan.setText(LeaveMan);

                            String Delay = String.valueOf(IssueData.getInt("Delay"));

                            if(Integer.valueOf(RegionID)==1){
                                textView_Delay.setText(Delay);
                                textView_delay_title.setVisibility(View.VISIBLE);
                                textView_Delay_status.setVisibility(View.VISIBLE);
                                //Btn_OverTime.setVisibility(View.VISIBLE);
                                textView_overtime_corner.setVisibility(View.VISIBLE);
                                linear_overtime_detail.setEnabled(true);
                                if(Double.valueOf(Delay) > Double.valueOf(LastWeek_Delay)){
                                    textView_Delay_status.setBackgroundResource(R.mipmap.dqaweekly_ic_report_upgrade);
                                }else if(Double.valueOf(Delay) < Double.valueOf(LastWeek_Delay)){
                                    textView_Delay_status.setBackgroundResource(R.mipmap.dqaweekly_ic_report_lowering);
                                }else{
                                    textView_Delay_status.setBackgroundResource(R.mipmap.dqaweekly_ic_report_level);
                                }
                            }else{
                                textView_Delay.setText("N/A");
                                textView_delay_title.setVisibility(View.GONE);
                                textView_Delay_status.setVisibility(View.GONE);
                                //Btn_OverTime.setVisibility(View.INVISIBLE);
                                textView_overtime_corner.setVisibility(View.INVISIBLE);
                                linear_overtime_detail.setEnabled(false);
                            }

                            String Night = String.valueOf(IssueData.getInt("Night"));
                            ViewGroup.LayoutParams params = (ViewGroup.LayoutParams) textView_Night_status.getLayoutParams();
                            if(Integer.valueOf(RegionID)==1){
                                textView_Night.setText(Night);
                                textView_night_title.setVisibility(View.VISIBLE);
                                textView_Night_status.setVisibility(View.VISIBLE);
                                if(Double.valueOf(Night) > Double.valueOf(LastWeek_Night)){
                                    params.height = 21;
                                    textView_Night_status.setLayoutParams(params);
                                    textView_Night_status.setBackgroundResource(R.mipmap.dqaweekly_ic_report_upgrade);
                                }else if(Double.valueOf(Night) < Double.valueOf(LastWeek_Night)){
                                    params.height = 21;
                                    textView_Night_status.setLayoutParams(params);
                                    textView_Night_status.setBackgroundResource(R.mipmap.dqaweekly_ic_report_lowering);
                                }else{
                                    params.height = 7;
                                    textView_Night_status.setLayoutParams(params);
                                    textView_Night_status.setBackgroundResource(R.mipmap.dqaweekly_ic_report_level);
                                    textView_Night_status.setVisibility(View.INVISIBLE);
                                }
                            }else{
                                textView_Night.setText("N/A");
                                textView_night_title.setVisibility(View.GONE);
                                textView_Night_status.setVisibility(View.GONE);
                            }

                            String OverTime = String.valueOf(IssueData.getDouble("OverTime"));

                            if(Integer.valueOf(RegionID)==1){
                                textView_OverTime.setText(String.format("%.1f",Double.valueOf(OverTime)));
                                textView_overtime_title.setVisibility(View.VISIBLE);
                                textView_OverTime_status.setVisibility(View.VISIBLE);
                                if(Double.valueOf(OverTime) > Double.valueOf(LastWeek_OverTime)){
                                    textView_OverTime_status.setBackgroundResource(R.mipmap.dqaweekly_ic_report_upgrade);
                                }else if(Double.valueOf(OverTime) < Double.valueOf(LastWeek_OverTime)){
                                    textView_OverTime_status.setBackgroundResource(R.mipmap.dqaweekly_ic_report_lowering);
                                }else{
                                    textView_OverTime_status.setBackgroundResource(R.mipmap.dqaweekly_ic_report_level);
                                }
                            }else{
                                textView_OverTime.setText("N/A");
                                textView_overtime_title.setVisibility(View.GONE);
                                textView_OverTime_status.setVisibility(View.GONE);
                            }

                            String TransferMan = String.valueOf(IssueData.getInt("TransferMan"));

                            textView_TransferMan.setText(TransferMan);

                            String LackMan = String.valueOf(IssueData.getInt("LackMan"));

                            if(Integer.valueOf(LackMan)>0){
                                textView_front_lackman.setText("待補");
                                textView_LackMan.setTextColor(Color.parseColor("#495566"));
                                textView_LackMan.setText(String.valueOf(Math.abs(Integer.valueOf(LackMan))));
                            }else if(Integer.valueOf(LackMan)<0){
                                textView_front_lackman.setText("超編");
                                textView_LackMan.setTextColor(Color.parseColor("#f0625d"));
                                textView_LackMan.setText(String.valueOf(Math.abs(Integer.valueOf(LackMan))));
                            }

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

    //總人數 + 編制人數
    public void Find_Weekly_utilization_Total(String Week,String Year,String RegionID) {

        RequestQueue mQueue = Volley.newRequestQueue(getActivity());

        final Float[] CheckChart_utilization = {null};

        getString("http://wtsc.msi.com.tw/IMS/Weekly_Report.asmx/Find_Weekly_utilization_Total?Week=" + Week + "&Year=" + Year + "&RegionID=" + RegionID, mQueue, new GetServiceData.VolleyCallback() {
            @Override
            public void onSuccess(JSONObject result) {

                try {

                    JSONArray UserArray = new JSONArray(result.getString("Key"));

                    // NOTE: The order of the entries when being added to the entries array determines their position around the center of
                    // the chart.
                    if (UserArray.length() > 0) {
                        JSONObject IssueData = UserArray.getJSONObject(0);

                        if (IssueData.isNull("utilization")){
                            String utilization = "null";
                            utilization = "null";
                            Log.w("utilization",utilization);
                        }else{
                            String utilization = String.valueOf(IssueData.getDouble("utilization"));//"utilization": 89.46558216089467
                            utilization = utilization;

                            CheckChart_utilization[0] = Float.valueOf(utilization);

                            Log.w("utilization",utilization);
                        }

                        String TotalMan = String.valueOf(IssueData.getInt("TotalMan"));//實際

                        textView_totalman.setText(TotalMan);

                        String ExpectMan = String.valueOf(IssueData.getInt("ExpectMan"));//編制

                        textView_expectman.setText(ExpectMan);

                        if (Integer.valueOf(TotalMan) > Integer.valueOf(ExpectMan)){
                            textView_totalman.setTextColor(Color.parseColor("#f0625d"));
                        }else{
                            textView_totalman.setTextColor(Color.parseColor("#527da3"));
                        }

                    }

                } catch (JSONException ex) {

                }

            }
        });

    }

    //抓費用
    public void Find_Total_Cost(String Week,String Year,final String RegionID,String Dept) {

        //顯示  讀取等待時間Bar
        progressBar.show();

        RequestQueue mQueue = Volley.newRequestQueue(getActivity());

        String Path = "http://wtsc.msi.com.tw/IMS/Weekly_Report.asmx/Find_Total_Cost?Week=" + Week + "&Year=" + Year + "&RegionID=" + RegionID + "&Dept=" + Dept;

        getString(Path, mQueue, new GetServiceData.VolleyCallback() {
            @Override
            public void onSuccess(JSONObject result) {

                try {
                    JSONArray UserArray = new JSONArray(result.getString("Key"));

                    if (UserArray.length() > 0) {
                        for (int i = 0; i < UserArray.length(); i++) {

                            JSONObject IssueData = UserArray.getJSONObject(i);

                            NumberFormat nf = NumberFormat.getInstance();//設定三位數一逗點格式方法

                            //Double ExpectCost = Double.valueOf(IssueData.getDouble("ExpectCost"));//人力成本
                            Integer ExpectCost = Integer.valueOf(IssueData.getInt("ExpectCost"));//人力成本
                            Integer RMB_ExpectCost = ExpectCost/5;

                            //Double RealCost = Double.valueOf(IssueData.getDouble("RealCost"));//實際攤提
                            Integer RealCost = Integer.valueOf(IssueData.getInt("RealCost"));//實際攤提
                            Integer RMB_RealCost = RealCost/5;
                            Double Double_RealCost = Double.valueOf(IssueData.getInt("RealCost"));//

                            //Double RealCost = Double.valueOf(IssueData.getDouble("RealCost"));//
                            if (IssueData.isNull("CostSum")) {
                                Set_CostSum = 0;
                            } else {
                                Set_CostSum = IssueData.getInt("CostSum");
                            }
                            Integer RMB_CostSum = Set_CostSum/5;
                            //Double Double_CostSum = Double.valueOf(IssueData.getInt("CostSum"));//
                            if (IssueData.isNull("CostSum")) {
                                Set_Double_CostSum = 0.0;
                            } else {
                                Set_Double_CostSum = Double.valueOf(IssueData.getInt("CostSum"));
                            }

                            //Double OutSource_Cost = Double.valueOf(IssueData.getDouble("OutSource_Cost"));//外測費用
                            Integer OutSource_Cost = Integer.valueOf(IssueData.getInt("OutSource_Cost"));//外測費用
                            Integer RMB_OutSoucre =OutSource_Cost/5;

                            //Double FacShare_Cost = Double.valueOf(IssueData.getInt("FacShare_Cost"));//儀器費用
                            Integer FacShare_Cost = Integer.valueOf(IssueData.getInt("FacShare_Cost"));//儀器費用
                            Integer RMB_Share_Cost = FacShare_Cost/5;

                            //幣值轉換
                            if(RegionID.indexOf("1")==-1){ //如果不是台北
                                textView_mancost.setText("人力成本");
                                textView_really_cost.setText("實際攤提");
                                textView_report_cost.setText("報告費用");
                                textView_money_type.setText("RMB");
//                                textView_OutSource_Cost.setText(nf.format(RMB_OutSoucre));
//                                textView_FacShare_Cost.setText(nf.format(RMB_Share_Cost));
//                                textView_ExpectCost.setText(nf.format(RMB_ExpectCost));
//                                textView_RealCost.setText(nf.format(RMB_RealCost));
                                textView_OutSource_Cost.setText(nf.format(OutSource_Cost));
                                textView_FacShare_Cost.setText(nf.format(FacShare_Cost));
                                textView_ExpectCost.setText(nf.format(ExpectCost));
                                textView_RealCost.setText(nf.format(RealCost));
                                textView_CostSum.setText(nf.format(Set_CostSum));

                                if((Set_Double_CostSum/Double_RealCost) >= 0.8 && ((Set_Double_CostSum/Double_RealCost) <= 1.2)){
                                    textView_CostSum.setTextColor(Color.parseColor("#495566"));
                                }
                                else if((Set_Double_CostSum/Double_RealCost) > 1.2){
                                    textView_CostSum.setTextColor(Color.parseColor("#f0625d"));
                                }
                                else if((Set_Double_CostSum/Double_RealCost) < 0.8){
                                textView_CostSum.setTextColor(Color.parseColor("#358900"));
                                }

                            }else {
                                textView_mancost.setText("人力成本( 786X、787X )");
                                textView_really_cost.setText("實際攤提( 786X、787X )");
                                textView_report_cost.setText("報告費用( 786X、787X )");
                                textView_money_type.setText("NTD");
                                textView_OutSource_Cost.setText(nf.format(OutSource_Cost));
                                textView_FacShare_Cost.setText(nf.format(FacShare_Cost));
                                textView_ExpectCost.setText(nf.format(ExpectCost));
                                textView_RealCost.setText(nf.format(RealCost));
                                textView_CostSum.setText(nf.format(Set_CostSum));

                                if((Set_Double_CostSum/Double_RealCost) >= 0.8 && ((Set_Double_CostSum/Double_RealCost) <= 1.2)){
                                    textView_CostSum.setTextColor(Color.parseColor("#495566"));
                                }
                                else if((Set_Double_CostSum/Double_RealCost) > 1.2){
                                    textView_CostSum.setTextColor(Color.parseColor("#f0625d"));
                                }
                                else if((Set_Double_CostSum/Double_RealCost) < 0.8){
                                    textView_CostSum.setTextColor(Color.parseColor("#358900"));
                                }

                            }


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

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProjectActivity_resource.
     */
    // TODO: Rename and change types and number of parameters
    public static msibook_dqaweekly_project_activity_resource newInstance(String param1, String param2) {
        msibook_dqaweekly_project_activity_resource fragment = new msibook_dqaweekly_project_activity_resource();
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
        mView = inflater.inflate(R.layout.fragment_msibook_dqaweekly_project_activity_resource, container, false);
        mContext = getContext();

        textView_expectman = (TextView) mView.findViewById(R.id.textView_expectman);
        textView_totalman = (TextView) mView.findViewById(R.id.textView_totalman);

        textView_money_type = (TextView) mView.findViewById(R.id.textView_money_type);

        textView_NewMan = (TextView) mView.findViewById(R.id.textView_NewMan);
        textView_LeaveMan = (TextView) mView.findViewById(R.id.textView_LeaveMan);
        textView_TransferMan = (TextView) mView.findViewById(R.id.textView_TransferMan);
        textView_front_lackman = (TextView) mView.findViewById(R.id.textView_front_lackman);
        textView_LackMan = (TextView) mView.findViewById(R.id.textView_LackMan);
        textView_Delay = (TextView) mView.findViewById(R.id.textView_Delay);
        textView_delay_title = (TextView) mView.findViewById(R.id.textView_delay_title);
        textView_Night = (TextView) mView.findViewById(R.id.textView_Night);
        textView_night_title = (TextView) mView.findViewById(R.id.textView_night_title);
        textView_OverTime = (TextView) mView.findViewById(R.id.textView_OverTime);
        textView_overtime_title = (TextView) mView.findViewById(R.id.textView_overtime_title);

        textView_Delay_status = (TextView) mView.findViewById(R.id.textView_Delay_status);
        textView_Night_status = (TextView) mView.findViewById(R.id.textView_Night_status);
        textView_OverTime_status = (TextView) mView.findViewById(R.id.textView_OverTime_status);


        textView_ExpectCost = (TextView) mView.findViewById(R.id.textView_ExpectCost);
        textView_RealCost = (TextView) mView.findViewById(R.id.textView_RealCost);
        textView_FacShare_Cost = (TextView) mView.findViewById(R.id.textView_FacShare_Cost);
        textView_OutSource_Cost = (TextView) mView.findViewById(R.id.textView_OutSource_Cost);
        textView_CostSum = (TextView) mView.findViewById(R.id.textView_CostSum);

        textView_mancost = (TextView) mView.findViewById(R.id.textView_mancost);
        textView_really_cost = (TextView) mView.findViewById(R.id.textView_really_cost);
        textView_report_cost = (TextView) mView.findViewById(R.id.textView_report_cost);

        linear_overtime_detail = (LinearLayout) mView.findViewById(R.id.linear_overtime_detail);
        textView_overtime_corner = (TextView) mView.findViewById(R.id.textView_overtime_corner);

        linear_overtime_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //新的 詳細資訊
                try {
                    Intent k = new Intent(mContext, msibook_dqaweekly_project_activity_resource_detail.class);
//                    Bundle bundle = new Bundle();
//                    bundle.putString("Week",Week);
//                    bundle.putString("Year", Year);
                    //將Bundle物件assign給intent
//                    k.putExtras(bundle);
                    startActivity(k);
                } catch(Exception e) {
                    e.printStackTrace();
                }
            }
        });

        Btn_OverTime = (Button) mView.findViewById(R.id.Btn_OverTime);

        Btn_OverTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //舊的加班
//                try {
//                    Intent k = new Intent(mContext, msibook_dqaweekly_project_activity_overtime.class);
//
//                    Bundle bundle = new Bundle();
//                    bundle.putString("Week",Week);
//                    bundle.putString("Year", Year);
//
//                    //將Bundle物件assign給intent
//                    k.putExtras(bundle);
//                    startActivity(k);
//                } catch(Exception e) {
//                    e.printStackTrace();
//                }

                //新的 詳細資訊
                try {
                    Intent k = new Intent(mContext, msibook_dqaweekly_project_activity_resource_detail.class);
//                    Bundle bundle = new Bundle();
//                    bundle.putString("Week",Week);
//                    bundle.putString("Year", Year);
                    //將Bundle物件assign給intent
//                    k.putExtras(bundle);
                    startActivity(k);
                } catch(Exception e) {
                    e.printStackTrace();
                }

            }
        });

        btn_cost_info = (Button) mView.findViewById(R.id.btn_cost_info);//msibook_dqaweekly_project_resource_cost_info

        btn_cost_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int []location=new int[2];
                v.getLocationOnScreen(location);
                int x=location[0];//获取当前位置的横坐标
                int y=location[1];

                Intent intent = new Intent(getActivity(), msibook_dqaweekly_project_resource_cost_info.class);

                intent.putExtra("x_Location",String.valueOf(x));
                Log.w("x_Location",String.valueOf(x));

                intent.putExtra("y_Location",String.valueOf(y));
                Log.w("y_Location",String.valueOf(y));

                msibook_dqaweekly_project_activity_resource.this.startActivity(intent);

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
}
