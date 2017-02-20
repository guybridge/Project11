package au.com.wsit.project11.ui;


import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import android.os.Bundle;
import android.util.Log;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

import au.com.wsit.project11.R;
import au.com.wsit.project11.adapters.BoardAdapter;
import au.com.wsit.project11.api.BoardHelper;
import au.com.wsit.project11.models.Board;
import au.com.wsit.project11.models.Pin;
import au.com.wsit.project11.ui.fragments.AddBoardFragment;
import au.com.wsit.project11.utils.Constants;
import au.com.wsit.project11.utils.FileHelper;
import au.com.wsit.project11.utils.Permissions;

public class MainActivity extends AppCompatActivity
        implements AddBoardFragment.CreateBoardCallback
{

    private static final String TAG = MainActivity.class.getSimpleName();
    private CoordinatorLayout mainLayout;
    private RecyclerView boardRecycler;
    private RecyclerView.LayoutManager layoutManager;
    private BoardAdapter boardAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private Uri mediaUri;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private ChildEventListener childEventListener;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        mainLayout = (CoordinatorLayout) findViewById(R.id.main_content);
        setSupportActionBar(toolbar);
        toolbar.setLogo(R.mipmap.ic_action_logo);
        getSupportActionBar().setTitle("");

        boardRecycler = (RecyclerView) findViewById(R.id.boardRecyclerView);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);

        layoutManager = new LinearLayoutManager(this);
        boardRecycler.setLayoutManager(layoutManager);
        boardAdapter = new BoardAdapter(this);
        boardRecycler.setAdapter(boardAdapter);

        // Firebase setup
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("boards");

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
        {
            @Override
            public void onRefresh()
            {
                getBoards();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        getBoards();

    }

    @Override
    protected void onPause()
    {
        super.onPause();
    }

    // Get the boards
    private void getBoards()
    {
        if(childEventListener == null)
        {
            childEventListener = new ChildEventListener()
            {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s)
                {
                    try
                    {
                        Board boardData = dataSnapshot.getValue(Board.class);
                        boardAdapter.add(boardData);
                        boardRecycler.scrollToPosition(0);

                        Log.i(TAG, "Got board: " + boardData.getBoardTitle());

                        for(Map.Entry<String,Pin> pin : boardData.getPins().entrySet())
                        {
                            Log.i(TAG, "pin name " + pin.getValue().getPinTitle());
                            Log.i(TAG, "pin media Url : " + pin.getValue().getMediaUrl());
                        }

                    }
                    catch(DatabaseException e)
                    {
                        Snackbar.make(mainLayout, "Add some boards", Snackbar.LENGTH_LONG).show();
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
            };

            databaseReference.addChildEventListener(childEventListener);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK)
        {
            if(requestCode == Constants.TAKE_PHOTO_REQUEST)
            {
                Log.i(TAG, "Took a photo");
                Log.i(TAG, "Uri is: " + mediaUri);
                Intent addPinIntent = new Intent(this, AddPinActivity.class);
                addPinIntent.setData(mediaUri);
                startActivity(addPinIntent);
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id)
        {
            case R.id.action_settings:
                break;
            case R.id.action_start_camera:
                // Add new pin
                startChooser();
                break;
            case R.id.action_add_board:
                android.app.FragmentManager fm = getFragmentManager();
                AddBoardFragment addBoardFragment = new AddBoardFragment();
                addBoardFragment.show(fm, "AddBoardFragment");
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void startChooser()
    {
        // First check permissions
        Permissions permissions = new Permissions(MainActivity.this);
        permissions.requestCameraPermissions(new Permissions.PermissionsCallback()
        {
            @Override
            public void onGranted()
            {
                // Now start the chooser.
                // Start a dialog to check which type of media to take
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setItems(R.array.camera_choices, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        switch(which)
                        {
                            case 0:
                                takePhoto();
                                break;
                            case 1:
                                // Take video
                                break;
                        }
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();

            }

            @Override
            public void onDenied()
            {
                Snackbar.make(mainLayout, "Permissions denied", Snackbar.LENGTH_LONG).show();
            }
        });


    }

    private void takePhoto()
    {
        FileHelper fileHelper = new FileHelper(this);
        mediaUri = fileHelper.getOutputMediaFileUri(Constants.MEDIA_TYPE_IMAGE);

        if (mediaUri == null)
        {
            Snackbar.make(mainLayout, "There was a problem accessing your external storage", Snackbar.LENGTH_LONG).show();
        }
        else
        {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, mediaUri);
            startActivityForResult(intent, Constants.TAKE_PHOTO_REQUEST);
        }
    }

    // Result of the adding of the image from the dialog fragment
    @Override
    public void onAddBoardSuccess(final String boardTitle)
    {
        BoardHelper boardHelper = new BoardHelper();
        boardHelper.addBoard(boardTitle, null, new BoardHelper.Callback()
        {
            @Override
            public void onSuccess()
            {
                Snackbar.make(mainLayout, "Added " + boardTitle, Snackbar.LENGTH_SHORT).show();
            }

            @Override
            public void onFail(String errorMessage)
            {

            }
        });
    }

    @Override
    public void onAddBoardFail(String result)
    {

    }
}
