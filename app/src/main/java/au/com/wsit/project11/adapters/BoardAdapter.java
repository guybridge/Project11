package au.com.wsit.project11.adapters;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;

import au.com.wsit.project11.R;
import au.com.wsit.project11.models.Board;
import au.com.wsit.project11.ui.fragments.AddBoardFragment;
import au.com.wsit.project11.utils.Constants;
import au.com.wsit.project11.utils.Generator;

/**
 * This is the main adapter that shows the boards
 */

public class BoardAdapter extends RecyclerView.Adapter<BoardAdapter.ViewHolder>
{
    private Context context;
    private ArrayList<Board> boards = new ArrayList<>();
    private static final String TAG = BoardAdapter.class.getSimpleName();

    public BoardAdapter(Context context)
    {
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.board_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    public void add(Board board)
    {
        if(board != null)
        {
            this.boards.add(0, board);
            notifyItemInserted(0);
        }
    }

    // Replace the board item at a certain index.
    public void replace(Board newBoard)
    {
        if(newBoard != null)
        {
            // Find the index of the current board
            for (int i = 0; i < boards.size(); i++)
            {
                if(boards.get(i).getBoardTitle().equals(newBoard.getBoardTitle()))
                {
                    Log.i(TAG, "Board found at index " + i);
                    // Remove the board
                    boards.remove(i);
                    // Add the new board at the same index
                    boards.add(i,newBoard);
                    notifyItemChanged(i);
                }
            }
        }
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
            Picasso.with(context).load(board.getImageUrl()).into(boardImage);

            pinRecycler.setLayoutManager(new ScrollessLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
            pinAdapter = new PinAdapter(context);
            pinRecycler.setAdapter(pinAdapter);
            pinAdapter.swap(board.getPins());

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
                                            showsPhotosChooser(getAdapterPosition(), null, board.getBoardTitle());
                                            break;
                                        case 1:
                                            // Delete the board
                                            deleteBoard(getAdapterPosition(), null);
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
                pinRecycler.setAlpha(0.1f);
                pinRecycler.setScaleY(0);
                pinRecycler.setPivotY(0);
                pinRecycler.animate().alpha(1).scaleY(1).setDuration(400).start();
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
        updateBoardFragment.setArguments(bundle);
        updateBoardFragment.show(fm, "ChangeAttributes");
    }

}
