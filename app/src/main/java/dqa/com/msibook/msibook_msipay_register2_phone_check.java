package dqa.com.msibook;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

//確認手機  以及設定 MSI PAY支付密碼
public class msibook_msipay_register2_phone_check extends AppCompatActivity {

    private LinearLayout linear_bottom;
    private LinearLayout linear_set_pay_pwd;
    private TextView textView_phone_check;

    private Context mContent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msibook_msipay_register2_phone_check);

        mContent = this;

        linear_bottom = (LinearLayout) findViewById(R.id.linear_bottom);
        linear_set_pay_pwd = (LinearLayout) findViewById(R.id.linear_set_pay_pwd);
        textView_phone_check = (TextView) findViewById(R.id.textView_phone_check);

        Integer getCheck_number = Integer.valueOf(getIntent().getStringExtra("Check"));

        if (getCheck_number == 1){
            linear_set_pay_pwd.setVisibility(View.VISIBLE);
            textView_phone_check.setVisibility(View.VISIBLE);
            linear_bottom.setVisibility(View.INVISIBLE);
        }else{
            linear_set_pay_pwd.setVisibility(View.INVISIBLE);
            textView_phone_check.setVisibility(View.INVISIBLE);
            linear_bottom.setVisibility(View.VISIBLE);
        }

        linear_set_pay_pwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent msipay = new Intent(mContent,msibook_msipay_register4_set_pay_pwd.class);

                mContent.startActivity(msipay);

            }
        });

        linear_bottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent msipay = new Intent(mContent,msibook_msipay_register3_check_number.class);

                mContent.startActivity(msipay);

            }
        });


    }
}
