package dqa.com.msibook;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
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

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link msibook_app_main.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link msibook_app_main#newInstance} factory method to
 * create an instance of this fragment.
 */
public class msibook_app_main extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private List<msibook_app_item> msibook_app_item_List = new ArrayList<msibook_app_item>();

    private RecyclerView recyclerView;
    private RecyclerView.Adapter recyclerView_Adapter;
    private RecyclerView.LayoutManager recyclerViewLayoutManager;

    private ProgressDialog progressBar;
    private Context mContext;

    private String Set_RoleCode;

    private String Set_RoleName;

    private String Set_RoleID;

    private String Set_F_Stat;

    private Button btn_application_app;

    // 資料庫物件
    private SQLiteDatabase db;
    // 表格名稱
    public static final String TABLE_NAME = "UserData";
    public static final String Region_COLUMN = "_Region";
    private Button button_chenge_MSIT;
    private Button button_chenge_MSIK;
    private Button button_chenge_MSIS;

    private TextView textView_onemsi_qr;
    private TextView textView_onemsi;


    public msibook_app_main() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment msibook_app_main.
     */
    // TODO: Rename and change types and number of parameters
    public static msibook_app_main newInstance(String param1, String param2) {
        msibook_app_main fragment = new msibook_app_main();
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
        View v = inflater.inflate(R.layout.fragment_msibook_app_main, container, false);
        mContext = getContext();

        progressBar = new ProgressDialog(this.mContext);
        progressBar.setCancelable(true);
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.setMessage("資料載入中");

        textView_onemsi_qr= (TextView) v.findViewById(R.id.textView_onemsi_qr);
        textView_onemsi= (TextView) v.findViewById(R.id.textView_onemsi);
        textView_onemsi_qr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse("https://drive.google.com/drive/folders/16bItYJ8yFibzUx66r0rag5vyvdXNQR2X?usp=sharing"));
                startActivity(i);
            }
        });
        textView_onemsi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse("https://drive.google.com/drive/folders/16bItYJ8yFibzUx66r0rag5vyvdXNQR2X?usp=sharing"));
                startActivity(i);
            }
        });

        btn_application_app = (Button) v.findViewById(R.id.btn_application_app);
        btn_application_app.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent msibook_application_app_use = new Intent(getActivity(),msibook_application_app_use.class);
                startActivityForResult(msibook_application_app_use,1);
            }
        });


        button_chenge_MSIT = (Button) v.findViewById(R.id.button_chenge_MSIT);//改台北
        button_chenge_MSIT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db = DBHelper.getDatabase(getActivity());
                String updateQuery = "UPDATE " + TABLE_NAME + " SET _Region = '" + "MSIT" + "'";
                db.execSQL(updateQuery);//更改資料表
                db.close();
                Toast.makeText(getActivity(), "權限切換台北區，重啟App", Toast.LENGTH_SHORT).show();
            }
        });

        button_chenge_MSIK = (Button) v.findViewById(R.id.button_chenge_MSIK);//改昆山
        button_chenge_MSIK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db = DBHelper.getDatabase(getActivity());
                String updateQuery = "UPDATE " + TABLE_NAME + " SET _Region = '" + "MSIK" + "'";
                db.execSQL(updateQuery);//更改資料表
                db.close();
                Toast.makeText(getActivity(), "權限切為昆山區，重啟App", Toast.LENGTH_SHORT).show();
            }
        });

        button_chenge_MSIS = (Button) v.findViewById(R.id.button_chenge_MSIS);//改寶安
        button_chenge_MSIS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db = DBHelper.getDatabase(getActivity());
                String updateQuery = "UPDATE " + TABLE_NAME + " SET _Region = '" + "MSIS" + "'";
                db.execSQL(updateQuery);//更改資料表
                db.close();
                Toast.makeText(getActivity(), "權限切換寶安區，重啟App", Toast.LENGTH_SHORT).show();
            }
        });

        if(UserData.WorkID.contains("10015812")){
//            button_chenge_MSIT.setVisibility(View.VISIBLE);
//            button_chenge_MSIK.setVisibility(View.VISIBLE);
//            button_chenge_MSIS.setVisibility(View.VISIBLE);

            button_chenge_MSIT.setVisibility(View.INVISIBLE);
            button_chenge_MSIK.setVisibility(View.INVISIBLE);
            button_chenge_MSIS.setVisibility(View.INVISIBLE);
        }else{
            button_chenge_MSIT.setVisibility(View.INVISIBLE);
            button_chenge_MSIK.setVisibility(View.INVISIBLE);
            button_chenge_MSIS.setVisibility(View.INVISIBLE);
        }

        recyclerView = (RecyclerView) v.findViewById(R.id.recycler_view_app);

        Find_System_Role_Type(UserData.WorkID);
        //Find_System_Role_Type("10015989");
        //Find_System_Role(UserData.WorkID);

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

    public void Find_System_Role_Type(final String WorkID) {

        msibook_app_item_List.clear();

        RequestQueue mQueue = Volley.newRequestQueue(getActivity());

        HTTPSTrustManager.allowAllSSL();//信任所有证书，信任憑證
        String Path = GetServiceData.ServicePath + "/Find_System_Role_Type?WorkID=" + WorkID;

        GetServiceData.getString(Path, mQueue, new GetServiceData.VolleyCallback() {
            @Override
            public void onSuccess(JSONObject result) {

                try {

                    JSONArray UserArray = new JSONArray(result.getString("Key"));

                    for (int i = 0; i < UserArray.length(); i++) {
                        int j = 0;

                        JSONObject IssueData = UserArray.getJSONObject(i);

                        String SysENName = String.valueOf(IssueData.getString("SysENName")); //英文

                        String SysCNName = String.valueOf(IssueData.getString("SysCNName")); //中文

                        if (IssueData.isNull("RoleCode")) { //等級 英文
                            String RoleCode = "null";
                            Set_RoleCode = "null";
                        } else {
                            String RoleCode = IssueData.getString("RoleCode");
                            Set_RoleCode = RoleCode;
                        }

                        if (IssueData.isNull("RoleName")) { //等級中文
                            String RoleName = "null";
                            Set_RoleName = "null";
                        } else {
                            String RoleName = IssueData.getString("RoleName");
                            Set_RoleName = RoleName;
                        }

                        if (IssueData.isNull("RoleID")) { //權限 數字
                            String RoleID = "null";
                            Set_RoleID = "null";
                        } else {
                            Integer RoleID = IssueData.getInt("RoleID");
                            Set_RoleID = String.valueOf(RoleID);
                        }

                        String SysType = String.valueOf(IssueData.getBoolean("SysType")); //Type

                        if (IssueData.isNull("F_Stat")) { //權限 數字
                            String F_Stat = "null";
                            Set_F_Stat = "null";
                        } else {
                            String F_Stat = IssueData.getString("F_Stat");
                            Set_F_Stat = String.valueOf(F_Stat);
                        }

                        String Rank = String.valueOf(IssueData.getInt("Rank"));

                        Log.w("F_StatF_Stat",String.valueOf(Set_F_Stat));

                        if(Boolean.valueOf(SysType)==true || (Set_RoleID.contains("null") == false && (Integer.valueOf(Set_F_Stat)!= 2) == true)){

                            msibook_app_item_List.add(j, new msibook_app_item(SysENName, SysCNName, Set_RoleCode, Set_RoleName, Set_RoleID, Set_F_Stat));
                            j++;

                        }

                    }
                    //Change 2 to your choice because here 2 is the number of Grid layout Columns in each row.
                    recyclerViewLayoutManager = new GridLayoutManager(mContext, 3);

                    recyclerView.setLayoutManager(recyclerViewLayoutManager);

                    recyclerView_Adapter = new RecyclerViewAdapter(mContext, msibook_app_item_List);

                    recyclerView.setAdapter(recyclerView_Adapter);

                    recyclerView_Adapter.notifyDataSetChanged();


                } catch (JSONException ex) {
                    Log.w("Json", ex.toString());
                }

            }
        });
    }

//    public void Find_System_Role(final String WorkID) {
//
//        msibook_app_item_List.clear();
//
//        RequestQueue mQueue = Volley.newRequestQueue(getActivity());
//
//        String Path = GetServiceData.ServicePath + "/Find_System_Role?WorkID=" + WorkID;
//
//        GetServiceData.getString(Path, mQueue, new GetServiceData.VolleyCallback() {
//            @Override
//            public void onSuccess(JSONObject result) {
//
//                try {
//
//                    JSONArray UserArray = new JSONArray(result.getString("Key"));
//
//                    for (int i = 0; i < UserArray.length(); i++) {
//
//                        JSONObject IssueData = UserArray.getJSONObject(i);
//
//                        String SysENName = String.valueOf(IssueData.getString("SysENName")); //英文
//
//                        String SysCNName = String.valueOf(IssueData.getString("SysCNName")); //中文
//
//                        if (IssueData.isNull("RoleCode")) { //等級 英文
//                            String RoleCode = "null";
//                            Set_RoleCode = "null";
//                        } else {
//                            String RoleCode = IssueData.getString("RoleCode");
//                            Set_RoleCode = RoleCode;
//                        }
//
//                        if (IssueData.isNull("RoleName")) { //等級中文
//                            String RoleName = "null";
//                            Set_RoleName = "null";
//                        } else {
//                            String RoleName = IssueData.getString("RoleName");
//                            Set_RoleName = RoleName;
//                        }
//
//                        if (IssueData.isNull("RoleID")) { //權限 數字
//                            String RoleID = "null";
//                            Set_RoleID = "null";
//                        } else {
//                            Integer RoleID = IssueData.getInt("RoleID");
//                            Set_RoleID = String.valueOf(RoleID);
//                        }
//
//                        String Rank = String.valueOf(IssueData.getInt("Rank"));
//
//                        msibook_app_item_List.add(i, new msibook_app_item(SysENName, SysCNName, Set_RoleCode, Set_RoleName, Set_RoleID,""));
//                        //msibook_pms_ips_item_Search_List.add(i, new msibook_pms_ips_item(Model, Set_ModelPic, Introduction, Stage, Set_RModel));
//                        Log.w("TTTTTTTTTT",String.valueOf(msibook_app_item_List.get(i)));
//
//                    }
//                    //Change 2 to your choice because here 2 is the number of Grid layout Columns in each row.
//                    recyclerViewLayoutManager = new GridLayoutManager(mContext, 3);
//
//                    recyclerView.setLayoutManager(recyclerViewLayoutManager);
//
//                    recyclerView_Adapter = new RecyclerViewAdapter(mContext, msibook_app_item_List);
//
//                    recyclerView.setAdapter(recyclerView_Adapter);
//
//                    recyclerView_Adapter.notifyDataSetChanged();
//
//
//                } catch (JSONException ex) {
//                    Log.w("Json", ex.toString());
//                }
//
//            }
//        });
//    }

    public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{

        List<msibook_app_item> values;
        Context context1;

        public RecyclerViewAdapter(Context context2, List<msibook_app_item> values2){

            values = values2;

            context1 = context2;
        }

        public class ViewHolder extends RecyclerView.ViewHolder{

            public TextView textView_App_Title;

            public ImageView Img_App_icon;

            public ViewHolder(View v){

                super(v);

                textView_App_Title = (TextView) v.findViewById(R.id.textView_app_title);

                Img_App_icon = (ImageView) v.findViewById(R.id.img_app_icon);

            }
        }

        @Override
        public RecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){

            View view1 = LayoutInflater.from(context1).inflate(R.layout.msibook_app_main_recycler_view_item,parent,false);

            RecyclerViewAdapter.ViewHolder viewHolder1 = new RecyclerViewAdapter.ViewHolder(view1);

            return viewHolder1;
        }

        @Override
        public void onBindViewHolder(final RecyclerViewAdapter.ViewHolder Vholder, final int position){

            Vholder.textView_App_Title.setText(values.get(position).GetSysCNName());

            String str = values.get(position).GetSysENName();
            switch (str) {
                case "Action_Item":
                    Vholder.Img_App_icon.setImageResource(R.mipmap.msibook_ic_msibook_workorder);
                    Log.w("工單系統","工單系統");
                    break;
                case "eHR":
                    Vholder.Img_App_icon.setImageResource(R.mipmap.msibook_ic_msibook_ehr);
                    Log.w("eHR","eHR");
                    break;
                case "Laboratory":
                    Vholder.Img_App_icon.setImageResource(R.mipmap.msibook_ic_msibook_laboratory);
                    Log.w("實驗室","實驗室");
                    break;
                case "MMC":
                    Vholder.Img_App_icon.setImageResource(R.mipmap.msibook_ic_msibook_assets);
                    Log.w("資產管理","資產管理");
                    break;
                case "Request_Form":
                    Vholder.Img_App_icon.setImageResource(R.mipmap.msibook_ic_msibook_demandlist);
                    Log.w("需求單系統","需求單系統");
                    break;
                case "WeeklyReport":
                    Vholder.Img_App_icon.setImageResource(R.mipmap.msibook_ic_msibook_weekly);
                    Log.w("週報","週報");
                    break;
                case "PMS":
                    Vholder.Img_App_icon.setImageResource(R.mipmap.msibook_ic_msibook_pms);
                    Log.w("PMS","PMS");
                    break;
                case "ims":
                    Vholder.Img_App_icon.setImageResource(R.mipmap.msibook_ic_msibook_ims);
                    Log.w("IMS系統","IMS系統");
                    break;
                case "OverTime":
                    Vholder.Img_App_icon.setImageResource(R.mipmap.msibook_ic_msibook_overtimeapp);
                    Log.w("加班系統","加班系統");
                    break;
                case "Certification":
                    Vholder.Img_App_icon.setImageResource(R.mipmap.msibook_ic_msibook_certified);
                    Log.w("認證系統","認證系統");
                    break;
                case "RDWork_Daily":
                    Vholder.Img_App_icon.setImageResource(R.mipmap.msibook_btn_msibook_rdworklog);
                    Log.w("RD工作日誌","RD工作日誌");
                    break;
                case "WorkReport":
                    Vholder.Img_App_icon.setImageResource(R.mipmap.msibook_ic_msibook_workreport);
                    Log.w("工作報告","工作報告");
                    break;

            }


//            if(values.get(position).GetModelPic().contains("null") || values.get(position).GetModelPic().isEmpty()){
//                Vholder.Img_Model.setImageResource(R.mipmap.pms_img_pms_no_pic);
//            }else{
//                String ImagePath = values.get(position).GetModelPic().replace("//172.16.111.114/File","http://wtsc.msi.com.tw/IMS/FileServer");
//
//                Log.w("IMagePath",ImagePath);
//                Glide
//                        .with(context1)
//                        .load(ImagePath)
//                        .asBitmap()
//                        .diskCacheStrategy(DiskCacheStrategy.ALL)
//                        .placeholder(R.mipmap.progress_image)
//                        .into(new SimpleTarget<Bitmap>(100, 100) {
//                            @Override
//                            public void onResourceReady(Bitmap resource, GlideAnimation glideAnimation) {
//
//                                Vholder.Img_Model.setImageBitmap(resource);
//                            }
//
//                            @Override
//                            public void onLoadFailed(Exception e, Drawable errorDrawable) {
////                            Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_SHORT).show();
//                            }
//                        });
//            }


            Vholder.Img_App_icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String str = values.get(position).GetSysENName();
                    switch (str) {
                        case "Action_Item":
                            if(Integer.valueOf(values.get(position).GetRoleID()) < 6){
                                Intent msibook_Request_Intent = new Intent(getActivity(),msibook_request_main.class);

                                //msibook_Request_Intent.putExtra("Control_request_RoleID    ", Control_request_RoleID);
                                //Intent msibook_Request_Intent = new Intent(mContent,msibook_request_main.class);
                                startActivityForResult(msibook_Request_Intent,1);
                            }else{
                                Intent msibook_Request_Intent = new Intent(getActivity(),msibook_my_request.class);
                                //msibook_Request_Intent.putExtra("Control_request_RoleID    ", Control_request_RoleID);
                                //Intent msibook_Request_Intent = new Intent(mContent,msibook_request_main.class);
                                startActivityForResult(msibook_Request_Intent,1);
                            }
                            Log.w("工單系統","工單系統");
                            break;
                        case "eHR":
                            Intent msibook_ehr_Intent = new Intent(getActivity(),msibook_ehr_splashscreen.class);
                            //msibook_ehr_Intent.putExtra("Control_eHR_RoleID ", Control_eHR_RoleID);
                            startActivityForResult(msibook_ehr_Intent,1);
                            Log.w("eHR","eHR");
                            break;
                        case "Laboratory":
                            Intent Btn_Lab = new Intent(getActivity(),msibook_facility.class);
                            //Btn_Lab.putExtra("Control_LAB_RoleID   ", Control_LAB_RoleID);
                            startActivityForResult(Btn_Lab,1);
                            Log.w("實驗室","實驗室");
                            break;
                        case "MMC":
                            Intent msibook_mmc = new Intent(getActivity(),msibook_mmc.class);
                            //msibook_mmc.putExtra("Control_MMC_RoleID    ", Control_MMC_RoleID);
                            startActivityForResult(msibook_mmc,1);
                            Log.w("資產管理","資產管理");
                            break;
                        case "Request_Form":
                            Intent requset_form = new Intent(getActivity(),msibook_requset_form_main.class);
                            //requset_form.putExtra("Control_requset_form_RoleID    ", Control_requset_form_RoleID);
                            startActivityForResult(requset_form,1);
                            Log.w("需求單系統","需求單系統");
                            break;
                        case "WeeklyReport":
                            if(UserData.Region.contains("MSIT")){

                                Intent dqaweekly = new Intent(getActivity(),msibook_dqaweekly_main.class);   // 2019-03-28 轉移 點了直接跳部級頁面
                                //Intent dqaweekly = new Intent(getActivity(),msibook_dqaweekly_project_activity.class);   // 2019-03-28 轉移 點了直接跳部級頁面

                                //dqaweekly.putExtra("Control_dqaweekly_RoleID    ", Control_dqaweekly_RoleID);
                                startActivityForResult(dqaweekly,1);
                                Log.w("部級週報","部級週報");
                            }else{

                                Intent intent = new Intent();
                                intent.setClass(getActivity(), msibook_dqaweekly_project_activity.class);//首頁  > 部級週報
                                //開啟Activity
                                startActivityForResult(intent,1);

                                //Toast.makeText(getActivity(), "不是台北DERRRRR!",Toast.LENGTH_LONG).show();
                            }

                            Log.w("週報","週報");
                            break;
                        case "PMS":
                            Intent pms = new Intent(getActivity(),msibook_pms_main.class);
                            //requset_form.putExtra("Control_requset_form_RoleID    ", Control_requset_form_RoleID);
                            startActivityForResult(pms,1);
                            Log.w("PMS","PMS");
                            break;
//                        case "PMS":
//                            Intent pms = new Intent(getActivity(),msibook_new_ims_main_tab.class);
//                            //requset_form.putExtra("Control_requset_form_RoleID    ", Control_requset_form_RoleID);
//                            startActivityForResult(pms,1);
//                            Log.w("PMS","PMS");
//                            break;
                        case "ims":
                            Intent msibook_ims_myissue = new Intent(getActivity(),msibook_ims_issue_myissue.class);
                            //msibook_ims_myissue.putExtra("Control_IMS_RoleID  ", Control_IMS_RoleID);
                            startActivityForResult(msibook_ims_myissue,1);
                            Log.w("IMS系統","IMS系統");
                            break;
                        case "OverTime":

//                            Intent msibook_overtime = new Intent(getActivity(),msibook_workreport.class);
//                            //RSS_Intent.putExtra("Control_RSS_RoleID    ", Control_RSS_RoleID);
//                            startActivityForResult(msibook_overtime,1);
//                            Log.w("加班系統","加班系統");

                            Intent msibook_overtime = new Intent(getActivity(),msibook_overtime.class);
                            //RSS_Intent.putExtra("Control_RSS_RoleID    ", Control_RSS_RoleID);
                            startActivityForResult(msibook_overtime,1);
                            Log.w("加班系統","加班系統");
                            break;
                        case "Certification":
                            Intent CEC_Intent = new Intent(getActivity(),msibook_cec_main_new.class);
                            //Intent CEC_Intent = new Intent(getActivity(),msibook_cec_main.class);
                            //RSS_Intent.putExtra("Control_RSS_RoleID    ", Control_RSS_RoleID);
                            startActivityForResult(CEC_Intent,1);
//                            if(Integer.valueOf(values.get(position).GetRoleID()) < 6) {
//                                Intent msibook_Certification_Intent = new Intent(getActivity(), msibook_certified_main_for_pm.class);
//                                startActivityForResult(msibook_Certification_Intent, 1);
//                            }else {
//                                Intent msibook_Certification_Intent = new Intent(getActivity(), msibook_certified_main_for_engineer.class);
//                                startActivityForResult(msibook_Certification_Intent, 1);
//                            }
                            Log.w("認證系統","認證系統");
                            break;
                        case "RDWork_Daily":
                            Intent msibook_Certification_Intent = new Intent(getActivity(), msibook_rd_main.class);
                            startActivityForResult(msibook_Certification_Intent, 1);
                            Log.w("RD工作日誌","RD工作日誌");
                            break;
                        case "WorkReport":
                            Intent RSS_Intent = new Intent(getActivity(),msibook_workreport_remake.class);
                            //Intent RSS_Intent = new Intent(getActivity(),msibook_rss.class);
                            //RSS_Intent.putExtra("Control_RSS_RoleID    ", Control_RSS_RoleID);
                            startActivityForResult(RSS_Intent,1);
                            Log.w("工作報告","工作報告");
                            break;

                    }

                }
            });

        }

        @Override
        public int getItemCount(){

            return values.size();
        }
    }

    public class msibook_app_item {

        String SysENName;
        String SysCNName;
        String RoleCode;
        String RoleName;
        String RoleID;
        String F_Stat;


        public msibook_app_item(String SysENName,String SysCNName,String RoleCode, String RoleName, String RoleID,String F_Stat)
        {
            this.SysENName = SysENName;
            this.SysCNName = SysCNName;
            this.RoleCode = RoleCode;
            this.RoleName = RoleName;
            this.RoleID = RoleID;
            this.F_Stat = F_Stat;
        }

        public String GetSysENName()   { return this.SysENName; }

        public String GetSysCNName()
        {
            return this.SysCNName;
        }

        public String GetRoleCode()
        {
            return this.RoleCode;
        }

        public String GetRoleName()
        {
            return this.RoleName;
        }

        public String GetRoleID()
        {
            return this.RoleID;
        }

        public String GetF_Stat()
        {
            return this.F_Stat;
        }

    }



}
