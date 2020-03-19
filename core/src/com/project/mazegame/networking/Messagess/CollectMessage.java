package com.project.mazegame.networking.Messagess;

<<<<<<< HEAD

import com.project.mazegame.objects.Direction;
import com.project.mazegame.objects.Item;
=======
>>>>>>> yueyi2
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

<<<<<<< HEAD
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
=======
/**
 * If items collected, will send this message to everyone
 */
public class CollectMessage implements Message {
    private int msgType = Message.PLAYER_COLLECT_MSG;
    private int id;
    private String itemType;
    private int x;
    private int y;
    private int indexOfItem;
    private boolean debug =true;

    private MultiPlayerGameScreen gameClient;


    public CollectMessage(int id, MultiPlayerGameScreen gameClient, String itemType, int x, int y, int indexOfItem) {
        this(gameClient);
        this.id = id;
        this.itemType = itemType;
        this.x = x;
        this.y = y;
        this.indexOfItem = indexOfItem;
    }

    public CollectMessage(MultiPlayerGameScreen gameClient) {
>>>>>>> yueyi2
        this.gameClient = gameClient;
    }

    @Override
<<<<<<< HEAD
    public void send(DatagramSocket ds, String ip, int server_UDP_Port) {
        // TODO Auto-generated method stub
        ByteArrayOutputStream baos = new ByteArrayOutputStream(30);
=======
    public void send(DatagramSocket ds, String serverIP, int serverUDPPort) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
>>>>>>> yueyi2
        DataOutputStream dos = new DataOutputStream(baos);
        try {
            dos.writeInt(msgType);
            dos.writeInt(id);
<<<<<<< HEAD
            dos.writeInt(coin);
            dos.writeInt(sword);
=======
            dos.writeUTF(itemType);
            dos.writeInt(x);
            dos.writeInt(y);
            dos.writeInt(indexOfItem);

>>>>>>> yueyi2
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] buf = baos.toByteArray();
<<<<<<< HEAD
        try{
            DatagramPacket dp = new DatagramPacket(buf, buf.length, new InetSocketAddress(ip, server_UDP_Port));
            //System.out.println("I'm id"+id+", I'll send a move message.");
            ds.send(dp);
=======
        try {
            DatagramPacket datagramPacket = new DatagramPacket(buf, buf.length, new InetSocketAddress(serverIP, serverUDPPort));
           if(debug) System.out.println("I'm id" + id + ", I'll send a player collect items message.");
            ds.send(datagramPacket);
>>>>>>> yueyi2
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

<<<<<<< HEAD

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
                    //send coins and other items message
                    if(t.coins > 0)
                    {
                        t.setCoins(t.getCoins());
                    }else{

                    }
                }
            }
=======
    @Override
    public void process(DataInputStream dis) {
        try {

            int id = dis.readInt();
            if (id == this.gameClient.getMultiPlayer().getID()) {
                return;
            }

            String itemType = dis.readUTF();
            int itemsX = dis.readInt();
            int itemsY = dis.readInt();
            int indexOfItem = dis.readInt();

            // add item to other player's items list
            for(Player t : gameClient.getPlayers()) {
                if (t.getID() == id) {
                    // add item to other player's items list
                    if (!itemType.equals("coin")) {
                        t.items.add(itemType);
                    }
                }
            }
            //remove this items from itemMap
            gameClient.mapItems.remove(indexOfItem);

            if(debug) {
                System.out.println("-------------------------------");
                System.out.println("My id: " + this.gameClient.getMultiPlayer().getID());
                System.out.println("This player collect items message is from: id" + id);
                System.out.println("Item's Type: " + itemType);
                System.out.println("Item's Position x: " + itemsX + " y: " + itemsY);
                System.out.println("After remove this item, mapItems: ");
                for (int i = 0; i < gameClient.mapItems.size(); i++) {
                    System.out.print("(" + gameClient.mapItems.get(i).getPosition().getX() + "," + gameClient.mapItems.get(i).getPosition().getY() + ")");
                }
                System.out.println();
                System.out.println("-------------------------------");
            }

>>>>>>> yueyi2
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
