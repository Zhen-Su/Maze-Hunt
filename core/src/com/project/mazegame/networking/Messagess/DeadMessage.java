package com.project.mazegame.networking.Messagess;

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

public class DeadMessage implements Message {
    private boolean debug = true;
    private int msgType = Message.DEAD_MSG;
    private int id;
    private String deadmsg;
    private MultiPlayerGameScreen gameClient;

    public DeadMessage(int id, String deadmsg)
    {
        this.id = id;
        this.deadmsg = deadmsg;
    }

    public DeadMessage(MultiPlayerGameScreen gameClient){this.gameClient = gameClient;}
    @Override
    public void send(DatagramSocket ds, String serverIP, int serverUDPPort) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        try{
            dos.write(msgType);
            dos.write(id);
            dos.writeUTF(deadmsg);

        }catch (IOException e) {
            e.printStackTrace();
        }
        byte[] buf = baos.toByteArray();
        try {
            DatagramPacket datagramPacket = new DatagramPacket(buf, buf.length, new InetSocketAddress(serverIP, serverUDPPort));
            if(debug) System.out.println("I'm id" + id + ",I dead");
            ds.send(datagramPacket);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void process(DataInputStream dis) {
        try{
            int id = dis.readInt();
            if(id == this.gameClient.getMultiPlayer().getID()){
                return;
            }
            String deadMsg = dis.readUTF();
            for(Player t : gameClient.getPlayers()){
                if(t.getID() == id)
                {
                    if(deadMsg == "DEAD") {
                        t.death();
                    }
                }
            }

            if(debug) {
                System.out.println("-------------------------------");
                System.out.println("My id: " + this.gameClient.getMultiPlayer().getID());
                System.out.println("I received that player dead");
                System.out.println("-------------------------------");
            }
        }catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void process(DataInputStream dis, int aiIndex) {

    }
}
