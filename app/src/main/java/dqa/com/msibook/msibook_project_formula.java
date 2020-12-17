package dqa.com.msibook;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class msibook_project_formula extends AppCompatActivity {

    private TextView textView_include;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msibook_project_formula);

        textView_include = (TextView) findViewById(R.id.textView_include);

        String getEtraWeek = getIntent().getStringExtra("RegionID");//抓第一頁選的部門代號
        switch(Integer.valueOf(getEtraWeek)) {
            case 1:
                textView_include.setText("人力資源分配含(7850、7860、7870、7880)");
                break;
            case 2:
                textView_include.setText("人力資源分配含(MSIK 微盟-設計支援部  K330)\n補充:排除請假資料 (尚未取得)");
                break;
            case 3:
                textView_include.setText("人力資源分配含(MSIS 恩斯邁-Common Pool C350)\n補充:排除請假資料 (尚未取得)");
                break;
        }

    }
}
