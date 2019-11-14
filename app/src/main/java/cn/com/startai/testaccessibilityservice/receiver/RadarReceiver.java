package cn.com.startai.testaccessibilityservice.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import cn.com.startai.accessibility.StartAIAccessibilityService;

/**
 * Created by Robin on 2019/8/26.
 * 419109715@qq.com 彬影
 */
public class RadarReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, Intent intent) {

        String action = intent.getAction();
        if (Intent.ACTION_BOOT_COMPLETED.equals(action)) {


//
            StartAIAccessibilityService.initAccessbilityService(context.getApplicationContext());

        }
    }


}
