// THIS FILE ADAPTED FROM INFS2605 19T3 WEEK 5 TUTORIAL

package helper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
        Statement stmt = conn.createStatement();
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

    public static void updateFromPreparedStatement(String updateQuery, String[] parameters) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement(updateQuery);
        for (int i = 0; i < parameters.length; i++) {
            stmt.setString(i + 1, parameters[i]);
        }
        stmt.executeUpdate();
        stmt.close();
    }

	/* Pass an SQL String into this method when inserting data into the database */
    public static void insertStatement(String insertQuery) throws SQLException {
        Statement stmt = null;
        openConnection();
        try {
            System.out.println("Database opened successfully");
            stmt = conn.createStatement();
            System.out.println("The query was: " + insertQuery);
            stmt.executeUpdate(insertQuery);
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
        Statement stmt = conn.createStatement();

        String createQuery = "CREATE TABLE IF NOT EXISTS entries" +
            "(id INTEGER PRIMARY KEY," +
            "category INTEGER REFERENCES categories(id) ON DELETE SET NULL," +
            "description TEXT NOT NULL," +
            "date TEXT NOT NULL," +
            "starttime TEXT NOT NULL," +
            "endtime TEXT NOT NULL)";
        stmt.execute(createQuery);

        String checkExistingQuery = "SELECT COUNT(*) FROM entries";
        ResultSet rs = getResultSet(checkExistingQuery);
        int rowCount = rs.getInt(1);

        if (rowCount == 0) {
            ArrayList<String> insertStatements = new ArrayList<String>();

            insertStatements.add("INSERT INTO entries (category, description, date, starttime, endtime) " +
                "VALUES (7,'First entry','2019-11-18','11:30','12:45')");
            insertStatements.add("INSERT INTO entries (category, description, date, starttime, endtime) " +
                "VALUES (7,'Second entry','2019-11-23','13:23','15:12')");
            insertStatements.add("INSERT INTO entries (category, description, date, starttime, endtime) " +
                "VALUES (7,'Third entry','2019-11-23','18:38','20:53')");
            insertStatements.add("INSERT INTO entries (category, description, date, starttime, endtime) " +
                "VALUES (7,'4th entry','2019-11-22','10:10','13:13')");
            insertStatements.add("INSERT INTO entries (category, description, date, starttime, endtime) " +
                "VALUES (1,'study for test 1 ','2019-11-19','12:00','15:00')");
            insertStatements.add("INSERT INTO entries (category, description, date, starttime, endtime) " +
                "VALUES (1,'study for test 2','2019-11-20','13:00','16:30')");
            insertStatements.add("INSERT INTO entries (category, description, date, starttime, endtime) " +
                "VALUES (1,'study for test 3','2019-11-21','19:00','20:30')");
            insertStatements.add("INSERT INTO entries (category, description, date, starttime, endtime) " +
                "VALUES (2,'work shift 1','2019-11-23','10:00','15:00')");
            insertStatements.add("INSERT INTO entries (category, description, date, starttime, endtime) " +
                "VALUES (2,'work shift 2','2019-11-24','09:10','16:30')");
            insertStatements.add("INSERT INTO entries (category, description, date, starttime, endtime) " +
                "VALUES (2,'work shift 3','2019-11-22','12:00','19:00')");
            insertStatements.add("INSERT INTO entries (category, description, date, starttime, endtime) " +
                "VALUES (3,'meditation','2019-10-23','10:00','11:00')");
            insertStatements.add("INSERT INTO entries (category, description, date, starttime, endtime) " +
                "VALUES (6,'hanging with friends','2019-11-24','13:00','20:30')");
            insertStatements.add("INSERT INTO entries (category, description, date, starttime, endtime) " +
                "VALUES (3,'out to see a movie','2019-11-18','14:00','17:00')");
            insertStatements.add("INSERT INTO entries (category, description, date, starttime, endtime) " +
                "VALUES (4,'Travel to work','2019-11-16','07:30','09:00')");
            insertStatements.add("INSERT INTO entries (category, description, date, starttime, endtime) " +
                "VALUES (2,'Morning work shift','2019-11-17','09:00','12:00')");
            insertStatements.add("INSERT INTO entries (category, description, date, starttime, endtime) " +
                "VALUES (5,'Jog to the beach','2019-11-19','12:00','13:00')");
            insertStatements.add("INSERT INTO entries (category, description, date, starttime, endtime) " +
                "VALUES (3,'Lunch with Anne','2019-11-19','13:00','14:00')");
            insertStatements.add("INSERT INTO entries (category, description, date, starttime, endtime) " +
                "VALUES (3,'Shopping at Westfield','2019-11-20','14:00','16:00')");
            insertStatements.add("INSERT INTO entries (category, description, date, starttime, endtime) " +
                "VALUES (4,'Heading home','2019-11-20','16:00','17:00')");
            insertStatements.add("INSERT INTO entries (category, description, date, starttime, endtime) " +
                "VALUES (1,'Java Assignment','2019-11-21','17:00','20:30')");
            insertStatements.add("INSERT INTO entries (category, description, date, starttime, endtime) " +
                "VALUES (3,'Meditate','2019-11-22','20:30','21:45')");
            insertStatements.add("INSERT INTO entries (category, description, date, starttime, endtime) " +
                "VALUES (1,'Review Bobs PR','2019-11-23','21:45','22:30')");

            for (String statement : insertStatements) {
                stmt.execute(statement);
            }
        }

        stmt.close();
    }

    public static void createTasksTable() throws SQLException {
        openConnection();
        Statement stmt = conn.createStatement();
        String createQuery = "CREATE TABLE IF NOT EXISTS tasks" +
            "(taskId INTEGER PRIMARY KEY," +
            "title TEXT NOT NULL," +
            "description TEXT NOT NULL," +
            "priority TEXT NOT NULL," +
            "dueDate TEXT NOT NULL," +
            "doDate TEXT NOT NULL," +
            "completed INTEGER NOT NULL)";
        stmt.execute(createQuery);

        String checkExistingQuery = "SELECT COUNT(*) FROM tasks";
        ResultSet rs = getResultSet(checkExistingQuery);
        int rowCount = rs.getInt(1);

        if (rowCount == 0) {
            ArrayList<String> insertStatements = new ArrayList<String>();

            insertStatements.add("INSERT INTO tasks (title, description, priority, dueDate, doDate, completed) " +
                "VALUES ('INFS2605 Project','Java assignment','90','2019-11-18','2019-11-18',0)");
            insertStatements.add("INSERT INTO tasks (title, description, priority, dueDate, doDate, completed) " +
                "VALUES ('INFS3873 Assignment 1','Linear regression','80','2019-11-18','2019-11-18',1)");
            insertStatements.add("INSERT INTO tasks (title, description, priority, dueDate, doDate, completed) " +
                "VALUES ('INFS3873 Assignment 2','Logistic regression','80','2019-11-19','2019-11-19',0)");
            insertStatements.add("INSERT INTO tasks (title, description, priority, dueDate, doDate, completed) " +
                "VALUES ('INFS3873 Group Project','Capital Bike Share','60','2019-11-19','2019-11-19',0)");
            insertStatements.add("INSERT INTO tasks (title, description, priority, dueDate, doDate, completed) " +
                "VALUES ('INFS3604 Group Assignment','Business process management','50','2019-11-20','2019-11-18',0)");
            insertStatements.add("INSERT INTO tasks (title, description, priority, dueDate, doDate, completed) " +
                "VALUES ('INFS2605 Labs','Intermediate Java','20','2019-11-20','2019-11-16',1)");
            insertStatements.add("INSERT INTO tasks (title, description, priority, dueDate, doDate, completed) " +
                "VALUES ('Prepare for tutoring','Read over notes and create worksheets','30','2019-11-23','2019-11-19',0)");
            insertStatements.add("INSERT INTO tasks (title, description, priority, dueDate, doDate, completed) " +
                "VALUES ('Gym','Chest and triceps','40','2019-11-24','2019-11-23',0)");
            insertStatements.add("INSERT INTO tasks (title, description, priority, dueDate, doDate, completed) " +
                "VALUES ('Gym','Back and biceps','40','2019-11-23','2019-11-22',0)");
            insertStatements.add("INSERT INTO tasks (title, description, priority, dueDate, doDate, completed) " +
                "VALUES ('Gym','Legs and shoulders','40','2019-11-24','2019-11-22',0)");
            insertStatements.add("INSERT INTO tasks (title, description, priority, dueDate, doDate, completed) " +
                "VALUES ('Gym','Everything else','30','2019-11-20','2019-11-18',0)");

            for (String statement : insertStatements) {
                stmt.execute(statement);
            }
        }

        stmt.close();
    }

    public static void createCategoriesTable() throws SQLException {
        openConnection();
        Statement stmt = conn.createStatement();

        String createQuery = "CREATE TABLE IF NOT EXISTS categories" +
            "(id INTEGER PRIMARY KEY," +
            "categoryname TEXT NOT NULL," +
            "hexString TEXT NOT NULL)";

        stmt.execute(createQuery);

        String checkExistingQuery = "SELECT COUNT(*) FROM categories";
        ResultSet rs = getResultSet(checkExistingQuery);
        int rowCount = rs.getInt(1);

        if (rowCount == 0) {
            ArrayList<String> insertStatements = new ArrayList<String>();

            insertStatements.add("INSERT INTO categories (categoryname, hexstring) " +
                "VALUES ('Study','#FFB380')");
            insertStatements.add("INSERT INTO categories (categoryname, hexstring) " +
                "VALUES ('Work','#9980E6')");
            insertStatements.add("INSERT INTO categories (categoryname, hexstring) " +
                "VALUES ('Relax','#99CC99')");
            insertStatements.add("INSERT INTO categories (categoryname, hexstring) " +
                "VALUES ('Travel','#FFA500')");
            insertStatements.add("INSERT INTO categories (categoryname, hexstring) " +
                "VALUES ('Exercise','#008080 ')");
            insertStatements.add("INSERT INTO categories (categoryname, hexstring) " +
                "VALUES ('Social','#87CEEB')");
            insertStatements.add("INSERT INTO categories (categoryname, hexstring) " +
                "VALUES ('Random','#FFB3B3')");

            for (String statement : insertStatements) {
                stmt.execute(statement);
            }
        }

        stmt.close();
    }


    public static void createDailyLearningTable() throws SQLException {
        openConnection();
        Statement stmt = conn.createStatement();

        String createQuery = "CREATE TABLE IF NOT EXISTS daily_learning" +
            "(id INTEGER PRIMARY KEY," +
            "date TEXT NOT NULL," +
            "wentWell TEXT NOT NULL," +
            "couldImprove TEXT NOT NULL)";
        stmt.execute(createQuery);

        String checkExistingQuery = "SELECT COUNT(*) FROM daily_learning";
        ResultSet rs = getResultSet(checkExistingQuery);
        int rowCount = rs.getInt(1);

        if (rowCount == 0) {
            ArrayList<String> insertStatements = new ArrayList<String>();

            insertStatements.add("INSERT INTO daily_learning (date, wentWell, couldImprove) " +
                "VALUES ('2019-11-16','Went to the Gym','Watched 3 hours of Netflix')");
            insertStatements.add("INSERT INTO daily_learning (date, wentWell, couldImprove) " +
                "VALUES ('2019-11-17','Went to lectures','Didn''t socialise with anyone')");
            insertStatements.add("INSERT INTO daily_learning (date, wentWell, couldImprove) " +
                "VALUES ('2019-11-12','Maths','Geography')");
            insertStatements.add("INSERT INTO daily_learning (date, wentWell, couldImprove) " +
                "VALUES ('2019-11-15','Woke up early','Didn''t socialise with anyone')");
            insertStatements.add("INSERT INTO daily_learning (date, wentWell, couldImprove) " +
                "VALUES ('2019-11-11','Didn''t use Facebook','Didn''t shower')");
            insertStatements.add("INSERT INTO daily_learning (date, wentWell, couldImprove) " +
                "VALUES ('2019-11-10','Went to lectures','Geography')");
            insertStatements.add("INSERT INTO daily_learning (date, wentWell, couldImprove) " +
                "VALUES ('2019-11-14','Went to the Gym','Didn''t shower')");
            insertStatements.add("INSERT INTO daily_learning (date, wentWell, couldImprove) " +
                "VALUES ('2019-11-11','Went to the Gym','Didn''t shower')");
            insertStatements.add("INSERT INTO daily_learning (date, wentWell, couldImprove) " +
                "VALUES ('2019-11-15','Maths','Watched 3 hours of Netflix')");

            for (String statement : insertStatements) {
                stmt.execute(statement);
            }
        }

        stmt.close();
    }
}
