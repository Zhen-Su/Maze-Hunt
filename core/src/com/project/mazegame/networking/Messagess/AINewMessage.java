//package com.project.mazegame.networking.Messagess;
//
//import com.project.mazegame.objects.AIGameClient;
//import com.project.mazegame.objects.AIPlayer;
//import com.project.mazegame.objects.Direction;
//import com.project.mazegame.objects.Player;
//import java.io.ByteArrayOutputStream;
//import java.io.DataInputStream;
//import java.io.DataOutputStream;
//import java.io.IOException;
//import java.net.DatagramPacket;
//import java.net.DatagramSocket;
//import java.net.InetSocketAddress;
//
//public class AINewMessage implements Message {
//    private int msgType = Message.AI_NEW_MSG;
//    private AIPlayer aiPlayer;
//    private AIGameClient gameClient;
//
//    public AINewMessage(AIPlayer aiPlayer) {
//        this.aiPlayer = aiPlayer;
//    }
//
//    public AINewMessage (AIGameClient gameClient) {
//        this.gameClient = gameClient;
//        aiPlayer = gameClient.getAiPlayer();
//    }
//
//    @Override
//    public void send(DatagramSocket datagramSocket, String ip, int server_UDP_Port) {
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        DataOutputStream dos = new DataOutputStream(baos);
//        try {
//            dos.writeInt(msgType);
//            dos.writeInt(aiPlayer.getID());
//            dos.writeInt(aiPlayer.position.getX());
//            dos.writeInt(aiPlayer.position.getY());
//            dos.writeInt(aiPlayer.getDir().ordinal());
//            dos.writeUTF(aiPlayer.getName());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        byte[] buf = baos.toByteArray();
//        try {
//            DatagramPacket datagramPacket = new DatagramPacket(buf, buf.length, new InetSocketAddress(ip, server_UDP_Port));
//            datagramSocket.send(datagramPacket);
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Override
//    public void process(DataInputStream dis) {
//        try {
//            int id = dis.readInt();
//            if(id == this.gameClient.getAiPlayer().getID()) {
//                return;
//            }
//            int x = dis.readInt();
//            int y = dis.readInt();
//            Direction dir = Direction.values()[dis.readInt()];
//            String username = dis.readUTF();
//            boolean exist = false;
//            for (Player t : gameClient.getPlayers()) {
//                if (id == t.getID()) {
//                    exist = true;
//                    break;
//                }
//            }
//            if (!exist) {
//                AINewMessage msg = new AINewMessage(gameClient);
//                gameClient.getNetClient().send(msg);
//                MultiPlayerAI newAIPlayer = new MultiPlayerAI(gameClient.getCollisionLayer(), username, x, y, gameClient, dir, gameClient.getMultiPlayerAI().getCo());
//
//                System.out.println("--------------------------------------");
//                System.out.println("my id: "+this.gameClient.getMultiPlayerAI().getId());
//                System.out.println("this player new message is from: id "+id);
//
//                newAIPlayer.setId(id);
//                gameClient.getAiPlayers().add(newAIPlayer);
//                gameClient.playersIdIndexList.put(id, gameClient.getAiPlayers().indexOf(newAIPlayer));
//                System.out.println("I've added this player to list!!");
//                System.out.println("--------------------------------------");
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//}
