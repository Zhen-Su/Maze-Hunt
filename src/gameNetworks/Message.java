package gameNetworks;

import java.io.DataInputStream;
import java.net.DatagramSocket;

public interface Message {
	
	
	public void send(DatagramSocket ds, String IP, int UDP_Port);
	
	public void process(DataInputStream dis);

}
