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

public class DecreaseHealthMessage implements Message {
    private int msgType = Message.DESCREASE_HP;
    private int beAttackedID;
    private int numOfHealth;
    private boolean debug = true;

    private MultiPlayerGameScreen gameClient;

    public DecreaseHealthMessage(int beAttackedID,int numOfHealth) {
        this.beAttackedID = beAttackedID;
        this.numOfHealth=numOfHealth;
    }

    public DecreaseHealthMessage(MultiPlayerGameScreen gameClient){
        this.gameClient = gameClient;
    }

    @Override
    public void send(DatagramSocket ds, String serverIP, int serverUDPPort) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        try{
            dos.writeInt(msgType);
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

    @Override
    public void process(DataInputStream dis) {
        try {
            int beAttackedID = dis.readInt();
            int numOfHealth = dis.readInt();
            if(beAttackedID != this.gameClient.getMultiPlayer().getID()){
                return;
            }else{
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
