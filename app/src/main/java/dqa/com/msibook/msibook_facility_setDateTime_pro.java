package dqa.com.msibook;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.DatePicker;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.TimePicker;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class msibook_facility_setDateTime_pro extends AppCompatActivity {

    private TextView textView_setTitle;
    private DatePicker datePicker;
    private TimePicker timePicker;
    private TextView textView_cancel;
    private TextView textView_sent;
    private final static int TIME_PICKER_INTERVAL = 30;

    private String nowDate;
    private String nowTime;

    private String SaveDate;
    private String SaveTime;

    private Integer Get_ClickCheck;

    //方法DateFix:將傳送進來的年月日轉成String，在判斷是否前面需要加0。
    private static String DateFix(int c){
        if (c >= 10)
            return String.valueOf(c);
        else
            return "0" + String.valueOf(c);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msibook_facility_set_date_time_pro);


        textView_setTitle = (TextView)findViewById(R.id.textView_setTitle);
        datePicker = (DatePicker)findViewById(R.id.datePicker);
        timePicker = (TimePicker)findViewById(R.id.timePicker);
        //timePicker.setSelectableTimes(generateTimepoints(30));

        textView_cancel = (TextView)findViewById(R.id.textView_cancel);
        textView_sent = (TextView)findViewById(R.id.textView_sent);

        Get_ClickCheck = Integer.valueOf(getIntent().getStringExtra("Click_check"));//抓第一頁選的部門代號
        Log.w("Get_ClickCheck",String.valueOf(Get_ClickCheck));

        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy/MM/dd"); //讓日期顯示為設定格式
        SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm:ss a"); //讓日期顯示為設定格式
        nowDate = String.valueOf(sdfDate.format(Calendar.getInstance().getTime()));
        nowTime = String.valueOf(sdfTime.format(Calendar.getInstance().getTime()));
        Log.w("nowDate",String.valueOf(nowDate));

        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        //預設日期時間
        if(Get_ClickCheck == 0){
            textView_setTitle.setText("起始時間");
            timePicker.setCurrentHour(8);
            timePicker.setCurrentMinute(30);

            calendar.set(Calendar.HOUR_OF_DAY, 8);// for 6 hour
            calendar.set(Calendar.MINUTE, 30);// for 0 min
            calendar.set(Calendar.SECOND, 0);// for 0 sec
            nowTime = String.valueOf(sdfTime.format(calendar.getTime()));
        }else{
            textView_setTitle.setText("結束時間");
            timePicker.setCurrentHour(18);
            timePicker.setCurrentMinute(30);

            calendar.set(Calendar.HOUR_OF_DAY, 18);// for 6 hour
            calendar.set(Calendar.MINUTE, 30);// for 0 min
            calendar.set(Calendar.SECOND, 0);// for 0 sec
            nowTime = String.valueOf(sdfTime.format(calendar.getTime()));
        }

        datePicker.init(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth(), new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                int monthOfYearAdd = monthOfYear+1;
                SaveDate = String.valueOf(year+"/"+monthOfYearAdd+"/"+dayOfMonth);

            }
        });

        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                String AM_PM ;
                String HOFDay;
                String Min;
                if(hourOfDay < 12) {
                    AM_PM = "AM";
                    HOFDay = "0"+String.valueOf(hourOfDay);
                } else {
                    AM_PM = "PM";
                    HOFDay=String.valueOf(hourOfDay);
                }
                if(minute == 0){
                    Min = "00";
                }else{
                    Min = "30";
                }
                    SaveTime = String.valueOf(HOFDay+":"+Min+ ":00 "+ AM_PM);
                    Log.w("SaveSTime",String.valueOf(hourOfDay+":"+minute+ ":00 "+ AM_PM));

            }
        });

        textView_cancel.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                finish();
                return false;
            }
        });

        textView_sent.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                Bundle bundle = new Bundle();
                bundle.putString("CheckSorE","0");

                if (SaveDate == null )
                {
                    SaveDate = nowDate;
                }

                 //0305 Error IOS沒呈現所以拿掉
                    if ( SaveTime == null   )
                    {
                        SaveTime = nowTime;
                    }

                bundle.putString("Type",String.valueOf(SaveDate+" "+SaveTime));
//                bundle.putString("Type",String.valueOf(SaveDate));
                bundle.putString("Booking_Check","0");
                msibook_facility_setDateTime_pro.this.setResult(RESULT_OK,msibook_facility_setDateTime_pro.this.getIntent().putExtras(bundle));
                finish();

                return false;
            }
        });

        try {
            Class<?> classForid = Class.forName("com.android.internal.R$id");
            Field timePickerField = classForid.getField("timePicker");

            Field field = classForid.getField("minute");

            NumberPicker minuteSpinner = (NumberPicker) timePicker
                    .findViewById(field.getInt(null));
            minuteSpinner.setMinValue(0);
            minuteSpinner.setMaxValue((60 / TIME_PICKER_INTERVAL) - 1);
            List<String> displayedValues = new ArrayList<>();
            for (int i = 0; i < 60; i += TIME_PICKER_INTERVAL) {
                displayedValues.add(String.format("%d", i));
            }
            minuteSpinner.setDisplayedValues(displayedValues
                    .toArray(new String[displayedValues.size()]));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
