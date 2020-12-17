package dqa.com.msibook;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class msibook_msipay_show_my_barcode extends AppCompatActivity {

    private LinearLayout linear_barcode;

    private ImageView imageView_qrcode;

    private Context mContent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msibook_msipay_show_my_barcode);

        mContent = this;

        linear_barcode = (LinearLayout) findViewById(R.id.linear_barcode);

        imageView_qrcode = (ImageView) findViewById(R.id.imageView_qrcode);

        linear_barcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent msipay = new Intent(mContent,msibook_msipay_show_pay_money_detial.class);

                //msipay.putExtra("Check","0");

                mContent.startActivity(msipay);

            }
        });

        imageView_qrcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent msipay = new Intent(mContent,msibook_msipay_show_pay_money_detial.class);

                //msipay.putExtra("Check","0");

                mContent.startActivity(msipay);

            }
        });
    }
}
