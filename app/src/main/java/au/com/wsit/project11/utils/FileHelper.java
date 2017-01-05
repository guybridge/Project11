package au.com.wsit.project11.utils;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;
import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO;

/**
 * Created by guyb on 4/01/17.
 */

public class FileHelper
{
    private static final String TAG = FileHelper.class.getSimpleName();
    private Context context;

    public FileHelper(Context context)
    {
        this.context = context;
    }

    public Uri getOutputMediaFileUri(int mediaType)
    {
        // check for external storage
        if(isExternalStorageAvailable())
        {
            // get the Uri

            // 1.  Get the external storage directory
            File mediaStorageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);

            // 2. Create a unique file name;
            String[] fileArray = createFileName(mediaType);

            File mediaFile;
            // 3. Create the file
            try
            {
                mediaFile = File.createTempFile(fileArray[0], fileArray[1], mediaStorageDir);
                Log.i(TAG, "File: " + Uri.fromFile(mediaFile));
                // 4. Return the file's URI
                return Uri.fromFile(mediaFile);
            }
            catch (IOException e)
            {
                Log.e(TAG, "Error creating file: " + mediaStorageDir.getAbsolutePath() + fileArray[0] + fileArray[1]);
            }

        }

        // Something went wrong
        return null;

    }

    private String[] createFileName(int mediaType)
    {
        Log.i(TAG, "Trying to create file name");
        String fileName = "";
        String fileType = "";
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

        if (mediaType == MEDIA_TYPE_IMAGE)
        {
            fileName = "IMG_"+ timeStamp;
            fileType = ".jpg";
            String[] fileArray = {fileName , fileType};
            return fileArray;
        }
        else if(mediaType == MEDIA_TYPE_VIDEO)
        {
            fileName = "VID_"+ timeStamp;
            fileType = ".mp4";
            String[] fileArray = {fileName , fileType};
            return fileArray;
        }
        else
        {
            return null;
        }
    }

    private boolean isExternalStorageAvailable()
    {
        String state = Environment.getExternalStorageState();
        if(Environment.MEDIA_MOUNTED.equals(state))
        {
            return true;
        }
        else
        {
            return false;
        }
    }
}
