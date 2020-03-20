# Game Networking

<<<<<<< HEAD
<<<<<<< HEAD
Through Server-Client Construction/Mode to implement multiplay game.
=======
Basic idea: Use Server-Client Mode to implement multiplay game.
>>>>>>> yueyi2
=======
Basic idea: Use Server-Client Mode to implement multiplay game.
>>>>>>> origin/yueyi1

I've implemented multiplayer join the game, and everyone can see each other's movements with java Swing ,which is in `yueyi` branch.

In this new `yueyi1` branch, i will merge my networking part into core, namly using LibGDX to implement multiplaye game.

<<<<<<< HEAD
<<<<<<< HEAD
=======
=======
>>>>>>> origin/yueyi1


**Here are some classes relates to multiplayer mode :**

<<<<<<< HEAD
>>>>>>> yueyi2
=======
>>>>>>> origin/yueyi1
## Objects Package

The package directory `package com.project.mazegame.objects` contains some of the classes that coordinate with the game network.

* MultiPlayer
* Direction

## Screens Package

The package directory `package com.project.mazegame.screens` contains some of the classes that coordinate with the game network.

* MultiPlayerScreen
<<<<<<< HEAD
<<<<<<< HEAD

* MultiPlayerGameScreen
=======
* MultiPlayerGameScreen
=======
* MultiPlayerGameScreen
>>>>>>> origin/yueyi1
* JoinMazeScreen
* CreatMazeScreen
* HostLobbyScreen
* OtherLobbyScreen

## Tools Package

* MultiCollect
<<<<<<< HEAD
>>>>>>> yueyi2
=======
>>>>>>> origin/yueyi1

## Networking Package

The package directory `com.project.mazegame.networking` contains all of the classes that deal with the game network

* Server
  * GameServer
<<<<<<< HEAD
<<<<<<< HEAD
  * ServerThread

=======
  
>>>>>>> yueyi2
=======
  
>>>>>>> origin/yueyi1
* Messages
  * Message
  * PlayerNewMessage
  * MoveMessage
<<<<<<< HEAD
<<<<<<< HEAD
  * AttackMessage
=======
  * PlayerExitMessage
>>>>>>> yueyi2
=======
  * PlayerExitMessage
>>>>>>> origin/yueyi1
  * and more other messages need to add in further……

* Client

  * ClientInfo
<<<<<<< HEAD
<<<<<<< HEAD
  * ClientThread
=======
>>>>>>> origin/yueyi1
  * NetClient
  
##  Description 

In Week6 prototye demo, we are going to make a demonstration on JOIN MAZE mode. For JOIN MAZE mode, we need to open the server separately, which means we have to run GameServer.java (`GameServer.java` is in `yueyi` repo under `package gameNetworks` package) in terminal , eclipse … Then run DesktopLauncher class under `package com.project.mazegame.desktop` package. we request that user enter server's ip and username to start multiplayer game.

##  Update Logs

* Version1.1:  When client connect to server (Through TCP socket) , client can send their own information to server,and server can save this client object and its info into a list. 
  
* Version1.2: After client connect to server, server can give this player an ID and server's UDP port through TCP socket.
  
* Version1.3: Player can send their own info(postion,ID,direction……)  to server after successfully connect to Server.
  
* Version1.4: Server can broadcast the messages which receive from someone to all Clients (players).
  
* Version1.5:  Player can see each other movements,but still some bugs.
  
* Version1.6: fixed bugs, player can see each other's movements with no bugs!
  
* version1.7: CREATEMAZE mode: When Player1 click CREATEMAZE then he/she create a room, player1 will be a server. Other player2 click JOINMAZE then enter Player1 ip ,this player2 can join the room which created by Player1.
  
  * 1.7.2 fix camera and integrate with collectable ……
  
  * 1.7.3 pleayer can genearte in random position in multiplayer mode
  * 1.7.4 fix motion bugs in multiplayer mode
  
  * 1.8 Players can see what items the opponent picks up.
  
* Version1.8: Players can see what items the opponent picks up.
  
* Version1.9: Create Host Lobby for the host player and Other Lobby for the other player, Other player Join the maze to enter the lobby, then press enter to confirm ready to game, then the ready player name will be showed on the Host Lobby Screen.
  
  * 1.9.1 other play can see each ready players in Other Lobby.
  * 1.9.2 handle player exit lobby, then delete name from Host Lobby&Other Lobby
  * 1.9.3 update Host Lobby&Other Lobby (Host player exit game)
  * 1.9.4 add new feather, check the ip whether correct when the player enter ip in `JoinMazeScreen` class
  
* Version2.0: The items can generate in the same location on the map.
  
  * 2.0.1 fix bugs on pick up items (include coin)
  * 2.0.2 integrate with timer and animations
  
* Version2.1: add animation in mutiplayer, but there are some small bugs.
  
  * 2.1.1 fix bugs, player animations can work in mutiplayer.
  
<<<<<<< HEAD
    * 1.7.4 fix motion bugs in multiplayer mode
=======
  * NetClient
  
##  Description 

In Week6 prototye demo, we are going to make a demonstration on JOIN MAZE mode. For JOIN MAZE mode, we need to open the server separately, which means we have to run GameServer.java (`GameServer.java` is in `yueyi` repo under `package gameNetworks` package) in terminal , eclipse … Then run DesktopLauncher class under `package com.project.mazegame.desktop` package. we request that user enter server's ip and username to start multiplayer game.

##  Update Logs

* Version1.1:  When client connect to server (Through TCP socket) , client can send their own information to server,and server can save this client object and its info into a list. 
  
* Version1.2: After client connect to server, server can give this player an ID and server's UDP port through TCP socket.
  
* Version1.3: Player can send their own info(postion,ID,direction……)  to server after successfully connect to Server.
  
* Version1.4: Server can broadcast the messages which receive from someone to all Clients (players).
  
* Version1.5:  Player can see each other movements,but still some bugs.
  
* Version1.6: fixed bugs, player can see each other's movements with no bugs!
  
* version1.7: CREATEMAZE mode: When Player1 click CREATEMAZE then he/she create a room, player1 will be a server. Other player2 click JOINMAZE then enter Player1 ip ,this player2 can join the room which created by Player1.
  
  * 1.7.2 fix camera and integrate with collectable ……
  
  * 1.7.3 pleayer can genearte in random position in multiplayer mode
  * 1.7.4 fix motion bugs in multiplayer mode
  
  * 1.8 Players can see what items the opponent picks up.
  
* Version1.8: Players can see what items the opponent picks up.
  
* Version1.9: Create Host Lobby for the host player and Other Lobby for the other player, Other player Join the maze to enter the lobby, then press enter to confirm ready to game, then the ready player name will be showed on the Host Lobby Screen.
  
  * 1.9.1 other play can see each ready players in Other Lobby.
  * 1.9.2 handle player exit lobby, then delete name from Host Lobby&Other Lobby
  * 1.9.3 update Host Lobby&Other Lobby (Host player exit game)
  * 1.9.4 add new feather, check the ip whether correct when the player enter ip in `JoinMazeScreen` class
  
* Version2.0: The items can generate in the same location on the map.
  
  * 2.0.1 fix bugs on pick up items (include coin)
  * 2.0.2 integrate with timer and animations
  * 2.0.3 better inheritance, ready to intergrate with AI in mutiplayer 
>>>>>>> yueyi2
=======
* Version3.1: add AI in mutiplayer mode, every player show different AI players, but if AI pick up an items, this item will dispear on the map at the same time. 
>>>>>>> origin/yueyi1
  
  
  
  
  
# TODO

* Try to fix bugs on player click cancel button on enter ip or username screen to back to menu Screen.
  
* Use Interface to simplify code for multiplayer mode.
  
* Try to complete code on server can close relevant resources and close client's UDP socket after player exit game in multiplayer game mode (JOIN MAZE).
  
* Integration with game logic (make more message's types)
  
* Complete `GDX.app.log(info,error…)` system
  
* make more directions to make motion more fluency
  
* make sure all resource will be dispose when switch to other screen.
  
  
  

  

  

  

  

  

  

  

  
  



 