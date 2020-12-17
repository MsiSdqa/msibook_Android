package dqa.com.msibook;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class msibook_ehr_candidate_list extends AppCompatActivity {

    private Context mContext;
    private ProgressDialog progressBar;

    private TextView textView_dpt_code;
    private TextView textView_dpt_name;
    private TextView textView_job_name;

    private ListView list_candidate_people;

    private Candidate_Adapter mCandidate_Adapter;
    private List<Candidate_people_Item> Candidate_people_Item_list = new ArrayList<Candidate_people_Item>();

    private String getF_SeqNo;
    private String getF_Job_Name;
    private String getF_Dpt_Name;
    private String getF_Job_DptCode;

    private  String Set_F_Introduce;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msibook_ehr_candidate_list);

        progressBar = new ProgressDialog(this);
        progressBar.setCancelable(true);
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.setMessage("資料載入中");

        mContext = msibook_ehr_candidate_list.this;

        textView_dpt_code = (TextView) findViewById(R.id.textView_dpt_code);//Title\
        textView_dpt_name = (TextView) findViewById(R.id.textView_dpt_name);//Title\
        textView_job_name = (TextView) findViewById(R.id.textView_job_name);//Title\

        list_candidate_people = (ListView)findViewById(R.id.list_candidate_people);

        getF_SeqNo = getIntent().getStringExtra("F_SeqNo");////職缺序號

        getF_Job_DptCode = getIntent().getStringExtra("F_DeptCode");//部門代號
        getF_Dpt_Name = getIntent().getStringExtra("F_Job_Dept");////部門名稱

        getF_Job_Name = getIntent().getStringExtra("F_Job_Name");//職缺名稱

        textView_dpt_code.setText(getF_Job_DptCode);
        textView_dpt_name.setText(getF_Dpt_Name);
        textView_job_name.setText(getF_Job_Name);


        Select_E_HR_Application_Detail(getF_SeqNo);


        list_candidate_people.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

//                Intent intent = new Intent();
//
//                intent.putExtra("Dept", Candidate_people_Item_list.get(position).GetDept());
//
//                intent.putExtra("F_Keyin",Candidate_people_Item_list.get(position).GetF_Keyin());
//
//                intent.putExtra("Name", Candidate_people_Item_list.get(position).GetName());
//
//                intent.putExtra("F_Introduce", Candidate_people_Item_list.get(position).GetF_Introduce());
//
//                intent.setClass(msibook_ehr_candidate_list.this, msibook_ehr_show_canditate_detial.class);
//                //開啟Activity
//                startActivity(intent);
            }
        });

    }

    //部門職缺資訊
    private void Select_E_HR_Application_Detail(String F_Master_ID) {

        //顯示  讀取等待時間Bar
        progressBar.show();

        Candidate_people_Item_list.clear();

        RequestQueue mQueue = Volley.newRequestQueue(this);

        HTTPSTrustManager.allowAllSSL();//信任所有证书，信任憑證
        String Path = GetServiceData.ServicePath +"/Select_E_HR_Application_Detail?F_Master_ID=" + F_Master_ID;

        GetServiceData.getString(Path, mQueue, new GetServiceData.VolleyCallback() {
            @Override
            public void onSuccess(JSONObject result) {

                try {

                    JSONArray UserArray = new JSONArray(result.getString("Key"));

                    for (int i = 0; i < UserArray.length(); i++) {

                        JSONObject IssueData = UserArray.getJSONObject(i);

                        String F_Master_ID = String.valueOf(IssueData.getInt("F_Master_ID"));

                        String F_CreateDate = String.valueOf(IssueData.getString("F_CreateDate"));

                        String F_Job_Dept = String.valueOf(IssueData.getString("F_Job_Dept"));

                        String F_Job_vacancies = String.valueOf(IssueData.getString("F_Job_vacancies"));

                        String Dept = String.valueOf(IssueData.getString("Dept"));

                        String Application = String.valueOf(IssueData.getString("Application"));

                        String Boss = String.valueOf(IssueData.getString("Boss"));

                        Candidate_people_Item_list.add(i,new Candidate_people_Item(F_Master_ID,F_CreateDate,F_Job_Dept,F_Job_vacancies,Dept,Application,Boss));

                    }
                    mCandidate_Adapter = new Candidate_Adapter(mContext, Candidate_people_Item_list);

                    list_candidate_people.setAdapter(mCandidate_Adapter);

                    //關閉-讀取等待時間Bar
                    progressBar.dismiss();
                }
                catch (JSONException ex) {

                    Log.w("Json",ex.toString());
                }

            }
        });


    }

    // --------------------------------------------Item--------------------------------------------
    public class Candidate_people_Item {

        String F_Master_ID;

        String F_CreateDate;

        String F_Job_Dept;

        String F_Job_vacancies;

        String Dept;

        String Application;

        String Boss;

        public Candidate_people_Item(String F_Master_ID,String F_CreateDate,String F_Job_Dept,String F_Job_vacancies,String Dept,String Application,String Boss)
        {
            this.F_Master_ID = F_Master_ID;

            this.F_CreateDate = F_CreateDate;

            this.F_Job_Dept = F_Job_Dept;

            this.F_Job_vacancies = F_Job_vacancies;

            this.Dept = Dept;

            this.Application = Application;

            this.Boss = Boss;
        }


        public String GetF_Master_ID()
        {
            return this.F_Master_ID;
        }

        public String GetF_CreateDate()
        {
            return this.F_CreateDate;
        }

        public String GetF_Job_Dept()
        {
            return this.F_Job_Dept;
        }

        public String GetF_Job_vacancies()
        {
            return this.F_Job_vacancies;
        }

        public String GetDept()
        {
            return this.Dept;
        }

        public String GetApplication()
        {
            return this.Application;
        }

        public String GetBoss()
        {
            return this.Boss;
        }
    }

    // --------------------------------------------Adapter-------------------------------------------
    public class Candidate_Adapter extends BaseAdapter {

        private LayoutInflater mLayInf;

        private List<Candidate_people_Item> Candidate_people_Item_list;

        private Context Candidate_people;

        private String Title;

        public Candidate_Adapter(Context context,  List<Candidate_people_Item> Candidate_people_Item_list)
        {
            mLayInf = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            Candidate_people = context;

            this.Title = Title;

            this.Candidate_people_Item_list = Candidate_people_Item_list;

        }
        @Override
        public int getCount() {
            return Candidate_people_Item_list.size();
        }

        @Override
        public Object getItem(int position) {
            return Candidate_people_Item_list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View v = new View(Candidate_people);

            v = mLayInf.inflate(R.layout.msibook_ehr_candidate_adapter_item, parent, false);

            TextView textView_dpt_name = (TextView) v.findViewById(R.id.textView_dpt_name);
            TextView textView_user = (TextView) v.findViewById(R.id.textView_user);
            TextView textView_user_boss = (TextView) v.findViewById(R.id.textView_user_boss);

            textView_dpt_name.setText(Candidate_people_Item_list.get(position).GetDept());
            textView_user.setText(Candidate_people_Item_list.get(position).GetApplication());
            textView_user_boss.setText(Candidate_people_Item_list.get(position).GetBoss());



            return v;
        }

    }

}
