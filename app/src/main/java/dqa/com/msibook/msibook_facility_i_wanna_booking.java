package dqa.com.msibook;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static java.security.AccessController.getContext;

public class msibook_facility_i_wanna_booking extends AppCompatActivity {

    private Context mContext;

    private TextView textView_F_Facility;
    private TextView textView_F_AssetNo;

    private String GetF_Facility;
    private String GetF_AssetNo;
    private String GetF_Master_ID;
    private String GetF_Is_Restrict;
    private String SaveModel;
    private String SaveModelID;//如同PM ID帶到下頁去

    private TextView textView_SDate;
    private TextView textView_EDate;
    private TextView textView_booking_rules;
    private EditText editText_F_Desc;

    private List<String> ArrayModelID = new ArrayList<String>();
    private List<String> ArrayModel = new ArrayList<String>();
    private DropDownModelAdapter mDropDownModelAdapter;
    private Spinner spinner_project;

    private LinearLayout linear_StartDate;
    private LinearLayout linear_EndDate;
    private LinearLayout linear_sent;

    private ProgressDialog progressBar;

    private Integer Click_check;

    //點選Local回傳狀態
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(data !=null) {
            Bundle bundle = data.getExtras();
            Integer CheckBooking = Integer.valueOf(bundle.getString("Booking_Check"));
            if(CheckBooking ==1){
                Intent intent = new Intent();
                Bundle b = new Bundle();
                b.putString("Booking_Check","1");
                intent.putExtras(b);
                setResult(RESULT_OK, intent);
                finish();
            }

            String Type = bundle.getString("Type");
            if(Click_check == 0){
                textView_SDate.setText(Type);
            }else{
                textView_EDate.setText(Type);
            }

//            switch (Int_Type) {
//                case 0:
//                    main_title_text.setText("台北實驗室");
//                    textView_location.setText("台北");
//                    Set_Location = 0;
//
//                    msibook_facility_machine Tab1 = (msibook_facility_machine) mSectionsPagerAdapter.getFragment(0);
//                    Tab1.Location = String.valueOf(Set_Location) ;
//                    Tab1.Find_Fac_Type_List(String.valueOf(Set_Location));
//                    Tab1.Find_Fac_List(String.valueOf(Set_Location),"0");
//                    break;
//
//                case 1:
//                    main_title_text.setText("昆山實驗室");
//                    textView_location.setText("昆山");
//                    Set_Location = 1;
//
//                    msibook_facility_machine Tab2 = (msibook_facility_machine) mSectionsPagerAdapter.getFragment(0);
//                    Tab2.Location = String.valueOf(Set_Location) ;
//                    Tab2.Find_Fac_Type_List(String.valueOf(Set_Location));
//                    Tab2.Find_Fac_List(String.valueOf(Set_Location),"0");
//                    break;
//
//                case 2:
//                    main_title_text.setText("寶安實驗室");
//                    textView_location.setText("寶安");
//                    Set_Location = 2;
//
//                    msibook_facility_machine Tab3 = (msibook_facility_machine) mSectionsPagerAdapter.getFragment(0);
//                    Tab3.Location = String.valueOf(Set_Location) ;
//                    Tab3.Find_Fac_Type_List(String.valueOf(Set_Location));
//                    Tab3.Find_Fac_List(String.valueOf(Set_Location),"0");
//                    break;
//
//            }

        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msibook_facility_i_wanna_booking);

        mContext = this;

        progressBar = new ProgressDialog(this);
        progressBar.setCancelable(true);
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.setMessage("資料載入中");

        textView_F_Facility = (TextView)findViewById(R.id.textView_F_Facility);
        textView_F_AssetNo = (TextView)findViewById(R.id.textView_F_AssetNo);

        linear_StartDate = (LinearLayout) findViewById(R.id.linear_StartDate);
        linear_EndDate = (LinearLayout) findViewById(R.id.linear_EndDate); //
        linear_sent = (LinearLayout) findViewById(R.id.linear_sent);

        GetF_Facility = getIntent().getStringExtra("F_Facility");//抓F_Facility
        GetF_AssetNo = getIntent().getStringExtra("F_AssetNo");//抓F_AssetNo
        GetF_Master_ID = getIntent().getStringExtra("F_Master_ID");//抓機器序號
        GetF_Is_Restrict = getIntent().getStringExtra("F_Is_Restrict");//抓0=3個月  1=3天

        textView_F_Facility.setText(GetF_Facility);
        textView_F_AssetNo.setText("財編 : " + GetF_AssetNo);

        textView_SDate = (TextView)findViewById(R.id.textView_SDate);
        textView_EDate = (TextView)findViewById(R.id.textView_EDate);
        textView_booking_rules =(TextView)findViewById(R.id.textView_booking_rules);
        editText_F_Desc = (EditText)findViewById(R.id.editText_F_Desc);

        if(Integer.valueOf(GetF_Is_Restrict) == 0){
            textView_booking_rules.setText("申請預約有效時間為3個月內");
        }else{
            textView_booking_rules.setText("申請預約有效時間為3日內");
        }


        linear_StartDate.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                Click_check = 0;

                //msibook_facility_setDateTime
                Intent intent = new Intent();
                //讓選日期判斷是 S還是E
                intent.putExtra("Click_check", String.valueOf(Click_check));
//
//                //給F_Facility
//                intent.putExtra("F_Facility", GetF_Facility);
//                //給F_AssetNo
//                intent.putExtra("F_AssetNo", GetF_AssetNo);
//                //給F_Facility
//                intent.putExtra("F_Master_ID", GetF_Master_ID);
//                //給F_AssetNo
//                intent.putExtra("F_Is_Restrict", GetF_Is_Restrict);

                // GO TO  booking_main
                intent.setClass(msibook_facility_i_wanna_booking.this, msibook_facility_setDateTime_pro.class);
                //開啟Activity
                startActivityForResult(intent,1);

                return false;
            }
        });

        linear_EndDate.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                Click_check = 1;

                //msibook_facility_setDateTime
                Intent intent = new Intent();
                //讓選日期判斷是 S還是E
                intent.putExtra("Click_check",  String.valueOf(Click_check));
//
//                //給F_Facility
//                intent.putExtra("F_Facility", GetF_Facility);
//                //給F_AssetNo
//                intent.putExtra("F_AssetNo", GetF_AssetNo);
//                //給F_Facility
//                intent.putExtra("F_Master_ID", GetF_Master_ID);
//                //給F_AssetNo
//                intent.putExtra("F_Is_Restrict", GetF_Is_Restrict);

                // GO TO  booking_main
                intent.setClass(msibook_facility_i_wanna_booking.this, msibook_facility_setDateTime_pro.class);
                //開啟Activity
                startActivityForResult(intent,1);

                return false;
            }
        });

        linear_sent.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                //判斷主旨有沒有KEY
                if(editText_F_Desc.getText().toString().length()<1){
                    Dialog dialog=new android.support.v7.app.AlertDialog.Builder(mContext)
                            .setTitle("ERROR")//设置提示内容
                            .setMessage("描述不得為空")//设置提示内容
                            //确定按钮
                            .setPositiveButton("確定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            })
                            .create();//创建对话框
                    dialog.show();//显示对话框

                }else{
                    Intent intent = new Intent();
                    //給F_Keyin 工號
                    intent.putExtra("F_Keyin", UserData.WorkID);
                    //給F_Keyin 工號   SaveModel
                    intent.putExtra("Model", SaveModel);
                    //F_Master_ID
                    intent.putExtra("F_Master_ID", GetF_Master_ID);
                    //F_Subject
                    intent.putExtra("F_Subject", editText_F_Desc.getText().toString());
                    //F_Desc
                    intent.putExtra("F_Desc","");
                    //給F_Facility
                    intent.putExtra("F_StartDate", textView_SDate.getText());
                    //給F_AssetNo
                    intent.putExtra("F_EndDate", textView_EDate.getText());
                    //給F_Facility
                    intent.putExtra("F_PM_ID", SaveModelID);
                    Log.w("SaveModelID",SaveModelID);
                    //給F_AssetNo
                    intent.putExtra("F_Is_Restrict", GetF_Is_Restrict);
                    // GO TO  booking_main
                    intent.setClass(msibook_facility_i_wanna_booking.this, msibook_facility_double_check_booking.class);
                    //開啟Activity
                    startActivityForResult(intent,1);
                }

                return false;
            }
        });

        Find_My_Fac_Model(UserData.WorkID);
    }


    public void Find_My_Fac_Model(String F_Keyin) {

        //顯示  讀取等待時間Bar
        progressBar.show();

        ArrayModelID.clear();
        ArrayModel.clear();

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

                        String ModelID = String.valueOf(IssueData.getInt("ModelID"));

                        String Model = String.valueOf(IssueData.getString("Model"));

                        ArrayModelID.add(i, ModelID);
                        Log.w("ModelID",String.valueOf(ModelID));

                        ArrayModel.add(i, Model);

                    }
                    spinner_project = (Spinner) findViewById(R.id.spinner_project);

                    mDropDownModelAdapter = new DropDownModelAdapter(msibook_facility_i_wanna_booking.this, ArrayModel,ArrayModelID);

                    spinner_project.setAdapter(mDropDownModelAdapter);

                    spinner_project.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {

                        public void onItemSelected(AdapterView adapterView, View view, int position, long id) {

                            DropDownModelAdapter DropDownModelAdapter =  (DropDownModelAdapter)adapterView.getAdapter();

                            DropDownModelAdapter.selectedItemdp = position;

                            mDropDownModelAdapter.notifyDataSetChanged();

                            SaveModel = ArrayModel.get(position);
                            SaveModelID = ArrayModelID.get(position);
                            Log.w("SaveModel",SaveModel +" "+SaveModelID);
//
//                            if (ArrayWeek.size() > 0)
//                            {
//                                Find_WeekReport(ArrayDeptID.get(position),SetF_Year,ArrayWeek.get(spinner_week.getSelectedItemPosition()).replace("週",""));
//                                //Find_Weekly_Content(ArrayDeptID.get(spinnerdp.getSelectedItemPosition()),ArrayWeek.get(position).replace("週",""));
//                                //Find_Over_Hour(ArrayDeptID.get(position),ArrayWeek.get(spinner_week.getSelectedItemPosition()).replace("週",""));
//                            }
//                            else
//                            {
//                                Find_WeekReport(ArrayDeptID.get(position),SetF_Year,"0");
//                                //Find_Weekly_Content(ArrayDeptID.get(position),"0");
//                                //Find_Over_Hour(ArrayDeptID.get(position),"0");
//                            }
//
//                            putEtradp = (String) spinner_project.getSelectedItem();
//                            //抓部門代號到第二頁去
//                            putEtraDepID = ArrayDeptID.get(position);
                        }

                        public void onNothingSelected(AdapterView arg0) {

                        }
                    });
                }
                catch (JSONException ex) {
                    Log.w("Json",ex.toString());
                }
            }
        });

        //關閉-讀取等待時間Bar
        progressBar.dismiss();

    }


    public class DropDownModelAdapter extends BaseAdapter {

        private LayoutInflater mLayInf;

        private List<String> ArrayModel;

        private List<String> ArrayModelID;

        private Context ProjectContext;

        public int selectedItemdp;

        public DropDownModelAdapter(Context context,  List<String> ArrayModel,List<String> ArrayModelID)
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

            v = mLayInf.inflate(R.layout.facility_booking_spinnertextlayout, parent, false);

            TextView style = (TextView) v.findViewById(R.id.style);

            style.setText(ArrayModel.get(position));

            Log.w("test","test");

            return v;
        }
        @Override
        public View getDropDownView(int position,View convertView,ViewGroup parent)
        {
            View v = null;
            v = mLayInf.inflate(R.layout.facility_booking_spinnertextlayout, parent, false);

            TextView style = (TextView) v.findViewById(R.id.style);

            style.setText(ArrayModel.get(position));

            // If this is the selected item position
            if (position == selectedItemdp) {
                style.setBackgroundColor(Color.parseColor("#e2e2e2"));//灰色
            }
            else {
                // for other views
                style.setBackgroundColor(Color.parseColor("#ffffff"));//藍色
            }
            return v;
        }

    }


}
