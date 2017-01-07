package au.com.wsit.project11.adapters;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.util.AttributeSet;

/**
 * Created by guyb on 7/01/17.
 */

public class ScrollessLayoutManager extends GridLayoutManager
{

    public ScrollessLayoutManager(Context context, int spanCount)
    {
        super(context, spanCount);
    }

    @Override
    public boolean canScrollVertically()
    {
        return false;
    }
}
