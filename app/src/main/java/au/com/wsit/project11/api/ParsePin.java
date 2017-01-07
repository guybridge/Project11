package au.com.wsit.project11.api;

import android.util.Log;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseRelation;
import com.parse.SaveCallback;

import java.util.ArrayList;

import au.com.wsit.project11.utils.Constants;

/**
 * Created by guyb on 5/01/17.
 */

public class ParsePin
{
    private static final String TAG = ParsePin.class.getSimpleName();

    public interface ParsePinCallback
    {
        void onSuccess(String result);
        void onFail(String result);
    }

    public void addPin(String title, String comment, String tags, ParseFile file, ArrayList<String> boardNames, final ParsePinCallback callback)
    {
        ParseObject parseObject = new ParseObject(Constants.PIN_CLASS);
        parseObject.put(Constants.KEY_PIN_TITLE, title);
        parseObject.put(Constants.KEY_PIN_COMMENT, comment);
        parseObject.put(Constants.KEY_PIN_TAGS, tags);
        parseObject.put(Constants.MEDIA_DATA, file);

        Log.i(TAG, "Starting to save");

        parseObject.saveInBackground(new SaveCallback()
        {
            @Override
            public void done(ParseException e)
            {
                if(e == null)
                {
                    Log.i(TAG, "Success in saving");
                    callback.onSuccess("Success");
                }
                else
                {
                    Log.i(TAG, "Failed to save " + e.getMessage());
                    callback.onFail(e.getMessage());
                }
            }
        });


    }
}
