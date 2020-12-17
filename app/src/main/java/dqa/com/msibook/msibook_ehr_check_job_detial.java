package dqa.com.msibook;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class msibook_ehr_check_job_detial extends AppCompatActivity {

    private TextView textView_Job_Name;
    private TextView textView_Job_Content;
    private TextView textView_Job_People;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msibook_ehr_check_job_detial);

        textView_Job_Name = (TextView) findViewById(R.id.textView_Job_Name);//
        textView_Job_Content = (TextView) findViewById(R.id.textView_Job_Content);//
        textView_Job_People = (TextView) findViewById(R.id.textView_Job_People);//

        String getJob_Name = getIntent().getStringExtra("F_Job_Name");//
        String getJob_Content = getIntent().getStringExtra("F_Job_Content");//
        String getJob_People = getIntent().getStringExtra("F_Job_People");//

        textView_Job_Name.setText(getJob_Name);
        textView_Job_Content.setText(getJob_Content);
        textView_Job_People.setText(getJob_People);

    }
}
