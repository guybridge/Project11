package au.com.wsit.project11.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import au.com.wsit.project11.R;
import au.com.wsit.project11.utils.Constants;

public class LargePinActivity extends AppCompatActivity
{
    private ImageView pinImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_large_pin);

        pinImageView = (ImageView) findViewById(R.id.pinImage);

        // Get the image Url
        Intent intent = getIntent();
        String imageUrl = intent.getStringExtra(Constants.KEY_PIN_IMAGE_URL);
        String pinName = intent.getStringExtra(Constants.KEY_PIN_TITLE);
        getSupportActionBar().setTitle(pinName);

        Picasso.with(this).load(imageUrl).into(pinImageView);
    }
}
