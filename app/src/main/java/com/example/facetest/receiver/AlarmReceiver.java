package com.example.facetest.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.robotemi.sdk.Robot;
import com.robotemi.sdk.TtsRequest;

public class AlarmReceiver extends BroadcastReceiver {

    Robot robot=Robot.getInstance();
    @Override
    public void onReceive(Context context, Intent intent) {
        robot.speak(TtsRequest.create("闹钟响了",false));
        Toast.makeText(context, "闹钟响了", Toast.LENGTH_SHORT).show();
    }

}
