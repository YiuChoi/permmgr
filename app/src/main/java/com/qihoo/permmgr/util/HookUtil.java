package com.qihoo.permmgr.util;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class HookUtil implements IXposedHookLoadPackage {
    private int diceCount = 0;
    private int morraNum = 0; // 0-剪刀 1-石头 2-布
    private Context mContext;

    @Override
    public void handleLoadPackage(final XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {

        if (!lpparam.packageName.equals("com.tencent.mm"))
            return;

        hookAndGetContext("com.tencent.mm.ui.MMFragmentActivity", lpparam, "onCreate");

        findAndHookMethod("com.tencent.mm.sdk.platformtools.bb", lpparam, "pu");
    }

    private void hookAndGetContext(String className, final XC_LoadPackage.LoadPackageParam lpparam, String methodName) {
        XposedHelpers.findAndHookMethod(className, lpparam.classLoader, methodName, Bundle.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                // Hook函数之前执行的代码
                if (mContext == null) {
                    mContext = (Context) param.thisObject;
                    Log.i("mm", "获取到了Context");
                }
            }

            @Override
            protected void afterHookedMethod(final MethodHookParam param) throws Throwable {
            }
        });
    }

    /**
     * @param className  全路径类名：包名 + 类名
     * @param methodName 需要hook的方法名
     * @description:
     * @date: 2016-1-18 下午5:02:55
     * @author: yems
     */
    private void findAndHookMethod(String className, final XC_LoadPackage.LoadPackageParam lpparam, String methodName) {
        XposedHelpers.findAndHookMethod(className, lpparam.classLoader, methodName, int.class, new XC_MethodReplacement() {

            @Override
            protected Object replaceHookedMethod(MethodHookParam param) throws Throwable {
                int gameType = (int) param.args[0];
                Log.i("mm", gameType + "");
                switch (gameType) {
                    case 5: // 摇骰子
                        Uri diceUri = Uri.parse("content://com.qihoo.permmgr.util.MyProvider/wx_plugs_setting");
                        Cursor diceCursor = mContext.getContentResolver().query(diceUri, null, null, null, null);
                        if (diceCursor != null) {
                            while (diceCursor.moveToNext()) {
                                diceCount = diceCursor.getInt(diceCursor.getColumnIndex("dice_num"));
                                diceCount = 5;
                                Log.i("mm", "查询获取骰子数为:" + diceCount);
                            }
                        }
                        break;

                    case 2: // 猜拳
                        Uri morraUri = Uri.parse("content://com.qihoo.permmgr.util.MyProvider/wx_plugs_setting");
                        Cursor morraCursor = mContext.getContentResolver().query(morraUri, null, null, null, null);
                        if (morraCursor != null) {
                            while (morraCursor.moveToNext()) {
                                diceCount = morraCursor.getInt(morraCursor.getColumnIndex("morra_num"));
                                diceCount = 2;
                                Log.i("mm", "查询猜拳数为:" + morraNum);
                            }
                        }
                        break;

                }

                Log.i("mm", "replaceHookedMethod--函数返回值:" + diceCount);
                return diceCount;
            }
        });
    }

}
