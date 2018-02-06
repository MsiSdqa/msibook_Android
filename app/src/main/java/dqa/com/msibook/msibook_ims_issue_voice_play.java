package dqa.com.msibook;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Environment;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;

public class msibook_ims_issue_voice_play extends RelativeLayout {


    LayoutInflater mInflater;

    public String fileName;

    public Context mcontext;

    public msibook_ims_issue_voice_play(Context context) {


        super(context);
        //mcontext = context;
        mInflater = LayoutInflater.from(context);
        init();

    }

    public msibook_ims_issue_voice_play(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        //mcontext = context;
        mInflater = LayoutInflater.from(context);
        init();
    }

    public msibook_ims_issue_voice_play(Context context, AttributeSet attrs) {
        super(context, attrs);
        //mcontext = context;
        mInflater = LayoutInflater.from(context);
        init();
    }

    public void SetVoicePath(String VoicePath) {

        fileName = VoicePath;


        try {
            String url = fileName; // your URL here
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setDataSource(url);
            mediaPlayer.prepare(); // might take long! (for buffering, etc)


            View v = mInflater.inflate(R.layout.activity_msibook_ims_issue_voice_play, this, true);
            TextView txt_Voice = (TextView) v.findViewById(R.id.txt_Voice);
            txt_Voice.setText(String.valueOf(mediaPlayer.getDuration() / 1000) + "Second");
            final ImageView btn_Play = (ImageView) v.findViewById(R.id.btn_Play);
            mediaPlayer.setOnCompletionListener(new OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    //mp.stop();
                    btn_Play.setBackgroundResource(R.mipmap.newissue_btn_play);
                    Log.w("Stop", "AutoStop");
                }
            });

        } catch (IOException Ex) {

        }
    }

    public void init() {

        View v = mInflater.inflate(R.layout.activity_msibook_ims_issue_voice_play, this, true);

        final ImageView btn_Play = (ImageView) v.findViewById(R.id.btn_Play);

        btn_Play.setBackgroundResource(R.mipmap.newissue_btn_play);

        btn_Play.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!mediaPlayer.isPlaying()) {
                    clickPlay();

                    //btn_Play.setImageResource();

                    btn_Play.setBackgroundResource(R.mipmap.newissue_btn_stop);


                } else {
                    clickStop();

                    //btn_Play.setImageResource(R.mipmap.newissue_btn_play);

                    btn_Play.setBackgroundResource(R.mipmap.newissue_btn_play);


                }
            }
        });
    }

    private MediaPlayer mediaPlayer;

    private File configFileName(String prefix, String extension) {

        String fileName = FileUtil.getUniqueFileName();

        File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);

        return new File(dir, prefix + fileName + extension);

    }

    public void clickPlay() {

        mediaPlayer.start();

    }

    public void clickPause() {
        // 暫停播放
        mediaPlayer.pause();
    }

    public void clickStop() {
        // 停止播放
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
        }

        // 回到開始的位置
        //mediaPlayer.seekTo(0);
    }

}