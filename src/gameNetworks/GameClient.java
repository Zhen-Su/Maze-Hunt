package gameNetworks;


/**
 * 
 * @author kevin
 *
 */
public class GameClient {

	private static NetClient nc = new NetClient();
	

	
	public static void main(String[] args) {
		nc.connect("10.222.143.1",GameServer.TCP_PORT);
	}
	
}
