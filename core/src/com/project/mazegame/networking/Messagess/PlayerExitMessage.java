package com.project.mazegame.networking.Messagess;

import com.project.mazegame.objects.MultiPlayer;
import com.project.mazegame.screens.MultiPlayerGameScreen;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;

public class PlayerExitMessage implements Message {
    private int msgType = Message.PLAYER_EXIT_MSG;
    private int id;
    //    private boolean isServerRunning;
    private MultiPlayerGameScreen gameClient;

    public PlayerExitMessage(MultiPlayerGameScreen gameClient)
    {
        this.gameClient = gameClient;
    }

    public PlayerExitMessage(MultiPlayerGameScreen gameClient,int id){
        this(gameClient);
        this.id = id;
//        this.isServerRunning = isServerRunning;
    }


    @Override
    public void send(DatagramSocket ds, String ip, int server_UDP_Port) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream(30);
        DataOutputStream dos = new DataOutputStream(baos);
        try {
            dos.writeInt(msgType);
            dos.writeInt(id);
//            dos.writeBoolean(isServerRunning);
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] buf = baos.toByteArray();
        try{
            DatagramPacket dp = new DatagramPacket(buf, buf.length, new InetSocketAddress(ip, server_UDP_Port));
            System.out.println("I'm id"+id+", I'll send a player exit message.");
            ds.send(dp);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    @Override
    public void process(DataInputStream dis) {
        try{
            int id = dis.readInt();
//            boolean isServerRunning = dis.readBoolean();
            if(id == this.gameClient.getMultiPlayer().getId()){
                return;
            }
            //delete this player from player list according to player's id.
            int indexOfExitPlayer = gameClient.playersIdIndexList.get(id);
            gameClient.getPlayers().remove(indexOfExitPlayer);
//            gameClient.setServerRunning(isServerRunning);

            System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
            System.out.println("My id: " +this.gameClient.getMultiPlayer().getId());
            System.out.println("This exit game message is from: id"+id);
            System.out.println("The idex of this exit player in players list: "+indexOfExitPlayer);
            System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
