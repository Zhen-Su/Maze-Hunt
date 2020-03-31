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
 * To send host items generate situation to everyone, and let other to copy that items
 * @author Yueyi Wang & Zhen Su
 */
public class ItemCreateMessage implements Message {
    private int msgType = Message.ITEMS_CREATE;
    private int id;
    private String itemType;
    private int x;
    private int y;
    private MultiPlayerGameScreen gameClient;
    private boolean debug=false;


    public ItemCreateMessage(int id, String itemType, int x, int y) {
        this.id = id;
        this.itemType = itemType;
        this.x = x;
        this.y = y;
    }

    public ItemCreateMessage(MultiPlayerGameScreen gameClient) {
        this.gameClient = gameClient;
    }
    /**
     * This method send generating items on map info to server, and let other client to receive that and process
     * @param ds Send data using DatagreamSocket from server
     * @param serverIP Input Server's IP address
     * @param serverUDPPort Input UDP's port
     * @throws Exception This exception is thrown when closing the stream fails
     */
    @Override
    public void send(DatagramSocket ds, String serverIP, int serverUDPPort) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        try {
            dos.writeInt(msgType);
            dos.writeInt(id);
            if (id == 1) {
                dos.writeUTF(itemType);
                dos.writeInt(x);
                dos.writeInt(y);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] buf = baos.toByteArray();
        try {
            DatagramPacket datagramPacket = new DatagramPacket(buf, buf.length, new InetSocketAddress(serverIP, serverUDPPort));
            if(debug) System.out.println("I'm id" + id + ", I'll send a Item generate message.");
            ds.send(datagramPacket);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    /**
     * Use DataInputStream to process the acquired data and transform
     * the player set in the client for generating map items operations.
     * @param dis Input stream
     * @throws Exception
     */
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

            Coordinate co = new Coordinate(itemsX, itemsY);
            Item item = new Item(itemType, co);
            gameClient.mapItems.add(item);

            if(debug) {
                System.out.println("-------------------------------");
                System.out.println("My id: " + this.gameClient.getMultiPlayer().getID());
                System.out.println("This item generation message is from: id" + id);
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

    @Override
    public void process(DataInputStream dis, int aiIndex) {

    }
}
