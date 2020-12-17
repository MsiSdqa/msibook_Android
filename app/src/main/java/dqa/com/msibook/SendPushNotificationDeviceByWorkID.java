package dqa.com.msibook;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class SendPushNotificationDeviceByWorkID {

    private Context mContext;

    public SendPushNotificationDeviceByWorkID(Context context) {
        this.mContext = context;

    }

    //新增推播
    public void Insert_SendPushNotificationDeviceByWorkID(String WorkID,String Title,String Message,String Tag,String Key,String Value) {

        RequestQueue mQueue = Volley.newRequestQueue(mContext);

        Map<String, String> map = new HashMap<String, String>();
        map.put("WorkID", WorkID);
        map.put("Title", Title);
        map.put("Message", Message);
        map.put("Tag", Tag);
        map.put("Key", Key);
        map.put("Value", Value);
        map.put("APP", "Book");

        Log.w("WorkID",WorkID);
        Log.w("Title",Title);
        Log.w("Message",Message);
        Log.w("Tag",Tag);
        Log.w("Key",Key);
        Log.w("Value",Value);

        if (mQueue == null) {
            mQueue = Volley.newRequestQueue(mContext);
        }

        String Path = "http://wtsc.msi.com.tw/IMS/MsiBook_App_Service.asmx/SendPushNotificationDeviceByWorkID";

        SendPostRequest(Path, mQueue, new GetServiceData.VolleyStringCallback() {
            @Override
            public void onSendRequestSuccess(String result) {

//                Integer SummaryNumber =  Integer.valueOf(main_summary_number.getText().toString());
//
//                main_summary_number.setText(String.valueOf((SummaryNumber +1)));

            }

            @Override
            public void onSendRequestError(String result) {



            }

        }, map);
    }



    public static void SendPostRequest(String Url, RequestQueue mQueue, final GetServiceData.VolleyStringCallback callback, final Map<String, String> map) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                callback.onSendRequestSuccess(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                callback.onSendRequestError(error.getMessage());

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String, String> map = new HashMap<String, String>();
//                map.put("params1", "value1");
//                map.put("params2", "value2");
                return map;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        mQueue.add(stringRequest);
    }

}
