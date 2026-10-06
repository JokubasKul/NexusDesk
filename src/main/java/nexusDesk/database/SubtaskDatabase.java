package nexusDesk.database;

import nexusDesk.models.Subtask;
import nexusDesk.models.Task;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * All the database operations of the subtasks table
 */
public class SubtaskDatabase {

    public void createSubtask(int task_id, String name) throws SQLException {
        String sql = """
        INSERT INTO subtasks (task_id, name) VALUES (?, ?)
        """;

        try (Connection connection = DatabaseConnection.connect();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, task_id);
            statement.setString(2, name);

            statement.executeUpdate();
        }
    }

    public List<Subtask> getAllSubtasks(int task_id) throws SQLException {

        List<Subtask> subtasks = new ArrayList<>();

        String sql = "SELECT * FROM subtasks WHERE task_id = ? AND isComplete=0";

        try (Connection connection = DatabaseConnection.connect();
             PreparedStatement statement = connection.prepareStatement(sql)){

            statement.setInt(1, task_id);

            try (ResultSet result = statement.executeQuery()){

                while (result.next()) {
                    Subtask subtask = new Subtask(
                            result.getInt("subtask_id"),
                            result.getInt("task_id"),
                            result.getString("name"),
                            result.getInt("isComplete")
                    );

                    subtasks.add(subtask);
                }
            }
        }
        return subtasks;
    }

    public void updateSubtaskName(int subtask_id, String name) throws SQLException {
        String sql = "UPDATE subtasks SET name = ? WHERE subtask_id = ?";

        try (Connection connection = DatabaseConnection.connect();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, name);
            statement.setInt(2, subtask_id);

            statement.executeUpdate();
        }
    }

    public void updateSubtaskCompletion(int subtask_id) throws SQLException{
        String sql="UPDATE subtasks SET isComplete = 1 WHERE subtask_id = ?";

        try (Connection connection = DatabaseConnection.connect();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, subtask_id);

            statement.executeUpdate();
        }
    }

    public void deleteSubtask(int subtask_id) throws SQLException {
        String sql = "DELETE FROM subtasks WHERE subtask_id = ?";

        try (Connection connection = DatabaseConnection.connect();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, subtask_id);

            statement.executeUpdate();
        }
    }
}
