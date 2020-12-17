package dqa.com.msibook;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
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

import java.text.NumberFormat;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link msibook_facility_machine_detail.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link msibook_facility_machine_detail#newInstance} factory method to
 * create an instance of this fragment.
 */
public class msibook_facility_machine_detail extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private ProgressDialog progressBar;
    private Context mContext;
    private View mView;

    private String Fist_F_SeqNo;

    private ImageView Img_Fac;//圖片
    private TextView textView_F_Type;//Mechanical(機構配備)
    private TextView textView_F_Facility;//落下試驗機 Drop tester
    private TextView textView_F_AssetNo;//TY0000000001362
    private TextView textView_F_Model;//VS-6467
    private TextView textView_F_Location;//台北三廠B1
    private TextView textView_F_Dept;//設計品質驗證部\n驗證二課
    private TextView textView_F_Owner;//畢文海
    private TextView textView_F_Spec;//Spec
    private TextView textView_F_Cost;//91,350 NTD
    private TextView textView_F_Buy_Date;//2011-04-24
    private TextView textView_F_Storage_Date;//2011-04-24
    private TextView textView_F_Use_Year;//3年
    private TextView textView_F_Factory;//振儀科技
    private TextView textView_F_Standard;//5 drops for each sample.

    private String Set_Applier;
    private String Set_ApplierSDate;
    private String Set_ApplierEDate;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public msibook_facility_machine_detail() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment msibook_facility_machine_detail.
     */
    // TODO: Rename and change types and number of parameters
    public static msibook_facility_machine_detail newInstance(String param1, String param2) {
        msibook_facility_machine_detail fragment = new msibook_facility_machine_detail();
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

        Log.w("params",String.valueOf(getArguments().getString("params")));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_msibook_facility_machine_detail, container, false);

        mContext = getContext();

        progressBar = new ProgressDialog(this.mContext);
        progressBar.setCancelable(true);
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.setMessage("資料載入中");

        Img_Fac = (ImageView) mView.findViewById(R.id.img_Fac);
        textView_F_Type = (TextView)mView.findViewById(R.id.textView_F_Type);
        textView_F_Facility = (TextView)mView.findViewById(R.id.textView_F_Facility);
        textView_F_AssetNo = (TextView)mView.findViewById(R.id.textView_F_AssetNo);
        textView_F_Model = (TextView)mView.findViewById(R.id.textView_F_Model);
        textView_F_Location = (TextView)mView.findViewById(R.id.textView_F_Location);
        textView_F_Dept = (TextView)mView.findViewById(R.id.textView_F_Dept);
        textView_F_Owner = (TextView)mView.findViewById(R.id.textView_F_Owner);
        textView_F_Spec = (TextView)mView.findViewById(R.id.textView_F_Spec);
        textView_F_Cost = (TextView)mView.findViewById(R.id.textView_F_Cost);
        textView_F_Buy_Date = (TextView)mView.findViewById(R.id.textView_F_Buy_Date);
        textView_F_Storage_Date = (TextView)mView.findViewById(R.id.textView_F_Storage_Date);
        textView_F_Use_Year = (TextView)mView.findViewById(R.id.textView_F_Use_Year);
        textView_F_Factory = (TextView)mView.findViewById(R.id.textView_F_Factory);
        textView_F_Standard = (TextView)mView.findViewById(R.id.textView_F_Standard);

        String strtext = getArguments().getString("F_SeqNo");
        Log.w("strtext",String.valueOf(strtext));

        //第一次載入去Activity抓值
        msibook_facility_fac_detail activity = (msibook_facility_fac_detail) getActivity();
        String getF_SeqNo = activity.getF_SeqNo;

        Find_Fac_Detail(getF_SeqNo);

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

    //抓週
    public void SetFist_F_SeqNo(String F_SeqNo) {
        Fist_F_SeqNo = F_SeqNo;
        Log.w("Fist_F_SeqNo",String.valueOf(Fist_F_SeqNo));
    }

    public void Find_Fac_Detail(String F_SeqNo) {

        //顯示  讀取等待時間Bar
        progressBar.show();

        RequestQueue mQueue = Volley.newRequestQueue(getActivity());
        HTTPSTrustManager.allowAllSSL();//信任所有证书，信任憑證
        String Path = GetServiceData.ServicePath + "/Find_Fac_Detail?F_SeqNo=" + F_SeqNo;

        GetServiceData.getString(Path, mQueue, new GetServiceData.VolleyCallback() {
            @Override
            public void onSuccess(JSONObject result) {

                try {

                    JSONArray UserArray = new JSONArray(result.getString("Key"));

                    for (int i = 0; i < UserArray.length(); i++) {

                        JSONObject IssueData = UserArray.getJSONObject(i);

                        NumberFormat nf = NumberFormat.getInstance();//設定三位數一逗點格式方法

                        String F_SeqNo = String.valueOf(IssueData.getInt("F_SeqNo"));//137

                        String F_AssetNo = String.valueOf(IssueData.getString("F_AssetNo"));//TY000000001362

                        String F_Type = String.valueOf(IssueData.getString("F_Type"));//Mechanical(機構配備)

                        String F_Location = String.valueOf(IssueData.getString("F_Location"));//台北三廠B1

                        String F_LocationID = String.valueOf(IssueData.getString("F_LocationID"));//03

                        String F_Facility = String.valueOf(IssueData.getString("F_Facility"));//落下試驗機

                        String F_Facility_cn = String.valueOf(IssueData.getString("F_Facility_cn"));//落下试验机

                        String F_Facility_en = String.valueOf(IssueData.getString("F_Facility_en"));//Drop tester

                        String F_Model = String.valueOf(IssueData.getString("F_Model"));//VS-6467

                        String F_Factory = String.valueOf(IssueData.getString("F_Factory"));//振儀科技

                        String F_Keeper = String.valueOf(IssueData.getInt("F_Keeper"));//724

                        String TEL = String.valueOf(IssueData.getString("TEL"));//

                        String F_Keyin = String.valueOf(IssueData.getString("F_Keyin"));//10015472

                        String F_Owner = String.valueOf(IssueData.getString("F_Owner"));//羅奕岷

                        String IMG = String.valueOf(IssueData.getString("IMG"));////172.16.111.114/File/SDQA-BAK/Code/Facility/137.jpg

                        //String F_Purpose = String.valueOf(IssueData.getString("F_Purpose"));

                        //String F_Introduction = String.valueOf(IssueData.getString("F_Introduction"));

                        String F_Spec = String.valueOf(IssueData.getString("F_Spec"));//載重:100 kg\r\n高度:190 cm

                        String F_Remark = String.valueOf(IssueData.getString("F_Remark"));//模擬產品摔落地面可能造成之破壞。

                        Integer F_Cost = IssueData.getInt("F_Cost");//91350

                        String F_Storage_Date = String.valueOf(IssueData.getString("F_Storage_Date").replace("T"," ").substring(0, 10));//2001-04-24T00:00:00

                        String F_Buy_Date = String.valueOf(IssueData.getString("F_Buy_Date").replace("T"," ").substring(0, 10));//2001-04-24T00:00:00

                        String F_Use_Year = String.valueOf(IssueData.getString("F_Use_Year"));//3

                        String F_Standard = String.valueOf(IssueData.getString("F_Standard"));//5 drops for each sample.

                        String F_Status = String.valueOf(IssueData.getString("F_Status"));//1

                        String F_Is_Restrict = String.valueOf(IssueData.getString("F_Is_Restrict"));//0

                        String F_Dept = String.valueOf(IssueData.getString("F_Dept"));//設計品質驗證部 驗證二課


                        if (IssueData.isNull("Applier")) {//null
                            String Applier = "無";
                            Set_Applier = "無";
                        } else {
                            String Applier = IssueData.getString("Applier");//
                            Set_Applier = Applier;
                        }

                        if (IssueData.isNull("ApplierSDate")) {//null
                            String ApplierSDate = "無";
                            Set_ApplierSDate = "無";
                        } else {
                            String ApplierSDate = String.valueOf(IssueData.getString("ApplierSDate").replace("T"," "));
                            Set_ApplierSDate = ApplierSDate;
                        }

                        if (IssueData.isNull("ApplierEDate")) {//null
                            String ApplierEDate = "無";
                            Set_ApplierEDate = "無";
                        } else {
                            String ApplierEDate = String.valueOf(IssueData.getString("ApplierEDate").replace("T"," "));
                            Set_ApplierEDate = ApplierEDate;
                        }

                        textView_F_Type.setText(F_Type);//Mechanical(機構配備)
                        textView_F_Facility.setText(F_Facility);;//落下試驗機 Drop tester
                        textView_F_AssetNo.setText(F_AssetNo);;//TY0000000001362
                        textView_F_Model.setText(F_Model);;//VS-6467
                        textView_F_Location.setText(F_Location);;//台北三廠B1
                        textView_F_Dept.setText(F_Dept);;//設計品質驗證部\n驗證二課
                        textView_F_Owner.setText(F_Owner);;//畢文海
                        textView_F_Spec.setText(F_Spec);;//Spec
                        textView_F_Cost.setText(nf.format(F_Cost) + " NTD");;//91,350 NTD
                        textView_F_Buy_Date.setText(F_Buy_Date);;//2011-04-24
                        textView_F_Storage_Date.setText(F_Storage_Date);;//2011-04-24
                        textView_F_Use_Year.setText(F_Use_Year+"年");;//3年
                        textView_F_Factory.setText(F_Factory);;//振儀科技
                        textView_F_Standard.setText(F_Standard);;//5 drops for each sample.

                        String ImagePath = IMG.replace("//172.16.111.114/File","http://wtsc.msi.com.tw/IMS/FileServer");

                        Log.w("IMagePath",ImagePath);
                        Glide
                                .with(mContext)
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


                    }

                    //關閉-讀取等待時間Bar
                    progressBar.dismiss();

                }
                catch (JSONException ex) {
                    Log.w("Json",ex.toString());
                }
            }
        });


    }



}
