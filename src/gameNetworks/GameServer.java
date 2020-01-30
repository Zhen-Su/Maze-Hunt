package gameNetworks;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class GameServer {
	
	public static final int TCP_PORT=8888;
	List<Client> clients = new ArrayList<>(); // Store all clients in this list
	
	/**
	 * @override
	 */
	public void start(){
		try {
			ServerSocket ss = new ServerSocket(TCP_PORT);
			while(true) {
				Socket s = ss.accept();
				printMsg("Waiting for a client……");
				//Receive udpPort from GameClient to store it.
				DataInputStream dis = new DataInputStream(s.getInputStream());
				int udp_Port=dis.readInt();
				String IP=s.getInetAddress().getHostAddress();//Get client's IP
				Client c = new Client(IP,udp_Port);//create a client object
				clients.add(c); //add this client object to list
printMsg("A Client Connected! Address--" + s.getInetAddress()+":"+s.getPort());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
	}
	
	public static void main(String[] args) {
		new GameServer().start();

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
	 * For debugging
	 * @param msg
	 */
	public static void printMsg(String msg) {
		System.out.println(msg);
	}

}
