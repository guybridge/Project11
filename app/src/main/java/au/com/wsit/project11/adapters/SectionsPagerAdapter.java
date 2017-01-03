package au.com.wsit.project11.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ConcurrentModificationException;

import au.com.wsit.project11.ui.MainActivity;
import au.com.wsit.project11.ui.fragments.BoardFragment;
import au.com.wsit.project11.ui.fragments.PinFragment;

/**
 * Created by guyb on 3/01/17.
 */

public class SectionsPagerAdapter extends FragmentPagerAdapter
{

    private Context context;

    public SectionsPagerAdapter(Context context, FragmentManager fm)
    {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position)
    {
        switch (position)
        {
            case 0:
                return new PinFragment();
            case 1:
                return new BoardFragment();
        }

        return null;
    }

    @Override
    public int getCount()
    {
        // Show 3 total pages.
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position)
    {
        switch (position)
        {
            case 0:
                return "Pins";
            case 1:
                return "Boards";
        }
        return null;
    }
}
