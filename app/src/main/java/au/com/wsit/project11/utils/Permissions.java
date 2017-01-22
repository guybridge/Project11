package au.com.wsit.project11.utils;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

/**
 * Created by guyb on 22/01/17.
 */

public class Permissions extends Activity
{
    private static final String TAG = Permissions.class.getSimpleName();
    private Activity activity;

    public interface PermissionsCallback
    {
        void onGranted();
        void onDenied();
    }

    public Permissions(Activity activity)
    {
        this.activity = activity;
    }

    public void requestCameraPermissions(PermissionsCallback callback)
    {
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
        {
                Log.i(TAG, "Permissions for camera not granted");
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions
                        (activity,
                                new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        Constants.PERMISSIONS_REQUEST);
        }
        else
        {
            callback.onGranted();
        }

    }
}
