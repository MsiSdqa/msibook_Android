package dqa.com.msibook;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class overtime_pop_activity extends AppCompatActivity {

    private ProgressDialog pDialog;

    private ConstraintLayout popconlayout;

    private LinearLayout pop_linlayout_main;

    private String getWorkID;

    private String Set_F_TotalHour;// 時數

    private String Set_F_Model;// 專案

    private String Set_F_Type;// 補休 加班費

    private String Set_F_IsApply;// 0=不申請 1=申請

    private String Set_F_OverTime_Reasons;// 原因

    private String Set_F_SeqNo;// DB 序號


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overtime_pop_activity);

        pDialog = new ProgressDialog(this);

        //宣告元件
        TextView textView_when = (TextView) findViewById(R.id.textView_when); //Title日期

        final TextView editText_project = (TextView) findViewById(R.id.editText_project);//專案

        final TextView editText_reason = (TextView) findViewById(R.id.editText_reason);//原因

        final TextView textView_apply_hour = (TextView) findViewById(R.id.textView_apply_hour);//時數

        Button button_close = (Button) findViewById(R.id.button_close);//關閉視窗

        Button button_cuthour = (Button) findViewById(R.id.button_cuthour);//關閉視窗

        Button button_addhour = (Button) findViewById(R.id.button_addhour);//關閉視窗

        final Button btn_rest = (Button) findViewById(R.id.btn_rest);// 補休鈕

        final Button button_money = (Button) findViewById(R.id.button_money);//加班費鈕

        final Button button_noapply = (Button) findViewById(R.id.button_noapply);//放棄鈕

        final LinearLayout linearlayout_sent = (LinearLayout) findViewById(R.id.linearlayout_sent);//送出鈕linearlayout_sent

        final TextView textView_sent = (TextView) findViewById(R.id.textView_sent);//送出 文字
//**********抓前頁資料
        String getWork_ID = getIntent().getStringExtra("Work_ID");//年月日
        getWorkID = getWork_ID;

        String getaStr_F_Date = getIntent().getStringExtra("F_Date1");//年月日

        String getF_STime = getIntent().getStringExtra("F_STime");//上班

        String getF_ETime = getIntent().getStringExtra("F_ETime");//下班

        textView_when.setText(getaStr_F_Date +"　"+ getF_STime +"-"+getF_ETime);// 年月日期  加班開始 結束

        String getF_Model = getIntent().getStringExtra("F_Model");//專案
        editText_project.setText(getF_Model);

        String getF_OverTime_Reasons = getIntent().getStringExtra("F_OverTime_Reasons");//原因
        editText_reason.setText(getF_OverTime_Reasons);

        String getF_BaseTotalHour = getIntent().getStringExtra("F_BaseTotalHour");//基底
        final Double Basehour = Double.parseDouble(getF_BaseTotalHour);
        Log.w("Basehour",String.valueOf(Basehour));

        final String getF_TotalHour = getIntent().getStringExtra("F_TotalHour");//申請
        final Double[] TotalHour = {Double.parseDouble(getF_TotalHour)};
        final Double[] answer = {TotalHour[0]};
        Log.w("Basehour",String.valueOf(TotalHour[0]));
        textView_apply_hour.setText(getF_TotalHour);

        String getF_Type = getIntent().getStringExtra("F_Type");// 抓給付方式
        Set_F_Type = getF_Type;
        Log.w("getF_Type",getF_Type);

        String getF_SeqNo = getIntent().getStringExtra("F_SeqNo");// 抓序號
        Set_F_SeqNo = getF_SeqNo;
        Log.w("getF_SeqNo",getF_SeqNo);

        String getF_Apply = getIntent().getStringExtra("F_Apply");// 抓給付方式
        Log.w("getF_Web_Stat",String.valueOf(getF_Apply));

        String getF_App_Stat = getIntent().getStringExtra("F_App_Stat");// 抓給付方式
        Log.w("getF_App_Stat",getF_App_Stat);

        String getF_Web_Stat = getIntent().getStringExtra("F_Web_Stat");// 抓給付方式
        Log.w("getF_Web_Stat",String.valueOf(getF_Web_Stat));

        if(getF_Web_Stat.equals("true") || getF_App_Stat.equals("true")){//Web & App是否編輯過
            linearlayout_sent.setVisibility(View.INVISIBLE);
            textView_sent.setVisibility(View.INVISIBLE);
        }

        Set_F_IsApply = "1"; // 預設 為 會申請

        popconlayout = (ConstraintLayout) findViewById(R.id.popconlayout);
        popconlayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //點選 Layout任一方 將鍵盤收起來
                InputMethodManager imm = (InputMethodManager)v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(editText_project.getWindowToken(),0);
                imm.hideSoftInputFromWindow(editText_reason.getWindowToken(),0);
                return false;
            }
        });


        pop_linlayout_main = (LinearLayout) findViewById(R.id.pop_linlayout_main);
        pop_linlayout_main.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //點選 Layout任一方 將鍵盤收起來
                InputMethodManager imm = (InputMethodManager)v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(editText_project.getWindowToken(),0);
                imm.hideSoftInputFromWindow(editText_reason.getWindowToken(),0);
                return false;
            }
        });



        //關閉 回前頁面
        button_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent();
//                intent.putExtra("login_WorkID",getWorkID);
//                intent.setClass(Main_Pop_Activity.this, MainActivity.class);
//                //開啟Activity
//                startActivity(intent);
                finish();
            }
        });

        //時數減少
        button_cuthour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Double test;
                if(TotalHour[0] <= 1){
                    //Do nothing
                }else{
                    test = TotalHour[0] -0.5;
                    textView_apply_hour.setText(String.valueOf(test));
                    TotalHour[0] =test;
                }

            }
        });
        //時數增加
        button_addhour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Double test;
                if(TotalHour[0] >= Basehour){
                    //Do nothing
                }else{
                    test = TotalHour[0] + 0.5;
                    textView_apply_hour.setText(String.valueOf(test));
                    TotalHour[0] =test;
                }
            }
        });

        //判斷是否不申請
        if(getF_Apply.equals("false")){
            button_noapply.setBackgroundResource(R.mipmap.overtime_btn_apply_noapply_sel);
        }else {
            //判斷 補休 OR 加班費 OR 放棄
            switch (getF_Type) {
                case "補休":
                    Log.w("抓到", "補休");
                    btn_rest.setBackgroundResource(R.mipmap.overtime_btn_apply_rest_sel);
                    break;
                case "加班費":
                    Log.w("抓到", "加班費");
                    button_money.setBackgroundResource(R.mipmap.overtime_btn_apply_money_sel);
                    break;
                case "放棄":
                    Log.w("抓到", "放棄");
                    button_noapply.setBackgroundResource(R.mipmap.overtime_btn_apply_noapply_sel);
                    break;
            }
        }

        //補休點選
        btn_rest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_rest.setBackgroundResource(R.mipmap.overtime_btn_apply_rest_sel);
                button_money.setBackgroundResource(R.mipmap.overtime_btn_apply_money_nor);
                button_noapply.setBackgroundResource(R.mipmap.overtime_btn_apply_noapply_nor);
                Set_F_Type = "補休";
                Set_F_IsApply = "1";
            }
        });
        //加班費點選
        button_money.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_rest.setBackgroundResource(R.mipmap.overtime_btn_apply_rest_nor);
                button_money.setBackgroundResource(R.mipmap.overtime_btn_apply_money_sel);
                button_noapply.setBackgroundResource(R.mipmap.overtime_btn_apply_noapply_nor);
                Set_F_Type = "加班費";
                Set_F_IsApply = "1";
            }
        });
        //放棄點選
        button_noapply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_rest.setBackgroundResource(R.mipmap.overtime_btn_apply_rest_nor);
                button_money.setBackgroundResource(R.mipmap.overtime_btn_apply_money_nor);
                button_noapply.setBackgroundResource(R.mipmap.overtime_btn_apply_noapply_sel);
                Set_F_Type = "補休";
                Set_F_IsApply = "0";
            }
        });


        //送出Layout觸發
        linearlayout_sent.setOnTouchListener(new Button.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {  //按下的時候改變背景及顏色
                    linearlayout_sent.setBackgroundResource(R.drawable.popup_enter_down);
                    textView_sent.setTextColor(Color.BLACK);
                }
                if (event.getAction() == MotionEvent.ACTION_UP) {  //起來的時候恢復背景與顏色
                    linearlayout_sent.setBackgroundResource(R.drawable.popup_enter_up);
                    textView_sent.setTextColor(Color.WHITE);
                }
                return false;
            }
        });

        //按下事件
        linearlayout_sent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (getChineseLength(editText_reason.getText().toString()) > 90)
                {
                    popupoverword();
                }else{
                    popupchecksent();

                    Set_F_TotalHour = String.valueOf(textView_apply_hour.getText());
                    Log.w("設定的 申請時數",Set_F_TotalHour);

                    Set_F_Model = String.valueOf(editText_project.getText());
                    Log.w("設定的 申請專案",Set_F_Model);

                    // 補休 加班費
                    Log.w("設定的 申請補休 or 加班費",Set_F_Type);

                    // 0=不申請 1=申請
                    Log.w("設定的 是否申請",Set_F_IsApply);

                    Set_F_OverTime_Reasons = String.valueOf(editText_reason.getText());// 原因
                    Log.w("設定的 申請原因",Set_F_OverTime_Reasons);

                    Log.w("設定的 申請序號",Set_F_SeqNo);

                }

            }
        });


    }

    public static int  getChineseLength(String validateStr) {
        int  valueLength =  0 ;
        String chinese =  "[\u0391-\uFFE5]" ;
        /* 獲取字段值的長度，如果含中文字符，則每個中文字符長度為2，否則為1 */
        for  ( int  i =  0 ; i < validateStr.length(); i++) {
            /* 獲取一個字符 */
            String temp = validateStr.substring(i, i +  1 );
            /* 判斷是否為中文字符 */
            if  (temp.matches(chinese)) {
                /* 中文字符長度為2 */
                valueLength +=  3 ;
            }  else  {
                /* 其他字符長度為1 */
                valueLength +=  1 ;
            }
        }
        return  valueLength;
    }

    private void popupoverword(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("注意!!").setMessage("加班原因字元已超過限制!!").setPositiveButton("我知道了", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        }).show();
    }

    private void popupchecksent(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("注意!!")
                .setMessage("確認送出即無法修改!!"+"\n")
                .setPositiveButton("確認送出", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Update_OverTime(Set_F_TotalHour, Set_F_Model,Set_F_Type,Set_F_IsApply,Set_F_OverTime_Reasons,Set_F_SeqNo);

                        overtime_pop_activity.this.finish();
                        dialog.cancel();

                        Toast.makeText(overtime_pop_activity.this, "資料已更新", Toast.LENGTH_SHORT).show();
//
//                Intent intent = new Intent();
//                intent.putExtra("login_WorkID",getWorkID);
//                intent.setClass(Main_Pop_Activity.this, MainActivity.class);
//                //開啟Activity
//                startActivity(intent);

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

    private void Update_OverTime(String F_TotalHour,String F_Model,String F_Type,String F_IsApply,String F_OverTime_Reasons,String F_SeqNo){
        RequestQueue mQueue = Volley.newRequestQueue(this);

        Map<String, String> map = new HashMap<String, String>();
        map.put("F_TotalHour",F_TotalHour);
        map.put("F_Model", F_Model);
        map.put("F_Type", F_Type);
        map.put("F_IsApply", F_IsApply);
        map.put("F_OverTime_Reasons", F_OverTime_Reasons);
        map.put("F_SeqNo", F_SeqNo);

        HTTPSTrustManager.allowAllSSL();//信任所有证书，信任憑證
        String Path = GetServiceData.ServicePath_overtime + "/Update_OverTime";

        GetServiceData.SendPostRequest(Path, mQueue, new GetServiceData.VolleyStringCallback() {
            @Override
            public void onSendRequestSuccess(String result) {

            }

            @Override
            public void onSendRequestError(String response) {

            }

        }, map);
    }

}
