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

public class AttackMessage implements Message{
    private boolean debug = true;
    private int msgType = Message.ATTACK_MSG;
    private String attackdir;
    private int id;

    private MultiPlayerGameScreen gameClient;

    public AttackMessage(int id, String attackdir)
    {
        this.id = id;
        this.attackdir = attackdir;
    }

    public AttackMessage(MultiPlayerGameScreen gameClient){}

    @Override
    public void send(DatagramSocket ds, String serverIP, int serverUDPPort) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        try{
            dos.write(msgType);
            dos.write(id);
            dos.writeUTF(attackdir);

        }catch (IOException e) {
            e.printStackTrace();
        }
        byte[] buf = baos.toByteArray();
        try {
            DatagramPacket datagramPacket = new DatagramPacket(buf, buf.length, new InetSocketAddress(serverIP, serverUDPPort));
            if(debug) System.out.println("I'm id" + id + ", Attack");
            ds.send(datagramPacket);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void process(DataInputStream dis) {
        try {
            int msgType = dis.readInt();
            int id = dis.readInt();
            String attackdir = dis.readUTF();
            for(MultiPlayer t : gameClient.getPlayers())
            {
                if(t.getId() == id)
                {
                    if(attackdir == "Right"){
                        t.attack();
                    }else if(attackdir == "Left"){
                        t.attack();
                    }else if(attackdir == "Up"){
                        t.attack();
                    }else if(attackdir == "Down"){
                        t.attack();
                    }
                }
            }

        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
