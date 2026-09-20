package nexusDesk.database;

import nexusDesk.models.Task;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TaskDatabase {


    public void createTask(int project_id, String name) throws SQLException {
        String sql = """
        INSERT INTO tasks (project_id, name)
        VALUES (?, ?)
        """;

        try (Connection connection = DatabaseConnection.connect();
        PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, project_id);
            statement.setString(2, name);

            statement.executeUpdate();
        }
    }

    public List<Task> getAllProjectTasks(int project_id) throws SQLException {

        List<Task> tasks = new ArrayList<>();

        String sql = "SELECT * FROM tasks WHERE project_id = ? AND isComplete=0";

        try (Connection connection = DatabaseConnection.connect();
        PreparedStatement statement = connection.prepareStatement(sql)){

            statement.setInt(1, project_id);

            try (ResultSet result = statement.executeQuery()){

            while (result.next()) {
                Task task = new Task(
                        result.getInt("task_id"),
                        result.getInt("project_id"),
                        result.getInt("colour_id"),
                        result.getString("name"),
                        result.getString("comment"),
                        result.getInt("isComplete")
                );

                tasks.add(task);
            }
            }
        }
        return tasks;
    }

    public void updateTaskName(int task_id, String name) throws SQLException {
        String sql = "UPDATE tasks SET name = ? WHERE task_id = ?";

        try (Connection connection = DatabaseConnection.connect();
        PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, name);
            statement.setInt(2, task_id);

            statement.executeUpdate();
        }
    }

    public void updateTaskCompletion(int task_id) throws SQLException{
        String sql="UPDATE tasks SET isComplete = 1 WHERE task_id = ?";

        try (Connection connection = DatabaseConnection.connect();
        PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, task_id);

            statement.executeUpdate();
        }
    }

    public void updateTaskComment(int task_id, String comment) throws SQLException {
        String sql = "UPDATE tasks SET comment = ? WHERE task_id = ?";

        try (Connection connection = DatabaseConnection.connect();
        PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, comment);
            statement.setInt(2, task_id);

            statement.executeUpdate();
        }
    }

    public void deleteTask(int task_id) throws SQLException {
        String sql = "DELETE FROM tasks WHERE task_id = ?";

        try (Connection connection = DatabaseConnection.connect();
        PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, task_id);

            statement.executeUpdate();
        }
    }

}