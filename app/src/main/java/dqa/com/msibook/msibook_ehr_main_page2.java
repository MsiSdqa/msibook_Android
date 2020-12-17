package dqa.com.msibook;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class msibook_ehr_main_page2 extends AppCompatActivity {

    private Context mContext;

    private Button btn_creat_pop;
    private TextView toolbar_title;
    private Button btn_add_new_job;
    private ListView eHR_main_list;
    private ProgressDialog progressBar;
    private ExpandableListAdapter listAdapter;
    private ExpandableListView expListView;

    private String getDpt_number;
    private String getDpt_name;
    private String getCount;

    private String Set_F_Job_Level_Base;
    private String Set_F_Job_Level_Top;
    private String Set_F_Job_Age_Base;
    private String Set_F_Job_Age_Top;
    private String Set_F_Job_People;
    private String Set_DeptName;
    private String Set_F_DeptCode;

    private List<Job_Dpt_Item> Job_Dpt_Item_Item_list = new ArrayList<Job_Dpt_Item>();
    private List<List<Job_detial_Item>> ChildJob_detial_Item_list = new ArrayList<List<Job_detial_Item>>();

    private String[] Keyman_HR;


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(data !=null) {
            Bundle bundle = data.getExtras();
            Integer CheckInterView = Integer.valueOf(bundle.getString("InterView_Check"));
            if(CheckInterView ==1){// 1 == 有應徵成功 跳到我的應徵
                Intent intent = new Intent();
                Bundle b = new Bundle();
                //b.putString("Booking_Check", "1");
                b.putString("InterView_Check", "1");
                intent.putExtras(b);
                setResult(RESULT_OK, intent);
                finish();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msibook_ehr_main_page2);

        mContext = msibook_ehr_main_page2.this;

        progressBar = new ProgressDialog(this);
        progressBar.setCancelable(true);
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.setMessage("資料載入中");

        Keyman_HR = new String[]{"10012565","10003275"};//人資開放使用功能
        //Keyman_HR = new String[]{"10015812", "10012565","10003275"};//人資開放使用功能

        btn_creat_pop = (Button) findViewById(R.id.btn_creat_pop);//漢堡列
        toolbar_title = (TextView) findViewById(R.id.toolbar_title);//Title
        btn_add_new_job = (Button) findViewById(R.id.btn_add_new_job);//新增
        eHR_main_list = (ListView) findViewById(R.id.eHR_main_list);//List

        expListView = (ExpandableListView) findViewById(R.id.exp_job_list);

        getDpt_number = getIntent().getStringExtra("Dpt_number");//
        Log.w("getDpt_number",String.valueOf(getDpt_number));
        getDpt_name = getIntent().getStringExtra("Dpt_name");//

        getCount = getIntent().getStringExtra("Count");//

        Select_E_HR_Master_Detail(getDpt_number);
        //設定箭頭置右
//        DisplayMetrics metrics = new DisplayMetrics();
//        this.getWindowManager().getDefaultDisplay().getMetrics(metrics);

//        expListView.setIndicatorBounds(metrics.widthPixels - GetDipsFromPixel(50), metrics.widthPixels - GetDipsFromPixel(20));

        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

                //如果是人資權限跳 職缺有哪些人應徵 ，否則只跳 查看職缺訊息
                if (Arrays.asList(Keyman_HR).contains(UserData.WorkID)){
                    Intent intent = new Intent();

                    intent.putExtra("F_SeqNo", ChildJob_detial_Item_list.get(groupPosition).get(childPosition).GetF_SeqNo());

                    intent.putExtra("F_Job_Name", ChildJob_detial_Item_list.get(groupPosition).get(childPosition).GetF_Job_Name());

                    intent.putExtra("F_Job_People", ChildJob_detial_Item_list.get(groupPosition).get(childPosition).GetF_Job_People());

                    intent.setClass(msibook_ehr_main_page2.this, msibook_ehr_candidate_list.class);
                    //開啟Activity
                    startActivity(intent);

                }else{
                    Intent intent = new Intent();

                    intent.putExtra("F_DeptCode", ChildJob_detial_Item_list.get(groupPosition).get(childPosition).GetF_DeptCode());

                    intent.putExtra("DeptName", ChildJob_detial_Item_list.get(groupPosition).get(childPosition).GetDeptName());

                    intent.putExtra("F_Job_Name", ChildJob_detial_Item_list.get(groupPosition).get(childPosition).GetF_Job_Name());

                    intent.putExtra("F_Job_Content", ChildJob_detial_Item_list.get(groupPosition).get(childPosition).GetF_Job_Content());

                    intent.putExtra("F_Job_People", ChildJob_detial_Item_list.get(groupPosition).get(childPosition).GetF_Job_People());

                    intent.setClass(msibook_ehr_main_page2.this, msibook_ehr_check_job_detial.class);
                    //開啟Activity
                    startActivity(intent);
                }

                return false;
            }
        });

    }

    //設定箭頭置右
    public int GetDipsFromPixel(float pixels)
    {
        // Get the screen's density scale
        final float scale = getResources().getDisplayMetrics().density;
        // Convert the dps to pixels, based on density scale
        return (int) (pixels * scale + 0.5f);
    }


    //部門職缺資訊
    private void Select_E_HR_Master_Detail(String F_DeptCode) {

        //顯示  讀取等待時間Bar
        progressBar.show();

        Job_Dpt_Item_Item_list.clear();
        ChildJob_detial_Item_list.clear();

        final List<Job_detial_Item> mChildJob_detial_Item_list = new ArrayList<Job_detial_Item>();

        RequestQueue mQueue = Volley.newRequestQueue(this);
        HTTPSTrustManager.allowAllSSL();//信任所有证书，信任憑證
        String Path = GetServiceData.ServicePath +"/Select_E_HR_Master_Detail?F_DeptCode=" + F_DeptCode;

        GetServiceData.getString(Path, mQueue, new GetServiceData.VolleyCallback() {
            @Override
            public void onSuccess(JSONObject result) {

                try {

                    JSONArray UserArray = new JSONArray(result.getString("Key"));

                    for (int i = 0; i < UserArray.length(); i++) {

                        JSONObject IssueData = UserArray.getJSONObject(i);

                        String F_SeqNo = String.valueOf(IssueData.getInt("F_SeqNo"));
                        Log.w("F_SeqNo",F_SeqNo);

                        String F_CreateDate = String.valueOf(IssueData.getString("F_CreateDate"));

                        String F_UpdateDate = String.valueOf(IssueData.getString("F_UpdateDate"));

                        String F_Keyin = String.valueOf(IssueData.getString("F_Keyin"));

                        String F_Stat = String.valueOf(IssueData.getString("F_Stat"));

                        String F_Job_Name = String.valueOf(IssueData.getString("F_Job_Name"));

                        String F_Job_Content = String.valueOf(IssueData.getString("F_Job_Content"));

                        String F_Job_InterView = String.valueOf(IssueData.getString("F_Job_InterView"));

                        String F_Job_gender = String.valueOf(IssueData.getInt("F_Job_gender"));

                        if (IssueData.isNull("F_Job_Level_Base")) {
                            String F_Job_Level_Base = "0";
                            Set_F_Job_Level_Base = "0";
                        } else {
                            String F_Job_Level_Base = String.valueOf(IssueData.getInt("F_Job_Level_Base"));
                            Set_F_Job_Level_Base = F_Job_Level_Base;
                        }

                        if (IssueData.isNull("F_Job_Level_Top")) {
                            String F_Job_Level_Top = "0";
                            Set_F_Job_Level_Top = "0";
                        } else {
                            String F_Job_Level_Top = String.valueOf(IssueData.getInt("F_Job_Level_Top"));
                            Set_F_Job_Level_Top = F_Job_Level_Top;
                        }

                        if (IssueData.isNull("F_Job_Age_Base")) {
                            String F_Job_Age_Base = "0";
                            Set_F_Job_Age_Base = "0";
                        } else {
                            String F_Job_Age_Base = String.valueOf(IssueData.getInt("F_Job_Age_Base"));
                            Set_F_Job_Age_Base = F_Job_Age_Base;
                        }

                        if (IssueData.isNull("F_Job_Age_Top")) {
                            String F_Job_Age_Top = "0";
                            Set_F_Job_Age_Top = "0";
                        } else {
                            String F_Job_Age_Top = String.valueOf(IssueData.getInt("F_Job_Age_Top"));
                            Set_F_Job_Age_Top = F_Job_Age_Top;
                        }

                        if (IssueData.isNull("F_Job_People")) {
                            String F_Job_People = "0";
                            Set_F_Job_People = "0";
                        } else {
                            String F_Job_People = String.valueOf(IssueData.getInt("F_Job_People"));
                            Set_F_Job_People = F_Job_People;
                        }

                        String F_Status = String.valueOf(IssueData.getString("F_Status"));

                        if (IssueData.isNull("DeptName")) {
                            String DeptName = "0";
                            Set_DeptName = "0";
                        } else {
                            String DeptName = String.valueOf(IssueData.getString("DeptName"));//設計品質驗證二部一課
                            Set_DeptName = DeptName;
                        }

                        if (IssueData.isNull("F_DeptCode")) {
                            String F_DeptCode = "0";
                            Set_F_DeptCode = "0";
                        } else {
                            String F_DeptCode = String.valueOf(IssueData.getString("F_DeptCode"));//G781
                            Set_F_DeptCode = F_DeptCode;
                        }
                        mChildJob_detial_Item_list.add(i,new Job_detial_Item(F_SeqNo,F_Keyin,Set_F_DeptCode,Set_DeptName,F_Job_Name,F_Job_Content,Set_F_Job_People));

                    }

                    Job_Dpt_Item_Item_list.add(0,new Job_Dpt_Item(getDpt_number,getDpt_name,getCount));

                    ChildJob_detial_Item_list.add(mChildJob_detial_Item_list);

                    listAdapter = new ExpandableListAdapter(msibook_ehr_main_page2.this, Job_Dpt_Item_Item_list, ChildJob_detial_Item_list);
                    // setting list adapter
                    expListView.setAdapter(listAdapter);

                    for (int i = 0 ;i< ChildJob_detial_Item_list.size();i++)
                    {
                        expListView.expandGroup(i);
                    }

                    expListView.setGroupIndicator(null);

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
    public class Job_Dpt_Item {

        String Dpt_number;

        String Dpt_name;

        String Count;

        public Job_Dpt_Item(String Dpt_number,String Dpt_name,String Count)
        {
            this.Dpt_number = Dpt_number;

            this.Dpt_name = Dpt_name;

            this.Count = Count;
        }


        public String GetDpt_number()
        {
            return this.Dpt_number;
        }

        public String GetDpt_name()
        {
            return this.Dpt_name;
        }

        public String GetCount()
        {
            return this.Count;
        }
    }

    public class Job_detial_Item {

        String F_SeqNo;

        String F_Keyin;

        String F_DeptCode;

        String DeptName;

        String F_Job_Name;

        String F_Job_Content;

        String F_Job_People;

        public Job_detial_Item(String F_SeqNo,String F_Keyin,String F_DeptCode,String DeptName,String F_Job_Name,String F_Job_Content,String F_Job_People)
        {
            this.F_SeqNo = F_SeqNo;

            this.F_Keyin = F_Keyin;

            this.F_DeptCode = F_DeptCode;

            this.DeptName = DeptName;

            this.F_Job_Name = F_Job_Name;

            this.F_Job_Content = F_Job_Content;

            this.F_Job_People = F_Job_People;

        }

        public String GetF_SeqNo()
        {
            return this.F_SeqNo;
        }

        public String GetF_Keyin()
        {
            return this.F_Keyin;
        }

        public String GetF_DeptCode()
        {
            return this.F_DeptCode;
        }

        public String GetDeptName()
        {
            return this.DeptName;
        }

        public String GetF_Job_Name()
        {
            return this.F_Job_Name;
        }

        public String GetF_Job_Content()
        {
            return this.F_Job_Content;
        }

        public String GetF_Job_People()
        {
            return this.F_Job_People;
        }

    }

    // --------------------------------------------Adapter--------------------------------------------

    // ExpandableListAdapter
    public class ExpandableListAdapter extends BaseExpandableListAdapter {

        private Context _context;
        private List<Job_Dpt_Item>Job_Dpt_Item_list;

        private List<List<Job_detial_Item>> ChildJob_detial_Item_list;

        public ExpandableListAdapter(Context context,List<Job_Dpt_Item> mJob_Dpt_Item_list,
                                     List<List<Job_detial_Item>> mChildJob_detial_Item_list){
            this._context = context;

            this.Job_Dpt_Item_list = mJob_Dpt_Item_list;

            this.ChildJob_detial_Item_list = mChildJob_detial_Item_list;

        }

        @Override
        public int getGroupCount() {
            return this.Job_Dpt_Item_list.size();
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return this.ChildJob_detial_Item_list.get(groupPosition).size();
        }

        @Override
        public Object getGroup(int groupPosition) {
            return this.Job_Dpt_Item_list.get(groupPosition);
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return this.ChildJob_detial_Item_list.get(groupPosition).get(childPosition);
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            Job_Dpt_Item Job_Dpt_Item = (Job_Dpt_Item)getGroup(groupPosition);

            if (convertView == null){
                LayoutInflater infalInflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = infalInflater.inflate(R.layout.msibook_ehr_job_group_item,null);
            }

            TextView textView_dpt_number = (TextView) convertView.findViewById(R.id.textView_dpt_number);
            TextView textView_dpt_name = (TextView) convertView.findViewById(R.id.textView_dpt_name);
            TextView textView_people_count = (TextView) convertView.findViewById(R.id.textView_people_count);

            ImageView parentImageViw = (ImageView) convertView.findViewById(R.id.parentImageViw);

            //判斷isExpanded就可以控制是按下還是關閉，同時更換圖片
            if(isExpanded){
                parentImageViw.setBackgroundResource(R.mipmap.ehr_btn_common_collapse);
            }else{
                parentImageViw.setBackgroundResource(R.mipmap.ehr_btn_common_unfolded);
            }

            textView_dpt_number.setText(Job_Dpt_Item.GetDpt_number());
            textView_dpt_name.setText(Job_Dpt_Item.GetDpt_name());
            textView_people_count.setText(Job_Dpt_Item.GetCount());

            return convertView;
        }

        @Override
        public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

            Job_detial_Item Job_detial_Item = (Job_detial_Item)getChild(groupPosition,childPosition);

            //final String childText = (String) getChild(groupPosition, childPosition);
            if (convertView == null) {
                LayoutInflater infalInflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = infalInflater.inflate(R.layout.msibook_ehr_joblist_detial_adapter_item,null);
            }
            TextView textView_dpt_name = (TextView) convertView.findViewById(R.id.textView_dpt_name);
            TextView textView_apply = (TextView) convertView.findViewById(R.id.textView_apply);
            TextView textView_icon = (TextView) convertView.findViewById(R.id.textView_icon);

            textView_dpt_name.setText(Job_detial_Item.GetF_Job_Name());
            //textView_apply.setText(Job_detial_Item.GetApply());

            if (Arrays.asList(Keyman_HR).contains(UserData.WorkID)){
                textView_apply.setVisibility(View.INVISIBLE);
                textView_apply.setEnabled(false);
                textView_icon.setVisibility(View.VISIBLE);
            }else{
                textView_icon.setVisibility(View.GONE);
            }

            //我要應徵
            textView_apply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent();

                    intent.putExtra("F_Keyin", ChildJob_detial_Item_list.get(groupPosition).get(childPosition).GetF_Keyin());

                    intent.putExtra("F_SeqNo", ChildJob_detial_Item_list.get(groupPosition).get(childPosition).GetF_SeqNo());

                    intent.putExtra("F_DeptCode", ChildJob_detial_Item_list.get(groupPosition).get(childPosition).GetF_DeptCode());

                    intent.putExtra("DeptName", ChildJob_detial_Item_list.get(groupPosition).get(childPosition).GetDeptName());

                    intent.putExtra("F_Job_Name", ChildJob_detial_Item_list.get(groupPosition).get(childPosition).GetF_Job_Name());

                    intent.putExtra("F_Job_Content", ChildJob_detial_Item_list.get(groupPosition).get(childPosition).GetF_Job_Content());

                    intent.putExtra("F_Job_People", ChildJob_detial_Item_list.get(groupPosition).get(childPosition).GetF_Job_People());

                    intent.setClass(msibook_ehr_main_page2.this, msibook_ehr_check_interview_detial.class);
                    //開啟Activity
                    startActivityForResult(intent,1);


//                    Dialog dialog=new android.support.v7.app.AlertDialog.Builder(mContext)
////                                    .setTitle("")//设置提示内容
//                            .setTitle("應徵確認")
//                            .setMessage("是否應徵該筆職缺\n\n"+message_title+"\n\n"+message_date)//设置提示内容
//                            //确定按钮
//                            .setPositiveButton("送出", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//
//                                    //Cancel_Fac_Schedule(My_Booking_item_List.get(position).GetF_SeqNo());
//
//                                    Toast.makeText(mContext, "應徵已送出", Toast.LENGTH_SHORT).show();
//
//                                    Find_Fac_My_Schedule_List(Set_Keyin);
//
//                                }
//                            })
//                            .setNeutralButton("返回", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//
//                                }
//                            })
//                            .create();//创建对话框
//                    dialog.show();//显示对话框

                    String F_Keyin = UserData.WorkID;
                    String Master_ID =  ChildJob_detial_Item_list.get(groupPosition).get(childPosition).GetF_SeqNo();

                    Log.w("F_Keyin",F_Keyin);
                    Log.w("Master_ID",Master_ID);
                }
            });

            return convertView;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }
    }
}
