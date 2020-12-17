package dqa.com.msibook;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.github.chrisbanes.photoview.PhotoView;

public class msibook_pms_big_image extends AppCompatActivity {

    private String ImagePath;
    private String SetImagePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msibook_pms_big_image);

        String getImagePath = getIntent().getStringExtra("ImagePath");//抓第一頁   // 21751
        ImagePath = getImagePath;
        Log.w("getImagePath",ImagePath);

        final PhotoView photoView = (PhotoView) findViewById(R.id.photo_view);

        if(ImagePath.contains("//172.16.111.114")){
            SetImagePath = ImagePath.replace("//172.16.111.114/File","http://wtsc.msi.com.tw/IMS/FileServer");
        }else{
            SetImagePath = ImagePath.replace("//172.18.16.24/File","http://wtsc.msi.com.tw/IMS/FileServers");
        }

        Log.w("IMagePath",ImagePath);
        Glide
                .with(this)
                .load(SetImagePath)
                .asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.mipmap.progress_image)
                .into(new SimpleTarget<Bitmap>(100, 100) {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation glideAnimation) {

                        photoView.setImageBitmap(resource);

                    }

                    @Override
                    public void onLoadFailed(Exception e, Drawable errorDrawable) {
//                            Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_SHORT).show();
                        //顯示  讀取等待時間Bar
                        //progressBar.dismiss();
                    }
                });

        //photoView.setImageResource(R.mipmap.pms_img_pms_no_pic);

    }
}
