package com.project.mazegame.networking.Client;

import com.project.mazegame.networking.Messages.Message;
import com.project.mazegame.networking.Messages.PlayerNewMessage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.DatagramSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Random;

public class NetClient {

    private GameClient gc;
    private int clientUDPPort;
    private int serverUDPPort;
    private String serverIP;
    public DatagramSocket ds = null;
    private Socket socket;

    public int getClientUDPPort() {
        return clientUDPPort;
    }

    public NetClient(GameClient gc){
        this.gc = gc;
        this.clientUDPPort=getRandomUDPPort();
        try {
            ds = new DatagramSocket(clientUDPPort);  // UDPSocket
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    /**
     * Conncet to GameServer,send udp port and IP to GameServer then close tcp socket.
     * @param ip   server IP
     * @param serverTCPPort server TCP port
     */
    public void connect(String ip, int serverTCPPort) {
        serverIP = ip;
        Socket socket = null;
        try {

            socket = new Socket(ip, serverTCPPort);   // TCPSocket
            printMsg("Connected to server");

            //Send client udp port to GameServer.
            OutputStream os = socket.getOutputStream();
            DataOutputStream dos = new DataOutputStream(os);
            dos.writeInt(clientUDPPort);
            printMsg("I've sent my udp port to Game Server!");

            //Receive an unique ID and Server udp port
            InputStream is = socket.getInputStream();
            DataInputStream dis = new DataInputStream(is);
            int id = dis.readInt();
            this.serverUDPPort = dis.readInt();
            printMsg("Server gives me ID is: " + id + " ,and server UDP Port is: " + serverUDPPort);
            gc.getPlayer().setId(id);

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

        new Thread(new ClientThread(this)).start();

        PlayerNewMessage msg = new PlayerNewMessage(gc.getPlayer());
        send(msg);


    }

    public void send(Message msg) {
        msg.send(ds, serverIP, serverUDPPort);

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
