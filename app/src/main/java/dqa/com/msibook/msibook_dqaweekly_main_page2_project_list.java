package dqa.com.msibook;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class msibook_dqaweekly_main_page2_project_list extends AppCompatActivity {

    private ProgressDialog progressBar;
    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
    private List<Week_Project_Item>Week_Project_Item_list = new ArrayList<Week_Project_Item>();
    private List<List<Week_Project_Item>> ChildWeek_Project_Item_list = new ArrayList<List<Week_Project_Item>>();

    private String m2putEtrawk;//給第三頁週
    private String m2putTitle;//給第三頁部門名稱

    private String m2putEtraDepID;//給第三頁部門代號
    private String m2putEtraYear;//給第三頁 年
    private String MainTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msibook_dqaweekly_main_page2_project_list);

        //讀取時間Bar
        progressBar = new ProgressDialog(this);
        progressBar.setCancelable(true);
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.setMessage("資料載入中");

        //list的Title
        TextView title1 = (TextView) findViewById(R.id.project_list_listtop);
        TextView textshowwk = (TextView) findViewById(R.id.project_list_week);
        TextView textshowdp = (TextView) findViewById(R.id.project_list_depart);

        String getEtraDepID = getIntent().getStringExtra("ChoiceDepID");//抓第一頁選的部門代號
        m2putEtraDepID = getEtraDepID;
        Log.w("DeptID",getEtraDepID);

        String getEtraYear = getIntent().getStringExtra("Year");//抓第一頁選的部門代號
        m2putEtraYear = getEtraYear;

        String Title = getIntent().getStringExtra("Title");//抓第一頁部門名稱
        m2putTitle = Title;
        textshowdp.setText(Title);
        //m2putTitle = Title;

        String Week = getIntent().getStringExtra("Week");//抓第一頁選的週次
        m2putEtrawk = Week.replace("週","");
        textshowwk.setText(Week);
        String RealWeek = Week.replace("週","");

        String RowType = getIntent().getStringExtra("RowType");//讀前頁部門Title
        title1.setText(RowType);

        // get the listview
        expListView = (ExpandableListView) findViewById(R.id.lvExp);
        Find_Week_Project(getEtraDepID,getEtraYear,RealWeek);
        // preparing list data
        //prepareListData();

        //子項目被點擊事件
        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                //Log.w("子項目:",listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition));
                //專案名稱
                Log.w("專案名稱:",ChildWeek_Project_Item_list.get(groupPosition).get(childPosition).ModelList);
                //專案代碼
                Log.w("專案代碼:",ChildWeek_Project_Item_list.get(groupPosition).get(childPosition).ModelIDList);

                Intent intent = new Intent();

                intent.putExtra("m3Week", m2putEtrawk);//給第三頁Week

                intent.putExtra("m3Year", m2putEtraYear);//給第三頁Week

                intent.putExtra("m3ChoiceDepID", m2putEtraDepID);//給第三頁部門代號

                intent.putExtra("m3putTitle", m2putTitle);//給第三頁部門名稱

                intent.putExtra("m3Rowtype",MainTitle);//給第三頁 選擇的 Type  EX 加班OR請假OR專案

                //intent.putExtra("Mantitle",Week_Project_Item_list.get(position).GetF_Map());//給第三頁 Title EX:MP、EVT、DVT
                intent.putExtra("m3ModelList",ChildWeek_Project_Item_list.get(groupPosition).get(childPosition).ModelList);//給第三頁 MS-7A61  MS-7A81
                intent.putExtra("m3ModelIDList",ChildWeek_Project_Item_list.get(groupPosition).get(childPosition).ModelIDList);//給第三頁 12637  12638

                //intent.setClass(msibook_dqaweekly_main_page2_project_list.this, Main4Activity.class);
                //開啟Activity
                startActivity(intent);


                return false;
            }
        });

        //設定箭頭置右
        DisplayMetrics metrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(metrics);

        expListView.setIndicatorBounds(metrics.widthPixels - GetDipsFromPixel(50), metrics.widthPixels - GetDipsFromPixel(20));

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
    private void Find_Week_Project(String DeptID,String Year,String Week) {

        //顯示  讀取等待時間Bar
        progressBar.show();

        Week_Project_Item_list.clear();
        ChildWeek_Project_Item_list.clear();
        RequestQueue mQueue = Volley.newRequestQueue(this);

        String Path = GetServiceData.DQAWeeklyPath +"/Find_Week_Project?DeptID=" + DeptID + "&Year=" + Year + "&Week=" + Week;

        GetServiceData.getString(Path, mQueue, new GetServiceData.VolleyCallback() {
            @Override
            public void onSuccess(JSONObject result) {

                try {

                    JSONArray UserArray = new JSONArray(result.getString("Key"));

                    for (int i = 0; i < UserArray.length(); i++) {

                        Log.w("UserArrayLength",String.valueOf(UserArray.length()));
//                        "F_Map": "EVT",
//                                "Cnt": 2,
//                                "ModelList": "MS-7A81,MS-7A89",
//                                "ModelIDList": "12638,12705"

                        JSONObject IssueData = UserArray.getJSONObject(i);

                        String F_Map = String.valueOf(IssueData.getString("F_Map"));
                        String Cnt = String.valueOf(IssueData.getString("Cnt"));

                        String ModelList = String.valueOf(IssueData.getString("ModelList"));

                        String ModelIDList = String.valueOf(IssueData.getString("ModelIDList"));

                        Week_Project_Item_list.add(i,new Week_Project_Item(F_Map,Cnt,ModelList,ModelIDList));

                        String[] ModelNameArray = ModelList.split(",");

                        String[] ModelIDArray = ModelIDList.split(",");

                        List<Week_Project_Item> mChild_Project_Item_list = new ArrayList<Week_Project_Item>();

                        for (int a = 0;a<ModelNameArray.length; a++)
                        {
                            mChild_Project_Item_list.add(a,new Week_Project_Item(F_Map,Cnt,ModelNameArray[a],ModelIDArray[a]));
                        }
                        ChildWeek_Project_Item_list.add(i,mChild_Project_Item_list);
                    }

                    listAdapter = new ExpandableListAdapter(msibook_dqaweekly_main_page2_project_list.this, Week_Project_Item_list, ChildWeek_Project_Item_list);
                    // setting list adapter
                    expListView.setAdapter(listAdapter);
                    //關閉-讀取等待時間Bar
                    progressBar.dismiss();

                    for (int i = 0 ;i< Week_Project_Item_list.size();i++)
                    {
                        expListView.expandGroup(i);
                    }
                }
                catch (JSONException ex) {
                    Log.w("Json",ex.toString());
                }
            }
        });
    }

    //-----------------------------------------------------Item-----------------------------------------------
    public class Week_Project_Item {

        String F_Map;//DVT MVT MP

        String Cnt;// 1   2 3 4

        String ModelList;//MS-0027     MS-7A61    MS-7A81

        String ModelIDList;// 1048        12637        12638

        public Week_Project_Item(String F_Map,String Cnt,String ModelList,String ModelIDList)
        {
            this.F_Map = F_Map;

            this.Cnt = Cnt;

            this.ModelList = ModelList;

            this.ModelIDList = ModelIDList;
        }

        public String GetF_Map()
        {
            return this.F_Map;
        }

        public String GetCnt()
        {
            return this.Cnt;
        }

        public String GetModelList()
        {
            return this.ModelList;
        }
        public String GetModelIDList()
        {
            return this.ModelIDList;
        }

    }


    //-----------------------------------------------------Adapter-----------------------------------------------
    public class ExpandableListAdapter extends BaseExpandableListAdapter {

        private Context _context;
        private List<Week_Project_Item>Week_Project_Item_list;

        private List<List<Week_Project_Item>> ChildWeek_Project_Item_list;

        public ExpandableListAdapter(Context context,List<Week_Project_Item> mWeek_Project_Item_list,
                                     List<List<Week_Project_Item>> mChildWeek_Project_Item_list){
            this._context = context;

            this.Week_Project_Item_list = mWeek_Project_Item_list;

            this.ChildWeek_Project_Item_list = mChildWeek_Project_Item_list;

        }

        @Override
        public int getGroupCount() {
            return this.Week_Project_Item_list.size();
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return this.ChildWeek_Project_Item_list.get(groupPosition).size();
        }

        @Override
        public Object getGroup(int groupPosition) {
            return this.Week_Project_Item_list.get(groupPosition);
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return this.ChildWeek_Project_Item_list.get(groupPosition).get(childPosition);
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
            Week_Project_Item Week_Project_Item = (Week_Project_Item)getGroup(groupPosition);

            String headerTitle = Week_Project_Item.GetF_Map() + "(" + Week_Project_Item.GetCnt() + ")";
            if (convertView == null){
                LayoutInflater infalInflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = infalInflater.inflate(R.layout.msibook_dqaweekly_project_list_group,null);

            }
            TextView lblListHeader = (TextView) convertView.findViewById(R.id.lblListHeader);
            lblListHeader.setTypeface(null , Typeface.BOLD);
            lblListHeader.setText(headerTitle);

            return convertView;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

            Week_Project_Item Week_Project_Item = (Week_Project_Item)getChild(groupPosition,childPosition);

            //final String childText = (String) getChild(groupPosition, childPosition);
            if (convertView == null) {
                LayoutInflater infalInflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = infalInflater.inflate(R.layout.msibook_dqaweekly_project_list_child,null);
            }
            TextView txtListChild = (TextView) convertView.findViewById(R.id.lblListItem);

            txtListChild.setText(Week_Project_Item.ModelList);
            return convertView;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }


    }

}
