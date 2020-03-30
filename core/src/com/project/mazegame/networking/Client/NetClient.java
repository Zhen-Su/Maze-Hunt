package com.project.mazegame.networking.Client;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.project.mazegame.networking.Messagess.AttackMessage;
import com.project.mazegame.networking.Messagess.DecreaseHealthMessage;
import com.project.mazegame.networking.Messagess.ItemCreateMessage;
import com.project.mazegame.networking.Messagess.Message;
import com.project.mazegame.networking.Messagess.MoveMessage;
import com.project.mazegame.networking.Messagess.PlayerExitMessage;
import com.project.mazegame.networking.Messagess.PlayerNewMessage;
import com.project.mazegame.networking.Messagess.StartGameMessage;
import com.project.mazegame.networking.Server.GameServer;
import com.project.mazegame.objects.Direction;
import com.project.mazegame.objects.MultiPlayer;
import com.project.mazegame.screens.MultiPlayerGameScreen;
import com.project.mazegame.tools.Assets;
import com.project.mazegame.tools.PlayersType;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Random;

public class NetClient {

    private String map;
    private TiledMap tileMap;
    private Texture mapTexture;
    private TiledMapTileLayer collisionLayer;
    private OrthogonalTiledMapRenderer tileMapRenderer;

    private Socket socket = null;
    private String serverIP;
    private int serverUDPPort;
    private int clientUDPPort;
    private int PLAYER_EXIT_UDP_PORT;
    private MultiPlayerGameScreen gameClient;
    private DatagramSocket datagramSocket = null;
    public static boolean debug = false;

    public NetClient(MultiPlayerGameScreen gameClient) {
        this.gameClient = gameClient;
        this.clientUDPPort = getRandomUDPPort();
    }

    /**
     * Conncet to GameServer,send udp port and IP to GameServer then close tcp socket.
     *
     * @param ip server IP
     */
    public synchronized void connect(String ip, boolean createAI, int index, boolean ishostPlayer) {
        serverIP = ip;
        try {
            try {
                if (debug) System.out.println("Client UDP socket have be opened");
                datagramSocket = new DatagramSocket(clientUDPPort);  // UDPSocket
            } catch (SocketException e) {
                e.printStackTrace();
            }
            socket = new Socket(ip, GameServer.SERVER_TCP_PORT);   // TCPSocket
            if (debug) printMsg("Connected to server!");

            //Send client udp port to GameServer.
            OutputStream os = socket.getOutputStream();
            DataOutputStream dos = new DataOutputStream(os);
            dos.writeInt(clientUDPPort);
            if (debug) printMsg("I've sent my udp port to Game Server!");

            //Receive an unique ID and Server udp port
            InputStream is = socket.getInputStream();
            DataInputStream dis = new DataInputStream(is);
            int id = dis.readInt();
            this.serverUDPPort = dis.readInt();
            this.PLAYER_EXIT_UDP_PORT = dis.readInt();
            if (id == 1) {
                //if i'm host player then send map to server
                dos.writeUTF(gameClient.map);
            } else {
                //if i'm not a host player then receive map from server
                this.map = dis.readUTF();
            }
            if (!createAI) {
                printMsg("Server gives me ID is: " + id + " ,and server UDP Port is: " + serverUDPPort);
                if (!ishostPlayer) {
                    if (map.equals("map1")) {
                        tileMap = new TmxMapLoader().load("Map1.tmx");
                        mapTexture = Assets.manager.get(Assets.map1Icon, Texture.class);
                    } else if (map.equals("map2")) {
                        tileMap = new TmxMapLoader().load("Map2.tmx");
                        mapTexture = Assets.manager.get(Assets.map2Icon, Texture.class);
                    } else {
                        tileMap = new TmxMapLoader().load("Map3.tmx");
                        mapTexture = Assets.manager.get(Assets.map3Icon, Texture.class);
                    }
                    tileMapRenderer = new OrthogonalTiledMapRenderer(tileMap);
                    collisionLayer = (TiledMapTileLayer) tileMap.getLayers().get("wallLayer");
                    settingMap();
                    MultiPlayer myMultiPlayer = new MultiPlayer(collisionLayer, gameClient.username, gameClient, Direction.STOP, gameClient.playerSkin, PlayersType.multi);
                    gameClient.setMultiPlayer(myMultiPlayer);
                    gameClient.getMultiPlayer().setID(id);
                }
                gameClient.getMultiPlayer().setID(id);
            } else {
                printMsg("Server gives me ID is: " + id + " ,and server UDP Port is: " + serverUDPPort);
                gameClient.aiGameClients.get(index).getAiPlayer().setID(id); //change AI player's id
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (socket != null) {
                    socket.close();  //We do not need this tcp socket anymore.
                    socket = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (createAI) {
            PlayerNewMessage msg = new PlayerNewMessage(gameClient.aiGameClients.get(index).getAiPlayer());
            send(msg);
        } else {
            PlayerNewMessage msg = new PlayerNewMessage(gameClient.getMultiPlayer());
            send(msg);
        }


        new Thread(new ClientThread(createAI, index)).start();
    }


    /**
     * send message to server
     *
     * @param msg
     */
    public void send(Message msg) {
        msg.send(datagramSocket, serverIP, serverUDPPort);
    }

    /**
     * To set map and relevant variables in MultiplayerGameScreen
     */
    public void settingMap() {
        gameClient.setTileMap(tileMap);
        gameClient.setMapTexture(mapTexture);
        gameClient.setTileMapRenderer(tileMapRenderer);
        gameClient.setCollisionLayer(collisionLayer);
    }

    /**
     * Inner class
     * For Client to receive and process messages
     */
    public class ClientThread implements Runnable {

        byte[] receiveBuf = new byte[1024];
        int aiIndex;
        boolean isAI;

        public ClientThread(boolean isAI, int aiIndex) {
            this.isAI = isAI;
            this.aiIndex = aiIndex;
        }

        @Override
        public void run() {
            if (debug) System.out.println("Client UDP thread start...");
            Thread.currentThread().setName("Client UDP Thread");
            while (null != datagramSocket) {
                DatagramPacket datagramPacket = new DatagramPacket(receiveBuf, receiveBuf.length);
                try {
                    datagramSocket.receive(datagramPacket);
                    process(datagramPacket);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        /**
         * Process the data which received from server
         *
         * @param datagramPacket
         */
        private synchronized void process(DatagramPacket datagramPacket) {
            ByteArrayInputStream bais = new ByteArrayInputStream(receiveBuf, 0, datagramPacket.getLength());
            DataInputStream dis = new DataInputStream(bais);

            int msgType = 0;
            try {
                msgType = dis.readInt();
            } catch (IOException e) {
                e.printStackTrace();
            }

            Message msg = null;
            switch (msgType) {
                case Message.PLAYER_NEW_MSG:
                    if (isAI) {
                        msg = new PlayerNewMessage(gameClient, gameClient.aiGameClients.get(aiIndex));
                        msg.process(dis, aiIndex);
                    } else {
                        msg = new PlayerNewMessage(gameClient);
                        msg.process(dis);
                    }
                    break;
                case Message.PLAYER_MOVE_MSG:
                    msg = new MoveMessage(gameClient);
                    msg.process(dis);
                    break;
                case Message.PLAYER_EXIT_MSG:
                    msg = new PlayerExitMessage(gameClient);
                    msg.process(dis);
                    break;
                case Message.ITEMS_CREATE:
                    msg = new ItemCreateMessage(gameClient);
                    msg.process(dis);
                    break;
                case Message.HOST_START:
                    msg = new StartGameMessage(gameClient);
                    msg.process(dis);
                    break;
                case Message.ATTACK_MSG:
                    msg = new AttackMessage(gameClient);
                    msg.process(dis);
                    break;
                case Message.DESCREASE_HP:
                    msg = new DecreaseHealthMessage(gameClient);
                    msg.process(dis);
                    break;
            }
        }
    }

    /**
     * To send player exit message.
     */
    public void sendClientDisconnectMsg() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream(88);
        DataOutputStream dos = new DataOutputStream(baos);
        try {
            dos.writeInt(gameClient.getMultiPlayer().getID());
            dos.writeInt(clientUDPPort);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != dos) {
                try {
                    dos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (null != baos) {
                try {
                    baos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        byte[] buf = baos.toByteArray();
        try {
            DatagramPacket dp = new DatagramPacket(buf, buf.length, new InetSocketAddress(serverIP, PLAYER_EXIT_UDP_PORT));
            datagramSocket.send(dp);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * * Randomly generate UDP port from 1024 to 5000
     *
     * @return
     */
    public static int getRandomUDPPort() {
        Random random = new Random();
        return random.nextInt(3977) + 1024;
        //rand.nextInt(MAX - MIN + 1) + MIN
    }

    /**
     * Only for debug (print message)
     *
     * @param msg
     */
    public void printMsg(String msg) {
        System.out.println(msg);
    }

}
