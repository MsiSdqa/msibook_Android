package dqa.com.msibook;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class msibook_ehr_main_user extends AppCompatActivity {

    private Button btn_my_application;

    private ProgressDialog progressBar;

    private Context mContext;

    private SwipeRefreshLayout mSwipeRefreshLayout; //頁面刷新

    private ehr_User_Main_Adapter mehr_User_Main_Adapter;
    private List<ehr_user_main_Item> ehr_user_main_Item_List = new ArrayList<ehr_user_main_Item>();

    private ListView eHR_user_main_list;

    private List<msibook_job_list_item> JOB_Item_List_Adapter_Item_List = new ArrayList<msibook_job_list_item>();
    private RecyclerView recyclerView;
    private RecyclerView.Adapter recyclerView_detail_Adapter;
    private RecyclerView.LayoutManager recyclerViewLayoutManager_detail;


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(data !=null) {
            Bundle bundle = data.getExtras();
            Integer CheckInterView = Integer.valueOf(bundle.getString("InterView_Check"));
            if(CheckInterView ==1){// 1 == 有應徵成功 跳到我的應徵
                Intent intent = new Intent();

                intent.setClass(msibook_ehr_main_user.this, msibook_ehr_my_interview.class);
                //開啟Activity
                startActivity(intent);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msibook_ehr_main_user);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        progressBar = new ProgressDialog(this);
        progressBar.setCancelable(true);
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.setMessage("資料載入中");

        mContext = msibook_ehr_main_user.this;

        recyclerView = (RecyclerView) findViewById(R.id.Recycle_jobs);

        btn_my_application = (Button) findViewById(R.id.btn_my_application);

        eHR_user_main_list = (ListView) findViewById(R.id.eHR_user_main_list);

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh_layout);

        Select_HR_List();

//        Select_E_HR_Master();

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSwipeRefreshLayout.setRefreshing(false);

                Select_HR_List();

//                Select_E_HR_Master();
            }
        });

        btn_my_application.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();

                intent.setClass(msibook_ehr_main_user.this, msibook_ehr_my_interview.class);
                //開啟Activity
                startActivity(intent);
            }
        });

    }

    // 舊版
    private void Select_E_HR_Master() {

        //顯示  讀取等待時間Bar
        progressBar.show();

        ehr_user_main_Item_List.clear();

        RequestQueue mQueue = Volley.newRequestQueue(this);

        HTTPSTrustManager.allowAllSSL();//信任所有证书，信任憑證
        String Path = GetServiceData.ServicePath + "/Select_E_HR_Master";

        GetServiceData.getString(Path, mQueue, new GetServiceData.VolleyCallback() {
            @Override
            public void onSuccess(JSONObject result) {

                try {

                    JSONArray UserArray = new JSONArray(result.getString("Key"));

                    for (int i = 0; i < UserArray.length(); i++) {
                        JSONObject IssueData = UserArray.getJSONObject(i);

                        String F_DeptName = String.valueOf(IssueData.getString("DeptName"));

                        String F_DeptCode = String.valueOf(IssueData.getString("DeptCode"));

                        String F_JobCount = String.valueOf(IssueData.getString("JobCount"));

                        ehr_user_main_Item_List.add(i,new ehr_user_main_Item(F_DeptName,F_DeptCode,F_JobCount));

                    }
                    mehr_User_Main_Adapter = new ehr_User_Main_Adapter(mContext,ehr_user_main_Item_List);

                    eHR_user_main_list.setAdapter(mehr_User_Main_Adapter);

                    //關閉-讀取等待時間Bar
                    progressBar.dismiss();

                }
                catch (JSONException ex) {

                    Log.w("Json",ex.toString());
                }
            }
        });
    }

    //Detail_Item
    public class ehr_user_main_Item {

        String DeptName;

        String DeptCode;

        String JobCount;


        public ehr_user_main_Item(String DeptName,String DeptCode,String JobCount)
        {
            this.DeptName = DeptName;

            this.DeptCode = DeptCode;

            this.JobCount = JobCount;

        }

        public String GetDeptName()
        {
            return this.DeptName;
        }

        public String GetDeptCode()
        {
            return this.DeptCode;
        }

        public String GetJobCount()
        {
            return this.JobCount;
        }
    }

    //DetailAdapter
    public class ehr_User_Main_Adapter extends BaseAdapter {

        private LayoutInflater mLayInf;

        private List<ehr_user_main_Item> eHR_user_main_list;

        private Context ProjectContext;

        private String Title;

        public ehr_User_Main_Adapter(Context context, List<ehr_user_main_Item> eHR_user_main_list)
        {
            mLayInf = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            ProjectContext = context;

            this.Title = Title;

            this.eHR_user_main_list = eHR_user_main_list;

        }
        @Override
        public int getCount() {
            return eHR_user_main_list.size();
        }

        @Override
        public Object getItem(int position) {
            return eHR_user_main_list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            View v = new View(ProjectContext);

            v = mLayInf.inflate(R.layout.msibook_ehr_joblist_adapter_item, parent, false);

            LinearLayout Linear1 = (LinearLayout) v.findViewById(R.id.Linear1);
            TextView textView_dpt_number = (TextView) v.findViewById(R.id.textView_dpt_number);
            TextView textView_dpt_name = (TextView) v.findViewById(R.id.textView_dpt_name);
            TextView textView_job_count = (TextView) v.findViewById(R.id.textView_job_count);

            textView_dpt_number.setText(eHR_user_main_list.get(position).GetDeptCode());
            textView_dpt_name.setText(eHR_user_main_list.get(position).GetDeptName());
            textView_job_count.setText(eHR_user_main_list.get(position).GetJobCount());

            Linear1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent();

                    intent.putExtra("Dpt_number", eHR_user_main_list.get(position).GetDeptCode());

                    intent.putExtra("Dpt_name", eHR_user_main_list.get(position).GetDeptName());//代年到下一頁

                    intent.putExtra("Count", eHR_user_main_list.get(position).GetJobCount());

                    //從MainActivity 到Main2Activity
                    intent.setClass(msibook_ehr_main_user.this, msibook_ehr_main_page2.class);
                    //開啟Activity
                    startActivityForResult(intent,1);

                }
            });


            return v;
        }

    }

    //0528改版    薪版
    private void Select_HR_List() {

        //顯示  讀取等待時間Bar
        progressBar.show();

        JOB_Item_List_Adapter_Item_List.clear();

        RequestQueue mQueue = Volley.newRequestQueue(this);

        String Path = GetServiceData.ServicePath + "/Select_HR_List";

        GetServiceData.getString(Path, mQueue, new GetServiceData.VolleyCallback() {
            @Override
            public void onSuccess(JSONObject result) {

                try {

                    JSONArray UserArray = new JSONArray(result.getString("Key"));


                    final Map<String,ArrayList<msibook_job_list_item>> mapContent = new HashMap<String,ArrayList<msibook_job_list_item>>();

                    List<msibook_job_list_item> fatherList = new ArrayList<>();
                    List<List<msibook_job_list_item>> childList = new ArrayList<>();
                    Map<String, ArrayList<msibook_job_list_item>> linkHashMap = new LinkedHashMap<>();
                    List<String> Child_CountList = new ArrayList<>();


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

                        if (!linkHashMap.containsKey(F_Job_Type)) {  //加入表內
                            ArrayList<msibook_job_list_item> innchildList = new ArrayList<>();
                            innchildList.add(new msibook_job_list_item(F_SeqNo,F_CreateDate,F_UpdateDate,F_Keyin,F_M_Type,F_Job_Type,F_Job_Name,F_Job_Content));
                            linkHashMap.put(F_Job_Type, innchildList);
                        } else {
                            linkHashMap.get(F_Job_Type).add(new msibook_job_list_item(F_SeqNo,F_CreateDate,F_UpdateDate,F_Keyin,F_M_Type,F_Job_Type,F_Job_Name,F_Job_Content));
                        }

                        //MMC_Item_List_Adapter_Item_List.add(i,new msibook_mmc_list_item(AssetSN,HPAssetSN,Class,BrandName,TypeName,CNAME,CrDate,EMail,ExistKey,Description,Vendor_PN,PartNO));

                    }

                    int index = 0;
                    for (Map.Entry<String, ArrayList<msibook_job_list_item>>  item  : linkHashMap.entrySet()){
                        fatherList.add(index  , item.getValue().get(0)) ;
                        ArrayList<msibook_job_list_item>  tempList = new ArrayList<>();
                        for (msibook_job_list_item childItem : item.getValue()){
                            tempList.add(childItem);
                            Log.w("175行index",String.valueOf(index));
                        }
                        childList.add(index, tempList);
                        Log.w("entrySet長度",String.valueOf(tempList.size()));
                        Log.w("179行index",String.valueOf(index));
                        index++;
                    }

                    for (int i = 0; i < fatherList.size(); i++) {
                        Log.w("顯示爸爸種類",fatherList.get(i).F_M_Type);
                    }

                    for (int i = 0; i < childList.size(); i++) {
                        Log.w("顯示兒子數量",String.valueOf(childList.get(i).size()));
                        Child_CountList.add(i,String.valueOf(childList.get(i).size())) ;
                    }


                    RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getApplicationContext(), 3);
                    recyclerView.setLayoutManager(mLayoutManager);
                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                    //recyclerView.setAdapter(mAdapter);

//                    //LinearLayoutManager recyclerViewLayoutManager= new LinearLayoutManager(mContext);
//                    recyclerViewLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
//                    recyclerView.setLayoutManager(recyclerViewLayoutManager);

                    recyclerView_detail_Adapter = new RecyclerView_Detail_Adapter(mContext, fatherList,Child_CountList);

                    recyclerView.setAdapter(recyclerView_detail_Adapter);

                    recyclerView_detail_Adapter.notifyDataSetChanged();


                    //關閉-讀取等待時間Bar
                    progressBar.dismiss();


                }
                catch (JSONException ex) {

                    Log.w("Json",ex.toString());

                }

            }
        });


    }

//    //2. BU細項
//    public class RecyclerView_Detail_AdapterOLD extends RecyclerView.Adapter<RecyclerView_Detail_Adapter.ViewHolder>{
//
//        List<msibook_job_list_item> values_father;
//        List<String> values_child;
//        Context context1;
//        Integer row_index=-1;
//
//        public RecyclerView_Detail_AdapterOLD(Context context2, List<msibook_job_list_item> values1, List<String> values2){
//
//            values_father = values1;
//
//            values_child = values2;
//
//            context1 = context2;
//
//        }
//
//        public class ViewHolder extends RecyclerView.ViewHolder{
//
//            public RelativeLayout real_main;
//
//            public LinearLayout linear_item_main;
//
//            public TextView textView_Class;
//
//            public TextView textView_Count;
//
//            public ViewHolder(View v){
//
//                super(v);
//
//                linear_item_main = (LinearLayout) v.findViewById(R.id.linear_item_main);
//
//                textView_Class = (TextView) v.findViewById(R.id.textView_Class);
//
//                textView_Count = (TextView) v.findViewById(R.id.textView_Count);
//
//            }
//        }
//
//        @Override
//        public RecyclerView_Detail_Adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
//
//            View view1 = LayoutInflater.from(context1).inflate(R.layout.msibook_mmc_recycle_item,parent,false);
//
//            RecyclerView_Detail_Adapter.ViewHolder viewHolder1 = new RecyclerView_Detail_Adapter.ViewHolder(view1);
//
//            return viewHolder1;
//        }
//
//        @Override
//        public void onBindViewHolder(final RecyclerView_Detail_Adapter.ViewHolder Vholder, final int position){
//
//            Vholder.textView_Class.setText(values_father.get(position).GetClass());
//
//            Vholder.textView_Count.setText(values_child.get(position));
//
//            Vholder.linear_item_main.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//
//                    row_index=position;
//                    notifyDataSetChanged();
//
//                    Intent intent = new Intent();
//
//                    intent.putExtra("Type","0");
//
//                    intent.putExtra("Class",values_father.get(position).GetClass());
//
//                    intent.setClass(msibook_ehr_main_user.this, msibook_mmc_single_class.class);
//
//                    //intent.setClass(msibook_mmc.this, msibook_mmc_detail.class);
//                    //開啟Activity
//                    startActivity(intent);
//                }
//            });
//
//        }
//        @Override
//        public int getItemCount(){
//            return values_father.size();
//        }
//    }

    //2. BU細項
    public class RecyclerView_Detail_Adapter extends RecyclerView.Adapter<RecyclerView_Detail_Adapter.ViewHolder>{

        List<msibook_job_list_item> values_father;
        List<String> values_child;
        Context context1;
        Integer row_index=-1;

        public RecyclerView_Detail_Adapter(Context context2, List<msibook_job_list_item> values1, List<String> values2){

            values_father = values1;

            values_child = values2;

            context1 = context2;

        }

        public class ViewHolder extends RecyclerView.ViewHolder{

            public RelativeLayout real_main;

            public LinearLayout linear_item_main;

            public TextView textView_Class;

            public TextView textView_Count;

            public ViewHolder(View v){

                super(v);

                linear_item_main = (LinearLayout) v.findViewById(R.id.linear_item_main);

                textView_Class = (TextView) v.findViewById(R.id.textView_Class);

                textView_Count = (TextView) v.findViewById(R.id.textView_Count);

            }
        }

        @Override
        public RecyclerView_Detail_Adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){

            View view1 = LayoutInflater.from(context1).inflate(R.layout.msibook_ehr_jobs_recycle_item,parent,false);

            RecyclerView_Detail_Adapter.ViewHolder viewHolder1 = new RecyclerView_Detail_Adapter.ViewHolder(view1);

            return viewHolder1;
        }

        @Override
        public void onBindViewHolder(final RecyclerView_Detail_Adapter.ViewHolder Vholder, final int position){

            Vholder.textView_Class.setText(values_father.get(position).GetF_Job_Type());

            Vholder.textView_Count.setText(values_child.get(position));

            Vholder.linear_item_main.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    row_index=position;
                    notifyDataSetChanged();

                    Intent intent = new Intent();

                    intent.putExtra("Type","0");

                    intent.putExtra("F_Job_Type",values_father.get(position).GetF_Job_Type());

                    intent.setClass(msibook_ehr_main_user.this, msibook_jobs_single_class.class);

                    //intent.setClass(msibook_mmc.this, msibook_mmc_detail.class);
                    //開啟Activity
                    startActivity(intent);
                }
            });

        }
        @Override
        public int getItemCount(){
            return values_father.size();
        }
    }


    public class msibook_job_list_item {

        String F_SeqNo;

        String F_CreateDate;

        String F_UpdateDate;

        String F_Keyin;

        String F_M_Type;

        String F_Job_Type;

        String F_Job_Name;

        String F_Job_Content;


        public msibook_job_list_item(String F_SeqNo,
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
