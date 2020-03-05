package com.project.mazegame.networking.Messagess;

import com.project.mazegame.objects.Item;
import com.project.mazegame.objects.MultiPlayer;
import com.project.mazegame.screens.MultiPlayerGameScreen;
import com.project.mazegame.tools.Coordinate;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.HashSet;

/**
 * If items collected, will send message to everyone
 */
public class ItemCollectedMessage implements Message {
    private int msgType = Message.ITEMS_CREATE;
    private int id;
    private String itemType;
    private int x;
    private int y;

    private MultiPlayerGameScreen gameClient;


    public ItemCollectedMessage(int id,String itemType, int x, int y)
    {
        this.id = id;
        this.itemType = itemType;
        this.x = x;
        this.y = y;
    }

    public ItemCollectedMessage(MultiPlayerGameScreen gameClient)
    {
        this.gameClient = gameClient;
    }

    @Override
    public void send(DatagramSocket ds, String serverIP, int serverUDPPort) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        try{
            dos.writeInt(msgType);
            dos.writeInt(id);
            dos.writeUTF(itemType);
            dos.writeInt(x);
            dos.writeInt(y);

        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] buf = baos.toByteArray();
        try {
            DatagramPacket datagramPacket = new DatagramPacket(buf, buf.length, new InetSocketAddress(serverIP, serverUDPPort));
            System.out.println("I'm id"+id+", I'll send a item collected message.");
            ds.send(datagramPacket);
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
            int itemsX = dis.readInt();
            int itemsY = dis.readInt();

            for (MultiPlayer t : gameClient.getPlayers()){
                if(t.getId() == id) {
                    Coordinate co = new Coordinate(itemsX, itemsY);
                    Item item = new Item(itemType, co);
                    gameClient.mapItems.remove(item);
                    System.out.println("-------------------------------");
                    System.out.println("My id: " + this.gameClient.getMultiPlayer().getId());
                    System.out.println("This item collected message is from: id" + id);
                    System.out.println("This (id" + id + ") player collect: " + itemType);
                    System.out.println("Items position x: "+itemsX+" y: "+itemsY);
                    System.out.println("-------------------------------");
                }
            }

        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
