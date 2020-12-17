package dqa.com.msibook;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class msibook_main_setting_foodmenu extends AppCompatActivity {

    private WebView webview_foodmenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msibook_main_setting_foodmenu);

        webview_foodmenu = (WebView) findViewById(R.id.webview_foodmenu);
        webview_foodmenu.getSettings().setJavaScriptEnabled(true);
        webview_foodmenu.requestFocus();
        webview_foodmenu.setWebViewClient(new MyWebViewClient());
        webview_foodmenu.loadUrl("https://msimenu.blogspot.com/2018/09/blog-post.html");

    }

    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return super.shouldOverrideUrlLoading(view, url);

        }
    }
}
