package com.project.mazegame.networking.Messages;

import com.project.mazegame.networking.Client.GameClient;
import com.project.mazegame.objects.Player;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;

public class PlayerNewMessage implements Message {


    private int msgType = Message.PLAYER_NEW_MSG;
    private Player player;
    private GameClient gameClient;

    public PlayerNewMessage(Player player) {
        this.player = player;
        this.gameClient = gameClient;
    }

    public PlayerNewMessage(GameClient gameClient) {
        // TODO Auto-generated constructor stub
        this.gameClient = gameClient;
        player = gameClient.getPlayer();

    }



    /**
     * Send a packet to Server,then Server broadcast this packet to all clients
     *
     * @param ds
     * @param ip
     * @param server_UDP_Port
     */
    @Override
    public void send(DatagramSocket ds, String ip, int server_UDP_Port) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        try {
            dos.writeInt(msgType);
            dos.writeInt(player.id);
            dos.writeInt((int) player.getX());
            dos.writeInt((int) player.getY());
            dos.writeInt(player.getDir().ordinal());
        } catch (IOException e) {
            e.printStackTrace();
        }

        byte[] buf = baos.toByteArray();
        try {
            DatagramPacket dp = new DatagramPacket(buf, buf.length, new InetSocketAddress(ip, server_UDP_Port));
            ds.send(dp);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void process(DataInputStream dis) {
        try{
            int id = dis.readInt();
            if(id == this.gameClient.getPlayer().id){
                return;
            }

            int x = dis.readInt();
            int y = dis.readInt();
            Player.Dir dir = Player.Dir.values()[dis.readInt()];


            boolean exist = false;
            for (Player t : gameClient.getPlayers()){
                if(id == t.id){
                    exist = true;
                    break;
                }
            }
            if(!exist) {
                PlayerNewMessage msg = new PlayerNewMessage(gameClient);
                gameClient.getNc().send(msg);
                Player t = new Player(x, y, gameClient,dir);
                t.id = id;
                gameClient.getPlayers().add(t);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
