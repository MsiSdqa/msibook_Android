package dqa.com.msibook;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class msibook_ehr_control_job extends AppCompatActivity {

    private String Set_WorkID;
    private ProgressDialog progressBar;
    private ListView eHR_job_control_list;

    private LinearLayout linear_add_job;

    private String Set_F_Job_Level_Base;
    private String Set_F_Job_Level_Top;
    private String Set_F_Job_Age_Base;
    private String Set_F_Job_Age_Top;
    private String Set_F_Job_People;
    private String Set_DeptName;
    private String Set_F_DeptCode;

    private List<msibook_ehr_control_job_item> Group_Control_job_TitleItem= new ArrayList<msibook_ehr_control_job_item>();

    private List<msibook_ehr_control_job_item> eHR_Control_job_List = new ArrayList<msibook_ehr_control_job_item>();

    private ArrayList<List<msibook_ehr_control_job_item>> eHR_Control_job_List_Group = new ArrayList<List<msibook_ehr_control_job_item>>();

    private ExpandableListView exp_ehr_control_job_list;

    private ehr_Control_job_List_Adapter exp_ehr_control_job_Adapter;



    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msibook_ehr_control_job);

        progressBar = new ProgressDialog(this);
        progressBar.setCancelable(true);
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.setMessage("資料載入中");

        mContext = msibook_ehr_control_job.this;

        Set_WorkID = UserData.WorkID;
        Log.w("Set_WorkID",Set_WorkID);

        linear_add_job = (LinearLayout) findViewById(R.id.linear_add_job);

        exp_ehr_control_job_list = (ExpandableListView)findViewById(R.id.exp_ehr_control_job_list);//expendList
        exp_ehr_control_job_list.setGroupIndicator(null);

        Select_E_HR_Master_Detail_By_Keyin(Set_WorkID);


        Bundle bundle = new Bundle();
        bundle.putString("Type", "1");
        msibook_ehr_control_job.this.setResult(RESULT_OK,msibook_ehr_control_job.this.getIntent().putExtras(bundle));


        linear_add_job.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int []location=new int[2];
                v.getLocationOnScreen(location);
                int x=location[0];//获取当前位置的横坐标
                int y=location[1];

                Intent intent = new Intent(msibook_ehr_control_job.this, msibook_ehr_new_job_add.class);

                startActivityForResult(intent,1);
            }
        });


    }

    //設定職缺是否開放
    public void Update_E_HR_Master_Status(String F_Status, String F_SeqNo) {
        //顯示  讀取等待時間Bar
        progressBar.show();

        RequestQueue mQueue = Volley.newRequestQueue(this);
        HTTPSTrustManager.allowAllSSL();//信任所有证书，信任憑證
        String Path = GetServiceData.ServicePath + "/Update_E_HR_Master_Status?F_Status=" + F_Status + "&F_SeqNo=" + F_SeqNo;

        GetServiceData.getString(Path, mQueue, new GetServiceData.VolleyCallback() {
            @Override
            public void onSuccess(JSONObject result) {

            }
        });
        //關閉-讀取等待時間Bar
        progressBar.dismiss();

    }

    //DB編制細部清單
    private void Select_E_HR_Master_Detail_By_Keyin(String Keyin) {

        //顯示  讀取等待時間Bar
        progressBar.show();

        Group_Control_job_TitleItem.clear();

        eHR_Control_job_List.clear();

        eHR_Control_job_List_Group.clear();

        final Set<String> GroupTitleItem_HS = new HashSet<String>();

        RequestQueue mQueue = Volley.newRequestQueue(this);
        HTTPSTrustManager.allowAllSSL();//信任所有证书，信任憑證
        String Path = GetServiceData.ServicePath + "/Select_E_HR_Master_Detail_By_Keyin?Keyin=" + Keyin;

        GetServiceData.getString(Path, mQueue, new GetServiceData.VolleyCallback() {
            @Override
            public void onSuccess(JSONObject result) {

                try {

                    JSONArray UserArray = new JSONArray(result.getString("Key"));

                    for (int i = 0; i < UserArray.length(); i++) {
                        JSONObject IssueData = UserArray.getJSONObject(i);

                        String F_SeqNo = String.valueOf(IssueData.getInt("F_SeqNo"));

                        String F_CreateDate = String.valueOf(IssueData.getString("F_CreateDate")).replace("T"," ");

                        String F_UpdateDate = String.valueOf(IssueData.getString("F_UpdateDate")).replace("T"," ");

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

                        msibook_ehr_control_job_item msibook_ehr_control_job_item = new msibook_ehr_control_job_item(F_SeqNo,
                                F_CreateDate,
                                F_UpdateDate,
                                F_Keyin,
                                F_Stat,
                                F_Job_Name,
                                F_Job_Content,
                                F_Job_InterView,
                                F_Job_gender,
                                Set_F_Job_Level_Base,
                                Set_F_Job_Level_Top,
                                Set_F_Job_Age_Base,
                                Set_F_Job_Age_Top,
                                Set_F_Job_People,
                                F_Status,
                                Set_DeptName,
                                Set_F_DeptCode);

                        eHR_Control_job_List.add(msibook_ehr_control_job_item);

                        GroupTitleItem_HS.add(msibook_ehr_control_job_item.DeptName);

                    }

                    HashMap<String, Integer> hashMap = new HashMap<String, Integer>();
                    for (msibook_ehr_control_job_item item : eHR_Control_job_List) {
                        if (hashMap.get(item.DeptName) != null) {
                            Integer value = hashMap.get(item.DeptName);
                            hashMap.put(item.DeptName, value+1);
                            System.out.println("the element:"+item.DeptName+" is repeat");
                        } else {
                            hashMap.put(item.DeptName, 1);
                        }
                    }

                    for (Map.Entry<String,Integer> entry:hashMap.entrySet()) {
                        System.out.println(entry.getKey()+" "+entry.getValue());

                        String DeptName =  entry.getKey();


                        Log.w("Group Add",DeptName);

                        List<msibook_ehr_control_job_item> FilterGroup = new ArrayList<msibook_ehr_control_job_item>();

                        int GroupCount = 0;

                        for (msibook_ehr_control_job_item a : eHR_Control_job_List) {

                            if(a.DeptName.contains(DeptName))
                            {
                                if(GroupCount == 0)
                                {
                                    Group_Control_job_TitleItem.add(a);
                                }

                                FilterGroup.add(a);

                                GroupCount++;
                            }

                        }
                        eHR_Control_job_List_Group.add(FilterGroup);
                    }

                    exp_ehr_control_job_Adapter = new ehr_Control_job_List_Adapter(mContext,Group_Control_job_TitleItem,eHR_Control_job_List_Group);
                    exp_ehr_control_job_list.setAdapter(exp_ehr_control_job_Adapter);

                    for (int i = 0; i < Group_Control_job_TitleItem.size(); i++) {
                        exp_ehr_control_job_list.expandGroup(i);

                        //Log.w("Group Title",String.valueOf(GroupTitleItem.get(i)));
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


    public class ehr_Control_job_List_Adapter extends BaseExpandableListAdapter {

        private LayoutInflater mLayInf;

        private String AdapterType;

        private Context context;

        List<msibook_ehr_control_job_item> groups;

        ArrayList<List<msibook_ehr_control_job_item>> childs;

        protected void onActivityResult(int requestCode, int resultCode, Intent data) {

            if(data !=null) {
                //Request_Tracking_List(UserData.WorkID);
            }
        }

        public ehr_Control_job_List_Adapter(Context context, List<msibook_ehr_control_job_item> groups, ArrayList<List<msibook_ehr_control_job_item>> childs) {
            mLayInf = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            this.groups = groups;
            this.childs = childs;
            this.context = context;
        }

        public Object getChild(int groupPosition, int childPosition) {

            return childs.get(groupPosition).get(childPosition);
        }

        public long getChildId(int groupPosition, int childPosition) {

            return childPosition;
        }

        //獲取二級清單的View物件
        public View getChildView(int groupPosition, final int childPosition, boolean isLastChild, View convertView,
                                 ViewGroup parent) {

            View v = new View(context);
            v =  mLayInf.inflate(R.layout.msibook_ehr_joblist_life_adapter_item, parent, false);
            final msibook_ehr_control_job_item msibook_ehr_control_job_item = (msibook_ehr_control_job_item) getChild(groupPosition, childPosition);

            final LinearLayout Linear_ehr_job = (LinearLayout) v.findViewById(R.id.Linear_ehr_job);
            final TextView textView_job_name = (TextView) v.findViewById(R.id.textView_job_name);
            final TextView textView_date_time = (TextView) v.findViewById(R.id.textView_date_time);
            final SwitchCompat switch_on_off = (SwitchCompat)v.findViewById(R.id.switch_on_off);

            textView_job_name.setText(msibook_ehr_control_job_item.F_Job_Name);
            textView_date_time.setText("更新時間：" + msibook_ehr_control_job_item.F_UpdateDate);

            if(msibook_ehr_control_job_item.GetF_Status().indexOf("1")==-1){
                switch_on_off.setChecked(false);
            }else{
                switch_on_off.setChecked(true);
            }

            switch_on_off.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (buttonView.isChecked()) {
                        Log.w("Yeah" , "Is Selected");
                        Update_E_HR_Master_Status("1",String.valueOf(msibook_ehr_control_job_item.GetF_SeqNo()));
                        Toast.makeText(msibook_ehr_control_job.this, "已開啟", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Log.w("NOOOO" , "Is  NotSelected");
                        Update_E_HR_Master_Status("0",String.valueOf(msibook_ehr_control_job_item.GetF_SeqNo()));
                        Toast.makeText(msibook_ehr_control_job.this, "已關閉", Toast.LENGTH_SHORT).show();
                    }

                    try{
                        // delay 0.5 second
                        Thread.sleep(100);
                        Select_E_HR_Master_Detail_By_Keyin(Set_WorkID);
                    } catch(InterruptedException e){
                        e.printStackTrace();

                    }
                }
            });

            //textView_job_name.setText(msibook_request_item.RespUser);

//            String dtStart = msibook_request_item.F_ExpectFixDate.replace("T"," ");
//            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
//            try {
//                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//                Date date = format.parse(dtStart);
//                String dateString = sdf.format(date);
//                textView_date.setText(dateString);
//
//                Date NowDate = new Date();
//                long diff = date.getTime() - NowDate.getTime();
//
//                if (diff <= 0)
//                {
//                    //textView_hour.setText("0");
//                }
//                else
//                {
//                    //textView_hour.setText(String.valueOf(diff/3600000));
//                }
//
//            } catch (ParseException e) {
//                Log.w("ErrorDate",e.toString());
//            }


            return v;
        }


        public int getChildrenCount(int groupPosition) {
            if (groups.size() == 0) {
                return 0;
            } else {
                if (childs.size() == 0) {

                    return 0;
                } else {

                    return childs.get(groupPosition).size();
                }

            }
        }

        public Object getGroup(int groupPosition) {


            return groups.get(groupPosition);
        }

        public int getGroupCount() {
            //Log.w("GroupSize",String.valueOf(groups.size()));


            return groups.size();
        }

        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        //獲取一級清單View物件
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            String DeptCode = groups.get(groupPosition).F_DeptCode;
            String DeptName = groups.get(groupPosition).DeptName;
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            //獲取一級清單佈局檔,設置相應元素屬性
            RelativeLayout RelativeLayout = (RelativeLayout) layoutInflater.inflate(R.layout.msibook_ehr_hr_group_item, null);
            TextView textView_dpt_id = (TextView) RelativeLayout.findViewById(R.id.textView_dpt_id);
            TextView textView_dpt_name = (TextView) RelativeLayout.findViewById(R.id.textView_dpt_name);
            textView_dpt_id.setText(DeptCode);
            textView_dpt_name.setText(DeptName);

            ImageView parentImageViw = (ImageView) RelativeLayout.findViewById(R.id.parentImageViw);

            if (isExpanded)
            {
                parentImageViw.setBackgroundResource(R.mipmap.ehr_btn_common_collapse);
            }
            else
            {
                parentImageViw.setBackgroundResource(R.mipmap.ehr_btn_common_unfolded);
            }

            return RelativeLayout;
        }

        public boolean hasStableIds() {
            return false;
        }

        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return false;
        }

    }

}
