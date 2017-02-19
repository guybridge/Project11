package au.com.wsit.project11.models;


/**
 * Created by guyb on 3/01/17.
 */

public class Board
{
    private String boardTitle;
    private String imageUrl;

    public Board()
    {

    }

    public Board(String boardTitle, String imageUrl)
    {
        this.boardTitle = boardTitle;
        this.imageUrl = imageUrl;
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
