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
import com.example.receiverproj3.AttFragment.ListSelectionListener;
public class att_frag extends AppCompatActivity implements ListSelectionListener {
    public static String[] mAttArray;
    public static String[] mWebArray;
    private FrameLayout mAttFrameLayout, mWebFrameLayout;
    private static final int MATCH_PARENT = LinearLayout.LayoutParams.MATCH_PARENT;
    private AwebFragment mWebFragment = new AwebFragment();
    private AttFragment mAttFragment =null;
    static String OLD_ITEM = "old_item";
    int mShownIndex = -1;
    // UB 2/24/2019 -- Android Pie twist: Original FragmentManager class is now deprecated.
    private FragmentManager mFragmentManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_att_frag);
        mAttArray = getResources().getStringArray(R.array.attraction);
        mWebArray = getResources().getStringArray(R.array._web);
        setContentView(R.layout.activity_att_frag);
        mAttFrameLayout = (FrameLayout)findViewById(R.id.att_fragment_container);
        mWebFrameLayout = (FrameLayout)findViewById(R.id._web_fragment_container);

        mFragmentManager = getSupportFragmentManager();

        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        mAttFragment = new AttFragment();
        mShownIndex = -1;

        fragmentTransaction.replace(
                R.id.att_fragment_container,mAttFragment);


        fragmentTransaction.commit();

        if(savedInstanceState != null){
            mShownIndex = savedInstanceState.getInt(OLD_ITEM);
        }




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
            mAttFragment.setSelection(mShownIndex);
            mAttFragment.getListView().setItemChecked(mShownIndex,true);
            mWebFragment = new AwebFragment();
            mWebFragment.showWebAtIndex(mShownIndex);
            if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
                mAttFrameLayout.setLayoutParams(new LinearLayout.LayoutParams(0,
                        MATCH_PARENT, 0f));
                mWebFrameLayout.setLayoutParams(new LinearLayout.LayoutParams(0,
                        MATCH_PARENT, 2f));
                Toast.makeText(this, "portrait", Toast.LENGTH_LONG).show() ;

            }
            else{
                mAttFrameLayout.setLayoutParams(new LinearLayout.LayoutParams(0,MATCH_PARENT,1f));
                mWebFrameLayout.setLayoutParams(new LinearLayout.LayoutParams(0,MATCH_PARENT,2f));
                Toast.makeText(this, "landscape", Toast.LENGTH_LONG).show() ;

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
                Intent in = new Intent(att_frag.this, res_frag.class);
                startActivity(in);

            case R.id.att:
                Toast.makeText(att_frag.this, "?????? ",
                        Toast.LENGTH_LONG).show() ;
            case R.id.res:
                Intent i = new Intent(att_frag.this, res_frag.class);
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
            fragmentTransaction.add(R.id._web_fragment_container, mWebFragment);

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
            mAttFrameLayout.setLayoutParams(new LinearLayout.LayoutParams(MATCH_PARENT,MATCH_PARENT));
            mWebFrameLayout.setLayoutParams(new LinearLayout.LayoutParams(0,MATCH_PARENT));
        }
        else{
            // Make the TitleLayout take 1/3 of the layout's width
            mAttFrameLayout.setLayoutParams(new LinearLayout.LayoutParams(0,
                    MATCH_PARENT, 0f));
            // Make the QuoteLayout take 2/3's of the layout's width
            mWebFrameLayout.setLayoutParams(new LinearLayout.LayoutParams(0,
                    MATCH_PARENT, 2f));
        }
    }
    private void setLandscapeLayout(){
        if(mWebFragment.isAdded()){
            mAttFrameLayout.setLayoutParams(new LinearLayout.LayoutParams(0,MATCH_PARENT,1f));
            mWebFrameLayout.setLayoutParams(new LinearLayout.LayoutParams(0,MATCH_PARENT,2f));
        }
        else{
            mAttFrameLayout.setLayoutParams(new LinearLayout.LayoutParams(MATCH_PARENT,MATCH_PARENT));
            mWebFrameLayout.setLayoutParams(new LinearLayout.LayoutParams(0,MATCH_PARENT));
        }
    }
}
