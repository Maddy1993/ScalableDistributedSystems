package main.java.com.northeastern.Utils;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.time.LocalDateTime;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

public class Utils {
    //Logger for the class instance.
    private static Logger LOGGER;

    /**
     *
     * @param logName
     */
    public Utils(String logName) {
        //Initialize the logger.
        try {
            //validate the log name to end with .log extension.
            //Else terminate the program.
            if (!logName.endsWith(".log")) {
                System.out.print("Incorrect log name format. Should end with .log extension");
                System.exit(-1);
            }

            LOGGER = Logger.getLogger(Utils.class.getName());
            LOGGER.setUseParentHandlers(false);
            LOGGER.addHandler(new FileHandler(logName));
        } catch (IOException e) {
            System.out.println("Error initializing the logger: " + e.getMessage());
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
    public String inverseAndReverse(String inputData) {
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
     * Closes the input and output streams for the socket.
     *
     * @param socket Represents the socket whose input and output
     *               streams need to be closed.
     */
    public void closeStreams(Socket socket) {
        try {
            //Close the input stream.
            socket.getInputStream().close();

            //Close the output stream.
            socket.getOutputStream().close();
        } catch (IOException e) {
            LOGGER.warning("Error while closing input and output streams for socket: "
                    + socket.getLocalSocketAddress() + ". The error message: " + e.getMessage());
        }
    }

    /**
     * Closes the connection established with the server.
     *
     * @param socket Represents the socket whose connection needs
     *               to be closed.
     */
    public void closeConnection(Socket socket) {
        try {
            socket.close();
        } catch (IOException e) {
            LOGGER.warning("Error closing the client socket: " + e.getMessage());
        }
    }

    /**
     * Reads the data available in the socket.
     *
     * @param socket Represents the socket whose input stream needs
     *               to be read.
     * @param source Integer represents source of incoming socket.
     *               1- server
     *               2- client
     * @return The data which has been read from the server socket.
     */
    public DataPacket readData(Socket socket, int source){
        DataPacket readData;

        //Set timeout only for client.
        if (source == 2) {
            try {
                socket.setSoTimeout(10000);
            } catch (SocketException e) {
                formatMessage("Error initializing the socket timeout.");
                LOGGER.warning("Error initializing socket timeout.");
                return null;
            }
        }

        //Open the streams.
        InputStream inputStream = null;
        try {
            inputStream = socket.getInputStream();
        } catch (IOException e) {
            formatMessage("Error acquiring input stream for the socket.");
            LOGGER.info("Error acquiring input stream for network socket: " + e.getMessage());
            return null;
        }

        DataInputStream dataInputStream = new DataInputStream(inputStream);

        ObjectInputStream objectInputStream;
        try {
            objectInputStream = new ObjectInputStream(dataInputStream);
            readData = (DataPacket) objectInputStream.readObject();
        } catch (IOException e) {
            formatMessage("Error reading data on network socket: " + e.getMessage());
            LOGGER.info("Error reading data on network socket: " + e.getMessage());
            return null;
        } catch (ClassNotFoundException e) {
            formatMessage("Error casting object data from network into data packet class");
            LOGGER.info("Error casting object data from network into data packet class: " + e.getMessage());
            return null;
        }

        return readData;
    }

    /**
     * Formats the message to be printed to the output stream.
     *
     * @param message The message to print to screen
     */
    public void formatMessage(String  message) {
        System.out.print("<" + LocalDateTime.now() + ">> ");
        System.out.println(message);
    }

    /**
     * Sends the data to the server on the socket
     * created.
     * @param readData Takes the data to send as input.
     * @param socket Represents the socket on which the data needs
     *               to be written to its output stream.
     */
    public void writeData(DataPacket readData, Socket socket) {
        try{
            //Opens the output stream.
            OutputStream outputStream = socket.getOutputStream();
            DataOutputStream dataOutputStream = new DataOutputStream(outputStream);

            ObjectOutputStream objectOutputStream
                    = new ObjectOutputStream(dataOutputStream);

            //Write the data to the stream.
            objectOutputStream.writeObject(readData);
        } catch (IOException e) {
            formatMessage("Error reading data to network socket.");
            LOGGER.info("Error reading data to network socket: " + e.getMessage());
        }

    }
}
