package io.github.venomfortyseven.permissionhelper;

import java.util.List;

public interface PermissionListener {

    /**
     * 사용자가 권한 부여를 승인했을 때 이벤트.
     */
    void onPermissionGranted();

    /**
     * 사용자가 권한 부여를 거부했을 때 이벤트.
     * @param deniedPermissions
     */
    void onPermissionDenied(List<String> deniedPermissions);
}
