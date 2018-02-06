package dqa.com.msibook;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class msibook_store extends AppCompatActivity {

    private RequestQueue mQueue;
    private Context mContext;
    private RecyclerView mRecyclerView;
    private msibook_store_Adapter msibook_store_Adapter;
    private List<msibook_store_Item> msibook_store_Item_List;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msibook_store);
        mContext = msibook_store.this;

        mRecyclerView = (RecyclerView)findViewById(R.id.Rcy_Store);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecyclerView.setLayoutManager(layoutManager);

        setTitle("MSI Point");

        msibook_store_Item_List = new ArrayList< msibook_store_Item>();

        Find_MSIBook_Store();

        mRecyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });



    }

    private void Find_MSIBook_Store() {

        if (mQueue == null) {
            mQueue = Volley.newRequestQueue(mContext);
        }

        String Path = GetServiceData.ServicePath + "/Find_MSIBook_Store";

        Map<String, String> map = new HashMap<String, String>();

        GetServiceData.SendPostRequest(Path, mQueue, new GetServiceData.VolleyStringCallback() {
            @Override
            public void onSendRequestSuccess(String result) {

                try {
                    msibook_store_Item_List.clear();

                    JSONObject obj = new JSONObject(result);

                    JSONArray UserArray = new JSONArray(obj.getString("Key"));

                    for (int i = 0; i < UserArray.length(); i++) {
                        JSONObject IssueData = UserArray.getJSONObject(i);

                        String F_SeqNo = String.valueOf(IssueData.getInt("F_SeqNo"));

                        String F_StoreName = String.valueOf(IssueData.getString("F_StoreName"));

                        Log.w("F_StoreName",F_StoreName);

                        msibook_store_Item_List.add(i, new msibook_store_Item(F_SeqNo,F_StoreName));
                    }

                    // ListView 中所需之資料參數可透過修改 Adapter 的建構子傳入
                    msibook_store_Adapter = new msibook_store_Adapter(mContext,msibook_store_Item_List);


                    //mListAdapter.notifyDataSetChanged();
                    //設定 ListView 的 Adapter
                    mRecyclerView.setAdapter(msibook_store_Adapter);


                } catch (JSONException ex) {

                }



            }

            @Override
            public void onSendRequestError(String result) {
                //Log.w("NotificationSuccess",result);
            }
        },map);
    }


    public class msibook_store_Adapter extends RecyclerView.Adapter<msibook_store_Adapter.ViewHolder> {

        private Context mContext;
        private LayoutInflater mInflater;
        private List<msibook_store_Item> mDatas;
        private AdapterView.OnItemClickListener mOnItemClickListener = null;

        public msibook_store_Adapter(Context context, List<msibook_store_Item> datats) {
            mInflater = LayoutInflater.from(context);
            mContext = context;
            mDatas = datats;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public TextView txt_Store_Name;


            public ViewHolder(View LayoutView) {
                super(LayoutView);

                txt_Store_Name = (TextView)LayoutView.findViewById(R.id.txt_Store_Name);



            }


        }

        @Override
        public int getItemCount() {
            return mDatas.size();
        }

        /**
         * 创建ViewHolder
         */
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup,final int i) {
            View view = mInflater.inflate(R.layout.activity_msibook_store_item,
                    viewGroup, false);

            ViewHolder viewHolder = new ViewHolder(view);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Bundle bundle = new Bundle();

                    bundle.putString("Master_ID", msibook_store_Item_List.get(i).GetStore_ID());

                    Intent intent = new Intent(msibook_store.this, msibook_store_account.class);

                    intent.putExtras(bundle);

                    startActivity(intent);
                }
            });



            return viewHolder;
        }

        /**
         * 设置值
         */
        @Override
        public void onBindViewHolder(final ViewHolder viewHolder, final int i) {

            viewHolder.txt_Store_Name.setText(mDatas.get(i).GetStore_Name());
        }

        public void setOnItemClickListener(AdapterView.OnItemClickListener listener) {
            this.mOnItemClickListener = listener;
        }
    }

    public class msibook_store_Item {

        String Store_ID;

        String Store_Name;

        public msibook_store_Item(String Store_ID, String Store_Name) {
            this.Store_ID = Store_ID;

            this.Store_Name = Store_Name;

        }

        public String GetStore_ID() {
            return this.Store_ID;
        }

        public String GetStore_Name() {
            return this.Store_Name;
        }


    }

}
