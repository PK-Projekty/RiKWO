package com.pkprojekty.rikwo.Service;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.pkprojekty.rikwo.MainActivity;

public class NotificationWorker extends Worker {

    public  static final String KEY_TASK_OUTPUT = "key_task_output";

    public NotificationWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        Data data = getInputData();
        MainActivity activity = new MainActivity();

        String desc = data.getString(MainActivity.KEY_TASK_DESC);
        displayNotification("Wykonano kopie zapasową", desc);

        Data data1 = new Data.Builder()
                .putString(KEY_TASK_OUTPUT, "Task Finished Successfully")
                .build();
        activity.EmailButton();

        return Result.success();
    }

    private void displayNotification(String task, String desc) {

        NotificationManager manager =
                (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("simplifiedcoding", "simplifiedcoding", NotificationManager.IMPORTANCE_DEFAULT);
            manager.createNotificationChannel(channel);
        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), "simplifiedcoding")
                .setContentTitle(task)
                .setContentText(desc);

        manager.notify(1, builder.build());

    }


}
