package gameNetworks;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * This class handle online players
 * @author kevin
 *
 */
public class NetClient {
	
	private int clientUDPPort;
	private int serverUDPPort;
	private GameClient gc;
	private Socket socket;
	private String serverIP;
	private DatagramSocket ds = null;
	
	
	public NetClient(GameClient gc){
        this.gc = gc;
    }
	
	public int getClientUDPPort() {
		return clientUDPPort;
	}

	public void setClientUDPPort(int clientUDPPort) {
		this.clientUDPPort = clientUDPPort;
	}
	
	/**
	 * Conncet to GameServer,send udp port and IP to GameServer then close tcp socket.
	 * @param ip   server IP
	 * @param port server TCP port
	 */
	public void connect(String ip, int serverTCPPort) {
		serverIP=ip;
		Socket socket = null;
		try {
			ds = new DatagramSocket(clientUDPPort);  // UDPSocket 
			socket = new Socket(ip,serverTCPPort);   // TCPSocket
printMsg("Connected to server"); 

			//Send client udp port to GameServer.
			DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
			dos.writeInt(clientUDPPort);
printMsg("I've sent my udp port to Game Server!"); 

			//Receive an unique ID and Server udp port 
			DataInputStream dis = new DataInputStream(socket.getInputStream());
			int id = dis.readInt(); /*Here we only received an ID, but need assign this ID to PLAYER(INTEGRATION WITH PLAYER CLASS)!!! */
			this.serverUDPPort = dis.readInt();
printMsg("Server gives me ID is: "+id+" ,and server UDP Port is: "+serverUDPPort); 

		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			try {
				socket.close();  //We do not need this tcp socket anymore.
				socket=null;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		//Send a message to the server when the player just connect to server.
		NewMessage msg = new NewMessage();
		send(msg);
		
		new Thread(new UDPReceiveThread()).start();
		
	}
	/**
	 * Send messages to Server, then Server can broadcast it to every clients.
	 * @param msg
	 */
	private void send(NewMessage msg) {
		msg.send(ds, serverIP, serverUDPPort);
		
	}
	
	/**
	 * 
	 * @author kevin
	 *
	 */
	private class UDPReceiveThread implements Runnable{
		
		 byte[] receiveBuf = new byte[1024];

        @Override
        public void run() {
            while(null != ds){
                DatagramPacket dp = new DatagramPacket(receiveBuf, receiveBuf.length);
                try{
                    ds.receive(dp);
                    process(dp);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
/**
 * Process packet which received from Server
 * @param dp
 */
		private void process(DatagramPacket dp) {
			ByteArrayInputStream bais = new ByteArrayInputStream(receiveBuf,0,receiveBuf.length);
			DataInputStream dis = new DataInputStream(bais);
			//Use Polymorphism and switch here! (Message msg= new NewMessage();/ Message msg= new AckMessage();)
			NewMessage msg = new NewMessage();
			msg.process(dis);
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
