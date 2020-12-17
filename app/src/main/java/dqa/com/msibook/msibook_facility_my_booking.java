package dqa.com.msibook;

import android.app.Activity;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
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
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

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
 * {@link msibook_facility_my_booking.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link msibook_facility_my_booking#newInstance} factory method to
 * create an instance of this fragment.
 */
public class msibook_facility_my_booking extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String message_title;
    private String message_date;

    private ListView mListView;

    private msibook_mybooking_adapter My_booking_Adapter;

    private List<My_Booking_item> My_Booking_item_List = new ArrayList<My_Booking_item>();
    private ProgressDialog progressBar;
    private Context mContext;

    private SwipeRefreshLayout mSwipeRefreshLayout; //頁面刷新

    private String Set_Keyin;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public msibook_facility_my_booking() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment msibook_facility_my_booking.
     */
    // TODO: Rename and change types and number of parameters
    public static msibook_facility_my_booking newInstance(String param1, String param2) {
        msibook_facility_my_booking fragment = new msibook_facility_my_booking();
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
        View v = inflater.inflate(R.layout.fragment_msibook_facility_my_booking, container, false);
        mContext = getContext();

        progressBar = new ProgressDialog(this.mContext);
        progressBar.setCancelable(true);
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.setMessage("資料載入中");

        mListView = (ListView) v.findViewById(R.id.Lsv_mybooking);

        mSwipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.refresh_layout);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                mSwipeRefreshLayout.setRefreshing(false);

                Find_Fac_My_Schedule_List(Set_Keyin);
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

    //重整的工號
    public void Set_Keyin(String Keyin) {
        Set_Keyin = Keyin;
    }


    public void Find_Fac_My_Schedule_List(String F_Keyin) {

        //顯示  讀取等待時間Bar
        progressBar.show();

        My_Booking_item_List.clear();

        RequestQueue mQueue = Volley.newRequestQueue(getActivity());

        String Path = GetServiceData.ServicePath + "/Find_Fac_My_Schedule_List?F_Keyin=" + F_Keyin;

        GetServiceData.getString(Path, mQueue, new GetServiceData.VolleyCallback() {
            @Override
            public void onSuccess(JSONObject result) {

                try {

                    JSONArray UserArray = new JSONArray(result.getString("Key"));

                    for (int i = 0; i < UserArray.length(); i++) {

                        JSONObject IssueData = UserArray.getJSONObject(i);

                        String F_AssetNo = String.valueOf(IssueData.getString("F_AssetNo"));

                        String F_Type = String.valueOf(IssueData.getString("F_Type"));

                        String F_Location = String.valueOf(IssueData.getString("F_Location"));

                        String F_Facility = String.valueOf(IssueData.getString("F_Facility"));

                        String F_Model = String.valueOf(IssueData.getString("F_Model"));

                        String F_Factory = String.valueOf(IssueData.getString("F_Factory"));

                        String Dept = String.valueOf(IssueData.getString("Dept"));

                        String TEL = String.valueOf(IssueData.getString("TEL"));

                        String EMail = String.valueOf(IssueData.getString("EMail"));

                        String HourCost = String.valueOf(IssueData.getDouble("HourCost"));

                        String F_Standard = String.valueOf(IssueData.getString("F_Standard"));

                        String F_Status = String.valueOf(IssueData.getString("F_Status"));

                        String F_Is_Restrict = String.valueOf(IssueData.getString("F_Is_Restrict"));

                        String Using = String.valueOf(IssueData.getInt("Using"));

                        String IMG = String.valueOf(IssueData.getString("IMG"));

                        String F_StartDate = String.valueOf(IssueData.getString("F_StartDate"));

                        String F_EndDate = String.valueOf(IssueData.getString("F_EndDate"));

                        String F_SeqNo = String.valueOf(IssueData.getInt("F_SeqNo"));

                        String F_Stat = String.valueOf(IssueData.getString("F_Stat"));

                        String F_CreateDate = String.valueOf(IssueData.getString("F_CreateDate"));

                        String ShowData = String.valueOf(IssueData.getInt("ShowData"));

                        My_Booking_item_List.add(i,new My_Booking_item(F_CreateDate, F_Facility, F_AssetNo,  F_StartDate,  F_EndDate,  F_SeqNo,F_Stat,ShowData));

                    }
                    My_booking_Adapter = new msibook_mybooking_adapter(mContext,My_Booking_item_List);

                    mListView.setAdapter(My_booking_Adapter);

                    //關閉-讀取等待時間Bar
                    progressBar.dismiss();

//                    //專案細項to 第三頁
//                    mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                        @Override
//                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//                            Intent intent = new Intent();
//
//
//                            //開啟Activity
//                            startActivity(intent);
//
//                        }
//                    });
                }
                catch (JSONException ex) {
                    Log.w("Json",ex.toString());
                }
            }
        });
    }

    public void Cancel_Fac_Schedule(String F_SeqNo) {

        //顯示  讀取等待時間Bar
        progressBar.show();

        My_Booking_item_List.clear();

        RequestQueue mQueue = Volley.newRequestQueue(getActivity());

        String Path = GetServiceData.ServicePath + "/Cancel_Fac_Schedule?F_SeqNo=" + F_SeqNo;

        GetServiceData.getString(Path, mQueue, new GetServiceData.VolleyCallback() {
            @Override
            public void onSuccess(JSONObject result) {

            }
        });
    }


    public class msibook_mybooking_adapter extends BaseAdapter {

        private LayoutInflater mLayInf;

        private List<My_Booking_item> My_Booking_item_List = new ArrayList<My_Booking_item>();

        private Context ProjectContext;


        public msibook_mybooking_adapter(Context context, List<My_Booking_item> My_Booking_item) {
            mLayInf = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            ProjectContext = context;

            this.My_Booking_item_List = My_Booking_item;

        }

        @Override
        public int getCount() {
            return My_Booking_item_List.size();
        }

        @Override
        public Object getItem(int position) {
            return My_Booking_item_List.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            View v = new View(ProjectContext);

            v = mLayInf.inflate(R.layout.msibook_facility_my_booking_item, parent, false);

            final TextView txt_fac_cancel = (TextView) v.findViewById(R.id.txt_fac_cancel);


            TextView textView_momth = (TextView) v.findViewById(R.id.textView_momth);//月
            TextView textView_date = (TextView) v.findViewById(R.id.textView_date);//日
            TextView txt_fac_name = (TextView) v.findViewById(R.id.txt_fac_name);//機台名稱
            TextView txt_AssetSN = (TextView) v.findViewById(R.id.txt_AssetSN);//產編
            TextView txt_fac_Date_to_Date = (TextView) v.findViewById(R.id.txt_fac_Date_to_Date);//預約時間

            String dateString = String.valueOf(My_Booking_item_List.get(position).GetF_CreateDate().replace("T"," ")); //文字
            String Start_dateString = String.valueOf(My_Booking_item_List.get(position).GetF_StartDate().replace("T"," ")); //文字
            String F_End_dateString = String.valueOf(My_Booking_item_List.get(position).GetF_EndDate().replace("T"," ")); //文字

            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//  設定 文字轉日期
            SimpleDateFormat change_sdf1 = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");//  設定 文字轉日期
            SimpleDateFormat sdf2 = new SimpleDateFormat("M"); //設定 轉完日期後只留月份
            SimpleDateFormat sdf3 = new SimpleDateFormat("dd"); //設定 轉完日期後只留日

            try {
                Date date1 = sdf1.parse(dateString);
                Date date2 = sdf1.parse(dateString);
                Date Star_date = sdf1.parse(Start_dateString);
                Date End_date = sdf1.parse(F_End_dateString);
                String formatDate1 = sdf2.format(date1);
                String formatDate2 = sdf3.format(date2);
                String format_Start_Date = change_sdf1.format(Star_date);
                String format_End_Date = change_sdf1.format(End_date);
                textView_momth.setText(String.valueOf(formatDate1)+"月");
                textView_date.setText(String.valueOf(formatDate2));

                txt_fac_Date_to_Date.setText("預約時間  "+String.valueOf(format_Start_Date.substring(0, 10)) + " - " + String.valueOf(format_End_Date.substring(0, 10)));
                message_date = "預約時間  "+String.valueOf(format_Start_Date.substring(0, 10)) + " - " + String.valueOf(format_End_Date.substring(0, 10));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            txt_fac_name.setText(My_Booking_item_List.get(position).GetF_Facility());
            message_title = My_Booking_item_List.get(position).GetF_Facility();
            txt_AssetSN.setText("財編 : " + My_Booking_item_List.get(position).GetF_AssetNo());


            //textView_date.setText(String.format("%s: %s",Set_date,sdf2.format(date)));

            //判斷是否過期
            if(Integer.valueOf(My_Booking_item_List.get(position).GetShowData()) == 0){
                txt_fac_cancel.setVisibility(View.INVISIBLE);
            }

            //判斷是否取消
            if(Integer.valueOf(My_Booking_item_List.get(position).GetF_Stat()) == 0){
                txt_fac_cancel.setText("已取消");
                txt_fac_cancel.setEnabled(false);
                txt_fac_cancel.setBackground(null);
            }

            //取消
            txt_fac_cancel.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            txt_fac_cancel.setBackgroundResource(R.drawable.facility_booking_btn_down);
                            return true;
                        case MotionEvent.ACTION_UP:
                            txt_fac_cancel.setBackgroundResource(R.drawable.facility_booking_btn_up);
                            //Dialog dialog=new android.support.v7.app.AlertDialog.Builder(mContext)
                            Dialog dialog=new android.support.v7.app.AlertDialog.Builder(mContext)
//                                    .setTitle("")//设置提示内容
                                    .setMessage("是否取消該筆預約\n\n"+message_title+"\n\n"+message_date)//设置提示内容
                                    //确定按钮
                                    .setPositiveButton("確定取消", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                            Cancel_Fac_Schedule(My_Booking_item_List.get(position).GetF_SeqNo());

                                            Toast.makeText(getActivity(), "已取消", Toast.LENGTH_SHORT).show();

                                            Find_Fac_My_Schedule_List(Set_Keyin);


                                            final int notifyID = 1; // 通知的識別號碼
                                            final boolean autoCancel = true; // 點擊通知後是否要自動移除掉通知
                                            final int requestCode = notifyID; // PendingIntent的Request Code
                                            final Intent intent_notification = new Intent(getActivity().getApplicationContext(), msibook_welcome.class); // 開啟另一個Activity的Intent
                                            final int flags = PendingIntent.FLAG_UPDATE_CURRENT; // ONE_SHOT：PendingIntent只使用一次；CANCEL_CURRENT：PendingIntent執行前會先結束掉之前的；NO_CREATE：沿用先前的PendingIntent，不建立新的PendingIntent；UPDATE_CURRENT：更新先前PendingIntent所帶的額外資料，並繼續沿用
                                            final TaskStackBuilder stackBuilder = TaskStackBuilder.create(getActivity().getApplicationContext()); // 建立TaskStackBuilder
                                            stackBuilder.addParentStack(msibook_welcome.class); // 加入目前要啟動的Activity，這個方法會將這個Activity的所有上層的Activity(Parents)都加到堆疊中
                                            stackBuilder.addNextIntent(intent_notification); // 加入啟動Activity的Intent
                                            final PendingIntent pendingIntent = stackBuilder.getPendingIntent(requestCode, flags); // 取得PendingIntent
                                            final NotificationManager notificationManager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE); // 取得系統的通知服務
                                            final Notification notification = new Notification.Builder(getActivity().getApplicationContext()).setSmallIcon(R.mipmap.msibook_ic_msibook_applogo).setContentTitle("實驗室取消通知").setContentText("您已取消一筆預約").setContentIntent(pendingIntent).setAutoCancel(autoCancel).build(); // 建立通知
                                            notificationManager.notify(notifyID, notification); // 發送通知

                                        }
                                    })
                                    .setNeutralButton("返回", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                        }
                                    })
                                    .create();//创建对话框
                            dialog.show();//显示对话框

                            return true;
                    }

                    return false;
                }
            });

            return v;
        }
    }

    public class My_Booking_item {

        String F_CreateDate;
        String F_Facility;
        String F_AssetNo;
        String F_StartDate;
        String F_EndDate;
        String F_SeqNo;
        String F_Stat;
        String ShowData;


        public My_Booking_item(String F_CreateDate,String F_Facility,String F_AssetNo,String F_StartDate, String F_EndDate, String F_SeqNo, String F_Stat, String ShowData)
        {
            this.F_CreateDate = F_CreateDate;
            this.F_Facility = F_Facility;
            this.F_AssetNo = F_AssetNo;
            this.F_StartDate = F_StartDate;
            this.F_EndDate = F_EndDate;
            this.F_SeqNo = F_SeqNo;
            this.F_Stat = F_Stat;
            this.ShowData = ShowData;

        }

        public String GetF_CreateDate()
        {
            return this.F_CreateDate;
        }

        public String GetF_Facility()
        {
            return this.F_Facility;
        }

        public String GetF_AssetNo()
        {
            return this.F_AssetNo;
        }

        public String GetF_StartDate()
        {
            return this.F_StartDate;
        }

        public String GetF_EndDate()
        {
            return this.F_EndDate;
        }

        public String GetF_SeqNo()
        {
            return this.F_SeqNo;
        }

        public String GetF_Stat()
        {
            return this.F_Stat;
        }

        public String GetShowData()
        {
            return this.ShowData;
        }


    }



}
