package dqa.com.msibook;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

public class msibook_dqaweekly_project_resource_cost_info extends AppCompatActivity {

    private Button button_close;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msibook_dqaweekly_project_resource_cost_info);

        final LayoutInflater factory = getLayoutInflater();

        final View textEntryView = factory.inflate(R.layout.activity_msibook_dqaweekly_project_resource_cost_info, null);

        //設定顯示座標位置
        String x_Location = getIntent().getStringExtra("x_Location");
        String y_Location = getIntent().getStringExtra("y_Location");

        WindowManager.LayoutParams lp = this.getWindow().getAttributes();
        lp.x=Integer.parseInt(x_Location)-100;lp.y=Integer.parseInt(y_Location)-450;

        this.setContentView(textEntryView, lp);

        button_close = (Button) findViewById(R.id.button_close);
        button_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}
