package au.com.wsit.project11.ui.fragments;

import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import au.com.wsit.project11.R;
import au.com.wsit.project11.utils.Constants;
import au.com.wsit.project11.utils.FileHelper;

import static android.app.Activity.RESULT_OK;

/**
 * Created by guyb on 4/01/17.
 */

public class AddBoardFragment extends DialogFragment
{
    private static final String TAG = AddBoardFragment.class.getSimpleName();
    private AddBoardCallback callback;
    private EditText boardName;
    private ImageView photoImage;
    private int TAKE_PHOTO_REQUEST_CODE = 9001;
    private Uri mediaUri;

    public interface AddBoardCallback
    {
        void onSuccess(String boardName);
        void onFail();
    }

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        callback = (AddBoardCallback) context;

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_add_board, container, false);

        boardName = (EditText) view.findViewById(R.id.boardNameEditText);
        photoImage = (ImageView) view.findViewById(R.id.takePhotoImageView);

        photoImage.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // Animate the button click
                photoImage.setScaleY(0);
                photoImage.setScaleX(0);
                photoImage.animate().scaleX(1).scaleY(1).start();

                // Start an Intent to take a photo
               // Intent takePhotoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE, )

                String name = boardName.getText().toString().trim();
                if(name != null)
                {
                    callback.onSuccess(name);
                    dismiss();

                }
                else
                {
                    dismiss();
                }



            }
        });

        return view;
    }

    // Helper
    private void takePhoto()
    {
        FileHelper fileHelper = new FileHelper(getActivity());
        mediaUri = fileHelper.getOutputMediaFileUri(Constants.MEDIA_TYPE_IMAGE);

        if (mediaUri == null)
        {
            Toast.makeText(getActivity(), "Problem accessing your external storage", Toast.LENGTH_LONG).show();
        }
        else
        {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, mediaUri);
            startActivityForResult(intent, Constants.TAKE_PHOTO_REQUEST);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK)
        {
            if(requestCode == TAKE_PHOTO_REQUEST_CODE)
            {

            }
        }
    }
}
