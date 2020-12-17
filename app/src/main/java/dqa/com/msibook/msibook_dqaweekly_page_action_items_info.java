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
import android.widget.AdapterView;
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
 * {@link msibook_dqaweekly_page_action_items_info.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link msibook_dqaweekly_page_action_items_info#newInstance} factory method to
 * create an instance of this fragment.
 */
public class msibook_dqaweekly_page_action_items_info extends Fragment {
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

    private WeeklyContentSummary_ActionItems_Adapter WeeklyContentSummary_ActionItems_Adapter;
    private List<WeeklyContentSummary_ActionItems_Item> WeeklyContentSummary_ActionItems_Item_List = new ArrayList<WeeklyContentSummary_ActionItems_Item>();

    private String Year;
    private String GetWeek;
    private String GetDeptID;
    private String GetDeptName;
    private String GetWorkID;

    private String Key_Manager;

    private SwipeRefreshLayout mSwipeRefreshLayout; //頁面刷新

    private ListView mListView;

    private LinearLayout linear_add_action_items;

    //讀取摘要內容
    public void Get_Weekly_Content_Summary_ActionItems(String Year,String Unit,String F_DeptID,String F_Type) {

        //顯示  讀取等待時間Bar
        progressBar.show();

        WeeklyContentSummary_ActionItems_Item_List.clear();

        RequestQueue mQueue = Volley.newRequestQueue(getActivity());

        String Path = "http://wtsc.msi.com.tw/IMS/Weekly_Report.asmx/Get_Weekly_Content_Summary?Year=" + Year + "&Unit=" + Unit + "&F_DeptID=" + F_DeptID+ "&F_Type="+F_Type;

        getString(Path, mQueue, new GetServiceData.VolleyCallback() {
            @Override
            public void onSuccess(JSONObject result) {

                try {

                    JSONArray UserArray = new JSONArray(result.getString("Key"));

                    for (int i = 0; i < UserArray.length(); i++) {
                        JSONObject IssueData = UserArray.getJSONObject(i);

                        String F_SeqNo = String.valueOf(IssueData.getInt("F_SeqNo"));

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

                        WeeklyContentSummary_ActionItems_Item_List.add(i,new WeeklyContentSummary_ActionItems_Item(F_SeqNo,String.valueOf(date_formate).replace("T"," "),F_Content));

                    }

                    mListView = (ListView)mView.findViewById(R.id.action_item_listview);

                    WeeklyContentSummary_ActionItems_Adapter = new WeeklyContentSummary_ActionItems_Adapter(mContext,WeeklyContentSummary_ActionItems_Item_List);

                    mListView.setAdapter(WeeklyContentSummary_ActionItems_Adapter);


                    //關閉-讀取等待時間Bar
                    progressBar.dismiss();

                    mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            Intent intent = new Intent();
                            String IntentYear = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
                            intent.putExtra("F_CreateDate", WeeklyContentSummary_ActionItems_Item_List.get(position).GetF_CreateDate());//代日期
                            intent.putExtra("F_Content", WeeklyContentSummary_ActionItems_Item_List.get(position).GetF_Content());//代內容
                            intent.putExtra("F_SeqNo", WeeklyContentSummary_ActionItems_Item_List.get(position).GetF_SeqNo());//代主體序號
                            intent.putExtra("Year", IntentYear);//代年
                            intent.putExtra("Week", GetWeek);//代週
                            intent.putExtra("DeptID", GetDeptID);//代週
                            intent.putExtra("DeptName", GetDeptName);//代週
                            intent.putExtra("WorkID", GetWorkID);//代工號



                            intent.setClass(getActivity(), msibook_dqaweekly_summary_talk_list.class);

                            //開啟Activity
                            startActivity(intent);





                        }
                    });

                }
                catch (JSONException ex) {

                    Log.w("Json",ex.toString());
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


    public msibook_dqaweekly_page_action_items_info() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Page_action_items_info.
     */
    // TODO: Rename and change types and number of parameters
    public static msibook_dqaweekly_page_action_items_info newInstance(String param1, String param2) {
        msibook_dqaweekly_page_action_items_info fragment = new msibook_dqaweekly_page_action_items_info();
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

        mView = inflater.inflate(R.layout.fragment_msibook_dqaweekly_page_action_items_info, container, false);
        mContext = getContext();

        Key_Manager="";

        //讀取時間Bar
        progressBar = new ProgressDialog(this.mContext);
        progressBar.setCancelable(true);
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.setMessage("資料載入中");

        linear_add_action_items = (LinearLayout) mView.findViewById(R.id.linear_add_action_items);

        //抓年 、抓首頁帶給 Main_summar 的資料[週、部門ID]
        Year = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
        GetWeek =  getActivity().getIntent().getStringExtra("Week").replace("週","");//抓週
        GetDeptID =  getActivity().getIntent().getStringExtra("ChoiceDepID");//抓部門ID
        GetDeptName =  getActivity().getIntent().getStringExtra("Title");//抓部門ID
        GetWorkID =  getActivity().getIntent().getStringExtra("WorkID");//抓工號

        switch (Integer.valueOf(GetDeptID)){
            case 21750://二部
                Key_Manager = "10010670";
                Log.w("二部","二部");
                break;
            case 21751://二部一課
                Key_Manager = "10010658";
                Log.w("二部一課","二部一課");
                break;
            case 21752://二部二課
                Key_Manager = "10011924";
                Log.w("二部二課","二部二課");
                break;
            case 21753://二部三課
                Key_Manager = "10010104";
                Log.w("二部三課","二部三課");
                break;
            case 21755://三部
                Key_Manager = "10003275"; // 10010059
                Log.w("三部","三部");
                break;
            case 21756://三部一課
                Key_Manager = "10003130";
                Log.w("三部一課","三部一課");
                break;
            case 21757://三部二課
                Key_Manager = "10015667";
                Log.w("三部二課","三部二課");
                break;
            case 21758://三部三課
                Key_Manager = "10013055";
                Log.w("三部三課","三部三課");
                break;
        }


        Get_Weekly_Content_Summary_ActionItems(Year,GetWeek,GetDeptID,"2");

        linear_add_action_items.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                Intent intent = new Intent(getActivity(), msibook_dqaweekly_summary_add.class);

                intent.putExtra("BigTitle", "Action Item");//代BigTitle
                intent.putExtra("Key_Manager", Key_Manager);//代年
                intent.putExtra("Year", Year);//代年
                intent.putExtra("Week", GetWeek);//代週
                intent.putExtra("DeptID", GetDeptID);//代週
                intent.putExtra("DeptName", GetDeptName);//代週
                intent.putExtra("WorkID", GetWorkID);//代工號
                intent.putExtra("Type", "2");//代Type
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

                Get_Weekly_Content_Summary_ActionItems(Year,GetWeek,GetDeptID,"2");
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

    //----------------Item------------------
    public class WeeklyContentSummary_ActionItems_Item {


        String F_SeqNo;

        String F_CreateDate;//2017-09-07T10:39:34.89

        String F_Content;// TestSummary



        public WeeklyContentSummary_ActionItems_Item(String F_SeqNo,String F_CreateDate, String F_Content)
        {
            this.F_SeqNo = F_SeqNo;

            this.F_CreateDate = F_CreateDate;

            this.F_Content = F_Content;

        }

        public String GetF_SeqNo()
        {
            return this.F_SeqNo;
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

    //---------------Adapter--------------
    public class WeeklyContentSummary_ActionItems_Adapter extends BaseAdapter {

        private LayoutInflater mLayInf;

        private List<WeeklyContentSummary_ActionItems_Item> WeeklyContentSummary_ActionItems_Item_List;

        private Context ProjectContext;

        private String Title;

        public WeeklyContentSummary_ActionItems_Adapter(Context context, List<WeeklyContentSummary_ActionItems_Item> WeeklyContentSummary_ActionItems_Item_List)
        {
            mLayInf = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            ProjectContext = context;

            this.Title = Title;

            this.WeeklyContentSummary_ActionItems_Item_List = WeeklyContentSummary_ActionItems_Item_List;

        }
        @Override
        public int getCount() {
            return WeeklyContentSummary_ActionItems_Item_List.size();
        }

        @Override
        public Object getItem(int position) {
            return WeeklyContentSummary_ActionItems_Item_List.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View v = new View(ProjectContext);

            v = mLayInf.inflate(R.layout.msibook_dqaweekly_weekly_content_summary_actionitems_item, parent, false);

            TextView txt_Content = (TextView) v.findViewById(R.id.txt_Content);
            TextView txt_CreateDate = (TextView) v.findViewById(R.id.txt_CreateDate);

            txt_Content.setText(WeeklyContentSummary_ActionItems_Item_List.get(position).GetF_Content());
            Log.w("conten123213123213213t",WeeklyContentSummary_ActionItems_Item_List.get(position).GetF_Content());

            txt_CreateDate.setText(WeeklyContentSummary_ActionItems_Item_List.get(position).GetF_CreateDate());

            return v;
        }

    }



}
