package dqa.com.msibook;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class msibook_dqaweekly_resource_delay_night_detail extends AppCompatActivity {

    private ProgressDialog progressBar;

    private Context mContext;

    private String SetYear;

    private String SetWeek;

    private String SetState;

    private String SetDept_Around;

    private TextView project_list_week;

    private TextView project_list_listtop;

    private ExpandableListAdapter listAdapter;
    private ExpandableListView expListView;

    private List<Delay_Night_Item> Delay_Night_Item_list = new ArrayList<Delay_Night_Item>();
    private List<List<Delay_Night_Item>> ChildDelay_Night_Item_list = new ArrayList<List<Delay_Night_Item>>();


    String Set_print_Date;
    String Set_print_week;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msibook_dqaweekly_resource_delay_night_detail);

        //讀取時間Bar
        progressBar = new ProgressDialog(this);
        progressBar.setCancelable(true);
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.setMessage("資料載入中");

        mContext = this;

        String getYear = getIntent().getStringExtra("Year");//
        SetYear = getYear;
        Log.w("Year",SetYear);

        String getWeek = getIntent().getStringExtra("Week");//
        SetWeek = getWeek;
        Log.w("Week",SetWeek);

        String getState = getIntent().getStringExtra("State");//
        SetState = getState;
        Log.w("State",SetState);

        String getDept_Around = getIntent().getStringExtra("Dept_Around");// 抓第一頁選的部門代號  7850   7860   7870   7880
        SetDept_Around = getDept_Around.substring(0,3);
        Log.w("Dept_Around",SetDept_Around);


        project_list_week = (TextView) findViewById(R.id.project_list_week);

        project_list_listtop = (TextView) findViewById(R.id.project_list_listtop);

        expListView = (ExpandableListView) findViewById(R.id.lvExp);

        project_list_week.setText(SetWeek+"週");

        if(SetState.contains("1")){
            project_list_listtop.setText("遲到");
        }else if(SetState.contains("2")){
            project_list_listtop.setText("夜歸");
        }

        Find_Resource_Detail(SetYear,SetWeek,SetState,SetDept_Around);

    }


    //設定箭頭置右
    public int GetDipsFromPixel(float pixels)
    {
        // Get the screen's density scale
        final float scale = getResources().getDisplayMetrics().density;
        // Convert the dps to pixels, based on density scale
        return (int) (pixels * scale + 0.5f);
    }

    //DB專案細部清單
    private void Find_Resource_Detail(String Year, String Week, final String SetState, final String SetDept_Around) {

        //顯示  讀取等待時間Bar
        progressBar.show();

        Delay_Night_Item_list.clear();
        ChildDelay_Night_Item_list.clear();

        RequestQueue mQueue = Volley.newRequestQueue(this);

        String Path = "http://wtsc.msi.com.tw/IMS/Weekly_Report.asmx/Find_Resource_Detail?Year=" + Year + "&Week=" + Week;

        GetServiceData.getString(Path, mQueue, new GetServiceData.VolleyCallback() {
            @Override
            public void onSuccess(JSONObject result) {

                try {

                    JSONArray UserArray = new JSONArray(result.getString("Key"));

                    final Map<String,ArrayList<Delay_Night_Item>> mapContent = new HashMap<String,ArrayList<Delay_Night_Item>>();

                    List<Delay_Night_Item> fatherList = new ArrayList<>();
                    List<List<Delay_Night_Item>> childList = new ArrayList<>();
                    Map<String, ArrayList<Delay_Night_Item>> linkHashMap = new LinkedHashMap<>();

                    for (int i = 0; i < UserArray.length(); i++) {

                        JSONObject IssueData = UserArray.getJSONObject(i);

                        String F_SeqNo = String.valueOf(IssueData.getInt("F_SeqNo")); //1072554,

                        String F_Keyin = String.valueOf(IssueData.getString("F_Keyin")); // "10003275",

                        String F_ID = String.valueOf(IssueData.getString("F_ID")); // "10015933",

                        String F_Name = String.valueOf(IssueData.getString("F_Name")); // "曾婉婷",

                        String F_Dept = String.valueOf(IssueData.getString("F_Dept")); // "7861",

                        String F_Date = String.valueOf(IssueData.getString("F_Date")); // "2019-03-05T00:00:00",

                        String F_Time = String.valueOf(IssueData.getString("F_Time")); //  "0903",

                        String F_Type = String.valueOf(IssueData.getString("F_Type")); //  "S",

                        String State = String.valueOf(IssueData.getString("State")); //  "1"

                        if(F_Dept.contains(SetDept_Around) && State.contains(SetState)){ //判斷  是否符合上頁選到的部門   跟 狀態為 1的    狀態 遲到", "1",  夜歸,  "2",
                            Log.w("條件符合進入","條件符合進入");
                            if (!linkHashMap.containsKey(F_Dept)){  //加入表內
                                ArrayList<Delay_Night_Item>  innchildList = new ArrayList<>();
                                innchildList.add(new Delay_Night_Item(F_SeqNo,F_Keyin,F_ID,F_Name,F_Dept,F_Date,F_Time,F_Type,State));
                                linkHashMap.put(F_Dept,innchildList);
                            } else {
                                linkHashMap.get(F_Dept).add(new Delay_Night_Item(F_SeqNo,F_Keyin,F_ID,F_Name,F_Dept,F_Date,F_Time,F_Type,State));
                            }
                        }
                    }

                    int index = 0;
                    for (Map.Entry<String, ArrayList<Delay_Night_Item>>  item  : linkHashMap.entrySet()){
                        fatherList.add(index  , item.getValue().get(0)) ;
                        ArrayList<Delay_Night_Item>  tempList = new ArrayList<>();
                        for (Delay_Night_Item childItem : item.getValue()){
                            tempList.add(childItem);
                            Log.w("175行index",String.valueOf(index));
                        }
                        childList.add(index, tempList);
                        Log.w("entrySet長度",String.valueOf(tempList.size()));
                        Log.w("179行index",String.valueOf(index));
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

    //-----------------------------------------------------Item-----------------------------------------------
    public class Delay_Night_Item {

        String F_SeqNo;//

        String F_Keyin;//

        String F_ID;//

        String F_Name;//

        String F_Dept;//

        String F_Date;//

        String F_Time;//

        String F_Type;//

        String State;//

        public Delay_Night_Item(String F_SeqNo,String F_Keyin,String F_ID,String F_Name,String F_Dept,String F_Date,String F_Time,String F_Type,String State)
        {
            this.F_SeqNo = F_SeqNo;

            this.F_Keyin = F_Keyin;

            this.F_ID = F_ID;

            this.F_Name = F_Name;

            this.F_Dept = F_Dept;

            this.F_Date = F_Date;

            this.F_Time = F_Time;

            this.F_Type = F_Type;

            this.State = State;
        }

        public String GetF_SeqNo()
        {
            return this.F_SeqNo;
        }

        public String GetF_Keyin()
        {
            return this.F_Keyin;
        }

        public String GetF_ID()
        {
            return this.F_ID;
        }

        public String GetF_Name()
        {
            return this.F_Name;
        }

        public String GetF_Dept()
        {
            return this.F_Dept;
        }

        public String GetF_Date()
        {
            return this.F_Date;
        }

        public String GetF_Time()
        {
            return this.F_Time;
        }

        public String GetF_Type()
        {
            return this.F_Type;
        }

        public String GetState()
        {
            return this.State;
        }

    }


    //-----------------------------------------------------Adapter-----------------------------------------------
    public class ExpandableListAdapter extends BaseExpandableListAdapter {

        private Context _context;
        private List<Delay_Night_Item> fatherList = new ArrayList<>();
        private List<List<Delay_Night_Item>> childList = new ArrayList<>();

        public ExpandableListAdapter(Context context, List<Delay_Night_Item> fatherList , List<List<Delay_Night_Item>> childList){
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
            return this.childList.get(groupPosition).size();
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
            Delay_Night_Item Delay_Night_Item = (Delay_Night_Item)getGroup(groupPosition);

            String headerTitle = Delay_Night_Item.GetF_Dept();
            if (convertView == null){
                LayoutInflater infalInflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = infalInflater.inflate(R.layout.dqaweekly_resource_delay_night_detail_group,null);   // dqaweekly_resource_delay_night_detail_group

            }
            TextView lblListHeader = (TextView) convertView.findViewById(R.id.lblListHeader);
            lblListHeader.setTypeface(null , Typeface.BOLD);
            lblListHeader.setText(headerTitle);

            return convertView;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

            Delay_Night_Item Delay_Night_Item = (Delay_Night_Item)getChild(groupPosition,childPosition);

            //final String childText = (String) getChild(groupPosition, childPosition);
            if (convertView == null) {
                LayoutInflater infalInflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = infalInflater.inflate(R.layout.dqaweekly_resource_delay_night_detail_child,null);  //dqaweekly_resource_delay_night_detail_child
            }
            TextView txtListChild = (TextView) convertView.findViewById(R.id.lblListItem);

            TextView textView_date = (TextView) convertView.findViewById(R.id.textView_date);

            TextView textView_time = (TextView) convertView.findViewById(R.id.textView_time);

            txtListChild.setText(Delay_Night_Item.F_Name);


            String Set_date = Delay_Night_Item.F_Date.replace("T", " ");

            SimpleDateFormat format1 =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                Date date1 = format1.parse(Set_date);
                Set_print_Date = format1.format(date1);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            SimpleDateFormat format2 =new SimpleDateFormat("E");
            try {
                Date date1 = format1.parse(Set_date);
                Set_print_week = format2.format(date1);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            textView_date.setText(Set_print_Date.substring(0,10) +" ("+Set_print_week.substring(1,2)+") ");

            textView_time.setText("時間"+Delay_Night_Item.F_Time.substring(0,2)+":"+Delay_Night_Item.F_Time.substring(2,4));

            return convertView;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }


    }
}
