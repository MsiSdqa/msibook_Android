package dqa.com.msibook;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class msibook_request_add extends AppCompatActivity {

    public class dept_Member_Item implements Serializable {

        String CNAME;
        String WorkID;

        public dept_Member_Item(String CNAME, String WorkID) {
            this.CNAME = CNAME;
            this.WorkID = WorkID;
        }
    }

    private List<dept_Member_Item> dept_Member_List = new ArrayList<dept_Member_Item>();

    private DropDownMember mDropDownMemberAdapter;

    private int SelectMemberPosition;

    private Context mContext;

    private EditText editText_subject;

    private LinearLayout linear_finish_time;

    private EditText editText_Content;

    private  Spinner spinner_case_officer;

    private LinearLayout linear_sent;

    private  TextView textView_finish_time;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msibook_request_add);

        mContext = this;

        textView_finish_time = (TextView)findViewById(R.id.textView_finish_time);

        textView_finish_time.setText("選擇完成日");

        editText_subject = (EditText)findViewById(R.id.editText_subject);

        linear_finish_time = (LinearLayout)findViewById(R.id.linear_finish_time);

        linear_finish_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int Click_check = 0;

                //msibook_facility_setDateTime
                Intent intent = new Intent();
                //讓選日期判斷是 S還是E

                //讓選日期判斷是 S還是E
                intent.putExtra("Click_check", String.valueOf(Click_check));
                // GO TO  booking_main
                intent.setClass(mContext, msibook_facility_setDateTime.class);
                //開啟Activity
                startActivityForResult(intent,1);
            }
        });

        editText_Content = (EditText)findViewById(R.id.editText_Content);

        spinner_case_officer = (Spinner)findViewById(R.id.spinner_case_officer);

        linear_sent = (LinearLayout)findViewById(R.id.linear_sent);

        linear_sent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                linear_sent.setFocusableInTouchMode(true);
                //linear_sent.setFocusable(false);
                //editText_Content.setClickable(false);
//                editText_Content.clearFocus();
//
                String F_Keyin;
                String F_Subject;
                String F_Desc;
                String F_ExpectFixDate;
                String F_RespUser;


                //0305 閃退問題順便加入防呆
                if(editText_subject.getText().toString().isEmpty()){
                    popup_subjectError();
                }else if(textView_finish_time.getText().toString().contains("選擇完成日")){
                    popup_finish_timeError();
                }else if(editText_Content.getText().toString().isEmpty()){
                    popup_ContentError();
                }else{
                    F_Keyin = UserData.WorkID;
                    F_Subject = editText_subject.getText().toString();
                    F_Desc = editText_Content.getText().toString();
                    F_ExpectFixDate = textView_finish_time.getText().toString();
                    F_RespUser = dept_Member_List.get(SelectMemberPosition).WorkID;
                    Log.w("新增工單資料顯示","\nF_Keyin - "+F_Keyin+"\nF_Subject -"+F_Subject+"\nF_Desc -"+F_Desc+"\nF_ExpectFixDate -"+F_ExpectFixDate+"\nF_RespUser -"+F_RespUser);
                    Insert_Request(F_Keyin,F_Subject,F_Desc,F_ExpectFixDate,F_RespUser);

                    //推播通知
//                    SendPushNotificationDeviceByWorkID mSendPushNotificationDeviceByWorkID;
//                    mSendPushNotificationDeviceByWorkID = new SendPushNotificationDeviceByWorkID(mContext);
//                    mSendPushNotificationDeviceByWorkID.Insert_SendPushNotificationDeviceByWorkID(F_RespUser,"工單提醒通知",UserData.Name + " 新增一筆工單給您，請確認。","","","");
                }




            }
        });

        //Find_Dept_Member(UserData.WorkID);
        Find_Dept_Member(UserData.DeptID);
    }

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
            String DateString = bundle.getString("Type");

            textView_finish_time = (TextView)findViewById(R.id.textView_finish_time);

            //textView_finish_time.setText(DateString.substring(0,10));
            textView_finish_time.setText(DateString);
        }



    }

    private void popup_subjectError(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Error!!").setMessage("主旨尚未輸入!!").setPositiveButton("確認", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        }).show();
    }

    private void popup_finish_timeError(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Error!!").setMessage("日期尚未選擇!!").setPositiveButton("確認", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        }).show();
    }

    private void popup_ContentError(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Error!!").setMessage("請輸入內容!!").setPositiveButton("確認", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        }).show();
    }

    private void Insert_Request(String F_Keyin,String F_Subject,String F_Desc,String F_ExpectFixDate,String F_RespUser) {



        RequestQueue mQueue = Volley.newRequestQueue(this);

        Map<String,String> map = new HashMap<String, String>();
        map.put("F_Keyin", F_Keyin);
        map.put("F_Subject", F_Subject);
        map.put("F_Desc", F_Desc);
        map.put("F_ExpectFixDate", F_ExpectFixDate);
        map.put("F_RespUser", F_RespUser);

        if (mQueue == null) {
            mQueue = Volley.newRequestQueue(this);
        }
        HTTPSTrustManager.allowAllSSL();//信任所有证书，信任憑證
        //String Path = GetServiceData.ServicePath + "/Insert_Request";
        String Path = "https://wtsc.msi.com.tw/TEST/MsiBook_App_Service.asmx//Insert_Request";
        //String Path = "http://wtsc.msi.com.tw/IMS/MsiBook_App_Service.asmx/Insert_E_HR_Master";

        GetServiceData.SendPostRequest(Path, mQueue, new GetServiceData.VolleyStringCallback() {
            public void onSendRequestSuccess(String result) {
                Bundle bundle = new Bundle();
                bundle.putString("Finish","0");
                msibook_request_add.this.setResult(RESULT_OK,msibook_request_add.this.getIntent().putExtras(bundle));
                finish();
            }

            @Override
            public void onSendRequestError(String result) {



            }

        }, map);
    }

    private void Find_Dept_Member(String DeptID) {

        RequestQueue mQueue = Volley.newRequestQueue(this);

        Map<String,String> map = new HashMap<String, String>();
        map.put("DeptID", DeptID);

        if (mQueue == null) {
            mQueue = Volley.newRequestQueue(this);
        }
        HTTPSTrustManager.allowAllSSL();//信任所有证书，信任憑證
        String Path = GetServiceData.ServicePath + "/Find_Dept_Member";
        //String Path = "http://wtsc.msi.com.tw/IMS/MsiBook_App_Service.asmx/Insert_E_HR_Master";

        GetServiceData.SendPostRequest(Path, mQueue, new GetServiceData.VolleyStringCallback() {
            @Override
            public void onSendRequestSuccess(String result) {

                try {

                    JSONObject obj = new JSONObject(result);

                    JSONArray UserArray = new JSONArray(obj.getString("Key"));

                    for (int i = 0; i < UserArray.length(); i++) {

                        JSONObject ReuqestData = UserArray.getJSONObject(i);

                        String CNAME = String.valueOf(ReuqestData.getString("CName"));
                        String WorkID = String.valueOf(ReuqestData.getString("WorkID"));

                        dept_Member_List.add(new dept_Member_Item(CNAME,WorkID));

                    }

                    Spinner spinner_case_officer = (Spinner)findViewById(R.id.spinner_case_officer);

                    mDropDownMemberAdapter = new msibook_request_add.DropDownMember(mContext, dept_Member_List);

                    spinner_case_officer.setAdapter(mDropDownMemberAdapter);

                    spinner_case_officer.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {

                        public void onItemSelected(AdapterView adapterView, View view, int position, long id) {

                            msibook_request_add.DropDownMember DropDownModelAdapter =  (msibook_request_add.DropDownMember)adapterView.getAdapter();

                            DropDownModelAdapter.selectedItemdp = position;

                            SelectMemberPosition = position;

                        }

                        public void onNothingSelected(AdapterView arg0) {

                        }
                    });



                }
                catch (JSONException ex) {
                    Log.w("Json",ex.toString());
                }

            }

            @Override
            public void onSendRequestError(String result) {



            }

        }, map);
    }



    public class DropDownMember extends BaseAdapter {

        private LayoutInflater mLayInf;

        private List<dept_Member_Item> dept_Member_List;

        private Context ProjectContext;

        public int selectedItemdp;

        public DropDownMember(Context context,   List<dept_Member_Item> dept_Member_List)
        {
            mLayInf = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            ProjectContext = context;

            this.dept_Member_List = dept_Member_List;
        }
        @Override
        public int getCount() {
            return dept_Member_List.size();
        }

        @Override
        public Object getItem(int position) {
            return dept_Member_List.get(position);
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

            try {
                style.setText(new String(dept_Member_List.get(position).CNAME.getBytes("ISO-8859-1"), "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            Log.w("test","test");

            return v;
        }
        @Override
        public View getDropDownView(int position,View convertView,ViewGroup parent)
        {
            View v = null;
            v = mLayInf.inflate(R.layout.facility_booking_spinnertextlayout, parent, false);

            TextView style = (TextView) v.findViewById(R.id.style);

            try {
                style.setText(new String(dept_Member_List.get(position).CNAME.getBytes("ISO-8859-1"), "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

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
