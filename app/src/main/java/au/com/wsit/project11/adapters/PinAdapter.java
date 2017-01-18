package au.com.wsit.project11.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import au.com.wsit.project11.R;
import au.com.wsit.project11.api.ParsePin;
import au.com.wsit.project11.models.Pin;

/**
 * This adapter is for showing the pins within a board
 */

public class PinAdapter extends RecyclerView.Adapter<PinAdapter.ViewHolder>
{
    private ArrayList<Pin> pins = new ArrayList<>();
    private Context context;

    public PinAdapter(Context context)
    {
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pin_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    public void swap(ArrayList<Pin> pins)
    {
        if(pins != null)
        {
            this.pins = pins;
            notifyDataSetChanged();
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position)
    {
        holder.bindViewHolder(pins.get(position));
    }

    @Override
    public int getItemCount()
    {
        return pins.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        private ImageView pinImage;
        private TextView pinTitle;
        private TextView pinComment;
        private TextView pinTags;

        public ViewHolder(View itemView)
        {
            super(itemView);
            pinTitle = (TextView) itemView.findViewById(R.id.pinTitle);
            pinComment = (TextView) itemView.findViewById(R.id.pinComment);
            pinTags = (TextView) itemView.findViewById(R.id.pinTags);
            pinImage = (ImageView) itemView.findViewById(R.id.pinImage);


        }

        private void bindViewHolder(final Pin pin)
        {
            pinTitle.setText(pin.getPinTitle());
            pinComment.setText(pin.getPinTitle());
            pinTags.setText(pin.getPinTags().toString());
            Picasso.with(context).load(pin.getMediaUrl()).into(pinImage);

            itemView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    // TODO: Expand view
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener()
            {
                @Override
                public boolean onLongClick(View v)
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context)
                            .setTitle("Pin Options")
                            .setItems(R.array.pin_choices, new DialogInterface.OnClickListener()
                            {
                                @Override
                                public void onClick(DialogInterface dialog, int which)
                                {
                                    switch(which)
                                    {
                                        case 0:
                                            // Add more tags to pin
                                            break;
                                        case 1:
                                            // Add pin to more boards
                                            break;
                                        case 2:
                                            deletePin(getAdapterPosition(), pin.getPinID());
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

    }

    public void deletePin(final int adapterPosition, String pinID)
    {
        ParsePin parsePin = new ParsePin();
        parsePin.deletePin(pinID, new ParsePin.ParsePinCallback()
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
}
