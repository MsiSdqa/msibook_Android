package dqa.com.msibook;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class msibook_cec_cer_info extends AppCompatActivity {

    private TextView textView_title;
    private TextView textView_subject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msibook_cec_cer_info);

        textView_title = (TextView) findViewById(R.id.textView_title);
        textView_subject = (TextView) findViewById(R.id.textView_subject);

        String get_title = getIntent().getStringExtra("title");
        String get_subject = getIntent().getStringExtra("subject");

        textView_title.setText(get_title);
        textView_subject.setText(get_subject);

    }
}
