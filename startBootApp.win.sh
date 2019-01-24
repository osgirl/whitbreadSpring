echo "Starting the hotel-booking-gta-apibridge-ws SpringBoot application......"

JAVA_OPTS="-Xdebug -Xrunjdwp:server=y,transport=dt_socket,address=8000,suspend=n "
JAVA_OPTS="$JAVA_OPTS -Dcacheloader.backup.dir=c:\\work\\gta\\data\\hbg\\kafkaMessages"

java ${JAVA_OPTS} -jar hotel-booking-gta-apibridge-ws/target/hotel-booking-gta-apibridge-ws-1.0-SNAPSHOT.jar 2> /dev/null

