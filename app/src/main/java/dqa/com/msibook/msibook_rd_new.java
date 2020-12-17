package dqa.com.msibook;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class msibook_rd_new extends Activity implements View.OnClickListener {

    String F_Keyin;
    String F_Data;
    String F_ModelID;
    String F_Owner;
    String F_Subject;
    String selectedtext;
    String F_Hr = "2";
    String F_CostSum;
    String F_TopicID_L3;

    Spinner sp_MS ;
    Spinner sp_Topic;
    Spinner sp_Type ;

    final HashMap<String,String> spinnerMap = new HashMap<String, String>();
    final HashMap<String,String> spinnerMap1 = new HashMap<String, String>();
    final HashMap<String,String> spinnerMap2 = new HashMap<String, String>();


    private Button[] btn = new Button[4];
    private Button btn_unfocus;
    private int[] btn_id = {R.id.btn_2, R.id.btn_4, R.id.btn_6, R.id.btn_8};



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msibook_rd_new);
        Button btn_complete = (Button)findViewById(R.id.btn_complete);
        sp_MS = (Spinner)findViewById(R.id.sp_MS);


        //sp_Topic = (Spinner)findViewById(R.id.sp_Topic);
        //sp_Type = (Spinner)findViewById(R.id.sp_Type);
        for(int i = 0; i < btn.length; i++){
            btn[i] = (Button) findViewById(btn_id[i]);
            btn[i].setBackgroundResource(R.drawable.rd_button);
            //btn[i].setTextColor(Color.parseColor("#6d7073"));
            btn[i].setOnClickListener(this);
        }
        btn_complete.setOnClickListener(this);
        btn_unfocus = btn[0];

        btn[0].setTextColor(Color.parseColor("#FFFFFF"));
        btn[0].setBackgroundResource(R.drawable.rd_button_pressdone);

        GetInitialData();
        TextView text_workhr =(TextView) findViewById(R.id.text_workhr);

        Bundle bData = this.getIntent().getExtras();
        text_workhr.setText(F_Data = bData.getString( "Date" ));

    }




    protected void GetInitialData() {

        RequestQueue mQueue = Volley.newRequestQueue(this);

        String []  Pathendary = {"RDLog_Model"};
        String []  ValueNameary = {"F_ModelID"};
        String []  DataNameary = {"Model"};
        String []  Spinnerary = {"sp_MS"};
        Bundle bData = this.getIntent().getExtras();
        for (int i =0; i <1;i++)
        {



            final String Path = "http://wtsc.msi.com.tw/IMS/MsiBook_App_Service.asmx/"+ Pathendary[i] + "?F_Keyin=" + bData.getString( "UserID" );
            final String DataNameS = DataNameary[i];
            final String ValueNameS = ValueNameary[i];
            final String Spinner1 = Spinnerary[i];
            final int resID = getResources().getIdentifier(Spinner1, "id", getPackageName());




            getString(Path, mQueue, new msibook_rd_main.VolleyCallback() {
                @Override
                public void onSuccess(JSONObject result) {

                    try {

                        JSONArray UserArray = new JSONArray(result.getString("Key"));

                        final String[] spinnerArray = new String[UserArray.length()];
                        if (UserArray.length() > 0) {

                            for (int i = 0; i < UserArray.length(); i++) {
                                String[] Content1 = new String[UserArray.length()];

                                JSONObject IssueData = UserArray.getJSONObject(i);

                                String DataName = String.valueOf(IssueData.getString(ValueNameS));//Value

                                String ValueName = String.valueOf(IssueData.getString(DataNameS));//Model

                                switch(Spinner1)
                                {
                                    case "sp_MS":
                                        spinnerMap.put(ValueName,DataName);
                                        spinnerArray[i]=ValueName;

                                        break;
                                    case "sp_Topic":
                                        spinnerMap1.put(ValueName,DataName);
                                        spinnerArray[i]=ValueName;
                                        break;
                                    case "sp_Type":
                                        spinnerMap2.put(ValueName,DataName);
                                        spinnerArray[i]=ValueName;
                                }





                            }

                            Spinner Spinner = (Spinner)findViewById(resID);



                            ArrayAdapter<String> MSList = new ArrayAdapter<String>(msibook_rd_new.this,R.layout.msibook_rd_customerspinner,spinnerArray);
                            MSList.setDropDownViewResource(R.layout.msibook_rd_dropdown_style);
                            Spinner.setAdapter(MSList);


                            //finish();
                        } else {
                            new AlertDialog.Builder(msibook_rd_new.this)
                                    .setTitle("Message")
                                    .setMessage("ID / Password  Error")
                                    .setPositiveButton("確定",
                                            new DialogInterface.OnClickListener(){
                                                public void onClick(DialogInterface dialoginterface, int i){
                                                    //按钮事件
//                                                Toast.makeText(login.this, "確定",Toast.LENGTH_LONG).show();

                                                }
                                            }).show();
                        }


                    } catch (JSONException ex) {



                    }



                }

            });
        }


    }
    public static void getString(String Url, RequestQueue mQueue, final msibook_rd_main.VolleyCallback callback) {

        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, Url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        callback.onSuccess(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("TestJsonObj");
                        System.out.println(error);
                    }
                }
        );

        int socketTimeout = 30000;
        getRequest.setRetryPolicy(new DefaultRetryPolicy(socketTimeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        mQueue.add(getRequest);
    }
    public static void SendPostRequest(String Url, RequestQueue mQueue, final VolleyStringCallback callback, final Map<String, String> map)
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Url,   new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                callback.onSendRequestSuccess(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", error.getMessage(), error);
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

        mQueue.add(stringRequest);
    }
    public interface VolleyStringCallback {

        void onSendRequestSuccess(String response);
    }





    @Override
    public void onClick(View v) {
        //setForcus(btn_unfocus, (Button) findViewById(v.getId()));
        //Or use switch
        switch (v.getId()){
            case R.id.btn_2 :
                F_Hr = "2";
                setFocus(btn_unfocus, btn[0]);
                break;

            case R.id.btn_4 :
                F_Hr = "4";
                setFocus(btn_unfocus, btn[1]);
                break;

            case R.id.btn_6 :
                F_Hr = "6";
                setFocus(btn_unfocus, btn[2]);
                break;

            case R.id.btn_8 :
                F_Hr = "8";
                setFocus(btn_unfocus, btn[3]);
                break;

            case R.id.btn_complete :

                setDate();
                Bundle bundle = new Bundle();
                bundle.putString("Add_Check","1");
                msibook_rd_new.this.setResult(RESULT_OK,msibook_rd_new.this.getIntent().putExtras(bundle));
//                Intent intent = new Intent();
//                intent.setClass(msibook_rd_new.this, msibook_rd_main.class);
//                startActivity(intent);
                finish();
                break;
        }
    }

    private void setFocus(Button btn_unfocus, Button btn_focus){
        btn_unfocus.setTextColor(Color.parseColor("#6d7073"));
        //btn_unfocus.setBackgroundColor(Color.parseColor("#000000"));
        btn_unfocus.setBackgroundResource(R.drawable.rd_button);
        btn_focus.setTextColor(Color.parseColor("#FFFFFF"));
        //btn_focus.setBackgroundColor(Color.parseColor("#E7222B"));
        btn_focus.setBackgroundResource(R.drawable.rd_button_pressdone);

        this.btn_unfocus = btn_focus;
    }

    private void setDate(){
        Bundle bData = this.getIntent().getExtras();

        F_ModelID =spinnerMap.get(sp_MS.getSelectedItem().toString());
        F_Keyin = bData.getString( "UserID" );
        F_Data = bData.getString( "Date" );
        F_Owner = bData.getString( "Owner" );
        //F_Subject = sp_Topic.getSelectedItem().toString();
        final RadioGroup rg = (RadioGroup) findViewById(R.id.radioGropuId);
        int radioButtonID = rg.getCheckedRadioButtonId();
        View radioButton = rg.findViewById(radioButtonID);
        int idx = rg.indexOfChild(radioButton);
        RadioButton r = (RadioButton)  rg.getChildAt(idx);
        selectedtext = r.getText().toString();
        F_Hr = F_Hr;
        F_CostSum = Integer.toString(Integer.parseInt(F_Hr) * 350);

        //F_TopicID_L3=spinnerMap2.get(sp_Type.getSelectedItem().toString());

        InsertDate(F_Keyin,F_Data,F_ModelID,F_Owner,selectedtext,F_Hr,F_CostSum,"10012");
    }

    private void InsertDate(String F_Keyin, String F_Date, String F_ModelID, String F_Owner, String F_Subject, String F_Hr, String F_CostSum, String F_TopicID_L3){
        final String Path = "http://wtsc.msi.com.tw/IMS/MsiBook_App_Service.asmx/RDLog_InsertRssLog";
        RequestQueue mQueue = Volley.newRequestQueue(this);
        Map<String, String> map = new HashMap<String, String>();
        map.put("F_Keyin", F_Keyin);
        map.put("F_Date", F_Date);
        map.put("F_ModelID", F_ModelID);
        map.put("F_Owner", F_Owner);
        map.put("F_Subject", F_Subject);
        map.put("F_Hr", F_Hr);
        map.put("F_CostSum", F_CostSum);
        map.put("F_TopicID_L3", F_TopicID_L3);
        if (mQueue == null) {
            mQueue = Volley.newRequestQueue(this);
        }

        SendPostRequest(Path, mQueue, new msibook_rd_new.VolleyStringCallback() {
            @Override
            public void onSendRequestSuccess(String result) {

                //利用Toast的靜態函式makeText來建立Toast物件
                Toast toast = Toast.makeText(msibook_rd_new.this,
                        "新增成功!", Toast.LENGTH_LONG);
                //顯示Toast
                toast.show();
            }


            public void onSendRequestError(String result) {
                android.util.Log.w("VolleyError",result);
            }

        }, map);
    }



}
