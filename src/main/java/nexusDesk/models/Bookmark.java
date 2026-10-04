package nexusDesk.models;

public class Bookmark {

    private int bookmark_id;
    private int colour_id;
    private String title;
    private String description;
    private int needsUrl;
    private String url;

    public Bookmark(int bookmark_id, int colour_id, String title, String description, int needsUrl, String url) {
        this.bookmark_id = bookmark_id;
        this.colour_id = colour_id;
        this.title = title;
        this.description = description;
        this.needsUrl = needsUrl;
        this.url = url;
    }

    public int getBookmarkId() {
        return bookmark_id;
    }
    public void setBookmarkId(int bookmark_id) {
        this.bookmark_id = bookmark_id;
    }

    public int getColourId() {
        return colour_id;
    }
    public void setColourId(int colour_id) {
        this.colour_id = colour_id;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public int getNeedsUrl() {
        return needsUrl;
    }
    public void setNeedsUrl(int needsUrl) {
        this.needsUrl = needsUrl;
    }

    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
}
