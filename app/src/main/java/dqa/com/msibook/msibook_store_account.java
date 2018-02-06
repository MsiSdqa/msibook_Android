package dqa.com.msibook;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class msibook_store_account extends AppCompatActivity {

    private RequestQueue mQueue;
    private Context mContext;
    private String Master_ID;
    private TextView txt_Point;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msibook_store_account);
        mContext = msibook_store_account.this;

        txt_Point = (TextView) findViewById(R.id.txt_Point);
        setTitle("Account");

        Bundle Bundle = getIntent().getExtras();

        Master_ID = Bundle.getString("Master_ID");

        Find_Store_Point(Master_ID, UserData.WorkID);
    }

    private void Find_Store_Point(String Master_ID, String Owner) {

        if (mQueue == null) {
            mQueue = Volley.newRequestQueue(mContext);
        }

        String Path = GetServiceData.ServicePath + "/Find_Store_Point";

        Map<String, String> map = new HashMap<String, String>();

        Log.w("F_Master_ID", Master_ID);
        Log.w("F_Owner", Owner);
        map.put("F_Master_ID", Master_ID);
        map.put("F_Owner", Owner);
        GetServiceData.SendPostRequest(Path, mQueue, new GetServiceData.VolleyStringCallback() {
            @Override
            public void onSendRequestSuccess(String result) {

                try {
                    JSONObject obj = new JSONObject(result);

                    JSONArray UserArray = new JSONArray(obj.getString("Key"));


                    if (UserArray.length() > 0) {
                        JSONObject IssueData = UserArray.getJSONObject(0);

                        String F_Point = String.valueOf(IssueData.getInt("F_Point"));

                        txt_Point.setText(F_Point);

                    }

                } catch (JSONException ex) {

                    Log.w("JsonExceptrion", ex.toString());

                }
            }

            @Override
            public void onSendRequestError(String result) {

            }
        }, map);
    }
}
