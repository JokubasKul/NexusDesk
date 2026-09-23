package nexusDesk.database;

import nexusDesk.models.Colour;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OptionDatabase {

    public String getColourHex(int colourId) throws SQLException {

        String sql = "SELECT hex FROM colours WHERE colour_id = ?";

        try (Connection connection = DatabaseConnection.connect();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, colourId);

            try (ResultSet result = statement.executeQuery()) {
                if (result.next()) {
                    return result.getString("hex");
                }
            }
        }

        return "#e2e2e2";
    }

    public List<Colour> getAllColours() throws SQLException {

        List<Colour> colours = new ArrayList<>();

        String sql = "SELECT * FROM colours";

        try (Connection connection = DatabaseConnection.connect();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet result = statement.executeQuery()) {

            while (result.next()) {

                Colour colour = new Colour(
                        result.getInt("colour_id"),
                        result.getString("colour"),
                        result.getString("hex")
                );

                colours.add(colour);
            }
        }

        return colours;
    }

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