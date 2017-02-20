package au.com.wsit.project11.ui.fragments;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Map;

import au.com.wsit.project11.R;
import au.com.wsit.project11.adapters.PreviewBoardAdapter;
import au.com.wsit.project11.models.Board;
import au.com.wsit.project11.models.Pin;
import au.com.wsit.project11.utils.Constants;
import au.com.wsit.project11.utils.Generator;

/**
 *  This fragment is used to add a new board and also to edit the board attributes
 */

public class AddBoardFragment extends DialogFragment implements PreviewBoardAdapter.OnItemClickListener
{
    private static final String TAG = AddBoardFragment.class.getSimpleName();
    private CreateBoardCallback createBoardCallback;
    private Button saveButton;
    private EditText boardTitle;

    private PreviewBoardAdapter previewBoardAdapter;
    private RecyclerView recyclerView;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    private String boardCoverUrl;


    // The callback from clicking on an image in the gridView
    @Override
    public void onItemClick(String resourceLocation)
    {
        Log.i(TAG, "Clicked on " + resourceLocation);
        boardCoverUrl = resourceLocation;

    }

    public interface CreateBoardCallback
    {
        void onAddBoardSuccess(String boardTitle, String coverUrl);
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
        recyclerView = (RecyclerView) view.findViewById(R.id.previewRecycler);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        previewBoardAdapter = new PreviewBoardAdapter(getActivity(), this);
        recyclerView.setAdapter(previewBoardAdapter);

        saveButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(boardTitle.getText().toString().equals(""))
                {
                    Toast.makeText(getActivity(), "Boards need a name", Toast.LENGTH_LONG).show();
                }
                else
                {
                    createBoardCallback.onAddBoardSuccess(boardTitle.getText().toString(), boardCoverUrl);
                    dismiss();
                }

            }
        });

        this.firebaseDatabase = FirebaseDatabase.getInstance();
        this.databaseReference = firebaseDatabase.getReference().child(Constants.BOARDS);

        getAllPins();


        return view;
    }

    public void getAllPins()
    {
        databaseReference.addChildEventListener(new ChildEventListener()
        {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s)
            {
                Board board = dataSnapshot.getValue(Board.class);

                for(Map.Entry<String,Pin> pins : board.getPins().entrySet())
                {
                    previewBoardAdapter.add(pins.getValue().getMediaUrl());
                    Log.i(TAG, "pin url added: " + pins.getValue().getMediaUrl());
                }


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



}
