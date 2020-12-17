package dqa.com.msibook;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class msibook_cec_select_model extends AppCompatActivity {

    private ListView list_mmc_class;

    private ListView mListView;

    private msibook_mmc_class_list_adapter MMC_Class_List_Adapter;
    private List<msibook_mmc_class_list_item> MMC_Class_List = new ArrayList<msibook_mmc_class_list_item>();

    private List<String> Get_ModelID_Item = new ArrayList<String>();
    private List<String> Get_Model_Item = new ArrayList<String>();

    private Context mContext;
    private ProgressDialog progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msibook_cec_select_model);

        //讀取時間Bar
        progressBar = new ProgressDialog(this);
        progressBar.setCancelable(true);
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.setMessage("資料載入中");

        mContext = msibook_cec_select_model.this;

        Get_ModelID_Item = getIntent().getStringArrayListExtra("ModelID_Item");
        Get_Model_Item = getIntent().getStringArrayListExtra("Model_Item");
        mListView = (ListView)findViewById(R.id.list_mmc_class);

        MMC_Class_List.clear();
        for (int i = 0; i < Get_ModelID_Item.size(); i++) {
            MMC_Class_List.add(i,new msibook_mmc_class_list_item(Get_ModelID_Item.get(i),Get_Model_Item.get(i)));
        }


        MMC_Class_List_Adapter = new msibook_mmc_class_list_adapter(mContext,MMC_Class_List);

        mListView.setAdapter(MMC_Class_List_Adapter);
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

            txt_Class.setText(MMC_List_Class_Item.get(position).GetModel());

            txt_Class.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putString("Choice_ModelID",MMC_List_Class_Item.get(position).GetModelID());
                    bundle.putString("Choice_Model",MMC_List_Class_Item.get(position).GetModel());
                    Log.w("Choice_Model",MMC_List_Class_Item.get(position).GetModel());
                    msibook_cec_select_model.this.setResult(RESULT_OK,msibook_cec_select_model.this.getIntent().putExtras(bundle));
                    finish();
                }
            });


            return v;
        }
    }

    public class msibook_mmc_class_list_item {

        String ModelID;
        String Model;

        public msibook_mmc_class_list_item(String ModelID,String Model)
        {
            this.ModelID = ModelID;
            this.Model = Model;
        }

        public String GetModelID()
        {
            return this.ModelID;
        }

        public String GetModel()
        {
            return this.Model;
        }


    }


}
