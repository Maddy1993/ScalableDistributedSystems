package main.java.com.northeastern.client;

import main.java.com.northeastern.Utils.DataPacket;
import main.java.com.northeastern.Utils.Utils;

import java.io.*;
import java.net.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.stream.Stream;

/**
 * The class represents a UDP client.
 * As a client, the server address and port are provided
 * as arguments to the program, and the client establishes
 * a connection with the server on that address.
 * Once the connection has been established, the client sends
 * a string, and expects the server to inverse the case of
 * each string and reverse the transformed string.
 *
 * @author mpothukuchi May 18, 2019
 */
public class Client_UDP {
    //Logger for the class.
    private static Logger LOGGER = Logger.getLogger(Client_TCP.class.getName());

    //Port Number of server.
    private static Integer portNumber;

    //Server address.
    private InetAddress serverAddress;

    //Server address parsed.
    private static String parsedServerAddress;

    //Instance of the Client_TCP.
    private static Client_UDP clientObject;

    //Socket for the client to send the data
    private DatagramSocket clientSocket;

    //Packet to fill the data and send on
    //socket.
    private DatagramPacket packet;

    //Utils instance for the client
    private static Utils utils;

    //List of available keys.
    private List<String> keys;

    //Packet size for UDP packet.
    final int packetSize = 5048;

    //Buffer for datagram connection.
    byte[] buffer;

    /**
     * Constructor which sends the data to the server,
     * and reads the response from the server.
     */
    private Client_UDP() {
        createSocket();
        keys = new ArrayList<>();
        formatMessage("Client datagram socket created successfully.");
        LOGGER.info("Client datagram socket created successfully.");
    }

    /**
     * Creates the socket and gets the iNetAddress
     * for the server.
     */
    private void createSocket() {
        try {
            clientSocket = new DatagramSocket();
            LOGGER.info("Client Datagram socket created successfully");
        } catch (SocketException e) {
            formatMessage("Unable to create client datagram socket.");
            LOGGER.severe("Unable to create client datagram socket: " + e.getMessage());
        }

        //Parse the address and get the address if name of the server
        //is entered.
        try {
            serverAddress = InetAddress.getByName(parsedServerAddress);
        } catch (UnknownHostException e) {
            formatMessage("Resolving server address by name failed.");
            LOGGER.severe("Resolving server address by name failed: " + e.getMessage());
            System.exit(-1);
        }
    }

    /**
     * Sends the packet to the server address parsed
     * from the arguments
     * @param responsePacket the message to send to client.
     */
    private void sendPacket(DataPacket responsePacket) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream
                    = new ObjectOutputStream(outputStream);
            objectOutputStream.writeObject(responsePacket);

            buffer = outputStream.toByteArray();
            packet = new DatagramPacket(buffer, buffer.length, serverAddress, portNumber);
            clientSocket.send(packet);
            formatMessage("Message successfully sent to server on: " + serverAddress.toString().replace("/", "") + ":" + portNumber);
        } catch (IOException e) {
            formatMessage(
                    "Send packet to: "
                            + serverAddress.toString()
                            + "on " + portNumber
                            + " failed.");
            LOGGER.warning("Send packet to: "
                    + serverAddress.toString()
                    + "on " + portNumber
                    + " failed: " + e.getMessage());
        }
    }

    /**
     * Receives the packet on socket and deserialize
     * the packet into DataPacket object.
     * @return  The instance of data packet object received
     *          on the network. Else null if the deserialization
     *          failed.
     */
    private DataPacket receivePacket() {
        DataPacket dataPacket = null;
        try {
            buffer = new byte[packetSize];
            packet = new DatagramPacket(buffer, buffer.length);
            clientSocket.setSoTimeout(10000);
            clientSocket.receive(packet);

            ObjectInputStream objectInputStream =
                    new ObjectInputStream(
                            new ByteArrayInputStream(
                                    packet.getData()
                            )
                    );
            dataPacket = (DataPacket) objectInputStream.readObject();
        } catch (IOException e) {
            formatMessage(
                    "Receiving packet from: "
                            + serverAddress.toString().replace("/", "")
                            + " on " + portNumber
                            + " failed.");
            LOGGER.warning("Receiving packet from: "
                    + serverAddress.toString().replace("/", "")
                    + " on " + portNumber
                    + " failed: " + e.getMessage());
            return null;
        } catch (ClassNotFoundException e) {
            LOGGER.severe("Error while deserializing the packet as the class casting failed: "
                    + e.getMessage());
            return null;
        }

        formatMessage("Packet received from the server.");
        LOGGER.info("Deserialization successful.");
        return dataPacket;
    }

    /**
     * Prints the options available to the user by
     * the client and performs the operation based on that.
     */
    private void receiveOptionAndExecute() {
        do {
            formatMessage("\nPlease choose the operation to perform:");
            System.out.println("\t1. Get Data.\n\t2. Save Data.\n\t3. Delete data.\n\t4. Quit");
            System.out.println("\nEnter the option number: ");

            Scanner input = new Scanner(System.in);
            int option = 0;
            try {
                option = input.nextInt();
            } catch (NoSuchElementException e) {
                LOGGER.warning("Error reading integer: " + e.getMessage());
            }

            //Execute the operation.
            DataPacket dataPacket = null;
            String fileName = null;
            String data = null;
            int responseCode;
            switch (option) {
                case 1:
                    //Print the list of available keys with the server.
                    responseCode = getKeyList();
                    if (responseCode == 0) {
                        break;
                    }

                    System.out.println("List of Available keys with the server:");
                    Stream.of(keys).forEach(x->System.out.println("\t" + x));

                    if (keys.size() == 0) {
                        break;
                    }

                    //Enter the file name from the available list.
                    System.out.println("\nEnter the filename: ");
                    do{
                        fileName = input.nextLine();
                    } while (fileName.isEmpty());

                    //Pack the data based on the file name as the object data.
                    dataPacket = new DataPacket(DataPacket.PacketType.GET, fileName);

                    //Send the file
                    clientObject.sendPacket(dataPacket);

                    //Deserialize and receive the file.
                    DataPacket receivedPacket = clientObject.receivePacket();
                    if (receivedPacket != null) {
                        System.out.println("The message for filename " + fileName + " is " + receivedPacket.getData());
                    }
                    break;
                case 2:
                    //Read the data to store and then
                    //the respective file name.
                    System.out.println("\nEnter the data: ");
                    do{
                        data = input.nextLine();
                    } while (data.isEmpty());

                    System.out.print("\nEnter the fileName for the data: ");
                    do{
                        fileName = input.nextLine();
                    } while (fileName.isEmpty());

                    //Create a list of key, value to be sent to the
                    //server.
                    List<String> values = new ArrayList<>();
                    values.add(fileName);
                    values.add(data);

                    //Pack the values into a datagram packet before being
                    //serialized.
                    dataPacket = new DataPacket(DataPacket.PacketType.PUT, values);
                    clientObject.sendPacket(dataPacket);

                    responseCode = validateSuccessResponse("PUT");
                    if (responseCode == 0) {
                        break;
                    }

                    //Add the file name to list of keys available.
                    keys.add(fileName);

                    break;
                case 3:
                    //Enter the fileName to delete from the list of
                    //available file names in the server.
                    responseCode = getKeyList();
                    if (responseCode == 0) {
                        break;
                    }

                    if (keys.size() == 0) {
                        formatMessage("Cannot delete values are none are saved yet.");
                    } else {
                        System.out.println("Available file names with server: ");
                        Stream.of(keys).forEach(x->System.out.println("\t" + x ));
                    }

                    System.out.print("\nEnter the filename to delete: ");
                    do {
                        fileName = input.nextLine();
                    } while (fileName.isEmpty());

                    dataPacket = new DataPacket(DataPacket.PacketType.DELETE, fileName);
                    clientObject.sendPacket(dataPacket);

                    responseCode = validateSuccessResponse("DELETE");
                    if (responseCode == 0) {
                        break;
                    }

                    //Remove the key from the local list as well.
                    keys.remove(fileName);

                    break;
                case 4:
                    System.exit(0);
                default:
                    System.out.println("Invalid Option Entered.");
                    LOGGER.warning("Invalid Option: " + option + "entered.");
            }
        } while (true);
    }

    /**
     * Gets the list of keys available for the client
     * at the server.
     */
    private int getKeyList() {
        DataPacket keyRequestPacket = new DataPacket(DataPacket.PacketType.KEYS, new ArrayList<>());

        //Send request to the server to return keys
        clientObject.sendPacket(keyRequestPacket);

        //Receive the request and assign the keys to the client
        //object.
        DataPacket keyResponsePacket = clientObject.receivePacket();
        if (keyResponsePacket == null) {
            return 0;
        }
        if (keyResponsePacket.getPacketType() == DataPacket.PacketType.DATA) {
            Object data = keyResponsePacket.getData();
            if (data instanceof ArrayList) {
                clientObject.keys = (ArrayList) data;
            } else {
                formatMessage(
                        "Incorrect data packet format received while getting keys");
                LOGGER.warning("Incorrect data packet format received while getting keys");
            }
        }

        return 1;
    }

    /**
     * Validates the success response from the server.
     *
     * @return Response code. 0 for failure. 1 for success.
     */
    private int validateSuccessResponse(String operationName) {
        DataPacket responsePacket = clientObject.receivePacket();
        if (responsePacket == null) {
            return 0;
        }

        //Compare the type of the packet.
        if (responsePacket.getPacketType() != DataPacket.PacketType.SUCCESS) {
            System.out.println("Response from server for " + operationName + " failed");
            LOGGER.info("Response from server for: " + operationName + " failed");
            return 0;
        }

        //Compare the time difference between request and response.
        Object data = responsePacket.getData();
        if (data instanceof LocalDateTime) {
            LocalDateTime serverTime = (LocalDateTime) responsePacket.getData();
            Duration difference = Duration.between(serverTime, LocalDateTime.now());

            if (difference.getSeconds() > 30) {
                System.out.println("Time slide between response is longer. Response from server not accepted.");
                LOGGER.warning("Time slide between response is longer. Response from server not accepted.");
                return 0;
            } else {
                System.out.println(operationName + " operation successful");
                LOGGER.info(operationName + " operation successful");
                return 1;
            }
        } else {
            System.out.println("Incorrect data packet type sent by server for " + operationName + " operation");
            LOGGER.info("Incorrect data packet type sent by server for " + operationName + " operation");
            return 0;
        }
    }

    /**
     * Formats the message to be printed to the output stream.
     *
     * @param message The message to print to screen
     */
    private static void formatMessage(String  message) {
        System.out.print("<" + LocalDateTime.now() + ">> ");
        System.out.println(message);
    }

    /**
     * Used for any setup required before the program execution
     * starts.
     */
    private static void setup(String fileName) {
        //Initialize the logger.
        try {
            LOGGER.setUseParentHandlers(false);
            LOGGER.addHandler(new FileHandler(fileName));
        } catch (IOException e) {
            System.out.println("Error initializing the logger: " + e.getMessage());
        }
    }

    /**
     * Used to parse the server address and port number
     * arguments passed to the client.
     * @param args The command line arguments passed to the program.
     */
    private static void parseArguments(String[] args) {
        if (args.length == 2) {
            try {
                parsedServerAddress = args[0];
                portNumber = Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
                LOGGER.severe("The arguments format doesn't match: " + e.getMessage());
                formatMessage("The port number argument type don't match.");
                System.exit(1);
            }
        } else {
            LOGGER.severe(
                    "The client expects the arguments in the following format."
                            + "\nClient_TCP.java <server_address> <server_port>");
            formatMessage("The client expects the arguments in the following format."
                    + "\nClient_TCP.java <server_address> <server_port>");
            System.exit(1);
        }
    }

    // Driver program for the client.
    public static void main(String[] args) {
        setup("logs/udp/client_udp.log");
        parseArguments(args);

        //create a client UDP socket.
        clientObject = new Client_UDP();
        clientObject.receiveOptionAndExecute();
    }
}
