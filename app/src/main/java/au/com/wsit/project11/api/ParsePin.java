package au.com.wsit.project11.api;

import android.util.Log;
import android.view.ViewGroup;

import com.parse.DeleteCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
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

        // Add the boards to which the pin is associated
        for(String boardName : boardNames)
        {
           parseObject.add(Constants.KEY_BOARD_NAME, boardName);
        }

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

    public void updatePin(String pinID, String pinName, String pinTags, final ParsePinCallback callback)
    {
        // TODO: Update pin tags
        ParseQuery<ParseObject> pinQuery = ParseQuery.getQuery(Constants.PIN_CLASS);
        try
        {
            ParseObject pin = pinQuery.get(pinID);
            pin.put(Constants.KEY_PIN_TAGS, pinTags);
            pin.saveInBackground(new SaveCallback()
            {
                @Override
                public void done(ParseException e)
                {
                    if(e == null)
                    {
                        callback.onSuccess("Updated Pin Tags");
                    }
                    else
                    {
                        callback.onFail(e.getMessage());
                    }

                }
            });
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }

    }

    public void deletePin(String pinID, final ParsePinCallback callback)
    {
        ParseQuery<ParseObject> deleteQuery = ParseQuery.getQuery(Constants.PIN_CLASS);
        try
        {
            ParseObject parseObject = deleteQuery.get(pinID);
            parseObject.deleteInBackground(new DeleteCallback()
            {
                @Override
                public void done(ParseException e)
                {
                    if(e == null)
                    {
                        callback.onSuccess("Deleted pin");
                    }
                    else
                    {
                        callback.onFail(e.getMessage());
                    }
                }
            });
        }
        catch (ParseException e)
        {
            e.printStackTrace();
            callback.onFail(e.getMessage());
        }

    }
}
