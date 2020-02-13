package com.project.mazegame.networking.Server;

import com.project.mazegame.networking.Client.ClientInfo;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * start() is the main method , we need to handle close thread and delete player from clients list.
 *
 */
public class GameServer {

    public static int ID = 1;//ID for every client.
    public static final int SERVER_TCP_PORT = 5555;//Server TCP port
    public static final int SERVER_UDP_PORT = 6666;//Server UDP port
    public List<ClientInfo> clients = new ArrayList<>();//List to store client's info
    private boolean isRunning = false; //server working flag
    private ServerSocket serverSocket =null;
    private Socket s=null;


    /**
     * launch game server
     */
    public void start(){

        //start server udp thread to send and receive data.
        new Thread(new ServerThread(this)).start();

        isRunning = true;
        try {
            serverSocket = new ServerSocket(SERVER_TCP_PORT);
            printMsg("GameServer is working..");
        } catch (IOException e) {
            e.printStackTrace();
        }

        while(isRunning){
            ;
            try {
                printMsg("Waiting for a clinet...");
                s=serverSocket.accept();

                //receive client's UDP Port from GameClient
                InputStream in = s.getInputStream();
                DataInputStream dis = new DataInputStream(in);
                int clientUDPPort = dis.readInt();

                //create a client object
                String clientIP = s.getInetAddress().getHostAddress();
                ClientInfo c = new ClientInfo(clientIP,clientUDPPort,ID);
                clients.add(c);
                printMsg("A Client Connected! Address--" + s.getInetAddress()+":"+s.getPort()+" Client's UDP Port:"+clientUDPPort);

                //send information to client
                OutputStream os = s.getOutputStream();
                DataOutputStream dos = new DataOutputStream(os);
                dos.writeInt(ID++);//Send id to client
                dos.writeInt(GameServer.SERVER_UDP_PORT);//Send server udp port to client
                } catch (IOException e) {
                 e.printStackTrace();
            }finally {
                if(s!=null){
                    try {
                        //we don't need this tcp socket,so close it.
                        s.close();
                        s=null;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

    }

    /**
     * Stop all threads
     */
    public void shutdown() {
        isRunning = false;
        //Close all Clients

        clients.clear();
        clients=null;
        //Close Server
        if(serverSocket!=null) {
            try {
                serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }



    /**
     * Only for debug (print message)
     * @param msg
     */
    public void printMsg(String msg){
        System.out.println(msg);
    }
}
