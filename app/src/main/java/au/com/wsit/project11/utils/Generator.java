package au.com.wsit.project11.utils;

import java.util.Random;

import au.com.wsit.project11.R;

/**
 * Created by guyb on 3/01/17.
 */

public class Generator
{
    public static int getImage()
    {
        int[] images = {
                R.drawable.asset1,
                R.drawable.asset2,
                R.drawable.asset3,
                R.drawable.asset4,
                R.drawable.asset5,
                R.drawable.asset6,
                R.drawable.asset7 };

        Random random = new Random(images.length);
        return random.nextInt();

    }
}
