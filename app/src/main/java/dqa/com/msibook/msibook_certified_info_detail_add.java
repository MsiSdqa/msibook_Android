package dqa.com.msibook;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link msibook_certified_info_detail_add.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link msibook_certified_info_detail_add#newInstance} factory method to
 * create an instance of this fragment.
 */
public class msibook_certified_info_detail_add extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


//    private Button textView_CEC;
    private Button textView_Energy;
    private Button textView_Erp;
    private Button textView_MEPS;
    private Button textView_CECP;
    private Button textView_HDMI;
    private Button textView_DisplayPort;
    private Button textView_HDCP;
    private Button textView_WHQL7;
    private Button textView_WHQL81;
    private Button textView_RS3;
    private Button textView_RS4;

    private TextView textView_CEC;
    private TextView textView_CEC_add;
    private TextView textView_CEC_badge;

    private RelativeLayout Rela_CEC;
    private RelativeLayout Rela_Energy;
    private RelativeLayout Rela_HDMI;
    private RelativeLayout Rela_HDCP;
    private RelativeLayout Rela_WHQL7;
    private RelativeLayout Rela_WHQL8;


    private Context mContext;

    private View mView;

    private OnFragmentInteractionListener mListener;

    private static String SaveModel;
    private static String SaveModelID;

    public msibook_certified_info_detail_add() {
        // Required empty public constructor
    }

    //點選Local回傳狀態
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(data !=null) {
            Bundle bundle = data.getExtras();
            Integer CheckBooking = Integer.valueOf(bundle.getString("Certified_Check"));
            if(CheckBooking ==1){
                textView_CEC.setBackgroundResource(R.drawable.certified_add_bg_red2);
                textView_CEC_add.setVisibility(View.INVISIBLE);
                textView_CEC_badge.setVisibility(View.VISIBLE);
                textView_CEC_badge.setText("0%");
                textView_CEC_badge.setBackgroundResource(R.drawable.certified_add_ceradd_bg);
            }

        }

    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment msibook_certified_info_detail_add.
     */
    // TODO: Rename and change types and number of parameters
    public static msibook_certified_info_detail_add newInstance(String param1, String param2,String Model,String ModelID) {
        msibook_certified_info_detail_add fragment = new msibook_certified_info_detail_add();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);

        SaveModel=Model;
        SaveModelID=ModelID;

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
        mView = inflater.inflate(R.layout.fragment_msibook_certified_info_detail_add, container, false);
        mContext = getContext();

        Rela_CEC = (RelativeLayout) mView.findViewById(R.id.Rela_CEC);
        textView_CEC = (TextView) mView.findViewById(R.id.textView_CEC);
        textView_CEC_add = (TextView) mView.findViewById(R.id.textView_CEC_add);
        textView_CEC_badge = (TextView) mView.findViewById(R.id.textView_CEC_badge);

//        textView_CEC = (Button) mView.findViewById(R.id.textView_CEC);
//        textView_Energy = (Button) mView.findViewById(R.id.textView_Energy);
//        textView_Erp = (Button) mView.findViewById(R.id.textView_Erp);
//        textView_MEPS = (Button) mView.findViewById(R.id.textView_MEPS);
//        textView_CECP = (Button) mView.findViewById(R.id.textView_CECP);
//        textView_HDMI = (Button) mView.findViewById(R.id.textView_HDMI);
//        textView_DisplayPort = (Button) mView.findViewById(R.id.textView_DisplayPort);
//        textView_HDCP = (Button) mView.findViewById(R.id.textView_HDCP);
//        textView_WHQL7 = (Button) mView.findViewById(R.id.textView_WHQL7);
//        textView_WHQL81 = (Button) mView.findViewById(R.id.textView_WHQL81);
//        textView_RS3 = (Button) mView.findViewById(R.id.textView_RS3);
//        textView_RS4 = (Button) mView.findViewById(R.id.textView_RS4);
//

        Rela_CEC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    addCer_Message(UserData.WorkID,SaveModel,SaveModelID,"環保類-CEC","61");
            }
        });

//        textView_CEC.setOnTouchListener(new Button.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                if (event.getAction() == MotionEvent.ACTION_DOWN) {  //按下的時候改變背景及顏色
//                    textView_CEC.setBackgroundResource(R.drawable.certified_add_bg_red);
//                }
//                if (event.getAction() == MotionEvent.ACTION_UP) {  //起來的時候恢復背景與顏色
//                    textView_CEC.setBackgroundResource(R.drawable.certified_add_bg_white);
//                    addCer_Message(UserData.WorkID,SaveModel,SaveModelID,"環保類-CEC","61");
//                }
//                return false;
//            }
//        });
//
//        textView_Energy.setOnTouchListener(new Button.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                if (event.getAction() == MotionEvent.ACTION_DOWN) {  //按下的時候改變背景及顏色
//                    textView_Energy.setBackgroundResource(R.drawable.certified_add_bg_red);
//                }
//                if (event.getAction() == MotionEvent.ACTION_UP) {  //起來的時候恢復背景與顏色
//                    textView_Energy.setBackgroundResource(R.drawable.certified_add_bg_white);
//                    addCer_Message(UserData.WorkID,SaveModel,SaveModelID,"環保類-Energy Star","20");
//                }
//                return false;
//            }
//        });
//
//        textView_Erp.setOnTouchListener(new Button.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                if (event.getAction() == MotionEvent.ACTION_DOWN) {  //按下的時候改變背景及顏色
//                    textView_Erp.setBackgroundResource(R.drawable.certified_add_bg_red);
//                }
//                if (event.getAction() == MotionEvent.ACTION_UP) {  //起來的時候恢復背景與顏色
//                    textView_Erp.setBackgroundResource(R.drawable.certified_add_bg_white);
//                    addCer_Message(UserData.WorkID,SaveModel,SaveModelID,"環保類-ErP Lot 3","53");
//                }
//                return false;
//            }
//        });
//
//        textView_MEPS.setOnTouchListener(new Button.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                if (event.getAction() == MotionEvent.ACTION_DOWN) {  //按下的時候改變背景及顏色
//                    textView_MEPS.setBackgroundResource(R.drawable.certified_add_bg_red);
//                }
//                if (event.getAction() == MotionEvent.ACTION_UP) {  //起來的時候恢復背景與顏色
//                    textView_MEPS.setBackgroundResource(R.drawable.certified_add_bg_white);
//                    addCer_Message(UserData.WorkID,SaveModel,SaveModelID,"環保類-MEPS","52");
//                }
//                return false;
//            }
//        });
//
//        textView_CECP.setOnTouchListener(new Button.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                if (event.getAction() == MotionEvent.ACTION_DOWN) {  //按下的時候改變背景及顏色
//                    textView_CECP.setBackgroundResource(R.drawable.certified_add_bg_red);
//                }
//                if (event.getAction() == MotionEvent.ACTION_UP) {  //起來的時候恢復背景與顏色
//                    textView_CECP.setBackgroundResource(R.drawable.certified_add_bg_white);
//                    addCer_Message(UserData.WorkID,SaveModel,SaveModelID,"環保類-中國CECP","21");
//                }
//                return false;
//            }
//        });
//
//        textView_HDMI.setOnTouchListener(new Button.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                if (event.getAction() == MotionEvent.ACTION_DOWN) {  //按下的時候改變背景及顏色
//                    textView_HDMI.setBackgroundResource(R.drawable.certified_add_bg_red);
//                }
//                if (event.getAction() == MotionEvent.ACTION_UP) {  //起來的時候恢復背景與顏色
//                    textView_HDMI.setBackgroundResource(R.drawable.certified_add_bg_white);
//                    addCer_Message(UserData.WorkID,SaveModel,SaveModelID,"硬體端子類-HDMI","12");
//                }
//                return false;
//            }
//        });
//
//        textView_DisplayPort.setOnTouchListener(new Button.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                if (event.getAction() == MotionEvent.ACTION_DOWN) {  //按下的時候改變背景及顏色
//                    textView_DisplayPort.setBackgroundResource(R.drawable.certified_add_bg_red);
//                }
//                if (event.getAction() == MotionEvent.ACTION_UP) {  //起來的時候恢復背景與顏色
//                    textView_DisplayPort.setBackgroundResource(R.drawable.certified_add_bg_white);
//                    addCer_Message(UserData.WorkID,SaveModel,SaveModelID,"設計認證類-DisplayPort","44");
//                }
//                return false;
//            }
//        });
//
//
//        textView_HDCP.setOnTouchListener(new Button.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                if (event.getAction() == MotionEvent.ACTION_DOWN) {  //按下的時候改變背景及顏色
//                    textView_HDCP.setBackgroundResource(R.drawable.certified_add_bg_red);
//                }
//                if (event.getAction() == MotionEvent.ACTION_UP) {  //起來的時候恢復背景與顏色
//                    textView_HDCP.setBackgroundResource(R.drawable.certified_add_bg_white);
//                    addCer_Message(UserData.WorkID,SaveModel,SaveModelID,"設計認證類-HDCP","14");
//                }
//                return false;
//            }
//        });
//
//        textView_WHQL7.setOnTouchListener(new Button.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                if (event.getAction() == MotionEvent.ACTION_DOWN) {  //按下的時候改變背景及顏色
//                    textView_WHQL7.setBackgroundResource(R.drawable.certified_add_bg_red);
//                }
//                if (event.getAction() == MotionEvent.ACTION_UP) {  //起來的時候恢復背景與顏色
//                    textView_WHQL7.setBackgroundResource(R.drawable.certified_add_bg_white);
//                    addCer_Message(UserData.WorkID,SaveModel,SaveModelID,"設計認證類-WHQL-System(Win7)","17");
//                }
//                return false;
//            }
//        });
//
//        textView_WHQL81.setOnTouchListener(new Button.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                if (event.getAction() == MotionEvent.ACTION_DOWN) {  //按下的時候改變背景及顏色
//                    textView_WHQL81.setBackgroundResource(R.drawable.certified_add_bg_red);
//                }
//                if (event.getAction() == MotionEvent.ACTION_UP) {  //起來的時候恢復背景與顏色
//                    textView_WHQL81.setBackgroundResource(R.drawable.certified_add_bg_white);
//                    addCer_Message(UserData.WorkID,SaveModel,SaveModelID,"設計認證類-WHQL-System(Win8.1)","49");
//                }
//                return false;
//            }
//        });
//
//        textView_RS3.setOnTouchListener(new Button.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                if (event.getAction() == MotionEvent.ACTION_DOWN) {  //按下的時候改變背景及顏色
//                    textView_RS3.setBackgroundResource(R.drawable.certified_add_bg_red);
//                }
//                if (event.getAction() == MotionEvent.ACTION_UP) {  //起來的時候恢復背景與顏色
//                    textView_RS3.setBackgroundResource(R.drawable.certified_add_bg_white);
//                    addCer_Message(UserData.WorkID,SaveModel,SaveModelID,"設計認證類-Win10 RS3","59");
//                }
//                return false;
//            }
//        });
//
//        textView_RS4.setOnTouchListener(new Button.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                if (event.getAction() == MotionEvent.ACTION_DOWN) {  //按下的時候改變背景及顏色
//                    textView_RS4.setBackgroundResource(R.drawable.certified_add_bg_red);
//                }
//                if (event.getAction() == MotionEvent.ACTION_UP) {  //起來的時候恢復背景與顏色
//                    textView_RS4.setBackgroundResource(R.drawable.certified_add_bg_white);
//                    addCer_Message(UserData.WorkID,SaveModel,SaveModelID,"設計認證類-Win10 RS4","63");
//                }
//                return false;
//            }
//        });



        return mView;
    }

    //存放 類別
    private void addCer_Message(String WorkID,String SaveModel,String SaveModelID,String SaveCerName,String SaveCerID) {

        Intent intent = new Intent();
        //給F_Keyin 工號
        intent.putExtra("F_Keyin", WorkID);
        //給model名稱
        intent.putExtra("Model", SaveModel);
        //model代號
        intent.putExtra("ModelID", SaveModelID);
        //類別代號
        intent.putExtra("CerName", SaveCerName);
        //類別名稱
        intent.putExtra("CerID", SaveCerID);
        //說明
        intent.putExtra("F_Subject", "");
        //承辦人
        intent.putExtra("RespUser", "劉慶忠");
        //承辦人ID
        intent.putExtra("RespUserID", "10003130");

        intent.setClass(getActivity(), msibook_certified_add_double_check.class);
        //開啟Activity
        startActivityForResult(intent,1);
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
