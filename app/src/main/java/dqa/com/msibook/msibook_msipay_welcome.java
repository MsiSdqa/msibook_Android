package dqa.com.msibook;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

public class msibook_msipay_welcome extends AppCompatActivity {

    private LinearLayout linear_bottom;

    private Context mContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msibook_msipay_welcome);

        mContent = this;

        linear_bottom = (LinearLayout) findViewById(R.id.linear_bottom);

        linear_bottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent dqaweekly = new Intent(mContent,msibook_msipay_register1_phone_insert.class);

                mContent.startActivity(dqaweekly);

            }
        });
    }
}
