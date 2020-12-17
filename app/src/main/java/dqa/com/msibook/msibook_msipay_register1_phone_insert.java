package dqa.com.msibook;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

public class msibook_msipay_register1_phone_insert extends AppCompatActivity {

    private LinearLayout linear_bottom;

    private Context mContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msibook_msipay_register1_phone_insert);

        mContent = this;

        linear_bottom = (LinearLayout) findViewById(R.id.linear_bottom);

        linear_bottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent msipay = new Intent(mContent,msibook_msipay_register2_phone_check.class);

                msipay.putExtra("Check","0");

                mContent.startActivity(msipay);

            }
        });
    }
}
