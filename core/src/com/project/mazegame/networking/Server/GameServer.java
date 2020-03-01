package com.project.mazegame.networking.Server;
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
import java.util.ArrayList;
import java.util.List;

public class GameServer {

    private static int ID= 0001;                    //every client has an unique ID.
    public static final int SERVER_TCP_PORT=9999;
    public static final int SERVER_UDP_PORT=7777;
    static List<Client> clients = new ArrayList<>(); // To store all client's IP and UDP_Port
    private boolean isRunning = false;
    private ServerSocket serverSocket;
    private Socket s;


    /**
     * Main method
     * @param args
     */
    public static void main(String[] args) {
        new GameServer().start();
    }

    /**
     * Start game! (NOT A THREAD)
     * For the final integration, remove main() method in this class, and put this start() method into constructor
     */
    @SuppressWarnings("resource")
    public void start(){

        isRunning = true;

        new Thread(new UDPThread()).start();

        ServerSocket serverSocket =null;
        try {
            serverSocket = new ServerSocket(SERVER_TCP_PORT);
        }catch(IOException e) {
            e.printStackTrace();
        }
        while(isRunning) {
            Socket s =null;
            try {
                printMsg("Waiting for a client��");
                s= serverSocket.accept();//Listens for a connection to be made to this socket and accepts it.

                //Receive client's UDP Port from GameClient
                InputStream in = s.getInputStream();
                DataInputStream dis = new DataInputStream(in);
                int clientUDPPort = dis.readInt();

                String clientIP = s.getInetAddress().getHostAddress();
                Client c = new Client(clientIP,clientUDPPort);//create a client object
                clients.add(c);                   //add this client object to list
                printMsg("A Client Connected! Address--" + s.getInetAddress()+":"+s.getPort()+" Client's UDP Port:"+clientUDPPort);

                //Send ID and Server UDP_PORT to client.
                OutputStream os = s.getOutputStream();
                DataOutputStream dos = new DataOutputStream(os);
                dos.writeInt(ID++);
                dos.writeInt(SERVER_UDP_PORT);
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                try {
                    if(s!=null) s.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }



    /**
     * Client inner class
     * @author kevin
     *
     */
    private class Client{
        String IP;
        int udp_Port;

        public Client(String IP,int udp_Port) {
            this.IP=IP;
            this.udp_Port=udp_Port;
        }
    }

    /**
     * This inner class will listen to whether client send messages to me,and send those messages to other clients
     * Broadcast to other clients
     * @author kevin
     *
     */
    private class UDPThread implements Runnable{

        byte[] receiveBuffer = new byte[1024];

        @Override
        public void run() {
            DatagramSocket ds =null;
            try {
                ds = new DatagramSocket(SERVER_UDP_PORT);
            } catch (SocketException e) {
                e.printStackTrace();
            }
            printMsg("UDP thread started at port: "+SERVER_UDP_PORT);
            while(ds!=null) {
                DatagramPacket dp = new DatagramPacket(receiveBuffer,receiveBuffer.length);
                try {
                    ds.receive(dp);
                    printMsg("I received a packet from a client, and i will broadcast to all clients!!!");
                    for (Client c : clients){
                        if(c!=null) {
                            dp.setSocketAddress(new InetSocketAddress(c.IP, c.udp_Port));
                            ds.send(dp);
                            printMsg("I've broadcasted to client");
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
    public void dispose() {
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
     * For debugging
     * @param msg
     */
    public static void printMsg(String msg) {
        System.out.println(msg);
    }

}