package com.project.mazegame.networking.Server;

import com.project.mazegame.networking.Messagess.PlayerExitMessage;
import com.project.mazegame.objects.Player;
import com.project.mazegame.screens.MultiPlayerGameScreen;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * GameServer class,every send and process on here, every message will process on server.
 * @author Yueyi Wang & Zhen Su
 */
public class GameServer implements Runnable {

    private static int ID = 0001;                    //every client has an unique ID.
    public static final int SERVER_TCP_PORT = 9999;
    public static final int SERVER_UDP_PORT = 7777;
    public static final int PLAYER_EXIT_UDP_PORT = 8888;
    private static List<Client> clients = new CopyOnWriteArrayList<>(); // To store all client's IP and UDP_Port
    private boolean isRunning = false;
    private ServerSocket serverSocket;
    private Socket s;
    private boolean debug = false;
    private String map;
    private MultiPlayerGameScreen gameClient;


    public void setGameClient(MultiPlayerGameScreen gameClient) {
        this.gameClient = gameClient;
    }

    @Override
    public void run() {

        isRunning = true;

        new Thread(new UDPThread()).start();
        new Thread(new PlayerExitUDPThread()).start();

        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(SERVER_TCP_PORT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        while (isRunning) {
            Socket s = null;
            try {
                printMsg("=======================Game-Server===========================");
                printMsg("Waiting for a client");
                Thread.currentThread().setName("Server TCP Thread");
                s = serverSocket.accept();//Listens for a connection to be made to this socket and accepts it.

                //Receive client's UDP Port from GameClient
                InputStream in = s.getInputStream();
                DataInputStream dis = new DataInputStream(in);
                int clientUDPPort = dis.readInt();

                String clientIP = s.getInetAddress().getHostAddress();
                Client c = new Client(clientIP, clientUDPPort);//create a client object
                clients.add(c);                   //add this client object to list
                printMsg("A Client Connected! Address--" + s.getInetAddress() + ":" + s.getPort() + " Client's UDP Port:" + clientUDPPort);
                printMsg("===============================================================");
                //Send ID and Server UDP_PORT to client.
                OutputStream os = s.getOutputStream();
                DataOutputStream dos = new DataOutputStream(os);
                dos.writeInt(ID);
                dos.writeInt(SERVER_UDP_PORT);
                dos.writeInt(PLAYER_EXIT_UDP_PORT);
                //if this player is a host player,the save the map info received from client
                if (ID == 1) {
                    this.map = dis.readUTF();
                } else {
                    dos.writeUTF(map);
                }
                ID++;
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (s != null) s.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Inner class for Player exit udp thread
     */
    private class PlayerExitUDPThread implements Runnable {
        byte[] buf = new byte[300];

        @Override
        public void run() {
            DatagramSocket ds = null;
            try {
                ds = new DatagramSocket(PLAYER_EXIT_UDP_PORT);
            } catch (SocketException e) {
                e.printStackTrace();
            }
            while (null != ds) {
                DatagramPacket dp = new DatagramPacket(buf, buf.length);
                ByteArrayInputStream bais = null;
                DataInputStream dis = null;
                try {
                    ds.receive(dp);
                    bais = new ByteArrayInputStream(buf, 0, dp.getLength());
                    dis = new DataInputStream(bais);
                    int ID = dis.readInt();
                    int UDPPort = dis.readInt();
                    //remove this client from list.
                    for (int i = 0; i < clients.size(); i++) {
                        Client c = clients.get(i);
                        if (c.udp_Port == UDPPort) {
                            clients.remove(c);
                        }
                    }
                    //remove this player from host players list
//                    int indexOfExitPlayer = gameClient.playersIdIndexList.get(ID);
//                    gameClient.getPlayers().remove(indexOfExitPlayer);
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (null != dis) {
                        try {
                            dis.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (null != bais) {
                        try {
                            bais.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }


    /**
     * Client inner class
     *
     * @author Yueyi Wang
     */
    private class Client {
        String IP;
        int udp_Port;

        public Client(String IP, int udp_Port) {
            this.IP = IP;
            this.udp_Port = udp_Port;
        }
    }

    /**
     * This inner class will listen to whether client send messages to me,and send those messages to other clients
     * Broadcast to other clients
     *
     * @author Yueyi Wang
     */
    private class UDPThread implements Runnable {

        public DatagramSocket ds = null;
        byte[] receiveBuffer = new byte[1024];

        @Override
        public void run() {
            Thread.currentThread().setName("Server UDP Thread");
            //DatagramSocket ds =null;
            try {
                ds = new DatagramSocket(SERVER_UDP_PORT);
            } catch (SocketException e) {
                e.printStackTrace();
            }
            printMsg("UDP thread started at port: " + SERVER_UDP_PORT);
            while (ds != null) {
                DatagramPacket dp = new DatagramPacket(receiveBuffer, receiveBuffer.length);
                try {
                    ds.receive(dp);
                    // printMsg("I received a packet from a client, and i will broadcast to all clients!!!");

                    //Use CopyOnWriteArrayList to prevent ConcurrentModificationException
                    for (Client c : clients) {
                        if (c != null) {
                            dp.setSocketAddress(new InetSocketAddress(c.IP, c.udp_Port));
                            ds.send(dp);
                            //printMsg("I've broadcasted to client");
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }

    }

    /**
     * Stop all threads
     */
    public void dispose(MultiPlayerGameScreen gameClient,boolean hasSentExitMsg) {
        isRunning = false;

        if(!hasSentExitMsg) {
            //Send message to all client the server will close.
            PlayerExitMessage message = new PlayerExitMessage(gameClient, gameClient.getMultiPlayer().getID());
            gameClient.getNc().send(message);
            gameClient.getNc().sendClientDisconnectMsg();
        }
        try {
            Thread.currentThread().sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //Close all Clients
        clients.clear();
        clients = null;
        //Close TCP Server
        if (serverSocket != null) {
            try {
                s.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Dispose server and clients...");
    }

    /**
     * For debugging
     *
     * @param msg
     */
    public static void printMsg(String msg) {
        System.out.println(msg);
    }

}
