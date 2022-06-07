package com.pkprojekty.rikwo.UI;

import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.pkprojekty.rikwo.R;

public class WebViewFragment extends Fragment {

    WebView web;

    private String username, password;

    public WebViewFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_web_view, container, false);

        SharedPreferences preferences = getActivity().getPreferences(MODE_PRIVATE);
        String provider = preferences.getString("Provider","");

        web = (WebView) view.findViewById(R.id.webView);
        web.getSettings()
                .setJavaScriptEnabled(true);
        web.setWebViewClient(new WebViewClient());

        if(provider.equals("OneDrive"))
            web.loadUrl("https://onedrive.live.com/about/pl-pl/signin/");
        if(provider.equals("Dropbox"))
            web.loadUrl("https://www.dropbox.com/pl/login");
        if(provider.equals("Dysk Google"))
            web.loadUrl("https://accounts.google.com/signin/v2/identifier?service=wise&passive=1209600&osid=1&continue=https%3A%2F%2Fdrive.google.com%2Fdrive%2Fmy-drive%3Fhl%3Dpl&followup=https%3A%2F%2Fdrive.google.com%2Fdrive%2Fmy-drive%3Fhl%3Dpl&hl=pl&flowName=GlifWebSignIn&flowEntry=ServiceLogin");

        return view;
    }
}