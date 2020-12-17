package dqa.com.msibook;

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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class msibook_certified_add extends AppCompatActivity {

    private Context mContext;

    private List<String> ArrayModelID = new ArrayList<String>();
    private List<String> ArrayModel = new ArrayList<String>();
    private DropDownModelAdapter mDropDownModelAdapter;
    private Spinner spinner_project;

    private List<String> ArrayCerID = new ArrayList<String>();
    private List<String> ArrayCerName = new ArrayList<String>();
    private DropDownCerAdapter mDropDownCerAdapter;
    private Spinner spinner_class;

    private String SaveModel;
    private String SaveModelID;//

    private String SaveCerID;
    private String SaveCerName;

    private LinearLayout linear_sent;
    private EditText editText_Content;

    private ProgressDialog progressBar;


    //點選Local回傳狀態
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(data !=null) {
            Bundle bundle = data.getExtras();
            Integer CheckBooking = Integer.valueOf(bundle.getString("Certified_Check"));
            if(CheckBooking ==1){
                Intent intent = new Intent();
                Bundle b = new Bundle();
                b.putString("Certified_Check","1");
                intent.putExtras(b);
                setResult(RESULT_OK, intent);
                finish();
            }

        }

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msibook_certified_add);

        mContext = this;

        progressBar = new ProgressDialog(this);
        progressBar.setCancelable(true);
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.setMessage("資料載入中");

        linear_sent = (LinearLayout) findViewById(R.id.linear_sent);
        editText_Content = (EditText) findViewById(R.id.editText_Content);

        Find_Certification_Model_List("10003130");

        Find_Cert_List();

        linear_sent.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {


                Intent intent = new Intent();
                //給F_Keyin 工號
                intent.putExtra("F_Keyin", UserData.WorkID);
                //給model名稱
                intent.putExtra("Model", SaveModel);
                //model代號
                intent.putExtra("ModelID", SaveModelID);
                //類別代號
                intent.putExtra("CerName", SaveCerName);
                //類別名稱
                intent.putExtra("CerID", SaveCerID);
                //說明
                intent.putExtra("F_Subject", editText_Content.getText().toString());
                //承辦人
                intent.putExtra("RespUser", "劉慶忠");
                //承辦人ID
                intent.putExtra("RespUserID", "10003130");

                intent.setClass(msibook_certified_add.this, msibook_certified_add_double_check.class);
                //開啟Activity
                startActivityForResult(intent,1);

                //判斷主旨有沒有KEY
//                if(editText_Content.getText().toString().length()<1){
//                    Dialog dialog=new android.support.v7.app.AlertDialog.Builder(mContext)
//                            .setTitle("ERROR")//设置提示内容
//                            .setMessage("說明不得為空")//设置提示内容
//                            //确定按钮
//                            .setPositiveButton("確定", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//
//                                }
//                            })
//                            .create();//创建对话框
//                    dialog.show();//显示对话框
//
//                }else{
//                    Intent intent = new Intent();
//                    //給F_Keyin 工號
//                    intent.putExtra("F_Keyin", UserData.WorkID);
//                    //給model名稱
//                    intent.putExtra("Model", SaveModel);
//                    //model代號
//                    intent.putExtra("ModelID", SaveModelID);
//                    //類別代號
//                    intent.putExtra("CerName", SaveCerName);
//                    //類別名稱
//                    intent.putExtra("CerID", SaveCerID);
//                    //說明
//                    intent.putExtra("F_Subject", editText_Content.getText().toString());
//                    //承辦人
//                    intent.putExtra("RespUser", "劉慶忠");
//                    //承辦人ID
//                    intent.putExtra("RespUserID", "10003130");
//
//                    intent.setClass(msibook_certified_add.this, msibook_certified_add_double_check.class);
//                    //開啟Activity
//                    startActivityForResult(intent,1);
//                }

                return false;
            }
        });

    }

    //專案
    private void Find_Certification_Model_List(String WorkID) {

        //顯示  讀取等待時間Bar
        progressBar.show();

        ArrayModelID.clear();
        ArrayModel.clear();

        RequestQueue mQueue = Volley.newRequestQueue(this);

        Map<String,String> map = new HashMap<String, String>();
        map.put("WorkID", WorkID);

        if (mQueue == null) {
            mQueue = Volley.newRequestQueue(this);
        }
        HTTPSTrustManager.allowAllSSL();//信任所有证书，信任憑證
        String Path = GetServiceData.ServicePath + "/Find_Certification_Model_List";
        //String Path = "http://wtsc.msi.com.tw/IMS/MsiBook_App_Service.asmx/Insert_E_HR_Master";

        GetServiceData.SendPostRequest(Path, mQueue, new GetServiceData.VolleyStringCallback() {
            @Override
            public void onSendRequestSuccess(String result) {

                try {

                    JSONObject obj = new JSONObject(result);

                    JSONArray UserArray = new JSONArray(obj.getString("Key"));

                    for (int i = 0; i < UserArray.length(); i++) {

                        JSONObject CertificationData = UserArray.getJSONObject(i);

                        String ModelID = String.valueOf(CertificationData.getInt("ModelID"));  //專案代碼
                        String ModelName = String.valueOf(CertificationData.getString("ModelName")); //專案名稱
                        String ModelPic = String.valueOf(CertificationData.getString("ModelPic")); //專案圖片
                        String DateDiff = String.valueOf(CertificationData.getInt("DateDiff")); //
                        String Product_Line = String.valueOf(CertificationData.getString("Product_Line")); //掛的BU

                        ArrayModelID.add(i, ModelID);

                        ArrayModel.add(i, ModelName);
                    }

                    spinner_project = (Spinner) findViewById(R.id.spinner_project);

                    mDropDownModelAdapter = new DropDownModelAdapter(msibook_certified_add.this, ArrayModel,ArrayModelID);

                    spinner_project.setAdapter(mDropDownModelAdapter);

                    spinner_project.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {

                        public void onItemSelected(AdapterView adapterView, View view, int position, long id) {

                            DropDownModelAdapter DropDownModelAdapter =  (DropDownModelAdapter)adapterView.getAdapter();

                            DropDownModelAdapter.selectedItemdp = position;

                            mDropDownModelAdapter.notifyDataSetChanged();

                            SaveModel = ArrayModel.get(position);
                            SaveModelID = ArrayModelID.get(position);
                            Log.w("SaveModel",SaveModel +" "+SaveModelID);
                        }

                        public void onNothingSelected(AdapterView arg0) {

                        }
                    });

                }
                catch (JSONException ex) {
                    Log.w("Json",ex.toString());
                }
                progressBar.hide();
            }

            @Override
            public void onSendRequestError(String result) {
                Log.w("RequestError",result);
                progressBar.hide();
            }

        }, map);
    }

    //存放 類別
    private void Find_Cert_List() {

        //顯示  讀取等待時間Bar
        progressBar.show();

        ArrayCerID.clear();

        ArrayCerName.clear();

        RequestQueue mQueue = Volley.newRequestQueue(this);

        Map<String,String> map = new HashMap<String, String>();
        //map.put("WorkID", WorkID);

        if (mQueue == null) {
            mQueue = Volley.newRequestQueue(this);
        }
        HTTPSTrustManager.allowAllSSL();//信任所有证书，信任憑證
        String Path = GetServiceData.ServicePath + "/Find_Cert_List";
        //String Path = "http://wtsc.msi.com.tw/IMS/MsiBook_App_Service.asmx/Insert_E_HR_Master";

        GetServiceData.SendPostRequest(Path, mQueue, new GetServiceData.VolleyStringCallback() {
            @Override
            public void onSendRequestSuccess(String result) {

                try {

                    JSONObject obj = new JSONObject(result);

                    JSONArray UserArray = new JSONArray(obj.getString("Key"));

                    for (int i = 0; i < UserArray.length(); i++) {

                        JSONObject CertificationData = UserArray.getJSONObject(i);

                        String F_SeqNo = String.valueOf(CertificationData.getInt("F_SeqNo"));  //專案代碼
                        String CerName = String.valueOf(CertificationData.getString("CerName")); //專案名稱

                        ArrayCerID.add(i, F_SeqNo);

                        ArrayCerName.add(i, CerName);

                    }

                    spinner_class = (Spinner) findViewById(R.id.spinner_class);

                    mDropDownCerAdapter = new DropDownCerAdapter(msibook_certified_add.this, ArrayCerName,ArrayCerID);

                    spinner_class.setAdapter(mDropDownCerAdapter);

                    spinner_class.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {

                        public void onItemSelected(AdapterView adapterView, View view, int position, long id) {

                            DropDownCerAdapter DropDownCerAdapter =  (DropDownCerAdapter)adapterView.getAdapter();

                            DropDownCerAdapter.selectedItemdp = position;

                            mDropDownCerAdapter.notifyDataSetChanged();

                            SaveCerName = ArrayCerName.get(position);
                            SaveCerID = ArrayCerID.get(position);
                            Log.w("SaveCer",SaveCerName +" "+SaveCerID);
                        }

                        public void onNothingSelected(AdapterView arg0) {

                        }
                    });



                }
                catch (JSONException ex) {
                    Log.w("Json",ex.toString());
                }
                progressBar.dismiss();
            }

            @Override
            public void onSendRequestError(String result) {
                Log.w("RequestError",result);
                progressBar.dismiss();

            }

        }, map);
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
                style.setBackgroundColor(Color.parseColor("#ffffff"));//白色
            }
            return v;
        }

    }


    public class DropDownCerAdapter extends BaseAdapter {

        private LayoutInflater mLayInf;

        private List<String> ArrayCerName;

        private List<String> ArrayCerID;

        private Context ProjectContext;

        public int selectedItemdp;

        public DropDownCerAdapter(Context context,  List<String> ArrayCerName,List<String> ArrayCerID)
        {
            mLayInf = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            ProjectContext = context;

            this.ArrayCerName = ArrayCerName;

            this.ArrayCerID = ArrayCerID;
        }
        @Override
        public int getCount() {
            return ArrayCerName.size();
        }

        @Override
        public Object getItem(int position) {
            return ArrayCerName.get(position);
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

            style.setText(ArrayCerName.get(position));

            Log.w("test","test");

            return v;
        }
        @Override
        public View getDropDownView(int position,View convertView,ViewGroup parent)
        {
            View v = null;
            v = mLayInf.inflate(R.layout.facility_booking_spinnertextlayout, parent, false);

            TextView style = (TextView) v.findViewById(R.id.style);

            style.setText(ArrayCerName.get(position));

            // If this is the selected item position
            if (position == selectedItemdp) {
                style.setBackgroundColor(Color.parseColor("#e2e2e2"));//灰色
            }
            else {
                // for other views
                style.setBackgroundColor(Color.parseColor("#ffffff"));//白色
            }
            return v;
        }

    }

}
