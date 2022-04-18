package com.pkprojekty.rikwo.Service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import java.util.Timer;
import java.util.TimerTask;

public class Services extends Service {

    private Handler handler = new Handler();
    public static final String KEY_TASK_DESC = "key_task_desc";

    private Timer timer = null;
    private long s;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        if(timer!=null){
            timer.cancel();
        }else {
            timer = new Timer();
        }
        Data data = new Data.Builder()
                .putString(KEY_TASK_DESC, "Hey im sending the work data")
                .build();

        OneTimeWorkRequest request = new OneTimeWorkRequest.Builder(NotificationWorker.class)
                .setInputData(data)
                .build();
        //ustawienie odpowiedniego czasu w zależności od opcji czasu wykonywania kopii
        timer.scheduleAtFixedRate(new TimeDisplayTimerTask(), 0, s*24*60*60*1000 );
    }

    //EXAMPLE
    class TimeDisplayTimerTask extends TimerTask {
        @Override
        public void run() {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    //WorkManager.getInstance().enqueue(request);
                }
            });
        }

    }
}