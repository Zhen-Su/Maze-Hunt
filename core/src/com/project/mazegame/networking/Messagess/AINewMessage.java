package com.project.mazegame.networking.Messagess;

import com.project.mazegame.objects.Direction;
import com.project.mazegame.objects.MultiPlayerAI;
import com.project.mazegame.screens.MultiPlayerGameScreen;
import com.sun.org.apache.xpath.internal.operations.Mult;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;

public class AINewMessage implements Message {
    private int msgType = Message.AI_New_MSG;
    private MultiPlayerAI aiPlayer;
    private MultiPlayerGameScreen gameClient;

    public AINewMessage(MultiPlayerAI aiPlayer) {
        this.aiPlayer = aiPlayer;
    }

    public AINewMessage (MultiPlayerGameScreen gameClient) {
        this.gameClient = gameClient;
        aiPlayer = gameClient.getMultiPlayerAI();
    }

    @Override
    public void send(DatagramSocket datagramSocket, String ip, int server_UDP_Port) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        try {
            dos.writeInt(msgType);
            dos.writeInt(aiPlayer.getId());
            dos.writeInt(aiPlayer.position.getX());
            dos.writeInt(aiPlayer.position.getY());
            dos.writeInt(aiPlayer.getDir().ordinal());
            dos.writeUTF(aiPlayer.getName());
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] buf = baos.toByteArray();
        try {
            DatagramPacket datagramPacket = new DatagramPacket(buf, buf.length, new InetSocketAddress(ip, server_UDP_Port));
            datagramSocket.send(datagramPacket);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void process(DataInputStream dis) {
        try {
            int id = dis.readInt();
            if(id == this.gameClient.getMultiPlayerAI().getId()) {
                return;
            }
            int x = dis.readInt();
            int y = dis.readInt();
            Direction dir = Direction.values()[dis.readInt()];
            String username = dis.readUTF();
            boolean exist = false;
            for (MultiPlayerAI t : gameClient.getAiPlayers()) {
                if (id == t.getId()) {
                    exist = true;
                    break;
                }
            }
            if (!exist) {
                AINewMessage msg = new AINewMessage(gameClient);
                gameClient.getNc().send(msg);
                MultiPlayerAI newAIPlayer = new MultiPlayerAI(gameClient.getCollisionLayer(), username, x, y, gameClient, dir, gameClient.getMultiPlayerAI().getCo());

                System.out.println("--------------------------------------");
                System.out.println("my id: "+this.gameClient.getMultiPlayerAI().getId());
                System.out.println("this player new message is from: id "+id);

                newAIPlayer.setId(id);
                gameClient.getAiPlayers().add(newAIPlayer);
                gameClient.playersIdIndexList.put(id, gameClient.getAiPlayers().indexOf(newAIPlayer));
                System.out.println("I've added this player to list!!");
                System.out.println("--------------------------------------");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
