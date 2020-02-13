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

public class MoveMessage implements Message {
    private int msgType = Message.PLAYER_MOVE_MSG;
    private int id;
    private int x, y;
    private Player.Dir dir;
    private GameClient gameClient;

    public MoveMessage(int id, int x, int y, Player.Dir dir) {
        // TODO Auto-generated constructor stub
        this.id=id;
        this.x=x;
        this.y=y;
        this.dir=dir;
    }

    public MoveMessage(GameClient gameClient) {
        // TODO Auto-generated constructor stub
        this.gameClient=gameClient;
    }


    @Override
    public void send(DatagramSocket ds, String ip, int server_UDP_Port) {
        // TODO Auto-generated method stub
        ByteArrayOutputStream baos = new ByteArrayOutputStream(30);
        DataOutputStream dos = new DataOutputStream(baos);
        try {
            dos.writeInt(msgType);
            dos.writeInt(id);
            dos.writeInt(dir.ordinal());
            dos.writeInt(x);
            dos.writeInt(y);
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] buf = baos.toByteArray();
        try{
            DatagramPacket dp = new DatagramPacket(buf, buf.length, new InetSocketAddress(ip, server_UDP_Port));
            ds.send(dp);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void process(DataInputStream dis) {
        // TODO Auto-generated method stub
        try{
            int id = dis.readInt();
            if(id == this.gameClient.getPlayer().id){
                return;
            }
            Player.Dir dir = Player.Dir.values()[dis.readInt()];
            int x = dis.readInt();
            int y = dis.readInt();
            for(Player t : gameClient.getPlayers()){
                if(t.id == id){
                    t.setDir(dir);
                    t.setX(x);
                    t.setY(y);
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
