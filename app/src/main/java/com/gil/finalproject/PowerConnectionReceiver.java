package com.gil.finalproject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.BatteryManager;
import android.widget.Toast;

public class PowerConnectionReceiver extends BroadcastReceiver {

        @Override
    public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Intent.ACTION_POWER_CONNECTED)) {
                Toast.makeText(context, "The device is charging", Toast.LENGTH_SHORT).show();
            } else {
                intent.getAction().equals(Intent.ACTION_POWER_DISCONNECTED);
                Toast.makeText(context, "The device is not charging", Toast.LENGTH_SHORT).show();
            }

            /*
            int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
            boolean isCharge = status == BatteryManager.BATTERY_STATUS_CHARGING ||
                    status == BatteryManager.BATTERY_STATUS_FULL;
            if (isCharge) {
                Toast.makeText(context, "Battery charging..", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "disconnecting charge..", Toast.LENGTH_SHORT).show();
            }
            */
        }
}
