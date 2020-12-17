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

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link msibook_dqaweekly_project_activity_new_project.OnFragmentInteractionListener} interfacef
 * to handle interaction events.
 * Use the {@link msibook_dqaweekly_project_activity_new_project#newInstance} factory method to
 * create an instance of this fragment.
 */
public class msibook_dqaweekly_project_activity_new_project extends Fragment {
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

    private OpenProjectAdapter mOpenProjectAdapter;
    private List<OpenProject_Item> OpenProject_Item_List = new ArrayList<OpenProject_Item>();
    private ListView mListView;

    private OnFragmentInteractionListener mListener;

    public msibook_dqaweekly_project_activity_new_project() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProjectActivity_new_project.
     */
    // TODO: Rename and change types and number of parameters
    public static msibook_dqaweekly_project_activity_new_project newInstance(String param1, String param2) {
        msibook_dqaweekly_project_activity_new_project fragment = new msibook_dqaweekly_project_activity_new_project();
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
        //讀取時間Bar
        progressBar = new ProgressDialog(getActivity());
        progressBar.setCancelable(true);
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.setMessage("資料載入中");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mContext = getContext();
        mView = inflater.inflate(R.layout.fragment_msibook_dqaweekly_project_activity_new_project, container, false);

        mListView = (ListView) mView.findViewById(R.id.listview_open_project);


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

    //當週新增專案 資訊
    public void Find_PM_NewModel(String Week,String Year) {

        //顯示  讀取等待時間Bar
        progressBar.show();

        Log.w("Find_PM_NewModel","Find_PM_NewModel");

        OpenProject_Item_List.clear();

        RequestQueue mQueue = Volley.newRequestQueue(getActivity());

        String Path = "http://wtsc.msi.com.tw/IMS/Weekly_Report.asmx/Find_PM_NewModel?Week=" + Week + "&Year=" + Year;

        getString(Path, mQueue, new GetServiceData.VolleyCallback() {
            @Override
            public void onSuccess(JSONObject result) {
                try {

                    JSONArray UserArray = new JSONArray(result.getString("Key"));
                    Log.w("UserArray.length()",String.valueOf(UserArray.length()));
                    if (UserArray.length() > 0) {

                        for (int i = 0; i < UserArray.length(); i++) {
                            JSONObject IssueData = UserArray.getJSONObject(i);

                            String HR_BU = String.valueOf(IssueData.getString("HR_BU"));// "AIO-OBM",

                            String ModelID = String.valueOf(IssueData.getLong("ModelID")); //13016,

                            String Model = String.valueOf(IssueData.getString("Model")); //"3FA4",

                            //String MarketName = String.valueOf(IssueData.getString("MarketName")); //null,

                            String CreateDate = String.valueOf(IssueData.getString("CreateDate")); //"2017-11-16T20:03:37.61"

                            OpenProject_Item_List.add(i, new OpenProject_Item(HR_BU, Model, CreateDate.substring(0, 10)));

                        }
                        mOpenProjectAdapter = new OpenProjectAdapter(mContext, OpenProject_Item_List);

                        mListView.setAdapter(mOpenProjectAdapter);

                    }else{
                        mListView.setEmptyView(mView.findViewById(R.id.empty));
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


    //---------------------Item------------------------
    public class OpenProject_Item {

        String HR_BU;

        String ModelID;

        String Model;

        String MarketName;

        String CreateDate;



        public OpenProject_Item(String HR_BU,String Model,String CreateDate)
        {
            this.HR_BU = HR_BU;

            this.Model = Model;

            this.CreateDate = CreateDate;

        }


        public String GetHR_BU()
        {
            return this.HR_BU;
        }

        public String GetModel()
        {
            return this.Model;
        }

        public String GetCreateDate()
        {
            return this.CreateDate;
        }

    }

    //--------------------Adapter---------------------
    public class OpenProjectAdapter extends BaseAdapter {

        private LayoutInflater mLayInf;

        private List<OpenProject_Item> OpenProject_Item_List;

        private Context ProjectContext;

        private String Title;

        public OpenProjectAdapter(Context context,  List<OpenProject_Item> OpenProject_Item_List)
        {
            mLayInf = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            ProjectContext = context;

            this.Title = Title;

            this.OpenProject_Item_List = OpenProject_Item_List;

        }
        @Override
        public int getCount() {
            return OpenProject_Item_List.size();
        }

        @Override
        public Object getItem(int position) {
            return OpenProject_Item_List.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View v = new View(ProjectContext);

            v = mLayInf.inflate(R.layout.msibook_dqaweekly_openproject_adapter, parent, false);

            TextView textView_bu = (TextView) v.findViewById(R.id.textView_bu);
            TextView textView_model = (TextView) v.findViewById(R.id.textView_model);
            TextView textView_createdate = (TextView) v.findViewById(R.id.textView_createdate);

            textView_bu.setText(OpenProject_Item_List.get(position).GetHR_BU());

            textView_model.setText("MS - " + OpenProject_Item_List.get(position).GetModel());

            textView_createdate.setText(OpenProject_Item_List.get(position).GetCreateDate());


            Log.w("DetailAdapter","test");

            return v;
        }

    }





}
