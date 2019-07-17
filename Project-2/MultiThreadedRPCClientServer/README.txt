MultiThreadedRPCClientServer

The implementation is a multi-threaded client server which use RPC. The mechanism used in this project is Apache Thrift RPC.
Due to knowledge limitations, the fat JAR file was not generated properly. Please execute manually.

Execution:
1. Unzip the folder.
2. Load the project into IntelliJ (or any other IDE)
3. Open a terminal and from the Root folder: MultiThreadedRPCClientServer, execute a maven clean install
    mvn clean install
4. Execute the server
    Server main class: src/main/java/com/northeastern/edu/server/RPCServer.java
    Arguments: PortNumber
5. Execute the multiple clients:
    Client main class: src/main/java/com/northeastern/edu/client/RPCClient.java
    Arguments: ServerAddress PortNumber
