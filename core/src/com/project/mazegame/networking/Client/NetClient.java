package com.project.mazegame.networking.Client;

import com.project.mazegame.networking.Messagess.CollectMessage;
import com.project.mazegame.networking.Messagess.Message;
import com.project.mazegame.networking.Messagess.MoveMessage;
import com.project.mazegame.networking.Messagess.PlayerNewMessage;
import com.project.mazegame.screens.MultiPlayerGameScreen;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Random;

public class NetClient {

    private MultiPlayerGameScreen gameClient;
    private int clientUDPPort;
    private int serverUDPPort;
    private String serverIP;
    private DatagramSocket datagramSocket = null;
    private Socket socket = null;
    public static boolean debug=true;

    public int getClientUDPPort() {
        return clientUDPPort;
    }

    public NetClient(MultiPlayerGameScreen gameClient){
        this.gameClient = gameClient;
        this.clientUDPPort=getRandomUDPPort();
    }

    /**
     * Conncet to GameServer,send udp port and IP to GameServer then close tcp socket.
     * @param ip   server IP
     * @param serverTCPPort server TCP port
     */
    public void connect(String ip, int serverTCPPort) {
        serverIP = ip;
        try {
            try {
                if(debug) System.out.println("Client UDP socket have be opened");
                datagramSocket = new DatagramSocket(clientUDPPort);  // UDPSocket
            } catch (SocketException e) {
                e.printStackTrace();
            }
            socket = new Socket(ip, serverTCPPort);   // TCPSocket
            if(debug) printMsg("Connected to server!");

            //Send client udp port to GameServer.
            OutputStream os = socket.getOutputStream();
            DataOutputStream dos = new DataOutputStream(os);
            dos.writeInt(clientUDPPort);
            if(debug) printMsg("I've sent my udp port to Game Server!");

            //Receive an unique ID and Server udp port
            InputStream is = socket.getInputStream();
            DataInputStream dis = new DataInputStream(is);
            int id = dis.readInt();
            this.serverUDPPort = dis.readInt();
            printMsg("Server gives me ID is: " + id + " ,and server UDP Port is: " + serverUDPPort);
            gameClient.getMultiPlayer().setId(id);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (socket != null) {
                    socket.close();  //We do not need this tcp socket anymore.
                    socket = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        PlayerNewMessage msg = new PlayerNewMessage(gameClient.getMultiPlayer());
        send(msg);

        new Thread(new ClientThread()).start();
    }

    public void send(Message msg) {
        msg.send(datagramSocket, serverIP, serverUDPPort);
    }

    /**
     * Inner class
     * For Client to send and receive messages
     */
    public class ClientThread implements Runnable {

        byte[] receiveBuf = new byte[1024];

        @Override
        public void run() {
            if(debug) System.out.println("Client UDP thread start...");
            Thread.currentThread().setName("Client UDP Thread");
            while (null != datagramSocket) {
                DatagramPacket datagramPacket = new DatagramPacket(receiveBuf, receiveBuf.length);
                try {
                    datagramSocket.receive(datagramPacket);
                    process(datagramPacket);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        /**
         * Process the data which rececive from server
         *
         * @param datagramPacket
         */
        private void process(DatagramPacket datagramPacket) {
            ByteArrayInputStream bais = new ByteArrayInputStream(receiveBuf, 0, datagramPacket.getLength());
            DataInputStream dis = new DataInputStream(bais);

            int msgType = 0;
            try {
                msgType = dis.readInt();
            } catch (IOException e) {
                e.printStackTrace();
            }

            Message msg = null;
            switch (msgType) {
                case Message.PLAYER_NEW_MSG:
                    msg = new PlayerNewMessage(gameClient);
                    msg.process(dis);
                    break;
                case Message.PLAYER_MOVE_MSG:
                    msg = new MoveMessage(gameClient);
                    msg.process(dis);
                    break;
                case Message.PLAYER_COLLECT_MSG:
                    msg = new CollectMessage(gameClient);
                    msg.process(dis);
            }
        }
    }

    /**
     ** Randomly generate UDP port from 1024 to 5000
     * @return
     */
    private int getRandomUDPPort(){
        Random random=new Random();
        return random.nextInt(3977)+1024;
        //rand.nextInt(MAX - MIN + 1) + MIN
    }

    /**
     * Only for debug (print message)
     * @param msg
     */
    public void printMsg(String msg){
        System.out.println(msg);
    }

}
