package au.com.wsit.project11.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import au.com.wsit.project11.R;

/**
 * This adapter is for when a user creates a board and wants to add a cover to it.
 */

public class PreviewBoardAdapter extends RecyclerView.Adapter<PreviewBoardAdapter.ViewHolder>
{
    private OnItemClickListener listener;
    private Context context;
    private ArrayList<String> pinUrls = new ArrayList<>();
    private static final String TAG = PreviewBoardAdapter.class.getSimpleName();

    public interface OnItemClickListener
    {
        void onItemClick(String resourceLocation);
    }

    public PreviewBoardAdapter(Context context, OnItemClickListener listener)
    {
        this.context = context;
        this.listener = listener;
    }

    public void add(String url)
    {
        if(url != null)
        {
            pinUrls.add(url);
            notifyDataSetChanged();
        }
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
        holder.bindViewHolder(pinUrls.get(position));
    }

    @Override
    public int getItemCount()
    {
        return pinUrls.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        private ImageView previewImage;

        public ViewHolder(final View itemView)
        {
            super(itemView);
            previewImage = (ImageView) itemView.findViewById(R.id.previewPhoto);
        }

        private void bindViewHolder(final String pinUrl)
        {
            Log.i(TAG, "Showing images");
            Picasso.with(context).load(pinUrl).into(previewImage);

            itemView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    // Animate the click
                    previewImage.setAlpha(0.2f);
                    previewImage.animate().alpha(1f).start();
                    listener.onItemClick(pinUrl);
                }
            });
        }

    }
}
