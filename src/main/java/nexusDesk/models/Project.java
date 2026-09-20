package nexusDesk.models;

public class Project {

    private int project_id;
    private int colour_id;
    private String title;

    public Project(int project_id, int colour_id, String title) {
        this.project_id = project_id;
        this.colour_id = colour_id;
        this.title = title;
    }

    public int getProjectId() {
        return project_id;
    }
    public void setProjectId(int project_id) {
        this.project_id = project_id;
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
}
