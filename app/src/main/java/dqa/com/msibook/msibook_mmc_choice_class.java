package dqa.com.msibook;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class msibook_mmc_choice_class extends AppCompatActivity {

    private ListView list_mmc_class;

    private ListView mListView;

    private msibook_mmc_class_list_adapter MMC_Class_List_Adapter;
    private List<msibook_mmc_class_list_item> MMC_Class_List = new ArrayList<msibook_mmc_class_list_item>();

    private Context mContext;
    private ProgressDialog progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msibook_mmc_choice_class);


        //讀取時間Bar
        progressBar = new ProgressDialog(this);
        progressBar.setCancelable(true);
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.setMessage("資料載入中");

        mContext = msibook_mmc_choice_class.this;

        Find_MMC_Class(UserData.WorkID);
    }

    private void Find_MMC_Class(String WorkID) {

        //顯示  讀取等待時間Bar
        progressBar.show();

        MMC_Class_List.clear();

        RequestQueue mQueue = Volley.newRequestQueue(this);

        String Path = GetServiceData.ServicePath + "/Find_MMC_Class?WorkID=" + WorkID;

        GetServiceData.getString(Path, mQueue, new GetServiceData.VolleyCallback() {
            @Override
            public void onSuccess(JSONObject result) {

                try {

                    JSONArray UserArray = new JSONArray(result.getString("Key"));

                    for (int i = 0; i < UserArray.length(); i++) {

                        JSONObject IssueData = UserArray.getJSONObject(i);

                        String Class = String.valueOf(IssueData.getString("Class"));// 1

                        MMC_Class_List.add(i,new msibook_mmc_class_list_item(Class));

                    }
                    MMC_Class_List.add(MMC_Class_List.size(),new msibook_mmc_class_list_item("All"));

                    mListView = (ListView)findViewById(R.id.list_mmc_class);

                    Collections.reverse(MMC_Class_List); //這句就是倒序

                    //mOverhourAdapter = new OverhourAdapter(mContext,Overhour_Item_List,MainTitle);
                    MMC_Class_List_Adapter = new msibook_mmc_class_list_adapter(mContext,MMC_Class_List);

                    mListView.setAdapter(MMC_Class_List_Adapter);

                    //關閉-讀取等待時間Bar
                    progressBar.dismiss();

                }
                catch (JSONException ex) {

                    Log.w("Json",ex.toString());

                }

            }
        });

    }

    public class msibook_mmc_class_list_adapter extends BaseAdapter {

        private LayoutInflater mLayInf;

        private List<msibook_mmc_class_list_item> MMC_List_Class_Item = new ArrayList<msibook_mmc_class_list_item>();

        private Context ProjectContext;


        public msibook_mmc_class_list_adapter(Context context, List<msibook_mmc_class_list_item> MMC_List_Class_Item) {
            mLayInf = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            ProjectContext = context;

            this.MMC_List_Class_Item = MMC_List_Class_Item;

        }

        @Override
        public int getCount() {
            return MMC_List_Class_Item.size();
        }

        @Override
        public Object getItem(int position) {
            return MMC_List_Class_Item.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            View v = new View(ProjectContext);

            v = mLayInf.inflate(R.layout.activity_msibook_mms_list_class_item, parent, false);


            TextView txt_Class = (TextView) v.findViewById(R.id.txt_Class);

            txt_Class.setText(MMC_List_Class_Item.get(position).GetClass());

            txt_Class.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putString("Choice_Class",MMC_List_Class_Item.get(position).GetClass());
                    Log.w("Choice_Class",MMC_List_Class_Item.get(position).GetClass());
                    msibook_mmc_choice_class.this.setResult(RESULT_OK,msibook_mmc_choice_class.this.getIntent().putExtras(bundle));
                    finish();
                }
            });


            return v;
        }
    }

    public class msibook_mmc_class_list_item {

        String Class;

        public msibook_mmc_class_list_item(String Class)
        {
            this.Class = Class;
        }

        public String GetClass()
        {
            return this.Class;
        }

    }




}
