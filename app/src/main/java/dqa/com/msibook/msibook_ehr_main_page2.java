package dqa.com.msibook;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class msibook_ehr_main_page2 extends AppCompatActivity {

    private Button btn_creat_pop;
    private TextView toolbar_title;
    private Button btn_add_new_job;
    private ListView eHR_main_list;
    private ProgressDialog progressBar;
    private ExpandableListAdapter listAdapter;
    private ExpandableListView expListView;

    private List<Job_Dpt_Item> Job_Dpt_Item_Item_list = new ArrayList<Job_Dpt_Item>();
    private List<List<Job_detial_Item>> ChildJob_detial_Item_list = new ArrayList<List<Job_detial_Item>>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msibook_ehr_main_page2);

        progressBar = new ProgressDialog(this);
        progressBar.setCancelable(true);
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.setMessage("資料載入中");


        btn_creat_pop = (Button) findViewById(R.id.btn_creat_pop);//漢堡列
        toolbar_title = (TextView) findViewById(R.id.toolbar_title);//Title
        btn_add_new_job = (Button) findViewById(R.id.btn_add_new_job);//新增
        eHR_main_list = (ListView) findViewById(R.id.eHR_main_list);//List

        expListView = (ExpandableListView) findViewById(R.id.exp_job_list);

        String getDpt_number = getIntent().getStringExtra("Dpt_number");//

        String getDpt_name = getIntent().getStringExtra("Dpt_name");//

        String getCount = getIntent().getStringExtra("Count");//

        for(int i=0;i<1;i++){
            Job_Dpt_Item_Item_list.add(i,new Job_Dpt_Item(getDpt_number,getDpt_name,getCount));

            String[] ModelNameArray ={"職等9或10 工程師"};

            String[] ModelIDArray ={""};

            List<Job_detial_Item> mChildJob_detial_Item_list = new ArrayList<Job_detial_Item>();

            for (int a = 0;a<1; a++)
            {
                mChildJob_detial_Item_list.add(a,new Job_detial_Item(ModelNameArray[a],ModelIDArray[a]));
            }
            ChildJob_detial_Item_list.add(i,mChildJob_detial_Item_list);
        }

        listAdapter = new ExpandableListAdapter(msibook_ehr_main_page2.this, Job_Dpt_Item_Item_list, ChildJob_detial_Item_list);
        // setting list adapter
        expListView.setAdapter(listAdapter);

        for (int i = 0 ;i< ChildJob_detial_Item_list.size();i++)
        {
            expListView.expandGroup(i);
        }

        expListView.setGroupIndicator(null);

        //設定箭頭置右
//        DisplayMetrics metrics = new DisplayMetrics();
//        this.getWindowManager().getDefaultDisplay().getMetrics(metrics);
//
//        expListView.setIndicatorBounds(metrics.widthPixels - GetDipsFromPixel(50), metrics.widthPixels - GetDipsFromPixel(20));

    }

    //設定箭頭置右
    public int GetDipsFromPixel(float pixels)
    {
        // Get the screen's density scale
        final float scale = getResources().getDisplayMetrics().density;
        // Convert the dps to pixels, based on density scale
        return (int) (pixels * scale + 0.5f);
    }

    // --------------------------------------------Item--------------------------------------------
    public class Job_Dpt_Item {

        String Dpt_number;

        String Dpt_name;

        String Count;

        public Job_Dpt_Item(String Dpt_number,String Dpt_name,String Count)
        {
            this.Dpt_number = Dpt_number;

            this.Dpt_name = Dpt_name;

            this.Count = Count;
        }


        public String GetDpt_number()
        {
            return this.Dpt_number;
        }

        public String GetDpt_name()
        {
            return this.Dpt_name;
        }

        public String GetCount()
        {
            return this.Count;
        }
    }

    public class Job_detial_Item {

        String Dpt_name;

        String Apply;

        public Job_detial_Item(String Dpt_name,String Apply)
        {
            this.Dpt_name = Dpt_name;

            this.Apply = Apply;

        }


        public String GetDpt_name()
        {
            return this.Dpt_name;
        }

        public String GetApply()
        {
            return this.Apply;
        }

    }

    // --------------------------------------------Adapter--------------------------------------------

    // ExpandableListAdapter
    public class ExpandableListAdapter extends BaseExpandableListAdapter {

        private Context _context;
        private List<Job_Dpt_Item>Job_Dpt_Item_list;

        private List<List<Job_detial_Item>> ChildJob_detial_Item_list;

        public ExpandableListAdapter(Context context,List<Job_Dpt_Item> mJob_Dpt_Item_list,
                                     List<List<Job_detial_Item>> mChildJob_detial_Item_list){
            this._context = context;

            this.Job_Dpt_Item_list = mJob_Dpt_Item_list;

            this.ChildJob_detial_Item_list = mChildJob_detial_Item_list;

        }

        @Override
        public int getGroupCount() {
            return this.Job_Dpt_Item_list.size();
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return this.ChildJob_detial_Item_list.get(groupPosition).size();
        }

        @Override
        public Object getGroup(int groupPosition) {
            return this.Job_Dpt_Item_list.get(groupPosition);
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return this.ChildJob_detial_Item_list.get(groupPosition).get(childPosition);
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
            Job_Dpt_Item Job_Dpt_Item = (Job_Dpt_Item)getGroup(groupPosition);

            if (convertView == null){
                LayoutInflater infalInflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = infalInflater.inflate(R.layout.msibook_ehr_job_group_item,null);
            }

            TextView textView_dpt_number = (TextView) convertView.findViewById(R.id.textView_dpt_number);
            TextView textView_dpt_name = (TextView) convertView.findViewById(R.id.textView_dpt_name);
            TextView textView_people_count = (TextView) convertView.findViewById(R.id.textView_people_count);

            ImageView parentImageViw = (ImageView) convertView.findViewById(R.id.parentImageViw);

            //判斷isExpanded就可以控制是按下還是關閉，同時更換圖片
            if(isExpanded){
                parentImageViw.setBackgroundResource(R.mipmap.ehr_btn_common_collapse);
            }else{
                parentImageViw.setBackgroundResource(R.mipmap.ehr_btn_common_unfolded);
            }

            textView_dpt_number.setText(Job_Dpt_Item.GetDpt_number());
            textView_dpt_name.setText(Job_Dpt_Item.GetDpt_name());
            textView_people_count.setText(Job_Dpt_Item.GetCount());

            return convertView;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

            Job_detial_Item Job_detial_Item = (Job_detial_Item)getChild(groupPosition,childPosition);

            //final String childText = (String) getChild(groupPosition, childPosition);
            if (convertView == null) {
                LayoutInflater infalInflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = infalInflater.inflate(R.layout.msibook_ehr_joblist_detial_adapter_item,null);
            }
            TextView textView_dpt_name = (TextView) convertView.findViewById(R.id.textView_dpt_name);
            TextView textView_apply = (TextView) convertView.findViewById(R.id.textView_apply);

            textView_dpt_name.setText(Job_detial_Item.GetDpt_name());
            //textView_apply.setText(Job_detial_Item.GetApply());

            return convertView;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }
    }
}
