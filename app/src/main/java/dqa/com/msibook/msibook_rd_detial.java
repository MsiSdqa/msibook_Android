package dqa.com.msibook;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class msibook_rd_detial extends AppCompatActivity {

    private TextView txt_HR;
    private TextView txt_Model;
    private TextView txt_Subject;
    private TextView txt_Type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msibook_rd_detial);

        String getModel = getIntent().getStringExtra("Model");//
        String getSubjec = getIntent().getStringExtra("Subjec");//
        String getType = getIntent().getStringExtra("Type");//
        String getHR = getIntent().getStringExtra("HR");//

        txt_Model = (TextView) findViewById(R.id.txt_Model);
        txt_HR = (TextView) findViewById(R.id.txt_HR);
        txt_Type = (TextView) findViewById(R.id.txt_Type);
        txt_Subject = (TextView) findViewById(R.id.txt_Subject);

        txt_Model.setText(getModel);
        txt_HR.setText(getHR);
        txt_Type.setText(getType);
        txt_Subject.setText(getSubjec);

    }
}
