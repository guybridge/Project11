package au.com.wsit.project11.api;

import android.text.BoringLayout;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import au.com.wsit.project11.models.Board;
import au.com.wsit.project11.models.Pin;
import au.com.wsit.project11.utils.Constants;

/**
 * Created by guyb on 4/01/17.
 */

public class ListBoard
{
    public interface ListBoardCallback
    {
        void onSuccess(ArrayList<Board> boards);
        void onFail(String result);
    }

    public void getBoards(final ListBoardCallback callback)
    {
        ParseQuery<ParseObject> parseQuery = ParseQuery.getQuery(Constants.BOARDS_CLASS);
        parseQuery.findInBackground(new FindCallback<ParseObject>()
        {
            @Override
            public void done(List<ParseObject> objects, ParseException e)
            {

                ArrayList<Board> boards = new ArrayList<>();

                for (ParseObject object : objects)
                {
                    Board board = new Board();
                    String boardTitle = object.getString(Constants.KEY_BOARD_NAME);
                    board.setBoardTitle(boardTitle);
                    // TODO: Add placeholder image url
                    boards.add(board);
                }

                callback.onSuccess(boards);
            }
        });
    }
}
