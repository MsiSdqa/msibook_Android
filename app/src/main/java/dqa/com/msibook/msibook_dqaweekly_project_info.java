package dqa.com.msibook;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class msibook_dqaweekly_project_info extends AppCompatActivity {

    private ProgressDialog progressBar;

    private RequestQueue mQueue;

    private String getF_Model_Name;
    private String getF_ModelID;

    private TextView txt_Check_fromweb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //讀取時間Bar
        progressBar = new ProgressDialog(this);
        progressBar.setCancelable(true);
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.setMessage("圖片載入中");

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(dqa.com.msibook.R.layout.activity_msibook_dqaweekly_project_info);

        DisplayMetrics metrics = getResources().getDisplayMetrics();

        int screenWidth = (int) (metrics.widthPixels * 0.9);

        int screenHeight = (int) (metrics.heightPixels * 0.78);

        getWindow().setLayout(screenWidth, screenHeight);

        getF_Model_Name = getIntent().getStringExtra("F_Model_Name");//專案名稱
        getF_ModelID = getIntent().getStringExtra("F_ModelID");//專案代碼
        Log.w("getF_ModelID",getF_ModelID);

        Find_Model_Detail(getF_ModelID,UserData.WorkID);
        //Find_Model_Detail("12907",AppClass.LoginUserWorkID);

        ImageView Img_Issue_List = (ImageView) findViewById(dqa.com.msibook.R.id.Img_Issue_List);

        Img_Issue_List.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ItemClick(0);
            }
        });

        ImageView Img_Spec = (ImageView) findViewById(dqa.com.msibook.R.id.Img_Spec);

        Img_Spec.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ItemClick(1);
            }
        });

        ImageView Img_Member = (ImageView) findViewById(dqa.com.msibook.R.id.Img_Member);

        Img_Member.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ItemClick(2);
            }
        });

    }

    private void Find_Model_Detail(String ModelID, String WorkID) {

        if (mQueue == null) {
            mQueue = Volley.newRequestQueue(this);
        }

        String Path = "http://wtsc.msi.com.tw/IMS/IMS_App_Service.asmx/Find_Model_Detail?ModelID=" + ModelID + "&WorkID=" + WorkID;

        getString(Path, mQueue, new GetServiceData.VolleyCallback() {
            @Override
            public void onSuccess(JSONObject result) {

                ModelData_Detail(result);
            }
        });

    }

    private void ModelData_Detail(JSONObject result) {

//        //顯示  讀取等待時間Bar
//        progressBar.show();


        try {

            JSONArray UserArray = new JSONArray(result.getString("Key"));

            if (UserArray.length() > 0) {


                JSONObject IssueData = UserArray.getJSONObject(0);

                String P1 = IssueData.getString("P1");

                String ModelID = String.valueOf(IssueData.getInt("ModelID"));

                String ModelPic = IssueData.getString("ModelPic");
                Log.w("ModelPic",ModelPic);

                boolean Model_Favorit = IssueData.getBoolean("Model_Favorit");

                Double CloseRate = IssueData.getDouble("CloseRate");

                String CurrentStage = IssueData.getString("CurrentStage");

                String MarketName = IssueData.getString("MarketName");

                String Introduction = IssueData.getString("Introduction");


                final ImageView Img_Project_Pic_Large = (ImageView) findViewById(dqa.com.msibook.R.id.Img_Project_Pic_Large);

                TextView txt_ProjectInfo_Name = (TextView) findViewById(dqa.com.msibook.R.id.txt_ProjectInfo_Name);

                TextView txt_ProjectInfo_Rate = (TextView) findViewById(dqa.com.msibook.R.id.txt_ProjectInfo_Rate);

                TextView txt_Model_Stage = (TextView) findViewById(dqa.com.msibook.R.id.txt_Model_Stage);

                TextView txt_Model_MarketName = (TextView) findViewById(dqa.com.msibook.R.id.txt_Model_MarketName);

                TextView txt_Model_Priority = (TextView) findViewById(dqa.com.msibook.R.id.txt_Model_Priority);

                txt_Check_fromweb = (TextView) findViewById(dqa.com.msibook.R.id.txt_Check_fromweb);

                if(Introduction.contains("FROMLayoutWeb") && ModelPic.length()<5){
                    txt_Check_fromweb.setVisibility(View.VISIBLE);
                    txt_Check_fromweb.setText("No Picture Since\nThis Project From LayoutWeb");
                    txt_Model_MarketName.setVisibility(View.GONE);
                    Img_Project_Pic_Large.setVisibility(View.GONE);
                }else{
                    txt_Check_fromweb.setVisibility(View.GONE);
                    txt_Model_MarketName.setVisibility(View.VISIBLE);
                    Img_Project_Pic_Large.setVisibility(View.VISIBLE);
                }

                if (!MarketName.contains("null")) {
                    txt_Model_MarketName.setText(MarketName);
                }

                if (!P1.contains("null")) {
                    txt_Model_Priority.setText(P1);
                }

                txt_Model_Stage.setText(CurrentStage);

                txt_ProjectInfo_Name.setText(getF_Model_Name);

                try {

                    txt_ProjectInfo_Rate.setText(String.format("%.0f", CloseRate * 100) + "%");

                    if (CloseRate * 100 < 80) {
                        txt_ProjectInfo_Rate.setTextColor(getResources().getColor(dqa.com.msibook.R.color.ProjectInfo_WarningColor));
                    }

                } catch (NumberFormatException e) {
                    txt_ProjectInfo_Rate.setText("0%");
                }

                try {

                    if(ModelPic.length()>5){ //有資料就讀取 沒資料就跳過

                        //顯示  讀取等待時間Bar
                        progressBar.show();

                        Glide.with(msibook_dqaweekly_project_info.this)
                                .load("http://wtsc.msi.com.tw/IMS/IMS_App_Service.asmx/Get_File?FileName=" + ModelPic)
                                .asBitmap()
                                .into(new SimpleTarget<Bitmap>(300, 300) {
                                    @Override
                                    public void onResourceReady(Bitmap bitmap, GlideAnimation anim) {

                                        //BitmapDrawable ob = new BitmapDrawable(getResources(), AppClass.roundCornerImage(bitmap,0));
                                        //Img_ProjectInfo.setBackground(ob);

                                        BitmapDrawable ob = new BitmapDrawable(getResources(), bitmap);
                                        Img_Project_Pic_Large.setImageBitmap(ob.getBitmap());
                                        //Img_Project_Pic_Large.setBackground(ob);
                                        //Rel_Project_Layout.setBackground(ob);

                                        //顯示  讀取等待時間Bar
                                        progressBar.dismiss();
                                    }
                                });


                    }else{
                        Img_Project_Pic_Large.setImageResource(R.mipmap.pms_img_pms_no_pic);
                    }


                } catch (Exception ex) {

                }


            }
        } catch (JSONException ex) {


        }
//
//        //關閉-讀取等待時間Bar
//        progressBar.dismiss();


    }

    private void ItemClick(int Position) {

        Bundle bundle = new Bundle();

        bundle.putString("ModelID", getF_ModelID);

        bundle.putString("ModelName", getF_Model_Name);



        Intent intent = new Intent();

        switch (Position) {
//                case 0:
//                    intent = new Intent(this, IssueList.class);
//
//                    intent.putExtras(bundle);
//
//                    startActivity(intent);
//
//                    //finish();
//                    break;
            case 1:
                intent = new Intent(this, msibook_dqaweekly_project_spec.class);

                intent.putExtras(bundle);

                startActivity(intent);

                //finish();
                break;
            case 2:
                intent = new Intent(this, msibook_dqaweekly_project_member.class);

                intent.putExtras(bundle);

                startActivity(intent);

                //finish();
                break;
        }

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
}
