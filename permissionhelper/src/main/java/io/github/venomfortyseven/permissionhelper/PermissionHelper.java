package io.github.venomfortyseven.permissionhelper;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.text.TextUtils;

public class PermissionHelper {


    public static final String EXTRA_PERMISSIONS = "permissions";
    public static final String EXTRA_RATIONALE_TITLE = "rationale_title";
    public static final String EXTRA_RATIONALE_MESSAGE = "rationale_message";
    public static final String EXTRA_DENY_TITLE = "deny_title";
    public static final String EXTRA_DENY_MESSAGE = "deny_message";
    public static final String EXTRA_GRANT_BUTTON_TEXT = "grant_button_text";
    public static final String EXTRA_DENY_BUTTON_TEXT = "deny_button_text";
    public static final String EXTRA_DENIED_CLOSE_BUTTON_TEXT = "denied_close_button_text";
    public static final String EXTRA_SETTING_BUTTON_TEXT = "setting_button_text";


    private Context context;
    /**
     * 요청할 권한.
     */
    private String[] permissions;
    /**
     * 권한을 거부한 이력이 있을 때 표시할 제목.
     */
    private CharSequence rationaleTitle;
    /**
     * 권한을 거부한 이력이 있을 때 표시할 내용.
     */
    private CharSequence rationaleMessage;
    /**
     * 권한 거부 시 표시할 제목.
     */
    private CharSequence denyTitle;
    /**
     * 권한 거부 시 표시할 내용.
     */
    private CharSequence denyMessage;
    /**
     * 승인 버튼 텍스트.
     */
    private CharSequence grantButtonText;
    /**
     * 거부 버튼 텍스트.
     */
    private CharSequence denyButtonText;
    /**
     * 권한 거부하면 나오는 설정화면으로 이동을 묻는 알림창 닫기 버튼 텍스트.
     */
    private CharSequence deniedCloseButtonText;
    /**
     * 권한 거부하면 나오는 설정화면으로 이동을 묻는 알리창 버튼 텍스트.
     */
    private CharSequence settingButtonText;
    /**
     * 권한 승인 또는 거부 시 이벤트.
     */
    private PermissionListener listener;


    public PermissionHelper(@NonNull Builder builder) {
        context = builder.context;
        permissions = builder.permissions;
        rationaleTitle = builder.rationaleTitle;
        rationaleMessage = builder.rationaleMessage;
        denyTitle = builder.denyTitle;
        denyMessage = builder.denyMessage;
        grantButtonText = builder.grantButtonText;
        denyButtonText = builder.denyButtonText;
        deniedCloseButtonText = builder.deniedCloseButtonText;
        settingButtonText = builder.settingButtonText;
        listener = builder.listener;

        if (TextUtils.isEmpty(grantButtonText))
            grantButtonText = context.getText(R.string.grant_button_text);
        if (TextUtils.isEmpty(denyButtonText))
            denyButtonText = context.getText(R.string.deny_button_text);
        if (TextUtils.isEmpty(denyMessage))
            denyMessage = context.getText(R.string.deny_message);
        if (TextUtils.isEmpty(deniedCloseButtonText))
            deniedCloseButtonText = context.getText(R.string.denied_close_button_text);
        if (TextUtils.isEmpty(settingButtonText))
            settingButtonText = context.getText(R.string.setting_button_text);
    }


    /**
     * 권한 요청.
     */
    public void request() {
        Intent intent = new Intent(context, PermissionActivity.class);
        Bundle extras = new Bundle();
        extras.putStringArray(EXTRA_PERMISSIONS, permissions);
        extras.putCharSequence(EXTRA_RATIONALE_TITLE, rationaleTitle);
        extras.putCharSequence(EXTRA_RATIONALE_MESSAGE, rationaleMessage);
        extras.putCharSequence(EXTRA_DENY_TITLE, denyTitle);
        extras.putCharSequence(EXTRA_DENY_MESSAGE, denyMessage);
        extras.putCharSequence(EXTRA_GRANT_BUTTON_TEXT, grantButtonText);
        extras.putCharSequence(EXTRA_DENY_BUTTON_TEXT, denyButtonText);
        extras.putCharSequence(EXTRA_DENIED_CLOSE_BUTTON_TEXT, deniedCloseButtonText);
        extras.putCharSequence(EXTRA_SETTING_BUTTON_TEXT, settingButtonText);
        intent.putExtras(extras);
        PermissionActivity.startActivity(context, intent, listener);
    }


    public static class Builder {


        private Context context;
        private String[] permissions;
        private CharSequence rationaleTitle;
        private CharSequence rationaleMessage;
        private CharSequence denyTitle;
        private CharSequence denyMessage;
        private CharSequence grantButtonText;
        private CharSequence denyButtonText;
        private CharSequence deniedCloseButtonText;
        private CharSequence settingButtonText;
        private PermissionListener listener;


        public Builder(@NonNull Context context) {
            this.context = context;
        }


        /**
         * 요청할 권한.
         * @param permissions
         * @return
         */
        public Builder permissions(@NonNull String... permissions) {
            this.permissions = permissions;
            return this;
        }


        /**
         * 권한을 거부한 이력이 있을 때 표시할 제목.
         * @param res
         * @return
         */
        public Builder rationaleTitle(@StringRes int res) {
            return rationaleTitle(context.getText(res));
        }


        /**
         * 권한을 거부한 이력이 있을 때 표시할 제목.
         * @param title
         * @return
         */
        public Builder rationaleTitle(CharSequence title) {
            rationaleTitle = title;
            return this;
        }


        /**
         * 권한을 거부한 이력이 있을 때 표시할 내용.
         * @param res
         * @return
         */
        public Builder rationaleMessage(@StringRes int res) {
            return rationaleMessage(context.getText(res));
        }


        /**
         * 권한을 거부한 이력이 있을 때 표시할 내용.
         * @param message
         * @return
         */
        public Builder rationaleMessage(CharSequence message) {
            rationaleMessage = message;
            return this;
        }


        /**
         * 권한 거부 시 표시할 제목.
         * @param res
         * @return
         */
        public Builder denyTitle(@StringRes int res) {
            return denyTitle(context.getText(res));
        }


        /**
         * 권한 거부 시 표시할 제목.
         * @param title
         * @return
         */
        public Builder denyTitle(CharSequence title) {
            denyTitle = title;
            return this;
        }


        /**
         * 권한 거부 시 표시할 내용.
         * @param res
         * @return
         */
        public Builder denyMessage(@StringRes int res) {
            return denyMessage(context.getText(res));
        }


        /**
         * 권한 거부 시 표시할 내용.
         * @param message
         * @return
         */
        public Builder denyMessage(CharSequence message) {
            denyMessage = message;
            return this;
        }


        /**
         * 승인 버튼 텍스트.
         * @param res
         * @return
         */
        public Builder grantButtonText(@StringRes int res) {
            return grantButtonText(context.getText(res));
        }


        /**
         * 승인 버튼 텍스트.
         * @param text
         * @return
         */
        public Builder grantButtonText(CharSequence text) {
            grantButtonText = text;
            return this;
        }


        /**
         * 거부 버튼 텍스트.
         * @param res
         * @return
         */
        public Builder denyButtonText(@StringRes int res) {
            return denyButtonText(context.getText(res));
        }


        /**
         * 거부 버튼 텍스트.
         * @param text
         * @return
         */
        public Builder denyButtonText(CharSequence text) {
            denyButtonText = text;
            return this;
        }


        /**
         * 권한 거부하면 나오는 설정화면으로 이동을 묻는 알림창 닫기 버튼 텍스트.
         * @param res
         * @return
         */
        public Builder deniedCloseButtonText(@StringRes int res) {
            return deniedCloseButtonText(context.getText(res));
        }


        /**
         * 권한 거부하면 나오는 설정화면으로 이동을 묻는 알림창 닫기 버튼 텍스트.
         * @param text
         * @return
         */
        public Builder deniedCloseButtonText(CharSequence text) {
            deniedCloseButtonText = text;
            return this;
        }


        /**
         * 권한 거부하면 나오는 설정화면으로 이동을 묻는 알리창 버튼 텍스트.
         * @param res
         * @return
         */
        public Builder settingButtonText(@StringRes int res) {
            return settingButtonText(context.getText(res));
        }


        /**
         * 권한 거부하면 나오는 설정화면으로 이동을 묻는 알리창 버튼 텍스트.
         * @param text
         * @return
         */
        public Builder settingButtonText(CharSequence text) {
            settingButtonText = text;
            return this;
        }


        /**
         * 권한 승인 또는 거부 시 이벤트.
         * @param listener
         * @return
         */
        public Builder listener(PermissionListener listener) {
            this.listener = listener;
            return this;
        }


        public PermissionHelper build() {
            return new PermissionHelper(this);
        }
    }

}
