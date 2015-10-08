package com.danielrozenberg.android.unpoweredbychrome;

import android.app.Activity;
import android.content.Intent;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class Module implements IXposedHookLoadPackage {

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam loadPackageParam) throws Throwable {
        if (!loadPackageParam.packageName.equals("com.android.chrome")) {
            return;
        }

        XposedHelpers.findAndHookMethod("org.chromium.chrome.browser.customtabs.CustomTabActivity", loadPackageParam.classLoader, "onStart", new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                Activity activity = (Activity) param.thisObject;
                Intent customTabIntent = activity.getIntent();

                Intent overridingIntent = new Intent(customTabIntent.getAction(), customTabIntent.getData());
                activity.startActivity(overridingIntent);
                activity.finish();
            }
        });
    }

}
