package au.com.wsit.project11.api;



import android.content.Context;
import android.util.Log;

import com.parse.FindCallback;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;

import java.util.List;

import au.com.wsit.project11.models.Board;

import au.com.wsit.project11.models.Pin;
import au.com.wsit.project11.utils.Constants;

/**
 * Created by guyb on 4/01/17.
 */

public class ListBoard
{

    public static final String TAG = ListBoard.class.getSimpleName();
    private Context context;

    public ListBoard(Context context)
    {
        this.context = context;
    }

    public interface ListBoardCallback
    {
        void onSuccess(ArrayList<Board> boards);
        void onFail(String result);
    }

    public void getBoards(final ListBoardCallback callback)
    {
        ParseQuery<ParseObject> parseQuery = ParseQuery.getQuery(Constants.BOARDS_CLASS);
        parseQuery.addDescendingOrder("createdAt");
        parseQuery.findInBackground(new FindCallback<ParseObject>()
        {

            @Override
            public void done(List<ParseObject> objects, ParseException e)
            {

                final ArrayList<Board> boards = new ArrayList<>();

                for (ParseObject object : objects)
                {
                    final Board board = new Board();
                    final String boardTitle = object.getString(Constants.KEY_BOARD_NAME);
                    board.setBoardTitle(boardTitle);
                    boards.add(board);

                    // For each board get the pins
                    ListPin listPin = new ListPin(context);
                    listPin.getPins(boardTitle, new ListPin.ListPinCallback()
                    {
                        @Override
                        public void onSuccess(ArrayList<Pin> pinsList)
                        {
                            board.setBoardPins(pinsList);
                        }

                        @Override
                        public void onFail(String errorMessage)
                        {

                        }
                    });

                }

                callback.onSuccess(boards);
            }
        });
    }
}
