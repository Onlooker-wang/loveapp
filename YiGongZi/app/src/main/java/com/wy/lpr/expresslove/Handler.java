package com.wy.lpr.expresslove;

import android.app.Activity;
import android.os.Bundle;
import android.os.Message;
import android.os.PersistableBundle;
import android.util.Log;

import androidx.annotation.Nullable;

import java.lang.ref.WeakReference;

public class Handler extends Activity {

    private MyHandler mMyHandler;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        mMyHandler = new MyHandler(this);
    }

    private static class MyHandler extends android.os.Handler {

        static final int MSG_GET_DUMP = 1;

        final WeakReference<Handler> handlerWeakReference;

        MyHandler(Handler handler) {
            handlerWeakReference = new WeakReference<>(handler);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Handler handler = handlerWeakReference.get();
            if (handler != null) {
                if (msg.what == MSG_GET_DUMP) {
                    handler.test();
                }
                Message message = handler.mMyHandler.obtainMessage();
                message.what = MyHandler.MSG_GET_DUMP;
                handler.mMyHandler.sendMessageDelayed(message, 1000);
            }
        }
    }

    private void test(){

    }
}
