package au.com.wsit.project11.adapters;

import android.app.Activity;
import android.app.Dialog;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import au.com.wsit.project11.R;
import au.com.wsit.project11.api.ListPin;
import au.com.wsit.project11.api.ParseBoard;
import au.com.wsit.project11.models.Board;
import au.com.wsit.project11.models.Pin;
import au.com.wsit.project11.ui.fragments.AddBoardFragment;
import au.com.wsit.project11.utils.Constants;
import au.com.wsit.project11.utils.Generator;

/**
 * This is the main adapter that shows the boards
 */

public class BoardAdapter extends RecyclerView.Adapter<BoardAdapter.ViewHolder> implements AddBoardFragment.UpdateBoardCallback
{
    private Context context;
    private ArrayList<Board> boards = new ArrayList<>();
    private static final String TAG = BoardAdapter.class.getSimpleName();
    private NotifyBoardChanges notifyBoardChanges;


    public BoardAdapter(Context context)
    {
        this.context = context;
        this.notifyBoardChanges = (NotifyBoardChanges) context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.board_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    public interface NotifyBoardChanges
    {
        void onChanged();
    }

    public void swap(ArrayList<Board> boards)
    {

        if(boards != null)
        {
            this.boards = boards;
            notifyDataSetChanged();
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position)
    {
        holder.bindViewHolder(boards.get(position));
    }

    @Override
    public int getItemCount()
    {
        return boards.size();
    }

    @Override
    public void onFail(String result)
    {

    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        private TextView boardTitle;
        private ImageView boardImage;
        private RecyclerView pinRecycler;
        private PinAdapter pinAdapter;

        public ViewHolder(final View itemView)
        {
            super(itemView);
            boardTitle = (TextView) itemView.findViewById(R.id.boardTitle);
            boardImage = (ImageView) itemView.findViewById(R.id.boardImage);
            pinRecycler = (RecyclerView) itemView.findViewById(R.id.pinRecycler);
            pinRecycler.setLayoutManager(new GridLayoutManager(context, 2));
            pinRecycler.setAdapter(pinAdapter);
        }

        private void bindViewHolder(final Board board)
        {
            boardTitle.setText(board.getBoardTitle());
            try
            {
                Picasso.with(context).load(board.getImageUrl()).into(boardImage);
            }
            catch(IllegalArgumentException e)
            {
                Log.i(TAG, "Error setting board image " + e.getMessage());
                Picasso.with(context).load(Generator.getRandomImage()).into(boardImage);
            }
            pinRecycler.setLayoutManager(new ScrollessLayoutManager(2, 0));
            pinAdapter = new PinAdapter(context);
            pinRecycler.setAdapter(pinAdapter);
            pinAdapter.swap(board.getBoardPins());

            itemView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    // Show we clicked on a board by changing the alpha
                    itemView.setAlpha(0.3f);
                    itemView.animate().alpha(1).setDuration(250).start();
                    togglePinView();
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener()
            {
                @Override
                public boolean onLongClick(View v)
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context)
                            .setTitle("Board options")
                            .setItems(R.array.board_choices, new DialogInterface.OnClickListener()
                            {
                                @Override
                                public void onClick(DialogInterface dialog, int which)
                                {
                                    switch (which)
                                    {
                                        case 0:
                                            // Change the board attributes
                                            showsPhotosChooser(getAdapterPosition(), board.getBoardID(), board.getBoardTitle());
                                            break;
                                        case 1:
                                            // Delete the board
                                            deleteBoard(getAdapterPosition(), board.getBoardID());
                                            break;
                                    }
                                }
                            });
                    AlertDialog dialog = builder.create();
                    dialog.show();

                    return true;
                }
            });
        }


        private void togglePinView()
        {
            if(pinRecycler.getVisibility() == View.GONE)
            {
                pinRecycler.setVisibility(View.VISIBLE);
            }
            else
            {
                pinRecycler.setVisibility(View.GONE);
            }
        }


    }

    // Deletes a board
    private void deleteBoard(final int adapterPosition, String boardID)
    {
        ParseBoard deleteBoard = new ParseBoard();
        deleteBoard.deleteBoard(boardID, new ParseBoard.ParseBoardCallback()
        {
            @Override
            public void onSuccess(String result)
            {
                notifyItemRemoved(adapterPosition);
            }

            @Override
            public void onFail(String result)
            {

            }
        });
    }

    private void showsPhotosChooser(int adapterPosition, String boardID, String boardName)
    {
        Activity activity = (Activity)context;
        Bundle bundle = new Bundle();
        bundle.putString(Constants.KEY_BOARD_NAME, boardName);
        bundle.putString(Constants.KEY_BOARD_ID, boardID);
        bundle.putInt(Constants.KEY_BOARD_POSITION, adapterPosition);
        FragmentManager fm = activity.getFragmentManager();
        AddBoardFragment updateBoardFragment = new AddBoardFragment();
        updateBoardFragment.setListener(BoardAdapter.this);
        updateBoardFragment.setArguments(bundle);
        updateBoardFragment.show(fm, "ChangeAttributes");
    }

    // Callback from changing the board attributes
    @Override
    public void onSuccess(final int boardPosition, String boardID, String boardName, int mediaUri)
    {
        Log.i(TAG, "Got callback from modify board");
        Log.i(TAG, "Board name is: " + boardName);
        Log.i(TAG, "Image asset is located at: " + mediaUri);
        ParseBoard updateBoard = new ParseBoard();
        updateBoard.updateBoardPhoto(boardID, boardName, mediaUri, new ParseBoard.ParseBoardCallback()
        {
            @Override
            public void onSuccess(String result)
            {
                Log.i(TAG, result);
                notifyBoardChanges.onChanged();
            }

            @Override
            public void onFail(String result)
            {
                Log.i(TAG, result);
            }
        });
    }
}
