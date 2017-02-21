package au.com.wsit.project11.adapters;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Map;

import au.com.wsit.project11.R;
import au.com.wsit.project11.models.Board;
import au.com.wsit.project11.models.Pin;
import au.com.wsit.project11.ui.LargePinActivity;
import au.com.wsit.project11.ui.fragments.ChangePinFragment;
import au.com.wsit.project11.utils.Constants;

/**
 * This adapter is for showing the pins within a board
 */

public class PinAdapter extends RecyclerView.Adapter<PinAdapter.ViewHolder> implements ChangePinFragment.PinCallback
{
    private static final String TAG = PinAdapter.class.getSimpleName();
    private ArrayList<Pin> pins = new ArrayList<>();
    private Context context;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    public PinAdapter(Context context)
    {
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child(Constants.BOARDS);
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pin_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    public void swap(Map<String, Pin> pinsMapData)
    {
        if(pinsMapData != null)
        {

            for(Map.Entry<String, Pin> pin : pinsMapData.entrySet())
            {
                pins.add(pin.getValue());
            }

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

    // Callback from changing a pin
    @Override
    public void onChanged(int adapterPosition, String pinName, String pinComment, String mediaUrl, String pinTags, String mediaType)
    {
        Log.i(TAG, "**Pin changed**");
        Log.i(TAG, "Adapter pos: " + adapterPosition);
        Log.i(TAG, "Pin Name: " + pinName);
        Log.i(TAG, "Pin Comment: " + pinComment);
        Log.i(TAG, "Pin mediaUrl: " + mediaUrl);
        Log.i(TAG, "PinTags: " + pinTags);
        Log.i(TAG, "Pin mediaType: " + mediaType);

        Pin pin = new Pin(pinName, pinComment, pinTags, mediaUrl, mediaType);

        databaseReference.addChildEventListener(new ChildEventListener()
        {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s)
            {
                Log.i(TAG, "onChildAdded called");

                for(DataSnapshot child : dataSnapshot.getChildren())
                {
                    Log.i(TAG, "Child key is: " + child.getKey());
                }
                //databaseReference.child(Constants.PINS).updateChildren()
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s)
            {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot)
            {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s)
            {

            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {

            }
        });

    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        private ImageView pinImage;
        private VideoView pinVideo;
        private TextView pinTitle;
        private TextView pinComment;
        private TextView pinTags;
        private String mediaType;

        public ViewHolder(View itemView)
        {
            super(itemView);
            pinTitle = (TextView) itemView.findViewById(R.id.pinTitle);
            pinComment = (TextView) itemView.findViewById(R.id.pinComment);
            pinTags = (TextView) itemView.findViewById(R.id.pinTags);
            pinImage = (ImageView) itemView.findViewById(R.id.pinImage);
            pinVideo = (VideoView)itemView.findViewById(R.id.pinVideo);
        }

        private void bindViewHolder(final Pin pin)
        {
            pinTitle.setText(pin.getPinTitle());
            pinComment.setText(pin.getPinTitle());
            pinTags.setText(pin.getPinTags());

            // Check for video or image
            mediaType = pin.getMediaType();
            if(mediaType == null)
            {
                mediaType = Constants.TYPE_IMAGE;
            }


            if(mediaType.equals(Constants.TYPE_VIDEO))
            {
                Log.i(TAG, "Media is type video");
                Log.i(TAG, "media URI is: " + pin.getMediaUrl());
                // Hide the imageView
                pinImage.setVisibility(View.GONE);
                pinVideo.setMediaController(null);
                pinVideo.setVideoURI(Uri.parse(pin.getMediaUrl()));

                // Play the video onClick
                itemView.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        pinVideo.start();
                        Log.i(TAG, "Video play starting");
                    }
                });
            }
            else
            {
                Log.i(TAG, "Pin is type image");
                // Load the image and hide the video
                Picasso.with(context).load(pin.getMediaUrl()).into(pinImage);
                pinVideo.setVisibility(View.GONE);

                // Expand the image if it's an image
                itemView.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        Log.i(TAG, "Show large image");
                        // Animate the shared element transition
                        View sharedElement = pinImage;
                        String transitionName = context.getString(R.string.expand_view);
                        ActivityOptionsCompat options =
                                ActivityOptionsCompat
                                        .makeSceneTransitionAnimation
                                                ((Activity)context, sharedElement, transitionName);

                        Intent intent = new Intent(context, LargePinActivity.class);
                        intent.putExtra(Constants.KEY_PIN_IMAGE_URL, pin.getMediaUrl());
                        intent.putExtra(Constants.KEY_PIN_TITLE, pin.getPinTitle());
                        ActivityCompat.startActivity(context, intent, options.toBundle());
                    }
                });
            }


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
                                            showEditPinDialog(getAdapterPosition(), pin.getPinTitle(), pin.getPinComment(), pin.getMediaUrl(), pin.getPinTags(), pin.getMediaType());
                                            break;
                                        case 1:
                                            // Add pin to more boards
                                            break;
                                        case 2:
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

    private void showEditPinDialog(int adapterPosition, String pinName, String pinComment, String mediaUrl, String pinTags, String mediaType)
    {
        Bundle bundle = new Bundle();
        bundle.putInt(Constants.KEY_BOARD_POSITION, adapterPosition);
        bundle.putString(Constants.KEY_PIN_TITLE, pinName);
        bundle.putString(Constants.MEDIA_URL, mediaUrl);
        bundle.putString(Constants.KEY_PIN_TAGS, pinTags);
        bundle.putString(Constants.KEY_PIN_COMMENT, pinComment);
        bundle.putString(Constants.MEDIA_TYPE, mediaType);

        Activity activity = (Activity) context;
        FragmentManager fm = activity.getFragmentManager();

        ChangePinFragment changePinFragment = new ChangePinFragment();
        changePinFragment.setArguments(bundle);
        changePinFragment.setListener(this);

        changePinFragment.show(fm, "ChangePinFragment");
    }
}
