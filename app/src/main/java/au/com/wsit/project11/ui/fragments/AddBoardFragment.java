package au.com.wsit.project11.ui.fragments;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import au.com.wsit.project11.R;
import au.com.wsit.project11.adapters.PreviewBoardAdapter;
import au.com.wsit.project11.utils.Constants;
import au.com.wsit.project11.utils.Generator;

/**
 *  This fragment is used to add a new board and also to edit the board attributes
 */

public class AddBoardFragment extends DialogFragment
{
    private static final String TAG = AddBoardFragment.class.getSimpleName();
    private CreateBoardCallback createBoardCallback;
    private Button saveButton;
    private EditText boardTitle;

    public interface CreateBoardCallback
    {
        void onAddBoardSuccess(String boardTitle);
        void onAddBoardFail(String result);
    }

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        createBoardCallback = (CreateBoardCallback) context;
    }

    // API 19
    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        createBoardCallback = (CreateBoardCallback) activity;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_add_board, container, false);

        boardTitle = (EditText) view.findViewById(R.id.boardNameEditText);
        saveButton = (Button) view.findViewById(R.id.saveBoardButton);

        saveButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                createBoardCallback.onAddBoardSuccess(boardTitle.getText().toString());
                dismiss();
            }
        });


        return view;
    }



}
