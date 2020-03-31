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

/**
 * This class to send and process damage info through client and server.
 * @author Yueyi Wang & Zhen Su
 */
public class DecreaseHealthMessage implements Message {
    private int msgType = Message.DESCREASE_HP;
    private int beAttackedID;
    private int myID;
    private int numOfHealth;
    private boolean debug = true;

    private MultiPlayerGameScreen gameClient;

    public DecreaseHealthMessage(int myID,int beAttackedID,int numOfHealth) {
        this.beAttackedID = beAttackedID;
        this.myID=myID;
        this.numOfHealth=numOfHealth;
    }

    public DecreaseHealthMessage(MultiPlayerGameScreen gameClient){
        this.gameClient = gameClient;
    }

    /**
     * This method send damage info to server, and let other client to receive that and process
     * @param ds Send data using DatagreamSocket from server
     * @param serverIP Input Server's IP address
     * @param serverUDPPort Input UDP's port
     * @throws Exception This exception is thrown when closing the stream fails
     */
    @Override
    public void send(DatagramSocket ds, String serverIP, int serverUDPPort) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        try{
            dos.writeInt(msgType);
            dos.writeInt(myID);
            dos.writeInt(beAttackedID);
            dos.writeInt(numOfHealth);

        }catch (IOException e) {
            e.printStackTrace();
        }
        byte[] buf = baos.toByteArray();
        try {
            DatagramPacket datagramPacket = new DatagramPacket(buf, buf.length, new InetSocketAddress(serverIP, serverUDPPort));
            if(debug) System.out.println("I attack a player, I'll send an Decrease message");
            ds.send(datagramPacket);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Use DataInputStream to process the acquired data and transform
     * the player set in the client for damage operations.
     * @param dis Input stream
     * @throws Exception
     */
    @Override
    public void process(DataInputStream dis) {
        try {
            //Read user ID
            int myID = dis.readInt();
            int beAttackedID = dis.readInt();
            int numOfHealth = dis.readInt();

            //if this message is sent by my, then ignore it.
            if(myID == this.gameClient.getMultiPlayer().getID()){
                return;
            }

            //if i'm not a Victim, then decrease health for this player in my players list.
            for(Player player : this.gameClient.getPlayers()){
                if(player.getID()==beAttackedID){
                    player.decreaseHealth(numOfHealth);
                }
            }

            //if i'm a Victim, then decrease health for me.
            if(beAttackedID == this.gameClient.getMultiPlayer().getID()){

                this.gameClient.getMultiPlayer().decreaseHealth(numOfHealth);

                if(debug) {
                    System.out.println("-------------------------------");
                    System.out.println("My id: " + this.gameClient.getMultiPlayer().getID());
                    System.out.println("I will decrease health by "+numOfHealth);
                    System.out.println("-------------------------------");
                }
            }


        } catch (IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public void process(DataInputStream dis, int aiIndex) {

    }
}
