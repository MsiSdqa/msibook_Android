package dqa.com.msibook;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class msibook_certified_main_for_pm extends AppCompatActivity {

    private ProgressDialog progressBar;

    private List<msibook_certified_item> Certified_List = new ArrayList<msibook_certified_item>();

    private List<String> GroupItem = new ArrayList<String>(); //組

    private List<msibook_certified_item> Favorite_Certified_List = new ArrayList<msibook_certified_item>();//我的最愛

    private List<msibook_certified_item> Project_Certified_List = new ArrayList<msibook_certified_item>();//專案

    private ArrayList<List<msibook_certified_item>> Certified_List_Group = new ArrayList<List<msibook_certified_item>>();//群組

    private ExpandableListView exp_my_certified_list;

    private Certified_List_Adapter exp_my_certified_list_Adapter;

    private Context mContext;

    private Button btn_new_certified;

    private Integer SaveBackValue;

    //點選Local回傳狀態
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            SaveBackValue = 1;
            Find_Certification_Model_List("10003130");// PM
        }


//        Bundle bundle = data.getExtras();
//        Integer CheckBooking = Integer.valueOf(bundle.getString("Certified_Check"));
//        if(CheckBooking == 1){
//
//            Log.w("哦哦哦哦哦哦","哦哦哦哦哦哦");
//        }
//
//        SaveBackValue = 1;
//        Find_Certification_Model_List("10003130");// PM
//        if(requestCode == 1000 && resultCode == 1001)
//        {
//            SaveBackValue = 1;
//            Find_Certification_Model_List("10003130");// PM
//        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msibook_certified_main_for_pm);

        mContext = this;
        progressBar = new ProgressDialog(this);
        progressBar.setCancelable(true);
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.setMessage("資料載入中");

        exp_my_certified_list = (ExpandableListView) findViewById(R.id.exp_my_certified_list);

        btn_new_certified = (Button) findViewById(R.id.btn_new_certified);

        Find_Certification_Model_List("10003130");// PM

        SaveBackValue = 0;

        btn_new_certified.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, msibook_certified_add.class);

                startActivity(intent);
            }
        });

//        //子項目被點擊事件
//        exp_my_certified_list.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
//            @Override
//            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
//                //Log.w("子項目:",listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition));
//                //專案代碼
//                Log.w("專案代碼:",Certified_List_Group.get(groupPosition).get(childPosition).ModelID);
//                //專案名稱
//                Log.w("專案名稱:",Certified_List_Group.get(groupPosition).get(childPosition).ModelName);
//
////                Intent intent = new Intent();
////
////                intent.putExtra("m3Week", m2putEtrawk);//給第三頁Week
////
////                intent.putExtra("m3Year", m2putEtraYear);//給第三頁Week
////
////                intent.putExtra("m3ChoiceDepID", m2putEtraDepID);//給第三頁部門代號
////
////                intent.putExtra("m3putTitle", m2putTitle);//給第三頁部門名稱
////
////                intent.putExtra("m3Rowtype",MainTitle);//給第三頁 選擇的 Type  EX 加班OR請假OR專案
////
////                //intent.putExtra("Mantitle",Week_Project_Item_list.get(position).GetF_Map());//給第三頁 Title EX:MP、EVT、DVT
////                intent.putExtra("m3ModelList",ChildWeek_Project_Item_list.get(groupPosition).get(childPosition).ModelList);//給第三頁 MS-7A61  MS-7A81
////                intent.putExtra("m3ModelIDList",ChildWeek_Project_Item_list.get(groupPosition).get(childPosition).ModelIDList);//給第三頁 12637  12638
////
////                //intent.setClass(msibook_dqaweekly_main_page2_project_list.this, Main4Activity.class);
////                //開啟Activity
////                startActivity(intent);
//
//
//                return false;
//            }
//        });


    }

    private void Find_Certification_Model_List(String WorkID) {

        //顯示  讀取等待時間Bar
        progressBar.show();

        Certified_List.clear();//塞總資料

        Favorite_Certified_List.clear();//我的最愛

        Project_Certified_List.clear();//專案>一般

        GroupItem.clear();//Title

        Certified_List_Group.clear();//群組

        RequestQueue mQueue = Volley.newRequestQueue(this);

        Map<String,String> map = new HashMap<String, String>();
        map.put("WorkID", WorkID);

        if (mQueue == null) {
            mQueue = Volley.newRequestQueue(this);
        }

        HTTPSTrustManager.allowAllSSL();//信任所有证书，信任憑證
        String Path = GetServiceData.ServicePath + "/Find_Certification_Model_List";
        //String Path = "http://wtsc.msi.com.tw/IMS/MsiBook_App_Service.asmx/Insert_E_HR_Master";

        GetServiceData.SendPostRequest(Path, mQueue, new GetServiceData.VolleyStringCallback() {
            @Override
            public void onSendRequestSuccess(String result) {

                try {

                    JSONObject obj = new JSONObject(result);

                    JSONArray UserArray = new JSONArray(obj.getString("Key"));

                    for (int i = 0; i < UserArray.length(); i++) {

                        JSONObject CertificationData = UserArray.getJSONObject(i);

                        String ModelID = String.valueOf(CertificationData.getInt("ModelID"));  //專案代碼
                        String ModelName = String.valueOf(CertificationData.getString("ModelName")); //專案名稱
                        String ModelPic = String.valueOf(CertificationData.getString("ModelPic")); //專案圖片
                        String DateDiff = String.valueOf(CertificationData.getInt("DateDiff")); //
                        String Product_Line = String.valueOf(CertificationData.getString("Product_Line")); //掛的BU

                        //String Favorite = String.valueOf(CertificationData.getInt("Favorite"));

                        msibook_certified_item msibook_certified_item = new msibook_certified_item(ModelID,ModelName,ModelPic,DateDiff,Product_Line);

                        Certified_List.add(msibook_certified_item);

                    }

//                    for (msibook_certified_item e : Certified_List) {
//
//                        if(e.Favorite.contains("1"))
//                        {
//                            Favorite_Certified_List.add(e);
//                        }
//                        else if(e.Favorite.contains("0") && e.Status.contains("NEW"))
//                        {
//                            Project_Certified_List.add(e);
//                        }
//
//                    }

                    GroupItem.add("我的最愛");
                    GroupItem.add("專案");

                    //Certified_List_Group.add(Favorite_Certified_List);
                    //Certified_List_Group.add(Project_Certified_List);
                    Certified_List_Group.add(Favorite_Certified_List);
                    Certified_List_Group.add(Certified_List);

                    exp_my_certified_list_Adapter = new Certified_List_Adapter(mContext,GroupItem,Certified_List_Group);
                    exp_my_certified_list.setAdapter(exp_my_certified_list_Adapter);

                    //設定Group最大限制
                    for (int i = 0; i < GroupItem.size(); i++) {
                        exp_my_certified_list.expandGroup(i);
                    }



                }
                catch (JSONException ex) {
                    Log.w("Json",ex.toString());
                }
                progressBar.hide();
            }

            @Override
            public void onSendRequestError(String result) {
                Log.w("RequestError",result);
                progressBar.hide();

            }

        }, map);
    }



    //------------------------Item----------------------
    public class msibook_certified_item implements Serializable {

        String ModelID;
        String ModelName;
        String ModelPic;
        String DateDiff;
        String Product_Line;

        public msibook_certified_item(String ModelID,String ModelName,String ModelPic,String DateDiff,String Product_Line) {

            this.ModelID = ModelID;
            this.ModelName = ModelName;
            this.ModelPic = ModelPic;
            this.DateDiff = DateDiff;
            this.Product_Line = Product_Line;
        }

    }


    //-----------------------Adapter-------------------
    public class Certified_List_Adapter extends BaseExpandableListAdapter {

        private LayoutInflater mLayInf;

        private String AdapterType;

        private Context context;

        List<String> groups;

        ArrayList<List<msibook_certified_item>> childs;

//        public void onActivityResult(int requestCode, int resultCode, Intent data) {
//
//            if(data !=null) {
//                Bundle bundle = data.getExtras();
//                Integer CheckBooking = Integer.valueOf(bundle.getString("Certified_Check"));
//                if(CheckBooking == 1){
//                    SaveBackValue = 1;
//                    Find_Certification_Model_List("10003130");// PM
//                    Log.w("哦哦哦哦哦哦","哦哦哦哦哦哦");
//                }
//            }
//
//        }

        public Certified_List_Adapter(Context context, List<String> groups, ArrayList<List<msibook_certified_item>> childs) {
            mLayInf = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            this.groups = groups;
            this.childs = childs;
            this.context = context;
        }

        public Object getChild(int groupPosition, int childPosition) {

            return childs.get(groupPosition).get(childPosition);

        }

        public long getChildId(int groupPosition, int childPosition) {

            return childPosition;

        }

        //獲取二級清單的View物件
        public View getChildView(int groupPosition, final int childPosition, boolean isLastChild, View convertView,
                                 ViewGroup parent) {

            View v = new View(context);

            v =  mLayInf.inflate(R.layout.msibook_certified_pm_item, parent, false);

            final msibook_certified_item msibook_certified_item = (msibook_certified_item) getChild(groupPosition, childPosition);

            final ImageView imageView_project = (ImageView) v.findViewById(R.id.imageView_project);
            final TextView textView_ModelName = (TextView) v.findViewById(R.id.textView_ModelName);
            final LinearLayout linear_Model = (LinearLayout) v.findViewById(R.id.linear_Model);
            final ImageView imageView_favorite = (ImageView) v.findViewById(R.id.imageView_favorite);
            imageView_favorite.setBackgroundResource(R.mipmap.certified_btn_certified_mark_nor);//textView_s_title2

            final TextView textView_s_title1 = (TextView) v.findViewById(R.id.textView_s_title1);
            final TextView textView_s_title2 = (TextView) v.findViewById(R.id.textView_s_title2);
            final TextView textView_s_title3 = (TextView) v.findViewById(R.id.textView_s_title3);

            if(childPosition==0){
                if(SaveBackValue == 1){
                    textView_s_title1.setVisibility(View.VISIBLE);
                    textView_s_title2.setVisibility(View.VISIBLE);
                    textView_s_title3.setVisibility(View.VISIBLE);
                }else{
                    textView_s_title1.setVisibility(View.GONE);
                    textView_s_title2.setVisibility(View.VISIBLE);
                    textView_s_title3.setVisibility(View.VISIBLE);
                }
            }else{
                textView_s_title1.setVisibility(View.GONE);
                textView_s_title2.setVisibility(View.GONE);
                textView_s_title3.setVisibility(View.GONE);
            }


            final Integer[] myFavorite = {0};
            imageView_favorite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(myFavorite[0] == 0){
                        imageView_favorite.setBackgroundResource(R.mipmap.certified_btn_certified_mark_sel);
                        myFavorite[0] = 1;
                        Toast.makeText(msibook_certified_main_for_pm.this, "專案 "+msibook_certified_item.ModelName+" 加入我的最愛", Toast.LENGTH_SHORT).show();
                    }else{
                        imageView_favorite.setBackgroundResource(R.mipmap.certified_btn_certified_mark_nor);
                        myFavorite[0] = 0;
                        Toast.makeText(msibook_certified_main_for_pm.this, "取消專案 "+msibook_certified_item.ModelName+" 我的最愛", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            textView_ModelName.setText(msibook_certified_item.ModelName);

            //抓圖片路徑
            if(msibook_certified_item.ModelPic.length()>5){ //有資料就讀取 沒資料就跳過

                Glide.with(msibook_certified_main_for_pm.this)
                        .load("http://wtsc.msi.com.tw/IMS/IMS_App_Service.asmx/Get_File?FileName=" + msibook_certified_item.ModelPic)
                        .asBitmap()
                        .into(new SimpleTarget<Bitmap>(300, 300) {
                            @Override
                            public void onResourceReady(Bitmap bitmap, GlideAnimation anim) {

                                //BitmapDrawable ob = new BitmapDrawable(getResources(), AppClass.roundCornerImage(bitmap,0));
                                //Img_ProjectInfo.setBackground(ob);

                                BitmapDrawable ob = new BitmapDrawable(getResources(), bitmap);
                                imageView_project.setImageBitmap(ob.getBitmap());
                                //Img_Project_Pic_Large.setBackground(ob);
                                //Rel_Project_Layout.setBackground(ob);
                            }
                        });
            }


//            if (msibook_certified_item.Favorite.contains("1"))
//            {
//                imageView_favorite.setBackgroundResource(R.mipmap.btn_star_sel);
//            }
//            else
//            {
//                imageView_favorite.setBackgroundResource(R.mipmap.btn_star_nor);
//            }

            //點選我的最愛
//            imageView_favorite.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Request_Favorite_Update(msibook_request_item.F_SeqNo,UserData.WorkID);
//                }
//            });

            //內容被點選
            linear_Model.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //專案代碼
                    Log.w("專案代碼:",msibook_certified_item.ModelID);
                    //專案名稱
                    Log.w("專案名稱:",msibook_certified_item.ModelName);
                    Intent intent = new Intent();

                    intent.putExtra("ModelID", msibook_certified_item.ModelID);

                    intent.putExtra("ModelName", msibook_certified_item.ModelName);

                    intent.putExtra("ModelPic", msibook_certified_item.ModelPic);

                    intent.putExtra("DateDiff", msibook_certified_item.DateDiff);

                    intent.putExtra("Product_Line", msibook_certified_item.Product_Line);

                    intent.putExtra("Favorite",String.valueOf(myFavorite[0]));

                    intent.setClass(msibook_certified_main_for_pm.this, msibook_certified_info.class);//跳轉頁面至第二頁
                    //開啟Activity
                    startActivityForResult(intent,1);
                }
            });



            return v;
        }




        public int getChildrenCount(int groupPosition) {
            if (groups.size() == 0) {
                return 0;
            } else {
                if (childs.size() == 0) {

                    return 0;
                } else {

                    return childs.get(groupPosition).size();
                }

            }
        }

        public Object getGroup(int groupPosition) {


            return groups.get(groupPosition);
        }

        public int getGroupCount() {
            //Log.w("GroupSize",String.valueOf(groups.size()));


            return groups.size();
        }

        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        //獲取一級清單View物件
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            String text = groups.get(groupPosition);
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

//獲取一級清單佈局檔,設置相應元素屬性
            RelativeLayout RelativeLayout = (RelativeLayout) layoutInflater.inflate(R.layout.msibook_certified_group_item, null);
            TextView textView_Title = (TextView) RelativeLayout.findViewById(R.id.textView_Title);
            textView_Title.setText(text);
            ImageView parentImageViw = (ImageView) RelativeLayout.findViewById(R.id.parentImageViw);

            if (isExpanded)
            {
                parentImageViw.setBackgroundResource(R.mipmap.certified_btn_certified_collapse);
            }
            else
            {
                parentImageViw.setBackgroundResource(R.mipmap.certified_btn_certified_unfolded);
            }

            return RelativeLayout;
        }

        public boolean hasStableIds() {
            return false;
        }

        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return false;
        }

    }



}
