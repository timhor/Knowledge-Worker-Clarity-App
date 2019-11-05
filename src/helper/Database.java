// THIS FILE ADAPTED FROM INFS2605 19T3 WEEK 5 TUTORIAL

package helper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Database {

    public static Connection conn;

	/* NO NEED TO CALL THIS METHOD OUTSIDE THE DATABASE CLASS */
    public static void openConnection() {
        if (conn == null) {
            try {
                conn = DriverManager.getConnection("jdbc:sqlite:Database.db");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

	/* Pass an SQL String into this method when querying the database */
    public ResultSet getResultSet(String sqlstatement) throws SQLException {
        openConnection();
        java.sql.Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sqlstatement);
        return rs;
    }

    public ResultSet getResultSetFromPreparedStatement(String sqlstatement, String[] parameters) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement(sqlstatement);
        for (int i = 0; i < parameters.length; i++) {
            stmt.setString(i + 1, parameters[i]);
        }
        ResultSet rs = stmt.executeQuery();
        // Note: don't have stmt.close() before return
        return rs;
    }

	/* Pass an SQL String into this method when inserting data into the database */
    public void insertStatement(String insert_query) throws SQLException {
        java.sql.Statement stmt = null;
        openConnection();
        try {
            System.out.println("Database opened successfully");
            stmt = conn.createStatement();
            System.out.println("The query was: " + insert_query);
            stmt.executeUpdate(insert_query);
            stmt.close();
            conn.commit();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        stmt.close();
    }

    public static void createEntriesTable() throws SQLException {
        openConnection();
        java.sql.Statement stmt = conn.createStatement();

        String createQuery = "CREATE TABLE IF NOT EXISTS entries" +
            "(id INTEGER PRIMARY KEY," +
            "desc TEXT NOT NULL," +
            "starttime TEXT NOT NULL," +
            "endtime TEXT NOT NULL," +
            "category INTEGER REFERENCES categories(id) ON DELETE SET NULL)";
        stmt.execute(createQuery);

        ArrayList<String> insertStatements = new ArrayList<String>();

        insertStatements.add("INSERT INTO entries (desc, starttime, endtime, category)" +
            "VALUES ('First entry', '2019-11-05T00:37:26.079Z', '2019-11-05T00:39:52.157Z', NULL)");
        insertStatements.add("INSERT INTO entries (desc, starttime, endtime, category)" +
            "VALUES ('Second entry', '2019-11-07T00:37:26.079Z', '2019-11-07T00:55:52.157Z', NULL)");
        insertStatements.add("INSERT INTO entries (desc, starttime, endtime, category)" +
            "VALUES ('Third entry', '2019-11-07T15:56:26.079Z', '2019-11-08T02:21:52.157Z', NULL)");

        for (String statement : insertStatements) {
            stmt.execute(statement);
        }

        stmt.close();
    }

}
