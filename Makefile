build:
	mvn package

test:
	mvn test

start-rmi:
	(CLASSPATH=./target/360t-messages-1.0.0-SNAPSHOT.jar rmiregistry 2700) &
	echo Started RMI registry $$!

kill-rmi:
	pkill -f rmiregistry

run-players: start-rmi
	echo Starting Player 1
	(java -jar target/360t-messages-1.0.0-SNAPSHOT.jar "Player 1" "Player 2" 2) &
	echo Starting Player 2
	java -jar target/360t-messages-1.0.0-SNAPSHOT.jar "Player 2"

run: build start-rmi run-players kill-rmi

build-run: build run

