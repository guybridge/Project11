package au.com.wsit.project11.ui;


import android.content.DialogInterface;
import android.content.Intent;
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

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseRelation;
import com.parse.SaveCallback;

import java.util.ArrayList;

import au.com.wsit.project11.R;
import au.com.wsit.project11.adapters.BoardAdapter;
import au.com.wsit.project11.api.ListBoard;
import au.com.wsit.project11.api.ParseBoard;
import au.com.wsit.project11.models.Board;
import au.com.wsit.project11.ui.fragments.AddBoardFragment;
import au.com.wsit.project11.utils.Constants;
import au.com.wsit.project11.utils.FileHelper;

public class MainActivity extends AppCompatActivity implements AddBoardFragment.AddBoardCallback
{

    private static final String TAG = MainActivity.class.getSimpleName();
    private CoordinatorLayout mainLayout;
    private RecyclerView boardRecycler;
    private RecyclerView.LayoutManager layoutManager;
    private BoardAdapter boardAdapter;
    private TextView errorTextView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private Uri mediaUri;

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
        errorTextView = (TextView) findViewById(R.id.errorTextView);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);

        layoutManager = new LinearLayoutManager(this);
        boardRecycler.setLayoutManager(layoutManager);
        boardAdapter = new BoardAdapter(this);
        boardRecycler.setAdapter(boardAdapter);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
        {
            @Override
            public void onRefresh()
            {
                getData();
            }
        });


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                android.app.FragmentManager fm = getFragmentManager();
                AddBoardFragment addBoardFragment = new AddBoardFragment();
                addBoardFragment.show(fm, "AddBoardFragment");
            }
        });


        getData();

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

    private void getData()
    {
        swipeRefreshLayout.setRefreshing(true);
        // Get data
        ListBoard listBoard = new ListBoard(this);
        listBoard.getBoards(new ListBoard.ListBoardCallback()
        {
            @Override
            public void onSuccess(ArrayList<Board> boards)
            {
                if(boards.size() == 0)
                {
                    Log.i(TAG, "No boards yet");
                    showEmptyView();
                }
                else
                {
                    swipeRefreshLayout.setRefreshing(false);
                    errorTextView.setVisibility(View.GONE);
                    boardAdapter.swap(boards);
                }

            }

            @Override
            public void onFail(String result)
            {
                toggleErrorView();
            }
        });

    }

    private void toggleErrorView()
    {
        runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                swipeRefreshLayout.setRefreshing(false);
                errorTextView.setVisibility(View.VISIBLE);
                errorTextView.setText(R.string.board_error);
            }
        });
    }

    private void showEmptyView()
    {
        runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                swipeRefreshLayout.setRefreshing(false);
                errorTextView.setVisibility(View.VISIBLE);
                errorTextView.setText(R.string.add_boards);
            }
        });
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
        }
        return super.onOptionsItemSelected(item);
    }

    private void startChooser()
    {
        // Start a dialog to check which type of media to take
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setItems(R.array.camera_choices, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                switch(which)
                {
                    case 0:
                        // Take photo
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
    public void onSuccess(String boardName)
    {
        ParseBoard parseBoard = new ParseBoard();
        parseBoard.addBoard(boardName, new ParseBoard.ParseBoardCallback()
        {
            @Override
            public void onSuccess(String result)
            {
                Snackbar.make(mainLayout, "Added new board " + result, Snackbar.LENGTH_LONG).show();
                getData();
            }

            @Override
            public void onFail(String result)
            {
                Snackbar.make(mainLayout, result, Snackbar.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onFail()
    {

    }
}
