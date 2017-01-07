package au.com.wsit.project11.ui;

import android.content.Intent;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseFile;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;

import au.com.wsit.project11.R;
import au.com.wsit.project11.adapters.SimpleBoardAdapter;
import au.com.wsit.project11.api.ListBoard;
import au.com.wsit.project11.api.ParsePin;
import au.com.wsit.project11.models.Board;
import au.com.wsit.project11.utils.Constants;
import au.com.wsit.project11.utils.FileHelper;

public class AddPinActivity extends AppCompatActivity implements SimpleBoardAdapter.boardListener
{

    private static final String TAG = AddPinActivity.class.getSimpleName();
    private Uri mediaUrl;
    private ImageView image;
    private EditText pinTitle;
    private EditText pinComment;
    private EditText pinTags;
    private RecyclerView boardsRecycler;
    private LinearLayout linearLayout;
    private ProgressBar progressBar;
    private ArrayList<String> boardNames = new ArrayList<>();

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
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);
        boardsRecycler = (RecyclerView) findViewById(R.id.boardRecyclerView);
        boardsRecycler.setLayoutManager(new LinearLayoutManager(this));

        Intent intent = getIntent();
        mediaUrl = intent.getData();

        // Preview the image
        image = (ImageView) findViewById(R.id.pinImageView);
        Picasso.with(this).load(mediaUrl).into(image);

        showBoards();

    }

    private void showBoards()
    {
        ListBoard listBoard = new ListBoard();
        listBoard.getBoards(new ListBoard.ListBoardCallback()
        {
            @Override
            public void onSuccess(ArrayList<Board> boards)
            {
                SimpleBoardAdapter simpleBoardAdapter = new SimpleBoardAdapter(AddPinActivity.this, boards);
                boardsRecycler.setAdapter(simpleBoardAdapter);
            }

            @Override
            public void onFail(String result)
            {

            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();

        switch (id)
        {
            case R.id.action_save_pin:
                // TODO: Save to backend
                savePin();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void savePin()
    {
        // Get the editTextData
        String title = pinTitle.getText().toString().trim();
        String comment = pinComment.getText().toString().trim();
        String tags = pinTags.getText().toString().trim();

        // We need at least a title
        if(title.equals(""))
        {
                Snackbar.make(linearLayout, "The pin needs a title", Snackbar.LENGTH_LONG).show();
        }
        else
        {
            progressBar.setVisibility(View.VISIBLE);
            // Create a file object with the url

            // Reduce the file size for upload
            byte[] imageData = FileHelper.getByteArrayFromFile(this, mediaUrl);
            byte[] reducedImage = FileHelper.reduceImageForUpload(imageData);

            // create a parse file
            ParseFile pinImage = new ParseFile(reducedImage);

            // Create the parsePin object and then add the pin
            ParsePin parsePin = new ParsePin();
            parsePin.addPin(title, comment, tags, pinImage, boardNames, new ParsePin.ParsePinCallback()
            {

                @Override
                public void onSuccess(String result)
                {
                    progressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(AddPinActivity.this, "Successfully added new pin", Toast.LENGTH_LONG).show();
                    finish();
                }

                @Override
                public void onFail(String result)
                {
                    progressBar.setVisibility(View.INVISIBLE);
                    Snackbar.make(linearLayout, result, Snackbar.LENGTH_LONG).show();
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

    // The clicks from the add to board listener
    @Override
    public void onClick(String boardName)
    {
        Log.i(TAG, "Adding " + boardName + " to the pin.");
        boardNames.add(boardName);
    }
}
