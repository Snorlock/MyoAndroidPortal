package snorreedwin.no.myofirebase;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import snorreedwin.no.myofirebase.utils.Obscurifier;

public class FirebaseLoginWebViewActivity extends Activity{

    private final String firebaseLoginUrl = "https://www.firebase.com/login/";
    private final String firebaseLoginSuccessUrl = "https://www.firebase.com/account/#/";
    private final String firebaseAdminTokenKey = "adminToken";
    WebView myWebView;

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.firebase_webview_activity);
        myWebView = (WebView) findViewById(R.id.webview);
        myWebView.getSettings().setJavaScriptEnabled(true);
        myWebView.loadUrl(firebaseLoginUrl);

        myWebView.setWebViewClient(new WebViewClient(){
            public void onPageFinished(WebView view, String url) {
                Log.d("Webview", url);
                Log.d("Webview", view.getTitle());
                if(url.equals(firebaseLoginSuccessUrl)) {
                    saveAdminToken(CookieManager.getInstance().getCookie("https://www.firebase.com/"));
                    setResult(1);
                    finish();
                }
            }
        });
    }

    private void saveAdminToken(String cookieString) {
        String [] cookies = cookieString.split(";");
        for(String cookie : cookies) {
            if(cookie.contains(firebaseAdminTokenKey)) {
                Obscurifier obscurifier = App.getObscurifier();
                obscurifier.cryptAndSaveString(cookie.trim());
            }
        }
    }


}
