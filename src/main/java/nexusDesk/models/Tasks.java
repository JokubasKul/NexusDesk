package nexusDesk.models;

public class Tasks {

    private int task_id;
    private int project_id;
    private int colour_id;
    private String name;
    private String comment;
    private int isComplete;

    public Tasks(int task_id, int project_id, int colour_id, String name, String comment, int isComplete) {
        this.task_id = task_id;
        this.project_id = project_id;
        this.colour_id = colour_id;
        this.name = name;
        this.comment = comment;
        this.isComplete = isComplete;
    }

    public int getTask_id() {
        return task_id;
    }
    public void setTask_id(int task_id) {
        this.task_id = task_id;
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

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getComment() {
        return comment;
    }
    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getIsComplete() {
        return isComplete;
    }
    public void setIsComplete(int complete) {
        isComplete = complete;
    }
}
