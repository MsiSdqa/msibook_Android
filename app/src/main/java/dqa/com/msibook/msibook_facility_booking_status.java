package dqa.com.msibook;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
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

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class msibook_facility_booking_status extends AppCompatActivity {

    private ListView mListView;
    private msibook_facility_booking_status_adapter Fac_Schedule_List_Adapter;
    private List<msibook_facility_booking_status_item> msibook_facility_booking_status_item_List = new ArrayList<msibook_facility_booking_status_item>();
    private ProgressDialog progressBar;
    private Context mContext;

    private TextView textView_title2;

    private String F_SeqNo;
    private String F_Facility;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msibook_facility_booking_status);

        progressBar = new ProgressDialog(this);
        progressBar.setCancelable(true);
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.setMessage("資料載入中");
        mContext = msibook_facility_booking_status.this;

        F_SeqNo = getIntent().getStringExtra("F_SeqNo");
        F_Facility = getIntent().getStringExtra("F_Facility");

        textView_title2 = (TextView) findViewById(R.id.textView_title2);

        textView_title2.setText(F_Facility);
        Find_Fac_Schedule_List(F_SeqNo);

    }

    private void Find_Fac_Schedule_List(String F_Master_ID) {

        //顯示  讀取等待時間Bar
        progressBar.show();

        msibook_facility_booking_status_item_List.clear();

        RequestQueue mQueue = Volley.newRequestQueue(this);

        String Path = GetServiceData.ServicePath + "/Find_Fac_Schedule_List?F_Master_ID=" + F_Master_ID;

        GetServiceData.getString(Path, mQueue, new GetServiceData.VolleyCallback() {
            @Override
            public void onSuccess(JSONObject result) {

                try {

                    JSONArray UserArray = new JSONArray(result.getString("Key"));

                    for (int i = 0; i < UserArray.length(); i++) {

                        JSONObject IssueData = UserArray.getJSONObject(i);

                        String F_Owner = String.valueOf(IssueData.getString("F_Owner"));//"陳信中",

                        String F_Master_ID = String.valueOf(IssueData.getInt("F_Master_ID"));//136,

                        String F_StartDate = String.valueOf(IssueData.getString("F_StartDate"));//"2018-02-21T00:00:00",

                        String F_EndDate = String.valueOf(IssueData.getString("F_EndDate"));//"2018-02-28T23:30:00",

                        String ModelName = String.valueOf(IssueData.getString("ModelName"));//"MS-7A88",

                        String F_Desc = String.valueOf(IssueData.getString("F_Desc"));// "test"

                        msibook_facility_booking_status_item_List.add(i,new msibook_facility_booking_status.msibook_facility_booking_status_item(F_Owner,F_Master_ID,F_StartDate,F_EndDate,ModelName,F_Desc));

                    }

                    mListView = (ListView)findViewById(R.id.Lsv_Facility);

                    Fac_Schedule_List_Adapter = new msibook_facility_booking_status.msibook_facility_booking_status_adapter(mContext,msibook_facility_booking_status_item_List);

                    mListView.setAdapter(Fac_Schedule_List_Adapter);

                    //關閉-讀取等待時間Bar
                    progressBar.dismiss();

                }
                catch (JSONException ex) {

                    Log.w("Json",ex.toString());

                }

            }
        });

    }

    public static String getDateTimeFormat(String dateTime, String oldFmt, String newFmt, Locale area) {
        java.text.SimpleDateFormat fmt = new java.text.SimpleDateFormat(oldFmt, area);
        java.util.Date date;
        try {
            date = fmt.parse(dateTime);
            fmt.applyPattern(newFmt);

            return fmt.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateTime;//轉換產生錯誤時，回傳原始的日期時間
    }

    public class msibook_facility_booking_status_adapter extends BaseAdapter {

        private LayoutInflater mLayInf;

        private List<msibook_facility_booking_status_item> msibook_facility_booking_status_item_List = new ArrayList<msibook_facility_booking_status_item>();

        private Context ProjectContext;


        public msibook_facility_booking_status_adapter(Context context, List<msibook_facility_booking_status_item> msibook_facility_booking_status_item) {
            mLayInf = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            ProjectContext = context;

            this.msibook_facility_booking_status_item_List = msibook_facility_booking_status_item;

        }

        @Override
        public int getCount() {
            return msibook_facility_booking_status_item_List.size();
        }

        @Override
        public Object getItem(int position) {
            return msibook_facility_booking_status_item_List.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View v = new View(ProjectContext);

            v = mLayInf.inflate(R.layout.activity_msibook_facility_booking_status_item, parent, false);

            TextView textView_project_title = (TextView) v.findViewById(R.id.textView_project_title);
            TextView textView_booking_name = (TextView) v.findViewById(R.id.textView_booking_name);

            //欲轉換的日期字串
            String Start_date = msibook_facility_booking_status_item_List.get(position).GetF_StartDate().substring(0,msibook_facility_booking_status_item_List.get(position).GetF_StartDate().length()-9);
            TextView textView_start_date = (TextView) v.findViewById(R.id.textView_start_date);

            String Start_time = getDateTimeFormat(msibook_facility_booking_status_item_List.get(position).GetF_StartDate(), "yyyy-MM-dd'T'hh:mm:ss", "hh:mm:ss a", java.util.Locale.US);
            TextView textView_start_time = (TextView) v.findViewById(R.id.textView_start_time);

            //欲轉換的日期字串
            String End_date = msibook_facility_booking_status_item_List.get(position).GetF_EndDate().substring(0,msibook_facility_booking_status_item_List.get(position).GetF_EndDate().length()-9);
            TextView textView_end_date = (TextView) v.findViewById(R.id.textView_end_date);

            String End_time = getDateTimeFormat(msibook_facility_booking_status_item_List.get(position).GetF_EndDate(), "yyyy-MM-dd'T'hh:mm:ss", "hh:mm:ss a", java.util.Locale.US);
            TextView textView_end_time = (TextView) v.findViewById(R.id.textView_end_time);

            textView_project_title.setText(msibook_facility_booking_status_item_List.get(position).GetModelName());
            textView_booking_name.setText(msibook_facility_booking_status_item_List.get(position).GetF_Owner());

            textView_start_date.setText(Start_date);
            textView_start_time.setText(Start_time);

            textView_end_date.setText(End_date);
            textView_end_time.setText(End_time);

//            if(msibook_facility_item_List.get(position).GetF_Status().indexOf("0")==-1){
//                if(msibook_facility_item_List.get(position).GetUsing().indexOf("1")==-1)
//                {
//                    txt_Status.setText("可預約");
//                    txt_Status.setTextColor(Color.parseColor("#ffffff"));
//                    txt_Status.setBackgroundColor(Color.parseColor("#3cd45b"));
//
//                }else{
//                    txt_Status.setText("使用中");
//                    txt_Status.setTextColor(Color.parseColor("#ffffff"));
//                    txt_Status.setBackgroundColor(Color.parseColor("#ed4a47"));
//                }
//
//            }else{
//                txt_Status.setText("不開放");
//                txt_Status.setTextColor(Color.parseColor("#6d7073"));
//                txt_Status.setBackgroundColor(Color.parseColor("#d8d8db"));
//
//            }



            return v;
        }
    }

    public class msibook_facility_booking_status_item {

        String F_Owner;
        String F_Master_ID;
        String F_StartDate;
        String F_EndDate;
        String ModelName;
        String F_Desc;


        public msibook_facility_booking_status_item(String F_Owner,String F_Master_ID,String F_StartDate, String F_EndDate, String ModelName, String F_Desc)
        {
            this.F_Owner = F_Owner;
            this.F_Master_ID = F_Master_ID;
            this.F_StartDate = F_StartDate;
            this.F_EndDate = F_EndDate;
            this.ModelName = ModelName;
            this.F_Desc = F_Desc;

        }

        public String GetF_Owner()
        {
            return this.F_Owner;
        }

        public String GetF_Master_ID()
        {
            return this.F_Master_ID;
        }

        public String GetF_StartDate()
        {
            return this.F_StartDate;
        }


        public String GetF_EndDate()
        {
            return this.F_EndDate;
        }
        public String GetModelName()
        {
            return this.ModelName;
        }

        public String GetF_Desc()
        {
            return this.F_Desc;
        }



    }


}
