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
    public static ResultSet getResultSet(String sqlstatement) throws SQLException {
        openConnection();
        java.sql.Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sqlstatement);
        return rs;
    }

    public static ResultSet getResultSetFromPreparedStatement(String sqlstatement, String[] parameters) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement(sqlstatement);
        for (int i = 0; i < parameters.length; i++) {
            stmt.setString(i + 1, parameters[i]);
        }
        ResultSet rs = stmt.executeQuery();
        // Note: don't have stmt.close() before return
        return rs;
    }

	/* Pass an SQL String into this method when inserting data into the database */
    public static void insertStatement(String insert_query) throws SQLException {
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
            "category INTEGER REFERENCES categories(id) ON DELETE SET NULL," +
            "desc TEXT NOT NULL," +
            "date TEXT NOT NULL," +
            "starttime TEXT NOT NULL," +
            "endtime TEXT NOT NULL)";
        stmt.execute(createQuery);

        String checkExistingQuery = "SELECT COUNT(*) FROM entries";
        ResultSet rs = getResultSet(checkExistingQuery);
        int rowCount = rs.getInt(1);

        if (rowCount == 0) {
            ArrayList<String> insertStatements = new ArrayList<String>();

            insertStatements.add("INSERT INTO entries (category, desc, date, starttime, endtime)" +
                "VALUES (NULL, 'First entry', '2019-11-05', '11:30', '12:45')");
            insertStatements.add("INSERT INTO entries (category, desc, date, starttime, endtime)" +
                "VALUES (NULL, 'Second entry', '2019-11-07', '13:23', '15:12')");
            insertStatements.add("INSERT INTO entries (category, desc, date, starttime, endtime)" +
                "VALUES (NULL, 'Third entry', '2019-11-07', '18:38', '20:53')");

            for (String statement : insertStatements) {
                stmt.execute(statement);
            }
        }

        stmt.close();
    }

}
