package com.project.mazegame.networking.Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class WithdrawData {
    public ArrayList<String> list = new ArrayList<>();
    public ArrayList<String> download() throws Exception {
        String result = null;
        Connection con = DriverManager.getConnection("jdbc:postgresql://127.0.0.1/", "dbuser", "12345678");
        String sql = "SELECT * FROM leaderboard order by coins";
        PreparedStatement pstat = con.prepareStatement(sql);
        ResultSet rs = pstat.executeQuery();
        while (rs.next()) {
            String username = rs.getString("username");
            int coin = rs.getInt("coins");
            result = username + " = " + coin;
            list.add(result);
        }
        return list;
    }
}