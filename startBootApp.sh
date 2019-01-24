
JAVA_OPTS="-Xdebug -Xrunjdwp:server=y,transport=dt_socket,address=8000,suspend=n "

java ${JAVA_OPTS}  -jar target/demo-0.0.1-SNAPSHOT.jar 2> /dev/null


