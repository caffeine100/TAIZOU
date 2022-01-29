package com.websarva.wings.android.production;

import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.navigation.NavigationBarView;

public class BottomSheetFragment extends BottomSheetDialogFragment {
    WebView webView;
    String url;

    NestedScrollView scrollView;

    public BottomSheetFragment() {
        url="https://www.google.com/?hl=ja";
    }
    public BottomSheetFragment(String url){
        this.url=url;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_dialog, container, false);

        scrollView = view.findViewById(R.id.nested_scroll);

        webView = view.findViewById(R.id.webView);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl(url);

        webView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
                    webView.goBack();
                    return true;
                }
                return false;
            }

        });

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        BottomNavigationView bottomNavigationView = view.findViewById(R.id.bt_youtube);
        bottomNavigationView.setSelectedItemId(R.id.home);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch (id) {
                    case R.id.home:
                        webView.loadUrl("https://www.google.com/?hl=ja");
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                scrollView.scrollTo(0, 0);
                            }
                        }, 500);
                        return true;
                    case R.id.youtube:
                        webView.loadUrl("https://www.youtube.com");
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                scrollView.scrollTo(0, 0);
                            }
                        }, 500);
                        return true;
                    case R.id.add:
                        String url = webView.getUrl();
                        BottomSheetDialog dialog=new BottomSheetDialog(url);
                        dialog.show(getActivity().getSupportFragmentManager(),"TAG");
                        return true;
                }
                return false;
            }
        });
        return view;
    }
}
