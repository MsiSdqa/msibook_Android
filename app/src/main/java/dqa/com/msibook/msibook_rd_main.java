package dqa.com.msibook;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.necer.ncalendar.calendar.NCalendar;
import com.necer.ncalendar.listener.OnCalendarChangedListener;
import com.necer.ncalendar.utils.MyLog;

import org.joda.time.LocalDate;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class msibook_rd_main extends AppCompatActivity implements OnCalendarChangedListener  {


    //修改成登入資訊
    //private String ID=ID="10015423";;//ID="10015423";
    private String ID=UserData.WorkID;//ID="10015423";
    //private String Owner="王凱文";//Owner="王凱文"
    private String Owner=UserData.Name;//Owner="王凱文"
    //修改成登入資訊
    private static final String TAG = "MainActivity";
    Dialog myDialog;
    private String currentDate = "";
    private TextView tv_ShowDate ;
    private TextView tv_totalHR ;
    private TextView tv_Model;
    private TextView tv_hour;
    private TextView tv_Subject;
    private NCalendar ncalendar;
    private RecyclerView recyclerView;
    private ListView listV;
    private MyAdapter adapter;

    public TextView txt_HR ;
    public TextView txt_Model;
    public TextView txt_Type;
    public TextView txt_Subject;
    public TextView txt_totalHR ;
    private JSONObject LOGData;
    final List<Log> log_list = new ArrayList<Log>();

    Date date = new Date();
    final SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
    final SimpleDateFormat cal = new SimpleDateFormat("yyyy-MM-dd");
    Action action = new Action();

    //判斷回首頁狀態
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (data != null) {
            Bundle bundle = data.getExtras();
            Integer Add_Check = Integer.valueOf(bundle.getString("Add_Check"));
            if(Add_Check ==1){
                GetTodaylog(currentDate);
                GetRssDate();
            }
        }
    }


    public interface VolleyCallback {

        void onSuccess(JSONObject result);

    }
    private static final int REQUEST_EXTERNAL_STORAGE = 1;

    private static final String WRITE_EXTERNAL_STORAGE = "android.permission.WRITE_EXTERNAL_STORAGE";
    private static final String READ_EXTERNAL_STORAGE = "android.permission.READ_EXTERNAL_STORAGE";



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msibook_rd_main);
        currentDate = sdf.format(date);  //當期日期
        tv_Model = (TextView)findViewById(R.id.tv_Model);
        tv_hour  = (TextView)findViewById(R.id.tv_hour);
        tv_Subject = (TextView)findViewById(R.id.tv_Subject);
        tv_ShowDate = (TextView) findViewById(R.id.tv_ShowDate);
        tv_totalHR = (TextView) findViewById(R.id.tvTotalHR);
        ncalendar = (NCalendar) findViewById(R.id.ncalendar);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        ncalendar.setOnCalendarChangedListener(this);

        ImageButton btn_never_add = (ImageButton) findViewById(R.id.btn_never_add);
        LinearLayout LL_never = (LinearLayout) findViewById(R.id.LL_never);
        ImageButton btn_already_add = (ImageButton) findViewById(R.id.btn_already_add);
        LL_never.setOnClickListener(action);
        btn_already_add.setOnClickListener(action);
        btn_never_add.setOnClickListener(action);
        GetTodaylog(currentDate);
        GetRssDate();
        myDialog = new Dialog(this);


    }

    public void ShowPopup(View v,String Model,String Subjec,String Type,String HR){

        myDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.setContentView(R.layout.rd_popup_layout);

        txt_HR = (TextView) myDialog.findViewById(R.id.txt_HR);
        txt_Model = (TextView) myDialog.findViewById(R.id.txt_Model);
        //txt_Type = (TextView) myDialog.findViewById(R.id.txt_Type);
        txt_Subject = (TextView) myDialog.findViewById(R.id.txt_Subject);

        txt_HR.setText(tv_ShowDate.getText()+" "+HR);
        txt_Model.setText(Model);
//        if (Type=="null"){
//            txt_Type.setText("無");
//        }else{
//            txt_Type.setText(Type);
//        }
        txt_Subject.setText(Subjec);

        myDialog.show();
    }


    //取得今天工作日誌
    protected void GetTodaylog(String Date) {

        final RequestQueue mQueue = Volley.newRequestQueue(this);
        final LinearLayout LL_never = (LinearLayout) findViewById(R.id.LL_never);
        final LinearLayout total_layout= (LinearLayout) findViewById(R.id.total_layout);
        final LinearLayout LL_already = (LinearLayout) findViewById(R.id.LL_already);

        final ArrayList<String> Model  = new ArrayList<String>() ;
        final ArrayList<String> HR  = new ArrayList<String>() ;
        final ArrayList<String> Subject  = new ArrayList<String>() ;

        final String Path = "http://wtsc.msi.com.tw/IMS/MsiBook_App_Service.asmx/RDLog_RssLog?F_Keyin="+ID+"&F_Date="+Date;

        getString(Path, mQueue, new msibook_rd_main.VolleyCallback() {
            @Override
            public void onSuccess(JSONObject result) {
                int sum = 0;
                List<String> list = new ArrayList<>();
                SwipeMenuListView listV;
                MyAdapter adapter;
                listV = (SwipeMenuListView)findViewById(R.id.lv_total);
                try {


                    JSONArray UserArray = new JSONArray(result.getString("Key"));
                    if (UserArray.length() > 0) {
                        log_list.clear();
                        for (int i = 0; i < UserArray.length(); i++) {

                            LOGData = UserArray.getJSONObject(i);
                            sum += Double.valueOf(LOGData.getString("F_WorkHour"));
                            tv_totalHR.setText("總時數: "+String.valueOf(sum)+"小時");
                            log_list.add(new Log(String.valueOf(LOGData.getString("F_SeqNo")),String.valueOf(LOGData.getString("Model")),String.valueOf(LOGData.getString("F_WorkHour"))+"小時",String.valueOf(LOGData.getString("F_Subject")),String.valueOf(LOGData.getString("RSSType"))));

                        }
                        adapter = new MyAdapter(msibook_rd_main.this,log_list);
                        SwipeMenuCreator creator = new SwipeMenuCreator() {

                            @Override
                            public void create(SwipeMenu menu) {
                                // create "delete" item
                                SwipeMenuItem deleteItem = new SwipeMenuItem(
                                        getApplicationContext());
                                // set item background
                                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
                                        0x3F, 0x25)));
                                // set item width
                                deleteItem.setWidth(170);
                                // set a icon
                                deleteItem.setIcon(R.drawable.ic_delete);
                                // add to menu
                                menu.addMenuItem(deleteItem);
                            }
                        };

// set creator
                        listV.setMenuCreator(creator);
                        listV.setAdapter(adapter);

                        listV.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                                switch (index) {
                                    case 0:
                                        android.util.Log.d(TAG, "onMenuItemClick: "+log_list.get(position).getID());
                                        //getString( "http://wtsc.msi.com.tw/IMS/MsiBook_App_Service.asmx/RDLog_RssLogDelete?ID="+log_list.get(position).getID(), mQueue,new MainActivity.VolleyCallback() );
                                        final String Path = "http://wtsc.msi.com.tw/IMS/MsiBook_App_Service.asmx/RDLog_RssLogDelete?ID="+log_list.get(position).getID();

                                        getString(Path, mQueue, new msibook_rd_main.VolleyCallback() {
                                            @Override
                                            public void onSuccess(JSONObject result) {
                                            }
                                        });
                                        GetTodaylog(currentDate);
                                        GetRssDate();
                                        break;
                                    case 1:
                                        android.util.Log.d(TAG, "onMenuItemClick: "+log_list.get(position).getID());
                                        break;
                                }
                                // false : close the menu; true : not close the menu
                                return false;
                            }
                        });
                        listV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                                Intent intent = new Intent();

                                intent.putExtra("Model", log_list.get(position).getModel());

                                intent.putExtra("Subjec", log_list.get(position).getSubject());

                                intent.putExtra("Type", log_list.get(position).getType());

                                intent.putExtra("HR", tv_ShowDate.getText()+" "+ log_list.get(position).getHR());

                                intent.setClass(msibook_rd_main.this, msibook_rd_detial.class);
                                //開啟Activity
                                startActivity(intent);

                                //ShowPopup(view, log_list.get(position).getModel(),log_list.get(position).getSubject(),log_list.get(position).getType(),log_list.get(position).getHR());

                            }
                        });
                        listV.setDivider(null);
                        total_layout.setVisibility(View.VISIBLE);
                        LL_never.setVisibility(View.GONE);
                        LL_already.setVisibility(View.VISIBLE);

                    }

                    else{
                        tv_totalHR.setText("");
                        LL_never.setVisibility(View.VISIBLE);
                        total_layout.setVisibility(View.GONE);
                        LL_already.setVisibility(View.GONE);
                    }
                }
                catch (JSONException ex) {

                }

            }

        });


    }

    //取得此工號有填工作日誌的日期
    protected void GetRssDate() {

        final RequestQueue mQueue = Volley.newRequestQueue(this);
        final String Path = "http://wtsc.msi.com.tw/IMS/MsiBook_App_Service.asmx/RDLog_RssLogDate?F_Keyin="+ID;

        getString(Path, mQueue, new msibook_rd_main.VolleyCallback() {
            @Override
            public void onSuccess(JSONObject result) {

                List<String> list = new ArrayList<>();

                try {

                    JSONArray UserArray = new JSONArray(result.getString("Key"));
                    if (UserArray.length() > 0) {

                        for (int i = 0; i < UserArray.length(); i++) {
                            LOGData = UserArray.getJSONObject(i);
                            list.add(String.valueOf(LOGData.getString("dateForRss")));

                        }
                        ncalendar.setPoint(list);
                    }
                }
                catch (JSONException ex) {

                }

            }

        });


    }

    public static void getString(String Url, RequestQueue mQueue, final msibook_rd_main.VolleyCallback callback) {

        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, Url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        callback.onSuccess(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("TestJsonObj");
                        System.out.println(error);
                    }
                }
        );

        int socketTimeout = 30000;
        getRequest.setRetryPolicy(new DefaultRetryPolicy(socketTimeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        mQueue.add(getRequest);
    }

    //跳至新的頁面新增日誌
    private class Action implements View.OnClickListener {

        public void onClick(View v) {
            //TextView text_show_month = (TextView) findViewById(R.id.text_show_month);
            //text_show_month.setText("btn_never_add");
            Intent intent = new Intent();
            intent.setClass(msibook_rd_main.this, msibook_rd_new.class);
            Bundle bundle = new Bundle();
            bundle.putString("UserID",ID);
            bundle.putString("Date",tv_ShowDate.getText().toString());
            bundle.putString("Owner",Owner);
            intent.putExtras(bundle);
            startActivityForResult(intent,1);

        }
    }

    public void setDate(View view) {
        tv_totalHR.setText("");
        ncalendar.setVisibility(View.VISIBLE);
        ncalendar.post(new Runnable() {
            @Override
            public void run() {
                currentDate = cal.format(date);
                ncalendar.setDate(currentDate);


            }
        });
    }

    public void onCalendarChanged(LocalDate date) {
        tv_ShowDate.setText(date.getYear() + "/" + date.getMonthOfYear() + "/" + date.getDayOfMonth() );

        currentDate = sdf.format(date.toDate());  //當期日期

        GetTodaylog(currentDate);
        MyLog.d("dateTime::" + currentDate);

    }

    public class MyAdapter extends BaseAdapter {

        private LayoutInflater myInflater;
        private List<Log> logs;

        public MyAdapter(Context context, List<Log> logs){
            myInflater = LayoutInflater.from(context);
            this.logs = logs;
        }

        private class ViewHolder {
            TextView tv_Model;
            TextView tv_hour;
            TextView tv_Subject;
            public ViewHolder(TextView tv_Model, TextView tv_hour, TextView tv_Subject){
                this.tv_Model = tv_Model;
                this.tv_hour = tv_hour;
                this.tv_Subject = tv_Subject;
            }
        }

        //getCount()就是可以取得到底有多少列
        @Override
        public int getCount() {
            return logs.size();
        }

        //取得某一列的內容
        @Override
        public Object getItem(int arg0) {
            return logs.get(arg0);
        }

        //取得某一列的id
        @Override
        public long getItemId(int position) {
            return logs.indexOf(getItem(position));
        }

        //修改某一列View的內容
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if(convertView==null){

                convertView = myInflater.inflate(R.layout.msibook_rd_total_list_item1, null);
                holder = new ViewHolder(
                        (TextView) convertView.findViewById(R.id.tv_Model),
                        (TextView) convertView.findViewById(R.id.tv_hour),
                        (TextView) convertView.findViewById(R.id.tv_Subject)
                );
                convertView.setTag(holder);
            }else{
                holder = (ViewHolder) convertView.getTag();
            }
            Log log = (Log)getItem(position);


            holder.tv_Model.setText("MS-"+log.getModel());

            holder.tv_hour.setText(log.getHR());

            holder.tv_Subject.setText(log.getSubject());


            return convertView;
        }
    }

    public class Log {
        private String ID;
        private String Model;
        private String HR;
        private String Subject;
        private String Type;

        public Log(String ID,String Model,String HR,String Subject,String Type) {
            this.ID = ID;
            this.Model = Model;
            this.HR = HR;
            this.Subject = Subject;
            this.Type = Type;
        }
        public String getID(){
            return ID;
        }
        public String getModel(){
            return Model;
        }
        public void setModel(String Model){
            this.Model = Model;
        }
        public String getHR(){
            return HR;
        }
        public void setHR(String HR){
            this.HR = HR;
        }
        public String getSubject(){
            return Subject;
        }
        public void setSubject(String Subject){
            this.Subject = Subject;
        }
        public String getType(){
            return Type;
        }
        public void setType(String Type){
            this.Type = Type;
        }
    }





}
