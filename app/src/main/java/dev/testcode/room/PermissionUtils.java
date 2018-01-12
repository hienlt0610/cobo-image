package dev.testcode.room;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hienl on 1/12/2018.
 */

public class PermissionUtils {
    private static PermissionUtils instance;
    private OnPermissionListener listener;

    private static PermissionUtils getInstance() {
        if (instance == null) {
            instance = new PermissionUtils();
        }
        return instance;
    }

    private void init(Builder builder) {
        this.listener = builder.listener;
    }

    public static Builder with(Activity activity) {
        return new Builder(activity);
    }

    public static Builder with(Fragment fragment) {
        return new Builder(fragment.getActivity());
    }

    public static void release() {
        instance = null;
    }

    public static void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        List<String> listGranted = new ArrayList<>();
        List<String> listDenied = new ArrayList<>();
        for (int i = 0; i < grantResults.length; i++) {
            boolean isGranted = grantResults[i] == PackageManager.PERMISSION_GRANTED;
            String permission = permissions[i];
            if (isGranted) {
                listGranted.add(permission);
            } else {
                listDenied.add(permission);
            }
        }
        PermissionUtils permissionUtils = PermissionUtils.getInstance();
        //return result
        if (permissionUtils.listener != null) {
            if (listGranted.size() == permissions.length) {
                permissionUtils.listener.onAllPermissionsGranted(requestCode, listGranted.toArray(new String[listGranted.size()]));
            } else {
                permissionUtils.listener.onPermissionsDenied(requestCode, listDenied.toArray(new String[listDenied.size()]));
            }
        }
    }

    public interface OnPermissionListener {
        void onAllPermissionsGranted(int requestCode, String[] permissions);

        void onPermissionsDenied(int requestCode, String[] permissions);
    }

    public static class Builder {
        private Activity activity;
        private OnPermissionListener listener;
        private String[] permissions;
        private int requestCode;
        private boolean isRationale;

        public Builder(Activity activity) {
            this.activity = activity;
        }

        public Builder setPermissionListener(OnPermissionListener listener) {
            this.listener = listener;
            return this;
        }

        public Builder setPermission(String... permissions) {
            this.permissions = permissions;
            return this;
        }

        public Builder setRationale(boolean rationale) {
            isRationale = rationale;
            return this;
        }

        public void request(int requestCode) {
            this.requestCode = requestCode;
            PermissionUtils.getInstance().init(this);
            checkAndRequestPermission();
        }

        public void request() {
            request(0);
        }

        private void checkAndRequestPermission() {
            boolean isNoNeedToCheck = Build.VERSION.SDK_INT < Build.VERSION_CODES.M;
            if (isNoNeedToCheck || hasPermission(permissions)) {
                if (listener != null) {
                    listener.onAllPermissionsGranted(requestCode, permissions);
                }
            } else {
                if (activity != null) {
                    if (isRationale) {
                        boolean isHasRationalePermission = false;
                        for (String permission : permissions) {
                            if (!ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
                                isHasRationalePermission = true;
                                break;
                            }
                        }
                        if (isHasRationalePermission) {
                            showRationaleConfirm(activity, "You need to grant access to many permission!!!", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    ActivityCompat.requestPermissions(activity, permissions, requestCode);
                                }
                            });
                        }
                    } else {
                        ActivityCompat.requestPermissions(activity, permissions, requestCode);
                    }
                }
            }
        }

        private void showRationaleConfirm(Context context, String message, DialogInterface.OnClickListener okClickListener) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setMessage(message);
            builder.setPositiveButton("Grant", okClickListener);
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.show();
        }

        private boolean hasPermission(String... permissions) {
            boolean isGranted = true;
            for (String permission : permissions) {
                if (ContextCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_DENIED) {
                    isGranted = false;
                    break;
                }
            }
            return isGranted;
        }
    }
}
