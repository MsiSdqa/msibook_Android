package dqa.com.msibook;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class msibook_jobs_single_class extends AppCompatActivity {


    private List<eHR_Job_Item> eHR_Job_Item_list = new ArrayList<eHR_Job_Item>();
    private eHR_JobAdapter meHR_JobAdapter;
    private Context mContext;
    private ProgressDialog progressBar;

    private ListView mListView;

    private String F_Job_Type;
    private TextView toolbar_title;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msibook_jobs_single_class);

        //讀取時間Bar
        progressBar = new ProgressDialog(this);
        progressBar.setCancelable(true);
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.setMessage("資料載入中");

        //list的Title
        TextView title1 = (TextView) findViewById(R.id.textView_listtop);

        mContext = msibook_jobs_single_class.this;
        mListView = (ListView) findViewById(R.id.list2);

        toolbar_title = (TextView) findViewById(R.id.toolbar_title);


        String getF_Job_Type = getIntent().getStringExtra("F_Job_Type");//抓第一頁選的部門代號
        F_Job_Type = getF_Job_Type;
        Log.w("DeptID",F_Job_Type);

        toolbar_title.setText(getF_Job_Type+"職缺資訊");

        //eHR_single_job_list

        Select_HR_List(F_Job_Type);
    }

    private void Select_HR_List(final String Type) {

        //顯示  讀取等待時間Bar
        progressBar.show();

        eHR_Job_Item_list.clear();

        RequestQueue mQueue = Volley.newRequestQueue(this);

        String Path = GetServiceData.ServicePath + "/Select_HR_List";

        GetServiceData.getString(Path, mQueue, new GetServiceData.VolleyCallback() {
            @Override
            public void onSuccess(JSONObject result) {

                try {

                    JSONArray UserArray = new JSONArray(result.getString("Key"));

                    int j=0;

                    for (int i = 0; i < UserArray.length(); i++) {


                        JSONObject IssueData = UserArray.getJSONObject(i);

                        String F_SeqNo = String.valueOf(IssueData.getInt("F_SeqNo"));// 1

                        String F_CreateDate = String.valueOf(IssueData.getString("F_CreateDate"));// 1

                        String F_UpdateDate = String.valueOf(IssueData.getString("F_UpdateDate"));// 1

                        String F_Keyin = String.valueOf(IssueData.getString("F_Keyin"));// 1

                        String F_M_Type = String.valueOf(IssueData.getString("F_M_Type"));// 1

                        String F_Job_Type = String.valueOf(IssueData.getString("F_Job_Type"));// 1

                        String F_Job_Name = String.valueOf(IssueData.getString("F_Job_Name"));// 1

                        String F_Job_Content = String.valueOf(IssueData.getString("F_Job_Content"));// 1

                        if(F_Job_Type.contains(Type)){
                            eHR_Job_Item_list.add(j,new eHR_Job_Item(F_SeqNo,F_CreateDate,F_UpdateDate,F_Keyin,F_M_Type,F_Job_Type,F_Job_Name,F_Job_Content));
                            j++;
                        }

                    }

                    mListView = (ListView)findViewById(R.id.eHR_single_job_list);

                    meHR_JobAdapter = new eHR_JobAdapter(mContext,eHR_Job_Item_list);

                    mListView.setAdapter(meHR_JobAdapter);

                    //關閉-讀取等待時間Bar
                    progressBar.dismiss();

                    //關閉-讀取等待時間Bar
                    progressBar.dismiss();


                }
                catch (JSONException ex) {

                    Log.w("Json",ex.toString());

                }

            }
        });


    }

    //WeekProjectAdapter
    public class eHR_JobAdapter extends BaseAdapter {

        private LayoutInflater mLayInf;

        private List<eHR_Job_Item> eHR_Job_Item_list;

        private Context ProjectContext;


        public eHR_JobAdapter(Context context,  List<eHR_Job_Item> eHR_Job_Item_list)
        {
            mLayInf = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            ProjectContext = context;

            this.eHR_Job_Item_list = eHR_Job_Item_list;

        }
        @Override
        public int getCount() {
            return eHR_Job_Item_list.size();
        }

        @Override
        public Object getItem(int position) {
            return eHR_Job_Item_list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            View v = new View(ProjectContext);

            v = mLayInf.inflate(R.layout.msibook_ehr_single_type_item, parent, false);

            TextView textView_dpt_name = (TextView) v.findViewById(R.id.textView_dpt_name);
            TextView textView_dpt_name2 = (TextView) v.findViewById(R.id.textView_dpt_name2);
            TextView textView_i_wanna = (TextView) v.findViewById(R.id.textView_i_wanna);

            textView_dpt_name.setText(eHR_Job_Item_list.get(position).GetF_Job_Name());
            textView_dpt_name2.setText(eHR_Job_Item_list.get(position).GetF_Job_Content());

            textView_i_wanna.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();

                    intent.putExtra("F_Keyin", eHR_Job_Item_list.get(position).GetF_Keyin());

                    intent.putExtra("F_SeqNo", eHR_Job_Item_list.get(position).GetF_SeqNo());

//                    intent.putExtra("F_DeptCode", eHR_Job_Item_list.get(position).GetF_DeptCode());
//
//                    intent.putExtra("DeptName", eHR_Job_Item_list.get(position).GetDeptName());

                    intent.putExtra("F_Job_Name", eHR_Job_Item_list.get(position).GetF_Job_Name());

                    intent.putExtra("F_Job_Content", eHR_Job_Item_list.get(position).GetF_Job_Content());

//                    intent.putExtra("F_Job_People", eHR_Job_Item_list.get(position).GetF_Job_People());

                    intent.setClass(msibook_jobs_single_class.this, msibook_ehr_check_interview_detial_new.class);
                    //開啟Activity
                    startActivityForResult(intent,1);
                }
            });

            Log.w("WeekProjectAdapter","test");

            return v;
        }

    }

    public class eHR_Job_Item {

        String F_SeqNo;

        String F_CreateDate;

        String F_UpdateDate;

        String F_Keyin;

        String F_M_Type;

        String F_Job_Type;

        String F_Job_Name;

        String F_Job_Content;


        public eHR_Job_Item(String F_SeqNo,
                                     String F_CreateDate,
                                     String F_UpdateDate,
                                     String F_Keyin,
                                     String F_M_Type,
                                     String F_Job_Type,
                                     String F_Job_Name,
                                     String F_Job_Content)
        {
            this.F_SeqNo = F_SeqNo;

            this.F_CreateDate = F_CreateDate;

            this.F_UpdateDate = F_UpdateDate;

            this.F_Keyin = F_Keyin;

            this.F_M_Type = F_M_Type;

            this.F_Job_Type = F_Job_Type;

            this.F_Job_Name = F_Job_Name;

            this.F_Job_Content = F_Job_Content;

        }


        public String GetF_SeqNo()
        {
            return this.F_SeqNo;
        }

        public String GetF_CreateDate()
        {
            return this.F_CreateDate;
        }

        public String GetF_UpdateDate()
        {
            return this.F_UpdateDate;
        }

        public String GetF_Keyin()
        {
            return this.F_Keyin;
        }

        public String GetF_M_Type()
        {
            return this.F_M_Type;
        }

        public String GetF_Job_Type()
        {
            return this.F_Job_Type;
        }

        public String GetF_Job_Name()
        {
            return this.F_Job_Name;
        }

        public String GetF_Job_Content()
        {
            return this.F_Job_Content;
        }

    }


}
