package dqa.com.msibook;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import java.util.Arrays;

public class msibook_ehr_splashscreen extends AppCompatActivity {

    // Splash screen timer
    private static int SPLASH_TIME_OUT = 0; //開啟畫面時間(3秒)
    private String User_WorkID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msibook_ehr_splashscreen);


        final String[] Keyman = {"10015812", "10012565","10003275"};//人資開放使用功能
        //final String[] Keyman = {"10012565","10003275"};//人資開放使用功能
        //final String[] Keyman = {"10012565"};//人資開放使用功能
        User_WorkID = UserData.WorkID;


        new Handler().postDelayed(new Runnable() {
            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */
            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity

                //導向頁面不同
                Intent i = new Intent(msibook_ehr_splashscreen.this, msibook_ehr_main_user.class); //MainActivity為主要檔案名稱
                startActivity(i);
                // close this activity
                finish();


//                if (Arrays.asList(Keyman).contains(User_WorkID)){
//                    Intent i = new Intent(msibook_ehr_splashscreen.this, msibook_ehr_main.class); //MainActivity為主要檔案名稱
//                    startActivity(i);
//                    // close this activity
//                    finish();
//
//                }else{
//                    Intent i = new Intent(msibook_ehr_splashscreen.this, msibook_ehr_main_user.class); //MainActivity為主要檔案名稱
//                    startActivity(i);
//                    // close this activity
//                    finish();
//
//                }


            }
        }, SPLASH_TIME_OUT);
    }
}
