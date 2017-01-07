package au.com.wsit.project11.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import au.com.wsit.project11.R;
import au.com.wsit.project11.models.Board;

/**
 * Shows the boards to choose from in the add in activity
 */

public class SimpleBoardAdapter extends RecyclerView.Adapter<SimpleBoardAdapter.ViewHolder>
{
    private Context context;
    private ArrayList<Board> boards;
    private boardListener boardListener;

    public interface boardListener
    {
        void onClick(String boardName);
    }

    public SimpleBoardAdapter(Context context, ArrayList<Board> boards)
    {
        this.context = context;
        this.boards = boards;
        boardListener = (boardListener)context;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.simple_board_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
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
        private TextView boardName;
        private ImageView addBoardImage;

        public ViewHolder(final View itemView)
        {
            super(itemView);
            boardName = (TextView) itemView.findViewById(R.id.boardTitle);
            addBoardImage = (ImageView) itemView.findViewById(R.id.addToboardImageView);

        }

        private void bindViewHolder(final Board boardItem)
        {
            boardName.setText(boardItem.getBoardTitle());

            itemView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    // Animate a board click
                    addBoardImage.setScaleX(0);
                    addBoardImage.setScaleY(0);
                    addBoardImage.animate().scaleX(1).scaleY(1).start();
                    itemView.setBackgroundColor(context.getResources().getColor(R.color.selected));
                    boardListener.onClick(boardItem.getBoardTitle());
                }
            });

        }


    }
}
