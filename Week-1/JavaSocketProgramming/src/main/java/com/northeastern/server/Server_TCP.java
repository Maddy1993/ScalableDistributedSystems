package main.java.com.northeastern.server;

import main.java.com.northeastern.Utils.DataPacket;
import main.java.com.northeastern.Utils.Utils;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.*;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

/**
 * Represents a TCP server.
 * The class initializes the tcp socket on the port specified
 * by the user during the class execution. Once the socket is
 * established, the server waits until an incoming connections
 * received and reverses the string passed by the client and sends
 * the string. The server then gracefully exists the execution.
 *
 * @author mpothukuchi May 4th, 2019.
 */
public class Server_TCP {

    //Logger for the class.
    static Logger LOGGER = Logger.getLogger(Server_TCP.class.getName());

    //Port Number for the server
    static Integer portNumber;

    //Instance of the class
    private static Server_TCP serverObject;

    //Server socket instance.
    private ServerSocket serverSocket;

    //Socket for incoming requests.
    private Socket socket;

    //Utils instance for the server
    private Utils utils;

    //Client to packet mapping store.
    Map<String, Map<String, Object>> clientPacketMapper;

    //Integer for server source.
    final int serverCode = 1;

    /**
     * Constructor for the program to take the
     * port number and create the socket.
     */
    public Server_TCP() {
        try {
            utils = new Utils("logs/tcp/tcp_server_utils.log");
            serverSocket = new ServerSocket(portNumber);
            clientPacketMapper = new HashMap<>();
            formatMessage("TCP Server Successfully initialized.");
            LOGGER.info("Server Successfully initialized.");
        } catch (IOException e) {
            formatMessage("Error initializing TCP server socket");
            LOGGER.severe("Error initializing TCP server socket: "
                    + e.getMessage());
            System.exit(-1);
        }
    }

    /**
     * Formats the message to be printed to the output stream.
     *
     * @param message The message to print to screen
     */
    private void formatMessage(String  message) {
        System.out.print("<" + LocalDateTime.now() + "> ");
        System.out.println(message);
    }

    /**
     * @return Returns the socket associated with the instance of server TCP.
     */
    private Socket getSocket() {
        return this.socket;
    }

    /**
     * Initializes the socket associated with the server TCP instance.
     */
    private void setSocket(Socket socket) {
        this.socket = socket;
    }

    /**
     * @return Returns the server socket associated with this class.
     */
    private ServerSocket getServerSocket() {
        return this.serverSocket;
    }

    /**
     * Reads the data from connection,
     * sends the data to the client,
     * and closes the connection.
     * @param socket The connection of a specific client.
     */
    private void connectionHandler(Socket socket) {
        //Get the client details.
        String clientAddress = socket.getRemoteSocketAddress().toString();
        clientAddress = clientAddress.replace("/", "");
        int port = socket.getPort();

        //Read the data.
        DataPacket receivedPacket;

        //Keep reading as long as the server doesn't receive
        //an exit message from the client.
        while (true) {
            receivedPacket = utils.readData(socket, serverCode);

            //When the server receives a malformed data packet,
            //terminate connection with the client.
            if(receivedPacket == null) {
                formatMessage("< Received incorrect object format from client");
                LOGGER.info("Received incorrect object format from client: " + clientAddress + " on " + port);

                formatMessage("--Terminating connection with the client: " + clientAddress + "--");
                LOGGER.info("Terminating connection with the client: " + clientAddress);
                utils.closeConnection(socket);
                return;
            }

            formatMessage("< Received packet from -> " + clientAddress);
            LOGGER.info("Received packet from -> " + clientAddress);

            //Mapper
            Map<String, Object> clientValueSet;
            if (clientPacketMapper.containsKey(clientAddress)) {
                clientValueSet = clientPacketMapper.get(clientAddress);
            } else {
                clientValueSet = new HashMap<>();
                clientPacketMapper.put(clientAddress, clientValueSet);
            }

            //Based on the packet, perform the operation.
            switch (receivedPacket.getPacketType()) {
                case GET:
                    formatMessage("< GET request received from client");
                    Object responseData = clientValueSet.get(receivedPacket.getData());
                    if (responseData == null) {
                        formatMessage("> Key requested: " + receivedPacket.getData() + " not found");
                        LOGGER.info("Key requested: " + receivedPacket.getData() + " not found");
                        break;
                    }

                    DataPacket responsePacket =
                            new DataPacket(
                                    DataPacket.PacketType.DATA,
                                    responseData);
                    utils.writeData(responsePacket, socket);
                    utils.formatMessage("> Sent to the client-> The value of KEY: " + receivedPacket.getData()
                            + " is " + responseData);
                    break;
                case PUT:
                    formatMessage("< PUT request received from client");
                    Object packetData = receivedPacket.getData();
                    if (packetData instanceof ArrayList) {
                        List<Object> data = (List<Object>) packetData;
                        clientValueSet.put((String) data.get(0), data.get(1));
                        clientPacketMapper.put(clientAddress, clientValueSet);

                        LOGGER.info("Put request received from client: " + data.get(0));
                        sendSuccessResponse(socket);
                    } else {
                        formatMessage("Unidentified object type received in data block"
                                + " of the packet. Ignoring request.");
                        LOGGER.warning("Unidentified object type received in data block"
                                + " of the packet. Ignoring request: " + packetData);
                    }

                    break;
                case DELETE:
                    formatMessage("< DELETE Operation requested from client");
                    if (clientValueSet.size() == 0) {
                        LOGGER.warning("Key value store for the client is empty.");
                    }

                    Object data = receivedPacket.getData();
                    if (data instanceof String) {
                        String dataVal = (String) receivedPacket.getData();
                        clientValueSet.remove(dataVal);

                        formatMessage("> Delete request for KEY: " + dataVal + " successful");
                        LOGGER.info("Delete request for KEY: " + dataVal +  "received");
                        sendSuccessResponse(socket);
                    } else {
                        formatMessage("Incorrect data type for data in the packet. Ignoring DELETE request.");
                        LOGGER.warning("Incorrect data type for data in the packet. Ignoring request: " + data);
                    }

                    break;
                case KEYS:
                    formatMessage("< GET KEYS request received from client.");
                    DataPacket keyResponsePacket =
                            new DataPacket(DataPacket.PacketType.DATA,
                                    new ArrayList<>(clientValueSet.keySet()));
                    utils.writeData(keyResponsePacket, socket);
                    break;
                case EXIT:
                    formatMessage("< EXIT message received from client.");
                    return;
                default:
                    formatMessage("Unidentified object type received from client.");
                    LOGGER.warning("Incorrect or unidentified object type received from client:" + receivedPacket.getPacketType());
            }
        }

    }

    /**
     * Creates a success info packet and sends the response
     * back to the client who made the request.
     *
     * @param socket The socket received from the client.
     *                       Contains address and port number of
     *                       the client.
     */
    private void sendSuccessResponse(Socket socket) {
        DataPacket successPacket = new DataPacket(DataPacket.PacketType.SUCCESS, LocalDateTime.now());
        utils.writeData(successPacket, socket);
    }

    /**
     * Used for any setup required before the program execution
     * starts.
     *
     * @param name  Name of the logger to use for the
     *              overriding class.
     */
    static void setup(String name) {
        //Initialize the logger.
        try {
            LOGGER.setUseParentHandlers(false);
            LOGGER.addHandler(new FileHandler(name));
        } catch (IOException e) {
            System.out.println("Error initializing the logger: " + e.getMessage());
        }
    }

    /**
     * Can be used to parse any command line arguments.
     * Currently parses the port number from the command
     * line arguments.
     * @param args  List of command line arguments
     */
    static void parseArguments(String[] args) {
        if (args.length == 1) {
            try {
                portNumber = Integer.parseInt(args[0]);
            } catch (NumberFormatException exception) {
                LOGGER.severe("Port number needs to be an integer.");
                System.exit(1);
            }
        } else {
            LOGGER.severe("The server needs the following inputs only:\n\t1. Port Number");
            System.exit(1);
        }
    }

    // Driver Program for the server.
    public static void main(String[] args) {
        setup("logs/tcp/server_tcp.log");
        parseArguments(args);

        //Initialize the socket.
        serverObject = new Server_TCP();

        while (true){
            try {
                //Wait for incoming connections.
                serverObject.setSocket(serverObject.getServerSocket().accept());
                LOGGER.info("Client connection accepted.");
            } catch (IOException e) {
                LOGGER.severe("Error while accepting connection from the client: " + e.getMessage());
                System.exit(1);
            }

            //Process the connection request.
            //Processing includes the reading the request
            //and sending the response. After this step, the
            //connection will be closed by the client and
            //client socket will no longer be available.
            serverObject.connectionHandler(serverObject.getSocket());
        }
    }
}
