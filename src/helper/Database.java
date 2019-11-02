// THIS FILE ADAPTED FROM INFS2605 19T3 WEEK 5 TUTORIAL

package helper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Database {

    public static Connection conn;

	/* NO NEED TO CALL THIS METHOD OUTSIDE THE DATABASE CLASS */
    public static void openConnection() {
        if (conn == null) {
            try {
                conn = DriverManager.getConnection("jdbc:sqlite:Database.db");
            } catch (SQLException ex) {
                ex.printStackTrace();
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

    public static void createLoginTable() throws SQLException {
        openConnection();
        java.sql.Statement stmt = conn.createStatement();

        String dropQuery = "DROP TABLE IF EXISTS users";
        stmt.execute(dropQuery);

        String createQuery = "CREATE TABLE users (username TEXT NOT NULL, password TEXT NOT NULL)";
        stmt.execute(createQuery);

        String insertQuery = "INSERT INTO users VALUES ('admin', 'admin')";
        stmt.execute(insertQuery);

        stmt.close();
    }

}
