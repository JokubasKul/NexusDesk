package nexusDesk.database;

import nexusDesk.models.Project;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProjectDatabase {


    public void createProject(String title) throws SQLException{
        String sql="INSERT INTO projects(title) VALUES(?)";

        try (Connection connection = DatabaseConnection.connect();
        PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, title);

            statement.executeUpdate();
        }
    }

    public List<Project> getAllProjects() throws SQLException{

        List<Project> projects = new ArrayList<>();

        String sql="SELECT * FROM projects WHERE project_id>0";

        try (Connection connection = DatabaseConnection.connect();
        PreparedStatement statement = connection.prepareStatement(sql);
        ResultSet result = statement.executeQuery()) {

            while (result.next()) {
                Project project = new Project(
                        result.getInt("project_id"),
                        result.getInt("colour_id"),
                        result.getString("title")
                );
                projects.add(project);
            }
        }
        return projects;
    }

    public void updateProjectTitle(int project_id, String title) throws SQLException{
        String sql="UPDATE projects SET title = ? WHERE project_id = ?";

        try (Connection connection = DatabaseConnection.connect();
        PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, title);
            statement.setInt(2, project_id);

            statement.executeUpdate();
        }
    }

    public void deleteProject(int project_id) throws SQLException{
        String sql="DELETE FROM projects WHERE project_id = ?";

        try (Connection connection = DatabaseConnection.connect();
        PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, project_id);

            statement.executeUpdate();
        }
    }
}
