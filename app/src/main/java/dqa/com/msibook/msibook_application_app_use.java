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
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class msibook_application_app_use extends AppCompatActivity {

    private ListView mListView;

    private AppUseAdapter mAppUseAdapter;

    private List<App_application_Item> App_application_Item_List = new ArrayList<App_application_Item>();

    private String Set_RoleCode;

    private String Set_RoleName;

    private String Set_RoleID;

    private String Set_F_Stat;

    private Context mContext;
    private ProgressDialog progressBar;


    //點選Local回傳狀態
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (data != null) {
            Bundle bundle = data.getExtras();
            Integer Application_Check = Integer.valueOf(bundle.getString("Application_Check"));
            if (Application_Check == 1) {
                try {
                    Thread.currentThread().sleep(500);
                    //Find_System_Role_Type(UserData.WorkID);
                    Find_System_Role_Type("10015989");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msibook_application_app_use);

        mContext = this;

        //讀取時間Bar
        progressBar = new ProgressDialog(this);
        progressBar.setCancelable(true);
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.setMessage("資料載入中");

        Find_System_Role_Type("10015989");
        //Find_System_Role_Type(UserData.WorkID);

    }

    //
    private void Find_System_Role_Type(String WorkID) {

        //顯示  讀取等待時間Bar
        progressBar.show();

        App_application_Item_List.clear();

        RequestQueue mQueue = Volley.newRequestQueue(this);
        HTTPSTrustManager.allowAllSSL();//信任所有证书，信任憑證
        String Path = GetServiceData.ServicePath + "/Find_System_Role_Type?WorkID=" + WorkID;

        GetServiceData.getString(Path, mQueue, new GetServiceData.VolleyCallback() {
            @Override
            public void onSuccess(JSONObject result) {

                try {

                    JSONArray UserArray = new JSONArray(result.getString("Key"));


                    for (int i = 0; i < UserArray.length(); i++) {
                        int j = 0;

                        JSONObject IssueData = UserArray.getJSONObject(i);

                        String ID = String.valueOf(IssueData.getInt("ID")); //英文

                        String SysENName = String.valueOf(IssueData.getString("SysENName")); //英文

                        String SysCNName = String.valueOf(IssueData.getString("SysCNName")); //中文

                        if (IssueData.isNull("RoleCode")) { //等級 英文
                            String RoleCode = "null";
                            Set_RoleCode = "null";
                        } else {
                            String RoleCode = IssueData.getString("RoleCode");
                            Set_RoleCode = RoleCode;
                        }

                        if (IssueData.isNull("RoleName")) { //等級中文
                            String RoleName = "null";
                            Set_RoleName = "null";
                        } else {
                            String RoleName = IssueData.getString("RoleName");
                            Set_RoleName = RoleName;
                        }

                        if (IssueData.isNull("RoleID")) { //權限 數字
                            String RoleID = "null";
                            Set_RoleID = "null";
                        } else {
                            Integer RoleID = IssueData.getInt("RoleID");
                            Set_RoleID = String.valueOf(RoleID);
                        }

                        String SysType = String.valueOf(IssueData.getBoolean("SysType")); //Type

                        if (IssueData.isNull("F_Stat")) { //權限 數字
                            String F_Stat = "3";
                            Set_F_Stat = "3";
                        } else {
                            String F_Stat = IssueData.getString("F_Stat");
                            Set_F_Stat = String.valueOf(F_Stat);
                        }

                        String Rank = String.valueOf(IssueData.getInt("Rank"));

                        if((Integer.valueOf(Set_F_Stat)== 2) || (Boolean.valueOf(SysType)==false && Set_RoleID.contains("null")==true)) {

                            if(SysENName.contains("WeeklyReport")){

                            }else{
                            App_application_Item_List.add(j, new App_application_Item(ID,SysENName, SysCNName, Set_RoleCode, Set_RoleName, Set_RoleID,SysType,Set_F_Stat));
                            j++;
                            }
                        }
                    }

                    mListView = (ListView) findViewById(R.id.list_application);

                    mAppUseAdapter = new AppUseAdapter(mContext, App_application_Item_List);

                    mListView.setAdapter(mAppUseAdapter);

                    //關閉-讀取等待時間Bar
                    progressBar.dismiss();

                } catch (JSONException ex) {

                    Log.w("Json", ex.toString());
                }
            }
        });
    }


    //WeekProjectAdapter
    public class AppUseAdapter extends BaseAdapter {

        private LayoutInflater mLayInf;

        private List<App_application_Item> App_application_Item_List;

        private Context ProjectContext;

        private String Title;

        public AppUseAdapter(Context context, List<App_application_Item> App_application_Item_List) {
            mLayInf = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            ProjectContext = context;

            this.Title = Title;

            this.App_application_Item_List = App_application_Item_List;

        }

        @Override
        public int getCount() {
            return App_application_Item_List.size();
        }

        @Override
        public Object getItem(int position) {
            return App_application_Item_List.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            View v = new View(ProjectContext);

            v = mLayInf.inflate(R.layout.msibook_application_app_adapter_item, parent, false);

            ImageView img_app_icon = (ImageView) v.findViewById(R.id.img_app_icon);
            TextView textView_app_name = (TextView) v.findViewById(R.id.textView_app_name);
            Button btn_application = (Button) v.findViewById(R.id.btn_application);  //F_Stat =2 show審核中

            if(Integer.valueOf(App_application_Item_List.get(position).GetF_Stat())==2){
                btn_application.setText("審核中");
                btn_application.setEnabled(false);
                btn_application.setBackgroundResource(R.drawable.application_app_btn_bg_off);
            }

            String str = App_application_Item_List.get(position).GetSysENName();

            switch (str) {
                case "Action_Item":
                    img_app_icon.setImageResource(R.mipmap.msibook_ic_msibook_workorder);
                    textView_app_name.setText("工單系統");
                    break;
                case "eHR":
                    img_app_icon.setImageResource(R.mipmap.msibook_ic_msibook_ehr);
                    textView_app_name.setText("eHR");
                    break;
                case "Laboratory":
                    img_app_icon.setImageResource(R.mipmap.msibook_ic_msibook_laboratory);
                    textView_app_name.setText("實驗室");
                    break;
                case "MMC":
                    img_app_icon.setImageResource(R.mipmap.msibook_ic_msibook_assets);
                    textView_app_name.setText("資產管理");
                    break;
                case "Request_Form":
                    img_app_icon.setImageResource(R.mipmap.msibook_ic_msibook_demandlist);
                    textView_app_name.setText("需求單系統");
                    break;
                case "WeeklyReport":
                    img_app_icon.setImageResource(R.mipmap.msibook_ic_msibook_weekly);
                    textView_app_name.setText("週報");
                    break;
                case "PMS":
                    img_app_icon.setImageResource(R.mipmap.msibook_ic_msibook_pms);
                    textView_app_name.setText("PMS");
                    break;
                case "ims":
                    img_app_icon.setImageResource(R.mipmap.msibook_ic_msibook_ims);
                    textView_app_name.setText("IMS系統");
//                    btn_application.setEnabled(false);
//                    btn_application.setText("尚未開放");
//                    btn_application.setBackgroundResource(R.drawable.application_app_btn_bg_off);
                    break;
                case "OverTime":
                    img_app_icon.setImageResource(R.mipmap.msibook_ic_msibook_overtimeapp);
                    textView_app_name.setText("加班系統");
                    break;
                case "Certification":
                    img_app_icon.setImageResource(R.mipmap.msibook_ic_msibook_certified);
                    textView_app_name.setText("認證系統");
                    break;
                case "RDWork_Daily":
                    img_app_icon.setImageResource(R.mipmap.msibook_btn_msibook_rdworklog);
                    textView_app_name.setText("RD工作日誌");
                    break;
                case "WorkReport":
                    img_app_icon.setImageResource(R.mipmap.msibook_ic_msibook_workreport);
                    textView_app_name.setText("工作報告");
//                    btn_application.setEnabled(false);
//                    btn_application.setText("尚未開放");
//                    btn_application.setBackgroundResource(R.drawable.application_app_btn_bg_off);
                    break;
                case "ResourceManager":
                    //img_app_icon.setImageResource(R.mipmap.msibook_btn_msibook_rdworklog);
                    textView_app_name.setText("資源管理");
                    break;
            }

            btn_application.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    Log.w("申請Btn Check", String.valueOf(App_application_Item_List.get(position).GetSysCNName() + App_application_Item_List.get(position).GetSysType()));
                    Intent msibook_application = new Intent(msibook_application_app_use.this,msibook_application_app_use_check.class);

                    msibook_application.putExtra("SysID",App_application_Item_List.get(position).GetID());

                    msibook_application.putExtra("SysCNName",App_application_Item_List.get(position).GetSysCNName());

                    startActivityForResult(msibook_application,1);

                }
            });

                return v;
            }

    }


        //--------------------------------------------------------------------------------Item--------------------------------------------------------------------------------
        //Detail_Item
        public class App_application_Item {

            String ID;
            String SysENName;
            String SysCNName;
            String RoleCode;
            String RoleName;
            String RoleID;
            String SysType;
            String F_Stat;


            public App_application_Item(String ID,String SysENName, String SysCNName, String RoleCode, String RoleName, String RoleID,String SysType,String F_Stat) {

                this.ID = ID;
                this.SysENName = SysENName;
                this.SysCNName = SysCNName;
                this.RoleCode = RoleCode;
                this.RoleName = RoleName;
                this.RoleID = RoleID;
                this.SysType = SysType;
                this.F_Stat = F_Stat;
            }

            public String GetID() {
                return this.ID;
            }

            public String GetSysENName() {
                return this.SysENName;
            }

            public String GetSysCNName() {
                return this.SysCNName;
            }

            public String GetRoleCode() {
                return this.RoleCode;
            }

            public String GetRoleName() {
                return this.RoleName;
            }

            public String GetRoleID() {
                return this.RoleID;
            }

            public String GetSysType() {
                return this.SysType;
            }

            public String GetF_Stat() {
                return this.F_Stat;
            }


        }

    }

