package com.example.lyl.smartprogressbar;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;

import com.example.lyl.smartprogressbar.view.HorizontalProgressbarWithProgress;

public class MainActivity extends AppCompatActivity {

    private HorizontalProgressbarWithProgress progressbar1;

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case 10:
                    progressbar1.setProgress((Integer) msg.obj);
                    break;
            }
            return false;
        }
    });;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        initView();


        new Thread(new Runnable() {
            @Override
            public void run() {


                for (int i = 0; i < 101; i++) {
                    try {
                        Thread.sleep(300);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Message message = handler.obtainMessage();
                    message.what = 10;
                    message.obj = i;
                    handler.sendMessage(message);
                }
            }
        }).start();
    }

    private void initView() {
        progressbar1 = (HorizontalProgressbarWithProgress) findViewById(R.id.progressbar1);
    }


}
