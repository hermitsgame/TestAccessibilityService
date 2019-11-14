package cn.com.startai.testaccessibilityservice;

import android.app.Application;

import cn.com.startai.accessibility.StartAIAccessibilityService;
import cn.com.startai.common.CommonSDKInterface;
import cn.com.startai.common.utils.CShellUtils;
import cn.com.startai.common.utils.CThreadPoolUtils;
import cn.com.startai.common.utils.TAndL;

/**
 * Created by Robin on 2019/10/31.
 * 419109715@qq.com 彬影
 */
public class MyApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        CommonSDKInterface.getInstance().init(this);
        TAndL.L("MyApp.onCreate");


        StartAIAccessibilityService.initAccessbilityService(this);

    }
}
