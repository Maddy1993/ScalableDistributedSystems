package main.java.com.northeastern.Utils;

import java.io.*;
import java.net.Socket;
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
     * @return The data which has been read from the server socket.
     */
    public String readData(Socket socket) {
        String readData = null;
        try {
            //Open the streams.
            InputStream inputStream = socket.getInputStream();
            DataInputStream dataInputStream = new DataInputStream(inputStream);

            //Read the data from the stream.
            readData = dataInputStream.readUTF();
        } catch (IOException e) {
            LOGGER.warning("Error reading data from the input stream: " + e.getMessage());
        }

        return readData;
    }

    /**
     * Sends the data to the server on the socket
     * created.
     * @param readData Takes the data to send as input.
     * @param socket Represents the socket on which the data needs
     *               to be written to its output stream.
     */
    public void writeData(String readData, Socket socket) {
        try {
            //Opens the output stream.
            OutputStream outputStream = socket.getOutputStream();
            DataOutputStream dataOutputStream = new DataOutputStream(outputStream);

            //Write the data to the stream.
            dataOutputStream.writeUTF(readData);
        } catch (IOException e) {
            LOGGER.warning("Error writing data to the output stream of socket: " + socket.getLocalAddress()
                    + " with error: " + e.getMessage());
        }
    }
}
