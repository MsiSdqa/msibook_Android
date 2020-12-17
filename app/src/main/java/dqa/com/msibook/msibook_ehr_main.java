package dqa.com.msibook;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.security.acl.Group;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class msibook_ehr_main extends AppCompatActivity {

    private Button btn_creat_pop;
    private TextView toolbar_title;

    private String getPush_Key;//抓推播的Key

    private String getPush_Value;//抓推播的Value

    private Button btn_my_interview;
    private Button btn_setting_job;

    private ListView eHR_main_list;
    private ProgressDialog progressBar;
    private ProgressDialog progressBarDB;

    private SwipeRefreshLayout mSwipeRefreshLayout; //頁面刷新

    private List<msibook_ehr_item> GroupTitleItem= new ArrayList<msibook_ehr_item>();

    private List<msibook_ehr_item> eHR_List = new ArrayList<msibook_ehr_item>();

    private ArrayList<List<msibook_ehr_item>> eHR_List_Group = new ArrayList<List<msibook_ehr_item>>();

    private ExpandableListView exp_ehr_list;

    private ehr_List_Adapter exp_ehr_Adapter;

    private Integer nowweek;

    private String User_WorkID;

    SQLiteDatabase db;//資料庫物件
    static final String db_name = "My_eHR_db";//資料庫名稱
    static final String tb_name_Find_Dept_List_Recursive = "Find_Dept_List_Recursive_data";// 總部門資料 - 資料表名稱

    private Context mContext;

    //重新載入Main_Activity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(data !=null) {
            Bundle bundle = data.getExtras();
            String Type = bundle.getString("Type");

            if(Type.equals("1")){
                Select_E_HR_Application_Data();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msibook_ehr_main);

        progressBar = new ProgressDialog(this);
        progressBar.setCancelable(true);
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.setMessage("資料載入中");

        progressBarDB = new ProgressDialog(this);
        progressBarDB.setCancelable(true);
        progressBarDB.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBarDB.setMessage("部門資料建置中，請稍等");

        mContext = msibook_ehr_main.this;

        btn_creat_pop = (Button) findViewById(R.id.btn_creat_pop);//漢堡列
        toolbar_title = (TextView) findViewById(R.id.toolbar_title);//Title
//        btn_my_interview = (Button) findViewById(R.id.btn_my_interview);//我的應徵
        btn_setting_job = (Button) findViewById(R.id.btn_setting_job);//管理

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh_layout);

        exp_ehr_list = (ExpandableListView)findViewById(R.id.exp_ehr_list);//expendList
        exp_ehr_list.setGroupIndicator(null);

        User_WorkID = UserData.WorkID;
        //Log.w("User_WorkID",User_WorkID);

        //lsv_main = (ListView) mSwipeRefreshLayout.findViewById(R.id.listView);

        Calendar w = Calendar.getInstance();
        nowweek = w.get(Calendar.WEEK_OF_YEAR);

        //開啟或建立資料庫    創欄位
        db = openOrCreateDatabase(db_name, Context.MODE_PRIVATE,null);
        Cursor c = db.rawQuery("SELECT * FROM sqlite_master WHERE name ='"+tb_name_Find_Dept_List_Recursive + "'",null);//查詢tb_name資料表中的所有資料
        if (c.getCount() == 0){
            //message_first_updateDB();
            Log.w("Main初次使用更新DB","初次使用需更新DB");
            create_DB();
        }else{
            Log.w("Main Check DB有資料","DB有資料");

            Select_E_HR_Application_Data();
        }

        // 管理 點選事件
        btn_setting_job.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int []location=new int[2];
                v.getLocationOnScreen(location);
                int x=location[0];//获取当前位置的横坐标
                int y=location[1];

                Intent intent = new Intent(msibook_ehr_main.this, msibook_ehr_control_job.class);

                startActivityForResult(intent,1);

            }
        });

        getPush_Key = getIntent().getStringExtra("Push_Key");//

        getPush_Value = getIntent().getStringExtra("Push_Value");//
        if(getPush_Key!=null) {
            Log.w("getPush_Key", getPush_Key);
            Log.w("getPush_Value", getPush_Value);

            List<String> Push_Valueitems = Arrays.asList(getPush_Value.split("\\s*,\\s*"));
            for (int i = 0; i < Push_Valueitems.size(); i++){
                Log.w("items",Push_Valueitems.get(i));
            }
            switch (String.valueOf(getPush_Key)) {
                case "eHR_Application":

                    //接收推播導向頁面
//                    Intent intent = new Intent();
//
//                    intent.putExtra("Push_Key", "eHR_Application");
//
//                    intent.putExtra("F_SeqNo", Push_Valueitems.get(0));//職缺序號
//
//                    intent.putExtra("F_DeptCode", Push_Valueitems.get(1));//部門序號
//
//                    intent.putExtra("F_Job_Dept", Push_Valueitems.get(2));//部門名稱
//
//                    intent.putExtra("F_Job_Name", Push_Valueitems.get(3));//職缺名稱
//
//                    intent.setClass(msibook_ehr_main.this, msibook_ehr_candidate_list.class);
//
//                    startActivity(intent);
                    break;
                case "123456":

                    break;
                case "4564523":

                    break;
            }
        }
    }

    private void create_DB(){
        String dropTable1 =  "DROP TABLE IF EXISTS " + tb_name_Find_Dept_List_Recursive;    //Myhrm資訊

        //開啟或建立資料庫    創欄位
        db = openOrCreateDatabase(db_name, Context.MODE_PRIVATE,null);

        //建立tb_name_Find_Dept_List_Recursive
        String createTable1 = "CREATE TABLE IF NOT EXISTS " +
                tb_name_Find_Dept_List_Recursive +
                "(id VARCHAR(32), "+    //id序號
                "F_ParentID_SeqNo VARCHAR(32)," +  //20494
                "DeptID VARCHAR(32),"+  //19507
                "F_DeptCode VARCHAR(32),"+  //0001
                "Dept VARCHAR(32),"+    //總經理
                "BossID VARCHAR(32),"+  //10001001
                "BossName VARCHAR(32),"+    //徐祥
                "F_Region VARCHAR(32),"+
                "LeaveCount VARCHAR(32),"+
                "TransferCount VARCHAR(32),"+
                "PrepareCount VARCHAR(32),"+
                "NowManCount VARCHAR(32),"+
                "TotalMan VARCHAR(32))";    //MSIT

        db.execSQL(dropTable1);//刪除資料表
        db.execSQL(createTable1);//建立資料表

        Find_Dept_List_Recursive("20696",String.valueOf(nowweek));//讀取 Web服務準備寫入資料 to Step2.
    }

    //DB編制細部清單 第二版本
    private void Select_E_HR_Application_Data() {

        //顯示  讀取等待時間Bar
        progressBar.show();

        GroupTitleItem.clear();

        eHR_List.clear();

        eHR_List_Group.clear();

        final Set<String> GroupTitleItem_HS = new HashSet<String>();

        RequestQueue mQueue = Volley.newRequestQueue(this);

        HTTPSTrustManager.allowAllSSL();//信任所有证书，信任憑證
        String Path = GetServiceData.ServicePath + "/Select_E_HR_Application_Data";

        GetServiceData.getString(Path, mQueue, new GetServiceData.VolleyCallback() {
            @Override
            public void onSuccess(JSONObject result) {

                try {
                    JSONArray UserArray = new JSONArray(result.getString("Key"));

                    for (int i = 0; i < UserArray.length(); i++) {
                        JSONObject IssueData = UserArray.getJSONObject(i);

                        String F_Master_ID = String.valueOf(IssueData.getInt("F_Master_ID"));

                        String F_DeptCode = String.valueOf(IssueData.getString("F_DeptCode"));

                        String F_Job_Dept = String.valueOf(IssueData.getString("F_Job_Dept"));

                        String F_Job_vacancies = String.valueOf(IssueData.getString("F_Job_vacancies"));

                        String F_Job_People = String.valueOf(IssueData.getInt("F_Job_People"));

                        String Member_JobCount = String.valueOf(IssueData.getInt("Member_JobCount"));

                        msibook_ehr_item msibook_ehr_item = new msibook_ehr_item(F_Master_ID,F_DeptCode,F_Job_Dept,F_Job_vacancies,F_Job_People,Member_JobCount);

                        eHR_List.add(msibook_ehr_item);

                        GroupTitleItem_HS.add(msibook_ehr_item.F_Job_Dept);
                    }

                    HashMap<String, Integer> hashMap = new HashMap<String, Integer>();
                    for (msibook_ehr_item item : eHR_List) {
                        if (hashMap.get(item.F_Job_Dept) != null) {
                            Integer value = hashMap.get(item.F_Job_Dept);
                            hashMap.put(item.F_Job_Dept, value+1);
                            System.out.println("the element:"+item.F_Job_Dept+" is repeat");
                        } else {
                            hashMap.put(item.F_Job_Dept, 1);
                        }
                    }

                    for (Map.Entry<String,Integer> entry:hashMap.entrySet()) {
                        System.out.println(entry.getKey()+" "+entry.getValue());

                        String DeptName =  entry.getKey();


                        Log.w("Group Add",DeptName);

                        List<msibook_ehr_item> FilterGroup = new ArrayList<msibook_ehr_item>();

                        int GroupCount = 0;

                        for (msibook_ehr_item a : eHR_List) {

                            if(a.F_Job_Dept.contains(DeptName))
                            {
                                if(GroupCount == 0)
                                {
                                    GroupTitleItem.add(a);
                                }

                                FilterGroup.add(a);

                                GroupCount++;
                            }

                        }
                        eHR_List_Group.add(FilterGroup);
                    }

                    exp_ehr_Adapter = new ehr_List_Adapter(mContext,GroupTitleItem,eHR_List_Group);
                    exp_ehr_list.setAdapter(exp_ehr_Adapter);

                    for (int i = 0; i < GroupTitleItem.size(); i++) {
                        exp_ehr_list.expandGroup(i);

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

    //建置總"部門資訊"
    private void Find_Dept_List_Recursive(String DeptID,String Week) {

        //顯示  讀取等待時間Bar
        //progressBar.show();
        progressBarDB.show();

        RequestQueue mQueue = Volley.newRequestQueue(this);

        String Path = "http://wtsc.msi.com.tw/IMS/HRM_App_Service.asmx/Find_Dept_List_Recursive?DeptID=" + DeptID+ "&Week="+Week;

        GetServiceData.getString(Path, mQueue, new GetServiceData.VolleyCallback() {
            @Override
            public void onSuccess(JSONObject result) {

                try {
                    JSONArray UserArray = new JSONArray(result.getString("Key"));

                    for (int i = 0; i < UserArray.length(); i++) {

                        JSONObject IssueData = UserArray.getJSONObject(i);

                        //String F_ParentID_SeqNo = String.valueOf(IssueData.getInt("F_ParentID_SeqNo")); // 父層級
                        String Check_F_ParentID_SeqNo;
                        if (IssueData.isNull("F_ParentID_SeqNo")){
                            Check_F_ParentID_SeqNo = "null";
                        }else{
                            Check_F_ParentID_SeqNo = String.valueOf(IssueData.getInt("F_ParentID_SeqNo"));
                        }

                        String DeptID = String.valueOf(IssueData.getInt("DeptID")); // 21751

                        String F_DeptCode = String.valueOf(IssueData.getString("F_DeptCode")); // 7861

                        String Dept = String.valueOf(IssueData.getString("Dept")); // 設計品質驗證二部一課

                        String BossID = String.valueOf(IssueData.getString("BossID")); // 10010658

                        String BossName = String.valueOf(IssueData.getString("BossName")); // 許文俊

                        String F_Region = String.valueOf(IssueData.getString("F_Region")); // MSIT

                        String LeaveCount = String.valueOf(IssueData.getInt("LeaveCount")); // 離職

                        String TransferCount = String.valueOf(IssueData.getInt("TransferCount")); // 轉調

                        String PrepareCount = String.valueOf(IssueData.getInt("PrepareCount")); // 編制

                        String NowManCount = String.valueOf(IssueData.getInt("NowManCount")); // 現有

                        String TotalMan = String.valueOf(IssueData.getInt("TotalMan")); // 組織下總數量


                        //呼叫自訂的addData()寫入資料
                        addData_Find_Dept_List_Recursive(String.valueOf(i),
                                Check_F_ParentID_SeqNo,
                                DeptID,
                                F_DeptCode,
                                Dept,
                                BossID,
                                BossName,
                                F_Region,
                                LeaveCount,
                                TransferCount,
                                PrepareCount,
                                NowManCount,
                                TotalMan);//每筆資料加到資料表

                    }
                    Cursor c = db.rawQuery("SELECT * FROM "+tb_name_Find_Dept_List_Recursive,null);//查詢tb_name資料表中的所有資料
//                    c.moveToFirst();
//                    String str = "測試總共有" + c.getCount() + "筆資料\n";
//                    str +="------\n";
//                    do {
//                        str+="id:"+c.getString(0)+"\n";
//                        str+="F_ParentID_SeqNo:"+c.getString(1)+"\n";
//                        str+="DeptID:"+c.getString(2)+"\n";
//                        str+="F_DeptCode:"+c.getString(3)+"\n";
//                        str+="Dept:"+c.getString(4)+"\n";
//                        str+="BossID:"+c.getString(5)+"\n";
//                        str+="BossName:"+c.getString(6)+"\n";
//                        str+="F_Region:"+c.getString(7)+"\n";
//                        str+="LeaveCount:"+c.getString(8)+"\n";
//                        str+="TransferCount:"+c.getString(9)+"\n";
//                        str+="PrepareCount:"+c.getString(10)+"\n";
//                        str+="NowManCount:"+c.getString(11)+"\n";
//                        str+="TotalMan:"+c.getString(12)+"\n";
//                        str+="----------------------------------------"+"\n";
//                    }while (c.moveToNext());
//                    Log.w("data:",str);

                    //關閉-讀取等待時間Bar
                    progressBarDB.dismiss();

                    Select_E_HR_Application_Data();

                } catch (JSONException ex) {

                }
            }
        });
    }

    //建置總"部門資訊"
    private void addData_Find_Dept_List_Recursive(String id,String F_ParentID_SeqNo,String DeptID,String F_DeptCode,String Dept,String BossID,String BossName,String F_Region,String LeaveCount,String TransferCount,String PrepareCount,String NowManCount,String TotalMan) {
        ContentValues cv = new ContentValues(13);//建立含2個資料項目的物件
        cv.put("id",id);       // 0、1、2、3
        cv.put("F_ParentID_SeqNo",F_ParentID_SeqNo);    //84
        cv.put("DeptID",DeptID);    //19762
        cv.put("F_DeptCode",F_DeptCode);    //7800
        cv.put("Dept",Dept);    //研發設計支援處
        cv.put("BossID",BossID);    //10003001
        cv.put("BossName",BossName);    //黃金請
        cv.put("F_Region",F_Region);    //MSIT
        cv.put("LeaveCount",LeaveCount);    //離職
        cv.put("TransferCount",TransferCount);    //轉調
        cv.put("PrepareCount",PrepareCount);    //編制
        cv.put("NowManCount",NowManCount);  //現有
        cv.put("TotalMan",TotalMan);    //體系下總人數

        db.insert(tb_name_Find_Dept_List_Recursive,null,cv);//將資料加入資料表
    }

    public class ehr_List_Adapter extends BaseExpandableListAdapter {

        private LayoutInflater mLayInf;

        private String AdapterType;

        private Context context;

        List<msibook_ehr_item> groups;

        ArrayList<List<msibook_ehr_item>> childs;

        protected void onActivityResult(int requestCode, int resultCode, Intent data) {

            if(data !=null) {
                //Request_Tracking_List(UserData.WorkID);
            }
        }

        public ehr_List_Adapter(Context context, List<msibook_ehr_item> groups, ArrayList<List<msibook_ehr_item>> childs) {
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
            v =  mLayInf.inflate(R.layout.msibook_ehr_hr_child_item, parent, false);
            final msibook_ehr_item msibook_ehr_item = (msibook_ehr_item) getChild(groupPosition, childPosition);

            final LinearLayout Linear_ehr_job = (LinearLayout) v.findViewById(R.id.Linear_ehr_job);
            final TextView textView_job_name = (TextView) v.findViewById(R.id.textView_job_name);
            final TextView textView_man_count = (TextView) v.findViewById(R.id.textView_man_count);

            textView_job_name.setText(msibook_ehr_item.F_Job_vacancies);
            textView_man_count.setText(msibook_ehr_item.Member_JobCount);
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

            Linear_ehr_job.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(context, msibook_request_detail.class);

                    intent.putExtra("F_SeqNo", msibook_ehr_item.F_Master_ID);//職缺序號

                    intent.putExtra("F_DeptCode", msibook_ehr_item.F_DeptCode);//部門序號

                    intent.putExtra("F_Job_Dept", msibook_ehr_item.F_Job_Dept);//部門名稱

                    intent.putExtra("F_Job_Name", msibook_ehr_item.F_Job_vacancies);//職缺名稱

                    intent.setClass(msibook_ehr_main.this, msibook_ehr_candidate_list.class);
//                    Bundle bundle=new Bundle();
//                    bundle.putSerializable("CustomObject ", msibook_request_item);
// intent.putExtras(bundle);
                    startActivity(intent);
                }
            });


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
            String DeptName = groups.get(groupPosition).F_Job_Dept;
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
