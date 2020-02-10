package messages;

import java.io.DataInputStream;
import java.net.DatagramSocket;

public interface Message {
	
	public static final int PLAYER_NEW_MSG = 1;
    public static final int PLAYER_MOVE_MSG= 2;
    public static final int PLAYER_DEAD_MESSAGE = 5;
	
	
	public void send(DatagramSocket ds, String ip, int server_UDP_Port);
	
	public void process(DataInputStream dis);

}
