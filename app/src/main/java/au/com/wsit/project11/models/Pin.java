package au.com.wsit.project11.models;


/**
 * Created by guyb on 3/01/17.
 */

public class Pin
{
    private String mediaUrl;
    private String pinTitle;
    private String pinComment;
    private String pinTags;
    private String pinID;

    public String getPinID()
    {
        return pinID;
    }

    public void setPinID(String pinID)
    {
        this.pinID = pinID;
    }


    public String getMediaUrl()
    {
        return mediaUrl;
    }

    public void setMediaUrl(String mediaUrl)
    {
        this.mediaUrl = mediaUrl;
    }

    public String getPinTitle()
    {
        return pinTitle;
    }

    public void setPinTitle(String pinTitle)
    {
        this.pinTitle = pinTitle;
    }

    public String getPinComment()
    {
        return pinComment;
    }

    public void setPinComment(String pinComment)
    {
        this.pinComment = pinComment;
    }

    public String getPinTags()
    {
        return pinTags;
    }

    public void setPinTags(String pinTags)
    {
        this.pinTags = pinTags;
    }

}
