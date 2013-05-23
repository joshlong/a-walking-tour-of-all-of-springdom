package com.joshlong.spring.walkingtour.android.view.activities;


import android.os.Bundle;
import android.view.View;
import android.webkit.*;
import android.widget.*;
import com.joshlong.spring.walkingtour.android.R;
import com.joshlong.spring.walkingtour.android.view.activities.support.AbstractActivity;

/**
 * handles the OAuth authentication with the backend service and it
 * stores the OAuth access token locally.
 *
 * @author Josh Long
 */
public class LoginActivity extends AbstractActivity {


    EditText username, password;
    Button button;
    WebView webView;
    WebChromeClient webChromeClient;
    LoginActivity activity = this;

    void onSaveButtonClicked() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.webView = new WebView(this);
        setContentView(this.webView);

        webView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                activity.setTitle(R.string.fetching);
                activity.setProgress(progress * 100);
                if (progress == 100) {
                    activity.setTitle(R.string.app_name);
                }
            }
        });

        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);

        button = (Button) findViewById(R.id.save_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSaveButtonClicked();
            }
        });


    }
}
