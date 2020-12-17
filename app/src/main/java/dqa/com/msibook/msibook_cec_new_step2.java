package dqa.com.msibook;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class msibook_cec_new_step2 extends AppCompatActivity {


    private Context mContext;

    private ProgressDialog progressBar;

    private TextView textView_model;
    private TextView textView_cec;
    private TextView textView_item;
    private String Column1;
    private String Get_Model_ID;

    private List<String> Get_Item = new ArrayList<String>();
    private List<String> Get_Bundle_Item = new ArrayList<String>();

    private List<String> Get_Temp_Item = new ArrayList<String>();
    private List<String> Get_Cer_Time_Item = new ArrayList<String>();
    private List<String> Get_Expense_Item = new ArrayList<String>();
    private List<String> Get_RWorkID_Item = new ArrayList<String>();

    private ListView mListView;
    private ApplyAdapter mApplyAdapter;
    private List<Apply_Item> Apply_Item_List = new ArrayList<Apply_Item>();

    private List<String> Save_Lab_Temp = new ArrayList<String>(); //項目 SeqNo
    private List<String> Save_Select_item = new ArrayList<String>(); //項目logo
    private List<String> Save_Cer_Time = new ArrayList<String>(); //項目 週
    private List<String> Save_Cer_Expense = new ArrayList<String>(); //項目 錢
    private List<String> Save_RWorkID = new ArrayList<String>(); //項目 負責工號

    private String SaveStr_Item;
    private String SaveStr_Temp;
    private String SaveStr_Temp_Info;


    private TextView textView_time;
    private TextView textView_cost;

    private TextView textView_cancel;
    private TextView textView_sent;

    private TextView textView_project_title;
    private TextView textView_choice_type;

    private TextView textView_totalTime;
    private TextView textView_totalUSD;

    private Double SaveTotaltime;
    private Double TotalUSD;

    private String ImagePath;

    private String Save_Select_CEC;

    //點選Local回傳狀態
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(data !=null) {
            Bundle bundle = data.getExtras();
            Integer CheckBooking = Integer.valueOf(bundle.getString("CEC_Application_Check"));
            Get_Bundle_Item = bundle.getStringArrayList("Item");
            if(CheckBooking ==1){
                Intent intent = new Intent();
                Bundle b = new Bundle();
                b.putString("CEC_Application_Check","1");
                b.putSerializable("Item", (Serializable) Get_Bundle_Item);
                intent.putExtras(b);
                setResult(RESULT_OK, intent);
                finish();
            }
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msibook_cec_new_step2);

        //讀取時間Bar
        progressBar = new ProgressDialog(this);
        progressBar.setCancelable(true);
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.setMessage("資料讀取中");

        mContext = msibook_cec_new_step2.this;

        textView_project_title = (TextView) findViewById(R.id.textView_project_title);
        textView_choice_type = (TextView) findViewById(R.id.textView_choice_type);
        textView_totalTime = (TextView) findViewById(R.id.textView_totalTime);
        textView_totalUSD = (TextView) findViewById(R.id.textView_totalUSD);

        textView_sent = (TextView) findViewById(R.id.textView_sent);

        SaveTotaltime = 0.0;
        TotalUSD = 0.0;

        //設定顯示座標位置
        Column1 = getIntent().getStringExtra("Column1"); //專案名稱
        Get_Model_ID = getIntent().getStringExtra("Model_ID");  //專案ID
        String F_Cer_Application = getIntent().getStringExtra("F_Cer_Application");// 驗證 名稱

        String Get_ApplicationTpye = getIntent().getStringExtra("Set_ApplicationTpye");// C  B  A

        switch (Get_ApplicationTpye) {
            case "C":
                Save_Select_CEC="Certification";
                break;
            case "B":
                Save_Select_CEC="Pretest";
                break;
            case "A":
                Save_Select_CEC="Submisiion";
                break;
        }

        textView_project_title.setText(Column1);
        textView_choice_type.setText(F_Cer_Application);

        Find_Certification_Apply(Get_ApplicationTpye);

        textView_sent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Save_Select_item.size() == 0){
                    showAlert("Error","尚未選擇細項");
                }else{
                    Intent msibook_cec_new_application = new Intent(msibook_cec_new_step2.this, msibook_cec_double_check.class);

                    msibook_cec_new_application.putExtra("Column1",Column1);

                    msibook_cec_new_application.putExtra("Model_ID",Get_Model_ID); //專案ID

                    msibook_cec_new_application.putExtra("F_Cer_Application",Save_Select_CEC);  //rg1.getChildAt(radioId)

                    msibook_cec_new_application.putStringArrayListExtra("Item", (ArrayList<String>) Save_Select_item);

                    msibook_cec_new_application.putStringArrayListExtra("Temp_Item", (ArrayList<String>) Save_Lab_Temp);

                    msibook_cec_new_application.putStringArrayListExtra("Cer_Time_Item", (ArrayList<String>) Save_Cer_Time);

                    msibook_cec_new_application.putStringArrayListExtra("Cer_Expense_Item", (ArrayList<String>) Save_Cer_Expense);

                    msibook_cec_new_application.putStringArrayListExtra("RWorkID_Item", (ArrayList<String>) Save_RWorkID);

                    msibook_cec_new_application.putExtra("F_Cer_Time",(String.valueOf(SaveTotaltime)));  //rg1.getChildAt(radioId)

                    msibook_cec_new_application.putExtra("F_Cer_Expense",String.valueOf(TotalUSD));  //rg1.getChildAt(radioId)

                    startActivityForResult(msibook_cec_new_application,1);
                }
            }
        });
    }

    private void showAlert(String title,String context)
    {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle(title);
        alert.setMessage(context);
        alert.show();
    }

    // 抓取 對硬 Radio 認證選項的 Item
    private void Find_Certification_Apply(String ApplicationTpye) {

        //顯示  讀取等待時間Bar
        progressBar.show();

        Apply_Item_List.clear();

        RequestQueue mQueue = Volley.newRequestQueue(this);

        //String Path = GetServiceData.ServicePath + "/Find_Certification_Apply?F_Keyin=" + ApplicationTpye;
        String Path = "http://wtsc.msi.com.tw/Test/MsiBook_App_Service.asmx/Find_Certification_Apply?ApplicationTpye=" + ApplicationTpye;

        GetServiceData.getString(Path, mQueue, new GetServiceData.VolleyCallback() {
            @Override
            public void onSuccess(JSONObject result) {

                try {

                    JSONArray UserArray = new JSONArray(result.getString("Key"));

                    for (int i = 0; i < UserArray.length(); i++) {
                        JSONObject IssueData = UserArray.getJSONObject(i);

                        String F_SeqNo = String.valueOf(IssueData.getInt("F_SeqNo")); //"F_SeqNo": 53,

                        String F_Cer_Class = String.valueOf(IssueData.getString("F_Cer_Class")); // "F_Cer_Class": "A",

                        String F_Cer_Logo = String.valueOf(IssueData.getString("F_Cer_Logo")); // "F_Cer_Logo": "ErP Lot 3",

                        String F_Cer_Time = String.valueOf(IssueData.getDouble("F_Cer_Time")); // "F_Cer_Time": 4.00,

                        String F_Cer_Expense = String.valueOf(IssueData.getDouble("F_Cer_Expense")); // "F_Cer_Expense": 1000.00,

                        String F_RWorkID = String.valueOf(IssueData.getString("F_RWorkID")); //"F_RWorkID": "10012947",

                        String F_Cer_Pic = String.valueOf(IssueData.getString("F_Cer_Pic")); //   "F_Cer_Pic": "10003130@2014-0703-0606-347545@10013133.bmp",

                        String F_Cer_Application = String.valueOf(IssueData.getString("F_Cer_Application")); // "F_Cer_Application": "0"

                        Apply_Item_List.add(i,new Apply_Item(F_SeqNo,F_Cer_Class,F_Cer_Logo,F_Cer_Time,F_Cer_Expense,F_RWorkID,F_Cer_Pic,F_Cer_Application));

                    }

                    mListView = (ListView)findViewById(R.id.list_apply);
                    mListView.setDivider(null);
                    mListView.setDividerHeight(0);

                    mApplyAdapter = new ApplyAdapter(mContext,Apply_Item_List);

                    mListView.setAdapter(mApplyAdapter);

                    //關閉-讀取等待時間Bar
                    progressBar.dismiss();

                    //編制細項to 第三頁
                    mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

//                            Intent intent = new Intent();
//
//                            intent.setClass(msibook_dqaweekly_main_page2.this, msibook_dqaweekly_main_page3.class);
//                            //開啟Activity
//                            startActivity(intent);


                        }
                    });
                }
                catch (JSONException ex) {

                    Log.w("Json",ex.toString());
                }

            }
        });


    }
    //ViewHolder静态类
    static class ViewHolder
    {
        public LinearLayout linear_apply;
        public CheckBox textView_checkbox;
        public ImageView imageView_F_Cer_Pic;
        public TextView textView_logo;
        public TextView textView_total_info;
    }

    //ApplyAdapter
    public class ApplyAdapter extends BaseAdapter {

        private LayoutInflater mLayInf;

        private List<Apply_Item> Apply_Item_List;

        private Context ProjectContext;

        private String Title;

        private boolean[] checks; //用于保存checkBox的选择状态

        public ApplyAdapter(Context context,List<Apply_Item> Apply_Item_List)
        {
            mLayInf = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            ProjectContext = context;

            this.Apply_Item_List = Apply_Item_List;

            checks = new boolean[Apply_Item_List.size()];

        }
        @Override
        public int getCount() {
            return Apply_Item_List.size();
        }

        @Override
        public Object getItem(int position) {
            return Apply_Item_List.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        public class textView_checkbox{public boolean checked;};
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            ViewHolder holder = null;
            //如果缓存convertView为空，则需要创建View
            if(convertView == null)
            {
                holder = new ViewHolder();
                //根据自定义的Item布局加载布局
                convertView = mLayInf.inflate(R.layout.msibook_cec_apply_item_adapter, null);
                holder.linear_apply = (LinearLayout)convertView.findViewById(R.id.linear_apply);
                holder.textView_checkbox = (CheckBox)convertView.findViewById(R.id.textView_checkbox);
                holder.imageView_F_Cer_Pic = (ImageView)convertView.findViewById(R.id.imageView_F_Cer_Pic);
                holder.textView_logo = (TextView)convertView.findViewById(R.id.textView_logo);
                holder.textView_total_info = (TextView)convertView.findViewById(R.id.textView_total_info);

                holder.textView_logo.setText(Apply_Item_List.get(position).GetF_Cer_Logo());
                holder.textView_total_info.setText(Apply_Item_List.get(position).GetF_Cer_Time()+" 週"+ "    " +"USD "+Apply_Item_List.get(position).GetF_Cer_Expense());

                ImagePath = Apply_Item_List.get(position).GetF_Cer_Pic().replace("http://wtsc.msi.com.tw/IMS/fileservers","http://wtsc.msi.com.tw/IMS/fileserver");
                final ViewHolder finalHolder = holder;
                Glide
                        .with(mContext)
                        .load(ImagePath)
                        .asBitmap()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .placeholder(R.mipmap.pms_img_pms_no_pic)
                        .into(new SimpleTarget<Bitmap>(100, 100) {
                            @Override
                            public void onResourceReady(Bitmap resource, GlideAnimation glideAnimation) {

                                finalHolder.imageView_F_Cer_Pic.setImageBitmap(resource);

                                //顯示  讀取等待時間Bar1080
                                progressBar.dismiss();
                            }

                            @Override
                            public void onLoadFailed(Exception e, Drawable errorDrawable) {
//                            Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_SHORT).show();
                                //顯示  讀取等待時間Bar
                                progressBar.dismiss();
                            }
                        });

                //将设置好的布局保存到缓存中，并将其设置在Tag里，以便后面方便取出Tag
                convertView.setTag(holder);
            }else
            {
                holder = (ViewHolder)convertView.getTag();

                holder.textView_logo.setText(Apply_Item_List.get(position).GetF_Cer_Logo());
                holder.textView_total_info.setText(Apply_Item_List.get(position).GetF_Cer_Time()+" 週"+ "    " +"USD:"+Apply_Item_List.get(position).GetF_Cer_Expense());

                ImagePath = Apply_Item_List.get(position).GetF_Cer_Pic().replace("http://wtsc.msi.com.tw/IMS/fileservers","http://wtsc.msi.com.tw/IMS/fileserver");
                final ViewHolder finalHolder = holder;
                Glide
                        .with(mContext)
                        .load(ImagePath)
                        .asBitmap()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .placeholder(R.mipmap.pms_img_pms_no_pic)
                        .into(new SimpleTarget<Bitmap>(100, 100) {
                            @Override
                            public void onResourceReady(Bitmap resource, GlideAnimation glideAnimation) {

                                finalHolder.imageView_F_Cer_Pic.setImageBitmap(resource);

                                //顯示  讀取等待時間Bar1080
                                progressBar.dismiss();
                            }

                            @Override
                            public void onLoadFailed(Exception e, Drawable errorDrawable) {
//                            Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_SHORT).show();
                                //顯示  讀取等待時間Bar
                                progressBar.dismiss();
                            }
                        });
            }
            final int pos  = position; //pos必须声明为final

            final ViewHolder finalHolder1 = holder;
            holder.linear_apply.setOnClickListener(new View.OnClickListener() { //主體點擊時 做判斷
                @Override
                public void onClick(View v) {
                    if(finalHolder1.textView_checkbox.isChecked() != true) //打勾 累加
                    {
                        finalHolder1.textView_checkbox.setChecked(true);
                        //textView_checkbox.setBackgroundResource(R.mipmap.facility_ic_area_check);
                        //運用DecimalFormat制定好金額顯示格式與小數點位數
                        DecimalFormat mDecimalFormat = new DecimalFormat("#,###.##");

                        SaveTotaltime += Double.valueOf(Apply_Item_List.get(position).GetF_Cer_Time());
                        Integer Int_totaltime = SaveTotaltime.intValue();

                        TotalUSD += Double.valueOf(Apply_Item_List.get(position).GetF_Cer_Expense());
                        Integer Int_USD  = TotalUSD.intValue();
                        String str__USD = mDecimalFormat.format(Double.parseDouble(String.valueOf(TotalUSD)));

                        textView_totalTime.setText(String.valueOf(Int_totaltime)+" 週");
                        textView_totalUSD.setText(String.valueOf(str__USD));

                        Save_Select_item.add(Apply_Item_List.get(position).GetF_Cer_Logo());// 陣列加入 ---認證項目名稱
                        Save_Lab_Temp.add(Apply_Item_List.get(position).GetF_SeqNo()); // 陣列加入 ---認證代碼
                        Save_Cer_Time.add(Apply_Item_List.get(position).GetF_Cer_Time()); // 陣列加入    ---週次
                        Save_Cer_Expense.add(Apply_Item_List.get(position).GetF_Cer_Expense());// 陣列加入    ---美金
                        Save_RWorkID.add(Apply_Item_List.get(position).GetF_RWorkID());// 陣列加入    ---負責人工號

                        Log.w("Add_Item",Apply_Item_List.get(position).GetF_Cer_Logo());
                        Log.w("Add_Lab_Temp",Apply_Item_List.get(position).GetF_SeqNo());
                        Log.w("Add_totalTiem",Apply_Item_List.get(position).GetF_Cer_Time());
                        Log.w("Add_USD",Apply_Item_List.get(position).GetF_Cer_Expense());
                        Log.w("Add_WorkID",Apply_Item_List.get(position).GetF_RWorkID());

                    }
                    else //勾勾拿掉  去除
                    {
                        finalHolder1.textView_checkbox.setChecked(false);

                        //運用DecimalFormat制定好金額顯示格式與小數點位數
                        DecimalFormat mDecimalFormat = new DecimalFormat("#,###.##");

                        SaveTotaltime -= Double.valueOf(Apply_Item_List.get(position).GetF_Cer_Time());
                        Integer Int_totaltime = SaveTotaltime.intValue();

                        TotalUSD -= Double.valueOf(Apply_Item_List.get(position).GetF_Cer_Expense());
                        Integer Int_USD  = TotalUSD.intValue();
                        String str__USD = mDecimalFormat.format(Double.parseDouble(String.valueOf(TotalUSD)));

                        textView_totalTime.setText(String.valueOf(Int_totaltime)+" 週");
                        textView_totalUSD.setText(String.valueOf(str__USD));

                        int Remove_mark;
                        for (int i = 0; i < Save_Select_item.size(); i++) {
                            if(Save_Select_item.get(i).contains(Apply_Item_List.get(position).GetF_Cer_Logo())){  //移除  項目
                                Save_Select_item.remove(i);

                                Save_Lab_Temp.remove(i);
                                Save_Cer_Time.remove(i);
                                Save_Cer_Expense.remove(i);
                                Save_RWorkID.remove(i);
                                //Log.w("RRRRE_Item",String.valueOf(Save_Select_item.get(i)));
                            }
                        }
//                        for (int i = 0; i < Save_Lab_Temp.size(); i++) {
//                            if(Save_Lab_Temp.get(i).contains(Apply_Item_List.get(position).GetF_SeqNo())){       //移除  驗證 代碼
//                                Save_Lab_Temp.remove(i);
//                                Log.w("RRRRE_Lab_Temp",String.valueOf(Save_Lab_Temp.get(i)));
//                            }
//                        }
//                        for (int i = 0; i < Save_Cer_Time.size(); i++) {
//                            if(Save_Cer_Time.get(i).contains(Apply_Item_List.get(position).GetF_Cer_Time())){        //移除 週次
//                                Save_Cer_Time.remove(i);
//                                Log.w("RRRRE_Cer_Time",String.valueOf(Save_Cer_Time.get(i)));
//                            }
//                        }
//                        for (int i = 0; i < Save_Cer_Expense.size(); i++) {
//                            if(Save_Cer_Expense.get(i).contains(Apply_Item_List.get(position).GetF_Cer_Expense())){ //移除 金額
//                                Save_Cer_Expense.remove(i);
//                                Log.w("RRRRE_Cer_Expense",String.valueOf(Save_Cer_Expense.get(i)));
//                            }
//                        }
//                        for (int i = 0; i < Save_RWorkID.size(); i++) {
//                            if(Save_RWorkID.get(i).contains(Apply_Item_List.get(position).GetF_RWorkID())){  //移除  工號
//                                Save_RWorkID.remove(i);
//                                Log.w("RRRRE_RWorkID",String.valueOf(Save_RWorkID.get(i)));
//                            }
//                        }


                    }
                }
            });

            holder.textView_checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
                @Override
                public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                    checks[pos] = isChecked;
                }});
            holder.textView_checkbox.setChecked(checks[pos]);




            return convertView;
//--------------------------------------------------------------------
        }

    }


    //--------------------------------------------------------------------------------Item--------------------------------------------------------------------------------
    //Detail_Item
    public class Apply_Item {

        String F_SeqNo;

        String F_Cer_Class;

        String F_Cer_Logo;

        String F_Cer_Time;

        String F_Cer_Expense;

        String F_RWorkID;

        String F_Cer_Pic;

        String F_Cer_Application;

        //boolean F_Checks;

        public Apply_Item(String F_SeqNo,String F_Cer_Class,String F_Cer_Logo,String F_Cer_Time,String F_Cer_Expense,String F_RWorkID,String F_Cer_Pic,String F_Cer_Application)
        {
            this.F_SeqNo = F_SeqNo;

            this.F_Cer_Class = F_Cer_Class;

            this.F_Cer_Logo = F_Cer_Logo;

            this.F_Cer_Time = F_Cer_Time;

            this.F_Cer_Expense = F_Cer_Expense;

            this.F_RWorkID = F_RWorkID;

            this.F_Cer_Pic = F_Cer_Pic;

            this.F_Cer_Application = F_Cer_Application;

            //this.F_Checks = F_Checks;
        }


        public String GetF_SeqNo()
        {
            return this.F_SeqNo;
        }

        public String GetF_Cer_Class()
        {
            return this.F_Cer_Class;
        }

        public String GetF_Cer_Logo()
        {
            return this.F_Cer_Logo;
        }

        public String GetF_Cer_Time()
        {
            return this.F_Cer_Time;
        }

        public String GetF_Cer_Expense()
        {
            return this.F_Cer_Expense;
        }

        public String GetF_RWorkID()
        {
            return this.F_RWorkID;
        }

        public String GetF_Cer_Pic()
        {
            return this.F_Cer_Pic;
        }

        public String GetF_Cer_Application()
        {
            return this.F_Cer_Application;
        }

//        public Boolean GetF_Checks()
//        {
//            return this.F_Checks;
//        }

    }



}
