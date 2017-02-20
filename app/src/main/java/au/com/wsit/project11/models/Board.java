package au.com.wsit.project11.models;


import java.util.HashMap;
import java.util.Map;

/**
 * Created by guyb on 3/01/17.
 */

public class Board
{
    private String boardTitle;
    private String imageUrl;
    private Map<String, Pin> pins = new HashMap<>();

    public Board()
    {

    }

    public Map<String, Pin> getPins()
    {
        return pins;
    }

    public void setPins(Map<String, Pin> pins)
    {
        this.pins = pins;
    }



    public Board(String boardTitle, String imageUrl, Map<String, Pin> pins)
    {
        this.boardTitle = boardTitle;
        this.imageUrl = imageUrl;
        this.pins = pins;
    }

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
        return imageUrl;
    }

    public void setImageUrl(String imageUrl)
    {
        this.imageUrl = imageUrl;
    }

}
