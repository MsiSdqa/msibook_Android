package dqa.com.msibook;

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import static com.google.android.gms.plus.PlusOneDummyView.TAG;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        //加這個才可以整個廣播
        FirebaseMessaging.getInstance().subscribeToTopic("dogs");

        Log.w("FCMMM", "onMessageReceived:"+remoteMessage.getFrom());

        Log.w(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());

        Log.w(TAG, "Message Notification Body: " + remoteMessage.getNotification().getTag());

    }

    private void sendNotification(RemoteMessage remoteMessage) {

        if (remoteMessage.getData().size() > 0) {

            Log.w("key", remoteMessage.getData().get("key").toString());
        }

    }

}
