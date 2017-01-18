package au.com.wsit.project11.utils;

import java.util.Random;

import au.com.wsit.project11.R;


/**
 * Created by guyb on 3/01/17.
 */

public class Generator
{
    static int[] images = {
            R.drawable.asset1,
            R.drawable.asset2,
            R.drawable.asset3,
            R.drawable.asset4,
            R.drawable.asset5,
            R.drawable.asset6,
            R.drawable.asset7,
            R.drawable.asset8,
            R.drawable.asset9,
            R.drawable.asset10,
            R.drawable.asset11,
            R.drawable.asset12,
            R.drawable.asset13,
            R.drawable.asset14,
            R.drawable.asset15,
            R.drawable.asset16,
            R.drawable.asset17,
            R.drawable.asset18,
            R.drawable.asset19,
            R.drawable.asset20 };

    // Get all the image assets
    public static int[] getImages()
    {
        return images;
    }

    // Gets a random image
    public static int getRandomImage()
    {
        Random random = new Random();
        return images[random.nextInt(images.length)];
    }
}
