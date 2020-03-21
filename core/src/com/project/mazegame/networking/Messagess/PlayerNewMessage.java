<<<<<<< HEAD
<<<<<<< HEAD
package com.project.mazegame.networking.Messagess;


=======
package com.project.mazegame.networking.Messagess;


import com.project.mazegame.objects.AIGameClient;
import com.project.mazegame.objects.AIPlayer;
>>>>>>> origin/yueyi1
import com.project.mazegame.objects.Direction;
import com.project.mazegame.objects.MultiPlayer;
import com.project.mazegame.objects.Player;
import com.project.mazegame.screens.MultiPlayerGameScreen;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;

public class PlayerNewMessage implements Message {


    private int msgType = Message.PLAYER_NEW_MSG;
<<<<<<< HEAD
    private MultiPlayer multiPlayer;
    private MultiPlayerGameScreen gameClient;

    public PlayerNewMessage(MultiPlayer multiPlayer) {
        this.multiPlayer = multiPlayer;
=======
    private Player player;
    private MultiPlayerGameScreen gameClient;
    private AIGameClient aigameClient;

    public PlayerNewMessage(Player player) {
        this.player = player;
>>>>>>> origin/yueyi1
    }

    public PlayerNewMessage(MultiPlayerGameScreen gameClient) {
        this.gameClient = gameClient;
<<<<<<< HEAD
        multiPlayer = gameClient.getMultiPlayer();
    }


=======
        player = gameClient.getMultiPlayer();
    }

    public PlayerNewMessage(MultiPlayerGameScreen gameClient,AIGameClient aiGameClient){
        this.aigameClient=aiGameClient;
        player=aiGameClient.getAiPlayer();
        this.gameClient = gameClient;
    }
>>>>>>> origin/yueyi1

    /**
     * Send a packet to Server,then Server broadcast this packet to all clients
     *
     * @param datagramSocket
     * @param ip  Server ip
     * @param server_UDP_Port
     */
    @Override
    public void send(DatagramSocket datagramSocket, String ip, int server_UDP_Port) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        try {
            dos.writeInt(msgType);
<<<<<<< HEAD
            dos.writeInt(multiPlayer.getID());
            dos.writeInt(multiPlayer.position.getX());
            dos.writeInt(multiPlayer.position.getY());
            dos.writeInt(multiPlayer.getDir().ordinal());
            dos.writeUTF(multiPlayer.getName());
=======
            dos.writeInt(player.getID());
            dos.writeInt(player.position.getX());
            dos.writeInt(player.position.getY());
            dos.writeInt(player.getDir().ordinal());
            dos.writeUTF(player.getName());
            if(player instanceof AIPlayer){
                dos.writeBoolean(true);
            }else{
                dos.writeBoolean(false);
            }
            dos.writeUTF(player.getColour());
>>>>>>> origin/yueyi1
        } catch (IOException e) {
            e.printStackTrace();
        }

        byte[] buf = baos.toByteArray();
        try {
            DatagramPacket datagramPacket = new DatagramPacket(buf, buf.length, new InetSocketAddress(ip, server_UDP_Port));
            datagramSocket.send(datagramPacket);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void process(DataInputStream dis) {
        try{
<<<<<<< HEAD

=======
>>>>>>> origin/yueyi1
            int id = dis.readInt();
            if(id == this.gameClient.getMultiPlayer().getID()){
                return;
            }

            int x = dis.readInt();
            int y = dis.readInt();
            Direction dir = Direction.values()[dis.readInt()];
            String username = dis.readUTF();
<<<<<<< HEAD
=======
            boolean isAIPlayer = dis.readBoolean();
            String colour = dis.readUTF();
>>>>>>> origin/yueyi1

            boolean exist = false;
            for (Player t : gameClient.getPlayers()){
                if(id == t.getID()){
                    exist = true;
                    break;
                }
            }
            if(!exist) {
<<<<<<< HEAD
                PlayerNewMessage msg = new PlayerNewMessage(gameClient);
                gameClient.getNc().send(msg);
                MultiPlayer newPlayer = new MultiPlayer(gameClient.getCollisionLayer(),username,x,y,gameClient,dir);

                System.out.println("--------------------------------------");
                System.out.println("my id: "+this.gameClient.getMultiPlayer().getID());
                System.out.println("this player new message is from: id "+id);

                newPlayer.setID(id);
                gameClient.getPlayers().add(newPlayer);
                gameClient.playersIdIndexList.put(id, gameClient.getPlayers().indexOf(newPlayer));
=======
                //if i find this player isn't in my players list, so this player is a new player, then i will send my info to he/she
                PlayerNewMessage msg = new PlayerNewMessage(gameClient);
                gameClient.getNc().send(msg);

                System.out.println("--------------------------------------");
                System.out.println("my id: "+this.gameClient.getMultiPlayer().getID());
                System.out.println("I'm a Real player: this player new message is from: id "+id);

                if(!isAIPlayer) {
                    MultiPlayer newPlayer = new MultiPlayer(gameClient.getCollisionLayer(), username, x, y, gameClient, dir,colour);
                    newPlayer.setID(id);
                    System.out.println("id"+id+" 's position: ("+x+","+y+")");
                    gameClient.getPlayers().add(newPlayer);
                    gameClient.playersIdIndexList.put(id, gameClient.getPlayers().indexOf(newPlayer));
                }else {
                    AIPlayer newAIPlayer = new AIPlayer(gameClient.getCollisionLayer(),username,id,"red",dir);
                    newAIPlayer.setID(id);
                    gameClient.getPlayers().add(newAIPlayer);
                    gameClient.playersIdIndexList.put(id, gameClient.getPlayers().indexOf(newAIPlayer));
                }
>>>>>>> origin/yueyi1

                System.out.println("I've added this player to list!!");
                System.out.println("--------------------------------------");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

<<<<<<< HEAD
}
=======
package com.project.mazegame.networking.Messagess;


import com.project.mazegame.objects.Direction;
import com.project.mazegame.objects.MultiPlayer;
import com.project.mazegame.objects.Player;
import com.project.mazegame.screens.MultiPlayerGameScreen;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;

public class PlayerNewMessage implements Message {


    private int msgType = Message.PLAYER_NEW_MSG;
    private MultiPlayer multiPlayer;
    private MultiPlayerGameScreen gameClient;

    public PlayerNewMessage(MultiPlayer multiPlayer) {
        this.multiPlayer = multiPlayer;
    }

    public PlayerNewMessage(MultiPlayerGameScreen gameClient) {
        this.gameClient = gameClient;
        multiPlayer = gameClient.getMultiPlayer();
    }



    /**
     * Send a packet to Server,then Server broadcast this packet to all clients
     *
     * @param datagramSocket
     * @param ip  Server ip
     * @param server_UDP_Port
     */
    @Override
    public void send(DatagramSocket datagramSocket, String ip, int server_UDP_Port) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        try {
            dos.writeInt(msgType);
            dos.writeInt(multiPlayer.getId());
            dos.writeInt(multiPlayer.position.getX());
            dos.writeInt(multiPlayer.position.getY());
            dos.writeInt(multiPlayer.getDir().ordinal());
            dos.writeUTF(multiPlayer.getName());
        } catch (IOException e) {
            e.printStackTrace();
        }

        byte[] buf = baos.toByteArray();
        try {
            DatagramPacket datagramPacket = new DatagramPacket(buf, buf.length, new InetSocketAddress(ip, server_UDP_Port));
            datagramSocket.send(datagramPacket);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void process(DataInputStream dis) {
        try{

            int id = dis.readInt();
            if(id == this.gameClient.getMultiPlayer().getId()){
=======
    @Override
    public void process(DataInputStream dis,int aiIndex) {
        try {
            int id = dis.readInt();
            if(id == this.aigameClient.getAiPlayer().getID()){
>>>>>>> origin/yueyi1
                return;
            }

            int x = dis.readInt();
            int y = dis.readInt();
            Direction dir = Direction.values()[dis.readInt()];
            String username = dis.readUTF();
<<<<<<< HEAD

            boolean exist = false;
            for (MultiPlayer t : gameClient.getPlayers()){
                if(id == t.getId()){
=======
            boolean isAIPlayer = dis.readBoolean();
            String colour = dis.readUTF();

            boolean exist = false;
            for (Player t : aigameClient.getPlayers()){
                if(id == t.getID()){
>>>>>>> origin/yueyi1
                    exist = true;
                    break;
                }
            }
<<<<<<< HEAD
            if(!exist) {
                PlayerNewMessage msg = new PlayerNewMessage(gameClient);
                gameClient.getNc().send(msg);
                MultiPlayer newPlayer = new MultiPlayer(gameClient.getCollisionLayer(),username,x,y,gameClient,dir);

                System.out.println("--------------------------------------");
                System.out.println("my id: "+this.gameClient.getMultiPlayer().getId());
                System.out.println("this player new message is from: id "+id);

                newPlayer.setId(id);
                gameClient.getPlayers().add(newPlayer);
                gameClient.playersIdIndexList.put(id, gameClient.getPlayers().indexOf(newPlayer));

                System.out.println("I've added this player to list!!");
                System.out.println("--------------------------------------");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
>>>>>>> origin/yueyi1
=======
            if(!exist){
                //if i find this player isn't in my players list, so this player is a new player, then i will send my info to he/she
                PlayerNewMessage msg = new PlayerNewMessage(gameClient,aigameClient);
                aigameClient.getNetClient().send(msg);

                System.out.println("--------------------------------------");
                System.out.println("my id: "+aigameClient.getAiPlayer().getID());
                System.out.println("I'm an AI player: this player new message is from: id "+id);

                if(!isAIPlayer) {
                    MultiPlayer newPlayer = new MultiPlayer(gameClient.getCollisionLayer(), username, x, y, gameClient, dir,colour);
                    newPlayer.setID(id);
                    aigameClient.getPlayers().add(newPlayer);
                    aigameClient.playersIdIndexList.put(id, gameClient.getPlayers().indexOf(newPlayer));
                }else {
                    AIPlayer newAIPlayer = new AIPlayer(gameClient.getCollisionLayer(),username,id,"red",dir);
                    newAIPlayer.setID(id);
                    aigameClient.getPlayers().add(newAIPlayer);
                    aigameClient.playersIdIndexList.put(id, gameClient.getPlayers().indexOf(newAIPlayer));
                }

                System.out.println("I've added this player to list!!");
                System.out.println("--------------------------------------");

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
>>>>>>> origin/yueyi1
