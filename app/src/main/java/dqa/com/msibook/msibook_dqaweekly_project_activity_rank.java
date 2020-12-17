package dqa.com.msibook;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link msibook_dqaweekly_project_activity_rank.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link msibook_dqaweekly_project_activity_rank#newInstance} factory method to
 * create an instance of this fragment.
 */
public class msibook_dqaweekly_project_activity_rank extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Context mContext;

    private View mView;

    private ProgressDialog progressBar;//讀取狀態

    private RankAdapter mRankAdapter;
    private List<Rank_Data_Item> Rank_Data_Item_List = new ArrayList<Rank_Data_Item>();

    private ListView mListView;

    private OnFragmentInteractionListener mListener;

    public msibook_dqaweekly_project_activity_rank() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProjectActivity_rank.
     */
    // TODO: Rename and change types and number of parameters
    public static msibook_dqaweekly_project_activity_rank newInstance(String param1, String param2) {
        msibook_dqaweekly_project_activity_rank fragment = new msibook_dqaweekly_project_activity_rank();
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

        progressBar = new ProgressDialog(getActivity());
        progressBar.setCancelable(true);
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.setMessage("資料載入中");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_msibook_dqaweekly_project_activity_rank, container, false);
        mContext = getContext();

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

    public void Find_RSS_Rank(String Year,String Week) {

        //顯示  讀取等待時間Bar
        progressBar.show();

        Rank_Data_Item_List.clear();

        RequestQueue mQueue = Volley.newRequestQueue(getActivity());

        String Path = "http://wtsc.msi.com.tw/IMS/Weekly_Report.asmx/Find_RSS_Rank?Year=" + Year + "&Week=" + Week;

        getString(Path, mQueue, new GetServiceData.VolleyCallback() {
            @Override
            public void onSuccess(JSONObject result) {

                try {
                    JSONArray UserArray = new JSONArray(result.getString("Key"));

                    for (int i = 0; i < UserArray.length(); i++) {

                        JSONObject IssueData = UserArray.getJSONObject(i);

                        NumberFormat nf = NumberFormat.getInstance();//設定三位數一逗點格式方法

                        String CostOrder = String.valueOf(IssueData.getInt("CostOrder")); //

                        String Category = String.valueOf(IssueData.getString("Category")); //

                        String Model = String.valueOf(IssueData.getString("Model")); //

                        String MarketName = String.valueOf(IssueData.getString("MarketName")); //

                        String TotalCost = String.valueOf(IssueData.getInt("TotalCost")); //
                        String TF_TotalCost = nf.format(Integer.valueOf(TotalCost));

                        String F_ModelID = String.valueOf(IssueData.getString("F_ModelID")); //

                        String DeptName = String.valueOf(IssueData.getString("DeptName")); //

                        Rank_Data_Item_List.add(i,new Rank_Data_Item(CostOrder,Category,Model,MarketName,TF_TotalCost,F_ModelID,DeptName));

                    }

                    mListView = (ListView)getActivity().findViewById(R.id.listview_rank_data);

                    mRankAdapter = new RankAdapter(mContext,Rank_Data_Item_List);

                    mListView.setAdapter(mRankAdapter);

                    //關閉-讀取等待時間Bar
                    progressBar.dismiss();


                } catch (JSONException ex) {
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
    public class Rank_Data_Item  {

        String CostOrder;

        String Category;

        String Model;

        String MarketName;

        String TotalCost;

        String F_ModelID;

        String DeptName;





        public Rank_Data_Item(String CostOrder,String Category,String Model,String MarketName,String TotalCost,String F_ModelID,String DeptName)
        {
            this.CostOrder = CostOrder;

            this.Category = Category;

            this.Model = Model;

            this.MarketName = MarketName;

            this.TotalCost = TotalCost;

            this.F_ModelID = F_ModelID;

            this.DeptName = DeptName;

        }

        public String GetCostOrder()
        {
            return this.CostOrder;
        }

        public String GetCategory()
        {
            return this.Category;
        }

        public String GetModel()
        {
            return this.Model;
        }

        public String GetMarketName()
        {
            return this.MarketName;
        }

        public String GetTotalCost()
        {
            return this.TotalCost;
        }

        public String GetF_ModelID()
        {
            return this.F_ModelID;
        }

        public String GetDeptName()
        {
            return this.DeptName;
        }

    }

    //------------------------------------Adapter-------------------------------------

    public class RankAdapter extends BaseAdapter {

        private LayoutInflater mLayInf;

        private List<Rank_Data_Item> Rank_Data_Item_List;

        private Context RankDataContext;

        private String Title;

        public RankAdapter(Context context, List<Rank_Data_Item> Rank_Data_Item_List) {
            mLayInf = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            RankDataContext = context;

            this.Title = Title;

            this.Rank_Data_Item_List = Rank_Data_Item_List;

        }

        @Override
        public int getCount() {
            return Rank_Data_Item_List.size();
        }

        @Override
        public Object getItem(int position) {
            return Rank_Data_Item_List.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View v = new View(RankDataContext);

            v = mLayInf.inflate(R.layout.msibook_dqaweekly_rankdata_adapter, parent, false);

            TextView textView_CostOrder = (TextView) v.findViewById(R.id.textView_CostOrder);
            TextView textView_Model = (TextView) v.findViewById(R.id.textView_Model);
            TextView textView_MarketName = (TextView) v.findViewById(R.id.textView_MarketName);
            TextView textView_Category = (TextView) v.findViewById(R.id.textView_Category);
            TextView textView_DeptName = (TextView) v.findViewById(R.id.textView_DeptName);
            TextView textView_TotalCost = (TextView) v.findViewById(R.id.textView_TotalCost);
            TextView textView_point1 = (TextView) v.findViewById(R.id.textView_point1);
            TextView textView_point2 = (TextView) v.findViewById(R.id.textView_point2);

            textView_CostOrder.setText(Rank_Data_Item_List.get(position).GetCostOrder());

            textView_Model.setText(Rank_Data_Item_List.get(position).GetModel());

            textView_MarketName.setText(Rank_Data_Item_List.get(position).GetMarketName());

            textView_Category.setText(Rank_Data_Item_List.get(position).GetCategory());

            textView_DeptName.setText(Rank_Data_Item_List.get(position).GetDeptName());

            textView_TotalCost.setText(Rank_Data_Item_List.get(position).GetTotalCost());

            if(Rank_Data_Item_List.get(position).GetMarketName().length()<1){
                textView_point1.setVisibility(View.GONE);
                textView_point2.setVisibility(View.GONE);
            }else{
                textView_point1.setVisibility(View.VISIBLE);
                textView_point2.setVisibility(View.VISIBLE);
            }



            return v;
        }
    }
}

