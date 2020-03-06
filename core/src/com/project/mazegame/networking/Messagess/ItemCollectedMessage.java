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
    private int msgType = Message.ITEM_COLLECTED;
    private int id;
    private String itemType;
    private int x;
    private int y;
    private int indexOfItem;
    private boolean debug =true;

    private MultiPlayerGameScreen gameClient;


    public ItemCollectedMessage(int id, MultiPlayerGameScreen gameClient, String itemType, int x, int y, int indexOfItem) {
        this.id = id;
        this.gameClient = gameClient;
        this.itemType = itemType;
        this.x = x;
        this.y = y;
        this.indexOfItem = indexOfItem;
    }

    public ItemCollectedMessage(MultiPlayerGameScreen gameClient) {
        this.gameClient = gameClient;
    }

    @Override
    public void send(DatagramSocket ds, String serverIP, int serverUDPPort) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        try {
            dos.writeInt(msgType);
            dos.writeInt(id);
            dos.writeUTF(itemType);
            dos.writeInt(x);
            dos.writeInt(y);
            dos.writeInt(indexOfItem);

        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] buf = baos.toByteArray();
        try {
            DatagramPacket datagramPacket = new DatagramPacket(buf, buf.length, new InetSocketAddress(serverIP, serverUDPPort));
           if(debug) System.out.println("I'm id" + id + ", I'll send a 'Item remove from mapItem' message.");
            ds.send(datagramPacket);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void process(DataInputStream dis) {
        try {

            int id = dis.readInt();
            if (id == this.gameClient.getMultiPlayer().getId()) {
                return;
            }

            String itemType = dis.readUTF();
            int itemsX = dis.readInt();
            int itemsY = dis.readInt();
            int indexOfItem = dis.readInt();

            gameClient.mapItems.remove(indexOfItem);

            if(debug) {
                System.out.println("-------------------------------");
                System.out.println("My id: " + this.gameClient.getMultiPlayer().getId());
                System.out.println("This 'Item remove from mapItem' message is from: id" + id);
                System.out.println("This (id" + id + ") player collect: " + itemType);
                System.out.println("Items position x: " + itemsX + " y: " + itemsY);
                System.out.println("mapItems: ");
                for (int i = 0; i < gameClient.mapItems.size(); i++) {
                    System.out.print("(" + gameClient.mapItems.get(i).getPosition().getX() + "," + gameClient.mapItems.get(i).getPosition().getY() + ")");
                }
                System.out.println();
                System.out.println("-------------------------------");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
