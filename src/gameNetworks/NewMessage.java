package gameNetworks;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;

/**
 * When the player connects to serve, send the current position, ID and other information to the server through udp.
 * @author kevin
 *
 */
public class NewMessage implements Message{
	
	public NewMessage() {
		
	}

	@Override
	 public void send(DatagramSocket ds, String IP, int UDP_Port){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        try {
            dos.writeChars("Hello everyone, i'm your opponent.");
        } catch (IOException e) {
            e.printStackTrace();
        }

        byte[] buf = baos.toByteArray();
        try{
            DatagramPacket dp = new DatagramPacket(buf, buf.length, new InetSocketAddress(IP, UDP_Port));
            ds.send(dp);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

	@Override
	public void process(DataInputStream dis) {
		// TODO Auto-generated method stub
		
	}
	
	

}
