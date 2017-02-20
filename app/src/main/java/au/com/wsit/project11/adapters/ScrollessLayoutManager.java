package au.com.wsit.project11.adapters;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;

/**
 * This custom layout manager stops the child recycler from thinking it's at the top so the swiperefresh still works.
 */

public class ScrollessLayoutManager extends LinearLayoutManager
{

    public ScrollessLayoutManager(Context context, int orientation, boolean reverseLayout)
    {
        super(context, orientation, reverseLayout);
    }

    @Override
    public boolean canScrollVertically()
    {
        return false;
    }


}
