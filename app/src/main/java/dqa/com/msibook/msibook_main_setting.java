package dqa.com.msibook;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.vision.text.Line;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link msibook_main_setting.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link msibook_main_setting#newInstance} factory method to
 * create an instance of this fragment.
 */
public class msibook_main_setting extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private static final int REQUEST_EXTERNAL_STORAGE = 1;

    private static final String WRITE_EXTERNAL_STORAGE = "android.permission.WRITE_EXTERNAL_STORAGE";
    private static final String READ_EXTERNAL_STORAGE = "android.permission.READ_EXTERNAL_STORAGE";

    // 資料庫物件
    private SQLiteDatabase db;
    // 表格名稱
    public static final String TABLE_NAME = "UserData";
    public static final String Region_COLUMN = "_Region";

    private ProgressDialog progressBar;
    private Context mContext;

    private TextView textView_user;//使用者

    private LinearLayout linear_push;//推播
    private TextView textView_push;
    private Switch push_switch;

    private LinearLayout linear_font_size;//設定字體
    private TextView textView_font_size;

    private LinearLayout linear_about_msi;//關於
    private TextView textView_about_msi;

    private LinearLayout linear_qbq;//環安衛
    private TextView textView_qbq;

    private LinearLayout linear_strain_team;//緊急應變小組
    private TextView textView_strain_team;

    private LinearLayout linear_foodmenu;//每週菜單
    private TextView textView_foodmenu;

    private LinearLayout linear_maskmap;//口罩地圖
    private TextView textView_maskmap;

    private LinearLayout linear_facebook;//facebook
    private TextView textView_facebook;

    private LinearLayout linear_youtube;//youtube
    private TextView textView_youtube;

    private LinearLayout linear_extension;//分機查詢
    private TextView textView_extension;

    private LinearLayout linear_version;//版本紀錄
    private TextView textView_version;
    private String updatePath;//更新path

    private LinearLayout linear_checkupdate;//檢查更新
    private TextView textView_checkupdate;
    private TextView textView_update_title;
    private TextView textView_lastupdate;//上次更新

    private LinearLayout linear_qa;//問題回報
    private TextView textView_qa;

    private SwitchCompat switch_main_push;

    private LinearLayout linear_login_out;
    private TextView textView_login_out;

    public msibook_main_setting() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment msibook_main_setting.
     */
    // TODO: Rename and change types and number of parameters
    public static msibook_main_setting newInstance(String param1, String param2) {
        msibook_main_setting fragment = new msibook_main_setting();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_msibook_main_setting, container, false);
        mContext = getContext();

        progressBar = new ProgressDialog(this.mContext);
        progressBar.setCancelable(true);
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.setMessage("資料載入中");

        textView_user = (TextView) v.findViewById(R.id.textView_user);

        String[] Keyman = {"10015812", "10016109","10016049","10003275","10016295","10015635"};//權限- 管理員
        if (Arrays.asList(Keyman).contains(UserData.WorkID)){
            textView_user.setText(UserData.Account+"("+UserData.Name+")  管理者");
        }else{
            textView_user.setText(UserData.Account+"("+UserData.Name+")  一般使用者");
        }

        linear_push = (LinearLayout) v.findViewById(R.id.linear_push);
        textView_push = (TextView) v.findViewById(R.id.textView_push);
        switch_main_push = (SwitchCompat)v.findViewById(R.id.switch_main_push);

        linear_font_size = (LinearLayout) v.findViewById(R.id.linear_font_size);
        textView_font_size = (TextView) v.findViewById(R.id.textView_font_size);

        linear_about_msi = (LinearLayout) v.findViewById(R.id.linear_about_msi);
        textView_about_msi = (TextView) v.findViewById(R.id.textView_about_msi);
        linear_about_msi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent msibook_qbq = new Intent(getActivity(),msibook_about_msi.class);
                //msibook_ims_myissue.putExtra("Control_IMS_RoleID  ", Control_IMS_RoleID);
                startActivityForResult(msibook_qbq,1);
            }
        });

        linear_qbq = (LinearLayout) v.findViewById(R.id.linear_qbq);
        textView_qbq = (TextView) v.findViewById(R.id.textView_qbq);
        linear_qbq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent msibook_qbq = new Intent(getActivity(),msibook_qbq_post_card.class);
                //msibook_ims_myissue.putExtra("Control_IMS_RoleID  ", Control_IMS_RoleID);
                startActivityForResult(msibook_qbq,1);
            }
        });

        //緊急應變
        linear_strain_team = (LinearLayout) v.findViewById(R.id.linear_strain_team);
        textView_strain_team = (TextView) v.findViewById(R.id.textView_strain_team);
        linear_strain_team.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent msibook_strain_team = new Intent(getActivity(),msibook_main_setting_strain_team.class);
                //msibook_ims_myissue.putExtra("Control_IMS_RoleID  ", Control_IMS_RoleID);
                startActivityForResult(msibook_strain_team,1);
            }
        });

        //每週菜單
        linear_foodmenu = (LinearLayout) v.findViewById(R.id.linear_foodmenu);
        textView_foodmenu = (TextView) v.findViewById(R.id.textView_foodmenu);
        linear_foodmenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent msibook_foodmenu = new Intent(getActivity(),msibook_main_setting_foodmenu.class);
                //msibook_ims_myissue.putExtra("Control_IMS_RoleID  ", Control_IMS_RoleID);
                startActivityForResult(msibook_foodmenu,1);
            }
        });

        //口罩地圖
        linear_maskmap = (LinearLayout) v.findViewById(R.id.linear_maskmap);
        textView_maskmap = (TextView) v.findViewById(R.id.textView_maskmap);
        linear_maskmap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://news.campaign.yahoo.com.tw/2019-nCoV/mask.php";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });

        //Facebook
        linear_facebook = (LinearLayout) v.findViewById(R.id.linear_facebook);
        textView_facebook = (TextView) v.findViewById(R.id.textView_facebook);
        linear_facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://m.facebook.com/DqaPG/?modal=admin_todo_tour";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });
        //Youtube
        linear_youtube = (LinearLayout) v.findViewById(R.id.linear_youtube);
        textView_youtube = (TextView) v.findViewById(R.id.textView_youtube);
        linear_youtube.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://dqapg.blogspot.com/2020/07/qa.html";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });

        linear_extension = (LinearLayout) v.findViewById(R.id.linear_extension);
        textView_extension = (TextView) v.findViewById(R.id.textView_extension);
        linear_extension.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent msibook_extension = new Intent(getActivity(),msibook_extension_search.class);
                //msibook_ims_myissue.putExtra("Control_IMS_RoleID  ", Control_IMS_RoleID);
                startActivityForResult(msibook_extension,1);
            }
        });

        //版本說明
        linear_version = (LinearLayout) v.findViewById(R.id.linear_version);
        textView_version = (TextView) v.findViewById(R.id.textView_version);
        linear_version.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                infoPopupWindow();
            }
        });

        linear_checkupdate = (LinearLayout) v.findViewById(R.id.linear_checkupdate);
        textView_checkupdate = (TextView) v.findViewById(R.id.textView_checkupdate);
        textView_update_title = (TextView) v.findViewById(R.id.textView_update_title);
        textView_lastupdate = (TextView) v.findViewById(R.id.textView_lastupdate);
        linear_checkupdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isUpdate(mContext);
            }
        });

        linear_qa = (LinearLayout) v.findViewById(R.id.linear_qa);
        textView_qa = (TextView) v.findViewById(R.id.textView_qa);
        linear_qa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                QA_Sent();
            }
        });

        linear_login_out = (LinearLayout) v.findViewById(R.id.linear_login_out);
        textView_login_out = (TextView) v.findViewById(R.id.textView_login_out);
        linear_login_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog=new AlertDialog.Builder(mContext)
                        //.setTitle("登出")
                        .setMessage("確定是否登出?")//设置提示内容
                        //确定按钮
                        .setPositiveButton("登出", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //先刪除資料表
                                //String dropTable0 =  "DROP TABLE IF EXISTS " + TABLE_NAME;    //刪除UserDB
                                String deleteTable0 =  "delete from "+ TABLE_NAME;    //刪除UserDB
                                db = DBHelper.getDatabase(getActivity());
                                db.execSQL(deleteTable0);//刪除資料表
                                db.close();

                                Intent intent = new Intent(getActivity(), msibook_login.class);
                                startActivity(intent);
                                getActivity().finish();

                                Toast.makeText(getActivity(), "已登出，請重新登入。", Toast.LENGTH_SHORT).show();
                            }
                        })
                        //取消按钮
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .create();//创建对话框
                dialog.show();//显示对话框
            }
        });

        isUpdateTextView(getActivity());

        return v;



    }

    //----------版本說明-----------------
    private Button btn_close_popinfo;
    private PopupWindow pwindoinfo;
    private void infoPopupWindow(){
        try{
            // We need to get the instance of the LayoutInflater
            LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View layout = inflater.inflate(R.layout.msibook_info_popup_window,(ViewGroup) getActivity().findViewById(R.id.popup_element));

            DisplayMetrics metrics = getResources().getDisplayMetrics();
            int screenWidth = (int) (metrics.widthPixels * 0.8);
            int screenHeight = (int) (metrics.heightPixels * 0.78);

            pwindoinfo = new PopupWindow(layout,screenWidth,screenHeight,true);
            pwindoinfo.showAtLocation(layout, Gravity.CENTER,0,0);

            btn_close_popinfo = (Button) layout.findViewById(R.id.close_info);
            btn_close_popinfo.setOnClickListener(cancelinfo_button_click_listener);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private View.OnClickListener cancelinfo_button_click_listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            pwindoinfo.dismiss();
        }
    };


    //---------比對版本-------
    private static int getVersionCode(Context context)
    {
        int versionCode = 0;
        try
        {
            Log.w("context.getPackageName",context.getPackageName());
            // 獲取軟件版本號，對應AndroidManifest.xml下android:versionCode
            versionCode = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e)
        {
            e.printStackTrace();
        }
        return versionCode;
    }

    //檢查更新
    public void isUpdateTextView(final Context context)
    {

        RequestQueue mQueue = Volley.newRequestQueue(context);

        //String Path = GetServiceData.ServicePath + "/Get_Application_Version?Type=Android";
        String Path = "http://wtsc.msi.com.tw/IMS/MsiBook_App_Service.asmx/Get_Application_Version?Type=Android";

        GetServiceData.getString(Path, mQueue, new GetServiceData.VolleyCallback() {
            @Override
            public void onSuccess(JSONObject result) {

                try {

                    JSONArray UserArray = new JSONArray(result.getString("Key"));

                    if (UserArray.length() > 0) {

                        UpdateManager mUpdateManager;

                        int versionCode = getVersionCode(context);

                        JSONObject IssueData = UserArray.getJSONObject(0);

                        String Version = String.valueOf(IssueData.getInt("Version"));

                        String Path = String.valueOf(IssueData.getString("Path"));
                        updatePath = Path;

                        Log.w("裝置版本",String.valueOf(versionCode));
                        Log.w("Server上版本",String.valueOf(Version));

                        if (Version != String.valueOf(versionCode))
                        {
                            textView_lastupdate.setVisibility(View.VISIBLE);
                            textView_update_title.setVisibility(View.VISIBLE);
                        }else{
                            textView_lastupdate.setVisibility(View.INVISIBLE);
                            textView_update_title.setVisibility(View.GONE);
                        }

                    }
                } catch (JSONException ex) {

                }

            }
        });

        //return VersionCheck;
    }


    //檢查更新
    public void isUpdate(final Context context)
    {

        RequestQueue mQueue = Volley.newRequestQueue(context);

        //String Path = GetServiceData.ServicePath + "/Get_Application_Version?Type=Android";
        String Path = "http://wtsc.msi.com.tw/IMS/MsiBook_App_Service.asmx/Get_Application_Version?Type=Android";

        GetServiceData.getString(Path, mQueue, new GetServiceData.VolleyCallback() {
            @Override
            public void onSuccess(JSONObject result) {

                try {

                    JSONArray UserArray = new JSONArray(result.getString("Key"));

                    if (UserArray.length() > 0) {

                        UpdateManager mUpdateManager;

                        int versionCode = getVersionCode(context);

                        JSONObject IssueData = UserArray.getJSONObject(0);

                        String Version = String.valueOf(IssueData.getInt("Version"));

                        String Path = String.valueOf(IssueData.getString("Path"));
                        updatePath = Path;

                        Log.w("裝置版本",String.valueOf(versionCode));
                        Log.w("Server上版本",String.valueOf(Version));

                        if (Version != String.valueOf(versionCode))
                        {
                            //showDialog(); Google play的更新
                            int permission = ActivityCompat.checkSelfPermission(getActivity(), WRITE_EXTERNAL_STORAGE);

                            if (permission != PackageManager.PERMISSION_GRANTED) {
                                // 無權限，向使用者請求
                                ActivityCompat.requestPermissions(
                                        getActivity(),
                                        new String[]{WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE}, REQUEST_EXTERNAL_STORAGE
                                );

                            } else {
                                mUpdateManager = new UpdateManager(context,Path);
                                mUpdateManager.checkUpdateInfo();
                            }


                        }else{
                            Toast.makeText(getActivity(), "目前為最新版本", Toast.LENGTH_SHORT).show();
                        }

                    }
                } catch (JSONException ex) {

                }

            }
        });

        //return VersionCheck;
    }


    //--------問題回報---------
    public void QA_Sent()
    {
        //指定與電子郵件相關的變量值
        String[] emails = new String[]{
                "iriswu@msi.com"
        };
        // emails for cc
        String[] emailsCC = new String[]{
                "larryliao@msi.com","matthewchi@msi.com","joanneyu@msi.com"
        };

        String mailSubject = "Android msiBook 問題回報";
        String mailBody = "問題描述 :";
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.putExtra(Intent.EXTRA_EMAIL,emails);
        intent.putExtra(Intent.EXTRA_CC,emailsCC);
        intent.putExtra(Intent.EXTRA_SUBJECT,mailSubject);
        intent.putExtra(Intent.EXTRA_TEXT,mailBody);
        intent.setData(Uri.parse("mailto:"));
        // Try to start the activity
        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivity(intent);
        }else{
            // If there are no email client installed in this device
            Toast.makeText(getActivity(),"No email client installed in this device.",Toast.LENGTH_SHORT).show();
        }
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
        Activity a;
        if (context instanceof Activity){
            a=(Activity) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
