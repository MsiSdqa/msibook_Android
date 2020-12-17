package dqa.com.msibook;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static dqa.com.msibook.GetServiceData.SendPostRequest;

public class msibook_ehr_new_job_add extends AppCompatActivity {

    private ProgressDialog progressBar;
    private ProgressDialog progressBarDB;

    SQLiteDatabase db;//資料庫物件
    static final String db_name = "My_eHR_db";//資料庫名稱
    static final String tb_name_Find_Dept_List_Recursive = "Find_Dept_List_Recursive_data";// 總部門資料 - 資料表名稱

    private Button btn_refresh_db;

    private List<Search_Dpt_Item> Search_Dpt_Item_List = new ArrayList<Search_Dpt_Item>();

    private List<Search_Dpt_Item> Search_Filter_Dpt_Item_List;

    private Search_Dpt_Adapter mSearch_adapter;

    private ListView mListView;

    private String WorkID;//工號

    private Integer nowweek;

    private ScrollView scrollview1;
    private RelativeLayout relativelayout_main;
    private LinearLayout linear_add_main;
    private LinearLayout linear_need_dpt;

    private EditText editText_dpt_Name;     //需求部門
    private EditText editText_Job_Name;     //職稱
    private EditText editText_Job_Content;   //內容
    private EditText editText_Job_InterView;    //面試人員

    private String Job_gender;          ///性別 自訂變數
    private RadioButton radioButton_Job_gender0;          ///性別Btn
    private RadioButton radioButton_Job_gender1;          ///性別Btn
    private RadioButton radioButton_Job_gender2;          ///性別Btn

    private Integer Job_Level_Base;      //職等 下  自訂變數
    private Integer Job_Level_Top;       //職等 上  自訂變數
    private EditText editText_Job_Level_Base;   //職等 下   輸入字串
    private EditText editText_Job_Level_Top;    //職等 上   輸入字串

    private Integer Job_Age_Base;        //年齡 下   自訂變數
    private Integer Job_Age_Top;         //年齡 上   自訂變數
    private EditText editText_Job_Age_Base;     //職等 下   輸入字串
    private EditText editText_Job_Age_Top;      //職等 上   輸入字串

    private EditText editText_Job_People;   //人數需求

    private TextView textView_sent;//送出
    private TextView textview_goto_bottom;//置底

    private Context mContext;

    private String Set_F_DeptCode;
    private String Set_Dept;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msibook_ehr_new_job_add);
        mContext = msibook_ehr_new_job_add.this;

        progressBar = new ProgressDialog(this);
        progressBar.setCancelable(true);
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.setMessage("資料載入中");

        progressBarDB = new ProgressDialog(this);
        progressBarDB.setCancelable(true);
        progressBarDB.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBarDB.setMessage("部門清單更新中，請稍等");

        WorkID = String.valueOf(UserData.WorkID);
        Log.w("WrokID",WorkID);

        scrollview1 = (ScrollView)findViewById(R.id.scrollview1);
        relativelayout_main = (RelativeLayout) findViewById(R.id.relativelayout_main);
        linear_need_dpt = (LinearLayout) findViewById(R.id.linear_need_dpt);
        linear_add_main = (LinearLayout) findViewById(R.id.linear_add_main);

        editText_dpt_Name = (EditText) findViewById(R.id.editText_dpt_Name);//setOnQueryTextListener(this);
        editText_dpt_Name.addTextChangedListener(textWatcher);
        editText_dpt_Name.clearFocus();//取消焦點
        mListView = (ListView)findViewById(R.id.search_listview);

        btn_refresh_db= (Button) findViewById(R.id.btn_refresh_db);

        editText_Job_Name = (EditText) findViewById(R.id.editText_Job_Name);
        editText_Job_Content = (EditText) findViewById(R.id.editText_Job_Content);
        editText_Job_InterView = (EditText) findViewById(R.id.editText_Job_InterView);

        radioButton_Job_gender0 = (RadioButton) findViewById(R.id.radioButton_Job_gender0);
        radioButton_Job_gender1 = (RadioButton) findViewById(R.id.radioButton_Job_gender1);
        radioButton_Job_gender2 = (RadioButton) findViewById(R.id.radioButton_Job_gender2);

        editText_Job_Level_Base = (EditText) findViewById(R.id.editText_Job_Level_Base);
        Job_Level_Base = 0;
        editText_Job_Level_Top = (EditText) findViewById(R.id.editText_Job_Level_Top);
        Job_Level_Top = 0;

        editText_Job_Age_Base = (EditText) findViewById(R.id.editText_Job_Age_Base);
        Job_Age_Base = 0;
        editText_Job_Age_Top = (EditText) findViewById(R.id.editText_Job_Age_Top);
        Job_Age_Top = 0;

        editText_Job_People = (EditText) findViewById(R.id.editText_Job_People);

        textView_sent = (TextView) findViewById(R.id.textView_sent);
        textview_goto_bottom = (TextView) findViewById(R.id.textview_goto_bottom);

        radioButton_Job_gender0.setChecked(true); //預設性別 == 不拘

        Calendar w = Calendar.getInstance();
        nowweek = w.get(Calendar.WEEK_OF_YEAR);

        linear_need_dpt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText_dpt_Name.clearFocus();
                InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(msibook_ehr_new_job_add.this.getCurrentFocus().getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
            }
        });

        btn_refresh_db.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                popup_update_Dpt_DB();

            }
        });

        relativelayout_main.setOnTouchListener(new View.OnTouchListener() {  //Relative lay out  點選
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //點選 Layout任一方 將鍵盤收起來
                InputMethodManager imm = (InputMethodManager)v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(editText_dpt_Name.getWindowToken(),0);
                imm.hideSoftInputFromWindow(editText_Job_Name.getWindowToken(),0);
                imm.hideSoftInputFromWindow(editText_Job_Content.getWindowToken(),0);
                imm.hideSoftInputFromWindow(editText_Job_InterView.getWindowToken(),0);

                imm.hideSoftInputFromWindow(editText_Job_Level_Base.getWindowToken(),0);
                imm.hideSoftInputFromWindow(editText_Job_Level_Top.getWindowToken(),0);
                imm.hideSoftInputFromWindow(editText_Job_Age_Base.getWindowToken(),0);
                imm.hideSoftInputFromWindow(editText_Job_Age_Top.getWindowToken(),0);

                imm.hideSoftInputFromWindow(editText_Job_People.getWindowToken(),0);
                return false;
            }
        });

        linear_add_main.setOnTouchListener(new View.OnTouchListener() {  //Linear 點選
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //點選 Layout任一方 將鍵盤收起來
                InputMethodManager imm = (InputMethodManager)v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(editText_dpt_Name.getWindowToken(),0);
                imm.hideSoftInputFromWindow(editText_Job_Name.getWindowToken(),0);
                imm.hideSoftInputFromWindow(editText_Job_Content.getWindowToken(),0);
                imm.hideSoftInputFromWindow(editText_Job_InterView.getWindowToken(),0);

                imm.hideSoftInputFromWindow(editText_Job_Level_Base.getWindowToken(),0);
                imm.hideSoftInputFromWindow(editText_Job_Level_Top.getWindowToken(),0);
                imm.hideSoftInputFromWindow(editText_Job_Age_Base.getWindowToken(),0);
                imm.hideSoftInputFromWindow(editText_Job_Age_Top.getWindowToken(),0);

                imm.hideSoftInputFromWindow(editText_Job_People.getWindowToken(),0);
                return false;
            }
        });

        relativelayout_main.setOnTouchListener(new View.OnTouchListener() {  //Linear  點選
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //點選 Layout任一方 將鍵盤收起來
                InputMethodManager imm = (InputMethodManager)v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(editText_dpt_Name.getWindowToken(),0);
                imm.hideSoftInputFromWindow(editText_Job_Name.getWindowToken(),0);
                imm.hideSoftInputFromWindow(editText_Job_Content.getWindowToken(),0);
                imm.hideSoftInputFromWindow(editText_Job_InterView.getWindowToken(),0);

                imm.hideSoftInputFromWindow(editText_Job_Level_Base.getWindowToken(),0);
                imm.hideSoftInputFromWindow(editText_Job_Level_Top.getWindowToken(),0);
                imm.hideSoftInputFromWindow(editText_Job_Age_Base.getWindowToken(),0);
                imm.hideSoftInputFromWindow(editText_Job_Age_Top.getWindowToken(),0);

                imm.hideSoftInputFromWindow(editText_Job_People.getWindowToken(),0);
                return false;
            }
        });

        //監聽ScrollView
        scrollview1.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                if (scrollview1.getScrollY() + scrollview1.getHeight() - scrollview1.getPaddingTop() -
                        scrollview1.getPaddingBottom() == scrollview1.getChildAt(0).getHeight()) {
                    System.out.println("底部");
                    textview_goto_bottom.setVisibility(View.INVISIBLE);
                }else{
                    textview_goto_bottom.setVisibility(View.VISIBLE);
                }
            }
        });

        textview_goto_bottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scrollview1.scrollTo(0, linear_add_main.getMeasuredHeight() - scrollview1.getHeight());
            }
        });

        CheckRadioButton();
        //*************************************性別*************************************
        radioButton_Job_gender0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckRadioButton();
            }
        });

        radioButton_Job_gender1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckRadioButton();
            }
        });

        radioButton_Job_gender2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckRadioButton();
            }
        });

        //*************************************職等*************************************
        editText_Job_Level_Base.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if(s.length() == 0) {
                    Job_Level_Base = 0;
                }else{
                    Job_Level_Base = Integer.valueOf(String.valueOf(editText_Job_Level_Base.getText()));
                }
                Log.w("Job_Level_Base", String.valueOf(Job_Level_Base));
            }
        });

        editText_Job_Level_Top.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if(s.length() == 0) {
                    Job_Level_Top = 0;
                }else{
                    Job_Level_Top = Integer.valueOf(String.valueOf(editText_Job_Level_Top.getText()));
                }
                Log.w("Job_Level_Top", String.valueOf(Job_Level_Top));
            }
        });

        //*************************************年齡*************************************
        editText_Job_Age_Base.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if(s.length() == 0) {
                    Job_Age_Base = 0;
                }else{
                    Job_Age_Base = Integer.valueOf(String.valueOf(editText_Job_Age_Base.getText()));
                }
                Log.w("Job_Age_Base", String.valueOf(Job_Age_Base));
            }
        });

        editText_Job_Age_Top.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if(s.length() == 0) {
                    Job_Age_Top = 0;
                }else{
                    Job_Age_Top = Integer.valueOf(String.valueOf(editText_Job_Age_Top.getText()));
                }
                Log.w("Job_Age_Top", String.valueOf(Job_Age_Top));
            }
        });


        //
        textView_sent.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                        switch (event.getAction()) {
                            case MotionEvent.ACTION_DOWN:
                                textView_sent.setBackground(getResources().getDrawable(R.drawable.ehr_btn_background_down));
                                return true;
                            case MotionEvent.ACTION_UP:
                                textView_sent.setBackground(getResources().getDrawable(R.drawable.ehr_btn_background_up));

                                if (editText_dpt_Name.getText().toString().equals("") || editText_Job_Name.getText().toString().equals("") || editText_Job_Content.getText().toString().equals("") || editText_Job_People.getText().toString().equals("")){//判斷帳號是否為空值
                                    if(editText_dpt_Name.getText().toString().equals("")) {
                                        editText_dpt_Name.setError("部門不得為空");
                                    }
                                    if(editText_Job_Name.getText().toString().equals("")){
                                        editText_Job_Name.setError("職稱不得為空");
                                    }
                                    if(editText_Job_Content.getText().toString().equals("")){
                                        editText_Job_Content.setError("工作內容不得為空");
                                    }
                                    if(editText_Job_People.getText().toString().equals("")){
                                        editText_Job_People.setError("需求人數不得為空");
                                    }
                                }else{
                                    popupchecksent_Insert_E_HR();
                                }


                                return true;
                        }
                return false;
            }
        });

        editText_dpt_Name.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){//获得焦点
                    mListView.setVisibility(View.VISIBLE);
                    textView_sent.setVisibility(View.INVISIBLE);
                }else{//失去焦点
                    mListView.setVisibility(View.GONE);
                    textView_sent.setVisibility(View.VISIBLE);
                }
            }
        });

        Local_DB_Search_Find_Dept_List();

    }

    private void popup_update_Dpt_DB(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("更新部門清單資料庫，可能需要一些時間。"+"\n")
                .setPositiveButton("更新", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        update_Dpt_List();

                    }
                })
                .setNeutralButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .show();
    }

    private void update_Dpt_List(){
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

                    Toast.makeText(msibook_ehr_new_job_add.this, "部門清單已更新", Toast.LENGTH_SHORT).show();

                    Local_DB_Search_Find_Dept_List();

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

    //搜尋 該單位已下部門
    private void Local_DB_Search_Find_Dept_List() {

        //顯示  讀取等待時間Bar
        progressBar.show();

        Search_Dpt_Item_List.clear();

        //開啟或建立資料庫
        db = openOrCreateDatabase(db_name,Context.MODE_PRIVATE,null);
        Cursor c = db.rawQuery("SELECT  id, DeptID, F_DeptCode, Dept FROM "+ tb_name_Find_Dept_List_Recursive +"",null);//查詢 掌管部門已下資訊
        c.moveToFirst();
        String str = "測試查詢完總共有" + c.getCount() + "筆資料\n";
        if (c.getCount() > 0) {//判斷有無查詢資料
            str += "------\n";
            do {
//                str += "id:" + c.getString(0) + "\n";
//                str += "DeptID:" + c.getString(1) + "\n";
//                str += "F_DeptCode:" + c.getString(2) + "\n";
//                str += "Dept:" + c.getString(3) + "\n";
//                str += "----------------------------------------" + "\n";

                Search_Dpt_Item_List.add(new Search_Dpt_Item(c.getString(1),c.getString(2),c.getString(3)));

            } while (c.moveToNext());
//            Log.w("data:", str);
        }

        mSearch_adapter = new Search_Dpt_Adapter(mContext,Search_Dpt_Item_List);

        mListView.setAdapter(mSearch_adapter);

        //關閉-讀取等待時間Bar
        progressBar.dismiss();

        //點選列表 轉回 首頁MainActivity MyHRM
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Search_Dpt_Adapter Adapter = (Search_Dpt_Adapter) parent.getAdapter();

                Search_Dpt_Item Dpt_Item = (Search_Dpt_Item) Adapter.getItem(position);

                editText_dpt_Name.setText(Dpt_Item.F_DeptCode +" "+ Dpt_Item.Dept);
                Set_F_DeptCode = Dpt_Item.F_DeptCode;
                Set_Dept = Dpt_Item.Dept;

                editText_Job_Name.requestFocus();//获取焦点 光标出现

                mListView.setVisibility(View.GONE);
                scrollview1.scrollTo(0, 0);// 改变滚动条的位置
            }
        });

    }

    //當文字改變 監聽
    private TextWatcher textWatcher = new TextWatcher() {

        @Override
        public void afterTextChanged(Editable s) {
            // TODO Auto-generated method stub

            Log.d("TAG","afterTextChanged--------------->");

        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
            // TODO Auto-generated method stub
            Log.d("TAG","beforeTextChanged--------------->");
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {
            Log.d("TAG","onTextChanged--------------->");
            Search_Filter_Dpt_Item_List = new ArrayList<Search_Dpt_Item>();

            for (Search_Dpt_Item a:Search_Dpt_Item_List) {

                if (a.F_DeptCode.contains(s) || a.Dept.contains(s))
                    Search_Filter_Dpt_Item_List.add(a);
            }
            mSearch_adapter = new Search_Dpt_Adapter(mContext,Search_Filter_Dpt_Item_List);

            mListView.setAdapter(mSearch_adapter);

            mListView.deferNotifyDataSetChanged();
        }
    };

    private void CheckRadioButton() {
        //依選取項目顯示不同訊息  //確認性別
        RadioGroup rg = (RadioGroup)findViewById(R.id.radioGroup1);

        switch (rg.getCheckedRadioButtonId()) {
            case R.id.radioButton_Job_gender0:
                Job_gender = "0";
                Log.w("不拘", "不拘" + Job_gender);
                break;
            case R.id.radioButton_Job_gender1:
                Job_gender="1";
                Log.w("男", "男" + Job_gender);
                break;
            case R.id.radioButton_Job_gender2:
                Job_gender="2";
                Log.w("女", "女" + Job_gender);
                break;
        }
    }

    private void popupchecksent_Insert_E_HR(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("確定新增職缺!?"+"\n")
                .setPositiveButton("新增", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Insert_E_HR_Master(String.valueOf(WorkID),
                                String.valueOf(Set_F_DeptCode),
                                String.valueOf(Set_Dept),
                                String.valueOf(editText_Job_Name.getText()),
                                String.valueOf(editText_Job_Content.getText()),
                                String.valueOf(editText_Job_InterView.getText()),
                                String.valueOf(Job_gender),
                                String.valueOf(Job_Level_Base),
                                String.valueOf(Job_Level_Top),
                                String.valueOf(Job_Age_Base),
                                String.valueOf(Job_Age_Top),
                                String.valueOf(editText_Job_People.getText()),
                                "1");

                        dialog.cancel();

                        Bundle bundle = new Bundle();
                        bundle.putString("Type", "1");
                        msibook_ehr_new_job_add.this.setResult(RESULT_OK,msibook_ehr_new_job_add.this.getIntent().putExtras(bundle));

                        finish();
                    }
                })
                .setNeutralButton("在檢查", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .show();
    }

    //新增
    private void Insert_E_HR_Master(String F_Keyin,
                                    String F_DeptCode,
                                    String F_DeptName,
                                    String F_Job_Name,
                                    String F_Job_Content,
                                    String F_Job_InterView,
                                    String F_Job_gender,
                                    String F_Job_Level_Base,
                                    String F_Job_Level_Top,
                                    String F_Job_Age_Base,
                                    String F_Job_Age_Top,
                                    String F_Job_People,
                                    String F_Type) {

        RequestQueue mQueue = Volley.newRequestQueue(this);

        Map<String,String> map = new HashMap<String, String>();
        map.put("F_Keyin", F_Keyin);
        map.put("F_DeptCode", F_DeptCode);
        map.put("F_DeptName", F_DeptName);
        map.put("F_Job_Name", F_Job_Name);
        map.put("F_Job_Content", F_Job_Content);
        map.put("F_Job_InterView", F_Job_InterView);
        map.put("F_Job_gender", F_Job_gender);
        map.put("F_Job_Level_Base", F_Job_Level_Base);
        map.put("F_Job_Level_Top", F_Job_Level_Top);
        map.put("F_Job_Age_Base", F_Job_Age_Base);
        map.put("F_Job_Age_Top", F_Job_Age_Top);
        map.put("F_Job_People", F_Job_People);
        map.put("F_Status", F_Type);

        Log.w("F_Keyin",F_Keyin);
        Log.w("F_DeptCode",F_DeptCode);
        Log.w("F_DeptName",F_DeptName);
        Log.w("F_Job_Name",F_Job_Name);
        Log.w("F_Job_Content",F_Job_Content);
        Log.w("F_Job_InterView",F_Job_InterView);
        Log.w("F_Job_gender",F_Job_gender);
        Log.w("F_Job_Level_Base",F_Job_Level_Base);
        Log.w("F_Job_Level_Top",F_Job_Level_Top);
        Log.w("F_Job_Age_Base",F_Job_Age_Base);
        Log.w("F_Job_Age_Top",F_Job_Age_Top);
        Log.w("F_Job_People",F_Job_People);
        Log.w("F_Status",F_Type);

        if (mQueue == null) {
            mQueue = Volley.newRequestQueue(this);
        }

        HTTPSTrustManager.allowAllSSL();//信任所有证书，信任憑證
        String Path = GetServiceData.ServicePath + "/Insert_E_HR_Master";
        //String Path = "http://wtsc.msi.com.tw/IMS/MsiBook_App_Service.asmx/Insert_E_HR_Master";

        GetServiceData.SendPostRequest(Path, mQueue, new GetServiceData.VolleyStringCallback() {
            @Override
            public void onSendRequestSuccess(String result) {

//                Integer SummaryNumber =  Integer.valueOf(main_summary_number.getText().toString());
//
//                main_summary_number.setText(String.valueOf((SummaryNumber +1)));

            }

            @Override
            public void onSendRequestError(String result) {



            }

        }, map);
    }

    //***********************************Item********************************
    public class Search_Dpt_Item {

        String F_ParentID_SeqNo;

        String DeptID;

        String F_DeptCode;

        String Dept;

        String BossID;

        String BossName;

        String F_Region;



        public Search_Dpt_Item(String DeptID,String F_DeptCode,String Dept)
        {
            this.DeptID = DeptID;

            this.F_DeptCode = F_DeptCode;

            this.Dept = Dept;

        }

        public String GetDeptID()
        {
            return this.DeptID;
        }

        public String GetF_DeptCode()
        {
            return this.F_DeptCode;
        }

        public String GetDept()
        {
            return this.Dept;
        }

    }

    //***********************************Adapter********************************
    public class Search_Dpt_Adapter extends BaseAdapter {

        private LayoutInflater mLayInf;

        private List<Search_Dpt_Item> Search_Dpt_Item_List;



        private Context AllDptContext;

        public Search_Dpt_Adapter(Context context, List<Search_Dpt_Item> Search_Dpt_Item_List)
        {
            mLayInf = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            AllDptContext = context;

            this.Search_Dpt_Item_List = Search_Dpt_Item_List;

        }



        @Override
        public int getCount() {
            return Search_Dpt_Item_List.size();
        }

        @Override
        public Object getItem(int position) {
            return Search_Dpt_Item_List.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View v = new View(AllDptContext);

            v = mLayInf.inflate(R.layout.msibook_ehr_search_list_dpt_items, parent, false);

//        LinearLayout adapter_dpt_linearlayout = (LinearLayout) v.findViewById(R.id.adapter_dpt_linearlayout);
            TextView name1 = (TextView) v.findViewById(R.id.nameLabel);
            TextView name2 = (TextView) v.findViewById(R.id.name);
//
            name1.setText(Search_Dpt_Item_List.get(position).GetF_DeptCode());
            name2.setText(Search_Dpt_Item_List.get(position).GetDept());

            return v;
        }

    }

}
