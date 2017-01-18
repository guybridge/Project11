package au.com.wsit.project11.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import au.com.wsit.project11.R;

/**
 * This adapter is for when a user creates a board and wants to add a cover to it.
 */

public class PreviewBoardAdapter extends RecyclerView.Adapter<PreviewBoardAdapter.ViewHolder>
{
    private OnItemClickListener listener;

    public interface OnItemClickListener
    {
        void onItemClick(int resourceLocation);
    }

    private Context context;
    private int[] boardItems;
    private static final String TAG = PreviewBoardAdapter.class.getSimpleName();

    public PreviewBoardAdapter(Context context, OnItemClickListener listener, int[] boardItems)
    {
        this.context = context;
        this.boardItems = boardItems;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        Log.i(TAG, "onCreateViewHolder called");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.small_board_item, parent, false);
        PreviewBoardAdapter.ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position)
    {
        holder.bindViewHolder(boardItems[position]);
    }

    @Override
    public int getItemCount()
    {
        return boardItems.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        private ImageView previewImage;

        public ViewHolder(final View itemView)
        {
            super(itemView);
            previewImage = (ImageView) itemView.findViewById(R.id.previewPhoto);


        }

        private void bindViewHolder(final int image)
        {
            Log.i(TAG, "Showing images");
            Picasso.with(context).load(image).into(previewImage);

            itemView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    // Animate the click
                    previewImage.setAlpha(0.2f);
                    previewImage.animate().alpha(1f).start();
                    listener.onItemClick(image);
                }
            });
        }

    }
}
