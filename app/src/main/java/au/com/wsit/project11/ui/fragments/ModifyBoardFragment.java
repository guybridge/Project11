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

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Map;

import au.com.wsit.project11.R;
import au.com.wsit.project11.adapters.PreviewBoardAdapter;
import au.com.wsit.project11.models.Board;
import au.com.wsit.project11.models.Pin;
import au.com.wsit.project11.utils.Constants;

/**
 *  This fragment is used to add a new board and also to edit the board attributes
 */

public class ModifyBoardFragment extends DialogFragment implements PreviewBoardAdapter.OnItemClickListener
{
    private static final String TAG = ModifyBoardFragment.class.getSimpleName();
    private EditText boardTitle;
    private Button saveButton;

    private PreviewBoardAdapter previewBoardAdapter;
    private RecyclerView recyclerView;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    private String boardCoverUrl;
    private String currentBoardName;
    private int boardIndex;
    private ArrayList<Board> boardsList = new ArrayList<>();

    // The callback from clicking on an image in the gridView
    @Override
    public void onItemClick(String resourceLocation)
    {
        Log.i(TAG, "Clicked on " + resourceLocation);
        boardCoverUrl = resourceLocation;

        saveChanges();
    }

    private void saveChanges()
    {
        if (boardTitle.getText().toString().equals(""))
        {
            Toast.makeText(getActivity(), "Board need a name", Toast.LENGTH_LONG).show();
        }
        else
        {
            for (int i = 0; i < boardsList.size(); i++)
            {
                if(currentBoardName.equals(boardsList.get(i).getBoardTitle()))
                {
                    Log.i(TAG, "board found at index: " + i);
                    boardIndex = i;
                }
            }
            // Update the board with it's name and it's pins
            final Board board = new Board(boardTitle.getText().toString(), boardCoverUrl, boardsList.get(boardIndex).getPins());

            // Remove the old board
            databaseReference.child(currentBoardName).removeValue().addOnSuccessListener(new OnSuccessListener<Void>()
            {
                @Override
                public void onSuccess(Void aVoid)
                {
                    // Add the new board
                    databaseReference.child(boardTitle.getText().toString()).setValue(board).addOnSuccessListener(new OnSuccessListener<Void>()
                    {
                        @Override
                        public void onSuccess(Void aVoid)
                        {
                            dismiss();
                        }
                    });
                }
            });

            }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_modify_board, container, false);

        boardTitle = (EditText) view.findViewById(R.id.boardNameEditText);
        recyclerView = (RecyclerView) view.findViewById(R.id.previewRecycler);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        previewBoardAdapter = new PreviewBoardAdapter(getActivity(), this);
        recyclerView.setAdapter(previewBoardAdapter);
        saveButton = (Button) view.findViewById(R.id.saveButton);

        // Get an instance to Firebase to display images from the collection
        this.firebaseDatabase = FirebaseDatabase.getInstance();
        this.databaseReference = firebaseDatabase.getReference().child(Constants.BOARDS);

        // Get all the pins
        getAllPins();

        try
        {
            Bundle bundle = getArguments();
            currentBoardName = bundle.getString(Constants.KEY_BOARD_NAME);
            boardTitle.setText(currentBoardName);
        }
        catch (NullPointerException e)
        {

        }

        saveButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                saveChanges();
            }
        });


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
                boardsList.add(board);

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
