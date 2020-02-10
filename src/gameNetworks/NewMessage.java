package gameNetworks;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;

import messages.Message;

/**
 * When the player connects to serve, send the current position, ID and other information to the server through udp.
 * @author kevin
 *
 */
public class NewMessage implements Message{
	
	public NewMessage() {
		
	}
	
	/**
	 * Send a packet to Server,then Server broadcast this packet to all clients
	 * @param ds
	 * @param ip
	 * @param server_UDP_Port
	 */
	@Override
	 public void send(DatagramSocket ds, String ip, int server_UDP_Port){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        try {
            dos.writeBytes("Hello everyone");
        } catch (IOException e) {
            e.printStackTrace();
        }

        byte[] buf = baos.toByteArray();
        try{
            DatagramPacket dp = new DatagramPacket(buf, buf.length, new InetSocketAddress(ip, server_UDP_Port));
            ds.send(dp);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

	@Override
	public void process(DataInputStream dis) {
		int length;
		byte[] array;
		try {
			length = dis.readInt();
			array = new byte[length];
			dis.read(array);
			for(byte b : array) {
				char c =(char)b;
				System.out.print(c+"");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		
		 
	}
	
	

}
