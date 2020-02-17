# Game Networking

Through Server-Client Construction/Mode to implement multiplay game.

I've implemented multiplayer join the game, and everyone can see each other's movements with java Swing ,which is in `yueyi` branch.

In this new `yueyi1` branch, i will merge my networking part into core, namly using LibGDX to implement multiplaye game.

## Objects Package

The package directory `package com.project.mazegame.objects` contains some of the classes that coordinate with the game network.

* MultiPlayer
* Direction

## Screens Package

The package directory `package com.project.mazegame.screens` contains some of the classes that coordinate with the game network.

* MultiPlayerScreen

* MultiPlayerGameScreen

## Networking Package

The package directory `com.project.mazegame.networking` contains all of the classes that deal with the game network

* Server
  * GameServer
  * ServerThread

* Messages
  * Message
  * PlayerNewMessage
  * MoveMessage
  * AttackMessage
  * and more other messages need to add in further……

* Client

  * ClientInfo
  * ClientThread
  * NetClient

  

  ##  Description 

  In Week6 prototye demo, we are going to make a demonstration on JOIN MAZE mode. For JOIN MAZE mode, we need to open the server separately, which means we have to run GameServer.java (`GameServer.java` is in `yueyi` repo under `package gameNetworks` package) in terminal , eclipse … Then run DesktopLauncher class under `package com.project.mazegame.desktop` package. we request that user enter server's ip and username to start multiplayer game.

  ##  Update Logs

  * 1.1 When client connect to server (Through TCP socket) , client can send their own information to server,and server can save this client object and its info into a list. 
  *  1.2 After client connect to server, server can give this player an ID and server's UDP port through TCP socket.
  * 1.3 Player can send their own info(postion,ID,direction……)  to server after successfully connect to Server.
  * 1.4 Server can broadcast the messages which receive from someone to all Clients (players).
  * 1.5  

  

  # TODO

  * Try to make other player can see others movements.

  * Try to fix bugs on player click cancel button on enter ip or username screen to back to menu Screen.

  * Try to complete code on server can close relevant resources and close client's UDP socket after player exit game in multiplayer game mode (JOIN MAZE).

  * Integration with game logic (make more message's types)

    

  

  

  

  

  

  

  

  

  



 