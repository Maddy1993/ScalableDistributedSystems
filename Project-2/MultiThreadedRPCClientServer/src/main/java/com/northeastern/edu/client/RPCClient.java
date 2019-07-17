package com.northeastern.edu.client;

import com.northeastern.edu.utils.RPCPacketServiceImpl;
import generated.thrift.impl.RPCPacketService;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.stream.Stream;

/**
 * Represents an RPC Client implemented as Apache Thrift client.
 *
 * @author mpothukuchi 12th June, 2019
 *
 * References:
 * @see <a href="https://thrift.apache.org/tutorial/java" />
 * @see <a href="https://www.baeldung.com/apache-thrift" />
 */
public class RPCClient {

    //Logger for the class.
    private static Logger LOGGER = Logger.getLogger(RPCClient.class.getName());

    //Port Number of server.
    private static Integer portNumber;

    //Server address parsed.
    private static String serverAddress;

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
                serverAddress = args[0];
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

    public static void main(String[] args) {
        setup("logs/RPCClient.log");
        parseArguments(args);

        LOGGER.info("Client pre-processing complete.");

        //Initializing the RPC Client.
        try {
            TTransport transport = new TSocket(serverAddress, portNumber);
            transport.open();

            TProtocol protocol = new TBinaryProtocol(transport);
            RPCPacketService.Client client = new RPCPacketService.Client(protocol);

            //Runs in a while loop until a quit input is received from
            //the user.
            perform(client);

            //Close once the method returns.
            transport.close();
            LOGGER.info("Connection closed with server");
            formatMessage("Connection ended with server");
        } catch (TTransportException e) {
            LOGGER.severe("Error opening a channel with the server on: " + serverAddress + "-> " + portNumber);
            LOGGER.severe("Exception while opening channel: " + e.getMessage());
            formatMessage("Error opening a channel with the server on: " + serverAddress + "-> " + portNumber);
            formatMessage("Exception while opening channel: " + e.getMessage());
            System.exit(-1);
        }
    }

    /**
     * Contains the logic behind client implementation.
     *
     * @param server The thrift processor which represents the client.
     */
    private static void perform(RPCPacketService.Client server) {

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

            //Switch statement to handle the different cases.
            String value;
            String key;
            int response;
            List<String> keys;
            switch (option) {
                case 1:
                    try {
                        keys = server.getKeys();

                        if (keys.size() == 0) {
                            System.out.println("Server does not have keys in its store.");
                            break;
                        } else {
                            System.out.println("List of Available keys with the server:");
                            Stream.of(keys).forEach(x->System.out.println("\t" + x));
                        }

                        //Enter the key name from the available list.
                        System.out.println("\nEnter the key name: ");
                        do{
                            key = input.nextLine();
                        } while (key.isEmpty());

                        //Get the value from the server.
                        value = server.getValue(key);
                        if (!value.equalsIgnoreCase(RPCPacketServiceImpl.OperationType.NONE.name())) {
                            System.out.println("The value associated with key: " + key + " is " + value);
                        } else {
                            System.out.println("Key does not exist.");
                        }
                        break;
                    } catch (TException e) {
                        formatMessage("Error while retrieving keys from the server: " + e.getMessage());
                        LOGGER.warning("Error while retrieving keys from the server: " + e.getMessage());
                        return;
                    }
                case 2:
                    //Read the data to store and then
                    //the respective file name.
                    System.out.println("\nEnter the data: ");
                    do{
                        value = input.nextLine();
                    } while (value.isEmpty());

                    System.out.print("\nEnter the key name for the data: ");
                    do{
                        key = input.nextLine();
                    } while (key.isEmpty());

                    //Add the key to keyStore at the server.
                    try {
                        server.setKeyValue(key, value);
                        System.out.println("Key saved at the server.");
                        break;
                    } catch (TException e) {
                        formatMessage("Error while saving keys from the server: " + e.getMessage());
                        LOGGER.warning("Error while saving keys to the server: " + e.getMessage());
                        return;
                    }
                case 3:
                    try {
                        keys = server.getKeys();
                        if (keys.size() == 0) {
                            System.out.println("Server does not have keys in its store.");
                            break;
                        } else {
                            System.out.println("List of Available keys with the server:");
                            Stream.of(keys).forEach(x->System.out.println("\t" + x));
                        }

                        //Enter the key name from the available list.
                        System.out.println("\nEnter the key name to delete: ");
                        do{
                            key = input.nextLine();
                        } while (key.isEmpty());

                        //Get the response for deletion from the server.
                        response = server.removeKey(key);

                        if (response == RPCPacketServiceImpl.OperationType.SUCCESS.getEnumValue()) {
                            System.out.println("Key: " + key + " successfully removed");
                        } else if (response == RPCPacketServiceImpl.OperationType.FAILURE.getEnumValue()){
                            System.out.println("Key does not exist.");
                        } else {
                            System.out.println("Invalid Response");
                            LOGGER.warning("Invalid response from delete operation");
                        }
                    } catch (TException e) {
                        formatMessage("Error while deleting keys from the server: " + e.getMessage());
                        LOGGER.warning("Error while deleting keys from the server: " + e.getMessage());
                        return;
                    }
                    break;
                case 4:
                    return;
                default:
                    formatMessage("Invalid Option Entered.");
                    LOGGER.warning("Invalid Option: " + option + "entered.");
            }
        } while (true);
    }
}
