package id.mochgani.latihanbeacon;

import android.app.Application;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.estimote.coresdk.observation.region.beacon.BeaconRegion;
import com.estimote.coresdk.recognition.packets.Beacon;
import com.estimote.coresdk.service.BeaconManager;

import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.UUID;

/**
 * Created by mochgani on 07/12/18.
 */

public class MyApplication extends Application {

    private BeaconManager beaconManager,beaconManager2;

    @Override
    public void onCreate() {
        super.onCreate();

        beaconManager = new BeaconManager(getApplicationContext());

        beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
            @Override
            public void onServiceReady() {
                beaconManager.startMonitoring(new BeaconRegion(
                        "region pintu",
                        UUID.fromString("cb10023f-a318-3394-4199-a8730c7c1aec"),
                        2, 284));
                beaconManager.startMonitoring(new BeaconRegion(
                        "region menu",
                        UUID.fromString("cb10023f-a318-3394-4199-a8730c7c1aec"),
                        1, 284));
            }
        });

        beaconManager.setMonitoringListener(new BeaconManager.BeaconMonitoringListener() {
            @Override
            public void onEnteredRegion(BeaconRegion region, List<Beacon> beacons) {

                if(region.getMajor() == 2){
                    showNotification(
                            "LAN Resto",
                            "Selamat Datang Gani");
                } else if(region.getMajor() == 1){
                    showNotification(
                            "LAN Resto",
                            "Silahkan pilih menu Gani, ada promo menarik untuk anda");
                }

            }
            @Override
            public void onExitedRegion(BeaconRegion region) {
                // could add an "exit" notification too if you want (-:
                if(region.getMajor() == 2){
                    showNotification(
                            "LAN Resto",
                            "Selamat Jalan Gani, Kami tunggu kedatangannya kembali.");
                }
            }
        });
    }

    public void showNotification(String title, String message) {
        Intent notifyIntent = new Intent(this, MainActivity.class);
        notifyIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivities(this, 0,
                new Intent[] { notifyIntent }, PendingIntent.FLAG_UPDATE_CURRENT);
        Notification notification = new Notification.Builder(this)
                .setSmallIcon(android.R.drawable.ic_menu_compass)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .build();
        notification.defaults |= Notification.DEFAULT_SOUND;
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        Random random = new Random();
        int m = random.nextInt(9999 - 1000) + 1000;
        notificationManager.notify(m, notification);
        Log.d("Notif", m + " " + message);
    }

}