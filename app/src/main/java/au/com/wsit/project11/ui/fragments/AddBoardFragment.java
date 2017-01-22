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
    private UpdateBoardCallback updateBoardCallback;
    private EditText boardName;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private PreviewBoardAdapter previewBoardAdapter;
    private String currentBoardName;
    private String boardID;
    private int boardPosition;

    PreviewBoardAdapter.OnItemClickListener listener = new PreviewBoardAdapter.OnItemClickListener()
    {
        @Override
        public void onItemClick(int resourceLocation)
        {
            String name = boardName.getText().toString();
            if(!name.equals("")) // make sure the board name is not null
            {
                try
                {
                    if(currentBoardName.equals(""))
                    {
                        Log.i(TAG, "Current board name is null - this is a new board");
                    }
                    else
                    {
                        // Callback to the adapter
                        updateBoardCallback.onSuccess(boardPosition, boardID, boardName.getText().toString(), resourceLocation);
                        dismiss();
                    }
                }
                catch(NullPointerException e)
                {
                    // current Board name null we are going to call back to main activity
                    Log.i(TAG, "Current board name is null - this is a new board");
                    createBoardCallback.onSuccess(name, resourceLocation);
                    dismiss();
                }
            }
            else
            {
                Toast.makeText(getActivity(), "Give the board a title", Toast.LENGTH_LONG).show();
            }
        }
    };

    public interface CreateBoardCallback
    {
        void onSuccess(String boardName, int mediaUri);
        void onFail(String result);
    }

    public interface UpdateBoardCallback
    {
        void onSuccess(int boardPosition, String boardID, String boardName, int mediaUri);
        void onFail(String result);
    }

    public void setListener(UpdateBoardCallback listener)
    {
        updateBoardCallback = listener;
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

        boardName = (EditText) view.findViewById(R.id.boardNameEditText);
        recyclerView = (RecyclerView) view.findViewById(R.id.previewBoardsRecycler);
        layoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(layoutManager);

        Bundle bundle = getArguments();
        try
        {
            currentBoardName = bundle.getString(Constants.KEY_BOARD_NAME);
            boardID = bundle.getString(Constants.KEY_BOARD_ID);
            boardPosition = bundle.getInt(Constants.KEY_BOARD_POSITION);
        }
        catch(NullPointerException e)
        {
            Log.i(TAG, "Nothing in bundle");
        }

        if(currentBoardName != null)
        {
            boardName.setText(currentBoardName);
        }

        showPhotosCollection();

        return view;
    }


    private void showPhotosCollection()
    {
        previewBoardAdapter = new PreviewBoardAdapter(getActivity(), listener, Generator.getImages());
        recyclerView.setAdapter(previewBoardAdapter);
    }
}
