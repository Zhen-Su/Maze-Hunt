package com.project.mazegame.networking.Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

/**
 * To generate leaderboard table on database
 */
class GenerateTable {
    public static void main(String args[])
    {
        Connection c = null;
        Statement stmt = null;
        try{
            Class.forName("org.postgresql.Driver").newInstance();
            c = DriverManager.getConnection("jdbc:postgresql://127.0.0.1/", "dbuser", "12345678");
            c.setAutoCommit(false);
            System.out.println("Opened database successfully");

            stmt = c.createStatement();
            String sql = "CREATE TABLE Leaderboard" +
                    "(USERNAME             TEXT     NOT NULL, " +
                    "COINS              TEXT     NOT NULL)";
            stmt.executeUpdate(sql);
            System.out.println("Table created successfully");
            stmt.close();
            c.commit();
            c.close();
        }catch (Exception e){
            System.out.println(e.getClass().getName()+": " + e.getMessage());
            System.exit(0);
        }
    }
}
