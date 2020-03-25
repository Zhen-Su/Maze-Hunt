package com.project.mazegame.networking.Messagess;


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

public class MoveMessage implements Message {
    private int msgType = Message.PLAYER_MOVE_MSG;
    private int id;
    private int pX, pY;
    private Direction dir;
    private MultiPlayerGameScreen gameClient;
    private boolean debug =true;

    public MoveMessage(int id, int pX, int pY, Direction dir) {
        this.id=id;
        this.pX=pX;
        this.pY=pY;
        this.dir=dir;
    }

    public MoveMessage(MultiPlayerGameScreen gameClient) {
        this.gameClient=gameClient;
    }


    @Override
    public void send(DatagramSocket ds, String ip, int server_UDP_Port) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream(30);
        DataOutputStream dos = new DataOutputStream(baos);
        try {
            dos.writeInt(msgType);
            dos.writeInt(id);
            dos.writeInt(dir.ordinal());
            dos.writeInt(pX);
            dos.writeInt(pY);
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] buf = baos.toByteArray();
        try{
            DatagramPacket dp = new DatagramPacket(buf, buf.length, new InetSocketAddress(ip, server_UDP_Port));
            if(debug) System.out.println("I'm id"+id+", I'll send a move message.");
            ds.send(dp);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void process(DataInputStream dis) {
        try{
            int id = dis.readInt();
            if(id == this.gameClient.getMultiPlayer().getID()){
                return;
            }
            Direction dir = Direction.values()[dis.readInt()];
            int newX = dis.readInt();
            int newY = dis.readInt();
            for(Player t : gameClient.getPlayers()){
                if(t.getID() == id){
                    //change coordinate and direction
                    t.setDir(dir);
                    //TODO this need think, setX() and position.setX()
                    t.setX(newX);
                    t.setY(newY);
                    t.position.setX(newX);
                    t.position.setY(newY);

                    if(debug) {
                        System.out.println("****************************");
                        System.out.println("My id: " + this.gameClient.getMultiPlayer().getID());
                        System.out.println("This move message is from: id" + id);
                        System.out.println("This (id" + id + ") player's position x: " + newX);
                        System.out.println("This (id" + id + ") player's position y: " + newY);
                        System.out.println("****************************");
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void process(DataInputStream dis, int aiIndex) {

    }
}
