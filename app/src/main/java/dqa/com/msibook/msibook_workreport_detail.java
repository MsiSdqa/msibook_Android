package dqa.com.msibook;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class msibook_workreport_detail extends AppCompatActivity {

    private TextView textView_ModelName;
    private TextView textView_F_Subject;
    private TextView textView_F_CreateDate;
    private TextView textView_F_WorkHour;
    private TextView textView_F_CostSum;

    private String Get_ModelName;
    private String Get_F_Subject;
    private String Get_F_CreateDate;
    private String Get_F_WorkHour;
    private String Get_F_CostSum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msibook_workreport_detail);

        textView_ModelName = (TextView) findViewById(R.id.textView_ModelName);
        textView_F_Subject = (TextView) findViewById(R.id.textView_F_Subject);
        textView_F_CreateDate = (TextView) findViewById(R.id.textView_F_CreateDate);
        textView_F_WorkHour = (TextView) findViewById(R.id.textView_F_WorkHour);
        textView_F_CostSum = (TextView) findViewById(R.id.textView_F_CostSum);


        Get_ModelName = getIntent().getStringExtra("ModelName");//
        Get_F_Subject = getIntent().getStringExtra("F_Subject");//
        Get_F_CreateDate = getIntent().getStringExtra("F_CreateDate");//
        Get_F_WorkHour = getIntent().getStringExtra("F_WorkHour");//
        Get_F_CostSum = getIntent().getStringExtra("F_CostSum");//

        textView_ModelName.setText(Get_ModelName);
        textView_F_Subject.setText(Get_F_Subject);
        textView_F_CreateDate.setText(Get_F_CreateDate);
        textView_F_WorkHour.setText(Get_F_WorkHour);
        textView_F_CostSum.setText("NTD. "+Get_F_CostSum);

    }
}
