package dqa.com.msibook;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import java.util.List;

/**
 * Created by androids on 2016/10/21.
 */
public class msibook_ims_issue_file_adapter extends RecyclerView.Adapter<msibook_ims_issue_file_adapter.ViewHolder> {

    private Context mContext;
    private LayoutInflater mInflater;
    private List<msibook_ims_issue_file_item> mDatas;


    public msibook_ims_issue_file_adapter(Context context, List<msibook_ims_issue_file_item> datats) {
        mInflater = LayoutInflater.from(context);
        mContext = context;
        mDatas = datats;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView mImg;
        msibook_ims_issue_video_play mIssueVideoPlay;
        msibook_ims_issue_voice_play mIssueVoicePlay;

        public ViewHolder(View LayoutView) {
            super(LayoutView);

            mImg = (ImageView) LayoutView.findViewById(R.id.Img_IssueFile_Image);

            mIssueVideoPlay = (msibook_ims_issue_video_play) LayoutView.findViewById(R.id.IssueVideoPlay);

            mIssueVoicePlay = (msibook_ims_issue_voice_play) LayoutView.findViewById(R.id.IssueVoicePlay);

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
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = mInflater.inflate(R.layout.activity_msibook_ims_issue_file_item,
                viewGroup, false);

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    /**
     * 设置值
     */
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {

        if (mDatas.get(i).GetImage().toLowerCase().contains(".mp4") || mDatas.get(i).GetImage().toLowerCase().contains(".mov")) {

            final String VideoPath = mDatas.get(i).GetImage();

            viewHolder.mIssueVideoPlay.setVisibility(View.VISIBLE);
            viewHolder.mIssueVoicePlay.setVisibility(View.GONE);
            viewHolder.mImg.setVisibility(View.GONE);

            viewHolder.mIssueVideoPlay.SetVideoPath(mDatas.get(i).GetImage());
            viewHolder.mIssueVideoPlay.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    Intent intent = new Intent(mContext, msibook_ims_issue_play_video.class);

                    Bundle intentBundle = new Bundle();

                    intentBundle.putString("VideoUrl", VideoPath);

                    intent.putExtras(intentBundle);

                    mContext.startActivity(intent);

                }
            });
        } else if (mDatas.get(i).GetImage().toLowerCase().contains(".3gp")) {
            viewHolder.mIssueVideoPlay.setVisibility(View.GONE);
            viewHolder.mImg.setVisibility(View.GONE);
            viewHolder.mIssueVoicePlay.setVisibility(View.VISIBLE);

            viewHolder.mIssueVoicePlay.fileName = mDatas.get(i).GetImage();

            viewHolder.mIssueVoicePlay.mcontext = mContext;

            viewHolder.mIssueVoicePlay.SetVoicePath(mDatas.get(i).GetImage());
        } else {


            String _ImagePath = mDatas.get(i).GetImage();

            if (_ImagePath.toLowerCase().contains("img")) {
                _ImagePath = GetServiceData.getUrlFromImgTag(_ImagePath);
            }

            final String Imagepath = _ImagePath;


            viewHolder.mIssueVideoPlay.setVisibility(View.GONE);
            viewHolder.mIssueVoicePlay.setVisibility(View.GONE);
            viewHolder.mImg.setVisibility(View.VISIBLE);


            Glide
                    .with(mContext)
                    .load(mDatas.get(i).GetImage())
                    .asBitmap()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.mipmap.progress_image)
                    .into(new SimpleTarget<Bitmap>(100, 100) {
                        @Override
                        public void onResourceReady(Bitmap resource, GlideAnimation glideAnimation) {

                            viewHolder.mImg.setImageBitmap(resource);

                        }

                        @Override
                        public void onLoadFailed(Exception e, Drawable errorDrawable) {
//                            Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

            viewHolder.mImg.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    msibook_ims_image_zoom.ImagePath = Imagepath;

                    Intent intent = new Intent(mContext, msibook_ims_image_zoom.class);

                    mContext.startActivity(intent);

                }
            });
        }


    }

}

