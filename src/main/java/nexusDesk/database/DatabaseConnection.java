package nexusDesk.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
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

            System.out.println("Database operation successfull");
            return true;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public static void printTable(String tableName) {

        try (Connection conn = connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM " + tableName)) {

            int columnCount = rs.getMetaData().getColumnCount();

            System.out.println("\nTABLE: " + tableName);

            while (rs.next()) {
                for (int i = 1; i <= columnCount; i++) {
                    System.out.print(
                            rs.getMetaData().getColumnName(i) + ": " +
                                    rs.getString(i) + " | "
                    );
                }
                System.out.println();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args){
        printTable("tasks");
        printTable("projects");
        printTable("notes");
        printTable("bookmarks");
        printTable("colours");
    }
}
