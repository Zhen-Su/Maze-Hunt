package com.project.mazegame.networking.Database;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.ArrayList;

public class AddData {
    public AddData()
    {

    }

    public static boolean exist(String username) throws Exception {
        ArrayList<String> list = new ArrayList<>();
        boolean exist = false;
        WithdrawData withdraw = new WithdrawData();
        list = withdraw.download();
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

    public void update(ArrayList<String> input)
    {
        Connection c = null;
        Statement stmt = null;
        String[] str;
        String[] name = new String[1000];
        String[] coins = new String[1000];
        for(int i = 0; i < input.size();i++)
        {
            str = input.get(i).split(" = ");
            name[i] = str[0];
            coins[i] = str[1];
            System.out.println(name[i] + " = " + coins[i]);
        }
        for (int i = 0; i < input.size();i++) {
            try {
                String sql;
                Class.forName("org.postgresql.Driver").newInstance();
                c = DriverManager.getConnection("jdbc:postgresql://127.0.0.1/", "dbuser", "12345678");
                c.setAutoCommit(false);
                System.out.println("Opened database successfully");

                //Update data to database
                stmt = c.createStatement();
                System.out.println(name[i] + " = " + coins[i]);
                boolean exist = exist(name[i]);
                if(exist == true){
                    sql = "UPDATE leaderboard SET coins = "+coins[i]+" WHERE username = '"+ name[i]+"';";

                }else {
                    sql = "INSERT INTO leaderboard(username, coins) VALUES(" +
                            "'" + name[i] + "', '" + coins[i] + "');";
                }

//            String sql = "UPDATE leaderboard set username = 'zhen' WHERE username = '"+coins+"';";
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
