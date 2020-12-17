package dqa.com.msibook;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
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

import java.io.IOException;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class msibook_cec_add_step1 extends AppCompatActivity {

    private Context mContext;

    private ProgressDialog progressBar;


    private List<String> Get_Item = new ArrayList<String>();
    private List<String> Get_Bundle_Item = new ArrayList<String>();

    private List<String> Get_Temp_Item = new ArrayList<String>();
    private List<String> Get_Cer_Time_Item = new ArrayList<String>();
    private List<String> Get_Expense_Item = new ArrayList<String>();
    private List<String> Get_RWorkID_Item = new ArrayList<String>();

    //2020-01-15改分類
    private RecyclerView recyclerView;
    private RecyclerView.Adapter recyclerView_Adapter;
    private RecyclerView.LayoutManager recyclerViewLayoutManager;

    private List<Apply_Item> Apply_Item_List = new ArrayList<Apply_Item>();

    private List<String> Save_Lab_Temp = new ArrayList<String>(); //項目 SeqNo
    private List<String> Save_Select_item = new ArrayList<String>(); //項目logo
    private List<String> Save_Cer_Time = new ArrayList<String>(); //項目 週
    private List<String> Save_Cer_Expense = new ArrayList<String>(); //項目 錢
    private List<String> Save_RWorkID = new ArrayList<String>(); //項目 負責工號
    private List<String> Save_Cer_Pic = new ArrayList<String>(); //項目 負責工號

    private TextView textView_totalTime;
    private TextView textView_totalUSD;
    private TextView textView_TotalCount;

    private Double SaveTotaltime;
    private Double TotalUSD;
    private Integer TotalCount;

    private String ImagePath;

    private TextView textView_next;

    //點選Local回傳狀態
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(data !=null) {
            Bundle bundle = data.getExtras();
            Integer CheckApplication = Integer.valueOf(bundle.getString("CEC_Application_Check"));
            if(CheckApplication ==1){
                Intent intent = new Intent();
                Bundle b = new Bundle();
                b.putString("CEC_Application_Check","1");
                intent.putExtras(b);
                setResult(RESULT_OK, intent);
                finish();
            }
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msibook_cec_add_step1);

        //讀取時間Bar
        progressBar = new ProgressDialog(this);
        progressBar.setCancelable(true);
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.setMessage("資料讀取中");

        mContext = msibook_cec_add_step1.this;

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view1);
        recyclerView.setItemViewCacheSize(500);
        //textView_totalTime = (TextView) findViewById(R.id.textView_totalTime);
        textView_totalUSD = (TextView) findViewById(R.id.textView_totalUSD);
        textView_TotalCount = (TextView) findViewById(R.id.textView_TotalCount);

        textView_next = (TextView) findViewById(R.id.textView_next);

        SaveTotaltime = 0.0;
        TotalUSD = 0.0;
        TotalCount = 0;

        Find_New_Certification();

        textView_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Save_Select_item.size() == 0){
                    showAlert("Error","尚未選擇項目");
                }else{
                    Intent msibook_cec_new_application = new Intent(msibook_cec_add_step1.this, msibook_cec_double_check_new.class);

//                    msibook_cec_new_application.putExtra("Column1",Column1);
//
//                    msibook_cec_new_application.putExtra("Model_ID",Get_Model_ID); //專案ID
//
//                    msibook_cec_new_application.putExtra("F_Cer_Application",Save_Select_CEC);  //rg1.getChildAt(radioId)

                    msibook_cec_new_application.putStringArrayListExtra("Item", (ArrayList<String>) Save_Select_item); //// 陣列加入 ---認證項目名稱

                    msibook_cec_new_application.putStringArrayListExtra("Temp_Item", (ArrayList<String>) Save_Lab_Temp); // 陣列加入 ---認證代碼

                    msibook_cec_new_application.putStringArrayListExtra("Cer_Time_Item", (ArrayList<String>) Save_Cer_Time); //// 陣列加入    ---週次

                    msibook_cec_new_application.putStringArrayListExtra("Cer_Expense_Item", (ArrayList<String>) Save_Cer_Expense); //// 陣列加入    ---美金

                    msibook_cec_new_application.putStringArrayListExtra("RWorkID_Item", (ArrayList<String>) Save_RWorkID); //// 陣列加入    ---負責人工號

                    msibook_cec_new_application.putStringArrayListExtra("Cer_Pic_Item", (ArrayList<String>) Save_Cer_Pic); //// 陣列加入    ---負責人工號

                    msibook_cec_new_application.putExtra("F_Cer_Time",(String.valueOf(SaveTotaltime)));  //rg1.getChildAt(radioId)

                    msibook_cec_new_application.putExtra("F_Cer_Expense",String.valueOf(TotalUSD));  //rg1.getChildAt(radioId)

                    startActivityForResult(msibook_cec_new_application,1);
                }
            }
        });

    }

    //                     showAlert("Error","尚未選擇細項");
    private void showAlert(String title,String context)
    {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle(title);
        alert.setMessage(context);
        alert.show();
    }

    // 抓取 對硬 Radio 認證選項的 Item
    private void Find_New_Certification() {

        //顯示  讀取等待時間Bar
        progressBar.show();

        Apply_Item_List.clear();

        RequestQueue mQueue = Volley.newRequestQueue(this);

        //String Path = GetServiceData.ServicePath + "/Find_Certification_Apply?F_Keyin=" + ApplicationTpye;
        String Path = "http://wtsc.msi.com.tw/IMS/MsiBook_App_Service.asmx/Find_New_Certification";

        GetServiceData.getString(Path, mQueue, new GetServiceData.VolleyCallback() {
            @Override
            public void onSuccess(JSONObject result) {

                try {

                    JSONArray UserArray = new JSONArray(result.getString("Key"));

                    for (int i = 0; i < UserArray.length(); i++) {
                        JSONObject IssueData = UserArray.getJSONObject(i);

                        String F_SeqNo = String.valueOf(IssueData.getInt("F_SeqNo")); //
                        String F_CreateDate = String.valueOf(IssueData.getString("F_CreateDate")); // "2019-07-22T00:00:00",
                        String F_UpdateTime = String.valueOf(IssueData.getString("F_UpdateTime")); // "2019-07-22T00:00:00",
                        String F_Stat = String.valueOf(IssueData.getString("F_Stat")); //  "0",
                        String F_Keyin = String.valueOf(IssueData.getString("F_Keyin")); // "10015834",
                        String F_Security = String.valueOf(IssueData.getString("F_Security")); // "U",
                        String F_Cer_Class = String.valueOf(IssueData.getString("F_Cer_Class")); // "HDMI",
                        String F_Cer_Logo = String.valueOf(IssueData.getString("F_Cer_Logo")); //"HDMI",
                        String F_Cer_ID = String.valueOf(IssueData.getString("F_Cer_ID")); //  "12",
                        String F_Cer_Time = String.valueOf(IssueData.getInt("F_Cer_Time")); // 4,
                        String F_Cer_Expense = String.valueOf(IssueData.getDouble("F_Cer_Expense"));//1500,
                        Log.w("F_Cer_Expense",F_Cer_Expense);
                        String F_Cer_Pic = String.valueOf(IssueData.getString("F_Cer_Pic")); //   "F_Cer_Pic": "10003130@2014-0703-0606-347545@10013133.bmp",
                        Log.w("F_Cer_Pic",F_Cer_Pic);
                        String F_RWorkID = String.valueOf(IssueData.getString("F_RWorkID")); //

                        Apply_Item_List.add(i,new Apply_Item(F_SeqNo,F_Stat,F_Keyin,F_Security,F_Cer_Class,F_Cer_Logo,F_Cer_ID,F_Cer_Time,F_Cer_Expense,F_Cer_Pic,F_RWorkID));
                        //-------------------------------
//                        String F_SeqNo = String.valueOf(IssueData.getInt("F_SeqNo")); //"F_SeqNo": 53,
//
//                        String F_Cer_Class = String.valueOf(IssueData.getString("F_Cer_Class")); // "F_Cer_Class": "A",
//
//                        String F_Cer_Logo = String.valueOf(IssueData.getString("F_Cer_Logo")); // "F_Cer_Logo": "ErP Lot 3",
//
//                        String F_Cer_Time = String.valueOf(IssueData.getDouble("F_Cer_Time")); // "F_Cer_Time": 4.00,
//
//                        String F_Cer_Expense = String.valueOf(IssueData.getDouble("F_Cer_Expense")); // "F_Cer_Expense": 1000.00,
//
//                        String F_RWorkID = String.valueOf(IssueData.getString("F_RWorkID")); //"F_RWorkID": "10012947",
//
//                        String F_Cer_Pic = String.valueOf(IssueData.getString("F_Cer_Pic")); //   "F_Cer_Pic": "10003130@2014-0703-0606-347545@10013133.bmp",
//
//                        String F_Cer_Application = String.valueOf(IssueData.getString("F_Cer_Application")); // "F_Cer_Application": "0"
//
//                        Apply_Item_List.add(i,new Apply_Item(F_SeqNo,F_Cer_Class,F_Cer_Logo,F_Cer_Time,F_Cer_Expense,F_RWorkID,F_Cer_Pic,F_Cer_Application));
//
                    }

                    recyclerViewLayoutManager = new GridLayoutManager(mContext, 2);

                    recyclerView.setLayoutManager(recyclerViewLayoutManager);

                    recyclerView_Adapter = new RecyclerViewAdapter(mContext, Apply_Item_List);

                    recyclerView.setAdapter(recyclerView_Adapter);

                    recyclerView_Adapter.notifyDataSetChanged();

                    //關閉-讀取等待時間Bar
                    progressBar.dismiss();

                }
                catch (JSONException ex) {

                    Log.w("Json",ex.toString());
                }

            }
        });


    }

    public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{

        List<Apply_Item> values;
        Context context1;

        private boolean[] checks; //用于保存checkBox的选择状态

        public RecyclerViewAdapter(Context context2, List<Apply_Item> values2){

            values = values2;

            context1 = context2;
        }

        public class ViewHolder extends RecyclerView.ViewHolder{

            public LinearLayout linear_apply;
            public CheckBox textView_checkbox;
            public TextView textView_double_checkbox;
            public ImageView imageView_F_Cer_Pic;
            public TextView textView_logo;
            public TextView textView_total_info;
            public TextView textView_cec_Type;

            public ViewHolder(View v){

                super(v);

                linear_apply = (LinearLayout) v.findViewById(R.id.linear_apply);

                imageView_F_Cer_Pic = (ImageView) v.findViewById(R.id.imageView_F_Cer_Pic); //圖片

                textView_logo = (TextView) v.findViewById(R.id.textView_logo); // 主TITLE

                textView_checkbox = (CheckBox) v.findViewById(R.id.textView_checkbox); //確認點選
                textView_double_checkbox = (TextView) v.findViewById(R.id.textView_double_checkbox); //確認點選

                textView_total_info = (TextView) v.findViewById(R.id.textView_total_info); //金額 跟 花費週次

                textView_cec_Type = (TextView) v.findViewById(R.id.textView_cec_Type); // 主TITLE

            }
        }

        @Override
        public RecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){

            View view1 = LayoutInflater.from(context1).inflate(R.layout.msibook_cec_new_add_step1_adapter,parent,false);

            RecyclerViewAdapter.ViewHolder viewHolder1 = new RecyclerViewAdapter.ViewHolder(view1);

            return viewHolder1;

        }

        @Override
        public int getItemCount(){
            return values.size();
        }

        @Override
        public void onBindViewHolder(final RecyclerViewAdapter.ViewHolder Vholder, final int position){

            ImagePath = values.get(position).GetF_Cer_Pic().replace("//172.16.111.114/File","http://wtsc.msi.com.tw/IMS/FileServer");
            //ImagePath = Apply_Item_List.get(position).GetF_Cer_Pic();
//            final ViewHolder finalHolder = holder;
            Glide
                    .with(mContext)
                    .load(ImagePath)
                    .asBitmap()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.mipmap.pms_img_pms_no_pic)
                    .into(new SimpleTarget<Bitmap>(100, 100) {
                        @Override
                        public void onResourceReady(Bitmap resource, GlideAnimation glideAnimation) {

                            Vholder.imageView_F_Cer_Pic.setImageBitmap(resource);

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

            Vholder.textView_logo.setText(values.get(position).GetF_Cer_Logo());

            Vholder.textView_total_info.setText(values.get(position).GetF_Cer_Expense() +"。"+Apply_Item_List.get(position).GetF_Cer_Time()+" 週");

            final int pos  = position; //pos必须声明为final

            Vholder.linear_apply.setOnClickListener(new View.OnClickListener() { //主體點擊時 做判斷
                @Override
                public void onClick(View v) {
                    if(Vholder.textView_checkbox.isChecked() != true) //打勾 累加
                    {
                        Vholder.textView_checkbox.setChecked(true);
                        Vholder.textView_double_checkbox.setBackground(ResourcesCompat.getDrawable(getResources(), R.mipmap.msibook_cec_btn_newcer_checked, null));
                        //textView_checkbox.setBackgroundResource(R.mipmap.facility_ic_area_check);
                        //運用DecimalFormat制定好金額顯示格式與小數點位數
                        DecimalFormat mDecimalFormat = new DecimalFormat("#,###.##");

                        SaveTotaltime += Double.valueOf(values.get(position).GetF_Cer_Time());
                        Integer Int_totaltime = SaveTotaltime.intValue();

                        TotalUSD += Double.valueOf(values.get(position).GetF_Cer_Expense());
                        Integer Int_USD  = TotalUSD.intValue();
                        String str__USD = mDecimalFormat.format(Double.parseDouble(String.valueOf(TotalUSD)));

                        TotalCount+=1;
                        //textView_totalTime.setText(String.valueOf(Int_totaltime)+" 週");
                        textView_totalUSD.setText("USD.$"+String.valueOf(str__USD));
                        textView_TotalCount.setText(String.valueOf("總數量  :"+TotalCount));

                        Save_Select_item.add(values.get(position).GetF_Cer_Logo());// 陣列加入 ---認證項目名稱
                        //Save_Lab_Temp.add(values.get(position).GetF_SeqNo()); // 陣列加入 ---認證代碼
                        Save_Lab_Temp.add(values.get(position).GetF_Cer_ID()); // 陣列加入 ---認證代碼
                        Save_Cer_Time.add(values.get(position).GetF_Cer_Time()); // 陣列加入    ---週次
                        Save_Cer_Expense.add(values.get(position).GetF_Cer_Expense());// 陣列加入    ---美金
                        Save_RWorkID.add(values.get(position).GetF_RWorkID());// 陣列加入    ---負責人工號
                        Save_Cer_Pic.add(values.get(position).GetF_Cer_Pic());// 陣列加入    ---圖片

                        Log.w("Add_Item",values.get(position).GetF_Cer_Logo());
                        Log.w("Add_Lab_Temp",values.get(position).GetF_SeqNo());
                        Log.w("Add_totalTiem",values.get(position).GetF_Cer_Time());
                        Log.w("Add_USD",values.get(position).GetF_Cer_Expense());
                        Log.w("Add_WorkID",values.get(position).GetF_RWorkID());

                    }
                    else //勾勾拿掉  去除
                    {
                        Vholder.textView_checkbox.setChecked(false);
                        Vholder.textView_double_checkbox.setBackground(ResourcesCompat.getDrawable(getResources(), R.mipmap.msibook_cec_btn_newcer_unchecked, null));
                        //運用DecimalFormat制定好金額顯示格式與小數點位數
                        DecimalFormat mDecimalFormat = new DecimalFormat("#,###.##");

                        SaveTotaltime -= Double.valueOf(values.get(position).GetF_Cer_Time());
                        Integer Int_totaltime = SaveTotaltime.intValue();

                        TotalUSD -= Double.valueOf(values.get(position).GetF_Cer_Expense());
                        Integer Int_USD  = TotalUSD.intValue();
                        String str__USD = mDecimalFormat.format(Double.parseDouble(String.valueOf(TotalUSD)));
                        TotalCount-=1;
                        //textView_totalTime.setText(String.valueOf(Int_totaltime)+" 週");
                        textView_totalUSD.setText("USD.$"+String.valueOf(str__USD));
                        textView_TotalCount.setText(String.valueOf("總數量  :"+TotalCount));

                        int Remove_mark;
                        for (int i = 0; i < Save_Select_item.size(); i++) {
                            if (Save_Select_item.get(i).contains(values.get(position).GetF_Cer_Logo())) {  //移除  項目
                                Save_Select_item.remove(i);

                                Save_Lab_Temp.remove(i);
                                Save_Cer_Time.remove(i);
                                Save_Cer_Expense.remove(i);
                                Save_RWorkID.remove(i);
                                Save_Cer_Pic.remove(i);
                                //Log.w("RRRRE_Item",String.valueOf(Save_Select_item.get(i)));
                            }
                        }

                    }
                }
            });

            switch (values.get(position).GetF_Cer_Class()) {
                case "HDMI":
                    Vholder.textView_cec_Type.setBackground(ResourcesCompat.getDrawable(getResources(), R.mipmap.msibook_cec_ic_cerclass_hdmi, null));
                    break;
                case "WHQL":
                    Vholder.textView_cec_Type.setBackground(ResourcesCompat.getDrawable(getResources(), R.mipmap.msibook_cec_ic_cerclass_whql, null));
                    break;
                case "能耗":
                    Vholder.textView_cec_Type.setBackground(ResourcesCompat.getDrawable(getResources(), R.mipmap.msibook_cec_ic_cerclass_energy, null));
                    break;
                case "語音":
                    Vholder.textView_cec_Type.setBackground(ResourcesCompat.getDrawable(getResources(), R.mipmap.msibook_cec_ic_cerclass_voice, null));
                    break;
                case "工程":
                    Vholder.textView_cec_Type.setBackground(ResourcesCompat.getDrawable(getResources(), R.mipmap.msibook_cec_ic_cerclass_reliability, null));
                    break;
                case "USB":
                    Vholder.textView_cec_Type.setBackground(ResourcesCompat.getDrawable(getResources(), R.mipmap.msibook_cec_ic_cerclass_usb, null));
                    break;
                case "DP":
                    Vholder.textView_cec_Type.setBackground(ResourcesCompat.getDrawable(getResources(), R.mipmap.msibook_cec_ic_cerclass_dp, null));
                    break;
            }


        }


    }


    //--------------------------------------------------------------------------------Item--------------------------------------------------------------------------------
    //Detail_Item
    public class Apply_Item {

        String F_SeqNo;

        String F_Stat;

        String F_Keyin;

        String F_Security;

        String F_Cer_Class;

        String F_Cer_Logo;

        String F_Cer_ID;

        String F_Cer_Time;

        String F_Cer_Expense;

        String F_Cer_Pic;

        String F_RWorkID;

        //boolean F_Checks;

        public Apply_Item(String F_SeqNo,String F_Stat,String F_Keyin,String F_Security,String F_Cer_Class,String F_Cer_Logo,String F_Cer_ID,String F_Cer_Time,String F_Cer_Expense,String F_Cer_Pic,String F_RWorkID)
        {
            this.F_SeqNo = F_SeqNo;

            this.F_Stat = F_Stat;

            this.F_Keyin = F_Keyin;

            this.F_Security = F_Security;

            this.F_Cer_Class = F_Cer_Class;

            this.F_Cer_Logo = F_Cer_Logo;

            this.F_Cer_ID = F_Cer_ID;

            this.F_Cer_Time = F_Cer_Time;

            this.F_Cer_Expense = F_Cer_Expense;

            this.F_Cer_Pic = F_Cer_Pic;

            this.F_RWorkID = F_RWorkID;
        }


        public String GetF_SeqNo()
        {
            return this.F_SeqNo;
        }

        public String GetF_F_Stat()
        {
            return this.F_Stat;
        }

        public String GetF_Keyin()
        {
            return this.F_Keyin;
        }

        public String GetF_Security()
        {
            return this.F_Security;
        }

        public String GetF_Cer_Class()
        {
            return this.F_Cer_Class;
        }

        public String GetF_Cer_Logo()
        {
            return this.F_Cer_Logo;
        }

        public String GetF_Cer_ID()
        {
            return this.F_Cer_ID;
        }

        public String GetF_Cer_Time()
        {
            return this.F_Cer_Time;
        }

        public String GetF_Cer_Expense()
        {
            return this.F_Cer_Expense;
        }

        public String GetF_Cer_Pic()
        {
            return this.F_Cer_Pic;
        }

        public String GetF_RWorkID()
        {
            return this.F_RWorkID;
        }

    }





}
