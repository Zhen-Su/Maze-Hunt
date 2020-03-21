package com.project.mazegame.networking.Messagess;

import com.project.mazegame.objects.MultiPlayer;
import com.project.mazegame.screens.MultiPlayerGameScreen;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;

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
           if(debug) System.out.println("I'm id" + id + ", I'll send a player collect items message.");
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

            // add item to other player's items list
            for(MultiPlayer t : gameClient.getPlayers()) {
                if (t.getId() == id) {
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
                System.out.println("My id: " + this.gameClient.getMultiPlayer().getId());
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

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}