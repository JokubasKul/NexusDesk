package nexusDesk.database;

import nexusDesk.models.Tasks;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TaskDatabase {

    private Connection connection;

    public void createTask(int project_id, String name) throws SQLException {
        String sql = """
        INSERT INTO tasks (project_id, colour_id, name)
        VALUES (?, 26, ?)
        """;

        PreparedStatement statement = connection.prepareStatement(sql);

        statement.setInt(1, project_id);
        statement.setString(2, name);

        statement.executeUpdate();
    }

    public List<Tasks> getAllProjectTasks(int project_id) throws SQLException {

        List<Tasks> tasks = new ArrayList<>();

        String sql = "SELECT * FROM tasks WHERE project_id = ?";

        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, project_id);

        ResultSet result = statement.executeQuery();

        while (result.next()) {
            Tasks task = new Tasks(
                    result.getInt("task_id"),
                    result.getInt("project_id"),
                    result.getInt("colour_id"),
                    result.getString("name"),
                    result.getString("comment"),
                    result.getInt("isComplete")
            );

            tasks.add(task);
        }
        return tasks;
    }

    public void updateTaskName(int task_id, String name) throws SQLException {
        String sql = "UPDATE tasks SET name = ? WHERE task_id = ?";

        PreparedStatement statement = connection.prepareStatement(sql);

        statement.setString(1, name);
        statement.setInt(2, task_id);

        statement.executeUpdate();
    }

    public void updateTaskComment(int task_id, String comment) throws SQLException {
        String sql = "UPDATE tasks SET comment = ? WHERE task_id = ?";

        PreparedStatement statement = connection.prepareStatement(sql);

        statement.setString(1, comment);
        statement.setInt(2, task_id);

        statement.executeUpdate();
    }

    public void deleteTask(int task_id) throws SQLException {
        String sql = "DELETE FROM tasks WHERE task_id = ?";

        PreparedStatement statement = connection.prepareStatement(sql);

        statement.setInt(1, task_id);

        statement.executeUpdate();
    }
}
