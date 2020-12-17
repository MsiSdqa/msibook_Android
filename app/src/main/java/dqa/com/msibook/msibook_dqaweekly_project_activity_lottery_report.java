package dqa.com.msibook;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link msibook_dqaweekly_project_activity_lottery_report.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link msibook_dqaweekly_project_activity_lottery_report#newInstance} factory method to
 * create an instance of this fragment.
 */
public class msibook_dqaweekly_project_activity_lottery_report extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Context mContext;
    private ProgressDialog progressBar;

    private String SetYear;
    private String SetWeek;


    private LinearLayout linear_history_btn;
    private LinearLayout linear7861;
    private LinearLayout linear7862;
    private LinearLayout linear7863;
    private LinearLayout linear7864;
    private LinearLayout linear7871;
    private LinearLayout linear7872;
    private LinearLayout linear7873;
    private LinearLayout linear7874;


    private LinearLayout linearK33C;
    private LinearLayout linearK33G;
    private LinearLayout linearK33H;
    private LinearLayout linearK33I;
    private LinearLayout linearK33J;

    private TextView textView7861_1;
    private TextView textView7862_1;
    private TextView textView7863_1;
    private TextView textView7871_1;
    private TextView textView7872_1;
    private TextView textView7873_1;

    private TextView textViewK33C_1;
    private TextView textViewK33G_1;
    private TextView textViewK33H_1;
    private TextView textViewK33I_1;
    private TextView textViewK33J_1;


    private TextView textView7861;
    private TextView textView7862;
    private TextView textView7863;
    private TextView textView7864;
    private TextView textView7871;
    private TextView textView7872;
    private TextView textView7873;
    private TextView textView7874;

    private TextView textViewK33C;
    private TextView textViewK33G;
    private TextView textViewK33H;
    private TextView textViewK33I;
    private TextView textViewK33J;

    private Drawable drawable;
    private Drawable drawable2;
    private Drawable drawable3;


    private OnFragmentInteractionListener mListener;

    public msibook_dqaweekly_project_activity_lottery_report() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProjectActivity_lottery_report.
     */
    // TODO: Rename and change types and number of parameters
    public static msibook_dqaweekly_project_activity_lottery_report newInstance(String param1, String param2) {
        msibook_dqaweekly_project_activity_lottery_report fragment = new msibook_dqaweekly_project_activity_lottery_report();
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
        progressBar.setCancelable(true);
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.setMessage("資料載入中");
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mContext = getContext();
        View v = inflater.inflate(R.layout.fragment_msibook_dqaweekly_project_activity_lottery_report, container, false);


        linear_history_btn = (LinearLayout) v.findViewById(R.id.linear_history_btn);

        linear7861 = (LinearLayout) v.findViewById(R.id.linear7861);
        //linear7861.setEnabled(false);
        linear7862 = (LinearLayout) v.findViewById(R.id.linear7862);
        //linear7862.setEnabled(false);
        linear7863 = (LinearLayout) v.findViewById(R.id.linear7863);
        //linear7863.setEnabled(false);
        linear7864 = (LinearLayout) v.findViewById(R.id.linear7864);
        //linear7864.setEnabled(false);
        linear7871 = (LinearLayout) v.findViewById(R.id.linear7871);
        //linear7871.setEnabled(false);
        linear7872 = (LinearLayout) v.findViewById(R.id.linear7872);
        //linear7872.setEnabled(false);
        linear7873 = (LinearLayout) v.findViewById(R.id.linear7873);
        //linear7873.setEnabled(false);
        linear7874 = (LinearLayout) v.findViewById(R.id.linear7874);
        //linear7874.setEnabled(false);

        linearK33C = (LinearLayout) v.findViewById(R.id.linearK33C);
        //linearK33G.setEnabled(false);
        linearK33G = (LinearLayout) v.findViewById(R.id.linearK33G);
        //linearK33G.setEnabled(false);
        linearK33H = (LinearLayout) v.findViewById(R.id.linearK33H);
        //linearK33H.setEnabled(false);
        linearK33I = (LinearLayout) v.findViewById(R.id.linearK33I);
        //linearK33I.setEnabled(false);
        linearK33J = (LinearLayout) v.findViewById(R.id.linearK33J);
        //linearK33J.setEnabled(false);

        textView7861_1 = (TextView) v.findViewById(R.id.textView7861_1);
        textView7862_1 = (TextView) v.findViewById(R.id.textView7862_1);
        textView7863_1 = (TextView) v.findViewById(R.id.textView7863_1);

        textView7871_1 = (TextView) v.findViewById(R.id.textView7871_1);
        textView7872_1 = (TextView) v.findViewById(R.id.textView7872_1);
        textView7873_1 = (TextView) v.findViewById(R.id.textView7873_1);

        textViewK33C_1 = (TextView) v.findViewById(R.id.textViewK33C_1);
        textViewK33G_1 = (TextView) v.findViewById(R.id.textViewK33G_1);
        textViewK33H_1 = (TextView) v.findViewById(R.id.textViewK33H_1);
        textViewK33I_1 = (TextView) v.findViewById(R.id.textViewK33I_1);
        textViewK33J_1 = (TextView) v.findViewById(R.id.textViewK33J_1);

        textView7861 = (TextView) v.findViewById(R.id.textView7861);
        textView7862 = (TextView) v.findViewById(R.id.textView7862);
        textView7863 = (TextView) v.findViewById(R.id.textView7863);
        textView7864 = (TextView) v.findViewById(R.id.textView7864);

        textView7871 = (TextView) v.findViewById(R.id.textView7871);
        textView7872 = (TextView) v.findViewById(R.id.textView7872);
        textView7873 = (TextView) v.findViewById(R.id.textView7873);
        textView7874 = (TextView) v.findViewById(R.id.textView7874);

        textViewK33C = (TextView) v.findViewById(R.id.textViewK33C);
        textViewK33G = (TextView) v.findViewById(R.id.textViewK33G);
        textViewK33H = (TextView) v.findViewById(R.id.textViewK33H);
        textViewK33I = (TextView) v.findViewById(R.id.textViewK33I);
        textViewK33J = (TextView) v.findViewById(R.id.textViewK33J);

        Resources res = this.getResources();
        drawable = res.getDrawable(R.drawable.dqaweekly_linear_lottery_up);

        Resources linear_history_up = this.getResources();
        drawable2 = linear_history_up.getDrawable(R.drawable.dqaweekly_lottery_report_history_down);

        Resources linear_history_down = this.getResources();
        drawable3 = linear_history_down.getDrawable(R.drawable.dqaweekly_lottery_report_history_up);

        linear7861.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Find_Random_RSS(SetWeek,SetYear,"21751",UserData.WorkID);
            }
        });
        linear7862.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Find_Random_RSS(SetWeek,SetYear,"21752",UserData.WorkID);
            }
        });
        linear7863.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Find_Random_RSS(SetWeek,SetYear,"21753",UserData.WorkID);
            }
        });
        linear7864.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Find_Random_RSS(SetWeek,SetYear,"21754",UserData.WorkID);
            }
        });
        linear7871.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Find_Random_RSS(SetWeek,SetYear,"21756",UserData.WorkID);
            }
        });
        linear7872.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Find_Random_RSS(SetWeek,SetYear,"21757",UserData.WorkID);
            }
        });
        linear7873.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Find_Random_RSS(SetWeek,SetYear,"21758",UserData.WorkID);
            }
        });
        linear7874.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Find_Random_RSS(SetWeek,SetYear,"21759",UserData.WorkID);
            }
        });

        linearK33C.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Find_Random_RSS(SetWeek,SetYear,"354",UserData.WorkID);
            }
        });
        linearK33G.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Find_Random_RSS(SetWeek,SetYear,"1461",UserData.WorkID);
            }
        });
        linearK33H.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Find_Random_RSS(SetWeek,SetYear,"1462",UserData.WorkID);
            }
        });
        linearK33I.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Find_Random_RSS(SetWeek,SetYear,"1484",UserData.WorkID);
            }
        });

        linearK33J.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Find_Random_RSS(SetWeek,SetYear,"1483",UserData.WorkID);
            }
        });

        //抽驗歷史紀錄
        linear_history_btn.setOnTouchListener(new View.OnTouchListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        linear_history_btn.setBackground(drawable2);//按下灰色
                        return true;
                    case MotionEvent.ACTION_UP:
                        linear_history_btn.setBackground(drawable3);//放開白色
                        Intent intent = new Intent();

                        //給第二頁Week
                        intent.putExtra("Week", SetWeek);//代 週次到下一頁
                        //給第二頁Year
                        intent.putExtra("Year", SetYear);//代年到下一頁
                        //給第二頁部門代號
                        intent.putExtra("WorkID", String.valueOf(UserData.WorkID));//代部門ID到下一頁 EX: 21751

                        //從MainActivity 到底下部門
                        intent.setClass(getActivity(), msibook_dqaweekly_project_activity_lottery_report_historylist.class);//跳轉頁面至抽驗歷史List
                        //開啟Activity
                        startActivity(intent);
                        return true;
                }

                return false;
            }
        });


        RSS_Dept_List();

        return v;
    }

    //抓年
    public void SetYear(String Year) {
        SetYear = Year;
    }

    //抓週
    public void SetWeek(String Week) {
        SetWeek = Week;
    }

    //抽驗報告
    public void Find_Random_RSS(String Week, String Year, final String DeptID, final String Keyin) {

//        //顯示  讀取等待時間Bar
//        progressBar.show();
//
//        RequestQueue mQueue = Volley.newRequestQueue(getActivity());
//
//        getString("http://wtsc.msi.com.tw/IMS/Weekly_Report.asmx/Find_Random_RSS?Week=" + Week + "&Year=" + Year+ "&DeptID=" + DeptID + "&Keyin=" + Keyin, mQueue, new MainActivity.VolleyCallback() {
//            @Override
//            public void onSuccess(JSONObject result) {
//
//                try {
//
//                    JSONArray UserArray = new JSONArray(result.getString("Key"));
//
//                    if (UserArray.length() > 0)
//                    {
//                        JSONObject IssueData = UserArray.getJSONObject(0);
//
//                        //關閉-讀取等待時間Bar
//                        progressBar.dismiss();
//
//
//
//
//                    }else{
//
//                        Dialog dialog=new AlertDialog.Builder(getActivity())
//                                .setMessage("本週無資料")//设置提示内容
//                                //确定按钮
//                                .setPositiveButton("確定", new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialog, int which) {
//
//                                    }
//                                })
//                                .create();//创建对话框
//                        dialog.show();//显示对话框
//
//                    }
//
//
//                } catch (JSONException ex) {
//
//                }
//
//            }
//        });
        Dialog dialog=new AlertDialog.Builder(getActivity())
                .setTitle("前往抽驗報告結果")
                .setMessage("您正在離開週報APP\n前往抽驗結果")//设置提示内容
                //确定按钮
                .setPositiveButton("立即前往", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

//                                            Intent WebIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(Path));
//                                            startActivity(WebIntent);
                        Intent intent = new Intent();
                        //給第抽驗Week
                        intent.putExtra("Week", SetWeek);//代 週次到下一頁
                        //給第抽驗Year
                        intent.putExtra("Year", SetYear);//代年到下一頁
                        //給第抽驗部門代號
                        intent.putExtra("DepID", DeptID);//代部門ID到下一頁 EX: 21751

                        //給第抽驗部門代號
                        intent.putExtra("Keyin", String.valueOf(UserData.WorkID));//代部門ID到下一頁 EX: 21751
                        Log.w("Keyin",String.valueOf(UserData.WorkID));

                        //從MainActivity 到底下部門
                        intent.setClass(getActivity(), msibook_dqaweekly_project_activity_lottery_report_detail.class);//跳轉頁面至抽驗歷史List
                        //開啟Activity
                        startActivity(intent);
                    }
                })
                //取消按钮
                .setNegativeButton("稍後再說", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.w("稍後再說","稍後再說");
                    }
                })
                .create();//创建对话框
        dialog.show();//显示对话框

    }

    //抽驗報告
    public void RSS_Dept_List() {

        //顯示  讀取等待時間Bar
        progressBar.show();

        RequestQueue mQueue = Volley.newRequestQueue(getActivity());

        getString("http://wtsc.msi.com.tw/IMS/Weekly_Report.asmx/RSS_Dept_List?", mQueue, new GetServiceData.VolleyCallback() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onSuccess(JSONObject result) {

                try {

                    JSONArray UserArray = new JSONArray(result.getString("Key"));

                    if (UserArray.length() > 0){
                        for (int i = 0; i < UserArray.length(); i++) {
                            JSONObject IssueData = UserArray.getJSONObject(i);

                            String Deptcode = IssueData.getString("Deptcode");

                            Integer DeptID = IssueData.getInt("DeptID");

                            String Region = IssueData.getString("Region");

                            Integer ShowDeptRandom = IssueData.getInt("ShowDeptRandom");

                            switch (DeptID) {
                                case 21751:
                                    if(ShowDeptRandom == 1){
                                        linear7861.setEnabled(true);
                                        linear7861.setBackground(drawable);
                                        textView7861_1.setTextColor(Color.parseColor("#212121"));
                                        textView7861.setTextColor(Color.parseColor("#FFFFFF"));
                                        textView7861.setBackgroundColor(Color.parseColor("#d21e25"));
                                    }

                                    break;

                                case 21752:
                                    if(ShowDeptRandom == 1){
                                        linear7862.setEnabled(true);
                                        linear7862.setBackground(drawable);
                                        textView7862_1.setTextColor(Color.parseColor("#212121"));
                                        textView7862.setTextColor(Color.parseColor("#FFFFFF"));
                                        textView7862.setBackgroundColor(Color.parseColor("#d21e25"));
                                    }

                                    break;

                                case 21753:
                                    if(ShowDeptRandom == 1){
                                        linear7863.setEnabled(true);
                                        linear7863.setBackground(drawable);
                                        textView7863_1.setTextColor(Color.parseColor("#212121"));
                                        textView7863.setTextColor(Color.parseColor("#FFFFFF"));
                                        textView7863.setBackgroundColor(Color.parseColor("#d21e25"));
                                    }
                                    break;

                                case 21754:
                                    if(ShowDeptRandom == 1){
                                        linear7864.setEnabled(true);
                                        linear7864.setBackground(drawable);
                                        textView7864.setTextColor(Color.parseColor("#FFFFFF"));
                                        textView7864.setBackgroundColor(Color.parseColor("#d21e25"));
                                    }
                                    break;

                                case 21756:
                                    if(ShowDeptRandom == 1){
                                        linear7871.setEnabled(true);
                                        linear7871.setBackground(drawable);
                                        textView7871_1.setTextColor(Color.parseColor("#212121"));
                                        textView7871.setTextColor(Color.parseColor("#FFFFFF"));
                                        textView7871.setBackgroundColor(Color.parseColor("#d21e25"));
                                    }
                                    break;

                                case 21757:
                                    if(ShowDeptRandom == 1){
                                        linear7872.setEnabled(true);
                                        linear7872.setBackground(drawable);
                                        textView7872_1.setTextColor(Color.parseColor("#212121"));
                                        textView7872.setTextColor(Color.parseColor("#FFFFFF"));
                                        textView7872.setBackgroundColor(Color.parseColor("#d21e25"));
                                    }
                                    break;


                                case 21758:
                                    if(ShowDeptRandom == 1){
                                        linear7873.setEnabled(true);
                                        linear7873.setBackground(drawable);
                                        textView7873_1.setTextColor(Color.parseColor("#212121"));
                                        textView7873.setTextColor(Color.parseColor("#FFFFFF"));
                                        textView7873.setBackgroundColor(Color.parseColor("#d21e25"));
                                    }
                                    break;
                                case 21759:
                                    if(ShowDeptRandom == 1){
                                        linear7874.setEnabled(true);
                                        linear7874.setBackground(drawable);
                                        textView7874.setTextColor(Color.parseColor("#FFFFFF"));
                                        textView7874.setBackgroundColor(Color.parseColor("#d21e25"));
                                    }
                                    break;

                                case 354:
                                    if(ShowDeptRandom == 1){
                                        linearK33C.setEnabled(true);
                                        linearK33C.setBackground(drawable);
                                        textViewK33C_1.setTextColor(Color.parseColor("#212121"));
                                        textViewK33C.setTextColor(Color.parseColor("#FFFFFF"));
                                        textViewK33C.setBackgroundColor(Color.parseColor("#d21e25"));
                                    }
                                    break;

                                case 1461:
                                    if(ShowDeptRandom == 1){
                                        linearK33G.setEnabled(true);
                                        linearK33G.setBackground(drawable);
                                        textViewK33G_1.setTextColor(Color.parseColor("#212121"));
                                        textViewK33G.setTextColor(Color.parseColor("#FFFFFF"));
                                        textViewK33G.setBackgroundColor(Color.parseColor("#d21e25"));
                                    }
                                    break;

                                case 1462:
                                    if(ShowDeptRandom == 1){
                                        linearK33H.setEnabled(true);
                                        linearK33H.setBackground(drawable);
                                        textViewK33H_1.setTextColor(Color.parseColor("#212121"));
                                        textViewK33H.setTextColor(Color.parseColor("#FFFFFF"));
                                        textViewK33H.setBackgroundColor(Color.parseColor("#d21e25"));
                                    }
                                    break;

                                case 1484:
                                    if(ShowDeptRandom == 1){
                                        linearK33I.setEnabled(true);
                                        linearK33I.setBackground(drawable);
                                        textViewK33I_1.setTextColor(Color.parseColor("#212121"));
                                        textViewK33I.setTextColor(Color.parseColor("#FFFFFF"));
                                        textViewK33I.setBackgroundColor(Color.parseColor("#d21e25"));
                                    }
                                    break;

                                case 1483:
                                    if(ShowDeptRandom == 1){
                                        linearK33J.setEnabled(true);
                                        linearK33J.setBackground(drawable);
                                        textViewK33J_1.setTextColor(Color.parseColor("#212121"));
                                        textViewK33J.setTextColor(Color.parseColor("#FFFFFF"));
                                        textViewK33J.setBackgroundColor(Color.parseColor("#d21e25"));
                                    }
                                    break;
                            }
                        }

                        //關閉-讀取等待時間Bar
                        progressBar.dismiss();

                    }else{

                    }


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
