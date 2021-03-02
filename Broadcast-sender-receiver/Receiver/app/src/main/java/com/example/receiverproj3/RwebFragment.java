package com.example.receiverproj3;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import androidx.fragment.app.Fragment;


public class RwebFragment extends Fragment {
    private static final String TAG = "rwebFragment";

    private WebView mWebView;
    private int idx = -1;
    private String KEY = "key";
    private int mCurrIdx = -1;
    private int mWebArrLen;

    int getShownIndex() {
        return mCurrIdx;
    }

    // Show the Quote string at position newIndex
    void showWebAtIndex(int newIndex) {
//        mWebArrLen = res_frag.mWebArray.length;
        if (newIndex < 0 || newIndex >= mWebArrLen)
            return;
        mCurrIdx = newIndex;
//        mWebView = (WebView)getView().findViewById(R.id.webView);
//        mWebView.getSettings().setJavaScriptEnabled(true);
//        mWebView.setWebViewClient(new WebViewClient());
//        mWebView.loadUrl(res_frag.mWebArray[mCurrIdx]);
//        mWebView.setWebViewClient();

        loadUrl(res_frag.mWebArray[mCurrIdx]);
        idx = mCurrIdx;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState != null){
            idx = savedInstanceState.getInt(KEY);
        }
        // Retain this Fragment across Activity reconfigurations
//        setRetainInstance(true);

    }
    @Override
    public void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        if(idx >= 0){
            outState.putInt(KEY,idx);
        }
        else{
            outState.putInt(KEY,-1);
        }
    }
    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.res_web_fragment,container,false);
//        WebView webView = (WebView)v.findViewById(R.id.webView);
//        mWebView = (WebView)v.findViewById(R.id.webView);
//        mWebView = getView().findViewById(R.id.webView);
//        mWebView.getSettings().setJavaScriptEnabled(true);
//        mWebView.setWebViewClient(new WebViewClient());
        return inflater.inflate(R.layout.res_web_fragment,container,false);
    }
//    @Override
//    public View onCreateView (LayoutInflater inflater, ViewGroup container,
//                              Bundle savedInstanceState) {
//        return(inflater.inflate(R.layout.res_web_fragment, container, false));
//    }
    public void loadUrl(String url) {
        ((WebView)(getView().findViewById(R.id.webView))).loadUrl(url);
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);
//        mWebView = (WebView)getActivity().findViewById(R.id.webView);
        mWebArrLen = res_frag.mWebArray.length;
        if(savedInstanceState != null)
            showWebAtIndex(savedInstanceState.getInt(KEY));
    }
}
