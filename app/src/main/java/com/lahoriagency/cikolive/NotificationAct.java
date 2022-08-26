package com.lahoriagency.cikolive;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.TaskStackBuilder;

@SuppressWarnings("deprecation")
public class NotificationAct extends AppCompatActivity {
    final static String CHANNEL_ID = "default_channel";
    private Context context;
    private ActivityCompat activityCompat;
    private String notificationDetails;
    private Bundle notBundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_notification);
        notBundle=getIntent().getExtras();
        if(notBundle !=null){
            context=notBundle.getParcelable("Context");
            activityCompat=notBundle.getParcelable("ActivityCompat");
            notificationDetails=notBundle.getString("Details");

        }
        sendNotification(context, activityCompat,notificationDetails);
    }
    public void sendNotification(Context context, ActivityCompat activityCompat,String notificationDetails) {

        Intent notificationIntent = new Intent(context, activityCompat.getClass());

        notificationIntent.putExtra("from_notification", true);

        // Construct a task stack.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);


        stackBuilder.addParentStack(NotificationAct.class);

        stackBuilder.addNextIntent(notificationIntent);

        PendingIntent notificationPendingIntent =
                stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);

        // Define the notification settings.
        builder.setSmallIcon(R.mipmap.ic_launcher)
                // In a real app, you may want to use a library like Volley
                // to decode the Bitmap.
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(),
                        R.mipmap.ic_launcher))
                .setColor(Color.RED)
                .setContentTitle("Location update")
                .setContentText(notificationDetails)
                .setContentIntent(notificationPendingIntent);

        builder.setAutoCancel(true);

        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = context.getString(R.string.app_name);
            // Create the channel for the notification
            NotificationChannel mChannel =
                    new NotificationChannel(CHANNEL_ID, name, NotificationManager.IMPORTANCE_DEFAULT);

            mNotificationManager.createNotificationChannel(mChannel);

            builder.setChannelId(CHANNEL_ID);
        }

        mNotificationManager.notify(0, builder.build());
    }
}