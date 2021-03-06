package com.project.mazegame.networking.Database;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

/**
 * <h1>CreateDataBase</h1>
 * The class is to create a default database table we need.
 * @Author Zhen Su
 * @author Yueyi Wang
 */
public class CreateDataBase {
    /**
     * To create a default database table we need
     * Including username and coins.
     * @param args Unused
     * @throws Exception Exception on database connection
     */
    public static void main(String args[]) {
        Connection c = null;
        Statement stmt = null;
        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager.getConnection("jdbc:postgresql://127.0.0.1/", "dbuser", "12345678");
            c.setAutoCommit(false);
            System.out.println("Opened database successfully");

            stmt = c.createStatement();
            String sql = "CREATE TABLE STUDENT " +
                    " (username            TEXT    NOT NULL, " +
                    " coins             TEXT    NOT NULL);";
            stmt.executeUpdate(sql);
            System.out.println("Table created successfully");

            stmt.close();
            c.commit();
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }
}