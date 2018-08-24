package io.github.venomfortyseven.permissionhelper;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

public class PermissionActivity extends AppCompatActivity {

    private static final int REQ_PERMISSIONS = 1530;
    private static final int REQ_SETTING = 1735;

    private String[] permissions;
    private CharSequence rationaleTitle;
    private CharSequence rationaleMessage;
    private CharSequence denyTitle;
    private CharSequence denyMessage;
    private CharSequence grantButtonText;
    private CharSequence denyButtonText;
    private CharSequence deniedCloseButtonText;
    private CharSequence settingButtonText;
    private static PermissionListener permissionListener;

    public static void startActivity(Context context, Intent intent, PermissionListener listener) {
        permissionListener = listener;
        context.startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(0, 0);
        super.onCreate(savedInstanceState);

        // 상태값 가져오기.
        if (savedInstanceState != null) restoreInstanceState(savedInstanceState);
        else restoreInstanceState(getIntent().getExtras());

        // 권한 상태 확인 및 요청.
        checkPermission();
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putStringArray(PermissionHelper.EXTRA_PERMISSIONS, permissions);
        outState.putCharSequence(PermissionHelper.EXTRA_RATIONALE_TITLE, rationaleTitle);
        outState.putCharSequence(PermissionHelper.EXTRA_RATIONALE_MESSAGE, rationaleMessage);
        outState.putCharSequence(PermissionHelper.EXTRA_DENY_TITLE, denyTitle);
        outState.putCharSequence(PermissionHelper.EXTRA_DENY_MESSAGE, denyMessage);
        outState.putCharSequence(PermissionHelper.EXTRA_GRANT_BUTTON_TEXT, grantButtonText);
        outState.putCharSequence(PermissionHelper.EXTRA_DENY_BUTTON_TEXT, denyButtonText);
        outState.putCharSequence(PermissionHelper.EXTRA_DENIED_CLOSE_BUTTON_TEXT, deniedCloseButtonText);
        outState.putCharSequence(PermissionHelper.EXTRA_SETTING_BUTTON_TEXT, settingButtonText);
        super.onSaveInstanceState(outState);
    }


    // 권한 요청 후..
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        // 거부된 권한.
        List<String> deniedPermissions = new ArrayList<>();
        for (String permission : permissions)
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED)
                deniedPermissions.add(permission);
        // 모든 권한이 승인된 경우..
        if (deniedPermissions.isEmpty()) onPermissionGranted();
        // 거부된 권한이 있는 경우..
        else if (!TextUtils.isEmpty(denyMessage)) showDenyDialog(deniedPermissions);
            // 거부된 권한이 있는 경우..
        else onPermissionDenied(deniedPermissions);
    }


    // 설정화면에서 복귀 후..
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQ_SETTING) {
            // 설정화면에서 복귀 후 승인이 필요한 권한 확인.
            List<String> deniedPermissions = new ArrayList<>();
            for (String permission : permissions)
                if (ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_DENIED)
                    deniedPermissions.add(permission);
            // 모든 권한이 승인된 경우..
            if (deniedPermissions.isEmpty()) onPermissionGranted();
            // 거부된 권한이 있는 경우..
            else onPermissionDenied(deniedPermissions);
        }
        else super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void finish() {
        super.finish();
        permissionListener = null;
        overridePendingTransition(0, 0);
    }

    /**
     * 상태 값 가져오기.
     * @param bundle
     */
    private void restoreInstanceState(@Nullable  Bundle bundle) {
        permissions = bundle.getStringArray(PermissionHelper.EXTRA_PERMISSIONS);
        rationaleTitle = bundle.getCharSequence(PermissionHelper.EXTRA_RATIONALE_TITLE);
        rationaleMessage = bundle.getCharSequence(PermissionHelper.EXTRA_RATIONALE_MESSAGE);
        denyTitle = bundle.getCharSequence(PermissionHelper.EXTRA_DENY_TITLE);
        denyMessage = bundle.getCharSequence(PermissionHelper.EXTRA_DENY_MESSAGE);
        grantButtonText = bundle.getCharSequence(PermissionHelper.EXTRA_GRANT_BUTTON_TEXT);
        denyButtonText = bundle.getCharSequence(PermissionHelper.EXTRA_DENY_BUTTON_TEXT);
        deniedCloseButtonText = bundle.getCharSequence(PermissionHelper.EXTRA_DENIED_CLOSE_BUTTON_TEXT);
        settingButtonText = bundle.getCharSequence(PermissionHelper.EXTRA_SETTING_BUTTON_TEXT);
    }


    /**
     * 권한 상태 확인 및 요청하기.
     */
    private void checkPermission() {
        // OS 버전 확인 후 마시멜로 미만이면 승인 이벤트 수행.
        if (isLowerThanMarshmallow()) {
            onPermissionGranted();
            return;
        }

        // 요청한 권한들 중에서 승인이 필요한 권한만 선별.
        List<String> needPermissions = new ArrayList<>();
        for (String permission : permissions)
            if (ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_DENIED)
                needPermissions.add(permission);
        // 승인이 필요한 권한이 없으면 승인 이벤트 수행.
        if (needPermissions.isEmpty()) {
            onPermissionGranted();
            return;
        }

        // 승인이 필요한 권한이 있을 경우.

        // 승인이 필요한 권한 중에서 사용자에게 거부된 이력이 있는 경우.
        if (shouldShowRequestPermissionRationale(needPermissions) && !TextUtils.isEmpty(rationaleMessage)) {
            // 거부 이력이 있을 경우 사용자에게 권한이 필요한 근거에 대한 알림창 표시.
            showRationaleDialog(needPermissions);
        } else {
            // 권한 요청.
            requestPermissions(needPermissions);
        }
    }


    /**
     * OS 버전 확인.
     * @return 마시멜로 미만이면 true, 이상이면 false.
     */
    private boolean isLowerThanMarshmallow() {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.M;
    }


    /**
     * 권한을 거부한 이력이 있는지 확인.
     * @param needPermissions
     * @return 권한을 거부한 이력이 있으면 true, 없으면 false.
     */
    private boolean shouldShowRequestPermissionRationale(List<String> needPermissions) {
        for (String permission : needPermissions)
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission))
                return true;
        return false;
    }


    /**
     * 권한 요청하기.
     * @param needPermissions
     */
    private void requestPermissions(List<String> needPermissions) {
        ActivityCompat.requestPermissions(this, needPermissions.toArray(new String[needPermissions.size()]), REQ_PERMISSIONS);
    }


    /**
     * 사용자가 권한을 거부한 이력이 있을 경우 권한이 필요한 근거를 설명할 다이얼로그.
     * @param needPermissions
     */
    private void showRationaleDialog(final List<String> needPermissions) {
        new AlertDialog.Builder(this, R.style.Theme_AppCompat_Light_Dialog_Alert)
                .setTitle(rationaleTitle)
                .setMessage(rationaleMessage)
                .setCancelable(false)
                .setPositiveButton(grantButtonText, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        requestPermissions(needPermissions);
                    }
                })
                .setNegativeButton(denyButtonText, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (!TextUtils.isEmpty(denyMessage))
                            showDenyDialog(needPermissions);
                        else
                            onPermissionDenied(needPermissions);
                    }
                })
                .show();
    }


    /**
     * 사용자가 권한을 거부한 경우 설정하면으로 이동할 수 있도록 하는 다이얼로그.
     * @param deniedPermissions
     */
    private void showDenyDialog(final List<String> deniedPermissions) {
        new AlertDialog.Builder(this, R.style.Theme_AppCompat_Light_Dialog_Alert)
                .setTitle(denyTitle)
                .setMessage(denyMessage)
                .setCancelable(false)
                .setPositiveButton(settingButtonText, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent settingIntent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        settingIntent.setData(Uri.parse("package:" + getPackageName()));
                        startActivityForResult(settingIntent, REQ_SETTING);
                    }
                })
                .setNegativeButton(deniedCloseButtonText, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        onPermissionDenied(deniedPermissions);
                    }
                })
                .show();
    }


    /**
     * 권한이 승인되면 준비된 이벤트 수행 후 액티비티 종료.
     */
    private void onPermissionGranted() {
        if (permissionListener != null)
            permissionListener.onPermissionGranted();
        finish();
    }


    /**
     * 권한이 거부되면 준비된 이벤트 수행 후 액티비티 종료.
     */
    private void onPermissionDenied(List<String> deniedPermissions) {
        if (permissionListener != null)
            permissionListener.onPermissionDenied(deniedPermissions);
        finish();
    }
}
