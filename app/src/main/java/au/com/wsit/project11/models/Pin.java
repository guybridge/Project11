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
    private String mediaType;

    public Pin()
    {

    }

    public Pin(String pinTitle, String pinComment, String pinTags, String mediaUrl, String mediaType)
    {
        this.pinTitle = pinTitle;
        this.pinComment = pinComment;
        this.pinTags = pinTags;
        this.mediaUrl = mediaUrl;
        this.mediaType = mediaType;
    }

    public String getMediaType()
    {
        return mediaType;
    }

    public void setMediaType(String mediaType)
    {
        this.mediaType = mediaType;
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
