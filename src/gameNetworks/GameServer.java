package gameNetworks;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class GameServer {
	
	private static int ID= 0001;   //every client has an unique ID.
	public static final int TCP_PORT=8888;
	static List<Client> clients = new ArrayList<>(); // Store all clients in this list
	
/**
 * Start game! (NOT A THREAD)
 */
	public void start(){
		ServerSocket ss =null;
		try {
			ss = new ServerSocket(TCP_PORT);
		}catch(IOException e) {
			e.printStackTrace();
		}
			while(true) {
				Socket s =null;
				try {
					printMsg("Waiting for a client……");
					s= ss.accept();
					//Receive udpPort from GameClient to store it.
					DataInputStream dis = new DataInputStream(s.getInputStream());
					int udp_Port=dis.readInt();
					String IP=s.getInetAddress().getHostAddress();//Get client's IP
					Client c = new Client(IP,udp_Port);//create a client object
					clients.add(c); //add this client object to list
					printMsg("A Client Connected! Address--" + s.getInetAddress()+":"+s.getPort()+" udpPort:"+udp_Port);
//					for(Client l :clients) {
//						System.out.println("Client: IP--"+l.IP+"  udpPort--"+l.udp_Port);
//					}
					//Server send an unique ID to client
					DataOutputStream dos = new DataOutputStream(s.getOutputStream());
					dos.writeInt(ID++);
				   } catch (IOException e) {
					e.printStackTrace();
				}finally {
					try {
						if(s!=null) {
						s.close();
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
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
