package com.project.mazegame.networking.Messagess;

import com.badlogic.gdx.Game;
import com.project.mazegame.objects.MultiPlayer;
import com.project.mazegame.screens.HostLobbyScreen;
import com.project.mazegame.screens.MultiPlayerGameScreen;
import com.project.mazegame.screens.OtherLobbyScreen;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;

/**
 * When host player "enter", and then tell everyone he wanna start
 */
public class StartGameMessage implements Message{
    public int msgType = Message.HOST_START;
    public boolean HostStartGame;
    private int id;
    private MultiPlayerGameScreen gameClient;
    private boolean debug =false;

    public StartGameMessage(MultiPlayerGameScreen gameClient) { this.gameClient = gameClient;}

    public StartGameMessage(MultiPlayerGameScreen gameClient,boolean HostStartGame,int id)
    {
        this(gameClient);
        this.id=id;
        this.HostStartGame = HostStartGame;
    }




    /**
     * When host player click start, then it message will send other players, and start game.
     * @param ds
     * @param serverIP Server ip
     * @param serverUDPPort
     * @throws Exception This exception is thrown when closing the stream fails
     */
    @Override
    public void send(DatagramSocket ds, String serverIP, int serverUDPPort) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        try{
            dos.writeInt(msgType);
            dos.writeInt(id);
            dos.writeBoolean(HostStartGame);
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] buf = baos.toByteArray();
        try {
            DatagramPacket datagramPacket = new DatagramPacket(buf, buf.length, new InetSocketAddress(serverIP, serverUDPPort));
            System.out.println("I'm id:"+id+"I'll send a start game message.");
            ds.send(datagramPacket);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * To process start game message when host player click start game.
     * @param dis
     * @throws Exception This exception is thrown when closing the stream fails
     */
    @Override
    public void process(DataInputStream dis) {
        try{

            int id = dis.readInt();
            if(id == this.gameClient.getMultiPlayer().getID()){
                return;
            }

            boolean start = dis.readBoolean();
            //set my HostStartGame true
            gameClient.setHostStartGame(start);

            if(debug) {
                System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
                System.out.println("My id: " + this.gameClient.getMultiPlayer().getID());
                System.out.println("This start game message is from: id" + id);
                System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
            }

        }catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void process(DataInputStream dis, int aiIndex) {

    }
}
