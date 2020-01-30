package gameNetworks;


/**
 * 
 * @author kevin
 *
 */
public class GameClient {

	private static NetClient nc = new NetClient();
	

	
	public static void main(String[] args) {
		//In MVP, we need to enter Server IP manually
		nc.connect("10.222.143.1",GameServer.TCP_PORT);
	}
	//public IP: 31.205.219.70
	//private ip:10.222.143.1
}
