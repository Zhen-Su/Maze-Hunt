package gameNetworks;

import java.util.Scanner;

/**
 * 
 * @author kevin
 *
 */
public class GameClient {

	private NetClient nc = new NetClient(this);

	
	public void startClient() {
		
		//In MVP, we need to enter Server IP manually
		Scanner sc = new Scanner(System.in); 
		System.out.println("Please enter Server IP: ");
		String serverIP = sc.nextLine();
		System.out.println("Please enter your UDP Port (Any Number): ");
		int clientUDPPort = sc.nextInt();
				
		nc.connect(serverIP,GameServer.SERVER_TCP_PORT);
		nc.setClientUDPPort(clientUDPPort);
				
		sc.close();
		//public IP: 8.208.8.136
		//private ip:10.222.143.1
	}
	

	
	public static void main(String[] args) {
		new GameClient().startClient();
	}
	
}
