package nexusDesk.models;

public class Projects {

    private int project_id;
    private int colour_id;
    private String title;

    public Projects(int project_id, int colour_id, String title) {
        this.project_id = project_id;
        this.colour_id = colour_id;
        this.title = title;
    }

    public int getProject_id() {
        return project_id;
    }
    public void setProject_id(int project_id) {
        this.project_id = project_id;
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
}
