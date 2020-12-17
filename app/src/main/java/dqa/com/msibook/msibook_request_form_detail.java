package dqa.com.msibook;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class msibook_request_form_detail extends AppCompatActivity {

    private TextView textView_detail_title;//標題
    private TextView textView_style;// 需求累型
    private TextView textView_status; //狀態 ex處理中
    private TextView textView_need_man; //需求者
    private TextView textView_subject; //需求說明日
    private TextView textView_F_date; //指定完成

    private msibook_requset_form_item msibook_requset_form_item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msibook_request_form_detail);

        msibook_requset_form_item = (msibook_requset_form_item)getIntent().getSerializableExtra("CustomObject ");

        textView_detail_title = (TextView)findViewById(R.id.textView_detail_title);
        textView_style = (TextView)findViewById(R.id.textView_style);
        textView_status = (TextView)findViewById(R.id.textView_status);
        textView_need_man = (TextView)findViewById(R.id.textView_need_man);
        textView_subject = (TextView)findViewById(R.id.textView_subject);
        textView_F_date = (TextView)findViewById(R.id.textView_F_date);

        textView_detail_title.setText(msibook_requset_form_item.Model);
        if(msibook_requset_form_item.RequestType.contains("不指定")){
            textView_style.setTextColor(Color.parseColor("#828795"));
        }else{
            textView_style.setTextColor(Color.parseColor("#d21e25"));
        }
        textView_style.setText(msibook_requset_form_item.RequestType);
        textView_status.setText(msibook_requset_form_item.Status);
        textView_need_man.setText(msibook_requset_form_item.AssignUser);
        textView_subject.setText(msibook_requset_form_item.F_Memo);

        //Log.w("F_ExpectFixDate",String.valueOf(msibook_requset_form_item.F_ExpectFixDate));

        if ((msibook_requset_form_item.F_ExpectFixDate).isEmpty())
        {
            textView_F_date.setText("N/A");
        }
        else
        {

            //String dtStart = msibook_requset_form_item.F_ExpectFixDate.replace("T"," ");

            String dtStart = msibook_requset_form_item.F_ExpectFixDate;

            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss");
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 E");
                Date date = format.parse(dtStart);
                String dateString = sdf.format(date);
                textView_F_date.setText(dateString);

                Date NowDate = new Date();
                long diff = date.getTime() - NowDate.getTime();

            } catch (ParseException e) {
                Log.w("ErrorDate",e.toString());
            }
        }

//        textView_subject.setText(msibook_requset_form_item.F_Desc);
//
//        textView_need_man.setText(msibook_requset_form_item.RespUser);


    }
}
