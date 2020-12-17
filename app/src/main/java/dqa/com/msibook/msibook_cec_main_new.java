package dqa.com.msibook;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class msibook_cec_main_new extends AppCompatActivity {

    private Context mContext;

    private Button btn_creat_pop;
    private TextView toolbar_title;
    private TextView btn_refresh;
    private ListView eHR_main_list;
    private ProgressDialog progressBar;
    private ExpandableListAdapter listAdapter;
    private ExpandableListView expListView;

    private List<CEC_Model_Item> CEC_Model_Item_list = new ArrayList<CEC_Model_Item>();
    private List<CEC_Model_Item> CEC_Model_Item_list_filter = new ArrayList<CEC_Model_Item>();
    private List<List<CEC_Model_detial_Item>> Child_Model_detial_Item_list = new ArrayList<List<CEC_Model_detial_Item>>();

    private String Set_Result;
    private String Set_F_Cer_Logo;
    private String Set_F_Reason;
    private String Set_F_SeqNo;
    private String Set_F_DownloadFilePath;
    private String Set_RRate;
    private Button btn_cec_add;

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(data !=null) {
            Bundle bundle = data.getExtras();
            Integer CheckApplication = Integer.valueOf(bundle.getString("CEC_Application_Check"));
            if(CheckApplication ==1){// 1 == 有應徵成功 跳到我的應徵
                AlertDialog dialog = new AlertDialog.Builder(mContext)
                        .setMessage("感謝您的申請，我們已收到您的申請資料\n您可以透過APP查詢您的申請進度。 ")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {

                                dialog.cancel();
                            }
                        })
                        .show();
                dialog.setCanceledOnTouchOutside(false);// 设置点击屏幕Dialog不消失
            }


            Find_Certification_Inquire(UserData.WorkID,"處理中");

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msibook_cec_main_new);

        mContext = msibook_cec_main_new.this;

        progressBar = new ProgressDialog(this);
        progressBar.setCancelable(true);
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.setMessage("資料載入中");

        btn_creat_pop = (Button) findViewById(R.id.btn_creat_pop);//漢堡列
        toolbar_title = (TextView) findViewById(R.id.toolbar_title);//Title
        btn_refresh = (TextView) findViewById(R.id.btn_refresh);//
        btn_cec_add= (Button) findViewById(R.id.btn_cec_add);//新增


        expListView = (ExpandableListView) findViewById(R.id.exp_cec_list);

        Find_Certification_Inquire(UserData.WorkID,"處理中");

        btn_cec_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent msibook_cec_main_new = new Intent(mContext,msibook_cec_add_step1.class);
                startActivityForResult(msibook_cec_main_new,1);
            }
        });

    }

    //部門職缺資訊
    public void Find_Certification_Inquire(String WorkID, final String TabStatus) {

        //顯示  讀取等待時間Bar
        progressBar.show();

        CEC_Model_Item_list.clear();
        CEC_Model_Item_list_filter.clear();
        Child_Model_detial_Item_list.clear();

        RequestQueue mQueue = Volley.newRequestQueue(this);

        String Path = "http://wtsc.msi.com.tw/IMS/MsiBook_App_Service.asmx/Find_Certification_Inquire?WorkID=" + WorkID;
        //String Path = GetServiceData.ServicePath + "/Find_Certification_Model?WorkID=" + WorkID;

        GetServiceData.getString(Path, mQueue, new GetServiceData.VolleyCallback() {
            @Override
            public void onSuccess(JSONObject result) {

                try {

                    JSONArray UserArray = new JSONArray(result.getString("Key"));

                    final Map<String, ArrayList<CEC_Model_Item>> mapContent = new HashMap<String,ArrayList<CEC_Model_Item>>();

                    List<CEC_Model_Item> fatherList = new ArrayList<>();
                    List<List<CEC_Model_Item>> childList = new ArrayList<>();
                    Map<String, ArrayList<CEC_Model_Item>> linkHashMap = new LinkedHashMap<>();

                    for (int i = 0; i < UserArray.length(); i++) {

                        JSONObject IssueData = UserArray.getJSONObject(i);

                        String F_RespUser = String.valueOf(IssueData.getString("F_RespUser"));// "鄭偉國",
                        String Model = String.valueOf(IssueData.getString("Model"));//"MS-V334",
                        String Status = String.valueOf(IssueData.getString("Status"));//"已完成", "處理中",  "尚未申請需求單",  "已退回",

                        if (IssueData.isNull("Result")) { //// 1 PASS  0 Fail
                            String Result = "null";
                            Set_Result = "null";
                        } else {
                            String Result = String.valueOf(IssueData.getString("Result"));
                            Set_Result = Result;
                        }

                        if (IssueData.isNull("F_SeqNo")) { //// 1 PASS  0 Fail
                            String F_SeqNo = "null";
                            Set_F_SeqNo = "null";
                        } else {
                            String F_SeqNo = String.valueOf(IssueData.getInt("F_SeqNo"));
                            Set_F_SeqNo = F_SeqNo;
                        }

                        String F_Keyin = String.valueOf(IssueData.getString("F_Keyin"));//

                        String AssignUser = String.valueOf(IssueData.getString("AssignUser"));//

                        String F_CreateDate = String.valueOf(IssueData.getString("F_CreateDate"));//

                        String F_Cer_Pic = String.valueOf(IssueData.getString("F_Cer_Pic"));// "//172.16.111.114/File/SDQA/Code/Certification/10011929@2011-0113-0529-463445@HDMI_black.jpg",

                        String F_Cer_ID = String.valueOf(IssueData.getInt("F_Cer_ID"));//

                        String F_Status = String.valueOf(IssueData.getString("F_Status"));// "

                        String F_Require_ID = String.valueOf(IssueData.getString("F_Require_ID"));//

                        String F_Cer_Expense = String.valueOf(IssueData.getDouble("F_Cer_Expense"));//  1500.00,

                        String F_Cer_Time = String.valueOf(IssueData.getDouble("F_Cer_Time"));//  4.00,

                        //String F_Cer_Logo = String.valueOf(IssueData.getString("F_Cer_Logo"));// "HDMI",
                        if (IssueData.isNull("F_Cer_Logo")) { //
                            String F_Reason = "null";
                            Set_F_Cer_Logo = "N/A";
                        } else {
                            String F_Cer_Logo = String.valueOf(IssueData.getString("F_Cer_Logo"));
                            Set_F_Cer_Logo = F_Cer_Logo;
                        }

                        if (IssueData.isNull("F_Reason")) { //
                            String F_Reason = "null";
                            Set_F_Reason = "null";
                        } else {
                            String F_Reason = String.valueOf(IssueData.getString("F_Reason"));
                            Set_F_Reason = F_Reason;
                        }

                        String F_RWorkID = String.valueOf(IssueData.getString("F_RWorkID"));// "HDMI",

                        if (IssueData.isNull("F_DownloadFilePath")) { // null, 表示 審核中
                            String F_DownloadFilePath = "null";
                            Set_F_DownloadFilePath = "null";
                        } else {
                            String F_DownloadFilePath = String.valueOf(IssueData.getString("F_DownloadFilePath"));
                            Set_F_DownloadFilePath = F_DownloadFilePath;
                        }

                        if (IssueData.isNull("RRate")) { // null, 表示 ???
                            String RRate = "null";
                            Set_RRate = "null";
                        } else {
                            String RRate = String.valueOf(IssueData.getString("RRate"));
                            Set_RRate = RRate;
                        }

                        String F_Is_Del = String.valueOf(IssueData.getString("F_Is_Del"));// "0"

                        if(Status.contains(TabStatus)){ //判斷 是否符合 狀態 已完成", "處理中",  "尚未申請需求單",  "已退回",

                            if (!linkHashMap.containsKey(Model)){  //加入表內
                                ArrayList<CEC_Model_Item>  innchildList = new ArrayList<>();
                                innchildList.add(new CEC_Model_Item(Model,Set_Result,AssignUser,F_Cer_Pic, Set_F_Cer_Logo));
                                linkHashMap.put(Model , innchildList);
                            } else {
                                linkHashMap.get(Model).add(new CEC_Model_Item(Model,Set_Result,AssignUser,F_Cer_Pic, Set_F_Cer_Logo));
                            }

                        }


                    }

                    int index = 0;
                    for (Map.Entry<String, ArrayList<CEC_Model_Item>>  item  : linkHashMap.entrySet()){
                        fatherList.add(index  , item.getValue().get(0)) ;
                        ArrayList<CEC_Model_Item>  tempList = new ArrayList<>();
                        for (CEC_Model_Item  childItem : item.getValue()){
                            tempList.add(childItem);
                        }
                        childList.add(index, tempList);
                        index++;
                    }


                    listAdapter = new ExpandableListAdapter(mContext, fatherList, childList);
                    // setting list adapter
                    expListView.setAdapter(listAdapter);

                    for (int i = 0 ;i< fatherList.size();i++)//預設展開
                    {
                        expListView.expandGroup(i);
                    }

                    expListView.setGroupIndicator(null);

                    //關閉-讀取等待時間Bar
                    progressBar.dismiss();


                }
                catch (JSONException ex) {

                    Log.w("Json",ex.toString());
                }

            }
        });


    }



    public class CEC_Model_Item {

        //        String F_RespUser;
        String Model;
        //        String Status;
        String Result;
        String AssignUser;
        //        String F_Keyin;
//        String F_SeqNo;
//        String F_Owner;
//        String F_CreateDate;
//        String F_Cer_ID;
//        String F_Status;
//        String F_Require_ID;
//        String F_Cer_Expense;
//        String F_Cer_Time;
//        String F_SDate;
//        String F_ExpectFixDate;
        String F_Cer_Pic;
        String F_Cer_Logo;
        //        String F_Reason;
//        String F_RWorkID;
//        String F_DownloadFilePath;
//        String F_Cer_Class;
//        String RRate;
//        String F_Is_Del;
//public CEC_Model_Item(String F_RespUser,String Column1,String Status,String F_Keyin,String F_SeqNo,String F_Owner,String F_CreateDate,String F_Cer_ID,String F_Status,String F_Require_ID,String F_Cer_Expense,String F_Cer_Time,String F_SDate,String F_ExpectFixDate,String F_Cer_Pic,String F_Cer_Logo,String F_Reason,String F_RWorkID,String F_DownloadFilePath,String F_Cer_Class,String RRate,String F_Is_Del)
//{
        public CEC_Model_Item(String Model,String Result,String AssignUser,String F_Cer_Pic,String F_Cer_Logo)
        {
//            this.F_RespUser = F_RespUser;
            this.Model = Model;
//            this.Status = Status;
            this.Result = Result;
//            this.F_Keyin = F_Keyin;
//            this.F_SeqNo = F_SeqNo;
//            this.F_Owner = F_Owner;
//            this.F_CreateDate = F_CreateDate;
//            this.F_Cer_ID = F_Cer_ID;
//            this.F_Status = F_Status;
//            this.F_Require_ID = F_Require_ID;
//            this.F_Cer_Expense = F_Cer_Expense;
//            this.F_Cer_Time = F_Cer_Time;
//            this.F_SDate = F_SDate;
//            this.F_ExpectFixDate = F_ExpectFixDate;
            this.AssignUser = AssignUser;
            this.F_Cer_Pic = F_Cer_Pic;
            this.F_Cer_Logo = F_Cer_Logo;
//            this.F_Reason = F_Reason;
//            this.F_RWorkID = F_RWorkID;
//            this.F_DownloadFilePath = F_DownloadFilePath;
//            this.F_Cer_Class = F_Cer_Class;
//            this.RRate = RRate;
//            this.F_Is_Del = F_Is_Del;
        }

        //        public String GetF_RespUser()
//        {
//            return this.F_RespUser;
//        }
        public String GetModel()
        {
            return this.Model;
        }
        //        public String GetStatus()
//        {
//            return this.Status;
//        }
        public String GetResult()
        {
            return this.Result;
        }
        //        public String GetF_Keyin()
//        {
//            return this.F_Keyin;
//        }
//        public String GetF_SeqNo()
//        {
//            return this.F_SeqNo;
//        }
//        public String GetF_Owner()
//        {
//            return this.F_Owner;
//        }
//        public String GetF_CreateDate()
//        {
//            return this.F_CreateDate;
//        }
//        public String GetF_Cer_ID()
//        {
//            return this.F_Cer_ID;
//        }
//        public String GetF_Status()
//        {
//            return this.F_Status;
//        }
//        public String GetF_Require_ID()
//        {
//            return this.F_Require_ID;
//        }
//        public String GetF_Cer_Expense()
//        {
//            return this.F_Cer_Expense;
//        }
//        public String GetF_Cer_Time()
//        {
//            return this.F_Cer_Time;
//        }
//        public String GetF_SDate()
//        {
//            return this.F_SDate;
//        }
//        public String GetF_ExpectFixDate()
//        {
//            return this.F_ExpectFixDate;
//        }
        public String GetAssignUser()
        {
            return this.AssignUser;
        }
        public String GetF_Cer_Pic()
        {
            return this.F_Cer_Pic;
        }
        public String GetF_Cer_Logo()
        {
            return this.F_Cer_Logo;
        }
//        public String GetF_Reason()
//        {
//            return this.F_Reason;
//        }
//        public String GetF_RWorkID()
//        {
//            return this.F_RWorkID;
//        }
//        public String GetF_DownloadFilePath()
//        {
//            return this.F_DownloadFilePath;
//        }
//        public String GetF_Cer_Class()
//        {
//            return this.F_Cer_Class;
//        }
//        public String GetRRate()
//        {
//            return this.RRate;
//        }
//        public String GetF_Is_Del()
//        {
//            return this.F_Is_Del;
//        }

    }//給Group用

    public class CEC_Model_detial_Item {

        String F_RespUser;
        String Column1;
        String Status;
        String F_Keyin;
        String F_SeqNo;
        String F_Owner;
        String F_CreateDate;
        String F_Cer_ID;
        String F_Status;
        String F_Require_ID;
        String F_Cer_Expense;
        String F_Cer_Time;
        String F_SDate;
        String F_ExpectFixDate;
        String F_Cer_Pic;
        String F_Cer_Logo;
        String F_Reason;
        String F_RWorkID;
        String F_DownloadFilePath;
        String F_Cer_Class;
        String RRate;
        String F_Is_Del;

        public CEC_Model_detial_Item(String F_RespUser,
                                     String Column1,
                                     String Status,
                                     String F_Keyin,
                                     String F_SeqNo,
                                     String F_Owner,
                                     String F_CreateDate,
                                     String F_Cer_ID,
                                     String F_Status,
                                     String F_Require_ID,
                                     String F_Cer_Expense,
                                     String F_Cer_Time,
                                     String F_SDate,
                                     String F_ExpectFixDate,
                                     String F_Cer_Pic,
                                     String F_Cer_Logo,
                                     String F_Reason,
                                     String F_RWorkID,
                                     String F_DownloadFilePath,
                                     String F_Cer_Class,
                                     String RRate,
                                     String F_Is_Del)
        {
            this.F_RespUser = F_RespUser;
            this.Column1 = Column1;
            this.Status = Status;
            this.F_Keyin = F_Keyin;
            this.F_SeqNo = F_SeqNo;
            this.F_Owner = F_Owner;
            this.F_CreateDate = F_CreateDate;
            this.F_Cer_ID = F_Cer_ID;
            this.F_Status = F_Status;
            this.F_Require_ID = F_Require_ID;
            this.F_Cer_Expense = F_Cer_Expense;
            this.F_Cer_Time = F_Cer_Time;
            this.F_SDate = F_SDate;
            this.F_ExpectFixDate = F_ExpectFixDate;
            this.F_Cer_Pic = F_Cer_Pic;
            this.F_Cer_Logo = F_Cer_Logo;
            this.F_Reason = F_Reason;
            this.F_RWorkID = F_RWorkID;
            this.F_DownloadFilePath = F_DownloadFilePath;
            this.F_Cer_Class = F_Cer_Class;
            this.RRate = RRate;
            this.F_Is_Del = F_Is_Del;
        }

        public String GetF_RespUser()
        {
            return this.F_RespUser;
        }
        public String GetColumn1()
        {
            return this.Column1;
        }
        public String GetStatus()
        {
            return this.Status;
        }
        public String GetF_Keyin()
        {
            return this.F_Keyin;
        }
        public String GetF_SeqNo()
        {
            return this.F_SeqNo;
        }
        public String GetF_Owner()
        {
            return this.F_Owner;
        }
        public String GetF_CreateDate()
        {
            return this.F_CreateDate;
        }
        public String GetF_Cer_ID()
        {
            return this.F_Cer_ID;
        }
        public String GetF_Status()
        {
            return this.F_Status;
        }
        public String GetF_Require_ID()
        {
            return this.F_Require_ID;
        }
        public String GetF_Cer_Expense()
        {
            return this.F_Cer_Expense;
        }
        public String GetF_Cer_Time()
        {
            return this.F_Cer_Time;
        }
        public String GetF_SDate()
        {
            return this.F_SDate;
        }
        public String GetF_ExpectFixDate()
        {
            return this.F_ExpectFixDate;
        }
        public String GetF_Cer_Pic()
        {
            return this.F_Cer_Pic;
        }
        public String GetF_Cer_Logo()
        {
            return this.F_Cer_Logo;
        }
        public String GetF_Reason()
        {
            return this.F_Reason;
        }
        public String GetF_RWorkID()
        {
            return this.F_RWorkID;
        }
        public String GetF_DownloadFilePath()
        {
            return this.F_DownloadFilePath;
        }
        public String GetF_Cer_Class()
        {
            return this.F_Cer_Class;
        }
        public String GetRRate()
        {
            return this.RRate;
        }
        public String GetF_Is_Del()
        {
            return this.F_Is_Del;
        }

    }//給Chrild用

    // ExpandableListAdapter
    public class ExpandableListAdapter extends BaseExpandableListAdapter {
        private Context _context;
        private List<CEC_Model_Item> fatherList = new ArrayList<>();
        private List<List<CEC_Model_Item>> childList = new ArrayList<>();

        public ExpandableListAdapter(Context context,  List<CEC_Model_Item> fatherList , List<List<CEC_Model_Item>> childList){
            this._context = context;
            this.fatherList = fatherList;
            this.childList = childList;
        }

        @Override
        public int getGroupCount() {

            return this.fatherList.size();
        }

        @Override
        public int getChildrenCount(int groupPosition) {

            return  this.childList.get(groupPosition).size();
        }

        @Override
        public Object getGroup(int groupPosition) {
            return this.fatherList.get(groupPosition);
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return this.childList.get(groupPosition).get(childPosition);
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            CEC_Model_Item CEC_Model_Item = (CEC_Model_Item)  getGroup(groupPosition);

            if (convertView == null){
                LayoutInflater infalInflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = infalInflater.inflate(R.layout.msibook_cec_main_new_group_item,null);
            }

            TextView textView_project = (TextView) convertView.findViewById(R.id.textView_project);

            ImageView parentImageViw = (ImageView) convertView.findViewById(R.id.parentImageViw);

            textView_project.setText(CEC_Model_Item.GetModel());

            //判斷isExpanded就可以控制是按下還是關閉，同時更換圖片
            if(isExpanded){
                parentImageViw.setBackgroundResource(R.mipmap.ehr_btn_common_collapse);
            }else{
                parentImageViw.setBackgroundResource(R.mipmap.ehr_btn_common_unfolded);
            }

            return convertView;
        }

        @Override
        public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

            CEC_Model_Item CEC_Model_Item = (CEC_Model_Item)getChild(groupPosition,childPosition);

            //final String childText = (String) getChild(groupPosition, childPosition);
            if (convertView == null) {
                LayoutInflater infalInflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = infalInflater.inflate(R.layout.msibook_cec_main_new_child_item,null);
            }
            final ImageView imageView_F_Cer_Pic = (ImageView) convertView.findViewById(R.id.imageView_F_Cer_Pic);
            TextView textView_F_Cer_Logo = (TextView) convertView.findViewById(R.id.textView_F_Cer_Logo);
            TextView textView_F_AssignUser = (TextView) convertView.findViewById(R.id.textView_F_AssignUser);

//            TextView textView_Result = (TextView) convertView.findViewById(R.id.textView_Result);//新增時show出
//
//            if(CEC_Model_Item.GetResult().contains("1")){
//                textView_Result.setVisibility(View.VISIBLE);
//                textView_Result.setText("PASS");
//                textView_Result.setTextColor(Color.parseColor("#358900"));
//            }else if(CEC_Model_Item.GetResult().contains("0")){
//                textView_Result.setVisibility(View.VISIBLE);
//                textView_Result.setText("Fail");
//                textView_Result.setTextColor(Color.parseColor("#d94045"));
//            }else{
//                textView_Result.setVisibility(View.GONE);
//            }


            String ImagePath = CEC_Model_Item.GetF_Cer_Pic().replace("//172.16.111.114/File","http://wtsc.msi.com.tw/IMS/FileServer");

            Log.w("IMagePath",ImagePath);
            Glide
                    .with(_context)
                    .load(ImagePath)
                    .asBitmap()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.mipmap.progress_image)
                    .into(new SimpleTarget<Bitmap>(100, 100) {
                        @Override
                        public void onResourceReady(Bitmap resource, GlideAnimation glideAnimation) {

                            imageView_F_Cer_Pic.setImageBitmap(resource);
                        }

                        @Override
                        public void onLoadFailed(Exception e, Drawable errorDrawable) {
//                            Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

            textView_F_Cer_Logo.setText(CEC_Model_Item.GetF_Cer_Logo());
            textView_F_AssignUser.setText(CEC_Model_Item.GetAssignUser());


            return convertView;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }
    }





}
