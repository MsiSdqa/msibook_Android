package dqa.com.msibook;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.multidex.MultiDex;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.google.firebase.messaging.FirebaseMessaging;


public class msibook_welcome extends AppCompatActivity {

    private Context _Context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msibook_welcome);

        MultiDex.install(getApplicationContext());
        // the following line is important
        Fresco.initialize(getApplicationContext());


        FirebaseMessaging.getInstance().subscribeToTopic("dogs");
//        AppClass.Get_Server_All_Image(this);

//        FirebaseMessaging.getInstance().subscribeToTopic("dogs");
        //這裡來檢測版本是否需要更新
        _Context = this;

        // String token = FirebaseInstanceId.getInstance().getToken();

        //Log.w("Token",token);
        String GetValue = "";
        String Getkey = "";
        Intent startingIntent = getIntent();
        if (startingIntent != null) {

            if (startingIntent.getStringExtra("key") != null) {
                if (startingIntent.getStringExtra("key").contains("eHR_Application")) {

                    UserDB UserDB = new UserDB(msibook_welcome.this);
                    if (UserDB.getCount() > 0) {

                        UserData UserData = new UserData();

                        UserData = UserDB.getAll().get(0);
                    }

                    GetValue = startingIntent.getStringExtra("value"); // Retrieve the id
                    Intent intent = new Intent();

                    intent.putExtra("Push_Key", "eHR_Application");//

                    intent.putExtra("Push_Value", GetValue);//

                    intent.setClass(msibook_welcome.this, MainPage.class);

                    startActivity(intent);

                    finish();
                }else if (startingIntent.getStringExtra("key").contains("YT")) { // YT

                    UserDB UserDB = new UserDB(msibook_welcome.this);
                    if (UserDB.getCount() > 0) {

                        UserData UserData = new UserData();

                        UserData = UserDB.getAll().get(0);
                    }

                    GetValue = startingIntent.getStringExtra("value"); // Retrieve the id
                    Intent intent = new Intent();

                    intent.putExtra("Push_Key", "YT");//

                    intent.putExtra("Push_Value", GetValue);//

                    intent.setClass(msibook_welcome.this, MainPage.class);

                    startActivity(intent);

                    finish();
                }else if (startingIntent.getStringExtra("key").contains("FB")) { //FB

                    UserDB UserDB = new UserDB(msibook_welcome.this);
                    if (UserDB.getCount() > 0) {

                        UserData UserData = new UserData();

                        UserData = UserDB.getAll().get(0);
                    }

                    GetValue = startingIntent.getStringExtra("value"); // Retrieve the id
                    Intent intent = new Intent();

                    intent.putExtra("Push_Key", "FB");//

                    intent.putExtra("Push_Value", GetValue);//

                    intent.setClass(msibook_welcome.this, MainPage.class);

                    startActivity(intent);

                    finish();
                }else if (startingIntent.getStringExtra("key").contains("MENU")) {  // MENU

                    UserDB UserDB = new UserDB(msibook_welcome.this);
                    if (UserDB.getCount() > 0) {

                        UserData UserData = new UserData();

                        UserData = UserDB.getAll().get(0);
                    }

                    GetValue = startingIntent.getStringExtra("value"); // Retrieve the id
                    Intent intent = new Intent();

                    intent.putExtra("Push_Key", "MENU");//

                    intent.putExtra("Push_Value", GetValue);//

                    intent.setClass(msibook_welcome.this, MainPage.class);

                    startActivity(intent);

                    finish();
                }else if (startingIntent.getStringExtra("key").contains("MMC")) {  //MMC

                    UserDB UserDB = new UserDB(msibook_welcome.this);
                    if (UserDB.getCount() > 0) {

                        UserData UserData = new UserData();

                        UserData = UserDB.getAll().get(0);
                    }

                    GetValue = startingIntent.getStringExtra("value"); // Retrieve the id
                    Intent intent = new Intent();

                    intent.putExtra("Push_Key", "MMC");//

                    intent.putExtra("Push_Value", GetValue);//

                    intent.setClass(msibook_welcome.this, MainPage.class);

                    startActivity(intent);

                    finish();
                }


                GetValue = startingIntent.getStringExtra("value"); // Retrieve the id
                Getkey = startingIntent.getStringExtra("key"); // Retrieve the id

                Log.w("GetValue", GetValue);
                Log.w("Getkey", Getkey);


//                    showRecordingNotification(idOffer);
//                }
            }else{
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // TODO Auto-generated method stub

                        UserDB UserDB = new UserDB(msibook_welcome.this);

                        //如果進來程式有資料的話就不用再登入
                        if (UserDB.getCount() > 0) {

                            UserData UserData = new UserData();

                            UserData = UserDB.getAll().get(0);

                            Intent intent = new Intent(msibook_welcome.this, MainPage.class);

                            startActivity(intent);

                             finish();
                        } else {
                            Intent intent = new Intent(msibook_welcome.this, msibook_login.class);
                            startActivity(intent);
                            msibook_welcome.this.finish();

                        }


                    }
                }, 2000);//两秒后跳转到另一个页面

            }

        }


    }

    @Override
    protected void onStart() {
        super.onStart();
//        Intent intent = getIntent();
//        String Tag = intent.getStringExtra("Tag");
//        String Key = intent.getStringExtra("Key");
//        String Value = intent.getStringExtra("Value");
//
//        if (Tag!=null)
//            Log.w("FCMTagTagTag", "Tag:"+Tag);
//        if (Key!=null)
//            Log.w("FCMKeyKeyKey", "Key:"+Key);
//        if (Value!=null)
//            Log.w("FCMValueValue", "Value:"+Value);

    }



}
