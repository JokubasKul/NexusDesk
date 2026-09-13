package nexusDesk.database;

import nexusDesk.models.Bookmarks;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class bookmarkDatabase {

    private Connection connection;

    public void createBookmark(int bookmark_id, String title, String description, int needsUrl, String url) throws SQLException{
        if(needsUrl==0){
            String sql="INSERT INTO bookmarks(bookmark_id, title, description) VALUES(?, ?, ?)";

            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setInt(1, bookmark_id);
            statement.setString(2, title);
            statement.setString(3, description);

            statement.executeUpdate();

        } else if(needsUrl==1){
            String sql="INSERT INTO bookmarks(bookmark_id, title, description, needsUrl, url) VALUES(?, ?, ?, ?, ?)";

            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setInt(1, bookmark_id);
            statement.setString(2, title);
            statement.setString(3, description);
            statement.setInt(4, needsUrl);
            statement.setString(5, url);

            statement.executeUpdate();
        }
    }

    public List<Bookmarks> getAllBookmarks() throws SQLException{

        List<Bookmarks> bookmarks = new ArrayList<>();

        String sql="SELECT * FROM bookmarks";

        PreparedStatement statement = connection.prepareStatement(sql);

        ResultSet result = statement.executeQuery();

        while (result.next()) {
            Bookmarks bookmark = new Bookmarks(
                    result.getInt("bookmark_id"),
                    result.getString("title"),
                    result.getString("description"),
                    result.getInt("needsUrl"),
                    result.getString("url")
            );
            bookmarks.add(bookmark);
        }
        return bookmarks;
    }

    public void updateBookmarkTitle(int bookmark_id, String title) throws SQLException{
        String sql="UPDATE bookmarks SET title = ? WHERE bookmark_id = ?";

        PreparedStatement statement = connection.prepareStatement(sql);

        statement.setString(1, title);
        statement.setInt(2, bookmark_id);

        statement.executeUpdate();
    }

    public void updateBookmarkDescription(int bookmark_id, String description) throws SQLException{
        String sql="UPDATE bookmarks SET description = ? WHERE bookmark_id = ?";

        PreparedStatement statement = connection.prepareStatement(sql);

        statement.setString(1, description);
        statement.setInt(2, bookmark_id);

        statement.executeUpdate();
    }

    public void updateBookmarkUrl(int bookmark_id, int needsUrl, String url) throws SQLException{
        if(needsUrl==0){
            String sql="UPDATE bookmarks SET needsUrl = ?, url = ? WHERE bookmark_id = ?";

            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setInt(1, needsUrl);
            statement.setString(2, url);
            statement.setInt(3, bookmark_id);

            statement.executeUpdate();

        } else if(needsUrl==1){
            String sql="UPDATE bookmarks SET url = ? WHERE bookmark_id = ?";

            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setString(1, url);
            statement.setInt(2, bookmark_id);

            statement.executeUpdate();
        }
    }

    public void deleteBookmark(int bookmark_id) throws SQLException{
        String sql="DELETE FROM bookmarks WHERE bookmark_id = ?";

        PreparedStatement statement = connection.prepareStatement(sql);

        statement.setInt(1, bookmark_id);

        statement.executeUpdate();
    }
}
