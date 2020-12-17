package dqa.com.msibook;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class msibook_mmc_detail extends AppCompatActivity {

    String AssetSN;
    String HPAssetSN;
    String Class;
    String BrandName;
    String TypeName;
    String CNAME;
    String CrDate;
    String EMail;
    String Description;
    String Vendor_PN;
    String PartNO;

    String getSearch;
    String gettxt_Edit;

    TextView txt_AssetSN;
    TextView txt_HPAssetSN;
    TextView txt_Class;
    TextView txt_BrandName;
    TextView txt_TypeName;
    TextView txt_CNAME;
    TextView txt_CrDate;
    TextView txt_EMail;
    TextView txt_Description;
    TextView txt_Vendor_PN;
    TextView txt_PartNO;

    private ProgressDialog progressBar;
    private Context mContext;

    private msibook_mmc_list_adapter MMC_List_Adapter;
    private List<msibook_mmc_list_item> MMC_Item_List = new ArrayList<msibook_mmc_list_item>();

    private ScrollView Scrollview;
    private ListView mListView;
    private TextView textView_title1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msibook_mmc_detail);

        //讀取時間Bar
        progressBar = new ProgressDialog(this);
        progressBar.setCancelable(true);
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.setMessage("資料載入中");

        txt_AssetSN = (TextView) findViewById(R.id.txt_AssetSN);
        txt_HPAssetSN = (TextView) findViewById(R.id.txt_HPAssetSN);
        txt_Class = (TextView) findViewById(R.id.txt_Class);
        txt_BrandName = (TextView) findViewById(R.id.txt_BrandName);
        txt_TypeName = (TextView) findViewById(R.id.txt_TypeName);
        txt_CNAME = (TextView) findViewById(R.id.txt_CNAME);
        txt_CrDate = (TextView) findViewById(R.id.txt_CrDate);
        txt_EMail = (TextView) findViewById(R.id.txt_EMail);
        txt_Description = (TextView) findViewById(R.id.txt_Description);
        txt_Vendor_PN = (TextView) findViewById(R.id.txt_Vendor_PN);
        txt_PartNO = (TextView) findViewById(R.id.txt_PartNO);

        mContext = msibook_mmc_detail.this;
        Scrollview = (ScrollView) findViewById(R.id.Scrollview);
        mListView = (ListView)findViewById(R.id.list_mmc_detail);
        textView_title1 = (TextView) findViewById(R.id.textView_title1);


        getSearch = getIntent().getStringExtra("Type");
        gettxt_Edit = getIntent().getStringExtra("txt_Edit");

        Log.w("Type + Edit",getSearch+" --- "+gettxt_Edit);

        if(Integer.valueOf(getSearch)==1){
            Log.w("用搜尋的","用搜尋的");
            Scrollview.setVisibility(View.GONE);
            textView_title1.setVisibility(View.VISIBLE);
            mListView.setVisibility(View.VISIBLE);
            Log.w("gettxt_Edit",String.valueOf(gettxt_Edit));
            Find_MMC_List_All(gettxt_Edit);
        }else{
            Log.w("用點的","用點的");
            Scrollview.setVisibility(View.VISIBLE);
            textView_title1.setVisibility(View.GONE);
            mListView.setVisibility(View.GONE);
        }

        AssetSN = getIntent().getStringExtra("AssetSN");
        HPAssetSN = getIntent().getStringExtra("HPAssetSN");
        Class = getIntent().getStringExtra("Class");
        BrandName = getIntent().getStringExtra("BrandName");
        TypeName = getIntent().getStringExtra("TypeName");
        CNAME = getIntent().getStringExtra("CNAME");
        CrDate = getIntent().getStringExtra("CrDate");
        EMail = getIntent().getStringExtra("EMail");
        Description = getIntent().getStringExtra("Description");
        Vendor_PN = getIntent().getStringExtra("Vendor_PN");
        PartNO = getIntent().getStringExtra("PartNO");



        txt_AssetSN.setText(AssetSN);
        txt_HPAssetSN.setText(HPAssetSN);
        txt_Class.setText(Class);
        txt_BrandName.setText(BrandName);
        txt_TypeName.setText(TypeName);
        txt_CNAME.setText(CNAME);
        txt_CrDate.setText(CrDate);
        txt_EMail.setText(EMail);
        txt_Description.setText(Description);
        txt_Vendor_PN.setText(Vendor_PN);
        txt_PartNO.setText(PartNO);

    }


    private void Find_MMC_List_All(String AssetSN) {

        //顯示  讀取等待時間Bar
        progressBar.show();

        MMC_Item_List.clear();

        RequestQueue mQueue = Volley.newRequestQueue(this);

        String Path = GetServiceData.ServicePath + "/Find_MMC_List_All?AssetSN=" + AssetSN;

        GetServiceData.getString(Path, mQueue, new GetServiceData.VolleyCallback() {
            @Override
            public void onSuccess(JSONObject result) {

                try {

                    JSONArray UserArray = new JSONArray(result.getString("Key"));

                    if (UserArray.length() > 0) {

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

                            MMC_Item_List.add(i, new msibook_mmc_list_item(AssetSN, HPAssetSN, Class, BrandName, TypeName, CNAME, CrDate, EMail, ExistKey, Description, Vendor_PN, PartNO));

                        }

                        //mOverhourAdapter = new OverhourAdapter(mContext,Overhour_Item_List,MainTitle);
                        MMC_List_Adapter = new msibook_mmc_list_adapter(mContext, MMC_Item_List);

                        mListView.setAdapter(MMC_List_Adapter);

                        //關閉-讀取等待時間Bar
                        progressBar.dismiss();
                    }else{
                        //關閉-讀取等待時間Bar
                        progressBar.dismiss();
                        //查無產邊代號
                        Dialog dialog=new android.support.v7.app.AlertDialog.Builder(mContext)
                                .setTitle("Search Result")//设置提示内容
                                .setMessage("無資料可顯示")//设置提示内容
                                //确定按钮
                                .setPositiveButton("確定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        finish();
                                    }
                                })
                                .create();//创建对话框
                        dialog.setCanceledOnTouchOutside( false ); //設置點擊屏幕Dialog不消失
                        dialog.show();//显示对话框

                    }
                }
                catch (JSONException ex) {

                    Log.w("Json",ex.toString());

                }

            }
        });


    }

    public class msibook_mmc_list_adapter extends BaseAdapter {

        private LayoutInflater mLayInf;

        private List<msibook_mmc_list_item> MMC_List_Item = new ArrayList<msibook_mmc_list_item>();

        private Context ProjectContext;


        public msibook_mmc_list_adapter(Context context, List<msibook_mmc_list_item> MMC_List_Item) {
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

            v = mLayInf.inflate(R.layout.activity_msibook_mms_search_item, parent, false);

            TextView txt_AssetSN = (TextView) v.findViewById(R.id.txt_AssetSN);
            TextView txt_HPAssetSN = (TextView) v.findViewById(R.id.txt_HPAssetSN);
            TextView txt_Class = (TextView) v.findViewById(R.id.txt_Class);
            TextView txt_BrandName = (TextView) v.findViewById(R.id.txt_BrandName);
            TextView txt_TypeName = (TextView) v.findViewById(R.id.txt_TypeName);
            TextView txt_CNAME = (TextView) v.findViewById(R.id.txt_CNAME);
            TextView txt_CrDate = (TextView) v.findViewById(R.id.txt_CrDate);
            TextView txt_EMail = (TextView) v.findViewById(R.id.txt_EMail);
            TextView txt_Description = (TextView) v.findViewById(R.id.txt_Description);
            TextView txt_Vendor_PN = (TextView) v.findViewById(R.id.txt_Vendor_PN);
            TextView txt_PartNO = (TextView) v.findViewById(R.id.txt_PartNO);

            txt_AssetSN.setText(MMC_List_Item.get(position).GetAssetSN());
            txt_HPAssetSN.setText(MMC_List_Item.get(position).GetHPAssetSN());
            txt_Class.setText(MMC_List_Item.get(position).GetClass());
            txt_BrandName.setText(MMC_List_Item.get(position).GetBrandName());
            txt_TypeName.setText(MMC_List_Item.get(position).GetTypeName());
            txt_CNAME.setText(MMC_List_Item.get(position).GetCNAME());
            txt_CrDate.setText(MMC_List_Item.get(position).GetCrDate());
            txt_EMail.setText(MMC_List_Item.get(position).GetEMail());
            txt_Description.setText(MMC_List_Item.get(position).GetDescription());
            txt_Vendor_PN.setText(MMC_List_Item.get(position).GetVendor_PN());
            txt_PartNO.setText(MMC_List_Item.get(position).GetPartNO());

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
