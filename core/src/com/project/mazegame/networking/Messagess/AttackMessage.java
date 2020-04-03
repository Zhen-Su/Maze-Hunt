package com.project.mazegame.networking.Messagess;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
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

/**
 * This class to send and process attack info through server and client.
 * @author Yueyi Wang & Zhen Su
 */
public class AttackMessage implements Message{
    private int msgType = Message.ATTACK_MSG;
    private int id;
    private boolean debug = false;

    private MultiPlayerGameScreen gameClient;

    public AttackMessage(int id)
    {
        this.id = id;
    }

    public AttackMessage(MultiPlayerGameScreen gameClient){this.gameClient = gameClient;}

    /**
     * This method send attack info to server, and let other client to receive that and process
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
            dos.writeInt(id);

        }catch (IOException e) {
            e.printStackTrace();
        }
        byte[] buf = baos.toByteArray();
        try {
            DatagramPacket datagramPacket = new DatagramPacket(buf, buf.length, new InetSocketAddress(serverIP, serverUDPPort));
            if(debug) System.out.println("I'm id" + id + ", I'll send an Attack message");
            ds.send(datagramPacket);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Use DataInputStream to process the acquired data and transform
     * the player set in the client for attack operations.
     * @param dis Input stream
     * @throws Exception
     */
    @Override
    public void process(DataInputStream dis) {
        try {
            //Read user ID
            int id = dis.readInt();
            //If is myself, so do nothing
            if(id == this.gameClient.getMultiPlayer().getID()){
                return;
            }

            //Iterate over the user to perform an action.
            for(Player t : gameClient.getPlayers())
            {
                if(t.getID() == id)
                {
                    t.isAttacking=true;
                }
            }

            if(debug) {
                System.out.println("-------------------------------");
                System.out.println("My id: " + this.gameClient.getMultiPlayer().getID());
                System.out.println("I received this attack message from ID:"+id+" player");
                System.out.println("-------------------------------");
            }

        } catch (IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public void process(DataInputStream dis, int aiIndex) {

    }
}
