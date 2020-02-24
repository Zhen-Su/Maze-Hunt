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

    public MoveMessage(int id, int pX, int pY, Direction dir) {
        // TODO Auto-generated constructor stub
        this.id=id;
        this.pX=pX;
        this.pY=pY;
        this.dir=dir;
    }

    public MoveMessage(MultiPlayerGameScreen gameClient) {
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
            dos.writeInt(pX);
            dos.writeInt(pY);
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
            if(id == this.gameClient.getMultiPlayer().getId()){
                return;
            }
            Direction dir = Direction.values()[dis.readInt()];
            int newx = dis.readInt();
            int newy = dis.readInt();
            for(MultiPlayer t : gameClient.getPlayers()){
                if(t.getId() == id){

                    //change coordinate and direction
                    t.setDir(dir);
                    t.getPosition().setX(newx);
                    t.getPosition().setY(newy);
//                    System.out.println("zhen x: "+x);
//                    System.out.println("zhen y:" +y);

                    //change player texture
                    if(t.bU ==true && t.bD == false){
                        t.setPlayerTexture(t.getPlayer_up());
                    }else if(t.bD==true&&t.bU==false){
                        t.setPlayerTexture(t.getPlayer_down());
                    }else if(t.bL==true&&t.bR==false){
                        t.setPlayerTexture(t.getPlayer_left());
                    }else if(t.bR==true&&t.bL==false){
                        t.setPlayerTexture(t.getPlayer_right());
                    }else {
                        t.setPlayerTexture(t.getPlayer_down());
                    }
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
