package nexusDesk.models;

public class Notes {

    private int note_id;
    private int colour_id;
    private String title;
    private String content;

    public Notes(int note_id, int colour_id, String title, String content) {
        this.note_id = note_id;
        this.colour_id = colour_id;
        this.title = title;
        this.content = content;
    }

    public int getNote_id() {
        return note_id;
    }
    public void setNote_id(int note_id) {
        this.note_id = note_id;
    }

    public int getColour_id() {
        return colour_id;
    }
    public void setColour_id(int colour_id) {
        this.colour_id = colour_id;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
}
