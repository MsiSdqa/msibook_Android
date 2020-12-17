package dqa.com.msibook;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link msibook_facility_system_notify.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link msibook_facility_system_notify#newInstance} factory method to
 * create an instance of this fragment.
 */
public class msibook_facility_system_notify extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private ListView mListView;

    private msibook_system_notify_adapter System_notify_Adapter;

    private List<System_notify_item> System_notify_item_List = new ArrayList<System_notify_item>();
    private ProgressDialog progressBar;
    private Context mContext;

    private SwipeRefreshLayout mSwipeRefreshLayout; //頁面刷新

    private OnFragmentInteractionListener mListener;

    public msibook_facility_system_notify() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment msibook_facility_system_notify.
     */
    // TODO: Rename and change types and number of parameters
    public static msibook_facility_system_notify newInstance(String param1, String param2) {
        msibook_facility_system_notify fragment = new msibook_facility_system_notify();
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
        View v = inflater.inflate(R.layout.fragment_msibook_facility_system_notify, container, false);
        mContext = getContext();

        progressBar = new ProgressDialog(this.mContext);
        progressBar.setCancelable(true);
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.setMessage("資料載入中");

        mListView = (ListView) v.findViewById(R.id.Lsv_sys_notify);

        mSwipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.refresh_layout);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                mSwipeRefreshLayout.setRefreshing(false);

                Find_System_notify();
            }
        });

        return v;
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

    //系統通知
    public void Find_System_notify() {

        //顯示  讀取等待時間Bar
        progressBar.show();
        System_notify_item_List.clear();

        for(int i = 0; i < 2; i++){
            switch (i)
            {
                case 0:
                    System_notify_item_List.add(i,new System_notify_item("System", "2018-02-08 AM8:15", "標題:台北廠區電力保養 \n日期: 5月14日(六)\n時間:08:30 ~ 17:30 \n地點: 一廠 三廠"));
                    break;
                case 1:
                    System_notify_item_List.add(i,new System_notify_item("System", "2018-05-08 AM10:15", "標題:台北廠區消毒作業 \n日期: 6月10日(日)\n時間:08:30 ~ 17:30 \n地點: 一廠 三廠"));
                    break;
            }

        }

        System_notify_Adapter = new msibook_system_notify_adapter(mContext,System_notify_item_List);

        mListView.setAdapter(System_notify_Adapter);

        //關閉-讀取等待時間Bar
        progressBar.dismiss();

//        RequestQueue mQueue = Volley.newRequestQueue(getActivity());
//        String Path = GetServiceData.ServicePath + "/Find_Fac_My_Schedule_List?F_Keyin=" + F_Keyin;
//        GetServiceData.getString(Path, mQueue, new GetServiceData.VolleyCallback() {
//            @Override
//            public void onSuccess(JSONObject result) {
//
//                try {
//                    JSONArray UserArray = new JSONArray(result.getString("Key"));
//
//                    for (int i = 0; i < UserArray.length(); i++) {
//
//                        JSONObject IssueData = UserArray.getJSONObject(i);
//
//                        String F_AssetNo = String.valueOf(IssueData.getString("F_AssetNo"));
//
//                        String F_Type = String.valueOf(IssueData.getString("F_Type"));
//
//                        String F_Location = String.valueOf(IssueData.getString("F_Location"));
//
//
//                        My_Booking_item_List.add(i,new msibook_facility_my_booking.My_Booking_item(F_CreateDate, F_Facility, F_AssetNo,  F_StartDate,  F_EndDate,  F_SeqNo,F_Stat,ShowData));
//
//                    }
//                    My_booking_Adapter = new msibook_facility_my_booking.msibook_mybooking_adapter(mContext,My_Booking_item_List);
//
//                    mListView.setAdapter(My_booking_Adapter);
//
//                    //關閉-讀取等待時間Bar
//                    progressBar.dismiss();
//                }
//                catch (JSONException ex) {
//                    Log.w("Json",ex.toString());
//                }
//            }
//        });
    }


    public class msibook_system_notify_adapter extends BaseAdapter {

        private LayoutInflater mLayInf;

        private List<System_notify_item> System_notify_item_List = new ArrayList<System_notify_item>();

        private Context ProjectContext;


        public msibook_system_notify_adapter(Context context, List<System_notify_item> System_notify_item) {
            mLayInf = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            ProjectContext = context;

            this.System_notify_item_List = System_notify_item;

        }

        @Override
        public int getCount() {
            return System_notify_item_List.size();
        }

        @Override
        public Object getItem(int position) {
            return System_notify_item_List.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            View v = new View(ProjectContext);

            v = mLayInf.inflate(R.layout.msibook_facility_system_notify_item, parent, false);


            //ImageView imageView_title = (ImageView) v.findViewById(R.id.imageView_title);//月
            TextView textView_name = (TextView) v.findViewById(R.id.textView_name);//日
            TextView textView_time = (TextView) v.findViewById(R.id.textView_time);//機台名稱
            TextView textView_subject = (TextView) v.findViewById(R.id.textView_subject);//產編

            textView_name.setText(System_notify_item_List.get(position).GetF_name());
            textView_time.setText(System_notify_item_List.get(position).GetF_time());
            textView_subject.setText(System_notify_item_List.get(position).GetF_subject());




            return v;
        }
    }

    public class System_notify_item {

        String F_name;
        String F_time;
        String F_subject;


        public System_notify_item(String F_name,String F_time,String F_subject)
        {
            this.F_name = F_name;
            this.F_time = F_time;
            this.F_subject = F_subject;

        }

        public String GetF_name()
        {
            return this.F_name;
        }

        public String GetF_time()
        {
            return this.F_time;
        }

        public String GetF_subject()
        {
            return this.F_subject;
        }


    }



}
