package main.java.com.northeastern.server;

import main.java.com.northeastern.Utils.DataPacket;
import main.java.com.northeastern.Utils.DataPacket.PacketType;
import main.java.com.northeastern.Utils.Utils;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.time.LocalDateTime;
import java.util.*;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

/**
 * Represents a UDP server.
 * The class initializes the udp socket on the port specified
 * by the user during the class execution. Once the socket is
 * established, the server waits until an incoming message is
 * received and reverses the string passed by the client and sends
 * the string. The server then gracefully exists the execution.
 *
 * @author mpothukuchi 18th May 2019
*/
public class Server_UDP {

    //Logger for the class.
    static Logger LOGGER = Logger.getLogger(Server_UDP.class.getName());

    //Port Number for the server
    static Integer portNumber;

    //Instance of the class
    private static Server_UDP serverObject;

    //Server datagram socket instance.
    private DatagramSocket socket;

    //Utils instance for the server
    private Utils utils;

    //Packet size for UDP packet.
    final int packetSize = 5048;

    //Buffer for datagram connection.
    byte[] buffer;

    //Packet to receive from the connection.
    DatagramPacket packet;

    //Client to packet mapping store.
    Map<String, Map<String, Object>> clientPacketMapper;

    /**
     * Constructor for the program to take the
     * port number and create the socket.
     */
    private Server_UDP() {
        try {
            utils = new Utils("logs/udp/udp_server_utils.log");
            socket = new DatagramSocket(portNumber);
            clientPacketMapper = new HashMap<>();
            formatMessage("UDP Server Successfully initialized.");
            LOGGER.info("UDP Server Successfully initialized.");
        } catch (IOException e) {
            LOGGER.severe("Error initializing datagram socket: "
                    + e.getMessage());
            System.exit(-1);
        }
    }

    /**
     * Processes the data sent by the client.
     * Also, prints the request by the client
     * to the IO.
     */
    private void processData() {
        //get the instance of current packet
        //and read the port number and inet address
        //of client.
        String clientAddress = packet.getAddress().toString();
        clientAddress = clientAddress.replace("/", "");
        int port = packet.getPort();
        formatMessage("< Received packet from -> " + clientAddress
                + ":" + port);
        LOGGER.info("Received packet from -> " + clientAddress
                + ":" + port);

        //Deserialize the packet from the stream.
        DataPacket receivedPacket = deserializePacket();
        if (receivedPacket == null) {
            return;
        }

        //Mapper.
        String completeAddress = clientAddress + ":" + port;
        Map<String, Object> clientValueSet;
        if (clientPacketMapper.containsKey(completeAddress)) {
            clientValueSet = clientPacketMapper.get(completeAddress);
        } else {
            clientValueSet = new HashMap<>();
            clientPacketMapper.put(completeAddress, clientValueSet);
        }

        //Based on the packet, perform the operation.
        switch (receivedPacket.getPacketType()) {
            case GET:
                formatMessage("< GET request received from client");
                Object responseData = clientValueSet.get(receivedPacket.getData());
                if (responseData == null) {
                    formatMessage("< Key requested: " + receivedPacket.getData() + " not found");
                    LOGGER.info("Key requested: " + receivedPacket.getData() + " not found");
                    break;
                }

                DataPacket responsePacket =
                        new DataPacket(
                                PacketType.DATA,
                                responseData);
                sendResponse(responsePacket, packet);
                break;
            case PUT:
                formatMessage("< PUT request received from client");
                Object packetData = receivedPacket.getData();
                if (packetData instanceof ArrayList) {
                    List<Object> data = (List<Object>) packetData;
                    clientValueSet.put((String) data.get(0), data.get(1));
                    clientPacketMapper.put(completeAddress, clientValueSet);

                    LOGGER.info("Put request received from client: " + data.get(0));
                    sendSuccessResponse(packet);
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
                    sendSuccessResponse(packet);
                } else {
                    formatMessage("Incorrect data type for data in the packet. Ignoring DELETE request.");
                    LOGGER.warning("Incorrect data type for data in the packet. Ignoring request: " + data);
                }

                break;
            case KEYS:
                formatMessage("< GET KEYS request received from client.");
                DataPacket keyResponsePacket =
                        new DataPacket(PacketType.DATA,
                                new ArrayList<>(clientValueSet.keySet()));
                sendResponse(keyResponsePacket, packet);
                break;
            default:
        }
    }

    /**
     * Creates a success info packet and sends the response
     * back to the client who made the request.
     *
     * @param receivedPacket The packet received from the client.
     *                       Contains address and port number of
     *                       the client.
     */
    private void sendSuccessResponse(DatagramPacket receivedPacket) {
        DataPacket successPacket = new DataPacket(PacketType.SUCCESS, LocalDateTime.now());
        sendResponse(successPacket, receivedPacket);
    }

    /**
     * Sends the response back to the client.
     *
     * @param responsePacket Data to send.
     */
    private void sendResponse(DataPacket responsePacket, DatagramPacket inputPacket) {
        //Serialize the response.
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ObjectOutputStream outputStream =
                    new ObjectOutputStream(out);
            outputStream.writeObject(responsePacket);
            outputStream.flush();

            byte[] responseByte = out.toByteArray();
            DatagramPacket response = new DatagramPacket(
                    responseByte,
                    responseByte.length,
                    inputPacket.getAddress(),
                    inputPacket.getPort());
            socket.send(response);
        } catch (IOException e) {
            formatMessage(
                    "Sending packet to: "
                            + inputPacket.getAddress().toString()
                            + "on " + inputPacket.getPort()
                            + " failed.");
            LOGGER.warning("Send packet to: "
                    + inputPacket.getAddress().toString()
                    + "on " + inputPacket.getPort()
                    + " failed: " + e.getMessage());
        }
    }

    /**
     * Deserialize the packet sent through the network stream.
     *
     * @return  A data packet instance initialized by reading the
     *          object stream from network.
     */
    private DataPacket deserializePacket() {
        DataPacket dataPacket = null;
        try {
            ObjectInputStream objectInputStream =
                    new ObjectInputStream(
                        new ByteArrayInputStream(
                                packet.getData()
                        )
                    );
            dataPacket = (DataPacket) objectInputStream.readObject();
        } catch (IOException e) {
            LOGGER.severe("Error while deserializing the packet from the network: "
                    + e.getMessage());
            return null;
        } catch (ClassNotFoundException e) {
            LOGGER.severe("Error while deserializing the packet as the class casting failed: "
                    + e.getMessage());
            return null;
        }

        LOGGER.info("Deserialization successful.");
        return dataPacket;
    }

    /**
     * Formats the message to be printed to the output stream.
     *
     * @param message The message to print to screen
     */
    private void formatMessage(String  message) {
        System.out.print("<" + LocalDateTime.now() + ">> ");
        System.out.println(message);
    }

    /**
     * Initializes the packet by flushing any existing
     * value from the buffer.
     */
    private void initializePacket() {
        buffer = new byte[packetSize];
        packet = new DatagramPacket(buffer, buffer.length);
    }

    /**
     * Reads the incoming data on the socket.
     */
    private void readData() {

        //Keep reading from the socket,
        //until the program exits forcefully.
        while (true) {
            initializePacket();
            LOGGER.info("Buffer reset and new datagram packet created.");

            try {
                socket.receive(packet);
                LOGGER.info("Packet successfully received from the socket.");

                processData();
            } catch (IOException e) {
                LOGGER.severe("Error while reading from the socket: " + e.getMessage());
            }
        }
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
        setup("logs/udp/server_udp.log");
        parseArguments(args);

        //Instantiate the datagram object.
        serverObject = new Server_UDP();

        //Read data on the socket created.
        serverObject.readData();
    }
}
