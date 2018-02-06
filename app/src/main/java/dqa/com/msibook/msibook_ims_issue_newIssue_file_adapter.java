package dqa.com.msibook;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.List;

/**
 * Created by androids on 2016/10/21.
 */
public class msibook_ims_issue_newIssue_file_adapter extends BaseAdapter {

    private LayoutInflater mLayInf;

    private List<msibook_ims_issue_newIssue_file_item> NewIssueFile_List;

    private Context mContext;

    public msibook_ims_issue_newIssue_file_adapter(Context context, List<msibook_ims_issue_newIssue_file_item> NewIssueFile_List) {
        mLayInf = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        mContext = context;
        this.NewIssueFile_List = NewIssueFile_List;
    }

    @Override
    public int getCount() {
        return NewIssueFile_List.size();
    }

    @Override
    public Object getItem(int position) {
        return NewIssueFile_List.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public List<msibook_ims_issue_newIssue_file_item> getAllitem() {
        return NewIssueFile_List;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = mLayInf.inflate(R.layout.activity_msibook_ims_newissuefile_item, parent, false);

        ImageView Img_Issue_File = (ImageView) v.findViewById(R.id.Img_Issue_File);

        final String VideoPath = NewIssueFile_List.get(position).GetVideoPath();

        switch (NewIssueFile_List.get(position).GetFileType()) {
            case Image:


                Img_Issue_File.setImageBitmap(NewIssueFile_List.get(position).GetImageBitMap());

                break;
            case Voice:

                v = mLayInf.inflate(R.layout.activity_msibook_ims_newissuefile_voice, parent, false);

                msibook_ims_issue_voice_play IssueVoicePlay = (msibook_ims_issue_voice_play) v.findViewById(R.id.IssueVoicePlay);

                IssueVoicePlay.fileName = NewIssueFile_List.get(position).GetVoicePath();

                IssueVoicePlay.mcontext = mContext;

                IssueVoicePlay.SetVoicePath(NewIssueFile_List.get(position).GetVoicePath());

                break;
            case Video:

                v = mLayInf.inflate(R.layout.activity_msibook_ims_newissuefilevideo_item, parent, false);

//                VideoView Vdo_Issue_File = (VideoView) v.findViewById(R.id.Vdo_Issue_File);
//
//                Uri uri = Uri.parse(NewIssueFile_List.get(position).GetVideoPath());
//
//                Vdo_Issue_File.setMediaController(new MediaController(parent.getContext()));
//                Vdo_Issue_File.setVideoURI(uri);
//                Vdo_Issue_File.start();
//                Vdo_Issue_File.requestFocus();
//
//
//                Vdo_Issue_File.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View arg0) {
//
//                        VideoView VideoView = (VideoView) arg0;
//
//                        VideoView.start();
//
//                        System.out.println("Start Video");
//
//                    }
//                });

                msibook_ims_issue_video_play mIssueVideoPlay = (msibook_ims_issue_video_play) v.findViewById(R.id.IssueVideoPlay);

                mIssueVideoPlay.setVisibility(View.VISIBLE);

                mIssueVideoPlay.SetVideoPath(VideoPath);
                mIssueVideoPlay.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {

                        Intent intent = new Intent(mContext, msibook_ims_issue_play_video.class);

                        Bundle intentBundle = new Bundle();

                        intentBundle.putString("VideoUrl", VideoPath);

                        intent.putExtras(intentBundle);

                        mContext.startActivity(intent);

                    }
                });


                break;
        }


        return v;
    }

    public void addItem(msibook_ims_issue_newIssue_file_item NewIssueFile_Item) {
        NewIssueFile_List.add(NewIssueFile_Item);
    }

    public void removeItem(int index) {
        NewIssueFile_List.remove(index);
    }
}
