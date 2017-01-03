package au.com.wsit.project11.utils;

import java.util.ArrayList;

/**
 * Created by guyb on 3/01/17.
 */

public class formatter
{
    // Sets the tags on the strings
    private static String setTags(ArrayList<String> tags)
    {
        ArrayList<String> taggedItems = new ArrayList<>();

        for (String tag : tags)
        {
            tag = "#" + tag;
            taggedItems.add(tag);
        }

        return taggedItems.toString();
    }
}
