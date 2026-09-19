package nexusDesk.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class OptionDatabase {


    public void updateTaskColour(int task_id, int colour_id) throws SQLException {
        String sql="UPDATE tasks SET colour_id = ? WHERE task_id = ?";

        try (Connection connection = DatabaseConnection.connect();
        PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, colour_id);
            statement.setInt(2, task_id);

            statement.executeUpdate();
        }
    }

    public void updateProjectColour(int project_id, int colour_id) throws SQLException {
        String sql="UPDATE projects SET colour_id = ? WHERE project_id = ?";

        try (Connection connection = DatabaseConnection.connect();
        PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, colour_id);
            statement.setInt(2, project_id);

            statement.executeUpdate();
        }
    }

    public void updateNoteColour(int note_id, int colour_id) throws SQLException {
        String sql="UPDATE notes SET colour_id = ? WHERE note_id = ?";

        try (Connection connection = DatabaseConnection.connect();
        PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, colour_id);
            statement.setInt(2, note_id);

            statement.executeUpdate();
        }
    }

}