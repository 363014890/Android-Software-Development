package com.example.broadcastpj3;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private BroadcastReceiver mReceiver1;
    private BroadcastReceiver mReceiver2;
    Button Button1,Button2;
    private IntentFilter mFilter ;
    private static final String RES_INTENT =
            "edu.uic.cs478.sp2020.restaurant";
    private static final String ATT_INTENT =
            "edu.uic.cs478.sp2020.attraction";
    private static final String MY_PERMISSION =
            "edu.uic.cs478.sp2020.project3" ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button Button1 = (Button)findViewById(R.id.button1);
        Button Button2 = (Button)findViewById(R.id.button2);
        Button1.setOnClickListener((v)->{sendAttraction();});
        Button2.setOnClickListener((v)->{sendRestaurant();});
    }
    private void sendAttraction(){
        if (ContextCompat.checkSelfPermission(this, MY_PERMISSION)
                == PackageManager.PERMISSION_GRANTED) {
            Intent aIntent = new Intent(ATT_INTENT) ;

            sendOrderedBroadcast(aIntent,null) ;
        }
        else {
            ActivityCompat.requestPermissions(this, new String[]{MY_PERMISSION}, 0) ;
            if (ContextCompat.checkSelfPermission(this, MY_PERMISSION)
                    == PackageManager.PERMISSION_GRANTED){
                sendAttraction();
            }
        }
    }
    private void sendRestaurant(){
        if (ContextCompat.checkSelfPermission(this, MY_PERMISSION)
                == PackageManager.PERMISSION_GRANTED) {
            Intent aIntent = new Intent(RES_INTENT) ;

            sendOrderedBroadcast(aIntent,null) ;
        }
        else {
            ActivityCompat.requestPermissions(this, new String[]{MY_PERMISSION}, 0) ;
            if (ContextCompat.checkSelfPermission(this, MY_PERMISSION)
                    == PackageManager.PERMISSION_GRANTED){
                sendRestaurant();
            }
        }
    }
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // Permission granted, go ahead and display map
                    //do nothing
                }
                else {
                    Toast.makeText(this, "BUMMER: No Permission :-(", Toast.LENGTH_LONG).show() ;
        }
    }
}
