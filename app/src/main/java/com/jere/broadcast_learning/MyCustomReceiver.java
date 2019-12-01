package com.jere.broadcast_learning;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

/**
 * @author jere
 */
public class MyCustomReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "MyCustomReceiver Running!", Toast.LENGTH_SHORT).show();
        //将 intent 中的信息解析出来，得到发送这信息。
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            String author = bundle.getString(MainActivity.SEND_BROADCAST_AUTHOR_KEY);
            Toast.makeText(context, "send by -> " + author, Toast.LENGTH_SHORT).show();
        }
    }
}
