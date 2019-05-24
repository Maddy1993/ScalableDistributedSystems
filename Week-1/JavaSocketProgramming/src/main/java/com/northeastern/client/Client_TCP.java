package main.java.com.northeastern.client;

import main.java.com.northeastern.Utils.DataPacket;
import main.java.com.northeastern.Utils.Utils;

import java.io.*;
import java.net.Socket;
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
 * The class represents a TCP client.
 * As a client, the server address and port are provided
 * as arguments to the program, and the client establishes
 * a connection with the server on that address.
 * Once the connection has been established, the client sends
 * a string, and expects the server to inverse the case of
 * each string and reverse the transformed string.
 *
 * @author mpothukuchi May 4, 2019
 */
public class Client_TCP {

    //Logger for the class.
    private static Logger LOGGER = Logger.getLogger(Client_TCP.class.getName());

    //Port Number of server.
    private static Integer portNumber;

    //Server address.
    private static String serverAddress;

    //Instance of the Client_TCP.
    private static Client_TCP clientObject;

    //Socket for the client to send the data
    private Socket clientSocket;

    //Utils instance for the client
    private static Utils utils;

    //List of available keys.
    private List<String> keys;

    //Integer to represent the client.
    final int clientSourceCode = 2;

    /**
     * Constructor which sends the data to the server,
     * and reads the response from the server.
     */
    private Client_TCP() {
        utils = new Utils("logs/tcp/client_utils.log");
        keys = new ArrayList<>();
        //Creates the connection to the client.
        createSocket();
        utils.formatMessage("TCP Client socket created successfully.");
    }

    /**
     * @return The socket associated with the instance.
     */
    private Socket getSocket() {
        return this.clientSocket;
    }

    /**
     * Prints the options available to the user by
     * the client and performs the operation based on that.
     */
    private void receiveOptionAndExecute() {
        do {
            utils.formatMessage("\nPlease choose the operation to perform:");
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
            DataPacket dataPacket;
            String fileName;
            String data;
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
                    utils.writeData(dataPacket, clientObject.getSocket());

                    //Deserialize and receive the file.
                    DataPacket receivedPacket;
                    receivedPacket = utils.readData(clientObject.getSocket(), clientSourceCode);

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

                    //Send the file
                    utils.writeData(dataPacket, clientObject.getSocket());

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
                        utils.formatMessage("Cannot delete values are none are saved yet.");
                    } else {
                        System.out.println("Available file names with server: ");
                        Stream.of(keys).forEach(x->System.out.println("\t" + x ));
                    }

                    System.out.print("\nEnter the filename to delete: ");
                    do {
                        fileName = input.nextLine();
                    } while (fileName.isEmpty());

                    dataPacket = new DataPacket(DataPacket.PacketType.DELETE, fileName);

                    //Send the file
                    utils.writeData(dataPacket, clientObject.getSocket());

                    responseCode = validateSuccessResponse("DELETE");
                    if (responseCode == 0) {
                        break;
                    }

                    //Remove the key from the local list as well.
                    keys.remove(fileName);

                    break;
                case 4:
                    dataPacket = new DataPacket(DataPacket.PacketType.EXIT, null);

                    //Send the file
                    utils.writeData(dataPacket, clientObject.getSocket());
                    utils.closeConnection(clientObject.getSocket());
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
        utils.writeData(keyRequestPacket, clientObject.getSocket());

        //Receive the request and assign the keys to the client
        //object.
        DataPacket keyResponsePacket;
        keyResponsePacket = utils.readData(clientObject.getSocket(), clientSourceCode);
        if (keyResponsePacket == null) {
            return 0;
        }

        if (keyResponsePacket.getPacketType() == DataPacket.PacketType.DATA) {
            Object data = keyResponsePacket.getData();
            if (data instanceof ArrayList) {
                clientObject.keys = (ArrayList) data;
            } else {
                utils.formatMessage("Incorrect data packet format received while getting keys");
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
        DataPacket responsePacket;
        responsePacket = utils.readData(clientObject.getSocket(), clientSourceCode);
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
            }
        } else {
            System.out.println("Incorrect data packet type sent by server for " + operationName + " operation");
            LOGGER.info("Incorrect data packet type sent by server for " + operationName + " operation");
            return 0;
        }

        return 1;
    }

    /**
     * Creates the socket based on the server address and port.
     * Exists when the server address could not be resolved.
     */
    private void createSocket() {
        try {
            clientSocket = new Socket(serverAddress, portNumber);
            LOGGER.info("TCP Client socket created successfully for the server address: " + serverAddress
                    + " and portNumber: " + portNumber);
        } catch (IOException e) {
            LOGGER.severe("Could not create socket: " + e.getMessage());
            System.exit(1);
        }
    }

    /**
     * Used for any setup required before the program execution
     * starts.
     */
    private static void setup() {
        //Initialize the logger.
        try {
            LOGGER.setUseParentHandlers(false);
            LOGGER.addHandler(new FileHandler("logs/tcp/client_tcp.log"));
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
                serverAddress = args[0];
                portNumber = Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
                LOGGER.severe("The arguments format doesn't match: " + e.getMessage());
                System.exit(1);
            }
        } else {
            LOGGER.severe(
                    "The client expects the arguments in the following format."
                            + "\nClient_TCP.java <server_address> <server_port>");
            System.exit(1);
        }
    }

    // Driver program for the client.
    public static void main(String[] args) {
        setup();
        parseArguments(args);
        clientObject = new Client_TCP();

        clientObject.receiveOptionAndExecute();

        //Close socket.
        utils.closeConnection(clientObject.getSocket());
    }
}
