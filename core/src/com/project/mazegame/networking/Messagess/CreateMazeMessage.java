package com.project.mazegame.networking.Messagess;

import com.project.mazegame.screens.HostLobbyScreen;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramSocket;

public class CreateMazeMessage {
    private String username;
    private int mazeNumber;
    private HostLobbyScreen lobby;
    private int id;

    public CreateMazeMessage(String username, int mazeNumber) {
        this.username = username;
        this.mazeNumber = mazeNumber;
    }


    public CreateMazeMessage(HostLobbyScreen lobby) { this.lobby = lobby;}


    public void send(DatagramSocket ds, String ip, int server_UDP_Port) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream(30);
        DataOutputStream dos = new DataOutputStream(baos);
        try{
            dos.writeInt(id);
            dos.writeInt(mazeNumber);
        }catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
