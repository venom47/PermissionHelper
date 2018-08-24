package io.github.venomfortyseven.permissionhelperdemo;

import android.Manifest;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.util.List;

import io.github.venomfortyseven.permissionhelper.PermissionHelper;
import io.github.venomfortyseven.permissionhelper.PermissionListener;

public class MainActivity extends AppCompatActivity implements PermissionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new PermissionHelper.Builder(MainActivity.this)
                        .permissions(Manifest.permission.READ_CALENDAR, Manifest.permission.CAMERA)
                        .listener(MainActivity.this)
                        .rationaleMessage("사용자에게 이 권한이 왜 필요한지 설명하고 승인할 수 있도록 하자.")
                        .build()
                        .request();
            }
        });
    }

    @Override
    public void onPermissionGranted() {
        Toast.makeText(this, "Granted!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPermissionDenied(List<String> deniedPermissions) {
        StringBuilder sb = new StringBuilder();
        sb.append("Denied Permissions");
        for (String permission : deniedPermissions) {
            sb.append("\n");
            sb.append(permission);
        }
        Toast.makeText(this, sb, Toast.LENGTH_SHORT).show();
    }
}
