package com.duyuan.photoview;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;

/**
 * Created by duyuan797 on 17/1/13.
 */

public class PermissionUtils {

    private static final String PACKAGE_URL_SCHEME = "package:";

    public boolean checkPermission(Activity activity, String permission) {
        if (ContextCompat.checkSelfPermission(activity, permission)
                != PackageManager.PERMISSION_GRANTED) {

        }
        return true;
    }

    public static void showMissingPermissionDialog(final Activity context) {
        PermissionUtils.showMissingPermissionDialog(context, null);
    }

    // 显示缺失权限提示
    public static void showMissingPermissionDialog(final Activity context,
            final OnDialogClickListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("帮助");
        builder.setMessage("");

        // 拒绝
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override public void onClick(DialogInterface dialog, int which) {
                if (listener != null) {
                    listener.onNegativeClick();
                }
            }
        });

        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override public void onClick(DialogInterface dialog, int which) {
                startAppSettings(context);
                if (listener != null) {
                    listener.onPositiveClick();
                }
            }
        });
        builder.setCancelable(false);
        builder.show();
    }

    // 启动应用的设置
    public static void startAppSettings(Context context) {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse(PACKAGE_URL_SCHEME + BuildConfig.APPLICATION_ID));
        context.startActivity(intent);
    }

    public interface OnDialogClickListener {

        void onPositiveClick();

        void onNegativeClick();
    }
}

