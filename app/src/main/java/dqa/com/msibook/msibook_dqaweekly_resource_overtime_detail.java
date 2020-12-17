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

public class msibook_dqaweekly_resource_overtime_detail extends AppCompatActivity {

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

    private List<Overtime_Item> Overtime_Item_list = new ArrayList<Overtime_Item>();
    private List<List<Overtime_Item>> ChildOvertime_Item_list = new ArrayList<List<Overtime_Item>>();


    String Set_print_Date;
    String Set_print_week;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msibook_dqaweekly_resource_overtime_detail);

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

        String getDept_Around = getIntent().getStringExtra("Dept_Around");// 抓第一頁選的部門代號  7850   7860   7870   7880
        SetDept_Around = getDept_Around.substring(0,3);

        project_list_week = (TextView) findViewById(R.id.project_list_week);

        project_list_listtop = (TextView) findViewById(R.id.project_list_listtop);

        expListView = (ExpandableListView) findViewById(R.id.lvExp);

        project_list_week.setText(SetWeek+"週");

        project_list_listtop.setText("加班");


        Find_Over_Hour_Resource_Detail(SetYear,SetWeek,SetDept_Around);

    }

    //DB專案細部清單
    private void Find_Over_Hour_Resource_Detail(String Year, String Week, final String SetDept_Around) {

        //顯示  讀取等待時間Bar
        progressBar.show();

        Overtime_Item_list.clear();
        ChildOvertime_Item_list.clear();

        RequestQueue mQueue = Volley.newRequestQueue(this);

        String Path = "http://wtsc.msi.com.tw/IMS/Weekly_Report.asmx/Find_Over_Hour_Resource_Detail?Year=" + Year + "&Week=" + Week;

        GetServiceData.getString(Path, mQueue, new GetServiceData.VolleyCallback() {
            @Override
            public void onSuccess(JSONObject result) {

                try {

                    JSONArray UserArray = new JSONArray(result.getString("Key"));

                    final Map<String,ArrayList<Overtime_Item>> mapContent = new HashMap<String,ArrayList<Overtime_Item>>();

                    List<Overtime_Item> fatherList = new ArrayList<>();
                    List<List<Overtime_Item>> childList = new ArrayList<>();
                    Map<String, ArrayList<Overtime_Item>> linkHashMap = new LinkedHashMap<>();

                    for (int i = 0; i < UserArray.length(); i++) {

                        JSONObject IssueData = UserArray.getJSONObject(i);

                        String F_Dept = String.valueOf(IssueData.getString("F_Dept")); //"7871",

                        String WorkID = String.valueOf(IssueData.getString("WorkID")); // "10003275",

                        String MonthTotal = String.valueOf(IssueData.getDouble("MonthTotal")); // 14.5,

                        String CName = String.valueOf(IssueData.getString("CName")); // "曾婉婷",

                        String Column1 = String.valueOf(IssueData.getDouble("Column1")); //  6.0

                        if(F_Dept.contains(SetDept_Around)){
                            Log.w("條件符合進入","條件符合進入");
                            if (!linkHashMap.containsKey(F_Dept)){  //加入表內
                                ArrayList<Overtime_Item>  innchildList = new ArrayList<>();
                                innchildList.add(new Overtime_Item(F_Dept,WorkID,MonthTotal,CName,Column1));
                                linkHashMap.put(F_Dept,innchildList);
                            } else {
                                linkHashMap.get(F_Dept).add(new Overtime_Item(F_Dept,WorkID,MonthTotal,CName,Column1));
                            }
                        }
                    }

                    int index = 0;
                    for (Map.Entry<String, ArrayList<Overtime_Item>>  item  : linkHashMap.entrySet()){
                        fatherList.add(index  , item.getValue().get(0)) ;
                        ArrayList<Overtime_Item>  tempList = new ArrayList<>();
                        for (Overtime_Item childItem : item.getValue()){
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
    public class Overtime_Item {

        String F_Dept;//

        String WorkID;//

        String MonthTotal;//

        String CName;//

        String Column1;//

        public Overtime_Item(String F_Dept,String WorkID,String MonthTotal,String CName,String Column1)
        {
            this.F_Dept = F_Dept;

            this.WorkID = WorkID;

            this.MonthTotal = MonthTotal;

            this.CName = CName;

            this.Column1 = Column1;

        }

        public String GetF_Dept()
        {
            return this.F_Dept;
        }

        public String GetWorkID()
        {
            return this.WorkID;
        }

        public String GetMonthTotal()
        {
            return this.MonthTotal;
        }

        public String GetCName()
        {
            return this.CName;
        }

        public String GetColumn1()
        {
            return this.Column1;
        }

    }


    //-----------------------------------------------------Adapter-----------------------------------------------
    public class ExpandableListAdapter extends BaseExpandableListAdapter {

        private Context _context;
        private List<Overtime_Item> fatherList = new ArrayList<>();
        private List<List<Overtime_Item>> childList = new ArrayList<>();

        public ExpandableListAdapter(Context context, List<Overtime_Item> fatherList , List<List<Overtime_Item>> childList){
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
            Overtime_Item Overtime_Item = (Overtime_Item)getGroup(groupPosition);

            String headerTitle = Overtime_Item.GetF_Dept();
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

            Overtime_Item Overtime_Item = (Overtime_Item)getChild(groupPosition,childPosition);

            //final String childText = (String) getChild(groupPosition, childPosition);
            if (convertView == null) {
                LayoutInflater infalInflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = infalInflater.inflate(R.layout.dqaweekly_resource_delay_overtime_child,null);  //dqaweekly_resource_delay_night_detail_child
            }
            TextView txtListChild = (TextView) convertView.findViewById(R.id.lblListItem);

            TextView textView_hour = (TextView) convertView.findViewById(R.id.textView_hour);

            txtListChild.setText(Overtime_Item.CName);

            textView_hour.setText(Overtime_Item.Column1);

            return convertView;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }


    }


}
