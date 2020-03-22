git add *
printf "\e[33m Enter your commit message\n"
read message
git commit -m "$message"
git push
echo "DONE"