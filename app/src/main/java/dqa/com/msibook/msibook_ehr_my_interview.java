package dqa.com.msibook;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class msibook_ehr_my_interview extends AppCompatActivity {

    private ListView list_my_interview;

    private ProgressDialog progressBar;
    private ProgressDialog progressBarDB;

    private Context mContext;

    private My_Interview_Adapter mMy_Interview_Adapter;
    private List<my_Interview_Item> my_Interview_Item_List = new ArrayList<my_Interview_Item>();

    private String Set_F_Introduce;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msibook_ehr_my_interview);

        progressBar = new ProgressDialog(this);
        progressBar.setCancelable(true);
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.setMessage("資料載入中");

        mContext = msibook_ehr_my_interview.this;

        Select_My_InterView(UserData.WorkID);

    }

    //DB編制細部清單
    private void Select_My_InterView(String F_Keyin) {

        //顯示  讀取等待時間Bar
        progressBar.show();

        my_Interview_Item_List.clear();

        RequestQueue mQueue = Volley.newRequestQueue(this);

        HTTPSTrustManager.allowAllSSL();//信任所有证书，信任憑證
        String Path = GetServiceData.ServicePath + "/Select_My_InterView?F_Keyin=" + F_Keyin;

        GetServiceData.getString(Path, mQueue, new GetServiceData.VolleyCallback() {
            @Override
            public void onSuccess(JSONObject result) {

                try {

                    JSONArray UserArray = new JSONArray(result.getString("Key"));

                    for (int i = 0; i < UserArray.length(); i++) {
                        JSONObject IssueData = UserArray.getJSONObject(i);

                        String F_SeqNo = String.valueOf(IssueData.getInt("F_SeqNo"));

                        String F_DeptCode = String.valueOf(IssueData.getString("F_DeptCode"));

                        String F_DeptName = String.valueOf(IssueData.getString("F_DeptName"));

                        String F_Job_Name = String.valueOf(IssueData.getString("F_Job_Name"));

                        if (IssueData.isNull("F_Introduce")) {
                            String F_Introduce = "";
                            Set_F_Introduce = "";
                        } else {
                            String F_Introduce = String.valueOf(IssueData.getString("F_Introduce"));
                            Set_F_Introduce = F_Introduce;
                        }


                        my_Interview_Item_List.add(i,new my_Interview_Item(F_SeqNo,F_DeptCode,F_DeptName,F_Job_Name,Set_F_Introduce));

                    }

                    list_my_interview = (ListView)findViewById(R.id.list_my_interview);

                    mMy_Interview_Adapter = new My_Interview_Adapter(mContext, my_Interview_Item_List);

                    list_my_interview.setAdapter(mMy_Interview_Adapter);

                    //關閉-讀取等待時間Bar
                    progressBar.dismiss();

                }
                catch (JSONException ex) {

                    Log.w("Json",ex.toString());
                }

            }
        });

    }

    //----------------Item
    public class my_Interview_Item {

        String F_SeqNo;

        String F_DeptCode;

        String F_DeptName;

        String F_Job_Name;

        String F_Introduce;

        public my_Interview_Item(String F_SeqNo,String F_DeptCode,String F_DeptName,String F_Job_Name,String F_Introduce)
        {
            this.F_SeqNo = F_SeqNo;

            this.F_DeptCode = F_DeptCode;

            this.F_DeptName = F_DeptName;

            this.F_Job_Name = F_Job_Name;

            this.F_Introduce = F_Introduce;
        }
        public String GetF_SeqNo()
        {
            return this.F_SeqNo;
        }

        public String GetF_DeptCode()
        {
            return this.F_DeptCode;
        }

        public String GetF_DeptName()
        {
            return this.F_DeptName;
        }

        public String GetF_Job_Name()
        {
            return this.F_Job_Name;
        }

        public String GetF_Introduce()
        {
            return this.F_Introduce;
        }
    }


    //----------------Adapter

    public class My_Interview_Adapter extends BaseAdapter {

        private LayoutInflater mLayInf;

        private List<my_Interview_Item> my_Interview_Item_List;

        private Context Interview_Context;

        private String Title;

        public My_Interview_Adapter(Context context,  List<my_Interview_Item> my_Interview_Item_List)
        {
            mLayInf = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            Interview_Context = context;

            this.Title = Title;

            this.my_Interview_Item_List = my_Interview_Item_List;

        }
        @Override
        public int getCount() {
            return my_Interview_Item_List.size();
        }

        @Override
        public Object getItem(int position) {
            return my_Interview_Item_List.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View v = new View(Interview_Context);

            v = mLayInf.inflate(R.layout.msibook_ehr_my_interview_item, parent, false);

            TextView textView_DeptName = (TextView) v.findViewById(R.id.textView_DeptName);
            TextView textView_Job_Name = (TextView) v.findViewById(R.id.textView_Job_Name);
            TextView textView_job_life_stats = (TextView) v.findViewById(R.id.textView_job_life_stats);

            textView_DeptName.setText(my_Interview_Item_List.get(position).GetF_DeptName());

            textView_Job_Name.setText(my_Interview_Item_List.get(position).GetF_Job_Name());

            textView_job_life_stats.setText("審核中");

            return v;
        }

    }


}
