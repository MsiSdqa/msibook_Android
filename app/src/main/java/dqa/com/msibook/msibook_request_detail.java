package dqa.com.msibook;

import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class msibook_request_detail extends AppCompatActivity {

    public class msibook_request_file_item implements Serializable {

        String F_FileName;
        String F_FileType;
        String F_DownloadFilePath;

        public msibook_request_file_item(String F_FileName,String F_FileType,String F_DownloadFilePath) {

            this.F_FileName = F_FileName;
            this.F_FileType = F_FileType;
            this.F_DownloadFilePath = F_DownloadFilePath;

        }

    }

    private msibook_request_item msibook_request_item;

    private msibook_file_list_adapter mmsibook_file_list_adapter;

    private RecyclerView rec_view_show_file;

    public ArrayList<msibook_request_file_item> msibook_request_file_List = new ArrayList<msibook_request_file_item>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msibook_request_detail);

        msibook_request_item = (msibook_request_item)getIntent().getSerializableExtra("CustomObject ");

        TextView editText_subject = (TextView)findViewById(R.id.editText_subject);
        TextView textView_finish_time = (TextView)findViewById(R.id.textView_finish_time);
        TextView editText_Content = (TextView)findViewById(R.id.editText_Content);
        TextView textView_Resp_User = (TextView)findViewById(R.id.textView_Resp_User);
        rec_view_show_file = (RecyclerView)findViewById(R.id.rec_view_show_file);
        try {
            editText_subject.setText(new String(msibook_request_item.F_Subject.getBytes("ISO-8859-1"), "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        Log.w("TTTTTTT",String.valueOf(msibook_request_item.F_ExpectFixDate));
        if (msibook_request_item.F_ExpectFixDate.length()<5) //判斷是不是為 null
        {
            textView_finish_time.setText("N/A");
        }
        else
        {

            String dtStart = msibook_request_item.F_ExpectFixDate.replace("T"," ");



            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date date = format.parse(dtStart);
                String dateString = sdf.format(date);
                textView_finish_time.setText(dateString);

                Date NowDate = new Date();
                long diff = date.getTime() - NowDate.getTime();

            } catch (ParseException e) {
                Log.w("ErrorDate",e.toString());
            }
        }

        try {
            editText_Content.setText(new String(msibook_request_item.F_Desc.getBytes("ISO-8859-1"), "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        try {
            textView_Resp_User.setText(new String(msibook_request_item.RespUser.getBytes("ISO-8859-1"), "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        try {
            Request_File_List(new String(msibook_request_item.F_SeqNo.getBytes("ISO-8859-1"), "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }

    private void Request_File_List_Mapping(String result)
    {
        msibook_request_file_List.clear();

        try {

            JSONObject obj = new JSONObject(result);



            JSONArray UserArray = new JSONArray(obj.getString("Request_File"));

            for (int i = 0; i < UserArray.length(); i++) {



                JSONObject ReuqestData = UserArray.getJSONObject(i);

                String F_FileName = String.valueOf(ReuqestData.getString("F_FileName"));
                String F_FileType = String.valueOf(ReuqestData.getString("F_FileType"));
                String F_DownloadFilePath = String.valueOf(ReuqestData.getString("F_DownloadFilePath"));

                msibook_request_file_List.add(new msibook_request_file_item(F_FileName,F_FileType,F_DownloadFilePath));

            }

            mmsibook_file_list_adapter = new msibook_file_list_adapter(msibook_request_file_List);



            final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            rec_view_show_file.setLayoutManager(layoutManager);
            rec_view_show_file.setAdapter(mmsibook_file_list_adapter);

        } catch (JSONException ex) {

            Log.w("Request_File",ex.toString());

        }

    }

    private void Request_File_List(String F_SeqNo) {

        RequestQueue mQueue = Volley.newRequestQueue(this);

        Map<String,String> map = new HashMap<String, String>();
        map.put("F_SeqNo", F_SeqNo);

        if (mQueue == null) {
            mQueue = Volley.newRequestQueue(this);
        }

        HTTPSTrustManager.allowAllSSL();//信任所有证书，信任憑證
        String Path = GetServiceData.ServicePath + "/Request_Detail";
        //String Path = "http://wtsc.msi.com.tw/IMS/MsiBook_App_Service.asmx/Insert_E_HR_Master";

        GetServiceData.SendPostRequest(Path, mQueue, new GetServiceData.VolleyStringCallback() {
            @Override
            public void onSendRequestSuccess(String result) {

                try {

                    JSONObject obj = new JSONObject(result);

                    Request_File_List_Mapping(obj.getJSONObject("Key").toString());

                } catch (JSONException ex)
                {
                    Log.w("Json", ex.toString());
                }
            }


            @Override
            public void onSendRequestError(String result) {
                Log.w("RequestError",result);

            }

        }, map);

    }

    public class msibook_file_list_adapter extends RecyclerView.Adapter<msibook_file_list_adapter.ViewHolder> {
        private ArrayList<msibook_request_detail.msibook_request_file_item> msibook_request_file_List ;

        public class ViewHolder extends RecyclerView.ViewHolder {
            private TextView mtextView_file_title;
            private ImageView mimageView_file;
            public ViewHolder(View v) {
                super(v);
                mtextView_file_title = (TextView) v.findViewById(R.id.textView_file_title);
                mimageView_file = (ImageView) v.findViewById(R.id.imageView_file);
            }
        }

        public msibook_file_list_adapter(ArrayList<msibook_request_detail.msibook_request_file_item> msibook_request_file_List) {


            this.msibook_request_file_List = msibook_request_file_List;
        }

        @Override
        public msibook_file_list_adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.msibook_request_attach_file_item, parent, false);
            ViewHolder vh = new ViewHolder(v);
            return vh;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {

            Log.w("RecycleTest",msibook_request_file_List.get(position).F_FileName);

            holder.mtextView_file_title.setText(msibook_request_file_List.get(position).F_FileName + msibook_request_file_List.get(position).F_FileType);

            Log.w("檔案網址",String.valueOf(msibook_request_file_List.get(position).F_DownloadFilePath));

            holder.mimageView_file.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    HTTPSTrustManager.allowAllSSL();//信任所有证书，信任憑證
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(GetServiceData.IMS_ServicePath + "/Get_File?FileName=" + msibook_request_file_List.get(position).F_DownloadFilePath));
                    startActivity(browserIntent);
                }
            });
        }

        @Override
        public int getItemCount() {
            Log.w("RequestFileSize",String.valueOf(msibook_request_file_List.size()));
            return msibook_request_file_List.size();
        }
    }


}
