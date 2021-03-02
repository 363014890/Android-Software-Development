package com.example.receiverproj3;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private IntentFilter mfilter1;
    private IntentFilter mfilter2;
    private BroadcastReceiver re_att;
    private BroadcastReceiver re_res;
    private static final String RES_INTENT =
            "edu.uic.cs478.sp2020.restaurant";
    private static final String ATT_INTENT =
            "edu.uic.cs478.sp2020.attraction";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mfilter1 = new IntentFilter(ATT_INTENT);
        mfilter2 = new IntentFilter(RES_INTENT);
        re_att = new ReceiverAtt();
        re_res = new ReceiverRes();
        registerReceiver(re_att,mfilter1);
        registerReceiver(re_res,mfilter2);
    }
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(re_att);
        unregisterReceiver(re_res);
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
                Toast.makeText(MainActivity.this, "?????? ",
                        Toast.LENGTH_LONG).show() ;
            case R.id.res:
                Intent i = new Intent(MainActivity.this, res_frag.class);
                startActivity(i);
            case R.id.att:
                Intent in = new Intent(MainActivity.this, att_frag.class);
                startActivity(in);
            default:
                return false;
        }
    }
}
