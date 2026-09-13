package nexusDesk.database;

import nexusDesk.models.Notes;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class NoteDatabase {

    private Connection connection;

    public void createNote(int note_id, String title) throws SQLException {

        String sql="INSERT INTO notes(note_id, title, colour_id) VALUES (?, ?, 26)";

        PreparedStatement statement = connection.prepareStatement(sql);

        statement.setInt(1, note_id);
        statement.setString(2, title);

        statement.executeUpdate();
    }

    public List<Notes> getAllNotes() throws SQLException{

        List<Notes> notes = new ArrayList<>();

        String sql="SELECT * FROM notes";

        PreparedStatement statement = connection.prepareStatement(sql);

        ResultSet result = statement.executeQuery();

        while (result.next()) {
            Notes note = new Notes(
                    result.getInt("note_id"),
                    result.getInt("colour_id"),
                    result.getString("title"),
                    result.getString("content")
            );
            notes.add(note);
        }
        return notes;
    }

    public void updateNoteTitle(int note_id, String title) throws SQLException {
        String sql="UPDATE notes SET title = ? WHERE note_id = ?";

        PreparedStatement statement = connection.prepareStatement(sql);

        statement.setString(1, title);
        statement.setInt(2, note_id);

        statement.executeUpdate();
    }

    public void updateNoteContent(int note_id, String content) throws SQLException {
        String sql="UPDATE notes SET content = ? WHERE note_id = ?";

        PreparedStatement statement = connection.prepareStatement(sql);

        statement.setString(1, content);
        statement.setInt(2, note_id);

        statement.executeUpdate();
    }

    public void deleteNote(int note_id) throws SQLException{
        String sql="DELETE FROM notes WHERE note_id = ?";

        PreparedStatement statement = connection.prepareStatement(sql);

        statement.setInt(1, note_id);

        statement.executeUpdate();
    }
}
