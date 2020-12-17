package dqa.com.msibook;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link msibook_dqaweekly_page_summary_info.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link msibook_dqaweekly_page_summary_info#newInstance} factory method to
 * create an instance of this fragment.
 */
public class msibook_dqaweekly_page_summary_info extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private LayoutInflater minflater;

    private View mView;

    private Context mContext;
    private ProgressDialog progressBar;

    private WeeklyContentSummaryAdapter WeeklyContentSummaryAdapter;
    private List<Weekly_Content_Summary_Item> Weekly_Content_Summary_Item_List = new ArrayList<Weekly_Content_Summary_Item>();

    private SwipeRefreshLayout mSwipeRefreshLayout; //頁面刷新

    private ListView mListView;

    private String Year;
    private String GetWeek;
    private String GetDeptID;
    private String GetWorkID;

    private LinearLayout linear_add_summary;


    //讀取摘要內容
    public void Get_Weekly_Content_Summary(String Year,String Unit,String F_DeptID,String F_Type) {

        //顯示  讀取等待時間Bar
        progressBar.show();

        Weekly_Content_Summary_Item_List.clear();

        RequestQueue mQueue = Volley.newRequestQueue(getActivity());

        String Path = "http://wtsc.msi.com.tw/IMS/Weekly_Report.asmx/Get_Weekly_Content_Summary?Year=" + Year + "&Unit=" + Unit + "&F_DeptID=" + F_DeptID+ "&F_Type="+F_Type;

        GetServiceData.getString(Path, mQueue, new GetServiceData.VolleyCallback() {
            @Override
            public void onSuccess(JSONObject result) {

                try {

                    JSONArray UserArray = new JSONArray(result.getString("Key"));

                    for (int i = 0; i < UserArray.length(); i++) {
                        JSONObject IssueData = UserArray.getJSONObject(i);

                        String F_CreateDate = String.valueOf(IssueData.getString("F_CreateDate"));

                        String F_Content = String.valueOf(IssueData.getString("F_Content"));

                        SimpleDateFormat dateFormatParse = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

                        F_CreateDate = F_CreateDate.substring(0,18);//取毫秒前方的字串

                        Date dateString = null;
                        String date_formate=null;
                        try {
                            dateString = dateFormatParse.parse(F_CreateDate);//文字轉日期
                            date_formate = dateFormatParse.format(dateString);//日期 Fromat 自訂格式 成字串

                        } catch (ParseException e) {
                            e.printStackTrace();
                            Log.w("eeeeee",e.getMessage());
                        }

                        Weekly_Content_Summary_Item_List.add(i,new Weekly_Content_Summary_Item(String.valueOf(date_formate).replace("T"," "),F_Content));

                    }

                    mListView = (ListView)mView.findViewById(R.id.summary_listview);

                    WeeklyContentSummaryAdapter = new WeeklyContentSummaryAdapter(mContext,Weekly_Content_Summary_Item_List);

                    mListView.setAdapter(WeeklyContentSummaryAdapter);


                    //關閉-讀取等待時間Bar
                    progressBar.dismiss();

                }
                catch (JSONException ex) {

                    Log.w("Json",ex.toString());
                }

            }
        });


    }

    public msibook_dqaweekly_page_summary_info() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Page_summary_info.
     */
    // TODO: Rename and change types and number of parameters
    public static msibook_dqaweekly_page_summary_info newInstance(String param1, String param2) {
        msibook_dqaweekly_page_summary_info fragment = new msibook_dqaweekly_page_summary_info();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        minflater = inflater;

        mView = inflater.inflate(R.layout.fragment_msibook_dqaweekly_page_summary_info, container, false);
        mContext = getContext();

        //讀取時間Bar
        progressBar = new ProgressDialog(this.mContext);
        progressBar.setCancelable(true);
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.setMessage("資料載入中");

        linear_add_summary = (LinearLayout) mView.findViewById(R.id.linear_add_summary);

        //抓年 、抓首頁帶給 Main_summar 的資料[週、部門ID]
        Year = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
        GetWeek =  getActivity().getIntent().getStringExtra("Week").replace("週","");//抓週
        GetDeptID =  getActivity().getIntent().getStringExtra("ChoiceDepID");//抓部門ID
        GetWorkID =  getActivity().getIntent().getStringExtra("WorkID");//抓工號

        Get_Weekly_Content_Summary(Year,GetWeek,GetDeptID,"1");

        linear_add_summary.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                Intent intent = new Intent(getActivity(), msibook_dqaweekly_summary_add.class);

                intent.putExtra("BigTitle", "摘要");//代BigTitle
                intent.putExtra("Year", Year);//代年
                intent.putExtra("Week", GetWeek);//代週
                intent.putExtra("DeptID", GetDeptID);//代週
                intent.putExtra("WorkID", GetWorkID);//代週
                intent.putExtra("Type", "1");//代Type
                //開啟Activity
                startActivityForResult(intent, 1);

                return false;
            }
        });

        mSwipeRefreshLayout = (SwipeRefreshLayout) mView.findViewById(R.id.refresh_layout);

        //lsv_main = (ListView) mSwipeRefreshLayout.findViewById(R.id.listView);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                mSwipeRefreshLayout.setRefreshing(false);

                Get_Weekly_Content_Summary(Year,GetWeek,GetDeptID,"1");
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

    @Override
    public void onResume(){
        super.onResume();

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


    //-----------------------Item--------------------------
    public class Weekly_Content_Summary_Item {



        String F_CreateDate;//2017-09-07T10:39:34.89

        String F_Content;// TestSummary



        public Weekly_Content_Summary_Item(String F_CreateDate, String F_Content)
        {
            this.F_CreateDate = F_CreateDate;

            this.F_Content = F_Content;

        }

        public String GetF_CreateDate()
        {
            return this.F_CreateDate;
        }

        public String GetF_Content()
        {
            return this.F_Content;
        }


    }





    //---------------------Adapter----------------------
    public class WeeklyContentSummaryAdapter extends BaseAdapter {

        private LayoutInflater mLayInf;

        private List<Weekly_Content_Summary_Item> Find_Weekly_Content_Summary_Item_list;

        private Context ProjectContext;

        private String Title;

        public WeeklyContentSummaryAdapter(Context context, List<Weekly_Content_Summary_Item> Find_Weekly_Content_Summary_Item_list)
        {
            mLayInf = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            ProjectContext = context;

            this.Title = Title;

            this.Find_Weekly_Content_Summary_Item_list = Find_Weekly_Content_Summary_Item_list;

        }
        @Override
        public int getCount() {
            return Find_Weekly_Content_Summary_Item_list.size();
        }

        @Override
        public Object getItem(int position) {
            return Find_Weekly_Content_Summary_Item_list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View v = new View(ProjectContext);

            v = mLayInf.inflate(R.layout.msibook_dqaweekly_weekly_content_summary_item, parent, false);

            TextView txt_Content = (TextView) v.findViewById(R.id.txt_Content);
            TextView txt_CreateDate = (TextView) v.findViewById(R.id.txt_CreateDate);

            txt_Content.setText(Find_Weekly_Content_Summary_Item_list.get(position).GetF_Content());
            Log.w("conten123213123213213t",Find_Weekly_Content_Summary_Item_list.get(position).GetF_Content());

            txt_CreateDate.setText(Find_Weekly_Content_Summary_Item_list.get(position).GetF_CreateDate());

            return v;
        }

    }




}
