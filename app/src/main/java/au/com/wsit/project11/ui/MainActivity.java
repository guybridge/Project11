package au.com.wsit.project11.ui;

import android.icu.text.TimeZoneFormat;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.SaveCallback;

import au.com.wsit.project11.R;
import au.com.wsit.project11.adapters.SectionsPagerAdapter;
import au.com.wsit.project11.api.ParseBoard;
import au.com.wsit.project11.ui.fragments.AddBoardFragment;

public class MainActivity extends AppCompatActivity implements AddBoardFragment.AddBoardCallback
{

    private static final String TAG = MainActivity.class.getSimpleName();
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private CoordinatorLayout mainLayout;

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

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);


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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings)
        {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // Result of the adding of the image
    @Override
    public void onSuccess(String boardName)
    {
        ParseBoard parseBoard = new ParseBoard();
        parseBoard.addBoard(boardName, new ParseBoard.ParseBoardCallback()
        {
            @Override
            public void onSuccess(String result)
            {
                Snackbar.make(mainLayout, "Added new board", Snackbar.LENGTH_LONG).show();
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
