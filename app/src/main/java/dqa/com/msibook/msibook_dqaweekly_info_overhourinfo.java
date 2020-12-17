package dqa.com.msibook;

        import android.content.Intent;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.util.DisplayMetrics;
        import android.util.Log;
        import android.view.Gravity;
        import android.view.LayoutInflater;
        import android.view.MotionEvent;
        import android.view.View;
        import android.view.WindowManager;
        import android.widget.Button;
        import android.widget.LinearLayout;
        import android.widget.TextView;

public class msibook_dqaweekly_info_overhourinfo extends AppCompatActivity {

    private LinearLayout linear_overhourinfo;
    private TextView textview_lastweek_total;
    private TextView textview_lastmonth_total;
    private Button btn_detail;

    private String Dpt_name;
    private String Dpt_id;
    private String Year;
    private String Week;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msibook_dqaweekly_info_overhourinfo);
        final LayoutInflater factory = getLayoutInflater();


        final View textEntryView = factory.inflate(R.layout.activity_msibook_dqaweekly_info_overhourinfo, null);
        //設定顯示座標位置
        String x_Location = getIntent().getStringExtra("x_Location");
        String y_Location = getIntent().getStringExtra("y_Location");

        WindowManager.LayoutParams lp = this.getWindow().getAttributes();
        lp.x=Integer.parseInt(x_Location);lp.y=Integer.parseInt(y_Location);

        this.setContentView(textEntryView, lp);

        linear_overhourinfo = (LinearLayout)findViewById(R.id.linear_overhourinfo);
        textview_lastweek_total = (TextView)findViewById(R.id.textview_lastweek_total);
        textview_lastmonth_total = (TextView)findViewById(R.id.textview_lastmonth_total);
        btn_detail = (Button) findViewById(R.id.btn_detail);

        String DptTitle = getIntent().getStringExtra("DptTitle");//抓第一頁部門名稱
        Dpt_name = DptTitle;
        Log.w("DeptID",DptTitle);

        String getEtraDepID = getIntent().getStringExtra("ChoiceDepID");//抓第一頁選的部門代號
        Dpt_id = getEtraDepID;
        Log.w("DeptID",getEtraDepID);

        String getYear = getIntent().getStringExtra("Year");//抓第一頁選的週次
        Year = getYear;

        String getWeek = getIntent().getStringExtra("Week");//抓第一頁選的週次
        Week = getWeek.replace("週","");

        String getLastWeekTotalHour = getIntent().getStringExtra("LastWeekTotalHour");//抓第一頁選的週次
        textview_lastweek_total.setText(getLastWeekTotalHour);

        String getLastMonthTotalHour = getIntent().getStringExtra("LastMonthTotalHour");//抓第一頁選的週次
        textview_lastmonth_total.setText(getLastMonthTotalHour);

        DisplayMetrics metrics = getResources().getDisplayMetrics();

        linear_overhourinfo.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                Intent intent = new Intent(msibook_dqaweekly_info_overhourinfo.this, msibook_dqaweekly_check_overtime_history.class);

                intent.putExtra("Dpt_name", Dpt_name);//部門名稱

                intent.putExtra("Dpt_id", Dpt_id);//部門ID

                intent.putExtra("Year", Year);//部門名稱

                intent.putExtra("Week", Week);//部門名稱

                msibook_dqaweekly_info_overhourinfo.this.startActivity(intent);

                return false;
            }
        });


//        int screenWidth = (int) (metrics.widthPixels * 0.8);
//
//        int screenHeight = (int) (metrics.heightPixels * 0.78);
//
//        getWindow().setLayout(screenWidth, screenHeight);
    }
}
