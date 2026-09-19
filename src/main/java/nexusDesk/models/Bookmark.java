package nexusDesk.models;

public class Bookmark {

    private int bookmark_id;
    private String title;
    private String description;
    private int needsUrl;
    private String url;

    public Bookmark(int bookmark_id, String title, String description, int needsUrl, String url) {
        this.bookmark_id = bookmark_id;
        this.title = title;
        this.description = description;
        this.needsUrl = needsUrl;
        this.url = url;
    }

    public int getBookmark_id() {
        return bookmark_id;
    }
    public void setBookmark_id(int bookmark_id) {
        this.bookmark_id = bookmark_id;
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

    public int isNeedsUrl() {
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
