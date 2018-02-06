package dqa.com.msibook;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;



public class msibook_welcome extends AppCompatActivity {

    private Context _Context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msibook_welcome);

//        AppClass.Get_Server_All_Image(this);

//        FirebaseMessaging.getInstance().subscribeToTopic("dogs");
        //這裡來檢測版本是否需要更新
        _Context = this;

       // String token = FirebaseInstanceId.getInstance().getToken();

        //Log.w("Token",token);

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
