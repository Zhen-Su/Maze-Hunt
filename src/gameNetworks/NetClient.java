package gameNetworks;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * This class handle online players
 * @author kevin
 *
 */
public class NetClient {
	
	private static int UDP_PORT_START = 2000;
	private int udpPort;
	
	public NetClient() {
		//DO not need to think sychronization here.
		udpPort = UDP_PORT_START++;
	}
	
	/**
	 * Conncet to GameServer,send udp port and IP to GameServer then close tcp socket.
	 * @param IP
	 * @param port
	 */
	public void connect(String IP, int port) {
		Socket s =null;
		try {
			s = new Socket(IP,port);
printMsg("Connected to server"); 
			//Send udpPort to GameServer.
			DataOutputStream dos = new DataOutputStream(s.getOutputStream());
			dos.writeInt(udpPort);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			try {
				s.close(); //We do not need this tcp socket anymore.
				s=null;
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
