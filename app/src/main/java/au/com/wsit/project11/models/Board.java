package au.com.wsit.project11.models;

import java.util.ArrayList;

/**
 * Created by guyb on 3/01/17.
 */

public class Board
{
    private String boardTitle;
    private String ImageUrl;
    private ArrayList<Pin> boardPins;

    public String getBoardTitle()
    {
        return boardTitle;
    }

    public void setBoardTitle(String boardTitle)
    {
        this.boardTitle = boardTitle;
    }

    public String getImageUrl()
    {
        return ImageUrl;
    }

    public void setImageUrl(String imageUrl)
    {
        ImageUrl = imageUrl;
    }

    public ArrayList<Pin> getBoardPins()
    {
        return boardPins;
    }

    public void setBoardPins(ArrayList<Pin> boardPins)
    {
        this.boardPins = boardPins;
    }
}
