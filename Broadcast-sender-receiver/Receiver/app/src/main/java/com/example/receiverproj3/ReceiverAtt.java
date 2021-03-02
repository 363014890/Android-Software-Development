package com.example.receiverproj3;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class ReceiverAtt extends BroadcastReceiver {
    Intent aIntent;
    @Override

    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "att received ",
                Toast.LENGTH_LONG).show() ;
        aIntent = new Intent(context,att_frag.class);
        context.startActivity(aIntent);
    }

}
