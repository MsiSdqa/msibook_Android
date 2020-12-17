package dqa.com.msibook;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.webkit.WebView;

import com.github.chrisbanes.photoview.PhotoView;

public class msibook_about_msi extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msibook_about_msi);

        PhotoView photoView = (PhotoView) findViewById(R.id.photo_view);
        photoView.setImageResource(R.mipmap.founder);

        AlignedTextView atv = (AlignedTextView) findViewById(R.id.atv);
        atv.setText("微星自1986年創立以來，一直以”產品卓越、品質精良、服務完美、客戶滿意”四大原則設計、製造產品，並以製造、生產消費者滿意的產品為我們努力的目標，經過二十多年的努力，我們已成為全球前3大板卡、以及一線主機板製造商，優良的品牌形象早已深植消費者心中，並深受市場的肯定。");
        //atv.setText("时间改变着一切，一切改变着我们。原先看不惯的，如今习惯了；曾经很想要的，现在不需要了；开始很执着的，后来很洒脱了。失去产生了痛苦，也铸就了坚强；经历付出了代价，也锤炼了成长。没流泪，不代表没眼泪；无所谓，不代表无所累。当你知道什么是欲哭无泪，欲诉无语，欲笑无声的时候，你成熟了。累了没人疼，你要学会休息；哭了没人哄，你要知道自立；痛了没人懂，你要扛起压力抱怨的话不要说。有些委屈，是说不出来的。即使有人问，也不知从何说起；就算有人疼，也代替不了自己。嘴里有话却说不出，沉默代表了一切；心中有疼却表不明，泪水倾诉着所有。一些经历，只有自己感受；一些心情，只有自己懂得。说不出的委屈，才最委屈；心里的疼痛，才最疼痛！总是为别人着想，却要独自去疗伤；一直在嘴上逞强，心却没那么坚强。");


    }
}
