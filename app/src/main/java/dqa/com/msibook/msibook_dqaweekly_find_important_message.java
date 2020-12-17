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
import android.webkit.WebSettings;
import android.webkit.WebView;
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
 * {@link msibook_dqaweekly_find_important_message.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link msibook_dqaweekly_find_important_message#newInstance} factory method to
 * create an instance of this fragment.
 */
public class msibook_dqaweekly_find_important_message extends Fragment {
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

    private Important_Data_Adapter mImportant_Data_Adapter;
    private List<Important_Data_Item> Important_Data_Item_List = new ArrayList<Important_Data_Item>();

    private ListView mListView;

    private OnFragmentInteractionListener mListener;

    public msibook_dqaweekly_find_important_message() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Find_important_message.
     */
    // TODO: Rename and change types and number of parameters
    public static msibook_dqaweekly_find_important_message newInstance(String param1, String param2) {
        msibook_dqaweekly_find_important_message fragment = new msibook_dqaweekly_find_important_message();
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
        mView = inflater.inflate(R.layout.fragment_msibook_dqaweekly_find_important_message, container, false);

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

    public void Find_Important_Message(String Year,String Week) {

        //顯示  讀取等待時間Bar
        progressBar.show();

        Important_Data_Item_List.clear();

        RequestQueue mQueue = Volley.newRequestQueue(getActivity());

        String Path;
        if(Integer.valueOf(Week)==1){
            Path = "http://wtsc.msi.com.tw/IMS/Weekly_Report.asmx/Find_Important_Message?Year=" + String.valueOf(Integer.valueOf(Year)-1) + "&Week=" + "53";
        }else {
            Path = "http://wtsc.msi.com.tw/IMS/Weekly_Report.asmx/Find_Important_Message?Year="+ Year +"&Week="+ Week;
        }

        getString(Path, mQueue, new GetServiceData.VolleyCallback() {
            @Override
            public void onSuccess(JSONObject result) {

                try {
                    JSONArray UserArray = new JSONArray(result.getString("Key"));

                    for (int i = 0; i < UserArray.length(); i++) {

                        JSONObject IssueData = UserArray.getJSONObject(i);

                        String Column1 = String.valueOf(IssueData.getString("Column1")); //

                        String F_Desc = String.valueOf(IssueData.getString("F_Desc")); //

                        Important_Data_Item_List.add(i,new Important_Data_Item(Column1,F_Desc));

                    }

                    mListView = (ListView)getActivity().findViewById(R.id.listview_important_data);

                    mImportant_Data_Adapter = new Important_Data_Adapter(mContext,Important_Data_Item_List);

                    mListView.setAdapter(mImportant_Data_Adapter);

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
    public class Important_Data_Item  {

        String Column1;

        String F_Desc;


        public Important_Data_Item(String Column1,String F_Desc)
        {
            this.Column1 = Column1;

            this.F_Desc = F_Desc;

        }

        public String GetColumn1()
        {
            return this.Column1;
        }

        public String GetF_Desc()
        {
            return this.F_Desc;
        }

    }

    //------------------------------------Adapter-------------------------------------

    public class Important_Data_Adapter extends BaseAdapter {

        private LayoutInflater mLayInf;

        private List<Important_Data_Item> Important_Data_Item_List;

        private Context Important_DataContext;

        private String Title;

        public Important_Data_Adapter(Context context, List<Important_Data_Item> Important_Data_Item_List) {
            mLayInf = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            Important_DataContext = context;

            this.Title = Title;

            this.Important_Data_Item_List = Important_Data_Item_List;

        }

        @Override
        public int getCount() {
            return Important_Data_Item_List.size();
        }

        @Override
        public Object getItem(int position) {
            return Important_Data_Item_List.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View v = new View(Important_DataContext);

            v = mLayInf.inflate(R.layout.msibook_dqaweekly_important_data_adapter, parent, false);

            TextView textView_Column1 = (TextView) v.findViewById(R.id.textView_Column1);
            WebView webview_F_Desc = (WebView) v.findViewById(R.id.webview_F_Desc);
//            webview_F_Desc.setScrollContainer(false);
//            webview_F_Desc.setVerticalScrollBarEnabled(false);
//            webview_F_Desc.setHorizontalScrollBarEnabled(false);
//
//            webview_F_Desc.setOnTouchListener(new View.OnTouchListener() {
//                @Override
//                public boolean onTouch(View v, MotionEvent event) {
//                    return (event.getAction() == MotionEvent.ACTION_MOVE);
//                }
//            });

            //抓圖片路徑 把圖片前面加上 http來顯示圖片
            int textString = Important_Data_Item_List.get(position).GetF_Desc().indexOf("//172");
            StringBuffer sb = new StringBuffer(Important_Data_Item_List.get(position).GetF_Desc());
            if(textString!=-1){
                sb.insert(textString,"http:");
            }else{
            }

            final String mimeType = "text/html";
            final String encoding = "UTF-8";
            WebSettings settings = webview_F_Desc.getSettings();
            webview_F_Desc.setInitialScale(350);
            settings.setBlockNetworkImage(false);//解决图片不显示
            //settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
            settings.setJavaScriptEnabled(true);
            settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);



            textView_Column1.setText(Important_Data_Item_List.get(position).GetColumn1());
            webview_F_Desc.loadDataWithBaseURL("", String.valueOf(sb), mimeType, encoding, "");

            Log.w("String Length",String.valueOf(Important_Data_Item_List.get(position).GetF_Desc().length()));
            //webview_F_Desc.setText(Html.fromHtml(Important_Data_Item_List.get(position).GetF_Desc().replace(": ","="), Html.FROM_HTML_MODE_COMPACT));

            //字數限制
//            double valueLength = 0;
//            String chinese = "[\u4e00-\u9fa5]";
//            for (int i = 0; i < Important_Data_Item_List.get(position).GetF_Desc().length(); i++) {
//                // 获取一个字符
//                String temp = Important_Data_Item_List.get(position).GetF_Desc().substring(i, i + 1);
//                // 判断是否为中文字符
//                if (temp.matches(chinese)) {
//                    // 中文字符长度为1
//                    valueLength += 1;
//                } else {
//                    // 其他字符长度为0.5
//                    //valueLength += 0.5;
//                }
//            }
//            //进位取整
//            Log.w("webview lenght",String.valueOf(Math.ceil(valueLength)));
//
//            if(Math.ceil(valueLength)>120){
//                webview_F_Desc.loadDataWithBaseURL("","<html><head>"
//                        + "<style type=\"text/css\">body{color:#828795; background-color: #fff;}"
//                        + "</style></head>"
//                        + "<body>"
//                        + "由於字數超過200字，請至網頁版查看。"+ Important_Data_Item_List.get(position).GetF_Desc().substring(0,800)
//                        + "</body></html>", mimeType, encoding, "");
//            }



            return v;
        }
    }



}
