package dqa.com.msibook;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
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
import java.util.List;

public class msibook_mmc extends AppCompatActivity {

    private ListView mListView;

    private String getWeek;
    private String getYear;
    private String getWorkID;

    private msibook_mmc_list_adapter MMC_List_Adapter;
    private List<msibook_mmc.msibook_mmc_list_item> MMC_Item_List = new ArrayList<msibook_mmc.msibook_mmc_list_item>();

    private Context mContext;
    private ProgressDialog progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msibook_mmc);

        setTitle("MMC");

        //讀取時間Bar
        progressBar = new ProgressDialog(this);
        progressBar.setCancelable(true);
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.setMessage("資料載入中");

        mContext = msibook_mmc.this;
        mListView = (ListView) findViewById(R.id.Lsv_MMC_List);

        //Find_RandomRSS_List(UserData.WorkID);
        Find_MMC_List("10015667");
    }

    private void Find_MMC_List(String WorkID) {

        //顯示  讀取等待時間Bar
        progressBar.show();

        MMC_Item_List.clear();

        RequestQueue mQueue = Volley.newRequestQueue(this);

        String Path = GetServiceData.ServicePath + "/Find_MMC_List?WorkID=" + WorkID;

        GetServiceData.getString(Path, mQueue, new GetServiceData.VolleyCallback() {
            @Override
            public void onSuccess(JSONObject result) {

                try {

                    JSONArray UserArray = new JSONArray(result.getString("Key"));

                    for (int i = 0; i < UserArray.length(); i++) {

                        JSONObject IssueData = UserArray.getJSONObject(i);

                        String AssetSN = String.valueOf(IssueData.getString("AssetSN"));// 1

                        String HPAssetSN = String.valueOf(IssueData.getString("HPAssetSN"));// 1

                        String Class = String.valueOf(IssueData.getString("Class"));// 1

                        String BrandName = String.valueOf(IssueData.getString("BrandName"));// 1

                        String TypeName = String.valueOf(IssueData.getString("TypeName"));// 1

                        String CNAME = String.valueOf(IssueData.getString("CNAME"));// 1

                        String CrDate = String.valueOf(IssueData.getString("CrDate"));// 1

                        String EMail = String.valueOf(IssueData.getString("EMail"));// 1

                        String ExistKey = String.valueOf(IssueData.getString("ExistKey"));// 1

                        String Description = String.valueOf(IssueData.getString("Description"));// 1

                        String Vendor_PN = String.valueOf(IssueData.getString("Vendor_PN"));// 1

                        String PartNO = String.valueOf(IssueData.getString("PartNO"));// 1

                        MMC_Item_List.add(i,new msibook_mmc_list_item(AssetSN,HPAssetSN,Class,BrandName,TypeName,CNAME,CrDate,EMail,ExistKey,Description,Vendor_PN,PartNO));

                    }

                    mListView = (ListView)findViewById(R.id.Lsv_MMC_List);

                    //mOverhourAdapter = new OverhourAdapter(mContext,Overhour_Item_List,MainTitle);
                    MMC_List_Adapter = new msibook_mmc_list_adapter(mContext,MMC_Item_List);

                    mListView.setAdapter(MMC_List_Adapter);

                    //關閉-讀取等待時間Bar
                    progressBar.dismiss();

                    //專案細項to 第三頁
                    mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            Intent intent = new Intent();

                            intent.putExtra("AssetSN",MMC_Item_List.get(position).GetAssetSN());

                            intent.putExtra("HPAssetSN",MMC_Item_List.get(position).GetHPAssetSN());

                            intent.putExtra("Class",MMC_Item_List.get(position).GetClass());

                            intent.putExtra("BrandName",MMC_Item_List.get(position).GetBrandName());

                            intent.putExtra("TypeName",MMC_Item_List.get(position).GetTypeName());

                            intent.putExtra("CNAME",MMC_Item_List.get(position).GetCNAME());

                            intent.putExtra("CrDate",MMC_Item_List.get(position).GetCrDate());

                            intent.putExtra("EMail",MMC_Item_List.get(position).GetEMail());

                            intent.putExtra("ExistKey",MMC_Item_List.get(position).GetExistKey());

                            intent.putExtra("Description",MMC_Item_List.get(position).GetDescription());

                            intent.putExtra("Vendor_PN",MMC_Item_List.get(position).GetVendor_PN());

                            intent.putExtra("PartNO",MMC_Item_List.get(position).GetPartNO());

                            intent.setClass(msibook_mmc.this, msibook_mmc_detail.class);
                            //開啟Activity
                            startActivity(intent);


                        }
                    });
                }
                catch (JSONException ex) {

                    Log.w("Json",ex.toString());

                }

            }
        });


    }

    public class msibook_mmc_list_adapter extends BaseAdapter {

        private LayoutInflater mLayInf;

        private List<msibook_mmc.msibook_mmc_list_item> MMC_List_Item = new ArrayList<msibook_mmc.msibook_mmc_list_item>();

        private Context ProjectContext;


        public msibook_mmc_list_adapter(Context context, List<msibook_mmc.msibook_mmc_list_item> MMC_List_Item) {
            mLayInf = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            ProjectContext = context;

            this.MMC_List_Item = MMC_List_Item;

        }

        @Override
        public int getCount() {
            return MMC_List_Item.size();
        }

        @Override
        public Object getItem(int position) {
            return MMC_List_Item.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View v = new View(ProjectContext);

            v = mLayInf.inflate(R.layout.activity_msibook_mms_list_item, parent, false);


            TextView txt_Class = (TextView) v.findViewById(R.id.txt_Class);
            TextView txt_CreatDate = (TextView) v.findViewById(R.id.txt_CreatDate);
            TextView txt_Description = (TextView) v.findViewById(R.id.txt_Description);
            TextView txt_BrandName = (TextView) v.findViewById(R.id.txt_BrandName);

            TextView textView_next = (TextView) v.findViewById(R.id.textView_next);


            txt_Class.setText(MMC_List_Item.get(position).GetClass());
            txt_CreatDate.setText(MMC_List_Item.get(position).GetCrDate());
            txt_Description.setText(MMC_List_Item.get(position).GetDescription());
            txt_BrandName.setText(MMC_List_Item.get(position).GetBrandName());


            return v;
        }
    }

    public class msibook_mmc_list_item {

        String AssetSN;

        String HPAssetSN;

        String Class;

        String BrandName;

        String TypeName;

        String CNAME;

        String CrDate;

        String EMail;

        String ExistKey;

        String Description;

        String Vendor_PN;

        String PartNO;

        public msibook_mmc_list_item(String AssetSN,String HPAssetSN,String Class,String BrandName,String TypeName,String CNAME,String CrDate,String EMail,String ExistKey,String Description,String Vendor_PN,String PartNO)
        {
            this.AssetSN = AssetSN;

            this.HPAssetSN = HPAssetSN;

            this.Class = Class;

            this.BrandName = BrandName;

            this.TypeName = TypeName;

            this.CNAME = CNAME;

            this.CrDate = CrDate;

            this.EMail = EMail;

            this.ExistKey = ExistKey;

            this.Description = Description;

            this.Vendor_PN = Vendor_PN;

            this.PartNO = PartNO;


        }


        public String GetAssetSN()
        {
            return this.AssetSN;
        }

        public String GetHPAssetSN()
        {
            return this.HPAssetSN;
        }

        public String GetClass()
        {
            return this.Class;
        }

        public String GetBrandName()
        {
            return this.BrandName;
        }

        public String GetTypeName()
        {
            return this.TypeName;
        }

        public String GetCNAME()
        {
            return this.CNAME;
        }

        public String GetCrDate()
        {
            return this.CrDate;
        }

        public String GetEMail()
        {
            return this.EMail;
        }

        public String GetExistKey()
        {
            return this.ExistKey;
        }

        public String GetDescription()
        {
            return this.Description;
        }

        public String GetVendor_PN()
        {
            return this.Vendor_PN;
        }

        public String GetPartNO()
        {
            return this.PartNO;
        }

    }
}
