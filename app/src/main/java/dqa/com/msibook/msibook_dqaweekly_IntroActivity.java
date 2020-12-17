package dqa.com.msibook;

import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.github.paolorotolo.appintro.AppIntro;

public class msibook_dqaweekly_IntroActivity extends AppIntro {

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addSlide(msibook_dqaweekly_SampleSlide.newInstance(R.layout.msibook_dqaweekly_pager1));
        addSlide(msibook_dqaweekly_SampleSlide.newInstance(R.layout.msibook_dqaweekly_pager2));
        addSlide(msibook_dqaweekly_SampleSlide.newInstance(R.layout.msibook_dqaweekly_pager3));
        addSlide(msibook_dqaweekly_SampleSlide.newInstance(R.layout.msibook_dqaweekly_pager4));
        addSlide(msibook_dqaweekly_SampleSlide.newInstance(R.layout.msibook_dqaweekly_pager5));
        addSlide(msibook_dqaweekly_SampleSlide.newInstance(R.layout.msibook_dqaweekly_pager6));
        addSlide(msibook_dqaweekly_SampleSlide.newInstance(R.layout.msibook_dqaweekly_pager7));

    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        finish();
        // Do something when users tap on Skip button.
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        finish();
        // Do something when users tap on Done button.
    }

    @Override
    public void onSlideChanged(@Nullable Fragment oldFragment, @Nullable Fragment newFragment) {
        super.onSlideChanged(oldFragment, newFragment);
        // Do something when the slide changes.
    }
}
