package au.com.wsit.project11.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.ByteArrayOutputStream;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
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
    public static final int SHORT_SIDE_TARGET = 256;

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

    public static byte[] getByteArrayFromFile(Context context, Uri uri)
    {
        byte[] fileBytes = null;
        InputStream inStream = null;
        ByteArrayOutputStream outStream = null;

        if (uri.getScheme().equals("content")) {
            try {
                inStream = context.getContentResolver().openInputStream(uri);
                outStream = new ByteArrayOutputStream();

                byte[] bytesFromFile = new byte[1024 * 1024]; // buffer size (1 MB)
                int bytesRead = inStream.read(bytesFromFile);
                while (bytesRead != -1) {
                    outStream.write(bytesFromFile, 0, bytesRead);
                    bytesRead = inStream.read(bytesFromFile);
                }

                fileBytes = outStream.toByteArray();
            } catch (IOException e) {
                Log.e(TAG, e.getMessage());
            } finally {
                try {
                    inStream.close();
                    outStream.close();
                } catch (IOException e) { /*( Intentionally blank */ }
            }
        } else {
            try {
                java.net.URI tempUri = new URI(uri.toString());
                fileBytes = IOUtils.toByteArray(tempUri);
            } catch (IOException e) {
                Log.e(TAG, e.getMessage());
            }
            catch (URISyntaxException e) {
                Log.e(TAG, e.getMessage());
            }
        }

        return fileBytes;
    }

    public static byte[] reduceImageForUpload(byte[] imageData) {
        Bitmap bitmap = ImageResizer.resizeImageMaintainAspectRatio(imageData, SHORT_SIDE_TARGET);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 70, outputStream);
        byte[] reducedData = outputStream.toByteArray();
        try {
            outputStream.close();
        } catch (IOException e) {
            // Intentionally blank
        }

        return reducedData;
    }

    public static String getFileName(Context context, Uri uri, String fileType)
    {
        String fileName = "uploaded_file.";

        if (fileType.equals(Constants.MEDIA_TYPE_IMAGE))
        {
            fileName += "png";
        } else {
            // For video, we want to get the actual file extension
            if (uri.getScheme().equals("content")) {
                // do it using the mime type
                String mimeType = context.getContentResolver().getType(uri);
                int slashIndex = mimeType.indexOf("/");
                String fileExtension = mimeType.substring(slashIndex + 1);
                fileName += fileExtension;
            } else {
                fileName = uri.getLastPathSegment();
            }
        }

        return fileName;
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
