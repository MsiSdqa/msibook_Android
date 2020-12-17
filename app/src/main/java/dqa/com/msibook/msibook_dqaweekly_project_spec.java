package dqa.com.msibook;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class msibook_dqaweekly_project_spec extends AppCompatActivity {

    List<Spec_Item> Spec_List = new ArrayList<Spec_Item>();
    private ListView lsv_main;
    private SpecAdapter mListAdapter;
    private ProgressDialog pDialog;
    String ModelName;
    private RequestQueue mQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msibook_dqaweekly_project_spec);

        pDialog = new ProgressDialog(msibook_dqaweekly_project_spec.this);

        //View v = inflater.inflate(R.layout.fragment_my_issue, container, false);
        //宣告 ListView 元件
        lsv_main = (ListView) findViewById(dqa.com.msibook.R.id.listView);

        //lsv_main.setOnItemClickListener(listViewOnItemClickListener);

        Bundle bundle = this.getIntent().getExtras();

        String ModelID = bundle.getString("ModelID");

        String ModelName = bundle.getString("ModelName");

        GetPM_SpecData(ModelID);

        setTitle(ModelName);

    }



    private void GetPM_SpecData(String PM_ID) {

        pDialog.setTitle("Loading...");

        pDialog.show();


        if (mQueue == null) {
            mQueue = Volley.newRequestQueue(this);
        }

        String Path = "http://wtsc.msi.com.tw/IMS/IMS_App_Service.asmx/Find_Model_Spec?PM_ID=" + PM_ID;

        getString(Path, mQueue, new GetServiceData.VolleyCallback() {
            @Override
            public void onSuccess(JSONObject result) {

                SpecDataMapping(result);

                pDialog.hide();
            }
        });

    }

    private void SpecDataMapping(JSONObject result) {
        try {
            Spec_List.clear();

            JSONArray UserArray = new JSONArray(result.getString("Key"));

            if (UserArray.length() > 0) {

                for (int i = 0; i < UserArray.length(); i++) {
                    JSONObject ModelData = UserArray.getJSONObject(i);

                    String F_FieldName = ModelData.getString("F_FieldName");

                    String F_FieldValue = ModelData.getString("F_SpecData").replace("<br>", "\n");

                    Spec_List.add(i, new Spec_Item(F_FieldName, F_FieldValue));
                }

                // ListView 中所需之資料參數可透過修改 Adapter 的建構子傳入
                mListAdapter = new SpecAdapter(this, Spec_List);


//                lsv_main.setEmptyView(findViewById(R.id.empty));
                //設定 ListView 的 Adapter
                lsv_main.setAdapter(mListAdapter);
            } else {
                lsv_main.setEmptyView(findViewById(dqa.com.msibook.R.id.empty));
            }


        } catch (JSONException ex) {

        }


    }

    public static void getString(String Url, RequestQueue mQueue, final GetServiceData.VolleyCallback callback) {

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



    //-----------------Item------------------
    public class Spec_Item {

        String ComponentName;

        String ComponentContent;


        public Spec_Item(String ComponentName, String ComponentContent) {
            this.ComponentName = ComponentName;

            this.ComponentContent = ComponentContent;


        }

        public String GetComponentName() {
            return this.ComponentName;
        }

        public String GetComponentContent() {
            return this.ComponentContent;
        }

    }


    //----------------Adapter---------------
    public class SpecAdapter extends BaseAdapter {

        private LayoutInflater mLayInf;

        private List<Spec_Item> Spec_List;

        public SpecAdapter(Context context, List<Spec_Item> Spec_List) {
            mLayInf = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            this.Spec_List = Spec_List;
        }


        @Override
        public int getCount() {
            return Spec_List.size();
        }

        @Override
        public Object getItem(int position) {
            return Spec_List.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View v = mLayInf.inflate(dqa.com.msibook.R.layout.msibook_dqaweekly_spec_item, parent, false);


            TextView txt_Spec_ComponentName = (TextView) v.findViewById(dqa.com.msibook.R.id.txt_Spec_ComponentName);
            TextView txt_Spec_ComponentContent = (TextView) v.findViewById(dqa.com.msibook.R.id.txt_Spec_ComponentContent);
            //Img_ProjectImage.setImageResource(Integer.valueOf(Project_List.get(position).GetImage().toString()));
            txt_Spec_ComponentName.setText(Spec_List.get(position).GetComponentName());
            txt_Spec_ComponentContent.setText(Spec_List.get(position).GetComponentContent());

            return v;
        }

    }



}
