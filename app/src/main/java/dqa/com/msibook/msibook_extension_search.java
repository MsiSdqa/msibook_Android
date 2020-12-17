package dqa.com.msibook;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class msibook_extension_search extends AppCompatActivity {

    private EditText editText_search_word;
    private RecyclerView recycle_view;

    private RecyclerView.Adapter recyclerView_Adapter;
    private RecyclerView.LayoutManager recyclerViewLayoutManager;

    private List<msibook_extension_item> msibook_extension_item_List = new ArrayList<msibook_extension_item>();

    private List<msibook_extension_item> msibook_extension_item_Search_List = new ArrayList<msibook_extension_item>();

    private ProgressDialog progressBar;
    private Context mContext;

    private Button btn_search;

    private String Set_TEL;

    private String Set_DeptID;

    private TextView empty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msibook_extension_search);

        mContext = this;

        progressBar = new ProgressDialog(this.mContext);
        progressBar.setCancelable(true);
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.setMessage("資料搜尋中");

        recycle_view = (RecyclerView) findViewById(R.id.recycle_view);
        editText_search_word = (EditText) findViewById(R.id.editText_search_word);
        btn_search = (Button) findViewById(R.id.btn_search);
        empty = (TextView) findViewById(R.id.empty);

        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Find_Extension_Input(String.valueOf(editText_search_word.getText()));
            }
        });
//
//        editText_search_word.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//            private Timer timer=new Timer();
//            private final long DELAY = 1000; // milliseconds
//
//            @Override
//            public void afterTextChanged(final Editable s) {
//                timer.cancel();
//                timer = new Timer();
//                timer.schedule(
//                        new TimerTask() {
//                            @Override
//                            public void run() {
//                                Find_Extension_Input(String.valueOf(s));
//                            }
//                        },
//                        DELAY
//                );
//            }
//        });

        //Find_Extension_Input(UserData.DeptID);


    }

    public void Find_Extension_Input(final String Input) {

        progressBar.show();

        msibook_extension_item_List.clear();

        RequestQueue mQueue = Volley.newRequestQueue(this);

        HTTPSTrustManager.allowAllSSL();//信任所有证书，信任憑證
        String Path = GetServiceData.ServicePath + "/Find_Extension_Input?Input=" + Input;

        GetServiceData.getString(Path, mQueue, new GetServiceData.VolleyCallback() {
            @Override
            public void onSuccess(JSONObject result) {

                try {

                    JSONArray UserArray = new JSONArray(result.getString("Key"));

                    if(UserArray.length()>0){
                        empty.setVisibility(View.GONE);
                        recycle_view.setVisibility(View.VISIBLE);
                    for (int i = 0; i < UserArray.length(); i++) {

                        JSONObject IssueData = UserArray.getJSONObject(i);

                        String WorkID = String.valueOf(IssueData.getString("WorkID")); //"00183845"

                        String Dept = String.valueOf(IssueData.getString("Dept")); //"設計品質驗證二部二課",

                        String ENAME = String.valueOf(IssueData.getString("ENAME")); //"larryliao",

                        String CNAME = String.valueOf(IssueData.getString("CNAME"));//"廖賴國"

                        if (IssueData.isNull("DeptID")) {
                            String DeptID = "null";
                            Set_DeptID = "null";
                        } else {
                            String DeptID = String.valueOf(IssueData.getInt("DeptID"));//"廖賴國"
                            Set_DeptID = DeptID;
                        }

                        if (IssueData.isNull("TEL")) {
                            String TEL = "暫無分機";
                            Set_TEL = "暫無分機";
                        } else {
                            String TEL = String.valueOf(IssueData.getString("TEL"));//"廖賴國"
                            Set_TEL = TEL;
                        }

                        msibook_extension_item_List.add(i, new msibook_extension_item(WorkID, Dept, ENAME, CNAME, Set_DeptID, Set_TEL));

                    }
                    //Change 2 to your choice because here 2 is the number of Grid layout Columns in each row.
                    recyclerViewLayoutManager = new GridLayoutManager(mContext, 1);

                    recycle_view.setLayoutManager(recyclerViewLayoutManager);

                    recyclerView_Adapter = new RecyclerViewAdapter(mContext, msibook_extension_item_List);

                    recycle_view.setAdapter(recyclerView_Adapter);

                    recyclerView_Adapter.notifyDataSetChanged();

                }else{
                        empty.setVisibility(View.VISIBLE);
                        recycle_view.setVisibility(View.GONE);
                    }


                } catch (JSONException ex) {
                    Log.w("Json", ex.toString());
                }

                progressBar.dismiss();

            }
        });
    }

    public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{

        List<msibook_extension_item> values;
        Context context1;

        public RecyclerViewAdapter(Context context2, List<msibook_extension_item> values2){

            values = values2;

            context1 = context2;
        }

        public class ViewHolder extends RecyclerView.ViewHolder{

            public TextView textView_wrokid;
            public TextView textView_CNAME;
            public TextView textView_ENAME;
            public TextView textView_Dept;
            public TextView textView_TEL;

            public ViewHolder(View v){

                super(v);

                textView_wrokid = (TextView) v.findViewById(R.id.textView_wrokid);

                textView_CNAME= (TextView) v.findViewById(R.id.textView_CNAME);

                textView_ENAME= (TextView) v.findViewById(R.id.textView_ENAME);

                textView_Dept= (TextView) v.findViewById(R.id.textView_Dept);

                textView_TEL= (TextView) v.findViewById(R.id.textView_TEL);

            }
        }

        @Override
        public RecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){

            View view1 = LayoutInflater.from(context1).inflate(R.layout.msibook_extension_recycler_view_item,parent,false);

            RecyclerViewAdapter.ViewHolder viewHolder1 = new RecyclerViewAdapter.ViewHolder(view1);

            return viewHolder1;
        }

        @Override
        public void onBindViewHolder(final RecyclerViewAdapter.ViewHolder Vholder, final int position){

            Vholder.textView_wrokid.setText(values.get(position).GetWorkID());

            Vholder.textView_CNAME.setText(values.get(position).GetCNAME());

            Vholder.textView_ENAME.setText(values.get(position).GetENAME());

            Vholder.textView_Dept.setText(values.get(position).GetDept());

            Vholder.textView_TEL.setText("分機: "+values.get(position).GetTEL());


        }

        @Override
        public int getItemCount(){

            return values.size();
        }
    }

    public class msibook_extension_item {

        String WorkID;
        String Dept;
        String ENAME;
        String CNAME;
        String DeptID;
        String TEL;


        public msibook_extension_item(String WorkID,String Dept,String ENAME, String CNAME, String DeptID,String TEL)
        {
            this.WorkID = WorkID;
            this.Dept = Dept;
            this.ENAME = ENAME;
            this.CNAME = CNAME;
            this.DeptID = DeptID;
            this.TEL = TEL;
        }

        public String GetWorkID()   { return this.WorkID; }

        public String GetDept()
        {
            return this.Dept;
        }

        public String GetENAME()
        {
            return this.ENAME;
        }

        public String GetCNAME()
        {
            return this.CNAME;
        }

        public String GetDeptID()
        {
            return this.DeptID;
        }

        public String GetTEL()
        {
            return this.TEL;
        }


    }


}
