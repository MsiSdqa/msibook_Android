package dqa.com.msibook;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link msibook_dqaweekly_project_activity_resource_detail_delay.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link msibook_dqaweekly_project_activity_resource_detail_delay#newInstance} factory method to
 * create an instance of this fragment.
 */
public class msibook_dqaweekly_project_activity_resource_detail_delay extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private Context mContext;
    private View mView;
    private ProgressDialog progressBar;

    private String SetDept;
    private String SetYear;
    private String SetWeek;

    private ArrayList<String> Dept_List = new ArrayList<String>();

    private List<Delay_Adapter_Item> Delay_Adapter_Item_List_LastWeek = new ArrayList<Delay_Adapter_Item>();
    private List<Delay_Adapter_Item> Delay_Adapter_Item_List = new ArrayList<Delay_Adapter_Item>();

    private Integer Save_Total_delay_lw;
    private Integer Save_Total_delay;

    private Integer Save7850_Total_man_lw;
    private Integer Save7850_Total_delay_lw;
    private Integer Save7850_Total_man;
    private Integer Save7850_Total_delay;

    private Integer Save7860_Total_man_lw;
    private Integer Save7860_Total_delay_lw;
    private Integer Save7860_Total_man;
    private Integer Save7860_Total_delay;

    private Integer Save7870_Total_man_lw;
    private Integer Save7870_Total_delay_lw;
    private Integer Save7870_Total_man;
    private Integer Save7870_Total_delay;

    private Integer Save7880_Total_man_lw;
    private Integer Save7880_Total_delay_lw;
    private Integer Save7880_Total_man;
    private Integer Save7880_Total_delay;

    private LinearLayout linear_delay_up_lw;
    private LinearLayout linear_delay_down_lw;
    private LinearLayout linear_delay_up;
    private LinearLayout linear_delay_down;

    private TextView empty1;
    private TextView empty2;
    private TextView textView_dqa_totalman;

    private TextView textView_updown_totalman;
    private TextView textView_delay_totalman;

    private RecyclerView recycle_dp_info;

    //10月18日新增 方框圖形化
    private RecyclerView recyclerView;
    private RecyclerView.Adapter recyclerView_Adapter;
    private RecyclerView.LayoutManager recyclerViewLayoutManager;


    public msibook_dqaweekly_project_activity_resource_detail_delay() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment msibook_dqaweekly_project_activity_resource_detail_delay.
     */
    // TODO: Rename and change types and number of parameters
    public static msibook_dqaweekly_project_activity_resource_detail_delay newInstance(String param1, String param2) {
        msibook_dqaweekly_project_activity_resource_detail_delay fragment = new msibook_dqaweekly_project_activity_resource_detail_delay();
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
        mContext = getContext();
        View mView = inflater.inflate(R.layout.fragment_msibook_dqaweekly_project_activity_resource_detail_delay, container, false);

        linear_delay_up = (LinearLayout) mView.findViewById(R.id.linear_delay_up);  //
        empty1 = (TextView) mView.findViewById(R.id.empty1);
        empty1.setVisibility(View.GONE);
        empty2 = (TextView) mView.findViewById(R.id.empty2);
        empty2.setVisibility(View.GONE);
        textView_dqa_totalman = (TextView) mView.findViewById(R.id.textView_dqa_totalman);  //DQA總人數
        textView_delay_totalman = (TextView) mView.findViewById(R.id.textView_delay_totalman);  // 遲到總人數
        textView_updown_totalman = (TextView) mView.findViewById(R.id.textView_updown_totalman);

        recyclerView = (RecyclerView) mView.findViewById(R.id.recycle_dp_info); // Recycle 部門資訊

        Dept_List.add("7850");
        Dept_List.add("7860");
        Dept_List.add("7870");
        Dept_List.add("7880");


        linear_delay_down = (LinearLayout) mView.findViewById(R.id.linear_delay_down);  //




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

    //抓選擇到的地區
    public void SetDept(String Dept) {
        SetDept = Dept;
    }

    //抓週
    public void SetWeek(String Week) {
        SetWeek = Week;
    }

    //抓年
    public void SetYear(String Year) {
        SetYear = Year;
    }

    public void Find_Total_OverTime_LastWeek(final String Week, final String Year) {

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

        //顯示 讀取等待時間Bar

        Save_Total_delay_lw = 0;

        Save7850_Total_man_lw = 0;
        Save7850_Total_delay_lw = 0;

        Save7860_Total_man_lw = 0;
        Save7860_Total_delay_lw = 0;

        Save7870_Total_man_lw = 0;
        Save7870_Total_delay_lw = 0;

        Save7880_Total_man_lw = 0;
        Save7880_Total_delay_lw = 0;

        RequestQueue mQueue = Volley.newRequestQueue(getActivity());

        String Path = "http://wtsc.msi.com.tw/IMS/Weekly_Report.asmx/Find_Total_OverTime?Week=" + laseWeek + "&Year=" + laseYear;

        getString(Path, mQueue, new GetServiceData.VolleyCallback() {
            @Override
            public void onSuccess(JSONObject result) {

                try {

                    JSONArray UserArray = new JSONArray(result.getString("Key"));

                    if (UserArray.length() > 0) {

                        if(SetDept.contains("1")) {

                            linear_delay_up.setVisibility(View.VISIBLE);
                            linear_delay_down.setVisibility(View.VISIBLE);

                            for (int i = 0; i < UserArray.length(); i++) {

                                JSONObject IssueData = UserArray.getJSONObject(i);

                                String OverTimeHour = String.valueOf(IssueData.getString("OverTimeHour")); //加班

                                String TotalMan = String.valueOf(IssueData.getString("TotalMan")); //總人數

                                String F_DeptCode = String.valueOf(IssueData.getString("F_DeptCode")); //總人數

                                String Delay = String.valueOf(IssueData.getString("Delay")); //總人數

                                String Night = String.valueOf(IssueData.getString("Night")); //總人數

                                Log.w("F_DeptCode", String.valueOf(F_DeptCode));

                                Save_Total_delay_lw += Integer.valueOf(Delay);

                                if (F_DeptCode.contains("785")) {
                                    Save7850_Total_man_lw += Integer.valueOf(TotalMan);
                                    Save7850_Total_delay_lw += Integer.valueOf(Delay);
                                } else if (F_DeptCode.contains("786")) {
                                    Save7860_Total_man_lw += Integer.valueOf(TotalMan);
                                    Save7860_Total_delay_lw += Integer.valueOf(Delay);
                                } else if (F_DeptCode.contains("787")) {
                                    Save7870_Total_man_lw += Integer.valueOf(TotalMan);
                                    Save7870_Total_delay_lw += Integer.valueOf(Delay);
                                } else if (F_DeptCode.contains("788")) {
                                    Save7880_Total_man_lw += Integer.valueOf(TotalMan);
                                    Save7880_Total_delay_lw += Integer.valueOf(Delay);
                                }

                            }
                            //Delay_Adapter_Item_List.add(0, new Delay_Adapter_Item(String.valueOf(Save7850_Total_man),"7850",String.valueOf(Save7850_Total_delay)));
                            Delay_Adapter_Item_List_LastWeek.add(0, new Delay_Adapter_Item(String.valueOf(Save7850_Total_man_lw),"7850",String.valueOf("N/A")));
                            Delay_Adapter_Item_List_LastWeek.add(1, new Delay_Adapter_Item(String.valueOf(Save7860_Total_man_lw),"7860",String.valueOf(Save7860_Total_delay_lw)));
                            Delay_Adapter_Item_List_LastWeek.add(2, new Delay_Adapter_Item(String.valueOf(Save7870_Total_man_lw),"7870",String.valueOf(Save7870_Total_delay_lw)));
                            //Delay_Adapter_Item_List.add(3, new Delay_Adapter_Item(String.valueOf(Save7880_Total_man),"7880",String.valueOf(Save7880_Total_delay)));
                            Delay_Adapter_Item_List_LastWeek.add(3, new Delay_Adapter_Item(String.valueOf(Save7880_Total_man_lw),"7880",String.valueOf("N/A")));

                            Find_Total_OverTime(Week,Year);

                        }else{

                            Find_Total_OverTime(Week,Year);
                        }
                    }else{

                    }

                } catch (JSONException ex) {

                }
            }
        });
    }

    public void Find_Total_OverTime(String Week,String Year) {

        //顯示 讀取等待時間Bar

        Save_Total_delay = 0;

        Save7850_Total_man = 0;
        Save7850_Total_delay = 0;

        Save7860_Total_man = 0;
        Save7860_Total_delay = 0;

        Save7870_Total_man = 0;
        Save7870_Total_delay = 0;

        Save7880_Total_man = 0;
        Save7880_Total_delay = 0;

        Delay_Adapter_Item_List.clear();

        RequestQueue mQueue = Volley.newRequestQueue(getActivity());

        String Path = "http://wtsc.msi.com.tw/IMS/Weekly_Report.asmx/Find_Total_OverTime?Week=" + Week + "&Year=" + Year;

        getString(Path, mQueue, new GetServiceData.VolleyCallback() {
            @Override
            public void onSuccess(JSONObject result) {

                try {

                    JSONArray UserArray = new JSONArray(result.getString("Key"));

                    if (UserArray.length() > 0) {

                        if(SetDept.contains("1")) {

                            linear_delay_up.setVisibility(View.VISIBLE);
                            linear_delay_down.setVisibility(View.VISIBLE);

                            for (int i = 0; i < UserArray.length(); i++) {

                                JSONObject IssueData = UserArray.getJSONObject(i);

                                String OverTimeHour = String.valueOf(IssueData.getString("OverTimeHour")); //加班

                                String TotalMan = String.valueOf(IssueData.getString("TotalMan")); //總人數

                                String F_DeptCode = String.valueOf(IssueData.getString("F_DeptCode")); //總人數

                                String Delay = String.valueOf(IssueData.getString("Delay")); //總人數

                                String Night = String.valueOf(IssueData.getString("Night")); //總人數

                                Log.w("F_DeptCode", String.valueOf(F_DeptCode));

                                Save_Total_delay += Integer.valueOf(Delay);



                                if (F_DeptCode.contains("785")) {
                                    Save7850_Total_man += Integer.valueOf(TotalMan);
                                    Save7850_Total_delay += Integer.valueOf(Delay);
                                } else if (F_DeptCode.contains("786")) {
                                    Save7860_Total_man += Integer.valueOf(TotalMan);
                                    Save7860_Total_delay += Integer.valueOf(Delay);
                                } else if (F_DeptCode.contains("787")) {
                                    Save7870_Total_man += Integer.valueOf(TotalMan);
                                    Save7870_Total_delay += Integer.valueOf(Delay);
                                } else if (F_DeptCode.contains("788")) {
                                    Save7880_Total_man += Integer.valueOf(TotalMan);
                                    Save7880_Total_delay += Integer.valueOf(Delay);
                                }
                            }
                            //Delay_Adapter_Item_List.add(0, new Delay_Adapter_Item(String.valueOf(Save7850_Total_man),"7850",String.valueOf(Save7850_Total_delay)));
                            Delay_Adapter_Item_List.add(0, new Delay_Adapter_Item(String.valueOf(Save7850_Total_man),"7850",String.valueOf("N/A")));
                            Delay_Adapter_Item_List.add(1, new Delay_Adapter_Item(String.valueOf(Save7860_Total_man),"7860",String.valueOf(Save7860_Total_delay)));
                            Delay_Adapter_Item_List.add(2, new Delay_Adapter_Item(String.valueOf(Save7870_Total_man),"7870",String.valueOf(Save7870_Total_delay)));
                            Delay_Adapter_Item_List.add(3, new Delay_Adapter_Item(String.valueOf(Save7880_Total_man),"7880",String.valueOf("N/A")));

                            textView_delay_totalman.setText(String.valueOf(Save_Total_delay));

                            if(Double.valueOf(Save_Total_delay) > Double.valueOf(Save_Total_delay_lw)){
                                textView_updown_totalman.setBackgroundResource(R.mipmap.dqaweekly_ic_upgrade);
                            }else if(Double.valueOf(Save_Total_delay) < Double.valueOf(Save_Total_delay_lw)){
                                textView_updown_totalman.setBackgroundResource(R.mipmap.dqaweekly_ic_lowering);
                            }else{
//                                textView_updown_totalman.setHeight(5);
//                                textView_updown_totalman.setBackgroundResource(R.mipmap.dqaweekly_ic_level);
                            }

                            //10月18新增方塊圖形
                            recyclerViewLayoutManager = new GridLayoutManager(mContext, 2);

                            recyclerView.setLayoutManager(recyclerViewLayoutManager);

                            recyclerView_Adapter = new RecyclerViewAdapter(mContext, Delay_Adapter_Item_List);

                            recyclerView.setAdapter(recyclerView_Adapter);

                            recyclerView_Adapter.notifyDataSetChanged();
//                        textView_7850_man.setText(String.valueOf(Save7850_Total_man));
//                        textView_7850_overtime.setText(String.valueOf("N/A"));
//                        textView_7850_overtime_avg.setText("N/A");
//
//                        textView_7860_man.setText(String.valueOf(Save7860_Total_man));
//                        textView_7860_overtime.setText(String.valueOf(Save7860_Total_overtime));
//                        textView_7860_overtime_avg.setText(String.format("%.2f",Save7860_Total_overtime/Double.valueOf(Save7860_Total_man)));
//                        Log.w("7860人數",String.valueOf(Save7860_Total_man));
//                        Log.w("7860加班",String.valueOf(Save7860_Total_overtime));
//
//                        textView_7870_man.setText(String.valueOf(Save7870_Total_man));
//                        textView_7870_overtime.setText(String.valueOf(Save7870_Total_overtime));
//                        textView_7870_overtime_avg.setText(String.format("%.2f",Save7870_Total_overtime/Double.valueOf(Save7870_Total_man)));
//                        Log.w("7870人數",String.valueOf(Save7870_Total_man));
//                        Log.w("7870加班",String.valueOf(Save7870_Total_overtime));
//
//                        textView_7880_man.setText(String.valueOf(Save7880_Total_man));
//                        textView_7880_overtime.setText("N/A");
//                        textView_7880_overtime_avg.setText("N/A");
//                        Log.w("7880人數",String.valueOf(Save7880_Total_man));
//                        Log.w("7880加班",String.valueOf(Save7880_Total_overtime));

                        }else{
                            linear_delay_up.setVisibility(View.GONE);
                            linear_delay_down.setVisibility(View.GONE);
                            empty1.setVisibility(View.VISIBLE);
                            empty2.setVisibility(View.VISIBLE);
                        }
                    }else{

                    }

                } catch (JSONException ex) {

                }
            }
        });
    }

    //抓費用
    public void Find_Total_OverTime_Total(String Week,String Year) {

        //顯示  讀取等待時間Bar
        progressBar.show();

        RequestQueue mQueue = Volley.newRequestQueue(getActivity());

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

                            //textView_TotalHour.setText(TotalHour);

                            String TotalMan = String.valueOf(IssueData.getDouble("TotalMan"));

                            textView_dqa_totalman.setText(String.valueOf(Double.valueOf(TotalMan).intValue()));

                            Double AverageMan = Double.valueOf(IssueData.getDouble("AverageMan"));

                            //textView_AverageMan.setText(String.format("%.2f", AverageMan));
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


    //-------------------------Item-------------------------
    public class Delay_Adapter_Item {

        String TotalMan;

        String F_DeptCode;

        String Delay;

        public Delay_Adapter_Item(String TotalMan,String F_DeptCode,String Delay)
        {
            this.TotalMan = TotalMan;

            this.F_DeptCode = F_DeptCode;

            this.Delay = Delay;

        }


        public String GetTotalMan()
        {
            return this.TotalMan;
        }

        public String GetF_DeptCode()
        {
            return this.F_DeptCode;
        }

        public String GetDelay()
        {
            return this.Delay;
        }

    }

    public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{

        List<Delay_Adapter_Item> values;
        Context context1;

        public RecyclerViewAdapter(Context context2, List<Delay_Adapter_Item> values2){

            values = values2;

            context1 = context2;
        }

        public class ViewHolder extends RecyclerView.ViewHolder{

            public LinearLayout linear_main;

            public TextView textView_dp;

            public TextView textView_dp_total_man;

            public TextView textView_dp_delaytime;

            public TextView textView_updown;

            public LinearLayout linear_item_main;

            public TextView textView_corner;

            public ViewHolder(View v){

                super(v);

                linear_main = (LinearLayout) v.findViewById(R.id.linear_main);

                linear_item_main = (LinearLayout) v.findViewById(R.id.linear_item_main);

                textView_dp = (TextView) v.findViewById(R.id.textView_dp);

                textView_dp_total_man = (TextView) v.findViewById(R.id.textView_dp_total_man);

                textView_dp_delaytime = (TextView) v.findViewById(R.id.textView_dp_delaytime);

                textView_updown = (TextView) v.findViewById(R.id.textView_updown);

                textView_corner = (TextView) v.findViewById(R.id.textView_corner);

            }
        }

        @Override
        public RecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){

            View view1 = LayoutInflater.from(context1).inflate(R.layout.msibook_dqaweekly_resource_recycle_item_delay,parent,false);

            RecyclerViewAdapter.ViewHolder viewHolder1 = new RecyclerViewAdapter.ViewHolder(view1);

            return viewHolder1;
        }

        @Override
        public void onBindViewHolder(final RecyclerViewAdapter.ViewHolder Vholder, final int position){

            Vholder.textView_dp.setText(values.get(position).GetF_DeptCode());

            Vholder.textView_dp_total_man.setText(values.get(position).GetTotalMan());

            Vholder.textView_dp_delaytime.setText(values.get(position).GetDelay());

            if(values.get(position).GetF_DeptCode().contains("7850")||values.get(position).GetF_DeptCode().contains("7880")){
                Vholder.textView_corner.setVisibility(View.INVISIBLE);
                Vholder.linear_main.setEnabled(false);
            }else{
                Vholder.textView_corner.setVisibility(View.VISIBLE);
                Vholder.linear_main.setEnabled(true);
            }

            if(values.get(position).GetDelay().contains("N/A"))
            {
                Vholder.textView_dp_delaytime.setText(values.get(position).GetDelay());
                Vholder.textView_updown.setVisibility(View.INVISIBLE);
            }else {
                if (Double.valueOf(values.get(position).GetDelay()) > Double.valueOf(Delay_Adapter_Item_List_LastWeek.get(position).GetDelay())) {
                    Vholder.textView_updown.setBackgroundResource(R.mipmap.dqaweekly_ic_upgrade);
                } else if (Double.valueOf(values.get(position).GetDelay()) < Double.valueOf(Delay_Adapter_Item_List_LastWeek.get(position).GetDelay())) {
                    Vholder.textView_updown.setBackgroundResource(R.mipmap.dqaweekly_ic_lowering);
                } else {
                    Vholder.textView_updown.setHeight(5);
                    Vholder.textView_updown.setBackgroundResource(R.mipmap.dqaweekly_ic_level);
                }
            }

            Vholder.linear_main.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent();

                    intent.putExtra("Year", SetYear);

                    intent.putExtra("Week", SetWeek);

                    intent.putExtra("Dept_Around", values.get(position).GetF_DeptCode());

                    intent.putExtra("State", "1");

                    intent.setClass(getActivity(), msibook_dqaweekly_resource_delay_night_detail.class);
                    //開啟Activity
                    startActivity(intent);

                    Log.w("點擊","點擊");
                }
            });

        }

        @Override
        public int getItemCount(){

            return values.size();
        }
    }



}
