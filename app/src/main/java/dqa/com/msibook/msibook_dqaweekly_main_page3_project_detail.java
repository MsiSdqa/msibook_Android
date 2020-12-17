package dqa.com.msibook;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
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

public class msibook_dqaweekly_main_page3_project_detail extends AppCompatActivity {


    private List<Find_Model_Member_Item> Find_Model_Member_Item_list = new ArrayList<Find_Model_Member_Item>();

    private FindModelMemberAdapter mFindModelMemberAdapter;
    private ProgressDialog progressBar;
    private ListView mListView;
    private Context mContext;
    private String m4getDepid;
    private String m4getYear;


    //專案細部清單
    private void Find_Model_Member_Item(String ModelIDList,String Year,String Model_week) {

        Find_Model_Member_Item_list.clear();
        //顯示  讀取等待時間Bar
        progressBar.show();

        RequestQueue mQueue = Volley.newRequestQueue(this);

        String Path = "http://wtsc.msi.com.tw/IMS/Weekly_Report.asmx/Find_RSS_Model_Member_List_Dept?ModelID=" + ModelIDList + "&Year=" + Year + "&DeptID=" + m4getDepid + "&Week=" + Model_week;

        getString(Path, mQueue, new GetServiceData.VolleyCallback() {
            @Override
            public void onSuccess(JSONObject result) {

                try {

                    JSONArray UserArray = new JSONArray(result.getString("Key"));

                    for (int i = 0; i < UserArray.length(); i++) {
                        JSONObject IssueData = UserArray.getJSONObject(i);

                        String WorkID = String.valueOf(IssueData.getString("WorkID"));

                        String MemberCName = String.valueOf(IssueData.getString("MemberCName"));

                        String MemberEName = String.valueOf(IssueData.getString("MemberEName"));

//                        String Header = String.valueOf(IssueData.getString("Header"));
//
//                        String F_Tel = String.valueOf(IssueData.getString("F_Tel"));

                        Find_Model_Member_Item_list.add(i,new Find_Model_Member_Item(WorkID,MemberCName,MemberEName));

                    }

                    mListView = (ListView)findViewById(R.id.list_m4);

                    //mOverhourAdapter = new OverhourAdapter(mContext,Overhour_Item_List,MainTitle);
                    mFindModelMemberAdapter = new FindModelMemberAdapter(mContext,Find_Model_Member_Item_list);

                    mListView.setAdapter(mFindModelMemberAdapter);

                    //關閉-讀取等待時間Bar
                    progressBar.dismiss();


                    mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        }
                    });

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



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msibook_dqaweekly_main_page3_project_detail);
        mContext = msibook_dqaweekly_main_page3_project_detail.this;

        //讀取時間Bar
        progressBar = new ProgressDialog(this);
        progressBar.setCancelable(true);
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.setMessage("資料載入中");

        TextView mantitl = (TextView) findViewById(R.id.m4textView_listtop);
        TextView textshowwk = (TextView) findViewById(R.id.m4textViewshowweek);
        TextView textshowdp = (TextView) findViewById(R.id.m4textViewshowdepart);

        String getm3putEtrawk = getIntent().getStringExtra("m3Week");//讀取前頁週次 EX第三週
        textshowwk.setText(getm3putEtrawk + "週");

        String getm3EtraDepID = getIntent().getStringExtra("m3ChoiceDepID");//部門代號 EX:21751
        m4getDepid = getm3EtraDepID;
        Log.w("getm4DepID:", getm3EtraDepID);

        String getm3EtraYear = getIntent().getStringExtra("m3Year");//部門代號 EX:21751
        m4getYear = getm3EtraYear;

        String getm2putTitle = getIntent().getStringExtra("m3putTitle");//讀取前頁部門名稱 EX 7861 驗證二部一課
        textshowdp.setText(getm2putTitle);

        String getm3ModelList = getIntent().getStringExtra("m3ModelList");//抓 MS-7A61  MS-7A81
        Log.w("getm3ModelList", getm3ModelList);
        String getm3ModelIDList = getIntent().getStringExtra("m3ModelIDList");// 抓  12637  12638
        Log.w("getm3ModelIDList", getm3ModelIDList);


        mantitl.setText(getm3ModelList);
        Find_Model_Member_Item(getm3ModelIDList,getm3EtraYear,getm3putEtrawk);

    }

    //----------------------------------Item-----------------------------------

    public class Find_Model_Member_Item {

        String WorkID;//10013875

        String MemberCName;// 王佑仁

        String MemberEName;//dashwang

        public Find_Model_Member_Item(String WorkID,String MemberCName,String MemberEName)
        {
            this.WorkID = WorkID;

            this.MemberCName = MemberCName;

            this.MemberEName = MemberEName;

        }
        public String GetWorkID()
        {
            return this.WorkID;
        }

        public String GetMemberCName()
        {
            return this.MemberCName;
        }

        public String GetMemberEName()
        {
            return this.MemberEName;
        }

    }

    //---------------------------------Adapter--------------------------------
    public class FindModelMemberAdapter extends BaseAdapter {

        private LayoutInflater mLayInf;

        private List<Find_Model_Member_Item> Find_Model_Member_Item_list;

        private Context ProjectContext;

        private String Title;

        public FindModelMemberAdapter(Context context,  List<Find_Model_Member_Item> Find_Model_Member_Item_list)
        {
            mLayInf = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            ProjectContext = context;

            this.Title = Title;

            this.Find_Model_Member_Item_list = Find_Model_Member_Item_list;

        }
        @Override
        public int getCount() {
            return Find_Model_Member_Item_list.size();
        }

        @Override
        public Object getItem(int position) {
            return Find_Model_Member_Item_list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View v = new View(ProjectContext);

            v = mLayInf.inflate(R.layout.msibook_dqaweekly_adapter4_project_item, parent, false);

            TextView textView1 = (TextView) v.findViewById(R.id.textView1);
            TextView textView2 = (TextView) v.findViewById(R.id.textView2);
            Button button1 = (Button) v.findViewById(R.id.button1);


            textView1.setText(Find_Model_Member_Item_list.get(position).GetMemberCName() +"  "+ Find_Model_Member_Item_list.get(position).GetMemberEName());
            //textView2.setText(Find_Model_Member_Item_list.get(position).GetHeader());
            button1.setVisibility(View.INVISIBLE);


            Log.w("WeekProjectAdapter","test");

            return v;
        }

    }

}

