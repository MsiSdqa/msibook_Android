package dqa.com.msibook;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link msibook_certified_info_detail_enginner.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link msibook_certified_info_detail_enginner#newInstance} factory method to
 * create an instance of this fragment.
 */
public class msibook_certified_info_detail_enginner extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Context mContext;

    private View mView;

    private OnFragmentInteractionListener mListener;

    private ProgressDialog progressBar;

    private List<msibook_certified_engineer_item> Certified_Enginner_List = new ArrayList<msibook_certified_engineer_item>();

    private List<String> GroupItem = new ArrayList<String>(); //組

    private List<msibook_certified_engineer_item> Favorite_Certified_Enginner_List = new ArrayList<msibook_certified_engineer_item>();//我的最愛

    private List<msibook_certified_engineer_item> Ongoing_Certified_Enginner_List = new ArrayList<msibook_certified_engineer_item>();//進行中

    private List<msibook_certified_engineer_item> Finish_Certified_Enginner_List = new ArrayList<msibook_certified_engineer_item>();//完成

    private ArrayList<List<msibook_certified_engineer_item>> Certified_List_Enginner_Group = new ArrayList<List<msibook_certified_engineer_item>>();//群組

    private ExpandableListView exp_my_certified_enginner_info_list;

    private Certified_Enginner_List_Adapter exp_my_certified_enginner_list_Adapter;

    private String Set_CheckF_Cer_Class;

    public msibook_certified_info_detail_enginner() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment msibook_certified_info_detail_enginner.
     */
    // TODO: Rename and change types and number of parameters
    public static msibook_certified_info_detail_enginner newInstance(String param1, String param2) {
        msibook_certified_info_detail_enginner fragment = new msibook_certified_info_detail_enginner();
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
        mView = inflater.inflate(R.layout.fragment_msibook_certified_info_detail_enginner, container, false);
        mContext = getContext();
        progressBar = new ProgressDialog(getActivity());
        progressBar.setCancelable(true);
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.setMessage("資料載入中");

        exp_my_certified_enginner_info_list = (ExpandableListView) mView.findViewById(R.id.exp_my_certified_enginner_info_list);

        Set_CheckF_Cer_Class="";

        Find_My_Certification_Info("10003130");


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
    //設定 當前Tab為 WHEN 1 THEN '處理中' WHEN 2 THEN '已取消' WHEN 3 THEN '已完成' ELSE '尚未申請需求單'
    public void CheckF_Cer_Class(String CheckF_Cer_Class) {
        Set_CheckF_Cer_Class = CheckF_Cer_Class;
    }

    public void Find_My_Certification_Info(String WorkID) {

        //顯示  讀取等待時間Bar
        progressBar.show();

        Certified_Enginner_List.clear();//塞總資料

        Favorite_Certified_Enginner_List.clear();//我的最愛

        Ongoing_Certified_Enginner_List.clear();//進行中

        Finish_Certified_Enginner_List.clear();//完成

        GroupItem.clear();//Title

        Certified_List_Enginner_Group.clear();//群組

        RequestQueue mQueue = Volley.newRequestQueue(getActivity());

        Map<String,String> map = new HashMap<String, String>();
        map.put("WorkID", WorkID);

        if (mQueue == null) {
            mQueue = Volley.newRequestQueue(getActivity());
        }
        HTTPSTrustManager.allowAllSSL();//信任所有证书，信任憑證
        String Path = GetServiceData.ServicePath + "/Find_My_Certification_Info";
        //String Path = "http://wtsc.msi.com.tw/IMS/MsiBook_App_Service.asmx/Insert_E_HR_Master";

        GetServiceData.SendPostRequest(Path, mQueue, new GetServiceData.VolleyStringCallback() {
            @Override
            public void onSendRequestSuccess(String result) {

                try {

                    JSONObject obj = new JSONObject(result);

                    JSONArray UserArray = new JSONArray(obj.getString("Key"));

                    for (int i = 0; i < UserArray.length(); i++) {

                        JSONObject CertificationData = UserArray.getJSONObject(i);

                        String ModelName = String.valueOf(CertificationData.getString("ModelName")); //已取消    尚未申請需求單"ModelName": "MS7792-10",

                        String F_RespUser = String.valueOf(CertificationData.getString("F_RespUser"));//"F_RespUser": "龔伯恩",

                        String Status = String.valueOf(CertificationData.getString("Status"));//"Status": "處理中",

                        String F_Keyin = String.valueOf(CertificationData.getString("F_Keyin"));//"F_Keyin": "10014560",

                        String F_SeqNo = String.valueOf(CertificationData.getInt("F_SeqNo"));//"F_SeqNo": 743,

                        String F_Owner = String.valueOf(CertificationData.getString("F_Owner"));//"F_Owner": "林忠燁"

                        String F_OwnF_CreateDateer = String.valueOf(CertificationData.getString("F_CreateDate"));//"F_CreateDate": "2012-08-14T18:45:00",

                        String F_Cer_ID = String.valueOf(CertificationData.getInt("F_Cer_ID"));//"F_Cer_ID": 16,

                        String F_Status = String.valueOf(CertificationData.getString("F_Status"));//"F_Status": "1",

                        String F_Require_ID = String.valueOf(CertificationData.getString("F_Require_ID"));//"F_Require_ID": "1491",

                        String F_Cer_Expense = String.valueOf(CertificationData.getDouble("F_Cer_Expense"));//"F_Cer_Expense": 250.00,

                        String F_Cer_Time = String.valueOf(CertificationData.getDouble("F_Cer_Time"));//"F_Cer_Time": 2.00,

                        String F_Cer_Pic = String.valueOf(CertificationData.getString("F_Cer_Pic"));//"F_Cer_Pic": "//172.16.111.114/File/SDQA/Code/Certification/10013133@2011-0117-0158-001101@sevenDeviceLogo.png",

                        String F_SDate = String.valueOf(CertificationData.getString("F_SDate"));//"F_SDate": "2012-08-14T18:45:00",

                        String F_ExpectFixDate = String.valueOf(CertificationData.getString("F_ExpectFixDate"));//"F_ExpectFixDate": "2012-08-21T00:00:00",

                        String F_Cer_Logo = String.valueOf(CertificationData.getString("F_Cer_Logo"));//"F_Cer_Logo": "WHQL-Device/Motherboard(Win7)",

                        String F_Reason = "";
                        if  (CertificationData.isNull("F_Reason"))//人名
                        {
                            F_Reason = "0";
                        }
                        else{
                            F_Reason = String.valueOf(CertificationData.getString("F_Reason"));
                        }

                        String F_RWorkID = String.valueOf(CertificationData.getString("F_RWorkID"));//"F_RWorkID": "10003130",

                        String F_Cer_Class = String.valueOf(CertificationData.getString("F_Cer_Class"));//"F_Cer_Class": "設計認證類",

                        String RRate = "";
                        if  (CertificationData.isNull("RRate"))//人名
                        {
                            RRate = "0";
                        }
                        else{
                            RRate = String.valueOf(CertificationData.getString("RRate"));
                        }

                        String F_Is_Del = String.valueOf(CertificationData.getString("F_Is_Del"));//"F_Is_Del": "1"

                        //String Favorite = String.valueOf(CertificationData.getInt("Favorite"));

                        if(F_Cer_Class.contains(Set_CheckF_Cer_Class)){
                            msibook_certified_engineer_item msibook_certified_engineer_item = new msibook_certified_engineer_item(F_Cer_Pic,ModelName,F_Cer_Logo,F_ExpectFixDate);

                            Certified_Enginner_List.add(msibook_certified_engineer_item);
                        }

                    }

//                    for (msibook_certified_item e : Certified_List) {
//
//                        if(e.Favorite.contains("1"))
//                        {
//                            Favorite_Certified_List.add(e);
//                        }
//                        else if(e.Favorite.contains("0") && e.Status.contains("NEW"))
//                        {
//                            Project_Certified_List.add(e);
//                        }
//
//                    }

                    GroupItem.add("我的最愛");
                    GroupItem.add("進行中");
                    GroupItem.add("完成");

                    Certified_List_Enginner_Group.add(Certified_Enginner_List);
                    Certified_List_Enginner_Group.add(Certified_Enginner_List);
                    Certified_List_Enginner_Group.add(Certified_Enginner_List);

                    exp_my_certified_enginner_list_Adapter = new Certified_Enginner_List_Adapter(mContext,GroupItem,Certified_List_Enginner_Group);
                    exp_my_certified_enginner_info_list.setAdapter(exp_my_certified_enginner_list_Adapter);

                    //設定Group最大限制
                    for (int i = 0; i < GroupItem.size(); i++) {
                        exp_my_certified_enginner_info_list.expandGroup(i);
                    }



                }
                catch (JSONException ex) {
                    Log.w("Json",ex.toString());
                }
                progressBar.hide();
            }

            @Override
            public void onSendRequestError(String result) {
                Log.w("RequestError",result);
                progressBar.hide();

            }

        }, map);
    }



    //------------------------Item----------------------
    public class msibook_certified_engineer_item implements Serializable {

        String F_Cer_Pic;
        String ModelName;
        String F_Cer_Logo;
        String F_ExpectFixDate;

        public msibook_certified_engineer_item(String F_Cer_Pic,String ModelName,String F_Cer_Logo,String F_ExpectFixDate) {

            this.F_Cer_Pic = F_Cer_Pic;
            this.ModelName = ModelName;
            this.F_Cer_Logo = F_Cer_Logo;
            this.F_ExpectFixDate = F_ExpectFixDate;
        }

    }


    //-----------------------Adapter-------------------
    public class Certified_Enginner_List_Adapter extends BaseExpandableListAdapter {

        private LayoutInflater mLayInf;

        private String AdapterType;

        private Context context;

        List<String> groups;

        ArrayList<List<msibook_certified_engineer_item>> childs;

        protected void onActivityResult(int requestCode, int resultCode, Intent data) {

//            if(data !=null) {
//                //Find_Certification_Model_List(UserData.WorkID);
//                Find_My_Certification_Info("10003130");
//            }

        }

        public Certified_Enginner_List_Adapter(Context context, List<String> groups, ArrayList<List<msibook_certified_engineer_item>> childs) {
            mLayInf = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            this.groups = groups;
            this.childs = childs;
            this.context = context;
        }

        public Object getChild(int groupPosition, int childPosition) {

            return childs.get(groupPosition).get(childPosition);

        }

        public long getChildId(int groupPosition, int childPosition) {

            return childPosition;

        }

        //獲取二級清單的View物件
        public View getChildView(int groupPosition, final int childPosition, boolean isLastChild, View convertView,
                                 ViewGroup parent) {

            View v = new View(context);

            v =  mLayInf.inflate(R.layout.msibook_certified_enginner_child_item, parent, false);

            final msibook_certified_engineer_item msibook_certified_engineer_item = (msibook_certified_engineer_item) getChild(groupPosition, childPosition);

            final ImageView imageView_Cer_Pic = (ImageView) v.findViewById(R.id.imageView_Cer_Pic);
            final TextView textView_ModelName = (TextView) v.findViewById(R.id.textView_ModelName);  //
            final TextView textView_F_ExpectFixDate = (TextView) v.findViewById(R.id.textView_F_ExpectFixDate);
            final TextView textView_F_Cer_Logo = (TextView) v.findViewById(R.id.textView_F_Cer_Logo);
            final ImageView imageView_favorite = (ImageView) v.findViewById(R.id.imageView_favorite);

            textView_ModelName.setText(msibook_certified_engineer_item.ModelName);
            textView_F_ExpectFixDate.setText(msibook_certified_engineer_item.F_ExpectFixDate.substring(0, 10));
            textView_F_Cer_Logo.setText(msibook_certified_engineer_item.F_Cer_Logo);

            //抓圖片路徑
            if(msibook_certified_engineer_item.F_Cer_Pic.length()>5){ //有資料就讀取 沒資料就跳過


                Glide.with(msibook_certified_info_detail_enginner.this)
                        .load("http://wtsc.msi.com.tw/IMS/IMS_App_Service.asmx/Get_File?FileName=" + msibook_certified_engineer_item.F_Cer_Pic)
                        .asBitmap()
                        .into(new SimpleTarget<Bitmap>(300, 300) {
                            @Override
                            public void onResourceReady(Bitmap bitmap, GlideAnimation anim) {

                                //BitmapDrawable ob = new BitmapDrawable(getResources(), AppClass.roundCornerImage(bitmap,0));
                                //Img_ProjectInfo.setBackground(ob);

                                BitmapDrawable ob = new BitmapDrawable(getResources(), bitmap);
                                imageView_Cer_Pic.setImageBitmap(ob.getBitmap());
                                //Img_Project_Pic_Large.setBackground(ob);
                                //Rel_Project_Layout.setBackground(ob);
                            }
                        });
            }

//            if (msibook_certified_item.Favorite.contains("1"))
//            {
//                imageView_favorite.setBackgroundResource(R.mipmap.btn_star_sel);
//            }
//            else
//            {
//                imageView_favorite.setBackgroundResource(R.mipmap.btn_star_nor);
//            }

            //點選我的最愛
//            imageView_favorite.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Request_Favorite_Update(msibook_request_item.F_SeqNo,UserData.WorkID);
//                }
//            });

            return v;
        }




        public int getChildrenCount(int groupPosition) {
            if (groups.size() == 0) {
                return 0;
            } else {
                if (childs.size() == 0) {

                    return 0;
                } else {

                    return childs.get(groupPosition).size();
                }

            }
        }

        public Object getGroup(int groupPosition) {


            return groups.get(groupPosition);
        }

        public int getGroupCount() {
            //Log.w("GroupSize",String.valueOf(groups.size()));


            return groups.size();
        }

        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        //獲取一級清單View物件
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            String text = groups.get(groupPosition);
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

//獲取一級清單佈局檔,設置相應元素屬性
            RelativeLayout RelativeLayout = (RelativeLayout) layoutInflater.inflate(R.layout.msibook_certified_group_item, null);
            TextView textView_Title = (TextView) RelativeLayout.findViewById(R.id.textView_Title);
            textView_Title.setText(text);
            ImageView parentImageViw = (ImageView) RelativeLayout.findViewById(R.id.parentImageViw);

            if (isExpanded)
            {
                parentImageViw.setBackgroundResource(R.mipmap.certified_btn_certified_collapse);
            }
            else
            {
                parentImageViw.setBackgroundResource(R.mipmap.certified_btn_certified_unfolded);
            }

            return RelativeLayout;
        }

        public boolean hasStableIds() {
            return false;
        }

        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return false;
        }

    }


}
