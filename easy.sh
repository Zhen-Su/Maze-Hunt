#!/bin/sh
cp /home/james/TeamProject/core/src/com/project/mazegame/objects/AIPlayer.java /home/james/TeamProject/git/anotherworld/
cp /home/james/TeamProject/core/src/com/project/mazegame/objects/Player.java /home/james/TeamProject/git/anotherworld/
cp /home/james/TeamProject/core/src/com/project/mazegame/objects/Pair.java /home/james/TeamProject/git/anotherworld/
git add *
git commit -m "$1"
git push git@git-teaching.cs.bham.ac.uk:mod-team-project-2019/anotherworld.git

