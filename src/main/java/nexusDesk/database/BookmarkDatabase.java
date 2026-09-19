package nexusDesk.database;

import nexusDesk.models.Bookmark;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BookmarkDatabase {


    public void createBookmark(String title, String description, int needsUrl, String url) throws SQLException{
        String sql="INSERT INTO bookmarks(title, description, needsUrl, url) VALUES(?, ?, ?, ?)";

        try (Connection connection = DatabaseConnection.connect();
        PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, title);
            statement.setString(2, description);
            statement.setInt(3, needsUrl);
            statement.setString(4, url);

            statement.executeUpdate();
        }

    }

    public List<Bookmark> getAllBookmarks() throws SQLException{

        List<Bookmark> bookmarks = new ArrayList<>();

        String sql="SELECT * FROM bookmarks";

        try (Connection connection = DatabaseConnection.connect();
        PreparedStatement statement = connection.prepareStatement(sql);
        ResultSet result = statement.executeQuery()) {

            while (result.next()) {
                Bookmark bookmark = new Bookmark(
                        result.getInt("bookmark_id"),
                        result.getString("title"),
                        result.getString("description"),
                        result.getInt("needsUrl"),
                        result.getString("url")
                );
                bookmarks.add(bookmark);
            }
        }
        return bookmarks;
    }

    public void updateBookmarkTitle(int bookmark_id, String title) throws SQLException{
        String sql="UPDATE bookmarks SET title = ? WHERE bookmark_id = ?";

        try (Connection connection = DatabaseConnection.connect();
        PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, title);
            statement.setInt(2, bookmark_id);

            statement.executeUpdate();
        }
    }

    public void updateBookmarkDescription(int bookmark_id, String description) throws SQLException{
        String sql="UPDATE bookmarks SET description = ? WHERE bookmark_id = ?";

        try (Connection connection = DatabaseConnection.connect();
        PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, description);
            statement.setInt(2, bookmark_id);

            statement.executeUpdate();
        }
    }

    public void updateBookmarkUrl(int bookmark_id, int needsUrl, String url) throws SQLException{
        if(needsUrl==0){
            String sql="UPDATE bookmarks SET needsUrl = ?, url = ? WHERE bookmark_id = ?";

            try (Connection connection = DatabaseConnection.connect();
            PreparedStatement statement = connection.prepareStatement(sql)) {

                statement.setInt(1, needsUrl);
                statement.setString(2, url);
                statement.setInt(3, bookmark_id);

                statement.executeUpdate();
            }

        } else if(needsUrl==1){
            String sql="UPDATE bookmarks SET url = ? WHERE bookmark_id = ?";

            try (Connection connection = DatabaseConnection.connect();
            PreparedStatement statement = connection.prepareStatement(sql)) {

                statement.setString(1, url);
                statement.setInt(2, bookmark_id);

                statement.executeUpdate();
            }
        }
    }

    public void deleteBookmark(int bookmark_id) throws SQLException{
        String sql="DELETE FROM bookmarks WHERE bookmark_id = ?";

        try (Connection connection = DatabaseConnection.connect();
        PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, bookmark_id);

            statement.executeUpdate();
        }
    }

}