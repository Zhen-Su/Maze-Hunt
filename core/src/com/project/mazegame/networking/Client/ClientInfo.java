package com.project.mazegame.networking.Client;

/**
 * Client send those information to server, server store them in a list.
 */
public class ClientInfo {

    public String IP;
    public int UDP_PORT;
    public int id;

    public ClientInfo(String ip, int UDP_PORT, int id) {
        this.IP = ip;
        this.UDP_PORT = UDP_PORT;
        this.id = id;
    }

}
