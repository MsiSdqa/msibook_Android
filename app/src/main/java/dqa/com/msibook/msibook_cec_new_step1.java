package dqa.com.msibook;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class msibook_cec_new_step1 extends AppCompatActivity {

    private List<String> ArrayModel = new ArrayList<String>();
    private List<String> ArrayModelID = new ArrayList<String>();

    private List<String> Get_Bundle_Item = new ArrayList<String>();

    private Context mContext;
    private ProgressDialog progressBar;

    private DropDownAdapter mDropDownAdapter;

    private String Save_Model;
    private String Save_ModelID;

    private Spinner spinner_project;

    private ListView mListView;
    private ApplyAdapter mApplyAdapter;
    private List<Apply_Item> Apply_Item_List = new ArrayList<Apply_Item>();

    private TextView textView_sent;

    private LinearLayout linear_Certification_main;
    private LinearLayout linear_Certification_choice;

    private LinearLayout linear_Pretest_main;
    private LinearLayout linear_Pretest_choice;

    private LinearLayout linear_Submission_main;
    private LinearLayout linear_Submission_choice;

    private String Save_Select_CEC_Linear;

    private String Set_ApplicationTpye;//給下一頁  讀取  子細項的項目

    //點選Local回傳狀態
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(data !=null) {
            Bundle bundle = data.getExtras();
            Integer CheckBooking = Integer.valueOf(bundle.getString("CEC_Application_Check"));
            Get_Bundle_Item = bundle.getStringArrayList("Item");
            if(CheckBooking ==1){
                Intent intent = new Intent();
                Bundle b = new Bundle();
                b.putString("CEC_Application_Check","1");
                b.putSerializable("Item", (Serializable) Get_Bundle_Item);
                intent.putExtras(b);
                setResult(RESULT_OK, intent);
                finish();
            }
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msibook_cec_new_step1);

        mContext = msibook_cec_new_step1.this;

        //讀取時間Bar
        progressBar = new ProgressDialog(this);
        progressBar.setCancelable(true);
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.setMessage("資料載入中");

        spinner_project = (Spinner) findViewById(R.id.spinner_project);//宣告部門下拉選單

        linear_Certification_main = (LinearLayout) findViewById(R.id.linear_Certification_main);//
        linear_Certification_choice = (LinearLayout) findViewById(R.id.linear_Certification_choice);//

        linear_Pretest_main = (LinearLayout) findViewById(R.id.linear_Pretest_main);//
        linear_Pretest_choice = (LinearLayout) findViewById(R.id.linear_Pretest_choice);//

        linear_Submission_main = (LinearLayout) findViewById(R.id.linear_Submission_main);//
        linear_Submission_choice = (LinearLayout) findViewById(R.id.linear_Submission_choice);//

        Set_ApplicationTpye =""; //帶下頁值 >> 服務 C B  A
        Save_Select_CEC_Linear="";//帶下頁值 >> 認證 主題

        textView_sent = (TextView) findViewById(R.id.textView_sent);

        Find_My_Fac_Model(UserData.WorkID);

        linear_Certification_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linear_Certification_choice.setBackgroundResource(R.drawable.cec_add_choice_on);
                linear_Pretest_choice.setBackgroundResource(R.drawable.cec_add_choice_off);
                linear_Submission_choice.setBackgroundResource(R.drawable.cec_add_choice_off);

                Set_ApplicationTpye="C";
                Save_Select_CEC_Linear="Certification";
            }
        });

        linear_Pretest_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linear_Certification_choice.setBackgroundResource(R.drawable.cec_add_choice_off);
                linear_Pretest_choice.setBackgroundResource(R.drawable.cec_add_choice_on);
                linear_Submission_choice.setBackgroundResource(R.drawable.cec_add_choice_off);

                Set_ApplicationTpye="B";
                Save_Select_CEC_Linear="Pretest";
            }
        });

        linear_Submission_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linear_Certification_choice.setBackgroundResource(R.drawable.cec_add_choice_off);
                linear_Pretest_choice.setBackgroundResource(R.drawable.cec_add_choice_off);
                linear_Submission_choice.setBackgroundResource(R.drawable.cec_add_choice_on);

                Set_ApplicationTpye="A";
                Save_Select_CEC_Linear="Submission";
            }
        });

        textView_sent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Set_ApplicationTpye.isEmpty()) {
                    showAlert("Error","尚未選擇申請項目");
                }else
                {
                    Intent msibook_cec_new_step1 = new Intent(msibook_cec_new_step1.this, msibook_cec_new_step2.class);

                    msibook_cec_new_step1.putExtra("Column1", Save_Model);

                    msibook_cec_new_step1.putExtra("Model_ID", Save_ModelID); //專案ID

                    msibook_cec_new_step1.putExtra("F_Cer_Application", Save_Select_CEC_Linear); //專案ID

                    msibook_cec_new_step1.putExtra("Set_ApplicationTpye", Set_ApplicationTpye); //專案ID

                    startActivityForResult(msibook_cec_new_step1, 1);
                }
            }
        });
    }

    private void showAlert(String title,String context)
    {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle(title);
        alert.setMessage(context);
        alert.show();
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

                    mDropDownAdapter = new DropDownAdapter(msibook_cec_new_step1.this, ArrayModel,ArrayModelID);

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
//                        SaveTotaltime += Double.valueOf(Apply_Item_List.get(position).GetF_Cer_Time());
//                        Integer Int_totaltime = SaveTotaltime.intValue();

//                        TotalUSD += Double.valueOf(Apply_Item_List.get(position).GetF_Cer_Expense());
//                        Integer Int_USD  = TotalUSD.intValue();

//                        textView_totalTime.setText(String.valueOf(Int_totaltime)+" 週");
//                        textView_totalUSD.setText(String.valueOf(Int_USD));

//                        Save_Select_item.add(Apply_Item_List.get(position).GetF_Cer_Logo());// 陣列加入 ---認證項目名稱
//                        Save_Lab_Temp.add(Apply_Item_List.get(position).GetF_SeqNo()); // 陣列加入 ---認證代碼
//                        Save_Cer_Time.add(Apply_Item_List.get(position).GetF_Cer_Time()); // 陣列加入    ---週次
//                        Save_Cer_Expense.add(Apply_Item_List.get(position).GetF_Cer_Expense());// 陣列加入    ---美金
//                        Save_RWorkID.add(Apply_Item_List.get(position).GetF_RWorkID());// 陣列加入    ---負責人工號

                        Log.w("Add_Item",Apply_Item_List.get(position).GetF_Cer_Logo());
                        Log.w("Add_Lab_Temp",Apply_Item_List.get(position).GetF_SeqNo());
                        Log.w("Add_totalTiem",Apply_Item_List.get(position).GetF_Cer_Time());
                        Log.w("Add_USD",Apply_Item_List.get(position).GetF_Cer_Expense());
                        Log.w("Add_WorkID",Apply_Item_List.get(position).GetF_RWorkID());

                    }
                    else if(textView_checkbox.getBackground().getConstantState()==getResources().getDrawable(R.mipmap.facility_ic_area_check).getConstantState()) //勾勾拿掉
                    {
                        textView_checkbox.setBackgroundResource(R.mipmap.ehr_btn_switch_point_off);

//                        SaveTotaltime -= Double.valueOf(Apply_Item_List.get(position).GetF_Cer_Time());
//                        Integer Int_totaltime = SaveTotaltime.intValue();

//                        TotalUSD -= Double.valueOf(Apply_Item_List.get(position).GetF_Cer_Expense());
//                        Integer Int_USD  = TotalUSD.intValue();

//                        textView_totalTime.setText(String.valueOf(Int_totaltime)+" 週");
//                        textView_totalUSD.setText(String.valueOf(Int_USD));
//                        int Remove_mark;
//                        for (int i = 0; i < Save_Select_item.size(); i++) {
//                            if(Save_Select_item.get(i).contains(Apply_Item_List.get(position).GetF_Cer_Logo())){  //移除  項目
//                                Save_Select_item.remove(i);
//
//                                Save_Lab_Temp.remove(i);
//                                Save_Cer_Time.remove(i);
//                                Save_Cer_Expense.remove(i);
//                                Save_RWorkID.remove(i);
//                                //Log.w("RRRRE_Item",String.valueOf(Save_Select_item.get(i)));
//                            }
//                        }
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
