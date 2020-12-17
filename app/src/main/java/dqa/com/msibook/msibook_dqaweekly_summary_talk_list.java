package dqa.com.msibook;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class msibook_dqaweekly_summary_talk_list extends AppCompatActivity {

    private Context mContext;
    private ProgressDialog progressBar;

    private Get_Weekly_Content_Summary_Detail_Adapter Get_Weekly_Content_Summary_Detail_Adapter;
    private List<Get_Weekly_Content_Summary_Detail_Item> Get_Weekly_Content_Summary_Detail_Item_List = new ArrayList<Get_Weekly_Content_Summary_Detail_Item>();

    private ListView mListView;

    private Button btn_open_pop;
    private TextView textView_week;
    private TextView textView_dptname;

    private TextView txt_CreateDate;//主體留言日期
    private Button btn_edit;//下箭頭btn
    private TextView txt_Content;//主體內容
    private Button btn_lookmessage;//留言小圖案

    private ScrollView scroll_list;//Scrollview list
    private ListView listview_response;//留言訊息 List

    private EditText editText_sent_message;//下方回復訊息
    private Button button_sent;//送出按鈕

    private String GetWeek;
    private String GetDeptID;
    private String GetDeptName;
    private String GetWorkID;//
    private String GetF_CreateDate;
    private String GetF_Content;
    private String GetF_SeqNo;

    //讀取摘要內容
    public void Get_Weekly_Content_Summary_ActionItems(String F_Master_ID) {

        //顯示  讀取等待時間Bar
        progressBar.show();

        Get_Weekly_Content_Summary_Detail_Item_List.clear();

        RequestQueue mQueue = Volley.newRequestQueue(this);

        String Path = "http://wtsc.msi.com.tw/IMS/Weekly_Report.asmx/Get_Weekly_Content_Summary_Detail?F_Master_ID=" + F_Master_ID;

        getString(Path, mQueue, new GetServiceData.VolleyCallback() {
            @Override
            public void onSuccess(JSONObject result) {

                try {

                    JSONArray UserArray = new JSONArray(result.getString("Key"));

                    for (int i = 0; i < UserArray.length(); i++) {
                        JSONObject IssueData = UserArray.getJSONObject(i);

                        String F_SeqNo = String.valueOf(IssueData.getInt("F_SeqNo"));

                        String F_CreateDate = String.valueOf(IssueData.getString("F_CreateDate"));

                        String F_Keyin = String.valueOf(IssueData.getString("F_Keyin"));

                        String F_Content = String.valueOf(IssueData.getString("F_Content"));

                        String F_Master_ID = String.valueOf(IssueData.getInt("F_Master_ID"));

                        SimpleDateFormat dateFormatParse = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

                        F_CreateDate = F_CreateDate.substring(0,18);//取毫秒前方的字串

                        Date dateString = null;
                        String date_formate=null;
                        try {
                            dateString = dateFormatParse.parse(F_CreateDate);//文字轉日期
                            date_formate = dateFormatParse.format(dateString);//日期 Fromat 自訂格式 成字串

                        } catch (ParseException e) {
                            e.printStackTrace();
                            Log.w("eeeeee",e.getMessage());
                        }

                        Get_Weekly_Content_Summary_Detail_Item_List.add(i,new Get_Weekly_Content_Summary_Detail_Item(F_SeqNo,String.valueOf(date_formate).replace("T"," "),F_Keyin,F_Content,F_Master_ID));

                    }

                    mListView = (ListView) findViewById(R.id.listview_response);

                    Get_Weekly_Content_Summary_Detail_Adapter = new Get_Weekly_Content_Summary_Detail_Adapter(mContext,Get_Weekly_Content_Summary_Detail_Item_List);

                    mListView.setAdapter(Get_Weekly_Content_Summary_Detail_Adapter);


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


    // Insert message++++++++++++++++++++++++++++++++

    public static void SendPostRequest(String Url, RequestQueue mQueue, final GetServiceData.VolleyStringCallback callback, final Map<String, String> map) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                callback.onSendRequestSuccess(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                callback.onSendRequestError(error.getMessage());

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String, String> map = new HashMap<String, String>();
//                map.put("params1", "value1");
//                map.put("params2", "value2");
                return map;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        mQueue.add(stringRequest);
    }

    //新增摘要
    private void Insert_Weekly_Content_Summary_Detail(String F_Keyin,String F_Content,String F_Master_ID) {

        RequestQueue mQueue = Volley.newRequestQueue(this);

        Map<String, String> map = new HashMap<String, String>();
        map.put("F_Keyin", F_Keyin);
        map.put("F_Content", F_Content);
        map.put("F_Master_ID", F_Master_ID);

        if (mQueue == null) {
            mQueue = Volley.newRequestQueue(this);
        }

        String Path = "http://wtsc.msi.com.tw/IMS/Weekly_Report.asmx/Insert_Weekly_Content_Summary_Detail";

        SendPostRequest(Path, mQueue, new GetServiceData.VolleyStringCallback() {
            @Override
            public void onSendRequestSuccess(String result) {

//                Integer SummaryNumber =  Integer.valueOf(main_summary_number.getText().toString());
//
//                main_summary_number.setText(String.valueOf((SummaryNumber +1)));

                Get_Weekly_Content_Summary_ActionItems(GetF_SeqNo);

            }

            @Override
            public void onSendRequestError(String result) {



            }

        }, map);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msibook_dqaweekly_summary_talk_list);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        mContext = msibook_dqaweekly_summary_talk_list.this;

        //讀取時間Bar
        progressBar = new ProgressDialog(this.mContext);
        progressBar.setCancelable(true);
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.setMessage("資料載入中");

        Button btn_open_pop = (Button) findViewById(R.id.btn_open_pop);
        TextView textView_week = (TextView) findViewById(R.id.textView_week);
        TextView textView_dptname = (TextView) findViewById(R.id.textView_dptname);

        TextView txt_CreateDate = (TextView) findViewById(R.id.txt_CreateDate);
        Button btn_edit = (Button) findViewById(R.id.btn_edit);
        TextView txt_Content = (TextView) findViewById(R.id.txt_Content);
        Button btn_lookmessage = (Button) findViewById(R.id.btn_lookmessage);

        ListView listview_response = (ListView) findViewById(R.id.listview_response);

        final EditText editText_sent_message =(EditText) findViewById(R.id.editText_sent_message);
        Button button_sent = (Button) findViewById(R.id.button_sent);

        GetWeek =  getIntent().getStringExtra("Week").replace("週","");//抓週
        GetDeptID =  getIntent().getStringExtra("ChoiceDepID");//抓部門ID
        GetDeptName =  getIntent().getStringExtra("DeptName");//抓部門ID
        GetWorkID =  getIntent().getStringExtra("WorkID");//抓工號
        Log.w("GetWorkID",GetWorkID);
        GetF_CreateDate =  getIntent().getStringExtra("F_CreateDate");//抓工號
        GetF_Content =  getIntent().getStringExtra("F_Content");//抓工號
        GetF_SeqNo =  getIntent().getStringExtra("F_SeqNo");//主體序號
        Log.w("GetF_SeqNo",GetF_SeqNo);


        textView_week.setText(GetWeek+" 週");
        textView_dptname.setText(GetDeptName);
        txt_CreateDate.setText(GetF_CreateDate);
        txt_Content.setText(GetF_Content);

        Get_Weekly_Content_Summary_ActionItems(GetF_SeqNo);

        //儲存 點擊
        button_sent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Insert_Weekly_Content_Summary_Detail(GetWorkID,String.valueOf(editText_sent_message.getText()),GetF_SeqNo);
                editText_sent_message.setText("");
                Toast.makeText(msibook_dqaweekly_summary_talk_list.this, "資料已更新", Toast.LENGTH_SHORT).show();
                //editText_sent_message.clearFocus();
            }
        });


    }

    //------------Item-----------------
    public class Get_Weekly_Content_Summary_Detail_Item  {


        String F_SeqNo;

        String F_CreateDate;//2017-09-07T10:39:34.89

        String F_Keyin;

        String F_Content;// TestSummary

        String F_Master_ID;


        public Get_Weekly_Content_Summary_Detail_Item(String F_SeqNo,String F_CreateDate,String F_Keyin,String F_Content,String F_Master_ID)
        {
            this.F_SeqNo = F_SeqNo;

            this.F_CreateDate = F_CreateDate;

            this.F_Keyin = F_Keyin;

            this.F_Content = F_Content;

            this.F_Master_ID = F_Master_ID;

        }

        public String GetF_SeqNo()
        {
            return this.F_SeqNo;
        }

        public String GetF_CreateDate()
        {
            return this.F_CreateDate;
        }

        public String GetF_Keyin()
        {
            return this.F_Keyin;
        }

        public String GetF_Content()
        {
            return this.F_Content;
        }

        public String GetF_Master_ID()
        {
            return this.F_Master_ID;
        }


    }


    //-----------Adapter--------------
    public class Get_Weekly_Content_Summary_Detail_Adapter extends BaseAdapter {

        private LayoutInflater mLayInf;

        private List<Get_Weekly_Content_Summary_Detail_Item> Get_Weekly_Content_Summary_Detail_Item_List;

        private Context ProjectContext;

        private String Title;

        public Get_Weekly_Content_Summary_Detail_Adapter(Context context, List<Get_Weekly_Content_Summary_Detail_Item> Get_Weekly_Content_Summary_Detail_Item_List)
        {
            mLayInf = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            ProjectContext = context;

            this.Title = Title;

            this.Get_Weekly_Content_Summary_Detail_Item_List = Get_Weekly_Content_Summary_Detail_Item_List;

        }
        @Override
        public int getCount() {
            return Get_Weekly_Content_Summary_Detail_Item_List.size();
        }

        @Override
        public Object getItem(int position) {
            return Get_Weekly_Content_Summary_Detail_Item_List.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View v = new View(ProjectContext);

            v = mLayInf.inflate(R.layout.msibook_dqaweekly_get_weekly_content_summary_detail_item, parent, false);

            TextView textview_name = (TextView) v.findViewById(R.id.textview_name);
            TextView textview_creatdate = (TextView) v.findViewById(R.id.textview_creatdate);
            TextView textview_content = (TextView) v.findViewById(R.id.textview_content);

            textview_name.setText(Get_Weekly_Content_Summary_Detail_Item_List.get(position).GetF_Keyin());

            textview_creatdate.setText(Get_Weekly_Content_Summary_Detail_Item_List.get(position).GetF_CreateDate());

            textview_content.setText(Get_Weekly_Content_Summary_Detail_Item_List.get(position).GetF_Content());



            return v;
        }

    }


}