package au.com.wsit.project11.api;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import au.com.wsit.project11.models.Board;

/**
 * Created by guyb on 16/02/17.
 */

public class BoardHelper
{
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    public BoardHelper()
    {
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("boards");

    }

    public interface Callback
    {
        void onSuccess();
        void onFail(String errorMessage);
    }

    public void addBoard(String boardTitle, String imageUrl, final Callback callback)
    {
        // Create a board object
        Board board = new Board(boardTitle, imageUrl);
        databaseReference.child(boardTitle).setValue(board).addOnSuccessListener(new OnSuccessListener<Void>()
        {
            @Override
            public void onSuccess(Void aVoid)
            {
                callback.onSuccess();
            }
        }).addOnFailureListener(new OnFailureListener()
        {
            @Override
            public void onFailure(@NonNull Exception e)
            {
                callback.onFail(e.toString());
            }
        });

    }


}
