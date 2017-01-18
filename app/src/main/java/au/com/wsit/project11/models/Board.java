package au.com.wsit.project11.models;

import java.util.ArrayList;

/**
 * Created by guyb on 3/01/17.
 */

public class Board
{
    private String boardTitle;
    private int ImageUrl;
    private ArrayList<Pin> boardPins;
    private String boardID;

    public String getBoardID()
    {
        return boardID;
    }

    public void setBoardID(String boardID)
    {
        this.boardID = boardID;
    }



    public String getBoardTitle()
    {
        return boardTitle;
    }

    public void setBoardTitle(String boardTitle)
    {
        this.boardTitle = boardTitle;
    }

    public int getImageUrl()
    {
        return ImageUrl;
    }

    public void setImageUrl(int imageUrl)
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
