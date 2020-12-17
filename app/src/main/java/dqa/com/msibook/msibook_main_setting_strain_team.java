package dqa.com.msibook;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class msibook_main_setting_strain_team extends AppCompatActivity {

    private WebView webview_strain_team;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msibook_main_setting_strain_team);

        webview_strain_team = (WebView) findViewById(R.id.webview_strain_team);
        webview_strain_team.getSettings().setJavaScriptEnabled(true);
        webview_strain_team.requestFocus();
        webview_strain_team.setWebViewClient(new MyWebViewClient());
        webview_strain_team.loadUrl("https://emergencyrteam.blogspot.com/2018/09/blog-post.html");

    }

    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return super.shouldOverrideUrlLoading(view, url);

        }
    }

}
