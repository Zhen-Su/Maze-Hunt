package com.project.mazegame.networking.Database;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * <h1>AddData</h1>
 * The class is to upload the player's coins and username to the database
 * @Author Zhen Su & Yueyi Wang
 */
public class AddData {

    /**
     * <h1>exist</h1>
     * This method is used to determine whether the user name exists in the database
     * Step1: Extract the stored information from the database, including the username and the amount of coins it gets.
     * Step2: Store the data downloaded from the database into a Arraylist called "list".
     * Step3: Extract the username and the entered username for judgment, return true if they are equal, and return false if they are not equal.
     * @param username Obtained from the update method
     * @return True if the username appears in the database, otherwise false
     * @throws Exception Exception on database connection
     */
    public static boolean exist(String username) throws Exception {
        boolean exist = false;
        WithdrawData withdraw = new WithdrawData();
        ArrayList<String> list = withdraw.download();
        String[] str;
        String[] name = new String[1000];
        for (int i = 0; i < list.size(); i++)
        {
            str = list.get(i).split(" = ");
            name[i] = str[0];
            if (username.equals(name[i]))
            {
                exist = true;
            }
        }
        return exist;
    }

    /**
     * The method is to upload the user's gold coins to the database.
     * @param input Obtained Arraylist data from the EndScreen Class
     * @throws Exception Exception on database connection
     */
    public void update(ArrayList<String> input)
    {
        Connection c = null;
        Statement stmt = null;
        String[] str;
        String[] name = new String[1000];
        String[] coins = new String[1000];

        //Separate the user names and gold coins in the arraylist for easy operation and storage in the database.
        for(int i = 0; i < input.size();i++)
        {
            str = input.get(i).split(" = ");
            name[i] = str[0];
            coins[i] = str[1];
            System.out.println(name[i] + " = " + coins[i]);
        }
        for (int i = 0; i < input.size();i++) {

            try {
                //To connect database
                String sql;
                Class.forName("org.postgresql.Driver").newInstance();
                c = DriverManager.getConnection("jdbc:postgresql://127.0.0.1/", "dbuser", "12345678");
                c.setAutoCommit(false);
                System.out.println("Opened database successfully");

                //Update data to database
                stmt = c.createStatement();
                System.out.println(name[i] + " = " + coins[i]);
                boolean exist = exist(name[i]);

                //Different judgment methods use different database statements.
                if(exist == true){
                    sql = "UPDATE leaderboard SET coins = "+coins[i]+" WHERE username = '"+ name[i]+"';";

                }else {
                    sql = "INSERT INTO leaderboard(username, coins) VALUES(" +
                            "'" + name[i] + "', '" + coins[i] + "');";
                }
                stmt.executeUpdate(sql);
                System.out.println("Data update successfully");
                stmt.close();
                c.commit();
                c.close();

            } catch (Exception e) {
                System.out.println(e.getClass().getName() + ": " + e.getMessage());
                System.exit(0);
            }
        }
    }


}
