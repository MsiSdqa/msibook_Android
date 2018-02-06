package dqa.com.msibook;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.widget.SwipeRefreshLayout;
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

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class msibook_ims_issue_myissue extends AppCompatActivity {

    private SwipeRefreshLayout mSwipeRefreshLayout;

    private ListView lsv_main;
    private msibook_ims_myissue_adapter mListAdapter;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    List<msibook_ims_myissue_item> Issue_List = new ArrayList<msibook_ims_myissue_item>();
    private Context mContext;
    //private OnFragmentInteractionListener mListener;
    private RequestQueue mQueue;
    private ProgressDialog progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msibook_ims_myissue);
        mContext = this;

        progressBar = new ProgressDialog(this);
        progressBar.setCancelable(true);
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.setMessage("Loading...");

        lsv_main = (ListView) findViewById(R.id.listView);

        lsv_main.setOnItemClickListener(listViewOnItemClickListener);




        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh_layout);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                System.out.println("MyIssueRefresh");
                mSwipeRefreshLayout.setRefreshing(false);


                if (!UserData.WorkID.matches("")) {


                    Find_My_Issue(UserData.WorkID, "300");
                }
            }
        });

        if (!UserData.WorkID.matches("")) {
            Find_My_Issue(UserData.WorkID, "300");
        }
    }

    private AdapterView.OnItemClickListener listViewOnItemClickListener
            = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            msibook_ims_myissue_adapter IssueAdapter = (msibook_ims_myissue_adapter) parent.getAdapter();

            msibook_ims_myissue_item Issue_Item = (msibook_ims_myissue_item) IssueAdapter.getItem(position);

            GoIssueInfo(Issue_Item.GetID());


        }
    };

    private void Find_My_Issue(String WorkID, String DateRange) {

        progressBar.show();

        if (mQueue == null) {
            mQueue = Volley.newRequestQueue(msibook_ims_issue_myissue.this);
        }
        String Path = GetServiceData.IMS_ServicePath + "/Find_My_Issue_List?F_Keyin=" + WorkID + "&DateRange=" + DateRange;

        GetServiceData.getString(Path, mQueue, new GetServiceData.VolleyCallback() {
            @Override
            public void onSuccess(JSONObject result) {

                IssueListDataMapping(result);

                progressBar.dismiss();
            }
        });
    }

    private void IssueListDataMapping(JSONObject result) {
        try {

            Issue_List.clear();

            JSONArray UserArray = new JSONArray(result.getString("Key"));

            for (int i = 0; i < UserArray.length(); i++) {

                JSONObject ModelData = UserArray.getJSONObject(i);

                String F_SeqNo = String.valueOf(ModelData.getInt("F_SeqNo"));

                String Model = ModelData.getString("Model");

                String F_Subject = ModelData.getString("F_Subject");

                String F_CreateDate = AppClass.ConvertDateString(ModelData.getString("F_CreateDate"));

                String F_Priority = ModelData.getString("F_Priority");

                String CommentRead = String.valueOf(ModelData.getInt("CommentRead"));

                String Read = String.valueOf(ModelData.getInt("Read"));

                String F_Owner = String.valueOf(ModelData.getString("F_Owner"));

                String Status_Display = String.valueOf(ModelData.getString("Status_Display"));

                F_Subject = F_Subject.replace("\n", "");

                Issue_List.add(i, new msibook_ims_myissue_item(F_SeqNo, Model, F_Subject, F_CreateDate, F_Priority, CommentRead, Read, F_Owner, Status_Display));

            }

            if (Issue_List.size() > 0) {  // ListView 中所需之資料參數可透過修改 Adapter 的建構子傳入
                mListAdapter = new msibook_ims_myissue_adapter(mContext, Issue_List, "MyIssue");

                //設定 ListView 的 Adapter
                lsv_main.setAdapter(mListAdapter);

            }


        } catch (JSONException ex) {
            System.out.println(ex);
        }
    }

    private void GoIssueInfo(String IssueID) {

        Bundle bundle = new Bundle();

        bundle.putString("IssueID", IssueID);
        // 建立啟動另一個Activity元件需要的Intent物件
        // 建構式的第一個參數：「this」
        // 建構式的第二個參數：「Activity元件類別名稱.class」
        Intent intent = new Intent(mContext, msibook_ims_issue_info.class);

        intent.putExtras(bundle);
        // 呼叫「startActivity」，參數為一個建立好的Intent物件
        // 這行敘述執行以後，如果沒有任何錯誤，就會啟動指定的元件
        startActivity(intent);
    }


    public class msibook_ims_myissue_item {

        String ID;

        String ProjectName;

        String Subject;

        String Date;

        String Priority;

        String WorkNoteCount;

        String Read;

        String Author;

        String IssueStatus;


        public msibook_ims_myissue_item(String ID, String ProjectName, String Subject, String Date, String Priority, String WorkNoteCount, String Read, String Author, String IssueStatus) {
            this.ID = ID;

            this.ProjectName = ProjectName;

            this.Subject = Subject;

            this.Date = Date;

            this.Priority = Priority;

            this.WorkNoteCount = WorkNoteCount;

            this.Read = Read;

            this.Author = Author;

            this.IssueStatus = IssueStatus;

        }

        public String GetID() {
            return this.ID;
        }

        public String GetProjectName() {
            return this.ProjectName;
        }

        public String GetSubject() {
            return this.Subject;
        }

        public String GetDate() {
            return this.Date;
        }

        public String GetPriority() {
            return this.Priority;
        }

        public String GetWorkNoteCount() {
            return this.WorkNoteCount;
        }

        public String GetRead() {
            return this.Read;
        }

        public String GetAuthor() {
            return this.Author;
        }

        public String GetIssueStatus() {
            return this.IssueStatus;
        }
    }

    public class msibook_ims_myissue_adapter extends BaseAdapter {

        private LayoutInflater mLayInf;

        private List<msibook_ims_myissue_item> Issue_List;

        private String AdapterType;

        private Context mContext;

        public msibook_ims_myissue_adapter(Context context, List<msibook_ims_myissue_item> Issue_List, String AdapterType) {
            this.AdapterType = AdapterType;

            if (context != null) {
                mLayInf = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            }

            mContext = context;

            this.Issue_List = Issue_List;
        }

        @Override
        public int getCount() {
            return Issue_List.size();
        }

        @Override
        public Object getItem(int position) {
            return Issue_List.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View v = mLayInf.inflate(R.layout.activity_msibook_ims_issue_item, parent, false);

            ImageView Img_Priority = (ImageView) v.findViewById(R.id.Img_Priority);

            TextView txt_Issue_Project_Name = (TextView) v.findViewById(R.id.txt_Issue_Project_Name);

            TextView txt_Issue_Date = (TextView) v.findViewById(R.id.txt_Issue_Date);

            TextView txt_Issue_Subject = (TextView) v.findViewById(R.id.txt_Issue_Subject);

            TextView txt_Issue_WorkNoteCount = (TextView) v.findViewById(R.id.txt_Issue_WorkNoteCount);

            TextView IssueList_Author = (TextView) v.findViewById(R.id.IssueList_Author);

//        GetServiceData.GetImageByImageLoad("http://172.16.111.114/File/SDQA/Code/Admin/10010670.jpg",Img_Priority);
            //Img_Priority.setImageResource(Integer.valueOf(Project_List.get(position).GetImage().toString()));

            if (AdapterType.equals("MyIssue")) {

                if (Issue_List.get(position).GetProjectName().toLowerCase().contains("ms-")) {
                    txt_Issue_Project_Name.setText(Issue_List.get(position).GetProjectName());
                } else {
                    txt_Issue_Project_Name.setText("MS-" + Issue_List.get(position).GetProjectName());
                }

            } else {
                txt_Issue_Project_Name.setText("#" + Issue_List.get(position).GetID());
            }

            txt_Issue_Date.setText(Issue_List.get(position).GetDate());

            txt_Issue_Subject.setText(Issue_List.get(position).GetSubject());

            IssueList_Author.setText(Issue_List.get(position).GetAuthor());

            if (Issue_List.get(position).GetRead().equals("0")) {
                txt_Issue_WorkNoteCount.setText("N");

                txt_Issue_WorkNoteCount.setTextColor(Color.parseColor("#ffffff"));
            } else {
                if (Issue_List.get(position).GetWorkNoteCount().equals("0")) {
                    txt_Issue_WorkNoteCount.setVisibility(View.GONE);
                } else {
                    txt_Issue_WorkNoteCount.setText(Issue_List.get(position).GetWorkNoteCount());
                }
            }


//            Img_Priority.setImageResource(AppClass.PriorityImage(Issue_List.get(position).GetPriority()));
//
//            LinearLayout IssueList_Background = (LinearLayout) v.findViewById(com.apps.ims.R.id.IssueList_Background);
//
//            if (Issue_List.get(position).GetIssueStatus().equals("3")) {
//                IssueList_Background.setBackgroundColor(mContext.getResources().getColor(com.apps.ims.R.color.Issue_Status_Close));
//            }

            return v;
        }


    }
}
