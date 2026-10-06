package nexusDesk.database;

import java.sql.*;

/**
 * Class responsible for the connection to the SQLite database
 */
public class DatabaseConnection {

    private static final String URL = "jdbc:sqlite:NexusDesk.db";

    /**
     * Makes the connection to the database
     */
    public static Connection connect() throws SQLException {
        Connection conn = DriverManager.getConnection(URL);

        try (Statement stmt = conn.createStatement()) {
            stmt.execute("PRAGMA foreign_keys = ON;");
        }

        return conn;
    }

    /**
     * Executes a sql query, it is convenient then typed with double quotations
     * @param sql the sql query
     */
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

    /**
     * Prints all table's rows
     * @param tableName the table
     */
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

    /**
     * Prints out a schema of a table
     * @param tableName the table
     */
    public static void printSchema(String tableName) {

        try (Connection conn = connect();
             Statement stmt = conn.createStatement()) {

            System.out.println("\n================================");
            System.out.println("TABLE: " + tableName);
            System.out.println("================================");

            String sql = """
                SELECT sql
                FROM sqlite_master
                WHERE type = 'table'
                AND name = ?
                """;

            try (PreparedStatement statement = conn.prepareStatement(sql)) {

                statement.setString(1, tableName);

                try (ResultSet rs = statement.executeQuery()) {

                    if (rs.next()) {
                        System.out.println("\nCREATE STATEMENT:");
                        System.out.println(rs.getString("sql"));
                    }
                }
            }

            System.out.println("\nFOREIGN KEYS:");

            try (Statement foreignKeyStatement = conn.createStatement();
                 ResultSet rs = foreignKeyStatement.executeQuery(
                         "PRAGMA foreign_key_list(" + tableName + ")"
                 )) {

                boolean hasForeignKeys = false;

                while (rs.next()) {

                    hasForeignKeys = true;

                    System.out.println(
                            "  " + rs.getString("from") +
                                    " -> " +
                                    rs.getString("table") +
                                    "." +
                                    rs.getString("to") +
                                    " | ON DELETE: " +
                                    rs.getString("on_delete") +
                                    " | ON UPDATE: " +
                                    rs.getString("on_update")
                    );
                }

                if (!hasForeignKeys) {
                    System.out.println("  No foreign keys");
                }
            }

            System.out.println();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args){
        execute("""
                
                """);

    }
}
