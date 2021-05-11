build:
	mvn package

run:
	java -jar target/360t-messages-1.0.0-SNAPSHOT.jar 3

build-run: build run

