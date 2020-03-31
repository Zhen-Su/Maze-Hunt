package com.project.mazegame.networking.Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class AddData {
    private String username;
    private int coins;


    public AddData(String username,int coins)
    {
        this.coins = coins;
        this.username = username;
    }


    public void update()
    {
        Connection c = null;
        Statement stmt = null;

        try{
            Class.forName("org.postgresql.Driver").newInstance();
            c = DriverManager.getConnection("jdbc:postgresql://127.0.0.1/", "dbuser", "12345678");
            c.setAutoCommit(false);
            System.out.println("Opened database successfully");

            //Update data to database
            stmt = c.createStatement();
            String sql = "INSERT INTO leaderboard(username, coins) VALUES(" +
                    "'"+username+"', '"+coins+"');";

//            String sql = "UPDATE leaderboard set username = 'zhen' WHERE username = '"+coins+"';";
            stmt.executeUpdate(sql);
            System.out.println("Data update successfully");
            stmt.close();
            c.commit();
            c.close();


        }catch (Exception e)
        {
            System.out.println(e.getClass().getName()+": " + e.getMessage());
            System.exit(0);
        }
    }


}
