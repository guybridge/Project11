package au.com.wsit.project11.api;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.SaveCallback;

import java.util.ArrayList;

import au.com.wsit.project11.utils.Constants;

/**
 * Created by guyb on 5/01/17.
 */

public class ParsePin
{
    public interface ParsePinCallback
    {
        void onSuccess(String result);
        void onFail(String result);
    }

    public void addPin(String title, String comment, ArrayList<String> tags, final ParsePinCallback callback)
    {
        ParseObject parseObject = new ParseObject(Constants.PIN_CLASS);
        parseObject.put(Constants.KEY_PIN_TITLE, title);
        parseObject.put(Constants.KEY_PIN_COMMENT, comment);
        parseObject.put(Constants.KEY_PIN_TAGS, tags);

        parseObject.saveInBackground(new SaveCallback()
        {
            @Override
            public void done(ParseException e)
            {
                if(e == null)
                {
                    callback.onSuccess("Success");
                }
                else
                {
                    callback.onFail(e.getMessage());
                }
            }
        });
    }
}
