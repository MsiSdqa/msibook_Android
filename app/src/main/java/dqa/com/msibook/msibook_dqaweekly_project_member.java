package dqa.com.msibook;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class msibook_dqaweekly_project_member extends AppCompatActivity {

    List<Member_Item> Member_List = new ArrayList<Member_Item>();
    List<Member_Item> GroupItem = new ArrayList<Member_Item>();
    private ListView lsv_main;
    private MemberAdapter mListAdapter;

    private ProgressDialog pDialog;

    String ModelName;
    private RequestQueue mQueue;

    @Override
    protected void onDestroy() {
        pDialog.dismiss();
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msibook_dqaweekly_project_member);
        pDialog = new ProgressDialog(msibook_dqaweekly_project_member.this);
        //View v = inflater.inflate(R.layout.fragment_my_issue, container, false);
        //宣告 ListView 元件
        lsv_main = (ListView) findViewById(R.id.listView);

        lsv_main.setOnItemClickListener(listViewOnItemClickListener);

        Bundle bundle = this.getIntent().getExtras();

        String ModelID = bundle.getString("ModelID");

        ModelName = bundle.getString("ModelName");

        if (!ModelID.matches((""))) {
            GetPM_MemberData(ModelID);
        }
        setTitle(ModelName);
    }

    private AdapterView.OnItemClickListener listViewOnItemClickListener
            = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        }
    };

    private void GetPM_MemberData(String PM_ID) {

        pDialog.setTitle("Loading...");

        pDialog.show();


        if (mQueue == null) {
            mQueue = Volley.newRequestQueue(this);
        }
        String Path = "http://wtsc.msi.com.tw/IMS/IMS_App_Service.asmx/Find_Model_Member_List?PM_ID=" + PM_ID;

        getString(Path, mQueue, new GetServiceData.VolleyCallback() {
            @Override
            public void onSuccess(JSONObject result) {
                MemberDataMapping(result);

                pDialog.hide();
            }
        });

    }

    private void MemberDataMapping(JSONObject result) {
        try {
            Member_List.clear();

            JSONArray UserArray = new JSONArray(result.getString("Key"));


            if (UserArray.length() > 0) {

                for (int i = 0; i < UserArray.length(); i++) {
                    JSONObject ModelData = UserArray.getJSONObject(i);

                    String MemberName = ModelData.getString("MemberName");

                    String Header = ModelData.getString("Header");

                    String F_Tel = ModelData.getString("F_Tel");

                    String WorkID = ModelData.getString("WorkID");

                    Member_List.add(i, new Member_Item(Header, MemberName, "", F_Tel, WorkID, false));
                }

                GroupItem = GroupItem();


                mListAdapter = new MemberAdapter(this, Mappingitem());

                //設定 ListView 的 Adapter
                lsv_main.setAdapter(mListAdapter);

                // lsv_main.setEmptyView(findViewById(R.id.emptyview));

            } else {
                lsv_main.setEmptyView(findViewById(R.id.empty));
            }
        } catch (JSONException ex) {

        }
    }

    private List<Member_Item> Mappingitem() {
        List<Member_Item> NewMemberList = new ArrayList<Member_Item>();

        int i = 0;

        for (Member_Item d : GroupItem) {

            NewMemberList.add(i, new Member_Item(d.GetTitle(), "", "", "", "", true));

            i++;

            for (Member_Item e : Member_List) {

                if (e.GetTitle().equals(d.GetTitle())) {


                    NewMemberList.add(i, e);

                    i++;
                }
            }
        }

        return NewMemberList;
    }

    private List<Member_Item> GroupItem() {

        GroupItem.clear();

        int i = 0;

        for (Member_Item d : Member_List) {

            if (!CheckRepeat(GroupItem, d.GetTitle())) {

                GroupItem.add(i, new Member_Item(d.GetTitle(), "", "", "", "", true));

                i++;
            }
        }

        return GroupItem;
    }

    private boolean CheckRepeat(List<Member_Item> _GroupItem, String Value) {
        boolean Check = false;

        for (Member_Item d : _GroupItem) {

            if (d.GetTitle().matches(Value)) {
                Check = true;
            }
        }
        return Check;

    }

    public static void getString(String Url, RequestQueue mQueue, final GetServiceData.VolleyCallback callback) {

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

    //----------------Item-----------------
    public class Member_Item {

        String Title;

        String Name;

        String Image;

        String Phone;

        String WorkID;

        Boolean Section;

        public Member_Item(String Title, String Name, String Image, String Phone, String WorkID, Boolean Section) {
            this.Title = Title;

            this.Name = Name;

            this.Image = Image;

            this.Phone = Phone;

            this.Section = Section;

            this.WorkID = WorkID;
        }

        public String GetTitle() {
            return this.Title;
        }

        public String GetName() {
            return this.Name;
        }

        public String GetImage() {
            return this.Image;
        }

        public String GetPhone() {
            return this.Phone;
        }

        public String GetWorkID() {
            return this.WorkID;
        }

        public Boolean GetSection() {
            return this.Section;
        }
    }


    //--------------Adapter----------------
    public class MemberAdapter extends BaseAdapter {

        private LayoutInflater mLayInf;

        private List<Member_Item> Member_List;

        private Context mcontext;

        public MemberAdapter(Context context, List<Member_Item> Member_List) {
            mLayInf = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            this.Member_List = Member_List;

            mcontext = context;
        }

        @Override
        public int getCount() {
            return Member_List.size();
        }

        @Override
        public Object getItem(int position) {
            return Member_List.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View v = mLayInf.inflate(R.layout.msibook_dqaweekly_member_item, parent, false);

            if (Member_List.get(position).GetSection()) {
                v = mLayInf.inflate(R.layout.msibook_dqaweekly_member_section, parent, false);

                TextView txt_Member_Title = (TextView) v.findViewById(R.id.txt_Member_Title);

                txt_Member_Title.setText(Member_List.get(position).GetTitle());

            } else

            {

                ImageView Img_Member = (ImageView) v.findViewById(R.id.Img_Member);

                ImageView Img_Member_Phone = (ImageView) v.findViewById(R.id.Img_Member_Phone);

                TextView txt_Member_Name = (TextView) v.findViewById(R.id.txt_Member_Name);

                TextView txt_Member_Phone = (TextView) v.findViewById(R.id.txt_Member_Phone);
                //Img_ProjectImage.setImageResource(Integer.valueOf(Project_List.get(position).GetImage().toString()));

                //GetServiceData.GetUserPhoto(mcontext, Member_List.get(position).GetWorkID(), Img_Member);

                txt_Member_Name.setText(Member_List.get(position).GetName());

                txt_Member_Phone.setText(Member_List.get(position).GetPhone());

                if (Member_List.get(position).GetPhone().matches("") || Member_List.get(position).GetPhone().matches("null")) {
                    txt_Member_Phone.setVisibility(View.GONE);

                    Img_Member_Phone.setVisibility(View.GONE);
                }
            }


            return v;
        }

    }


}
