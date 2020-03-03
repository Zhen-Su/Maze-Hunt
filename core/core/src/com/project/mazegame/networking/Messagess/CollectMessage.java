package com.project.mazegame.networking.Messagess;


import com.project.mazegame.objects.Direction;
import com.project.mazegame.objects.Item;
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

public class CollectMessage implements Message{
    private int msgType = Message.PLAYER_COLLECT_MSG;
    private int id;
    private int coin;
    private int sword;
    private MultiPlayerGameScreen gameClient;

    public CollectMessage(int msgType, int id, int coin, int sword) {
        this.msgType = msgType;
        this.id = id;
        this.coin = coin;
        this.sword = sword;
    }

    public CollectMessage(MultiPlayerGameScreen gameClient)
    {
        this.gameClient = gameClient;
    }

    @Override
    public void send(DatagramSocket ds, String ip, int server_UDP_Port) {
        // TODO Auto-generated method stub
        ByteArrayOutputStream baos = new ByteArrayOutputStream(30);
        DataOutputStream dos = new DataOutputStream(baos);
        try {
            dos.writeInt(msgType);
            dos.writeInt(id);
            dos.writeInt(coin);
            dos.writeInt(sword);
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] buf = baos.toByteArray();
        try{
            DatagramPacket dp = new DatagramPacket(buf, buf.length, new InetSocketAddress(ip, server_UDP_Port));
            //System.out.println("I'm id"+id+", I'll send a move message.");
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
            int newCoin = dis.readInt();
            int newSword = dis.readInt();
            for(MultiPlayer t : gameClient.getPlayers()){
                if(t.getId() == id){

                    //Change Collect things states
                    t.setX(newCoin);
                    t.setY(newSword);


                    System.out.println("****************************");
                    System.out.println("My id: " +this.gameClient.getMultiPlayer().getId());
                    System.out.println("This move message is from: id"+id);
                    System.out.println("This (id"+id+ ") player's Collect Coin: " +newCoin);
                    System.out.println("This (id"+id+ ") player's Collect Sword: " +newSword);
                    System.out.println("****************************");

                    //change player texture
                    if(t.coins > 0)
                    {
                        t.setCoins(t.getCoins());
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
