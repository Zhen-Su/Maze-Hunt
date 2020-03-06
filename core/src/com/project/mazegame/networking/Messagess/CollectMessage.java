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
    private String itemType;
    private MultiPlayerGameScreen gameClient;

    public CollectMessage(MultiPlayerGameScreen gameClient)
    {
        this.gameClient = gameClient;
    }

    public CollectMessage(int id , MultiPlayerGameScreen gameClient,String itemType) {
        this(gameClient);
        this.id =id;
        this.itemType=itemType;
    }

    @Override
    public void send(DatagramSocket ds, String ip, int server_UDP_Port) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream(30);
        DataOutputStream dos = new DataOutputStream(baos);
        try {
            dos.writeInt(msgType);
            dos.writeInt(id);
            dos.writeUTF(itemType);
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] buf = baos.toByteArray();
        try{
            DatagramPacket dp = new DatagramPacket(buf, buf.length, new InetSocketAddress(ip, server_UDP_Port));
            System.out.println("I'm id"+id+", I'll send a collection message.");
            ds.send(dp);
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

            String itemType = dis.readUTF();

            for(MultiPlayer t : gameClient.getPlayers()){
                if(t.getId() == id){
                    // add item to other player's items list
                    if(itemType != "coin"){
                        t.items.add(itemType);
                        System.out.println("itemType: "+itemType);
                    }

                    System.out.println("-------------------------------");
                    System.out.println("My id: " +this.gameClient.getMultiPlayer().getId());
                    System.out.println("This collection message is from: id"+id);
                    System.out.println("This (id"+id+ ") player collect: "+itemType);
                    System.out.println("-------------------------------");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
