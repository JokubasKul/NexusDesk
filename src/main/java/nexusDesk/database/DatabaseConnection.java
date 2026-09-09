package nexusDesk.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class DatabaseConnection {

    private static final String URL = "jdbc:sqlite:NexusDesk.db";

    public static Connection connect() throws Exception {
        Connection conn = DriverManager.getConnection(URL);

        try (Statement stmt = conn.createStatement()) {
            stmt.execute("PRAGMA foreign_keys = OFF;");
        }

        return conn;
    }

    public static boolean execute(String sql) {

        try (Connection conn = connect();
             Statement stmt = conn.createStatement()) {

            stmt.execute(sql);

            System.out.println("Database created successfully");
            return true;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public static void main(String[] args){
        execute("""
                CREATE TABLE tasks (
                    task_id INTEGER PRIMARY KEY AUTOINCREMENT,
                    project_id INTEGER,
                    colour_id INTEGER,
                    name TEXT NOT NULL,
                    comment TEXT,
                    FOREIGN KEY (colour_id) REFERENCES colours(colour_id),
                    FOREIGN KEY (project_id) REFERENCES projects(project_id)
                );
                """);
    }
}
