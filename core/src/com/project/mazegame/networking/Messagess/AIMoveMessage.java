//package com.project.mazegame.networking.Messagess;
//
//import com.project.mazegame.objects.Direction;
//import com.project.mazegame.objects.MultiPlayerAI;
//import com.project.mazegame.screens.MultiPlayerGameScreen;
//
//import java.io.ByteArrayOutputStream;
//import java.io.DataInputStream;
//import java.io.DataOutputStream;
//import java.io.IOException;
//import java.net.DatagramPacket;
//import java.net.DatagramSocket;
//import java.net.InetSocketAddress;
//
//public class AIMoveMessage implements Message {
//    private int msgType = Message.AI_MOVE_MSG;
//    private int id;
//    private int pX, pY;
//    private Direction dir;
//    private MultiPlayerGameScreen gameClient;
//
//    public AIMoveMessage (int id, int pX, int pY, Direction dir) {
//        this.id = id;
//        this.pX = pX;
//        this.pY = pY;
//        this.dir = dir;
//    }
//
//    public AIMoveMessage(MultiPlayerGameScreen gameClient) {
//        this.gameClient = gameClient;
//    }
//
//    @Override
//    public void send(DatagramSocket ds, String ip, int server_UDP_Port) {
//        ByteArrayOutputStream baos = new ByteArrayOutputStream(30);
//        DataOutputStream dos = new DataOutputStream(baos);
//        try {
//            dos.writeInt(msgType);
//            dos.writeInt(id);
//            dos.writeInt(dir.ordinal());
//            dos.writeInt(pX);
//            dos.writeInt(pY);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        byte[] buf = baos.toByteArray();
//        try {
//            DatagramPacket dp = new DatagramPacket(buf, buf.length, new InetSocketAddress(ip, server_UDP_Port));
//            ds.send(dp);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Override
//    public void process(DataInputStream dis) {
//        try {
//            int id = dis.readInt();
//            if (id == this.gameClient.getMultiPlayerAI().getId()) {
//                return;
//            }
//            Direction dir = Direction.values()[dis.readInt()];
//            int newX = dis.readInt();
//            int newY = dis.readInt();
//
//            for (MultiPlayerAI t : gameClient.getAiPlayers()) {
//                if (t.getId() == id) {
//                    t.setDir(dir);
//                    t.setX(newX);
//                    t.setY(newY);
//                }
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//}
