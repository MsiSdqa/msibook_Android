package dqa.com.msibook;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

public class msibook_msipay_main extends AppCompatActivity {

    private LinearLayout linear_my_barcode;

    private Context mContent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msibook_msipay_main);

        mContent = this;

        linear_my_barcode = (LinearLayout) findViewById(R.id.linear_my_barcode);

        linear_my_barcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

        Intent msipay = new Intent(mContent,msibook_msipay_register_my_barcode.class);

        //msipay.putExtra("Check","0");

        mContent.startActivity(msipay);

            }
        });
    }
}
