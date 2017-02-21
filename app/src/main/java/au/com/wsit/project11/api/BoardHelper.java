package au.com.wsit.project11.api;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import au.com.wsit.project11.models.Board;
import au.com.wsit.project11.utils.Constants;


/**
 * Created by guyb on 16/02/17.
 */

public class BoardHelper
{
    private static final String TAG = BoardHelper.class.getSimpleName();
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
        if(imageUrl == null)
        {
            imageUrl = Constants.DEFAULT_COVER_IMAGE_URL;
        }

        Log.i(TAG, "Cover image URL is: " + imageUrl);

        Board board = new Board(boardTitle, imageUrl, null);
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
