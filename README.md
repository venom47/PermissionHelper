# Permission Helper
[![](https://jitpack.io/v/venom47/PermissionHelper.svg)](https://jitpack.io/#venom47/PermissionHelper)

## Download
To get a Git project into your build:<br>
**Step 1**. Add the JitPack repository to your build file.<br>
Add it in your root build.gradle at the end of repositories:
```gradle
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```

**Step 2**. Add the dependency
```gradle
dependencies {
    implementation 'com.github.venom47:PermissionHelper:1.0.0'
}
```

## Usage
```java
new PermissionHelper.Builder(this)
        .permissions(Manifest.permission.READ_CALENDAR, Manifest.permission.CAMERA)
        .listener(new PermissionListener() {
            @Override
            public void onPermissionGranted() {

            }

            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {

            }
        })
        .rationaleMessage("사용자에게 이 권한이 왜 필요한지 설명하고 승인받도록 하자.")
        .build()
        .request();
```