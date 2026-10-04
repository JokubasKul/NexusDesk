package nexusDesk.models;

public class Subtask {

    private int subtask_id;
    private int task_id;
    private String name;
    private int isComplete;

    public Subtask(int subtask_id, int task_id, String name, int isComplete) {
        this.subtask_id = subtask_id;
        this.task_id = task_id;
        this.name = name;
        this.isComplete = isComplete;
    }

    public int getSubtaskId() {
        return subtask_id;
    }
    public void setSubtaskId(int subtask_id) {
        this.subtask_id = subtask_id;
    }

    public int getTaskId() {
        return task_id;
    }
    public void setTaskId(int task_id) {
        this.task_id = task_id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public int getIsComplete() {
        return isComplete;
    }
    public void setIsComplete(int isComplete) {
        this.isComplete = isComplete;
    }
}
