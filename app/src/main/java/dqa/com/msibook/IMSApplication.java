package dqa.com.msibook;

import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.facebook.drawee.backends.pipeline.Fresco;

public class IMSApplication extends MultiDexApplication {

    @Override
    public void onCreate() {
        super.onCreate();

        MultiDex.install(getApplicationContext());
        // the following line is important
        Fresco.initialize(getApplicationContext());
    }


}
