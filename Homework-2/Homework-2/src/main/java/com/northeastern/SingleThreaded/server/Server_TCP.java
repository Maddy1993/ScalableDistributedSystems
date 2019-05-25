
package main.java.com.northeastern.SingleThreaded.server;

import main.java.com.northeastern.Utils.Utils;

import java.io.*;
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

    //Utils object instance.
    private static Utils utils;

    /**
     * Constructor for the program to take the
     * port number and create the socket.
     */
    private Server_TCP() {
        super();
        try {
            serverSocket = new ServerSocket(portNumber);
            utils = Utils.getInstance();
            LOGGER.info("Server Successfully initialized.");
        } catch (IOException e) {
            e.printStackTrace();
        }
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
        //Read the data.
        String inputData = readData(socket);
        if(inputData == null) {
            utils.closeConnection(socket);
        }

        //Apply program specific conversion.
        inputData = utils.inverseAndReverse(inputData);

        //Send the transformed data back to the
        //client.
        sendData(socket, inputData);
    }

    /**
     * Accesses the output stream of the socket
     * and sends the data on the stream.
     * @param socket The socket to send the data on.
     * @param data The transformed data to send to the client.
     */
    private void sendData(Socket socket, String data) {
        try {
            //Open the stream for writing.
            OutputStream outputStream = socket.getOutputStream();
            DataOutputStream dataOutputStream = new DataOutputStream(outputStream);

            //Write the data into the stream.
            dataOutputStream.writeUTF(data);
        } catch (IOException e) {
            LOGGER.warning("Error streaming the data to the socket: " + e.getMessage());
        }
    }

    /**
     * Reads the data sent by the client.
     * Specific to this program, the case of
     * each character is inverted, and string is
     * reversed.
     * @param socket The socket on which the data is being
     *               received by the client.
     * @return Returns the transformed string.
     */
    private String readData(Socket socket) {
        try {
            // get the stream of data.
            InputStream inputStream = socket.getInputStream();
            DataInputStream dataInputStream = new DataInputStream(inputStream);

            //Read the data from the stream.
            String inputData = dataInputStream.readUTF();
            LOGGER.info("Data received from the client: " + inputData);

            //Returns the data.
            return inputData;
        } catch (IOException e) {
            LOGGER.warning("Error while reading data from the input stream.\n" + e.getMessage());
            return null;
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
            LOGGER.addHandler(new FileHandler("logs/tcp/single_server.log"));
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

        //Initialize the socket.
        serverObject = new Server_TCP();

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
        LOGGER.info("socket is no longer available.");
        LOGGER.info("Program execution complete. Server exiting.");
        System.exit(0);
    }
}