package dqa.com.msibook;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.github.chrisbanes.photoview.PhotoView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class msibook_pms_spec extends AppCompatActivity {

    private Context mContext;
    private ProgressDialog progressBar;

    private List<msibook_pms_spec_item> msibook_pms_spec_item_List = new ArrayList<msibook_pms_spec_item>();

    private List<String> GroupItem = new ArrayList<String>();

    private List<msibook_pms_spec_item> Introduction_List = new ArrayList<msibook_pms_spec_item>();

    private List<msibook_pms_spec_item> Introduction_Spec_List = new ArrayList<msibook_pms_spec_item>();

    private ArrayList<List<msibook_pms_spec_item>> Spec_List_Group = new ArrayList<List<msibook_pms_spec_item>>();

    private ExpandableListView exp_spec_list;

    private Spec_List_Adapter exp_Spec_List_Adapter;

    private TextView textview_title;
    private TextView textView_Stage;
    private TextView textView_RModel;

    private String[] Array_ModelPic;

    private String getIntroduction;

    private ViewPager viewPager;
    private msibook_pms_SliderViewPageAdapter adapter;
    private LinearLayout sliderDots;
    private int dotCounts;
    private ImageView[] dots;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msibook_pms_spec);
        mContext = this;

        progressBar = new ProgressDialog(this);
        progressBar.setCancelable(true);
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.setMessage("資料載入中");

        textview_title = (TextView) findViewById(R.id.textview_title);
        textView_Stage = (TextView) findViewById(R.id.textView_Stage);
        textView_RModel = (TextView) findViewById(R.id.textView_RModel);

        exp_spec_list = (ExpandableListView) findViewById(R.id.exp_spec_list);

        final PhotoView photoView = (PhotoView) findViewById(R.id.photo_view);

        String getModel = getIntent().getStringExtra("Model");
        String getStage = getIntent().getStringExtra("Stage");//抓第一頁選的部門代號
        String getRModel = getIntent().getStringExtra("RModel");//抓第一頁選的部門代號
        String getModelPic = getIntent().getStringExtra("ModelPic");//抓第一頁選的圖片路徑
        getIntroduction = getIntent().getStringExtra("Introduction");//抓第一頁選的部門代號        textView_RModel.setText(getRModel);

        textview_title.setText("MS - " + getModel);
        textView_Stage.setText(getStage);

        if(getRModel.length()<4){
            textView_RModel.setText("N/A");
        }else{
            textView_RModel.setText(getRModel.substring(0, 4));
        }

        viewPager = (ViewPager) findViewById(R.id.viewPager);//幻燈片
        sliderDots = (LinearLayout) findViewById(R.id.SliderDots);//幻燈小逗號

        //判斷圖片路徑是否為空
        if(getModelPic.isEmpty()){
            photoView.setImageResource(R.mipmap.pms_img_pms_no_pic);
            photoView.setVisibility(View.VISIBLE);
            viewPager.setVisibility(View.GONE);
            sliderDots.setVisibility(View.GONE);

        }else{

            photoView.setVisibility(View.GONE);
            viewPager.setVisibility(View.VISIBLE);
            sliderDots.setVisibility(View.VISIBLE);
            Array_ModelPic = getModelPic.split(",");
            //String ImagePath = getModelPic.replace("//172.16.111.114/File","http://wtsc.msi.com.tw/IMS/FileServer");
            //String ImagePath = Array_ModelPic[0].replace("//172.16.111.114/File","http://wtsc.msi.com.tw/IMS/FileServer");

            adapter = new msibook_pms_SliderViewPageAdapter(this,Array_ModelPic);
            viewPager.setAdapter(adapter);

            dotCounts = adapter.getCount();
            dots = new ImageView[dotCounts];

            for(int i=0;i<dotCounts;i++) {
                dots[i] = new ImageView(this);
                dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.msibook_pms_slideshow_nonactive_dot));
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                params.setMargins(8, 2, 8, 0);
                sliderDots.addView(dots[i], params);
            }

            dots[0].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.msibook_pms_slideshow_dot));

            viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                }
                @Override
                public void onPageSelected(int position) {
                    for(int i=0;i<dotCounts;i++){
                        dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.msibook_pms_slideshow_nonactive_dot));
                    }	                dots[position].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.msibook_pms_slideshow_dot));

                }
                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
        }

//        Timer timer = new Timer(); //圖片跑馬燈
//        timer.scheduleAtFixedRate(new MyTimerTask(),3000,6000);

        PMS_IPS_Spec(getModel);

    }

    //圖片跑馬燈
    private class MyTimerTask extends TimerTask {
        @Override
        public void run() {
            msibook_pms_spec.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(viewPager.getCurrentItem()==0){
                        viewPager.setCurrentItem(1);
                    }else if(viewPager.getCurrentItem()==1){
                        viewPager.setCurrentItem(2);
                    }else{
                        viewPager.setCurrentItem(0);
                    }
                }
            });
        }
    }

    private void PMS_IPS_Spec(String Model) {

        //顯示  讀取等待時間Bar
        progressBar.show();

        msibook_pms_spec_item_List.clear();

        Introduction_List.clear();

        Introduction_Spec_List.clear();

        GroupItem.clear();

        Spec_List_Group.clear();

        RequestQueue mQueue = Volley.newRequestQueue(this);

        Map<String,String> map = new HashMap<String, String>();
        map.put("Model", Model);

        if (mQueue == null) {
            mQueue = Volley.newRequestQueue(this);
        }

        HTTPSTrustManager.allowAllSSL();//信任所有证书，信任憑證
        String Path = GetServiceData.ServicePath + "/PMS_IPS_Spec";
        //String Path = "http://wtsc.msi.com.tw/IMS/MsiBook_App_Service.asmx/Insert_E_HR_Master";

        GetServiceData.SendPostRequest(Path, mQueue, new GetServiceData.VolleyStringCallback() {
            @Override
            public void onSendRequestSuccess(String result) {

                String Answer =""; //解決Jason亂碼問題
                try {
                    Answer = new String(result.getBytes("ISO-8859-1"), "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                try {

                    JSONObject obj = new JSONObject(Answer);

                    JSONArray UserArray = new JSONArray(obj.getString("Key"));

                    for (int i = 0; i < UserArray.length(); i++) {

                        JSONObject ReuqestData = UserArray.getJSONObject(i);

                        String FieldName = String.valueOf(ReuqestData.getString("FieldName"));
                        String SpecData = String.valueOf(ReuqestData.getString("SpecData"));

                        msibook_pms_spec_item msibook_pms_spec_item = new msibook_pms_spec_item(FieldName,SpecData);

                        msibook_pms_spec_item_List.add(msibook_pms_spec_item);

                    }

                    if(getIntroduction.isEmpty()){

                        Introduction_List.add(new msibook_pms_spec_item("N/A",""));

                    }else{

                        Introduction_List.add(new msibook_pms_spec_item(getIntroduction,""));

                    }

                    GroupItem.add("專案簡介 / 開案原由 / 產品介紹");
                    GroupItem.add("規格");

                    Spec_List_Group.add(Introduction_List);
                    Spec_List_Group.add(msibook_pms_spec_item_List);

                    exp_Spec_List_Adapter = new Spec_List_Adapter(mContext,GroupItem,Spec_List_Group);
                    exp_spec_list.setAdapter(exp_Spec_List_Adapter);

                    for (int i = 0; i < GroupItem.size(); i++) {
                        exp_spec_list.expandGroup(i);
                    }

                }
                catch (JSONException ex) {

                    Log.w("Json",ex.toString());

                }
                progressBar.dismiss();
            }

            @Override
            public void onSendRequestError(String result) {
                Log.w("RequestError",result);
                progressBar.dismiss();

            }

        }, map);
    }

    public class Spec_List_Adapter extends BaseExpandableListAdapter {

        private LayoutInflater mLayInf;

        private String AdapterType;

        private Context context;

        List<String> groups;

        ArrayList<List<msibook_pms_spec_item>> childs;

        protected void onActivityResult(int requestCode, int resultCode, Intent data) {

            if(data !=null) {
//                PMS_IPS_Spec(getModel);
            }

        }

        public Spec_List_Adapter(Context context, List<String> groups, ArrayList<List<msibook_pms_spec_item>> childs) {
            mLayInf = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            this.groups = groups;
            this.childs = childs;
            this.context = context;
        }

        public Object getChild(int groupPosition, int childPosition) {

            return childs.get(groupPosition).get(childPosition);
        }

        public long getChildId(int groupPosition, int childPosition) {

            return childPosition;
        }

        //獲取二級清單的View物件
        public View getChildView(int groupPosition, final int childPosition, boolean isLastChild, View convertView,
                                 ViewGroup parent) {

            View v = new View(context);
            v =  mLayInf.inflate(R.layout.msibook_pms_child_item, parent, false);
            final msibook_pms_spec_item msibook_pms_spec_item = (msibook_pms_spec_item) getChild(groupPosition, childPosition);
            final TextView textView_title = (TextView) v.findViewById(R.id.textView_title);
            final TextView textView_content = (TextView) v.findViewById(R.id.textView_content);
            textView_title.setText(msibook_pms_spec_item.FieldName);
            textView_content.setText(msibook_pms_spec_item.SpecData);

            return v;
        }




        public int getChildrenCount(int groupPosition) {
            if (groups.size() == 0) {
                return 0;
            } else {
                if (childs.size() == 0) {
                    return 0;
                } else {
                    return childs.get(groupPosition).size();
                }

            }
        }

        public Object getGroup(int groupPosition) {
            return groups.get(groupPosition);
        }

        public int getGroupCount() {
            //Log.w("GroupSize",String.valueOf(groups.size()));
            return groups.size();
        }

        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        //獲取一級清單View物件
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            String text = groups.get(groupPosition);
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            //獲取一級清單佈局檔,設置相應元素屬性
            RelativeLayout RelativeLayout = (RelativeLayout) layoutInflater.inflate(R.layout.msibook_pms_group_item, null);
            TextView textView_people_count = (TextView) RelativeLayout.findViewById(R.id.textView_people_count);
            textView_people_count.setText(text);
            ImageView parentImageViw = (ImageView) RelativeLayout.findViewById(R.id.parentImageViw);

            if (isExpanded)
            {
                parentImageViw.setBackgroundResource(R.mipmap.pms_btn_collapse);
            }
            else
            {
                parentImageViw.setBackgroundResource(R.mipmap.pms_btn_unfolded);
            }

            return RelativeLayout;
        }

        public boolean hasStableIds() {
            return false;
        }

        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return false;
        }

    }
//                if(Array_ModelPic[0].contains("//172.16.111.114")){
//                    ImagePath = Array_ModelPic[0].replace("//172.16.111.114/File","http://wtsc.msi.com.tw/IMS/FileServer");
//                }else{
//                    ImagePath = Array_ModelPic[0].replace("//172.18.16.24/File","http://wtsc.msi.com.tw/IMS/FileServers");
//                }


}
