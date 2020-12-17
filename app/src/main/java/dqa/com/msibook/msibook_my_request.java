package dqa.com.msibook;


import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.acl.Group;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class msibook_my_request extends AppCompatActivity {

    private ProgressDialog progressBar;

    private List<msibook_request_item> Request_List = new ArrayList<msibook_request_item>();

    private List<String> GroupItem = new ArrayList<String>();

    private List<msibook_request_item> Favorite_Request_List = new ArrayList<msibook_request_item>();

    private List<msibook_request_item> Ongoing_Request_List = new ArrayList<msibook_request_item>();

    private List<msibook_request_item> Finish_Request_List = new ArrayList<msibook_request_item>();

    private ArrayList<List<msibook_request_item>> Request_List_Group = new ArrayList<List<msibook_request_item>>();

    private ExpandableListView exp_my_request_list;

    private Request_List_Adapter exp_requestList_Adapter;

    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msibook_my_request);
        mContext = this;
        progressBar = new ProgressDialog(this);
        progressBar.setCancelable(true);
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.setMessage("資料載入中");

        exp_my_request_list = (ExpandableListView) findViewById(R.id.exp_my_request_list);

        Request_List(UserData.WorkID);
        //Request_List("10013055");

        Button btn_new_request = (Button)findViewById(R.id.btn_new_request);

        btn_new_request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, msibook_request_add.class);

                startActivity(intent);
            }
        });
    }


    private void Request_List(String F_Keyin) {

        //顯示  讀取等待時間Bar
        progressBar.show();

        Request_List.clear();

        Favorite_Request_List.clear();

        Ongoing_Request_List.clear();

        Finish_Request_List.clear();

        GroupItem.clear();

        Request_List_Group.clear();

        RequestQueue mQueue = Volley.newRequestQueue(this);

        Map<String,String> map = new HashMap<String, String>();
        map.put("WorkID", F_Keyin);

        if (mQueue == null) {
            mQueue = Volley.newRequestQueue(this);
        }

        HTTPSTrustManager.allowAllSSL();//信任所有证书，信任憑證
        String Path = GetServiceData.ServicePath + "/Request_List";
        //String Path = "http://wtsc.msi.com.tw/IMS/MsiBook_App_Service.asmx/Insert_E_HR_Master";

        GetServiceData.SendPostRequest(Path, mQueue, new GetServiceData.VolleyStringCallback() {
            @Override
            public void onSendRequestSuccess(String result) {

                try {

                    JSONObject obj = new JSONObject(result);

                    JSONArray UserArray = new JSONArray(obj.getString("Key"));

                    for (int i = 0; i < UserArray.length(); i++) {

                        JSONObject ReuqestData = UserArray.getJSONObject(i);

                        String F_SeqNo = String.valueOf(ReuqestData.getInt("F_SeqNo"));
                        String IMG = String.valueOf(ReuqestData.getString("IMG"));
                        String F_NO = String.valueOf(ReuqestData.getString("F_NO"));
                        String F_CreateDate = String.valueOf(ReuqestData.getString("F_CreateDate"));
                        String F_UpdateTime = String.valueOf(ReuqestData.getString("F_UpdateTime"));
                        String F_Keyin = String.valueOf(ReuqestData.getString("F_Keyin"));
                        String F_Owner = String.valueOf(ReuqestData.getString("F_Owner"));
                        String F_Status = String.valueOf(ReuqestData.getString("F_Status"));
                        String Status = String.valueOf(ReuqestData.getString("Status"));
                        String F_Subject = String.valueOf(ReuqestData.getString("F_Subject"));
                        String F_Desc = String.valueOf(ReuqestData.getString("F_Desc"));
                        String F_Grade = String.valueOf(ReuqestData.getString("F_Grade"));
                        String Grade = String.valueOf(ReuqestData.getString("Grade"));
                        String F_Grade_Note = String.valueOf(ReuqestData.getString("F_Grade_Note"));
                        String F_AssinUser = String.valueOf(ReuqestData.getString("F_AssinUser"));
                        String AssinUser = String.valueOf(ReuqestData.getString("AssinUser"));
                        String F_RespUser = String.valueOf(ReuqestData.getString("F_RespUser"));
                        String RespUser = String.valueOf(ReuqestData.getString("RespUser"));
                        String F_SDate = String.valueOf(ReuqestData.getString("F_SDate"));
                        String F_EDate = String.valueOf(ReuqestData.getString("F_EDate"));
                        String F_ExpectFixDate = "";
                        if  (ReuqestData.isNull("F_ExpectFixDate"))
                        {
                            F_ExpectFixDate = "N/A";
                        }
                        else{
                            F_ExpectFixDate = String.valueOf(ReuqestData.getString("F_ExpectFixDate"));
                        }

                        String F_Master_Table = String.valueOf(ReuqestData.getString("F_Master_Table"));
                        String F_Master_ID = "";
                        if  (ReuqestData.isNull("F_Master_ID"))
                        {
                            F_Master_ID = "0";
                        }
                        else{
                            F_Master_ID = String.valueOf(ReuqestData.getInt("F_Master_ID"));
                        }


                        String DueDays = String.valueOf(ReuqestData.getInt("DueDays"));

                        String OverDueDays = "";

                        if  (ReuqestData.isNull("OverDueDays"))
                        {
                            OverDueDays = "0";
                        }
                        else{
                            OverDueDays = String.valueOf(ReuqestData.getInt("OverDueDays"));
                        }

                        String Is_Over = String.valueOf(ReuqestData.getString("Is_Over"));
                        String Favorite = String.valueOf(ReuqestData.getInt("Favorite"));

                        msibook_request_item msibook_request_item = new msibook_request_item(F_SeqNo,IMG,F_NO,F_CreateDate,F_UpdateTime,F_Keyin,F_Owner,F_Status,Status,F_Subject,F_Desc,F_Grade,Grade,F_Grade_Note,F_AssinUser,AssinUser,F_RespUser,RespUser,F_SDate,F_EDate,F_ExpectFixDate,F_Master_Table,F_Master_ID,DueDays,OverDueDays,Is_Over,Favorite);

                        Request_List.add(msibook_request_item);

                    }

                    for (msibook_request_item e : Request_List) {

                        if(e.Favorite.contains("1"))
                        {
                            Favorite_Request_List.add(e);
                        }
                        else if(e.Favorite.contains("0") && e.Status.contains("NEW"))
                        {
                            Ongoing_Request_List.add(e);
                        }
                        else if (e.Favorite.contains("0") && e.Status.contains("CLOSE"))
                        {
                            Finish_Request_List.add(e);
                        }

                    }

                    GroupItem.add("我的最愛");
                    GroupItem.add("進行中");
                    GroupItem.add("已完成");

                    Request_List_Group.add(Favorite_Request_List);
                    Request_List_Group.add(Ongoing_Request_List);
                    Request_List_Group.add(Finish_Request_List);
                    exp_requestList_Adapter = new Request_List_Adapter(mContext,GroupItem,Request_List_Group);
                    exp_my_request_list.setAdapter(exp_requestList_Adapter);

                    for (int i = 0; i < GroupItem.size(); i++) {
                        exp_my_request_list.expandGroup(i);
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


    private void Request_Favorite_Update(String F_SeqNo,String F_Keyin) {

        RequestQueue mQueue = Volley.newRequestQueue(this);

        Map<String,String> map = new HashMap<String, String>();
        map.put("F_SeqNo", F_SeqNo);
        map.put("F_Keyin", F_Keyin);
        if (mQueue == null) {
            mQueue = Volley.newRequestQueue(this);
        }

        HTTPSTrustManager.allowAllSSL();//信任所有证书，信任憑證
        String Path = GetServiceData.ServicePath + "/Request_Favorite_Update";
        //String Path = "http://wtsc.msi.com.tw/IMS/MsiBook_App_Service.asmx/Insert_E_HR_Master";

        GetServiceData.SendPostRequest(Path, mQueue, new GetServiceData.VolleyStringCallback() {
            @Override
            public void onSendRequestSuccess(String result) {
                Request_List(UserData.WorkID);
                exp_requestList_Adapter.notifyDataSetChanged();
            }

            @Override
            public void onSendRequestError(String result) {
                Log.w("RequestError",result);
                progressBar.hide();
            }

        }, map);
    }
    public class Request_List_Adapter extends BaseExpandableListAdapter {

        private LayoutInflater mLayInf;

        private String AdapterType;

        private Context context;

        List<String> groups;

        ArrayList<List<msibook_request_item>> childs;

        protected void onActivityResult(int requestCode, int resultCode, Intent data) {

            if(data !=null) {
                Request_List(UserData.WorkID);
            }

        }

        public Request_List_Adapter(Context context, List<String> groups, ArrayList<List<msibook_request_item>> childs) {
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

            v =  mLayInf.inflate(R.layout.msibook_request_my_item, parent, false);

            final msibook_request_item msibook_request_item = (msibook_request_item) getChild(groupPosition, childPosition);
            //final TextView textView_name = (TextView) v.findViewById(R.id.textView_name);
            final TextView textView_date = (TextView) v.findViewById(R.id.textView_date);
            final TextView textView_subject = (TextView) v.findViewById(R.id.textView_subject);
            final ImageView imageView_favorite = (ImageView) v.findViewById(R.id.imageView_favorite);
            final TextView textView_hour = (TextView) v.findViewById(R.id.textView_hour);




                if (msibook_request_item.F_CreateDate.indexOf("N/A") == -1){
                    String dtStart = msibook_request_item.F_CreateDate.replace("T"," ");

                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                    try {
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        Date date = format.parse(dtStart);
                        String dateString = sdf.format(date);
                        textView_date.setText(dateString);

                        Date NowDate = new Date();
                        long diff = date.getTime() - NowDate.getTime();

                    if (diff <= 0)
                    {
                        textView_hour.setText("0");
                    }
                    else
                    {
                        textView_hour.setText(String.valueOf(diff/3600000));
                    }

                    } catch (ParseException e) {
                        Log.w("ErrorDate",e.toString());
                    }

                }else{
                    textView_hour.setText("- -");
                }




            textView_subject.setText(msibook_request_item.F_Subject);

            if (msibook_request_item.Favorite.contains("1"))
            {
                imageView_favorite.setBackgroundResource(R.mipmap.btn_star_sel);
            }
            else
            {
                imageView_favorite.setBackgroundResource(R.mipmap.btn_star_nor);
            }

            imageView_favorite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Request_Favorite_Update(msibook_request_item.F_SeqNo,UserData.WorkID);
                }
            });

            textView_subject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(context, msibook_request_detail.class);

                    Bundle bundle=new Bundle();

                    bundle.putSerializable("CustomObject ", msibook_request_item);

                    intent.putExtras(bundle);
                    startActivity(intent);
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
            RelativeLayout RelativeLayout = (RelativeLayout) layoutInflater.inflate(R.layout.msibook_request_group_item, null);
            TextView textView_people_count = (TextView) RelativeLayout.findViewById(R.id.textView_people_count);
            textView_people_count.setText(text);
            ImageView parentImageViw = (ImageView) RelativeLayout.findViewById(R.id.parentImageViw);

            if (isExpanded)
            {
                parentImageViw.setBackgroundResource(R.mipmap.ehr_btn_common_collapse);
            }
            else
            {
                parentImageViw.setBackgroundResource(R.mipmap.ehr_btn_common_unfolded);
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
