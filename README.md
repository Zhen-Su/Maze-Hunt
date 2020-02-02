# Game Rendering

__I will post the image of the code structure real soon__

## Setting up the project
1. Preferred IDE for this project: Android Studio/IntelliJ. (Eclipse works too but Android Studio/IntelliJ doesn't complain lol)
2. Launch Android Studio/IntelliJ
3. Import project
4. Main code to edit is the class `Game` located under the `core` folder. under the package `com.project.mazegame`
5. To adjust the window size, navigate to `DesktopLauncher` class under the  `desktop` folder (We won't be bothering much with this class)


## Main class
  The main class is the MazeGame.java right under the `com.project.mazegame` package directory. However, we wont be editing the class much, or at all.

## Resources
All of the resources needed in the game will be put in the folder `android/assets`.

## Objects
The package directory `com.project.mazegame.objects` contains all of the classes that deals with the game object and logic:
```
1. Player
2. AI
3. Collectibles
4. etc
```

## Screens
The package directory `com.project.mazegame.screens` contains all of the screens needed for the game to run.
The screens are:

```
1. MenuScreen (halfway done)
- Solo Play Button
- Multiplayer Play Button
- Settings Button
- Exit Button

2. PreferencesScreen (hasn't started)
- Adjust volume (Enable/Disable/Up/Down)
- Adjust Screen size

3. GameScreen (working on)
- The actual Game

4. EndScreen (hasn't started)
- Shows game result and leaderboard

5. LobbyScreen (hasn't started)
- For Multiplayer gameplay

6. LoadingScreen (hasn't started)
- As a placeholder between screens
```
## Tools
The package `com.project.mazegame.tools` contains the classes for the other back-end stuffs:
```
1. AppPreferences

2. InputHandler

3. OrthoCam

4. Variables
```
