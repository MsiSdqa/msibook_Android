package dqa.com.msibook;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link msibook_dqaweekly_check_7days_one_day.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link msibook_dqaweekly_check_7days_one_day#newInstance} factory method to
 * create an instance of this fragment.
 */
public class msibook_dqaweekly_check_7days_one_day extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private ProgressDialog progressBar;
    private Context mContext;

    private ListView mListView;
    private Check7day_oneday_adapter mCheck7day_oneday_adapter;
    private List<Check7day_oneday_item> Check7day_oneday_item_List = new ArrayList<Check7day_oneday_item>();

    private View mView;

    private String First_DeptID;
    private String First_Date;
    private String GetTitle;
    private String GetWeek;
    private String GetYear;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public msibook_dqaweekly_check_7days_one_day() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment check_7days_one_day.
     */
    // TODO: Rename and change types and number of parameters
    public static msibook_dqaweekly_check_7days_one_day newInstance(String param1, String param2) {
        msibook_dqaweekly_check_7days_one_day fragment = new msibook_dqaweekly_check_7days_one_day();
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
        mView = inflater.inflate(R.layout.fragment_msibook_dqaweekly_check_7days_one_day, container, false);

        mContext = getContext();

        progressBar = new ProgressDialog(this.mContext);
        progressBar.setCancelable(true);
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.setMessage("資料載入中");

        mListView = (ListView) mView.findViewById(R.id.Lsv_7days);

        //Find_Over_Hour_ByDate("21753","2018/04/10");

        //第一次載入去Activity抓值
        msibook_dqaweekly_check_7day_overtime activity = (msibook_dqaweekly_check_7day_overtime) getActivity();
        String First_DeptID = activity.m2putEtraDepID;
        String First_Date = activity.date_one;

        Find_Over_Hour_ByDate(First_DeptID,First_Date);

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
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
        Activity a;

        if (context instanceof Activity){
            a=(Activity) context;
        }
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

    //抓年
    public void SetFirst_DeptID(String DeptID) {
        First_DeptID = DeptID;
    }

    //抓週
    public void SetFist_Date(String Date) {
        First_Date = Date;
    }

    public void GetTitle(String Title) {
        GetTitle = Title;
    }

    public void GetWeek(String Week) {
        GetWeek = Week;
    }

    public void GetYear(String Year) {
        GetYear = Year;
    }


    public void Find_Over_Hour_ByDate(String DeptID,String Date) {

        //顯示  讀取等待時間Bar
        progressBar.show();

        Check7day_oneday_item_List.clear();

        RequestQueue mQueue = Volley.newRequestQueue(getActivity());

        String Path = "http://wtsc.msi.com.tw/IMS/Weekly_Report.asmx/Find_Over_Hour_ByDate?DeptID=" + DeptID + "&Date=" + Date;

        getString(Path, mQueue, new GetServiceData.VolleyCallback() {
            @Override
            public void onSuccess(JSONObject result) {

                try {

                    JSONArray UserArray = new JSONArray(result.getString("Key"));

                    if (UserArray.length() > 0) {

                        mListView.setVisibility(View.VISIBLE);

                        for (int i = 0; i < UserArray.length(); i++) {

                            JSONObject IssueData = UserArray.getJSONObject(i);

                            String WorkID = String.valueOf(IssueData.getString("WorkID"));

                            String MonthTotal = String.valueOf(IssueData.getDouble("MonthTotal"));

                            String CName = String.valueOf(IssueData.getString("CName"));

                            String Column1 = String.valueOf(IssueData.getDouble("Column1"));

                            Check7day_oneday_item_List.add(i, new Check7day_oneday_item(WorkID, MonthTotal, CName, Column1));

                        }

                        //mOverhourAdapter = new OverhourAdapter(mContext,Overhour_Item_List,MainTitle);
                        mCheck7day_oneday_adapter = new Check7day_oneday_adapter(mContext, Check7day_oneday_item_List);

                        mListView.setAdapter(mCheck7day_oneday_adapter);

                        //關閉-讀取等待時間Bar
                        progressBar.dismiss();

                        //專案細項to 第三頁
                        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                                Intent intent = new Intent();

                                intent.putExtra("m2Week", GetWeek);//給第三頁Week
                                Log.w("GetWeek",String.valueOf(GetWeek));

                                intent.putExtra("m2ChoiceDepID", First_DeptID);//給第三頁部門代號
                                Log.w("First_DeptID",String.valueOf(First_DeptID));

                                intent.putExtra("m2ChoiceYear", GetYear);//給第三頁部門代號
                                Log.w("GetYear",String.valueOf(GetYear));

                                intent.putExtra("m2putTitle", GetTitle);//給第三頁部門名稱
                                Log.w("GetTitle",String.valueOf(GetTitle));

                                intent.putExtra("m2Rowtype","加班");//給第三頁 選擇的 Type  EX 加班OR請假


                                //intent.putExtra("Mantitle",DetailAdapter.Detail_Item_List.get(position).Gettitle());
                                intent.putExtra("Mantitle",Check7day_oneday_item_List.get(position).GetCName());
                                Log.w("Mantitle",String.valueOf(Check7day_oneday_item_List.get(position).GetCName()));
                                intent.putExtra("Mannumber",Check7day_oneday_item_List.get(position).GetWorkID());//給第三頁員工 工號
                                Log.w("Mannumber",String.valueOf(Check7day_oneday_item_List.get(position).GetWorkID()));
                                intent.putExtra("getDate",First_Date);//給第三頁員工 工號
                                intent.setClass(getActivity(), msibook_dqaweekly_main_page3.class);
                                //開啟Activity
                                startActivity(intent);


                            }
                        });
                    }else{
                        //mListView.setVisibility(View.GONE);
                        mListView.setEmptyView(mView.findViewById(R.id.empty));
                        //關閉-讀取等待時間Bar
                        progressBar.dismiss();
                    }


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

    //------------------------------------Item-------------------------------------
//       "CostOrder": 1,
//               "Category": "DT",
//               "Model": "B157H1",
//               "MarketName": "Higos2",
//               "TotalCost": 158550,
//               "F_ModelID": "13045",
//               "DeptName": "7861,7862,7863,7871,7872,7873"
    public class Check7day_oneday_item {

        String WorkID;

        String MonthTotal;

        String CName;

        String Column1;


        public Check7day_oneday_item(String WorkID,String MonthTotal,String CName,String Column1)
        {
            this.WorkID = WorkID;

            this.MonthTotal = MonthTotal;

            this.CName = CName;

            this.Column1 = Column1;

        }

        public String GetWorkID()
        {
            return this.WorkID;
        }

        public String GetMonthTotal()
        {
            return this.MonthTotal;
        }

        public String GetCName()
        {
            return this.CName;
        }

        public String GetColumn1()
        {
            return this.Column1;
        }

    }

    //------------------------------------Adapter-------------------------------------

    public class Check7day_oneday_adapter extends BaseAdapter {

        private LayoutInflater mLayInf;

        private List<Check7day_oneday_item> Check7day_oneday_item_List;

        private Context Check7day_oneday_DataContext;

        private String Title;

        public Check7day_oneday_adapter(Context context, List<Check7day_oneday_item> Check7day_oneday_item_List) {
            mLayInf = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            Check7day_oneday_DataContext = context;

            this.Title = Title;

            this.Check7day_oneday_item_List = Check7day_oneday_item_List;

        }

        @Override
        public int getCount() {
            return Check7day_oneday_item_List.size();
        }

        @Override
        public Object getItem(int position) {
            return Check7day_oneday_item_List.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View v = new View(Check7day_oneday_DataContext);

            v = mLayInf.inflate(R.layout.msibook_dqaweekly_check_7days_one_day_adapter, parent, false);

            TextView textview_CName = (TextView) v.findViewById(R.id.textview_CName);
            TextView textview_Column1 = (TextView) v.findViewById(R.id.textview_Column1);

            textview_CName.setText(Check7day_oneday_item_List.get(position).GetCName());
            textview_Column1.setText(Check7day_oneday_item_List.get(position).GetColumn1());



            return v;
        }
    }


}
