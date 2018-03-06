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
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class msibook_ehr_main extends AppCompatActivity {

    private Button btn_creat_pop;
    private TextView toolbar_title;
    private Button btn_add_new_job;
    private ListView eHR_main_list;
    private ProgressDialog progressBar;

    private Job_Dpt_Adapter mJob_Dpt_Adapter;
    private List<Job_Dpt_Item> Job_Dpt_Item_List = new ArrayList<Job_Dpt_Item>();

    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msibook_ehr_main);

        progressBar = new ProgressDialog(this);
        progressBar.setCancelable(true);
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.setMessage("資料載入中");

        mContext = msibook_ehr_main.this;

        btn_creat_pop = (Button) findViewById(R.id.btn_creat_pop);//漢堡列
        toolbar_title = (TextView) findViewById(R.id.toolbar_title);//Title
        btn_add_new_job = (Button) findViewById(R.id.btn_add_new_job);//新增
        eHR_main_list = (ListView) findViewById(R.id.eHR_main_list);//List

//        Job_Dpt_Item_List.add(0,new Job_Dpt_Item("76A0","電磁相容暨安規三部","1"));
//        Job_Dpt_Item_List.add(1,new Job_Dpt_Item("7001","技術文件管制中心","1"));
//        Job_Dpt_Item_List.add(2,new Job_Dpt_Item("A811","A811硬體課","1"));
//        Job_Dpt_Item_List.add(3,new Job_Dpt_Item("A2D1","A2D1 ACE業務一課","1"));
//        Job_Dpt_Item_List.add(4,new Job_Dpt_Item("0007","資訊中心","1"));
//        Job_Dpt_Item_List.add(5,new Job_Dpt_Item("B120","硬體單位","2"));
//        Job_Dpt_Item_List.add(6,new Job_Dpt_Item("6D94","專案管理課","1"));
//
//        mJob_Dpt_Adapter = new Job_Dpt_Adapter(this, Job_Dpt_Item_List);
//
//        eHR_main_list.setAdapter(mJob_Dpt_Adapter);

        Select_E_HR_Master();

        //加班細項to 第三頁
        eHR_main_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent();

                intent.putExtra("Dpt_number",Job_Dpt_Item_List.get(position).GetDpt_number());

                intent.putExtra("Dpt_name",Job_Dpt_Item_List.get(position).GetDpt_name());//給第三頁員工 工號

                intent.putExtra("Count",Job_Dpt_Item_List.get(position).GetCount());//給第三頁員工 工號

                intent.setClass(msibook_ehr_main.this, msibook_ehr_main_page2.class);
                //開啟Activity
                startActivity(intent);

            }
        });

        //新增Job 點選事件
        btn_add_new_job.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int []location=new int[2];
                v.getLocationOnScreen(location);
                int x=location[0];//获取当前位置的横坐标
                int y=location[1];

                Intent intent = new Intent(msibook_ehr_main.this, msibook_ehr_new_job_add.class);

//                intent.putExtra("x_Location",String.valueOf(x));
//                Log.w("x_Location",String.valueOf(x));
//
//                intent.putExtra("y_Location",String.valueOf(y));
//                Log.w("y_Location",String.valueOf(y));
//
//                intent.putExtra("putManpower_status",putManpower_status);
//
//                intent.putExtra("putWorkhour_status",putWorkhour_status);
//
//                intent.putExtra("putMessage",putMessage);

                msibook_ehr_main.this.startActivity(intent);

            }
        });

    }


    //DB編制細部清單
    private void Select_E_HR_Master() {

        //顯示  讀取等待時間Bar
        progressBar.show();

        Job_Dpt_Item_List.clear();

        RequestQueue mQueue = Volley.newRequestQueue(this);

        String Path = GetServiceData.ServicePath + "/Select_E_HR_Master";

        GetServiceData.getString(Path, mQueue, new GetServiceData.VolleyCallback() {
            @Override
            public void onSuccess(JSONObject result) {

                try {

                    JSONArray UserArray = new JSONArray(result.getString("Key"));

                    for (int i = 0; i < UserArray.length(); i++) {
                        JSONObject IssueData = UserArray.getJSONObject(i);

                        String DeptName = String.valueOf(IssueData.getString("DeptName"));

                        String DeptCode = String.valueOf(IssueData.getString("DeptCode"));

                        String JobCount = String.valueOf(IssueData.getString("JobCount"));

                        Job_Dpt_Item_List.add(i,new msibook_ehr_main.Job_Dpt_Item(DeptName,DeptCode,JobCount));

                    }

                    eHR_main_list = (ListView)findViewById(R.id.eHR_main_list);

                    mJob_Dpt_Adapter = new Job_Dpt_Adapter(mContext, Job_Dpt_Item_List);

                    eHR_main_list.setAdapter(mJob_Dpt_Adapter);

                    //關閉-讀取等待時間Bar
                    progressBar.dismiss();

                }
                catch (JSONException ex) {

                    Log.w("Json",ex.toString());
                }

            }
        });


    }


    // Item
    public class Job_Dpt_Item {

        String Dpt_number;

        String Dpt_name;

        String Count;

        public Job_Dpt_Item(String Dpt_name,String Dpt_number,String Count)
        {
            this.Dpt_name = Dpt_name;

            this.Dpt_number = Dpt_number;

            this.Count = Count;
        }
        public String GetDpt_name()
        {
            return this.Dpt_name;
        }

        public String GetDpt_number()
        {
            return this.Dpt_number;
        }

        public String GetCount()
        {
            return this.Count;
        }
    }

    //Adapter
    public class Job_Dpt_Adapter extends BaseAdapter {

        private LayoutInflater mLayInf;

        private List<Job_Dpt_Item> Job_Dpt_Item_List;

        private Context ProjectContext;

        private String Title;

        public Job_Dpt_Adapter(Context context,  List<Job_Dpt_Item> Job_Dpt_Item_List)
        {
            mLayInf = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            ProjectContext = context;

            this.Title = Title;

            this.Job_Dpt_Item_List = Job_Dpt_Item_List;

        }
        @Override
        public int getCount() {
            return Job_Dpt_Item_List.size();
        }

        @Override
        public Object getItem(int position) {
            return Job_Dpt_Item_List.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View v = new View(ProjectContext);

            v = mLayInf.inflate(R.layout.msibook_ehr_joblist_adapter_item, parent, false);

            TextView textView_dpt_number = (TextView) v.findViewById(R.id.textView_dpt_number);
            TextView textView_dpt_name = (TextView) v.findViewById(R.id.textView_dpt_name);
            TextView textView_people_count = (TextView) v.findViewById(R.id.textView_people_count);

            textView_dpt_number.setText(Job_Dpt_Item_List.get(position).GetDpt_number());

            textView_dpt_name.setText(Job_Dpt_Item_List.get(position).GetDpt_name());

            textView_people_count.setText(Job_Dpt_Item_List.get(position).GetCount());

            return v;
        }

    }
}
