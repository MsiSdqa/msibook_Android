package dqa.com.msibook;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.vision.text.Line;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class msibook_mmc extends AppCompatActivity {

    private ListView mListView;

    private String getWeek;
    private String getYear;
    private String getWorkID;

    private msibook_mmc_list_adapter MMC_List_Adapter;
    private List<msibook_mmc_list_item> MMC_Item_List = new ArrayList<msibook_mmc.msibook_mmc_list_item>();

    private List<msibook_mmc_list_item> MMC_Item_List_Adapter_Item_List = new ArrayList<msibook_mmc_list_item>();
    private RecyclerView recyclerView;
    private RecyclerView.Adapter recyclerView_detail_Adapter;
    private RecyclerView.LayoutManager recyclerViewLayoutManager_detail;

    private Context mContext;
    private ProgressDialog progressBar;

    private LinearLayout linear_filter;

    private EditText edit_number;

    private TextView txt_barcode;

    private TextView txt_filter;

    private IntentIntegrator scanIntegrator;

    //點選Local回傳狀態
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

//        if(data !=null) { //0310舊版本
//            Bundle bundle = data.getExtras();
//            String Choice_Class = bundle.getString("Choice_Class");
//            Log.w("回傳選到的Class",Choice_Class);
//            Find_MMC_List(UserData.WorkID,Choice_Class);
//            txt_filter.setText(Choice_Class);
//        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msibook_mmc);

        setTitle("MMC");

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        //讀取時間Bar
        progressBar = new ProgressDialog(this);
        progressBar.setCancelable(true);
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.setMessage("資料載入中");

        mContext = msibook_mmc.this;

        recyclerView = (RecyclerView) findViewById(R.id.Recycle_MMC);

        //mListView = (ListView) findViewById(R.id.Lsv_MMC_List); // 0310改版暫時隱藏

        linear_filter = (LinearLayout) findViewById(R.id.linear_filter);
        txt_filter = (TextView) findViewById(R.id.txt_filter);

        txt_barcode = (TextView) findViewById(R.id.txt_barcode);

        edit_number = (EditText) findViewById(R.id.edit_number);

        edit_number.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    // Perform action on key press
                    String setEdit_text= String.valueOf(edit_number.getText());
                    Intent intent = new Intent();

                    intent.putExtra("Type","1");

                    intent.putExtra("txt_Edit",setEdit_text);
                    Log.w("gettxt_Edit",String.valueOf(edit_number.getText()));

                    intent.setClass(msibook_mmc.this, msibook_mmc_detail.class);
                    //開啟Activity
                    startActivity(intent);
                    return true;
                }
                return false;
            }
        });

        txt_barcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(msibook_mmc.this, msibook_mmc_barcode_search.class);


                startActivityForResult(intent,1);
            }
        });


        linear_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(msibook_mmc.this, msibook_mmc_choice_class.class);

                //intent.putExtra("Now_DeptID", String.valueOf(Now_DeptID));//代部門名稱到下一頁

                startActivityForResult(intent,1);
            }
        });

        //Find_RandomRSS_List(UserData.WorkID);
        //Find_MMC_List(UserData.WorkID,"All");  //0310舊版本

        Find_MMC_List_ReMake(UserData.WorkID);

    }

    private void Find_MMC_List_ReMake(String WorkID) {

        //顯示  讀取等待時間Bar
        progressBar.show();

        MMC_Item_List_Adapter_Item_List.clear();

        RequestQueue mQueue = Volley.newRequestQueue(this);

        String Path = GetServiceData.ServicePath + "/Find_MMC_List?WorkID=" + WorkID;

        GetServiceData.getString(Path, mQueue, new GetServiceData.VolleyCallback() {
            @Override
            public void onSuccess(JSONObject result) {

                try {

                    JSONArray UserArray = new JSONArray(result.getString("Key"));


                    final Map<String,ArrayList<msibook_mmc_list_item>> mapContent = new HashMap<String,ArrayList<msibook_mmc_list_item>>();

                    List<msibook_mmc_list_item> fatherList = new ArrayList<>();
                    List<List<msibook_mmc_list_item>> childList = new ArrayList<>();
                    Map<String, ArrayList<msibook_mmc_list_item>> linkHashMap = new LinkedHashMap<>();
                    List<String> Child_CountList = new ArrayList<>();


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

                        if (!linkHashMap.containsKey(Class)) {  //加入表內
                            ArrayList<msibook_mmc_list_item> innchildList = new ArrayList<>();
                            innchildList.add(new msibook_mmc_list_item(AssetSN,HPAssetSN,Class,BrandName,TypeName,CNAME,CrDate,EMail,ExistKey,Description,Vendor_PN,PartNO));
                            linkHashMap.put(Class, innchildList);
                        } else {
                            linkHashMap.get(Class).add(new msibook_mmc_list_item(AssetSN,HPAssetSN,Class,BrandName,TypeName,CNAME,CrDate,EMail,ExistKey,Description,Vendor_PN,PartNO));
                        }

                        //MMC_Item_List_Adapter_Item_List.add(i,new msibook_mmc_list_item(AssetSN,HPAssetSN,Class,BrandName,TypeName,CNAME,CrDate,EMail,ExistKey,Description,Vendor_PN,PartNO));

                    }

                    int index = 0;
                    for (Map.Entry<String, ArrayList<msibook_mmc_list_item>>  item  : linkHashMap.entrySet()){
                        fatherList.add(index  , item.getValue().get(0)) ;
                        ArrayList<msibook_mmc_list_item>  tempList = new ArrayList<>();
                        for (msibook_mmc_list_item childItem : item.getValue()){
                            tempList.add(childItem);
                            Log.w("175行index",String.valueOf(index));
                        }
                        childList.add(index, tempList);
                        Log.w("entrySet長度",String.valueOf(tempList.size()));
                        Log.w("179行index",String.valueOf(index));
                        index++;
                    }

                    for (int i = 0; i < fatherList.size(); i++) {
                        Log.w("顯示爸爸種類",fatherList.get(i).Class);
                    }

                    for (int i = 0; i < childList.size(); i++) {
                        Log.w("顯示兒子數量",String.valueOf(childList.get(i).size()));
                        Child_CountList.add(i,String.valueOf(childList.get(i).size())) ;
                    }


                    RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
                    recyclerView.setLayoutManager(mLayoutManager);
                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                    //recyclerView.setAdapter(mAdapter);

//                    //LinearLayoutManager recyclerViewLayoutManager= new LinearLayoutManager(mContext);
//                    recyclerViewLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
//                    recyclerView.setLayoutManager(recyclerViewLayoutManager);

                    recyclerView_detail_Adapter = new RecyclerView_Detail_Adapter(mContext, fatherList,Child_CountList);

                    recyclerView.setAdapter(recyclerView_detail_Adapter);

                    recyclerView_detail_Adapter.notifyDataSetChanged();


                    //關閉-讀取等待時間Bar
                    progressBar.dismiss();


                }
                catch (JSONException ex) {

                    Log.w("Json",ex.toString());

                }

            }
        });


    }

    private void Find_MMC_List(String WorkID,String Type) {

        //顯示  讀取等待時間Bar
        progressBar.show();

        MMC_Item_List.clear();

        final String CheckType=Type;

        final int[] j = {0};

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

                        if(CheckType.contains("All")){
                            MMC_Item_List.add(i,new msibook_mmc_list_item(AssetSN,HPAssetSN,Class,BrandName,TypeName,CNAME,CrDate,EMail,ExistKey,Description,Vendor_PN,PartNO));
                        }else{
                            if(Class.contains(CheckType)){
                                MMC_Item_List.add(j[0],new msibook_mmc_list_item(AssetSN,HPAssetSN,Class,BrandName,TypeName,CNAME,CrDate,EMail,ExistKey,Description,Vendor_PN,PartNO));
                                j[0]++;
                            }
                        }
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

                            intent.putExtra("Type","0");

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
            TextView txt_AssetSN = (TextView) v.findViewById(R.id.txt_AssetSN);

            TextView textView_next = (TextView) v.findViewById(R.id.textView_next);


            txt_Class.setText(MMC_List_Item.get(position).GetClass());
            txt_CreatDate.setText(MMC_List_Item.get(position).GetCrDate());
            txt_Description.setText(MMC_List_Item.get(position).GetDescription());
            txt_BrandName.setText(MMC_List_Item.get(position).GetBrandName());
            txt_AssetSN.setText(MMC_List_Item.get(position).GetAssetSN());


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

    //2. BU細項
    public class RecyclerView_Detail_Adapter extends RecyclerView.Adapter<RecyclerView_Detail_Adapter.ViewHolder>{

        List<msibook_mmc_list_item> values_father;
        List<String> values_child;
        Context context1;
        Integer row_index=-1;

        public RecyclerView_Detail_Adapter(Context context2, List<msibook_mmc_list_item> values1, List<String> values2){

            values_father = values1;

            values_child = values2;

            context1 = context2;

        }

        public class ViewHolder extends RecyclerView.ViewHolder{

            public RelativeLayout real_main;

            public LinearLayout linear_item_main;

            public TextView textView_Class;

            public TextView textView_Count;

            public ViewHolder(View v){

                super(v);

                linear_item_main = (LinearLayout) v.findViewById(R.id.linear_item_main);

                textView_Class = (TextView) v.findViewById(R.id.textView_Class);

                textView_Count = (TextView) v.findViewById(R.id.textView_Count);

            }
        }

        @Override
        public RecyclerView_Detail_Adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){

            View view1 = LayoutInflater.from(context1).inflate(R.layout.msibook_mmc_recycle_item,parent,false);

            RecyclerView_Detail_Adapter.ViewHolder viewHolder1 = new RecyclerView_Detail_Adapter.ViewHolder(view1);

            return viewHolder1;
        }

        @Override
        public void onBindViewHolder(final RecyclerView_Detail_Adapter.ViewHolder Vholder, final int position){

            Vholder.textView_Class.setText(values_father.get(position).GetClass());

            Vholder.textView_Count.setText(values_child.get(position));

            Vholder.linear_item_main.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    row_index=position;
                    notifyDataSetChanged();

                    Intent intent = new Intent();

                    intent.putExtra("Type","0");

                    intent.putExtra("Class",values_father.get(position).GetClass());

                    intent.setClass(msibook_mmc.this, msibook_mmc_single_class.class);

                    //intent.setClass(msibook_mmc.this, msibook_mmc_detail.class);
                    //開啟Activity
                    startActivity(intent);
                }
            });

        }
        @Override
        public int getItemCount(){
            return values_father.size();
        }
    }




}
