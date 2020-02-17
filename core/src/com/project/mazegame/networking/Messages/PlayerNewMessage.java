package com.project.mazegame.networking.Messages;


import com.project.mazegame.objects.Direction;
import com.project.mazegame.objects.MultiPlayer;
import com.project.mazegame.objects.Player;
import com.project.mazegame.screens.MultiPlayerGameScreen;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;

public class PlayerNewMessage implements Message {


    private int msgType = Message.PLAYER_NEW_MSG;
    private MultiPlayer multiPlayer;
    private MultiPlayerGameScreen gameClient;

    public PlayerNewMessage(MultiPlayer multiPlayer) {
        this.multiPlayer = multiPlayer;
        this.gameClient = gameClient;
    }

    public PlayerNewMessage(MultiPlayerGameScreen gameClient) {
        this.gameClient = gameClient;
        multiPlayer = gameClient.getMultiPlayer();
    }



    /**
     * Send a packet to Server,then Server broadcast this packet to all clients
     *
     * @param datagramSocket
     * @param ip  Server ip
     * @param server_UDP_Port
     */
    @Override
    public void send(DatagramSocket datagramSocket, String ip, int server_UDP_Port) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        try {
            dos.writeInt(msgType);
            dos.writeInt(multiPlayer.getId());
            dos.writeInt((int) multiPlayer.getX());
            dos.writeInt((int) multiPlayer.getY());
            dos.writeInt(multiPlayer.getDir().ordinal());
            dos.writeUTF(multiPlayer.getName());
        } catch (IOException e) {
            e.printStackTrace();
        }

        byte[] buf = baos.toByteArray();
        try {
            DatagramPacket datagramPacket = new DatagramPacket(buf, buf.length, new InetSocketAddress(ip, server_UDP_Port));
            datagramSocket.send(datagramPacket);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void process(DataInputStream dis) {
        try{

            int id = dis.readInt();
            if(id == this.gameClient.getMultiPlayer().getId()){
                return;
            }

            int x = dis.readInt();
            int y = dis.readInt();
            Direction dir = Direction.values()[dis.readInt()];
            String tankName = dis.readUTF();

            boolean exist = false;
            for (MultiPlayer t : gameClient.getPlayers()){
                if(id == t.getId()){
                    exist = true;
                    break;
                }
            }
            if(!exist) {
                PlayerNewMessage msg = new PlayerNewMessage(gameClient);
                gameClient.getNc().send(msg);
                MultiPlayer newPlayer = new MultiPlayer(gameClient.getCollisionLayer(),tankName,x,y,gameClient,dir);
                newPlayer.setId(id);
                gameClient.getPlayers().add(newPlayer);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
