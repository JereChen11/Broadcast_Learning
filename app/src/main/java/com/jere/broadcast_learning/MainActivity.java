package com.jere.broadcast_learning;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

/**
 * @author jere
 */
public class MainActivity extends AppCompatActivity {
    private MyCustomReceiver myCustomReceiver;
    public static final String MY_CUSTOM_RECEIVER_NAME = "JERE_TEST_CUSTOM_RECEIVER";
    public static final String SEND_BROADCAST_AUTHOR_KEY = "SEND_BROADCAST_AUTHOR";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button sendBroadcastBtn = findViewById(R.id.send_broadcast_btn);
        sendBroadcastBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //发送广播
                Bundle bundle = new Bundle();
                bundle.putString(SEND_BROADCAST_AUTHOR_KEY, "Jere");
                Intent intent = new Intent();
                intent.setAction(MY_CUSTOM_RECEIVER_NAME);
                intent.putExtras(bundle);
                sendBroadcast(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        //注册广播
        myCustomReceiver = new MyCustomReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(MY_CUSTOM_RECEIVER_NAME);
        registerReceiver(myCustomReceiver, intentFilter);
    }

    @Override
    protected void onPause() {
        //注销广播
        unregisterReceiver(myCustomReceiver);
        super.onPause();

    }
}
