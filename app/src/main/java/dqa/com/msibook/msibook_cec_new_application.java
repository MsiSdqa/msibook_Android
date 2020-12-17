package dqa.com.msibook;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Parcelable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.vision.text.Line;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class msibook_cec_new_application extends AppCompatActivity {

    private List<String> ArrayModel = new ArrayList<String>();
    private List<String> ArrayModelID = new ArrayList<String>();

    private Context mContext;
    private ProgressDialog progressBar;

    private DropDownAdapter mDropDownAdapter;

    private String Save_Model;
    private String Save_ModelID;

    private Spinner spinner_project;

    private RadioGroup rdg1;
    private RadioButton radio_btn_Cer;
    private RadioButton radio_btn_Pre;
    private RadioButton radio_btn_Sub;
    private TextView textView_cer_info;
    private TextView textView_pre_info;
    private TextView textView_sub_info;

    private LinearLayout linear_cer;
    private CheckBox checkBox_cer_cec;
    private CheckBox checkBox_cec_erp;

    private LinearLayout linear_pre;
    private CheckBox checkBox_pre_cec;
    private CheckBox checkBox_pre_erp;
    private CheckBox checkBox_pre_eng;

    private TextView textView_totalTime;
    private TextView textView_totalUSD;
    private Double SaveTotaltime;
    private Double TotalUSD;
    private String Save_Select_Radio_Btn;

    private ListView mListView;
    private ApplyAdapter mApplyAdapter;
    private List<Apply_Item> Apply_Item_List = new ArrayList<Apply_Item>();

    private List<String> Save_Lab_Temp = new ArrayList<String>(); //項目 SeqNo
    private List<String> Save_Select_item = new ArrayList<String>(); //項目logo
    private List<String> Save_Cer_Time = new ArrayList<String>(); //項目 週
    private List<String> Save_Cer_Expense = new ArrayList<String>(); //項目 錢
    private List<String> Save_RWorkID = new ArrayList<String>(); //項目 負責工號

    private TextView textView_sent;


    //點選Local回傳狀態
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(data !=null) {
            Bundle bundle = data.getExtras();
            Integer CheckBooking = Integer.valueOf(bundle.getString("CEC_Application_Check"));
            if(CheckBooking ==1){
                Intent intent = new Intent();
                Bundle b = new Bundle();
                b.putString("CEC_Application_Check","1");
                intent.putExtras(b);
                setResult(RESULT_OK, intent);
                finish();
            }
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msibook_cec_new_application);

        SaveTotaltime = 0.0;
        TotalUSD = 0.0;

        mContext = msibook_cec_new_application.this;

        //讀取時間Bar
        progressBar = new ProgressDialog(this);
        progressBar.setCancelable(true);
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.setMessage("資料載入中");

        spinner_project = (Spinner) findViewById(R.id.spinner_project);//宣告部門下拉選單

        rdg1 = (RadioGroup) findViewById(R.id.rdg1);

        final RadioButton radio_btn_Cer = (RadioButton) findViewById(R.id.radio_btn_Cer);
        final RadioButton radio_btn_Pre = (RadioButton) findViewById(R.id.radio_btn_Pre);
        final RadioButton radio_btn_Sub = (RadioButton) findViewById(R.id.radio_btn_Sub);
        textView_cer_info= (TextView)findViewById(R.id.textView_cer_info);
        textView_pre_info= (TextView)findViewById(R.id.textView_pre_info);
        textView_sub_info= (TextView)findViewById(R.id.textView_sub_info);

        mListView = (ListView) findViewById(R.id.list_apply);

        textView_totalTime = (TextView) findViewById(R.id.textView_totalTime);
        textView_totalUSD = (TextView) findViewById(R.id.textView_totalUSD);

        textView_sent = (TextView) findViewById(R.id.textView_sent);

        rdg1.setOnCheckedChangeListener(listener1);

        Save_Select_item.clear();//存 選取的任務
        Save_Lab_Temp.clear();//存 選取任務 F_SeqNo
        Save_Cer_Time.clear();
        Save_Cer_Expense.clear();
        Save_RWorkID.clear();

        textView_totalTime.setText("0 週");
        textView_totalUSD.setText("0");

        Find_My_Fac_Model(UserData.WorkID);

        //認證Info
        textView_cer_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(msibook_cec_new_application.this, msibook_cec_cer_info.class);

                intent.putExtra("title","認證 Certification");

                intent.putExtra("subject","Pretest + Submission");

                msibook_cec_new_application.this.startActivity(intent);

            }
        });

        //驗證Info
        textView_pre_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(msibook_cec_new_application.this, msibook_cec_cer_info.class);

                intent.putExtra("title","驗證 Pretest");

                intent.putExtra("subject","進行產品測試，\n並產出報告給客戶。");

                msibook_cec_new_application.this.startActivity(intent);

            }
        });

        //取證
        textView_sub_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(msibook_cec_new_application.this, msibook_cec_cer_info.class);

                intent.putExtra("title","取證 Submission");

                intent.putExtra("subject","確認測試報告無誤後，\n提交至認證機構取證。");

                msibook_cec_new_application.this.startActivity(intent);

            }
        });


        textView_sent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent msibook_cec_new_application = new Intent(msibook_cec_new_application.this, msibook_cec_double_check.class);

                msibook_cec_new_application.putExtra("Column1",Save_Model);

                msibook_cec_new_application.putExtra("Model_ID",Save_ModelID); //專案ID

                msibook_cec_new_application.putExtra("F_Cer_Application",Save_Select_Radio_Btn);  //rg1.getChildAt(radioId)

                msibook_cec_new_application.putStringArrayListExtra("Item", (ArrayList<String>) Save_Select_item);

                msibook_cec_new_application.putStringArrayListExtra("Temp_Item", (ArrayList<String>) Save_Lab_Temp);

                msibook_cec_new_application.putStringArrayListExtra("Cer_Time_Item", (ArrayList<String>) Save_Cer_Time);

                msibook_cec_new_application.putStringArrayListExtra("Cer_Expense_Item", (ArrayList<String>) Save_Cer_Expense);

                msibook_cec_new_application.putStringArrayListExtra("RWorkID_Item", (ArrayList<String>) Save_RWorkID);

                msibook_cec_new_application.putExtra("F_Cer_Time",(String.valueOf(SaveTotaltime)));  //rg1.getChildAt(radioId)

                msibook_cec_new_application.putExtra("F_Cer_Expense",String.valueOf(TotalUSD));  //rg1.getChildAt(radioId)

                startActivityForResult(msibook_cec_new_application,1);
            }
        });

    }

    private RadioGroup.OnCheckedChangeListener listener1 = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            // TODO Auto-generated method stub
            switch (checkedId) {
                case R.id.radio_btn_Cer:
                    Find_Certification_Apply("0");

                    Save_Select_item.clear();
                    Save_Lab_Temp.clear();
                    Save_Cer_Time.clear();
                    Save_Cer_Expense.clear();
                    Save_RWorkID.clear();

                    int selectedRadioButtonID = rdg1.getCheckedRadioButtonId();
                    if (selectedRadioButtonID != -1) {
                        RadioButton selectedRadioButton = (RadioButton) findViewById(selectedRadioButtonID);
                        Save_Select_Radio_Btn = selectedRadioButton.getText().toString();
                    }
                    else{
                        Save_Select_Radio_Btn="";
                    }

                    Log.w("認證","認證");
                    SaveTotaltime = 0.0;
                    TotalUSD = 0.0;
                    textView_totalTime.setText("0 週");
                    textView_totalUSD.setText("0");
                    break;
                case R.id.radio_btn_Pre:
                    Find_Certification_Apply("1");

                    Save_Select_item.clear();
                    Save_Lab_Temp.clear();
                    Save_Cer_Time.clear();
                    Save_Cer_Expense.clear();
                    Save_RWorkID.clear();

                    int selectedRadioButtonID2 = rdg1.getCheckedRadioButtonId();
                    if (selectedRadioButtonID2 != -1) {
                        RadioButton selectedRadioButton = (RadioButton) findViewById(selectedRadioButtonID2);
                        Save_Select_Radio_Btn = selectedRadioButton.getText().toString();
                    }
                    else{
                        Save_Select_Radio_Btn="";
                    }

                    Log.w("驗證","驗證");
                    SaveTotaltime = 0.0;
                    TotalUSD = 0.0;
                    break;
                case R.id.radio_btn_Sub:
                    Find_Certification_Apply("2");

                    Save_Select_item.clear();
                    Save_Lab_Temp.clear();
                    Save_Cer_Time.clear();
                    Save_Cer_Expense.clear();
                    Save_RWorkID.clear();

                    int selectedRadioButtonID3 = rdg1.getCheckedRadioButtonId();
                    if (selectedRadioButtonID3 != -1) {
                        RadioButton selectedRadioButton = (RadioButton) findViewById(selectedRadioButtonID3);
                        Save_Select_Radio_Btn = selectedRadioButton.getText().toString();
                    }
                    else{
                        Save_Select_Radio_Btn="";
                    }

                    Log.w("取證","取證");
                    SaveTotaltime = 0.0;
                    TotalUSD = 0.0;
                    break;
            }
        }
    };

//    private RadioGroup.OnCheckedChangeListener listener2 = new RadioGroup.OnCheckedChangeListener() {
//
//        @Override
//        public void onCheckedChanged(RadioGroup group, int checkedId) {
//            // TODO Auto-generated method stub
//
//            switch (checkedId) {
//                case R.id.radio_btn_Pre:
//                    radio_group1.clearCheck();
//                    break;
//            }
//        }
//    };


    // 抓取 對硬 Radio 認證選項的 Item
    private void Find_Certification_Apply(String ApplicationTpye) {

        //顯示  讀取等待時間Bar
        progressBar.show();

        Apply_Item_List.clear();

        RequestQueue mQueue = Volley.newRequestQueue(this);

        //String Path = GetServiceData.ServicePath + "/Find_Certification_Apply?F_Keyin=" + ApplicationTpye;
        String Path = "http://wtsc.msi.com.tw/Test/MsiBook_App_Service.asmx/Find_Certification_Apply?ApplicationTpye=" + ApplicationTpye;

        GetServiceData.getString(Path, mQueue, new GetServiceData.VolleyCallback() {
            @Override
            public void onSuccess(JSONObject result) {

                try {

                    JSONArray UserArray = new JSONArray(result.getString("Key"));

                    for (int i = 0; i < UserArray.length(); i++) {
                        JSONObject IssueData = UserArray.getJSONObject(i);

                        String F_SeqNo = String.valueOf(IssueData.getInt("F_SeqNo")); //"F_SeqNo": 53,

                        String F_Cer_Class = String.valueOf(IssueData.getString("F_Cer_Class")); // "F_Cer_Class": "A",

                        String F_Cer_Logo = String.valueOf(IssueData.getString("F_Cer_Logo")); // "F_Cer_Logo": "ErP Lot 3",

                        String F_Cer_Time = String.valueOf(IssueData.getDouble("F_Cer_Time")); // "F_Cer_Time": 4.00,

                        String F_Cer_Expense = String.valueOf(IssueData.getDouble("F_Cer_Expense")); // "F_Cer_Expense": 1000.00,

                        String F_RWorkID = String.valueOf(IssueData.getString("F_RWorkID")); //"F_RWorkID": "10012947",

                        String F_Cer_Pic = String.valueOf(IssueData.getString("F_Cer_Pic")); //   "F_Cer_Pic": "10003130@2014-0703-0606-347545@10013133.bmp",

                        String F_Cer_Application = String.valueOf(IssueData.getString("F_Cer_Application")); // "F_Cer_Application": "0"

                        Apply_Item_List.add(i,new Apply_Item(F_SeqNo,F_Cer_Class,F_Cer_Logo,F_Cer_Time,F_Cer_Expense,F_RWorkID,F_Cer_Pic,F_Cer_Application));

                    }

                    mListView = (ListView)findViewById(R.id.list_apply);
                    mListView.setDivider(null);
                    mListView.setDividerHeight(0);

                    mApplyAdapter = new ApplyAdapter(mContext,Apply_Item_List);

                    mListView.setAdapter(mApplyAdapter);

                    //關閉-讀取等待時間Bar
                    progressBar.dismiss();

                    //編制細項to 第三頁
                    mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

//                            Intent intent = new Intent();
//
//                            intent.setClass(msibook_dqaweekly_main_page2.this, msibook_dqaweekly_main_page3.class);
//                            //開啟Activity
//                            startActivity(intent);


                        }
                    });
                }
                catch (JSONException ex) {

                    Log.w("Json",ex.toString());
                }

            }
        });


    }


    //抓部門代號、名稱 Spinner
    private void Find_My_Fac_Model(String F_Keyin) {

        ArrayModel.clear();
        ArrayModelID.clear();

        RequestQueue mQueue = Volley.newRequestQueue(this);

        HTTPSTrustManager.allowAllSSL();//信任所有证书，信任憑證
        String Path = GetServiceData.ServicePath + "/Find_My_Fac_Model?F_Keyin=" + F_Keyin;
        GetServiceData.getString(Path, mQueue, new GetServiceData.VolleyCallback() {
            @Override
            public void onSuccess(JSONObject result) {

                try {
                    JSONArray UserArray = new JSONArray(result.getString("Key"));

                    for (int i = 0; i < UserArray.length(); i++) {
                        JSONObject IssueData = UserArray.getJSONObject(i);

                        String Model = IssueData.getString("Model");

                        String ModelID = String.valueOf(IssueData.getInt("ModelID"));

                        ArrayModel.add(i, Model);

                        ArrayModelID.add(i, ModelID);
                    }
//                    Log.w("wowowowowow",String.valueOf(ArrayWeek.size()));

                    spinner_project = (Spinner) findViewById(R.id.spinner_project);

                    mDropDownAdapter = new DropDownAdapter(msibook_cec_new_application.this, ArrayModel,ArrayModelID);

                    spinner_project.setAdapter(mDropDownAdapter);

                    spinner_project.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {


                        public void onItemSelected(AdapterView adapterView, View view, int position, long id) {

                            DropDownAdapter DropDownAdapter =  (DropDownAdapter)adapterView.getAdapter();

                            DropDownAdapter.selectedItem_project = position;

                            mDropDownAdapter.notifyDataSetChanged();


                            Save_Model = ArrayModel.get(position);
                            Save_ModelID = ArrayModelID.get(position);

                            Log.w("選到的model",String.valueOf(Save_Model));
                            Log.w("選到的ID",String.valueOf(Save_ModelID));

                        }

                        public void onNothingSelected(AdapterView arg0) {

                        }
                    });

                    //Find_WeekReport(ArrayDeptID.get(0),ArrayWeek.get(0));

                } catch (JSONException ex) {

                }

            }
        });

    }

    //--------------------------------------------------------------------------------Adapter--------------------------------------------------------------------------------
    //*****DropdownAdapter
    public class DropDownAdapter extends BaseAdapter {

        private LayoutInflater mLayInf;

        private List<String> ArrayModel;

        private List<String> ArrayModelID;

        private Context ProjectContext;

        public int selectedItem_project;

        public DropDownAdapter(Context context,  List<String> ArrayModel,List<String> ArrayModelID)
        {
            mLayInf = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            ProjectContext = context;

            this.ArrayModel = ArrayModel;

            this.ArrayModelID = ArrayModelID;
        }
        @Override
        public int getCount() {
            return ArrayModel.size();
        }

        @Override
        public Object getItem(int position) {
            return ArrayModel.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View v = new View(ProjectContext);

            v = mLayInf.inflate(R.layout.msibook_cec_spinner_layout, parent, false);

            TextView style = (TextView) v.findViewById(R.id.style);

            style.setText(ArrayModel.get(position));

            Log.w("test","test");

            return v;
        }
        @Override
        public View getDropDownView(int position,View convertView,ViewGroup parent)
        {
            View v = null;
            v = mLayInf.inflate(R.layout.msibook_cec_spinner_layout, parent, false);

            TextView style = (TextView) v.findViewById(R.id.style);

            style.setText(ArrayModel.get(position));

            // If this is the selected item position
            if (position == selectedItem_project) {
                style.setBackgroundColor(Color.parseColor("#d94045"));//灰色 848484
            }
            else {
                // for other views
                style.setBackgroundColor(Color.parseColor("#ffffff"));//藍色 618db5
            }
            return v;
        }

    }



    //ApplyAdapter
    public class ApplyAdapter extends BaseAdapter {

        private LayoutInflater mLayInf;

        private List<Apply_Item> Apply_Item_List;

        private Context ProjectContext;

        private String Title;

        public ApplyAdapter(Context context, List<Apply_Item> Apply_Item_List)
        {
            mLayInf = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            ProjectContext = context;

            this.Apply_Item_List = Apply_Item_List;

        }
        @Override
        public int getCount() {
            return Apply_Item_List.size();
        }

        @Override
        public Object getItem(int position) {
            return Apply_Item_List.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            View v = new View(ProjectContext);

            v = mLayInf.inflate(R.layout.msibook_cec_apply_item_adapter, parent, false);

            final LinearLayout linear_apply = (LinearLayout) v.findViewById(R.id.linear_apply);
            final TextView textView_checkbox = (TextView) v.findViewById(R.id.textView_checkbox);
            TextView textView_logo = (TextView) v.findViewById(R.id.textView_logo);

            textView_checkbox.setBackgroundResource(R.mipmap.ehr_btn_switch_point_off);

            textView_logo.setText(Apply_Item_List.get(position).GetF_Cer_Logo());

            linear_apply.setOnClickListener(new View.OnClickListener() { //主體點擊時 做判斷
                @Override
                public void onClick(View v) {
                    if(textView_checkbox.getBackground().getConstantState()==getResources().getDrawable(R.mipmap.ehr_btn_switch_point_off).getConstantState()) //打勾
                    {
                        textView_checkbox.setBackgroundResource(R.mipmap.facility_ic_area_check);
                        SaveTotaltime += Double.valueOf(Apply_Item_List.get(position).GetF_Cer_Time());
                        Integer Int_totaltime = SaveTotaltime.intValue();

                        TotalUSD += Double.valueOf(Apply_Item_List.get(position).GetF_Cer_Expense());
                        Integer Int_USD  = TotalUSD.intValue();

                        textView_totalTime.setText(String.valueOf(Int_totaltime)+" 週");
                        textView_totalUSD.setText(String.valueOf(Int_USD));

                        Save_Select_item.add(Apply_Item_List.get(position).GetF_Cer_Logo());// 陣列加入 ---認證項目名稱
                        Save_Lab_Temp.add(Apply_Item_List.get(position).GetF_SeqNo()); // 陣列加入 ---認證代碼
                        Save_Cer_Time.add(Apply_Item_List.get(position).GetF_Cer_Time()); // 陣列加入    ---週次
                        Save_Cer_Expense.add(Apply_Item_List.get(position).GetF_Cer_Expense());// 陣列加入    ---美金
                        Save_RWorkID.add(Apply_Item_List.get(position).GetF_RWorkID());// 陣列加入    ---負責人工號

                        Log.w("Add_Item",Apply_Item_List.get(position).GetF_Cer_Logo());
                        Log.w("Add_Lab_Temp",Apply_Item_List.get(position).GetF_SeqNo());
                        Log.w("Add_totalTiem",Apply_Item_List.get(position).GetF_Cer_Time());
                        Log.w("Add_USD",Apply_Item_List.get(position).GetF_Cer_Expense());
                        Log.w("Add_WorkID",Apply_Item_List.get(position).GetF_RWorkID());

                    }
                    else if(textView_checkbox.getBackground().getConstantState()==getResources().getDrawable(R.mipmap.facility_ic_area_check).getConstantState()) //勾勾拿掉
                    {
                        textView_checkbox.setBackgroundResource(R.mipmap.ehr_btn_switch_point_off);

                        SaveTotaltime -= Double.valueOf(Apply_Item_List.get(position).GetF_Cer_Time());
                        Integer Int_totaltime = SaveTotaltime.intValue();

                        TotalUSD -= Double.valueOf(Apply_Item_List.get(position).GetF_Cer_Expense());
                        Integer Int_USD  = TotalUSD.intValue();

                        textView_totalTime.setText(String.valueOf(Int_totaltime)+" 週");
                        textView_totalUSD.setText(String.valueOf(Int_USD));
                        int Remove_mark;
                        for (int i = 0; i < Save_Select_item.size(); i++) {
                            if(Save_Select_item.get(i).contains(Apply_Item_List.get(position).GetF_Cer_Logo())){  //移除  項目
                                Save_Select_item.remove(i);

                                Save_Lab_Temp.remove(i);
                                Save_Cer_Time.remove(i);
                                Save_Cer_Expense.remove(i);
                                Save_RWorkID.remove(i);
                                //Log.w("RRRRE_Item",String.valueOf(Save_Select_item.get(i)));
                            }
                        }
//                        for (int i = 0; i < Save_Lab_Temp.size(); i++) {
//                            if(Save_Lab_Temp.get(i).contains(Apply_Item_List.get(position).GetF_SeqNo())){       //移除  驗證 代碼
//                                Save_Lab_Temp.remove(i);
//                                Log.w("RRRRE_Lab_Temp",String.valueOf(Save_Lab_Temp.get(i)));
//                            }
//                        }
//                        for (int i = 0; i < Save_Cer_Time.size(); i++) {
//                            if(Save_Cer_Time.get(i).contains(Apply_Item_List.get(position).GetF_Cer_Time())){        //移除 週次
//                                Save_Cer_Time.remove(i);
//                                Log.w("RRRRE_Cer_Time",String.valueOf(Save_Cer_Time.get(i)));
//                            }
//                        }
//                        for (int i = 0; i < Save_Cer_Expense.size(); i++) {
//                            if(Save_Cer_Expense.get(i).contains(Apply_Item_List.get(position).GetF_Cer_Expense())){ //移除 金額
//                                Save_Cer_Expense.remove(i);
//                                Log.w("RRRRE_Cer_Expense",String.valueOf(Save_Cer_Expense.get(i)));
//                            }
//                        }
//                        for (int i = 0; i < Save_RWorkID.size(); i++) {
//                            if(Save_RWorkID.get(i).contains(Apply_Item_List.get(position).GetF_RWorkID())){  //移除  工號
//                                Save_RWorkID.remove(i);
//                                Log.w("RRRRE_RWorkID",String.valueOf(Save_RWorkID.get(i)));
//                            }
//                        }


                    }
                }
            });

            return v;
        }

    }


    //--------------------------------------------------------------------------------Item--------------------------------------------------------------------------------
    //Detail_Item
    public class Apply_Item {

        String F_SeqNo;

        String F_Cer_Class;

        String F_Cer_Logo;

        String F_Cer_Time;

        String F_Cer_Expense;

        String F_RWorkID;

        String F_Cer_Pic;

        String F_Cer_Application;



        public Apply_Item(String F_SeqNo,String F_Cer_Class,String F_Cer_Logo,String F_Cer_Time,String F_Cer_Expense,String F_RWorkID,String F_Cer_Pic,String F_Cer_Application)
        {
            this.F_SeqNo = F_SeqNo;

            this.F_Cer_Class = F_Cer_Class;

            this.F_Cer_Logo = F_Cer_Logo;

            this.F_Cer_Time = F_Cer_Time;

            this.F_Cer_Expense = F_Cer_Expense;

            this.F_RWorkID = F_RWorkID;

            this.F_Cer_Pic = F_Cer_Pic;

            this.F_Cer_Application = F_Cer_Application;
        }


        public String GetF_SeqNo()
        {
            return this.F_SeqNo;
        }

        public String GetF_Cer_Class()
        {
            return this.F_Cer_Class;
        }

        public String GetF_Cer_Logo()
        {
            return this.F_Cer_Logo;
        }

        public String GetF_Cer_Time()
        {
            return this.F_Cer_Time;
        }

        public String GetF_Cer_Expense()
        {
            return this.F_Cer_Expense;
        }

        public String GetF_RWorkID()
        {
            return this.F_RWorkID;
        }

        public String GetF_Cer_Pic()
        {
            return this.F_Cer_Pic;
        }

        public String GetF_Cer_Application()
        {
            return this.F_Cer_Application;
        }

    }


}
