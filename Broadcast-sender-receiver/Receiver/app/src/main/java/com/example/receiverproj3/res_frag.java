package com.example.receiverproj3;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.receiverproj3.ResFragment.ListSelectionListener;

public class res_frag extends AppCompatActivity implements ListSelectionListener{
    public static String[] mResArray;
    public static String[] mWebArray;
    private FrameLayout mResFrameLayout, mWebFrameLayout;
    private static final int MATCH_PARENT = LinearLayout.LayoutParams.MATCH_PARENT;
    private RwebFragment mWebFragment = new RwebFragment();
    private ResFragment mResFragment =null;
    static String OLD_ITEM = "old_item";
    int mShownIndex = -1;
    // UB 2/24/2019 -- Android Pie twist: Original FragmentManager class is now deprecated.
    private FragmentManager mFragmentManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_res_frag);
        mResArray = getResources().getStringArray(R.array.restaurant);
        mWebArray = getResources().getStringArray(R.array.web);
        setContentView(R.layout.activity_res_frag);
        mResFrameLayout = (FrameLayout)findViewById(R.id.res_fragment_container);
        mWebFrameLayout = (FrameLayout)findViewById(R.id.web_fragment_container);

        mFragmentManager = getSupportFragmentManager();

        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
//        mWebFragment = new RwebFragment();
        mResFragment = new ResFragment();
        mShownIndex = -1;

        fragmentTransaction.replace(
                R.id.res_fragment_container,mResFragment);
//        fragmentTransaction.add(
//                R.id.web_fragment_container,mWebFragment);

        fragmentTransaction.commit();
//        mFragmentManager.addOnBackStackChangedListener(
//                ()->{setLayout();}
//        );
        if(savedInstanceState != null){
            mShownIndex = savedInstanceState.getInt(OLD_ITEM);
        }


//        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
//            mFragmentManager.addOnBackStackChangedListener(
//                    ()->{setPortraitLayout();}
//            );
//        }
//        else{
//            mFragmentManager.addOnBackStackChangedListener(
//                    ()->{setLandscapeLayout();}
//            );
//        }


        mFragmentManager.addOnBackStackChangedListener(
                ()->{
                    if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
                        setPortraitLayout();
                    }
                    else
                        setLandscapeLayout();
                }
        );

    }
    @Override
    public void onStart(){
        super.onStart();
        if(mShownIndex >=0){
            mResFragment.setSelection(mShownIndex);
            mResFragment.getListView().setItemChecked(mShownIndex,true);
            mWebFragment = new RwebFragment();
            mWebFragment.showWebAtIndex(mShownIndex);
            if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
                mResFrameLayout.setLayoutParams(new LinearLayout.LayoutParams(0,
                        MATCH_PARENT, 0f));
                mWebFrameLayout.setLayoutParams(new LinearLayout.LayoutParams(0,
                        MATCH_PARENT, 2f));
                Toast.makeText(this, "portrait", Toast.LENGTH_LONG).show() ;
//                mWebFragment.showWebAtIndex(mShownIndex);
            }
            else{
                mResFrameLayout.setLayoutParams(new LinearLayout.LayoutParams(0,MATCH_PARENT,1f));
                mWebFrameLayout.setLayoutParams(new LinearLayout.LayoutParams(0,MATCH_PARENT,2f));
                Toast.makeText(this, "landscape", Toast.LENGTH_LONG).show() ;
//                mWebFragment.showWebAtIndex(mShownIndex);
            }

        }
    }
    @Override
    public void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        if(mShownIndex >= 0){
            outState.putInt(OLD_ITEM,mShownIndex);
        }
        else{
            outState.putInt(OLD_ITEM,-1);
        }
    }

//    @Override
//    public void onConfigurationChanged(Configuration newConfig) {
//               super.onConfigurationChanged(newConfig);
//        if(newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
//            setPortraitLayout();
////            mFragmentManager.addOnBackStackChangedListener(
////                    ()->{setPortraitLayout();});
//        }
//        else if(newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE){
//            setLandscapeLayout();
////            mFragmentManager.addOnBackStackChangedListener(
////                    ()->{setLandscapeLayout();});
//        }
//    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.top_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.switcher:
                Intent in = new Intent(res_frag.this, att_frag.class);
                startActivity(in);

            case R.id.res:
                Toast.makeText(res_frag.this, "?????? ",
                        Toast.LENGTH_LONG).show() ;
            case R.id.att:
                Intent i = new Intent(res_frag.this, att_frag.class);
                startActivity(i);
            default:
                return false;
        }
    }

    @Override
    public void onListSelection(int index) {
        if (!mWebFragment.isAdded()) {

            // Start a new FragmentTransaction
            // UB 2/24/2019 -- Now must use compatible version of FragmentTransaction
            FragmentTransaction fragmentTransaction = mFragmentManager
                    .beginTransaction();

            // Add the QuoteFragment to the layout
            fragmentTransaction.add(R.id.web_fragment_container, mWebFragment);

            // Add this FragmentTransaction to the backstack
            fragmentTransaction.addToBackStack(null);

            // Commit the FragmentTransaction
            fragmentTransaction.commit();

            // Force Android to execute the committed FragmentTransaction
            mFragmentManager.executePendingTransactions();
        }

        if (mWebFragment.getShownIndex() != index) {

            // Tell the QuoteFragment to show the quote string at position index
            mWebFragment.showWebAtIndex(index);
            mShownIndex = index;
        }
    }
    private void setPortraitLayout(){
        if(!mWebFragment.isAdded()){
            mResFrameLayout.setLayoutParams(new LinearLayout.LayoutParams(MATCH_PARENT,MATCH_PARENT));
            mWebFrameLayout.setLayoutParams(new LinearLayout.LayoutParams(0,MATCH_PARENT));
        }
        else{
            // Make the TitleLayout take 1/3 of the layout's width
            mResFrameLayout.setLayoutParams(new LinearLayout.LayoutParams(0,
                    MATCH_PARENT, 0f));
            // Make the QuoteLayout take 2/3's of the layout's width
            mWebFrameLayout.setLayoutParams(new LinearLayout.LayoutParams(0,
                    MATCH_PARENT, 2f));
        }
    }
    private void setLandscapeLayout(){
        if(mWebFragment.isAdded()){
            mResFrameLayout.setLayoutParams(new LinearLayout.LayoutParams(0,MATCH_PARENT,1f));
            mWebFrameLayout.setLayoutParams(new LinearLayout.LayoutParams(0,MATCH_PARENT,2f));
        }
        else{
            mResFrameLayout.setLayoutParams(new LinearLayout.LayoutParams(MATCH_PARENT,MATCH_PARENT));
            mWebFrameLayout.setLayoutParams(new LinearLayout.LayoutParams(0,MATCH_PARENT));
        }
    }
    private void setLayout() {

        // Determine whether the QuoteFragment has been added
        if (!mWebFragment.isAdded()) {

            // Make the TitleFragment occupy the entire layout
            mResFrameLayout.setLayoutParams(new LinearLayout.LayoutParams(
                    MATCH_PARENT, MATCH_PARENT));
            mWebFrameLayout.setLayoutParams(new LinearLayout.LayoutParams(0,
                    MATCH_PARENT));
        } else {

            // Make the TitleLayout take 1/3 of the layout's width
            mResFrameLayout.setLayoutParams(new LinearLayout.LayoutParams(0,
                    MATCH_PARENT, 0f));

            // Make the QuoteLayout take 2/3's of the layout's width
            mWebFrameLayout.setLayoutParams(new LinearLayout.LayoutParams(0,
                    MATCH_PARENT, 2f));
        }
    }
}
