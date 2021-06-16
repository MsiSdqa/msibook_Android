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
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class msibook_requset_form_main extends AppCompatActivity {

    private ProgressDialog progressBar;

    private List<msibook_requset_form_item> Requset_Form_List = new ArrayList<msibook_requset_form_item>();

    private List<String> GroupItem = new ArrayList<String>();

    private List<msibook_requset_form_item> Cancel_Request_Form_List = new ArrayList<msibook_requset_form_item>();

    private List<msibook_requset_form_item> Ongoing_Request_Form_List = new ArrayList<msibook_requset_form_item>();

    private List<msibook_requset_form_item> Finish_Request_Form_List = new ArrayList<msibook_requset_form_item>();

    private ArrayList<List<msibook_requset_form_item>> Request_Form_List_Group = new ArrayList<List<msibook_requset_form_item>>();

    private ExpandableListView exp_my_requset_form_list;

    private Request_Form_List_Adapter exp_requset_form_List_Adapter;

    private Context mContent;

    private Button btn_chart;

    private String Year;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msibook_requset_form_main);

        mContent = this;
        progressBar = new ProgressDialog(this);
        progressBar.setCancelable(true);
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.setMessage("資料載入中");

        exp_my_requset_form_list = (ExpandableListView) findViewById(R.id.exp_my_requset_form_list);
        exp_my_requset_form_list.setGroupIndicator(null);//將控件默認的左邊箭頭去掉，

        btn_chart= (Button) findViewById(R.id.btn_chart);

        if(UserData.WorkID.contains("10013055")||UserData.WorkID.contains("10015812")){
            btn_chart.setVisibility(View.VISIBLE);
        }else{
            btn_chart.setVisibility(View.INVISIBLE);
        }

        //Find_RequestForm_list("2018","10013055");
        //Find_RequestForm_list("2018","10015700");
        Calendar yy = Calendar.getInstance();
        Year = String.valueOf(yy.get(Calendar.YEAR));
        Find_RequestForm_list(Year,UserData.WorkID);
        Log.w("測試資料",Year + " -- " + UserData.WorkID);

        btn_chart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent IntentChart = new Intent(mContent,msibook_requset_form_chart.class);
                startActivityForResult(IntentChart,1);
            }
        });
    }


    private void Find_RequestForm_list(String year,String F_RespUser) {

        //顯示  讀取等待時間Bar
        progressBar.show();

        Requset_Form_List.clear();

        Cancel_Request_Form_List.clear();

        Ongoing_Request_Form_List.clear();

        Finish_Request_Form_List.clear();

        GroupItem.clear();

        Request_Form_List_Group.clear();
        HTTPSTrustManager.allowAllSSL();//信任所有证书，信任憑證
        RequestQueue mQueue = Volley.newRequestQueue(this);

        Map<String,String> map = new HashMap<String, String>();
        map.put("year", year);
        map.put("F_RespUser", F_RespUser);

        if (mQueue == null) {
            mQueue = Volley.newRequestQueue(this);
        }

        //String Path = GetServiceData.ServicePath + "/Find_RequestForm_list";

        String Path = "http://wtsc.msi.com.tw/IMS/MsiBook_App_Service.asmx/Find_RequestForm_list";
        //String Path = "http://wtsc.msi.com.tw/Test/MsiBook_App_Service.asmx/Find_RequestForm_list";
        //String Path = "http://wtsc.msi.com.tw/IMS/MsiBook_App_Service.asmx/Insert_E_HR_Master";

        GetServiceData.SendPostRequest(Path, mQueue, new GetServiceData.VolleyStringCallback() {
            @Override
            public void onSendRequestSuccess(String result) {

//                String Answer =""; //解決Jason亂碼問題
//                try {
//                    Answer = new String(result.getBytes("ISO-8859-1"), "UTF-8");
//                } catch (UnsupportedEncodingException e) {
//                    e.printStackTrace();
//                }

                try {
                    JSONObject obj = new JSONObject(result);

                    JSONArray UserArray = new JSONArray(obj.getString("Key"));

                    for (int i = 0; i < UserArray.length(); i++) {

                        JSONObject ReuqestData = UserArray.getJSONObject(i);

                        String F_SeqNo = String.valueOf(ReuqestData.getInt("F_SeqNo"));
                        String F_CreateDate = String.valueOf(ReuqestData.getString("F_CreateDate"));
                        String Status = String.valueOf(ReuqestData.getString("Status"));
                        String Model = String.valueOf(ReuqestData.getString("Model"));
                        String F_Memo = String.valueOf(ReuqestData.getString("F_Memo"));
                        String F_ExpectFixDate = String.valueOf(ReuqestData.getString("F_ExpectFixDate"));
                        String AssignUser = String.valueOf(ReuqestData.getString("AssignUser"));
                        String RequestType = String.valueOf(ReuqestData.getString("RequestType"));

                        msibook_requset_form_item msibook_requset_form_item = new msibook_requset_form_item(F_SeqNo,F_CreateDate,Status,Model,F_Memo,F_ExpectFixDate,AssignUser,RequestType);

                        Requset_Form_List.add(msibook_requset_form_item);

                        Log.w("測試服務",F_SeqNo + " -- " + F_CreateDate + " -- " + Status + " -- " + Model + " -- " + F_Memo + " -- " + F_ExpectFixDate + " -- " + AssignUser + " -- " + RequestType);

                    }

                    for (msibook_requset_form_item e : Requset_Form_List) {

                        if(e.Status.contains("已取消"))
                        {
                            Cancel_Request_Form_List.add(e);
                        }
                        else if(e.Status.contains("處理中"))
                        {
                            Ongoing_Request_Form_List.add(e);
                        }
                        else if (e.Status.contains("已完成"))
                        {
                            Finish_Request_Form_List.add(e);
                        }

                    }

                    GroupItem.add("處理中");
                    GroupItem.add("已完成");
                    GroupItem.add("已取消");

                    Request_Form_List_Group.add(Ongoing_Request_Form_List);
                    Request_Form_List_Group.add(Finish_Request_Form_List);
                    Request_Form_List_Group.add(Cancel_Request_Form_List);

                    exp_requset_form_List_Adapter = new Request_Form_List_Adapter(mContent,GroupItem,Request_Form_List_Group);
                    exp_my_requset_form_list.setAdapter(exp_requset_form_List_Adapter);

                    for (int i = 0; i < GroupItem.size(); i++) {
                        exp_my_requset_form_list.expandGroup(i);
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


    public class Request_Form_List_Adapter extends BaseExpandableListAdapter {

        private LayoutInflater mLayInf;

        private String AdapterType;

        private Context context;

        List<String> groups;

        ArrayList<List<msibook_requset_form_item>> childs;

        protected void onActivityResult(int requestCode, int resultCode, Intent data) {

            if(data !=null) {
                Find_RequestForm_list(Year,UserData.WorkID);
            }

        }

        public Request_Form_List_Adapter(Context context, List<String> groups, ArrayList<List<msibook_requset_form_item>> childs) {
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

            v =  mLayInf.inflate(R.layout.msibook_request_form_child_item, parent, false);

            final msibook_requset_form_item msibook_requset_form_item = (msibook_requset_form_item) getChild(groupPosition, childPosition);
            //final TextView textView_name = (TextView) v.findViewById(R.id.textView_name);
            final LinearLayout linear_childmain = (LinearLayout) v.findViewById(R.id.linear_childmain);
            final TextView textView_date = (TextView) v.findViewById(R.id.textView_date);
            final TextView textView_subject = (TextView) v.findViewById(R.id.textView_subject);
            final ImageView imageView_favorite = (ImageView) v.findViewById(R.id.imageView_favorite);
            final TextView textView_project_title = (TextView) v.findViewById(R.id.textView_project_title);

            textView_project_title.setText(msibook_requset_form_item.Model);

            textView_subject.setText(msibook_requset_form_item.F_Memo);

            String dtStart = msibook_requset_form_item.F_CreateDate.replace("T"," ");

            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            try {
                //SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat sdf = new SimpleDateFormat("MM月dd日");
                Date date = format.parse(dtStart);
                String dateString = sdf.format(date);
                textView_date.setText(dateString);

            } catch (ParseException e) {
                Log.w("ErrorDate",e.toString());
            }

            linear_childmain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, msibook_request_form_detail.class);
                    Bundle bundle=new Bundle();
                    bundle.putSerializable("CustomObject ", msibook_requset_form_item);
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
            RelativeLayout RelativeLayout = (RelativeLayout) layoutInflater.inflate(R.layout.msibook_request_form_group_item, null);
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


    //------------Item---------------


}
