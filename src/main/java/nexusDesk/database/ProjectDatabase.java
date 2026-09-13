package nexusDesk.database;

import nexusDesk.models.Projects;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProjectDatabase {

    private Connection connection;

    public void createProject(int project_id, String title) throws SQLException{
        String sql="INSERT INTO projects(project_id, colour_id, title) VALUES(?, 26, ?)";

        PreparedStatement statement = connection.prepareStatement(sql);

        statement.setInt(1, project_id);
        statement.setString(2, title);

        statement.executeUpdate();
    }

    public List<Projects> getAllProjects() throws SQLException{

        List<Projects> projects = new ArrayList<>();

        String sql="SELECT * FROM projects";

        PreparedStatement statement = connection.prepareStatement(sql);

        ResultSet result = statement.executeQuery();

        while (result.next()) {
            Projects project = new Projects(
                   result.getInt("project_id"),
                   result.getInt("colour_id"),
                   result.getString("title")
            );
            projects.add(project);
        }
        return projects;
    }

    public void updateProjectTitle(int project_id, String title) throws SQLException{
        String sql="UPDATE projects SET title = ? WHERE project_id = ?";

        PreparedStatement statement = connection.prepareStatement(sql);

        statement.setString(1, title);
        statement.setInt(2, project_id);

        statement.executeUpdate();
    }

    public void deleteProject(int project_id) throws SQLException{
        String sql="DELETE FROM projects WHERE project_id = ?";

        PreparedStatement statement = connection.prepareStatement(sql);

        statement.setInt(1, project_id);

        statement.executeUpdate();
    }
}
