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

    public AttackMessage(MultiPlayerGameScreen gameClient){this.gameClient = gameClient;}

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
            int id = dis.readInt();
            if(id == this.gameClient.getMultiPlayer().getID()){
                return;
            }
            String attackdir = dis.readUTF();

            for(Player t : gameClient.getPlayers())
            {
                if(t.getID() == id)
                {
                    if(attackdir == "Attack")
                    {
                        t.attack();
                    }
                }
            }

            if(debug) {
                System.out.println("-------------------------------");
                System.out.println("My id: " + this.gameClient.getMultiPlayer().getID());
                System.out.println("I received that attack Msg " + attackdir + " from that player");
                System.out.println("-------------------------------");
            }

        } catch (IOException e){
            e.printStackTrace();
        }
    }
<<<<<<< HEAD
=======

    @Override
    public void process(DataInputStream dis, int aiIndex) {

    }
>>>>>>> origin/yueyi1
}
