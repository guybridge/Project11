package au.com.wsit.project11.api;

import android.util.Log;

import com.parse.DeleteCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;
import com.squareup.picasso.Callback;

import au.com.wsit.project11.utils.Constants;

/**
 * API Calls to support CRUD operations in Parse backend
 */

public class ParseBoard
{
    private static final String TAG = ParseBoard.class.getSimpleName();

    public interface ParseBoardCallback
    {
        void onSuccess(String result);
        void onFail(String result);
    }

    public void addBoard(final String boardName, int mediaUri, final ParseBoardCallback callback)
    {
        ParseObject parseObject = new ParseObject(Constants.BOARDS_CLASS);
        parseObject.put(Constants.KEY_BOARD_NAME, boardName);
        parseObject.put(Constants.KEY_BOARD_IMAGE_URL, mediaUri);
        parseObject.saveInBackground(new SaveCallback()
        {
            @Override
            public void done(ParseException e)
            {
                if(e == null)
                {
                    callback.onSuccess(boardName);
                }
                else
                {
                    callback.onFail(e.getMessage());
                }
            }
        });
    }

    // Updates a boards photo
    public void updateBoardPhoto(String boardID, String boardName, int imageLocation, final ParseBoardCallback callback)
    {
        ParseQuery<ParseObject> boardQuery = ParseQuery.getQuery(Constants.BOARDS_CLASS);
        try
        {
            ParseObject parseObject = boardQuery.get(boardID);
            parseObject.put(Constants.KEY_BOARD_IMAGE_URL, imageLocation);
            parseObject.put(Constants.KEY_BOARD_NAME, boardName);
            parseObject.saveInBackground(new SaveCallback()
            {
                @Override
                public void done(ParseException e)
                {
                    if(e == null)
                    {
                        callback.onSuccess("Saved");
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

    // Delete a board
    public void deleteBoard(String boardID, final ParseBoardCallback callback)
    {
        ParseQuery<ParseObject> deleteQuery = ParseQuery.getQuery(Constants.BOARDS_CLASS);
        try
        {
            ParseObject board = deleteQuery.get(boardID);
            board.deleteEventually(new DeleteCallback()
            {
                @Override
                public void done(ParseException e)
                {
                    if(e == null)
                    {
                        callback.onSuccess("Deleted");
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
            Log.i(TAG, "Error deleting board " + e.getMessage());
        }

    }

}
