# Start the hotelbeds bridge application in docker images

# Build image if it does not already exist
image=jlaudemo

force_rebuild=true
hasimage=`sudo docker ls`

if [[ -z hasimage || ${force_rebuild} ]]
then
  if [[ ${force_rebuild} && -n hasimage ]]
  then
    echo "Remove and build Docker image ${image}."
    sudo docker rmi -f ${image} .
    sudo docker build --tag=${image} .
    sudo docker image -a
  else 
    echo "Docker image ${image} does not exist. Building it now from the current"
    sudo docker build --tag=${image} .
  fi
fi

echo "Docker ${image} exists. Running it now."
echo ""

instance_count=2

for i in $(seq 1 $instance_count)
  do

#sudo docker run --network="host" -p 808${i}:8085 -it ${image}  /bin/bash
    sudo docker run -p 808${i}:8085 -it ${image}  /bin/bash

done
