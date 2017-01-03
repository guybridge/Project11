package au.com.wsit.project11.adapters;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import au.com.wsit.project11.R;
import au.com.wsit.project11.models.Board;
import au.com.wsit.project11.models.Pin;

/**
 * Created by guyb on 3/01/17.
 */

public class BoardAdapter extends RecyclerView.Adapter<BoardAdapter.ViewHolder>
{
    private Context context;
    private LinkedHashMap<Board, ArrayList<Pin>> boards;

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

    public void swap(LinkedHashMap<Board, ArrayList<Pin>> boards)
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
        // Get the board
        Board boardItem = (Board) boards.keySet().toArray()[position];
        holder.bindViewHolder(boardItem, boards.get(boardItem));
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

        public ViewHolder(View itemView)
        {
            super(itemView);
            boardTitle = (TextView) itemView.findViewById(R.id.boardTitle);
            boardImage = (ImageView) itemView.findViewById(R.id.boardImage);
            pinRecycler = (RecyclerView) itemView.findViewById(R.id.pinRecycler);

            itemView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    togglePinView();
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

        private void bindViewHolder(Board board, ArrayList<Pin> pins)
        {
            boardTitle.setText(board.getBoardTitle());
            Picasso.with(context).load(board.getImageUrl()).into(boardImage);
            pinRecycler.setLayoutManager(new GridLayoutManager(context, 2));
            pinAdapter = new PinAdapter(context, pins);
            pinRecycler.setAdapter(pinAdapter);
        }


    }
}
