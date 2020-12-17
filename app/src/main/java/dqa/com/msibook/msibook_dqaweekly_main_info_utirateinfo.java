package dqa.com.msibook;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

public class msibook_dqaweekly_main_info_utirateinfo extends AppCompatActivity {

    private TextView man_power_ic_status;
    private TextView workhour_ic_status;
    private TextView loading_message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msibook_dqaweekly_main_info_utirateinfo);

        final LayoutInflater factory = getLayoutInflater();

        final View textEntryView = factory.inflate(R.layout.activity_msibook_dqaweekly_main_info_utirateinfo, null);

        man_power_ic_status = (TextView)textEntryView.findViewById(R.id.man_power_ic_status);
        workhour_ic_status = (TextView)textEntryView.findViewById(R.id.workhour_ic_status);
        loading_message = (TextView)textEntryView.findViewById(R.id.loading_message);

        //設定顯示座標位置
        String x_Location = getIntent().getStringExtra("x_Location");
        String y_Location = getIntent().getStringExtra("y_Location");

        WindowManager.LayoutParams lp = this.getWindow().getAttributes();
        lp.x=Integer.parseInt(x_Location);lp.y=Integer.parseInt(y_Location)-500;

        this.setContentView(textEntryView, lp);

        String putManpower_status = getIntent().getStringExtra("putManpower_status");
        Log.w("putManpower_status",putManpower_status);
        String putWorkhour_status = getIntent().getStringExtra("putWorkhour_status");
        Log.w("putWorkhour_status",putWorkhour_status);
        String putMessage = getIntent().getStringExtra("putMessage");
        Log.w("putMessage",putMessage);

        switch (String.valueOf(putManpower_status)) {
            case "ic_status_shortage":
                man_power_ic_status.setBackgroundResource(R.mipmap.dqaweekly_ic_status_shortage);
                break;
            case "ic_status_fulledition":
                man_power_ic_status.setBackgroundResource(R.mipmap.dqaweekly_ic_status_fulledition);
                break;
            case "ic_status_super":
                man_power_ic_status.setBackgroundResource(R.mipmap.dqaweekly_ic_status_super);
                break;
        }

        switch (String.valueOf(putWorkhour_status)) {
            case "ic_status_fullload":
                workhour_ic_status.setBackgroundResource(R.mipmap.dqaweekly_ic_status_fullload);
                break;
            case "ic_status_overload":
                workhour_ic_status.setBackgroundResource(R.mipmap.dqaweekly_ic_status_overload);
                break;
            case "ic_status_lowload":
                workhour_ic_status.setBackgroundResource(R.mipmap.dqaweekly_ic_status_lowload);
                break;
        }
        loading_message.setText(putMessage);

    }
}
