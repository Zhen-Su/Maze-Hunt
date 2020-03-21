package com.project.mazegame.networking.Client;

import com.project.mazegame.networking.Messagess.AttackMessage;
<<<<<<< HEAD
import com.project.mazegame.networking.Messagess.CollectMessage;
=======
>>>>>>> origin/yueyi1
import com.project.mazegame.networking.Messagess.ItemCreateMessage;
import com.project.mazegame.networking.Messagess.Message;
import com.project.mazegame.networking.Messagess.MoveMessage;
import com.project.mazegame.networking.Messagess.PlayerExitMessage;
import com.project.mazegame.networking.Messagess.PlayerNewMessage;
import com.project.mazegame.networking.Messagess.StartGameMessage;
import com.project.mazegame.networking.Server.GameServer;
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
<<<<<<< HEAD
    public static boolean debug=true;
=======
    public static boolean debug = true;
>>>>>>> origin/yueyi1

    public int getClientUDPPort() {
        return clientUDPPort;
    }

<<<<<<< HEAD
    public NetClient(MultiPlayerGameScreen gameClient){
        this.gameClient = gameClient;
        this.clientUDPPort=getRandomUDPPort();
=======
    public NetClient(MultiPlayerGameScreen gameClient) {
        this.gameClient = gameClient;
        this.clientUDPPort = getRandomUDPPort();
>>>>>>> origin/yueyi1
    }

    /**
     * Conncet to GameServer,send udp port and IP to GameServer then close tcp socket.
<<<<<<< HEAD
     * @param ip   server IP
     */
    public synchronized void connect(String ip, boolean createAI, int index) {
        serverIP = ip;
        try {
            try {
                if(debug) System.out.println("Client UDP socket have be opened");
=======
     *
     * @param ip server IP
     */
    public void connect(String ip, boolean createAI, int index) {
        serverIP = ip;
        try {
            try {
                if (debug) System.out.println("Client UDP socket have be opened");
>>>>>>> origin/yueyi1
                datagramSocket = new DatagramSocket(clientUDPPort);  // UDPSocket
            } catch (SocketException e) {
                e.printStackTrace();
            }
            socket = new Socket(ip, GameServer.SERVER_TCP_PORT);   // TCPSocket
<<<<<<< HEAD
            if(debug) printMsg("Connected to server!");
=======
            if (debug) printMsg("Connected to server!");
>>>>>>> origin/yueyi1

            //Send client udp port to GameServer.
            OutputStream os = socket.getOutputStream();
            DataOutputStream dos = new DataOutputStream(os);
            dos.writeInt(clientUDPPort);
<<<<<<< HEAD
            if(debug) printMsg("I've sent my udp port to Game Server!");
=======
            if (debug) printMsg("I've sent my udp port to Game Server!");
>>>>>>> origin/yueyi1

            //Receive an unique ID and Server udp port
            InputStream is = socket.getInputStream();
            DataInputStream dis = new DataInputStream(is);
            int id = dis.readInt();
            this.serverUDPPort = dis.readInt();
<<<<<<< HEAD
            if(!createAI) {
                printMsg("Server gives me ID is: " + id + " ,and server UDP Port is: " + serverUDPPort);
                gameClient.getMultiPlayer().setID(id);
            }else{
=======
            if (!createAI) {
                printMsg("Server gives me ID is: " + id + " ,and server UDP Port is: " + serverUDPPort);
                gameClient.getMultiPlayer().setID(id);
            } else {
>>>>>>> origin/yueyi1
                gameClient.aiGameClients.get(index).getAiPlayer().setID(id); //change AI player's id
            }
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


<<<<<<< HEAD
        //TODO need to integrate two kind of message(AI&Player)
        if(createAI) {
            PlayerNewMessage msg = new PlayerNewMessage(gameClient.aiGameClients.get(index).getAiPlayer());
            send(msg);
        }else{
=======
        //TODO send ID,x,y,etc
        if (createAI) {
            PlayerNewMessage msg = new PlayerNewMessage(gameClient.aiGameClients.get(index).getAiPlayer());
            send(msg);
        } else {
>>>>>>> origin/yueyi1
            PlayerNewMessage msg = new PlayerNewMessage(gameClient.getMultiPlayer());
            send(msg);
        }


<<<<<<< HEAD
        new Thread(new ClientThread(createAI,index)).start();
=======
        new Thread(new ClientThread(createAI, index)).start();
>>>>>>> origin/yueyi1
    }

    public void send(Message msg) {
        msg.send(datagramSocket, serverIP, serverUDPPort);
    }

    /**
     * Inner class
     * For Client to receive and process messages
     */
    public class ClientThread implements Runnable {

        byte[] receiveBuf = new byte[1024];
        int aiIndex;
        boolean isAImsg;

<<<<<<< HEAD
        public ClientThread(boolean isAImsg,int aiIndex){
            this.isAImsg=isAImsg;
            this.aiIndex=aiIndex;
=======
        public ClientThread(boolean isAImsg, int aiIndex) {
            this.isAImsg = isAImsg;
            this.aiIndex = aiIndex;
>>>>>>> origin/yueyi1
        }

        @Override
        public void run() {
<<<<<<< HEAD
            if(debug) System.out.println("Client UDP thread start...");
=======
            if (debug) System.out.println("Client UDP thread start...");
>>>>>>> origin/yueyi1
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
         * Process the data which received from server
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
<<<<<<< HEAD
                    if(isAImsg){
                        msg = new PlayerNewMessage(gameClient,gameClient.aiGameClients.get(aiIndex));
                        msg.process(dis,aiIndex);
                    }else{
=======
                    if (isAImsg) {
                        msg = new PlayerNewMessage(gameClient, gameClient.aiGameClients.get(aiIndex));
                        msg.process(dis, aiIndex);
                    } else {
>>>>>>> origin/yueyi1
                        msg = new PlayerNewMessage(gameClient);
                        msg.process(dis);
                    }
                    break;
                case Message.PLAYER_MOVE_MSG:
                    msg = new MoveMessage(gameClient);
                    msg.process(dis);
                    break;
<<<<<<< HEAD
                case Message.PLAYER_COLLECT_MSG:
                    msg = new CollectMessage(gameClient);
                    msg.process(dis);
                    break;
=======
>>>>>>> origin/yueyi1
                case Message.PLAYER_EXIT_MSG:
                    msg = new PlayerExitMessage(gameClient);
                    msg.process(dis);
                    break;
                case Message.ITEMS_CREATE:
                    msg = new ItemCreateMessage(gameClient);
                    msg.process(dis);
                    break;
                case Message.HOST_START:
                    msg = new StartGameMessage(gameClient);
                    msg.process(dis);
                    break;
                case Message.ATTACK_MSG:
                    msg = new AttackMessage(gameClient);
                    msg.process(dis);
                    break;
            }
        }
    }

    /**
<<<<<<< HEAD
     ** Randomly generate UDP port from 1024 to 5000
     * @return
     */
    private int getRandomUDPPort(){
        Random random=new Random();
        return random.nextInt(3977)+1024;
=======
     * * Randomly generate UDP port from 1024 to 5000
     *
     * @return
     */
    private int getRandomUDPPort() {
        Random random = new Random();
        return random.nextInt(3977) + 1024;
>>>>>>> origin/yueyi1
        //rand.nextInt(MAX - MIN + 1) + MIN
    }

    /**
     * Only for debug (print message)
<<<<<<< HEAD
     * @param msg
     */
    public void printMsg(String msg){
        System.out.println(msg);
    }

}
=======
     *
     * @param msg
     */
    public void printMsg(String msg) {
        System.out.println(msg);
    }

}
>>>>>>> origin/yueyi1
