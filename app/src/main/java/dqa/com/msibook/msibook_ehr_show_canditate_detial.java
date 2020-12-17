package dqa.com.msibook;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class msibook_ehr_show_canditate_detial extends AppCompatActivity {

    private Context mContext;

    private String getDept;
    private String getF_Keyin;
    private String getName;
    private String getF_Introduce;

    private TextView textView_dept;
    private TextView textView_keyin_name;
    private TextView textView_introduce;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msibook_ehr_show_canditate_detial);

        mContext = msibook_ehr_show_canditate_detial.this;

        getDept = getIntent().getStringExtra("Dept");//
        getF_Keyin = getIntent().getStringExtra("F_Keyin");//
        getName = getIntent().getStringExtra("Name");//
        getF_Introduce = getIntent().getStringExtra("F_Introduce");//

        textView_dept = (TextView) findViewById(R.id.textView_dept);
        textView_keyin_name = (TextView) findViewById(R.id.textView_keyin_name);
        textView_introduce = (TextView) findViewById(R.id.textView_introduce);

        textView_dept.setText(getDept);
        textView_keyin_name.setText(getF_Keyin+" - "+getName);
        textView_introduce.setText(getF_Introduce);

    }
}
