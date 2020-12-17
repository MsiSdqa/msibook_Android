package dqa.com.msibook;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link msibook_facility_machine.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link msibook_facility_machine#newInstance} factory method to
 * create an instance of this fragment.
 */
public class msibook_facility_machine extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private TabLayout mTablayout;
    private ListView mListView;
    private msibook_facility_adapter Fac_List_Adapter;
    private List<String> ArrayF_ID = new ArrayList<String>();//存 F_ID 來對應Tabs
    private List<msibook_facility_item> msibook_facility_item_List = new ArrayList<msibook_facility_item>();
    private ProgressDialog progressBar;
    private Context mContext;

    private String Set_Applier;
    private String Set_ApplierSDate;
    private String Set_ApplierEDate;

    public String Location = "0";

    public String SetLocation;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public msibook_facility_machine() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment msibook_facility_machine.
     */
    // TODO: Rename and change types and number of parameters
    public static msibook_facility_machine newInstance(String param1, String param2) {
        msibook_facility_machine fragment = new msibook_facility_machine();
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
        View v = inflater.inflate(R.layout.fragment_msibook_facility_machine, container, false);
        mContext = getContext();

        progressBar = new ProgressDialog(this.mContext);
        progressBar.setCancelable(true);
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.setMessage("資料載入中");

        mListView = (ListView) v.findViewById(R.id.Lsv_Facility);
        mTablayout = (TabLayout) v.findViewById(R.id.tabs);


//        Find_Fac_Type_List(Location);
//        Find_Fac_List(Location,"0");

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

    public void Find_Fac_Type_List(String Region) {

        //顯示  讀取等待時間Bar
        progressBar.show();

        ArrayF_ID.clear();//存 F_ID 來對應Tabs

        mTablayout.removeAllTabs();

        RequestQueue mQueue = Volley.newRequestQueue(getActivity());

        HTTPSTrustManager.allowAllSSL();//信任所有证书，信任憑證
        String Path = GetServiceData.ServicePath + "/Find_Fac_Type_List?Region=" + Region;

        GetServiceData.getString(Path, mQueue, new GetServiceData.VolleyCallback() {
            @Override
            public void onSuccess(JSONObject result) {

                try {

                    JSONArray UserArray = new JSONArray(result.getString("Key"));

                    for (int i = 0; i < UserArray.length(); i++) {

                        JSONObject IssueData = UserArray.getJSONObject(i);

                        String F_ID = String.valueOf(IssueData.getString("F_ID"));

                        String F_Name = String.valueOf(IssueData.getString("F_Name"));

                        ArrayF_ID.add(i,F_ID);

                        mTablayout.addTab(mTablayout.newTab().setText(F_Name));
                    }

                }
                catch (JSONException ex) {
                    Log.w("Json",ex.toString());
                }
            }
        });


        mTablayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                int Position = tab.getPosition();

                Find_Fac_List(Location,ArrayF_ID.get(Position));
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                int Position = tab.getPosition();

                Find_Fac_List(Location,ArrayF_ID.get(Position));
            }
        });


    }

    public void Find_Fac_List(String Region, String Type) {

        //顯示  讀取等待時間Bar
        progressBar.show();

        msibook_facility_item_List.clear();

        RequestQueue mQueue = Volley.newRequestQueue(getActivity());
        HTTPSTrustManager.allowAllSSL();//信任所有证书，信任憑證
        String Path = GetServiceData.ServicePath + "/Find_Fac_List?Region=" + Region + "&Type=" + Type;

        GetServiceData.getString(Path, mQueue, new GetServiceData.VolleyCallback() {
            @Override
            public void onSuccess(JSONObject result) {

                try {

                    JSONArray UserArray = new JSONArray(result.getString("Key"));

                    for (int i = 0; i < UserArray.length(); i++) {

                        JSONObject IssueData = UserArray.getJSONObject(i);

                        String F_SeqNo = String.valueOf(IssueData.getInt("F_SeqNo"));

                        String F_AssetNo = String.valueOf(IssueData.getString("F_AssetNo"));

                        String F_Type = String.valueOf(IssueData.getString("F_Type"));

                        String F_Location = String.valueOf(IssueData.getString("F_Location"));

                        String F_Facility = String.valueOf(IssueData.getString("F_Facility"));

                        String F_Model = String.valueOf(IssueData.getString("F_Model"));

                        String F_Factory = String.valueOf(IssueData.getString("F_Factory"));

                        String Dept = String.valueOf(IssueData.getString("Dept"));

                        String TEL = String.valueOf(IssueData.getString("TEL"));

                        String EMail = String.valueOf(IssueData.getString("EMail"));

                        String F_Owner = String.valueOf(IssueData.getString("F_Owner"));

                        String HourCost = String.valueOf(IssueData.getDouble("HourCost"));

                        String F_Standard = String.valueOf(IssueData.getString("F_Standard"));

                        String F_Status = String.valueOf(IssueData.getString("F_Status"));

                        String F_Is_Restrict = String.valueOf(IssueData.getString("F_Is_Restrict"));

                        String Using = String.valueOf(IssueData.getInt("Using"));

                        String IMG = String.valueOf(IssueData.getString("IMG"));

                        if (IssueData.isNull("Applier")) {
                            String Applier = "無";
                            Set_Applier = "無";
                        } else {
                            String Applier = IssueData.getString("Applier");//工作性質
                            Set_Applier = Applier;
                        }

                        if (IssueData.isNull("ApplierSDate")) {
                            String ApplierSDate = "無";
                            Set_ApplierSDate = "無";
                        } else {
                            String ApplierSDate = String.valueOf(IssueData.getString("ApplierSDate").replace("T"," "));
                            Set_ApplierSDate = ApplierSDate;
                        }

                        if (IssueData.isNull("ApplierEDate")) {
                            String ApplierEDate = "無";
                            Set_ApplierEDate = "無";
                        } else {
                            String ApplierEDate = String.valueOf(IssueData.getString("ApplierEDate").replace("T"," "));
                            Set_ApplierEDate = ApplierEDate;
                        }

                        msibook_facility_item_List.add(i,new msibook_facility_item( F_SeqNo, F_AssetNo, F_Type,  F_Location,  F_Facility,  F_Model,  F_Factory,  Dept,  TEL,  EMail,  F_Owner,  HourCost,  F_Standard,  F_Status,  F_Is_Restrict,  Using,  IMG, Set_Applier, Set_ApplierSDate, Set_ApplierEDate));

                    }
                    Fac_List_Adapter = new msibook_facility_adapter(mContext,msibook_facility_item_List);

                    mListView.setAdapter(Fac_List_Adapter);

                    //關閉-讀取等待時間Bar
                    progressBar.dismiss();

                    //點選進入機台詳細資訊
                    mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            Intent intent = new Intent();

                            intent.putExtra("F_SeqNo", msibook_facility_item_List.get(position).GetF_SeqNo());//機台序號

                            intent.putExtra("F_Is_Restrict", msibook_facility_item_List.get(position).GetF_Is_Restrict()); //判斷只能約3日還是3月

                            intent.putExtra("Location", Location);//帶實驗室地區

                            intent.setClass(getActivity(), msibook_facility_fac_detail.class);
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

    private void initData() {

    }

//    private void initView() {
//        //mTablayout = (TabLayout) findViewById(R.id.tabs);
//        mTablayout.addTab(mTablayout.newTab().setText("機構配備"));
//        mTablayout.addTab(mTablayout.newTab().setText("環測配備"));
//        mTablayout.addTab(mTablayout.newTab().setText("熱流配備"));
//        mTablayout.addTab(mTablayout.newTab().setText("無響室設備"));
//        mTablayout.addTab(mTablayout.newTab().setText("電子配備"));
//        mTablayout.addTab(mTablayout.newTab().setText("音像配備"));
//        mTablayout.addTab(mTablayout.newTab().setText("掃地機配備"));
//    }


    public class msibook_facility_adapter extends BaseAdapter {

        private LayoutInflater mLayInf;

        private List<msibook_facility_item> msibook_facility_item_List = new ArrayList<msibook_facility_item>();

        private Context ProjectContext;


        public msibook_facility_adapter(Context context, List<msibook_facility_item> msibook_facility_item) {
            mLayInf = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            ProjectContext = context;

            this.msibook_facility_item_List = msibook_facility_item;

        }

        @Override
        public int getCount() {
            return msibook_facility_item_List.size();
        }

        @Override
        public Object getItem(int position) {
            return msibook_facility_item_List.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            View v = new View(ProjectContext);

            v = mLayInf.inflate(R.layout.activity_msibook_facility_item, parent, false);

            final ImageView Img_Fac = (ImageView) v.findViewById(R.id.Img_Fac);
            final TextView txt_fac_booking = (TextView) v.findViewById(R.id.txt_fac_booking);

            TextView txt_Status = (TextView) v.findViewById(R.id.txt_Status);
            TextView txt_AssetSN = (TextView) v.findViewById(R.id.txt_AssetSN);
            TextView txt_fac_name = (TextView) v.findViewById(R.id.txt_fac_name);
            TextView txt_fac_location = (TextView) v.findViewById(R.id.txt_fac_location);
            TextView txt_fac_Applier = (TextView) v.findViewById(R.id.txt_fac_Applier);
            TextView txt_fac_Date_to_Date = (TextView) v.findViewById(R.id.txt_fac_Date_to_Date);

            txt_fac_booking.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            txt_fac_booking.setBackgroundResource(R.drawable.facility_booking_btn_down);
                            return true;
                        case MotionEvent.ACTION_UP:
                            txt_fac_booking.setBackgroundResource(R.drawable.facility_booking_btn_up);
                            Intent intent = new Intent();

                            //給F_Facility
                            intent.putExtra("F_Facility", String.valueOf(msibook_facility_item_List.get(position).GetF_Facility()));
                            //給F_AssetNo
                            intent.putExtra("F_AssetNo", String.valueOf(msibook_facility_item_List.get(position).GetF_AssetNo()));
                            //給F_Master_ID
                            intent.putExtra("F_Master_ID", String.valueOf(msibook_facility_item_List.get(position).GetF_SeqNo()));
                            //給F_Is_Restrict
                            intent.putExtra("F_Is_Restrict", String.valueOf(msibook_facility_item_List.get(position).GetF_Is_Restrict()));

                            intent.putExtra("Location", Location);//帶實驗室地區

                            // GO TO  booking_main
                            intent.setClass(getActivity(), msibook_facility_booking_main.class);
                            //開啟Activity
                            startActivityForResult(intent,0);
                            //startActivity(intent);
                            return true;
                    }

                    return false;
                }
            });

            if(msibook_facility_item_List.get(position).GetF_Status().indexOf("0")==-1){
            //如果開放在show出 借出人員跟日期
                if(Integer.valueOf(msibook_facility_item_List.get(position).GetUsing())==0){
                    txt_fac_Applier.setText("借出人員：無");
                    txt_fac_Date_to_Date.setText("借出日期：無");
                }else{
                    if(msibook_facility_item_List.get(position).GetApplier().indexOf("無") == -1) {
                        txt_fac_Applier.setText("借出人員：" + msibook_facility_item_List.get(position).GetApplier());//預約人員
                    }else{
                        txt_fac_Applier.setText("借出人員：無");
                    }

                    if(msibook_facility_item_List.get(position).GetApplierSDate().indexOf("無") == -1) {
                        txt_fac_Date_to_Date.setText("借出日期：" + String.valueOf(msibook_facility_item_List.get(position).GetApplierSDate().substring(0, 10)) + " - " + String.valueOf(msibook_facility_item_List.get(position).GetApplierEDate().substring(0, 10)));
                    }else{
                        txt_fac_Date_to_Date.setText("借出日期：無");
                    }

                    //0 = 不開放  1=開放
                    if(msibook_facility_item_List.get(position).GetUsing().indexOf("1")==-1)  // 1 = 使用鐘 0 = 可預約
                    {
//                    txt_Status.setText("可預約");
//                    txt_Status.setTextColor(Color.parseColor("#ffffff"));
//                    txt_Status.setBackgroundColor(Color.parseColor("#3cd45b"));
                    }else{
                        txt_Status.setText("使用中");
                        txt_Status.setTextColor(Color.parseColor("#ffffff"));
                        txt_Status.setBackgroundColor(Color.parseColor("#ed4a47"));
                    }
                }


            }else{
                txt_Status.setText("不開放");
                txt_Status.setTextColor(Color.parseColor("#ffffff"));
                txt_Status.setBackgroundColor(Color.parseColor("#505050"));
                txt_fac_booking.setEnabled(false);
                txt_fac_booking.setVisibility(View.INVISIBLE);
                txt_fac_Applier.setText("借出人員：無");
                txt_fac_Date_to_Date.setText("借出日期：無");
                //txt_fac_booking.setBackgroundResource(R.drawable.facility_booking_btn_down);
            }

            txt_AssetSN.setText("財編 : "+msibook_facility_item_List.get(position).GetF_AssetNo());
            txt_fac_name.setText(msibook_facility_item_List.get(position).GetF_Facility());
            txt_fac_location.setText("存放位置：" + msibook_facility_item_List.get(position).GetF_Location());

            //txt_booking_status.setText(msibook_facility_item_List.get(position).Using == "0" ? "使用中" : "未使用");

            String ImagePath = msibook_facility_item_List.get(position).GetIMG().replace("//172.16.111.114/File","http://wtsc.msi.com.tw/IMS/FileServer");

            Log.w("IMagePath",ImagePath);
            Glide
                    .with(ProjectContext)
                    .load(ImagePath)
                    .asBitmap()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.mipmap.progress_image)
                    .into(new SimpleTarget<Bitmap>(100, 100) {
                        @Override
                        public void onResourceReady(Bitmap resource, GlideAnimation glideAnimation) {

                            Img_Fac.setImageBitmap(resource);

                        }

                        @Override
                        public void onLoadFailed(Exception e, Drawable errorDrawable) {
//                            Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

            return v;
        }
    }

    public class msibook_facility_item {

        String F_SeqNo;
        String F_AssetNo;
        String F_Type;
        String F_Location;
        String F_Facility;
        String F_Model;
        String F_Factory;
        String Dept;
        String TEL;
        String EMail;
        String F_Owner;
        String HourCost;
        String F_Standard;
        String F_Status;
        String F_Is_Restrict;
        String Using;
        String IMG;
        String Applier;
        String ApplierSDate;
        String ApplierEDate;

        public msibook_facility_item(String F_SeqNo,String F_AssetNo,String F_Type, String F_Location, String F_Facility, String F_Model, String F_Factory, String Dept, String TEL, String EMail, String F_Owner, String HourCost, String F_Standard, String F_Status, String F_Is_Restrict, String Using, String IMG,String Applier,String ApplierSDate,String ApplierEDate)
        {
            this.F_SeqNo = F_SeqNo;
            this.F_AssetNo = F_AssetNo;
            this.F_Type = F_Type;
            this.F_Location = F_Location;
            this.F_Facility = F_Facility;
            this.F_Model = F_Model;
            this.F_Factory = F_Factory;
            this.Dept = Dept;
            this.TEL = TEL;
            this.EMail = EMail;
            this.F_Owner = F_Owner;
            this.HourCost = HourCost;
            this.F_Standard = F_Standard;
            this.F_Status = F_Status;
            this.F_Is_Restrict = F_Is_Restrict;
            this.Using = Using;
            this.IMG = IMG;
            this.Applier = Applier;
            this.ApplierSDate = ApplierSDate;
            this.ApplierEDate = ApplierEDate;
        }

        public String GetF_SeqNo()   { return this.F_SeqNo; }

        public String GetF_AssetNo()
        {
            return this.F_AssetNo;
        }

        public String GetF_Type()
        {
            return this.F_Type;
        }

        public String GetF_Facility()
        {
            return this.F_Facility;
        }
        public String GetF_Location()
        {
            return this.F_Location;
        }

        public String GetF_Model()
        {
            return this.F_Model;
        }

        public String GetF_Factory()
        {
            return this.F_Factory;
        }

        public String GetDept()
        {
            return this.Dept;
        }

        public String GetTEL()
        {
            return this.TEL;
        }

        public String GetEMail()
        {
            return this.EMail;
        }

        public String GetF_Owner()
        {
            return this.F_Owner;
        }

        public String GetHourCost()
        {
            return this.HourCost;
        }

        public String GetF_Standard()
        {
            return this.F_Standard;
        }

        public String GetF_Status()
        {
            return this.F_Status;
        }

        public String GetF_Is_Restrict()
        {
            return this.F_Is_Restrict;
        }

        public String GetUsing()
        {
            return this.Using;
        }

        public String GetIMG()
        {
            return this.IMG;
        }

        public String GetApplier()
        {
            return this.Applier;
        }

        public String GetApplierSDate()
        {
            return this.ApplierSDate;
        }

        public String GetApplierEDate()
        {
            return this.ApplierEDate;
        }

    }


}