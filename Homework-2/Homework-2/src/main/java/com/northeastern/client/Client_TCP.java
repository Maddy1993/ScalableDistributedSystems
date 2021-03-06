package main.java.com.northeastern.client;

import java.io.*;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

import main.java.com.northeastern.Utils.Utils;

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

    //Utils object instance.
    private static Utils utils;

    /**
     * Constructor which sends the data to the server,
     * and reads the response from the server.
     */
    private Client_TCP() {
        //Creates the connection to the client.
        utils = Utils.getInstance();
        createSocket();
    }

    /**
     * @return The socket associated with the instance.
     */
    private Socket getSocket() {
        return this.clientSocket;
    }

    /**
     * Takes the input from the user.
     * The input represents the data to be
     * sent to the server.
     * @return  Returns the data read from the user.
     */
    private String generateData() {
        try {
            this.formatMessage("\nEnter the data (Q/q to Exit): ");
            Scanner input = new Scanner(System.in);
            return input.nextLine();
        } catch (NoSuchElementException e) {
            LOGGER.info("Error while reading input: " + e.getMessage());
            return null;
        }
    }

    /**
     * Creates the socket based on the server address and port.
     * Exists when the server address could not be resolved.
     */
    private void createSocket() {
        try {
            clientSocket = new Socket(serverAddress, portNumber);
            LOGGER.info("Client socket created successfully for the server address: " + serverAddress
                    + " and portNumber: " + portNumber);
        } catch (IOException e) {
            LOGGER.severe("Could not create socket: " + e.getMessage());
            System.exit(1);
        }
    }

    /**
     * Formats the message passed as parameter
     *
     * @param message Message to print to the screen.
     */
    private void formatMessage(String message) {
        System.out.print("<" + LocalDateTime.now() + "> ");
        System.out.println(message);
    }
    /**
     * Used for any setup required before the program execution
     * starts.
     */
    private static void setup() {
        //Initialize the logger.
        try {
            LOGGER.setUseParentHandlers(false);
            LOGGER.addHandler(new FileHandler("logs/tco/tcp_client.log"));
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

        while (true) {
            //Reads the line to be transformed.
            String readData = clientObject.generateData();
            if (readData == null) {
                utils.closeConnection(clientObject.getSocket());
            }

            if (readData.length() <=0 || !readData.equalsIgnoreCase("Q")) {
                LOGGER.info("Data which is to be sent to the server successfully read.");

                //Write the data to the server connection stream.
                utils.writeData(readData, clientObject.getSocket());
                LOGGER.info("Data: " + readData + ", is sent to the server.");

                //Read the data from the server.
                String serverData = utils.readData(clientObject.getSocket());

                //Compare the result.
                if(serverData.equalsIgnoreCase(utils.inverseAndReverse(readData))) {
                    clientObject.formatMessage("Server response: " +  serverData);
                    clientObject.formatMessage("Data sent by the server matches.");
                    LOGGER.info("Data sent by the server matches.");
                } else {
                    clientObject.formatMessage("Data returned by the server does not match.");
                    LOGGER.info("Data returned by the server does not match");
                }

            } else {
                clientObject.formatMessage("Data entered by the user is either empty or exit.");
                LOGGER.warning("Data entered by the user is either empty or exit.");
                break;
            }
        }

        //Close socket.
        utils.closeConnection(clientObject.getSocket());
    }
}