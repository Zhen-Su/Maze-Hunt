package gameNetworks;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import messages.Message;
import messages.MoveMessage;
import messages.NewMessage;

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
			OutputStream os = socket.getOutputStream();
			DataOutputStream dos = new DataOutputStream(os);
			dos.writeInt(clientUDPPort);
printMsg("I've sent my udp port to Game Server!"); 

			//Receive an unique ID and Server udp port 
			InputStream is = socket.getInputStream();
			DataInputStream dis = new DataInputStream(is);
			int id = dis.readInt(); /*Here we only received an ID, but need assign this ID to PLAYER(INTEGRATION WITH PLAYER CLASS)!!! */
			this.serverUDPPort = dis.readInt();
printMsg("Server gives me ID is: "+id+" ,and server UDP Port is: "+serverUDPPort); 
			gc.getPlayer().id=id;
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			try {
				if(socket!=null) {
				socket.close();  //We do not need this tcp socket anymore.
				socket=null;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		//Send a message to the server when the player just connect to server.
		NewMessage msg = new NewMessage(gc.getPlayer());
        send(msg);
		
		new Thread(new UDPReceiveThread()).start();
		
	}
	
	/**
	 * Send messages to Server, then Server can broadcast it to every clients.
	 * @param msg
	 */
	public void send(Message msg) {
		msg.send(ds, serverIP, serverUDPPort);
	}
	
	/**
	 * For debugging
	 * @param msg
	 */
	public void printMsg(String msg) {
		System.out.println(msg);
	}
	
	/**
	 * udp receive inner class
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
			ByteArrayInputStream bais = new ByteArrayInputStream(receiveBuf, 0, dp.getLength());
            DataInputStream dis = new DataInputStream(bais);
            int msgType = 0;
            try {
                msgType = dis.readInt();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Message msg = null;
            switch (msgType){
                case Message.PLAYER_NEW_MSG:
                    msg = new NewMessage(gc);
                    msg.process(dis);
                    break;
                case  Message.PLAYER_MOVE_MSG:
                    msg = new MoveMessage(gc);
                    msg.process(dis);
                    break;
		}	
	}
}
}
