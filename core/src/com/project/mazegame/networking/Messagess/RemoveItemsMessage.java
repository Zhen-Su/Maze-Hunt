package com.project.mazegame.networking.Messagess;

import com.project.mazegame.objects.Player;
import com.project.mazegame.screens.MultiPlayerGameScreen;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;

public class RemoveItemsMessage implements Message {
    private int msgType = Message.REMOVE_MSG;
    private int id;
    private int index;
    private String itemType;
    private boolean debug = true;
    private MultiPlayerGameScreen gameClient;

    public RemoveItemsMessage(MultiPlayerGameScreen gameClient) {
        this.gameClient = gameClient;
    }

    public RemoveItemsMessage(int id, int index, String itemType, MultiPlayerGameScreen gameClient) {
        this.id = id;
        this.index = index;
        this.itemType = itemType;
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
            dos.writeInt(index);
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] buf = baos.toByteArray();
        try {
            DatagramPacket datagramPacket = new DatagramPacket(buf, buf.length, new InetSocketAddress(serverIP, serverUDPPort));
            if (debug) System.out.println("I'm id" + id + ", I'll send a remove items message");
            ds.send(datagramPacket);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void process(DataInputStream dis) {
        try {
            int id = dis.readInt();
            if (id == this.gameClient.getMultiPlayer().getID()) {
                return;
            }

            String itemType = dis.readUTF();
            int index = dis.readInt();

            if (debug) {
                System.out.println("===========Remove-Item(Received)=====================");
                System.out.println("My ID:" + this.gameClient.getMultiPlayer().getID());
                System.out.println("This remove item message from ID" + id);
                System.out.println("Item's Type: " + itemType);
                System.out.println("Item's index:" + index);
            }

            for (Player t : gameClient.getPlayers()) {
                if (t.getID() == id) {
                    if(debug) System.out.println("Before remove item from list:" + t.items);
                    if (itemType.equals("shield") || itemType.equals("gearEnchantment")) {
                        t.items.remove(index);
                        if(debug) System.out.println("After remove item from list:" + t.items);
                    }
                }
            }

            if(debug) System.out.println("=======================================================");


        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void process(DataInputStream dis, int aiIndex) {

    }
}
