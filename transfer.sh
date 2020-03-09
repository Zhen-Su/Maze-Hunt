#!/bin/sh
cp -r /home/james/TeamProject/core/src/com/project/mazegame/objects/ /home/james/TeamProject/git/anotherworld/

cp -r /home/james/TeamProject/core/src/com/project/mazegame/tools/ /home/james/TeamProject/git/anotherworld/
cp -r /home/james/TeamProject/core/src/com/project/mazegame/screens/ /home/james/TeamProject/git/anotherworld/
cp -r /home/james/TeamProject/core/src/com/project/mazegame/networking/ /home/james/TeamProject/git/anotherworld/

git add *
git commit -m "$1" -m "$2"
>
