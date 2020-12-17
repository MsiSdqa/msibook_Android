package dqa.com.msibook;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

public class msibook_facility_setting_location extends AppCompatActivity {

    private LinearLayout linear_taipei;
    private LinearLayout linear_kusan;
    private LinearLayout linear_boane;
    private TextView textView_taipei;
    private TextView textView_kusan;
    private TextView textView_boane;
    private String Get_Location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msibook_facility_setting_location);


        linear_taipei = (LinearLayout) findViewById(R.id.linear_taipei);
        linear_kusan = (LinearLayout) findViewById(R.id.linear_kusan);
        linear_boane = (LinearLayout) findViewById(R.id.linear_boane);
        textView_taipei = (TextView) findViewById(R.id.textView_taipei);
        textView_kusan = (TextView) findViewById(R.id.textView_kusan);
        textView_boane = (TextView) findViewById(R.id.textView_boane);

        textView_taipei.setVisibility(View.VISIBLE);
        textView_kusan.setVisibility(View.INVISIBLE);
        textView_boane.setVisibility(View.INVISIBLE);

        Bundle bundle = new Bundle();
        bundle.putString("Type", "1");

        Get_Location = getIntent().getStringExtra("Set_Location");//抓第一頁選的部門代號
        switch (Integer.valueOf(Get_Location)) {
            case 0:
                textView_taipei.setVisibility(View.VISIBLE);
                textView_kusan.setVisibility(View.INVISIBLE);
                textView_boane.setVisibility(View.INVISIBLE);
                break;

            case 1:
                textView_taipei.setVisibility(View.INVISIBLE);
                textView_kusan.setVisibility(View.VISIBLE);
                textView_boane.setVisibility(View.INVISIBLE);

                break;

            case 2:
                textView_taipei.setVisibility(View.INVISIBLE);
                textView_kusan.setVisibility(View.INVISIBLE);
                textView_boane.setVisibility(View.VISIBLE);

                break;
        }

        linear_taipei.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        linear_taipei.setBackgroundColor(Color.parseColor("#e2e2e2"));//按下灰色
                        return true;
                    case MotionEvent.ACTION_UP:
                        linear_taipei.setBackgroundColor(Color.parseColor("#FFFFFF"));//放開白色
                        textView_taipei.setVisibility(View.VISIBLE);
                        textView_kusan.setVisibility(View.INVISIBLE);
                        textView_boane.setVisibility(View.INVISIBLE);

                        Bundle bundle = new Bundle();
                        bundle.putString("Type", "0");
                        bundle.putString("Booking_Check", "0");
                        msibook_facility_setting_location.this.setResult(RESULT_OK,msibook_facility_setting_location.this.getIntent().putExtras(bundle));
                        finish();

                        return true;
                }
                return false;
            }
        });

        linear_kusan.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        linear_kusan.setBackgroundColor(Color.parseColor("#e2e2e2"));//按下灰色
                        return true;
                    case MotionEvent.ACTION_UP:
                        linear_kusan.setBackgroundColor(Color.parseColor("#FFFFFF"));//放開白色
                        textView_taipei.setVisibility(View.INVISIBLE);
                        textView_kusan.setVisibility(View.VISIBLE);
                        textView_boane.setVisibility(View.INVISIBLE);
                        Bundle bundle = new Bundle();
                        bundle.putString("Type", "1");
                        bundle.putString("Booking_Check", "0");
                        msibook_facility_setting_location.this.setResult(RESULT_OK,msibook_facility_setting_location.this.getIntent().putExtras(bundle));
                        finish();

                        return true;
                }
                return false;
            }
        });

        linear_boane.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        linear_boane.setBackgroundColor(Color.parseColor("#e2e2e2"));//按下灰色
                        return true;
                    case MotionEvent.ACTION_UP:
                        linear_boane.setBackgroundColor(Color.parseColor("#FFFFFF"));//放開白色
                        textView_taipei.setVisibility(View.INVISIBLE);
                        textView_kusan.setVisibility(View.INVISIBLE);
                        textView_boane.setVisibility(View.VISIBLE);
                        Bundle bundle = new Bundle();
                        bundle.putString("Type", "2");
                        bundle.putString("Booking_Check", "0");
                        msibook_facility_setting_location.this.setResult(RESULT_OK,msibook_facility_setting_location.this.getIntent().putExtras(bundle));
                        finish();

                        return true;
                }
                return false;
            }
        });

    }
}
