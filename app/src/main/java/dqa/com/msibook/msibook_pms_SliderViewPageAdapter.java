package dqa.com.msibook;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import java.util.List;

public class msibook_pms_SliderViewPageAdapter extends PagerAdapter{

    private ProgressDialog progressBar;
    private Context context;
    private LayoutInflater layoutInflater;

    //private Integer [] images = {R.drawable.dqaweekly_guide_1,R.drawable.dqaweekly_guide_2,R.drawable.dqaweekly_guide_3};

    private String[] Array_ModelPic;//服務圖片陣列
    private String ImagePath;

    public msibook_pms_SliderViewPageAdapter(Context context,String[] Array_ModelPic) {
        this.context = context;
        this.Array_ModelPic = Array_ModelPic;
    }

    @Override
    public int getCount() {
        return Array_ModelPic.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.msibook_pms_custom_layout,null);

        progressBar = new ProgressDialog(context);
        progressBar.setCancelable(true);
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.setMessage("資料載入中");

        final ImageView imageView = (ImageView) view.findViewById(R.id.imageView);
        //imageView.setImageResource(images[position]);
        Log.w("Array_ModelPic長度",String.valueOf(Array_ModelPic.length));
        Log.w("目前Position",String.valueOf(position));

        //顯示  讀取等待時間Bar
        progressBar.show();

            if(Array_ModelPic[position].contains("//172.16.111.114")){
                ImagePath = Array_ModelPic[position].replace("//172.16.111.114/File","http://wtsc.msi.com.tw/IMS/FileServer");
            }else{
                ImagePath = Array_ModelPic[position].replace("//172.18.16.24/File","http://wtsc.msi.com.tw/IMS/FileServers");
            }

            Log.w("IMagePath",ImagePath);
            Glide
                    .with(context)
                    .load(ImagePath)
                    .asBitmap()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.mipmap.progress_image)
                    .into(new SimpleTarget<Bitmap>(100, 100) {
                        @Override
                        public void onResourceReady(Bitmap resource, GlideAnimation glideAnimation) {

                            imageView.setImageBitmap(resource);

                            imageView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

//                                    Intent intent = new Intent(context, msibook_pms_big_image.class);
//
//                                    intent.putExtra("ImagePath", String.valueOf(ImagePath));// 點擊Go to放大圖Activity
//
//                                    ((msibook_pms_spec) context).startActivityForResult(intent,1);

                                }
                            });
                        }

                        @Override
                        public void onLoadFailed(Exception e, Drawable errorDrawable) {
//                            Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_SHORT).show();
                            //顯示  讀取等待時間Bar
                            //progressBar.dismiss();
                        }
                    });

        //顯示  讀取等待時間Bar
        progressBar.dismiss();

        ViewPager vp = (ViewPager) container;
        vp.addView(view,0);

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        ViewPager vp = (ViewPager) container;
        View view = (View) object;
        vp.removeView(view);
    }


}
