package au.com.wsit.project11.ui.fragments;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import au.com.wsit.project11.R;
import au.com.wsit.project11.utils.Constants;

/**
 * Created by guyb on 21/01/17.
 */

public class ChangePinFragment extends DialogFragment
{
    private EditText pin;
    private EditText tags;
    private Button save;
    private PinCallback callback;
    private int adapterPosition;

    public interface PinCallback
    {
        void onChanged(int adapterPosition, String pinName, String pinID, String pinTags);
    }

    public void setListener(PinCallback callback)
    {
        this.callback = callback;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_change_pin, container, false);

        pin = (EditText) rootView.findViewById(R.id.pinName);
        tags = (EditText) rootView.findViewById(R.id.pinTags);
        save = (Button) rootView.findViewById(R.id.savePinButton);

        final Bundle bundle = getArguments();
        adapterPosition = bundle.getInt(Constants.KEY_BOARD_POSITION);

        // set the pin name and current tags
        pin.setText(bundle.getString(Constants.KEY_PIN_TITLE));
        tags.setText(bundle.getString(Constants.KEY_PIN_TAGS));


        save.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                animate();

                callback.onChanged
                        (adapterPosition, pin.getText().toString(),
                                bundle.getString(Constants.KEY_PIN_ID)
                                ,tags.getText().toString());

                dismiss();
            }
        });

        return rootView;

    }

    private void animate()
    {
        save.setScaleY(0);
        save.setScaleX(0);
        save.animate().scaleX(1).scaleY(1).start();
    }
}
