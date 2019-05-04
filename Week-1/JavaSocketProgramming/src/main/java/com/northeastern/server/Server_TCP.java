package com.northeastern.server;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
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
    private static Logger LOGGER = Logger.getLogger(Server_TCP.class.getName());

    //Instance of the class
    private static Server_TCP serverObject;

    //Server socket instance.
    private ServerSocket serverSocket;

    //Socket for incoming requests.
    private Socket socket;

    //Port Number for the server
    private static Integer portNumber;

    /**
     * Constructor for the program to take the
     * port number and create the socket.
     * @param portNumber The port number to create
     *                   the socket.
     */
    private Server_TCP(Integer portNumber) {
        try {
            serverSocket = new ServerSocket(portNumber);
            LOGGER.info("Server Successfully initialized.");

            socket = serverSocket.accept();
            LOGGER.info("Client connection accepted.");

            readData(socket);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Reads the data sent by the client.
     * Specific to this program, the case of
     * each character is inverted, and string is
     * reversed.
     * @param socket The socket on which the data is being
     *               received by the client.
     */
    private void readData(Socket socket) {
        // get the stream of data.
        try {
            InputStream inputStream = socket.getInputStream();
            DataInputStream dataInputStream = new DataInputStream(inputStream);

            String inputData = dataInputStream.readUTF();

            // Apply program specific conversion.
            inputData = inverseAndReverse(inputData);


        } catch (IOException e) {
            LOGGER.severe("Error while reading data from the input stream.\n" + e.getMessage());
            return;
        }
    }

    /**
     * Takes a string of characters as input.
     * Inverses the case of each character in the
     * string.
     * Reverse the string
     * @param inputData The string to apply the transformation.
     * @return Returns the transformed string.
     */
    private String inverseAndReverse(String inputData) {
        if (inputData != null) {
            StringBuilder builder = new StringBuilder();

            // Inverse the case of each character and also reverse the string.
            for(char character: inputData.toCharArray()) {
                character = Character.isLowerCase(character) ?
                        Character.toUpperCase(character) :
                        Character.toLowerCase(character);
                builder.insert(0, character);
            }

            inputData = builder.toString();
        } else {
            LOGGER.info("Input data given as parameter is null and hence, transformation is" +
                    "not applied.");
        }

        return inputData;
    }

    /**
     * Used for any setup required before the program execution
     * starts.
     */
    private static void setup() {
        //Initialize the logger.
        try {
            LOGGER.setUseParentHandlers(false);
            LOGGER.addHandler(new FileHandler("./server.log"));
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
    private static void parseArguments(String[] args) {
        if (args.length == 1) {
            try {
                portNumber = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
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
        setup();
        parseArguments(args);
        serverObject = new Server_TCP(portNumber);
    }
}
