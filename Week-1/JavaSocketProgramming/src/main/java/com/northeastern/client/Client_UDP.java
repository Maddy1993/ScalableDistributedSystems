package main.java.com.northeastern.client;

import main.java.com.northeastern.Utils.DataPacket;
import main.java.com.northeastern.Utils.Utils;

import java.io.*;
import java.net.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

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
            formatMessage("Message successfully send to server on: " + serverAddress.toString() + "-" + portNumber);
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
                            + serverAddress.toString()
                            + "on " + portNumber
                            + " failed.");
            LOGGER.warning("Receiving packet from: "
                    + serverAddress.toString()
                    + "on " + portNumber
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
        System.out.println("Please choose the operation to perform:");
        System.out.println("\t1. Get Data.\n\t2. Save Data.\n\t3. Delete data");
        System.out.print("Enter the option: ");

        Scanner input = new Scanner(System.in);
        int option = input.nextInt();

        //Execute the operation.
        DataPacket dataPacket = null;
        String fileName = null;
        String data = null;
        switch (option) {
            case 1:
                System.out.println("\nEnter the filename: ");
                fileName = input.nextLine();
                dataPacket = new DataPacket(DataPacket.PacketType.GET, fileName);
                clientObject.sendPacket(dataPacket);
                clientObject.receivePacket();
                System.out.print("The message for filename " + fileName + " is " + dataPacket.getData());
                break;
            case 2:
                System.out.println("\nEnter the data: ");
                data = input.nextLine();
                System.out.print("\nEnter the fileName for the data: ");
                fileName = input.nextLine();
                keys.add(fileName);
                List<String> values = new ArrayList<>();
                values.add(fileName);
                values.add(data);
                dataPacket = new DataPacket(DataPacket.PacketType.PUT, values);
                clientObject.sendPacket(dataPacket);
                break;
            case 3:
                System.out.print("\nEnter the filename to delete: ");
                fileName = input.nextLine();
                dataPacket = new DataPacket(DataPacket.PacketType.DELETE, fileName);
                clientObject.sendPacket(dataPacket);
                break;
            default:
                System.out.println("Invalid Option Entered.");
                LOGGER.warning("Invalid Option: " + option + "entered.");
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
