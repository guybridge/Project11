package au.com.wsit.project11.api;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.SaveCallback;

import au.com.wsit.project11.utils.Constants;

/**
 * Created by guyb on 4/01/17.
 */

public class ParseBoard
{
    public interface ParseBoardCallback
    {
        void onSuccess(String result);
        void onFail(String result);
    }

    public void addBoard(String boardName, final ParseBoardCallback callback)
    {
        ParseObject parseObject = new ParseObject(Constants.BOARDS_CLASS);
        parseObject.put(Constants.KEY_BOARD_NAME, boardName);
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
