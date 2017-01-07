package au.com.wsit.project11.api;

import android.content.Context;
import android.util.Log;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

import au.com.wsit.project11.models.Pin;
import au.com.wsit.project11.utils.Constants;



/**
 * Created by guyb on 7/01/17.
 */

public class ListPin
{
    private static final String TAG = ListPin.class.getSimpleName();
    private Context context;

    public ListPin(Context context)
    {
        this.context = context;
    }

    public interface ListPinCallback
    {
        void onSuccess(ArrayList<Pin> pinsList);
        void onFail(String errorMessage);
    }

    public void getPins(final String boardTitle, final ListPinCallback callback)
    {
        Log.i(TAG, "Querying pins list for board: " + boardTitle);
        // For each board query the pins class to get what pins should appear in each board
        ParseQuery<ParseObject> pins = ParseQuery.getQuery(Constants.PIN_CLASS);
        // Each pin has an array of board names associated with it
        pins.whereContains(Constants.KEY_BOARD_NAME, boardTitle);
        pins.findInBackground(new FindCallback<ParseObject>()
        {
            @Override
            public void done(List<ParseObject> objects, ParseException e)
            {
                if(objects.size() == 0)
                {
                    Log.i(TAG, "No pins for board " + boardTitle);
                }
                ArrayList<Pin> pinsList = new ArrayList<>();
                for(ParseObject pinInstance : objects)
                {
                    Pin pin = new Pin();
                    Log.i(TAG, "Pins that are within the " + boardTitle + " are " + pinInstance.getString(Constants.KEY_PIN_TITLE));
                    String pinTitle = pinInstance.getString(Constants.KEY_PIN_TITLE);
                    String pinComment = pinInstance.getString(Constants.KEY_PIN_COMMENT);
                    String pinTags = pinInstance.getString(Constants.KEY_PIN_TAGS);
                    ParseFile parseFile = pinInstance.getParseFile(Constants.MEDIA_DATA);
                    String pinImageUrl = parseFile.getUrl();

                    Log.i(TAG, "Url of image is: " + pinImageUrl);

                    pin.setPinTitle(pinTitle);
                    pin.setPinComment(pinComment);
                    pin.setPinTags(pinTags);
                    pin.setMediaUrl(pinImageUrl);
                    pinsList.add(pin);
                }

                callback.onSuccess(pinsList);

            }
        });
    }
}
