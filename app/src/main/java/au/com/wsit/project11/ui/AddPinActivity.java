package au.com.wsit.project11.ui;

import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.parse.ParseFile;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import au.com.wsit.project11.R;
import au.com.wsit.project11.adapters.SimpleBoardAdapter;
import au.com.wsit.project11.models.Board;
import au.com.wsit.project11.models.Pin;
import au.com.wsit.project11.utils.Constants;
import au.com.wsit.project11.utils.FileHelper;

public class AddPinActivity extends AppCompatActivity implements SimpleBoardAdapter.BoardListener
{

    private static final String TAG = AddPinActivity.class.getSimpleName();
    private Uri mediaUrl;
    private ImageView image;
    private EditText pinTitle;
    private EditText pinComment;
    private EditText pinTags;
    private RecyclerView boardsRecycler;
    private SimpleBoardAdapter simpleBoardAdapter;
    private LinearLayout linearLayout;
    private ArrayList<String> boardNames = new ArrayList<>();

    // Firebase Storage
    private FirebaseStorage firebaseStorage;
    private StorageReference pinStorageReference;

    // Firebase Database
    private DatabaseReference boardsDatabaseReference;
    private FirebaseDatabase firebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_pin);
        getSupportActionBar().setTitle("Add Pin");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        pinTitle = (EditText) findViewById(R.id.pinTitle);
        pinComment = (EditText) findViewById(R.id.pinComment);
        pinTags = (EditText) findViewById(R.id.pinTags);

        linearLayout = (LinearLayout) findViewById(R.id.activity_add_pin);
        boardsRecycler = (RecyclerView) findViewById(R.id.boardRecyclerView);
        boardsRecycler.setLayoutManager(new LinearLayoutManager(this));
        simpleBoardAdapter = new SimpleBoardAdapter(AddPinActivity.this);
        simpleBoardAdapter.setListener(this);
        boardsRecycler.setAdapter(simpleBoardAdapter);

        Intent intent = getIntent();
        mediaUrl = intent.getData();

        // Preview the image
        image = (ImageView) findViewById(R.id.pinImageView);
        Picasso.with(this).load(mediaUrl).into(image);

        firebaseStorage = FirebaseStorage.getInstance();
        pinStorageReference = firebaseStorage.getReference().child(Constants.PINS);

        firebaseDatabase = FirebaseDatabase.getInstance();
        boardsDatabaseReference = firebaseDatabase.getReference().child(Constants.BOARDS);

        boardsDatabaseReference.addChildEventListener(new ChildEventListener()
        {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s)
            {
                Board board = dataSnapshot.getValue(Board.class);
                simpleBoardAdapter.add(board);
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

    private void savePin()
    {
        // Get the editText data
        final String title = pinTitle.getText().toString().trim();
        final String comment = pinComment.getText().toString().trim();
        final String tags = pinTags.getText().toString().trim();

        // We need at least a title
        if(title.equals(""))
        {
                Snackbar.make(linearLayout, "The pin needs a title", Snackbar.LENGTH_LONG).show();
        }
        else
        {
            // Reduce the file size for upload
            byte[] imageData = FileHelper.getByteArrayFromFile(this, mediaUrl);
            byte[] reducedImage = FileHelper.reduceImageForUpload(imageData);

            // Save the file
            StorageReference photoReference = pinStorageReference.child(mediaUrl.getLastPathSegment());
            photoReference.putBytes(reducedImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>()
            {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
                {
                    Log.i(TAG, "Download URL is: " + taskSnapshot.getDownloadUrl());

                    // Once save then link the photo the boards that were selected
                    for(String boardName : boardNames)
                    {
                        Pin pin = new Pin(title, comment, tags, taskSnapshot.getDownloadUrl().toString());
                        boardsDatabaseReference.child(boardName).child(Constants.PINS).push().setValue(pin);
                    }

                    Snackbar.make(linearLayout, "Saved pin", Snackbar.LENGTH_LONG).show();
                    finish();

                }
            });

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.add_pin_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();

        switch (id)
        {
            case R.id.action_save_pin:
                savePin();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    // Callback from the boards onClick
    @Override
    public void onClick(String boardName)
    {
        boardNames.add(boardName);
    }
}
