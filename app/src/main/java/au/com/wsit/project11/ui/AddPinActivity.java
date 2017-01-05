package au.com.wsit.project11.ui;

import android.content.Intent;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import au.com.wsit.project11.R;
import au.com.wsit.project11.utils.Constants;

public class AddPinActivity extends AppCompatActivity
{

    private static final String TAG = AddPinActivity.class.getSimpleName();
    private Uri mediaUrl;
    private ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_pin);
        getSupportActionBar().setTitle("Add Pin");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        mediaUrl = intent.getData();

        // Preview the image
        image = (ImageView) findViewById(R.id.pinImageView);
        Picasso.with(this).load(mediaUrl).into(image);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();

        switch (id)
        {
            case R.id.action_add_pin:
                // TODO: Save to backend
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.add_pin_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }
}
