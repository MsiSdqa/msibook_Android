package dqa.com.msibook;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link msibook_pms_ips.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link msibook_pms_ips#newInstance} factory method to
 * create an instance of this fragment.
 */
public class msibook_pms_ips extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String Set_RModel;
    private String Set_ModelPic;

    private String[] Array_ModelPic;

    private List<msibook_pms_ips_item> msibook_pms_ips_item_List = new ArrayList<msibook_pms_ips_item>();

    private List<msibook_pms_ips_item> msibook_pms_ips_item_Search_List = new ArrayList<msibook_pms_ips_item>();

    private RecyclerView recyclerView;
    private RecyclerView.Adapter recyclerView_Adapter;
    private RecyclerView.LayoutManager recyclerViewLayoutManager;

    private ProgressDialog progressBar;
    private Context mContext;

    private String SearchString = "";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public msibook_pms_ips() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment msibook_pms_ips.
     */
    // TODO: Rename and change types and number of parameters
    public static msibook_pms_ips newInstance(String param1, String param2) {
        msibook_pms_ips fragment = new msibook_pms_ips();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void SetSearchSYS(String SearchString)
    {
        this.SearchString = SearchString;

        //PMS_IPS("SYS");

        FilterIPS(SearchString);
    }

    public void SetSearchMB(String SearchString)
    {
        this.SearchString = SearchString;

        PMS_IPS("MB");
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
        View v = inflater.inflate(R.layout.fragment_msibook_pms_ips, container, false);
        mContext = getContext();

        progressBar = new ProgressDialog(this.mContext);
        progressBar.setCancelable(true);
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.setMessage("資料載入中");

        recyclerView = (RecyclerView) v.findViewById(R.id.recycler_view1);

        PMS_IPS("SYS");

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

    //搜尋撈的
    public void FilterIPS(String SearchText)
    {
        msibook_pms_ips_item_Search_List.clear();
        int SearchCount = 0;
        String j = "";
        for(int i = 0;i<msibook_pms_ips_item_List.size();i++){
            j = String.valueOf(msibook_pms_ips_item_List.get(i).Model);
            if(j.contains(SearchText)){

                msibook_pms_ips_item_Search_List.add(SearchCount,msibook_pms_ips_item_List.get(i)); //把ips_item的值都撈給ips_item_search
                SearchCount ++ ;

            }
        }

        recyclerViewLayoutManager = new GridLayoutManager(mContext, 2);

        recyclerView.setLayoutManager(recyclerViewLayoutManager);

        recyclerView_Adapter = new RecyclerViewAdapter(mContext, msibook_pms_ips_item_Search_List);

        recyclerView.setAdapter(recyclerView_Adapter);

        recyclerView_Adapter.notifyDataSetChanged();

    }

    //第一次撈的
    public void PMS_IPS(final String type) {

        progressBar.show();

        msibook_pms_ips_item_List.clear();

        RequestQueue mQueue = Volley.newRequestQueue(getActivity());

        HTTPSTrustManager.allowAllSSL();//信任所有证书，信任憑證
        String Path = GetServiceData.ServicePath + "/PMS_IPS?type=" + type;

        GetServiceData.getString(Path, mQueue, new GetServiceData.VolleyCallback() {
            @Override
            public void onSuccess(JSONObject result) {

                try {

                    JSONArray UserArray = new JSONArray(result.getString("Key"));

                    for (int i = 0; i < UserArray.length(); i++) {

                        JSONObject IssueData = UserArray.getJSONObject(i);

                        String Model = String.valueOf(IssueData.getString("Model"));

                        if (IssueData.isNull("ModelPic")) {
                            String ModelPic = "null";
                            Set_ModelPic = "null";
                        } else {
                            String ModelPic = IssueData.getString("ModelPic");//工作性質
                            Set_ModelPic = ModelPic;
                        }

                        String Introduction = String.valueOf(IssueData.getString("Introduction"));

                        String Stage = String.valueOf(IssueData.getString("Stage"));

                        if (IssueData.isNull("RModel")) {
                            String RModel = "N/A";
                            Set_RModel = "N/A";
                        } else {
                            String RModel = IssueData.getString("RModel");//工作性質
                            Set_RModel = RModel;
                        }

                        msibook_pms_ips_item_List.add(i, new msibook_pms_ips_item(Model, Set_ModelPic, Introduction, Stage, Set_RModel));
                        //msibook_pms_ips_item_Search_List.add(i, new msibook_pms_ips_item(Model, Set_ModelPic, Introduction, Stage, Set_RModel));
                        Log.w("TTTTTTTTTT",String.valueOf(msibook_pms_ips_item_List.get(i)));

                    }
                    //Change 2 to your choice because here 2 is the number of Grid layout Columns in each row.
                    recyclerViewLayoutManager = new GridLayoutManager(mContext, 2);

                    recyclerView.setLayoutManager(recyclerViewLayoutManager);

                    recyclerView_Adapter = new RecyclerViewAdapter(mContext, msibook_pms_ips_item_List);

                    recyclerView.setAdapter(recyclerView_Adapter);

                    recyclerView_Adapter.notifyDataSetChanged();


                } catch (JSONException ex) {
                    Log.w("Json", ex.toString());
                }
                progressBar.dismiss();
            }
        });
    }

    public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{

        List<msibook_pms_ips_item> values;
        Context context1;

        public RecyclerViewAdapter(Context context2, List<msibook_pms_ips_item> values2){

            values = values2;

            context1 = context2;
        }

        public class ViewHolder extends RecyclerView.ViewHolder{

            public TextView textView;

            public ImageView Img_Model;

            public ViewHolder(View v){

                super(v);

                textView = (TextView) v.findViewById(R.id.textview1);

                Img_Model = (ImageView) v.findViewById(R.id.Img_pms_model);

            }
        }

        @Override
        public RecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){

            View view1 = LayoutInflater.from(context1).inflate(R.layout.msibook_pms_recycler_view_item,parent,false);

            RecyclerViewAdapter.ViewHolder viewHolder1 = new RecyclerViewAdapter.ViewHolder(view1);

            return viewHolder1;
        }

        @Override
        public void onBindViewHolder(final RecyclerViewAdapter.ViewHolder Vholder, final int position){

            String ImagePath;

            Vholder.textView.setText("MS-" + values.get(position).GetModel());

            if(values.get(position).GetModelPic().contains("null") || values.get(position).GetModelPic().isEmpty()){
                Vholder.Img_Model.setImageResource(R.mipmap.pms_img_pms_no_pic);
            }else{
                Array_ModelPic = values.get(position).GetModelPic().split(",");
                //String ImagePath = values.get(position).GetModelPic().replace("//172.16.111.114/File","http://wtsc.msi.com.tw/IMS/FileServer");
                //ImagePath = Array_ModelPic[0].replace("//172.16.111.114/File","http://wtsc.msi.com.tw/IMS/FileServer");
                //Log.w("擷取最後一個斜線的位置",Array_ModelPic[0].lastIndexOf('\\')+"");
                if(Integer.valueOf(Array_ModelPic[0].lastIndexOf('\\')+"")>0) {

                    Log.w("圖片檔名 正斜", Array_ModelPic[0].substring(Integer.valueOf(Array_ModelPic[0].lastIndexOf('\\') + "")));
                    ImagePath = "http://wtsc.msi.com.tw/IMS/FileServer/SDQA/Code/PM/Resize/" + Array_ModelPic[0].substring(Integer.valueOf(Array_ModelPic[0].lastIndexOf('\\') + ""));
                }else{

                    Log.w("圖片檔名 反斜", Array_ModelPic[0].substring(Integer.valueOf(Array_ModelPic[0].lastIndexOf('/') + "")));
                    ImagePath = "http://wtsc.msi.com.tw/IMS/FileServer/SDQA/Code/PM/Resize/" + Array_ModelPic[0].substring(Integer.valueOf(Array_ModelPic[0].lastIndexOf('/') + ""));
                }
//                if(Array_ModelPic[0].contains("//172.16.111.114")){
//                    ImagePath = Array_ModelPic[0].replace("//172.16.111.114/File","http://wtsc.msi.com.tw/IMS/FileServer");
//                }else{
//                    ImagePath = Array_ModelPic[0].replace("//172.18.16.24/File","http://wtsc.msi.com.tw/IMS/FileServers");
//                }
                    //  http://wtsc.msi.com.tw/IMS/FileServer/SDQA/Code/PM/Resize/ + 檔名

                Log.w("IMagePath",ImagePath);
                Glide
                        .with(context1)
                        .load(ImagePath)
                        .asBitmap()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .placeholder(R.mipmap.progress_image)
                        .into(new SimpleTarget<Bitmap>(100, 100) {
                            @Override
                            public void onResourceReady(Bitmap resource, GlideAnimation glideAnimation) {
                                http://wtsc.msi.com.tw/IMS/FileServer/SDQA\Code\PM\@2018-1101-0227-451367@MS-98L2_2D_SKU1.jpg
                                Vholder.Img_Model.setImageBitmap(resource);
                            }

                            @Override
                            public void onLoadFailed(Exception e, Drawable errorDrawable) {
//                            Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            }



            Vholder.Img_Model.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.w("圖片被點擊",String.valueOf(values.get(position).GetModel()));

                    Log.w("getModel",values.get(position).GetModel());
                    Log.w("getStage",values.get(position).GetStage());
                    Log.w("getRModel",values.get(position).GetRModel());
                    Log.w("getModelPic",values.get(position).GetModelPic());

//                    //INTENT OBJ
////                    Intent i=new Intent(context1,msibook_pms_spec.class);
////                    //ADD DATA TO OUR INTENT
////                    i.putExtra("Model ", String.valueOf(values.get(position).GetModel()));
////                    //START DETAIL ACTIVITY
////                    context1.startActivity(i);
//
                    Context context = v.getContext();
                    Intent intent = new Intent(context, msibook_pms_spec.class);
                    intent.putExtra("Model", String.valueOf(values.get(position).GetModel()));
                    intent.putExtra("Stage", values.get(position).GetStage());
                    intent.putExtra("RModel", values.get(position).GetRModel());
                    intent.putExtra("ModelPic", values.get(position).GetModelPic());
                    intent.putExtra("Introduction", values.get(position).GetIntroduction());
                    context.startActivity(intent);

                }
            });

        }

        @Override
        public int getItemCount(){

            return values.size();
        }
    }

    public class msibook_pms_ips_item {

        String Model;
        String ModelPic;
        String Introduction;
        String Stage;
        String RModel;


        public msibook_pms_ips_item(String Model,String ModelPic,String Introduction, String Stage, String RModel)
        {
            this.Model = Model;
            this.ModelPic = ModelPic;
            this.Introduction = Introduction;
            this.Stage = Stage;
            this.RModel = RModel;
        }

        public String GetModel()   { return this.Model; }

        public String GetModelPic()
        {
            return this.ModelPic;
        }

        public String GetIntroduction()
        {
            return this.Introduction;
        }

        public String GetStage()
        {
            return this.Stage;
        }

        public String GetRModel()
        {
            return this.RModel;
        }


    }


}
