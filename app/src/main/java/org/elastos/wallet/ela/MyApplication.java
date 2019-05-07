package org.elastos.wallet.ela;

import android.content.Context;
import android.support.multidex.MultiDexApplication;

import com.blankj.utilcode.util.Utils;

import org.elastos.wallet.BuildConfig;
import org.elastos.wallet.ela.di.component.ApplicationComponent;
import org.elastos.wallet.ela.di.component.DaggerApplicationComponent;
import org.elastos.wallet.ela.di.moudule.ApplicationModule;
import com.tencent.bugly.crashreport.CrashReport;

import io.realm.Realm;


public class MyApplication extends MultiDexApplication {

    private static MyApplication myApplication;
    public static int chainID = 0;//0正式  1testnet

    private ApplicationComponent mApplicationComponent;


    @Override
    public void onCreate() {
        super.onCreate();
        //  JPushInterface.setDebugMode(true); 	// 设置开启日志,发布时请关闭日志
        // JPushInterface.init(this);
        myApplication = this;
        initApplicationComponent();
        Utils.init(this);
        Realm.init(getApplicationContext());
        if (!BuildConfig.DEBUG) {
            CrashReport.initCrashReport(getApplicationContext(), "9c89947c00", false);
        }
        String pachageName = getPackageName();
        if (pachageName.endsWith("testnet")) {
            chainID = 1;
        }
    }


    public static Context getAppContext() {
        return myApplication.getApplicationContext();
    }


    /**
     * 初始化ApplicationComponent
     */
    private void initApplicationComponent() {
        mApplicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .build();
    }

    public ApplicationComponent getApplicationComponent() {
        return mApplicationComponent;
    }

    public static MyApplication getInstance() {
        return myApplication;
    }


}
