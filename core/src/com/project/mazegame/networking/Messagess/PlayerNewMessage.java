package com.project.mazegame.networking.Messagess;


import com.project.mazegame.objects.AIGameClient;
import com.project.mazegame.objects.AIPlayer;
import com.project.mazegame.objects.Direction;
import com.project.mazegame.objects.MultiAIPlayer;
import com.project.mazegame.objects.MultiPlayer;
import com.project.mazegame.objects.Player;
import com.project.mazegame.screens.MultiPlayerGameScreen;
import com.project.mazegame.tools.PlayersType;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
/**
 * To send new player joining message through server and client
 * @author Yueyi Wang & Zhen Su
 */
public class PlayerNewMessage implements Message {


    private int msgType = Message.PLAYER_NEW_MSG;
    private Player player;
    private MultiPlayerGameScreen gameClient;
    private AIGameClient aigameClient;

    public PlayerNewMessage(Player player) {
        this.player = player;
    }

    public PlayerNewMessage(MultiPlayerGameScreen gameClient) {
        this.gameClient = gameClient;
        player = gameClient.getMultiPlayer();
    }

    public PlayerNewMessage(MultiPlayerGameScreen gameClient, AIGameClient aiGameClient) {
        this.aigameClient = aiGameClient;
        player = aiGameClient.getAiPlayer();
        this.gameClient = gameClient;
    }

    /**
     * Send a packet to Server,then Server broadcast this packet to all clients
     *
     * @param datagramSocket
     * @param ip Server ip
     * @param server_UDP_Port
     * @throws Exception This exception is thrown when closing the stream fails
     */
    @Override
    public void send(DatagramSocket datagramSocket, String ip, int server_UDP_Port) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        try {
            dos.writeInt(msgType);
            dos.writeInt(player.getID());
            dos.writeInt(player.position.getX());
            dos.writeInt(player.position.getY());
            dos.writeInt(player.getDir().ordinal());
            dos.writeUTF(player.getName());
            //MultiAIPlayer is the type of AIplayer or Player, so it works here
            if (player instanceof AIPlayer) {
                dos.writeBoolean(true);
            } else {
                dos.writeBoolean(false);
            }
            dos.writeUTF(player.getColour());
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

    /**
     * When player join in the server, then add that on set of players
     * @param dis
     * @throws Exception This exception is thrown when closing the stream fails
     */
    @Override
    public void process(DataInputStream dis) {
        try {
            int id = dis.readInt();
            if (id == this.gameClient.getMultiPlayer().getID()) {
                return;
            }

            int x = dis.readInt();
            int y = dis.readInt();
            Direction dir = Direction.values()[dis.readInt()];
            String username = dis.readUTF();
            boolean isAIPlayer = dis.readBoolean();
            String colour = dis.readUTF();


            boolean exist = false;
            for (Player t : gameClient.getPlayers()) {
                if (id == t.getID()) {
                    exist = true;
                    break;
                }
            }
            if (!exist) {
                //if i find this player isn't in my players list, so this player is a new player, then i will send my info to he/she
                PlayerNewMessage msg = new PlayerNewMessage(gameClient);
                gameClient.getNc().send(msg);

                System.out.println("--------------------------------------");
                System.out.println("my id: " + this.gameClient.getMultiPlayer().getID());
                System.out.println("I'm a Real player: this player new message is from: id " + id);

                if (!isAIPlayer) {
                    MultiPlayer newPlayer = new MultiPlayer(gameClient.getCollisionLayer(), username, x, y, gameClient, dir, colour, PlayersType.multi);
                    newPlayer.setID(id);
                    System.out.println("id" + id + " 's position: (" + x + "," + y + ")");
                    gameClient.getPlayers().add(newPlayer);
                    gameClient.playersIdIndexList.put(id, gameClient.getPlayers().indexOf(newPlayer));
                    gameClient.getHumanPlayers().add(newPlayer);
                } else {
                    MultiAIPlayer newAIPlayer = new MultiAIPlayer(gameClient.getCollisionLayer(), username, x, y, gameClient, dir, colour, PlayersType.multi);
                    newAIPlayer.setID(id);
                    gameClient.getPlayers().add(newAIPlayer);
                    gameClient.playersIdIndexList.put(id, gameClient.getPlayers().indexOf(newAIPlayer));
                }

                System.out.println("I've added this player to list!!");
                System.out.println("--------------------------------------");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void process(DataInputStream dis, int aiIndex) {
        try {
            int id = dis.readInt();
            if (id == this.aigameClient.getAiPlayer().getID()) {
                return;
            }

            int x = dis.readInt();
            int y = dis.readInt();
            Direction dir = Direction.values()[dis.readInt()];
            String username = dis.readUTF();
            boolean isAIPlayer = dis.readBoolean();
            String colour = dis.readUTF();

            boolean exist = false;
            for (Player t : aigameClient.getPlayers()) {
                if (id == t.getID()) {
                    exist = true;
                    break;
                }
            }
            if (!exist) {
                //if i find this player isn't in my players list, so this player is a new player, then i will send my info to he/she
                PlayerNewMessage msg = new PlayerNewMessage(gameClient, aigameClient);
                aigameClient.getNetClient().send(msg);

                System.out.println("--------------------------------------");
                System.out.println("my id: " + aigameClient.getAiPlayer().getID());
                System.out.println("I'm an AI player: this player new message is from: id " + id);

                if (!isAIPlayer) {
                    MultiPlayer newPlayer = new MultiPlayer(gameClient.getCollisionLayer(), username, x, y, gameClient, dir, colour, PlayersType.multi);
                    newPlayer.setID(id);
                    aigameClient.getPlayers().add(newPlayer);
                    aigameClient.playersIdIndexList.put(id, gameClient.getPlayers().indexOf(newPlayer));
                } else {
                    //TODO this need to think
                    MultiAIPlayer newAIPlayer = new MultiAIPlayer(gameClient.getCollisionLayer(), username, x, y, gameClient, dir, colour, PlayersType.multi);
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
