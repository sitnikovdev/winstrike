package ru.prsolution.winstrike.ui.common;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;

import ru.prsolution.winstrike.R;

/*
 * Created by oleg on 22.03.2018.
 */

public class HtmlViewer extends AppCompatActivity {
    private WebView wv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String response;
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                response= null;
            } else {
                response= extras.getString("response");
            }
        } else {
            response= (String) savedInstanceState.getSerializable("response");
        }

        setContentView(R.layout.ac_web);
        wv = findViewById(R.id.WebView01);
        wv.loadData(response,"text/html","UTF-8");
    }

}

